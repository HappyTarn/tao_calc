package taoCalc.event;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;

import google.GoogleSheets;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import taoCalc.CalcManager;
import taoCalc.ChannelManager;
import taoCalc.Const;
import taoCalc.RankManager;
import taoCalc.RankMessageManager;
import taoCalc.db.Sqlite;
import taoCalc.dto.CalcInfo;
import taoCalc.dto.Member;
import taoCalc.dto.PetInfo;
import taoCalc.dto.Summary;
import taoCalc.util.Utility;

public class MessageEvent extends ListenerAdapter {

	@Override
	public void onSelectionMenu(SelectionMenuEvent event) {

		String componentId = event.getComponentId();
		if (componentId.equals("menu:class")) {
			event.editMessage(event.getSelectedOptions().get(0).getLabel()).queue();
			event.editSelectionMenu(null).queue();

			String selectedValue = event.getSelectedOptions().get(0).getValue();
			if (selectedValue.equals("se")) {
				String guildId = event.getGuild().getId();
				String memberId = event.getMember().getId();
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle("保有経験値");

				Member member = Sqlite.selectMemberById(guildId, memberId);
				if (member != null) {
					eb.appendDescription(String.format("> %s ： <@%s>\n", member.getFormatExp(), member.getId()));
					eb.appendDescription(member.get割合());
					if (member.getレア率() > 2) {
						eb.setFooter("こいつやってますよ！！！（多分）");
					} else if (member.getレア率() > 1) {
						eb.setFooter("MMO君使ってないよね？");
					}
				} else {
					eb.addField(event.getMember().getUser().getName(), "0", false);
				}
				event.getMessage().replyEmbeds(eb.build()).queue();
			} else if (selectedValue.equals("sp")) {
				String guildId = event.getGuild().getId();
				String memberId = event.getMember().getId();
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle("ペット集計結果");

				PetInfo petInfo = Sqlite.selectPetInfo(guildId, memberId);
				if (petInfo != null) {
					eb.appendDescription(petInfo.get割合());
					event.getMessage().replyEmbeds(eb.build()).queue();
				} else {
					event.getMessage().reply("まだペット情報がないよ").queue();
					;
				}
			}

		}
	}

	public void onButtonClick(ButtonClickEvent event) {

		event.getMessage().getReferencedMessage();
		//通知ボタン
		if (event.getComponentId().equals("tcmt")) {
			for (Button b : event.getMessage().getButtons()) {
				if (b.isDisabled()) {
					event.editMessage(event.getMessage()).queue();
					return;
				}
			}
			MessageChannel mtChannel = null;

			Guild guild = event.getGuild();
			if (guild.getId().equals("602561685450129408") || guild.getId().equals("853332936530460713")) {
				//もえにょだった場合
				guild = event.getJDA().getGuildById("823574484660518932");

			}

			for (GuildChannel c : guild.getChannels()) {
				if (c.getName().contains("レアキャラ報告")) {
					mtChannel = (MessageChannel) c;
				}
			}

			String roleId = "";
			if (guild.getId().equals("602561685450129408") || guild.getId().equals("853332936530460713")) {
				//もえにょだった場合
				roleId = Sqlite.getRole(guild.getId(), Const.他鯖超激レア報告OK);

			} else {
				roleId = Sqlite.getRole(guild.getId(), Const.超激レア報告OK);
			}
			Role role = guild.getRoleById(roleId);

			if (mtChannel != null && role != null) {

				mtChannel.sendMessage(role.getAsMention() + "：<#" + event.getChannel().getId() + "> で超激レアが出たよ！\n"
						+ "> 通知した人：<@" + event.getMember().getId() + ">").queue();

				event.editButton(event.getButton().asDisabled()).queue();
			}
		}

		if (event.getComponentId().equals("tcmt_no")) {
			for (Button b : event.getMessage().getButtons()) {
				if (b.isDisabled()) {
					event.editMessage(event.getMessage()).queue();
					return;
				}
			}
			event.editButton(event.getButton().asDisabled()).queue();
		}

		//通知ボタン
		if (event.getComponentId().startsWith("tcmto")) {
			MessageChannel mtChannel = null;
			for (GuildChannel c : event.getGuild().getChannels()) {
				if (c.getName().contains("レアキャラ報告")) {
					mtChannel = (MessageChannel) c;
				}
			}

			String roleId = Sqlite.getRole(event.getGuild().getId(), Const.他鯖超激レア報告OK);
			Role role = event.getGuild().getRoleById(roleId);

			if (mtChannel != null && role != null) {

				String servername = event.getComponentId().split(" ")[1];
				mtChannel.sendMessage(role.getAsMention() + "：" + servername + "\n"
						+ "> 通知した人：<@" + event.getMember().getId() + ">").queue();

				event.getMessage().delete().queue();
				event.getChannel().sendMessage("通知完了：" + servername).queue();
			}
		}

		//発言不可解除ボタン
		if (event.getComponentId().equals("removeRole")) {
			event.editMessage(event.getMessage()).queue();
			String roleId = Sqlite.getRole(event.getGuild().getId(), Const.発言不可);
			Role role = event.getGuild().getRoleById(roleId);
			if (role != null) {
				event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
			} else {
				event.getChannel().sendMessage("発言不可解除失敗！管理者に外してもらって！");
			}
		}

		//ランクのアンドロイド表示
		if (event.getComponentId().startsWith("rviewa")) {
			RankMessageManager rankMessageManager = RankMessageManager.getINSTANCE();
			MessageEmbed embed = rankMessageManager.getRankMessage(event.getComponentId().split(" ")[1]).getEmbeds()
					.get(0);

			EmbedBuilder eb = new EmbedBuilder(embed);

			eb.setDescription(embed.getDescription().replaceAll("\\(<\\('', ''\\)>\\)", ""));

			event.getMessage().delete().queue();
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
		}

		//キャンセル
		if (event.getComponentId().equals("cancel")) {
			event.getMessage().delete().queue();
		}
		//キャンセル
		if (event.getComponentId().equals("end")) {
			event.editMessage(event.getMessage()).setActionRows().queue();
		}

		rankButtonEvent(event);
	}

	private void rankButtonEvent(ButtonClickEvent event) {
		Date date = new Date(); // 今日の日付
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = dateFormat.format(date);
		String strFirstDate = dateFormat.format(Utility.getFirstDate(date));
		String tdate = event.getMessage().getEmbeds().get(0).getAuthor().getName();
		if (tdate.startsWith("指定日")) {
			tdate = tdate.replace("指定日：", "").replace("のランキング", "");
			tdate = tdate.substring(0, 4) + "-"
					+ tdate.substring(4, 6) + "-"
					+ tdate.substring(6, 8);
		}
		//この鯖
		if (event.getComponentId().equals("rank_this_d") || event.getComponentId().equals("rank_this_m")
				|| event.getComponentId().equals("rank_this_t") || event.getComponentId().equals("rank_this_a")) {
			//			for (Button b : event.getMessage().getButtons()) {
			//				if (b.isDisabled()) {
			//					event.editMessage(event.getMessage()).queue();
			//					return;
			//				}
			//			}
			//			
			//			event.editButton(event.getButton().asDisabled()).queue();
			event.getMessage().delete().queue();
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle(event.getGuild().getName() + "ランキング");
			eb.setAuthor(event.getMessage().getEmbeds().get(0).getAuthor().getName());

			event.getChannel().sendMessageEmbeds(eb.build()).setActionRows(
					ActionRow.of(Button.of(ButtonStyle.PRIMARY, event.getComponentId() + "_combat", "討伐"),
							Button.of(ButtonStyle.PRIMARY, event.getComponentId() + "_ground", "地上げ")),
					ActionRow.of(Button.of(ButtonStyle.PRIMARY, event.getComponentId() + "_exp", "経験値獲得"),
							Button.of(ButtonStyle.PRIMARY, event.getComponentId() + "_sozai", "素材獲得"),
							Button.of(ButtonStyle.PRIMARY, event.getComponentId() + "_weapon", "武器獲得"),
							Button.of(ButtonStyle.PRIMARY, event.getComponentId() + "_bukikon", "武器魂獲得")),
					ActionRow.of(
							Button.of(ButtonStyle.DANGER, event.getComponentId() + "_ban", "BAN回数"),
							Button.of(ButtonStyle.DANGER, "cancel", "キャンセル")))
					.queue();
		}
		if (event.getComponentId().startsWith("rank_this_")
				&& (event.getComponentId().endsWith("_combat") || event.getComponentId().endsWith("_ground")
						|| event.getComponentId().endsWith("_exp") || event.getComponentId().endsWith("_sozai")
						|| event.getComponentId().endsWith("_weapon") || event.getComponentId().endsWith("_bukikon")
						|| event.getComponentId().endsWith("_ban"))) {
			for (Button b : event.getMessage().getButtons()) {
				if (b.isDisabled()) {
					event.editMessage(event.getMessage()).queue();
					return;
				}
			}
			event.editButton(event.getButton().asDisabled()).queue();
			EmbedBuilder eb = new EmbedBuilder();
			NumberFormat nfNum = NumberFormat.getNumberInstance();
			RankManager rankManager = RankManager.getINSTANCE();

			List<Summary> list = new ArrayList<Summary>();
			String rankingName = "";
			int count = 1;
			String command = event.getComponentId().replace("rank_this_", "");
			if (command.endsWith("combat")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("combat_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("combat_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("combat_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("combat_count",
							"where guild_id ='" + event.getGuild().getId() + "'");
				}
				rankingName = "討伐数ランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getCombatCount()) + "体**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				String url = GoogleSheets.create(list, event);
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent())
						.setActionRow(Button.link(url, "スプレッドシート")).queue(success -> {
							Pages.paginate(success, pages);
						});

			} else if (command.endsWith("ground")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("ground_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("ground_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("ground_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("ground_count",
							"where guild_id ='" + event.getGuild().getId() + "'");
				}
				rankingName = "地上げランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getGroundCount()) + "体**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> {
					Pages.paginate(success, pages);
				});
			} else if (command.endsWith("exp")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("exp",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("exp",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("exp",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("exp",
							"where guild_id ='" + event.getGuild().getId() + "'");
				}
				rankingName = "経験値獲得ランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getExp()) + " exp体**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> {
					Pages.paginate(success, pages);
				});
			} else if (command.endsWith("sozai")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("sozai_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("sozai_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("sozai_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("sozai_count",
							"where guild_id ='" + event.getGuild().getId() + "'");
				}
				rankingName = "素材獲得ランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getSozaiCount()) + "個**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> {
					Pages.paginate(success, pages);
				});
			} else if (command.endsWith("weapon")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("weapon_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("weapon_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("weapon_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("weapon_count",
							"where guild_id ='" + event.getGuild().getId() + "'");
				}
				rankingName = "武器獲得ランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getWeaponCount()) + "本**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> {
					Pages.paginate(success, pages);
				});
			} else if (command.endsWith("bukikon")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("bukikon_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("bukikon_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("bukikon_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("bukikon_count",
							"where guild_id ='" + event.getGuild().getId() + "'");
				}
				rankingName = "武器魂獲得ランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getBukikonCount()) + "個**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> {
					Pages.paginate(success, pages);
				});
			} else if (command.endsWith("ban")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("ban_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("ban_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("ban_count",
							"where guild_id ='" + event.getGuild().getId() + "' and create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("ban_count",
							"where guild_id ='" + event.getGuild().getId() + "'");
				}
				rankingName = "BAN回数ランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getBanCount()) + "回**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> {
					Pages.paginate(success, pages);
				});
			}
			rankManager.setData(event.getUser().getId(), list);
		}

		/**
		 * 全体
		 */
		if (event.getComponentId().equals("rank_all_d") || event.getComponentId().equals("rank_all_m")
				|| event.getComponentId().equals("rank_all_t") || event.getComponentId().equals("rank_all_a")) {
			//			for (Button b : event.getMessage().getButtons()) {
			//				if (b.isDisabled()) {
			//					event.editMessage(event.getMessage()).queue();
			//					return;
			//				}
			//			}
			//			
			//			event.editButton(event.getButton().asDisabled()).queue();
			event.getMessage().delete().queue();
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("導入鯖全体ランキング");
			eb.setAuthor(event.getMessage().getEmbeds().get(0).getAuthor().getName());

			event.getChannel().sendMessageEmbeds(eb.build()).setActionRows(
					ActionRow.of(Button.of(ButtonStyle.PRIMARY, event.getComponentId() + "_combat", "討伐"),
							Button.of(ButtonStyle.PRIMARY, event.getComponentId() + "_ground", "地上げ")),
					ActionRow.of(Button.of(ButtonStyle.PRIMARY, event.getComponentId() + "_exp", "経験値獲得"),
							Button.of(ButtonStyle.PRIMARY, event.getComponentId() + "_sozai", "素材獲得"),
							Button.of(ButtonStyle.PRIMARY, event.getComponentId() + "_weapon", "武器獲得"),
							Button.of(ButtonStyle.PRIMARY, event.getComponentId() + "_bukikon", "武器魂獲得")),
					ActionRow.of(
							Button.of(ButtonStyle.DANGER, event.getComponentId() + "_ban", "BAN回数"),
							Button.of(ButtonStyle.DANGER, "cancel", "キャンセル")))
					.queue();
		}
		if (event.getComponentId().startsWith("rank_all_")
				&& (event.getComponentId().endsWith("_combat") || event.getComponentId().endsWith("_ground")
						|| event.getComponentId().endsWith("_exp") || event.getComponentId().endsWith("_sozai")
						|| event.getComponentId().endsWith("_weapon") || event.getComponentId().endsWith("_bukikon")
						|| event.getComponentId().endsWith("_ban"))) {
			for (Button b : event.getMessage().getButtons()) {
				if (b.isDisabled()) {
					event.editMessage(event.getMessage()).queue();
					return;
				}
			}
			event.editButton(event.getButton().asDisabled()).queue();
			EmbedBuilder eb = new EmbedBuilder();
			NumberFormat nfNum = NumberFormat.getNumberInstance();
			RankManager rankManager = RankManager.getINSTANCE();

			List<Summary> list = new ArrayList<Summary>();
			String rankingName = "";
			int count = 1;
			String command = event.getComponentId().replace("rank_all_", "");
			if (command.endsWith("combat")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("combat_count",
							"where create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("combat_count",
							"where create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("combat_count",
							"where create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("combat_count",
							"");
				}
				rankingName = "討伐数ランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getCombatCount()) + "体**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				String url = GoogleSheets.create(list, event);
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent())
						.setActionRow(Button.link(url, "スプレッドシート")).queue(success -> {
							Pages.paginate(success, pages);
						});
				//				event.getMessage().reply(eb.build())
				//						.setActionRow(Button.link(url, "スプレッドシート")).queue();
			} else if (command.endsWith("ground")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("ground_count",
							"where create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("ground_count",
							"where create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("ground_count",
							"where create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("ground_count",
							"");
				}
				rankingName = "地上げランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getGroundCount()) + "体**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> {
					Pages.paginate(success, pages);
				});
			} else if (command.endsWith("exp")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("exp",
							"where create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("exp",
							"where create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("exp",
							"where create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("exp",
							"");
				}
				rankingName = "経験値獲得ランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getExp()) + " exp**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> {
					Pages.paginate(success, pages);
				});
			} else if (command.endsWith("sozai")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("sozai_count",
							"where create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("sozai_count",
							"where create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("sozai_count",
							"where create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("sozai_count",
							"");
				}
				rankingName = "素材獲得ランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getSozaiCount()) + "個**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> {
					Pages.paginate(success, pages);
				});
			} else if (command.endsWith("weapon")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("weapon_count",
							"where create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("weapon_count",
							"where create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("weapon_count",
							"where create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("weapon_count",
							"");
				}
				rankingName = "武器獲得ランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getWeaponCount()) + "個**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> {
					Pages.paginate(success, pages);
				});
			} else if (command.endsWith("bukikon")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("bukikon_count",
							"where create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("bukikon_count",
							"where create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("bukikon_count",
							"where create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("bukikon_count",
							"");
				}
				rankingName = "武器魂獲得ランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getBukikonCount()) + "個**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> {
					Pages.paginate(success, pages);
				});
			} else if (command.endsWith("ban")) {
				if (command.startsWith("d")) {
					list = Sqlite.selectSummaryOrderByExample("ban_count",
							"where create_date >='" + strDate + "'");
				} else if (command.startsWith("m")) {
					list = Sqlite.selectSummaryOrderByExample("ban_count",
							"where create_date >='" + strFirstDate
									+ "'");
				} else if (command.startsWith("t")) {
					list = Sqlite.selectSummaryOrderByExample("ban_count",
							"where create_date like'"
									+ tdate + "%'");
				} else if (command.startsWith("a")) {
					list = Sqlite.selectSummaryOrderByExample("ban_count",
							"");
				}
				rankingName = "BAN回数ランキング";
				eb.setAuthor(rankingName);
				ArrayList<Page> pages = new ArrayList<>();
				for (Summary s : list) {
					String name = "(" + s.getMemberId() + ")確認中...";
					User m = event.getJDA().getUserById(s.getMemberId());
					if (m != null) {
						name = m.getAsTag();
					}
					s.setMemberName(name);
					if ((count - 1) % 10 == 0 && count != 1) {
						eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
						pages.add(new Page(eb.build()));
						//						event.getMessage().reply(eb.build()).queue();
						eb.clear();
						eb.setAuthor(rankingName + "\n");
					}

					eb.appendDescription(count + "位 `" + name + "` **" + nfNum.format(s.getBanCount()) + "回**\n");
					count++;
				}
				eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				pages.add(new Page(eb.build()));
				event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> {
					Pages.paginate(success, pages);
				});
			}

			rankManager.setData(event.getUser().getId(), list);
		}

	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		new AmmountEvent().onMessageReceived(event);
		new TrainingEvent().onMessageReceived(event);
		new PrizeMoneyEvent().onMessageReceived(event);
		new PetCount().onMessageReceived(event);
		rmapCalc(event);

		materiaCalc(event);

		ChannelManager channelManager = ChannelManager.getINSTANCE();
		channelManager.setData(event.getChannel().getId(), new Date());
		channelManager.setCData(event.getChannel().getId(), event.getChannel());

	}

	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {
		new PetCount().onMessageUpdate(event);
		rankCalc(event);
	}

	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		new AmmountEvent().onGuildMessageReactionAdd(event);
	}

	/**
	 * 素材のカウント
	 * @param event
	 */
	private void materiaCalc(MessageReceivedEvent event) {
		if (!event.getAuthor().getId().equals(Const.TAO_ID)) {
			return;
		}

		if (event.getMessage().getEmbeds() != null && event.getMessage().getEmbeds().isEmpty()) {
			return;
		}

		MessageEmbed embed = event.getMessage().getEmbeds().get(0);

		if (embed.getDescription() == null || embed.getDescription().isEmpty()) {
			return;
		}

		Pattern banP = Pattern.compile(".*<@([0-9]+)>.*さん.*セルフBOT検知しました.*");
		Matcher banm = banP.matcher(embed.getDescription());
		if (banm.find()) {
			String userId = banm.group(1);
			event.getChannel().sendMessage("::unban " + userId).queue();
			if (userId == null || userId.isEmpty()) {
				return;
			}
			User user = event.getJDA().getUserById(userId);
			if (user == null) {
				return;
			}
			Summary summary = new Summary();
			summary.setGuildId(event.getGuild().getId());
			summary.setMemberId(userId);
			summary.setBanCount(1D);
			Sqlite.insertSummary(summary);

		}

		if (!"戦闘結果:".equals(embed.getTitle())) {
			return;
		}

		String userId = event.getMessage().getReferencedMessage().getAuthor().getId();
		if (userId == null || userId.isEmpty()) {
			return;
		}

		CalcManager Calcmanager = CalcManager.getINSTANCE();
		CalcInfo calcInfo = Calcmanager.getUserId(userId);
		if (calcInfo == null) {
			calcInfo = new CalcInfo();
		}

		Pattern expP = Pattern.compile("<@" + userId + ">は(.*)EXP");
		String exp = "";
		if (embed.getDescription().contains("倒した")) {
			Matcher expM = expP.matcher(embed.getDescription());
			if (expM.find()) {
				exp = expM.group(1).substring(2);
			}
		}

		calcInfo.addExp(Double.parseDouble(exp.replaceAll(",", "")));

		boolean isMateria = false;
		boolean isBukikon = false;
		boolean isWeapon = false;
		for (Field f : embed.getFields()) {

			if (f.getValue().contains("[素材]")) {
				isMateria = true;
			}
			if (f.getValue().contains("武器魂")) {
				isBukikon = true;
			}
			if (f.getValue().contains("[武器]")) {
				isWeapon = true;
			}
		}
		calcInfo.addBattleCount();
		Summary summary = new Summary();
		summary.setGuildId(event.getGuild().getId());
		summary.setMemberId(userId);
		summary.setCombatCount(1D);
		summary.setExp(Double.parseDouble(exp.replaceAll(",", "")));
		summary.setGroundCount(1D);

		if (isMateria) {
			summary.setSozaiCount(1D);
			calcInfo.addMateriaCount();
		}

		if (isBukikon) {
			summary.setBukikonCount(1D);
			calcInfo.addBukikonCount();
		}

		if (isWeapon) {
			summary.setWeaponCount(1D);
			calcInfo.addWeaponCount();
		}
		Calcmanager.setData(userId, calcInfo);

		Sqlite.insertSummary(summary);

	}

	/**
	 * rmapの集計
	 * @param event
	 */
	public void rmapCalc(MessageReceivedEvent event) {

		if (!event.getAuthor().getId().equals(Const.TAO_ID)) {
			return;
		}

		if (event.getMessage().getReferencedMessage() == null) {
			return;
		}

		if (!event.getMessage().getReferencedMessage().getContentRaw().startsWith("::rmap")) {
			return;
		}

		if (event.getMessage().getEmbeds() != null && event.getMessage().getEmbeds().isEmpty()) {
			return;
		}

		MessageEmbed embed = event.getMessage().getEmbeds().get(0);

		Long total = 0L;
		Long totalLv = 0L;
		Long skillP = 0L;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("```\nLv -> 経験値変換\n");
		int count = 0;
		String[] alphalist = new String[] { "1a", "1b", "1c", "1d", "1e", "1f", "1g", "1h", "1i", "1j", "2a", "2b",
				"2c", "2d", "2e", "2f", "2g", "2h", "2i", "2j", "3a", "3b", "3c", "3d", "3e", "3f", "3g", "3h", "3i",
				"3j" };
		for (Field field : embed.getFields()) {

			String strLv = field.getValue().replaceAll("素早さ.*", "").substring(9).trim();
			Long lv = Utility.convertCommmaToLong(strLv);
			skillP = skillP + lv / 20000;
			totalLv += lv;
			Long exp = lv * lv;
			total += exp;

			if (embed.getFields().size() > 10) {
				stringBuilder.append("role " + alphalist[count] + " : " + Utility.convertCommaToStr(exp) + " exp\n");
			} else {
				if (count < 10) {
					stringBuilder.append("role " + count + "  : " + Utility.convertCommaToStr(exp) + " exp\n");
				} else {
					stringBuilder.append("role " + count + " : " + Utility.convertCommaToStr(exp) + " exp\n");
				}
			}
			count++;
		}
		stringBuilder.append("----------------------------------\n");
		stringBuilder.append("total   : " + Utility.convertCommaToStr(total) + " exp\n");

		stringBuilder.append("        : " + Utility.convertKanjiNum(total) + " exp\n");
		stringBuilder.append("total Lv: " + Utility.convertCommaToStr(totalLv) + "\n");
		stringBuilder.append("total SP: " + Utility.convertCommaToStr(skillP) + "\n");
		stringBuilder.append("```");

		event.getMessage().reply(stringBuilder.toString()).queue();

	}

	/**
	 * rank の表示
	 * @param event
	 */
	public void rankCalc(MessageUpdateEvent event) {

		if (!event.getAuthor().getId().equals(Const.TAO_ID)) {
			return;
		}

		if (event.getMessage().getReferencedMessage() == null) {
			return;
		}

		if (!event.getMessage().getReferencedMessage().getContentRaw().contains("rank")) {
			return;
		}

		if (event.getMessage().getEmbeds() != null && event.getMessage().getEmbeds().isEmpty()) {
			return;
		}

		if (!event.getMessage().getContentRaw().contains("処理を終了させました")) {
			return;
		}

		boolean isServer = false;
		if (event.getMessage().getEmbeds().get(0).getAuthor() != null
				&& event.getMessage().getEmbeds().get(0).getAuthor().getName().contains("サーバー")) {
			isServer = true;
		}

		if (isServer) {
			RankMessageManager rankMessageManager = RankMessageManager.getINSTANCE();
			rankMessageManager.setRankMessage(event.getMessage());
			event.getMessage().reply("Androidで見れる表示にしますか？")
					.setActionRow(Button.of(ButtonStyle.PRIMARY, "rviewa " + event.getMessage().getId(), "表示"),
							Button.of(ButtonStyle.DANGER, "cancel", "キャンセル"))
					.queue();
		}

	}
}

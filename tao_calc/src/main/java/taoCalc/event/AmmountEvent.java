package taoCalc.event;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import taoCalc.CalcManager;
import taoCalc.Const;
import taoCalc.PlayerManager;
import taoCalc.db.Sqlite;
import taoCalc.dto.Attack;
import taoCalc.dto.Member;
import taoCalc.dto.MonsterRate;
import taoCalc.dto.Penetration;
import taoCalc.dto.PrizeMoneyInfo;
import taoCalc.dto.Rate;
import taoCalc.util.Utility;

public class AmmountEvent extends MessageEvent {

	static final String TAO_ID = "526620171658330112";
	static final String NG_REPLY = "::re";

	public void onMessageReceived(MessageReceivedEvent event) {

		if (!event.getAuthor().getId().equals(TAO_ID)) {
			return;
		}

		if (event.getMessage().getReferencedMessage().getContentRaw().startsWith(NG_REPLY)) {
			return;
		}
		
		Boolean isWrite = PermissionUtil.checkPermission(event.getGuild().getTextChannelById(event.getChannel().getId()),event.getMember(),Permission.MESSAGE_WRITE);
		String guildId = event.getGuild().getId();
		String memberId = event.getMessage().getReferencedMessage().getAuthor().getId();
		// お試し
		if (event.getMessage().getReferencedMessage().getContentRaw().toLowerCase().startsWith("::atk")) {
			if (event.getMessage().getEmbeds().isEmpty()) {
				if (event.getMessage().getContentRaw().contains("【超激レア】")) {
					String roleId = Sqlite.getRole(guildId, Const.発言不可);
					Role role = event.getGuild().getRoleById(roleId);
					if (role != null) {
						event.getGuild().addRoleToMember(memberId, role).queue();
						EmbedBuilder eb = new EmbedBuilder();
						eb.setTitle("リアクション押下で発言不可解除です！");
						eb.setDescription("「tc:mt <メッセージ>」で通知を出せるよ！");
						eb.setFooter("↓押してね！");
						Message ms = event.getMessage().reply(eb.build()).complete();
						ms.addReaction("U+1F91E").queue();
					}
				}
			}
		}

		String rank = "";
		PlayerManager Playermanager = PlayerManager.getINSTANCE();
		if (!event.getMessage().getEmbeds().isEmpty() && !(event.getMessage().getEmbeds().get(0).getTitle() == null)) {
			if (event.getMessage().getEmbeds().get(0).getTitle().contains("待ち構えている")) {
				if (event.getMessage().getEmbeds().get(0).getTitle().startsWith("属性")) {

					if (event.getMessage().getEmbeds().get(0).getTitle().contains("【" + Const.通常 + "】")) {
						rank = Const.通常;
						if (event.getMessage().getEmbeds().get(0).getTitle().contains("素早さ: 100")) {
							rank = Const.鯖限;
						}
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("【" + Const.弱敵 + "】")) {
						rank = Const.弱敵;
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("【" + Const.強敵 + "】")) {
						rank = Const.強敵;
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("【" + Const.超強敵 + "】")) {
						rank = Const.超強敵;
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("【" + Const.シリーズ + "】")) {
						rank = Const.シリーズ;
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("【" + Const.レア + "】")) {
						rank = Const.レア;
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("【" + Const.激レア + "】")) {
						rank = Const.激レア;
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("【" + Const.超激レア + "】")) {
						rank = Const.超激レア;
						String roleId = Sqlite.getRole(guildId, Const.発言不可);
						Role role = event.getGuild().getRoleById(roleId);
						if (role != null) {
							event.getGuild().addRoleToMember(memberId, role).queue();
							EmbedBuilder eb = new EmbedBuilder();
							eb.setTitle("リアクション押下で発言不可解除です！");
							eb.setDescription("「tc:mt <メッセージ>」で通知を出せるよ！");
							eb.setFooter("↓押してね！");
							Message ms = event.getMessage().reply(eb.build()).complete();
							ms.addReaction("U+1F91E").queue();
						}
						if (Playermanager.isJoin(event.getGuild().getId())
								&& memberId.equals(Playermanager.getUserId(event.getGuild().getId()))) {
							//音楽を再生
							Playermanager.loadAndPlay(event.getTextChannel(), "Yukkuri.超激レア.9462A487.wav");
							//BOTの音楽再生時の音量を設定
							Playermanager.getGuildMusicManager(event.getGuild()).player.setVolume(200);
						}
					} else if (Const.tohru_image_list.contains(
							event.getMessage().getEmbeds().get(0).getImage().getUrl().replaceFirst("\\?.*", ""))) {
						rank = Const.tohru;
					}
				}
			}
		}
		CalcManager Calcmanager = CalcManager.getINSTANCE();
		Object obj = Calcmanager.getUserId(memberId);

		//攻撃力処理
		Pattern atk = Pattern.compile("に(\\d+)のダメージ");
		try {
			Matcher atkm = atk.matcher(event.getMessage().getContentRaw().replaceAll(",", ""));
			if (atkm.find()) {
				String dm = atkm.group(1);
				if (obj instanceof Attack) {
					Attack attack = (Attack) obj;
					attack.add(Double.parseDouble(dm));
					Calcmanager.setData(memberId, attack);
				}
			}
		} catch (Exception e) {

		}

		// モンスター名処理
		Pattern mp = Pattern.compile("属性.* ランク:【(.+)】(.*)が待ち構えている");
		Pattern mp2 = Pattern.compile("【(.+)");
		String monsterName = "";
		try {
			Matcher mm = mp.matcher(
					event.getMessage().getEmbeds().get(0).getTitle().replaceAll("\\r", "").replaceAll("\\n", ""));
			if (mm.find()) {
				Matcher mm2 = mp2.matcher(mm.group(1));
				if (mm2.find()) {
					monsterName = "【" + mm2.group(1) + "】";
				}
				monsterName = monsterName + mm.group(2);
				if (Const.tohru_list.contains(monsterName)) {
					rank = Const.tohru;
					String roleId = Sqlite.getRole(guildId, Const.発言不可);
					Role role = event.getGuild().getRoleById(roleId);
					if (role != null) {
						event.getGuild().addRoleToMember(memberId, role).queue();
						EmbedBuilder eb = new EmbedBuilder();
						eb.setTitle("リアクション押下で発言不可解除です！");
						eb.setDescription("「tc:mt <メッセージ>」で通知を出せるよ！");
						eb.setFooter("↓押してね！");
						Message ms = event.getMessage().reply(eb.build()).complete();
						ms.addReaction("U+1F91E").queue();
					}
				}
			}
		} catch (Exception e) {
		}

		boolean isSkip = false;
		for (Category cat : event.getGuild().getCategories()) {
			if (cat.getName().contains("付与対象外")) {
				for (GuildChannel c : cat.getChannels()) {
					if (c.getId().equals(event.getChannel().getId())) {
						isSkip = true;
					}
				}
			}
		}
		if (event.getChannel().getName().startsWith("！")) {
			isSkip = true;
		}

		Long waru = 1L;
		if (event.getChannel().getName().startsWith("÷")) {
			waru = 2L;
		}

		if (!rank.isEmpty()) {
			if (Playermanager.isJoin(event.getGuild().getId())
					&& memberId.equals(Playermanager.getUserId(event.getGuild().getId()))) {
				//音楽を再生
				Playermanager.loadAndPlay(event.getTextChannel(), "Yukkuri.アタック.C280DDBF.wav");
				//BOTの音楽再生時の音量を設定
				Playermanager.getGuildMusicManager(event.getGuild()).player.setVolume(150);
			}

			MonsterRate monsterRate = null;
			Long prizeMoney = 0L;
			if (!monsterName.isEmpty()) {

				if (!isSkip) {
					monsterRate = Sqlite.selectMonsterRateInfoByName(guildId, monsterName);

					PrizeMoneyInfo pmi = Sqlite.selectPrizeMoneyInfoByName(guildId, monsterName);

					if (pmi != null && pmi.getExp() != 0) {
						Random rand = new Random();
						if (pmi.getExp() > 10) {
							switch (rand.nextInt(100) + 1) {
							case 1:
								prizeMoney = pmi.getExp();
								break;
							case 2:
							case 3:
							case 4:
							case 5:
							case 6:
							case 7:
							case 8:
							case 9:
							case 10:
							case 11:
								prizeMoney = (long) (rand.nextInt(pmi.getExp().intValue()) + 1);
								break;
							default:
								prizeMoney = (long) (rand.nextInt(10) + 1);
								break;
							}
						} else {
							prizeMoney = (long) (rand.nextInt(pmi.getExp().intValue()) + 1);
						}
						pmi.setExp(pmi.getExp() - prizeMoney);
						Sqlite.updatePrizeMoneyInfo(guildId, pmi);
					}
				}
			}

			Rate rate = Sqlite.selectRateByRank(guildId, rank);
			Member member = Sqlite.selectMemberById(guildId, memberId);
			Long before = 0L;
			Long after = 0L;
			if (member == null) {
				member = new Member(memberId, (rate != null && rate.getAmount() != 0) ? rate.getAmount() / waru : 0L);
				if (!isSkip) {
					member = new Member(memberId, 0L);

					if (monsterRate != null && monsterRate.getAmount() != 0) {
						member.addExp(monsterRate.getAmount() / waru);
					}
					member.addExp(prizeMoney.longValue());
				}
				member.addRank(rank);
				Sqlite.insertMember(guildId, member);
				after = member.getExp();
			} else {
				before = member.getExp();
				if (!isSkip) {
					member.addExp((rate != null && rate.getAmount() != 0) ? rate.getAmount() / waru : 0L);
					if (monsterRate != null && monsterRate.getAmount() != 0) {
						member.addExp(monsterRate.getAmount() / waru);
					}
					member.addExp(prizeMoney.longValue());
				}
				member.addRank(rank);
				Sqlite.updateMember(guildId, member);
				after = member.getExp();
			}

			if (!isSkip) {
				if ((rate != null && rate.getAmount() != 0) || (monsterRate != null && monsterRate.getAmount() != 0)
						|| prizeMoney != 0) {
					EmbedBuilder eb = new EmbedBuilder();
					NumberFormat nfNum = NumberFormat.getNumberInstance();
					eb.setTitle("経験値を付与しました。");

					if (prizeMoney != 0) {
						eb.addField("懸賞金獲得", Utility.convertCommaToStr(prizeMoney.longValue()), false);
					}

					Long exp = (rate != null && rate.getAmount() != 0) ? rate.getAmount() / waru : 0L;
					exp += (monsterRate != null && monsterRate.getAmount() != 0) ? monsterRate.getAmount() / waru : 0L;

					if (exp != 0) {
						eb.addField("経験値獲得", Utility.convertCommaToStr(exp), false);
					}

					eb.addField(event.getMessage().getReferencedMessage().getAuthor().getName(),
							nfNum.format(before) + " -> " + nfNum.format(after), false);
					
					if(isWrite) {
						event.getMessage().reply(eb.build()).queue();
					}

				}
			}
		}

		//イン堀対応

		List<String> inhoriList = new ArrayList<String>();
		if (event.getMessage() != null && !event.getMessage().getContentRaw().isEmpty()) {
			Pattern p1 = Pattern.compile(
					".*属性.* ランク:【(.+)】.*が待ち構えている.*素早さ: ([0-9]+).*倒した！.*属性.* ランク:【(.+)】.*が待ち構えている.*素早さ: ([0-9]+).*倒した！");
			Pattern p2 = Pattern.compile(".*属性.* ランク:【(.+)】.*が待ち構えている.*素早さ: ([0-9]+).*倒した！");
			Matcher m = p1.matcher(event.getMessage().getContentRaw().replaceAll("\\r", "").replaceAll("\\n", ""));
			if (m.find()) {
				for (int i = 1; i <= m.groupCount(); i++) {
					inhoriList.add(m.group(i));
				}
			} else {
				m = p2.matcher(event.getMessage().getContentRaw().replaceAll("\\r", "").replaceAll("\\n", ""));
				if (m.find()) {
					for (int i = 1; i <= m.groupCount(); i++) {
						inhoriList.add(m.group(i));
					}
				}
			}
		}

		if (!inhoriList.isEmpty()) {
			Member member = Sqlite.selectMemberById(guildId, memberId);
			if (member != null) {
				Long before = 0L;
				Long after = 0L;

				before = member.getExp();
				//インホリ
				boolean isInhoriUpdate = false;
				boolean isAddRank = false;
				Penetration p = null;
				if (obj instanceof Penetration) {
					p = (Penetration) obj;
				}
				if (inhoriList.size() == 2) {
					//貫通１回
					if (p != null) {
						p.addOne();
						Calcmanager.setData(memberId, p);
					}
					Rate rate = null;
					if (Utility.getRank(inhoriList.get(0), inhoriList.get(1)) != null) {
						rate = Sqlite.selectRateByRank(guildId, Utility.getRank(inhoriList.get(0), inhoriList.get(1)));
					}
					if (rate != null && rate.getAmount() != 0) {
						if (!isSkip) {
							member.addExp(rate.getAmount() / waru);
							isInhoriUpdate = true;
						}
					}
					if (Utility.getRank(inhoriList.get(0), inhoriList.get(1)) != null) {
						member.addRank(Utility.getRank(inhoriList.get(0), inhoriList.get(1)));
						isAddRank = true;
					}
				} else {
					//貫通２回
					if (p != null) {
						p.addTwo();
						Calcmanager.setData(memberId, p);
					}
					Rate rate = null;
					if (Utility.getRank(inhoriList.get(0), inhoriList.get(1)) != null) {
						rate = Sqlite.selectRateByRank(guildId, Utility.getRank(inhoriList.get(0), inhoriList.get(1)));
					}
					if (rate != null && rate.getAmount() != 0) {
						if (!isSkip) {
							member.addExp(rate.getAmount() / waru);
							isInhoriUpdate = true;
						}
					}
					if (Utility.getRank(inhoriList.get(0), inhoriList.get(1)) != null) {
						member.addRank(Utility.getRank(inhoriList.get(0), inhoriList.get(1)));
						isAddRank = true;
					}

					if (Utility.getRank(inhoriList.get(2), inhoriList.get(1)) != null) {
						rate = Sqlite.selectRateByRank(guildId, Utility.getRank(inhoriList.get(2), inhoriList.get(1)));
					}
					if (rate != null && rate.getAmount() != 0) {
						if (!isSkip) {
							member.addExp(rate.getAmount() / waru);
							isInhoriUpdate = true;
						}
					}
					if (Utility.getRank(inhoriList.get(0), inhoriList.get(1)) != null) {
						member.addRank(Utility.getRank(inhoriList.get(0), inhoriList.get(1)));
						isAddRank = true;
					}
				}
				after = member.getExp();

				if (isInhoriUpdate || isAddRank) {
					Sqlite.updateMember(guildId, member);

				}

				//				if (isAddRank) {
				//					EmbedBuilder eb = new EmbedBuilder();
				//					NumberFormat nfNum = NumberFormat.getNumberInstance();
				//					eb.setTitle("貫通追加分");
				//					StringBuilder sb = new StringBuilder();
				//					if (inhoriList.size() == 2) {
				//						sb.append(inhoriList.get(0) + "：+1");
				//					} else {
				//						if (inhoriList.get(0).equals(inhoriList.get(2))) {
				//							sb.append(inhoriList.get(0) + "：+2");
				//						} else {
				//							sb.append(inhoriList.get(0) + "：+1\n");
				//							sb.append(inhoriList.get(2) + "：+1");
				//						}
				//					}
				//					eb.addField(event.getMessage().getReferencedMessage().getAuthor().getName(),
				//							sb.toString(), false);
				//					eb.setFooter("貫通分の表示（お試し）");
				//					event.getMessage().reply(eb.build()).queue();
				//				}
				if (isInhoriUpdate) {
					EmbedBuilder eb = new EmbedBuilder();
					NumberFormat nfNum = NumberFormat.getNumberInstance();
					eb.setTitle("経験値を付与しました。");
					eb.addField(event.getMessage().getReferencedMessage().getAuthor().getName(),
							nfNum.format(before) + " -> " + nfNum.format(after), false);
					eb.setFooter("貫通分の表示（お試し）");
					if(isWrite) {
						event.getMessage().reply(eb.build()).queue();
					}
				}
			}

		}
	}

	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {

		if (event.getReaction().toString().contains("RE:U+1f91e")) {
			String roleId = Sqlite.getRole(event.getGuild().getId(), Const.発言不可);
			Role role = event.getGuild().getRoleById(roleId);
			if (role != null) {
				event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
				;
			} else {
				event.getChannel().sendMessage("発言不可解除失敗！管理者に外してもらって！");
			}
		}
	}

}

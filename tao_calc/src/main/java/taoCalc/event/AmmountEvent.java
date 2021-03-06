package taoCalc.event;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import taoCalc.CalcManager;
import taoCalc.ChannelManager;
import taoCalc.Const;
import taoCalc.PlayerManager;
import taoCalc.db.Sqlite;
import taoCalc.dto.CalcInfo;
import taoCalc.dto.Member;
import taoCalc.dto.MonsterRate;
import taoCalc.dto.PrizeMoneyInfo;
import taoCalc.dto.RaidInfo;
import taoCalc.dto.RaidMemberInfo;
import taoCalc.dto.Rate;
import taoCalc.dto.Summary;
import taoCalc.util.Utility;

public class AmmountEvent extends MessageEvent {

	static final String NG_REPLY = "::re";

	public void onMessageReceived(MessageReceivedEvent event) {

		if (!event.getAuthor().getId().equals(Const.TAO_ID)) {
			return;
		}

		if (event.getMessage().getReferencedMessage() == null) {
			return;
		}
		if (event.getMessage().getReferencedMessage().getContentRaw().startsWith(NG_REPLY)) {
			return;
		}

		Boolean isWrite = PermissionUtil.checkPermission(
				event.getGuild().getTextChannelById(event.getChannel().getId()), event.getGuild().getSelfMember(),
				Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS);
		String guildId = event.getGuild().getId();
		String memberId = event.getMessage().getReferencedMessage().getAuthor().getId();
		ChannelManager channelManager = ChannelManager.getINSTANCE();
		String channelId = event.getChannel().getId();

		// ?????????
		if (event.getMessage().getReferencedMessage().getContentRaw().toLowerCase().startsWith("::atk")) {
			if (event.getMessage().getEmbeds().isEmpty()) {
				if (event.getMessage().getContentRaw().contains("??????????????????")) {
					String roleId = Sqlite.getRole(guildId, Const.????????????);
					Role role = event.getGuild().getRoleById(roleId);
					if (role != null) {
						event.getGuild().addRoleToMember(memberId, role).queue();
						EmbedBuilder eb = new EmbedBuilder();
						eb.setTitle("???????????????????????????");
						event.getMessage().replyEmbeds(eb.build())
								.setActionRow(
										Button.of(ButtonStyle.SUCCESS, "removeRole", "??????????????????",
												Emoji.fromUnicode("U+1F91E")),
										Button.of(ButtonStyle.PRIMARY, "tcmt", "??????"),
										Button.of(ButtonStyle.DANGER, "tcmt_no", "???????????????"))
								.queue();
						channelManager.setFData(channelId, Const.??????????????????);
					}
				}
			}
		}

		String rank = "";
		PlayerManager Playermanager = PlayerManager.getINSTANCE();
		if (!event.getMessage().getEmbeds().isEmpty() && !(event.getMessage().getEmbeds().get(0).getTitle() == null)) {
			if (event.getMessage().getEmbeds().get(0).getTitle().contains("?????????????????????")) {
				if (event.getMessage().getEmbeds().get(0).getTitle().startsWith("??????")) {

					if (event.getMessage().getEmbeds().get(0).getTitle().contains("???" + Const.?????? + "???")) {
						rank = Const.??????;
						if (event.getMessage().getEmbeds().get(0).getTitle().contains("?????????: 100")) {
							rank = Const.??????;
						}
						channelManager.setFData(channelId, "??????");
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("???" + Const.?????? + "???")) {
						rank = Const.??????;
						channelManager.setFData(channelId, "??????");
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("???" + Const.?????? + "???")) {
						rank = Const.??????;
						channelManager.setFData(channelId, "??????");
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("???" + Const.????????? + "???")) {
						rank = Const.?????????;
						channelManager.setFData(channelId, "??????");
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("???" + Const.???????????? + "???")) {
						rank = Const.????????????;
						channelManager.setFData(channelId, "??????");
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("???" + Const.?????? + "???")) {
						rank = Const.??????;
						channelManager.setFData(channelId, "??????");
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("???" + Const.????????? + "???")) {
						rank = Const.?????????;
						channelManager.setFData(channelId, "??????");
					} else if (event.getMessage().getEmbeds().get(0).getTitle().contains("???" + Const.???????????? + "???")) {
						rank = Const.????????????;
						String roleId = Sqlite.getRole(guildId, Const.????????????);
						Role role = event.getGuild().getRoleById(roleId);
						if (role != null) {
							event.getGuild().addRoleToMember(memberId, role).queue();
							EmbedBuilder eb = new EmbedBuilder();
							eb.setTitle("???????????????????????????");
							event.getMessage().replyEmbeds(eb.build())
									.setActionRow(
											Button.of(ButtonStyle.SUCCESS, "removeRole", "??????????????????",
													Emoji.fromUnicode("U+1F91E")),
											Button.of(ButtonStyle.PRIMARY, "tcmt", "??????"),
											Button.of(ButtonStyle.DANGER, "tcmt_no", "???????????????"))
									.queue();
							channelManager.setFData(channelId, Const.??????????????????);
						}
						if (Playermanager.isJoin(event.getGuild().getId())
								&& memberId.equals(Playermanager.getUserId(event.getGuild().getId()))) {
							//???????????????
							Playermanager.loadAndPlay(event.getTextChannel(), "Yukkuri.????????????.9462A487.wav");
							//BOT????????????????????????????????????
							Playermanager.getGuildMusicManager(event.getGuild()).player.setVolume(200);
						}
					}
				}
			}
		}
		CalcManager Calcmanager = CalcManager.getINSTANCE();
		CalcInfo calcInfo = Calcmanager.getUserId(memberId);

		//???????????????
		Pattern atk = Pattern.compile("(.*)???(\\d+)???????????????");
		Pattern criticalatk = Pattern.compile(".*??????????????????(.*)???(\\d+)???????????????");
		String dm = "";
		try {
			Matcher atkm = atk.matcher(event.getMessage().getContentRaw().replaceAll(",", ""));
			Matcher criticalatkm = criticalatk.matcher(event.getMessage().getContentRaw().replaceAll(",", ""));
			if (criticalatkm.find()) {
				dm = criticalatkm.group(2);
				if (calcInfo == null) {
					calcInfo = new CalcInfo();
				}
				calcInfo.addCritical(Double.parseDouble(dm));
				Calcmanager.setData(memberId, calcInfo);
			} else {
				if (atkm.find()) {
					dm = atkm.group(2);
					if (calcInfo == null) {
						calcInfo = new CalcInfo();
					}
					calcInfo.addDamage(Double.parseDouble(dm));
					Calcmanager.setData(memberId, calcInfo);
				}
			}
			if (Sqlite.countSqliteMasterByTableName(guildId, "raid_info") != 0 && !dm.isEmpty()) {
				RaidInfo raidInfo = Sqlite.selectRaidInfoByValid(guildId);
				if (raidInfo != null) {
					if (event.getMessage().getContentRaw().contains(raidInfo.getName())) {
						double hp = Double.parseDouble(raidInfo.getHp().replace(",", ""));
						if (hp > 0) {
							hp = hp - Double.parseDouble(dm);
							if (hp <= 0) {
								hp = 0;
								raidInfo.setLimit("");
							}
							raidInfo.setHp(Utility.convertCommaToStr(hp));
							Sqlite.updateRaidInfo(guildId, raidInfo);
							RaidMemberInfo raidMemberInfo = Sqlite.selectRaidMemberInfoById(guildId,
									raidInfo.getRaidNo(), memberId);
							if (raidMemberInfo == null) {
								raidMemberInfo = new RaidMemberInfo();
								raidMemberInfo.setMemberId(memberId);
								raidMemberInfo.setRaidNo(raidInfo.getRaidNo());
								raidMemberInfo.setDamage(String.valueOf(Double.parseDouble(dm)));
								Sqlite.insertRaidMemberInfo(guildId, raidMemberInfo);
							} else {
								raidMemberInfo.setDamage(BigDecimal.valueOf(Double.parseDouble(raidMemberInfo.getDamage()) + Double.parseDouble(dm)).toPlainString());
								Sqlite.updateRaidMemberInfo(guildId, raidMemberInfo);
							}
						}
					}
				}
			}
		} catch (

		Exception e) {

		}

		// ????????????????????????
		Pattern mp = Pattern.compile("??????.* ?????????:???(.+)???(.*)????????????????????????");
		Pattern mp2 = Pattern.compile("???(.+)");
		String monsterName = "";
		try {
			Matcher mm = mp.matcher(
					event.getMessage().getEmbeds().get(0).getTitle().replaceAll("\\r", "").replaceAll("\\n", ""));
			if (mm.find()) {
				Matcher mm2 = mp2.matcher(mm.group(1));
				if (mm2.find()) {
					monsterName = "???" + mm2.group(1) + "???";
				}
				monsterName = monsterName + mm.group(2);
				if (Const.tohru_list.contains(monsterName)) {
					rank = Const.tohru;
					String roleId = Sqlite.getRole(guildId, Const.????????????);
					Role role = event.getGuild().getRoleById(roleId);
					if (role != null) {
						event.getGuild().addRoleToMember(memberId, role).queue();
						EmbedBuilder eb = new EmbedBuilder();
						eb.setTitle("????????????????????????");
						event.getMessage().replyEmbeds(eb.build())
								.setActionRow(
										Button.of(ButtonStyle.SUCCESS, "removeRole", "??????????????????",
												Emoji.fromUnicode("U+1F91E")),
										Button.of(ButtonStyle.PRIMARY, "tcmt", "??????"),
										Button.of(ButtonStyle.DANGER, "tcmt_no", "???????????????"))
								.queue();
						channelManager.setFData(channelId, Const.???????????????);
					}
				}
			}
		} catch (Exception e) {
		}

		if (!rank.isEmpty()) {
			channelManager.setData(channelId, new Date());
			channelManager.setCData(channelId, event.getChannel());
			channelManager.setEData(channelId, event.getMessage().getReferencedMessage().getAuthor().getName());
		}

		boolean isSkip = false;
		for (Category cat : event.getGuild().getCategories()) {
			if (cat.getName().contains("???????????????")) {
				for (GuildChannel c : cat.getChannels()) {
					if (c.getId().equals(event.getChannel().getId())) {
						isSkip = true;
					}
				}
			}
		}
		if (event.getChannel().getName().startsWith("???")) {
			isSkip = true;
		}

		Long waru = 1L;
		if (event.getChannel().getName().startsWith("??")) {
			waru = 2L;
		}

		if (!rank.isEmpty()) {
			if (Playermanager.isJoin(event.getGuild().getId())
					&& memberId.equals(Playermanager.getUserId(event.getGuild().getId()))) {
				//???????????????
				Playermanager.loadAndPlay(event.getTextChannel(), "Yukkuri.????????????.C280DDBF.wav");
				//BOT????????????????????????????????????
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
					eb.setTitle("?????????????????????????????????");

					if (prizeMoney != 0) {
						eb.addField("???????????????", Utility.convertCommaToStr(prizeMoney.longValue()), false);
					}

					Long exp = (rate != null && rate.getAmount() != 0) ? rate.getAmount() / waru : 0L;
					exp += (monsterRate != null && monsterRate.getAmount() != 0) ? monsterRate.getAmount() / waru : 0L;

					if (exp != 0) {
						eb.addField("???????????????", Utility.convertCommaToStr(exp), false);
					}

					eb.addField(event.getMessage().getReferencedMessage().getAuthor().getName(),
							nfNum.format(before) + " -> " + nfNum.format(after), false);

					if (isWrite) {
						event.getMessage().replyEmbeds(eb.build()).queue();
					}

				}
			}
		}

		//???????????????

		List<String> inhoriList = new ArrayList<String>();
		if (event.getMessage() != null && !event.getMessage().getContentRaw().isEmpty()) {
			Pattern p1 = Pattern.compile(
					".*??????.* ?????????:???(.+)???.*????????????????????????.*?????????: ([0-9]+).*????????????.*??????.* ?????????:???(.+)???.*????????????????????????.*?????????: ([0-9]+).*????????????");
			Pattern p2 = Pattern.compile(".*??????.* ?????????:???(.+)???.*????????????????????????.*?????????: ([0-9]+).*????????????");
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
				//????????????
				boolean isInhoriUpdate = false;
				boolean isAddRank = false;
				if (inhoriList.size() == 2) {
					//????????????
					if (calcInfo != null) {
						calcInfo.addOne();
						Calcmanager.setData(memberId, calcInfo);
						Summary summary = new Summary();
						summary.setGuildId(event.getGuild().getId());
						summary.setMemberId(memberId);
						summary.setGroundCount(1D);
						Sqlite.insertSummary(summary);
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
					//????????????
					if (calcInfo != null) {
						calcInfo.addTwo();
						Calcmanager.setData(memberId, calcInfo);
						Summary summary = new Summary();
						summary.setGuildId(event.getGuild().getId());
						summary.setMemberId(memberId);
						summary.setGroundCount(2D);
						Sqlite.insertSummary(summary);
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
					rate = null;
					if (Utility.getRank(inhoriList.get(2), inhoriList.get(3)) != null) {
						rate = Sqlite.selectRateByRank(guildId, Utility.getRank(inhoriList.get(2), inhoriList.get(3)));
					}
					if (rate != null && rate.getAmount() != 0) {
						if (!isSkip) {
							member.addExp(rate.getAmount() / waru);
							isInhoriUpdate = true;
						}
					}
					if (Utility.getRank(inhoriList.get(2), inhoriList.get(3)) != null) {
						member.addRank(Utility.getRank(inhoriList.get(2), inhoriList.get(3)));
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
				//					eb.setTitle("???????????????");
				//					StringBuilder sb = new StringBuilder();
				//					if (inhoriList.size() == 2) {
				//						sb.append(inhoriList.get(0) + "???+1");
				//					} else {
				//						if (inhoriList.get(0).equals(inhoriList.get(2))) {
				//							sb.append(inhoriList.get(0) + "???+2");
				//						} else {
				//							sb.append(inhoriList.get(0) + "???+1\n");
				//							sb.append(inhoriList.get(2) + "???+1");
				//						}
				//					}
				//					eb.addField(event.getMessage().getReferencedMessage().getAuthor().getName(),
				//							sb.toString(), false);
				//					eb.setFooter("?????????????????????????????????");
				//					event.getMessage().reply(eb.build()).queue();
				//				}
				if (isInhoriUpdate) {
					EmbedBuilder eb = new EmbedBuilder();
					NumberFormat nfNum = NumberFormat.getNumberInstance();
					eb.setTitle("?????????????????????????????????");
					eb.addField(event.getMessage().getReferencedMessage().getAuthor().getName(),
							nfNum.format(before) + " -> " + nfNum.format(after), false);
					eb.setFooter("?????????????????????????????????");
					if (isWrite) {
						event.getMessage().replyEmbeds(eb.build()).queue();
					}
				}
			}

		}
	}

	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {

		if (event.getReaction().toString().contains("RE:U+1f91e")) {
			String roleId = Sqlite.getRole(event.getGuild().getId(), Const.????????????);
			Role role = event.getGuild().getRoleById(roleId);
			if (role != null) {
				event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
				;
			} else {
				event.getChannel().sendMessage("???????????????????????????????????????????????????????????????");
			}
		}
	}

}

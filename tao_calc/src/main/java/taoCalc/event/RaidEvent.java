package taoCalc.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import taoCalc.Const;
import taoCalc.RaidManager;
import taoCalc.db.Sqlite;
import taoCalc.dto.RaidInfo;
import taoCalc.util.Utility;

public class RaidEvent extends MessageEvent {

	public void onMessageReceived(MessageReceivedEvent event) {

		String guildId = event.getGuild().getId();

		if (event.getAuthor().isBot()) {
			return;
		}

		if (event.getMessage().getReferencedMessage() == null) {
			return;
		}

		if (!event.getMessage().getReferencedMessage().getAuthor().getId().equals(Const.TAO_ID)) {
			return;
		}

		Message message = event.getMessage();
		Message referencedMessage = event.getMessage().getReferencedMessage();

		if (!message.getContentRaw().startsWith("レイド")) {
			return;
		}

		if (referencedMessage.getEmbeds().isEmpty() || referencedMessage.getEmbeds().get(0).getTitle().isEmpty()) {
			event.getMessage().reply("そいつは無理").queue();
			return;
		}

		boolean isExec = false;
		if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
			isExec = true;
		} else {
			String roleId = Sqlite.getRole(guildId, Const.経験値管理係);
			if (roleId.isEmpty()) {
				for (Role role : event.getMember().getRoles()) {
					if (role.getName().equals("経験値管理係")) {
						isExec = true;
					}
				}
			} else {
				for (Role role : event.getMember().getRoles()) {
					if (role.getId().equals(roleId)) {
						isExec = true;
					}
				}
			}
		}

		if (event.getMember().getId().equals("300984197634588672")) {
			isExec = true;
		}

		if (!isExec) {
			event.getMessage().reply("権限ないぞ").queue();
			return;
		}

		Pattern p1 = Pattern.compile("属性:(.*) ランク:【(.+)】(.*)が待ち構えている.*HP: (.*) 素早さ");
		Matcher m = p1.matcher(
				referencedMessage.getEmbeds().get(0).getTitle().replaceAll("\\r", "").replaceAll("\\n", ""));

		String monsterName = "";
		String hp = "";
		String rank = "";
		String zokusei = "";
		String messageId = event.getMessageId();
		if (m.find()) {
			zokusei = m.group(1);
			rank = m.group(2);
			monsterName = m.group(3);
			hp = m.group(4);
		}

		if (monsterName.isEmpty() || hp.isEmpty() || rank.isEmpty()) {
			event.getMessage().reply("ランクor敵名orHPの取得に失敗しました").queue();
			return;
		} else {

			RaidManager raidManager = RaidManager.getINSTANCE();
			if (raidManager.getDate(guildId) != null) {
				event.getMessage().reply("現在どこかのチャンネルでレイドボス設定中...").queue();
				return;
			}

			if (Sqlite.countSqliteMasterByTableName(guildId, "raid_info") == 0) {
				Sqlite.executeSql(guildId,
						"create table raid_info(raid_no,name,zokusei,hp,total_hp,rank,url,limit_date)");
			}

			if (Sqlite.countSqliteMasterByTableName(guildId, "raid_member_info") == 0) {
				Sqlite.executeSql(guildId,
						"create table raid_member_info(raid_no,member_id,damage)");
			}

			RaidInfo raidInfo = Sqlite.selectRaidInfoByValid(guildId);

			if (raidInfo != null) {

				//開催中
				if (Double.parseDouble(raidInfo.getHp().replace(",", "")) <= 0) {
					//死んでる
				} else {
					event.getMessage().reply("現在レイドボスが出現中です。").queue();
					EmbedBuilder eb = Utility.createRaidBoss(raidInfo);
					event.getMessage().replyEmbeds(eb.build()).queue();
					return;
				}
			}

			hp = Utility.raidBossHP(rank, hp);
			raidInfo = new RaidInfo();
			raidInfo.setName(monsterName);
			raidInfo.setHp(hp);
			raidInfo.setTotalHp(hp);
			raidInfo.setRank(rank);
			raidInfo.setURL(referencedMessage.getEmbeds().get(0).getImage().getUrl());
			raidInfo.setZokusei(zokusei);

			raidManager.setData(guildId, raidInfo);

			SelectionMenu menu = SelectionMenu.create("menu:raid")
					.setPlaceholder("実行するコマンドを選んでください") // shows the placeholder indicating what this menu is for
					.setRequiredRange(1, 1) // only one can be selected
					.addOption("1時間", "1hour")
					.addOption("3時間", "3hour")
					.addOption("6時間", "6hour")
					.addOption("12時間", "12hour")
					.addOption("24時間", "24hour")
					.addOption("48時間", "48hour")
					.build();

			event.getMessage().reply("開催する期間を設定してください。")
					.setActionRow(menu)
					.queue();

		}
	}

}

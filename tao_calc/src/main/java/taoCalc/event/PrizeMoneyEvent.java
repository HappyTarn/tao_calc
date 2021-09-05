package taoCalc.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import taoCalc.db.Sqlite;
import taoCalc.dto.Member;
import taoCalc.dto.PrizeMoneyInfo;
import taoCalc.util.Utility;

public class PrizeMoneyEvent extends MessageEvent {

	static final String TAO_ID = "526620171658330112";

	public void onMessageReceived(MessageReceivedEvent event) {

		String guildId = event.getGuild().getId();
		

		if (event.getAuthor().isBot()) {
			return;
		}

		if (event.getMessage().getReferencedMessage() == null) {
			return;
		}

		if (!event.getMessage().getReferencedMessage().getAuthor().getId().equals(TAO_ID)) {
			return;
		}

		Message message = event.getMessage();
		Message referencedMessage = event.getMessage().getReferencedMessage();

		if (!message.getContentRaw().startsWith("懸賞金")) {
			return;
		}
		if (message.getContentRaw().split(" ").length != 2) {
			event.getMessage().reply("設定がおかしいぞ").queue();
			return;
		}

		Long prizeMoney = Utility.convertLong(message.getContentRaw().split(" ")[1]);

		if (prizeMoney == null || prizeMoney < 0) {
			return;
		}

		if (referencedMessage.getEmbeds().isEmpty() || referencedMessage.getEmbeds().get(0).getTitle().isEmpty()) {
			return;
		}

		Pattern p1 = Pattern.compile("属性.* ランク:【(.+)】(.*)が待ち構えている");
		Matcher m = p1.matcher(
				referencedMessage.getEmbeds().get(0).getTitle().replaceAll("\\r", "").replaceAll("\\n", ""));

		String monsterName = "";
		if (m.find()) {
			monsterName = m.group(2);
		}

		if (monsterName.isEmpty()) {
			event.getMessage().reply("敵名の取得に失敗しました").queue();
		} else {

			Member member = Sqlite.selectMemberById(guildId, event.getAuthor().getId());
			
			if (member.getExp() < prizeMoney) {
				event.getMessage().reply("ウホウホ！保有経験値不足ゴリ").queue();
				return;
			}

			String beforeExp = member.getFormatExp();
			member.substExp(prizeMoney);
			Sqlite.updateMember(guildId, member);
			String afterExp = member.getFormatExp();

			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("懸賞金設定");
			eb.setDescription("この敵に懸賞金を掛けました。\n");
			eb.addField(monsterName, prizeMoney.toString(), false);
			eb.addField("保有経験値", beforeExp + " -> " + afterExp, false);
			message.reply(eb.build()).queue();
			
			ArrayList<HashMap<String, String>> serverInfo = Sqlite.getMapExecuteSql(event.getGuild().getId(),
					"select * from server_info");
			
			String beforePrize = "";
			String afterPrize = "";
			if (Sqlite.countSqliteMasterByTableName(guildId, "prize_money_info") == 0) {
				Sqlite.executeSql(guildId,
						"create table prize_money_info(name,exp)");
			} else {
				PrizeMoneyInfo pmi = Sqlite.selectPrizeMoneyInfoByName(guildId, monsterName);

				if (pmi == null) {
					pmi = new PrizeMoneyInfo(monsterName, prizeMoney);
					Sqlite.insertPrizeMoneyInfo(guildId, pmi);
					beforePrize = "0";
					afterPrize = pmi.getFormatExp();
				} else {
					beforePrize = pmi.getFormatExp();
					pmi.addExp(prizeMoney);
					Sqlite.updatePrizeMoneyInfo(guildId, pmi);
					afterPrize = pmi.getFormatExp();
				}
			}
			if (!serverInfo.isEmpty() && !serverInfo.get(0).get("prize_info_channel").isEmpty()) {
				String prizeInfoChannel = serverInfo.get(0).get("prize_info_channel");
				EmbedBuilder embedBuilder = new EmbedBuilder();
				embedBuilder.setTitle("懸賞金設定");
				embedBuilder.setDescription(event.getAuthor().getName() + "がこの敵に懸賞金を掛けました。\n");
				embedBuilder.addField(monsterName, prizeMoney.toString(), false);
				embedBuilder.addField("懸賞金額", beforePrize + " -> " + afterPrize, false);
				embedBuilder.addField("", "[direct_link](" + Utility.getLinkURL(guildId, event.getChannel().getId(), event.getMessageId()) +")", false);
				event.getGuild().getTextChannelById(prizeInfoChannel).sendMessage(embedBuilder.build()).queue();
			}
		}
	}

}

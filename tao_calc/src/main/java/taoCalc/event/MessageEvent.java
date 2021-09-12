package taoCalc.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import taoCalc.CalcManager;
import taoCalc.Const;
import taoCalc.RankMessageManager;
import taoCalc.db.Sqlite;
import taoCalc.dto.CalcInfo;
import taoCalc.util.Utility;

public class MessageEvent extends ListenerAdapter {

	public void onButtonClick(ButtonClickEvent event) {

		//通知ボタン
		if (event.getComponentId().equals("tcmt")) {
			MessageChannel mtChannel = null;
			for (GuildChannel c : event.getGuild().getChannels()) {
				if (c.getName().contains("レアキャラ報告")) {
					mtChannel = (MessageChannel) c;
				}
			}

			String roleId = Sqlite.getRole(event.getGuild().getId(), Const.超激レア報告OK);
			Role role = event.getGuild().getRoleById(roleId);

			if (mtChannel != null && role != null) {

				mtChannel.sendMessage(role.getAsMention() + "：<#" + event.getChannel().getId() + "> で超激レアが出たよ！\n"
						+ "> 通知した人：<@" + event.getMember().getId() + ">").queue();

				event.editButton(event.getButton().asDisabled()).queue();
			}
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
			MessageEmbed embed = rankMessageManager.getRankMessage(event.getComponentId().split(" ")[1]).getEmbeds().get(0);

			EmbedBuilder eb = new EmbedBuilder(embed);

			eb.setDescription(embed.getDescription().replaceAll("\\(<\\('', ''\\)>\\)", ""));

			
			event.getMessage().delete().queue();
			event.getChannel().sendMessage(eb.build()).queue();
		}

		//キャンセル
		if (event.getComponentId().equals("cancel")) {
			event.getMessage().delete().queue();
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
		if (!event.getAuthor().getId().equals("526620171658330112")) {
			return;
		}

		if (event.getMessage().getEmbeds() != null && event.getMessage().getEmbeds().isEmpty()) {
			return;
		}

		MessageEmbed embed = event.getMessage().getEmbeds().get(0);

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

		boolean isMateria = false;
		boolean isBukikon = false;
		for (Field f : embed.getFields()) {
			if (f.getValue().contains("[素材]")) {
				isMateria = true;
			}
			if (f.getValue().contains("武器魂")) {
				isBukikon = true;
			}
		}
		calcInfo.addBattleCount();

		if (isMateria) {
			calcInfo.addMateriaCount();
		}

		if (isBukikon) {
			calcInfo.addBukikonCount();
		}

		Calcmanager.setData(userId, calcInfo);

	}

	/**
	 * rmapの集計
	 * @param event
	 */
	public void rmapCalc(MessageReceivedEvent event) {

		if (!event.getAuthor().getId().equals("526620171658330112")) {
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
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("```\nLv -> 経験値変換\n");
		int count = 0;
		String[] alphalist = new String[] { "1a", "1b", "1c", "1d", "1e", "1f", "1g", "1h", "1i", "1j", "2a", "2b",
				"2c", "2d", "2e", "2f", "2g", "2h", "2i", "2j", "3a", "3b", "3c", "3d", "3e", "3f", "3g", "3h", "3i",
				"3j" };
		for (Field field : embed.getFields()) {

			String strLv = field.getValue().replaceAll("素早さ.*", "").substring(9).trim();
			Long lv = Utility.convertCommmaToLong(strLv);
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
		stringBuilder.append("```");

		event.getMessage().reply(stringBuilder.toString()).queue();

	}

	/**
	 * rank の表示
	 * @param event
	 */
	public void rankCalc(MessageUpdateEvent event) {

		if (!event.getAuthor().getId().equals("526620171658330112")) {
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
			event.getMessage().reply("Androidで見れる表示にしますか？").setActionRow(Button.of(ButtonStyle.PRIMARY, "rviewa " + event.getMessage().getId(), "表示"),
					Button.of(ButtonStyle.DANGER, "cancel", "キャンセル")).queue();
		}

	}
}

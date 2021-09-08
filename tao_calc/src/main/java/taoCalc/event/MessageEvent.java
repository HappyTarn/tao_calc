package taoCalc.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import taoCalc.CalcManager;
import taoCalc.dto.Material;
import taoCalc.util.Utility;

public class MessageEvent extends ListenerAdapter {

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
		Object obj = Calcmanager.getUserId(userId);
		if(obj == null) {
			return;
		}
		Material material = null;
		if (obj instanceof Material) {
			material = (Material) obj;
		}
		
		boolean isMateria = false;
		for(Field f : embed.getFields()) {
			if(f.getValue().contains("[素材]")) {
				isMateria = true;
			}
		}
		
		if(isMateria) {
			material.addMateriaCount();
		}else {
			material.addBattleCount();
		}
		
		Calcmanager.setData(userId, material);

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
	 * rmapの集計
	 * @param event
	 */
	public void rankCalc(MessageUpdateEvent event) {

		if (!event.getAuthor().getId().equals("526620171658330112")) {
			return;
		}

		if (!event.getMessage().getReferencedMessage().getContentRaw().contains("rank")) {
			return;
		}

		if (!event.getMessage().getReferencedMessage().getContentRaw().endsWith("a")) {
			return;
		}

		if (event.getMessage().getEmbeds() != null && event.getMessage().getEmbeds().isEmpty()) {
			return;
		}

		if (!event.getMessage().getContentRaw().contains("見たいページを発言してください")) {
			return;
		}

		MessageEmbed embed = event.getMessage().getEmbeds().get(0);

		EmbedBuilder eb = new EmbedBuilder(embed);

		eb.setDescription(embed.getDescription().replaceAll("\\(<\\('', ''\\)>\\)", ""));

		event.getMessage().reply(eb.build()).queue();

	}
}

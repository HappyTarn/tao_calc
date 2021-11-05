package taoCalc.commands;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import taoCalc.ChannelManager;

public class AkiCommand extends Command {

	public AkiCommand() {
		this.name = "aki";
		this.help = "空いてるチャンネル";
		this.guildOnly = true;
		this.ownerCommand = false;
		this.aliases = new String[] { "あき" };
	}

	//冥界、開発室、もえにょ
	private static final List<String> guilds = Arrays.asList("823574484660518932", "853332936530460713",
			"602561685450129408");

	private static final List<String> channels = Arrays.asList(
			"833683675345977416", //冥界本編
			"833670681589186600", //冥界2nd
			"834103647058395236",//冥界3rd
			"834778594642886746",//冥界4th
			"834521648916201522",//冥界5th
			"602561685450129413",//もえにょ
			"602766571802198032",//もえにょ2nd
			"863801642352246806");//開発室

	@Override
	protected void execute(CommandEvent event) {

		if (!guilds.contains(event.getGuild().getId())) {
			return;
		}

		ChannelManager channelManager = ChannelManager.getINSTANCE();
		Calendar calendar = Calendar.getInstance();
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("チャンネル空きチェック");
		
		for(String channelId : channels) {
			Date date = channelManager.getDate(channelId);
			MessageChannel messageChannel = channelManager.getCDate(channelId);
			if (date == null) {
				continue;
			}
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, 3);
			
			Date now = new Date();
			if (now.after(calendar.getTime())) {
				eb.addField(messageChannel.getName(), "【空き】<#" + channelId + ">", false);
			}else {
				eb.addField(messageChannel.getName(), "使用中", false);
			}
		}
		
		event.getMessage().replyEmbeds(eb.build()).queue();
		
	}

}

package taoCalc.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import taoCalc.db.Sqlite;
import taoCalc.dto.PetInfo;

public class ShowPetCommand extends Command {

	public ShowPetCommand() {
		this.name = "showPet";
		this.help = "ペットの集計結果を見る";
		this.arguments = "[none,tao]";
		this.guildOnly = true;
		this.ownerCommand = false;
		this.aliases = new String[] { "showpet", "Showpet", "ShowPet","sp" };
	}

	@Override
	protected void execute(CommandEvent event) {

		String guildId = event.getGuild().getId();
		String memberId = event.getAuthor().getId();
		EmbedBuilder eb = new EmbedBuilder();
		if (event.getArgs().isEmpty()) {
			eb.setTitle("ペット集計結果");

			PetInfo petInfo = Sqlite.selectPetInfo(guildId, memberId);
			if (petInfo != null) {
				eb.appendDescription(petInfo.get割合());
				event.getMessage().replyEmbeds(eb.build()).queue();
			} else {
				event.getMessage().reply("まだペット情報がないよ").queue();;
			}
		
		} else if (event.getArgs().startsWith("tao")) {
			eb.setTitle("Unknown以上集計結果");

			PetInfo petInfo = Sqlite.selectPetInfo(guildId, memberId);
			if (petInfo != null) {
				eb.appendDescription(petInfo.getTao割合());
				event.getMessage().replyEmbeds(eb.build()).queue();
			} else {
				event.getMessage().reply("まだペット情報がないよ").queue();;
			}
		} else if (event.getArgs().startsWith("reset")) {
			eb.setTitle("ペット集計をリセットしました。");

			PetInfo petInfo = Sqlite.selectPetInfo(guildId, memberId);
			if (petInfo != null) {
				petInfo.setnCount(0L);
				petInfo.setrCount(0L);
				petInfo.setSrCount(0L);
				petInfo.setUnknownCount(0L);
				petInfo.setMmoCount(0L);
				petInfo.setTaoCount(0L);
				Sqlite.updatePetInfo(guildId, petInfo);
				eb.appendDescription(petInfo.getTao割合());
				event.getMessage().replyEmbeds(eb.build()).queue();
			} else {
				event.getMessage().reply("まだペット情報がないよ").queue();;
			}
		}

	}

}

package taoCalc.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import taoCalc.util.Utility;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class Rank extends Command {

	public Rank() {
		this.name = "rank";
		this.help = "taoCalcでのランキング";
		this.arguments = "[d,m,yyyy-mm-dd]";
		this.guildOnly = false;
	}

	@Override
	protected void execute(CommandEvent event) {

		String kbn = "";
		if (event.getArgs().isEmpty()) {
			kbn = "d";
		} else {
			kbn = event.getArgs().split(" ")[0];
		}
		if (kbn.isEmpty() || !"d".equals(kbn.toLowerCase()) || !"m".equals(kbn.toLowerCase()) || Utility.checkDate(kbn)) {
			event.getMessage().reply("ランキングにはこの検索機能はないようだ。");
		}

		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("区分を選択してください");
		event.getMessage().reply(eb.build())
				.setActionRow(Button.of(ButtonStyle.SUCCESS, "rank_this_" + kbn, event.getGuild().getName()),
						Button.of(ButtonStyle.SUCCESS, "rank_all_" + kbn, "全体"))
				.queue();
	}

}

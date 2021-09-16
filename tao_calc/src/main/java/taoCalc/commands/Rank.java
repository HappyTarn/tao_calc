package taoCalc.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class Rank extends Command {

	public Rank() {
		this.name = "rank";
		this.help = "taoCalcでのランキング";
		this.arguments = "[d,m,yyyymmdd]";
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
		if (kbn.isEmpty()) {
			event.getMessage().reply("ランキングにはこの検索機能はないようだ。").queue();
			return;
		}
		if ("d".equals(kbn.toLowerCase()) || "m".equals(kbn.toLowerCase()) || kbn.length() == 8) {
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("区分を選択してください");
			if(kbn.equals("d")) {
				eb.setAuthor("本日のランキング");
			}else if(kbn.equals("d")) {
				eb.setAuthor("今月のランキング");
			}else if(kbn.startsWith("2")) {
				eb.setAuthor("指定日："+kbn+"のランキング");
				kbn = "t";
			}
			event.getMessage().reply(eb.build())
			.setActionRow(Button.of(ButtonStyle.SUCCESS, "rank_this_" + kbn, event.getGuild().getName()),
					Button.of(ButtonStyle.SUCCESS, "rank_all_" + kbn, "全体"),
					Button.of(ButtonStyle.DANGER, "cancel", "キャンセル"))
			.queue();
		}else {
			event.getMessage().reply("ランキングにはこの検索機能はないようだ。").queue();
			return;
		}
	}

}

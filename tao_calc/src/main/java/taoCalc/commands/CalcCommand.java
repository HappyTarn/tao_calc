package taoCalc.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import taoCalc.CalcManager;
import taoCalc.dto.CalcInfo;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class CalcCommand extends Command {

	public CalcCommand() {
		this.name = "calc";
		this.help = "いろいろ計算";
		this.arguments = "[reset]";
		this.guildOnly = false;
		this.aliases = new String[] { "calc" };
	}

	@Override
	protected void execute(CommandEvent event) {

		CalcManager Calcmanager = CalcManager.getINSTANCE();

		if (event.getArgs().isEmpty()) {
			CalcInfo calcInfo = Calcmanager.getUserId(event.getAuthor().getId());

			if (calcInfo == null) {
				event.getMessage().reply("データがないよ").queue();
				return;
			}

			EmbedBuilder eb = new EmbedBuilder();

			eb.addField("最大ダメージ", calcInfo.getMaxString(), true);
			eb.addField("最小ダメージ", calcInfo.getMinString(), true);
			
			eb.addField("会心最大ダメージ", calcInfo.getCriString(), false);

			eb.addField("貫通割合", calcInfo.get貫通割合(), false);

			eb.addField("素材割合", calcInfo.get素材割合(), false);

			eb.addField("武器魂割合", calcInfo.get武器魂割合(), false);

			event.getMessage().reply(eb.build()).queue();

		}

		if ("reset".equals(event.getArgs())) {
			Calcmanager.setData(event.getAuthor().getId(), null);
			event.getMessage().reply("データをリセットしました。").queue();
		}

	}

}

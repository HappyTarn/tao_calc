package taoCalc.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import taoCalc.CalcManager;
import taoCalc.dto.Attack;
import taoCalc.dto.Penetration;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class CalcCommand extends Command {

	public CalcCommand() {
		this.name = "calc";
		this.help = "貫通の計算、攻撃力を計算";
		this.arguments = "[貫通,攻撃]";
		this.guildOnly = false;
		this.aliases = new String[] { "calc" };
	}

	@Override
	protected void execute(CommandEvent event) {

		CalcManager Calcmanager = CalcManager.getINSTANCE();

		if (event.getArgs().isEmpty()) {
			Object obj = Calcmanager.getUserId(event.getAuthor().getId());

			if (obj == null) {
				event.getMessage().reply("データがないよ").queue();
				return;
			}

			if (obj instanceof Penetration) {
				EmbedBuilder eb = new EmbedBuilder();
				eb.setDescription(((Penetration) obj).get割合());
				event.getMessage().reply(eb.build()).queue();
			}
			
			if (obj instanceof Attack) {
				Attack attack = (Attack)obj;
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle("攻撃力の結果");
				eb.addField("最大ダメージ", attack.getMaxString(), true);
				eb.addField("最小ダメージ", attack.getMinString(), true);
				event.getMessage().reply(eb.build()).queue();
			}

		}

		if ("貫通".equals(event.getArgs())) {
			Penetration p = new Penetration();
			Calcmanager.setData(event.getAuthor().getId(), p);
			event.getMessage().reply("貫通の計測を始めます").queue();
		}
		
		if ("攻撃".equals(event.getArgs())) {
			Attack a = new Attack();
			Calcmanager.setData(event.getAuthor().getId(), a);
			event.getMessage().reply("攻撃力の計測を始めます").queue();
		}
	}

}

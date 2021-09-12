package taoCalc.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import taoCalc.Const;
import taoCalc.db.Sqlite;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class MtoCommand extends Command {

	public MtoCommand() {
		this.name = "mto";
		this.help = "他鯖超激レア報告OKロールに通知を出します。";
		this.guildOnly = true;
		this.aliases = new String[] { "MTO", "Mto" };
	}

	@Override
	protected void execute(CommandEvent event) {

		MessageChannel mtChannel = null;
		for (GuildChannel c : event.getGuild().getChannels()) {
			if (c.getName().contains("レアキャラ報告")) {
				mtChannel = (MessageChannel) c;
			}
		}
		
		String roleId = Sqlite.getRole(event.getGuild().getId(), Const.他鯖超激レア報告OK);
		Role role = event.getGuild().getRoleById(roleId);

		if (mtChannel != null && role != null) {
			
			if(event.getArgs().isEmpty()) {
				if(event.getGuild().getId().equals("823574484660518932")) {
					EmbedBuilder eb = new EmbedBuilder();
					eb.setTitle("どこで出た？");
					event.getMessage().reply(eb.build()).setActionRow(Button.of(ButtonStyle.PRIMARY,"tcmto 1", "公式"),
							Button.of(ButtonStyle.PRIMARY,"tcmto 2", "DW"),
							Button.of(ButtonStyle.PRIMARY,"tcmto 3", "KING"),
							Button.of(ButtonStyle.PRIMARY,"tcmto 4", "もえにょ")).queue();
				}else {
					event.getChannel().sendMessage("tc:mto <どこの鯖>\nどこの鯖で超激でたか書いてね！").queue();
				}
			}else {
				event.getChannel().sendMessage("通知完了").queue();
				
				mtChannel.sendMessage(role.getAsMention() +"：" + event.getArgs() + "\n"
						+ "> 通知した人：<@" + event.getAuthor().getId() + ">").queue();
			}
		}

	}

}

package taoCalc.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import taoCalc.Const;
import taoCalc.db.Sqlite;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class MtCommand extends Command {

	public MtCommand() {
		this.name = "mt";
		this.help = "超激レア報告OKロールに通知を出します。";
		this.guildOnly = true;
		this.aliases = new String[] { "MT", "Mt", "mT" };
	}

	@Override
	protected void execute(CommandEvent event) {

		MessageChannel mtChannel = null;
		for (GuildChannel c : event.getGuild().getChannels()) {
			if (c.getName().contains("レアキャラ報告")) {
				mtChannel = (MessageChannel) c;
			}
		}
		
		String roleId = Sqlite.getRole(event.getGuild().getId(), Const.超激レア報告OK);
		Role role = event.getGuild().getRoleById(roleId);

		if (mtChannel != null && role != null) {
			event.getChannel().sendMessage("通知完了").queue();

			mtChannel.sendMessage(role.getAsMention() +"：<#" + event.getChannel().getId() + "> で超激レアが出たよ！\n"
					+ event.getArgs() + "\n"
					+ "> 通知した人：<@" + event.getAuthor().getId() + ">").queue();
		}

	}

}

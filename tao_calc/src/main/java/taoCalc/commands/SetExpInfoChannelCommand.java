package taoCalc.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import taoCalc.db.Sqlite;

public class SetExpInfoChannelCommand extends Command {

	public SetExpInfoChannelCommand() {
		this.name = "setExpInfoChannel";
		this.help = "保有経験値情報の垂れ流しチャンネルを設定";
		this.arguments = "[#channel]";
		this.guildOnly = true;
		this.userPermissions = new Permission[] { Permission.ADMINISTRATOR };
		this.aliases = new String[] { "seic" };
	}

	@Override
	protected void execute(CommandEvent event) {
		String guildId = event.getGuild().getId();
		String args = event.getArgs();
		if (args.isEmpty()) {
			event.getMessage().reply("引数が変だぞ").queue();
			return;
		}
		Pattern p = Pattern.compile("([0-9]+)");
		Matcher m = p.matcher(event.getArgs().split(" ")[0]);
		if (!m.find()) {
			event.getMessage().reply("引数が正しくないです（" + event.getArgs() + "）").queue();
			return;
		}
		Sqlite.executeSqlNotResult(guildId, "update server_info set exp_info_channel='" + m.group() + "'");

		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("垂れ流しチャンネルを設定");
		eb.setDescription("<#" + m.group() + ">");
		event.getMessage().replyEmbeds(eb.build()).queue();
		;

	}

}

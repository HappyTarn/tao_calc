package taoCalc.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import taoCalc.Const;
import taoCalc.db.Sqlite;
import taoCalc.util.Utility;

public class SetRoleCommand extends Command {

	public SetRoleCommand() {
		this.name = "setRole";
		this.help = "ロールの設定";
		this.arguments = "[経験値管理係 @role,経験値変更係 @role,発言不可 @role,超激レア報告OK @role,他鯖超激レア報告OK @role]";
		this.guildOnly = true;
		this.ownerCommand = false;
        this.userPermissions = new Permission[] {Permission.ADMINISTRATOR};
		this.aliases = new String[] { "setrole"};
	}

	@Override
	protected void execute(CommandEvent event) {
		String guildId = event.getGuild().getId();
		String args = event.getArgs();
		if(args.isEmpty()) {
			event.getMessage().reply("引数が変だぞ").queue();
			return;
		}
		if(args.split(" ").length != 2) {
			event.getMessage().reply("引数が変だぞ").queue();
			return;
		}
		String[] argsList = args.split(" ");
		
		Role role = event.getGuild().getRoleById(Utility.getId(argsList[1]));
		if(role == null) {
			event.getMessage().reply("ロールまちがってんぞ").queue();
			return;
		}
		switch (argsList[0]){
			case "経験値管理係":
				Sqlite.executeSql(guildId, "update role set "+ Const.経験値管理係 + "='" + Utility.getId(argsList[1]) + "'");
				break;
			case "経験値変更係":
				Sqlite.executeSql(guildId, "update role set "+ Const.経験値変更係 + "='" + Utility.getId(argsList[1]) + "'");
				break;
			case "発言不可":
				Sqlite.executeSql(guildId, "update role set "+ Const.発言不可 + "='" + Utility.getId(argsList[1]) + "'");
				break;
			case "超激レア報告OK":
				Sqlite.executeSql(guildId, "update role set "+ Const.超激レア報告OK + "='" + Utility.getId(argsList[1]) + "'");
				break;
			case "他鯖超激レア報告OK":
				Sqlite.executeSql(guildId, "update role set "+ Const.他鯖超激レア報告OK + "='" + Utility.getId(argsList[1]) + "'");
				break;
			default:
				event.getMessage().reply(argsList[0] + "なんてないぞ").queue();
				return;
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("ロール変更");
		eb.setDescription(argsList[0] + " -> " + role.getAsMention());
		event.getMessage().replyEmbeds(eb.build()).queue();;
		
	}

}

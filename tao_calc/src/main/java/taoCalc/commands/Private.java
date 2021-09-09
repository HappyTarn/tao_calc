package taoCalc.commands;

import java.util.EnumSet;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.Permission;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class Private extends Command {

	public Private() {
		this.name = "private";
		this.help = "プライベートチャンネル設定【要管理者権限】";
		this.arguments = "on off";
		this.guildOnly = false;
		this.aliases = new String[] { "p" };
	}

	@Override
	protected void execute(CommandEvent event) {
		
		EnumSet<Permission> memberPermissions = event.getMember().getPermissions(event.getGuild().getGuildChannelById(event.getChannel().getId()));
		if(!memberPermissions.contains(Permission.ADMINISTRATOR)) {
			event.getMessage().reply("権限がありません。").queue();
			return;
		}
		
		if("on".equals(event.getArgs())) {
			event.getGuild().getGuildChannelById(event.getChannel().getId()).putPermissionOverride(event.getGuild().getPublicRole()).setDeny(Permission.VIEW_CHANNEL).queue();
		}else if("off".equals(event.getArgs())) {
			event.getGuild().getGuildChannelById(event.getChannel().getId()).putPermissionOverride(event.getGuild().getPublicRole()).setAllow(Permission.VIEW_CHANNEL).queue();
		}
	}
	

}

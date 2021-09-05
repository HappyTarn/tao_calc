package taoCalc.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;
import taoCalc.PlayerManager;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class StopCommand extends Command {

	public StopCommand() {
		this.name = "stop";
		this.help = "stop";
		this.guildOnly = false;
		this.aliases = new String[] { "stop" };
	}

	@Override
	protected void execute(CommandEvent event) {
		//VoiceChannelに接続
		Guild guild = event.getGuild();

		AudioManager manager = guild.getAudioManager();
		manager.closeAudioConnection();
		PlayerManager Playermanager = PlayerManager.getINSTANCE();
		Playermanager.setJoin(guild.getId(), "");
	}

}

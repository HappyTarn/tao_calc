package taoCalc.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import taoCalc.PlayerManager;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class StartCommand extends Command {

	public StartCommand() {
		this.name = "start";
		this.help = "start";
		this.guildOnly = false;
		this.aliases = new String[] { "start" };
	}

	@Override
	protected void execute(CommandEvent event) {
		//VoiceChannelに接続
		Guild guild = event.getGuild();
		VoiceChannel voiceChannel = null;
		for(VoiceChannel vc : guild.getVoiceChannels()) {
			if(vc.getMembers().contains(event.getMember())){
				voiceChannel = vc;
			}
		}
		
		if(voiceChannel == null) {
			event.getChannel().sendMessage("でも君、VCにいないじゃん？").queue();
			return;
		}
		PlayerManager Playermanager = PlayerManager.getINSTANCE();
		if(Playermanager.isJoin(guild.getId())) {
			event.getChannel().sendMessage("使われてるよ").queue();
			return;
		}else {
			Playermanager.setJoin(guild.getId(),event.getAuthor().getId());
		}
		
		AudioManager manager = guild.getAudioManager();
		manager.openAudioConnection(voiceChannel);
		
		Playermanager.loadAndPlay(event.getTextChannel(), event.getArgs());
		//BOTの音楽再生時の音量を設定
		Playermanager.getGuildMusicManager(event.getGuild()).player.setVolume(200);
	}

}

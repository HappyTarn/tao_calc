package taoCalc.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import taoCalc.PlayerManager;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class PlayCommand extends Command {

	public PlayCommand() {
		this.name = "play";
		this.help = "play";
		this.arguments = "tao or mmo";
		this.guildOnly = false;
		this.aliases = new String[] { "play" };
	}

	@Override
	protected void execute(CommandEvent event) {

		if (event.getArgs() != null && event.getArgs().isEmpty()) {
			return;
		}

		PlayerManager Playermanager = PlayerManager.getINSTANCE();
		if (Playermanager.isJoin(event.getGuild().getId())
				&& event.getAuthor().getId().equals(Playermanager.getUserId(event.getGuild().getId()))) {
			if ("mmo".equals(event.getArgs().toLowerCase())) {
				//音楽を再生
				Playermanager.loadAndPlay(event.getTextChannel(), "mmo.mp3");
				//BOTの音楽再生時の音量を設定
				Playermanager.getGuildMusicManager(event.getGuild()).player.setVolume(200);
			} else if ("tao".equals(event.getArgs().toLowerCase())) {
				//音楽を再生
				Playermanager.loadAndPlay(event.getTextChannel(), "tao.mp3");
				//BOTの音楽再生時の音量を設定
				Playermanager.getGuildMusicManager(event.getGuild()).player.setVolume(200);
			} else if ("unknown".equals(event.getArgs().toLowerCase())) {
				//音楽を再生
				Playermanager.loadAndPlay(event.getTextChannel(), "unknown.mp3");
				//BOTの音楽再生時の音量を設定
				Playermanager.getGuildMusicManager(event.getGuild()).player.setVolume(200);
			} else if ("超激レア".equals(event.getArgs())) {
				//音楽を再生
				Playermanager.loadAndPlay(event.getTextChannel(), "超激レア.mp3");
				//BOTの音楽再生時の音量を設定
				Playermanager.getGuildMusicManager(event.getGuild()).player.setVolume(200);

			}
		}
	}

}

package taoCalc;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayerManager {
	private static PlayerManager INSTANCE;
	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;

	private HashMap<String, String> joinData = new HashMap<String, String>();

	private PlayerManager() {
		this.musicManagers = new HashMap<>();

		this.playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}

	public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
		long guildId = guild.getIdLong();
		GuildMusicManager musicManager = musicManagers.get(guildId);

		if (musicManager == null) {
			musicManager = new GuildMusicManager(playerManager);
			musicManagers.put(guildId, musicManager);
		}

		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

		return musicManager;

	}

	public void loadAndPlay(TextChannel channel, String trackURL) {
		String prefix = "[MusicBotInfo]";
		GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());

		playerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				//            	channel.sendMessage(prefix + "次の音楽を再生します:" + track.getInfo());
				play(musicManager, track);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();

				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}

				//                channel.sendMessage(prefix + "次の音楽を再生します:" + firstTrack.getInfo());

				play(musicManager, firstTrack);
			}

			@Override
			public void noMatches() {
				//見つからなかった
				//                channel.sendMessage(prefix + "指定されたURLの音楽が見つかりませんでした。");
			}

			@Override
			public void loadFailed(FriendlyException e) {
				//ロード時にエラー発生
				//                channel.sendMessage(prefix + "再生のロードでエラーが発生しました。");
			}
		});

	}

	private void play(GuildMusicManager musicManager, AudioTrack track) {
		musicManager.scheduler.queue(track);
	}

	public static synchronized PlayerManager getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new PlayerManager();
		}

		return INSTANCE;
	}

	public boolean isJoin(String guildId) {
		String id = joinData.get(guildId);
		if (id == null) {
			return false;
		} else {
			return !id.isEmpty();
		}
	}

	public void setJoin(String guildId, String userId) {
		joinData.put(guildId, userId);
	}

	public Object getUserId(String id) {
		return joinData.get(id);
	}

}
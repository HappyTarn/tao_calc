package taoCalc.event;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import taoCalc.Const;
import taoCalc.PlayerManager;
import taoCalc.db.Sqlite;
import taoCalc.dto.PetInfo;

public class PetCount extends MessageEvent {


	@Override
	public void onMessageUpdate(MessageUpdateEvent event) {

		if (!event.getMessage().getAuthor().getId().equals(Const.TAO_ID)) {
			return;
		}

		if (event.getMessage() != null && event.getMessage().getEmbeds().isEmpty()) {
			return;
		}

		MessageEmbed messageEmbed = event.getMessage().getEmbeds().get(0);

		if (messageEmbed.getFields().isEmpty() || messageEmbed.getFields().size() != 4) {
			return;
		}

		if (messageEmbed.getDescription() != null && !messageEmbed.getDescription().isEmpty()
				&& messageEmbed.getDescription().contains("の仲間にしました！")) {

			String[] list = messageEmbed.getFields().get(3).getValue().split("\\|");

			if (list.length == 2) {
				list[0] = list[0].trim().substring(6);

				if (Sqlite.countSqliteMasterByTableName(event.getGuild().getId(), "pet_info") == 0) {
					Sqlite.executeSql(event.getGuild().getId(),
							"create table pet_info(id,n_count INTEGER,r_count INTEGER,sr_count INTEGER,u_count INTEGER,mmo_count INTEGER,tao_count INTEGER)");
				}

				String memberId = event.getMessage().getReferencedMessage().getAuthor().getId();
				PetInfo petInfo = Sqlite.selectPetInfo(event.getGuild().getId(), memberId);
				if (petInfo.getId().isEmpty()) {
					petInfo.setId(memberId);
					petInfo.addCount(list[0]);
					Sqlite.insertPetInfo(event.getGuild().getId(), petInfo);

				} else {
					petInfo.addCount(list[0]);
					Sqlite.updatePetInfo(event.getGuild().getId(), petInfo);

					if (list[0].equals(Const.PET_U) || list[0].equals(Const.PET_MMO) || list[0].equals(Const.PET_TAO)) {
						if (list[0].equals(Const.PET_MMO) || list[0].equals(Const.PET_TAO)) {
							event.getChannel().sendMessage("<@" + memberId + ">" + list[0] + "ペットだよ").queue();
						}
						
						PlayerManager Playermanager = PlayerManager.getINSTANCE();
						if (Playermanager.isJoin(event.getGuild().getId())
								&& memberId.equals(Playermanager.getUserId(event.getGuild().getId()))) {
							if (list[0].equals(Const.PET_U)) {
								//音楽を再生
								Playermanager.loadAndPlay(event.getTextChannel(), "unknown.mp3");
								//BOTの音楽再生時の音量を設定
								Playermanager.getGuildMusicManager(event.getGuild()).player.setVolume(200);
							} else if (list[0].equals(Const.PET_MMO)) {
								//音楽を再生
								Playermanager.loadAndPlay(event.getTextChannel(), "mmo.mp3");
								//BOTの音楽再生時の音量を設定
								Playermanager.getGuildMusicManager(event.getGuild()).player.setVolume(200);
							} else if (list[0].equals(Const.PET_TAO)) {
								//音楽を再生
								Playermanager.loadAndPlay(event.getTextChannel(), "tao.mp3");
								//BOTの音楽再生時の音量を設定
								Playermanager.getGuildMusicManager(event.getGuild()).player.setVolume(200);
							}
						}
					}
				}

			}

		}
	}
	
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getMessage().getAuthor().getId().equals(Const.TAO_ID)) {
			return;
		}

		if (event.getMessage() != null && event.getMessage().getEmbeds().isEmpty()) {
			return;
		}

		MessageEmbed messageEmbed = event.getMessage().getEmbeds().get(0);

		if (messageEmbed.getFields().isEmpty() || messageEmbed.getFields().size() != 4) {
			return;
		}
		PlayerManager Playermanager = PlayerManager.getINSTANCE();
		String memberId = event.getMessage().getReferencedMessage().getAuthor().getId();
		if (messageEmbed.getDescription() != null && !messageEmbed.getDescription().isEmpty()
				&& messageEmbed.getDescription().contains("仲間にしますか？")) {
			if (Playermanager.isJoin(event.getGuild().getId())
					&& memberId.equals(Playermanager.getUserId(event.getGuild().getId()))) {
				//音楽を再生
				Playermanager.loadAndPlay(event.getTextChannel(), "Yukkuri.ペット.AEC61022.wav");
				//BOTの音楽再生時の音量を設定
				Playermanager.getGuildMusicManager(event.getGuild()).player.setVolume(200);
			}
		}
	}
}

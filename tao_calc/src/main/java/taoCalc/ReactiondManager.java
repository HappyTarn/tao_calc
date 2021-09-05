package taoCalc;

import java.util.HashMap;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public class ReactiondManager {
	private static ReactiondManager INSTANCE;

	private HashMap<String, HashMap<String, Message>> commandData = new HashMap<String, HashMap<String, Message>>();

	private ReactiondManager() {
	}

	public synchronized HashMap<String, Message> getCommandData(Guild guild) {

		return commandData.get(guild.getId());

	}

	public static synchronized ReactiondManager getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new ReactiondManager();
		}

		return INSTANCE;
	}

	public void setCommandData(String guildId, String userId, Message message) {
		HashMap<String, Message> map = commandData.get(guildId);
		if (map == null) {
			map = new HashMap<String, Message>();
		}
		map.put(userId, message);
		commandData.put(guildId, map);
	}

}
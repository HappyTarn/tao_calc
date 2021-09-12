package taoCalc;

import java.util.HashMap;

import net.dv8tion.jda.api.entities.Message;

public class RankMessageManager {
	private static RankMessageManager INSTANCE;

	private HashMap<String, Message> rankMessage = new HashMap<String, Message>();

	private RankMessageManager() {
	}

	public synchronized Message getRankMessage(String id) {

		return rankMessage.get(id);

	}

	public static synchronized RankMessageManager getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new RankMessageManager();
		}

		return INSTANCE;
	}

	public void setRankMessage(Message message) {
		rankMessage.put(message.getId(),message);
	}

}
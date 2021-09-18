package taoCalc;

import java.util.HashMap;
import java.util.List;

import taoCalc.dto.Summary;

public class RankManager {
	private static RankManager INSTANCE;

	private HashMap<String, List<Summary>> data = new HashMap<String, List<Summary>>();

	private RankManager() {
	}

	public static synchronized RankManager getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new RankManager();
		}

		return INSTANCE;
	}


	public void setData(String key, List<Summary> date) {
		data.put(key, date);
	}

	public List<Summary> getDate(String key) {
		return data.get(key);
	}

}
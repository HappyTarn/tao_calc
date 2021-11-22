package taoCalc;

import java.util.HashMap;

import taoCalc.dto.RaidInfo;

public class RaidManager {
	private static RaidManager INSTANCE;

	private HashMap<String, RaidInfo> data = new HashMap<String, RaidInfo>();

	private RaidManager() {
	}

	public static synchronized RaidManager getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new RaidManager();
		}

		return INSTANCE;
	}

	public void setData(String guildId, RaidInfo raidInfo) {
		data.put(guildId, raidInfo);
	}


	public RaidInfo getDate(String guildId) {
		return data.get(guildId);
	}


}
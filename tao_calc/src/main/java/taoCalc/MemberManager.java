package taoCalc;

import java.util.HashMap;

public class MemberManager {
	private static MemberManager INSTANCE;

	private HashMap<String, String> joinData = new HashMap<String, String>();

	private MemberManager() {
	}

	public static synchronized MemberManager getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new MemberManager();
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
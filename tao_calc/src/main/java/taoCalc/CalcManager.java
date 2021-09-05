package taoCalc;

import java.util.HashMap;

public class CalcManager {
	private static CalcManager INSTANCE;

	private HashMap<String, Object> data = new HashMap<String, Object>();

	private CalcManager() {
	}

	public static synchronized CalcManager getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new CalcManager();
		}

		return INSTANCE;
	}


	public void setData(String userId, Object obj) {
		data.put(userId, obj);
	}

	public Object getUserId(String id) {
		return data.get(id);
	}

}
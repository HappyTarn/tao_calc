package taoCalc;

import java.util.HashMap;

import taoCalc.dto.CalcInfo;

public class CalcManager {
	private static CalcManager INSTANCE;

	private HashMap<String, CalcInfo> data = new HashMap<String, CalcInfo>();

	private CalcManager() {
	}

	public static synchronized CalcManager getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new CalcManager();
		}

		return INSTANCE;
	}


	public void setData(String userId, CalcInfo obj) {
		data.put(userId, obj);
	}

	public CalcInfo getUserId(String id) {
		return data.get(id);
	}

}
package taoCalc;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ItemManager {
	private static ItemManager INSTANCE;

	private HashMap<String, LinkedHashMap<String, List<String>>> data = new HashMap<String, LinkedHashMap<String,List<String>>>();

	private ItemManager() {
	}

	public static synchronized ItemManager getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new ItemManager();
		}

		return INSTANCE;
	}


	public void setData(String userId,  LinkedHashMap<String, List<String>> obj) {
		data.put(userId, obj);
	}

	public  LinkedHashMap<String, List<String>> getData(String id) {
		return data.get(id);
	}

}
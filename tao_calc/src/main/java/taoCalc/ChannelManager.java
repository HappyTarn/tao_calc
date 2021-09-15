package taoCalc;

import java.util.Date;
import java.util.HashMap;

public class ChannelManager {
	private static ChannelManager INSTANCE;

	private HashMap<String, Date> data = new HashMap<String, Date>();

	private ChannelManager() {
	}

	public static synchronized ChannelManager getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new ChannelManager();
		}

		return INSTANCE;
	}


	public void setData(String channelId, Date date) {
		data.put(channelId, date);
	}

	public Date getDate(String id) {
		return data.get(id);
	}

}
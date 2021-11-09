package taoCalc;

import java.util.Date;
import java.util.HashMap;

import net.dv8tion.jda.api.entities.MessageChannel;

public class ChannelManager {
	private static ChannelManager INSTANCE;

	private HashMap<String, Date> data = new HashMap<String, Date>();
	private HashMap<String, MessageChannel> cdata = new HashMap<String, MessageChannel>();
	private HashMap<String, String> edata = new HashMap<String, String>();
	private HashMap<String, String> fdata = new HashMap<String, String>();

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

	public void setCData(String channelId, MessageChannel messageChannel) {
		cdata.put(channelId, messageChannel);
	}

	public void setEData(String channelId, String str) {
		edata.put(channelId, str);
	}

	public void setFData(String channelId, String str) {
		fdata.put(channelId, str);
	}

	public Date getDate(String id) {
		return data.get(id);
	}

	public MessageChannel getCDate(String id) {
		return cdata.get(id);
	}

	public String getEDate(String id) {
		return edata.get(id);
	}

	public String getFDate(String id) {
		return fdata.get(id);
	}

}
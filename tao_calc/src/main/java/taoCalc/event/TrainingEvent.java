package taoCalc.event;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TrainingEvent extends MessageEvent {

	static final String TAO_ID = "526620171658330112";
	static final String OK_REPLY = "::t a";

	public void onMessageReceived(MessageReceivedEvent event) {

		if (!event.getAuthor().getId().equals(TAO_ID)) {
			return;
		}

		if (event.getMessage().getReferencedMessage().getContentRaw().startsWith(OK_REPLY)) {
			if (event.getMessage().getEmbeds().isEmpty()) {
				return;
			}

			Pattern p = Pattern.compile("『(.*)』");
			Matcher m = p.matcher(event.getMessage().getEmbeds().get(0).getDescription());
			if (m.find()) {
				List<Jukugo> jukugoList = new ArrayList<Jukugo>();
				try {
					ObjectMapper mapper = new ObjectMapper();
					jukugoList = mapper.readValue(new File("answer.json"),
							new TypeReference<List<Jukugo>>() {
							});
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				String ans = "";
				for (Jukugo jukugo : jukugoList) {
					if (jukugo.jukugo.equals(m.group().replace("『", "").replace("』", ""))) {
						ans = jukugo.yomi;
					}
				}
				if (ans != null && !ans.isEmpty()) {
					event.getChannel().sendMessage("「" + ans + "」だろ。知ってる。").queue();
				}else {
					event.getChannel().sendMessage("<@300984197634588672> 知らない問題でたぞ").queue();
				}
			}
		}
	}

}

class JukugoList {
	public List<Jukugo> list = new ArrayList<Jukugo>();
}

class Jukugo {
	public String jukugo = "";
	public String yomi = "";
}

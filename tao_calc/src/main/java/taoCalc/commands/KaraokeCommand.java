package taoCalc.commands;

import java.time.temporal.ChronoUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

import net.dv8tion.jda.api.EmbedBuilder;

/**
 *
 * @author John Grosh (jagrosh)
 */
@CommandInfo(name = { "Ping", "Pong" }, description = "Checks the bot's latency")
@Author("John Grosh (jagrosh)")
public class KaraokeCommand extends Command {

	public KaraokeCommand() {
		this.name = "ping";
		this.help = "ping";
		this.guildOnly = false;
		this.aliases = new String[] { "pong" };
	}

	@Override
	protected void execute(CommandEvent event) {
		event.reply("Ping: ...", m -> {
			long ping = event.getMessage().getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS);
			//            m.editMessage("Ping: " + ping  + "ms | Websocket: " + event.getJDA().getGatewayPing() + "ms").queue();
			m.editMessageEmbeds(new EmbedBuilder().setTitle("応答速度")
					.setDescription("Ping: " + ping + "ms | Websocket: " + event.getJDA().getGatewayPing() + "ms")
					.build()).queue();
		});
	}
	

}

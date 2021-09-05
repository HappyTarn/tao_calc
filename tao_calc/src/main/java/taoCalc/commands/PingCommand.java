package taoCalc.commands;

import java.time.temporal.ChronoUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class PingCommand extends Command {

	public PingCommand() {
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
			m.editMessage(new EmbedBuilder().setTitle("応答速度")
					.setDescription("Ping: " + ping + "ms | Websocket: " + event.getJDA().getGatewayPing() + "ms")
					.build()).queue();
		});
	}
	

}

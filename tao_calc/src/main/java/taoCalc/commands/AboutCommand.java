package taoCalc.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class AboutCommand extends Command {

	public AboutCommand() {
		this.name = "about";
		this.help = "about";
		this.guildOnly = false;
	}

	@Override
	protected void execute(CommandEvent event) {


	}

}

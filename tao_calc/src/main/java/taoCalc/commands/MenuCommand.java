package taoCalc.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class MenuCommand extends Command {

	public MenuCommand() {
		this.name = "menu";
		this.help = "メニュー";
		this.guildOnly = false;
		this.aliases = new String[] { "m" };
	}

	@Override
	protected void execute(CommandEvent event) {

		SelectionMenu menu = SelectionMenu.create("menu:class")
				.setPlaceholder("実行するコマンドを選んでください") // shows the placeholder indicating what this menu is for
				.setRequiredRange(1, 1) // only one can be selected
				.addOption("保有経験値", "se")
				.addOption("ペット情報", "sp")
				.build();

		event.getMessage().reply("実行するコマンドを選んでください。")
				.setActionRow(menu)
				.queue();
	}

}

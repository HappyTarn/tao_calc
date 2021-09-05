package taoCalc.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.Permission;
import taoCalc.db.Sqlite;

public class initCommand extends Command {

	public initCommand() {
		this.name = "init";
		this.help = "初期設定";
		this.guildOnly = true;
        this.ownerCommand = false;
        this.userPermissions = new Permission[] {Permission.ADMINISTRATOR};
	}

	@Override
	protected void execute(CommandEvent event) {
		String guildId = event.getGuild().getId();
		try {
			Files.deleteIfExists(Paths.get("db/" + guildId + ".db"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Sqlite.init(guildId);
		event.reply("設定を初期化しました。");
	}

}

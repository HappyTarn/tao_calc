package taoCalc.commands;

import java.util.ArrayList;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import taoCalc.db.Sqlite;

public class DbCommand extends Command {

	public DbCommand() {
		this.name = "db";
		this.help = "dbへのアクセス";
		this.guildOnly = false;
		this.ownerCommand = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		long startTime = System.currentTimeMillis();
		if (event.getArgs().isEmpty()) {
			return;
		}
		String[] args = event.getArgs().split("::");
		if (args.length == 2) {
			ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
			if (args[1].startsWith("select")) {
				dataList = Sqlite.getResultexecuteSql(args[0], args[1]);
			} else {
				Sqlite.executeSqlNotResult(args[0], args[1]);
			}
			StringBuilder sb = new StringBuilder();
			for (ArrayList<String> row : dataList) {
				for (String data : row) {
					sb.append(data + "|");
				}
				sb.append("\n");
			}
			event.getMessage().reply(sb.toString()).queue();
			;
		} else if (args.length == 1) {
			ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
			if (args[0].startsWith("select")) {
				dataList = Sqlite.getResultexecuteSql(event.getGuild().getId(), args[0]);
			} else {
				Sqlite.executeSqlNotResult(event.getGuild().getId(), args[0]);
			}
			StringBuilder sb = new StringBuilder();
			for (ArrayList<String> row : dataList) {
				for (String data : row) {
					sb.append(data + "|");
				}
				sb.append("\n");
			}
			long endTime = System.currentTimeMillis();
			event.getMessage().reply("処理時間：" + (endTime - startTime) + " ms\n" + sb.toString()).queue();
			;
		}
	}

}

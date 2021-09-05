package taoCalc.commands;

import java.util.ArrayList;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import taoCalc.Const;
import taoCalc.db.Sqlite;
import taoCalc.dto.MonsterRate;
import taoCalc.dto.Rate;

public class ChangeRateCommand extends Command {

	public ChangeRateCommand() {
		this.name = "changeRate";
		this.help = "レートを変更（「経験値管理係」ロールが付与サれている人のみ）";
		this.arguments = "レア=5 激レア=30 超激レア=100 ...";
		this.guildOnly = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		
		String guildId = event.getGuild().getId();
		boolean isExec = false;
		if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
			isExec = true;
		}else {
			String roleId = Sqlite.getRole(guildId, Const.経験値管理係);
			if (roleId.isEmpty()) {
				for (Role role : event.getMember().getRoles()) {
					if (role.getName().equals("経験値管理係")) {
						isExec = true;
					}
				}
			} else {
				for (Role role : event.getMember().getRoles()) {
					if (role.getId().equals(roleId)) {
						isExec = true;
					}
				}
			}
		}
		
		if(!isExec) {
			event.getMessage().reply("権限ないぞ").queue();
			return;
		}

		List<String> rankList = new ArrayList<String>();
		rankList.add(Const.通常);
		rankList.add(Const.強敵);
		rankList.add(Const.シリーズ);
		rankList.add(Const.弱敵);
		rankList.add(Const.レア);
		rankList.add(Const.激レア);
		rankList.add(Const.超激レア);
		rankList.add(Const.鯖限);
		rankList.add(Const.tohru);
		rankList.add(Const.超強敵);

		if (event.getArgs().isEmpty()) {
			event.reply("レートを設定してください。（詳細はhelp）");
			return;
		}

		for (String str : event.getArgs().split("\\s+")) {
			if (str.split("=").length != 2) {
				event.reply("正しくないレート設定があります。（" + str + "）");
				return;
			}
			String rank = str.split("=")[0];
			String amount = str.split("=")[1];

			if (!amount.chars().allMatch(Character::isDigit)) {
				event.reply("正しくないレート設定があります。（" + str + "）");
				return;
			}

			if (!rankList.contains(rank)) {
				if (Sqlite.countSqliteMasterByTableName(event.getGuild().getId(), "monster_rate_info") == 0) {
					Sqlite.executeSql(event.getGuild().getId(),
							"create table monster_rate_info(name,amount)");
				}
				Sqlite.deleteAndInsert(guildId,
						"delete from monster_rate_info where name ='" + rank + "';",
						"insert into monster_rate_info(name,amount) values('" + rank + "','" + amount + "');");
			}else {
				Sqlite.deleteAndInsert(guildId,
						"delete from rate where rank ='" + rank + "';",
						"insert into rate(rank,amount) values('" + rank + "','" + amount + "');");
			}
			
		}
		List<Rate> rateList = Sqlite.selectRate(guildId, "select * from rate order by amount");
		List<MonsterRate> monsterRateList = Sqlite.selectMonsterRate(guildId, "select * from monster_rate_info order by amount");

		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("現在のレート設定");
		for (Rate rate : rateList) {
			if(rate.getAmount() > 0) {
				eb.addField("ランク：" + rate.getRank(), rate.getFormatAmount(), false);
			}
		}
		for (MonsterRate rate : monsterRateList) {
			if(rate.getAmount() > 0) {
				eb.addField("敵名：" + rate.getName(),rate.getFormatAmount(), false);
			}
		}
		event.reply(eb.build());
	}

}

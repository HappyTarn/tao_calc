package taoCalc.commands;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import taoCalc.db.Sqlite;
import taoCalc.dto.MonsterRate;
import taoCalc.dto.PrizeMoneyInfo;
import taoCalc.dto.Rate;

public class ShowRateCommand extends Command {

	public ShowRateCommand() {
		this.name = "showRate";
		this.help = "現在のレートを表示";
		this.guildOnly = true;
		this.aliases = new String[] { "showRate","showrate","Showrate","sr" };
	}

	@Override
	protected void execute(CommandEvent event) {
		String guildId = event.getGuild().getId();
		List<Rate> rateList = Sqlite.selectRate(guildId, "select * from rate order by amount");
		List<MonsterRate> monsterRateList = Sqlite.selectMonsterRate(guildId, "select * from monster_rate_info order by amount");
		List<PrizeMoneyInfo> prizeMoneyInfoList = Sqlite.selectPrizeMoneyInfo(guildId ,"select * from prize_money_info order by exp");

		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("現在のレート設定");
		int count = 1;
		for (Rate rate : rateList) {
			if(rate.getAmount() > 0) {
				eb.addField("ランク：" +rate.getRank(),rate.getFormatAmount(), false);
				count++;
				if (count % 25 == 0) {
					event.reply(eb.build());
					eb.clear();
					eb.setTitle("現在のレート設定");
				}
			}
		}
		for (MonsterRate rate : monsterRateList) {
			if(rate.getAmount() > 0) {
				eb.addField("敵名：" + rate.getName(),rate.getFormatAmount(), false);
				count++;
				if (count % 25 == 0) {
					event.reply(eb.build());
					eb.clear();
					eb.setTitle("現在のレート設定");
				}
			}
		}
		
		for (PrizeMoneyInfo pmi : prizeMoneyInfoList) {
			if(pmi.getExp() > 0) {
				eb.addField("指名手配(残高)：" + pmi.getName(),pmi.getFormatExp(), false);
				count++;
				if (count % 25 == 0) {
					event.reply(eb.build());
					eb.clear();
					eb.setTitle("現在のレート設定");
				}
			}
		}
		
		if(!eb.getFields().isEmpty()) {
			event.reply(eb.build());
		}
	}

}

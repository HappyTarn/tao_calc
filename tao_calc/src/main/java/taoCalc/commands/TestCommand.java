package taoCalc.commands;

import java.text.NumberFormat;
import java.util.Random;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import taoCalc.Const;
import taoCalc.db.Sqlite;
import taoCalc.dto.Member;
import taoCalc.dto.PrizeMoneyInfo;
import taoCalc.util.Utility;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class TestCommand extends Command {

	public TestCommand() {
		this.name = "test";
		this.help = "test";
		this.guildOnly = false;
		this.ownerCommand = true;
	}

	@Override
	protected void execute(CommandEvent event) {

		if (event.getArgs().equals("1")) {

			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("属性:[1周年] ランク:【ありがとう！】\r\n"
					+ "............が待ち構えている...！\r\n"
					+ "Lv.10,150  HP: 101,550 素早さ: 400");
			eb.setImage(
					"https://media.discordapp.net/attachments/623052991838289930/661411050461331466/image0.jpg?width=372&height=662");
			event.getMessage().reply(eb.build()).queue();
		} else if (event.getArgs().equals("2")) {

			event.getMessage().reply("```diff\n- 属性:[闇] ランク:【弱敵】\n"
					+ "- 闇の石が待ち構えている...！\n"
					+ "- Lv.230,327  HP: 2,303,320 素早さ: 50```"
					+ "```diff\n+ スキル発動！闇の石は5,048,186ダメの追加攻撃を受けた！```"
					+ "闇の石を倒した！\n"
					+ "```diff\n- 属性:[陰] ランク:【通常】\n"
					+ "- 花嫁の亡霊が待ち構えている...！\n"
					+ "- Lv.230,328  HP: 2,303,330 素早さ: 100```"
					+ "```diff\n+ スキル発動！花嫁の亡霊は5,048,186ダメの追加攻撃を受けた！```"
					+ "闇の石を倒した！\n").queue();
		} else if (event.getArgs().equals("3")) {

			event.getMessage().reply("```diff\n- 属性:[闇] ランク:【弱敵】\n"
					+ "- 闇の石が待ち構えている...！\n"
					+ "- Lv.230,327  HP: 2,303,320 素早さ: 50```"
					+ "```diff\n+ スキル発動！闇の石は5,048,186ダメの追加攻撃を受けた！```"
					+ "闇の石を倒した！").queue();
		} else if (event.getArgs().equals("3")) {

			EmbedBuilder eb = new EmbedBuilder();
			eb.setDescription(""
					+ "@パトはTAOがしたいの仲間にしました！\n"
					+ "名前:\n"
					+ "ウィングベアー　クイナ　\n"
					+ "攻撃確率:\n"
					+ "27%\n"
					+ "状態:\n"
					+ "ロックされていない。\n"
					+ "特殊能力:\n"
					+ "SRレア | 能力:77777ダメを確率で出す");
			event.getMessage().reply(eb.build()).queue();
		} else if (event.getArgs().equals("4")) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("属性:[ふーんえっちじゃん] ランク:【超激レア】\r\n"
					+ "りやるごテンペストが待ち構えている...！\r\n"
					+ "Lv.4,034,751  HP: 40,347,560 素早さ: 50");
			eb.setImage(
					"https://media.discordapp.net/attachments/859205662441209897/871363870676115456/ea18237a67b1851bf992d4878214d40f.png?width=115&height=115");
			event.getMessage().reply(eb.build()).queue();
		} else if (event.getArgs().equals("iro")) {
			String memberId = "695257922854453358";
			if(!event.getAuthor().getId().equals(memberId)) {
				return;
			}
			String guildId = event.getGuild().getId();
			String monsterName = "The=One (王)";

			PrizeMoneyInfo pmi = Sqlite.selectPrizeMoneyInfoByName(guildId, monsterName);
			Long prizeMoney = 0L;
			if (!(pmi.getExp() == 1000L)) {
				return;
			}
			if (pmi != null && pmi.getExp() != 0) {
				Random rand = new Random();
				if (pmi.getExp() > 10) {
					switch (rand.nextInt(100) + 1) {
					case 1:
						prizeMoney = pmi.getExp();
						break;
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
						prizeMoney = (long) (rand.nextInt(pmi.getExp().intValue()) + 1);
						break;
					default:
						prizeMoney = (long) (rand.nextInt(10) + 1);
						break;
					}
				} else {
					prizeMoney = (long) (rand.nextInt(pmi.getExp().intValue()) + 1);
				}
				pmi.setExp(pmi.getExp() - prizeMoney);
				Sqlite.updatePrizeMoneyInfo(guildId, pmi);
			}

			Member member = Sqlite.selectMemberById(guildId, memberId);
			String rank = Const.tohru;
			Long before = 0L;
			Long after = 0L;
			before = member.getExp();
			member.addExp(prizeMoney.longValue());
			member.addRank(rank);
			Sqlite.updateMember(guildId, member);
			after = member.getExp();

			EmbedBuilder eb = new EmbedBuilder();
			NumberFormat nfNum = NumberFormat.getNumberInstance();
			eb.setTitle("経験値を付与しました。");

			if (prizeMoney != 0) {
				eb.addField("懸賞金獲得", Utility.convertCommaToStr(prizeMoney.longValue()), false);
			}

			eb.addField(event.getAuthor().getName(),
					nfNum.format(before) + " -> " + nfNum.format(after), false);
			event.getMessage().reply(eb.build()).queue();

		}

	}

}

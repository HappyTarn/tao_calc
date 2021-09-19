package taoCalc.commands;

import java.util.ArrayList;

import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

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
					+ "【テスト】............が待ち構えている...！\r\n"
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
		} else if (event.getArgs().equals("5")) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("どこで出た？");
			event.getMessage().reply(eb.build()).setActionRow(Button.of(ButtonStyle.PRIMARY,"tcmto 公式", "公式"),
					Button.of(ButtonStyle.PRIMARY,"tcmto DW", "DW"),
					Button.of(ButtonStyle.PRIMARY,"tcmto KING", "KING"),
					Button.of(ButtonStyle.PRIMARY,"tcmto もえにょ", "もえにょ")).queue();
		}else if(event.getArgs().equals("6")) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("超激レアが出たよ！");
			event.getMessage().reply(eb.build()).setActionRow(Button.of(ButtonStyle.SUCCESS,"removeRole", "発言不可解除",Emoji.fromUnicode("U+1F91E")),
					Button.of(ButtonStyle.PRIMARY,"tcmt", "通知"),Button.of(ButtonStyle.DANGER,"tcmt_no", "通知しない")).queue();
			
		}else if(event.getArgs().equals("7")) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("超激レアが出たよ！");
			ActionRow s1 = ActionRow.of(Button.of(ButtonStyle.SUCCESS,"removeRole1", "発言不可解除",Emoji.fromUnicode("U+1F91E")),
					Button.of(ButtonStyle.PRIMARY,"tcmt1", "通知1"),Button.of(ButtonStyle.DANGER,"tcmt_no1", "通知しない"));
			ActionRow s2 = ActionRow.of(Button.of(ButtonStyle.SUCCESS,"removeRole", "発言不可解除",Emoji.fromUnicode("U+1F91E")),
					Button.of(ButtonStyle.PRIMARY,"tcmt", "通知2"),Button.of(ButtonStyle.DANGER,"tcmt_no", "通知しない"));
			event.getMessage().reply(eb.build()).setActionRows(s1,s2).queue();
		}else if(event.getArgs().equals("8")) {
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setDescription(">>> <@300984197634588672>さん...セルフBOT検知しました。\n問答無用で永久BANです＾＾");
			event.getChannel().sendMessage(eb.build()).queue();
		}else if (event.getArgs().equals("9")) {
			
			ArrayList<Page> pages = new ArrayList<>();

			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Example Embed");
			eb.setDescription("Hello World!");
			//Adding 10 pages to the list
			for (int i = 0; i < 10; i++) {
				eb.clear();
				eb.setDescription("This is entry Nº " + i);
				pages.add(new Page(eb.build()));
			}
			
			event.getChannel().sendMessage((MessageEmbed) pages.get(0).getContent()).queue(success -> {
				Pages.paginate(success,pages);
			});
		}

	}

}

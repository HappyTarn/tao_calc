package taoCalc.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.ygimenez.method.Pages;
import com.github.ygimenez.model.Page;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import taoCalc.db.Sqlite;
import taoCalc.dto.RaidInfo;
import taoCalc.dto.RaidMemberInfo;
import taoCalc.util.Utility;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class RaidCommand extends Command {

	public RaidCommand() {
		this.name = "raid";
		this.help = "raidboss情報";
		this.arguments = "レイドボスNo";
		this.guildOnly = false;
	}

	@Override
	protected void execute(CommandEvent event) {

		String guildId = event.getGuild().getId();
		RaidInfo raidInfo;
		if (event.getArgs().isEmpty()) {
			raidInfo = Sqlite.selectRaidInfoByValid(guildId);
			if (raidInfo == null) {
				event.getMessage().reply("表示するデータがありません。").queue();
				return;
			}
		} else {
			Pattern p = Pattern.compile("([0-9]+)");
			Matcher m = p.matcher(event.getArgs().split(" ")[0]);
			if (!m.find()) {
				event.getMessage().reply("引数が正しくないです（" + event.getArgs() + "）").queue();
				return;
			}
			Integer raidNo = Integer.parseInt(event.getArgs().split(" ")[0]);
			raidInfo = Sqlite.selectRaidInfoByNo(guildId, raidNo);
			if (raidInfo == null) {
				event.getMessage().reply("表示するデータがありません。").queue();
				return;
			}
		}

		event.getMessage().replyEmbeds(Utility.createRaidBoss(raidInfo).build()).queue();

		List<RaidMemberInfo> list = Sqlite.selectRaidMembers(guildId, raidInfo.getRaidNo());
		if(list.isEmpty()) {
			return;
		}
		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor("レイドボスNo." + raidInfo.getRaidNo() + " ランキング");
		ArrayList<Page> pages = new ArrayList<>();
		int count = 1;
		for (RaidMemberInfo s : list) {
			String name = "(" + s.getMemberId() + ")確認中...";
			User m = event.getJDA().getUserById(s.getMemberId());
			if (m != null) {
				name = m.getAsTag();
			}
			if ((count - 1) % 10 == 0 && count != 1) {
				eb.setFooter(count / 10 + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
				pages.add(new Page(eb.build()));
				eb.clear();
				eb.setAuthor("レイドボスNo." + raidInfo.getRaidNo() + " ランキング");
			}

			eb.appendDescription(count + "位 `" + name + "` **" + Utility.convertCommaToStr(Double.parseDouble(s.getDamage())) + "ダメ**\n");
			count++;
		}
		eb.setFooter((list.size() / 10 + 1) + "ページ/" + (list.size() / 10 + 1) + "ページ目を表示中");
		pages.add(new Page(eb.build()));
		event.getChannel().sendMessageEmbeds((MessageEmbed) pages.get(0).getContent()).queue(success -> {
			Pages.paginate(success, pages);
		});
	}

}

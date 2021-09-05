package taoCalc.commands;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import taoCalc.db.Sqlite;
import taoCalc.dto.Member;

public class ShowExpCommand extends Command {

	public ShowExpCommand() {
		this.name = "showExp";
		this.help = "現在の保有経験値を表示";
		this.arguments = "all or @mention";
		this.guildOnly = true;
		this.ownerCommand = false;
		this.aliases = new String[] { "showexp", "Showexp", "ShowExp", "se" };
	}

	@Override
	protected void execute(CommandEvent event) {

		String guildId = event.getGuild().getId();
		String memberId = event.getAuthor().getId();
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("保有経験値");
		if (event.getArgs().isEmpty()) {

			Member member = Sqlite.selectMemberById(guildId, memberId);
			if (member != null) {
				eb.appendDescription(String.format("> %s ： <@%s>\n", member.getFormatExp(), member.getId()));
				eb.appendDescription(member.get割合());
				if (member.getレア率() > 2) {
					eb.setFooter("こいつやってますよ！！！（多分）");
				} else if (member.getレア率() > 1) {
					eb.setFooter("MMO君使ってないよね？");
				}
			} else {
				eb.addField(event.getAuthor().getName(), "0", false);
			}
			event.getMessage().reply(eb.build()).queue();
		} else if (event.getArgs().equals("all")) {
			List<Member> memberList = Sqlite.selectMemberOrderByExpDesc(guildId);
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("保有経験値");

			int count = 1;
			for (Member member : memberList) {
				if (count % 20 == 0) {
					event.getMessage().reply(embedBuilder.build()).queue();
					embedBuilder.clear();
					embedBuilder.setTitle("保有経験値");
				}
				embedBuilder.appendDescription(
						String.format("> %s ： <@%s> | 最終更新 ： %s \n", member.getFormatExp(), member.getId(),
								member.getUpdateDate()));
				count++;
			}
			event.getMessage().reply(embedBuilder.build()).queue();
		} else if (event.getArgs().equals("sall")) {
			List<Member> memberList = Sqlite.selectMemberOrderBySubjugation(guildId);
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("討伐数一覧");

			int count = 1;
			for (Member member : memberList) {
				if (count % 20 == 0) {
					event.getMessage().reply(embedBuilder.build()).queue();
					embedBuilder.clear();
					embedBuilder.setTitle("討伐数一覧");
				}
				embedBuilder.appendDescription(
						String.format("> %s ： <@%s> \n", member.get合計(), member.getId()));
				count++;
			}
			event.getMessage().reply(embedBuilder.build()).queue();
		} else if (event.getArgs().split(" ").length > 0) {
			Pattern p = Pattern.compile("([0-9]+)");
			Matcher m = p.matcher(event.getArgs().split(" ")[0]);
			if (!m.find()) {
				event.getMessage().reply("引数が正しくないです（" + event.getArgs() + "）").queue();
				return;
			}
			Member member = Sqlite.selectMemberById(guildId, m.group());
			if (member == null) {
				event.getMessage().reply("保有経験値はないみたい").queue();
				return;
			} else {
				eb.appendDescription(String.format("> %s ： <@%s>\n", member.getFormatExp(), member.getId()));
				eb.appendDescription(member.get割合());
				if (member.getレア率() > 2) {
					eb.setFooter("こいつやってますよ！！！（多分）");
				} else if (member.getレア率() > 1) {
					eb.setFooter("MMO君使ってないよね？");
				}
				event.getMessage().reply(eb.build()).queue();
			}
		}

	}

}

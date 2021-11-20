package taoCalc.commands;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import taoCalc.Const;
import taoCalc.db.Sqlite;
import taoCalc.dto.Member;
import taoCalc.util.Utility;

public class ShowExpCommand extends Command {

	public ShowExpCommand() {
		this.name = "showExp";
		this.help = "現在の保有経験値、討伐数を表示";
		this.arguments = "[all,sall,pall,@mention]";
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
			event.getMessage().replyEmbeds(eb.build()).queue();
		} else if (event.getArgs().equals("all")) {
			List<Member> memberList = Sqlite.selectMemberOrderByExpDesc(guildId);
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("保有経験値");

			int count = 1;
			Long sum = 0L;
			for (Member member : memberList) {
				if (count % 20 == 0) {
					event.getMessage().replyEmbeds(embedBuilder.build()).queue();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					embedBuilder.clear();
					embedBuilder.setTitle("保有経験値");
				}
				embedBuilder.appendDescription(
						String.format("> %s ： <@%s> | 最終更新 ： %s \n", member.getFormatExp(), member.getId(),
								member.getUpdateDate()));
				sum = sum + member.getExp();
				count++;
			}
			event.getMessage().replyEmbeds(embedBuilder.build()).queue();
			embedBuilder.clear();
			embedBuilder.setTitle("保有経験値");
			embedBuilder.appendDescription(
					String.format("> %s ： %s \n", Utility.convertCommaToStr(sum), "全員の合計"));
			event.getMessage().replyEmbeds(embedBuilder.build()).queue();
		} else if (event.getArgs().equals("sall")) {
			List<Member> memberList = Sqlite.selectMemberOrderBySubjugation(guildId);
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("討伐数一覧");

			int count = 1;
			Long sum = 0L;
			for (Member member : memberList) {
				if (member.get合計() == 0) {
					continue;
				}
				if (count % 20 == 0) {
					event.getMessage().replyEmbeds(embedBuilder.build()).queue();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					embedBuilder.clear();
					embedBuilder.setTitle("討伐数一覧");
				}
				embedBuilder.appendDescription(
						String.format("> %s ： <@%s> \n", member.get合計(), member.getId()));
				sum = sum + member.get合計();
				count++;
			}
			event.getMessage().replyEmbeds(embedBuilder.build()).queue();
			embedBuilder.clear();
			embedBuilder.setTitle("討伐数一覧");
			embedBuilder.appendDescription(
					String.format("> %s ： %s \n", Utility.convertCommaToStr(sum), "全員の合計"));
			event.getMessage().replyEmbeds(embedBuilder.build()).queue();

		} else if (event.getArgs().equals("pall")) {
			List<Member> memberList = Sqlite.selectMemberOrderBySubjugation(guildId);
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("討伐数一覧（％）");

			int count = 1;
			Long sum = 0L;
			for (Member member : memberList) {
				sum = sum + member.get合計();
			}
			for (Member member : memberList) {
				if (member.get合計() == 0) {
					continue;
				}
				if (count % 20 == 0) {
					event.getMessage().replyEmbeds(embedBuilder.build()).queue();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					embedBuilder.clear();
					embedBuilder.setTitle("討伐数一覧（％）");
				}
				embedBuilder.appendDescription(
						String.format("> %s％  ： <@%s> \n", ((float) member.get合計() / (float) sum * 100),
								member.getId()));
				count++;
			}
			event.getMessage().replyEmbeds(embedBuilder.build()).queue();
		} else if (event.getArgs().equals("all reset")) {
			boolean isExec = false;
			if(event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
				isExec = true;
			}else {
				String roleId = Sqlite.getRole(guildId, Const.経験値変更係);
				if (roleId.isEmpty()) {
					for (Role role : event.getMember().getRoles()) {
						if (role.getName().equals("経験値変更係")) {
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
			List<Member> memberList = Sqlite.selectMemberOrderByExpDesc(guildId);
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("保有経験値");

			int count = 1;
			Long sum = 0L;
			for (Member member : memberList) {
				if (count % 20 == 0) {
					event.getMessage().replyEmbeds(embedBuilder.build()).queue();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					embedBuilder.clear();
					embedBuilder.setTitle("保有経験値");
				}
				embedBuilder.appendDescription(
						String.format("> %s ： <@%s> | 最終更新 ： %s \n", member.getFormatExp(), member.getId(),
								member.getUpdateDate()));
				sum = sum + member.getExp();
				count++;
			}
			event.getMessage().replyEmbeds(embedBuilder.build()).queue();
			embedBuilder.clear();
			embedBuilder.setTitle("保有経験値");
			embedBuilder.appendDescription(
					String.format("> %s ： %s \n", Utility.convertCommaToStr(sum), "全員の合計"));
			event.getMessage().replyEmbeds(embedBuilder.build()).queue();
			event.getMessage().reply("全員の保有経験値をリセットしますか？")
					.setActionRow(Button.of(ButtonStyle.SUCCESS, "seallreset", "リセット"),
							Button.of(ButtonStyle.DANGER, "cancel", "キャンセル"))
					.queue();
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
				event.getMessage().replyEmbeds(eb.build()).queue();
			}
		}

	}

}

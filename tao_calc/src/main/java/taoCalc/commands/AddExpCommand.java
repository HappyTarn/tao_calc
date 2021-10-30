package taoCalc.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import taoCalc.Const;
import taoCalc.db.Sqlite;
import taoCalc.dto.Member;

public class AddExpCommand extends Command {

	public AddExpCommand() {
		this.name = "addExp";
		this.help = "経験値を追加【要[経験値変更係]ロール】";
		this.arguments = "[@mention exp]";
		this.guildOnly = true;
		this.ownerCommand = false;
		this.aliases = new String[] { "addexp", "Addexp", "AddExp","ae" };
	}

	@Override
	protected void execute(CommandEvent event) {
		String guildId = event.getGuild().getId();
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
		
		
		if (event.getArgs().isEmpty()) {
			event.getMessage().reply("引数が正しくないです").queue();
			return;
		} else if (event.getArgs().split(" ").length != 2) {
			event.getMessage().reply("引数が正しくないです（" + event.getArgs() + "）").queue();
			return;
		} else if (event.getArgs().split(" ")[0].length() < 10) {
			event.getMessage().reply("引数が正しくないです（" + event.getArgs() + "）").queue();
			return;
		}
		Long addExp;
		try {
			addExp = Long.parseLong(event.getArgs().split(" ")[1]);
		} catch (Exception e) {
			event.getMessage().reply("追加する経験値が数値ではありません。").queue();
			return;
		}

		Pattern p = Pattern.compile("([0-9]+)");
		Matcher m = p.matcher(event.getArgs().split(" ")[0]);
		if (!m.find()) {
			event.getMessage().reply("引数が正しくないです（" + event.getArgs() + "）").queue();
			return;
		}
		Member member = Sqlite.selectMemberById(guildId, m.group());

		if (member == null) {
			member = new Member(m.group(), addExp);
			Sqlite.insertMember(guildId, member);
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("経験値追加確認");
			eb.setDescription("以下の通り保有経験値を追加しました。");
			eb.addField("変更前", "0", false);
			eb.addField("変更後", member.getFormatExp(), false);

			event.getMessage().replyEmbeds(eb.build()).queue();
			return;
		}

		String beforExp = member.getFormatExp();
		member.addExp(addExp);
		Sqlite.updateMember(guildId, member);
		String afterExp = member.getFormatExp();
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("経験値追加確認");
		eb.setDescription("以下の通り保有経験値を追加しました。");
		eb.addField("変更前", beforExp, false);
		eb.addField("変更後", afterExp, false);

		event.getMessage().replyEmbeds(eb.build()).queue();
	}

}

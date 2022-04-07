package taoCalc.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import taoCalc.Const;
import taoCalc.dto.RaidInfo;

public class Utility {

	public static String getLinkURL(String guildId, String channelId, String messageId) {
		return "https://discord.com/channels/" + guildId + "/" + channelId + "/" + messageId;
	}

	public static Long convertCommmaToLong(String value) {
		Number number;
		try {
			number = NumberFormat.getInstance().parse(value);
		} catch (ParseException e) {
			number = 0;
		}
		return number.longValue();
	}

	public static double convertCommmaToDouble(String value) {
		Number number;
		try {
			number = NumberFormat.getInstance().parse(value);
		} catch (ParseException e) {
			number = 0;
		}
		return number.doubleValue();
	}

	public static String convertCommaToStr(Long value) {
		NumberFormat nfNum = NumberFormat.getNumberInstance();
		return nfNum.format(value);
	}

	public static String convertCommaToStr(Double value) {
		NumberFormat nfNum = NumberFormat.getNumberInstance();
		return nfNum.format(value);
	}

	public static String convertKanjiNum(Long value) {
		String totalValue = value.toString();

		StringBuilder sb = new StringBuilder();
		if (totalValue.length() <= 4) {
			sb.append(totalValue);
		} else if (totalValue.length() <= 8) {
			String sen = totalValue.substring(totalValue.length() - 4);
			String man = totalValue.substring(0, totalValue.length() - sen.length());
			sb.append(man + "万" + sen);
		} else if (totalValue.length() <= 12) {
			String sen = totalValue.substring(totalValue.length() - 4);
			String man = totalValue.substring(totalValue.length() - 8, totalValue.length() - sen.length());
			String oku = totalValue.substring(0, totalValue.length() - sen.length() - man.length());
			sb.append(oku + "億" + man + "万" + sen);
		} else if (totalValue.length() <= 16) {
			String sen = totalValue.substring(totalValue.length() - 4);
			String man = totalValue.substring(totalValue.length() - 8, totalValue.length() - sen.length());
			String oku = totalValue.substring(totalValue.length() - 12,
					totalValue.length() - sen.length() - man.length());
			String cho = totalValue.substring(0, totalValue.length() - sen.length() - man.length() - oku.length());
			sb.append(cho + "兆" + oku + "億" + man + "万" + sen);
		} else if (totalValue.length() <= 20) {
			String sen = totalValue.substring(totalValue.length() - 4);
			String man = totalValue.substring(totalValue.length() - 8, totalValue.length() - sen.length());
			String oku = totalValue.substring(totalValue.length() - 12,
					totalValue.length() - sen.length() - man.length());
			String cho = totalValue.substring(totalValue.length() - 16,
					totalValue.length() - sen.length() - man.length() - oku.length());
			String kei = totalValue.substring(0,
					totalValue.length() - sen.length() - man.length() - oku.length() - cho.length());
			sb.append(kei + "京" + cho + "兆" + oku + "億" + man + "万" + sen);
		}
		return sb.toString();
	}

	public static String getId(String arg) {
		Pattern p = Pattern.compile("([0-9]+)");
		Matcher m = p.matcher(arg);
		return m.find() ? m.group() : "";
	}

	public static String getRank(String rank, String speed) {

		if (Const.通常.equals(rank)) {
			if (speed.equals("100")) {
				return Const.鯖限;
			}
			return Const.通常;
		} else if (Const.弱敵.equals(rank)) {
			return Const.弱敵;
		} else if (Const.強敵.equals(rank)) {
			return Const.強敵;
		} else if (Const.超強敵.equals(rank)) {
			return Const.超強敵;
		} else if (Const.シリーズ.equals(rank)) {
			return Const.シリーズ;
		} else if (Const.レア.equals(rank)) {
			return Const.レア;
		} else if (Const.激レア.equals(rank)) {
			return Const.激レア;
		} else if (Const.超激レア.equals(rank)) {
			return Const.超激レア;
		}
		return null;
	}

	public static String raidBossHP(String rank, String hp) {

		double hpd = Utility.convertCommmaToDouble(hp);

		String result = "";

		if (Const.通常.equals(rank)) {
			result = Utility.convertCommaToStr(hpd * 200000);
		} else if (Const.弱敵.equals(rank)) {
			result = Utility.convertCommaToStr(hpd * 100000);
		} else if (Const.強敵.equals(rank)) {
			result = Utility.convertCommaToStr(hpd * 400000);
		} else if (Const.超強敵.equals(rank)) {
			result = Utility.convertCommaToStr(hpd * 2000000);
		} else if (Const.シリーズ.equals(rank)) {
			result = Utility.convertCommaToStr(hpd * 2000000);
		} else if (Const.レア.equals(rank)) {
			result = Utility.convertCommaToStr(hpd * 2000000);
		} else if (Const.激レア.equals(rank)) {
			result = Utility.convertCommaToStr(hpd * 4000000);
		} else if (Const.超激レア.equals(rank)) {
			result = Utility.convertCommaToStr(hpd * 5000000);
		} else {
			result = Utility.convertCommaToStr(hpd * 10000000);
		}

		return result;
	}

	public static void sendMessage(MessageChannel chan, String message) {
		chan.sendMessage(message).queue();
	}

	public static Long convertLong(String value) {
		Pattern p = Pattern.compile("([0-9]+)");
		Matcher m = p.matcher(value);
		if (!m.find()) {
			return null;
		}

		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date getFirstDate(Date date) {

		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int first = calendar.getActualMinimum(Calendar.DATE);
		calendar.set(Calendar.DATE, first);

		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.MILLISECOND, 000);

		return calendar.getTime();
	}

	public static boolean checkDate(String strDate) {
		if (strDate == null || strDate.length() != 10) {
			return false;
		} else {
			return true;
		}
		//	    strDate = strDate.replace('-', '/');
		//	    DateFormat format = DateFormat.getDateInstance();
		//	    format.setLenient(false);
		//	    try {
		//	        format.parse(strDate);
		//	        return true;
		//	    } catch (Exception e) {
		//	        return false;
		//	    }
	}

	public static EmbedBuilder createRaidBoss(RaidInfo raidInfo) {

		EmbedBuilder eb = new EmbedBuilder();
		eb.setAuthor("レイドボスNo." + raidInfo.getRaidNo());
		eb.setTitle("属性:" + raidInfo.getZokusei() + " ランク:【" + raidInfo.getRank() + "】\r\n"
				+ raidInfo.getName() + "が待ち構えている...！\r\n"
				+ "Lv.???????\r\n"
				+ "現HP: " + raidInfo.getHp() + "\r\n"
				+ "総HP: " + raidInfo.getTotalHp());

		eb.setImage(raidInfo.getURL());

		eb.setFooter(raidInfo.getLimit().isEmpty() ? "終了" : raidInfo.getLimit() + "まで");

		return eb;

	}

	public static File createItemFile(String memberId, LinkedHashMap<String, List<String>> data) {

		File file = null;
		try {
			file = new File("tmp/" + memberId + ".txt");
			PrintWriter pw = new PrintWriter(new BufferedWriter
	                (new OutputStreamWriter(new FileOutputStream(file),"Shift-JIS")));
			
			for(List<String> list : data.values()) {
				for(String value : list) {
					pw.println(value);
				}
			}
			
			pw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			file = null;
		}

		return file;
	}
}

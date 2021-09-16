package taoCalc.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.MessageChannel;
import taoCalc.Const;

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

	public static String convertCommaToStr(Long value) {
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

		if (date==null) return null;

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
	    }else {
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
}

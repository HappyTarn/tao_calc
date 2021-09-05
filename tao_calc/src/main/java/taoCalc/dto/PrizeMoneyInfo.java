package taoCalc.dto;

import java.text.NumberFormat;

public class PrizeMoneyInfo {

	private String name = "";
	private Long exp = 0L;

	public PrizeMoneyInfo(String name, Long exp) {
		this.name = name;
		this.exp = exp;
	}

	public String getFormatExp() {
		NumberFormat nfNum = NumberFormat.getNumberInstance();
		return nfNum.format(this.exp);
	}

	public String getName() {
		return name;
	}

	public Long getExp() {
		return exp;
	}

	public void setExp(Long exp) {
		this.exp = exp;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addExp(Long exp) {
		this.exp += exp;
	}

}

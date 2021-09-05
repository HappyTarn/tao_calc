package taoCalc.dto;

import java.text.NumberFormat;

public class Rate {

	private String rank = "";
	private Long amount = 0L;
	
	public Rate(String rank,Long amount) {
		this.rank = rank;
		this.amount = amount;
	}
	
	public String getFormatAmount() {
		 NumberFormat nfNum = NumberFormat.getNumberInstance();
		 return nfNum.format(this.amount);
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}
}

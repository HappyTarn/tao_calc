package taoCalc.dto;

import java.text.NumberFormat;

public class MonsterRate {

	private String name = "";
	private Long amount = 0L;
	
	public MonsterRate(String name,Long amount) {
		this.name = name;
		this.amount = amount;
	}
	
	public String getFormatAmount() {
		 NumberFormat nfNum = NumberFormat.getNumberInstance();
		 return nfNum.format(this.amount);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}
}

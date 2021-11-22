package taoCalc.dto;

public class RaidInfo {

	private Integer raidNo;

	private String name = "";
	private String rank = "";
	private String totalHp = "";
	private String hp = "";
	private String URL = "";
	private String zokusei = "";

	private String limit = "";

	public Integer getRaidNo() {
		return raidNo;
	}

	public void setRaidNo(Integer raidNo) {
		this.raidNo = raidNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getTotalHp() {
		return totalHp;
	}

	public void setTotalHp(String totalHp) {
		this.totalHp = totalHp;
	}

	public String getHp() {
		return hp;
	}

	public void setHp(String hp) {
		this.hp = hp;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getZokusei() {
		return zokusei;
	}

	public void setZokusei(String zokusei) {
		this.zokusei = zokusei;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}



}

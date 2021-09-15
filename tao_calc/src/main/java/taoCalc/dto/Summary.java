package taoCalc.dto;

public class Summary {

	private String guildId = "";

	private String memberId = "";

	private Double combatCount = 0D;

	private Double groundCount = 0D;

	private Double exp = 0D;

	private Double sozaiCount = 0D;

	private Double weaponCount = 0D;

	private Double bukikonCount = 0D;
	
	private Double banCount = 0D;

	public String getGuildId() {
		return guildId;
	}

	public void setGuildId(String guildId) {
		this.guildId = guildId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Double getCombatCount() {
		return combatCount;
	}

	public void setCombatCount(Double combatCount) {
		this.combatCount = combatCount;
	}

	public Double getGroundCount() {
		return groundCount;
	}

	public void setGroundCount(Double groundCount) {
		this.groundCount = groundCount;
	}

	public Double getExp() {
		return exp;
	}

	public void setExp(Double exp) {
		this.exp = exp;
	}

	public Double getSozaiCount() {
		return sozaiCount;
	}

	public void setSozaiCount(Double sozaiCount) {
		this.sozaiCount = sozaiCount;
	}

	public Double getWeaponCount() {
		return weaponCount;
	}

	public void setWeaponCount(Double weaponCount) {
		this.weaponCount = weaponCount;
	}

	public Double getBukikonCount() {
		return bukikonCount;
	}

	public void setBukikonCount(Double bukikonCount) {
		this.bukikonCount = bukikonCount;
	}

	public Double getBanCount() {
		return banCount;
	}

	public void setBanCount(Double banCount) {
		this.banCount = banCount;
	}

}

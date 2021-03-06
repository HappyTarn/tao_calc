package taoCalc.dto;

public class RaidMemberInfo {

	private Integer raidNo;

	private String memberId = "";

	private String damage = "";

	private Double damageD = 0D;

	public Integer getRaidNo() {
		return raidNo;
	}

	public void setRaidNo(Integer raidNo) {
		this.raidNo = raidNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getDamage() {
		return damage;
	}

	public void setDamage(String damage) {
		this.damage = damage;
		this.damageD = Double.parseDouble(damage);
	}

	public Double getDamageD() {
		return damageD;
	}

}

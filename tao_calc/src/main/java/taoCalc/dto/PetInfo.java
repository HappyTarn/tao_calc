package taoCalc.dto;

import taoCalc.Const;

public class PetInfo {

	private String id = "";

	private Long nCount = 0L;
	private Long rCount = 0L;
	private Long srCount = 0L;
	private Long unknownCount = 0L;
	private Long mmoCount = 0L;
	private Long taoCount = 0L;
	
	public void addCount(String rare) {
		
		switch (rare) {
		case Const.PET_N:
			this.rCount++;
			break;
		case Const.PET_R:
			this.rCount++;
			break;
		case Const.PET_SR:
			this.srCount++;
			break;
		case Const.PET_U:
			this.unknownCount++;
			break;
		case Const.PET_MMO:
			this.mmoCount++;
			break;
		case Const.PET_TAO:
			this.taoCount++;
			break;
		}
		
	}
	
	public String get割合() {
		Long total = this.nCount + this.rCount + this.srCount +
				this.unknownCount + this.mmoCount + this.taoCount;
		if (total == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		if (this.nCount == 0) {
			sb.append(Const.PET_N + "(" + this.nCount + ")：0％\n");
		} else {
			sb.append(Const.PET_N + "(" + this.nCount + ")：" + ((float) this.nCount / (float) total * 100) + "％\n");
		}

		if (this.rCount == 0) {
			sb.append(Const.PET_R + "(" + this.rCount + ")：0％\n");
		} else {
			sb.append(Const.PET_R + "(" + this.rCount + ")：" + ((float) this.rCount / (float) total * 100) + "％\n");
		}

		if (this.srCount == 0) {
			sb.append(Const.PET_SR + "(" + this.srCount + ")：0％\n");
		} else {
			sb.append(Const.PET_SR + "(" + this.srCount + ")：" + ((float) this.srCount / (float) total * 100) + "％\n");
		}

		if (this.unknownCount == 0) {
			sb.append(Const.PET_U + "(" + this.unknownCount + ")：0％\n");
		} else {
			sb.append(Const.PET_U + "(" + this.unknownCount + ")：" + ((float) this.unknownCount / (float) total * 100) + "％\n");
		}

		if (this.mmoCount == 0) {
			sb.append(Const.PET_MMO + "(" + this.mmoCount + ")：0％\n");
		} else {
			sb.append(Const.PET_MMO + "(" + this.mmoCount + ")：" + ((float) this.mmoCount / (float) total * 100) + "％\n");
		}

		if (this.taoCount == 0) {
			sb.append(Const.PET_TAO + "(" + this.taoCount + ")：0％\n");
		} else {
			sb.append(Const.PET_TAO + "(" + this.taoCount + ")：" + ((float) this.taoCount / (float) total * 100) + "％\n");
		}

		return sb.toString();
	}
	
	public String getTao割合() {
		Long total = this.nCount + this.rCount + this.srCount +
				this.unknownCount + this.mmoCount + this.taoCount;
		if (total == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		
		if (this.unknownCount == 0) {
			sb.append(Const.PET_U + "(" + this.unknownCount + ")：0％\n");
		} else {
			sb.append(Const.PET_U + "(" + this.unknownCount + ")：" + ((float) this.unknownCount / (float) this.unknownCount * 100) + "％\n");
		}

		if (this.mmoCount == 0) {
			sb.append(Const.PET_MMO + "(" + this.mmoCount + ")：0％\n");
		} else {
			sb.append(Const.PET_MMO + "(" + this.mmoCount + ")：" + ((float) this.mmoCount / (float) this.unknownCount * 100) + "％\n");
		}

		if (this.taoCount == 0) {
			sb.append(Const.PET_TAO + "(" + this.taoCount + ")：0％\n");
		} else {
			sb.append(Const.PET_TAO + "(" + this.taoCount + ")：" + ((float) this.taoCount / (float) this.mmoCount * 100) + "％\n");
		}

		return sb.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getnCount() {
		return nCount;
	}

	public void setnCount(Long nCount) {
		this.nCount = nCount;
	}

	public Long getrCount() {
		return rCount;
	}

	public void setrCount(Long rCount) {
		this.rCount = rCount;
	}

	public Long getSrCount() {
		return srCount;
	}

	public void setSrCount(Long srCount) {
		this.srCount = srCount;
	}

	public Long getUnknownCount() {
		return unknownCount;
	}

	public void setUnknownCount(Long unknownCount) {
		this.unknownCount = unknownCount;
	}

	public Long getMmoCount() {
		return mmoCount;
	}

	public void setMmoCount(Long mmoCount) {
		this.mmoCount = mmoCount;
	}

	public Long getTaoCount() {
		return taoCount;
	}

	public void setTaoCount(Long taoCount) {
		this.taoCount = taoCount;
	}
}

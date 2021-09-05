package taoCalc.dto;

import java.text.NumberFormat;

import taoCalc.Const;

public class Member {

	private String id = "";
	private Long exp = 0L;

	private Long 通常 = 0L;
	private Long 強敵 = 0L;
	private Long シリーズ = 0L;
	private Long 弱敵 = 0L;
	private Long レア = 0L;
	private Long 激レア = 0L;
	private Long 超激レア = 0L;
	private Long 鯖限 = 0L;
	private Long tohru = 0L;
	private Long 超強敵 = 0L;
	private Long 合計 = 0L;

	private String updateDate;

	public String getUpdateDate() {
		return updateDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getExp() {
		return exp;
	}

	public void setExp(Long exp) {
		this.exp = exp;
	}

	public Member(String id, Long exp, String updateDate) {
		this.id = id;
		this.exp = exp;
		this.updateDate = updateDate;
	}

	public Member(String id) {
		this.id = id;
	}

	public Member(String id, Long exp) {
		this.id = id;
		this.exp = exp;
	}
	
	public String getFormatExp() {
		NumberFormat nfNum = NumberFormat.getNumberInstance();
		return nfNum.format(this.exp);
	}

	public void addExp(Long exp) {
		this.exp += exp;
	}

	public void substExp(Long exp) {
		this.exp -= exp;
	}

	public Long get通常() {
		return 通常;
	}

	public void set通常(Long 通常) {
		this.通常 = 通常;
	}

	public Long get強敵() {
		return 強敵;
	}

	public void set強敵(Long 強敵) {
		this.強敵 = 強敵;
	}

	public Long getシリーズ() {
		return シリーズ;
	}

	public void setシリーズ(Long シリーズ) {
		this.シリーズ = シリーズ;
	}

	public Long get弱敵() {
		return 弱敵;
	}

	public void set弱敵(Long 弱敵) {
		this.弱敵 = 弱敵;
	}

	public Long getレア() {
		return レア;
	}

	public void setレア(Long レア) {
		this.レア = レア;
	}

	public Long get激レア() {
		return 激レア;
	}

	public void set激レア(Long 激レア) {
		this.激レア = 激レア;
	}

	public Long get超激レア() {
		return 超激レア;
	}

	public void set超激レア(Long 超激レア) {
		this.超激レア = 超激レア;
	}

	public Long get鯖限() {
		return 鯖限;
	}

	public void set鯖限(Long 鯖限) {
		this.鯖限 = 鯖限;
	}

	public Long getTohru() {
		return tohru;
	}

	public void setTohru(Long tohru) {
		this.tohru = tohru;
	}

	public Long get超強敵() {
		return 超強敵;
	}

	public void set超強敵(Long 超強敵) {
		this.超強敵 = 超強敵;
	}

	public String get割合() {
		Long total = this.通常 + this.弱敵 + this.強敵 +
				this.超強敵 + this.シリーズ + this.レア + this.激レア + this.超激レア + this.鯖限 + this.tohru;
		if (total == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		if (this.通常 == 0) {
			sb.append(Const.通常 + "(" + this.通常 + ")：0％\n");
		} else {
			sb.append(Const.通常 + "(" + this.通常 + ")：" + ((float) this.通常 / (float) total * 100) + "％\n");
		}

		if (this.弱敵 == 0) {
			sb.append(Const.弱敵 + "(" + this.弱敵 + ")：0％\n");
		} else {
			sb.append(Const.弱敵 + "(" + this.弱敵 + ")：" + ((float) this.弱敵 / (float) total * 100) + "％\n");
		}

		if (this.強敵 == 0) {
			sb.append(Const.強敵 + "(" + this.強敵 + ")：0％\n");
		} else {
			sb.append(Const.強敵 + "(" + this.強敵 + ")：" + ((float) this.強敵 / (float) total * 100) + "％\n");
		}

		if (this.超強敵 == 0) {
			sb.append(Const.超強敵 + "(" + this.超強敵 + ")：0％\n");
		} else {
			sb.append(Const.超強敵 + "(" + this.超強敵 + ")：" + ((float) this.超強敵 / (float) total * 100) + "％\n");
		}

		if (this.シリーズ == 0) {
			sb.append(Const.シリーズ + "(" + this.シリーズ + ")：0％\n");
		} else {
			sb.append(Const.シリーズ + "(" + this.シリーズ + ")：" + ((float) this.シリーズ / (float) total * 100) + "％\n");
		}

		if (this.レア == 0) {
			sb.append(Const.レア + "(" + this.レア + ")：0％\n");
		} else {
			sb.append(Const.レア + "(" + this.レア + ")：" + ((float) this.レア / (float) total * 100) + "％\n");
		}

		if (this.激レア == 0) {
			sb.append(Const.激レア + "(" + this.激レア + ")：0％\n");
		} else {
			sb.append(Const.激レア + "(" + this.激レア + ")：" + ((float) this.激レア / (float) total * 100) + "％\n");
		}

		if (this.超激レア == 0) {
			sb.append(Const.超激レア + "(" + this.超激レア + ")：0％\n");
		} else {
			sb.append(Const.超激レア + "(" + this.超激レア + ")：" + ((float) this.超激レア / (float) total * 100) + "％\n");
		}

		if (this.鯖限 == 0) {
			sb.append(Const.鯖限 + "(" + this.鯖限 + ")：0％\n");
		} else {
			sb.append(Const.鯖限 + "(" + this.鯖限 + ")：" + ((float) this.鯖限 / (float) total * 100) + "％\n");
		}

		if (this.tohru == 0) {
			sb.append(Const.tohru + "(" + this.tohru + ")：0％\n");
		} else {
			sb.append(Const.tohru + "(" + this.tohru + ")：" + ((float) this.tohru / (float) total * 100) + "％\n");
		}
		sb.append("合計：" + total + "\n");

		return sb.toString();
	}

	public float getレア率() {
		Long total = this.通常 + this.弱敵 + this.強敵 +
				this.超強敵 + this.シリーズ + this.レア + this.激レア + this.超激レア + this.鯖限 + this.tohru;
		if (total == 0) {
			return 0;
		}
		if (this.レア == 0) {
			return 0;
		} else {
			return ((float) this.レア / (float) total * 100);
		}
	}

	public void addRank(String rank) {
		switch (rank) {
		case Const.通常:
			this.通常 += 1;
			break;
		case Const.弱敵:
			this.弱敵 += 1;
			break;
		case Const.強敵:
			this.強敵 += 1;
			break;
		case Const.超強敵:
			this.超強敵 += 1;
			break;
		case Const.シリーズ:
			this.シリーズ += 1;
			break;
		case Const.レア:
			this.レア += 1;
			break;
		case Const.激レア:
			this.激レア += 1;
			break;
		case Const.超激レア:
			this.超激レア += 1;
			break;
		case Const.鯖限:
			this.鯖限 += 1;
			break;
		case Const.tohru:
			this.tohru += 1;
			break;
		default:

		}
	}

	public Long get合計() {
		return 合計;
	}

	public void set合計(Long 合計) {
		this.合計 = 合計;
	}

}

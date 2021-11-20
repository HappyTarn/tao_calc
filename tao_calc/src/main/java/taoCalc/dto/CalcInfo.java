package taoCalc.dto;

import java.text.NumberFormat;

public class CalcInfo {

	private Double exp = 0D;

	private Integer one = 0;
	private Integer two = 0;
	private Integer ten = 0;

	private Integer battleCount = 0;
	private Integer materiaCount = 0;

	private Integer bukikonCount = 0;

	private Integer weaponCount = 0;

	private Double max = Double.MIN_VALUE;
	private Double min = Double.MAX_VALUE;

	private Double critical = 0D;

	private Integer enchant_n = 0;
	private Integer enchant_r = 0;
	private Integer enchant_sr = 0;
	private Integer enchant_unknown = 0;
	private Integer enchant_anijaaa = 0;
	private Integer enchant_tsukishima = 0;
	private Integer enchant_mmo = 0;
	private Integer enchant_tao = 0;

	public void addEnchant_n() {
		this.enchant_n++;
	}

	public void addEnchant_r() {
		this.enchant_r++;
	}

	public void addEnchant_sr() {
		this.enchant_sr++;
	}

	public void addEnchant_unknown() {
		this.enchant_unknown++;
	}

	public void addEnchant_anijaaa() {
		this.enchant_anijaaa++;
	}

	public void addEnchant_tsukishima() {
		this.enchant_tsukishima++;
	}

	public void addEnchant_mmo() {
		this.enchant_mmo++;
	}

	public void addEnchant_tao() {
		this.enchant_tao++;
	}

	public void addExp(Double d) {
		exp = exp + d;
	}

	public void addCritical(Double d) {
		if (d > critical) {
			critical = d;
		}
	}

	public void addDamage(Double i) {
		if (i > max) {
			max = i;
		}
		if (i < min) {
			min = i;
		}
	}

	public void addBattleCount() {
		battleCount++;
	}

	public void addMateriaCount() {
		materiaCount++;
	}

	public void addWeaponCount() {
		weaponCount++;
	}

	public Integer getOne() {
		return one;
	}

	public void setOne(Integer one) {
		this.one = one;
	}

	public Integer getTwo() {
		return two;
	}

	public void setTwo(Integer two) {
		this.two = two;
	}

	public void addOne() {
		this.one++;
	}

	public void addTen() {
		this.ten++;
	}

	public void addTwo() {
		this.two++;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public String getExpString() {
		NumberFormat nfNum = NumberFormat.getNumberInstance();
		return nfNum.format(exp);
	}

	public String getMaxString() {
		NumberFormat nfNum = NumberFormat.getNumberInstance();
		return nfNum.format(max);
	}

	public String getMinString() {
		NumberFormat nfNum = NumberFormat.getNumberInstance();
		return min == Double.MAX_VALUE ? "0" : nfNum.format(min);
	}

	public String getCriString() {
		NumberFormat nfNum = NumberFormat.getNumberInstance();
		return nfNum.format(critical);
	}

	public String get素材割合() {
		if (materiaCount == 0) {
			return "データがないよ";
		}

		if (this.materiaCount == 0) {
			return battleCount + "(" + materiaCount + ")：0％\n";
		} else {
			return battleCount + "(" + materiaCount + ")：" + ((float) materiaCount / (float) battleCount * 100) + "％\n";
		}

	}

	public void addBukikonCount() {
		bukikonCount++;
	}

	public String get武器魂割合() {
		if (bukikonCount == 0) {
			return "データがないよ";
		}

		if (this.bukikonCount == 0) {
			return battleCount + "(" + bukikonCount + ")：0％\n";
		} else {
			return battleCount + "(" + bukikonCount + ")：" + ((float) bukikonCount / (float) battleCount * 100) + "％\n";
		}

	}

	public String get武器割合() {
		if (weaponCount == 0) {
			return "データがないよ";
		}

		if (this.weaponCount == 0) {
			return battleCount + "(" + weaponCount + ")：0％\n";
		} else {
			return battleCount + "(" + weaponCount + ")：" + ((float) weaponCount / (float) battleCount * 100) + "％\n";
		}

	}

	public String get貫通割合() {
		Integer total = this.one + this.two;
		StringBuilder sb = new StringBuilder();
		if (this.one == 0) {
			sb.append("１体貫通(" + this.one + ")：0％\n");
		} else {
			sb.append("１体貫通(" + this.one + ")：" + ((float) this.one / (float) total * 100) + "％\n");
		}

		if (this.two == 0) {
			sb.append("２体貫通(" + this.two + ")：0％\n");
		} else {
			sb.append("２体貫通(" + this.two + ")：" + ((float) this.two / (float) total * 100) + "％\n");
		}

		if (this.ten == 0) {
			sb.append("１０体貫通(" + this.ten + ")：0％\n");
		} else {
			sb.append("１０体貫通(" + this.ten + ")：" + ((float) this.ten / (float) battleCount * 100) + "％\n");
		}

		return sb.toString();
	}

	public String get通常枠() {
		Integer total = this.enchant_n + this.enchant_r + this.enchant_sr + this.enchant_unknown;
		if (total == 0) {
			return "データがないよ";
		}
		StringBuilder sb = new StringBuilder();
		if (this.enchant_n == 0) {
			sb.append("Nレア(" + this.enchant_n + ")：0％\n");
		} else {
			sb.append("Nレア(" + this.enchant_n + ")：" + ((float) this.enchant_n / (float) total * 100) + "％\n");
		}
		if (this.enchant_r == 0) {
			sb.append("Rレア(" + this.enchant_r + ")：0％\n");
		} else {
			sb.append("Rレア(" + this.enchant_r + ")：" + ((float) this.enchant_r / (float) total * 100) + "％\n");
		}
		if (this.enchant_sr == 0) {
			sb.append("SRレア(" + this.enchant_sr + ")：0％\n");
		} else {
			sb.append("SRレア(" + this.enchant_sr + ")：" + ((float) this.enchant_sr / (float) total * 100) + "％\n");
		}
		if (this.enchant_unknown == 0) {
			sb.append("Unknownレア(" + this.enchant_unknown + ")：0％\n");
		} else {
			sb.append("Unknownレア(" + this.enchant_unknown + ")：" + ((float) this.enchant_unknown / (float) total * 100)
					+ "％\n");
		}

		return sb.toString();
	}

	public String get特殊枠() {
		Integer total = this.enchant_anijaaa + this.enchant_tsukishima + this.enchant_mmo + this.enchant_tao;
		if (total == 0) {
			return "データがないよ";
		}
		StringBuilder sb = new StringBuilder();
		if (this.enchant_anijaaa == 0) {
			sb.append("Anijaaaレア(" + this.enchant_anijaaa + ")：0％\n");
		} else {
			sb.append("Anijaaaレア(" + this.enchant_anijaaa + ")：" + ((float) this.enchant_anijaaa / (float) total * 100)
					+ "％\n");
		}
		if (this.enchant_tsukishima == 0) {
			sb.append("Tsukishimaレア(" + this.enchant_tsukishima + ")：0％\n");
		} else {
			sb.append("Tsukishimaレア(" + this.enchant_tsukishima + ")："
					+ ((float) this.enchant_tsukishima / (float) total * 100) + "％\n");
		}
		if (this.enchant_mmo == 0) {
			sb.append("MMOレア(" + this.enchant_mmo + ")：0％\n");
		} else {
			sb.append("MMOレア(" + this.enchant_mmo + ")：" + ((float) this.enchant_mmo / (float) total * 100) + "％\n");
		}
		if (this.enchant_tao == 0) {
			sb.append("TAOレア(" + this.enchant_tao + ")：0％\n");
		} else {
			sb.append("TAOレア(" + this.enchant_tao + ")：" + ((float) this.enchant_tao / (float) total * 100) + "％\n");
		}

		return sb.toString();
	}
}

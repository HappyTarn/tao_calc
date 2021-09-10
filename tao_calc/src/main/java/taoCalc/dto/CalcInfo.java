package taoCalc.dto;

import java.text.NumberFormat;

public class CalcInfo {

	private Integer one = 0;
	private Integer two = 0;

	private Integer battleCount = 0;
	private Integer materiaCount = 0;

	private Integer bukikonCount = 0;

	private Double max = Double.MIN_VALUE;
	private Double min = Double.MAX_VALUE;
	
	private Double critical = 0D;
	
	public void addCritical(Double d) {
		if(d > critical) {
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

	public String getMaxString() {
		NumberFormat nfNum = NumberFormat.getNumberInstance();
		return nfNum.format(max);
	}

	public String getMinString() {
		NumberFormat nfNum = NumberFormat.getNumberInstance();
		return nfNum.format(min);
	}
	
	public String getCriString() {
		NumberFormat nfNum = NumberFormat.getNumberInstance();
		return nfNum.format(critical);
	}

	public void addBattleCount() {
		battleCount++;
	}

	public void addMateriaCount() {
		materiaCount++;
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

	public void addTwo() {
		this.two++;
	}

	public String get貫通割合() {
		Integer total = this.one + this.two;
		if (total == 0) {
			return "データがないよ"
					+ "";
		}
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

		return sb.toString();
	}
}

package taoCalc.dto;

import java.text.NumberFormat;

public class Attack {

	private Double max = Double.MIN_VALUE;
	private Double min = Double.MAX_VALUE;

	public void add(Double i) {
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

}

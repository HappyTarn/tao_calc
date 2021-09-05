package taoCalc.dto;

public class Penetration {

	private Integer one = 0;
	private Integer two = 0;

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

	public String get割合() {
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

		sb.append("合計：" + total + "\n");

		return sb.toString();
	}
}

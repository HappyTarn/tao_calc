package taoCalc.dto;

public class Material {

	private Integer battleCount = 0;
	private Integer materiaCount = 0;

	public void addBattleCount() {
		battleCount++;
	}

	public void addMateriaCount() {
		battleCount++;
		materiaCount++;
	}

	public String get割合() {
		if (battleCount == 0) {
			return "データがないよ";
		}

		if (this.materiaCount == 0) {
			return battleCount + "(" + materiaCount + ")：0％\n";
		} else {
			return battleCount + "(" + materiaCount + ")：" + ((float) materiaCount / (float) battleCount * 100) + "％\n";
		}

	}
}

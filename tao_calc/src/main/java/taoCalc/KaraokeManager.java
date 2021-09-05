package taoCalc;

public class KaraokeManager {
	private static KaraokeManager INSTANCE;

	private Integer count = 0;

	private KaraokeManager() {
	}

	public static synchronized KaraokeManager getINSTANCE() {
		if (INSTANCE == null) {
			INSTANCE = new KaraokeManager();
		}

		return INSTANCE;
	}


	public Integer count() {
		count++;
		return count;
	}


}
package technique;

public class Cassette {
	private static Cassette instance;
	
	private int maxCount;
	private int currentCount;	
	private float timeRecharge;
	
	private Cassette () {
		
	}
	
	public static Cassette getInstance () {
		if (instance == null) instance = new Cassette();
		return instance;
	}
	
	public void setMaxCount (int maxCount) {
		this.maxCount = maxCount;
	}
	
	public void setCount (int currentCount) {
		this.currentCount = currentCount;
	}
	
	public void setTimeRecharge (float timeRecharge) {
		this.timeRecharge = timeRecharge;
	}
	
	public int getCurrentCount () {
		return currentCount;
	}
	
	public int getMaxCount () {
		return maxCount;
	}
	
	public float getTimeRecharge () {
		return timeRecharge;
	}
}

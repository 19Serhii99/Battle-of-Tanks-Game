package technique;

public class Cassette {	
	private int maxCount;
	private float timeRecharge;
	
	private ShootingStyle shootingStyle;
	
	public Cassette () {
		
	}
	
	public void setMaxCount (int maxCount) {
		this.maxCount = maxCount;
	}
	
	public void setTimeRecharge (float timeRecharge) {
		this.timeRecharge = timeRecharge;
	}
	
	public void setShootingStyle (ShootingStyle shootingStyle) {
		this.shootingStyle = shootingStyle;
	}
	
	public int getMaxCount () {
		return maxCount;
	}
	
	public float getTimeRecharge () {
		return timeRecharge;
	}
	
	public ShootingStyle getShootingStyle () {
		return shootingStyle;
	}
}
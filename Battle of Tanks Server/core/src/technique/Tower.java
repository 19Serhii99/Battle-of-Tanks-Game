package technique;

public class Tower extends Module {
	private float timeReduction;
	private float minRadius;
	private float maxRadius;
	private float rotationLeft;
	private float rotationRight;
	private float timeRecharge;
	private float x;
	private float y;
	private float originX;
	private float originY;
	private int minDamage;
	private int maxDamage;
	private int shellsTotal;
	
	private Gun gun;
	
	public Tower(int id) {
		super(id);
	}
	
	public void setTimeReduction (float timeReduction) {
		this.timeReduction = timeReduction;
	}
	
	public void setMinRadius (float minRadius) {
		this.minRadius = minRadius;
	}
	
	public void setMaxRadius (float maxRadius) {
		this.maxRadius = maxRadius;
	}
	
	public void setRotationLeft (float rotationLeft) {
		this.rotationLeft = rotationLeft;
	}
	
	public void setRotationRight (float rotationRight) {
		this.rotationRight = rotationRight;
	}
	
	public void setTimeRecharge (float timeRecharge) {
		this.timeRecharge = timeRecharge;
	}
	
	public void setMinDamage (int minDamage) {
		this.minDamage = minDamage;
	}
	
	public void setMaxDamage (int maxDamage) {
		this.maxDamage = maxDamage;
	}
	
	public void setShellsTotal (int shellsTotal) {
		this.shellsTotal = shellsTotal;
	}
	
	public void setGun (Gun gun) {
		this.gun = gun;
	}
	
	public void setX (float x) {
		this.x = x;
	}
	
	public void setY (float y) {
		this.y = y;
	}
	
	public void setOriginX (float x) {
		this.originX = x;
	}
	
	public void setOriginY (float y) {
		this.originY = y;
	}
	
	public float getTimeReduction () {
		return timeReduction;
	}
	
	public float getMinRadius () {
		return minRadius;
	}
	
	public float getMaxRadius () {
		return maxRadius;
	}
	
	public float getRotationLeft () {
		return rotationLeft;
	}
	
	public float getRotationRight () {
		return rotationRight;
	}
	
	public float getTimeRecharge () {
		return timeRecharge;
	}
	
	public int getMinDamage () {
		return minDamage;
	}
	
	public int getMaxDamage () {
		return maxDamage;
	}
	
	public int getShellsTotal () {
		return shellsTotal;
	}
	
	public Gun getGun () {
		return gun;
	}
	
	public float getX () {
		return x;
	}
	
	public float getY () {
		return y;
	}
	
	public float getOriginX () {
		return originX;
	}
	
	public float getOriginY () {
		return originY;
	}
}
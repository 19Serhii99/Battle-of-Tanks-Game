package additional;

import java.io.Serializable;

public class Tower extends Module implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private float timeReduction;
	private float minRadius;
	private float maxRadius;
	private float rotationLeft;
	private float rotationRight;
	private float timeRecharge;
	private float shellSpeed;
	private float rechargeShell;
	private float x;
	private float y;
	private float originX;
	private float originY;
	private float shellWidth;
	private float shellHeight;
	private int minDamage;
	private int maxDamage;
	private int shellsTotal;
	private int shellsCassette;
	
	private String shootingStyle;
	
	public Tower(int id) {
		super(id);
	}
	
	public void setShellSpeed (float speed) {
		shellSpeed = speed;
	}
	
	public void setRechargeShell (float value) {
		rechargeShell = value;
	}
	
	public void setX (float x) {
		this.x = x;
	}
	
	public void setY (float y) {
		this.y = y;
	}
	
	public void setOriginX (float originX) {
		this.originX = originX;
	}
	
	public void setOriginY (float originY) {
		this.originY = originY;
	}
	
	public void setShellsCassette (int value) {
		shellsCassette = value;
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
	
	public void setShootingStyle (String shootingStyle) {
		this.shootingStyle = shootingStyle;
	}
	
	public void setShellWidth (float width) {
		shellWidth = width;
	}
	
	public void setShellHeight (float height) {
		shellHeight = height;
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
	
	public int getShellsCassette () {
		return shellsCassette;
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
	
	public float getShellSpeed () {
		return shellSpeed;
	}
	
	public float getRechargeShell () {
		return rechargeShell;
	}
	
	public String getShootingStyle () {
		return shootingStyle;
	}
	
	public float getShellWidth () {
		return shellWidth;
	}
	
	public float getShellHeight () {
		return shellHeight;
	}
}
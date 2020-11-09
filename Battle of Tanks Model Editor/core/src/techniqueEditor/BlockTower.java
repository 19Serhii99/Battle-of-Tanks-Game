package techniqueEditor;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import technique.ShootingStyle;

public class BlockTower extends Block implements Disposable {
	private float timeReduction;
	private float minRadius;
	private float maxRadius;
	private float rotationLeft;
	private float rotationRight;
	private float xOrigin;
	private float yOrigin;
	private float timeRecharge;
	private float timeRechargeShellCassette;
	private int experience;
	private int minDamage;
	private int maxDamage;
	private int totalShells;
	private int totalShellsCassette;
	
	private String name;
	
	private ArrayList <Vector2> gunPositions;
	private ShootingStyle shootingStyle;
	
	public BlockTower (float x, float y) {
		super(x, y);
		gunPositions = new ArrayList <Vector2>(1);
	}
	
	public void setExperience (int experience) {
		this.experience = experience;
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
	
	public void setMinDamage (int minDamage) {
		this.minDamage = minDamage;
	}
	
	public void setMaxDamage (int maxDamage) {
		this.maxDamage = maxDamage;
	}
	
	public void setRotationLeft (float rotationLeft) {
		this.rotationLeft = rotationLeft;
	}
	
	public void setRotationRight (float rotationRight) {
		this.rotationRight = rotationRight;
	}
	
	public void setXOrigin (float xOrigin) {
		this.xOrigin = xOrigin;
	}
	
	public void setYOrigin (float yOrigin) {
		this.yOrigin = yOrigin;
	}
	
	public void setTotalShells (int totalShells) {
		this.totalShells = totalShells;
	}
	
	public void setTotalShellsCassette (int totalShellsCassette) {
		this.totalShellsCassette = totalShellsCassette;
	}
	
	public void setTimeRecharge (float timeRecharge) {
		this.timeRecharge = timeRecharge;
	}
	
	public void setTimeRechargeShellCassette (float timeRechargeShellCassette) {
		this.timeRechargeShellCassette = timeRechargeShellCassette;
	}
	
	public void addGunPosition (float x, float y) {
		gunPositions.add(new Vector2(x, y));
	}
	
	public void setShootingStyle (ShootingStyle shootingStyle) {
		this.shootingStyle = shootingStyle;
	}
	
	public void setName (String name) {
		this.name = name;
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
	
	public float getXOrigin () {
		return xOrigin;
	}
	
	public float getYOrigin () {
		return yOrigin;
	}
	
	public float getTimeRecharge () {
		return timeRecharge;
	}
	
	public float getTimeRechargeShellCassette () {
		return timeRechargeShellCassette;
	}
	
	public int getMinDamage () {
		return minDamage;
	}
	
	public int getMaxDamage () {
		return maxDamage;
	}
	
	public int getTotalShells () {
		return totalShells;
	}
	
	public int getTotalShellsCassette () {
		return totalShellsCassette;
	}
	
	public int getExperience () {
		return experience;
	}
	
	public ArrayList <Vector2> getGunPositions () {
		return gunPositions;
	}
	
	public ShootingStyle getShootingStyle () {
		return shootingStyle;
	}
	
	public String getName () {
		return name;
	}
}
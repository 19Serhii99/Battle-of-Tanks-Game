package technique;

import java.util.ArrayList;

public class Corps extends Module {
	private TechniqueType techniqueType;
	private ArrayList <Tower> towers;
	
	private int maxHealth;
	private float maxSpeed;
	private float acceleration;
	
	public Corps (int id) {
		super(id);
		towers = new ArrayList <Tower>(1);
	}
	
	public void setMaxHealth (int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public void setMaxSpeed (float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public void setTechniqueType (TechniqueType techniqueType) {
		this.techniqueType = techniqueType;
	}
	
	public void setAcceleration (float acceleration) {
		this.acceleration = acceleration;
	}
	
	public int getMaxHealth () {
		return maxHealth;
	}
	
	public float getMaxSpeed () {
		return maxSpeed;
	}
	
	public float getAcceleration () {
		return acceleration;
	}
	
	public TechniqueType getTechniqueType () {
		return techniqueType;
	}
	
	public ArrayList <Tower> getTowers () {
		return towers;
	}
}
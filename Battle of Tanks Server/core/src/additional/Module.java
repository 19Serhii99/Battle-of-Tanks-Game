package additional;

import java.io.Serializable;

public abstract class Module implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected int id;
	protected int position;
	protected int level;
	protected int experience;
	protected int money;
	protected float speedRotation;
	protected float width;
	protected float height;
	protected boolean isAvailable;
	protected boolean isExplored;
	protected boolean isBought;
	
	protected String name;
	
	public Module (int id) {
		this.id = id;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public void setSpeedRotation (float speedRotation) {
		this.speedRotation = speedRotation;
	}
	
	public void setPosition (int position) {
		this.position = position;
	}
	
	public void setLevel (int level) {
		this.level = level;
	}
	
	public void setMoney (int money) {
		this.money = money;
	}
	
	public void setExplored (boolean isExplored) {
		this.isExplored = isExplored;
	}
	
	public void setBought (boolean isBought) {
		this.isBought = isBought;
	}
	
	public void setAvailable (boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	public void setExperience (int experience) {
		this.experience = experience;
	}
	
	public void setWidth (float width) {
		this.width = width;
	}
	
	public void setHeight (float height) {
		this.height = height;
	}
	
	public int getId () {
		return id;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getLevel () {
		return level;
	}
	
	public int getExperience () {
		return experience;
	}
	
	public int getMoney () {
		return money;
	}
	
	public boolean isAvailable () {
		return isAvailable;
	}
	
	public boolean isExplored () {
		return isExplored;
	}
	
	public boolean isBought () {
		return isBought;
	}
	
	public String getName () {
		return name;
	}
	
	public float getWidth () {
		return width;
	}
	
	public float getHeight () {
		return height;
	}
	
	public float getSpeedRotation () {
		return speedRotation;
	}
}
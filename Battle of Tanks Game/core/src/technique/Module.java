package technique;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;

public abstract class Module implements Disposable {
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
	
	protected Sprite background;
	protected Texture whole;
	protected Texture destroyed;
	protected String name;
	
	public Module (int id) {
		this.id = id;
		this.background = new Sprite();
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
	
	public void setExperience (int exp) {
		this.experience = exp;
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
	
	public void setWholeTexture (Texture texture) {
		whole = texture;
	}
	
	public void setDestroyedTexture (Texture texture) {
		destroyed = texture;
	}
	
	public void setLevel (int level) {
		this.level = level;
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
	
	public float getSpeedRotation () {
		return speedRotation;
	}
	
	public Sprite getBackground () {
		return background;
	}
	
	public Texture getWholeTexture () {
		return whole;
	}
	
	public Texture getDestroyedTexture () {
		return destroyed;
	}
	
	public int getLevel () {
		return level;
	}
	
	public int getExperience () {
		return experience;
	}
	
	public float getWidth () {
		return width;
	}
	
	public float getHeight () {
		return height;
	}
	
	@Override
	public void dispose () {
		if (whole != null) whole.dispose();
		if (destroyed != null) destroyed.dispose();
	}
}
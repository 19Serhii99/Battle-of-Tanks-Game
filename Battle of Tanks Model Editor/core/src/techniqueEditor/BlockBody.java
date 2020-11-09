package techniqueEditor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import technique.TechniqueType;

public class BlockBody extends Block implements Disposable {
	private String name;
	private TechniqueType techniqueType;
	
	private float maxSpeed;
	private float acceleration;
	private float maxHealth;
	
	public BlockBody (float x, float y) {
		super(x, y);
	}
	
	public void show (SpriteBatch batch) {
		super.show(batch);
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public void setMaxSpeed (float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public void setAcceleration (float acceleration) {
		this.acceleration = acceleration;
	}
	
	public void setMaxHealth (float maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public void setTechniqueType (TechniqueType techniqueType) {
		this.techniqueType = techniqueType;
	}
	
	public String getName () {
		return name;
	}
	
	public float getMaxSpeed () {
		return maxSpeed;
	}
	
	public float getAcceleration () {
		return acceleration;
	}
	
	public float getMaxHealth () {
		return maxHealth;
	}
	
	public TechniqueType getTechniqueType () {
		return techniqueType;
	}

	@Override
	public void dispose () {
		super.dispose();
	}
}
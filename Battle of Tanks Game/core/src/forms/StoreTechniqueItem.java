package forms;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import gui.Button;
import gui.Label;
import technique.Corps;
import technique.TechniqueType;

public class StoreTechniqueItem extends Button {
	private Corps corps;
	private Label type;
	private Label typeValue;
	private Label name;
	private Label nameValue;
	
	public StoreTechniqueItem (Texture texture, Texture textureOver, Texture textureFocused, Corps corps, BitmapFont font) {
		background.setSize(250, 200);
		setTexture(texture);
		setTextureOver(textureOver);
		setTextureFocused(textureFocused);
		
		this.corps = corps;
		
		type = new Label("“ËÔ ÚÂıÌ≥ÍË:", font);
		name = new Label("Õ‡ÈÏ.:", font);
		
		if (corps.getTechniqueType() == TechniqueType.TANK) {
			typeValue = new Label("“‡ÌÍ", font);
		} else if (corps.getTechniqueType() == TechniqueType.ARTY) {
			typeValue = new Label("—¿”", font);
		} else if (corps.getTechniqueType() == TechniqueType.REACTIVE_SYSTEM) {
			typeValue = new Label("–—«¬", font);
		} else if (corps.getTechniqueType() == TechniqueType.FLAMETHROWER_SYSTEM) {
			typeValue = new Label("“¬—", font);
		}
		
		nameValue = new Label(corps.getName(), font);
	}
	
	public void setPosition (float x, float y) {
		background.setPosition(x, y);
		corps.getBackground().setPosition(background.getX() + background.getWidth() / 2 - corps.getWidth() / 2, background.getVertices()[Batch.Y2] - corps.getHeight() - 10);
		corps.getTowers().get(0).getBackground().setPosition(corps.getBackground().getX() + corps.getTowers().get(0).getX(), corps.getBackground().getY() + corps.getTowers().get(0).getY());
		
		type.setPosition(background.getX() + 5, corps.getBackground().getY() - 10);
		name.setPosition(background.getX() + 5, type.getY() - type.getHeight() - 20);
		typeValue.setPosition(type.getX() + type.getWidth() + 10, type.getY());
		nameValue.setPosition(name.getX() + name.getWidth() + 10, name.getY());
	}
	
	public float getX () {
		return background.getX();
	}
	
	public float getY () {
		return background.getY();
	}
	
	public float getWidth () {
		return background.getWidth();
	}
	
	public float getHeight () {
		return background.getHeight();
	}
	
	public Corps getCorps () {
		return corps;
	}
	
	public void show (SpriteBatch batch) {
		super.show(batch);
		corps.getBackground().draw(batch);
		corps.getTowers().get(0).getBackground().draw(batch);
		type.draw(batch);
		typeValue.draw(batch);
		name.draw(batch);
		nameValue.draw(batch);
	}
}
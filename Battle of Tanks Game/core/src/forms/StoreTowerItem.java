package forms;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import gui.Button;
import technique.Tower;

public class StoreTowerItem extends Button {
	private Tower tower;
	private Sprite towerAct;
	
	public StoreTowerItem (Texture texture, Texture textureOver, Texture textureFocused, Tower tower, BitmapFont font) {
		towerAct = new Sprite(tower.getWholeTexture());
		
		background.setSize(250, 100);
		setTexture(texture);
		setTextureOver(textureOver);
		setTextureFocused(textureFocused);
		
		this.tower = tower;
		
		towerAct.setSize(tower.getWidth(), tower.getHeight());
	}
	
	public void setPosition (float x, float y) {
		background.setPosition(x, y);
		towerAct.setPosition(background.getX() + background.getWidth() / 2 - towerAct.getWidth() / 2, background.getY() + background.getHeight() / 2 - towerAct.getHeight() / 2);
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
	
	public Tower getTower () {
		return tower;
	}
	
	public void setAlpha (float alpha) {
		tower.getBackground().setAlpha(alpha);
		towerAct.setAlpha(alpha);
		super.background.setAlpha(alpha);
	}
	
	public void show (SpriteBatch batch) {
		super.show(batch);
		towerAct.draw(batch);
	}
}
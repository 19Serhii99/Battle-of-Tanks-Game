package net;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import gui.Label;

public class PlayerItem {
	private Player player;
	private Label number;
	private Label nickname;
	private Label technique;
	private Label kills;
	private Label damage;
	
	private int killsAmount;
	private int damageAmount;
	
	private boolean isMe;
	
	public PlayerItem (Player player, int num, String name, String techniqueName, BitmapFont font, boolean isMe) {
		number = new Label(String.valueOf(num), font);
		nickname = new Label(name, font);
		technique = new Label(techniqueName, font);
		kills = new Label(String.valueOf(killsAmount), font);
		damage = new Label(String.valueOf(damageAmount), font);
		
		this.player = player;
		this.isMe = isMe;
	}
	
	public void addKill () {
		killsAmount++;
		kills.setText(String.valueOf(killsAmount));
	}
	
	public void addDamage (int amount) {
		damageAmount += amount;
		damage.setText(String.valueOf(damageAmount));
	}
	
	public void setPositionNumber (float x, float y) {
		number.setPosition(x, y);
		align(number);
	}
	
	public void setPositionNickname(float x, float y) {
		nickname.setPosition(x, y);
		align(nickname);
	}
	
	public void setPositionTechnique (float x, float y) {
		technique.setPosition(x, y);
		align(technique);
	}
	
	public void setPositionKills (float x, float y) {
		kills.setPosition(x, y);
		align(kills);
	}
	
	public void setPositionDamage (float x, float y) {
		damage.setPosition(x, y);
		align(damage);
	}
	
	private void align (Label label) {
		label.translate(-label.getWidth() / 2, 0);
	}
	
	public void show (SpriteBatch batch) {
		number.draw(batch);
		nickname.draw(batch);
		technique.draw(batch);
		kills.draw(batch);
		damage.draw(batch);
	}
	
	public Player getPlayer () {
		return player;
	}
	
	public String getNickname () {
		return nickname.getText();
	}
	
	public String getTechnique () {
		return technique.getText();
	}
	
	public int getNumber () {
		return Integer.parseInt(number.getText());
	}
	
	public int getKills () {
		return killsAmount;
	}
	
	public int getDamage () {
		return damageAmount;
	}
	
	public boolean isMe () {
		return isMe;
	}
	
	public float getX () {
		return number.getX();
	}
	
	public float getWidth () {
		return damage.getX() + damage.getWidth() - number.getX();
	}
	
	public float getY () {
		return number.getY();
	}
	
	public float getHeight () {
		return number.getHeight();
	}
}
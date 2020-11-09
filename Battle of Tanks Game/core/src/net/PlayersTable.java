package net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import gui.Label;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public abstract class PlayersTable implements Disposable {
	protected Sprite background;
	protected Label timeLabel;
	protected Label mapNameLabel;
	protected Label battleTypeLabel;
	
	protected Label expLabel;
	protected Label moneyLabel;
	protected Label isWonLabel;
	
	protected float battleTime;
	protected int exp;
	protected int money;
	protected boolean isCommand;
	protected boolean isBattleFinished;
	protected boolean isWon;
	
	public PlayersTable (String mapName, String battleType) {
		initBackground();
		initLabels(mapName, battleType);
	}
	
	private void initBackground () {
		background = new Sprite(TextureCreator.createTexture(Color.BLACK));
		background.setAlpha(0.65f);
		background.setSize(1500, 800);
	}
	
	private void initLabels (String mapName, String battleType) {
		battleTime = 600.0f;
		timeLabel = new Label("", Font.getInstance().generateBitmapFont(Color.WHITE, 40));
		
		mapNameLabel = new Label(mapName, Font.getInstance().generateBitmapFont(Color.WHITE, 30));
		
		battleTypeLabel = new Label(battleType, Font.getInstance().generateBitmapFont(Color.WHITE, 20));
	}
	
	public void updateTime () {
		battleTime -= Gdx.graphics.getDeltaTime();
		if (battleTime < 0) battleTime = 0;
	}
	
	public void setBattleFinish (boolean isWon, int exp, int money) {
		this.isBattleFinished = true;
		this.isWon = isWon;
		this.exp = exp;
		this.money = money;
		
		isWonLabel = new Label(isWon ? "Перемога" : "Поразка", Font.getInstance().generateBitmapFont(isWon ? Color.GREEN : Color.RED, 30));
		expLabel = new Label("+" + String.valueOf(exp) + " досвіду", Font.getInstance().generateBitmapFont(Color.GREEN, 25));
		moneyLabel = new Label("+" + String.valueOf(money) + " золота", Font.getInstance().generateBitmapFont(Color.GOLD, 25));
	}
	
	public void show (SpriteBatch batch) {
		CameraController camera = CameraController.getInstance();
		background.setPosition(camera.getX() - background.getWidth() / 2, camera.getY() - background.getHeight() / 2);
		
		int minutes = (int) battleTime / 60;
		int tempSec = minutes * 60;
		int sec = (int) battleTime - tempSec;
		String m = String.valueOf(minutes);
		String s = String.valueOf(sec);
		if (m.length() == 1) m = "0" + m;
		if (s.length() == 1) s = "0" + s;
		
		timeLabel.setText(m + " : " + s);
		timeLabel.setPosition(background.getVertices()[Batch.X3] - timeLabel.getWidth() - 5, background.getVertices()[Batch.Y3] - 5);
		
		mapNameLabel.setPosition(background.getX() + background.getWidth() / 2 - mapNameLabel.getWidth() / 2, background.getVertices()[Batch.Y3] - 5);
		battleTypeLabel.setPosition(background.getX() + background.getWidth() / 2 - battleTypeLabel.getWidth() / 2, mapNameLabel.getY() - mapNameLabel.getHeight() - 10);
		
		background.draw(batch);
		timeLabel.draw(batch);
		mapNameLabel.draw(batch);
		battleTypeLabel.draw(batch);
		
		if (isBattleFinished) {
			isWonLabel.setPosition(background.getX() + background.getWidth() / 2 - isWonLabel.getWidth() / 2, background.getY() + 100);
			expLabel.setPosition(background.getX() + background.getWidth() / 2 - expLabel.getWidth() / 2, background.getY() + 70);
			moneyLabel.setPosition(background.getX() + background.getWidth() / 2 - moneyLabel.getWidth() / 2, background.getY() + 40);
			
			isWonLabel.draw(batch);
			expLabel.draw(batch);
			moneyLabel.draw(batch);
		}
	}
	
	public boolean isCommand () {
		return isCommand;
	}
	
	public float getBattleTime () {
		return battleTime;
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		timeLabel.getFont().dispose();
		mapNameLabel.getFont().dispose();
		battleTypeLabel.getFont().dispose();
		if (isWonLabel != null) isWonLabel.getFont().dispose();
		if (expLabel != null) expLabel.getFont().dispose();
		if (moneyLabel != null) moneyLabel.getFont().dispose();
	}
}
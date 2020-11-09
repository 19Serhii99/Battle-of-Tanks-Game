package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Settings;

import answers.GetDataPlayerAnswer;
import gui.Label;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public class StatisticsForm implements Disposable {
	private Sprite background;
	private Label caption;
	
	private Label battles;
	private Label wins;
	private Label averageDamage;
	private Label kills;
	private Label deaths;
	private Label survival;
	
	private Label battlesValue;
	private Label winsValue;
	private Label averageDamageValue;
	private Label killsValue;
	private Label deathsValue;
	private Label survivalValue;
	
	private boolean isHide = false;
	private float alpha = 0.0f;
	
	public StatisticsForm (GetDataPlayerAnswer getDataPlayerAnswer) {
		background = new Sprite(TextureCreator.createTexture(Color.BLACK));
		background.setAlpha(0.8f);
		background.setSize(700, 380);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2, CameraController.getInstance().getHeight() - background.getHeight() - 70);
		
		BitmapFont captionFont = Font.getInstance().generateBitmapFont(Color.WHITE, 25);
		captionFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		caption = new Label("Статистика гравця " + Settings.getInstance().getName(), captionFont);
		caption.setPosition(background.getX() + background.getWidth() / 2 - caption.getWidth() / 2, background.getVertices()[Batch.Y2] - 10);
		
		BitmapFont labelFont = Font.getInstance().generateBitmapFont(Color.WHITE, 30);
		labelFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		battles = new Label("Боїв:", labelFont);
		wins = new Label("Перемог:", labelFont);
		averageDamage = new Label("Середній урон за бій:", labelFont);
		kills = new Label("Знищенних:", labelFont);
		deaths = new Label("Знищень:", labelFont);
		survival = new Label("Виживання:", labelFont);
		
		float maxWidth = Math.max(Math.max(Math.max(battles.getWidth(), wins.getWidth()), Math.max(averageDamage.getWidth(), kills.getWidth())), Math.max(deaths.getWidth(), survival.getWidth()));
		
		battles.setPosition(background.getX() + maxWidth - battles.getWidth() + 50, caption.getY() - 50);
		wins.setPosition(background.getX() + maxWidth - wins.getWidth() + 50, caption.getY() - 100);
		averageDamage.setPosition(background.getX() + maxWidth - averageDamage.getWidth() + 50, caption.getY() - 150);
		kills.setPosition(background.getX() + maxWidth - kills.getWidth() + 50, caption.getY() - 200);
		deaths.setPosition(background.getX() + maxWidth - deaths.getWidth() + 50, caption.getY() - 250);
		survival.setPosition(background.getX() + maxWidth - survival.getWidth() + 50, caption.getY() - 300);
		
		BitmapFont valuesFont = Font.getInstance().generateBitmapFont(new Color(221f / 255f, 91f / 255f, 91f / 255f, 1.0f), 30);
		valuesFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		battlesValue = new Label(String.valueOf(getDataPlayerAnswer.getBattles()), valuesFont);
		winsValue = new Label(getDataPlayerAnswer.getPercentWins() + "%", valuesFont);
		averageDamageValue = new Label(String.valueOf(getDataPlayerAnswer.getAverageDamage()), valuesFont);
		killsValue = new Label(String.valueOf(getDataPlayerAnswer.getKills()), valuesFont);
		deathsValue = new Label(String.valueOf(getDataPlayerAnswer.getDeaths()), valuesFont);
		survivalValue = new Label(getDataPlayerAnswer.getSurvival() + "%", valuesFont);
		
		battlesValue.setPosition(background.getX() + maxWidth + 100, caption.getY() - 50);
		winsValue.setPosition(background.getX() + maxWidth + 100, caption.getY() - 100);
		averageDamageValue.setPosition(background.getX() + maxWidth + 100, caption.getY() - 150);
		killsValue.setPosition(background.getX() + maxWidth + 100, caption.getY() - 200);
		deathsValue.setPosition(background.getX() + maxWidth + 100, caption.getY() - 250);
		survivalValue.setPosition(background.getX() + maxWidth + 100, caption.getY() - 300);
	}
	
	public void show (SpriteBatch batch) {
		if (isHide) {
			alpha -= Gdx.graphics.getDeltaTime() * 5;
			if (alpha < 0.0f) alpha = 0.0f;
		} else {
			alpha += Gdx.graphics.getDeltaTime() * 2;
			if (alpha > 1.0f) alpha = 1.0f;
		}
		
		background.setAlpha(alpha);
		if (!isHide && background.getColor().a > 0.8f) {
			background.setAlpha(0.8f);
		}
		caption.setAlphas(alpha);
		battles.setAlphas(alpha);
		wins.setAlphas(alpha);
		averageDamage.setAlphas(alpha);
		kills.setAlphas(alpha);
		deaths.setAlphas(alpha);
		survival.setAlphas(alpha);
		battlesValue.setAlphas(alpha);
		winsValue.setAlphas(alpha);
		averageDamageValue.setAlphas(alpha);
		killsValue.setAlphas(alpha);
		deathsValue.setAlphas(alpha);
		survivalValue.setAlphas(alpha);
		
		batch.begin();
		background.draw(batch);
		caption.draw(batch);
		battles.draw(batch);
		wins.draw(batch);
		averageDamage.draw(batch);
		kills.draw(batch);
		deaths.draw(batch);
		survival.draw(batch);
		battlesValue.draw(batch);
		winsValue.draw(batch);
		averageDamageValue.draw(batch);
		killsValue.draw(batch);
		deathsValue.draw(batch);
		survivalValue.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		caption.getFont().dispose();
		battles.getFont().dispose();
		battlesValue.getFont().dispose();
	}
}
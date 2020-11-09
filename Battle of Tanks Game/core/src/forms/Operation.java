package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Objects;

import gui.Label;
import gui.TextButton;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public class Operation implements Disposable {
	private Sprite background;
	private Sprite border;
	
	private Label answer;
	private Label cost;
	
	private TextButton yes;
	private TextButton no;
	
	private boolean isHide = false;
	private float alpha = 0.0f;
	private int type;
	
	public Operation (int type, String name, int costValue) {
		background = new Sprite(TextureCreator.createTexture(Color.GRAY));
		border = new Sprite(TextureCreator.createTexture(Color.GREEN));
		
		background.setSize(500, 200);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2, CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2);
		
		if (type == 1) {
			answer = new Label("Ви впевнені, що хочете придбати " + name + "?", Font.getInstance().generateBitmapFont(Color.WHITE, 20));
			cost = new Label("Вартість придбання: " + costValue + " золота", Font.getInstance().generateBitmapFont(Color.WHITE, 15));
		} else if (type == 2) {
			answer = new Label("Ви впевнені, що хочете дослідити " + name + "?", Font.getInstance().generateBitmapFont(Color.WHITE, 20));
			cost = new Label("Вартість дослідження: " + costValue + " досвіду", Font.getInstance().generateBitmapFont(Color.WHITE, 15));
		} else {
			answer = new Label("Ви впевнені, що хочете продати " + name + "?", Font.getInstance().generateBitmapFont(Color.WHITE, 20));
			cost = new Label("Вартість продажу: " + costValue + " золота", Font.getInstance().generateBitmapFont(Color.WHITE, 15));
		}
		
		answer.setPosition(background.getX() + background.getWidth() / 2 - answer.getWidth() / 2, background.getVertices()[Batch.Y2] - 20);
		cost.setPosition(background.getX() + background.getWidth() / 2 - cost.getWidth() / 2, answer.getY() - 30);
		
		yes = new TextButton("Так", Objects.getInstance().getButtonBitmapFont());
		yes.setSize(100, 50);
		yes.setTexture(Objects.getInstance().getButtonTexture());
		yes.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		yes.setTextureOver(Objects.getInstance().getButtonOverTexture());
		yes.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		yes.setPosition(background.getX() + background.getWidth() / 2 - yes.getWidth() - 20, background.getY() + yes.getHeight() / 2);
		yes.setAlpha(alpha);
		
		no = new TextButton("Ні", Objects.getInstance().getButtonBitmapFont());
		no.setSize(100, 50);
		no.setTexture(Objects.getInstance().getButtonTexture());
		no.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		no.setTextureOver(Objects.getInstance().getButtonOverTexture());
		no.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		no.setPosition(background.getX() + background.getWidth() / 2 + 20, background.getY() + no.getHeight() / 2);
		no.setAlpha(alpha);
		
		this.type = type;
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
		border.setAlpha(alpha);
		answer.setAlphas(alpha);
		cost.setAlphas(alpha);
		yes.setAlpha(alpha);
		no.setAlpha(alpha);
		
		batch.begin();
		background.draw(batch);
		
		border.setSize(background.getWidth(), 2);
		border.setPosition(background.getX(), background.getY());
		border.draw(batch);
		
		border.setSize(background.getWidth(), 2);
		border.setPosition(background.getX(), background.getY() + background.getHeight() - border.getHeight());
		border.draw(batch);
		
		border.setSize(2, background.getHeight());
		border.setPosition(background.getX(), background.getY());
		border.draw(batch);
		
		border.setSize(2, background.getHeight());
		border.setPosition(background.getX() + background.getWidth() - border.getWidth(), background.getY());
		border.draw(batch);
		
		answer.draw(batch);
		cost.draw(batch);
		
		yes.show(batch);
		no.show(batch);
		
		batch.end();
	}
	
	public int getType () {
		return type;
	}
	
	public boolean isYes () {
		return yes.isReleased();
	}
	
	public boolean isNo () {
		return no.isReleased();
	}

	@Override
	public void dispose () {
		background.getTexture().dispose();
		border.getTexture().dispose();
		answer.getFont().dispose();
		cost.getFont().dispose();
	}
}
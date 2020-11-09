package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import gui.Label;
import gui.TextButton;
import util.CameraController;
import util.Font;

public class ExitFromGameForm implements Disposable {
	private Sprite background;
	private Label caption;
	private Label ask;
	private TextButton yesButton;
	private TextButton noButton;
	
	private float alpha = 0.0f;
	private boolean isExit = false;
	private boolean isCancel = false;
	private boolean isHide = false;
	
	public ExitFromGameForm () {
		background = new Sprite(new Texture(Gdx.files.internal("images/primary forms/exitFromGame.png")));
		background.setSize(600, 300);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2,
				CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2);
		background.setAlpha(alpha);
		
		BitmapFont bitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 40);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		caption = new Label("Вихід з гри", bitmapFont);
		caption.setPosition(background.getX() + 10, background.getVertices()[Batch.Y2] - 13);
		caption.setAlphas(alpha);
		
		bitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 30);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		ask = new Label("Ви впевнені, що хочете вийти з гри?", bitmapFont);
		ask.setPosition(background.getX() + background.getWidth() / 2 - ask.getWidth() / 2, background.getY() + background.getHeight() / 2);
		ask.setAlphas(alpha);
		
		Texture texture = new Texture(Gdx.files.internal("images/gui/button.png"));
		Texture textureFocused = new Texture(Gdx.files.internal("images/gui/buttonFocused.png"));
		Texture textureOver = new Texture(Gdx.files.internal("images/gui/buttonOver.png"));
		
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureFocused.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		BitmapFont bitmapFontFocused = Font.getInstance().generateBitmapFont(Color.BLACK, 30);
		bitmapFontFocused.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		yesButton = new TextButton("Так", bitmapFont);
		yesButton.setSize(100, 50);
		yesButton.setTexture(texture);
		yesButton.setTextureFocused(textureFocused);
		yesButton.setTextureOver(textureOver);
		yesButton.setBitmapFontFocused(bitmapFontFocused);
		yesButton.setPosition(background.getX() + background.getWidth() / 2 - yesButton.getWidth() - 20, background.getY() + yesButton.getHeight() / 2);
		yesButton.setAlpha(alpha);
		
		noButton = new TextButton("Ні", bitmapFont);
		noButton.setSize(100, 50);
		noButton.setTexture(texture);
		noButton.setTextureFocused(textureFocused);
		noButton.setTextureOver(textureOver);
		noButton.setBitmapFontFocused(bitmapFontFocused);
		noButton.setPosition(background.getX() + background.getWidth() / 2 + 20, background.getY() + yesButton.getHeight() / 2);
		noButton.setAlpha(alpha);
	}
	
	public void show (SpriteBatch batch) {
		batch.begin();
		background.draw(batch);
		caption.draw(batch);
		ask.draw(batch);
		yesButton.show(batch);
		noButton.show(batch);
		batch.end();
		
		if (isHide) {
			alpha -= Gdx.graphics.getDeltaTime() * 5;
			if (alpha < 0.0f) alpha = 0.0f;
		} else {
			alpha += Gdx.graphics.getDeltaTime() * 2;
			if (alpha > 1.0f) alpha = 1.0f;
		}
		
		background.setAlpha(alpha);
		caption.setAlphas(alpha);
		ask.setAlphas(alpha);
		yesButton.setAlpha(alpha);
		noButton.setAlpha(alpha);
		
		if (!isHide) {
			if (yesButton.isReleased()) isExit = true;
			else if (noButton.isReleased()) isCancel = true;	
		}
	}
	
	public boolean isExit () {
		return isExit;
	}
	
	public boolean isCancel () {
		return isCancel;
	}
	
	public void hide () {
		isHide = true;
	}
	
	public float getAlpha () {
		return alpha;
	}

	@Override
	public void dispose () {
		background.getTexture().dispose();
		caption.getFont().dispose();
		ask.getFont().dispose();
		yesButton.getTexture().dispose();
		yesButton.getTextureFocused().dispose();
		yesButton.getTextureOver().dispose();
		yesButton.getLabelFocusedBitmapFont().dispose();
	}
}
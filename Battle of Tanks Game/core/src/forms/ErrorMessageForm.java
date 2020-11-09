package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Objects;

import gui.Label;
import gui.TextButton;
import util.CameraController;
import util.Font;

public class ErrorMessageForm implements Disposable {
	private Sprite background;
	private Label caption;
	private Label message;
	private TextButton ok;
	
	private float alpha = 0.0f;
	private boolean isHide = false;
	private boolean isOk = false;
	
	public ErrorMessageForm (float width, float height, String text) {
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/primary forms/errorMessageForm.png"));
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Sprite(backgroundTexture);
		background.setSize(width, height);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2,
				CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2);
		
		BitmapFont bitmapFontCaption = Font.getInstance().generateBitmapFont(Color.RED, 40);
		bitmapFontCaption.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		BitmapFont bitmapFontMessage = Font.getInstance().generateBitmapFont(Color.RED, 20);
		bitmapFontMessage.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		caption = new Label("Помилка", bitmapFontCaption);
		caption.setPosition(background.getX() + 5, background.getY() + background.getHeight() - 10);
		
		message = new Label(text, bitmapFontMessage);
		message.setPosition(background.getX() + background.getWidth() / 2 - message.getWidth() / 2, background.getY() + background.getHeight() / 2 + caption.getHeight() / 2);
		
		ok = new TextButton("ОК", Objects.getInstance().getButtonBitmapFont());
		ok.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		ok.setTexture(Objects.getInstance().getButtonTexture());
		ok.setTextureOver(Objects.getInstance().getButtonOverTexture());
		ok.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		ok.setSize(100, 50);
		ok.setPosition(background.getX() + background.getWidth() / 2 - ok.getWidth() / 2, background.getY() + 10);
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
		caption.setAlphas(alpha);
		message.setAlphas(alpha);
		ok.setAlpha(alpha);
		
		background.draw(batch);
		caption.draw(batch);
		message.draw(batch);
		ok.show(batch);
		
		if (ok.isReleased()) {
			isOk = true;
		}
	}
	
	public void hide () {
		isHide = true;
	}
	
	public boolean isOk () {
		return isOk;
	}
	
	public float getAlpha () {
		return alpha;
	}

	@Override
	public void dispose () {
		background.getTexture().dispose();
		caption.getFont().dispose();
		message.getFont().dispose();
	}
}
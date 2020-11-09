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
import com.mygdx.game.Objects;

import gui.Label;
import gui.TextButton;
import util.CameraController;
import util.Font;

public class SuccessfulySignUpForm implements Disposable {
	private Sprite background;
	private Label label1;
	private Label label2;
	private TextButton ok;
	
	private boolean isHide = false;
	private boolean isOk = false;
	private float alpha = 0.0f;
	
	public SuccessfulySignUpForm () {
		Texture texture = new Texture(Gdx.files.internal("images/primary forms/message.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Sprite(texture);
		background.setSize(500, 150);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2, CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2);
		
		BitmapFont bitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		label1 = new Label("Реєстрація пройшла успішно!", bitmapFont);
		label1.setPosition(background.getX() + background.getWidth() / 2 - label1.getWidth() / 2, background.getVertices()[Batch.Y2] - 20);
		
		label2 = new Label("На ваш email був надіслений код підтвердження", bitmapFont);
		label2.setPosition(background.getX() + background.getWidth() / 2 - label2.getWidth() / 2, label1.getY() - label1.getHeight() - 10);
		
		ok = new TextButton("ОК", Objects.getInstance().getButtonBitmapFont());
		ok.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		ok.setTexture(Objects.getInstance().getButtonTexture());
		ok.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		ok.setTextureOver(Objects.getInstance().getButtonOverTexture());
		ok.setSize(100, 50);
		ok.setPosition(background.getX() + background.getWidth() / 2 - ok.getWidth() / 2, background.getY() + 20);
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
		label1.setAlphas(alpha);
		label2.setAlphas(alpha);
		ok.setAlpha(alpha);
		
		background.draw(batch);
		label1.draw(batch);
		label2.draw(batch);
		ok.show(batch);
		
		if (ok.isReleased()) {
			isOk = true;
		}
	}
	
	public void hide () {
		isHide = true;
	}
	
	public float getAlpha () {
		return alpha;
	}
	
	public boolean isOk () {
		return isOk;
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		label1.getFont().dispose();
	}
}
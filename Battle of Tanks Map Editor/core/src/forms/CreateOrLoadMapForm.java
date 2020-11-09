package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.editor.Objects;

import gui.Label;
import gui.TextButton;
import util.CameraController;
import util.Font;

public class CreateOrLoadMapForm implements Disposable {
	private Sprite background;
	private Label caption;
	private TextButton createButton;
	private TextButton openButton;
	private InputMultiplexer inputMultiplexer;
	
	private CreatingMapForm creatingMapForm;
	private FileChooserForm loadingMapForm;
	private Stage stage;
	
	private float alpha = 0.0f;
	private boolean isHide = false;
	private boolean isCreate = false;
	private boolean isOpen = false;
	
	public CreateOrLoadMapForm () {
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/form.png"));
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Sprite(backgroundTexture);
		background.setSize(500, 150);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2,
				CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2);
		
		BitmapFont captionBitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 30);
		captionBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		caption = new Label("Редактор карт \"Battle of Tanks\"", captionBitmapFont);
		caption.setPosition(background.getX() + background.getWidth() / 2 - caption.getWidth() / 2, background.getVertices()[Batch.Y2] - 10);
		
		createButton = new TextButton("Создать", Objects.getInstance().getButtonBitmapFont());
		createButton.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		createButton.setTexture(Objects.getInstance().getButtonTexture());
		createButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		createButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		createButton.setSize(200, 50);
		createButton.setPosition(background.getX() + 10, background.getY() + 10);
		
		openButton = new TextButton("Открыть", Objects.getInstance().getButtonBitmapFont());
		openButton.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		openButton.setTexture(Objects.getInstance().getButtonTexture());
		openButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		openButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		openButton.setSize(200, 50);
		openButton.setPosition(background.getX() + background.getWidth() - openButton.getWidth() - 10, background.getY() + 10);
		
		stage = new Stage();
		
		inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		inputMultiplexer.addProcessor(stage);
	}
	
	public void show (SpriteBatch batch) {
		if (isHide) {
			alpha -= Gdx.graphics.getDeltaTime() * 5;
			if (alpha < 0.0f) alpha = 0.0f;
		} else {
			alpha += Gdx.graphics.getDeltaTime() * 2;
			if (alpha > 1.0f) alpha = 1.0f;
		}
		
		batch.begin();
		
		background.setAlpha(alpha);
		caption.setAlphas(alpha);
		createButton.setAlpha(alpha);
		openButton.setAlpha(alpha);
		
		background.draw(batch);
		caption.draw(batch);
		createButton.show(batch);
		openButton.show(batch);
		
		if (createButton.isReleased()) {
			if (creatingMapForm == null && loadingMapForm == null) creatingMapForm = new CreatingMapForm(stage);
		} else if (openButton.isReleased()) {
			if (loadingMapForm == null && creatingMapForm == null) loadingMapForm = new FileChooserForm();
		}
		
		if (creatingMapForm != null) {
			creatingMapForm.show(batch);
			if (creatingMapForm.isCancel()) {
				creatingMapForm.hide();
				if (creatingMapForm.getAlpha() == 0.0f) {
					creatingMapForm.dispose();
					creatingMapForm = null;
				}
			} else if (creatingMapForm.isCreate()) {
				isCreate = true;
			}
		}
		
		batch.end();
		

		if (loadingMapForm != null) {
			loadingMapForm.show(batch);
			if (loadingMapForm.isCancel()) {
				loadingMapForm.dispose();
				loadingMapForm = null;
			} else if (loadingMapForm.isOpen()) {
				isOpen = true;
			}
		}
		
		stage.act();
		stage.draw();
	}
	
	public void hide () {
		isHide = true;
	}
	
	public float getAlpha () {
		return alpha;
	}
	
	public boolean isCreate () {
		return isCreate;
	}
	
	public boolean isOpen () {
		return isOpen;
	}
	
	public CreatingMapForm getCreatingMapForm () {
		return creatingMapForm;
	}
	
	public FileChooserForm getLoadingMapForm () {
		return loadingMapForm;
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		caption.getFont().dispose();
		stage.clear();
		stage.dispose();
		inputMultiplexer.removeProcessor(stage);
		if (loadingMapForm != null) loadingMapForm.dispose();
		if (creatingMapForm != null) creatingMapForm.dispose();
	}
}
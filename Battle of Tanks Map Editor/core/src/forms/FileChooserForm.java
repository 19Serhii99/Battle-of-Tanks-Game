package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.editor.Objects;

import gui.CustomTextField;
import gui.TextButton;
import util.CameraController;

public class FileChooserForm implements Disposable {
	private Sprite background;
	private Stage stage;
	private InputMultiplexer inputMultiplexer;
	private TextButton openButton;
	private TextButton cancelButton;
	private CustomTextField fileField;
	
	private boolean isCancel = false;
	private boolean isOpen = false;
	
	public FileChooserForm () {
		background = new Sprite(new Texture(Gdx.files.internal("images/form.png")));
		background.setSize(600, 200);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2, CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2);
		
		stage = new Stage();
		inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		inputMultiplexer.addProcessor(stage);
		
		openButton = new TextButton("Открыть", Objects.getInstance().getButtonBitmapFont());
		openButton.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		openButton.setTexture(Objects.getInstance().getButtonTexture());
		openButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		openButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		openButton.setSize(200, 50);
		openButton.setPosition(background.getX() + 20, background.getY() + 20);
		
		cancelButton = new TextButton("Отменить", Objects.getInstance().getButtonBitmapFont());
		cancelButton.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		cancelButton.setTexture(Objects.getInstance().getButtonTexture());
		cancelButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		cancelButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		cancelButton.setSize(200, 50);
		cancelButton.setPosition(background.getVertices()[Batch.X4] - cancelButton.getWidth() - 20, background.getY() + 20);
		
		fileField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		fileField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		fileField.getBackground().setSize(500, 50);
		fileField.getBackground().setPosition(background.getX() + 50, background.getVertices()[Batch.Y2] - 100);
		fileField.getTextField().setSize(482, 50);
		fileField.getTextField().setPosition(background.getX() + 60, background.getVertices()[Batch.Y2] - 94);
		fileField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					fileField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					fileField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		fileField.getTextField().setAlignment(Align.center);
		
		stage.addActor(fileField.getTextField());
	}
	
	public void show (SpriteBatch batch) {
		batch.begin();
		
		background.draw(batch);
		openButton.show(batch);
		cancelButton.show(batch);
		fileField.getBackground().draw(batch);
		
		if (cancelButton.isReleased()) {
			isCancel = true;
		} else if (openButton.isReleased()) {
			isOpen = true;
		}
		
		batch.end();
		
		stage.act();
		stage.draw();
	}
	
	public boolean isCancel () {
		return isCancel;
	}
	
	public boolean isOpen () {
		return isOpen;
	}
	
	public String getPath () {
		return fileField.getTextField().getText().trim();
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		inputMultiplexer.removeProcessor(stage);
		stage.dispose();
	}
}
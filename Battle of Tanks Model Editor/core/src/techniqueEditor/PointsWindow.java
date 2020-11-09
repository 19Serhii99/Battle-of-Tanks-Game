package techniqueEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Editor;

import util.TextureCreator;

public class PointsWindow implements Disposable {
	private Sprite background;
	private Array <TextButton> buttons;
	
	private boolean isSelected;
	private int id;
	
	public PointsWindow (final Stage stage, TextButtonStyle textButtonStyle, Array <GunPoint> gunPoints) {
		background = new Sprite(TextureCreator.createTexture(Color.BROWN));
		background.setSize(300, 500);
		background.setPosition(Editor.getCamera().viewportWidth / 2 - background.getWidth() / 2, Editor.getCamera().viewportHeight / 2 - background.getHeight() / 2);
		
		buttons = new Array <TextButton>(gunPoints.size);
		
		float buttonHeight = 40;
		float tempY = background.getVertices()[Batch.Y2] - buttonHeight - 5;
		for (int i = 0; i < gunPoints.size; i++) {
			final int index = i;
			final TextButton textButton = new TextButton("Point - " + i, textButtonStyle);
			textButton.setSize(140, buttonHeight);
			textButton.setPosition(background.getX() + background.getWidth() / 2 - textButton.getWidth() / 2, tempY);
			textButton.addListener(new InputListener() {
				@Override
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
				@Override
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
					if (button == Buttons.LEFT && cursor.x >= textButton.getX() && cursor.x <= textButton.getX() + textButton.getWidth()
					&& cursor.y >= textButton.getY() && cursor.y <= textButton.getY() + textButton.getHeight()) {
						isSelected = true;
						id = index;
					}
				}
			});
			tempY -= buttonHeight + 5;
			stage.addActor(textButton);
			buttons.add(textButton);
		}
	}
	
	public void show (SpriteBatch batch) {
		background.draw(batch);
	}
	
	public boolean isSelected () {
		return isSelected;
	}
	
	public int getId () {
		return id;
	}

	@Override
	public void dispose () {
		background.getTexture().dispose();
		for (TextButton button : buttons) {
			button.remove();
		}
	}	
}

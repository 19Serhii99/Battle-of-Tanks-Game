package techniqueEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
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

public class ViewTowersWindow implements Disposable {
	private Sprite background;
	private Array <TextButton> textButtons;
	
	private boolean isSelected;
	private int positionTower;
	
	public ViewTowersWindow (TechniqueWorkspace techniqueWorkspace, TextButtonStyle textButtonStyle, final Stage stage) {
		background = new Sprite(TextureCreator.createTexture(Color.BROWN));
		background.setSize(300, 500);
		background.setPosition(Editor.getCamera().viewportWidth / 2 - background.getWidth() / 2, Editor.getCamera().viewportHeight / 2 - background.getHeight() / 2);
		
		textButtons = new Array <TextButton>(techniqueWorkspace.getBlockTowers().size);
		
		float buttonHeight = 50;
		
		float tempY = background.getY() + background.getHeight() - buttonHeight - 10;
		
		for (BlockTower blockTower : techniqueWorkspace.getBlockTowers()) {
			final TextButton textButton = new TextButton("Tower - " + blockTower.getPosition(), textButtonStyle);		
			textButton.setSize(200, buttonHeight);
			textButton.setPosition(background.getX() + background.getWidth() / 2 - textButton.getWidth() / 2, tempY);
			stage.addActor(textButton);
			
			final int tempPosition = blockTower.getPosition();
			
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
						positionTower = tempPosition;
					}
				}
			});
			textButtons.add(textButton);
			tempY -= buttonHeight + 10;
		}
	}
	
	public void show (SpriteBatch batch) {
		background.draw(batch);
	}
	
	public boolean isSelected () {
		return isSelected;
	}
	
	public int getPositionTower () {
		return positionTower;
	}

	@Override
	public void dispose () {
		background.getTexture().dispose();
		for (TextButton textButton : textButtons) {
			textButton.remove();
		}
	}
}
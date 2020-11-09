package techniqueEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Editor;

import util.Font;

public class TechniqueEditor implements Disposable {
	private TextButtonStyle textButtonStyle;
	private TextButton createButton;
	private TextButton openButton;
	private TextButton backButton;
	private TextButton insertTowerButton;
	private BitmapFont bitmapFont;
	private FreeTypeFontParameter freeTypeFontParameter;
	private Skin skin;
	private TechniqueWorkspace techniqueWorkspace;
	private TechniqueProperties techniqueProperties;
	
	public TechniqueEditor (final Stage stage, final Skin skin) {
		this.skin = skin;
		
		techniqueWorkspace = new TechniqueWorkspace();
		
		freeTypeFontParameter = new FreeTypeFontParameter();
		freeTypeFontParameter.size = 20;
		
		bitmapFont = Font.getFont().generateBitmapFont(freeTypeFontParameter);
		
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = this.skin.getDrawable("button");
		textButtonStyle.over = this.skin.getDrawable("buttonActivated");
		textButtonStyle.font = bitmapFont;
		textButtonStyle.fontColor = Color.WHITE;
		
		createButton = new TextButton("Create", textButtonStyle);
		createButton.setSize(100, 40);
		createButton.setPosition(10, Editor.getCamera().viewportHeight - createButton.getHeight() - 10);
		createButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= createButton.getX() && cursor.x <= createButton.getX() + createButton.getWidth()
				&& cursor.y >= createButton.getY() && cursor.y <= createButton.getY() + createButton.getHeight()) {
					createButton.setVisible(false);
					openButton.setVisible(false);
					techniqueWorkspace.create();
					techniqueProperties = new TechniqueProperties(techniqueWorkspace, stage, skin, backButton, insertTowerButton);
					insertTowerButton.setVisible(true);
				}
			}
		});
		
		openButton = new TextButton("Open", textButtonStyle);
		openButton.setSize(100, 40);
		openButton.setPosition(300, Editor.getCamera().viewportHeight - openButton.getHeight() - 10);
		openButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= openButton.getX() && cursor.x <= openButton.getX() + openButton.getWidth()
				&& cursor.y >= openButton.getY() && cursor.y <= openButton.getY() + openButton.getHeight()) {
					
				}
			}
		});
		
		backButton = new TextButton("Back", textButtonStyle);
		backButton.setSize(100, 40);
		backButton.setPosition(Editor.getCamera().viewportWidth - backButton.getWidth() - 10, Editor.getCamera().viewportHeight - backButton.getHeight() - 10);
		backButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= backButton.getX() && cursor.x <= backButton.getX() + backButton.getWidth()
				&& cursor.y >= backButton.getY() && cursor.y <= backButton.getY() + backButton.getHeight()) {
					
				}
			}
		});
		
		insertTowerButton = new TextButton("Insert tower", textButtonStyle);
		insertTowerButton.setSize(150, 40);
		insertTowerButton.setPosition(createButton.getX(), createButton.getY());
		insertTowerButton.setVisible(false);
		insertTowerButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));			
				if (button == Buttons.LEFT && cursor.x >= insertTowerButton.getX() && cursor.x <= insertTowerButton.getX() + insertTowerButton.getWidth()
				&& cursor.y >= insertTowerButton.getY() && cursor.y <= insertTowerButton.getY() + insertTowerButton.getHeight()) {
					techniqueWorkspace.addTower();
				}
			}
		});
		
		stage.addActor(createButton);
		stage.addActor(openButton);
		stage.addActor(backButton);
		stage.addActor(insertTowerButton);
	}
	
	public void show (SpriteBatch batch) {
		techniqueWorkspace.show(batch);
		if (techniqueProperties != null) {
			techniqueProperties.show(batch);
		}
	}

	@Override
	public void dispose () {
		techniqueWorkspace.dispose();
		bitmapFont.dispose();
	}
}
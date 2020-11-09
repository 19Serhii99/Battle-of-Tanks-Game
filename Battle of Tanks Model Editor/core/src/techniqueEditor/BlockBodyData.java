package techniqueEditor;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

import util.Font;

public class BlockBodyData extends BlockData implements Disposable {
	private BitmapFontCache speedRotationLabel;
	private BitmapFontCache nameLabel;
	private BitmapFontCache techniqueTypeLabel;
	private BitmapFontCache maxSpeedLabel;
	private BitmapFontCache accelerationLabel;
	private BitmapFontCache maxHealthLabel;
	
	private TextField speedRotationField;
	private TextField nameField;
	private TextField techniqueTypeField;
	private TextField maxSpeedField;
	private TextField accelerationField;
	private TextField maxHealthField;
	
	public BlockBodyData (BitmapFont bitmapFont, TextFieldStyle textFieldStyle, float x, float y, Stage stage) {
		super(bitmapFont, textFieldStyle, x, y, stage);
		
		speedRotationLabel = new BitmapFontCache(bitmapFont);
		speedRotationLabel.setText("Speed rotation:", 0, 0);
		speedRotationLabel.setPosition(x, moneyLabel.getY() - shiftY);
		
		nameLabel = new BitmapFontCache(bitmapFont);
		nameLabel.setText("Name:", 0, 0);
		nameLabel.setPosition(x, speedRotationLabel.getY() - shiftY);
		
		techniqueTypeLabel = new BitmapFontCache(bitmapFont);
		techniqueTypeLabel.setText("Technique type:", 0, 0);
		techniqueTypeLabel.setPosition(x, nameLabel.getY() - shiftY);
		
		maxSpeedLabel = new BitmapFontCache(bitmapFont);
		maxSpeedLabel.setText("Max speed:", 0, 0);
		maxSpeedLabel.setPosition(x, techniqueTypeLabel.getY() - shiftY);
		
		accelerationLabel = new BitmapFontCache(bitmapFont);
		accelerationLabel.setText("Acceleration:", 0, 0);
		accelerationLabel.setPosition(x, maxSpeedLabel.getY() - shiftY);
		
		maxHealthLabel = new BitmapFontCache(bitmapFont);
		maxHealthLabel.setText("Max health:", 0, 0);
		maxHealthLabel.setPosition(x, accelerationLabel.getY() - shiftY);
		
		speedRotationField = new TextField("0.0", textFieldStyle);
		speedRotationField.setAlignment(Align.center);
		speedRotationField.setSize(240, 25);
		speedRotationField.setPosition(x + 200, speedRotationLabel.getY() - Font.getFont().getHeight(bitmapFont, "Speed rotation:") - 5);
		
		nameField = new TextField("", textFieldStyle);
		nameField.setAlignment(Align.center);
		nameField.setSize(240, 25);
		nameField.setPosition(x + 200, nameLabel.getY() - Font.getFont().getHeight(bitmapFont, "Name:") - 5);
		
		techniqueTypeField = new TextField("", textFieldStyle);
		techniqueTypeField.setAlignment(Align.center);
		techniqueTypeField.setSize(240, 25);
		techniqueTypeField.setPosition(x + 200, techniqueTypeLabel.getY() - Font.getFont().getHeight(bitmapFont, "Technique type:") - 5);
		
		maxSpeedField = new TextField("0.0", textFieldStyle);
		maxSpeedField.setAlignment(Align.center);
		maxSpeedField.setSize(240, 25);
		maxSpeedField.setPosition(x + 200, maxSpeedLabel.getY() - Font.getFont().getHeight(bitmapFont, "Max speed:") - 5);
		
		accelerationField = new TextField("0.0", textFieldStyle);
		accelerationField.setAlignment(Align.center);
		accelerationField.setSize(240, 25);
		accelerationField.setPosition(x + 200, accelerationLabel.getY() - Font.getFont().getHeight(bitmapFont, "Acceleration:") - 5);
		
		maxHealthField = new TextField("0", textFieldStyle);
		maxHealthField.setAlignment(Align.center);
		maxHealthField.setSize(240, 25);
		maxHealthField.setPosition(x + 200, maxHealthLabel.getY() - Font.getFont().getHeight(bitmapFont, "Max health:") - 5);
		
		stage.addActor(speedRotationField);
		stage.addActor(nameField);
		stage.addActor(techniqueTypeField);
		stage.addActor(maxSpeedField);
		stage.addActor(accelerationField);
		stage.addActor(maxHealthField);
	}
	
	public void hide () {
		super.hide();
		speedRotationField.setVisible(false);
		nameField.setVisible(false);
		techniqueTypeField.setVisible(false);
		maxSpeedField.setVisible(false);
		accelerationField.setVisible(false);
		maxHealthField.setVisible(false);
	}
	
	public void resume () {
		super.resume();
		speedRotationField.setVisible(true);
		nameField.setVisible(true);
		techniqueTypeField.setVisible(true);
		maxSpeedField.setVisible(true);
		accelerationField.setVisible(true);
		maxHealthField.setVisible(true);
	}
	
	public float getSpeedRotation () {
		return Float.parseFloat(speedRotationField.getText());
	}
	
	public String getName () {
		return nameField.getText();
	}
	
	public String getTechniqueType () {
		return techniqueTypeField.getText();
	}
	
	public float getMaxSpeed () {
		return Float.parseFloat(maxSpeedField.getText());
	}
	
	public float getAcceleration () {
		return Float.parseFloat(accelerationField.getText());
	}
	
	public float getMaxHealth () {
		return Float.parseFloat(maxHealthField.getText());
	}
	
	public void show (SpriteBatch batch) {
		super.show(batch);
		speedRotationLabel.draw(batch);
		nameLabel.draw(batch);
		techniqueTypeLabel.draw(batch);
		maxSpeedLabel.draw(batch);
		accelerationLabel.draw(batch);
		maxHealthLabel.draw(batch);
	}

	@Override
	public void dispose () {
		super.dispose();
		speedRotationField.remove();
		nameField.remove();
		techniqueTypeField.remove();
		maxSpeedField.remove();
		accelerationField.remove();
		maxHealthField.remove();
	}
}
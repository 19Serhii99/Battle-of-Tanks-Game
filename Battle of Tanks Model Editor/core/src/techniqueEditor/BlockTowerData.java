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

public class BlockTowerData extends BlockData implements Disposable {
	private BitmapFontCache timeReductionLabel;
	private BitmapFontCache minRadiusLabel;
	private BitmapFontCache maxRadiusLabel;
	private BitmapFontCache rotationLeftLabel;
	private BitmapFontCache rotationRightLabel;
	private BitmapFontCache xOriginLabel;
	private BitmapFontCache yOriginLabel;
	private BitmapFontCache timeRechargeLabel;
	private BitmapFontCache timeRechargeShellCassetteLabel;
	private BitmapFontCache experienceLabel;
	private BitmapFontCache minDamageLabel;
	private BitmapFontCache maxDamageLabel;
	private BitmapFontCache totalShellsLabel;
	private BitmapFontCache totalShellsCassetteLabel;
	private BitmapFontCache shootingStyleLabel;
	private BitmapFontCache speedRotationLabel;
	private BitmapFontCache nameLabel;
	
	private TextField timeReductionField;
	private TextField minRadiusField;
	private TextField maxRadiusField;
	private TextField rotationLeftField;
	private TextField rotationRightField;
	private TextField xOriginField;
	private TextField yOriginField;
	private TextField timeRechargeField;
	private TextField timeRechargeShellCassetteField;
	private TextField experienceField;
	private TextField minDamageField;
	private TextField maxDamageField;
	private TextField totalShellsField;
	private TextField totalShellsCassetteField;
	private TextField shootingStyleField;
	private TextField speedRotationField;
	private TextField nameField;
	
	public BlockTowerData (BitmapFont bitmapFont, TextFieldStyle textFieldStyle, float x, float y, Stage stage) {
		super(bitmapFont, textFieldStyle, x, y, stage);
		
		timeReductionLabel = new BitmapFontCache(bitmapFont);
		timeReductionLabel.setText("Time reduction:", 0, 0);
		timeReductionLabel.setPosition(x, moneyLabel.getY() - shiftY);
		
		minRadiusLabel = new BitmapFontCache(bitmapFont);
		minRadiusLabel.setText("Min radius:", 0, 0);
		minRadiusLabel.setPosition(x, timeReductionLabel.getY() - shiftY);
		
		maxRadiusLabel = new BitmapFontCache(bitmapFont);
		maxRadiusLabel.setText("Max radius:", 0, 0);
		maxRadiusLabel.setPosition(x, minRadiusLabel.getY() - shiftY);
		
		rotationLeftLabel = new BitmapFontCache(bitmapFont);
		rotationLeftLabel.setText("Rotation left:", 0, 0);
		rotationLeftLabel.setPosition(x, maxRadiusLabel.getY() - shiftY);
		
		rotationRightLabel = new BitmapFontCache(bitmapFont);
		rotationRightLabel.setText("Rotation right:", 0, 0);
		rotationRightLabel.setPosition(x, rotationLeftLabel.getY() - shiftY);
		
		xOriginLabel = new BitmapFontCache(bitmapFont);
		xOriginLabel.setText("X origin:", 0, 0);
		xOriginLabel.setPosition(x, rotationRightLabel.getY() - shiftY);
		
		yOriginLabel = new BitmapFontCache(bitmapFont);
		yOriginLabel.setText("Y origin: ", 0, 0);
		yOriginLabel.setPosition(x, xOriginLabel.getY() - shiftY);
		
		timeRechargeLabel = new BitmapFontCache(bitmapFont);
		timeRechargeLabel.setText("Time recharge:", 0, 0);
		timeRechargeLabel.setPosition(x, yOriginLabel.getY() - shiftY);
		
		timeRechargeShellCassetteLabel = new BitmapFontCache(bitmapFont);
		timeRechargeShellCassetteLabel.setText("Time recharge shell:", 0, 0);
		timeRechargeShellCassetteLabel.setPosition(x, timeRechargeLabel.getY() - shiftY);
		
		experienceLabel = new BitmapFontCache(bitmapFont);
		experienceLabel.setText("Experience:", 0, 0);
		experienceLabel.setPosition(x, timeRechargeShellCassetteLabel.getY() - shiftY);
		
		minDamageLabel = new BitmapFontCache(bitmapFont);
		minDamageLabel.setText("Min damage:", 0, 0);
		minDamageLabel.setPosition(x, experienceLabel.getY() - shiftY);
		
		maxDamageLabel = new BitmapFontCache(bitmapFont);
		maxDamageLabel.setText("Max damage:", 0, 0);
		maxDamageLabel.setPosition(x, minDamageLabel.getY() - shiftY);
		
		totalShellsLabel = new BitmapFontCache(bitmapFont);
		totalShellsLabel.setText("Total shells:", 0, 0);
		totalShellsLabel.setPosition(x, maxDamageLabel.getY() - shiftY);
		
		totalShellsCassetteLabel = new BitmapFontCache(bitmapFont);
		totalShellsCassetteLabel.setText("Total shells cassette:", 0, 0);
		totalShellsCassetteLabel.setPosition(x, totalShellsLabel.getY() - shiftY);
		
		shootingStyleLabel = new BitmapFontCache(bitmapFont);
		shootingStyleLabel.setText("Shooting style:", 0, 0);
		shootingStyleLabel.setPosition(positionLabel.getX(), positionLabel.getY());
		
		speedRotationLabel = new BitmapFontCache(bitmapFont);
		speedRotationLabel.setText("Speed rotation:", 0, 0);
		speedRotationLabel.setPosition(xLabel.getX(), xLabel.getY());
		
		nameLabel = new BitmapFontCache(bitmapFont);
		nameLabel.setText("Name:", 0, 0);
		nameLabel.setPosition(yLabel.getX(), yLabel.getY());
		
		timeReductionField = new TextField("", textFieldStyle);
		timeReductionField.setAlignment(Align.center);
		timeReductionField.setSize(240, 25);
		timeReductionField.setPosition(x + 200, timeReductionLabel.getY() - Font.getFont().getHeight(bitmapFont, "Time reduction:") - 5);
		
		minRadiusField = new TextField("", textFieldStyle);
		minRadiusField.setAlignment(Align.center);
		minRadiusField.setSize(240, 25);
		minRadiusField.setPosition(x + 200, minRadiusLabel.getY() - Font.getFont().getHeight(bitmapFont, "Min radius:") - 5);
		
		maxRadiusField = new TextField("", textFieldStyle);
		maxRadiusField.setAlignment(Align.center);
		maxRadiusField.setSize(240, 25);
		maxRadiusField.setPosition(x + 200, maxRadiusLabel.getY() - Font.getFont().getHeight(bitmapFont, "Max radius:") - 5);
		
		rotationLeftField = new TextField("", textFieldStyle);
		rotationLeftField.setAlignment(Align.center);
		rotationLeftField.setSize(240, 25);
		rotationLeftField.setPosition(x + 200, rotationLeftLabel.getY() - Font.getFont().getHeight(bitmapFont, "Rotation left:") - 5);
		
		rotationRightField = new TextField("", textFieldStyle);
		rotationRightField.setAlignment(Align.center);
		rotationRightField.setSize(240, 25);
		rotationRightField.setPosition(x + 200, rotationRightLabel.getY() - Font.getFont().getHeight(bitmapFont, "Rotation right:") - 5);
		
		xOriginField = new TextField("", textFieldStyle);
		xOriginField.setAlignment(Align.center);
		xOriginField.setSize(240, 25);
		xOriginField.setPosition(x + 200, xOriginLabel.getY() - Font.getFont().getHeight(bitmapFont, "X origin:") - 5);
		
		yOriginField = new TextField("", textFieldStyle);
		yOriginField.setAlignment(Align.center);
		yOriginField.setSize(240, 25);
		yOriginField.setPosition(x + 200, yOriginLabel.getY() - Font.getFont().getHeight(bitmapFont, "Y origin:") - 5);
		
		timeRechargeField = new TextField("", textFieldStyle);
		timeRechargeField.setAlignment(Align.center);
		timeRechargeField.setSize(240, 25);
		timeRechargeField.setPosition(x + 200, timeRechargeLabel.getY() - Font.getFont().getHeight(bitmapFont, "Time recharge:") - 5);
		
		timeRechargeShellCassetteField = new TextField("", textFieldStyle);
		timeRechargeShellCassetteField.setAlignment(Align.center);
		timeRechargeShellCassetteField.setSize(240, 25);
		timeRechargeShellCassetteField.setPosition(x + 200, timeRechargeShellCassetteLabel.getY() - Font.getFont().getHeight(bitmapFont, "Time recharge shell cassette:") - 5);
		
		experienceField = new TextField("", textFieldStyle);
		experienceField.setAlignment(Align.center);
		experienceField.setSize(240, 25);
		experienceField.setPosition(x + 200, experienceLabel.getY() - Font.getFont().getHeight(bitmapFont, "Experience:") - 5);
		
		minDamageField = new TextField("", textFieldStyle);
		minDamageField.setAlignment(Align.center);
		minDamageField.setSize(240, 25);
		minDamageField.setPosition(x + 200, minDamageLabel.getY() - Font.getFont().getHeight(bitmapFont, "Min damage:") - 5);
		
		maxDamageField = new TextField("", textFieldStyle);
		maxDamageField.setAlignment(Align.center);
		maxDamageField.setSize(240, 25);
		maxDamageField.setPosition(x + 200, maxDamageLabel.getY() - Font.getFont().getHeight(bitmapFont, "Max damage:") - 5);
		
		totalShellsField = new TextField("", textFieldStyle);
		totalShellsField.setAlignment(Align.center);
		totalShellsField.setSize(240, 25);
		totalShellsField.setPosition(x + 200, totalShellsLabel.getY() - Font.getFont().getHeight(bitmapFont, "Total shells:") - 5);
		
		totalShellsCassetteField = new TextField("", textFieldStyle);
		totalShellsCassetteField.setAlignment(Align.center);
		totalShellsCassetteField.setSize(240, 25);
		totalShellsCassetteField.setPosition(x + 200, totalShellsCassetteLabel.getY() - Font.getFont().getHeight(bitmapFont, "Total shells cassette:") - 5);
		
		shootingStyleField = new TextField("", textFieldStyle);
		shootingStyleField.setAlignment(Align.center);
		shootingStyleField.setSize(240, 25);
		shootingStyleField.setPosition(x + 200, shootingStyleLabel.getY() - Font.getFont().getHeight(bitmapFont, "Shooting style:") - 5);
		
		speedRotationField = new TextField("", textFieldStyle);
		speedRotationField.setAlignment(Align.center);
		speedRotationField.setSize(240, 25);
		speedRotationField.setPosition(x + 200, speedRotationLabel.getY() - Font.getFont().getHeight(bitmapFont, "Speed rotation:") - 5);
		
		nameField = new TextField("", textFieldStyle);
		nameField.setAlignment(Align.center);
		nameField.setSize(240, 25);
		nameField.setPosition(x + 200, nameLabel.getY() - Font.getFont().getHeight(bitmapFont, "Name:") - 5);
		
		stage.addActor(timeReductionField);
		stage.addActor(minRadiusField);
		stage.addActor(maxRadiusField);
		stage.addActor(rotationLeftField);
		stage.addActor(rotationRightField);
		stage.addActor(xOriginField);
		stage.addActor(yOriginField);
		stage.addActor(timeRechargeField);
		stage.addActor(timeRechargeShellCassetteField);
		stage.addActor(experienceField);
		stage.addActor(minDamageField);
		stage.addActor(maxDamageField);
		stage.addActor(totalShellsField);
		stage.addActor(totalShellsCassetteField);
		stage.addActor(shootingStyleField);
		stage.addActor(speedRotationField);
		stage.addActor(nameField);
	}
	
	public void show (SpriteBatch batch) {
		super.show(batch);
		if (isFirstPage) {
			timeReductionLabel.draw(batch);
			minRadiusLabel.draw(batch);
			maxRadiusLabel.draw(batch);
			rotationLeftLabel.draw(batch);
			rotationRightLabel.draw(batch);
			xOriginLabel.draw(batch);
			yOriginLabel.draw(batch);
			timeRechargeLabel.draw(batch);
			timeRechargeShellCassetteLabel.draw(batch);
			experienceLabel.draw(batch);
			minDamageLabel.draw(batch);
			maxDamageLabel.draw(batch);
			totalShellsLabel.draw(batch);
			totalShellsCassetteLabel.draw(batch);
		} else if (isSecondPage) {
			shootingStyleLabel.draw(batch);
			speedRotationLabel.draw(batch);
			nameLabel.draw(batch);
		}
	}
	
	public void hide () {
		super.hide();
		timeReductionField.setVisible(false);
		minRadiusField.setVisible(false);
		maxRadiusField.setVisible(false);
		rotationLeftField.setVisible(false);
		rotationRightField.setVisible(false);
		xOriginField.setVisible(false);
		yOriginField.setVisible(false);
		timeRechargeField.setVisible(false);
		timeRechargeShellCassetteField.setVisible(false);
		experienceField.setVisible(false);
		minDamageField.setVisible(false);
		maxDamageField.setVisible(false);
		totalShellsField.setVisible(false);
		totalShellsCassetteField.setVisible(false);
		shootingStyleField.setVisible(false);
		speedRotationField.setVisible(false);
	}
	
	public void resume () {
		super.resume();
		timeReductionField.setVisible(true);
		minRadiusField.setVisible(true);
		maxRadiusField.setVisible(true);
		rotationLeftField.setVisible(true);
		rotationRightField.setVisible(true);
		xOriginField.setVisible(true);
		yOriginField.setVisible(true);
		timeRechargeField.setVisible(true);
		timeRechargeShellCassetteField.setVisible(true);
		experienceField.setVisible(true);
		minDamageField.setVisible(true);
		maxDamageField.setVisible(true);
		totalShellsField.setVisible(true);
		totalShellsCassetteField.setVisible(true);
		shootingStyleField.setVisible(true);
		speedRotationField.setVisible(true);
	}
	
	public void setFirstPage () {
		isFirstPage = true;
		isSecondPage = false;
		super.resume();
		timeReductionField.setVisible(true);
		minRadiusField.setVisible(true);
		maxRadiusField.setVisible(true);
		rotationLeftField.setVisible(true);
		rotationRightField.setVisible(true);
		xOriginField.setVisible(true);
		yOriginField.setVisible(true);
		timeRechargeField.setVisible(true);
		timeRechargeShellCassetteField.setVisible(true);
		experienceField.setVisible(true);
		minDamageField.setVisible(true);
		maxDamageField.setVisible(true);
		totalShellsField.setVisible(true);
		totalShellsCassetteField.setVisible(true);
		shootingStyleField.setVisible(false);
		speedRotationField.setVisible(false);
		nameField.setVisible(false);
	}
	
	public void setSecondPage () {
		isSecondPage = true;
		isFirstPage = false;
		super.hide();
		timeReductionField.setVisible(false);
		minRadiusField.setVisible(false);
		maxRadiusField.setVisible(false);
		rotationLeftField.setVisible(false);
		rotationRightField.setVisible(false);
		xOriginField.setVisible(false);
		yOriginField.setVisible(false);
		timeRechargeField.setVisible(false);
		timeRechargeShellCassetteField.setVisible(false);
		experienceField.setVisible(false);
		minDamageField.setVisible(false);
		maxDamageField.setVisible(false);
		totalShellsField.setVisible(false);
		totalShellsCassetteField.setVisible(false);
		shootingStyleField.setVisible(true);
		speedRotationField.setVisible(true);
		nameField.setVisible(true);
	}
	
	public void setTimeReduction (float value) {
		timeReductionField.setText(String.valueOf(value));
	}
	
	public void setMinRadius (float value) {
		minRadiusField.setText(String.valueOf(value));
	}
	
	public void setMaxRadius (float value) {
		maxRadiusField.setText(String.valueOf(value));
	}
	
	public void setRotationLeft (float value) {
		rotationLeftField.setText(String.valueOf(value));
	}
	
	public void setRotationRight (float value) {
		rotationRightField.setText(String.valueOf(value));
	}
	
	public void setXOrigin (float value) {
		xOriginField.setText(String.valueOf(value));
	}
	
	public void setYOrigin (float value) {
		yOriginField.setText(String.valueOf(value));
	}
	
	public void setTimeRecharge (float value) {
		timeRechargeField.setText(String.valueOf(value));
	}
	
	public void setTimeRechargeShellCassette (float value) {
		timeRechargeShellCassetteField.setText(String.valueOf(value));
	}
	
	public void setExperience (int value) {
		experienceField.setText(String.valueOf(value));
	}
	
	public void setMinDamage (int value) {
		minDamageField.setText(String.valueOf(value));
	}
	
	public void setMaxDamage (int value) {
		maxDamageField.setText(String.valueOf(value));
	}
	
	public void setTotalShells (int value) {
		totalShellsField.setText(String.valueOf(value));
	}
	
	public void setTotalShellsCassette (int value) {
		totalShellsCassetteField.setText(String.valueOf(value));
	}
	
	public void setShootingStyle (String value) {
		shootingStyleField.setText(value);
	}
	
	public void setSpeedRotation (float value) {
		speedRotationField.setText(String.valueOf(value));
	}
	
	public float getTimeReduction () {
		return Float.parseFloat(timeReductionField.getText());
	}
	
	public float getMinRadius () {
		return Float.parseFloat(minRadiusField.getText());
	}
	
	public float getMaxRadius () {
		return Float.parseFloat(maxRadiusField.getText());
	}
	
	public float getRotationLeft () {
		return Float.parseFloat(rotationLeftField.getText());
	}
	
	public float getRotationRight () {
		return Float.parseFloat(rotationRightField.getText());
	}
	
	public float getXOrigin () {
		return Float.parseFloat(xOriginField.getText());
	}
	
	public float getYOrigin () {
		return Float.parseFloat(yOriginField.getText());
	}
	
	public float getTimeRecharge () {
		return Float.parseFloat(timeRechargeField.getText());
	}
	
	public float getTimeRechargeShellCassette () {
		return Float.parseFloat(timeRechargeShellCassetteField.getText());
	}
	
	public int getExperience () {
		return Integer.parseInt(experienceField.getText());
	}
	
	public int getMinDamage () {
		return Integer.parseInt(minDamageField.getText());
	}
	
	public int getMaxDamage () {
		return Integer.parseInt(maxDamageField.getText());
	}
	
	public int getTotalShells () {
		return Integer.parseInt(totalShellsField.getText());
	}
	
	public int getTotalShellsCassette () {
		return Integer.parseInt(totalShellsCassetteField.getText());
	}
	
	public String getShootingStyle () {
		return shootingStyleField.getText();
	}
	
	public float getSpeedRotation () {
		return Float.parseFloat(speedRotationField.getText());
	}
	
	public String getName () {
		return nameField.getText();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		timeReductionField.remove();
		minRadiusField.remove();
		maxRadiusField.remove();
		rotationLeftField.remove();
		rotationRightField.remove();
		xOriginField.remove();
		yOriginField.remove();
		timeRechargeField.remove();
		timeRechargeShellCassetteField.remove();
		experienceField.remove();
		minDamageField.remove();
		maxDamageField.remove();
		totalShellsField.remove();
		totalShellsCassetteField.remove();
		shootingStyleField.remove();
		speedRotationField.remove();
		nameField.remove();
	}
}
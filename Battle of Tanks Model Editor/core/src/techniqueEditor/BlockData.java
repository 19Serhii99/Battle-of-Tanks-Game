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

public abstract class BlockData implements Disposable {
	protected BitmapFontCache positionLabel;
	protected BitmapFontCache xLabel;
	protected BitmapFontCache yLabel;
	protected BitmapFontCache widthLabel;
	protected BitmapFontCache heightLabel;
	protected BitmapFontCache moneyLabel;
	
	protected TextField positionField;
	protected TextField xField;
	protected TextField yField;
	protected TextField widthField;
	protected TextField heightField;
	protected TextField moneyField;
	
	protected boolean isFirstPage = true;
	protected boolean isSecondPage = false;
	
	protected static final float shiftY = 30;
	
	public BlockData (BitmapFont bitmapFont, TextFieldStyle textFieldStyle, float x, float y, Stage stage) {
		positionLabel = new BitmapFontCache(bitmapFont);
		positionLabel.setText("Position:", 0, 0);
		positionLabel.setPosition(x, y - 100);
		
		xLabel = new BitmapFontCache(bitmapFont);
		xLabel.setText("X coordinate:", 0, 0);
		xLabel.setPosition(x, positionLabel.getY() - shiftY);
		
		yLabel = new BitmapFontCache(bitmapFont);
		yLabel.setText("Y coordinate:", 0, 0);
		yLabel.setPosition(x, xLabel.getY() - shiftY);
		
		widthLabel = new BitmapFontCache(bitmapFont);
		widthLabel.setText("Width:", 0, 0);
		widthLabel.setPosition(x, yLabel.getY() - shiftY);
		
		heightLabel = new BitmapFontCache(bitmapFont);
		heightLabel.setText("Height:", 0, 0);
		heightLabel.setPosition(x, widthLabel.getY() - shiftY);
		
		moneyLabel = new BitmapFontCache(bitmapFont);
		moneyLabel.setText("Money (integer):", 0, 0);
		moneyLabel.setPosition(x, heightLabel.getY() - shiftY);
		
		positionField = new TextField("", textFieldStyle);
		positionField.setAlignment(Align.center);
		positionField.setSize(240, 25);
		positionField.setPosition(x + 200, positionLabel.getY() - Font.getFont().getHeight(bitmapFont, "Position:") - 5);
		
		xField = new TextField("", textFieldStyle);
		xField.setAlignment(Align.center);
		xField.setSize(240, 25);
		xField.setPosition(x + 200, xLabel.getY() - Font.getFont().getHeight(bitmapFont, "X coordinate:") - 5);
		
		yField = new TextField("", textFieldStyle);
		yField.setAlignment(Align.center);
		yField.setSize(240, 25);
		yField.setPosition(x + 200, yLabel.getY() - Font.getFont().getHeight(bitmapFont, "Y coordinate:") - 5);
		
		widthField = new TextField("", textFieldStyle);
		widthField.setAlignment(Align.center);
		widthField.setSize(240, 25);
		widthField.setPosition(x + 200, widthLabel.getY() - Font.getFont().getHeight(bitmapFont, "Width:") - 5);
		
		heightField = new TextField("", textFieldStyle);
		heightField.setAlignment(Align.center);
		heightField.setSize(240, 25);
		heightField.setPosition(x + 200, heightLabel.getY() - Font.getFont().getHeight(bitmapFont, "Height:") - 5);
		
		moneyField = new TextField("0", textFieldStyle);
		moneyField.setAlignment(Align.center);
		moneyField.setSize(240, 25);
		moneyField.setPosition(x + 200, moneyLabel.getY() - Font.getFont().getHeight(bitmapFont, "Money (integer):") - 5);
		
		stage.addActor(positionField);
		stage.addActor(xField);
		stage.addActor(yField);
		stage.addActor(widthField);
		stage.addActor(heightField);
		stage.addActor(moneyField);
	}
	
	public void hide () {
		positionField.setVisible(false);
		xField.setVisible(false);
		yField.setVisible(false);
		widthField.setVisible(false);
		heightField.setVisible(false);
		moneyField.setVisible(false);
	}
	
	public void resume () {
		positionField.setVisible(true);
		xField.setVisible(true);
		yField.setVisible(true);
		widthField.setVisible(true);
		heightField.setVisible(true);
		moneyField.setVisible(true);
	}
	
	public void show (SpriteBatch batch) {
		if (isFirstPage) {
			positionLabel.draw(batch);
			xLabel.draw(batch);
			yLabel.draw(batch);
			widthLabel.draw(batch);
			heightLabel.draw(batch);
			moneyLabel.draw(batch);	
		}
	}
	
	public void setX (float x) {
		xField.setText(String.valueOf(x));
	}
	
	public void setY (float y) {
		yField.setText(String.valueOf(y));
	}
	
	public void setWidth (float width) {
		widthField.setText(String.valueOf(width));
	}
	
	public void setHeight (float height) {
		heightField.setText(String.valueOf(height));
	}
	
	public int getPosition () {
		return Integer.parseInt(positionField.getText());
	}
	
	public float getX () {
		return Float.parseFloat(xField.getText());
	}
	
	public float getY () {
		return Float.parseFloat(yField.getText());
	}
	
	public float getWidth () {
		return Float.parseFloat(widthField.getText());
	}
	
	public float getHeight () {
		return Float.parseFloat(heightField.getText());
	}
	
	public int getMoney () {
		return Integer.parseInt(moneyField.getText());
	}
	
	@Override
	public void dispose () {
		positionField.remove();
		xField.remove();
		yField.remove();
		widthField.remove();
		heightField.remove();
		moneyField.remove();
	}
}
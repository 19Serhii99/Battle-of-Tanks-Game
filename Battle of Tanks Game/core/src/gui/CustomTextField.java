package gui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

public class CustomTextField {
	private TextField textField;
	private Sprite background;
	
	public CustomTextField (TextFieldStyle textFieldStyle, Stage stage) {
		textField = new TextField("", textFieldStyle);
		stage.addActor(textField);
		background = new Sprite();
	}
	
	public TextField getTextField () {
		return textField;
	}
	
	public Sprite getBackground () {
		return background;
	}
}
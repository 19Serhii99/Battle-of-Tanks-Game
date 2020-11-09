package technique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Editor;

public class Controller {
	private Model model;
	
	public Controller (Model model) {
		this.model = model;
	}
	
	public void act () {
		if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.S)) {
			if (Gdx.input.isKeyPressed(Keys.W) && !Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D)) {
				model.moveForward();
			} else if (Gdx.input.isKeyPressed(Keys.W) && Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D)) {	
				model.moveForwardLeft();				
			} else if (Gdx.input.isKeyPressed(Keys.W) && !Gdx.input.isKeyPressed(Keys.A) && Gdx.input.isKeyPressed(Keys.D)) {
				model.moveForwardRight();
			}
			if (Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D)) {
				model.moveBackward();
			} else if (Gdx.input.isKeyPressed(Keys.S) && Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D)) {
				model.moveBackwardLeft();
			} else if (Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.A) && Gdx.input.isKeyPressed(Keys.D)) {
				model.moveBackwardRight();
			}	
		} else {
			if (Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D)) {
				model.turnLeft();
			} else if (!Gdx.input.isKeyPressed(Keys.A) && Gdx.input.isKeyPressed(Keys.D)) {
				model.turnRight();
			} else {
				model.slow();
			}
		}
		
		Vector3 cursorPosition = Editor.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
		model.turnTower(cursorPosition.x, cursorPosition.y);
		
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			model.getShooting().createShell(cursorPosition.x, cursorPosition.y);
		}
	}
	
	public Model getModel () {
		return model;
	}
}
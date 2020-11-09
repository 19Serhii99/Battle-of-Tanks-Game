package technique;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class View {
	private Model model;
	
	public View (Model model) {
		this.model = model;
	}
	
	public void draw (SpriteBatch batch) {
		model.getBody().draw(batch);
		model.getTower().draw(batch);
		model.getTest().draw(batch);
		model.getTest1().draw(batch);
		
		for (Shell shell : model.getShooting().getShells()) {
			shell.moveTo();
			shell.draw(batch);
		}
	}
}

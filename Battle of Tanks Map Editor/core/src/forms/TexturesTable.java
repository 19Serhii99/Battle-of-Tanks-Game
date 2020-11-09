package forms;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.editor.Events;
import com.mygdx.editor.Properties;

import map.MapTexture;
import util.CameraController;
import util.TextureCreator;

public class TexturesTable implements Disposable {
	private Sprite background;
	private Array <TextureItem> items;
	private Vector3 cursor;
	
	private boolean isScrolling = false;
	private boolean isSelected = false;
	
	private TextureItem selectedItem;
	
	private Rectangle scissors = new Rectangle();
	
	public TexturesTable (Properties properties) {
		items = new Array <TextureItem>(1);
		
		background = new Sprite(TextureCreator.createTexture(Color.BLACK));
		background.setAlpha(0.5f);
		background.setSize(280, 600);
		background.setPosition(properties.getBackground().getX() + 10, properties.getBackground().getY() + 20);
		
		cursor = new Vector3(0, 0, 0);
	}
	
	public void addMapTexture (MapTexture mapTexture) {
		if (items.size > 0) {
			TextureItem item = null;
			if (items.get(items.size - 1).getBackground().getX() + 70 >= background.getX() + background.getWidth() - 5) {
				item = new TextureItem(background.getX() + 5, items.get(items.size - 1).getBackground().getY() - 35, 32, 32, mapTexture);
			} else {
				item = new TextureItem(items.get(items.size - 1).getBackground().getX() + 35, items.get(items.size - 1).getBackground().getY(), 32, 32, mapTexture);
			}
			items.add(item);
		} else {
			items.add(new TextureItem(background.getX() + 5, background.getVertices()[Batch.Y2] - 35, 32, 32, mapTexture));
		}
	}
	
	public void show (SpriteBatch batch, Array <MapTexture> mapTextures) {
		background.draw(batch);
		batch.flush();
		
		if (items.size > 0) {
			if (Events.getInstance().isMouseRightPressed()) {
				Vector3 tempCursor = CameraController.getInstance().unproject();
				if (tempCursor.x >= background.getX() && tempCursor.x <= background.getX() + background.getWidth()
				&& tempCursor.y >= background.getY() && tempCursor.y <= background.getY() + background.getHeight()) {
					cursor = new Vector3(tempCursor);
					isScrolling = true;
				}
			}
		}
		
		if (isScrolling) {
			Vector3 tempCursor = CameraController.getInstance().unproject();
			float tempY = tempCursor.y - cursor.y;
			for (int i = 0; i < items.size; i++) {
				items.get(i).setPosition(items.get(i).getBackground().getX(), items.get(i).getBackground().getY() + tempY);
			}
			cursor = new Vector3(tempCursor);
			selectedItem = null;
			isSelected = false;
			if (Events.getInstance().isMouseRightReleased()) {
				isScrolling = false;
			}
		}
		
		ScissorStack.calculateScissors(CameraController.getInstance().getCamera(), batch.getTransformMatrix(), background.getBoundingRectangle(), scissors);
		ScissorStack.pushScissors(scissors);
		
		for (int i = 0; i < items.size; i++) {
			if (items.get(i).isRemove()) {
				if (i != items.size - 1) {
					for (int j = i + 1; j < items.size; j++) {
						items.get(j);
					}
				}
				if (i != items.size - 1) {
					items.get(items.size - 1).setPosition(items.get(i).getBackground().getX(), items.get(i).getBackground().getY());
					items.set(i, items.get(items.size - 1));
				}
				mapTextures.get(mapTextures.size - 1).setId(mapTextures.get(i + 1).getId());
				mapTextures.set(i + 1, mapTextures.get(mapTextures.size - 1));
				mapTextures.removeIndex(i + 1);
				items.removeIndex(items.size - 1);
				i--;
				continue;
			} else {
				try {
					if (items.get(i).isReleased()) {
						isSelected = true;
						selectedItem = items.get(i);
					}
				} catch (IndexOutOfBoundsException e) {
					/** just skip **/
				}
			}
			try {
				items.get(i).show(batch);
			} catch (IndexOutOfBoundsException e) {
				/** just skip **/
			}
		}
		
		batch.flush();
		ScissorStack.popScissors();
	}
	
	public boolean isSelected () {
		return isSelected;
	}
	
	public TextureItem getSelectedItem () {
		return selectedItem;
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		for (TextureItem item : items) item.dispose();
		items.clear();
	}
}
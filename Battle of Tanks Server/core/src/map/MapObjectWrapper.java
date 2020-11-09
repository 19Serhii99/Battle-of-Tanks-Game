package map;

import com.badlogic.gdx.utils.Array;

public class MapObjectWrapper extends MapObjectBase {
	private Array <MapObjectBase> objects;
	
	public MapObjectWrapper () {
		objects = new Array <MapObjectBase>(1);
	}
	
	public void changePosition (float x, float y) {
		float tempX = x - this.getX();
		float tempY = y - this.getY();
		
		super.setPosition(x, y);
		
		for (MapObjectBase obj : objects) {
			obj.setPosition(obj.getX() + tempX, obj.getY() + tempY);
		}
	}
	
	public void resize () {
		float minX = 0;
		float maxX = 0;
		float minY = 0;
		float maxY = 0;
		for (int i = 0; i < objects.size; i++) {
			if (i == 0) {
				minX = objects.get(i).getX();
				maxX = objects.get(i).getX() + objects.get(i).getWidth();
				minY = objects.get(i).getY();
				maxY = objects.get(i).getY() + objects.get(i).getHeight();
			} else {
				if (objects.get(i).getX() < minX) minX = objects.get(i).getX();
				if (objects.get(i).getX() + objects.get(i).getWidth() > maxX) maxX = objects.get(i).getX() + objects.get(i).getWidth();
				if (objects.get(i).getY() < minY) minY = objects.get(i).getY();
				if (objects.get(i).getY() + objects.get(i).getHeight() > maxY) maxY = objects.get(i).getY() + objects.get(i).getHeight();
			}
		}
		super.setPosition(minX, minY);
		super.setSize(maxX - minX, maxY - minY);
	}
	
	@Override
	public boolean isWrapper () {
		return true;
	}
	
	public Array <MapObjectBase> getMapObjects () {
		return objects;
	}
}
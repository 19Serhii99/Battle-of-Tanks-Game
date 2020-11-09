package util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class CameraController {
	private static CameraController cameraController;
	private OrthographicCamera camera;
	private Vector3 temp;
	
	private CameraController () {
		camera = new OrthographicCamera();
		temp = new Vector3(0.0f, 0.0f, 0.0f);
	}
	
	public static CameraController getInstance () {
		if (cameraController == null) cameraController = new CameraController();
		return cameraController;
	}
	
	public void setSize (float width, float height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}
	
	public void setPosition (float x, float y) {
		camera.position.x = x;
		camera.position.y = y;
	}
	
	public void translate (float x, float y) {
		camera.position.x += x;
		camera.position.y += y;
	}
	
	public Vector3 unproject () {
		temp.x = Gdx.input.getX();
		temp.y = Gdx.input.getY();
		return camera.unproject(temp);
	}
	
	public float getWidth () {
		return camera.viewportWidth;
	}
	
	public float getHeight () {
		return camera.viewportHeight;
	}
	
	public float getX () {
		return camera.position.x;
	}
	
	public float getY () {
		return camera.position.y;
	}
	
	public void update () {
		camera.update();
	}
	
	public Matrix4 getCombinedMatrix () {
		return camera.combined;
	}
	
	public OrthographicCamera getCamera () {
		return camera;
	}
}
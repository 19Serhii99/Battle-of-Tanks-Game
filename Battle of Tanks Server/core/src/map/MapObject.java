package map;

public class MapObject extends MapObjectBase {
	private float rotation;
	private boolean isWall;
	private boolean isDestroyedWall;
	
	private MapTexture wholeTexture;
	private MapTexture destroyedTexture;
	
	public MapObject () {
		
	}
	
	public void setRotation (float rotation) {
		this.rotation = rotation;
		super.background.setRotation(rotation);
	}
	
	public void setWholeTexture (MapTexture wholeTexture) {
		this.wholeTexture = wholeTexture;
	}
	
	public void setDestroyedTexture (MapTexture destroyedTexture) {
		this.destroyedTexture = destroyedTexture;
	}
	
	public void setWall (boolean value) {
		isWall = value;
	}
	
	public void setDestroyedWall (boolean value) {
		this.isDestroyedWall = value;
	}
	
	public float getRotation () {
		return rotation;
	}
	
	public MapTexture getWholeTexture () {
		return wholeTexture;
	}
	
	public MapTexture getDestroyedTexture () {
		return destroyedTexture;
	}
	
	public boolean isWall () {
		return isWall;
	}
	
	public boolean isDestroyedWall () {
		return isDestroyedWall;
	}
}
package map;

public class MapObject extends MapObjectBase {
	private float rotation;
	private boolean isWall;
	private boolean isDestroyedWall;
	
	private WallTexture wholeTexture;
	private WallTexture destroyedTexture;
	
	public MapObject () {
		
	}
	
	public void setRotation (float rotation) {
		this.rotation = rotation;
	}
	
	public void setWholeTexture (WallTexture wholeTexture) {
		this.wholeTexture = wholeTexture;
	}
	
	public void setDestroyedTexture (WallTexture destroyedTexture) {
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
	
	public WallTexture getWholeTexture () {
		return wholeTexture;
	}
	
	public WallTexture getDestroyedTexture () {
		return destroyedTexture;
	}
	
	public boolean isWall () {
		return isWall;
	}
	
	public boolean isDestroyedWall () {
		return isDestroyedWall;
	}
}
package answers;

import java.io.Serializable;

public class OtherPlayerTechnique implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int idTechnique;
	private int idTower;
	private boolean isLeftSide;
	
	private String name;
	
	public OtherPlayerTechnique (int id, int idTechnique, int idTower, boolean isLeftSide, String name) {
		this.id = id;
		this.idTechnique = idTechnique;
		this.idTower = idTower;
		this.isLeftSide = isLeftSide;
		this.name = name;
	}
	
	public int getId () {
		return id;
	}
	
	public int getIdTechnique () {
		return idTechnique;
	}
	
	public int getIdTower () {
		return idTower;
	}
	
	public boolean isLeftSide () {
		return isLeftSide;
	}
	
	public String getName () {
		return name;
	}
}
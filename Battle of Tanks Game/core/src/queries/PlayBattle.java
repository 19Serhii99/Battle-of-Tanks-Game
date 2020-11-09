package queries;

import java.io.Serializable;

public class PlayBattle implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String battleType;
	
	private int idTechnique;
	private int idTower;
	
	public PlayBattle (String battleType, int idTechnique, int idTower) {
		this.battleType = battleType;
		this.idTechnique = idTechnique;
		this.idTower = idTower;
	}
	
	public String getBattleType () {
		return battleType;
	}
	
	public int getIdTechnique () {
		return idTechnique;
	}
	
	public int getIdTower () {
		return idTower;
	}
}
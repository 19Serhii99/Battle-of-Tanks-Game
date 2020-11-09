package technique;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import map.Chunk;
import util.GeometricData;

public class Technique extends GeometricData {
	private Corps corps;
	private Tower tower;
	
	private Sprite corpsBackground;
	private Sprite towerBackground;
	
	private Array <Vector2> gunPoints;
	
	private Array <Chunk> currentChunks;
	
	public Technique (Corps corps, Tower tower) {
		this.corps = corps;
		this.tower = tower;
		this.gunPoints = new Array <Vector2>();
		this.currentChunks = new Array <Chunk>();
		
		for (Vector2 gunPoint : tower.getGun().getPoints()) {
			gunPoints.add(new Vector2(gunPoint));
		}
		
		initBackgrounds();
		
		super.calculateData(corpsBackground.getVertices()[Batch.X1], corpsBackground.getVertices()[Batch.Y1], corpsBackground.getVertices()[Batch.X2], corpsBackground.getVertices()[Batch.Y2],
				corpsBackground.getVertices()[Batch.X3], corpsBackground.getVertices()[Batch.Y3], corpsBackground.getVertices()[Batch.X4], corpsBackground.getVertices()[Batch.Y4]);
	}
	
	private void initBackgrounds () {
		corpsBackground = new Sprite(corps.getWholeTexture());
		corpsBackground.setSize(corps.getWidth(), corps.getHeight());
		corpsBackground.setOriginCenter();
		
		towerBackground = new Sprite(tower.getWholeTexture());
		towerBackground.setSize(tower.getWidth(), tower.getHeight());
		towerBackground.setPosition(tower.getX(), tower.getY());
		towerBackground.setOrigin(tower.getOriginX(), tower.getOriginY());
	}
	
	public Corps getCorps () {
		return corps;
	}
	
	public Tower getTower () {
		return tower;
	}
	
	public Sprite getCorpsBackground () {
		return corpsBackground;
	}
	
	public Sprite getTowerBackground () {
		return towerBackground;
	}
	
	public Array <Vector2> getGunPoints () {
		return gunPoints;
	}
	
	public Array <Chunk> getCurrentChunks () {
		return currentChunks;
	}
}
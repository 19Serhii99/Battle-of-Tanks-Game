package techniqueEditor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Editor;

import util.TextureCreator;

public class TechniqueWorkspace implements Disposable {	
	private Sprite background;
	private BlockBody blockBody;
	private BlockTower currentBlockTower;
	private Array <BlockTower> blockTowers;
	
	private boolean isVisible = true;
	
	public TechniqueWorkspace () {	
		background = new Sprite(TextureCreator.createTexture(Color.GRAY));
		background.setSize(500, 400);
		background.setPosition(200, Editor.getCamera().viewportHeight / 2 - background.getHeight() / 2);
		
		blockTowers = new Array <BlockTower>(1);
		
		System.out.println(background.getY());
	}
	
	public void create () {
		blockBody = new BlockBody(background.getX(), background.getY());
		blockBody.getBackground().setSize(150, 100);
		blockBody.getBackground().setTexture(TextureCreator.createTexture(Color.RED));
		
		addTower();
	}
	
	public void setVisible (boolean value) {
		isVisible = value;
	}
	
	public void show (SpriteBatch batch) {
		if (isVisible) {
			background.draw(batch);
			
			if (blockBody != null) {
				blockBody.show(batch);
			}
			if (currentBlockTower != null) {
				currentBlockTower.show(batch);
			}
		}
	}
	
	public void addTower () {
		currentBlockTower = new BlockTower(background.getX(), background.getY());
		blockTowers.add(currentBlockTower);
		currentBlockTower.getBackground().setSize(150, 50);
		currentBlockTower.getBackground().setTexture(TextureCreator.createTexture(Color.BLUE));
	}
	
	public void setCurrentBlockTower (BlockTower currentBlockTower) {
		this.currentBlockTower = currentBlockTower;
	}
	
	public BlockTower getCurrentBlockTower () {
		return currentBlockTower;
	}
	
	public Array <BlockTower> getBlockTowers () {
		return blockTowers;
	}
	
	public Sprite getBackogrund () {
		return background;
	}
	
	public BlockBody getBlockBody () {
		return blockBody;
	}

	@Override
	public void dispose () {
		if (blockBody != null) blockBody.dispose();
		for (BlockTower blockTower : blockTowers) blockTower.dispose();
	}
}
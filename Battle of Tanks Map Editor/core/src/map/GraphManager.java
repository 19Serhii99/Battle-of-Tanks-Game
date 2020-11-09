package map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.editor.Events;

import util.CameraController;
import util.Mathematics;
import util.TextureCreator;

public class GraphManager implements Disposable {
	private Sprite apple;
	private Sprite vertexConnector;
	private Sprite border;
	private Sprite spawn;
	private Vertex currentVertex;
	private Chunk currentChunk;
	
	private boolean isBorder = false;
	private boolean isSelected = false;
	private boolean isMoving = false;
	private boolean isSpawnOne = false;
	private boolean isSpawnTwo = false;
	
	public GraphManager () {
		apple = new Sprite(new Texture(Gdx.files.internal("images/apple.png")));
		apple.setSize(15, 15);
		
		vertexConnector = new Sprite(TextureCreator.createTexture(Color.GREEN));
		vertexConnector.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		vertexConnector.setSize(100, 0.5f);
		vertexConnector.setOrigin(0, 0);
		
		border = new Sprite(TextureCreator.createTexture(Color.RED));
		
		spawn = new Sprite(TextureCreator.createTexture(Color.GOLD));
		spawn.setSize(5, 5);
	}
	
	public void show (SpriteBatch batch, Chunk chunk, Sprite workspace, float chunkWidth, float chunkHeight) {
		for (Vertex vertex : chunk.getVertices()) {
			apple.setPosition(vertex.getX() - apple.getWidth() / 2, vertex.getY() - apple.getHeight() / 2);
			apple.draw(batch);
			if ((vertex.isSpawnOne() && isSpawnOne) || (vertex.isSpawnTwo() && isSpawnTwo)) {
				spawn.setPosition(apple.getX(), apple.getY());
				spawn.draw(batch);
			}
			vertexConnector.setPosition(apple.getX() + apple.getWidth() / 2, apple.getY() + apple.getHeight() / 2 - vertexConnector.getHeight() / 2);
			for (Vertex neighbor : vertex.getNeighbors()) {
				float distance = Mathematics.getDistance(vertexConnector.getX(), vertexConnector.getY(), neighbor.getX(), neighbor.getY());
				vertexConnector.setSize(distance, vertexConnector.getHeight());
				float angle = MathUtils.atan2(neighbor.getY() - vertexConnector.getY(), neighbor.getX() - vertexConnector.getX()) * 180 / MathUtils.PI;
				vertexConnector.setRotation(angle);
				vertexConnector.draw(batch);
			}
			if (Events.getInstance().isMouseLeftReleased()) {
				Vector3 cursor = CameraController.getInstance().unproject();
				if (cursor.x >= apple.getX() && cursor.x <= apple.getX() + apple.getWidth() && cursor.y >= apple.getY() && cursor.y <= apple.getY() + apple.getHeight()) {
					if (isSpawnOne || isSpawnTwo) {
						if (isSpawnOne) vertex.setSpawnOne(vertex.isSpawnOne() ? false : true);
						if (isSpawnTwo) vertex.setSpawnTwo(vertex.isSpawnTwo() ? false : true);
						currentVertex = null;
						currentChunk = null;
					} else {
						if (currentVertex == null) {
							currentVertex = vertex;
							currentChunk = chunk;
						} else {
							if (currentVertex.getNeighbors().size == 0) {
								currentVertex.getNeighbors().add(vertex);
								vertex.getNeighbors().add(currentVertex);
							} else {
								boolean isHas = false;
								for (Vertex neighbor : currentVertex.getNeighbors()) {
									if (neighbor.equals(vertex)) {
										isHas = true;
									}
								}
								if (!isHas) {
									currentVertex.getNeighbors().add(vertex);
									vertex.getNeighbors().add(currentVertex);
								}
							}
							currentVertex = null;
							currentChunk = null;
						}
						isSelected = true;
					}
				}
			}
		}
		
		if (Events.getInstance().isTextInput()) {
			if (!isSpawnOne && !isSpawnTwo) {
				if (Gdx.input.isKeyPressed(Keys.FORWARD_DEL)) {
					if (currentVertex != null) {
						for (int i = 0; i < currentChunk.getVertices().size; i++) {
							Vertex vertex = currentChunk.getVertices().get(i);
							if (currentVertex.equals(vertex)) {
								for (Vertex neighbor : vertex.getNeighbors()) {
									for (int j = 0; j < neighbor.getNeighbors().size; j++) {
										Vertex neighborOfNeighbor = neighbor.getNeighbors().get(j);
										if (currentVertex.equals(neighborOfNeighbor)) {
											neighbor.getNeighbors().removeIndex(j);
											break;
										}
									}
								}
								currentChunk.getVertices().removeIndex(i);
							}
						}
						currentVertex = null;
						currentChunk = null;
					}
				} else if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
					currentVertex = null;
					currentChunk = null;
				} else {
					boolean moving = isMoving;
					if (Gdx.input.isKeyPressed(Keys.W)) {
						if (!isMoving && currentVertex != null) {
							float shift = 5;
							if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
								shift = 1;
							}
							currentVertex.setPosition(currentVertex.getX(), currentVertex.getY() + shift);
							moving = true;
						}
					}
					if (Gdx.input.isKeyPressed(Keys.S)) {
						if (!isMoving && currentVertex != null) {
							float shift = 5;
							if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
								shift = 1;
							}
							currentVertex.setPosition(currentVertex.getX(), currentVertex.getY() - shift);
							moving = true;
						}
					}
					if (Gdx.input.isKeyPressed(Keys.A)) {
						if (!isMoving && currentVertex != null) {
							float shift = 5;
							if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
								shift = 1;
							}
							currentVertex.setPosition(currentVertex.getX() - shift, currentVertex.getY());
							moving = true;
						}
					}
					if (Gdx.input.isKeyPressed(Keys.D)) {
						if (!isMoving && currentVertex != null) {
							float shift = 5;
							if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
								shift = 1;
							}
							currentVertex.setPosition(currentVertex.getX() + shift, currentVertex.getY());
							moving = true;
						}
					}
					isMoving = moving;
				}
			}
		}
		if (Events.getInstance().isMouseLeftReleased() && !isSelected) {
			Vector3 cursor = CameraController.getInstance().unproject();
			if (cursor.x >= chunk.getX() && cursor.x <= chunk.getX() + chunkWidth && cursor.y >= chunk.getY() && cursor.y <= chunk.getY() + chunkHeight) {
				if (cursor.x >= workspace.getX() && cursor.x <= workspace.getX() + workspace.getWidth() && cursor.y >= workspace.getY() && cursor.y <= workspace.getY() + workspace.getHeight()) {
					if (!isSpawnOne && !isSpawnTwo) chunk.getVertices().add(new Vertex(cursor.x, cursor.y));
				}
			}
		}
		
		if (currentVertex != null && isBorder) {
			float x = currentVertex.getX() - apple.getWidth() / 2;
			float y = currentVertex.getY() - apple.getHeight() / 2;
			
			border.setSize(apple.getWidth(), 1);
			border.setPosition(x, y);
			border.draw(batch);
			
			border.setSize(apple.getWidth(), 1);
			border.setPosition(x, y + apple.getHeight() - 1);
			border.draw(batch);
			
			border.setSize(1, apple.getHeight());
			border.setPosition(x, y);
			border.draw(batch);
			
			border.setSize(1, apple.getHeight());
			border.setPosition(x + apple.getWidth() - 1, y);
			border.draw(batch);
			
			isBorder = false;
		}
	}
	
	public void setSpawnOne (boolean value) {
		this.isSpawnOne = value;
	}
	
	public void setSpawnTwo (boolean value) {
		this.isSpawnTwo = value;
	}
	
	public void reset () {
		isBorder = true;
		isSelected = false;
		isMoving = false;
	}
	
	public boolean isSpawnOne () {
		return isSpawnOne;
	}
	
	public boolean isSpawnTwo () {
		return isSpawnTwo;
	}
	
	@Override
	public void dispose () {
		apple.getTexture().dispose();
		vertexConnector.getTexture().dispose();
		border.getTexture().dispose();
		spawn.getTexture().dispose();
	}
}
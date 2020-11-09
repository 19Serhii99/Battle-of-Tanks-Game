package net;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import gui.Label;
import util.Font;
import util.TextureCreator;

public class PlayersSubtable implements Disposable {
	private Label name;
	private Label number;
	private Label nickname;
	private Label technqiue;
	private Label kills;
	private Label damage;
	private Label lives;
	private Label count;
	
	private int maxPlayers;
	private int currentPlayers;
	private boolean isLeftSide;
	
	private Sprite border;
	private Sprite myBorder;
	
	private Array <PlayerItem> items;
	
	public PlayersSubtable (String tableName, Array <Player> players, boolean isLeftSide) {
		maxPlayers = players.size;
		currentPlayers = maxPlayers;
		
		initLabels(tableName);
		initItems(players);
		initBorders();
		
		this.isLeftSide = isLeftSide;
	}
	
	private void initLabels (String tableName) {
		BitmapFont font = Font.getInstance().generateBitmapFont(Color.WHITE, 25);
		
		name = new Label(tableName, font);
		number = new Label("№", font);
		nickname = new Label("Ім'я", font);
		technqiue = new Label("Техніка", font);
		kills = new Label("Знищених", font);
		damage = new Label("Урону", font);
		lives = new Label("Живих", Font.getInstance().generateBitmapFont(Color.WHITE, 20));
		count = new Label(currentPlayers + " / " + maxPlayers, lives.getFont());
	}
	
	private void initBorders () {
		border = new Sprite(TextureCreator.createTexture(Color.ORANGE));
		myBorder = new Sprite(TextureCreator.createTexture(Color.GREEN));
	}
	
	private void initItems (Array <Player> players) {
		items = new Array <PlayerItem> (players.size);
		
		for (int i = 0; i < players.size; i++) {
			Player player = players.get(i);
			PlayerItem item = new PlayerItem(player, i == 0 ? 1 : (items.get(i - 1).getNumber() + 1), player.getNameLabel().getText(), player.getTechnique().getCorps().getName(),
					name.getFont(), player.isMe());
			items.add(item);
		}
	}
	
	public void addDead () {
		currentPlayers--;
		count.setText(currentPlayers + " / " + maxPlayers);
	}
	
	public void show (SpriteBatch batch, float startX, float startY) {
		name.setPosition(startX, startY - 5);
		number.setPosition(startX, name.getY() - name.getHeight() - 15);
		nickname.setPosition(number.getX() + number.getWidth() + 50, number.getY());
		technqiue.setPosition(nickname.getX() + nickname.getWidth() + 50, number.getY());
		kills.setPosition(technqiue.getX() + technqiue.getWidth() + 50, number.getY());
		damage.setPosition(kills.getX() + kills.getWidth() + 50, number.getY());
		lives.setPosition(damage.getX(), damage.getY() + 20);
		count.setPosition(lives.getX() + lives.getWidth() + 5, lives.getY());
		
		name.draw(batch);
		number.draw(batch);
		nickname.draw(batch);
		technqiue.draw(batch);
		kills.draw(batch);
		damage.draw(batch);
		lives.draw(batch);
		count.draw(batch);
		
		for (int i = 0; i < items.size; i++) {
			PlayerItem item = items.get(i);
			float y;
			
			if (i == 0) {
				y = number.getY() - number.getHeight() - 30;
			} else {
				y = items.get(i - 1).getY() - items.get(i - 1).getHeight() - 30;
			}
			
			item.setPositionNumber(number.getX() + number.getWidth() / 2, y);
			item.setPositionNickname(nickname.getX() + nickname.getWidth() / 2, y);
			item.setPositionTechnique(technqiue.getX() + technqiue.getWidth() / 2, y);
			item.setPositionKills(kills.getX() + kills.getWidth() / 2, y);
			item.setPositionDamage(damage.getX() + damage.getWidth() / 2, y);
			item.show(batch);
			
			Sprite tempBorder = item.isMe() ? myBorder : border;
			
			float width = damage.getX() + damage.getWidth() - number.getX();
			float height = 20;
			
			tempBorder.setSize(1, height + 17);
			tempBorder.setPosition(number.getX(), item.getY() - height - 9);
			tempBorder.draw(batch);
			
			tempBorder.setSize(1, height + 17);
			tempBorder.setPosition(number.getX() + width + 99, item.getY() - height - 9);
			tempBorder.draw(batch);
			
			tempBorder.setSize(width + 100, 1);
			tempBorder.setPosition(number.getX(), item.getY() - height - 9);
			tempBorder.draw(batch);
			
			tempBorder.setSize(width + 100, 1);
			tempBorder.setPosition(number.getX(), item.getY() + 7);
			tempBorder.draw(batch);
		}
	}
	
	public Array <PlayerItem> getItems () {
		return items;
	}
	
	public int getCurrentPlayers () {
		return currentPlayers;
	}
	
	public boolean isLeftSide () {
		return isLeftSide;
	}
	
	@Override
	public void dispose () {
		border.getTexture().dispose();
		myBorder.getTexture().dispose();
		name.getFont().dispose();
		lives.getFont().dispose();
	}
}
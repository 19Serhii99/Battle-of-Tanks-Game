package net;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class CommandPlayersTable extends PlayersTable {
	private PlayersSubtable table1;
	private PlayersSubtable table2;
	
	public CommandPlayersTable (String mapName, String battleType, Array <Player> playersLeft, Array <Player> playersRight, boolean isLeftSide) {
		super(mapName, battleType);
		super.isCommand = true;
		
		table1 = new PlayersSubtable("Союзники", isLeftSide ? playersLeft : playersRight, isLeftSide);
		table2 = new PlayersSubtable("Супротивники", isLeftSide ? playersRight : playersLeft, isLeftSide ? false : true);
	}
	
	public void addDamage (Player player, boolean leftSide, int amount, boolean killed) {
		PlayersSubtable table = leftSide ? (table1.isLeftSide() ? table1 : table2) : (table1.isLeftSide() ? table2 : table1);
		for (PlayerItem item : table.getItems()) {
			if (item.getPlayer().equals(player)) {
				item.addDamage(amount);
				if (killed) {
					item.addKill();
					if (leftSide) {
						if (table2.isLeftSide()) table1.addDead();
						else table2.addDead();
					} else {
						if (table2.isLeftSide()) table2.addDead();
						else table1.addDead();
					}
				}
				break;
			}
		}
	}
	
	public void show (SpriteBatch batch) {
		super.show(batch);
		float y = super.background.getY() + super.background.getHeight() - 75;
		table1.show(batch, super.background.getX() + 20, y);
		table2.show(batch, super.background.getX() + 800, y);
	}
	
	public PlayersSubtable getTable1 () {
		return table1;
	}
	
	public PlayersSubtable getTable2 () {
		return table2;
	}
}
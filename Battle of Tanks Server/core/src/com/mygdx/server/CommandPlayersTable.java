package com.mygdx.server;

import java.util.ArrayList;

public class CommandPlayersTable extends PlayersTable {
	private PlayersSubtable table1;
	private PlayersSubtable table2;
	
	private WinSide winSide;
	
	private boolean isBattleEnded;
	
	public CommandPlayersTable (ArrayList <Player> playersLeft, ArrayList <Player> playersRight, boolean isLeftSide) {
		isCommand = true;
		
		table1 = new PlayersSubtable(isLeftSide ? playersLeft : playersRight, true);
		table2 = new PlayersSubtable(isLeftSide ? playersRight : playersLeft, false);
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
	
	public void check () {
		if (battleTime <= 0) {
			winSide = WinSide.DRAW;
			isBattleEnded = true;
		} else {
			if (table1.getCurrentPlayers() == 0) {
				winSide = table1.isLeftSide() ? WinSide.RIGHT : WinSide.LEFT;
				isBattleEnded = true;
				return;
			}
			if (table2.getCurrentPlayers() == 0) {
				winSide = table2.isLeftSide() ? WinSide.RIGHT : WinSide.LEFT;
				isBattleEnded = true;
				return;
			}
		}
	}
	
	public PlayersSubtable getTable1 () {
		return table1;
	}
	
	public PlayersSubtable getTable2 () {
		return table2;
	}
	
	public WinSide getWinSide () {
		return winSide;
	}
	
	public boolean isBattleEnded () {
		return isBattleEnded;
	}
}
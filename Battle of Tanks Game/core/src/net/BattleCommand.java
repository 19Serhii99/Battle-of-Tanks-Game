package net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import answers.OtherPlayersTechniqueAnswer;

public class BattleCommand extends Battle implements Disposable {
	private Array <Player> commandLeft;
	private Array <Player> commandRight;
	private CommandPlayersTable table;
	
	private long timer;
	
	public BattleCommand () {
		commandLeft = new Array <Player>();
		commandRight = new Array <Player>();
	}
	
	public void loadPlayers (OtherPlayersTechniqueAnswer answer) {
		super.loadPlayers(answer);
		
		if (player.isLeftSide()) commandLeft.add(player);
		else commandRight.add(player);
		player.createDataAroundPlayer(Color.WHITE);
		
		for (Player otherPlayer : players) {
			if (!player.equals(otherPlayer)) {
				if (player.isLeftSide()) {
					if (otherPlayer.isLeftSide()) {
						commandLeft.add(otherPlayer);
						otherPlayer.createDataAroundPlayer(Color.GREEN);
					} else {
						commandRight.add(otherPlayer);
						otherPlayer.createDataAroundPlayer(Color.RED);
					}
				} else {
					if (otherPlayer.isLeftSide()) {
						commandLeft.add(otherPlayer);
						otherPlayer.createDataAroundPlayer(Color.RED);
					} else {
						commandRight.add(otherPlayer);
						otherPlayer.createDataAroundPlayer(Color.GREEN);
					}
				}
			}
		}
		
		table = new CommandPlayersTable("Місто", "Командний бій", commandLeft, commandRight, player.isLeftSide());
	}
	
	public void show (SpriteBatch batch) {
		super.show(batch, table);
		table.updateTime();
		
		if (player.isBattleFinished() && !isFinished) {
			isFinished = true;
			table.setBattleFinish(player.isWon(), player.getGotExperience(), player.getGotMoney());
			timer = System.currentTimeMillis();
		}
		
		if (Gdx.input.isKeyPressed(Keys.TAB) || player.isBattleFinished()) {
			table.show(batch);
		}
		
		if (table.isBattleFinished) {
			if (System.currentTimeMillis() - timer >= 10000) {
				isExit = true;
			}
		}
	}
	
	@Override
	public void dispose () {
		super.dispose();
		table.dispose();
	}
}
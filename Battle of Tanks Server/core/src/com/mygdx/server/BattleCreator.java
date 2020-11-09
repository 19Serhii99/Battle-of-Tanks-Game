package com.mygdx.server;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;

public class BattleCreator extends Thread {
	private volatile ArrayDeque<PlayerSettings> readyPlayers;
	private ArrayDeque<BattleSettings> waitingPlayers;
	private LinkedList<Battle> battles;

	private final int maxPlayers = 2;

	public BattleCreator() {
		waitingPlayers = new ArrayDeque<BattleSettings>();
		battles = new LinkedList<Battle>();

		for (int i = 0; i < 5; i++) {
			battles.add(new Battle(45555 + i, 45555 + i));
		}

		readyPlayers = new ArrayDeque<PlayerSettings>();
		super.setDaemon(true);
	}

	public void addPlayer(PlayerSettings player) {
		readyPlayers.add(player);
	}

	@Override
	public void run() {
		while (true) {
			try {
				final Iterator<PlayerSettings> readyPlayersIterator = readyPlayers.iterator();
				while (readyPlayersIterator.hasNext()) {
					final PlayerSettings player = readyPlayersIterator.next();
					if (player.getUserHandler().getStatus().isWantsPlay()) {
						boolean isFound = false;
						for (BattleSettings settings : waitingPlayers) {
							if (settings.getPlayers().size() == maxPlayers)
								continue;
							if ((player.getLevel() == settings.getAverageLevel()
									|| player.getLevel() + 1 == settings.getAverageLevel()
									|| player.getLevel() - 1 == settings.getAverageLevel())
									&& player.getBattleType() == settings.getBattleType()) {
								isFound = true;
								settings.getPlayers().add(player);
								readyPlayersIterator.remove();
								break;
							}
						}
						if (!isFound) {
							final BattleSettings settings = new BattleSettings(player.getBattleType(),
									player.getLevel());
							settings.getPlayers().add(player);
							waitingPlayers.add(settings);
							readyPlayersIterator.remove();
						}
					} else {
						readyPlayersIterator.remove();
					}
				}

				final Iterator<BattleSettings> settingsIterator = waitingPlayers.iterator();
				while (settingsIterator.hasNext()) {
					final BattleSettings settings = settingsIterator.next();
					final Iterator<PlayerSettings> playersIterator = settings.getPlayers().iterator();
					while (playersIterator.hasNext()) {
						final PlayerSettings player = playersIterator.next();
						if (!player.getUserHandler().getStatus().isWantsPlay()) {
							playersIterator.remove();
						}
					}
					if (settings.getPlayers().size() == maxPlayers) {
						final Iterator<Battle> battleIterator = battles.iterator();
						while (battleIterator.hasNext()) {
							final Battle battle = battleIterator.next();
							if (!battle.isBusy()) {
								battle.load(settings.getPlayers());
								settingsIterator.remove();
								break;
							}
						}
					} else {
						/*
						 * if (System.currentTimeMillis() - settings.getStartTime() >= 10000) { Iterator
						 * <Battle> battleIterator = battles.iterator(); while
						 * (battleIterator.hasNext()) { Battle battle = battleIterator.next(); if
						 * (!battle.isBusy()) { battle.load(settings.getPlayers());
						 * settingsIterator.remove(); break; } } }
						 */
					}
				}

				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
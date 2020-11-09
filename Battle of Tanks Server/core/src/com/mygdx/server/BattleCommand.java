package com.mygdx.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;

import answers.BattleEndedAnswer;
import answers.CreateShellsAnswer;
import map.Collision;
import map.Map;
import map.Vertex;
import net.UDP;
import net.UDPOutputObject;
import technique.Shell;

public class BattleCommand {
	private ArrayList <Player> commandLeft;
	private ArrayList <Player> commandRight;
	private ArrayList <Player> players;
	private CommandPlayersTable table;
	
	private boolean isFindSpawn = true;
	private boolean isFinished;
	
	public static final int maxPlayers = 2;
	
	public BattleCommand (ArrayList <Player> players) {
		this.players = players;
		
		commandLeft = new ArrayList <Player>();
		commandRight = new ArrayList <Player>();
		
		boolean left = true;
		
		for (Player player : players) {
			if (left) {
				commandLeft.add(player);
				player.setLeftSide(true);
				left = false;
			} else {
				commandRight.add(player);
				left = true;
			}
		}
		
		table = new CommandPlayersTable(commandLeft, commandRight, true);
	}
	
	public void act (Map map, Collision collision, boolean isPlayersLoaded, UDP udp, CreateShellsAnswer createShellsAnswer) {
		if (isFindSpawn) {
			for (int i = 0; i < commandLeft.size(); i++) {
				Player player = commandLeft.get(i);
				int random = MathUtils.random(0, map.getSpawnCommandOne().size - 1);
				Vertex vertex = map.getSpawnCommandOne().get(random).getVertex();
				player.setStartPosition(vertex.getX(), vertex.getY(), map);
				map.getSpawnCommandOne().removeIndex(random);
			}
			for (int i = 0; i < commandRight.size(); i++) {
				Player player = commandRight.get(i);
				int random = MathUtils.random(0, map.getSpawnCommandTwo().size - 1);
				Vertex vertex = map.getSpawnCommandTwo().get(random).getVertex();
				player.setStartPosition(vertex.getX(), vertex.getY(), map);
				map.getSpawnCommandTwo().removeIndex(random);
				player.rotate(180);
			}
			isFindSpawn = false;
		} else {
			if (isPlayersLoaded) {
				for (Player player : players) {
					player.act(udp, createShellsAnswer, players);
					map.checkPlayer(player);
				}
				for (Shell shell : collision.getShells()) {
					for (Player player : players) {
						if (shell.getIdSender() == player.getId()) {
							if (player.isUpdated()) shell.moveTo(player.getDelta());
							break;
						}
					}
				}
				map.checkShells(collision);
				collision.act(map, players, table, udp.getSocket());
				
				for (Player player : players) {
					player.setUpdated(false);
				}
				
				if (table.isBattleEnded()) {
					results(table.getWinSide(), udp);
				} else {
					table.check();
				}
			}
		}
	}
	
	public void results (WinSide side, UDP udp) {
		for (Player player : players) {
			UserHandler user = player.getUser();
			try {
				Statement data = user.getConnection().createStatement();
				ResultSet dataResult = data.executeQuery("SELECT experience, money, battles, wins, average_damage, kills, deaths, survival FROM player WHERE id = " + user.getIdPlayer());
				dataResult.next();
				
				int experience = dataResult.getInt(1);
				int money = dataResult.getInt(2);
				
				final int battles = dataResult.getInt(3) + 1;
				final int wins = dataResult.getInt(4) + ((side == WinSide.LEFT && player.isLeftSide()) || (side == WinSide.RIGHT && !player.isLeftSide()) ? 1 : 0);
				
				int damage = dataResult.getInt(5);
				int kills = dataResult.getInt(6);
				
				final int deaths = dataResult.getInt(7) + (player.isLive() ? 0 : 1);
				final int amountAlive  = battles - deaths;
				final float survival = battles == 0 ? 100 : (amountAlive * 100) / battles;
				
				if (player.isLeftSide()) {
					if (table.getTable1().isLeftSide()) {
						for (PlayerItem item : table.getTable1().getItems()) {
							if (item.getPlayer().getId() == player.getId()) {
								if (side == WinSide.LEFT) {
									experience += 2 * item.getDamage();
									money += 10 * item.getDamage();
								} else {
									experience += item.getDamage();
									money += 3 * item.getDamage();
								}
								kills += item.getKills();
								damage += item.getDamage();
								experience += 5 * item.getKills();
								money += 5 * item.getKills() + 1;
								break;
							}
						}
					} else {
						for (PlayerItem item : table.getTable2().getItems()) {
							if (item.getPlayer().getId() == player.getId()) {
								if (side == WinSide.LEFT) {
									experience += 2 * item.getDamage();
									money += 10 * item.getDamage();
								} else {
									experience += item.getDamage();
									money += 3 * item.getDamage();
								}
								kills += item.getKills();
								damage += item.getDamage();
								experience += 5 * item.getKills();
								money += 5 * item.getKills() + 1;
								break;
							}
						}
					}
				} else {
					if (!table.getTable1().isLeftSide()) {
						for (PlayerItem item : table.getTable1().getItems()) {
							if (item.getPlayer().getId() == player.getId()) {
								if (side == WinSide.RIGHT) {
									experience += 2 * item.getDamage();
									money += 10 * item.getDamage();
								} else {
									experience += item.getDamage();
									money += 3 * item.getDamage();
								}
								kills += item.getKills();
								damage += item.getDamage();
								experience += 5 * item.getKills();
								money += 5 * item.getKills() + 1;
								break;
							}
						}
					} else {
						for (PlayerItem item : table.getTable2().getItems()) {
							if (item.getPlayer().getId() == player.getId()) {
								if (side == WinSide.RIGHT) {
									experience += 2 * item.getDamage();
									money += 10 * item.getDamage();
								} else {
									experience += item.getDamage();
									money += 3 * item.getDamage();
								}
								kills += item.getKills();
								damage += item.getDamage();
								experience += 5 * item.getKills();
								money += 5 * item.getKills() + 1;
								break;
							}
						}
					}
				}
				
				Statement dataUpdated = user.getConnection().createStatement();
				dataUpdated.executeUpdate("UPDATE player SET experience = " + experience + ", money = " + money + ", battles = " + battles + ", wins = " + wins + ","
						+ "average_damage = " + damage + ", kills = " + kills + ", deaths = " + deaths + ", survival = " + survival + " WHERE id = " + user.getIdPlayer());
				
				boolean isWon = false;
				
				if (player.isLeftSide()) isWon = side == WinSide.LEFT;
				else isWon = side == WinSide.RIGHT;
				
				BattleEndedAnswer answer = new BattleEndedAnswer(experience - dataResult.getInt(1), money - dataResult.getInt(2), isWon);
				UDPOutputObject object = new UDPOutputObject(player.getInetAddress(), player.getPort(), answer);
				object.sendObject(udp.getSocket());
				
				data.close();
				dataUpdated.close();
				
				player.getUser().getStatus().setPlaying(false);
				player.getUser().getStatus().setReady(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		isFinished = true;
	}
	
	public ArrayList <Player> getCommandLeft () {
		return commandLeft;
	}
	
	public ArrayList <Player> getCommandRight () {
		return commandRight;
	}
	
	public boolean isFinished () {
		return isFinished;
	}
}
package additional;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.mygdx.server.BattleType;
import com.mygdx.server.PlayerSettings;
import com.mygdx.server.Server;
import com.mygdx.server.UserHandler;

import answers.AddNewFriendAnswer;
import answers.AddToBlackListAnswer;
import answers.ConfirmNewFriendAnswer;
import answers.FindFriendAnswer;
import answers.FriendOBJ;
import answers.FriendsAnswer;
import answers.GetDataPlayerAnswer;
import answers.Human;
import answers.ModulesAnswer;
import answers.MyBalanceAnswer;
import answers.OperationAnswer;
import answers.PlayBattleAnswer;
import queries.AddNewFriend;
import queries.AddToBlackList;
import queries.ConfirmNewFriend;
import queries.FindFriend;
import queries.Operation;
import queries.PlayBattle;

public class DataPlayerOBJ {
	private Statement statement;
	private Integer idPlayer;
	private ObjectOutputStream objectOutputStream;
	private String nickname;
	private LinkedList<FriendOBJ> friends;

	private int experience;
	private int money;
	private int battles;
	private int wins;
	private int averageDamage;
	private int kills;
	private int deaths;
	private int winScore;
	private int failScore;
	private float survival;
	private boolean isFights;
	private short battleType;
	private short gameType;

	public DataPlayerOBJ(Statement statement, Integer idPlayer, ObjectOutputStream objectOutputStream) {
		this.statement = statement;
		this.idPlayer = idPlayer;
		this.objectOutputStream = objectOutputStream;
		this.friends = new LinkedList<FriendOBJ>();

		try {
			ResultSet result = statement.executeQuery("SELECT nickname FROM player WHERE id = " + idPlayer);
			if (result.next()) {
				nickname = result.getString(1);
			}
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getData() {
		getStatistics();
		getFriendsData();
	}

	private void getFriendsData() {
		LinkedList<Integer> friends = new LinkedList<Integer>();
		FriendsAnswer answer = new FriendsAnswer();
		/*
		 * String query = "SELECT id_friend, confirmed FROM friends WHERE id_player = "
		 * + idPlayer; try { ResultSet result = statement.executeQuery(query); while
		 * (result.next()) { if (result.getBoolean(2)) friends.add(result.getInt(1)); }
		 * result.close(); } catch (SQLException e) { e.printStackTrace(); } for (int i
		 * = 0; i < friends.size(); i++) { FriendOBJ friend = null; Iterator
		 * <UserHandler> players = Server.getTCP().getPlayers().iterator(); while
		 * (players.hasNext()) { UserHandler player = players.next(); if
		 * (player.getIdPlayer() == friends.get(i)) { friend = new
		 * FriendOBJ(friends.get(i), true, player.getDataPlayer().isFights,
		 * player.getDataPlayer().nickname); if (player.getDataPlayer().isFights) {
		 * friend.setScore(winScore, failScore); } friend.setStatus(gameType,
		 * battleType); player.getDataPlayer().updateFriend(idPlayer, isFights,
		 * nickname, winScore, failScore, gameType, battleType); } } if (friend == null)
		 * { query = "SELECT nickname, dt_last_seen FROM player WHERE id = " +
		 * friends.get(i); try { ResultSet result = statement.executeQuery(query); if
		 * (result.next()) { friend = new FriendOBJ(friends.get(i), false, false,
		 * result.getString(1)); friend.setStatus(result.getString(2)); } } catch
		 * (SQLException e) { e.printStackTrace(); } } if (friend != null) {
		 * this.friends.add(friend); answer.addFriend(friend); } }
		 */
		sendObject(answer);
	}

	public void updateFriend(int id, boolean fights, String nickname, int winScore, int failScore, short gameType,
			short battleType) {
		FriendsAnswer answer = new FriendsAnswer();
		FriendOBJ friend = new FriendOBJ(id, true, fights, nickname);
		friend.setScore(winScore, failScore);
		friend.setStatus(gameType, battleType);
		answer.addFriend(friend);
		sendObject(answer);
	}

	public void updateFriend(int id, String nickname, String dt_last_seen) {
		FriendsAnswer answer = new FriendsAnswer();
		FriendOBJ friend = new FriendOBJ(id, false, false, nickname);
		friend.setStatus(dt_last_seen);
		answer.addFriend(friend);
		sendObject(answer);
	}

	private void getStatistics() {
		String query = "SELECT experience, money, battles, wins, average_damage, kills, deaths, survival FROM player WHERE id = "
				+ idPlayer;
		try {
			ResultSet result = statement.executeQuery(query);
			if (result.next()) {
				experience = result.getInt(1);
				money = result.getInt(2);
				battles = result.getInt(3);
				wins = battles == 0 ? 0 : (result.getInt(4) * 100) / battles;
				averageDamage = battles == 0 ? 0 : result.getInt(5) / battles;
				kills = result.getInt(6);
				deaths = result.getInt(7);
				survival = result.getFloat(8);
				GetDataPlayerAnswer getDataPlayerAnswer = new GetDataPlayerAnswer(experience, money, battles,
						averageDamage, kills, deaths, survival, wins);
				sendObject(getDataPlayerAnswer);
			}
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getModules() {
		Statement techniques = null;
		Statement towers = null;
		try {
			techniques = statement.getConnection().createStatement();
			towers = statement.getConnection().createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrayList<Corps> corpses = new ArrayList<Corps>(1);
		String queryTechniques = "SELECT * FROM technique";
		try {
			ResultSet resultTechniques = techniques.executeQuery(queryTechniques);
			while (resultTechniques.next()) {
				Corps corps = new Corps(resultTechniques.getInt(1));
				corps.setName(resultTechniques.getString(2));
				corps.setExperience(resultTechniques.getInt(3));
				corps.setMoney(resultTechniques.getInt(4));
				corps.setSpeedRotation(resultTechniques.getFloat(6));
				corps.setMaxHealth(resultTechniques.getInt(7));
				corps.setMaxSpeed(resultTechniques.getFloat(8));
				corps.setAcceleration(resultTechniques.getFloat(9));
				corps.setLevel(resultTechniques.getInt(10));
				corps.setWidth(resultTechniques.getFloat(11));
				corps.setHeight(resultTechniques.getFloat(12));
				corps.setPosition(resultTechniques.getInt(13));
				ResultSet resultType = statement
						.executeQuery("SELECT type FROM technique_type WHERE id = " + resultTechniques.getString(5));
				if (resultType.next())
					corps.setTechniqueType(resultType.getString(1));
				resultType.close();
				String queryTowers = "SELECT * FROM tower WHERE id IN ( SELECT id_tower FROM technique_tower WHERE id_technique = "
						+ corps.getId() + ")";
				ResultSet resultTowers = towers.executeQuery(queryTowers);
				while (resultTowers.next()) {
					Tower tower = new Tower(resultTowers.getInt(1));
					tower.setName(resultTowers.getString(2));
					tower.setExperience(resultTowers.getInt(3));
					tower.setMoney(resultTowers.getInt(4));
					tower.setSpeedRotation(resultTowers.getFloat(5));
					tower.setTimeReduction(resultTowers.getFloat(6));
					tower.setTimeRecharge(resultTowers.getFloat(7));
					tower.setMinDamage(resultTowers.getInt(8));
					tower.setMaxDamage(resultTowers.getInt(9));
					tower.setShellsTotal(resultTowers.getInt(10));
					tower.setShellSpeed(resultTowers.getFloat(11));
					tower.setLevel(resultTowers.getInt(12));
					tower.setMinRadius(resultTowers.getFloat(13));
					tower.setMaxRadius(resultTowers.getFloat(14));
					tower.setShellsCassette(resultTowers.getString(15) == null ? 0 : resultTowers.getInt(15));
					tower.setRechargeShell(resultTowers.getString(16) == null ? 0 : resultTowers.getFloat(16));
					tower.setRotationLeft(resultTowers.getString(17) == null ? 0 : resultTowers.getFloat(17));
					tower.setRotationRight(resultTowers.getString(18) == null ? 0 : resultTowers.getFloat(18));
					tower.setX(resultTowers.getFloat(19));
					tower.setY(resultTowers.getFloat(20));
					tower.setWidth(resultTowers.getFloat(21));
					tower.setHeight(resultTowers.getFloat(22));
					tower.setOriginX(resultTowers.getFloat(23));
					tower.setOriginY(resultTowers.getFloat(24));
					tower.setPosition(resultTowers.getInt(25));
					tower.setShellWidth(resultTowers.getFloat(27));
					tower.setShellHeight(resultTowers.getFloat(28));
					ResultSet resultStyle = statement
							.executeQuery("SELECT style FROM shooting_style WHERE id = " + resultTowers.getString(26));
					if (resultStyle.next())
						tower.setShootingStyle(resultStyle.getString(1));
					resultStyle.close();
					ResultSet result = statement
							.executeQuery("SELECT available, explored, bought FROM player_tower WHERE id_player = "
									+ idPlayer + " and id_tower = " + tower.getId());
					if (result.next()) {
						tower.setAvailable(result.getBoolean(1));
						tower.setExplored(result.getBoolean(2));
						tower.setBought(result.getBoolean(3));
					}
					result.close();
					corps.getTowers().add(tower);
				}
				resultTowers.close();
				ResultSet result = statement
						.executeQuery("SELECT available, explored, bought FROM player_technique WHERE id_player = "
								+ idPlayer + " and id_technique = " + corps.getId());
				if (result.next()) {
					corps.setAvailable(result.getBoolean(1));
					corps.setExplored(result.getBoolean(2));
					corps.setBought(result.getBoolean(3));
				}
				result.close();
				corpses.add(corps);
			}
			resultTechniques.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ModulesAnswer answer = new ModulesAnswer();
		for (Corps corps : corpses) {
			answer.getCorpses().add(corps);
		}
		sendObject(answer);
	}

	public void findFriend(FindFriend find) {
		String query = "SELECT id FROM player WHERE confirmed = true AND nickname LIKE '%" + find.getSearch() + "%'";
		try {
			ResultSet result = statement.executeQuery(query);
			ArrayDeque<Integer> people = new ArrayDeque<Integer>();
			block: {
				while (result.next()) {
					if (find.getLastPlayer() >= 0) {
						int id = result.getInt(1);
						if (id == find.getLastPlayer()) {
							if (find.isUp()) {
								break;
							} else {
								people.clear();
								if (result.getInt(1) != idPlayer)
									people.add(result.getInt(1));
								for (int i = 0; i < 20; i++) {
									if (result.next()) {
										if (result.getInt(1) != idPlayer)
											people.add(result.getInt(1));
									} else {
										break block;
									}
								}
							}
						} else {
							if (people.size() == 20) {
								people.removeFirst();
								people.add(id);
							}
						}
					} else {
						if (result.getInt(1) != idPlayer)
							people.add(result.getInt(1));
						for (int i = 0; i < 19; i++) {
							if (result.next()) {
								if (result.getInt(1) != idPlayer)
									people.add(result.getInt(1));
							} else {
								break block;
							}
						}
					}
				}
			}
			Iterator<Integer> peopleIterator = people.iterator();
			StringBuffer ids = new StringBuffer();
			while (peopleIterator.hasNext()) {
				if (ids.length() > 0) {
					ids.append(",");
				}
				ids.append(peopleIterator.next());
			}
			FindFriendAnswer answer = new FindFriendAnswer();
			if (ids.length() > 0) {
				query = "SELECT id, nickname FROM player WHERE confirmed = true AND id IN (" + ids + ")";
				result = statement.executeQuery(query);
				while (result.next()) {
					Human human = new Human(result.getInt(1), result.getString(2));
					answer.getPeople().add(human);
				}
			}
			sendObject(answer);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void playBattle(PlayBattle playBattle, UserHandler userHandler) {
		if (!userHandler.getStatus().isWantsPlay() && !userHandler.getStatus().isPlaying()) {
			PlayBattleAnswer playBattleAnswer = null;
			String query = "SELECT bought FROM player_technique WHERE id_player = " + idPlayer + " AND id_technique = "
					+ playBattle.getIdTechnique();
			try {
				ResultSet result = statement.executeQuery(query);
				if (result.next()) {
					if (result.getBoolean(1)) {
						query = "SELECT bought FROM player_tower WHERE id_player = " + idPlayer + " AND id_tower = "
								+ playBattle.getIdTower();
						result = statement.executeQuery(query);
						if (result.next()) {
							if (result.getBoolean(1)) {
								playBattleAnswer = new PlayBattleAnswer(true);
							} else {
								playBattleAnswer = new PlayBattleAnswer(false);
							}
						} else {
							playBattleAnswer = new PlayBattleAnswer(false);
						}
					} else {
						playBattleAnswer = new PlayBattleAnswer(false);
					}
				} else {
					playBattleAnswer = new PlayBattleAnswer(false);
				}

				sendObject(playBattleAnswer);

				if (playBattleAnswer.getValue()) {
					userHandler.getStatus().setWantsPlay(true);
					BattleType battleType;
					if (playBattle.getBattleType().equals("command")) {
						battleType = BattleType.COMMAND;
					} else if (playBattle.getBattleType().equals("assault")) {
						battleType = BattleType.ASSAULT;
					} else {
						battleType = BattleType.DEATHMATCH;
					}
					query = "SELECT level FROM technique WHERE id = " + playBattle.getIdTechnique();
					result = statement.executeQuery(query);
					if (result.next()) {
						int level = result.getInt(1);
						PlayerSettings playerSettings = new PlayerSettings(userHandler, battleType,
								playBattle.getIdTechnique(), playBattle.getIdTower(), level);
						Server.getTCP().getBattleCreator().addPlayer(playerSettings);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void addNewFriend(AddNewFriend friend) {
		try {
			ResultSet result = statement.executeQuery("SELECT id FROM friends WHERE id_friend = " + friend.getId());
			AddNewFriendAnswer answer = null;
			if (!result.next()) {
				statement.executeUpdate(
						"INSERT INTO friends (id_player, id_friend) VALUES (" + idPlayer + ", " + friend.getId() + ")");
				answer = new AddNewFriendAnswer(true);
			} else {
				answer = new AddNewFriendAnswer(false);
			}
			sendObject(answer);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void confirmNewFriend(ConfirmNewFriend friend) {
		try {
			int rows = statement.executeUpdate("UPDATE friends SET confirmed = true WHERE id_player = " + friend.getId()
					+ " AND id_friend = " + idPlayer);
			if (rows != 0) {
				rows = statement.executeUpdate("INSERT INTO friends (id_player, id_friend, confirmed) VALUES ("
						+ idPlayer + ", " + friend.getId() + ", true)");
				ConfirmNewFriendAnswer answer = null;
				if (rows == 0) {
					answer = new ConfirmNewFriendAnswer(false);
				} else {
					answer = new ConfirmNewFriendAnswer(true);
				}
				sendObject(answer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addToBlackList(AddToBlackList player) {
		try {
			ResultSet result = statement
					.executeQuery("SELECT id FROM black_list WHERE id_player_blocked = " + player.getId());
			AddToBlackListAnswer answer = null;
			if (!result.next()) {
				statement.executeUpdate("INSERT INTO black_list (id_player, id_player_blocked) VALUES (" + idPlayer
						+ ", " + player.getId() + ")");
				answer = new AddToBlackListAnswer(true);
			} else {
				answer = new AddToBlackListAnswer(false);
			}
			sendObject(answer);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getBalance() {
		try {
			ResultSet result = statement.executeQuery("SELECT experience, money FROM player WHERE id = " + idPlayer);
			if (result.next()) {
				MyBalanceAnswer answer = new MyBalanceAnswer(result.getInt(1), result.getInt(2));
				sendObject(answer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void operation(Operation operation) {
		try {
			OperationAnswer answer = null;
			if (operation.isCorps()) {
				if (operation.getType() == 1) {
					ResultSet result = statement
							.executeQuery("SELECT price FROM technique WHERE id = " + operation.getId());
					if (result.next()) {
						int difference = money - result.getInt(1);
						if (difference >= 0) {
							statement.executeUpdate("UPDATE player_technique SET bought = true WHERE id_player = "
									+ idPlayer + " AND id_technique = " + operation.getId());
							statement.executeUpdate(
									"UPDATE player SET money = " + difference + " WHERE id = " + idPlayer);
							money = difference;
							answer = new OperationAnswer(true);
						} else {
							answer = new OperationAnswer(false);
						}
					}
				} else if (operation.getType() == 2) {
					ResultSet result = statement
							.executeQuery("SELECT experience, position FROM technique WHERE id = " + operation.getId());
					if (result.next()) {
						int difference = experience - result.getInt(1);
						int position = result.getInt(2);
						if (difference >= 0) {
							statement.executeUpdate("UPDATE player_technique SET explored = true WHERE id_player = "
									+ idPlayer + " AND id_technique = " + operation.getId());
							statement.executeUpdate(
									"UPDATE player SET experience = " + difference + " WHERE id = " + idPlayer);
							result = statement
									.executeQuery("SELECT id FROM technique WHERE position = " + (position + 1));
							if (result.next()) {
								int id = result.getInt(1);
								statement.executeUpdate(
										"INSERT INTO player_technique (id_player, id_technique, available, bought, explored) VALUES ("
												+ idPlayer + ", " + id + ", true, false, false)");
								result = statement.executeQuery(
										"SELECT id, position FROM tower WHERE id IN (SELECT id_tower FROM technique_tower WHERE id_technique = "
												+ id + ")");
								while (result.next()) {
									if (position == 0) {
										statement.executeUpdate(
												"INSERT INTO player_tower (id_player, id_tower, available, bought, explored) VALUES ("
														+ idPlayer + ", " + result.getInt(1) + ", true, true, true)");
									} else if (position == 1) {
										statement.executeUpdate(
												"INSERT INTO player_tower (id_player, id_tower, available, bought, explored) VALUES ("
														+ idPlayer + ", " + result.getInt(1) + ", true, false, false)");
									} else {
										break;
									}
								}
							}
							experience = difference;
							answer = new OperationAnswer(true);
						} else {
							answer = new OperationAnswer(false);
						}
					}
				} else {
					ResultSet result = statement
							.executeQuery("SELECT id, price FROM technique WHERE id = " + operation.getId());
					if (result.next()) {
						int id = result.getInt(1);
						int price = result.getInt(2);
						statement.executeUpdate("UPDATE player_technique SET bought = false WHERE id_player = "
								+ idPlayer + " AND id_technique = " + id);
						money += price / 2;
						statement.executeUpdate("UPDATE player SET money = " + money + " WHERE id = " + idPlayer);
						answer = new OperationAnswer(true);
					}
				}
			} else {
				if (operation.getType() == 1) {
					ResultSet result = statement
							.executeQuery("SELECT id, price FROM tower WHERE id = " + operation.getId());
					if (result.next()) {
						int id = result.getInt(1);
						int difference = money - result.getInt(2);
						if (difference >= 0) {
							statement.executeUpdate("UPDATE player_tower SET bought = true WHERE id_player = "
									+ idPlayer + " AND id_tower = " + id);
							statement.executeUpdate(
									"UPDATE player SET money = " + difference + " WHERE id = " + idPlayer);
							money = difference;
							answer = new OperationAnswer(true);
						} else {
							answer = new OperationAnswer(false);
						}
					}
				} else if (operation.getType() == 2) {
					ResultSet result = statement
							.executeQuery("SELECT experience, position FROM tower WHERE id = " + operation.getId());
					if (result.next()) {
						int difference = experience - result.getInt(1);
						int position = result.getInt(2);
						if (difference >= 0) {
							statement.executeUpdate("UPDATE player_tower SET explored = true WHERE id_player = "
									+ idPlayer + " AND id_tower = " + operation.getId());
							statement.executeUpdate(
									"UPDATE player SET experience = " + difference + " WHERE id = " + idPlayer);
							result = statement.executeQuery("SELECT id FROM tower WHERE position = " + (position + 1));
							if (result.next()) {
								statement.executeUpdate(
										"INSERT INTO player_tower (id_player, id_technique, available, bought, explored) VALUES ("
												+ idPlayer + ", " + result.getInt(1) + ", true, false, false)");
							}
							experience = difference;
							answer = new OperationAnswer(true);
						} else {
							answer = new OperationAnswer(false);
						}
					}
				} else {
					ResultSet result = statement
							.executeQuery("SELECT id, price FROM tower WHERE id = " + operation.getId());
					if (result.next()) {
						int id = result.getInt(1);
						int price = result.getInt(2);
						statement.executeUpdate("UPDATE player_tower SET bought = false WHERE id_player = " + idPlayer
								+ " AND id_tower = " + id);
						money += price / 2;
						statement.executeUpdate("UPDATE player SET money = " + money + " WHERE id = " + idPlayer);
						answer = new OperationAnswer(true);
					}
				}
			}
			sendObject(answer);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void sendObject(Object object) {
		try {
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
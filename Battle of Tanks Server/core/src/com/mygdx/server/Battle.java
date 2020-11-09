package com.mygdx.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import answers.ConnectToBattleServerAnswer;
import answers.CreateShellsAnswer;
import answers.OtherPlayerTechnique;
import answers.OtherPlayersTechniqueAnswer;
import map.Collision;
import map.Map;
import net.UDP;
import net.UDPInputObject;
import net.UDPOutputObject;
import queries.ConfirmPlayer;
import technique.Corps;
import technique.Gun;
import technique.Shell;
import technique.ShootingStyle;
import technique.Technique;
import technique.TechniqueType;
import technique.Tower;

public class Battle extends Thread {
	private UDP udp;
	private ServerSocket serverSocket;
	private ArrayList<Player> players;
	private volatile ArrayDeque<Socket> tempPlayers;
	private ArrayDeque<PlayerBattle> playerLoaded;
	private BattleType battleType;
	private Map map;
	private Collision collision;
	private BattleCommand battleCommand;
	private LinkedList<Shell> shells;
	private CreateShellsAnswer createShellsAnswer;

	private Thread receivePlayers;
	private Thread playerListener;

	private boolean isFinished;
	private boolean isPlayersLoaded;
	private boolean isBusy;

	public Battle(int portTCP, int portUDP) {
		playerLoaded = new ArrayDeque<PlayerBattle>();
		tempPlayers = new ArrayDeque<Socket>();
		shells = new LinkedList<Shell>();
		createShellsAnswer = new CreateShellsAnswer();

		try {
			serverSocket = new ServerSocket(portTCP);
		} catch (IOException e) {
			e.printStackTrace();
		}

		udp = new UDP(portUDP);

		createReceivePlayersListener();
		createPlayerListener();
	}

	private void createReceivePlayersListener() {
		receivePlayers = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						tempPlayers.add(serverSocket.accept());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void createPlayerListener() {
		playerListener = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						checkConnectedPlayers();
						checkPlayerTCP();
						checkPlayerUDP();
						Thread.sleep(1);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		});
	}

	private void checkConnectedPlayers() {
		final Iterator<Socket> socketIterator = tempPlayers.iterator();
		while (socketIterator.hasNext()) {
			final Socket socket = socketIterator.next();
			try {
				final InputStream inputStream = socket.getInputStream();
				if (inputStream.available() > 0) {
					final ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
					final Object object = objectInputStream.readObject();
					if (object.getClass() == ConfirmPlayer.class) {
						final ConfirmPlayer confirm = (ConfirmPlayer) object;
						for (Player player : players) {
							if (!player.isConnected() && confirm.getId() == player.getId()) {
								playerLoaded.add(new PlayerBattle(socket, player));
								player.setConnected(true);
								socketIterator.remove();
								break;
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Метод проверяет входящие TCP пакеты и направляет их в класс соответстввующего
	 * игрока
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 **/
	private void checkPlayerTCP() {
		for (PlayerBattle playerBattle : playerLoaded) {
			try {
				final InputStream inputStream = playerBattle.getSocket().getInputStream();
				if (inputStream.available() > 0) {
					final ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
					final Object object = objectInputStream.readObject();
					playerBattle.getPlayer().addTCPInputObject(object);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Метод проверяет входящие UDP пакеты и направляет их в класс соответстввующего
	 * игрока
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 **/
	private void checkPlayerUDP() {
		while (udp.getObjects().size() > 0) {
			final UDPInputObject object = udp.getObjects().poll();
			if (object != null) {
				for (int i = 0; i < players.size(); i++) {
					final Player player = players.get(i);
					final DatagramPacket packet = object.getPacket();
					final boolean ip = packet.getAddress().equals(player.getInetAddress());
					final boolean port = object.getPacket().getPort() == player.getPort();
					if (ip && port) {
						player.addUDPInputObject(object);
						break;
					}
				}
			}
		}
	}

	public void load(ArrayDeque<PlayerSettings> players) {
		this.players = new ArrayList<Player>();

		isBusy = true;
		isFinished = false;

		battleType = players.getFirst().getBattleType();
		collision = new Collision(shells);

		loadPlayers(players);
		loadMap();

		if (battleType == BattleType.COMMAND)
			battleCommand = new BattleCommand(this.players);

		sendServerInfo(players);

		try {
			Thread.sleep(500); // IMPORTANT!!!
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		sendOtherPlayersInfo();
		threadControl();
	}

	private void sendOtherPlayersInfo() {
		for (int i = 0; i < players.size(); i++) {
			final OtherPlayersTechniqueAnswer answer = new OtherPlayersTechniqueAnswer();
			for (int j = 0; j < players.size(); j++) {
				if (i == j)
					continue;
				final Player player = players.get(j);
				answer.getPlayers()
						.add(new OtherPlayerTechnique(player.getId(), player.getTechnique().getCorps().getId(),
								player.getTechnique().getTower().getId(), player.isLeftSide(),
								player.getUser().getNamePlayer()));
			}
			this.players.get(i).getUser().sendObject(answer);
		}
	}

	private void sendServerInfo(ArrayDeque<PlayerSettings> players) {
		final Iterator<PlayerSettings> playerSettingsIterator = players.iterator();
		while (playerSettingsIterator.hasNext()) {
			final PlayerSettings playerSettings = playerSettingsIterator.next();
			int id = 0;
			Player currPlayer = null;
			for (Player player : this.players) {
				if (playerSettings.getUserHandler().equals(player.getUser())) {
					id = player.getId();
					currPlayer = player;
					break;
				}
			}
			try {
				final ConnectToBattleServerAnswer answer = new ConnectToBattleServerAnswer(
						InetAddress.getLocalHost().getHostAddress(), serverSocket.getLocalPort(), udp.getPort(),
						map.getName(), id, currPlayer.isLeftSide());
				playerSettings.getUserHandler().sendObject(answer);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadPlayers(ArrayDeque<PlayerSettings> players) {
		int index = 0;
		final Iterator<PlayerSettings> playerSettingsIterator = players.iterator();
		while (playerSettingsIterator.hasNext()) {
			final PlayerSettings playerSettings = playerSettingsIterator.next();
			final Status status = playerSettings.getUserHandler().getStatus();
			status.setPlaying(true);
			status.setWantsPlay(false);
			createPlayer(playerSettings, index);
			index++;
		}
	}

	private void loadMap() {
		final FileHandle folder = Gdx.files.internal("maps/");
		final int mapAmount = folder.list().length;
		final int randomMap = MathUtils.random(0, mapAmount - 1);
		final String mapName = folder.list()[randomMap].nameWithoutExtension();
		map = new Map(mapName);
		while (!map.isLoaded()) {
			map.act();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void createPlayer(PlayerSettings playerSettings, int index) {
		try {
			final Statement statementTechnique = Server.getConnection().createStatement();
			final Statement statementTower = Server.getConnection().createStatement();
			final String queryTechnique = "SELECT * FROM technique WHERE id = " + playerSettings.getIdTechnique();
			final String queryTower = "SELECT * FROM tower WHERE id = " + playerSettings.getIdTower();
			final ResultSet resultTechnique = statementTechnique.executeQuery(queryTechnique);
			final ResultSet resultTower = statementTower.executeQuery(queryTower);

			final Corps corps = createCorps(resultTechnique);
			statementTechnique.close();

			final Tower tower = createTower(resultTower, corps);
			statementTower.close();

			final Technique technique = new Technique(corps, tower);

			final Player player = new Player(technique, playerSettings.getUserHandler(), index, shells);
			player.getShooting().setShellsList(shells);

			players.add(player);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Tower createTower(ResultSet resultTower, Corps corps) {
		Tower tower = null;
		try {
			if (resultTower.next()) {
				final Gun gun = new Gun();
				tower = new Tower(resultTower.getInt(1));
				tower.setName(resultTower.getString(2));
				tower.setSpeedRotation(resultTower.getFloat(5));
				tower.setTimeReduction(resultTower.getFloat(6));
				tower.setTimeRecharge(resultTower.getFloat(7));
				tower.setMinDamage(resultTower.getInt(8));
				tower.setMaxDamage(resultTower.getInt(9));
				tower.setShellsTotal(resultTower.getInt(10));
				gun.setShellSpeed(resultTower.getFloat(11));
				tower.setLevel(resultTower.getInt(12));
				tower.setMinRadius(resultTower.getFloat(13));
				tower.setMaxRadius(resultTower.getFloat(14));
				tower.setRotationLeft(resultTower.getString(17) == null ? 0 : resultTower.getFloat(17));
				tower.setRotationRight(resultTower.getString(18) == null ? 0 : resultTower.getFloat(18));
				tower.setX(resultTower.getFloat(19));
				tower.setY(resultTower.getFloat(20));
				tower.setWidth(resultTower.getFloat(21));
				tower.setHeight(resultTower.getFloat(22));
				tower.setOriginX(resultTower.getFloat(23));
				tower.setOriginY(resultTower.getFloat(24));
				gun.setShellWidth(resultTower.getFloat(27));
				gun.setShellHeight(resultTower.getFloat(28));

				final int shellsCassette = resultTower.getString(15) == null ? 0 : resultTower.getInt(15);
				final float shellRecharge = resultTower.getString(16) == null ? 0 : resultTower.getFloat(16);

				final Statement statement = Server.getConnection().createStatement();

				final ResultSet resultStyle = statement
						.executeQuery("SELECT style FROM shooting_style WHERE id = " + resultTower.getString(26));
				ShootingStyle shootingStyle = null;
				if (resultStyle.next())
					shootingStyle = giveShootingStyle(resultStyle.getString(1));

				if (shootingStyle != null) {
					gun.setCassette(shellsCassette, shellRecharge, shootingStyle);
				}

				if (gun.getPoints().size == 0) {
					final String[] points = Gdx.files
							.internal("models/" + corps.getName() + "/images/towers/1/gunPoints.txt").readString()
							.split("\\s+");
					for (int i = 0; i < points.length; i++) {
						gun.getPoints().add(new Vector2(Float.parseFloat(points[i]), Float.parseFloat(points[++i])));
					}
				}

				tower.setGun(gun);
				statement.close();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tower;
	}

	private ShootingStyle giveShootingStyle(String style) {
		ShootingStyle shootingStyle = null;
		if (style.toLowerCase().equals("in_turn")) {
			shootingStyle = ShootingStyle.IN_TURN;
		} else if (style.toLowerCase().equals("random")) {
			shootingStyle = ShootingStyle.RANDOM;
		} else if (style.toLowerCase().equals("together")) {
			shootingStyle = ShootingStyle.TOGETHER;
		}
		return shootingStyle;
	}

	private Corps createCorps(ResultSet resultTechnique) {
		Corps corps = null;
		try {
			if (resultTechnique.next()) {
				corps = new Corps(resultTechnique.getInt(1));
				corps.setName(resultTechnique.getString(2));
				corps.setSpeedRotation(resultTechnique.getFloat(6));
				corps.setMaxHealth(resultTechnique.getInt(7));
				corps.setMaxSpeed(resultTechnique.getFloat(8));
				corps.setAcceleration(resultTechnique.getFloat(9));
				corps.setLevel(resultTechnique.getInt(10));
				corps.setWidth(resultTechnique.getFloat(11));
				corps.setHeight(resultTechnique.getFloat(12));
				giveCorpsType(resultTechnique.getInt(5), corps);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return corps;
	}

	private void giveCorpsType(int type, Corps corps) {
		if (type == 1) {
			corps.setTechniqueType(TechniqueType.TANK);
		} else if (type == 2) {
			corps.setTechniqueType(TechniqueType.ARTY);
		} else if (type == 3) {
			corps.setTechniqueType(TechniqueType.REACTIVE_SYSTEM);
		} else {
			corps.setTechniqueType(TechniqueType.FLAMETHROWER_SYSTEM);
		}
	}

	private void threadControl() {
		if (!receivePlayers.isAlive())
			receivePlayers.start();
		if (!playerListener.isAlive())
			playerListener.start();
		if (!udp.isAlive())
			udp.start();
		if (!isAlive())
			start();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public BattleType getBattleType() {
		return battleType;
	}

	public boolean isBusy() {
		return isBusy;
	}

	private void checkReadyingPlayers() {
		if (!isPlayersLoaded) {
			boolean isReady = true;
			for (Player player : players) {
				player.readinessTest();
				if (!player.isReady()) {
					isReady = false;
				}
			}
			isPlayersLoaded = isReady;
		}
	}

	private void battleCommandController() {
		if (battleCommand != null) {
			battleCommand.act(map, collision, isPlayersLoaded, udp, createShellsAnswer);
			if (battleCommand.isFinished()) {
				battleCommand = null;
				players.clear();
				playerLoaded.clear();
				tempPlayers.clear();
				shells.clear();
				isBusy = false;
				isPlayersLoaded = false;
			}
		}
	}

	private void shellsController() {
		if (!createShellsAnswer.getShells().isEmpty()) {
			for (Player player : players) {
				final UDPOutputObject object = new UDPOutputObject(player.getInetAddress(), player.getPort(),
						createShellsAnswer);
				object.sendObject(udp.getSocket());
			}
			createShellsAnswer.getShells().clear();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				checkReadyingPlayers();
				battleCommandController();
				shellsController();
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
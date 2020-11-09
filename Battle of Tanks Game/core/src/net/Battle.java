package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.BattleType;
import com.mygdx.game.DeathsBlock;
import com.mygdx.game.Objects;

import answers.OtherPlayerTechnique;
import answers.OtherPlayersTechniqueAnswer;
import map.Map;
import queries.ConfirmPlayer;
import queries.UDPData;
import technique.Corps;
import technique.Gun;
import technique.Shell;
import technique.Technique;
import technique.Tower;

public class Battle implements Disposable {
	protected Socket socket;
	protected DatagramSocket socketUDP;
	protected Map map;
	protected Array <Player> players;
	protected Player player;
	protected Thread serverListenerTCP;
	protected Thread serverListenerUDP;
	protected ObjectInputStream objectInputStream;
	protected ObjectOutputStream objectOutputStream;
	protected LinkedList <Shell> shells;
	protected BattleType battleType;
	protected Texture destroyShellTexture;
	protected Texture destroyTechniqueTexture;
	protected Texture spotTexture;
	protected Texture flameTexture;
	protected Music music1;
	protected Music music2;
	protected DeathsBlock deathsBlock;
	
	protected int portUDP;
	protected boolean isConnected;
	protected boolean isMapLoaded;
	protected boolean isFinished;
	protected boolean isExit;
	
	public Battle () {
		players = new Array <Player>();
		
		destroyShellTexture = new Texture(Gdx.files.internal("models/shellDestroy.png"));
		destroyTechniqueTexture = new Texture(Gdx.files.internal("models/techniqueDestroy.png"));
		spotTexture = new Texture(Gdx.files.internal("models/spot.png"));
		flameTexture = new Texture(Gdx.files.internal("images/flame.png"));
		
		music1 = Gdx.audio.newMusic(Gdx.files.internal("sounds/entertainment.mp3"));
		music2 = Gdx.audio.newMusic(Gdx.files.internal("sounds/entertainment2.mp3"));
		
		music1.setLooping(true);
		music1.setVolume(0.3f);
		music2.setLooping(true);
		
		serverListenerTCP = new Thread (new Runnable () {
			@Override
			public void run () {
				try {
					while (true) {
						if (socket.getInputStream().available() > 0) {
							objectInputStream = new ObjectInputStream(socket.getInputStream());
							objectInputStream.readObject();
						}
						Thread.sleep(1);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		
		serverListenerUDP = new Thread (new Runnable () {
			@Override
			public void run () {
				while (true) {
					try {
						byte[] buf = new byte[1024];
						DatagramPacket packet = new DatagramPacket(buf, buf.length);
						socketUDP.receive(packet);
						player.addUDPInputObject(new UDPInputObject(packet));
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		deathsBlock = new DeathsBlock();
	}
	
	public void connectTo (final String host, final int portTCP, int portUDP, final String mapName) {
		this.portUDP = portUDP;
		new Thread (new Runnable () {
			@Override
			public void run () {
				try {
					socket = new Socket(host, portTCP);
					socketUDP = new DatagramSocket();
					isConnected = true;
					shells = new LinkedList <Shell>();
					serverListenerTCP.start();
					serverListenerUDP.start();
					map = new Map(mapName);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void loadMap () {
		if (!isMapLoaded) {
			if (map != null) {
				map.act();
				if (map.isLoaded()) {
					isMapLoaded = true;
				}
			}
		}
	}
	
	public void loadPlayer (int id, int idTechnique, int idTower, boolean isLeftSide, String name) {
		player = createPlayer(id, idTechnique, idTower, isLeftSide, name);
		player.setMe(true);
		players.add(player);
		player.setFlameTexture(flameTexture);
		if (!isLeftSide) player.rotate(180);
	}
	
	public void loadPlayers (OtherPlayersTechniqueAnswer answer) {
		for (OtherPlayerTechnique playerData : answer.getPlayers()) {
			players.add(createPlayer(playerData.getId(), playerData.getIdTechnique(), playerData.getIdTower(), playerData.isLeftSide(), playerData.getName()));
		}
	}
	
	public Player createPlayer (int id, int idTechnique, int idTower, boolean isLeftSide, String name) {
		Corps corps = null;
		Tower tower = null;
		
		block : {
			for (Corps corpsTemp : Objects.getInstance().getCorpses()) {
				if (corpsTemp.getId() == idTechnique) {
					corps = corpsTemp;
					for (Tower towerTemp : corps.getTowers()) {
						if (towerTemp.getId() == idTower) {
							tower = towerTemp;
							break block;
						}
					}
				}
			}
		}
		
		if (corps.getWholeTexture() == null) corps.setWholeTexture(new Texture(Gdx.files.internal("models/" + corps.getName() + "/images/corps.png")));
		if (tower.getWholeTexture() == null) tower.setWholeTexture(new Texture(Gdx.files.internal("models/" + corps.getName() + "/images/towers/1/tower.png")));
		if (corps.getDestroyedTexture() == null) corps.setDestroyedTexture(new Texture(Gdx.files.internal("models/" + corps.getName() + "/images/corpsDestroyed.png")));
		if (tower.getDestroyedTexture() == null) tower.setDestroyedTexture(new Texture(Gdx.files.internal("models/" + corps.getName() + "/images/towers/1/towerDestroyed.png")));
		
		Gun gun = tower.getGun();
		
		if (gun.getShellTexture() == null) gun.setShellTexture(new Texture(Gdx.files.internal("models/" + corps.getName() + "/images/shell.png")));
		if (gun.getShotSound() == null) gun.setShotSound(Gdx.audio.newSound(Gdx.files.internal("models/" + corps.getName() + "/sounds/shot.mp3")));
		
		if (gun.getPoints().size == 0) {
			String [] points = Gdx.files.internal("models/" + corps.getName() + "/images/towers/1/gunPoints.txt").readString().split("\\s+");
			for (int i = 0; i < points.length; i++) {
				gun.getPoints().add(new Vector2(Float.parseFloat(points[i]), Float.parseFloat(points[++i])));
			}
		}
		
		Player player = new Player(new Technique(corps, tower), id, destroyTechniqueTexture, destroyShellTexture, name);
		player.setLeftSide(isLeftSide);
		if (isLeftSide) player.rotate(180);
		return player;
	}
	
	public void setReady () {
		try {
			ConfirmPlayer confirm = new ConfirmPlayer(player.getId());
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject(confirm);
			objectOutputStream.flush();
			UDPData data = new UDPData(InetAddress.getLocalHost(), socketUDP.getLocalPort());
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectOutputStream.writeObject(data);
			objectOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void show (SpriteBatch batch, PlayersTable table) {
		music1.play();
		music2.play();
		map.show(batch);
		player.act(socketUDP, socket.getInetAddress().getHostAddress(), portUDP, map, players, shells, table, deathsBlock);
		if (player.isStartPosition()) {
			for (Player otherPlayer : players) {
				otherPlayer.getTechnique().getCorpsBackground().draw(batch);
				otherPlayer.getTechnique().getTowerBackground().draw(batch);
				
				if (!otherPlayer.isLive()) {
					otherPlayer.getDestroyAnimation().show(batch);
				}
				
				Rectangle rectangle = otherPlayer.getTechnique().getCorpsBackground().getBoundingRectangle();
				
				otherPlayer.getNameLabel().setPosition(rectangle.getX() + rectangle.getWidth() / 2 - otherPlayer.getNameLabel().getWidth() / 2, rectangle.getY() + rectangle.getHeight() + 70);
				otherPlayer.getHealthLine().setPosition(rectangle.getX() + rectangle.getWidth() / 2 - otherPlayer.getHealthLine().getWidth() / 2, rectangle.getY() + rectangle.getHeight() + 30);
				otherPlayer.getHp().setPosition(rectangle.getX() + rectangle.getWidth() / 2 - otherPlayer.getHp().getWidth() / 2, rectangle.getY() + rectangle.getHeight() + 20);
				
				otherPlayer.getNameLabel().draw(batch);
				otherPlayer.getHealthLine().draw(batch);
				otherPlayer.getHp().draw(batch);
				if (otherPlayer.getHps() != null) {
					otherPlayer.updateHps();
					if (otherPlayer.getHps() != null) {
						otherPlayer.getHps().draw(batch);
					}
				}
			}
		}
		for (int i = 0; i < shells.size(); i++) {
			Shell shell = shells.get(i);
			shell.moveTo(batch);
			if (!shell.isLive()) shells.remove(i);
		}
		
		deathsBlock.show(batch);
		player.getAim().show(batch);
	}
	
	public boolean isConnected () {
		return isConnected;
	}
	
	public boolean isMapLoaded () {
		return isMapLoaded;
	}
	
	public int getPortUDP () {
		return portUDP;
	}
	
	public Map getMap () {
		return map;
	}
	
	public boolean isFinished () {
		return isFinished;
	}
	
	public boolean isExit () {
		return isExit;
	}

	@Override
	public void dispose () {
		if (map != null) map.dispose();
		destroyShellTexture.dispose();
		destroyTechniqueTexture.dispose();
		spotTexture.dispose();
		music1.dispose();
		music2.dispose();
		flameTexture.dispose();
	}
}
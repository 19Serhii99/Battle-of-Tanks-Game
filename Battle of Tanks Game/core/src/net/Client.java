package net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.BattleType;

import queries.AddNewFriend;
import queries.AddToBlackList;
import queries.ConfirmationCodeAcc;
import queries.Exit;
import queries.FindFriend;
import queries.Operation;
import queries.PasswordAfterRecovery;
import queries.PasswordRecovery;
import queries.PasswordRecoveryCode;
import queries.PlayBattle;
import queries.Query;
import queries.Registration;
import queries.SignIn;

public class Client {
	private static Client client;
	private Socket socket;
	private ServerListener serverListener;
	private ObjectOutputStream objectOutputStream;
	
	private boolean isServerConnection = false;
	private boolean isSent = false;
	private boolean isSendingError = false;
	
	private Client () {
		
	}
	
	public void connectToServer () {
		new Thread (new Runnable () {
			@Override
			public void run () {
				try {
					socket = new Socket(Gdx.files.internal("server.txt").readString(), 45678);
					serverListener = new ServerListener(socket);
					objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
					isServerConnection = true;
					serverListener.start();
				} catch (UnknownHostException e) {
					isServerConnection = false;
				} catch (IOException e) {
					isServerConnection = false;
				}
			}
		}).start();
	}
	
	public static Client getInstance () {
		if (client == null) client = new Client();
		return client;
	}
	
	public void signIn (String login, String password) {
		SignIn signIn = new SignIn(login, password);
		sendObject(signIn);
	}
	
	public void registration (String login, String nickname, String password) {
		Registration registration = new Registration(login, password, nickname);
		sendObject(registration);
	}
	
	public void confirmCodeAcc (String code) {
		ConfirmationCodeAcc confirmationCodeAcc = new ConfirmationCodeAcc(code);
		sendObject(confirmationCodeAcc);
	}
	
	public void passwordRecovery (String login) {
		PasswordRecovery passwordRecovery = new PasswordRecovery(login);
		sendObject(passwordRecovery);
	}
	
	public void passwordRecoveryCode (String code) {
		PasswordRecoveryCode passwordRecoveryCode = new PasswordRecoveryCode(code);
		sendObject(passwordRecoveryCode);
	}
	
	public void passwordAfterRecovery (String password) {
		PasswordAfterRecovery passwordAfterRecovery = new PasswordAfterRecovery(password);
		sendObject(passwordAfterRecovery);
	}
	
	public void sendQuery (String query) {
		Query getDataPlayer = new Query(query);
		sendObject(getDataPlayer);
	}
	
	public void exit (int code) {
		Exit exit = new Exit(code);
		sendObject(exit);
	}
	
	public void playBattle (BattleType battleType, int idTechnique, int idTower) {
		PlayBattle playBattle = new PlayBattle(battleType.toString().toLowerCase(), idTechnique, idTower);
		sendObject(playBattle);
	}
	
	public void findFriend (String nickname, int id, boolean isUp) {
		FindFriend find = new FindFriend(nickname, id, isUp);
		sendObject(find);
	}
	
	public void addNewFriend (int id) {
		AddNewFriend add = new AddNewFriend(id);
		sendObject(add);
	}
	
	public void addToBlackList (int id) {
		AddToBlackList add = new AddToBlackList(id);
		sendObject(add);
	}
	
	public void doOperation (int type, boolean isCorps, int id) {
		Operation operation = new Operation(type, isCorps, id);
		sendObject(operation);
	}
	
	private void sendObject (final Object object) {
		new Thread (new Runnable () {
			@Override
			public void run () {
				try {
					objectOutputStream.writeObject(object);
					objectOutputStream.flush();
					isSent = true;
				} catch (IOException e) {
					isSendingError = true;
				} catch (NullPointerException e) {
					isSendingError = true;
				}
			}
		}).start();
	}
	
	public boolean isServerConnection () {
		return isServerConnection;
	}
	
	public void setServerConnection (boolean value) {
		isServerConnection = value;
	}
	
	public void resetData () {
		isSent = false;
		isSendingError = false;
	}
	
	public boolean isSent () {
		return isSent;
	}
	
	public boolean isSendingError () {
		return isSendingError;
	}
	
	public ServerListener getServerListener () {
		return serverListener;
	}
}
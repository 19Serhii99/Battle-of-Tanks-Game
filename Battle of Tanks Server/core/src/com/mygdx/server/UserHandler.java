package com.mygdx.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import additional.DataPlayerOBJ;
import additional.Mail;
import additional.RecoveryPasswordOBJ;
import answers.ConfirmCancelBattleAnswer;
import answers.ConfirmationCodeAccAnswer;
import answers.IsOnlinePlayerAnswer;
import answers.RSAKey;
import answers.RegistrationAnswer;
import answers.SignInAnswer;
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

public class UserHandler extends Thread implements Comparable<UserHandler> {
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private Statement statement;
	private volatile Status status;
	private RecoveryPasswordOBJ recoveryPasswordOBJ;
	private Integer idPlayer;
	private DataPlayerOBJ dataPlayer;
	private String name;

	private PrivateKey privateKey;
	private PublicKey publicKey;

	private long sleepTime = 1;
	private boolean online = true;

	public UserHandler(Socket socket, Connection mysql) {
		this.socket = socket;
		this.status = new Status();
		try {
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			statement = mysql.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		super.setDaemon(true);
		start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (online) {
					try {
						IsOnlinePlayerAnswer answer = new IsOnlinePlayerAnswer();
						sendObject(answer);
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void createRSAKeys() {
		try {
			final int keySize = 2048;

			final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(keySize);

			final KeyPair pair = generator.generateKeyPair();

			publicKey = pair.getPublic();
			privateKey = pair.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private byte[] encrypt(String string) throws Exception {
		final Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(string.getBytes("ISO-8859-1"));
	}

	private byte[] decrypt(String string) throws Exception {
		final Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(string.getBytes("ISO-8859-1"));
	}

	@Override
	public void run() {
		createRSAKeys();
		sendObject(new RSAKey(publicKey.getEncoded()));
		while (online) {
			try {
				if (socket.getInputStream().available() > 0) {
					try {
						Object object = objectInputStream.readObject();
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							dispose();
						}
						if (object.getClass() == SignIn.class) {
							signIn((SignIn) object);
						} else if (object.getClass() == Registration.class) {
							registration((Registration) object);
						} else if (object.getClass() == ConfirmationCodeAcc.class) {
							confirmCodeAcc((ConfirmationCodeAcc) object);
						} else if (object.getClass() == PasswordRecovery.class) {
							if (recoveryPasswordOBJ == null)
								recoveryPasswordOBJ = new RecoveryPasswordOBJ(idPlayer, statement, objectOutputStream);
							recoveryPasswordOBJ.passwordRecovery((PasswordRecovery) object);
						} else if (object.getClass() == PasswordRecoveryCode.class) {
							recoveryPasswordOBJ.passwordRecoveryCode((PasswordRecoveryCode) object);
						} else if (object.getClass() == PasswordAfterRecovery.class) {
							recoveryPasswordOBJ.passwordAfterRecovery((PasswordAfterRecovery) object);
						} else if (object.getClass() == Query.class) {
							Query query = (Query) object;
							if (query.getQuery().equals("dataPlayer")) {
								if (status.isAuthorizated() && status.isConfirmedAcc()) {
									dataPlayer.getData();
								}
							} else if (query.getQuery().equals("modules")) {
								dataPlayer.getModules();
							} else if (query.getQuery().equals("cancelBattle")) {
								status.setWantsPlay(false);
								final ConfirmCancelBattleAnswer answer = new ConfirmCancelBattleAnswer(true);
								sendObject(answer);
							} else if (query.getQuery().equals("balance")) {
								dataPlayer.getBalance();
							}
						} else if (object.getClass() == PlayBattle.class) {
							dataPlayer.playBattle((PlayBattle) object, this);
						} else if (object.getClass() == Exit.class) {
							handleExit((Exit) object);
						} else if (object.getClass() == FindFriend.class) {
							dataPlayer.findFriend((FindFriend) object);
						} else if (object.getClass() == AddNewFriend.class) {
							dataPlayer.addNewFriend((AddNewFriend) object);
						} else if (object.getClass() == AddToBlackList.class) {
							dataPlayer.addToBlackList((AddToBlackList) object);
						} else if (object.getClass() == Operation.class) {
							dataPlayer.operation((Operation) object);
						}
					} catch (ClassNotFoundException e) {
						dispose();
					}
				}
			} catch (IOException e) {
				dispose();
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				dispose();
			}
		}
	}

	private void signIn(SignIn logIn) {
		SignInAnswer answer = null;
		if (!status.isAuthorizated() || !status.isConfirmedAcc()) {
			if (logIn.getLogin() != null && logIn.getPassword() != null) {
				try {
					final String login = new String(decrypt(logIn.getLogin()));
					final String password = new String(decrypt(logIn.getPassword()));
					final String query = "SELECT id, confirmed, nickname FROM player WHERE login LIKE '" + login
							+ "' AND password LIKE '" + password + "'";
					final ResultSet result = statement.executeQuery(query);
					if (result.next()) {
						status.setAuthorizated(true);
						idPlayer = result.getInt(1);
						status.setConfirmedAcc(result.getBoolean(2));
						name = result.getString(3);
						answer = new SignInAnswer(status.isAuthorizated(), result.getBoolean(2), result.getString(3));
					} else {
						answer = new SignInAnswer(false, false, null);
					}
					result.close();
				} catch (SQLException e) {
					dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				answer = new SignInAnswer(false, status.isConfirmedAcc(), null);
			}
		} else {
			answer = new SignInAnswer(true, status.isConfirmedAcc(), name);
		}
		sendObject(answer);
		if (status.isAuthorizated() && status.isConfirmedAcc()) {
			sleepTime = 1;
			dataPlayer = new DataPlayerOBJ(statement, idPlayer, objectOutputStream);
			Server.getTCP().getPlayers().add(this);
		}
	}

	private void registration(Registration registration) {
		if (registration.getLogin() != null && registration.getNickname() != null
				&& registration.getPassword() != null) {
			String login = registration.getLogin().trim();
			String nickname = registration.getNickname().trim();
			String password = registration.getPassword().trim();
			String query = "SELECT id FROM player WHERE login LIKE '" + login + "'";
			try {
				if (!statement.executeQuery(query).next()) {
					query = "SELECT id FROM player WHERE nickname LIKE '" + nickname + "'";
					if (!statement.executeQuery(query).next()) {
						if (login.length() > 5 && login.length() <= 150 && nickname.length() >= 4
								&& nickname.length() <= 50 && password.length() >= 5 && password.length() <= 20) {
							Pattern loginPattern = Pattern.compile("[A-Za-z0-9._]+@[A-Za-z0-9._]+");
							if (!loginPattern.matcher(login).matches()) {
								return;
							}
							Pattern pattern = Pattern.compile("[a-zA-Z0-9_]+");
							if (!pattern.matcher(nickname).matches()) {
								return;
							}
							if (pattern.matcher(password).matches()) {
								StringBuilder code = new StringBuilder();
								int count = 10 + (int) Math.random() * 10;
								for (int i = 0; i < count; i++) {
									code.append((int) (Math.random() * 10));
								}
								Mail.send(login, "Подтверждение регистрации аккаунта Battle of Tanks",
										"Код для подтверждения регистрации: " + code + ".");

								statement.executeUpdate(
										"INSERT INTO player (login, nickname, password, dt_reg, dt_last_seen) VALUES ('"
												+ login + "', '" + nickname + "', '" + password + "', '"
												+ getCurrentDateTime() + "', '" + getCurrentDateTime() + "')");

								FileHandle file = Gdx.files.local("codes/" + login + ".txt");
								file.writeString(String.valueOf(code), false);
								sendRegistrationAnswer(true, 0);
							}
						}
					} else {
						sendRegistrationAnswer(false, 1);
						return;
					}
				} else {
					sendRegistrationAnswer(false, 2);
					return;
				}
			} catch (SQLException e) {
				dispose();
			}
		}
	}

	private void sendRegistrationAnswer(boolean confirmed, int code) {
		RegistrationAnswer answer;
		if (code != 0)
			answer = new RegistrationAnswer(confirmed, code);
		else
			answer = new RegistrationAnswer(confirmed);
		sendObject(answer);
	}

	private void handleExit(Exit exit) {
		if (exit.getReason() == 0) {
			dataPlayer = null;
			sleepTime = 3000;
			status.setAuthorizated(false);
			status.setConfirmedAcc(false);
			Server.getTCP().getPlayers().remove(this);
		} else if (exit.getReason() == 1) {
			dataPlayer = null;
			try {
				socket.close();
			} catch (IOException e) {
				dispose();
			}
		}
	}

	private void confirmCodeAcc(ConfirmationCodeAcc code) {
		if (!status.isConfirmedAcc()) {
			ConfirmationCodeAccAnswer confirmationCodeAccAnswer;
			String tempCode = code.getCode().trim();
			String query = "SELECT login FROM player WHERE id =" + idPlayer;
			try {
				ResultSet result = statement.executeQuery(query);
				if (result.next()) {
					FileHandle file = Gdx.files.local("codes/" + result.getString(1) + ".txt");
					if (file.exists()) {
						String fileCode = file.readString().trim();
						if (tempCode.equals(fileCode)) {
							file.delete();
							query = "UPDATE player SET confirmed = true WHERE id = " + idPlayer;
							statement.executeUpdate(query);
							status.setConfirmedAcc(true);
							confirmationCodeAccAnswer = new ConfirmationCodeAccAnswer(true);
							Statement additionalStatement = statement.getConnection().createStatement();

							ResultSet techniqueResult = additionalStatement
									.executeQuery("SELECT id, position FROM technique");
							while (techniqueResult.next()) {
								boolean bought = false, available = false, explored = false, isInsert = false;
								if (techniqueResult.getInt(2) == 0) {
									bought = available = explored = isInsert = true;
								} else if (techniqueResult.getInt(2) == 1) {
									bought = explored = false;
									available = true;
									isInsert = true;
								}
								if (isInsert) {
									query = "INSERT INTO player_technique (id_player, id_technique, bought, available, explored) VALUES"
											+ "(" + idPlayer + ", " + techniqueResult.getInt(1) + ", " + bought + ", "
											+ available + ", " + explored + ")";
									statement.executeUpdate(query);
								}
							}
							techniqueResult.close();

							ResultSet towerResult = additionalStatement.executeQuery("SELECT id, position FROM tower");
							while (towerResult.next()) {
								boolean bought, available, explored;
								if (towerResult.getInt(2) == 0) {
									bought = available = explored = true;
								} else if (towerResult.getInt(2) == 1) {
									bought = explored = false;
									available = true;
								} else {
									bought = available = explored = false;
								}
								query = "INSERT INTO player_tower (id_player, id_tower, bought, available, explored) VALUES"
										+ "(" + idPlayer + ", " + towerResult.getInt(1) + ", " + bought + ", "
										+ available + ", " + explored + ")";
								statement.executeUpdate(query);
							}
							towerResult.close();

						} else {
							confirmationCodeAccAnswer = new ConfirmationCodeAccAnswer(false);
						}
						sendObject(confirmationCodeAccAnswer);
					}
				}
			} catch (SQLException e) {
				dispose();
			}
		}
		if (status.isAuthorizated() && status.isConfirmedAcc()) {
			sleepTime = 1;
			dataPlayer = new DataPlayerOBJ(statement, idPlayer, objectOutputStream);
			Server.getTCP().getPlayers().add(this);
		}
	}

	private String getCurrentDateTime() {
		return String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) + "-"
				+ String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1) + "-"
				+ String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + " "
				+ String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + ":"
				+ String.valueOf(Calendar.getInstance().get(Calendar.MINUTE)) + ":"
				+ String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
	}

	public void sendObject(Object object) {
		try {
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
		} catch (IOException e) {
			dispose();
		}
	}

	public int getIdPlayer() {
		return idPlayer;
	}

	public DataPlayerOBJ getDataPlayer() {
		return dataPlayer;
	}

	public Status getStatus() {
		return status;
	}

	public Socket getSocket() {
		return socket;
	}

	public String getNamePlayer() {
		return name;
	}

	public Connection getConnection() {
		try {
			return statement.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int compareTo(UserHandler user) {
		return idPlayer.compareTo(user.getIdPlayer());
	}

	public void dispose() {
		Server.getTCP().getUsers().remove(this);
		if (status.isAuthorizated() && status.isConfirmedAcc()) {
			Server.getTCP().getPlayers().remove(this);
		}
		try {
			statement.close();
		} catch (SQLException e) {

		}
		online = false;
	}
}

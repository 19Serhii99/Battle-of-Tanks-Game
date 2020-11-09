package additional;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import answers.PasswordAfterRecoveryAnswer;
import answers.PasswordRecoveryAnswer;
import answers.PasswordRecoveryCodeAnswer;
import queries.PasswordAfterRecovery;
import queries.PasswordRecovery;
import queries.PasswordRecoveryCode;

public class RecoveryPasswordOBJ {
	private Integer idPlayer;	
	private Statement statement;
	private ObjectOutputStream objectOutputStream;
	
	public RecoveryPasswordOBJ (Integer idPlayer, Statement statement, ObjectOutputStream objectOutputStream) {
		this.idPlayer = idPlayer;
		this.statement = statement;
		this.objectOutputStream = objectOutputStream;
	}
	
	public void passwordRecovery (PasswordRecovery recovery) {
		if (recovery.getLogin() != null) {
			PasswordRecoveryAnswer answer = null;
			String login = recovery.getLogin().trim();
			String query = "SELECT id FROM player WHERE login LIKE '" + login + "'";
			try {
				ResultSet result = statement.executeQuery(query);
				if (result.next()) {
					StringBuilder code = new StringBuilder();
					int count = 10 + (int)Math.random() * 10;
					for (int i = 0; i < count; i++) {
						code.append((int)(Math.random() * 10));
					}
					Mail.send(login, "Восстановление пароля аккаунта Battle of Tanks", "Код для восстановления пароля: " + code + ".");
					FileHandle file = Gdx.files.local("codes/" + login + "_recovery.txt");
					file.writeString(code.toString(), false);
					answer = new PasswordRecoveryAnswer(true);
					idPlayer = result.getInt(1);
				} else {
					answer = new PasswordRecoveryAnswer(false);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sendObject(answer);
		}
	}
	
	public void passwordRecoveryCode (PasswordRecoveryCode code) {
		if (code.getCode() != null) {
			String tempCode = code.getCode().trim();
			String query = "SELECT login FROM player WHERE id = " + idPlayer;
			try {
				ResultSet result = statement.executeQuery(query);
				if (result.next()) {
					FileHandle file = Gdx.files.local("codes/" + result.getString(1) + "_recovery.txt");
					if (file.exists()) {
						PasswordRecoveryCodeAnswer answer = null;
						if (tempCode.equals(file.readString())) {
							answer = new PasswordRecoveryCodeAnswer(true);
						} else {
							answer = new PasswordRecoveryCodeAnswer(false);
						}
						sendObject(answer);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void passwordAfterRecovery (PasswordAfterRecovery object) {
		if (object.getPassword() != null) {
			String password = object.getPassword().trim();
			if (password.length() >= 5 && password.length() <= 20) {
				Pattern pattern = Pattern.compile("[a-zA-Z0-9_]+");
				Matcher matcher = pattern.matcher(password);
				PasswordAfterRecoveryAnswer answer = null;
				if (matcher.matches()) {
					String query = "SELECT login FROM player WHERE id = " + idPlayer;
					try {
						ResultSet result = statement.executeQuery(query);
						if (result.next()) {
							FileHandle file = Gdx.files.local("codes/" + result.getString(1) + "_recovery.txt");
							if (file.exists()) {
								file.delete();
								query = "UPDATE player SET password = '" + password + "' WHERE id = " + idPlayer;
								statement.executeUpdate(query);
								answer = new PasswordAfterRecoveryAnswer(true);	
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					answer = new PasswordAfterRecoveryAnswer(false);
				}
				sendObject(answer);
			}
		}
	}
	
	private void sendObject (Object object) {
		try {
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
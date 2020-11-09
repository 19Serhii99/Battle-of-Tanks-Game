package language;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.badlogic.gdx.Gdx;

public class Language {
	private Properties properties;
	
	private String signIn;
	private String signUp;
	private String password;
	private String forgotPassword;
	private String createAcc;
	private String signInLabel;
	private String code;
	private String send;
	private String recoveryPassword;
	private String back;
	private String ok;
	private String recoveryPasswordTooltipOne;
	private String recoveryPasswordTooltipTwo;
	private String recoveryPasswordTooltipThree;
	private String exit;
	private String yes;
	private String no;
	private String sureToExit;
	private String authentication;
	private String processing;
	private String error;
	private String emailBusy;
	private String nicknameBusy;
	private String nickname;
	private String repeatPassword;
	private String sending;
	private String emailNotFound;
	private String loadingModules;
	private String loadingMenu;
	
	public Language (LanguageType type) {
		load(type);
	}
	
	public void load (LanguageType type) {
		properties = new Properties();
		try {
			properties.load(Gdx.files.internal("languages/" + type.toString().toLowerCase() + ".properties").read());
			signIn = getPhrase(properties.getProperty("signIn"));
			signUp = getPhrase(properties.getProperty("signUp"));
			password = getPhrase(properties.getProperty("password"));
			forgotPassword = getPhrase(properties.getProperty("forgotPassword"));
			createAcc = getPhrase(properties.getProperty("createAcc"));
			signInLabel = getPhrase(properties.getProperty("signInLabel"));
			code = getPhrase(properties.getProperty("code"));
			send = getPhrase(properties.getProperty("send"));
			recoveryPassword = getPhrase(properties.getProperty("send"));
			back = getPhrase(properties.getProperty("back"));
			ok = getPhrase(properties.getProperty("ok"));
			recoveryPasswordTooltipOne = getPhrase(properties.getProperty("recoveryPasswordTooltipOne"));
			recoveryPasswordTooltipTwo = getPhrase(properties.getProperty("recoveryPasswordTooltipTwo"));
			recoveryPasswordTooltipThree = getPhrase(properties.getProperty("recoveryPasswordTooltipThree"));
			exit = getPhrase(properties.getProperty("exit"));
			yes = getPhrase(properties.getProperty("yes"));
			no = getPhrase(properties.getProperty("no"));
			sureToExit = getPhrase(properties.getProperty("sureToExit"));
			authentication = getPhrase(properties.getProperty("authentication"));
			processing = getPhrase(properties.getProperty("processing"));
			error = getPhrase(properties.getProperty("error"));
			emailBusy = getPhrase(properties.getProperty("emailBusy"));
			nicknameBusy = getPhrase(properties.getProperty("nicknameBusy"));
			nickname = getPhrase(properties.getProperty("nickname"));
			repeatPassword = getPhrase(properties.getProperty("repeatPassword"));
			sending = getPhrase(properties.getProperty("sending"));
			emailNotFound = getPhrase(properties.getProperty("emailNotFound"));
			loadingModules = getPhrase(properties.getProperty("loadingModules"));
			loadingMenu = getPhrase(properties.getProperty("loadingMenu"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getPhrase (String phrase) {
		try {
			String [] strings = phrase.split("_");
			StringBuilder output = new StringBuilder();
			for (int i = 0; i < strings.length; i++) {
				output.append(strings[i]);
				if (i < strings.length - 1) {
					output.append(" ");
				}
			}
			return output.toString();
		} catch (NullPointerException e) {
			return "";
		}
	}
	
	public String getSignIn () {
		return signIn;
	}
	
	public String getSignUp () {
		return signUp;
	}
	
	public String getPassword () {
		return password;
	}
	
	public String getForgotPassword () {
		return forgotPassword;
	}
	
	public String getCreateAcc () {
		return createAcc;
	}
	
	public String getSignInLabel () {
		return signInLabel;
	}
	
	public String getCode () {
		return code;
	}
}
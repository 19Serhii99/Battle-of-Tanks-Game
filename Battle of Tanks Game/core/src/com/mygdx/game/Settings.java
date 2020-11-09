package com.mygdx.game;

import java.io.IOException;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import language.Language;
import language.LanguageType;

public class Settings {
	private static Settings settings;
	private Properties properties;
	private String login;
	private String password;
	private String name;
	private Language language;
	
	private float cameraWidth;
	private float cameraHeight;
	private float generalVolume;
	private float interfaceVolume;
	private float effectsVolume;
	private float techniqueVolume;
	private float environmentVolume;
	private boolean isVSync;
	private boolean isRememberAccount;
	private boolean isFullscreen;
	private boolean isFrameLimitation;
	private boolean isMute;
	private boolean isMuteWhenHidden;
	
	private Settings () {
		properties = new Properties();
		FileHandle file = Gdx.files.local("config.txt");
		if (file.exists()) {
			try {
				properties.load(file.read());
				cameraWidth = Float.parseFloat(properties.getProperty("cameraWidth"));
				cameraHeight = Float.parseFloat(properties.getProperty("cameraHeight"));
				isVSync = Boolean.parseBoolean(properties.getProperty("isVSync"));
				isRememberAccount = Boolean.parseBoolean(properties.getProperty("isRememberAccount"));
				generalVolume = Float.parseFloat(properties.getProperty("generalVolume"));
				interfaceVolume = Float.parseFloat(properties.getProperty("interfaceVolume"));
				effectsVolume = Float.parseFloat(properties.getProperty("effectsVolume"));
				techniqueVolume = Float.parseFloat(properties.getProperty("techniqueVolume"));
				environmentVolume = Float.parseFloat(properties.getProperty("environmentVolume"));
				isFullscreen = Boolean.parseBoolean(properties.getProperty("isFullscreen"));
				isFrameLimitation = Boolean.parseBoolean(properties.getProperty("isFrameLimitation"));
				isMute = Boolean.parseBoolean(properties.getProperty("isMute"));
				isMuteWhenHidden = Boolean.parseBoolean(properties.getProperty("isMuteWhenHidden"));
				if (isRememberAccount) {
					login = properties.getProperty("login");
					password = properties.getProperty("password");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Gdx.app.exit();
		}
		language = new Language(LanguageType.UA);
	}
	
	public void save () {
		properties.setProperty("cameraWidth", String.valueOf(cameraWidth));
		properties.setProperty("cameraHeight", String.valueOf(cameraHeight));
		properties.setProperty("isVSync", String.valueOf(isVSync));
		properties.setProperty("isRememberAccount", String.valueOf(isRememberAccount));
		if (isRememberAccount) {
			properties.setProperty("login", login);
			properties.setProperty("password", password);
		}
		properties.setProperty("generalVolume", String.valueOf(generalVolume));
		properties.setProperty("interfaceVolume", String.valueOf(interfaceVolume));
		properties.setProperty("effectsVolume", String.valueOf(effectsVolume));
		properties.setProperty("techniqueVolume", String.valueOf(techniqueVolume));
		properties.setProperty("environmentVolume", String.valueOf(environmentVolume));
		properties.setProperty("isFullscreen", String.valueOf(isFullscreen));
		properties.setProperty("isFrameLimitation", String.valueOf(isFrameLimitation));
		properties.setProperty("isMute", String.valueOf(isMute));
		properties.setProperty("isMuteWhenHidden", String.valueOf(isMuteWhenHidden));
		FileHandle file = Gdx.files.local("config.txt");
		try {
			properties.store(file.write(false), "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setCameraSize (float width, float height) {
		this.cameraWidth = width;
		this.cameraHeight = height;
	}
	
	public void setFullscreenMode (boolean value) {
		isFullscreen = value;
	}
	
	public void setFrameLimitation (boolean isFrameLimitation) {
		this.isFrameLimitation = isFrameLimitation;
	}
	
	public void setVSync (boolean value) {
		isVSync = value;
	}
	
	public void setLogin (String login) {
		this.login = login;
	}
	
	public void setPassword (String password) {
		this.password = password;
	}
	
	public void setRememberAccount (boolean value) {
		isRememberAccount = value;
	}
	
	public void setGeneralVolume (float value) {
		generalVolume = value;
	}
	
	public void setInterfaceVolume (float value) {
		interfaceVolume = value;
	}
	
	public void setEffectsVolume (float value) {
		effectsVolume = value;
	}
	
	public void setTechniqueVolume (float value) {
		techniqueVolume = value;
	}
	
	public void setEnvironmentVolume (float value) {
		environmentVolume = value;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public void setMute (boolean value) {
		isMute = value;
	}
	
	public void setMuteWhenHidden (boolean value) {
		isMuteWhenHidden = value;
	}
	
	public float getCameraWidth () {
		return cameraWidth;
	}
	
	public boolean isFullscreen () {
		return isFullscreen;
	}
	
	public float getCameraHeight () {
		return cameraHeight;
	}
	
	public boolean isVSync () {
		return isVSync;
	}
	
	public boolean isRememberAccount () {
		return isRememberAccount;
	}
	
	public static Settings getInstance () {
		if (settings == null) settings = new Settings();
		return settings;
	}
	
	public String getLogin () {
		return login;
	}
	
	public String getPassword () {
		return password;
	}
	
	public Language getLanguage () {
		return language;
	}
	
	public String getName () {
		return name;
	}
	
	public boolean isFrameLimitation () {
		return isFrameLimitation;
	}
	
	public float getGeneralVolume () {
		return generalVolume;
	}
	
	public float getInterfaceVolume () {
		return interfaceVolume;
	}
	
	public float getEffectsVolume () {
		return effectsVolume;
	}
	
	public float getTechniqueVolume () {
		return techniqueVolume;
	}
	
	public float getEnvironmentVolume () {
		return environmentVolume;
	}
	
	public boolean isMute () {
		return isMute;
	}
	
	public boolean isMuteWhenHidden () {
		return isMuteWhenHidden;
	}
}
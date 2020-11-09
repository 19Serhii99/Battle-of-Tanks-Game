package com.mygdx.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import net.TCP;

public class Server extends ApplicationAdapter {
	private static TCP tcp;
	private static Connection mysql;

	@Override
	public void create() {
		tcp = new TCP(45678);
		try {
			System.out.println("Подключение к MySQL...");
			mysql = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/battle_of_tanks?useSSL=false&serverTimezone=UTC", "root",
					"Rhfcfdxbr1");
			System.out.println("Подключение к MySQL выполнено успешно");
			System.out.println("Запуск слушателей...");
			tcp.setConnection(mysql);
			tcp.start();
		} catch (SQLException e) {
			System.out.println("Ошибка подключения к MySQL...");
		}
		Gdx.app.getGraphics().setContinuousRendering(false);
	}

	public static TCP getTCP() {
		return tcp;
	}

	synchronized public static Connection getConnection() {
		return mysql;
	}

	@Override
	public void render() {

	}

	@Override
	public void dispose() {
		// tcp.dispose();
		// udp.dispose();
	}
}
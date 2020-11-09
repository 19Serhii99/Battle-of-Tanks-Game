package answers;

import java.io.Serializable;

public class ShellAnswer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int idSender;
	private int idRecipient;
	private float x;
	private float y;
	private float nextX;
	private float nextY;
	
	public ShellAnswer (int id, int idSender, int idRecipient, float x, float y, float nextX, float nextY) {
		this.id = id;
		this.idSender = idSender;
		this.x = x;
		this.y = y;
		this.nextX = nextX;
		this.nextY = nextY;
	}
	
	public int getId () {
		return id;
	}
	
	public int getIdSender () {
		return idSender;
	}
	
	public int getIdRecipient () {
		return idRecipient;
	}
	
	public float getX () {
		return x;
	}
	
	public float getY () {
		return y;
	}
	
	public float getNextX () {
		return nextX;
	}
	
	public float getNextY () {
		return nextY;
	}
}
package answers;

import java.io.Serializable;

public class Hit implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int idSender;
	private int idRecipient;
	private int damage;
	private boolean isLeftSide;
	
	public Hit (int idSender, int idRecipient, int damage, boolean isLeftSide) {
		this.idSender = idSender;
		this.idRecipient = idRecipient;
		this.damage = damage;
		this.isLeftSide = isLeftSide;
	}
	
	public int getIdSender () {
		return idSender;
	}
	
	public int getIdRecipient () {
		return idRecipient;
	}
	
	public int getDamage () {
		return damage;
	}
	
	public boolean isLeftSide () {
		return isLeftSide;
	}
}
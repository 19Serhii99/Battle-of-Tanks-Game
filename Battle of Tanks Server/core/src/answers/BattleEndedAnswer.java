package answers;

import java.io.Serializable;

public class BattleEndedAnswer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int exp;
	private int money;
	private boolean isWon;
	
	public BattleEndedAnswer (int exp, int money, boolean isWon) {
		this.exp = exp;
		this.money = money;
		this.isWon = isWon;
	}
	
	public int getExp () {
		return exp;
	}
	
	public int getMoney () {
		return money;
	}
	
	public boolean isWon () {
		return isWon;
	}
}
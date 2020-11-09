package answers;

import java.io.Serializable;

public class MyBalanceAnswer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int exp;
	private int money;
	
	public MyBalanceAnswer (int exp, int money) {
		this.exp = exp;
		this.money = money;
	}
	
	public int getExp () {
		return exp;
	}
	
	public int getMoney () {
		return money;
	}
}
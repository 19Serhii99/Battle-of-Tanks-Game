package answers;

import java.io.Serializable;

public class PlayBattleAnswer extends Verification implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public PlayBattleAnswer (boolean value) {
		super(value);
	}
}
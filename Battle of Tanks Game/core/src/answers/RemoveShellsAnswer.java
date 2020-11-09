package answers;

import java.io.Serializable;
import java.util.ArrayList;

public class RemoveShellsAnswer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList <Integer> shells;
	
	public RemoveShellsAnswer () {
		shells = new ArrayList <Integer>(1);
	}
	
	public ArrayList <Integer> getShells () {
		return shells;
	}
}
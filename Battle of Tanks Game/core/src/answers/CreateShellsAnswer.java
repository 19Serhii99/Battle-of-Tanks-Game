package answers;

import java.io.Serializable;
import java.util.ArrayList;

public class CreateShellsAnswer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList <ShellAnswer> shells;
	
	public CreateShellsAnswer () {
		shells = new ArrayList <ShellAnswer>();
	}
	
	public ArrayList <ShellAnswer> getShells () {
		return shells;
	}
}
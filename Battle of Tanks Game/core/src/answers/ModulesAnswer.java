package answers;

import java.io.Serializable;
import java.util.ArrayList;

import additional.Corps;

public class ModulesAnswer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList <Corps> corpses;
	
	public ModulesAnswer () {
		corpses = new ArrayList <Corps>(1);
	}
	
	public ArrayList <Corps> getCorpses () {
		return corpses;
	}
}
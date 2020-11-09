package answers;

import java.io.Serializable;

public class GetDataPlayerAnswer implements Serializable {
	private static final long serialVersionUID = 1L;
	private int experience;
	private int money;
	private int battles;
	private int averageDamage;
	private int kills;
	private int deaths;
	private float survival;
	private float percentWins;
	
	public GetDataPlayerAnswer (int experience, int money, int battles, int averageDamage, int kills, int deaths, float survival, float percentWins) {
		this.experience = experience;
		this.money = money;
		this.battles = battles;
		this.averageDamage = averageDamage;
		this.kills = kills;
		this.deaths = deaths;
		this.survival = survival;
		this.percentWins = percentWins;
	}
	
	public int getExperience () {
		return experience;
	}
	
	public int getMoney () {
		return money;
	}
	
	public int getBattles () {
		return battles;
	}
	
	public int getAverageDamage () {
		return averageDamage;
	}
	
	public int getKills () {
		return kills;
	}
	
	public int getDeaths () {
		return deaths;
	}
	
	public float getSurvival () {
		return survival;
	}
	
	public float getPercentWins () {
		return percentWins;
	}
}
package technique;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import answers.CreateShellsAnswer;
import answers.ShellAnswer;
import map.Chunk;

public class Shooting {
	private ShootingStyle shootingStyle;
	private Technique technique;
	private Tower tower;
	private LinkedList <Shell> shells;
	
	private long timeStartRecharge;
	private int index;
	private int currentShellsTotal;
	private int idSender;
	private int currentCount;
	private boolean isRecharge = true;
	
	public Shooting (Technique technique, int idSender) {
		this.technique = technique;
		this.tower = technique.getTower();
		this.idSender = idSender;
		this.currentShellsTotal = tower.getShellsTotal();
		
		this.timeStartRecharge = System.currentTimeMillis();
	}
	
	public void setShootingStyle (ShootingStyle shootingStyle) {
		this.shootingStyle = shootingStyle;
	}
	
	public void setShellsList (LinkedList <Shell> shells) {
		this.shells = shells;
	}
	
	public void createShell (float nextX, float nextY, Aim aim, CreateShellsAnswer createShellsAnswer, ArrayList <Chunk> chunks) {
		if (isRecharge) {
			if (tower.getGun().isCassette()) {
				if (currentCount == 0) {
					if ((float)(System.currentTimeMillis() - timeStartRecharge) / 1000 >= tower.getTimeRecharge()) {
						isRecharge = false;
						if (currentShellsTotal - tower.getGun().getCassette().getMaxCount() < 0) {
							currentCount = currentShellsTotal;
							currentShellsTotal = 0;
						} else {
							currentCount = tower.getGun().getCassette().getMaxCount();
							currentShellsTotal -= tower.getGun().getCassette().getMaxCount();
						}
					}
				} else {
					if ((float)(System.currentTimeMillis() - timeStartRecharge) / 1000 >= tower.getGun().getCassette().getTimeRecharge()) {
						isRecharge = false;
					}
				}
			} else {
				if ((float)(System.currentTimeMillis() - timeStartRecharge) / 1000 >= tower.getTimeRecharge()) {
					isRecharge = false;
					currentShellsTotal--;
				}
			}
		} else {
			if (tower.getGun().isCassette()) {
				if (shootingStyle == ShootingStyle.IN_TURN) {
					creatingShell(technique.getGunPoints().get(index).x, technique.getGunPoints().get(index).y, nextX, nextY, createShellsAnswer, chunks);
					index++;
					currentCount--;
					if (index > technique.getGunPoints().size() - 1) index = 0;
				} else if (shootingStyle == ShootingStyle.TOGETHER) {
					for (Vector2 point : technique.getGunPoints()) {
						creatingShell(point.x, point.y, nextX, nextY, createShellsAnswer, chunks);
						currentCount--;
						if (currentCount == 0) break;
					}
				} else if (shootingStyle == ShootingStyle.RANDOM) {
					index = MathUtils.random(0, technique.getGunPoints().size() - 1);
					creatingShell(technique.getGunPoints().get(index).x, technique.getGunPoints().get(index).y, nextX, nextY, createShellsAnswer, chunks);
					currentCount--;
				}
				isRecharge = true;
				timeStartRecharge = System.currentTimeMillis();
			} else {
				if (currentShellsTotal > 0) {
					creatingShell(technique.getGunPoints().get(0).x, technique.getGunPoints().get(0).y, nextX, nextY, createShellsAnswer, chunks);
					aim.reset();
					isRecharge = true;
					timeStartRecharge = System.currentTimeMillis();
				}
			}
		}
	}
	
	private void creatingShell (float x, float y, float nextX, float nextY, CreateShellsAnswer createShellsAnswer, ArrayList <Chunk> chunks) {
		int tempId = shells.size() == 0 ? 1 : shells.get(shells.size() - 1).getId() + 1;
		Shell shell = new Shell(tempId, idSender);
		shell.setSpeed(tower.getGun().getShellSpeed());
		shell.setSize(tower.getGun().getShellWidth(), tower.getGun().getShellHeight());
		shell.setOriginCenter();
		shell.nextPoint(x, y, nextX, nextY, chunks);
		shells.add(shell);
		ShellAnswer answer = new ShellAnswer(shell.getId(), idSender, 0, x, y, nextX, nextY);
		createShellsAnswer.getShells().add(answer);
	}
	
	public LinkedList <Shell> getShells () {
		return shells;
	}
	
	public ShootingStyle getShootingStyle() {
		return shootingStyle;
	}
}
package technique;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

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
	private boolean isRecharge;
	
	public Shooting (Technique technique, int idSender) {
		this.technique = technique;
		this.tower = technique.getTower();
		this.idSender = idSender;
		this.currentShellsTotal = tower.getShellsTotal();
	}
	
	public void setShootingStyle (ShootingStyle shootingStyle) {
		this.shootingStyle = shootingStyle;
	}
	
	public void setShellsList (LinkedList <Shell> shells) {
		this.shells = shells;
	}
	
	public void createShell (float nextX, float nextY, Aim aim, Texture texture) {
		if (isRecharge) {
			if (tower.getGun().isCassette()) {
				if (currentCount == 0) {
					if ((float)(System.currentTimeMillis() - timeStartRecharge) / 1000 >= tower.getTimeRecharge()) {
						isRecharge = false;
						if (tower.getShellsTotal() - tower.getGun().getCassette().getMaxCount() < 0) {
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
					creatingShell(technique.getGunPoints().get(index).x, technique.getGunPoints().get(index).y, nextX, nextY, texture);
					index++;
					currentCount--;
					if (index > technique.getGunPoints().size - 1) index = 0;
				} else if (shootingStyle == ShootingStyle.TOGETHER) {
					for (Vector2 point : technique.getGunPoints()) {
						creatingShell(point.x, point.y, nextX, nextY, texture);
						currentCount--;
						if (currentCount == 0) break;
					}
				} else if (shootingStyle == ShootingStyle.RANDOM) {
					index = MathUtils.random(0, technique.getGunPoints().size - 1);
					creatingShell(technique.getGunPoints().get(index).x, technique.getGunPoints().get(index).y, nextX, nextY, texture);
					currentCount--;
				}
				tower.getGun().getShotSound().play();
				isRecharge = true;
				timeStartRecharge = System.currentTimeMillis();
			} else {
				if (currentShellsTotal > 0) {
					creatingShell(technique.getGunPoints().get(0).x, technique.getGunPoints().get(0).y, nextX, nextY, texture);
					long id = tower.getGun().getShotSound().play();
					tower.getGun().getShotSound().setPitch(id, MathUtils.random(0.1f, 5.0f));
					aim.reset();
					isRecharge = true;
					timeStartRecharge = System.currentTimeMillis();
				}
			}
		}
	}
	
	private void creatingShell (float x, float y, float nextX, float nextY, Texture texture) {
		Shell shell = new Shell(tower.getGun().getShellTexture(), texture, 1, idSender);
		shell.setSpeed(tower.getGun().getShellSpeed());
		shell.setSize(tower.getGun().getShellWidth(), tower.getGun().getShellHeight());
		shell.setOriginCenter();
		shell.nextPoint(x, y, nextX, nextY);
		shells.add(shell);
	}
	
	public LinkedList <Shell> getShells () {
		return shells;
	}
	
	public ShootingStyle getShootingStyle() {
		return shootingStyle;
	}
}
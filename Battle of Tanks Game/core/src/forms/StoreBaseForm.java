package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Loading;
import com.mygdx.game.Objects;

import answers.ModulesAnswer;
import answers.MyBalanceAnswer;
import answers.OperationAnswer;
import gui.Label;
import net.Client;
import technique.Corps;
import technique.Gun;
import technique.ShootingStyle;
import technique.TechniqueType;
import technique.Tower;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public abstract class StoreBaseForm implements Disposable {
	protected Sprite background;
	protected Sprite corpsBackground;
	protected Sprite towerBackground;
	protected Sprite separator;
	protected Sprite backgroundRight;
	protected Sprite borderRight;
	
	protected Label caption;
	
	protected Label corpsLabel;
	protected Label corpsNameLabel;
	protected Label corpsTypeLabel;
	protected Label corpsExperienceLabel;
	protected Label corpsPriceLabel;
	protected Label corpsHealthLabel;
	protected Label corpsMaxSpeedLabel;
	protected Label corpsRotationLabel;
	protected Label corpsLevelLabel;
	
	protected Label towerLabel;
	protected Label towerNameLabel;
	protected Label towerExperienceLabel;
	protected Label towerPriceLabel;
	protected Label towerRotationLabel;
	protected Label towerReductionLabel;
	protected Label towerDamageLabel;
	protected Label towerRechargeLabel;
	protected Label towerShellsLabel;
	protected Label towerLevelLabel;
	protected Label towerShellsCassetteLabel;
	protected Label towerShellRechargeLabel;
	
	protected Label corpsName;
	protected Label corpsType;
	protected Label corpsExperience;
	protected Label corpsPrice;
	protected Label corpsHealth;
	protected Label corpsMaxSpeed;
	protected Label corpsRotation;
	protected Label corpsLevel;
	
	protected Label towerName;
	protected Label towerExperience;
	protected Label towerPrice;
	protected Label towerRotation;
	protected Label towerReduction;
	protected Label towerDamage;
	protected Label towerRecharge;
	protected Label towerShells;
	protected Label towerLevel;
	protected Label towerShellCassette;
	protected Label towerShellRecharge;
	
	protected Label balanceLabel;
	protected Label experience;
	protected Label money;
	
	protected Operation operation;
	protected Corps corps;
	protected Tower tower;
	
	protected boolean isHide = false;
	protected float alpha = 0.0f;
	protected int expCorpsValue;
	protected int moneyCorpsValue;
	protected int expTowerValue;
	protected int moneyTowerValue;
	
	protected boolean isLoading;
	protected boolean isRestart;
	protected Loading loading;
	
	public StoreBaseForm (String captionText) {
		initBackground();
		initCorpsLabels();
		initTowerLabels();
		initBalance();
		
		corpsBackground = new Sprite();
		towerBackground = new Sprite();
		
		BitmapFont captionFont = Font.getInstance().generateBitmapFont(Color.WHITE, 30);
		captionFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		caption = new Label(captionText, captionFont);
		caption.setPosition(background.getX() + background.getWidth() / 2 - caption.getWidth() / 2, background.getVertices()[Batch.Y2] - 30);
		
		separator = new Sprite(TextureCreator.createTexture(Color.GRAY));
		separator.setSize(400, 2);
		
		Client.getInstance().sendQuery("balance");
	}
	
	private void initBackground () {
		Texture texture = new Texture(Gdx.files.internal("images/primary forms/signInForm.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Sprite(texture);
		background.setSize(1200, 730);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2, CameraController.getInstance().getHeight() / 2 - 250);
		
		backgroundRight = new Sprite(TextureCreator.createTexture(Color.BLACK));
		backgroundRight.setAlpha(0.5f);
		backgroundRight.setSize(250, 150);
		backgroundRight.setPosition(CameraController.getInstance().getWidth() - backgroundRight.getWidth() - 50, background.getVertices()[Batch.Y2] - backgroundRight.getHeight() - 300);
		
		borderRight = new Sprite(TextureCreator.createTexture(Color.ORANGE));
	}
	
	private void initCorpsLabels () {
		BitmapFont font = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		corpsLabel = new Label("Корпус", font);
		corpsNameLabel = new Label("Найменування", font);
		corpsTypeLabel = new Label("Тип техніки", font);
		corpsExperienceLabel = new Label("Вартість дослідження", font);
		corpsPriceLabel = new Label("Вартість придбання", font);
		corpsHealthLabel = new Label("Міцність", font);
		corpsMaxSpeedLabel = new Label("Максимальна швидкість", font);
		corpsRotationLabel = new Label("Швидкість повороту", font);
		corpsLevelLabel = new Label("Рівень", font);
		
		corpsLabel.setPosition(background.getX() + 170, background.getVertices()[Batch.Y2] - 13);
		corpsNameLabel.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 50);
		corpsTypeLabel.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 90);
		corpsExperienceLabel.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 130);
		corpsPriceLabel.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 170);
		corpsHealthLabel.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 210);
		corpsMaxSpeedLabel.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 250);
		corpsRotationLabel.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 290);
		corpsLevelLabel.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 330);
	}
	
	private void initTowerLabels () {
		BitmapFont font = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		towerLabel = new Label("Башта", font);
		towerNameLabel = new Label("Найменування", font);
		towerExperienceLabel = new Label("Вартість дослідження", font);
		towerPriceLabel = new Label("Вартість придбання", font);
		towerRotationLabel = new Label("Швидкість повороту", font);
		towerReductionLabel = new Label("Час зведення", font);
		towerDamageLabel = new Label("Середній урон", font);
		towerRechargeLabel = new Label("Час перезарядки", font);
		towerShellsLabel = new Label("Усього снарядів", font);
		towerLevelLabel = new Label("Рівень", font);
		towerShellsCassetteLabel = new Label("Снарядів за заряд", font);
		towerShellRechargeLabel = new Label("Час перезарядки снаряду", font);
		
		towerLabel.setPosition(background.getX() + background.getWidth() - 250, background.getVertices()[Batch.Y2] - 13);
		towerNameLabel.setPosition(background.getX() + background.getWidth() - 420, background.getVertices()[Batch.Y2] - 50);
		towerExperienceLabel.setPosition(background.getX() + background.getWidth() - 420, background.getVertices()[Batch.Y2] - 90);
		towerPriceLabel.setPosition(background.getX() + background.getWidth() - 420, background.getVertices()[Batch.Y2] - 130);
		towerRotationLabel.setPosition(background.getX() + background.getWidth() - 420, background.getVertices()[Batch.Y2] - 170);
		towerReductionLabel.setPosition(background.getX() + background.getWidth() - 420, background.getVertices()[Batch.Y2] - 210);
		towerDamageLabel.setPosition(background.getX() + background.getWidth() - 420, background.getVertices()[Batch.Y2] - 250);
		towerRechargeLabel.setPosition(background.getX() + background.getWidth() - 420, background.getVertices()[Batch.Y2] - 290);
		towerShellsLabel.setPosition(background.getX() + background.getWidth() - 420, background.getVertices()[Batch.Y2] - 330);
		towerLevelLabel.setPosition(background.getX() + background.getWidth() - 420, background.getVertices()[Batch.Y2] - 370);
		towerShellsCassetteLabel.setPosition(background.getX() + background.getWidth() - 420, background.getVertices()[Batch.Y2] - 410);
		towerShellRechargeLabel.setPosition(background.getX() + background.getWidth() - 420, background.getVertices()[Batch.Y2] - 450);
	}
	
	private void initBalance () {
		balanceLabel = new Label("На балансі", Font.getInstance().generateBitmapFont(Color.WHITE, 25));
		balanceLabel.setPosition(backgroundRight.getX() + backgroundRight.getWidth() / 2 - balanceLabel.getWidth() / 2, backgroundRight.getVertices()[Batch.Y2] - 5);
	}
	
	public void initCorpsValues (Corps corps) {
		if (corpsName != null) corpsName.getFont().dispose();
		
		BitmapFont font = Font.getInstance().generateBitmapFont(new Color(235f / 255f, 114f / 255f, 114f / 255f, 1f), 20);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		corpsName = new Label(corps.getName(), font);
		
		String type = "";
		if (corps.getTechniqueType() == TechniqueType.TANK) {
			type = "Танк";
		} else if (corps.getTechniqueType() == TechniqueType.ARTY) {
			type = "САУ";
		} else if (corps.getTechniqueType() == TechniqueType.REACTIVE_SYSTEM) {
			type = "РСЗВ";
		} else if (corps.getTechniqueType() == TechniqueType.FLAMETHROWER_SYSTEM) {
			type = "ТВС";
		}
		
		corpsType = new Label(type, font);
		corpsExperience = new Label(corps.getExperience() + " од.", font);
		corpsPrice = new Label(corps.getMoney() + " золота", font);
		corpsHealth = new Label(corps.getMaxHealth() + " од.", font);
		corpsMaxSpeed = new Label(String.format("%(.2f", corps.getMaxSpeed()) + " од.", font);
		corpsRotation = new Label(String.format("%(.2f", corps.getSpeedRotation()) + " град.", font);
		corpsLevel = new Label(corps.getLevel() + "", font);
		
		corpsName.setPosition(background.getX() + 280, background.getVertices()[Batch.Y2] - 50);
		corpsType.setPosition(background.getX() + 280, background.getVertices()[Batch.Y2] - 90);
		corpsExperience.setPosition(background.getX() + 280, background.getVertices()[Batch.Y2] - 130);
		corpsPrice.setPosition(background.getX() + 280, background.getVertices()[Batch.Y2] - 170);
		corpsHealth.setPosition(background.getX() + 280, background.getVertices()[Batch.Y2] - 210);
		corpsMaxSpeed.setPosition(background.getX() + 280, background.getVertices()[Batch.Y2] - 250);
		corpsRotation.setPosition(background.getX() + 280, background.getVertices()[Batch.Y2] - 290);
		corpsLevel.setPosition(background.getX() + 280, background.getVertices()[Batch.Y2] - 330);
		
		expCorpsValue = corps.getExperience();
		moneyCorpsValue = corps.getMoney();
		
		this.corps = corps;
	}
	
	public void initTowerValues (Tower tower) {
		if (towerName != null) towerName.getFont().dispose();
		
		BitmapFont font = Font.getInstance().generateBitmapFont(new Color(235f / 255f, 114f / 255f, 114f / 255f, 1f), 20);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		towerName = new Label(tower.getName(), font);
		towerExperience = new Label(tower.getExperience() + "", font);
		towerPrice = new Label(tower.getMoney() + "", font);
		towerRotation = new Label(tower.getSpeedRotation() + "", font);
		towerReduction = new Label(tower.getTimeReduction() + "", font);
		towerDamage = new Label((tower.getMinDamage() + tower.getMaxDamage()) / 2 + "", font);
		towerRecharge = new Label(tower.getTimeRecharge() + "", font);
		towerShells = new Label(tower.getShellsTotal() + "", font);
		towerLevel = new Label(tower.getLevel() + "", font);
		
		if (tower.getGun().isCassette()) {
			towerShellCassette = new Label(tower.getGun().getCassette().getMaxCount() + "", font);
			towerShellRecharge = new Label(tower.getGun().getCassette().getTimeRecharge() + "", font);
		} else {
			if (towerShellCassette != null) towerShellCassette = null;
			if (towerShellRecharge != null) towerShellRecharge = null;
		}
		
		towerName.setPosition(background.getVertices()[Batch.X3] - 170, background.getVertices()[Batch.Y2] - 50);
		towerExperience.setPosition(background.getVertices()[Batch.X3] - 170, background.getVertices()[Batch.Y2] - 90);
		towerPrice.setPosition(background.getVertices()[Batch.X3] - 170, background.getVertices()[Batch.Y2] - 130);
		towerRotation.setPosition(background.getVertices()[Batch.X3] - 170, background.getVertices()[Batch.Y2] - 170);
		towerReduction.setPosition(background.getVertices()[Batch.X3] - 170, background.getVertices()[Batch.Y2] - 210);
		towerDamage.setPosition(background.getVertices()[Batch.X3] - 170, background.getVertices()[Batch.Y2] - 250);
		towerRecharge.setPosition(background.getVertices()[Batch.X3] - 170, background.getVertices()[Batch.Y2] - 290);
		towerShells.setPosition(background.getVertices()[Batch.X3] - 170, background.getVertices()[Batch.Y2] - 330);
		towerLevel.setPosition(background.getVertices()[Batch.X3] - 170, background.getVertices()[Batch.Y2] - 370);
		if (tower.getGun().isCassette()) {
			towerShellCassette.setPosition(background.getVertices()[Batch.X3] - 170, background.getVertices()[Batch.Y2] - 410);
			towerShellRecharge.setPosition(background.getVertices()[Batch.X3] - 170, background.getVertices()[Batch.Y2] - 450);
		}
		
		expTowerValue = tower.getExperience();
		moneyTowerValue = tower.getMoney();
		
		this.tower = tower;
	}
	
	public void show (SpriteBatch batch) {
		if (isHide) {
			alpha -= Gdx.graphics.getDeltaTime() * 5;
			if (alpha < 0.0f) alpha = 0.0f;
		} else {
			alpha += Gdx.graphics.getDeltaTime() * 2;
			if (alpha > 1.0f) alpha = 1.0f;
		}
		
		background.setAlpha(alpha);
		
		caption.setAlphas(alpha);
		
		corpsLabel.setAlphas(alpha);
		corpsNameLabel.setAlphas(alpha);
		corpsTypeLabel.setAlphas(alpha);
		corpsExperienceLabel.setAlphas(alpha);
		corpsPriceLabel.setAlphas(alpha);
		corpsHealthLabel.setAlphas(alpha);
		corpsMaxSpeedLabel.setAlphas(alpha);
		corpsRotationLabel.setAlphas(alpha);
		corpsLevelLabel.setAlphas(alpha);
		
		towerLabel.setAlphas(alpha);
		towerNameLabel.setAlphas(alpha);
		towerExperienceLabel.setAlphas(alpha);
		towerPriceLabel.setAlphas(alpha);
		towerRotationLabel.setAlphas(alpha);
		towerReductionLabel.setAlphas(alpha);
		towerDamageLabel.setAlphas(alpha);
		towerRechargeLabel.setAlphas(alpha);
		towerShellsLabel.setAlphas(alpha);
		towerLevelLabel.setAlphas(alpha);
		towerShellsCassetteLabel.setAlphas(alpha);
		towerShellRechargeLabel.setAlphas(alpha);
		
		corpsName.setAlphas(alpha);
		corpsType.setAlphas(alpha);
		corpsExperience.setAlphas(alpha);
		corpsPrice.setAlphas(alpha);
		corpsHealth.setAlphas(alpha);
		corpsMaxSpeed.setAlphas(alpha);
		corpsRotation.setAlphas(alpha);
		corpsLevel.setAlphas(alpha);
		
		towerName.setAlphas(alpha);
		towerExperience.setAlphas(alpha);
		towerPrice.setAlphas(alpha);
		towerRotation.setAlphas(alpha);
		towerReduction.setAlphas(alpha);
		towerDamage.setAlphas(alpha);
		towerRecharge.setAlphas(alpha);
		towerShells.setAlphas(alpha);
		towerLevel.setAlphas(alpha);
		
		if (towerShellCassette != null) {
			towerShellCassette.setAlphas(alpha);
			towerShellRecharge.setAlphas(alpha);
		}
		
		background.draw(batch);
		
		caption.draw(batch);
		
		corpsLabel.draw(batch);
		corpsNameLabel.draw(batch);
		corpsTypeLabel.draw(batch);
		corpsExperienceLabel.draw(batch);
		corpsPriceLabel.draw(batch);
		corpsHealthLabel.draw(batch);
		corpsMaxSpeedLabel.draw(batch);
		corpsRotationLabel.draw(batch);
		corpsLevelLabel.draw(batch);
		
		separator.setPosition(corpsNameLabel.getX() - 5, corpsNameLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(corpsTypeLabel.getX() - 5, corpsTypeLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(corpsExperienceLabel.getX() - 5, corpsExperienceLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(corpsPriceLabel.getX() - 5, corpsPriceLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(corpsHealthLabel.getX() - 5, corpsHealthLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(corpsMaxSpeedLabel.getX() - 5, corpsMaxSpeedLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(corpsRotationLabel.getX() - 5, corpsRotationLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(corpsLevelLabel.getX() - 5, corpsLevelLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(corpsLevelLabel.getX() - 5, corpsLevelLabel.getY() - corpsLevelLabel.getHeight() - 10);
		separator.draw(batch);
		
		towerLabel.draw(batch);
		towerNameLabel.draw(batch);
		towerExperienceLabel.draw(batch);
		towerPriceLabel.draw(batch);
		towerRotationLabel.draw(batch);
		towerReductionLabel.draw(batch);
		towerDamageLabel.draw(batch);
		towerRechargeLabel.draw(batch);
		towerShellsLabel.draw(batch);
		towerLevelLabel.draw(batch);
		towerShellsCassetteLabel.draw(batch);
		towerShellRechargeLabel.draw(batch);
		
		separator.setPosition(towerNameLabel.getX() - 5, towerNameLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(towerExperienceLabel.getX() - 5, towerExperienceLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(towerPriceLabel.getX() - 5, towerPriceLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(towerRotationLabel.getX() - 5, towerRotationLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(towerReductionLabel.getX() - 5, towerReductionLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(towerDamageLabel.getX() - 5, towerDamageLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(towerRechargeLabel.getX() - 5, towerRechargeLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(towerShellsLabel.getX() - 5, towerShellsLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(towerLevelLabel.getX() - 5, towerLevelLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(towerShellsCassetteLabel.getX() - 5, towerShellsCassetteLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(towerShellRechargeLabel.getX() - 5, towerShellRechargeLabel.getY() + 10);
		separator.draw(batch);
		separator.setPosition(towerShellRechargeLabel.getX() - 5, towerShellRechargeLabel.getY() - towerShellRechargeLabel.getHeight() - 10);
		separator.draw(batch);
			
		corpsName.draw(batch);
		corpsType.draw(batch);
		corpsExperience.draw(batch);
		corpsPrice.draw(batch);
		corpsHealth.draw(batch);
		corpsMaxSpeed.draw(batch);
		corpsRotation.draw(batch);
		corpsLevel.draw(batch);
		
		towerName.draw(batch);
		towerExperience.draw(batch);
		towerPrice.draw(batch);
		towerRotation.draw(batch);
		towerReduction.draw(batch);
		towerDamage.draw(batch);
		towerRecharge.draw(batch);
		towerShells.draw(batch);
		towerLevel.draw(batch);
		if (towerShellCassette != null) {
			towerShellCassette.draw(batch);
			towerShellRecharge.draw(batch);
		}
		
		corpsBackground.draw(batch);
		towerBackground.draw(batch);
		
		//backgroundRight.setAlpha(alpha);
		backgroundRight.draw(batch);
		
		borderRight.setAlpha(alpha);
		
		borderRight.setSize(backgroundRight.getWidth(), 2);
		borderRight.setPosition(backgroundRight.getX(), backgroundRight.getY());
		borderRight.draw(batch);
		
		borderRight.setSize(backgroundRight.getWidth(), 2);
		borderRight.setPosition(backgroundRight.getX(), backgroundRight.getY() + backgroundRight.getHeight() - borderRight.getHeight());
		borderRight.draw(batch);
		
		borderRight.setSize(2, backgroundRight.getHeight());
		borderRight.setPosition(backgroundRight.getX(), backgroundRight.getY());
		borderRight.draw(batch);
		
		borderRight.setSize(2, backgroundRight.getHeight());
		borderRight.setPosition(backgroundRight.getX() + backgroundRight.getWidth() - borderRight.getWidth(), backgroundRight.getY());
		borderRight.draw(batch);
		
		balanceLabel.setAlphas(alpha);
		balanceLabel.draw(batch);
		
		if (experience != null) {
			experience.setAlphas(alpha);
			experience.draw(batch);
		}
		if (money != null) {
			money.setAlphas(alpha);
			money.draw(batch);
		}
		
		if (Client.getInstance().getServerListener().getObjects().size() > 0) {
			final Object object = Client.getInstance().getServerListener().getObjects().pop();
			if (object.getClass() == MyBalanceAnswer.class) {
				MyBalanceAnswer answer = (MyBalanceAnswer) object;
				experience = new Label(answer.getExp() + " досвіду", Font.getInstance().generateBitmapFont(Color.GREEN, 20));
				experience.setPosition(backgroundRight.getX() + backgroundRight.getWidth() / 2 - experience.getWidth() / 2, balanceLabel.getY() - 50);
				money = new Label(answer.getMoney() + " золота", Font.getInstance().generateBitmapFont(Color.GOLDENROD, 20));
				money.setPosition(backgroundRight.getX() + backgroundRight.getWidth() / 2 - money.getWidth() / 2, experience.getY() - 30);
			} else if (object.getClass() == OperationAnswer.class) {
				OperationAnswer answer = (OperationAnswer) object;
				if (answer.getValue()) {
					Client.getInstance().sendQuery("modules");
				}
			} else if (object.getClass() == ModulesAnswer.class) {
				for (Corps corps : Objects.getInstance().getCorpses()) {
					corps.dispose();
					for (Tower tower : corps.getTowers()) {
						tower.dispose();
					}
				}
				Objects.getInstance().getCorpses().clear();
				ModulesAnswer modulesAnswer = (ModulesAnswer) object;
				Array <technique.Corps> corpses = new Array <technique.Corps>(modulesAnswer.getCorpses().size());
				for (int i = 0; i < modulesAnswer.getCorpses().size(); i++) {
					technique.Corps corps = new technique.Corps(modulesAnswer.getCorpses().get(i).getId());
					corps.setPosition(modulesAnswer.getCorpses().get(i).getPosition());
					corps.setLevel(modulesAnswer.getCorpses().get(i).getLevel());
					corps.setExperience(modulesAnswer.getCorpses().get(i).getExperience());
					corps.setMoney(modulesAnswer.getCorpses().get(i).getMoney());
					corps.setSpeedRotation(modulesAnswer.getCorpses().get(i).getSpeedRotation());
					corps.setWidth(modulesAnswer.getCorpses().get(i).getWidth());
					corps.setHeight(modulesAnswer.getCorpses().get(i).getHeight());
					corps.setAvailable(modulesAnswer.getCorpses().get(i).isAvailable());
					corps.setExplored(modulesAnswer.getCorpses().get(i).isExplored());
					corps.setBought(modulesAnswer.getCorpses().get(i).isBought());
					corps.setName(modulesAnswer.getCorpses().get(i).getName());
					if (modulesAnswer.getCorpses().get(i).getTechniqueType().equals("tank")) {
						corps.setTechniqueType(TechniqueType.TANK);
					} else if (modulesAnswer.getCorpses().get(i).getTechniqueType().equals("arty")) {
						corps.setTechniqueType(TechniqueType.ARTY);
					} else if (modulesAnswer.getCorpses().get(i).getTechniqueType().equals("reactive_system")) {
						corps.setTechniqueType(TechniqueType.REACTIVE_SYSTEM);
					} else if (modulesAnswer.getCorpses().get(i).getTechniqueType().equals("flamethrower_system")) {
						corps.setTechniqueType(TechniqueType.FLAMETHROWER_SYSTEM);
					}
					corps.setMaxHealth(modulesAnswer.getCorpses().get(i).getMaxHealth());
					corps.setMaxSpeed(modulesAnswer.getCorpses().get(i).getMaxSpeed());
					corps.setAcceleration(modulesAnswer.getCorpses().get(i).getAcceleration());
					for (int j = 0; j < modulesAnswer.getCorpses().get(i).getTowers().size(); j++) {
						technique.Tower tower = new technique.Tower(modulesAnswer.getCorpses().get(i).getTowers().get(j).getId());
						tower.setPosition(modulesAnswer.getCorpses().get(i).getTowers().get(j).getPosition());
						tower.setLevel(modulesAnswer.getCorpses().get(i).getTowers().get(j).getLevel());
						tower.setExperience(modulesAnswer.getCorpses().get(i).getTowers().get(j).getExperience());
						tower.setMoney(modulesAnswer.getCorpses().get(i).getTowers().get(j).getMoney());
						tower.setSpeedRotation(modulesAnswer.getCorpses().get(i).getTowers().get(j).getSpeedRotation());
						tower.setWidth(modulesAnswer.getCorpses().get(i).getTowers().get(j).getWidth());
						tower.setHeight(modulesAnswer.getCorpses().get(i).getTowers().get(j).getHeight());
						tower.setAvailable(modulesAnswer.getCorpses().get(i).getTowers().get(j).isAvailable());
						tower.setExplored(modulesAnswer.getCorpses().get(i).getTowers().get(j).isExplored());
						tower.setBought(modulesAnswer.getCorpses().get(i).getTowers().get(j).isBought());
						tower.setName(modulesAnswer.getCorpses().get(i).getTowers().get(j).getName());
						tower.setTimeReduction(modulesAnswer.getCorpses().get(i).getTowers().get(j).getTimeReduction());
						tower.setMinDamage(modulesAnswer.getCorpses().get(i).getTowers().get(j).getMinDamage());
						tower.setMaxDamage(modulesAnswer.getCorpses().get(i).getTowers().get(j).getMaxDamage());
						tower.setMinRadius(modulesAnswer.getCorpses().get(i).getTowers().get(j).getMinRadius());
						tower.setMaxRadius(modulesAnswer.getCorpses().get(i).getTowers().get(j).getMaxRadius());
						tower.setRotationLeft(modulesAnswer.getCorpses().get(i).getTowers().get(j).getRotationLeft());
						tower.setRotationRight(modulesAnswer.getCorpses().get(i).getTowers().get(j).getRotationRight());
						tower.setTimeRecharge(modulesAnswer.getCorpses().get(i).getTowers().get(j).getTimeRecharge());
						tower.setX(modulesAnswer.getCorpses().get(i).getTowers().get(j).getX());
						tower.setY(modulesAnswer.getCorpses().get(i).getTowers().get(j).getY());
						tower.setOriginX(modulesAnswer.getCorpses().get(i).getTowers().get(j).getOriginX());
						tower.setOriginY(modulesAnswer.getCorpses().get(i).getTowers().get(j).getOriginY());
						tower.setShellsTotal(modulesAnswer.getCorpses().get(i).getTowers().get(j).getShellsTotal());
						Gun gun = new Gun();
						gun.setShellSpeed(modulesAnswer.getCorpses().get(i).getTowers().get(j).getShellSpeed());
						if (modulesAnswer.getCorpses().get(i).getTowers().get(j).getShootingStyle() != null) {
							ShootingStyle shootingStyle = null;
							if (modulesAnswer.getCorpses().get(i).getTowers().get(j).getShootingStyle().equals("in_turn")) {
								shootingStyle = ShootingStyle.IN_TURN;
							} else if (modulesAnswer.getCorpses().get(i).getTowers().get(j).getShootingStyle().equals("together")) {
								shootingStyle = ShootingStyle.TOGETHER;
							} else if (modulesAnswer.getCorpses().get(i).getTowers().get(j).getShootingStyle().equals("random")) {
								shootingStyle = ShootingStyle.RANDOM;
							}
							gun.setCassette(modulesAnswer.getCorpses().get(i).getTowers().get(j).getShellsCassette(),
									modulesAnswer.getCorpses().get(i).getTowers().get(j).getRechargeShell(), shootingStyle);
						}
						gun.setShellWidth(modulesAnswer.getCorpses().get(i).getTowers().get(j).getShellWidth());
						gun.setShellHeight(modulesAnswer.getCorpses().get(i).getTowers().get(j).getShellHeight());
						tower.setGun(gun);
						corps.getTowers().add(tower);
					}
					corpses.add(corps);
				}
				Objects.getInstance().setCorpses(corpses);
				isLoading = false;
				isRestart = true;
			}
		}
	}
	
	public Sprite getBackground () {
		return background;
	}
	
	public Sprite getCorpsBackground () {
		return corpsBackground;
	}
	
	public Sprite getTowerBackground () {
		return towerBackground;
	}
	
	public boolean isRestart () {
		return isRestart;
	}
	
	@Override
	public void dispose () {
		caption.getFont().dispose();
		background.getTexture().dispose();
		separator.getTexture().dispose();
		towerLabel.getFont().dispose();
		corpsLabel.getFont().dispose();
		corpsName.getFont().dispose();
		towerName.getFont().dispose();
		for (Corps corps : Objects.getInstance().getCorpses()) {
			for (Tower tower : corps.getTowers()) {
				if (tower.getWholeTexture() != null) {
					tower.getWholeTexture().dispose();
					tower.setWholeTexture(null);
				}
			}
			if (corps.getWholeTexture() != null) {
				corps.getWholeTexture().dispose();
				corps.setWholeTexture(null);
			}
		}
		backgroundRight.getTexture().dispose();
		borderRight.getTexture().dispose();
		balanceLabel.getFont().dispose();
		if (experience != null) experience.getFont().dispose();
		if (money != null) money.getFont().dispose();
	}
}
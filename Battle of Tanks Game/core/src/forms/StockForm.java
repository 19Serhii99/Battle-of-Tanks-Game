package forms;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Loading;
import com.mygdx.game.Objects;

import gui.TextButton;
import net.Client;
import technique.Corps;
import technique.Tower;

public class StockForm extends StoreBaseForm implements Disposable {
	private TechniqueTape techniqueTape;
	private TowerTape towerTape;
	
	private TextButton corpsButton;
	private TextButton towerButton;
	
	private boolean isCorps;
	private int idSelected;
	
	public StockForm () {
		super("Склад");
		towerTape = new TowerTape(this);
		techniqueTape = new TechniqueTape(this);
	}
	
	public void initCorpsValues (Corps corps) {
		super.initCorpsValues(corps);
		corpsButton = new TextButton("Продати", Objects.getInstance().getButtonBitmapFont());
		
		if (corpsButton != null) {
			corpsButton.setTexture(Objects.getInstance().getButtonTexture());
			corpsButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
			corpsButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
			corpsButton.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
			corpsButton.setSize(200, 50);
			corpsButton.setPosition(background.getX() + 130, background.getVertices()[Batch.Y2] - 420);
		}
	}
	
	public void initTowerValues (Tower tower) {	
		super.initTowerValues(tower);
		towerButton = new TextButton("Продати", Objects.getInstance().getButtonBitmapFont());
		
		if (towerButton != null) {
			towerButton.setTexture(Objects.getInstance().getButtonTexture());
			towerButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
			towerButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
			towerButton.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
			towerButton.setSize(200, 50);
			towerButton.setPosition(background.getVertices()[Batch.X3] - 320, background.getVertices()[Batch.Y2] - 540);
		}
	}
	
	public void show (SpriteBatch batch) {		
		batch.begin();
		
		super.show(batch);
		
		techniqueTape.show(batch);
		towerTape.show(batch);
		
		corpsBackground.draw(batch);
		towerBackground.draw(batch);
		
		if (corpsButton != null) {
			corpsButton.show(batch);
			if (corpsButton.isReleased()) {
				super.operation = new Operation(3, super.corpsName.getText(), super.moneyCorpsValue / 2);
				corpsButton.setDisable(true);
				towerButton.setDisable(true);
				isCorps = true;
				idSelected = super.corps.getId();
			}
		}
		if (towerButton != null) {
			towerButton.show(batch);
			if (towerButton.isReleased()) {
				super.operation = new Operation(3, super.towerName.getText(), super.moneyTowerValue / 2);
				corpsButton.setDisable(true);
				towerButton.setDisable(true);
				isCorps = false;
				idSelected = super.tower.getId();
			}
		}
		
		batch.end();
		
		if (super.operation != null) {
			super.operation.show(batch);
			if (super.operation.isNo()) {
				super.operation.dispose();
				super.operation = null;
				corpsButton.setDisable(false);
				towerButton.setDisable(false);
			} else if (super.operation.isYes()) {
				Client.getInstance().doOperation(super.operation.getType(), isCorps, idSelected);
				super.operation.dispose();
				super.operation = null;
				isLoading = true;
				loading = new Loading();
				loading.setText("Обробка");
			}
		}
	}
	
	public TowerTape getTowerTape () {
		return towerTape;
	}
	
	@Override
	public void dispose () {
		super.dispose();
		techniqueTape.dispose();
		towerTape.dispose();
	}
}
package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.BattleType;
import com.mygdx.game.Loading;
import com.mygdx.game.Objects;
import com.mygdx.game.Settings;

import answers.ConfirmCancelBattleAnswer;
import answers.ConnectToBattleServerAnswer;
import answers.OtherPlayersTechniqueAnswer;
import answers.PlayBattleAnswer;
import gui.TextButton;
import net.Battle;
import net.BattleCommand;
import net.Client;
import util.CameraController;
import util.TextureCreator;

public class WaitForGameForm implements Disposable {
	private Sprite background;
	private Loading loading;
	private BattleCommand battle;
	private TextButton cancel;
	private BattleType battleType;
	private Sprite loadingBackground;
	private Sprite progressBar;

	private int idTechnique;
	private int idTower;
	private boolean isConnecting;
	private boolean isBattle;
	private boolean isFinished;

	public WaitForGameForm(BattleType battleType, int idTechnique, int idTower) {
		background = new Sprite(new Texture(Gdx.files.internal("images/background.jpg")));
		background.setSize(Settings.getInstance().getCameraWidth(), Settings.getInstance().getCameraHeight());

		loading = new Loading();
		loading.setText("Обробка...");

		loadingBackground = new Sprite(TextureCreator.createTexture(Color.GRAY));
		loadingBackground.setSize(1500, 50);
		loadingBackground.setPosition(CameraController.getInstance().getWidth() / 2 - loadingBackground.getWidth() / 2,
				200);

		progressBar = new Sprite(TextureCreator.createTexture(Color.GREEN));
		progressBar.setPosition(loadingBackground.getX(), loadingBackground.getY());

		cancel = new TextButton("Відмінити", Objects.getInstance().getButtonBitmapFont());
		cancel.setTexture(Objects.getInstance().getButtonTexture());
		cancel.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		cancel.setTextureOver(Objects.getInstance().getButtonOverTexture());
		cancel.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		cancel.setSize(200, 50);
		cancel.setPosition(CameraController.getInstance().getWidth() / 2 - cancel.getWidth() / 2, 200);

		this.battleType = battleType;
		this.idTechnique = idTechnique;
		this.idTower = idTower;
	}

	public void show(SpriteBatch batch) {
		if (!isBattle) {
			batch.begin();
			background.draw(batch);
			batch.end();

			loading.show(batch);

			batch.begin();
			if (isConnecting) {
				loadingBackground.draw(batch);
				progressBar.draw(batch);
			} else {
				cancel.show(batch);
				if (cancel.isReleased()) {
					Client.getInstance().sendQuery("cancelBattle");
				}
			}
			batch.end();
		}

		if (battle != null) {
			if (battle.isConnected() && !battle.isMapLoaded()) {
				loading.setText("Завантаження мапи...");
				battle.loadMap();
				progressBar.setSize(1300, 50);
			} else if (battle.isConnected() && battle.isMapLoaded()) {
				loading.setText("Завантаження ігрових даних...");
				progressBar.setSize(1500, 50);
			}
		}

		if (!Client.getInstance().getServerListener().getObjects().isEmpty()) {
			Object object = Client.getInstance().getServerListener().getObjects().pop();
			if (object.getClass() == PlayBattleAnswer.class) {
				PlayBattleAnswer playBattleAnswer = (PlayBattleAnswer) object;
				if (playBattleAnswer.getValue()) {
					loading.setText("Пошук гравців та серверів...");
				}
			} else if (object.getClass() == ConnectToBattleServerAnswer.class) {
				ConnectToBattleServerAnswer answer = (ConnectToBattleServerAnswer) object;
				battle = new BattleCommand();
				isConnecting = true;
				loading.setText("Підключення до серверу...");
				battle.connectTo(answer.getHost(), answer.getPortTCP(), answer.getPortUDP(), answer.getMapName());
				progressBar.setSize(150, 50);
				battle.loadPlayer(answer.getId(), idTechnique, idTower, answer.isLeftSide(),
						Settings.getInstance().getName());
				progressBar.setSize(200, 50);
			} else if (object.getClass() == OtherPlayersTechniqueAnswer.class) {
				OtherPlayersTechniqueAnswer answer = (OtherPlayersTechniqueAnswer) object;
				battle.loadPlayers(answer);
				progressBar.setSize(500, 50);
			} else if (object.getClass() == ConfirmCancelBattleAnswer.class) {
				ConfirmCancelBattleAnswer answer = (ConfirmCancelBattleAnswer) object;
				if (answer.getValue()) {
					isFinished = true;
				}
			}
		}

		if (battle != null) {
			if (battle.isConnected() && battle.isMapLoaded() && !isBattle) {
				battle.setReady();
				isBattle = true;
			}

			if (isBattle) {
				batch.begin();
				battle.show(batch);
				if (battle.isExit()) {
					battle.dispose();
					isFinished = true;
				}
				batch.end();
			}
		}
	}

	public Battle getBattle() {
		return battle;
	}

	public boolean isFinished() {
		return isFinished;
	}

	@Override
	public void dispose() {
		background.getTexture().dispose();
		loading.dispose();
		loadingBackground.getTexture().dispose();
		progressBar.getTexture().dispose();
	}
}
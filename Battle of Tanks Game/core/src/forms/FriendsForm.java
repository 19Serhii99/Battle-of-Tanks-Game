package forms;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Events;
import com.mygdx.game.Objects;

import answers.FindFriendAnswer;
import answers.FriendOBJ;
import answers.FriendsAnswer;
import gui.CustomTextField;
import gui.Label;
import gui.PopupMenu;
import gui.Scrollbar;
import net.Client;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public class FriendsForm implements Disposable {
	private Sprite background;
	private Label caption;
	private CustomTextField searchField;
	private Array <FriendForm> friends;
	private Array <FriendForm> otherPeople;
	private BitmapFont nicknameBitmapFont;
	private BitmapFont onlineBitmapFont;
	private BitmapFont offlineBitmapFont;
	private BitmapFont onlineSecondaryStatusBitmapFont;
	private BitmapFont offlineSecondaryStatusBitmapFont;
	private Scrollbar scroll;
	private Rectangle scissors;
	private Rectangle area;
	private Texture friendFormTexture;
	private Texture friendFormOverTexture;
	private Sprite messages;
	private Label tooltipSearch;
	private Stage stage;
	private InputMultiplexer inputMultiplexer;
	private Label noFriendsLabel;
	
	private Texture scrollbarTexture;
	private Texture scrollTexture;
	private Texture scrollOverTexture;
	private Texture scrollFocusedTexture;
	
	private boolean isTooltipSearch = true;
	private boolean isHide = false;
	private boolean isSearchPeople = false;
	private float alpha = 0.0f;
	
	private FriendForm currentForm;
	private PopupMenu menu;
	
	public FriendsForm (FriendsAnswer friendsAnswer) {
		stage = new Stage();
		inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		inputMultiplexer.addProcessor(stage);
		
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/menu/formFriends.png"));
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background = new Sprite(backgroundTexture);
		background.setSize(400, 950);
		background.setPosition(50, CameraController.getInstance().getHeight() - background.getHeight() - 70);
		
		BitmapFont captionBitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		captionBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		caption = new Label("Друзі", captionBitmapFont);
		caption.setPosition(background.getX() + background.getWidth() / 2 - caption.getWidth() / 2, background.getVertices()[Batch.Y2] - 15);
		
		initFields();
		initFriends(friendsAnswer);
		initScrollbar();
		
		scissors = new Rectangle();
		area = new Rectangle(background.getX(), background.getY() + 20, background.getWidth(), 800);
		
		messages = new Sprite(new Texture(Gdx.files.internal("images/menu/messages.png")));
		messages.setSize(50, 50);
		messages.setPosition(background.getX() + background.getWidth() / 2 - messages.getWidth() / 2, caption.getY() - caption.getHeight() - messages.getHeight() - 10);
		
		BitmapFont fontItalic = Font.getInstance().generateBitmapFontItalic(Color.BLACK, 15);
		fontItalic.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		tooltipSearch = new Label("Пошук...", fontItalic);
		tooltipSearch.setPosition(searchField.getBackground().getX() + 10, searchField.getBackground().getY() + searchField.getBackground().getHeight() - 10);
	}
	
	private void initPopupMenu (float x, float y, float width, float height, String ... items) {
		if (menu != null) menu.dispose();
		
		Texture texture = TextureCreator.createTexture(new Color(72f / 255f, 72f / 255f, 72f / 255f, 1.0f));
		Texture textureOver = TextureCreator.createTexture(new Color(98f / 255f, 98f / 255f, 98f / 255f, 1.0f));
		Texture textureFocused = TextureCreator.createTexture(new Color(233f / 255f, 134f / 255f, 37f / 255f, 1.0f));
		
		menu = new PopupMenu(Color.WHITE, Color.BLACK, 20, texture, textureOver, textureFocused, x, y, width, height);
		
		for (String string : items) {
			menu.addItem(string);
		}
		
		menu.setBorder(5, Color.ORANGE);
	}
	
	private void initFields () {		
		searchField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		searchField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		searchField.getBackground().setSize(300, 40);
		searchField.getBackground().setPosition(background.getX() + background.getWidth() / 2 - searchField.getBackground().getWidth() / 2, background.getVertices()[Batch.Y2] - 130);
		searchField.getTextField().setSize(285, 40);
		searchField.getTextField().setPosition(searchField.getBackground().getX() + 10, background.getVertices()[Batch.Y2] - 125);
		searchField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					searchField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
					isTooltipSearch = false;
				} else {
					searchField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
					if (searchField.getTextField().getText().length() == 0) isTooltipSearch = true;
				}
			}
		});
		searchField.getTextField().setMaxLength(20);
		searchField.getTextField().setTextFieldListener(new TextFieldListener () {
			@Override
			public void keyTyped (TextField textField, char c) {
				searchPlayer();
			}
		});
		
		stage.addActor(searchField.getTextField());
	}
	
	private void initFriends (FriendsAnswer friendsAnswer) {
		friends = new Array <FriendForm>(1);
		otherPeople = new Array <FriendForm>(1);
		
		friendFormTexture = new Texture(Gdx.files.internal("images/menu/friendForm.png"));
		friendFormTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		friendFormOverTexture = TextureCreator.createTexture(Color.GRAY);
		friendFormOverTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		nicknameBitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		nicknameBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		onlineBitmapFont = Font.getInstance().generateBitmapFont(Color.GREEN, 20);
		onlineBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		offlineBitmapFont = Font.getInstance().generateBitmapFont(Color.RED, 20);
		offlineBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		onlineSecondaryStatusBitmapFont = Font.getInstance().generateBitmapFont(Color.GREEN, 15);
		onlineSecondaryStatusBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		offlineSecondaryStatusBitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 15);
		offlineSecondaryStatusBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		for (int i = 0; i < friendsAnswer.getFriends().size(); i++) {
			FriendOBJ friendOBJ = friendsAnswer.getFriends().get(i);
			Label nickname = new Label(friendOBJ.getNickname(), nicknameBitmapFont);
			FriendForm friend = null;
			if (i == 0) {
				friend = new FriendForm(friendOBJ.getId(), friendFormTexture, friendFormOverTexture, background.getX() + background.getWidth() / 2 - 300 / 2,
						searchField.getBackground().getY() - 90, 300, 80);
			} else {
				friend = new FriendForm(friendOBJ.getId(), friendFormTexture, friendFormOverTexture, background.getX() + background.getWidth() / 2 - 300 / 2,
						friends.get(i - 1).getBackground().getY() - 80, 300, 80);
			}
			friend.setNickname(nickname);
			if (friendOBJ.isOnline()) {
				friend.setPrimaryStatus(new Label("Online", onlineBitmapFont));
				if (friendOBJ.isFights()) {
					String gameType;
					String battleType;
					switch (friendOBJ.getGameType()) {
					case 1 : gameType = "гра з ботами"; break;
					case 2 : gameType = "мультиплеєр"; break;
					default : gameType = "";
					}
					switch (friendOBJ.getBattleType()) {
					case 1 : battleType = "Командний режим [" + friendOBJ.getWinScore() + ":" + friendOBJ.getFailScore() + "]"; break;
					case 2 : battleType = "Штурм [" + friendOBJ.getWinScore() + ":" + friendOBJ.getFailScore() + "]"; break;
					case 3 : battleType = "Бій на смерть"; break;
					default : battleType = "";
					}
					friend.setSecondaryStatus("Статус: в бою - " + gameType, battleType, onlineSecondaryStatusBitmapFont);
				} else {
					friend.setSecondaryStatus("Статус: не в бою", null, onlineSecondaryStatusBitmapFont);
				}
			} else {
				friend.setPrimaryStatus(new Label("Offline", offlineBitmapFont));
				friend.setSecondaryStatus("Був " + friendOBJ.getLastSeen(), null, offlineSecondaryStatusBitmapFont);
			}
			friends.add(friend);
		}
		
		if (friends.size == 0) {
			noFriendsLabel = new Label("Жодного друга", Font.getInstance().generateBitmapFontItalic(Color.WHITE, 20));
			noFriendsLabel.setPosition(background.getX() + background.getWidth() / 2 - noFriendsLabel.getWidth() / 2, background.getVertices()[Batch.Y2] - 150);
		} else {
			isSearchPeople = false;
		}
	}
	
	private void searchPlayer () {
		String nickname = searchField.getTextField().getText().trim();
		LinkedList <FriendForm> selectedFriends = new LinkedList <FriendForm>();
		if (nickname.length() == 0) {
			for (FriendForm friend : friends) {
				friend.setVisible(true);
				selectedFriends.add(friend);
			}
		} else {
			for (FriendForm friend : friends) {
				if (friend.getNickname().toLowerCase().contains(nickname.trim().toLowerCase())) {
					friend.setVisible(true);
					selectedFriends.add(friend);
				} else {
					friend.setVisible(false);
				}
			}
		}
		float height = 80 * selectedFriends.size() + 20;
		if (background.getHeight() - 160 < height) {
			if (scroll == null) {
				scroll = new Scrollbar(scrollbarTexture, scrollTexture, scrollFocusedTexture, scrollOverTexture, 20, background.getHeight() - 160, height,
						background.getX() + background.getWidth() - 30, background.getY() + 20);
			} else {
				scroll.calculateHeight(height);
			}
		} else {
			scroll = null;
		}
		for (int i = 0; i < selectedFriends.size(); i++) {
			if (i == 0) {
				selectedFriends.get(i).setGlobalPosition(searchField.getBackground().getY() - 90);
			} else {
				selectedFriends.get(i).setGlobalPosition(selectedFriends.get(i - 1).getBackground().getY() - selectedFriends.get(i).getBackground().getHeight());
			}
		}
		if (selectedFriends.size() == 0 && nickname.length() > 0) {
			Client.getInstance().findFriend(nickname, -1, false);
		}
	}
	
	public void updateFriend (FriendsAnswer friendsAnswer) {
		for (int i = 0; i < friendsAnswer.getFriends().size(); i++) {
			for (FriendForm friend : friends) {
				FriendOBJ friendOBJ = friendsAnswer.getFriends().get(i);
				if (friend.getId() == friendOBJ.getId()) {
					if (friendOBJ.isOnline()) {
						friend.setPrimaryStatus(new Label("Online", onlineBitmapFont));
						if (friendOBJ.isFights()) {
							String gameType;
							String battleType;
							switch (friendOBJ.getGameType()) {
							case 1 : gameType = "гра з ботами"; break;
							case 2 : gameType = "мультиплеєр"; break;
							default : gameType = "";
							}
							switch (friendOBJ.getBattleType()) {
							case 1 : battleType = "Командний режим [" + friendOBJ.getWinScore() + ":" + friendOBJ.getFailScore() + "]"; break;
							case 2 : battleType = "Штурм [" + friendOBJ.getWinScore() + ":" + friendOBJ.getFailScore() + "]"; break;
							case 3 : battleType = "Deathmatch"; break;
							default : battleType = "";
							}
							friend.setSecondaryStatus("Статус: в бою - " + gameType, battleType, onlineSecondaryStatusBitmapFont);
						} else {
							friend.setSecondaryStatus("Статус: не в бою", null, onlineSecondaryStatusBitmapFont);
						}
					} else {
						friend.setPrimaryStatus(new Label("Offline", offlineBitmapFont));
						friend.setSecondaryStatus("Був " + friendOBJ.getLastSeen(), null, offlineSecondaryStatusBitmapFont);
					}
				}
			}
		}
	}
	
	private void initScrollbar () {
		scrollbarTexture = TextureCreator.createTexture(new Color(59f / 255f, 59f / 255f, 59f / 255f, 1.0f));
		scrollTexture = TextureCreator.createTexture(new Color(172f / 255f, 172f / 255f, 172f / 255f, 1.0f));
		scrollOverTexture = TextureCreator.createTexture(new Color(148f / 255f, 148f / 255f, 148f / 255f, 1.0f));
		scrollFocusedTexture = TextureCreator.createTexture(new Color(98f / 255f, 98f / 255f, 98f / 255f, 1.0f));
		
		scrollbarTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
		scrollTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
		scrollOverTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
		scrollFocusedTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.MipMapLinearLinear);
		
		float height = 80 * friends.size + 20;
		
		if (background.getHeight() - 160 < height) {
			scroll = new Scrollbar(scrollbarTexture, scrollTexture, scrollFocusedTexture, scrollOverTexture, 20, background.getHeight() - 160, height,
					background.getX() + background.getWidth() - 30, background.getY() + 20);
		}
	}
	
	public void findFriends (FindFriendAnswer answer) {
		friends.clear();
		for (int i = 0; i < answer.getPeople().size(); i++) {
			FriendForm human;
			if (i == 0) {
				human = new FriendForm(answer.getPeople().get(i).getId(), friendFormTexture, friendFormOverTexture, background.getX() + background.getWidth() / 2 - 300 / 2,
						searchField.getBackground().getY() - 90, 300, 80);
			} else {
				human = new FriendForm(answer.getPeople().get(i).getId(), friendFormTexture, friendFormOverTexture, background.getX() + background.getWidth() / 2 - 300 / 2,
						friends.get(i - 1).getBackground().getY() - 80, 300, 80);
			}
			human.setNickname(new Label(answer.getPeople().get(i).getNickname(), nicknameBitmapFont));
			friends.add(human);
		}
		if (friends.size > 0) {
			if (noFriendsLabel != null) {
				noFriendsLabel.getFont().dispose();
				noFriendsLabel = null;
			}
		}
		isSearchPeople = true;
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
		searchField.getBackground().setAlpha(alpha);
		
		boolean wasMenu = true;
		
		batch.begin();
		background.draw(batch);
		caption.draw(batch);
		searchField.getBackground().draw(batch);
		if (scroll != null) scroll.show(batch);
		batch.flush();
		if (scroll != null) {
			ScissorStack.calculateScissors(CameraController.getInstance().getCamera(), batch.getTransformMatrix(), area, scissors);
			ScissorStack.pushScissors(scissors);
			if (scroll.isScrolling()) {
				for (FriendForm friend : friends) {
					friend.setPosition(scroll.heightScrolling());
					friend.setDisable(menu == null ? false : true);
					friend.show(batch);
					if (friend.isReleased()) {
						Vector3 cursor = CameraController.getInstance().unproject();
						initPopupMenu(cursor.x, cursor.y, 250, 50, "Додати у друзі", "Додати у чорний список");
						wasMenu = false;
						currentForm = friend;
					}
				}
			} else {
				for (FriendForm friend : friends) {
					friend.setDisable(menu == null ? false : true);
					friend.show(batch);
					if (friend.isReleased()) {
						Vector3 cursor = CameraController.getInstance().unproject();
						initPopupMenu(cursor.x, cursor.y, 250, 50, "Додати у друзі", "Додати у чорний список");
						wasMenu = false;
						currentForm = friend;
					}
				}
			}
			batch.flush();
			ScissorStack.popScissors();
		} else {
			for (FriendForm friend : friends) {
				friend.setDisable(menu == null ? false : true);
				friend.show(batch);
				if (friend.isReleased()) {
					Vector3 cursor = CameraController.getInstance().unproject();
					initPopupMenu(cursor.x, cursor.y, 250, 50, "Додати у друзі", "Додати у чорний список");
					wasMenu = false;
					currentForm = friend;
				}
			}
		}
		messages.draw(batch);
		
		if (isTooltipSearch) {
			tooltipSearch.draw(batch);
		}
		
		if (noFriendsLabel != null) noFriendsLabel.draw(batch);
		
		batch.end();
		
		stage.act();
		stage.draw();
		
		if (menu != null) {
			menu.show(batch);
			if (menu.isSelected()) {
				if (menu.getTextItem().equals("Додати у друзі")) {
					Client.getInstance().addNewFriend(currentForm.getId());
				} else if (menu.getTextItem().equals("Додати у чорний список")) {
					Client.getInstance().addToBlackList(currentForm.getId());
				}
				menu.dispose();
				menu = null;
			} else {
				if (Events.getInstance().isMouseLeftReleased() && wasMenu) {
					menu.dispose();
					menu = null;
				}
			}
		}
		
		if (Events.getInstance().isMouseLeftPressed()) {
			Vector3 cursor = CameraController.getInstance().unproject();
			if (!(cursor.x >= searchField.getTextField().getX() && cursor.x <= searchField.getTextField().getX() + searchField.getTextField().getWidth()
					&& cursor.y >= searchField.getTextField().getY() && cursor.y <= searchField.getTextField().getY() + searchField.getTextField().getHeight())) {
				stage.setKeyboardFocus(null);
			}
		}
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		caption.getFont().dispose();
		searchField.getTextField().remove();
		friendFormTexture.dispose();
		friendFormOverTexture.dispose();
		nicknameBitmapFont.dispose();
		onlineBitmapFont.dispose();
		offlineBitmapFont.dispose();
		onlineSecondaryStatusBitmapFont.dispose();
		offlineSecondaryStatusBitmapFont.dispose();
		stage.dispose();
		inputMultiplexer.removeProcessor(stage);
		scrollbarTexture.dispose();
		scrollTexture.dispose();
		scrollOverTexture.dispose();
		scrollFocusedTexture.dispose();
		tooltipSearch.getFont().dispose();
		if (noFriendsLabel != null) noFriendsLabel.getFont().dispose();
		if (menu != null) menu.dispose();
	}
}
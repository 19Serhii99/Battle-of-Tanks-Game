package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import gui.BaseActor;
import gui.Label;

public class FriendForm extends BaseActor {
	private int id;
	private Sprite background;
	private Label nickname;
	private Label primaryStatus;
	private Label secondaryStatus;
	private Label additionalInfo;
	
	private Texture backgroundTexture;
	private Texture backgroundOverTexture;
	
	private float startYBackground;
	private float startYNickname;
	private float startYPrimaryStatus;
	private float startYSecondaryStatus;
	private float startYAdditionalInfo;
	private float alpha = 0.0f;
	private boolean isVisible = true;
	private boolean isHide = false;
	
	private float differentNickname;
	private float differentPrimaryStatus;
	private float differentSecondaryStatus;
	private float differentAdditionalInfo;
	
	public FriendForm (int id, Texture backgroundTexture, Texture backgroundOverTexture, float x, float y, float width, float height) {
		this.id = id;
		this.backgroundTexture = backgroundTexture;
		this.backgroundOverTexture = backgroundOverTexture;
		background = new Sprite(backgroundTexture);
		background.setSize(width, height);
		background.setPosition(x, y);
		startYBackground = y;
	}
	
	public void setPrimaryStatus (Label primaryStatus) {
		this.primaryStatus = primaryStatus;
		this.primaryStatus.setPosition(background.getX() + background.getWidth() - this.primaryStatus.getWidth() - 10, background.getVertices()[Batch.Y2] - 10);
		startYPrimaryStatus = this.primaryStatus.getY();
		differentPrimaryStatus = this.primaryStatus.getY() - background.getY();
	}
	
	public void setNickname (Label nickname) {
		this.nickname = nickname;
		this.nickname.setPosition(background.getX() + 10, background.getVertices()[Batch.Y2] - 10);
		startYNickname = this.nickname.getY();
		differentNickname = this.nickname.getY() - background.getY();
	}
	
	public void setSecondaryStatus (String status, String statusBattleAndScore, BitmapFont bitmapFont) {
		if (statusBattleAndScore != null) {
			secondaryStatus = new Label(status + ",", bitmapFont);
			additionalInfo = new Label(statusBattleAndScore, bitmapFont);
			additionalInfo.setPosition(background.getX() + background.getWidth() / 2 - additionalInfo.getWidth() / 2, background.getY() + additionalInfo.getHeight() + 10);
			secondaryStatus.setPosition(background.getX() + background.getWidth() / 2 - secondaryStatus.getWidth() / 2, additionalInfo.getY() + additionalInfo.getHeight() + 10);
			startYAdditionalInfo = additionalInfo.getY();
			differentAdditionalInfo = this.additionalInfo.getY() - background.getY();
		} else {
			secondaryStatus = new Label(status, bitmapFont);
			secondaryStatus.setPosition(background.getX() + background.getWidth() / 2 - secondaryStatus.getWidth() / 2, background.getY() + secondaryStatus.getHeight() + 10);
		}
		startYSecondaryStatus = secondaryStatus.getY();
		differentSecondaryStatus = this.secondaryStatus.getY() - background.getY();
	}
	
	public void setPosition (float y) {
		background.setY(startYBackground + y);
		nickname.setPosition(nickname.getX(), startYNickname + y);
		if (primaryStatus != null) primaryStatus.setPosition(primaryStatus.getX(), startYPrimaryStatus + y);
		if (secondaryStatus != null) secondaryStatus.setPosition(secondaryStatus.getX(), startYSecondaryStatus + y);
		if (additionalInfo != null) additionalInfo.setPosition(additionalInfo.getX(), startYAdditionalInfo + y);
	}
	
	public void setGlobalPosition (float y) {
		background.setY(y);
		nickname.setPosition(nickname.getX(), background.getY() + differentNickname);
		if (primaryStatus != null) primaryStatus.setPosition(primaryStatus.getX(), background.getY() + differentPrimaryStatus);
		if (secondaryStatus != null) secondaryStatus.setPosition(secondaryStatus.getX(), background.getY() + differentSecondaryStatus);
		if (additionalInfo != null) additionalInfo.setPosition(additionalInfo.getX(), background.getY() + differentAdditionalInfo);
	}
	
	public void hide () {
		isHide = true;
	}
	
	public float getAlpha () {
		return alpha;
	}
	
	public void show (SpriteBatch batch) {
		if (isHide) {
			alpha -= Gdx.graphics.getDeltaTime() * 5;
			if (alpha < 0.0f) alpha = 0.0f;
		} else {
			alpha += Gdx.graphics.getDeltaTime() * 2;
			if (alpha > 1.0f) alpha = 1.0f;
		}
		
		if (isVisible) {
			background.setAlpha(alpha);
			nickname.setAlphas(alpha);
			if (primaryStatus != null) primaryStatus.setAlphas(alpha);
			if (secondaryStatus != null) secondaryStatus.setAlphas(alpha);
			if (additionalInfo != null) additionalInfo.setAlphas(alpha);
			
			super.act(background.getX(), background.getY(), background.getWidth(), background.getHeight());
			
			if (super.isOver) {
				background.setRegion(backgroundOverTexture);
			} else {
				background.setRegion(backgroundTexture);
			}
			
			background.draw(batch);
			nickname.draw(batch);
			if (primaryStatus != null) primaryStatus.draw(batch);
			if (secondaryStatus != null) secondaryStatus.draw(batch);
			if (additionalInfo != null) additionalInfo.draw(batch);	
		}
	}
	
	public void setVisible (boolean value) {
		isVisible = value;
	}
	
	public String getPrimaryStatus () {
		return primaryStatus.getText();
	}
	
	public String getSecondaryStatus () {
		return secondaryStatus.getText() + additionalInfo != null ? ", " + additionalInfo.getText() : "";
	}
	
	public String getNickname () {
		return nickname.getText();
	}
	
	public Sprite getBackground () {
		return background;
	}
	
	public boolean isVisible () {
		return isVisible;
	}
	
	public int getId () {
		return id;
	}
}
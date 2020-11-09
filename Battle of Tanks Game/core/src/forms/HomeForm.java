package forms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import answers.FindFriendAnswer;
import answers.FriendsAnswer;
import answers.GetDataPlayerAnswer;
import net.Client;

public class HomeForm implements Disposable {
	private FriendsForm friendsForm;
	private StatisticsForm statisticsForm;
	private ImageBlock generalChatForm;
	private ImageBlockLeft leftBlock;
	
	private boolean isLoaded = false;
	
	public HomeForm () {
		Client.getInstance().sendQuery("dataPlayer");
		generalChatForm = new ImageBlock();
		leftBlock = new ImageBlockLeft();
	}
	
	public void show (SpriteBatch batch) {
		if (statisticsForm != null) statisticsForm.show(batch);
		//if (friendsForm != null) friendsForm.show(batch);
		if (generalChatForm != null) generalChatForm.show(batch);
		if (leftBlock != null) leftBlock.show(batch);
		serverListener();
	}
	
	private void serverListener () {
		if (Client.getInstance().getServerListener() != null) {
			if (Client.getInstance().getServerListener().getObjects() != null) {
				if (Client.getInstance().getServerListener().getObjects().size() > 0) {
					Object object = Client.getInstance().getServerListener().getObjects().pop();
					if (object.getClass() == GetDataPlayerAnswer.class) {
						GetDataPlayerAnswer getDataPlayerAnswer = (GetDataPlayerAnswer) object;
						if (statisticsForm == null) {
							statisticsForm = new StatisticsForm(getDataPlayerAnswer);	
						}
					} else if (object.getClass() == FriendsAnswer.class) {
						FriendsAnswer friendsAnswer = (FriendsAnswer) object;
						if (friendsForm == null) {
							friendsForm = new FriendsForm(friendsAnswer);	
						} else {
							friendsForm.updateFriend(friendsAnswer);
						}
					} else if (object.getClass() == FindFriendAnswer.class) {
						friendsForm.findFriends((FindFriendAnswer) object);
					}
				}
			}
		}
		
		if (statisticsForm != null && friendsForm != null) {
			isLoaded = true;
		}
	}
	
	public boolean isLoaded () {
		return isLoaded;
	}

	@Override
	public void dispose () {
		if (friendsForm != null) friendsForm.dispose();
		if (statisticsForm != null) statisticsForm.dispose();
		if (generalChatForm != null) generalChatForm.dispose();
	}
}
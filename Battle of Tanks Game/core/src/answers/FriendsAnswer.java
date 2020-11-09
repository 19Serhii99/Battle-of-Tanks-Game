package answers;

import java.io.Serializable;
import java.util.LinkedList;

public class FriendsAnswer implements Serializable {
	private static final long serialVersionUID = 1L;
	private LinkedList <FriendOBJ> friends;
	
	public FriendsAnswer () {
		friends = new LinkedList <FriendOBJ>();
	}
	
	public void addFriend (FriendOBJ friend) {
		friends.add(friend);
	}
	
	public LinkedList <FriendOBJ> getFriends () {
		return friends;
	}
}
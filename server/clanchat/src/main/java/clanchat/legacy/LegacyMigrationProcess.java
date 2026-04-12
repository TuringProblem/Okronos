package clanchat.legacy;

import clanchat.FriendChat;

public class LegacyMigrationProcess {

	public static void start() {
		LegacyClanLoader.loadAll(fl -> {
			var newFl = new FriendChat(fl.username);
		});
	}

}

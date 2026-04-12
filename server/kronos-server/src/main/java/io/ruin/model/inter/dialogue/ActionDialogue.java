package io.ruin.model.inter.dialogue;

import io.ruin.model.entity.player.Player;

public class ActionDialogue implements Dialogue {

	private Runnable action;

	public ActionDialogue(Runnable action) {
		this.action = action;
	}

	@Override
	public void open(Player player) {
		player.removeDialogueInterface();
		action.run();
	}

}
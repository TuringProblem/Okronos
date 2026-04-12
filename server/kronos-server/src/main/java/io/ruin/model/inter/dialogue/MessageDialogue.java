package io.ruin.model.inter.dialogue;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.*;
import io.ruin.model.inter.actions.SimpleAction;

import static io.ruin.model.inter.ComponentID.MessageDialogueComps.*;

public class MessageDialogue implements Dialogue {

	private String message;

	private Runnable action;

	private int lineHeight;

	private boolean hideContinue;

	public MessageDialogue(String message) {
		this.message = message;
	}

	public MessageDialogue action(Runnable action) {
		this.action = action;
		return this;
	}

	public MessageDialogue lineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
		return this;
	}

	public MessageDialogue hideContinue() {
		this.hideContinue = true;
		return this;
	}

	@Override
	public void open(Player player) {
		player.getPacketSender().sendToplevelSubInterface(Interface.MESSAGE_DIALOGUE, Subcomponent.DIALOGUE, ToplevelComponent.CHATBOX);
		player.getPacketSender().sendString(Interface.MESSAGE_DIALOGUE, TEXT, message);
		player.getPacketSender().setTextStyle(Interface.MESSAGE_DIALOGUE, TEXT, 1, 1, lineHeight);
		if (hideContinue)
			player.getPacketSender().setHidden(Interface.MESSAGE_DIALOGUE, CONTINUE, true);
		else
			player.getPacketSender().sendString(Interface.MESSAGE_DIALOGUE, CONTINUE, "Click here to continue");
		if (action != null)
			action.run();
	}

	public static void register() {
		InterfaceHandler.register(Interface.MESSAGE_DIALOGUE, h -> h.actions[CONTINUE] = (SimpleAction) Player::continueDialogue);
	}

}

package io.ruin.model.inter.dialogue;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.Subcomponent;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;

import static io.ruin.model.inter.ComponentID.HeadDialogueComps.*;

public class PlayerDialogue implements Dialogue {

	private String message;

	private Runnable action;

	private int animationId = 554;

	private int lineHeight;

	private boolean hideContinue;

	public PlayerDialogue(String message) {
		this.message = message;
	}

	public PlayerDialogue action(Runnable action) {
		this.action = action;
		return this;
	}

	public PlayerDialogue animate(int animationId) {
		this.animationId = animationId;
		return this;
	}

	public PlayerDialogue lineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
		return this;
	}

	public PlayerDialogue hideContinue() {
		this.hideContinue = true;
		return this;
	}

	@Override
	public void open(Player player) {
		player.getPacketSender().sendToplevelSubInterface(Interface.PLAYER_DIALOGUE, Subcomponent.DIALOGUE, ToplevelComponent.CHATBOX);
		player.getPacketSender().sendPlayerHead(Interface.PLAYER_DIALOGUE, HEAD_MODEL);
		player.getPacketSender().animateInterface(Interface.PLAYER_DIALOGUE, HEAD_MODEL, animationId);
		player.getPacketSender().sendString(Interface.PLAYER_DIALOGUE, NAME, player.getName());
		player.getPacketSender().sendString(Interface.PLAYER_DIALOGUE, TEXT, message);
		player.getPacketSender().setTextStyle(Interface.PLAYER_DIALOGUE, TEXT, 1, 1, lineHeight);
		player.getPacketSender().sendIfEvents(Interface.PLAYER_DIALOGUE, CONTINUE, -1, -1, 1);
		if (hideContinue)
			player.getPacketSender().setHidden(Interface.PLAYER_DIALOGUE, CONTINUE, true);
		else
			player.getPacketSender().sendString(Interface.PLAYER_DIALOGUE, CONTINUE, "Click here to continue");
		if (action != null)
			action.run();
	}

	public static void register() {
		InterfaceHandler.register(Interface.PLAYER_DIALOGUE, h -> h.actions[CONTINUE] = (SimpleAction) Player::continueDialogue);
	}

}

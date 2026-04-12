package io.ruin.model.inter.dialogue;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.*;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.utility.CS2Script;

public class UninteractableDialogue implements Dialogue {

	private String message;


	private int lineHeight;

	private boolean hideContinue;

	public UninteractableDialogue(String message) {
		this.message = message;
	}


	public UninteractableDialogue lineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
		return this;
	}

	public UninteractableDialogue hideContinue() {
		this.hideContinue = true;
		return this;
	}

	@Override
	public void open(Player player) {
		player.getPacketSender().sendToplevelSubInterface(Interface.MESSAGE_DIALOGUE, Subcomponent.DIALOGUE, ToplevelComponent.CHATBOX);
		player.getPacketSender().sendString(Interface.MESSAGE_DIALOGUE, ComponentID.MessageDialogueComps.TEXT, message);
		player.getPacketSender().setTextStyle(Interface.MESSAGE_DIALOGUE, ComponentID.MessageDialogueComps.TEXT, 1, 1, lineHeight);
		CS2Script.CHATBOX_EMPTY.sendScript(player, message);
//        player.getPacketSender().sendString(174, 3, "");
//        player.getPacketSender().sendToplevelSubInterface(174, 1, ClientInterfaceType.OVERLAY);
//        CS2Script.FADE_OVERLAY.sendScript(player,0, 255, 0, 0, 50);
//        CS2Script.CC_DELETEALL.sendScript(player,19857409);

		//
//        if(hideContinue)
//            player.getPacketSender().setHidden(Interface.MESSAGE_DIALOGUE, ComponentID.MessageDialogueComps.CONTINUE, true);
//        else
//            player.getPacketSender().sendString(Interface.MESSAGE_DIALOGUE, ComponentID.MessageDialogueComps.CONTINUE, "Click here to continue");

	}


}
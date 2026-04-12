package io.ruin.model.inter.dialogue;

import io.ruin.cache.NPCType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.Subcomponent;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import lombok.extern.slf4j.Slf4j;

import static io.ruin.model.inter.ComponentID.HeadDialogueComps.*;

@Slf4j
public class NPCDialogue implements Dialogue {

	private NPCType npcDef;

	private String message;

	private Runnable action;

	private Runnable onDialogueOpened;

	private int animationId = 554;

	private int lineHeight;

	private boolean hideContinue;

	public NPCDialogue(int npcId, String message) {
		this(NPCType.get(npcId), message);
	}

	public NPCDialogue(NPC npc, String message) {
		this(npc.getDef(), message);
	}

	private NPCDialogue(NPCType npcDef, String message) {
		this.npcDef = npcDef;
		this.message = message;
	}

	public NPCDialogue onDialogueOpened(Runnable openDialogueOpened) {
		this.onDialogueOpened = openDialogueOpened;
		return this;
	}

	public NPCDialogue animate(int animationId) {
		this.animationId = animationId;
		return this;
	}

	public NPCDialogue lineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
		return this;
	}

	public NPCDialogue hideContinue() {
		this.hideContinue = true;
		return this;
	}

	public NPCDialogue action(Runnable action) {
		this.action = action;
		return this;
	}

	@Override
	public void open(Player player) {
		player.getPacketSender().sendToplevelSubInterface(Interface.NPC_DIALOGUE, Subcomponent.DIALOGUE, ToplevelComponent.CHATBOX);
		player.getPacketSender().sendNpcHead(Interface.NPC_DIALOGUE, HEAD_MODEL, npcDef.id);
		player.getPacketSender().animateInterface(Interface.NPC_DIALOGUE, HEAD_MODEL, animationId);
		player.getPacketSender().sendString(Interface.NPC_DIALOGUE, NAME, npcDef.name);
		player.getPacketSender().sendString(Interface.NPC_DIALOGUE, TEXT, message);
		player.getPacketSender().setTextStyle(Interface.NPC_DIALOGUE, TEXT, 1, 1, lineHeight);
		player.getPacketSender().sendIfEvents(Interface.NPC_DIALOGUE, CONTINUE, -1, -1, 1);
		if (hideContinue)
			player.getPacketSender().setHidden(Interface.NPC_DIALOGUE, CONTINUE, true);
		else
			player.getPacketSender().sendString(Interface.NPC_DIALOGUE, CONTINUE, "Click here to continue");
		if (onDialogueOpened != null)
			onDialogueOpened.run();
	}

	public static void register() {
		InterfaceHandler.register(Interface.NPC_DIALOGUE, h -> h.actions[CONTINUE] = (SimpleAction) player -> {
			player.continueDialogue();
			player.onDialogueContinued();
		});
	}

}

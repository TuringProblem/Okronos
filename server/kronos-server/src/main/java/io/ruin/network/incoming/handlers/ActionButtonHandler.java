package io.ruin.network.incoming.handlers;

import io.ruin.api.buffer.InBuffer;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.*;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.handlers.AchievementInterface;
import io.ruin.model.inter.questtab.main.Achievements;

import io.ruin.model.tutorial.GameModeInterface;
import io.ruin.network.incoming.Incoming;
import io.ruin.utility.DebugMessage;
import io.ruin.utility.IdHolder;
import lombok.extern.slf4j.Slf4j;
import net.rsprot.protocol.game.incoming.buttons.If1Button;
import net.rsprot.protocol.game.incoming.buttons.If3Button;
import net.rsprot.protocol.game.incoming.buttons.IfSubOp;
import net.rsprot.protocol.game.incoming.resumed.ResumePauseButton;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import static io.ruin.network.ClientProt204.*;

@Slf4j
public class ActionButtonHandler {

	public static class If3 implements MessageConsumer<Player, If3Button> {
		@Override
		public void consume(Player player, If3Button msg) {
			handleAction(player, msg.getOp(), msg.getCombinedId(), msg.getSub(), msg.getObj(), false);
		}
	}

	public static class IfSub implements MessageConsumer<Player, IfSubOp> {
		@Override
		public void consume(Player player, IfSubOp msg) {
			player.sendMessage("if_sub: " + msg);
		}
	}

	public static class If1 implements MessageConsumer<Player, If1Button> {
		@Override
		public void consume(Player player, If1Button msg) {
			handleAction(player, 1, msg.getCombinedId(), -1, -1, false);
		}
	}

	public static class DialogueResume implements MessageConsumer<Player, ResumePauseButton> {
		@Override
		public void consume(Player player, ResumePauseButton msg) {
			handleAction(player, 1, msg.getCombinedId(), msg.getSub(), -1, true);
		}
	}

	@IdHolder(ids = { IF_BUTTON1, IF_BUTTON2, IF_BUTTON3, IF_BUTTON4, IF_BUTTON5, IF_BUTTON6, IF_BUTTON7, IF_BUTTON8,
			IF_BUTTON9, IF_BUTTON10 })
	public static final class DefaultHandler implements Incoming {

		@Override
		public void handle(Player player, InBuffer in, int opcode) {
			int option = OPTIONS[opcode];
			int interfaceHash = in.readInt();
			int slot = in.readUnsignedShort();
			int itemId = in.readUnsignedShort();
			handleAction(player, option, interfaceHash, slot, itemId, false);
		}

	}

	// done
	@IdHolder(ids = { OPHELD1, OPHELD2, OPHELD3, OPHELD4, OPHELD5 })
	public static final class InventoryHandler implements Incoming {

		@Override
		public void handle(Player player, InBuffer in, int opcode) {
			int option = OPTIONS[opcode];
			if (option == 1) {
				int itemId = in.readUnsignedShort();
				int slot = in.readUnsignedShort();
				int interfaceHash = in.readIntLE();
				handleAction(player, option, interfaceHash, slot, itemId, false);
				return;
			}
			if (option == 2) {
				int interfaceHash = in.readIntLE();
				int slot = in.readUnsignedShort();
				int itemId = in.readUnsignedShort();
				handleAction(player, option, interfaceHash, slot, itemId, false);
				return;
			}
			if (option == 3) {
				int itemId = in.readUnsignedShort();
				int slot = in.readUnsignedShort();
				int interfaceHash = in.readIntLE();
				handleAction(player, option, interfaceHash, slot, itemId, false);
				return;
			}
			if (option == 4) {
				int itemId = in.readUnsignedLEShort();
				int interfaceHash = in.readIntLE();
				int slot = in.readUnsignedLEShortA();
				handleAction(player, option, interfaceHash, slot, itemId, false);
				return;
			}
			if (option == 5) {
				int interfaceHash = in.readIntME();
				int itemId = in.readUnsignedShort();
				int slot = in.readUnsignedLEShortA();
				handleAction(player, option, interfaceHash, slot, itemId, false);
				return;
			}
			player.sendFilteredMessage("Unhandled interface action: option=" + option + " opcode=" + opcode);
		}

	}

	@IdHolder(ids = { IF1_BUTTON1, IF1_BUTTON2, IF1_BUTTON3, IF1_BUTTON4, IF1_BUTTON5 })
	public static final class ExchangeHandler implements Incoming {
		@Override
		public void handle(Player player, InBuffer in, int opcode) {
			int option = OPTIONS[opcode];
			if (option == 1) { // Value
				int interfaceHash = in.readInt();
				int slot = in.readUnsignedShortA();
				int itemId = in.readUnsignedLEShort();
				handleAction(player, option, interfaceHash, slot, itemId, false);
				return;
			}
			if (option == 2) { // Exchange 1
				int interfaceHash = in.readIntME();
				int slot = in.readUnsignedShort();
				int itemId = in.readUnsignedShortA();
				handleAction(player, option, interfaceHash, slot, itemId, false);
				return;
			}
			if (option == 3) { // Exchange 5
				int itemId = in.readUnsignedShortA();
				int interfaceHash = in.readIntLE();
				int slot = in.readUnsignedLEShortA();
				handleAction(player, option, interfaceHash, slot, itemId, false);
				return;
			}
			if (option == 4) { // Exchange 10
				int slot = in.readUnsignedLEShortA();
				int interfaceHash = in.readInt();
				int itemId = in.readUnsignedLEShort();
				handleAction(player, option, interfaceHash, slot, itemId, false);
				return;
			}
			if (option == 5) { // Exchange X
				int slot = in.readUnsignedLEShortA();
				int itemId = in.readUnsignedLEShortA();
				int interfaceHash = in.readInt();
				handleAction(player, option, interfaceHash, slot, itemId, false);
				return;
			}
			player.sendFilteredMessage("Unhandled interface action: option=" + option + " opcode=" + opcode);
		}

	}

	@IdHolder(ids = { RESUME_PAUSEBUTTON })
	public static final class DialogueHandler implements Incoming {

		@Override
		public void handle(Player player, InBuffer in, int opcode) {
			int interfaceHash = in.readIntME();
			int slot = in.readUnsignedLEShort();

			handleAction(player, 1, interfaceHash, slot, -1, true);
		}

	}

	@IdHolder(ids = { MODELOP1 })
	public static final class OtherHandler implements Incoming {

		@Override
		public void handle(Player player, InBuffer in, int opcode) {
			int interfaceHash = in.readInt();
			handleAction(player, 1, interfaceHash, -1, -1, false);
		}

	}

	public static void handleAction(Player player, int option, int interfaceHash, int slot, int itemId,
			boolean dialogue) {
		int interfaceId = interfaceHash >> 16;
		int childId = interfaceHash & 0xffff;
		if (childId == 65535)
			childId = -1;
		if (slot == 65535)
			slot = -1;
		if (itemId == 65535)
			itemId = -1;
		if (player.debug) {
			DebugMessage debug = new DebugMessage()
					.add("option", option)
					.add("inter", interfaceId)
					.add("child", childId)
					.add("slot", slot)
					.add("item", itemId);
			player.sendFilteredMessage("[ActionButton] " + debug.toString());
		}
		AchievementInterface achievementInterface = new AchievementInterface();

		switch (interfaceId) {
			case 1100:
				switch (childId) {
					case 125:
						player.getGameModeInterface().confirmIronmanSettings(player);
						break;
					case 118:
						player.getGameModeInterface().dismissContinueIronmanWindow(player);
						break;
					case 28:
						player.getGameModeInterface().changeIronType(player, true);
						break;
					case 27:
						player.getGameModeInterface().changeIronType(player, false);
						break;
					case 33:
						player.getGameModeInterface().continueIronmanSettings(player);
						break;
				}
				break;
			case 1101:
				switch (childId) {
					case 124:
						player.getGameModeInterface().confirmAccountCreation(player);
						break;
					case 117:
						player.getGameModeInterface().closeDifficultyPopupWarning(player);
						break;
					case 28:
						player.getGameModeInterface().changeDifficultyType(player, true);
						break;
					case 27:
						player.getGameModeInterface().changeDifficultyType(player, false);
						break;
					case 32:
						player.getGameModeInterface().sendDifficultyPopupWarning(player);
						break;

				}
				break;
			case 849:
				switch (childId) {
					case 123:
						player.AchievementRewardHandler(player.currentAchievement);
						achievementInterface.GetAchievementTypeButtonDown(player);
						break;
					case 129:
						player.currentAchievement = Achievements.ARE_YOU_SHORE_ABOUT_THIS;
						achievementInterface.Init(player, player.currentAchievement.getSkillName());
						for (Achievements ach : Achievements.VALUES) {
							player.getPacketSender().setHidden(interfaceId, ach.getHiddenButtonID(), true);
						}
						player.getPacketSender().setHidden(interfaceId, player.currentAchievement.getHiddenButtonID(), false);
						achievementInterface.GetAchievementTypeButtonDown(player);
						break;
					case 130:
						player.currentAchievement = Achievements.IBANT_BELIEVE_THIS;
						achievementInterface.Init(player, player.currentAchievement.getSkillName());
						achievementInterface.GetAchievementTypeButtonDown(player);
						break;
					case 131:
						player.currentAchievement = Achievements.PROTECTIVE_HEADGEAR;
						achievementInterface.Init(player, player.currentAchievement.getSkillName());
						achievementInterface.GetAchievementTypeButtonDown(player);
						break;
					case 132:
						player.currentAchievement = Achievements.LUMBERJACK_IV;
						achievementInterface.Init(player, player.currentAchievement.getSkillName());
						achievementInterface.GetAchievementTypeButtonDown(player);
						break;
				}
				break;
		}
		if (interfaceId == 849) {
			for (Achievements ach : Achievements.VALUES) {
				if (ach.getButtonID() == childId
						&& player.currentAchievement.getAchievementType() == ach.getAchievementType()) {
					for (Achievements ach2 : Achievements.VALUES) {
						player.getPacketSender().setHidden(849, ach2.getHiddenButtonID(), true);
					}
					player.currentAchievement = ach;
					achievementInterface.Init(player, player.currentAchievement.getSkillName());
					player.openInterface(ToplevelComponent.MAINMODAL, player.currentAchievement.getInterfaceID());
					player.getPacketSender().setHidden(interfaceId, player.currentAchievement.getHiddenButtonID(), false);
					break;
				}
			}
		}
		switch (interfaceId) {
			case 883:
				switch (childId) {
					case 28:
					case 29:
					case 30:
					case 31:
						GameModeInterface.handleButtonClick(player, childId, 10206);
						break;
					case 16:
					case 17:
					case 18:
					case 19:
					case 20:
					case 58:
						GameModeInterface.handleButtonClick(player, childId, 10205);
						break;
					case 49:
						GameModeInterface.confirmChoice(player);
						break;
				}
				break;
			case 887:
				switch (childId) {
					case 19:
						player.getGamble().getGambleGameManager().HandleBlackJackHit(player);
						break;
					case 16:
						player.getGamble().getGambleGameManager().HandleBlackJackStick(player);
						break;
				}
				break;
		}
		if (interfaceId != ComponentID.LOGIN_CLICK_TO_PLAY_GROUP_ID && player.inTutorial && interfaceId != Interface.LOGOUT
				&& !dialogue && interfaceId != Interface.STARTER_INTERFACE
				&& interfaceId != Interface.APPEARANCE_CUSTOMIZATION) {
			return;
		}
		if (option == 10 && interfaceId == 149 && itemId != -1) {
			var item = player.getInventory().get(slot);
			if (item == null) {
				return;
			}
			item.examine(player);
			return;
		}
		InterfaceAction action = InterfaceHandler.getAction(player, interfaceId, childId);

		if (action == null && player.debug) {
			player.sendFilteredMessage("NO_ACTION");
		}

		if (action != null) {
			action.handleClick(player, option, slot, itemId);
		}
	}

}

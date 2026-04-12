package io.ruin.network.incoming.handlers;

import io.ruin.api.buffer.InBuffer;
import io.ruin.cache.ItemID;

import io.ruin.model.activities.gauntlet.Gauntlet;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;

import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.itemeffects.ItemEffects;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.skills.magic.spells.modern.Alchemy;
import io.ruin.model.skills.magic.spells.modern.JewelleryEnchant;
import io.ruin.model.stat.StatType;
import io.ruin.network.incoming.Incoming;
import io.ruin.utility.DebugMessage;
import io.ruin.utility.IdHolder;
import net.rsprot.protocol.game.incoming.buttons.IfButtonT;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import static io.ruin.network.ClientProt204.*;

public class InterfaceOnInterfaceHandler {

	@IdHolder(ids = {IF_BUTTONT})
	public static final class OnItem implements MessageConsumer<Player, IfButtonT> {
		@Override
		public void consume(Player player, IfButtonT msg) {
			int fromItemId = msg.getSelectedObj();
			int fromInterfaceHash = msg.getSelectedCombinedId();
			int toItemId = msg.getTargetObj();
			int fromSlot = msg.getSelectedSub();
			int toInterfaceHash = msg.getTargetCombinedId();
			int toSlot = msg.getTargetSub();
			handleAction(player, fromInterfaceHash, fromSlot, fromItemId, toInterfaceHash, toSlot, toItemId);
		}

		public void handle(Player player, InBuffer in, int opcode) {
			int fromItemId = in.readShort();
			int fromInterfaceHash = in.readIntME();
			int toItemId = in.readShortA();
			int fromSlot = in.readLEShortA();
			int toInterfaceHash = in.readInt();
			int toSlot = in.readShortA();
			handleAction(player, fromInterfaceHash, fromSlot, fromItemId, toInterfaceHash, toSlot, toItemId);
		}
	}

	@IdHolder(ids = {OPHELDT})
	public static final class OnInterface implements Incoming {
		@Override
		public void handle(Player player, InBuffer in, int opcode) {
			int toItemId = in.readUnsignedShortA();
			int fromInterfaceHash = in.readIntME();
			int toSlot = in.readUnsignedShort();
			int fromSlot = in.readUnsignedShort();
			int toInterfaceHash = in.readInt();
			handleAction(player, fromInterfaceHash, fromSlot, -1, toInterfaceHash, toSlot, toItemId);
		}
	}

	static SkillItem skillItem1 = new SkillItem(23884).addAction((player, amount, event) -> {
		amount = player.getInventory().getAmount(23881);
		while (amount-- > 0) {
			Item herbItem = player.getInventory().findItem(23881);
			if (herbItem == null)
				return;
			Item vialItem = player.getInventory().findItem(23867);
			if (vialItem == null)
				return;
			Gauntlet.createPotion(player, amount, 23867);
			event.delay(1);
		}
	});
	static SkillItem skillItem2 = new SkillItem(23884).addAction((player, amount, event) -> {
		amount = player.getInventory().getAmount(23881);
		while (amount-- > 0) {
			Item herbItem = player.getInventory().findItem(23881);
			if (herbItem == null)
				return;
			Item vialItem = player.getInventory().findItem(23830);
			if (vialItem == null)
				return;
			Gauntlet.createPotion(player, amount, 23830);
			event.delay(1);
		}
	});

	private static void handleAction(Player player, int fromInterfaceHash, int fromSlot, int fromItemId, int toInterfaceHash, int toSlot, int toItemId) {
		if (toItemId == -6009 || fromItemId == -6009) {
			for (ItemEffects effect : ItemEffects.VALUES) {
				if (toItemId == effect.getSigilItem().getId() && player.getInventory().get(fromSlot).getDef().equipSlot == Equipment.SLOT_WEAPON) {
					effect.useOnWeapon(player, player.getInventory().get(fromSlot));
					return;
				}
				if (fromItemId == effect.getSigilItem().getId() && player.getInventory().get(toSlot).getDef().equipSlot == Equipment.SLOT_WEAPON) {
					effect.useOnWeapon(player, player.getInventory().get(toSlot));
					return;
				}
			}
		}

		if (player.isLocked())
			return;
		player.resetActions(true, false, true);

		int fromInterfaceId = fromInterfaceHash >> 16;
		int fromChildId = fromInterfaceHash & 0xffff;
		if (fromChildId == 65535)
			fromChildId = -1;
		if (fromSlot == 65535)
			fromSlot = -1;
		if (fromItemId == 65535)
			fromItemId = -1;

		int toInterfaceId = toInterfaceHash >> 16;
		int toChildId = toInterfaceHash & 0xffff;
		if (toChildId == 65535)
			toChildId = -1;
		if (toSlot == 65535)
			toSlot = -1;
		if (toItemId == 65535)
			toItemId = -1;

		if (fromItemId == 23867 && toItemId == 23881) {
			if (player.getInventory().hasMultiple(23881, 23867)) {
				SkillDialogue.make(player, skillItem1);
				return;
			}
			Gauntlet.createPotion(player, 1, 23867);
			return;
		}
		if (fromItemId == 23830 && toItemId == 23881) {
			if (player.getInventory().hasMultiple(23881, 23830)) {
				SkillDialogue.make(player, skillItem2);
				return;
			}
			Gauntlet.createPotion(player, 1, 23830);
			return;
		}

		if (player.debug) {
			player.sendFilteredMessage("[DragItem]");
			DebugMessage debug = new DebugMessage()
				.add("inter", fromInterfaceId)
				.add("child", fromChildId)
				.add("slot", fromSlot)
				.add("item", fromItemId);
			player.sendFilteredMessage("    From: " + debug.toString());
			debug = new DebugMessage()
				.add("inter", toInterfaceId)
				.add("child", toChildId)
				.add("slot", toSlot)
				.add("item", toItemId);
			player.sendFilteredMessage("    To: " + debug.toString());
		}

		InterfaceAction action = InterfaceHandler.getAction(player, fromInterfaceId, fromChildId);
		if (action != null)
			action.handleOnInterface(player, fromSlot, fromItemId, toInterfaceId, toChildId, toSlot, toItemId);
	}

}

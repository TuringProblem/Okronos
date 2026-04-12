package io.ruin.model.item.actions.impl;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.map.object.actions.impl.AncientForge;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Misc;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author R-Y-M-R
 * @date 3/8/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public class Torva {
	public static final int HELMET_FIXED = 26382;
	public static final int BODY_FIXED = 26384;
	public static final int LEGS_FIXED = 26386;
	public static final int BANDOS_COMPONENTS = 26394;
	public static final int HELMET_DAMAGED = 26376;
	public static final int BODY_DAMAGED = 26378;
	public static final int LEGS_DAMAGED = 26380;
	private static final int SMITHING_REQ = 90;

	public static void register() {
		ItemAction.registerInventory(HELMET_DAMAGED, "wear", Torva::attemptWear);
		ItemAction.registerInventory(LEGS_DAMAGED, "wear", Torva::attemptWear);
		ItemAction.registerInventory(BODY_DAMAGED, "wear", Torva::attemptWear);

		ItemItemAction.register(Torva.BODY_DAMAGED, Torva.BANDOS_COMPONENTS, Torva::attemptFixTorva);
		ItemItemAction.register(Torva.LEGS_DAMAGED, Torva.BANDOS_COMPONENTS, Torva::attemptFixTorva);
		ItemItemAction.register(Torva.HELMET_DAMAGED, Torva.BANDOS_COMPONENTS, Torva::attemptFixTorva);
	}

	private static void attemptWear(Player player, Item item) {
		player.dialogue(new ItemDialogue().one(item.getId(), "You need to repair your " + item.getDef().name + " before you can wear it."),
			new PlayerDialogue("I should use some Bandos components to fix this up."));
	}


	private static void attemptFixTorva(Player player, Item item, Item b) {
		final int id = (item.getDef().name.contains("(damaged)") ? item.getId() : b.getId());
		if (player.getStats().get(StatType.Smithing).currentLevel < SMITHING_REQ) {
			player.dialogue(new ItemDialogue().one(id, "You need at least " + SMITHING_REQ + " smithing to fix " + ObjType.get(id).name + "."));
			return;
		}
		if (!AncientForge.hasHammer(player, false)) return;
		try {
			Optional<TorvaRepairing> tr = TorvaRepairing.getByDamagedId(id);
			if (tr.isPresent()) {
				TorvaRepairing repair = tr.get();
				final String piece = ObjType.get(repair.damagedID).name;
				if (!player.getInventory().contains(repair.damagedID)) {
					player.dialogue(new MessageDialogue("You need a " + piece + " to repair."));
					return;
				}
				if (!player.getInventory().contains(Torva.BANDOS_COMPONENTS, repair.componentsRequired)) {
					player.dialogue(new MessageDialogue("You need at least " + Misc.formatNumber(repair.componentsRequired)
						+ " Bandos components to repair that."));
					return;
				}
				player.dialogue(new MessageDialogue("Repairing your damaged Torva will consume " + repair.componentsRequired + " Bandos components! This process cannot be reversed."),
					new OptionsDialogue("Fix your Torva for " + repair.componentsRequired + " components?",
						new Option("Yes", () -> fixTorva(player, repair)),
						new Option("No", player::closeDialogue)));
			}
		} catch (Exception e) {
			player.sendMessage("Something unexpected ocurred! Submit a bug report with what happened.");
			e.printStackTrace();
		}

	}


	private static void fixTorva(Player player, TorvaRepairing repair) {
		player.closeDialogue();
		player.getInventory().remove(repair.damagedID, 1);
		player.getInventory().remove(Torva.BANDOS_COMPONENTS, repair.componentsRequired);
		player.getInventory().add(repair.fixedID, 1);
		player.getStats().addXp(StatType.Smithing, 2250, false);
		player.dialogue(new ItemDialogue().one(repair.fixedID, "You you repair your " + ObjType.get(repair.fixedID).name + " and gain some Smithing experience!"));
	}


	enum TorvaRepairing {
		BODY(BODY_FIXED, BODY_DAMAGED, 2),
		LEGS(LEGS_FIXED, LEGS_DAMAGED, 2),
		HELM(HELMET_FIXED, HELMET_DAMAGED, 1),
		;

		final int fixedID;
		final int damagedID;
		final int componentsRequired;

		TorvaRepairing(int fixedID, int damagedID, int componentsRequired) {
			this.fixedID = fixedID;
			this.damagedID = damagedID;
			this.componentsRequired = componentsRequired;
		}

		public static Optional<TorvaRepairing> getByDamagedId(int damagedID) {
			return Arrays.stream(TorvaRepairing.values()).filter(t -> t.damagedID == damagedID).findFirst();
		}

	}
}

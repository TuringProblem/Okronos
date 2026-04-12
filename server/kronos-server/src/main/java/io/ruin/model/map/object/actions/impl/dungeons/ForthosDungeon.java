package io.ruin.model.map.object.actions.impl.dungeons;

import io.ruin.api.utils.Random;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Region;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;

import static io.ruin.model.map.object.actions.impl.Ladder.climb;

public class ForthosDungeon {

	private static final Bounds SARACHNIS = new Bounds(1829, 9888, 1851, 9911, 0);
	public static int KNIFE = 946;

	public static void register() {
		ObjectAction peekAction = (player, obj) -> {
			int regularCount = Region.get(7322).players.size() - 1;
			if (regularCount <= 0)
				player.sendMessage("You listen and hear no adventurers inside the crypt.");
			else
				player.sendMessage("You listen and hear " + regularCount + " adventurer" + (regularCount > 1 ? "s" : "") + " inside the crypt.");
		};
		ObjectAction enterCrypt = (player, obj) -> {
			if (player.getAbsY() == 9912) {
				player.dialogue(
					new OptionsDialogue("<col=FF0000>You are about to enter a dangerous area",
						new Option("Enter.", () -> enterSarachnisLair(player)),
						new Option("Do not enter."))
				);
			} else {
				enterSarachnisLair(player);
			}
		};
		ObjectAction.register(34865, 1669, 3567, 0, "climb-down", (player, obj) -> climb(player, 1800, 9967, 0, false, true, false));
		ObjectAction.register(34864, 1798, 9967, 0, "climb-up", (player, obj) -> climb(player, 1670, 3569, 0, true, true, false));
		ObjectAction.register(34898, 2, peekAction);
		ObjectAction.register(34843, 1, (player, obj) -> player.startEvent(e -> {
			player.lock();
			e.delay(1);
			if (player.getAbsX() == 1824) {
				player.stepAbs(player.getAbsX() + 1, player.getAbsY(), StepType.WALK);
			} else {
				player.stepAbs(player.getAbsX() - 1, player.getAbsY(), StepType.WALK);
			}
			player.unlock();
		}));

		ObjectAction.register(34898, "slash", ForthosDungeon::slashWeb);
		ObjType.forEach(def -> {
			if (def.equipSlot != Equipment.SLOT_WEAPON)
				return;
			String name = def.name.toLowerCase();
			if (name.contains("axe") || name.equalsIgnoreCase("voidwaker") || name.contains("claws") || name.contains("dagger") || name.contains("sword")
				|| name.contains("scimitar") || name.contains("halberd") || name.contains("whip") || name.contains("tentacle")
				|| name.contains("blade") || name.contains("machete") || name.contains("scythe") || name.contains("staff of the dead")
				|| name.contains("xil-ek") || name.contains("excalibur") || name.contains("spear") || name.contains("hasta")
				|| name.contains("rapier"))
				def.sharpWeapon = true;
		});
	}


	private static void enterSarachnisLair(Player player) {
		ObjType wepDef = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
		boolean knife;
		if (wepDef != null && wepDef.sharpWeapon) {
			knife = false;
		} else {
			if (!player.getInventory().hasId(KNIFE)) {
				player.sendMessage("Only a sharp blade can cut through this sticky web.");
				return;
			}
			knife = true;
		}
		player.startEvent(e -> {
			player.lock();
			player.animate(knife ? 911 : player.getCombat().weaponType.attackAnimation);
			e.delay(1);
			if (player.getAbsY() == 9912) {
				player.stepAbs(player.getAbsX(), player.getAbsY() - 1, StepType.WALK);
			} else {
				player.stepAbs(player.getAbsX(), player.getAbsY() + 1, StepType.WALK);
			}
			player.unlock();
		});
	}

	public static void slashWeb(Player player, GameObject web) {
		boolean knife, wildernessSword;
		ObjType wepDef = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
		if (wepDef != null && wepDef.sharpWeapon) {
			knife = false;
			wildernessSword = wepDef.id == 13111;
		} else {
			if (!player.getInventory().hasId(KNIFE)) {
				player.sendMessage("Only a sharp blade can cut through this sticky web.");
				return;
			}
			knife = true;
			wildernessSword = false;
		}
		player.startEvent(event -> {
			player.animate(knife ? 911 : player.getCombat().weaponType.attackAnimation);
			if (wildernessSword || Random.rollDie(2, 1)) {
				player.lock();
				player.sendMessage("You slash the web apart.");
				event.delay(1);
				World.startEvent(e -> {
					web.setId(734);
					e.delay(50);
					web.setId(34898);
				});
				player.unlock();
				return;
			}
			player.sendMessage("You fail to cut through it.");
		});
	}
}

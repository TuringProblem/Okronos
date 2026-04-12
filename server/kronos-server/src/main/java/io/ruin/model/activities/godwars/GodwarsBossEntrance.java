package io.ruin.model.activities.godwars;

import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.pvminstances.InstanceDialogue;
import io.ruin.model.activities.pvminstances.InstanceType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.SecondaryGroup;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.item.Item;
import io.ruin.model.map.object.actions.ObjectAction;

public class GodwarsBossEntrance {

	private static final int ECUMENICAL_KEY = 11942;

	private static int kcRequirement(Player player) {
		if (player.isSecondaryGroup(SecondaryGroup.SUPREME_DONATOR)) {
			return 0;
		} else if (player.isSecondaryGroup(SecondaryGroup.LEGENDARY_DONATOR)) {
			return 0;
		} else if (player.isSecondaryGroup(SecondaryGroup.PLATINUM_DONATOR)) {
			return 0;
		} else if (player.isSecondaryGroup(SecondaryGroup.GOLD_DONATOR)) {
			return 0;
		} else if (player.isSecondaryGroup(SecondaryGroup.NOBLE_DONATOR)) {
			return 10;
		} else if (player.isSecondaryGroup(SecondaryGroup.ELITE_DONATOR)) {
			return 20;
		} else if (player.isSecondaryGroup(SecondaryGroup.SUPER_DONATOR)) {
			return 30;
		} else if (player.isSecondaryGroup(SecondaryGroup.DONATOR)) {
			return 35;
		} else {
			return 40;
		}
	}

	private static boolean enterThroughDoor(Player player, VarPlayerRepository config, String name) {
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.GOD_WARS_VETERAN)) {
			return true;
		}
		Item key = player.getInventory().findItem(ECUMENICAL_KEY);
		if (key != null) {
			key.remove();
			player.sendFilteredMessage("Your ecumenical key melts into the door.");
			return true;
		}

		int requiredKC = kcRequirement(player);
		if (config.get(player) >= requiredKC) {
			player.sendFilteredMessage("The door devours the life-force of " + requiredKC + " followers of " + name + " that you have slain.");
			config.set(player, config.get(player) - requiredKC);
			return true;
		}

		player.sendFilteredMessage("This door is locked by the power of " + name + "! You need to collect the essence of at least " + requiredKC +
			" followers before the door will open.");
		return false;
	}

	public static void register() {
		/**
		 * Zamorak
		 */
		ObjectAction.register(26505, 1, (player, obj) -> {
			if (player.isAt(2925, 5333) && enterThroughDoor(player, VarPlayerRepository.GWD_ZAMORAK_KC, "Zamorak")) {
				player.getMovement().teleport(2925, 5331, 2);
			} else if (player.isAt(2925, 5331) || player.isAt(2925, 5332)) {
				player.getMovement().teleport(2925, 5333, 2);
			} else if (player.getPosition().regionId() != 11603) {
				player.face(obj);
				Position playerLocation = player.getPosition();
				Direction playerFacingDirection = Direction.getDirection(player.getPosition(), obj.getPosition());
				if (playerFacingDirection == Direction.NORTH) {
					player.getMovement().teleport(playerLocation.getX(), playerLocation.getY() + 2, playerLocation.getZ());
				} else if (playerFacingDirection == Direction.SOUTH) {
					player.getMovement().teleport(playerLocation.getX(), playerLocation.getY() - 3, playerLocation.getZ());
				}

			}
		});
		ObjectAction.register(26505, 2925, 5332, 2, "instance", (player, obj) -> InstanceDialogue.open(player, InstanceType.ZAMORAK_GWD));

		/**
		 * Bandos
		 */
		ObjectAction.register(26503, 1, (player, obj) -> {
			if (player.isAt(2862, 5354) && enterThroughDoor(player, VarPlayerRepository.GWD_BANDOS_KC, "Bandos")) {
				player.getMovement().teleport(2864, 5354, 2);
			} else if (player.isAt(2864, 5354) || player.isAt(2864, 5353)) {
				player.getMovement().teleport(2862, 5354, 2);
			} else if (player.getPosition().regionId() != 11347) {
				player.face(obj);
				Position playerLocation = player.getPosition();
				Direction playerFacingDirection = Direction.getDirection(player.getPosition(), obj.getPosition());
				if (playerFacingDirection == Direction.EAST) {
					player.getMovement().teleport(playerLocation.getX() + 2, playerLocation.getY(), playerLocation.getZ());
				} else if (playerFacingDirection == Direction.WEST) {
					player.getMovement().teleport(playerLocation.getX() - 3, playerLocation.getY(), playerLocation.getZ());
				}

			}
		});
		ObjectAction.register(26503, 2863, 5354, 2, "instance", (player, obj) -> InstanceDialogue.open(player, InstanceType.BANDOS_GWD));


		/**
		 * Saradomin
		 */
		ObjectAction.register(26504, 1, (player, obj) -> {
			if (player.isAt(2909, 5265) && enterThroughDoor(player, VarPlayerRepository.GWD_SARADOMIN_KC, "Saradomin")) {
				player.getMovement().teleport(2907, 5265, 0);
			} else if (player.isAt(2907, 5265) || player.isAt(2908, 5265)) {
				player.getMovement().teleport(2909, 5265, 0);
			} else if (player.getPosition().regionId() != 11602) {
				player.face(obj);
				Position playerLocation = player.getPosition();
				Direction playerFacingDirection = Direction.getDirection(player.getPosition(), obj.getPosition());
				if (playerFacingDirection == Direction.WEST) {
					player.getMovement().teleport(playerLocation.getX() - 2, playerLocation.getY(), playerLocation.getZ());
				} else if (playerFacingDirection == Direction.EAST) {
					player.getMovement().teleport(playerLocation.getX() + 3, playerLocation.getY(), playerLocation.getZ());
				}

			}
		});
		ObjectAction.register(26504, 2908, 5265, 0, "instance", (player, obj) -> InstanceDialogue.open(player, InstanceType.SARADOMIN_GWD));

		/**
		 * Armadyl
		 */
		ObjectAction.register(26502, 1, (player, obj) -> {
			if (player.isAt(2839, 5294) && enterThroughDoor(player, VarPlayerRepository.GWD_ARMADYL_KC, "Armadyl")) {
				player.getMovement().teleport(2839, 5296, 2);
			} else if (player.isAt(2839, 5296) || player.isAt(2839, 5295)) {
				player.getMovement().teleport(2839, 5294, 2);
			} else if (player.getPosition().regionId() != 11346) {
				player.face(obj);
				Position playerLocation = player.getPosition();
				Direction playerFacingDirection = Direction.getDirection(player.getPosition(), obj.getPosition());
				if (playerFacingDirection == Direction.NORTH) {
					player.getMovement().teleport(playerLocation.getX(), playerLocation.getY() + 2, playerLocation.getZ());
				} else if (playerFacingDirection == Direction.SOUTH) {
					player.getMovement().teleport(playerLocation.getX(), playerLocation.getY() - 3, playerLocation.getZ());
				}

			}
		});
		ObjectAction.register(26502, 2839, 5295, 2, "instance", (player, obj) -> InstanceDialogue.open(player, InstanceType.ARMADYL_GWD));

	}

}

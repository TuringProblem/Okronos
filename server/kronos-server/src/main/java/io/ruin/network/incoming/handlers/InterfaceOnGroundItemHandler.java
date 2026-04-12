package io.ruin.network.incoming.handlers;

import io.ruin.api.buffer.InBuffer;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.Tile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.route.RouteType;
import io.ruin.model.map.route.routes.ProjectileRoute;
import io.ruin.model.map.route.routes.TargetRoute;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.skills.magic.rune.RuneRemoval;
import io.ruin.model.stat.StatType;
import io.ruin.network.incoming.Incoming;
import io.ruin.utility.IdHolder;

import static io.ruin.network.ClientProt204.OPOBJT;
import static io.ruin.network.ClientProt204.OPOBJU;

public class InterfaceOnGroundItemHandler {

	@IdHolder(ids = {OPOBJU})
	public static final class FromItem implements Incoming {
		@Override
		public void handle(Player player, InBuffer in, int opcode) {
			int ctrlRun = in.readByteS();
			int slot = in.readUnsignedShortA();
			int itemId = in.readUnsignedShortA();
			int x = in.readUnsignedShortA();
			int y = in.readUnsignedLEShortA();
			int interfaceHash = in.readIntME();
			int groundItemId = in.readUnsignedShort();
			handleAction(player, interfaceHash, slot, itemId, groundItemId, x, y, ctrlRun);
		}
	}


	@IdHolder(ids = {OPOBJT})
	public static final class FromInterface implements Incoming {
		@Override
		public void handle(Player player, InBuffer in, int opcode) {

			int itemId = in.readUnsignedShort();
			int ctrlRun = in.readByte();
			int groundItemId = in.readUnsignedLEShort();
			int interfaceHash = in.readInt();
			int y = in.readUnsignedShort();
			int slot = in.readUnsignedShort();
			int x = in.readUnsignedShortA();
			int telegrabHash = 14286871;
			if (interfaceHash == telegrabHash) {
				handleTelegrab(player, groundItemId, x, y, ctrlRun == 1);
			} else {
				handleAction(player, interfaceHash, slot, itemId, groundItemId, x, y, ctrlRun);
			}
		}
	}


	public static boolean withinReach(int absX, int absY, int height, int targetX, int targetY, int distance) {
		if (!TargetRoute.inTarget(absX, absY, 1, targetX, targetY, 1)
				&& TargetRoute.inRange(absX, absY, 1, targetX, targetY, 1, distance)
				&& ProjectileRoute.allow(absX, absY, height, 1, targetX, targetY, 1)) {
			return true;
		}
		return false;
	}


	private static final Projectile TELEGRAB = new Projectile(143, 35, 0, 45, 45, 8, 10, 10);

	public static void handleTelegrab(Player player, int groundItemId, int targetX, int targetY, boolean ctrlRun) {
		if (!player.getStats().check(StatType.Magic, 33, "cast this spell"))
			return;
		RuneRemoval r = RuneRemoval.get(player, new Item[] {Rune.AIR.toItem(1), Rune.LAW.toItem(1)});
		if (r == null) {
			player.sendMessage("You don't have the runes to cast this spell. You need 1 air and 1 law rune.");
			return;
		}
		int z = player.getHeight();
		Tile tile = Tile.get(targetX, targetY, z, false);
		GroundItem groundItem = tile.getPickupItem(groundItemId, player.getName());
		if (groundItem == null) {
			return;
		}

		if (player.getGameMode().isIronMan() || player.getGameMode().isHardcoreIronman()
				|| player.getGameMode().isUltimateIronman()) {
			if (!groundItem.droppedByIronPlayer(player)) {
				player.sendMessage("As an ironman you cannot telegrab items dropped by other players.");
				return;
			}
		}

		player.getMovement().setCtrlRun(ctrlRun);
		player.getRouteFinder().routeGroundItem(groundItem, distance -> player.getMovement().reset());
		RouteType route = player.getRouteFinder().routeAbsolute(groundItem.getX(), groundItem.getY());
		player.startEvent(event -> {
			if (!(player.getPosition().getX() == targetX && player.getPosition().getY() == targetY
					&& player.getPosition().getZ() == groundItem.getZ())) {
				while (!withinReach(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(),
						targetX, targetY, 7) || !player.getPosition().isWithinDistance(new Position(targetX, targetY, z), 7)) {
					if (route.finished(player.getPosition())) {
						if (!route.reachable) {
							player.sendMessage("I can't reach that!");
							player.face(targetX, targetY);
							return;
						}
					}
					event.delay(1);
				}
			}
			if (tile == null)
				return;
			if (groundItem == null)
				return;
			if (groundItem.tile == null) {
				return;
			}
			r.remove();
			player.privateSound(3006, 1, 0);
			player.animate(723, 0);
			player.graphics(142, 70, 0);
			TELEGRAB.send(player, new Position(targetX, targetY, z));
			player.face(targetX, targetY);
			player.lock();
			player.getMovement().reset();
			event.delay(2);
			player.unlock();
			if (groundItem.isRemoved()) {
				player.sendMessage("It's gone!");
				return;
			}
			player.getInventory().add(groundItem.id, groundItem.amount);
			groundItem.remove();
		});
	}

	private static void handleAction(Player player, int interfaceHash, int slot, int itemId, int groundItemId, int x,
			int y, int ctrlRun) {
		if (player.isLocked())
			return;
		player.resetActions(true, true, true);
		int z = player.getHeight();
		Tile tile = Tile.get(x, y, z, false);
		if (tile == null)
			return;
		GroundItem groundItem = tile.getPickupItem(groundItemId, player.getName());
		if (groundItem == null) {
			return;
		}
		player.getMovement().setCtrlRun(ctrlRun == 1);
		if (ObjType.get(itemId).skipPathingBeforeInteracting) {
			// instant activation not dependant on pathing to target
			action(player, interfaceHash, slot, itemId, groundItem, player.getPosition().distance(groundItem));
			return;
		}
		player.getRouteFinder().routeGroundItem(groundItem,
				distance -> action(player, interfaceHash, slot, itemId, groundItem, distance));
	}

	private static void action(Player player, int interfaceHash, int slot, int itemId, GroundItem groundItem,
			int distance) {
		InterfaceAction action = InterfaceHandler.getAction(player, interfaceHash);
		if (action == null)
			return;
		if (slot == 65535)
			slot = -1;
		if (itemId == 65535)
			itemId = -1;
		action.handleOnGroundItem(player, slot, itemId, groundItem, distance);
	}

}

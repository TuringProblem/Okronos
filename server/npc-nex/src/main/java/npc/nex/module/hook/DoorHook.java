package npc.nex.module.hook;

import io.ruin.cache.LocType;
import io.ruin.model.map.object.actions.impl.Door;
import npc.nex.loc.ZarosDoors;

public class DoorHook {

	public static void register() {
		Door.hooks.registerReturn(Door.Hook.OnHandle.class, DoorHook::handle);
	}

	private static boolean handle(Door.Hook.OnHandle ctx) {
		var obj = ctx.obj();
		var def = obj.getDef();
		var player = ctx.player();
		return switch (def.id) {
			case ZarosDoors.WAITING_ROOM_DOOR -> {
				ZarosDoors.handleWaitingRoomDoor(player, obj);
				yield true;
			}
			case ZarosDoors.ANOTHER_DOOR -> {
				ZarosDoors.handleAnotherDoor(player, obj);
				yield true;
			}
			case ZarosDoors.FROZEN_DOOR_A, ZarosDoors.FROZEN_DOOR_A2 -> {
				ZarosDoors.handleFrozenDoorA(player, obj);
				yield true;
			}
			case ZarosDoors.FROZEN_DOOR_B, ZarosDoors.FROZEN_DOOR_B2 -> {
				ZarosDoors.handleFrozenDoorB(player, obj);
				yield true;
			}
			default -> {
				yield false;
			}
		};
	}
}

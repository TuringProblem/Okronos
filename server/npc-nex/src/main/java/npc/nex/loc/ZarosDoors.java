package npc.nex.loc;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Common;

/**
 * @author R-Y-M-R
 * @date 3/15/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
public class ZarosDoors {
	public static final int FROZEN_DOOR_A = 42841;
	public static final int FROZEN_DOOR_A2 = 42840;
	public static final int FROZEN_DOOR_B = 42931;
	public static final int FROZEN_DOOR_B2 = 42932;
	public static final int WAITING_ROOM_DOOR = 42934;
	public static final int ANOTHER_DOOR = 42933;

	public static void register() {
		// the below don't actually work right now, so I have added them to a switch statement in doors.java. At a future date, the code below may start working. I believe gameobject cache defs are missing right now.
		ObjectAction.register(WAITING_ROOM_DOOR, "open", ZarosDoors::handleWaitingRoomDoor);
		ObjectAction.register(ANOTHER_DOOR, "open", ZarosDoors::handleAnotherDoor);
		ObjectAction.register(FROZEN_DOOR_A, "open", ZarosDoors::handleFrozenDoorA);
		ObjectAction.register(FROZEN_DOOR_A2, "open", ZarosDoors::handleFrozenDoorA);
		ObjectAction.register(FROZEN_DOOR_B, "open", ZarosDoors::handleFrozenDoorB);
		ObjectAction.register(FROZEN_DOOR_B2, "open", ZarosDoors::handleFrozenDoorB);
	}

	public static void useDoor(Player p, GameObject obj, int destX, int destY, int destZ) {
		p.startEvent(event -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.face(obj);
			Common.openDoorEffects(p);
			event.delay(1);
			p.getMovement().teleport(destX, destY, destZ);
			p.unlock();
		});
	}

	// the gwd-side door
	public static void handleFrozenDoorA(Player p, GameObject obj) {
		useDoor(p, obj, 2855, 5227, 0);
	}

	// the nex area door
	public static void handleFrozenDoorB(Player p, GameObject obj) {
		useDoor(p, obj, 2883, 5280, 2);
	}

	// the door between frozen door B and nex area
	public static void handleAnotherDoor(Player p, GameObject obj) {
		final int destX = p.getPosition().getX() == 2861 ? 2863 : 2861;
		useDoor(p, obj, destX, p.getPosition().getY(), p.getPosition().getZ());
	}

	// the door outside nex
	public static void handleWaitingRoomDoor(Player p, GameObject obj) {
		// 70 agility/70 HP/70 ranged and strength required
		if (!Common.hasStatRequirements(p, 70, true, StatType.Agility, StatType.Hitpoints, StatType.Ranged, StatType.Strength)) {
			return;
		}
		final int destX = p.getPosition().getX() == 2900 ? 2899 : 2900;
		useDoor(p, obj, destX, p.getPosition().getY(), p.getPosition().getZ());
	}


}

package io.ruin.model.map.object;

import io.ruin.Server;
import io.ruin.cache.LocType;
import io.ruin.model.WorldObject;
import io.ruin.model.activities.cluescrolls.impl.CrypticClue;
import io.ruin.model.activities.cluescrolls.impl.MapClue;
import io.ruin.model.activities.raids.xeric.chamber.impl.CorruptedScavengerChamber;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.map.ClipUtils;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.object.owned.OwnedObject;
import io.ruin.model.skills.hunter.traps.Trap;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static io.ruin.api.utils.RsAttribute.*;
import static io.ruin.api.utils.RsAttribute.grubChestType;

public class GameObject extends WorldObject {

	/**
	 * Utils
	 */
	public static void forObj(int id, int x, int y, int z, Consumer<GameObject> consumer) {
		// At first I didn't want to have this method,
		// but it's convenient so we don't have to manually check if null.
		GameObject obj = Tile.getObject(id, x, y, z);
		if (obj != null)
			consumer.accept(obj);
	}

	public static GameObject spawn(int id, int x, int y, int z, int type, int direction) {
		return new GameObject(id, x, y, z, type, direction).spawn();
	}

	public static GameObject spawn(int id, Position pos, int type, int direction) {
		return new GameObject(id, pos.getX(), pos.getY(), pos.getZ(), type, direction).spawn();
	}

	public static GameObject spawnUnclipped(int id, int x, int y, int z, int type, int direction) {
		return new GameObject(id, x, y, z, type, direction).skipClipping(true).spawn();
	}

	public static GameObject spawnUnclipped(int id, Position pos, int type, int direction) {
		return new GameObject(id, pos, type, direction).skipClipping(true).spawn();
	}

	public transient int originalId;
	private transient boolean skipClipping, spawned;
	public transient Tile tile;
	public transient Position walkTo;
	public transient Predicate<Position> skipReachCheck;
	public transient ObjectAction[] actions;
	public transient HashMap<Integer, ItemObjectAction> itemActions;
	public transient ItemObjectAction defaultItemAction;
	public transient CrypticClue crypticClue;
	public transient MapClue mapClue;
	public transient GameObject doorReplaced;
	public transient Trap trap; // object attributes when? idk i like to hog memory

	public GameObject(int id, int x, int y, int z, int type, int direction) {
		super(id, type, direction, new Position(x, y, z));
		this.originalId = id;
	}

	public GameObject(int id, Position pos, int type, int direction) {
		this(id, pos.getX(), pos.getY(), pos.getZ(), type, direction);
	}

	public GameObject(int id, Position pos) {
		this(id, pos.getX(), pos.getY(), pos.getZ(), 22, 0);
	}

	public GameObject(int id, int type, Position pos) {
		this(id, pos.getX(), pos.getY(), pos.getZ(), type, 0);
	}

	public int doorCloses() {
		return this.get(DOOR_CLOSE, 0);
	}

	public void doorCloses(int doorCloses) {
		set(DOOR_CLOSE, (Object) doorCloses);
	}

	public boolean depleting() {
		return this.<Boolean>get(DEPLEATING, Boolean.FALSE);
	}

	public void depleting(boolean depleting1) {
		if (!depleting1)
			remove(DEPLEATING);
		else
			set(DEPLEATING, (Object) depleting1);
	}

	public long lastAnimationTick() {
		return get(lastAnimationTick, 0L);
	}

	public void lastAnimationTick(long l) {
		set(lastAnimationTick, (Object) l);
	}

	public int kindling() {
		return get(kindling, 0);
	}

	public void kindling(int kindling1) {
		set(kindling, (Object) kindling1);
	}

	public int doorOriginalId() {
		return get(doorOriginalId, -1);
	}

	public void doorOriginalId(int doorOriginalId1) {
		set(doorOriginalId, (Object) doorOriginalId1);
	}

	public int burnerTime() {
		return get(burnerTime, 0);
	}

	public void burnerTime(int burnerTime1) {
		if (burnerTime1 == 0)
			remove(burnerTime);
		else
			set(burnerTime, (Object) burnerTime1);
	}

	public boolean dummyRemoved() {
		return get(dummyRemoved, false);
	}

	public void dummyRemoved(boolean dummyRemoved1) {
		if (!dummyRemoved1)
			remove(dummyRemoved);
		else
			set(dummyRemoved, (Object) dummyRemoved1);
	}

	public CorruptedScavengerChamber.ChestType grubChestType() {
		return get(grubChestType, null);
	}

	public void grubChestType(CorruptedScavengerChamber.ChestType grubChestType1) {
		if (grubChestType1 == null)
			remove(grubChestType);
		else
			set(grubChestType, grubChestType1);
	}

	public long doorJamEnd() {
		return get(doorJamEnd, 0L);
	}

	public void doorJamEnd(long doorJamEnd1) {
		if (doorJamEnd1 == 0L)
			remove(doorJamEnd);
		else
			set(doorJamEnd, (Object) doorJamEnd1);
	}

	public Position getPosition() {
		return Position.of(x, y, z);
	}

	public Position getCenterPosition() {
		return new Position(
				getPosition().getX() + getDef().xLength / 2,
				getPosition().getY() + getDef().yLength / 2,
				getPosition().getZ());
	}

	public GameObject skipClipping(boolean skipClipping) {
		this.skipClipping = skipClipping;
		return this;
	}

	public GameObject spawn() {
		spawned = true;
		Tile.get(x, y, z, true).addObject(this);
		for (Player player : tile.region.players)
			send(player);
		return this;
	}

	public GameObject remove() {
		setId(-1);
		return this;
	}

	public GameObject restore() {
		setId(originalId);
		return this;
	}

	@Override
	public void setId(int newId) {
		var players = getPosition().getRegion().players;
		if (spawned && newId == -1) {
			// perma deleted, not just setting invisible
			if (tile != null) {
				tile.removeObject(this);
			}
			for (Player player : players)
				sendRemove(player);
		} else {
			clip(true);
			super.setId(newId);
			Tile.get(x, y, z, true).checkActive();
			clip(false);
			for (Player player : players)
				send(player);
		}
	}

	public void clip(boolean remove) {
		if (getId() == -1 || skipClipping)
			return;
		LocType def = getDef();
		if (def == null)
			return;
		var type = getType();
		if (type == 22) {
			if (def.isClippedDecoration()) {
				if (def.clipType == 1) {
					if (remove)
						tile.unflagDecoration();
					else
						tile.flagDecoration();
				}
			}
		} else if (type >= 9) {
			int xLength, yLength;
			if (getDirection() == 1 || getDirection() == 3) {
				xLength = def.yLength;
				yLength = def.xLength;
			} else {
				xLength = def.xLength;
				yLength = def.yLength;
			}
			if (def.clipType != 0) {
				if (remove) {
					ClipUtils.removeClipping(x, y, z, xLength, yLength, def.tall, false);
					if (def.tall)
						ClipUtils.removeClipping(x, y, z, xLength, yLength, true, true);
				} else {
					ClipUtils.addClipping(x, y, z, xLength, yLength, def.tall, false);
					if (def.tall)
						ClipUtils.addClipping(x, y, z, xLength, yLength, true, true);
				}
			}
		} else if (type >= 0 && type <= 3) {
			if (def.clipType != 0) {
				if (remove) {
					ClipUtils.removeVariableClipping(x, y, z, getType(), getDirection(), def.tall, false);
					if (def.tall)
						ClipUtils.removeVariableClipping(x, y, z, getType(), getDirection(), true, true);
				} else {
					ClipUtils.addVariableClipping(x, y, z, getType(), getDirection(), def.tall, false);
					if (def.tall)
						ClipUtils.addVariableClipping(x, y, z, getType(), getDirection(), true, true);
				}
			}
		}
	}

	public void send(Player player) {
		if (getId() != -1)
			sendCreate(player);
		else
			sendRemove(player);
	}

	public void sendCreate(Player player) {
		player.getPacketSender().sendCreateObject(getId(), x, y, z, getType(), getDirection());
	}

	public void sendRemove(Player player) {
		player.getPacketSender().sendRemoveObject(x, y, z, getType(), getDirection());
	}

	public void animate(int id) {
		if (tile == null) {
			return;
		}
		long currentTick = Server.currentTick();
		if (lastAnimationTick() != currentTick) {
			lastAnimationTick(currentTick);
			for (Player player : tile.region.players) {
				player.getPacketSender().sendObjectAnimation(x, y, z, getType(), getDirection(), id);
			}
		}
	}

	public void graphics(int id) {
		graphics(id, 0, 0);
	}

	public void graphics(int id, int height, int delay) {
		for (Player player : tile.region.players)
			player.getPacketSender().sendGraphics(id, height, delay, x, y, z);
	}

	/**
	 * Note: This basically means the object isn't static on the map.
	 */
	public boolean isSpawned() {
		return spawned;
	}

	public boolean isRemoved() {
		return getId() == -1 || tile == null;
	}

	public boolean isCustom() {
		return spawned || getId() != originalId/* || conOwner != -1 */;
	}

	public LocType getDef() {
		return LocType.get(this.getId());
	}

	@Override
	public String toString() {
		return "GameObject[name=" + getDef().name + ", id=" + getId() + ", x=" + x + ", y=" + y + ", z=" + z + "]";
	}

	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}

	public int component1() {
		return getId();
	}

	public int component2() {
		return x;
	}

	public int component3() {
		return y;
	}

	public boolean isOwnedObject() {
		return this instanceof OwnedObject;
	}

	public OwnedObject asOwnedObject() {
		return ((OwnedObject) this);
	}
}

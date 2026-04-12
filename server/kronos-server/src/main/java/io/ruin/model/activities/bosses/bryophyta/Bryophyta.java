package io.ruin.model.activities.bosses.bryophyta;

import io.ruin.cache.ItemID;
import io.ruin.cache.NPCType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.utility.FormatMessage;

public class Bryophyta extends NPCCombat {

	private enum GATE {
		ENTRANCE(32534, 32534, 3174, 9901, 0, 1, false);

		public final int id, replacementId, doorX, doorY, doorZ, direction;
		public final boolean horizontal;

		GATE(int id, int replacementId, int doorX, int doorY, int doorZ, int direction, boolean horizontal) {
			this.id = id;
			this.replacementId = replacementId;
			this.doorX = doorX;
			this.doorY = doorY;
			this.doorZ = doorZ;
			this.direction = direction;
			this.horizontal = horizontal;
		}

		public static final GATE[] VALUES = values();
	}

	private static final int gfx_MageId = 139;
	public static final Projectile mageProjectile = new Projectile(gfx_MageId, 43, 31, 51, 36, 10, 15, 127);
	private final int anim_MageID = 7173;
	public static final int GROWTHLIN = 8194, BRYOPHYTA = 8195;
	private static final int[][] growthlinOffset = {
			{ 0, 1 },
			{ 1, 0 },
			{ -1, 0 },
	};

	private static final Position SPAWN_POSITION = new Position(3223, 9933, 0);

	private boolean growthlinSpawned = false;
	private final NPC[] growthlin = new NPC[3];

	public static void register() {

		for (GATE gate : GATE.VALUES) {
			ObjectAction.register(gate.id, gate.doorX, gate.doorY, gate.doorZ, "open",
					(player, obj) -> createAndEnter(player));
			ObjectAction.register(32534, "check-count", (player, obj) -> {
				player.sendMessage(FormatMessage.sendColoredMessage(
						"You have killed Bryophyta " + player.bryophytaKills.getKills() + " times.",
						FormatMessage.Colors.ASH_BLUE));
			});
			ObjectAction.register(32536, "Take-axe", (player, obj) -> {
				player.getInventory().addOrDrop(ItemID.BRONZE_AXE, 1);
			});
		}
	}

	@Override
	public void init() {
		npc.hitListener = new HitListener()
				.preDefend(this::block)
				.postDefend(this::spawnGrowthlin);
		NPCType.get(GROWTHLIN).ignoreOccupiedTiles = true;
	}

	private void spawnGrowthlin(Hit hit) {
		if (!growthlinSpawned && npc.getHp() <= (75)) {
			growthlinSpawned = true;
			for (int i = 0; i < growthlin.length; i++) {
				growthlin[i] = new NPC(GROWTHLIN).spawn(new Position(target.getPosition().getX() + growthlinOffset[i][0],
						target.getPosition().getY() + growthlinOffset[i][1], target.getPosition().getZ()));
				growthlin[i].getCombat().setTarget(target);
				growthlin[i].face(target);
			}
		}
	}

	@Override
	public void follow() {
		follow(1);
	}

	private void block(Hit hit) {
		for (NPC growthlin : growthlin) {
			if (growthlin != null && !growthlin.isRemoved() && !growthlin.getCombat().isDead()) {
				hit.block();
				break;
			}
		}
	}

	@Override
	public boolean attack() {
		if (target.getPosition().isWithinDistance(npc.getPosition(), 1)) {
			basicAttack();
			return true;
		} else {
			if (npc.getPosition().isWithinDistance(target.getPosition())) {
				mageAttack();
				return true;
			}
		}
		return false;
	}

	@Override
	public void process() {

	}

	private void mageAttack() {
		npc.animate(anim_MageID);
		int delay = mageProjectile.send(npc, target);
		target.hit(new Hit(npc, AttackStyle.MAGIC, null).randDamage(info.max_damage).clientDelay(delay));
	}

	public static void createAndEnter(Player player) {
		DynamicMap map;
		try {
			map = new DynamicMap().build(12955, 1);
		} catch (DynamicMap.DynamicMapBuildException e) {
			player.sendMessage("Unable to create dynamic map.");
			return;
		}

		NPC bryophta = new NPC(BRYOPHYTA).spawn(map.convertX(SPAWN_POSITION.getX()), map.convertY(SPAWN_POSITION.getY()), 0,
				Direction.NORTH, 2);
		map.addNpc(bryophta);
		if (!player.getInventory().contains(ItemID.MOSSY_KEY, 1)) {
			player.sendMessage(
					FormatMessage.sendColoredMessage("You need a mossy key to enter.", FormatMessage.Colors.PURE_RED));
			return;
		}
		player.startEvent(event -> {
			player.lock();
			player.animate(4540);
			player.getPacketSender().fadeOut();
			event.delay(1);
			player.getMovement().teleport(3214, 9937, 0);

			player.getInventory().remove(ItemID.MOSSY_KEY, 1);
			player.face(Direction.EAST);
			player.getPacketSender().fadeIn();
			event.delay(2);
			map.assignListener(player).onExit((p, logout) -> {
				if (logout)
					p.getMovement().teleport(3174, 9900, 0);
				p.deathEndListener = null;
				map.destroy();
			});
			player.unlock();
		});
		ObjectAction exitAction = (p, obj) -> {
			p.startEvent(event -> {
				p.lock(LockType.FULL_DELAY_DAMAGE);
				p.animate(839);
				p.getMovement().force(-3, 0, 0, 0, 0, 60, Direction.WEST);
				player.getPacketSender().fadeOut();
				event.delay(3);
				p.getMovement().teleport(3174, 9900, 0);
				player.face(Direction.SOUTH);
				player.getPacketSender().fadeIn();
				event.delay(1);
				p.unlock();
			});
		};
		ObjectAction.register(32535, "clamber", exitAction);

	}
}

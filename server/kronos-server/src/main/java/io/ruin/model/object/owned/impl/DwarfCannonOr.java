package io.ruin.model.object.owned.impl;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import io.ruin.cache.Color;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.map.route.routes.ProjectileRoute;
import io.ruin.model.object.owned.OwnedObject;
import io.ruin.model.stat.StatType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Andrew on 11/19/2019
 * @ Project norse
 * TODO add support for granite cannonballs
 * TODO add area restrictions
 */
@Slf4j
public class DwarfCannonOr extends OwnedObject {

	public static final String IDENTIFIER = "dwarfCannonOr";
	private static int GRANITE_CANNON_BALL = 21728, CANNON_BALL = 2;
	public static final int BASE = 26520, STAND = 26522, BARRELS = 26524, FURNACE = 26526;
	public static final int[] CANNON_PARTS = {BASE, STAND, BARRELS, FURNACE};
	private static final int[] CANNON_OBJECTS = {43029, 43030, 43031, 43027};
	private static final int SETUP_ANIM = 827;
	private static final int BROKEN_CANNON = 43028;
	private static final int CANNON_RANGE = 12;
	private static int MAX_HIT = 35;
	private static final int DECAY_TIME = 20;
	private static final int BROKEN_TIME = 25;
	private static final int Gfx1 = 9239;
	private static final int Gfx2 = 9240;
	private static final int Gfx3 = 9241;
	private static final int Gfx4 = 9242;
	private static final int Gfx5 = 9243;
	private static final int Gfx6 = 9244;
	private static final int Gfx7 = 9245;
	private static final int Gfx8 = 9246;
	private static final int CANNONBALLS_4 = 4;

	@Getter
	private Stopwatch decayTimer = Stopwatch.createUnstarted();
	@Getter
	@Setter
	private int ammo;
	@Getter
	private CannonStage stage;
	@Getter
	@Setter
	private CannonDirection cannonDirection = CannonDirection.NORTH;

	int filledWith = 0;

	private static final Bounds[] AREA_RESTRICTIONS = {
		new Bounds(3281, 3158, 3304, 3178, -1), //alkarid palace
		new Bounds(2368, 3072, 2431, 3135, -1), //castle wars
		new Bounds(2950, 9800, 3071, 9855, -1), //dwarven mine
		new Bounds(2994, 9698, 3071, 9799, -1), //dwarven mine
		new Bounds(3008, 6016, 3071, 6079, -1), //zalcano
		new Bounds(3229, 10151, 3257, 10187, -1), //revenant caves
		new Bounds(3245, 10136, 3259, 10154, -1), //revenant caves
		new Bounds(3520, 9664, 3583, 9727, -1), //barrows crypt
		new Bounds(1990, 3526, 2112, 3648, -1), //Home
		new Bounds(2628, 2627, 2680, 2683, -1), //Pest control

		new Bounds(3326, 3202, 3392, 3266, -1), //Duel arena
		new Bounds(3349, 3267, 3392, 3325, -1), //Duel arena
		new Bounds(3642, 3204, 3683, 3234, -1), //Ver sinhaza

		new Bounds(3085, 3486, 3100, 3501, -1),


	};

	public DwarfCannonOr(Player owner, int id) {
		super(owner, IDENTIFIER, id, owner.getPosition(), 10, 0);
		this.stage = CannonStage.BASE;
		setCannonDirection(CannonDirection.NORTH);
	}

	@Override
	public void tick() {
		if (decayTimer == null) {
			decayTimer = Stopwatch.createUnstarted();
		}
		if (cannonDirection == null) {
			cannonDirection = CannonDirection.NORTH;
		}
		checkDecayTimer();
		rotate();
	}

	public void fill() {
		int MAX_AMMO = 30;
		switch (getOwner().getSecondaryGroup()) {
			case DONATOR -> MAX_AMMO = 35;
			case SUPER_DONATOR -> MAX_AMMO = 40;
			case ELITE_DONATOR -> MAX_AMMO = 50;
			case NOBLE_DONATOR -> MAX_AMMO = 60;
			case GOLD_DONATOR -> MAX_AMMO = 70;
			case PLATINUM_DONATOR -> MAX_AMMO = 80;
			case LEGENDARY_DONATOR -> MAX_AMMO = 90;
			case SUPREME_DONATOR -> MAX_AMMO = 100;
		}
		if (getAmmo() > 0) {
			switch (filledWith) {
				case 2:
					if (getAmmo() < MAX_AMMO && getOwner().getInventory().hasAtLeastOneOf(CANNON_BALL)) {
						int needed = MAX_AMMO - getAmmo();
						int available = getOwner().getInventory().getAmount(CANNON_BALL);

						if (needed > available)
							needed = available;

						if (needed > 0) {
							getOwner().getInventory().remove(CANNON_BALL, needed);
							getOwner().sendMessage("You load the cannon with " + (needed == 1 ? "one" : needed) + " cannonball" + ((needed > 1) ? "s." : "."));
							setAmmo(getAmmo() + needed);
						}
						filledWith = 2;
						setStage(CannonStage.FIRING, false);
					} else getOwner().sendMessage("You don't have any cannonballs to fill your cannon with.");
					break;
				case 21728:
					if (getAmmo() < MAX_AMMO && getOwner().getInventory().hasAtLeastOneOf(21728)) {
						int needed = MAX_AMMO - getAmmo();
						int available = getOwner().getInventory().getAmount(21728);

						if (needed > available)
							needed = available;

						if (needed > 0) {
							getOwner().getInventory().remove(21728, needed);
							getOwner().sendMessage("You load the cannon with " + (needed == 1 ? "one" : needed) + " granite cannonball" + ((needed > 1) ? "s." : "."));
							setAmmo(getAmmo() + needed);
						}
						filledWith = 21728;
						setStage(CannonStage.FIRING, false);
					} else getOwner().sendMessage("You don't have any cannonballs to fill your cannon with.");
					break;
			}
		} else {
			if (getOwner().getInventory().hasAtLeastOneOf(ItemID.GRANITE_CANNONBALL)) {
				int needed = MAX_AMMO - getAmmo();
				int available = getOwner().getInventory().getAmount(ItemID.GRANITE_CANNONBALL);

				if (needed > available)
					needed = available;

				if (needed > 0) {
					getOwner().getInventory().remove(ItemID.GRANITE_CANNONBALL, needed);
					getOwner().sendMessage("You load the cannon with " + (needed == 1 ? "one" : needed) + " granite cannonball" + ((needed > 1) ? "s." : "."));
					setAmmo(getAmmo() + needed);
				}
				filledWith = ItemID.GRANITE_CANNONBALL;
				setStage(CannonStage.FIRING, false);
			} else if (getAmmo() < MAX_AMMO && getOwner().getInventory().hasAtLeastOneOf(CANNON_BALL)) {
				int needed = MAX_AMMO - getAmmo();
				int available = getOwner().getInventory().getAmount(CANNON_BALL);

				if (needed > available)
					needed = available;

				if (needed > 0) {
					getOwner().getInventory().remove(CANNON_BALL, needed);
					getOwner().sendMessage("You load the cannon with " + (needed == 1 ? "one" : needed) + " cannonball" + ((needed > 1) ? "s." : "."));
					setAmmo(getAmmo() + needed);
				}
				filledWith = 2;
				setStage(CannonStage.FIRING, false);
			} else getOwner().sendMessage("You don't have any cannonballs to fill your cannon with.");
		}

	}

	public void pickup() {
		int spaces = 4;
//        if (getAmmo() > 0) {
//            spaces += getOwner().getInventory().hasAtLeastOneOf(CANNON_BALL) ? 0 : 1;
//        }
		if (getAmmo() > 0) {
			spaces += getOwner().getInventory().hasAtLeastOneOf(GRANITE_CANNON_BALL) ? 0 : 1;
		}
		if (getOwner().getInventory().hasFreeSlots(spaces)) {
			IntStream.of(getStage().parts).mapToObj(Item::new).forEach(getOwner().getInventory()::add);
//            if (getAmmo() > 0)
//                getOwner().getInventory().add(CANNON_BALL, getAmmo());
			if (getAmmo() > 0)
				getOwner().getInventory().add(GRANITE_CANNON_BALL, getAmmo());
			getOwner().animate(SETUP_ANIM);
			destroy();
			getOwner().sendMessage("You pick up the cannon.");
		} else {
			getOwner().sendMessage("You don't have enough inventory space to do that.");
		}
	}

	public void rotate() {
//        if (getStage().equals(CannonStage.FIRING) || getStage().equals(CannonStage.BROKEN)) {
//            if (!cannonDirection.equals(CannonDirection.NORTH)) {
//                animate(cannonDirection.getAnimationId());
//                cannonDirection = cannonDirection.next();
//                return;
//            }
//        }
		boolean ownerOnline = getOwnerOpt().isPresent();
		Optional<NPC> target = Optional.empty();
		if (ownerOnline && getStage().equals(CannonStage.FIRING)) {
          /*  if (!getOwner().inMulti() && Objects.nonNull(getOwner().getCombat().getTarget())) {
                Entity combatTarget = getOwner().getCombat().getTarget();
                target = Optional.ofNullable(combatTarget.npc);
                if (target.isPresent()) {
                    if (!cannonDirection.validArea(getPosition(), target.get().getPosition().center(target.get().getSize()))) {
                        target = Optional.empty();
                    }
                }
            } else {*/
			Position pos = getCorrectedPosition(getPosition());
			Position center = pos.translate(1, 1, 0);
			target = World.npcsNonNull().
				filter(npc -> npc.getPosition().isWithinDistance(pos, CANNON_RANGE)).
				filter(npc -> npc.getDef().combatLevel > 0 && npc.getHp() > 0).
				filter(npc -> !npc.getCombat().isDead()).
				filter(npc -> cannonDirection.validArea(center, npc.getPosition())).
				filter(npc -> ProjectileRoute.allow(pos.getX(),
					pos.getY(), pos.getZ(), 1,
					npc.getPosition().getX(), npc.getPosition().getY(), npc.getSize())).
				findAny();
			//  }


		}

		if (ownerOnline && getStage().equals(CannonStage.FIRING)) {
			animate(cannonDirection.getAnimationId());
			cannonDirection = cannonDirection.next();
		} else if (getStage().equals(CannonStage.FURNACE) && getAmmo() <= 0 && getCannonDirection() != CannonDirection.NORTH) {
			animate(cannonDirection.getAnimationId());
			cannonDirection = cannonDirection.next();
		}

		target.ifPresent(npc -> {
			if (!getOwner().getCombat().multiCheck(npc, false)) {
				return;
			}
			Projectile cannonBall = new Projectile(2018, 50, 50, 0, 0, 10, 0, 64);
			if (filledWith == 2)
				MAX_HIT = 35;
			else
				MAX_HIT = 45;
			Hit hit = new Hit(getOwner(), AttackStyle.CANNON, AttackType.RAPID_RANGED).randDamage(MAX_HIT).ignoreDefence();
			int delay = cannonBall.send(getCorrectedPosition(getPosition()), npc);
			int tickDelay = 0;//(((50 * delay)) / 600);
			npc.hit(hit.delay(tickDelay));
			if (hit.damage > 0) {
				getOwner().getStats().addXp(StatType.Ranged, Math.ceil(hit.damage / 2), true);
			}
			setAmmo(getAmmo() - 1);
			if (getAmmo() <= 0) {
				getOwner().sendMessage("Your cannon is out of ammo!");
				setStage(CannonStage.FURNACE, true);
			}
		});

	}

	public void checkDecayTimer() {

	}

	public boolean needsDecaying() {
		return decayTimer.elapsed(TimeUnit.MINUTES) > DECAY_TIME && !getStage().equals(CannonStage.BROKEN);
	}

	public boolean needsDestroyed() {
		return decayTimer.elapsed(TimeUnit.MINUTES) > BROKEN_TIME && getStage().equals(CannonStage.BROKEN);
	}

	public CannonStage incrementSetupStage() {
		setStage(this.stage.next(), true);
		return stage;
	}

	public boolean isValidSpot() {
		Position pos = getPosition();
		List<Position> positions = Lists.newArrayList();
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				positions.add(new Position(pos.getX() + x, pos.getY() + y, pos.getZ()));
			}
		}
		return positions.stream().allMatch(position -> {
			boolean free = Tile.get(position, true).isTileFreeCheckDecor();
			for (int x = -1; x < 1; x++) {
				for (int y = -1; y < 1; y++) {
					Tile tile = Tile.get(position.getX() + x, position.getY() + y, position.getZ(), true);
					if (!tile.isTileFreeCheckDecor())
						return false;
				}
			}
			return free;
		});
	}

	public boolean hasParts() {
		return IntStream.of(CANNON_PARTS).allMatch(getOwner().getInventory()::contains);
	}

	public boolean isPart(int id) {
		return IntStream.of(CANNON_PARTS).anyMatch(part -> part == id);
	}

	public void addPart(Item item) {
		if (!isPart(item.getId())) {
			getOwner().sendMessage("This isn't a cannon part.");
			return;
		}
		getOwner().startEvent(e -> {
			if (getStage().equals(CannonStage.FURNACE) || getStage().equals(CannonStage.FIRING) || getStage().equals(CannonStage.BROKEN)) {
				getOwner().sendMessage("You can't add anymore parts your cannon.");
				return;
			}
			for (int index = getStage().ordinal(); index < 4; index++) {
				getOwner().face(this);
				Item required = new Item(CANNON_PARTS[index], 1);
				if (!getOwner().getInventory().contains(required)) {
					getOwner().sendMessage("You don't have the required parts needed to complete your cannon.");
					return;
				}
				getOwner().animate(SETUP_ANIM);
				incrementSetupStage();
				String name = required.getDef().name.toLowerCase().replace("cannon", "").trim();
				getOwner().getInventory().remove(required);
				getOwner().sendMessage("You add the " + name + ".");
				e.delay(2);
			}
		});
	}

	private static Position getCorrectedPosition(Position pos) {
		return pos.copy().translate(1, 1, 0);
	}

	public void setStage(CannonStage stage, boolean changeId) {
		this.stage = stage;
		if (changeId)
			setId(stage.getObjectId());
	}

	public boolean handleAreaRestriction() {
		if (getOwner().pestGame != null) {
			getOwner().sendMessage("You can't place a cannon in Pest Control.");
			return false;
		}
		if (getOwner().currentInstance != null) {
			getOwner().sendMessage("You can't place your cannon down in an instance.");
			return false;
		}
		if (getOwner().raidsParty != null) {
			getOwner().sendMessage("You can't place a cannon in Chambers of Xeric.");
			return false;
		}
		if (getOwner().fightCaves != null) {
			getOwner().sendMessage("You can't place a cannon in Fight Caves.");
			return false;
		}
		if (getOwner().inferno != null) {
			getOwner().sendMessage("You can't place a cannon in Inferno.");
			return false;
		}
		if (getOwner().getCurrentHouse() != null) {
			getOwner().sendMessage("You can't place a cannon in Player owned house.");
			return false;
		}
		if (getOwner().getBounds().intersects(new Bounds(2944, 4736, 3135, 4927, 0))) {
			getOwner().sendMessage("That horrible slime on the ground makes this area unsuitable for a cannon.");
			return false;
		}
		if (getOwner().getBounds().intersects(new Bounds(2999, 3501, 3034, 3523, 0))) {
			getOwner().sendMessage("It is not permitted to set up a cannon this close to the Dwarf Black Guard.");
			return false;
		}
		if (getOwner().getBounds().intersects(new Bounds(2688, 9984, 2815, 10047, 0))) {
			getOwner().sendMessage("The humid air in these tunnels won't do your cannon any good!");
			return false;
		}
		if (getOwner().getBounds().intersects(new Bounds(3138, 3468, 3189, 3516, 0))) {
			getOwner().sendMessage("The Grand Exchange staff prefer not to have heavy artillery operated around their premises.");
			return false;
		}
		if (getOwner().getBounds().intersects(new Bounds(3136, 4544, 3199, 4671, 0))) {
			getOwner().sendMessage("This temple is ancient and would probably collapse if you started firing a cannon.");
			return false;
		}
		if (getOwner().getBounds().intersects(new Bounds(1280, 9920, 1343, 9983, 0))) {
			getOwner().sendMessage("This temple is ancient and would probably collapse if you started firing a cannon.");
			return false;
		}
		boolean normal = Stream.of(AREA_RESTRICTIONS).anyMatch(bounds -> getOwner().getBounds().intersects(bounds));
		if (normal) {
			getOwner().sendMessage("You can't deploy a cannon here.");
			return false;
		}
		return true;
	}

	public static void register() {
		ItemAction.registerInventory(BASE, 1, (player, item) -> {
			World.doCannonReclaim(player.getName(), (reclaim) -> {
				if (reclaim) {
					player.sendMessage("You can't deploy this cannon, you have one you need to reclaim.");
				} else {

					DwarfCannonOr cannon = new DwarfCannonOr(player, CANNON_OBJECTS[0]);

					if (!cannon.hasParts()) {
						player.sendMessage("You don't have all the parts to build your cannon.");
						return;
					}
					if (!cannon.isValidSpot()) {
						player.sendMessage("There's not enough room to setup your cannon here.");
						return;
					}

					if (!cannon.handleAreaRestriction()) {
						return;
					}

					World.startEvent(event -> {

						if (World.getOwnedObject(player, DwarfCannonOr.IDENTIFIER) != null) {
							player.sendMessage("You already have a cannon deployed.");
							return;
						}

						World.registerOwnedObject(cannon);
						player.face(cannon);

						cannon.getDecayTimer().start();

						cannon.spawn();
						player.animate(SETUP_ANIM);
						player.getInventory().remove(CANNON_PARTS[cannon.getStage().ordinal()], 1);
						player.sendMessage("You place down the base.");
						player.lock(LockType.FULL_REGULAR_DAMAGE);
						event.delay(2);

						for (int index = 0; index < 3; index++) {
							player.face(cannon);
							player.animate(SETUP_ANIM);
							cannon.incrementSetupStage();
							Item cannonPart = new Item(CANNON_PARTS[cannon.getStage().ordinal()], 1);
							String name = cannonPart.getDef().name.toLowerCase().replace("cannon", "").trim();
							player.getInventory().remove(CANNON_PARTS[cannon.getStage().ordinal()], 1);
							player.sendMessage("You add the " + name + ".");
							event.delay(2);
						}

						cannon.fill();
						player.unlock();

					}).setCancelCondition(() -> !cannon.getOwnerOpt().isPresent() || cannon.getOwner().getCombat().isDead());
				}
			});
		});
//        for (int partId : CANNON_PARTS) {
//            for (int objectId : CANNON_OBJECTS) {
//                ItemObjectAction.register(partId, objectId, (player, item, object) -> {
//                    if (object.isOwnedObject() && object.asOwnedObject().isOwner(player)) {
//                        ((DwarfCannon) object).addPart(item);
//                    }
//                });
//            }
//        }
		ItemObjectAction.register(21728, 43027, (player, item, object) -> {
			if (object.isOwnedObject() && object.asOwnedObject().isOwner(player)) {
				((DwarfCannonOr) object).fill();
			} else {
				player.sendMessage("You're not the owner of this cannon.");
			}
		});
		ObjectAction.register(43029, 1, (player, object) -> {
			if (object.isOwnedObject() && object.asOwnedObject().isOwner(player)) {
				((DwarfCannonOr) object).pickup();
			} else {
				player.sendMessage("You're not the owner of this cannon.");
			}
		});
		ObjectAction.register(43030, 1, (player, object) -> {
			if (object.isOwnedObject() && object.asOwnedObject().isOwner(player)) {
				((DwarfCannonOr) object).pickup();
			} else {
				player.sendMessage("You're not the owner of this cannon.");
			}
		});
		ObjectAction.register(43031, 1, (player, object) -> {
			if (object.isOwnedObject() && object.asOwnedObject().isOwner(player)) {
				((DwarfCannonOr) object).pickup();
			} else {
				player.sendMessage("You're not the owner of this cannon.");
			}
		});
		ObjectAction.register(43027, 2, (player, object) -> {
			if (object.isOwnedObject() && object.asOwnedObject().isOwner(player)) {
				((DwarfCannonOr) object).pickup();
			} else {
				player.sendMessage("You're not the owner of this cannon.");
			}
		});
		ObjectAction.register(43027, 1, (player, object) -> {
			if (object.isOwnedObject() && object.asOwnedObject().isOwner(player)) {
				((DwarfCannonOr) object).fill();
			} else {
				player.sendMessage("You're not the owner of this cannon.");
			}
		});
		//Repairing
		ObjectAction.register(43028, 1, (player, object) -> {
			if (object.isOwnedObject() && object.asOwnedObject().isOwner(player)) {
				DwarfCannonOr cannon = ((DwarfCannonOr) object);
				player.startEvent((e) -> {
					cannon.getDecayTimer().reset();
					cannon.getDecayTimer().start();
					player.animate(3684);
					e.delay(2);
					cannon.setStage(CannonStage.FIRING, true);
				});
			} else {
				player.sendMessage("You're not the owner of this cannon.");
			}
		});
		ObjectAction.register(43027, 3, (player, object) -> {
			if (!object.isOwnedObject()) {
				return;
			}
			if (object.asOwnedObject().isOwner(player)) {
				DwarfCannonOr cannon = ((DwarfCannonOr) object);
				if (player.getInventory().hasFreeSlots(1) || player.getInventory().hasAtLeastOneOf(GRANITE_CANNON_BALL)) {

//                    if (cannon.getAmmo() > 0) {
//                        player.getInventory().add(CANNON_BALL, cannon.getAmmo());
//                        player.sendMessage("You unload your cannon and receive Cannonball x " + cannon.getAmmo());
//                        cannon.setAmmo(0);
//                        cannon.setStage(CannonStage.FURNACE, false);
//                    }
					if (cannon.getAmmo() > 0) {
						player.getInventory().add(GRANITE_CANNON_BALL, cannon.getAmmo());
						player.sendMessage("You unload your cannon and receive Cannonball x " + cannon.getAmmo());
						cannon.setAmmo(0);
						cannon.setStage(CannonStage.FURNACE, false);
					}
				} else {
					player.sendMessage("You don't have enough inventory space to do that.");
				}
			} else {
				player.sendMessage("You're not the owner of this cannon.");
			}
		});
		LoginListener.register(player -> {
			boolean reclaim = player.lostCannon == true;
			if (reclaim)
				player.sendMessage(Color.RED.wrap("Your cannon has been destroyed, you can reclaim it by typing ::reclaimcannon."));
		});
	}

	@Getter
	private enum CannonStage {

		BASE(43029, DwarfCannonOr.BASE),
		STAND(43030, DwarfCannonOr.BASE, DwarfCannonOr.STAND),
		BARREL(43031, DwarfCannonOr.BASE, DwarfCannonOr.STAND, DwarfCannonOr.BARRELS),
		FURNACE(43027, DwarfCannonOr.CANNON_PARTS),
		FIRING(43027, DwarfCannonOr.CANNON_PARTS),
		BROKEN(43028, DwarfCannonOr.CANNON_PARTS);

		private int objectId;
		private int[] parts;

		CannonStage(int objectId, int... parts) {
			this.objectId = objectId;
			this.parts = parts;
		}

		public static CannonStage forId(int objectId) {
			for (CannonStage stage : values()) {
				if (stage.getObjectId() == objectId)
					return stage;
			}
			return null;
		}

		public CannonStage next() {
			return values()[forId(objectId).ordinal() + 1];
		}

	}

	@Getter
	@RequiredArgsConstructor
	public enum CannonDirection {

		NORTH(0, Gfx1),
		NORTH_EAST(1, Gfx2),
		EAST(2, Gfx3),
		SOUTH_EAST(3, Gfx4),
		SOUTH(4, Gfx5),
		SOUTH_WEST(5, Gfx6),
		WEST(6, Gfx7),
		NORTH_WEST(7, Gfx8);

		public static CannonDirection forId(int direction) {
			for (CannonDirection facingState : CannonDirection.VALUES) {
				if (facingState.getDirection() == direction) {
					return facingState;
				}
			}
			return CannonDirection.NORTH;
		}

		public static final CannonDirection[] VALUES = values();

		public CannonDirection next() {
			return forId(direction + 1);
		}

		public boolean validArea(Position center, Position location) {
			switch (this) {
				case NORTH:
					return (location.getY() > center.getY() && location.getX() >= center.getX() - 1 && location.getX() <= center.getX() + 1);
				case NORTH_EAST:
					return (location.getX() >= center.getX() + 1 && location.getY() >= center.getY() + 1);
				case EAST:
					return (location.getX() > center.getX() && location.getY() >= center.getY() - 1 && location.getY() <= center.getY() + 1);
				case SOUTH_EAST:
					return (location.getY() <= center.getY() - 1 && location.getX() >= center.getX() + 1);
				case SOUTH:
					return (location.getY() < center.getY() && location.getX() >= center.getX() - 1 && location.getX() <= center.getX() + 1);
				case SOUTH_WEST:
					return (location.getX() <= center.getX() - 1 && location.getY() <= center.getY() - 1);
				case WEST:
					return (location.getX() < center.getX() && location.getY() >= center.getY() - 1 && location.getY() <= center.getY() + 1);
				case NORTH_WEST:
					return (location.getX() <= center.getX() - 1 && location.getY() >= center.getY() + 1);
				default:
					return false;
			}
		}

		private final int direction;
		private final int animationId;

	}

}

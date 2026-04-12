package io.ruin.model.activities.bosses.araxxor;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.SecuringTheBag;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.handlers.EquipmentStats;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.Tile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Broadcast;

import java.util.ArrayList;
import java.util.List;

public class Araxxor extends NPCCombat {

	List<Position> venomPools = new ArrayList<>();
	List<GameObject> acidPoolsObjects = new ArrayList<>();
	List<NPC> spiders = new ArrayList<>();
	List<NPC> spiderEggs = new ArrayList<>();
	List<Position> eggSpawns = new ArrayList<>();
	List<GameObject> eggObjects = new ArrayList<>();

	private static final Projectile RANGED_PROJECTILE = new Projectile(1622, 60, 31, 35, 35, 10, 0, 32);
	private static final int RANGED_ANIM = 11484;

	private static final Projectile MAGIC_PROJECTILE = new Projectile(1622, 60, 31, 25, 35, 10, 0, 32);
	private static final int MAGIC_ANIM = 11484;

	private static final Projectile ACID_POOL_PROJECTILE = new Projectile(1481, 25 + 10, 0, 30 + 20, 180 - 30, 0, 55,
			255);
	private static final Projectile ACID_STRIKE_PROJECTILE = new Projectile(1481, 25 + 10, 0, 30 + 20, 180 - 30, 0, 55,
			255);

	boolean enraged = false;

	List<NPC> mirrorbacks = new ArrayList<>();

	@Override
	public void init() {
		eggSpawns.add(0,
				new Position(npc.getPosition().getRegion().baseX + 58, npc.getPosition().getRegion().baseY + 16, 0));
		eggSpawns.add(1,
				new Position(npc.getPosition().getRegion().baseX + 55, npc.getPosition().getRegion().baseY + 12, 0));
		eggSpawns.add(2,
				new Position(npc.getPosition().getRegion().baseX + 45, npc.getPosition().getRegion().baseY + 13, 0));
		eggSpawns.add(3,
				new Position(npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 26, 0));
		eggSpawns.add(4,
				new Position(npc.getPosition().getRegion().baseX + 46, npc.getPosition().getRegion().baseY + 33, 0));
		eggSpawns.add(5,
				new Position(npc.getPosition().getRegion().baseX + 54, npc.getPosition().getRegion().baseY + 33, 0));
		eggSpawns.add(6,
				new Position(npc.getPosition().getRegion().baseX + 59, npc.getPosition().getRegion().baseY + 31, 0));
		eggSpawns.add(7,
				new Position(npc.getPosition().getRegion().baseX + 39, npc.getPosition().getRegion().baseY + 19, 0));
		eggSpawns.add(8,
				new Position(npc.getPosition().getRegion().baseX + 41, npc.getPosition().getRegion().baseY + 32, 0));
		spawnEggs();
		npc.getDef().occupyTiles = false;
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDamage((hit) -> {
			if (hit.attackStyle != null) {
				if (!hit.attackStyle.isMelee()) {
					hit.attacker.player.sendMessage("Only melee attacks can deal damage to Araxxor.");
					hit.block();
				}
			}
			if (!mirrorbacks.isEmpty() && hit.damage > 0) {
				for (int i = mirrorbacks.size() - 1; i >= 0; i--) {
					int damageToMirrorback = hit.damage / 4;
					hit.damage -= damageToMirrorback;
					if (target == null)
						break;
					if (mirrorbacks.get(i).getPosition().distance(target.getPosition()) < 7) {
						mirrorbacks.get(i).hit(new Hit(hit.attacker).fixedDamage(damageToMirrorback));
					}
					if (mirrorbacks.get(i).getHp() < 1) {
						mirrorbacks.remove(i);
					}
				}
			}
		});
		npc.deathEndListener = (entity, killer, killHit) -> onDeath(killer.player);

	}

	boolean canAttack = true;

	private void postDamage(Hit hit) {
		int healthPercentage = npc.getHp() * 100 / npc.getMaxHp();
		if (healthPercentage <= 25 && !enraged) {
			canAttack = false;
			npc.animate(11488);
			npc.getCombat().getInfo().attack_ticks = 4;
			npc.getCombat().getStat(StatType.Defence).alter(npc.getCombat().getStat(StatType.Defence).currentLevel + 35);
			npc.getCombat().getStat(StatType.Magic).alter(npc.getCombat().getStat(StatType.Magic).currentLevel + 28);
			npc.getCombat().getStat(StatType.Ranged).alter(npc.getCombat().getStat(StatType.Ranged).currentLevel + 31);
			World.startEvent(e -> {
				e.delay(3);
				canAttack = true;
				enraged = true;
			});
		}
	}

	@Override
	public void follow() {
		follow(0);
	}

	int attacks = 0;
	int attacksUntilEgg = 4;
	boolean timerStarted = false;

	@Override
	public boolean attack() {
		if (npc.getId() == 13669 || npc.isHidden() || !canAttack)
			return true;
		if (!timerStarted) {
			timerStarted = true;
			target.player.araxxorTimer = new ActivityTimer();
		}
		if (attacksUntilEgg-- == 0) {
			attacksUntilEgg = 4;
			hatchEgg();
		}
		if (attacks == 7) {
			attacks = 0;
			if (Random.get(1) == 0) {
				npc.animate(11476);
				acidDisperse(target.player);
				return true;
			} else {
				acidDrip(target.player);
				return true;
			}
		}
		if (withinDistance(1)) {
			if (!enraged)
				meleeAttack();
			else
				cleaveAttack(target.player);
		} else {
			int magicDefence = target.player.getEquipment().bonuses[EquipmentStats.MAGIC_DEFENCE];
			int rangedDefence = target.player.getEquipment().bonuses[EquipmentStats.RANGE_DEFENCE];
			if (magicDefence > rangedDefence) {
				magicAttack();
			} else {
				rangedAttack();
			}
		}
		attacks++;
		return true;
	}

	private void meleeAttack() {
		npc.animate(11480);
		int maxHit = info.max_damage;
		if (target.isPlayer() && target.player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE))
			maxHit = 12;
		target.hit(new Hit().randDamage(maxHit));
	}

	private void rangedAttack() {
		npc.animate(RANGED_ANIM);
		int delay = RANGED_PROJECTILE.send(npc, target);
		Hit hit = new Hit(npc, AttackStyle.RANGED)
				.randDamage(info.max_damage)
				.clientDelay(delay);
		target.hit(hit);
	}

	private void magicAttack() {
		npc.animate(MAGIC_ANIM);
		int delay = MAGIC_PROJECTILE.send(npc, target);
		Hit hit = new Hit(npc, AttackStyle.MAGIC)
				.randDamage(info.max_damage)
				.clientDelay(delay);
		target.hit(hit);
	}

	private void cleaveAttack(Player player) {
		final Position[] centrePos = {player.getPosition().copy()};

		List<Position> impactPositions = new ArrayList<>();
		npc.animate(11487);
		npc.forceText("Skreee!");
		impactPositions.add(centrePos[0]);
		Direction direction = Direction.getDirection(npc.getCentrePosition(), player.getPosition());
		int deltaX = centrePos[0].getX() - npc.getCentrePosition().getX();
		int deltaY = centrePos[0].getY() - npc.getCentrePosition().getY();

		World.startEvent(e -> {
			if (direction == Direction.NORTH || direction == Direction.SOUTH || (deltaX == 2 && deltaY == 4)
					|| (deltaX == 1 && deltaY == 4)
					|| (deltaX == -1 && deltaY == 4) || (deltaX == -2 && deltaY == -4) || (deltaX == -1 && deltaY == -4)
					|| (deltaX == 1 && deltaY == -4)) {
				impactPositions.add(new Position(centrePos[0].getX() + 1, centrePos[0].getY(), centrePos[0].getZ()));
				impactPositions.add(new Position(centrePos[0].getX() - 1, centrePos[0].getY(), centrePos[0].getZ()));
			} else if (direction == Direction.EAST || direction == Direction.WEST || (deltaX == 4 && deltaY == -2)
					|| (deltaX == 4 && deltaY == -1)
					|| (deltaX == 4 && deltaY == 1) || (deltaX == -4 && deltaY == -3) || (deltaX == -4 && deltaY == 2)
					|| (deltaX == -4 && deltaY == 1)
					|| (deltaX == -4 && deltaY == -1)) {
				impactPositions.add(new Position(centrePos[0].getX(), centrePos[0].getY() + 1, centrePos[0].getZ()));
				impactPositions.add(new Position(centrePos[0].getX(), centrePos[0].getY() - 1, centrePos[0].getZ()));
			} else if (direction == Direction.SOUTH_EAST) {
				centrePos[0] = new Position(npc.getCentrePosition().getX() + 2, npc.getCentrePosition().getY() - 4,
						npc.getPosition().getZ());
				impactPositions.add(centrePos[0]);
			} else if (direction == Direction.SOUTH_WEST) {
				centrePos[0] = new Position(npc.getCentrePosition().getX() - 3, npc.getCentrePosition().getY() - 4,
						npc.getPosition().getZ());
				impactPositions.add(centrePos[0]);
			} else if (direction == Direction.NORTH_EAST) {
				centrePos[0] = new Position(npc.getCentrePosition().getX() + 2, npc.getCentrePosition().getY() + 4,
						npc.getPosition().getZ());
				impactPositions.add(centrePos[0]);
			} else if (direction == Direction.NORTH_WEST) {
				centrePos[0] = new Position(npc.getCentrePosition().getX() + 4, npc.getCentrePosition().getY() + 3,
						npc.getPosition().getZ());
				impactPositions.add(centrePos[0]);
			}
			e.delay(2);
			venomPools.addAll(impactPositions);
			for (NPC n : npc.localNpcs()) {
				for (Position pos : impactPositions) {
					if (n.getPosition().distance(pos) < 1) {
						npc.hit(new Hit().randDamage(info.max_damage));
					}
				}
			}
			for (Player p : npc.localPlayers()) {
				for (Position pos : impactPositions) {
					if (p.getPosition().distance(pos) < 1) {
						p.hit(new Hit().randDamage(info.max_damage));
					}
				}
			}
		});
	}

	private void acidDrip(Player player) {
		player.sendMessage(Color.RED.wrap("You have been soaked in venom!"));
		npc.animate(11477);
		World.startEvent(e -> {
			e.setCancelCondition(() -> player == null || npc == null || npc.getHp() < 1 || player.getHp() < 1
					|| player.getPosition().regionId() != npc.getPosition().regionId());
			for (int i = 0; i < 6; i++) {
				e.delay(1);
				venomPools.add(player.getPosition().copy());
			}
		});
	}

	private void acidDisperse(Player player) {
		List<Position> positions = new ArrayList<>();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				positions.add(new Position(player.getPosition().getX() + x, player.getPosition().getY() + y,
						player.getPosition().getZ()));
			}
		}
		for (int i = 0; i < 9; i++) {
			Position pos = Random.get(positions);
			positions.remove(pos);
			World.startEvent(e -> {
				int delay = ACID_POOL_PROJECTILE.send(npc, pos.getX(), pos.getY());
				e.delay(World.getTicks(delay) + 1);
				venomPools.add(pos);
			});
		}
	}

	private void onDeath(Player player) {
		acidPoolsObjects.forEach(GameObject::remove);
		acidPoolsObjects.clear();
		venomPools.clear();
		spiders.forEach(NPC::remove);
		spiders.clear();
		eggObjects.forEach(GameObject::remove);
		eggObjects.clear();
		spiderEggs.forEach(NPC::remove);
		spiderEggs.clear();
		mirrorbacks.forEach(NPC::remove);
		mirrorbacks.clear();
		enraged = false;
		npc.transform(13669);
		if (player.araxxorTimer != null)
			player.araxxorBestTime = player.araxxorTimer.stop(player, player.araxxorBestTime);
	}

	@Override
	public void process() {
		if (npc.isHidden())
			return;
		for (Position pos : venomPools) {
			if (Tile.getObject(54148, pos.getX(), pos.getY(), 0) == null) {
				GameObject pool = new GameObject(54148, pos, 10, 0).spawn();
				acidPoolsObjects.add(pool);
			}
			npc.localPlayers().forEach(p -> {
				World.startEvent(event -> {
					event.delay(1);
					if (p.getPosition().distance(pos) < 1 && npc.getHp() > 0) {
						p.hit(new Hit(HitType.VENOM).randDamage(9, 12));
					}
				});
			});
		}
	}

	final int WHITE_EGG = 13670;
	final int GREEN_EGG = 13674;
	final int RED_EGG = 13672;

	final int GREEN_SPIDER = 13675;
	final int WHITE_SPIDER = 13671;
	final int RED_SPIDER = 13673;

	int getEggFromPattern(int pattern) {
		return switch (pattern) {
			case 0 -> RED_EGG;
			case 1 -> WHITE_EGG;
			case 2 -> GREEN_EGG;
			default -> -1;
		};
	}

	int getSpiderIdFromEgg(int egg) {
		return switch (egg) {
			case GREEN_EGG -> GREEN_SPIDER;
			case WHITE_EGG -> WHITE_SPIDER;
			case RED_EGG -> RED_SPIDER;
			default -> -1;
		};
	}

	private void spawnEggs() {
		int pattern = Random.get(2);
		for (int i = 0; i < 9; i++) {
			if (pattern > 2)
				pattern = 0;
			int egg = getEggFromPattern(pattern);
			NPC eggNPC = new NPC(egg).spawn(eggSpawns.get(i));
			eggNPC.getCombat().setAllowRetaliate(false);
			spiderEggs.add(eggNPC);
			pattern++;
		}
	}

	private void hatchEgg() {
		if (spiderEggs.isEmpty())
			return;
		spiderEggs.getFirst().getCombat().setAllowRetaliate(true);
		spiderEggs.getFirst().transform(spiderEggs.getFirst().getId() + 1);
		spiderEggs.getFirst().animate(11503);
		spiderEggs.getFirst().getCombat().setTarget(target);
		spiders.add(spiderEggs.getFirst());
		if (spiderEggs.getFirst().getId() == WHITE_SPIDER)
			mirrorbacks.add(spiderEggs.getFirst());
		spiderEggs.removeFirst();
	}

	private void fireVenomBall(Player player) {
		Direction direction = Direction.getDirection(npc.getPosition(), player.getPosition());
		switch (direction) {
			case NORTH -> {
				Position startPosition = new Position(player.getPosition().getX(), npc.getCentrePosition().getY() + 2,
						npc.getCentrePosition().getZ());
				Position targetPos = new Position(player.getPosition().getX(), npc.getCentrePosition().getY() + 14,
						npc.getCentrePosition().getZ());
				int delay = ACID_STRIKE_PROJECTILE.send(npc, targetPos.getX(), targetPos.getY());
				World.startEvent(e -> {
					Position currentPosition = startPosition.copy();
					for (int i = 0; i < 6; i++) {
						e.delay(1);
						for (int j = 0; j < 2; j++) {
							if (player.getPosition().distance(currentPosition) < 2)
								player.hit(new Hit(HitType.VENOM).randDamage(9, 12));
							venomPools.add(currentPosition);
							currentPosition = new Position(currentPosition.getX(), currentPosition.getY() + 1,
									currentPosition.getZ());
						}
					}
				});
			}
			case SOUTH -> {
				Position startPosition = new Position(player.getPosition().getX(), npc.getCentrePosition().getY() - 2,
						npc.getCentrePosition().getZ());
				Position targetPos = new Position(player.getPosition().getX(), npc.getCentrePosition().getY() - 14,
						npc.getCentrePosition().getZ());
				int delay = ACID_STRIKE_PROJECTILE.send(npc, targetPos.getX(), targetPos.getY());
				World.startEvent(e -> {
					Position currentPosition = startPosition.copy();
					for (int i = 0; i < 6; i++) {
						e.delay(1);
						for (int j = 0; j < 2; j++) {
							if (player.getPosition().distance(currentPosition) < 2)
								player.hit(new Hit(HitType.VENOM).randDamage(9, 12));
							venomPools.add(currentPosition);
							currentPosition = new Position(currentPosition.getX(), currentPosition.getY() - 1,
									currentPosition.getZ());
						}
					}
				});
			}
			case EAST -> {
				Position startPosition = new Position(npc.getCentrePosition().getX() + 2, player.getPosition().getY(),
						npc.getCentrePosition().getZ());
				Position targetPos = new Position(npc.getCentrePosition().getX() + 14, player.getPosition().getY(),
						npc.getCentrePosition().getZ());
				int delay = ACID_STRIKE_PROJECTILE.send(npc, targetPos.getX(), targetPos.getY());
				World.startEvent(e -> {
					Position currentPosition = startPosition.copy();
					for (int i = 0; i < 6; i++) {
						e.delay(1);
						for (int j = 0; j < 2; j++) {
							if (player.getPosition().distance(currentPosition) < 2)
								player.hit(new Hit(HitType.VENOM).randDamage(9, 12));
							venomPools.add(currentPosition);
							currentPosition = new Position(currentPosition.getX() + 1, currentPosition.getY(),
									currentPosition.getZ());
						}
					}
				});
			}
			case WEST -> {
				Position startPosition = new Position(npc.getCentrePosition().getX() - 2, player.getPosition().getY(),
						npc.getCentrePosition().getZ());
				Position targetPos = new Position(npc.getCentrePosition().getX() - 14, player.getPosition().getY(),
						npc.getCentrePosition().getZ());
				int delay = ACID_STRIKE_PROJECTILE.send(npc, targetPos.getX(), targetPos.getY());
				World.startEvent(e -> {
					Position currentPosition = startPosition.copy();
					for (int i = 0; i < 6; i++) {
						e.delay(1);
						for (int j = 0; j < 2; j++) {
							if (player.getPosition().distance(currentPosition) < 2)
								player.hit(new Hit(HitType.VENOM).randDamage(9, 12));
							venomPools.add(currentPosition);
							currentPosition = new Position(currentPosition.getX() - 1, currentPosition.getY(),
									currentPosition.getZ());
						}
					}
				});
			}
		}
	}

	public static LootTable table = new LootTable()
			.addTable(1,
					new LootItem(29794, 1, 2).broadcast(Broadcast.GLOBAL),
					new LootItem(29790, 1, 2).broadcast(Broadcast.GLOBAL),
					new LootItem(29792, 1, 2).broadcast(Broadcast.GLOBAL),
					new LootItem(29799, 1, 1).broadcast(Broadcast.GLOBAL))
			.addTable(35,
					new LootItem(ItemID.RUNE_KITESHIELD + 1, 2, 4),
					new LootItem(ItemID.RUNE_PLATELEGS + 1, 2, 4),
					new LootItem(ItemID.DRAGON_MACE, 2, 4),
					new LootItem(ItemID.RUNE_2H_SWORD + 1, 5, 1),
					new LootItem(ItemID.DRAGON_PLATELEGS + 1, 2, 1),
					new LootItem(29784, 2, 4))
			.addTable(20,
					new LootItem(ItemID.DEATH_RUNE, 250, 6),
					new LootItem(ItemID.NATURE_RUNE, 80, 3),
					new LootItem(ItemID.MUD_RUNE, 100, 1),
					new LootItem(ItemID.BLOOD_RUNE, 180, 1))
			.addTable(17,
					new LootItem(ItemID.YEW_SEED, 1, 6),
					new LootItem(ItemID.TOADFLAX_SEED, 4, 4),
					new LootItem(ItemID.RANARR_SEED, 3, 1),
					new LootItem(ItemID.SNAPDRAGON_SEED, 3, 1),
					new LootItem(ItemID.MAGIC_SEED, 2, 1))
			.addTable(19,
					new LootItem(ItemID.RANARR_SEED, 1, 12),
					new LootItem(ItemID.SNAPDRAGON_SEED, 1, 11),
					new LootItem(ItemID.TORSTOL_SEED, 1, 10),
					new LootItem(ItemID.WATERMELON_SEED, 15, 10),
					new LootItem(ItemID.WILLOW_SEED, 1, 8),
					new LootItem(ItemID.MAHOGANY_SEED, 1, 8),
					new LootItem(ItemID.MAPLE_SEED, 1, 8),
					new LootItem(ItemID.TEAK_SEED, 1, 8),
					new LootItem(ItemID.YEW_SEED, 1, 8),
					new LootItem(ItemID.PAPAYA_TREE_SEED, 1, 5),
					new LootItem(ItemID.MAGIC_SEED, 1, 3),
					new LootItem(ItemID.PALM_TREE_SEED, 1, 3),
					new LootItem(ItemID.SPIRIT_SEED, 1, 2),
					new LootItem(ItemID.DRAGONFRUIT_TREE_SEED, 1, 2),
					new LootItem(ItemID.CELASTRUS_SEED, 1, 1),
					new LootItem(ItemID.REDWOOD_TREE_SEED, 1, 1))
			.addTable(19,
					new LootItem(ItemID.COAL + 1, 120, 6),
					new LootItem(ItemID.ADAMANTITE_ORE + 1, 85, 6),
					new LootItem(ItemID.RAW_SHARK + 1, 21, 6),
					new LootItem(ItemID.YEW_LOGS + 1, 70, 5),
					new LootItem(ItemID.RUNITE_ORE + 1, 12, 3),
					new LootItem(ItemID.RAW_SHARK + 1, 100, 2),
					new LootItem(ItemID.RAW_MONKFISH + 1, 120, 2),
					new LootItem(ItemID.PURE_ESSENCE + 1, 1200, 2))
			.addTable(25,
					new LootItem(29782, 3, 50),
					new LootItem(ItemID.EARTH_ORB + 1, 45, 42),
					new LootItem(29784, 6, 40),
					new LootItem(ItemID.MORT_MYRE_FUNGUS + 1, 25, 35),
					new LootItem(ItemID.ANTIDOTE3 + 1, 6, 35),
					new LootItem(ItemID.WINE_OF_ZAMORAK + 1, 8, 25),
					new LootItem(ItemID.RED_SPIDERS_EGGS + 1, 40, 17),
					new LootItem(29784, 12, 17),
					new LootItem(ItemID.BARK, 15, 11),
					new LootItem(29788, 1, 4),
					new LootItem(29786, 1, 1));

	boolean looted = false;

	public static void register() {
		AraxyteVenomSack.register();

		NPCAction.register(13669, "harvest", (player, npc) -> {
			if (((Araxxor) npc.getCombat()).looted) {
				return;
			}
			((Araxxor) npc.getCombat()).looted = true;
			player.animate(832);
			int rolls = 1;
			if (player.getEquipment().get(Equipment.SLOT_RING) != null
					&& player.getEquipment().get(Equipment.SLOT_RING).getId() == 30592 && Random.get(3) == 0)
				rolls++;
			if (player.doubleDropBonus.remaining() > 0)
				rolls *= 2;
			if (NPCCombat.rollExtraDonatorDrop(player))
				rolls++;
			if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.ADDITIONAL_ROLL_CHANCE)
					&& Random.get(2) == 0)
				rolls++;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SECURING_THE_BAG)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SECURING_THE_BAG);
				SecuringTheBag c = (SecuringTheBag) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				assert c != null;
				if (Random.rollPercent(c.getAnotherRollChance())) {
					rolls++;
				}
			}
			for (int i = 0; i < rolls; i++) {
				Item item = table.rollItem();
				if (item.lootBroadcast != null) {
					npc.getCombat().getRareDropAnnounce(player, item, npc);
				}
				player.addToCollectionLog(item);
				GroundItem gi = new GroundItem(item.getId(), item.getAmount()).owner(player).position(npc.getPosition())
						.spawn();
			}
			npc.remove();
			World.startEvent(e -> {
				e.delay(10);
				new NPC(13668).spawn(npc.getPosition().getRegion().baseX + 49, npc.getPosition().getRegion().baseY + 24, 0, 4);
			});
		});

		NPCAction.register(13669, "destroy", (player, npc) -> {
			if (npc.isRemoved())
				return;
			player.dialogue(
					new YesNoDialogue("Are you sure you want to destroy your regular loot for a chance at receiving nid?",
							"This roll is 2x more common to roll the pet.", new Item(29836), () -> {
								npc.remove();
								maybeDropPetOnDestroy(player);
								World.startEvent(e -> {
									e.delay(10);
									new NPC(13668).spawn(npc.getPosition().getRegion().baseX + 49,
											npc.getPosition().getRegion().baseY + 24, 0, 4);
								});
							}));
		});
	}

	private static void maybeDropPetOnDestroy(Player player) {
		var pPerks = player.getPlayerPerkHandler();
		var petDropAverage = 500.0F;
		if (pPerks.getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
			var perkIndex = pPerks.getActivePerkIndex(player, Perks.THE_PET_HUNTER);
			var c = (ThePetHunter) pPerks.getActivePerks(player).get(perkIndex).getPerk(player);
			petDropAverage *= (float) c.getPetChanceBoost();
		}

		if (player.petDropBonus.isDelayed()) {
			petDropAverage *= 0.8f;
		}

		if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES)) {
			petDropAverage *= 0.85F;
		}

		if (Random.get(0, (int) petDropAverage) == 0) {
			Pet.NID.unlock(player, 13668);
		}
	}
}

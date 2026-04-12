package io.ruin.model.entity.player;

import io.ruin.HooksV2;
import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.ItemID;
import io.ruin.cache.NpcID;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.activities.perktree.PerkSets;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.*;
import io.ruin.model.activities.perktree.perksets.*;
import io.ruin.model.activities.raids.chambersrework.CustomXericRaid;
import io.ruin.model.activities.raids.toa.Invocations;
import io.ruin.model.activities.raids.xeric.ChambersOfXeric;
import io.ruin.model.activities.wilderness.BloodyChest;
import io.ruin.model.combat.*;
import io.ruin.model.combat.special.Special;
import io.ruin.model.combat.special.magic.StaffOfTheDead;
import io.ruin.model.combat.special.melee.VestasSpear;
import io.ruin.model.combat.special.ranged.DragonThrownaxe;
import io.ruin.model.combat.special.ranged.ToxicBlowpipe;
import io.ruin.model.content.combatachievements.CombatAchievementSystem;
import io.ruin.model.content.itembreaking.ItemBreakPerkHandler;
import io.ruin.model.content.upgrade.ItemEffect;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.PetNPC;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.inter.Widget;
import io.ruin.model.inter.handlers.EquipmentStats;
import io.ruin.model.inter.handlers.TabCombat;
import io.ruin.model.inter.questtab.toggles.TargetOverlay;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.RingOfAoE;
import io.ruin.model.item.actions.impl.TransformationRing;
import io.ruin.model.item.actions.impl.chargable.*;
import io.ruin.model.item.actions.impl.combine.SlayerHelm;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.DoubleHit;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.SpecialEnergyManagement;
import io.ruin.model.item.actions.impl.jewellery.*;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.actions.impl.skillcapes.DefenceSkillCape;
import io.ruin.model.item.actions.impl.wilderness_keys.WildernessKeyHandler;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.route.routes.TargetRoute;
import io.ruin.model.skills.magic.SpellBook;
import io.ruin.model.skills.magic.spells.TargetSpell;
import io.ruin.model.skills.magic.spells.lunar.Vengeance;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.model.skills.prayer.Redemption;
import io.ruin.model.skills.prayer.Retribution;
import io.ruin.model.skills.slayer.Slayer;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.Common;
import io.ruin.utility.Misc;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.ruin.cache.ItemID.*;
import static io.ruin.model.inter.Interface.COMBAT_OPTIONS;
import static io.ruin.model.item.containers.Equipment.SLOT_RING;

@Slf4j
public class PlayerCombat extends Combat {

	public static interface Hook {
		record OnDeath(Player player, Killer killer, Player pKiller) implements Hook {
		};

		record PreAttackWithSpell(PlayerCombat combat, Player player, Entity target) implements Hook {
		};
	}

	public static transient HooksV2<Hook> hooks = new HooksV2<>(Hook.class);
	public transient HooksV2<Hook> selfHooks = new HooksV2<>(Hook.class);

	public static List<String> UNDEAD_NPCS = Arrays.asList("Aberrant spectre", "Ankou",
			"Banshee", "Crawling Hand", "Ghast", "Ghost", "Mummy", "Revenant", "Shade",
			"Skeleton", "Skogre", "Summoned Zombie", "Tortured Soul", "Undead Chicken",
			"Undead Cow", "Undead One", "Zogre", "Zombified Spawn", "Zombie", "Vet'ion",
			"Pestilent Bloat", "Tree spirit", "Mi-Gor", "Treus Dayth", "Nazastarool",
			"Slash Bash", "Ulfric", "Vorkath");

	private transient Player player;
	public transient Entity lastTarget;
	private transient int lastTargetTimeoutTicks;
	public transient RangedData rangedData;
	public transient TargetSpell queuedSpell, autocastSpell;
	@Getter
	@Setter
	public transient int specialDistance = 1;
	private transient int level;
	private transient boolean updateLevel = true;
	public transient WeaponType weaponType;
	public transient AttackSet attackSet;
	public transient boolean attackedWithSpecial = false;
	private transient Special specialActive;
	private transient int graniteMaulSpecials;
	private transient int graniteMaulTimeoutTicks;
	private transient HashSet<Integer> skullers;
	public int skullDelay;
	public boolean highRiskSkull;
	public int tbTicks, tbImmunityTicks;
	public int chargeTicks;
	public int forinthySkullDelay;

	public void init(Player player) {
		this.player = player;
		this.selfHooks.clear();
		player.hitListener = new HitListener()
				// hits to this player
				.preDefend(this::preDefend)
				.postDefend(this::postDefend)
				.preDamage(this::preDamage)
				.postDamage(this::postDamage)
				// hits from this player
				.preTargetDefend(this::preTargetDefend)
				.postTargetDefend(this::postTargetDefend)
				.preTargetDamage(this::preTargetDamage)
				.postTargetDamage(this::postTargetDamage);
	}

	public void start() { // different than init because player isn't online yet when init happens
		setLevel();
		if (highRiskSkull)
			player.getAppearance().setSkullIcon(KillingSpree.overheadId(player));
		else if (forinthySkullDelay > 0)
			player.getAppearance().setSkullIcon(3);
		else if (skullDelay > 0)
			player.getAppearance().setSkullIcon(KillingSpree.overheadId(player));
	}

	/**
	 * Target
	 */

	@Override
	public void setTarget(Entity target) {
		updateLastTarget(target);
		super.setTarget(target);
		TargetOverlay.set(player, target);
		if (target.player != null)
			VarPlayerRepository.PLAYER_PRIORITY.set(player, target.getIndex());
	}

	/**
	 * Pre-attack
	 */

	public void preAttack() {
		forinthySkullDelay--;
		depleteTb();
		depleteSkull();
		depleteCharge();
		checkLastTarget();
		checkGraniteMaul();
		if (player.dragonfireShieldSpecial) {
			if (canAttack(target, true))
				DragonfireShield.specialAttack(player, target);
			else
				player.dragonfireShieldSpecial = false;
		}

		int distanceAddition = 0;
		if (target != null && target.isNpc() && getAttackStyle().isRanged()) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SKILLED_RANGER)) {
				if (getAttackType() == AttackType.RAPID_RANGED || getAttackType() == AttackType.LONG_RANGED
						|| getAttackType() == AttackType.CONTROLLED) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SKILLED_RANGER);
					SkilledRanger c = (SkilledRanger) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
							.getPerk(player);
					assert c != null;
					distanceAddition += c.getAttackRange();
				}
			}
		}
		if (target != null) {
			if (!canAttack(target, true))
				reset();
			else {
				var distance = weaponType.maxDistance;
				var additionalDistance = getAttackType() == AttackType.LONG_RANGED ? 2 + distanceAddition : distanceAddition;
				TargetRoute
						.set(player, target,
								useSpell() || (wearsNightmareStaff(player) && specialActive != null) ? 10
										: Math.min((distance + additionalDistance), 10));
			}
		}
	}

	/**
	 * Attack checks
	 */
	List<Integer> ratBaneWeaponIds = Arrays.asList(
			28792,
			28794,
			28796);

	public boolean isDefendingFromPlayer() {
		if (this.target == null) {
			return false;
		}

		return this.target.isPlayer();
	}

	public boolean canAttack(Entity target, boolean message) {
		if (isDead()) {
			return false;
		}

		if (target == null || target.isHidden()) {
			return false;
		}

		if (target.getCombat() == null) {
			if (message)
				player.sendMessage("You can't attack that npc.");
			return false;
		}

		if (target instanceof Player) {
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& ratBaneWeaponIds.contains(player.getEquipment().get(Equipment.SLOT_WEAPON).getId())) {
				player.sendMessage("You can only use this weapon against rats.");
				return false;
			}
		}
		if (target instanceof NPC) {
			if (!target.npc.getDef().rat) {
				if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null
						&& ratBaneWeaponIds.contains(player.getEquipment().get(Equipment.SLOT_WEAPON).getId())) {
					player.sendMessage("You can only use this weapon against rats.");
					return false;
				}
			}
			if (target.npc.getId() == 10507 || target.npc.getId() == 2668) {
				if (target.getCombat().isDefending(12)) {
					if (target.getCombat().lastAttacker != null &&
							!target.getCombat().lastAttacker.player.getName().equalsIgnoreCase(player.getName())) {
						player.sendMessage("Somebody else is already attacking this dummy.");
						return false;
					}
				}
			}
		}

		if (target instanceof PetNPC) {
			Pet.pickup(player, (PetNPC) target, ((PetNPC) target).petType, false);
			return false;
		}

		if (target.getCombat().isDead()) {
			return false;
		}

		if (player.isStunned()) {
			if (message)
				player.sendMessage("You're stunned!");
			return false;
		}

		// if we're in multi, but our target is not, return
		if (!multiCheck(target, message)) {
			return false;
		}

		if (target.player != null) {
			if (player.attackPlayerListener != null) {
				if (!player.attackPlayerListener.allow(player, target.player, message))
					return false;
			} else {
				if (message)
					player.sendMessage("You can't attack players from where you're standing.");
				return false;
			}
		} else {

			// what is 'unatackable npc' Likw shops
			if (target.npc.getCombat().getInfo().slayer_level > 0
					&& player.getStats().get(StatType.Slayer).currentLevel < target.npc.getCombat().getInfo().slayer_level) {
				if (message)
					player.sendMessage("You need a Slayer level of at least " + target.npc.getCombat().getInfo().slayer_level
							+ " to attack this monster.");
				return false;
			}

			if (target.npc.hasTarget() && !target.npc.isTargeting(player) && !target.npc.getDef().ignoreMultiCheck) {
				if (message)
					player.sendMessage("That monster isn't after you!");
				return false;
			}
			if (target.npc.attackNpcListener != null && !target.npc.attackNpcListener.allow(player, target.npc, message))
				return false;
			if (player.attackNpcListener != null && !player.attackNpcListener.allow(player, target.npc, message))
				return false;
		}

		return true;
	}

	private static boolean isMulti(Entity entity) {

		if (entity.inMulti()) {
			return true;
		}

		if (entity.getPosition().getTile().multi) {
			return true;
		}

		List<Integer> regionIds = Arrays.asList(
				10063,
				9808,
				14745,
				10064);

		if (regionIds.contains(entity.getPosition().regionId())) {
			return true;
		}

		if (entity.getPosition().getRegion().id == 10056) {
			return true;
		}

		if (entity.player != null && BloodyChest.hasBloodyKey(entity.player)) {
			return true;
		}

		final Bounds argentavisLair = new Bounds(2131, 5523, 2159, 5546, 3);
		if (entity.getPosition().inBounds(argentavisLair)) {
			return true;
		}

		return false;
	}

	public boolean multiCheck(Entity target, boolean message) {
		if (isMulti(target)) {
			return true;
		}

		if (isMulti(this.player)) {
			return true;
		}

		if (player != null) {
			if (player.gauntlet != null) {
				if (player.gauntlet.inGauntlet)
					return true;
			}
		}

		// if(entity == player.getBountyHunter().target)
		// return true;
		// ^This is how RS works, but people weren't liking this.. And it's kind of bad
		// for a smaller pk scene.
		boolean galvek = false;
		if (target.isNpc() && target.npc.getDef() != null) {
			NPC npc = (NPC) target;
			galvek = npc.getId() == 11774 || npc.getId() == 7221 || npc.getId() == 7223 || npc.getId() == 13675
					|| npc.getId() == 13674 || npc.getId() == 13673 || npc.getId() == 13672 || npc.getId() == 13671
					|| npc.getId() == 13670 || npc.getId() == 13668 || npc.getId() == 762 || npc.getId() == 763
					|| npc.getId() == 8367 || npc.getId() == 11778 || npc.getId() == 11783 || npc.getId() == 11782
					|| npc.getId() == 11775 || npc.getId() == 11772 || npc.getId() == 11761 || npc.getId() == 8095
					|| npc.getId() == 8096 || npc.getId() == 8097 || npc.getId() == 8098
					|| npc.getDef().name.toLowerCase().contains("phantom muspah") || npc.getId() == 12223 || npc.getId() == 12226;
		}

		boolean global = false;
		if (target.isNpc() && target.npc.getDef() != null) {
			if (target.npc.getDef().name.equalsIgnoreCase("olm") || target.npc.getDef().name.equalsIgnoreCase("dawn")
					|| target.npc.getDef().name.equalsIgnoreCase("Dusk") || target.npc.getDef().name.contains("Zebak")
					|| target.npc.getDef().name.contains("akkha") || target.npc.getDef().name.contains("Core")
					|| target.npc.getDef().name.contains("Elidinis") || target.npc.getDef().name.contains("Tumeken"))
				return true;
		}

		if (target.isNpc() && target.npc.getDef() != null) {
			NPC npc = (NPC) target;
			global = npc.getId() == 8262 || npc.getId() == 1787 || npc.getId() == 11895 || npc.getId() == 11896
					|| npc.getId() == 84;
		}

		if (target.isNpc() && target.npc.getDef() != null) {
			if (target.npc.getId() == 11797 || target.npc.getId() == 11794 || target.npc.getId() == 11790
					|| target.npc.getId() == 11792 || target.npc.getId() == 11779 || target.npc.getId() == 11783
					|| target.npc.getId() == 11782 || target.npc.getId() == 11723 || target.npc.getId() == 11727
					|| target.npc.getId() == 11726 || target.npc.getId() == 11729 || target.npc.getId() == 11728
					|| target.npc.getId() == 11725 || target.npc.getId() == 11724 || target.npc.getId() == 11719)
				return true;
			if (target.npc.getId() == 12336 || target.npc.getId() == 763 || target.npc.getId() == 6477
					|| target.npc.getId() == 11903 || target.npc.getId() == 7903)
				return true;
		}


		if (!target.getCombat().allowPj(player) && !galvek && !global) {
			if (message)
				player.sendMessage("That " + (target.player != null ? "player" : "monster") + " is already in combat!");
			return false;
		}

		if (!allowPj(target) && !galvek && !global) {
			if (lastAttacker != null && (lastAttacker.getHp() == 0 || lastAttacker.getCombat().isDead())) {
				return true;
			}
			if (message) {
				player.sendMessage("You are already in combat!");
			}
			return false;
		}

		return true;
	}

	/**
	 * Attacking
	 */
	public void attack() {
		this.attackedWithSpecial = false;

		if (target == null || !player.getRouteFinder().targetRoute.withinDistance)
			return;

		if (target.isNpc() && target.npc.ownerId != -1) {
			if (target.npc.ownerId != player.getUserId()) {
				player.sendMessage("This isn't spawned for you.");
				reset();
				return;
			}
		}
		if (target.isPlayer() && player.getPosition().regionId() == 12192) {
			player.sendMessage("You can't attack players in this area.");
			reset();
			return;
		}

		if (player.mageArena && attackSet.style != AttackStyle.MAGIC) {
			player.sendMessage("You can only use magic inside the arena!");
			reset();
			return;
		}
		if (player.currentParty != null && player.currentParty.deadPlayers.contains(player)) {
			player.sendMessage("You can't attack, you are dead.");
			reset();
			return;
		}

		if (player.getEquipment().hasId(30513)) {
			RingOfAoE.handleAoE(player);
		}

		if (player.getEquipment().hasId(8876) && specialActive != null && !hasAttackDelay()) {
			handleSpecial(AttackStyle.MAGIC, AttackType.ACCURATE, 0);
			if (player.getCombat().getAttackStyle() != AttackStyle.MAGIC) {
				player.getCombat().reset();
			}
		}

		if (player.getEquipment().hasId(8878) && specialActive != null && !hasAttackDelay()) {
			handleSpecial(AttackStyle.MAGIC, AttackType.ACCURATE, 0);
			if (player.getCombat().getAttackStyle() != AttackStyle.MAGIC) {
				player.getCombat().reset();
			}
		}

		if (wearsNightmareStaff(player) && specialActive != null && !hasAttackDelay()) {
			handleSpecial(AttackStyle.MAGIC, AttackType.ACCURATE, 0);
			if (player.getCombat().getAttackStyle() != AttackStyle.MAGIC) {
				player.getCombat().reset();
			}
		} else if (useSpell()) {

			if (hooks.handle(new Hook.PreAttackWithSpell(this, this.player, this.target))) {
				return;
			}

			if (this.target.preAttackWithSpell(this.player, this.currentTargetSpell())) {
				return;
			}

			if (this.selfHooks.handle(new Hook.PreAttackWithSpell(this, this.player, this.target))) {
				return;
			}

			if (!hasAttackDelay()) {
				attackWithMagic();
			}

			return;
		} else if (attackSet.style == AttackStyle.RANGED) {
			if (!hasAttackDelay()
					|| (specialActive instanceof DragonThrownaxe && VarPlayerRepository.SPECIAL_ENERGY.get(player) >= 250))
				attackWithRanged();
		} else if (attackSet.style == AttackStyle.MAGIC) {
			if (!hasAttackDelay())
				attackWithMagicWeapon();
		} else {
			if (target != null && target.isNpc() && target.npc.getId() == 15016) { // brutal lava dragon flying
				player.sendMessage("You can't attack this npc with melee.");
				updateLastAttack(4);
				return;
			}
			if (!specialGraniteMaul() && !hasAttackDelay()) {
				attackWithMelee();
			}
		}
	}

	public void burnEvent(Player player, int loops, int minDamage, int maxDamage, int delay) {
		World.startEvent(e -> {
			e.setCancelCondition(() -> player == null || player.getHp() < 1);
			for (int i = 0; i < loops; i++) {
				player.graphics(78);
				player.hit(new Hit().randDamage(minDamage, maxDamage));
				e.delay(delay);
			}
		});
	}

	public void removeAmmo(Item ammo, Hit... hits) {
		if (rangedData.alwaysBreak) {
			ammo.remove(hits.length);
			return;
		}
		boolean assembler = hasAvaAssembler();
		boolean ava = hasAvaDevice();
		for (Hit hit : hits) {
			if (hasQuiver()) {

			} else if (hasAvaAssembler()) {
				if (assembler && rollAssemblerChance()) {
					// player.sendMessage("Saved Ammo 80% chance");
				} else if (assembler && rollAssemblerDestroy()) {
					// player.sendMessage("Destroyed Ammo 20% chance");
					ammo.incrementAmount(-1);
				}
			} else if (hasAvaDevice()) {
				if (ava && rollAvaChance()) {
					// player.sendMessage("Saved Ammo 72% chance");
				} else if (ava && rollAvaDestroy()) {
					// player.sendMessage("Destroyed Ammo 20% chance");
					ammo.incrementAmount(-1);
				} else if (ava && rollAvaDrop()) {
					// player.sendMessage("Dropped Ammo 8% chance");
					ammo.incrementAmount(-1);
					hit.drop(new GroundItem(ammo.getId(), 1).owner(player).position(target.getPosition()));
				}
			} else {
				ammo.incrementAmount(-1);
				hit.drop(new GroundItem(ammo.getId(), 1).owner(player).position(target.getPosition()));
			}
		}
	}

	public boolean useSpell() {
		return queuedSpell != null || autocastSpell != null;
	}

	public void queueSpell(TargetSpell spell, Entity target) {
		// If specialAttack is triggered, break that special and attack normal
		if (specialActive != null)
			deactivateSpecial();
		queuedSpell = spell;
		setTarget(target);
	}

	/*
	 * You could use the already defined magic bonuses they use for regular magic but I already made it as I thought they
	 * didn't have it done xD it's your choice what you use. But this one also gives 100% correct combat bonus
	 */
	public double getRegularMagicDamageBoost(boolean skipNightmareStaff) {
		double bonus = 1;
		Equipment equipment = player.getEquipment();
		if (equipment.wearsAmuletOfTheDamned() && equipment.wearsAhrimRobes()) {
			bonus += 0.3;
		}
		if (equipment.wearsTormentedBracelet()) {
			bonus += 0.05;
		}
		if (equipment.wearsOcculNecklace()) {
			bonus += 0.05;
		}
		if (equipment.getItemIdForSlot(Equipment.SLOT_AMULET) == 30507) {
			bonus += 0.08;
		}
		if (equipment.wearsStaffOfTheDead() || equipment.wearsStaffOfLight() || equipment.wearsStaffOfBalance()) {
			bonus += 0.15;
		}
		if (equipment.wearsAncestralHat()) {
			bonus += 0.02;
		}
		if (equipment.wearsAncestralTop()) {
			bonus += 0.02;
		}
		if (equipment.wearsAncestralBottom()) {
			bonus += 0.02;
		}
		if (equipment.wearsKodaiWand()) {
			bonus += 0.15;
		}
		if (equipment.wearsAhrimStaff()) {
			bonus += 0.05;
		}
		if (equipment.wearsEliteVoid()) {
			bonus += 0.075;
		}
		if (equipment.wearsImbuedGodCape()) {
			bonus += 0.02;
		}
		if (!skipNightmareStaff && equipment.wearsNightmareStaff()) {
			bonus += 0.15;
		}
		if (SpellBook.MODERN.isActive(player) && equipment.wearsSmokeBattleStaff()) {
			bonus += 0.1;
		}
		return bonus;
	}

	/**
	 * Reset
	 */

	@Override
	public void reset() {
		updateLastTarget(target);
		super.reset();
		queuedSpell = null;
		rangedData = null;
		player.faceNone(!isDead());
		TargetRoute.reset(player);
	}

	/**
	 * Retliate
	 */

	@Override
	public boolean allowRetaliate(Entity attacker) {
		return target == null &&
				player.getMovement().isAtDestination() &&
				VarPlayerRepository.AUTO_RETALIATE.get(player) == 0 &&
				!player.isLocked();
	}

	@Override
	public int getDefendAnimation() {
		if (player.recentlyEquipped.isDelayed())
			return -1;
		ObjType shieldDef = player.getEquipment().getDef(Equipment.SLOT_SHIELD);
		if (shieldDef != null && shieldDef.shieldType != null)
			return shieldDef.shieldType.animationId;
		if (this.weaponType == null)
			return -1;
		return weaponType.defendAnimation;
	}

	/**
	 * Death
	 */

	@Override
	public void startDeath(Hit killHit) {
		if (player.revivalActive() && killHit.attacker != null && killHit.attacker.isNpc()) {
			player.reviveActive = false;
			if (player.getGameMode().isHardcoreIronman()) {
				GameMode.hardcoreDeath(player, killHit);
			}
			player.setHp(player.getMaxHp());
			player.sendMessage("Your revival scroll protects you from death and has healed you completely.");
			return;
		}
		player.lock();
		setDead(true);
		if (target != null) {
			reset();
		}
		Killer killer = getKiller();
		if (player.deathStartListener != null) {
			player.deathStartListener.handle(player, killer, killHit);
		}
		player.resetActions(true, true, true);
		player.startEvent(event -> {
			event.delay(1);
			player.animate(836);
			Retribution.check(player, killer);
			event.delay(4);
			player.resetAnimation();
			player.getHealthHud().close();
			setTruelyDead(true);
			if (player.deathEndListener != null && !player.inTob) {
				player.deathEndListener.handle(player, killer, killHit);
			} else {
				Player pKiller = killer != null ? killer.player : null;

				/**
				 * Rewards (Keep before items lost on death!)
				 */
				boolean bhTarget = player.getBountyHunter().deathByTarget(pKiller);
				if (pKiller != null) {
					killer.reward(player, bhTarget);
					VarPlayerRepository.PVP_DEATHS.increment(player, 1);
					player.currentKillSpree = 0;
				}

				/**
				 * Items lost on death
				 */
				boolean useDeathStorage = pKiller == null && player.wildernessLevel == 0;
				if (useDeathStorage) {
					// player.getDeathStorage().death(killer);
				} else {
					if (hooks.handle(new Hook.OnDeath(player, killer, pKiller))) {
						log.error("Tried to return hook on death");
					}
				}
				/**
				 * Drop bones (Keep after all other item drops!)
				 */
				if (pKiller == null)
					new GroundItem(526, 1).owner(player).position(player.getPosition()).spawn(60);
				else
					new GroundItem(526, 1).owner(pKiller).position(player.getPosition()).spawn();
				/**
				 * Misc
				 */
				player.face(Direction.NORTH);
				player.sendMessage("Oh dear, you are dead!");
				player.getCombat().removeKillerFromDamagedPlayers();

				player.getMovement().teleport(World.HOME);

				/**
				 * HCIM - revoke status
				 */

				if (player.getGameMode().isHardcoreIronman()) {
					GameMode.hardcoreDeath(player, killHit);
				}

			}
			resetSkull();
			resetSkullers();
			restore();
			event.delay(1);
			setTruelyDead(false);
			setDead(false);
			player.unlock();
		});
	}

	/**
	 * Restore
	 */

	public void restore() {
		player.getStats().restore(true);
		player.getMovement().restoreEnergy(100);
		player.getPrayer().deactivateAll();
		player.resetFreeze();
		player.cureVenom(0);
		restoreSpecial(100);
		if (specialActive != null)
			deactivateSpecial();
		TargetOverlay.reset(player);
	}

	public void updateLevel() {
		updateLevel = true;
	}

	public void checkLevel() {
		if (updateLevel) {
			updateLevel = false;
			setLevel();
		}
	}

	public void updateCombatLevel() {
		player.getPacketSender().sendString(COMBAT_OPTIONS, 3, "Combat Lvl: " + level);
	}

	public int getLevel() {
		return level;
	}

	public void updateWeapon(boolean fromAutocast) {
		this.updateWeapon(fromAutocast, false);
	}

	public void updateWeapon(boolean fromAutocast, boolean login) {
		ObjType wepDef = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
		WeaponType newWeapon = (wepDef == null || wepDef.weaponType == null) ? WeaponType.UNARMED : wepDef.weaponType;
		if (weaponType != newWeapon || login) {
			weaponType = newWeapon;
			updateAttackSet();
			if (weaponType != null) {
				VarPlayerRepository.WEAPON_TYPE.set(player, weaponType.config);
			}
		}

		if (!fromAutocast) {
			TabCombat.updateAutocast(player, login);
		}

		if (specialActive != null && (wepDef == null || wepDef.special != specialActive)) {
			deactivateSpecial();
		}

		player.getPacketSender().sendString(COMBAT_OPTIONS, 2, wepDef == null ? "Unarmed" : wepDef.name);
		if (player.sotdDelay.isDelayed() && (wepDef == null || !(wepDef.special instanceof StaffOfTheDead)))
			player.sotdDelay.reset();
		if (player.vestasSpearSpecial.isDelayed() && (wepDef == null || !(wepDef.special instanceof VestasSpear)))
			player.vestasSpearSpecial.reset();
	}

	public void changeAttackSet(int newSetIndex) {
		boolean invalidAttackSet = weaponType.attackSets[newSetIndex] == null;
		attackSet = weaponType.attackSets[invalidAttackSet ? 0 : newSetIndex];
		VarPlayerRepository.ATTACK_SET.set(player, invalidAttackSet ? 0 : newSetIndex);
		TabCombat.resetAutocast(player);
	}

	@Override
	public AttackStyle getAttackStyle() {
		return attackSet.style;
	}

	@Override
	public AttackType getAttackType() {
		if (this.attackSet == null)
			return null;
		return attackSet.type;
	}

	public void toggleSpecial() {
		ObjType wepDef = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
		if (specialActive != null) {
			deactivateSpecial();
			if (wepDef != null && wepDef.graniteMaul)
				queueGraniteMaulSpecial();
			return;
		}
		if (wepDef != null && wepDef.special != null) {
			if (DuelRule.NO_SPECIALS.isToggled(player)) {
				player.sendMessage("Special attacks have been disabled for this duel!");
				return;
			}

			if (wepDef.weaponType != null)
				specialDistance = wepDef.weaponType.maxDistance;

			if (!wepDef.special.handleActivation(player)) {
				if (wepDef.graniteMaul)
					queueGraniteMaulSpecial();
				specialActive = wepDef.special;
				VarPlayerRepository.SPECIAL_ACTIVE.set(player, 1);
			}
		}
	}

	public void restoreSpecial(int percent) {
		int energy = VarPlayerRepository.SPECIAL_ENERGY.get(player);
		int newEnergy = Math.min(1000, energy + (percent * 10));
		if (energy != newEnergy)
			VarPlayerRepository.SPECIAL_ENERGY.set(player, newEnergy);
	}

	public boolean useSpecialEnergy(int amount) {

		int energy = VarPlayerRepository.SPECIAL_ENERGY.get(player);
		if (player.wildernessLevel < 1) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_SPECIAL_ATTACK)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_SPECIAL_ATTACK);
				TheSpecialAttacker c = (TheSpecialAttacker) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				amount *= c.getSpecialAttackSaveMultiplier();
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
				if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON),
						AttributeTypes.SPECIAL_ENERGY_LOWERER)) {
					if (AttributeExtensions.getCharges(AttributeTypes.SPECIAL_ENERGY_LOWERER,
							player.getEquipment().get(Equipment.SLOT_WEAPON)) > 0) {
						amount *= 0.75;
						SpecialEnergyManagement.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON),
								getAttackStyle());
					}

				}
			}

		}
		if (player.getCurrentToARaid() != null
				&& player.getCurrentToARaid().getInvocations().contains(Invocations.OVERLY_DRAINING)) {
			amount = 100;
		}
		if (player.liquidAdrenalineDelay.remaining() > 0)
			amount /= 2;
		if (amount > energy / 10) {
			player.sendMessage("You need at least " + amount + "% special attack energy to use this.");
			return false;
		}

		VarPlayerRepository.SPECIAL_ENERGY.set(player, energy - (amount * 10));
		return true;

	}

	public void skull(Player pTarget) {
		if (player.wildernessLevel == 0 && !player.pvpAttackZone)
			return;
		if (skullers != null && skullers.contains(pTarget.getUserId()) && pTarget.getCombat().isSkulled())
			return;
		if (pTarget.getCombat().skullers == null)
			pTarget.getCombat().skullers = new HashSet<>(5);
		pTarget.getCombat().skullers.add(player.getUserId());
		if (!highRiskSkull)
			skullNormal();
		WildernessKeyHandler.setSkull(player);
	}

	public void skullNormal() {
		skullDelay = 1000;
		highRiskSkull = false;
		player.getAppearance().setSkullIcon(KillingSpree.overheadId(player));
	}

	public void skullHighRisk() {
		skullDelay = 1000;
		highRiskSkull = true;
		player.getAppearance().setSkullIcon(KillingSpree.overheadId(player));
		if (player.getPrayer().isActive(Prayer.PROTECT_ITEM))
			player.getPrayer().deactivate(Prayer.PROTECT_ITEM);
		player.sendMessage(
				Color.ORANGE_RED.wrap("Warning:") + " The Protect Item prayer is disabled when marked with a high-risk skull.");
	}

	public void resetSkull() {
		if (!player.dead()) {
			if (player.wildernessLevel > 0 || player.getCombat().isAttacking(10) || player.getCombat().isDefending(10)) {
				player.sendMessage("You cannot unskull whilst in the wilderness or in Combat");
				return;
			}
		}
		skullDelay = 0;
		highRiskSkull = false;
		if (player.getAppearance().getSkullIcon() != -1)
			player.getAppearance().setSkullIcon(-1);
	}

	public boolean isSkulled() {
		return highRiskSkull || skullDelay > 0 || forinthySkullDelay > 0;
	}

	public void teleblock() {
		if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
			tbTicks = 250;
			player.getPacketSender().sendWidgetTimerCustom(Widget.TELEBLOCK, 150);
			player
					.sendMessage("<col=4f006f>A teleblock spell has been cast on you. It will expire in 2 minutes, 30 seconds.");
		} else {
			tbTicks = 500;
			player.getPacketSender().sendWidgetTimerCustom(Widget.TELEBLOCK, 300);
			player.sendMessage("<col=4f006f>A teleblock spell has been cast on you. It will expire in 5 minutes.");
		}
	}

	public void resetTb() {
		tbTicks = 0;
		tbImmunityTicks = 0;
		player.getPacketSender().sendWidgetTimerCustom(Widget.TELEBLOCK, 0);
	}

	public boolean checkTb() {
		if (tbTicks == 0)
			return false;
		long ms = Server.tickMs() * tbTicks;
		long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(minutes);
		if (minutes == 0) {
			if (seconds <= 1)
				player.sendMessage("A teleport block has been cast on you. It should wear off in 1 second.");
			else
				player.sendMessage("A teleport block has been cast on you. It should wear off in " + seconds + " seconds.");
		} else if (minutes == 1) {
			if (seconds == 0)
				player.sendMessage("A teleport block has been cast on you. It should wear off in 1 minute.");
			else if (seconds == 1)
				player.sendMessage("A teleport block has been cast on you. It should wear off in 1 minute, 1 second.");
			else
				player.sendMessage(
						"A teleport block has been cast on you. It should wear off in 1 minute, " + seconds + " seconds.");
		} else {
			if (seconds == 0)
				player.sendMessage("A teleport block has been cast on you. It should wear off in " + minutes + " minutes.");
			else if (seconds == 1)
				player.sendMessage(
						"A teleport block has been cast on you. It should wear off in " + minutes + " minutes, 1 second.");
			else
				player.sendMessage("A teleport block has been cast on you. It should wear off in " + minutes + " minutes, "
						+ seconds + " seconds.");
		}
		return true;
	}

	public boolean charge() {
		if (chargeTicks > 0) {
			player.sendMessage("You can't recast that yet, your current Charge is too strong.");
			return false;
		}
		player.animate(811);
		player.graphics(111, 130, 3);
		chargeTicks = 700;
		return true;
	}

	/**
	 * Misc
	 */

	@Override
	public double getLevel(StatType stat) {
		return player.getStats().get(stat).currentLevel;
	}

	@Override
	public double getBonus(int bonusType) {
		// if (bonusType == EquipmentStats.RANGED_STRENGTH)
		// return DizanaQuiver.getPrimaryOrQuiverCombatBonus(player);
		return player.getEquipment().bonuses[bonusType];
	}

	@Override
	public double getDragonfireResistance() {

		int shieldId = player.getEquipment().getId(Equipment.SLOT_SHIELD);
		int bootId = player.getEquipment().getId(Equipment.SLOT_FEET);
		/**
		 * Dragon fire
		 */
		double absorbDamage = 0.0;

		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_DRAGON_SLAYER)) {
			absorbDamage += 1.0;
		}
		if (bootId == 30505) {
			absorbDamage += 1.0;
		}
		if (player.superAntifireTicks > 0)
			absorbDamage += 1.0;
		else if (player.antifireTicks > 0)
			absorbDamage += 0.8;

		if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
			absorbDamage += 0.2;

		if (shieldId == 1540) { // antifire shield
			absorbDamage += 0.8;
		} else if (shieldId == 11283 || shieldId == 11284 || shieldId == 21634 || shieldId == 21633 || // dragonfire shield
				shieldId == 22003 || shieldId == 22002) {
			absorbDamage += 0.8;
		}
		return absorbDamage;
	}

	// private boolean TomeOfFire(Player player, Hit hit, Entity entity) {
	// final int TomeOfFire = 20714;
	// if (hit.attackStyle != null && target != null && target.isNpc()) {
	// Item book = player.getEquipment().get(Equipment.SLOT_SHIELD);
	//
	//
	// if (book == null) {
	// return false;
	// }
	// if (hit.attackStyle.isMagic() && book.getId() == TomeOfFire) {
	// hit.boostDamage(0.5);
	// return true;
	// }
	// }
	// return true; i think that got copied from this, we have siren, but i cant rem
	// why this is commented out
	// }

	@Override
	public void faceTarget() {
		player.face(target);
	}

	public Special specialActive() {
		return specialActive;
	}

	public boolean hasSpecialActive() {
		return specialActive != null;
	}

	protected void setLevel() {
		int newLevel = CombatUtils.getCombatLevel(player);
		if (level != newLevel) {
			level = newLevel;
			player.getAppearance().update();
			player.getPacketSender().sendString(COMBAT_OPTIONS, 3, "Combat Lvl: " + level);
		}
	}

	private boolean forinthrySkullBoost(Player player, Hit hit, Entity entity) {
		if (player.getCombat().forinthySkullDelay > 0) {
			if (entity != null && entity.isNpc()) {
				if (AmuletOfAvarice.isWearingAmuletOfAvarice(player) && AmuletOfAvarice.targetIsRevenant(entity)) {
					hit.boostDamage(0.15);
					return true;
				}
			}
		}
		return false;
	}

	private void updateLastTarget(Entity target) {
		lastTarget = target;
		lastTargetTimeoutTicks = 5;
	}

	private void checkLastTarget() {
		if (lastTargetTimeoutTicks > 0 && --lastTargetTimeoutTicks == 0)
			lastTarget = null;
	}

	private boolean wearsNightmareStaff(Player player) {
		final Item item = player.getEquipment().get(3);
		if (item != null) {
			int weaponId = item.getId();
			return weaponId == 24422 || weaponId == 24423 || weaponId == 24424 || weaponId == 24425;
		}
		return false;
	}

	private boolean wearsHarmonisedStaff(Player player) {
		final Item item = player.getEquipment().get(3);
		if (item != null) {
			int weaponId = item.getId();
			return weaponId == 24423;
		}
		return false;
	}

	private void attackWithMagicWeapon() {
		ObjType weaponDef = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
		boolean swamp = weaponDef.id == 12899;
		double attackBoost = 0;
		double damageBoost = 1;
		boolean doubleAttackEffect = false;
		int attackTicks = 0;
		int baseAttackSpeed = 4;
		if (target.isNpc()) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ENHANCED_MAGICAL_WEAPONS)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ENHANCED_MAGICAL_WEAPONS);
				EnhancedMagicalWeapons c = (EnhancedMagicalWeapons) player.getPlayerPerkHandler().getActivePerks(player)
						.get(perkIndex).getPerk(player);
				assert c != null;
				attackBoost += c.getAccuracyBoost();
				switch (c.getPerkLevel()) {
					case 1:
						baseAttackSpeed = 4;
						break;
					case 2:
					case 3:
						baseAttackSpeed = 3;
						break;
					case 4:
					case 5:
						baseAttackSpeed = 2;
						break;
				}
				if (player.berserkerRageActive)
					baseAttackSpeed /= 2;
			}
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.TAKE_YOUR_TIME)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.TAKE_YOUR_TIME);
				TakeYourTime c = (TakeYourTime) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				attackTicks += 2;
				assert c != null;
				attackBoost += c.getAccuracyBoost();
			}
			if (target.npc.getDef().name.equalsIgnoreCase("zulrah")) {
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SNAKE_CHARMER)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SNAKE_CHARMER);
					SnakeCharmer c = (SnakeCharmer) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
							.getPerk(player);
					damageBoost += c.getDamageMultiplier();
				}
			}
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.GOD_WARS_VETERAN)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.GOD_WARS_VETERAN);
				GodWarsVeteran c = (GodWarsVeteran) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				if (target.npc.getDef().id == NpcID.GENERAL_GRAARDOR || target.npc.getId() == NpcID.COMMANDER_ZILYANA
						|| target.npc.getId() == NpcID.KREEARRA || target.npc.getId() == NpcID.KRIL_TSUTSAROTH ||
						target.npc.getDef().name.contains("nex")) {
					assert c != null;
					attackBoost += c.getAccuracyBonus();
					damageBoost += c.getDamageBonus();
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
				if (player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 33000) {
					baseAttackSpeed--;
				}
				if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON),
						AttributeTypes.DOUBLE_HIT)) {
					if (DoubleHit.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON), target,
							getAttackStyle()))
						doubleAttackEffect = true;
				}
			}
		}

		if (weaponDef.id == TridentOfTheSeas.UNCHARGED || weaponDef.id == 12900) {
			player.sendMessage("Your trident has no charges left!");
			updateLastAttack(baseAttackSpeed + attackTicks);
			return;
		}

		if (weaponDef.id == SanguinestiStaff.UNCHARGED) {
			player.sendMessage("Your Sanguinesti staff has no charges left!");
			updateLastAttack(baseAttackSpeed + attackTicks);
			return;
		}
		if (weaponDef.id == TumekensShadow.UNCHARGED) {
			player.sendMessage("Your Tumekens shadow has no charges left!");
			updateLastAttack(baseAttackSpeed + attackTicks);
			return;
		}
		if (swamp || weaponDef.id == TridentOfTheSeas.CHARGED || weaponDef.id == TridentOfTheSeas.FULLY_CHARGED
				|| weaponDef.id == 28796) {
			if (target.player != null) {
				player.sendMessage(Color.RED.wrap("This staff's spell cannot be used against other players."));
				return;
			}
			player.animate(1167);
			player.graphics(swamp ? 665 : 1251, 92, 0);
			int duration = new Projectile(swamp ? 1040 : 1252, 30, 0, 51, 56, 10, 16, 11).send(player, target);
			final int distance = getChebyshevDistance(player, target);
			final int delayTicks = getMagicHitDelay(distance);
			Hit hit;
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
					&& target.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				int chance = 25 - (level * 2);
				if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
					int delay = 50 - (level * 3);
					player.soulReaverDelay.delay(delay);
					String name = target.npc.getDef().name;
					if (name.contains("dummy"))
						return;
					target.hit(new Hit(player, AttackStyle.MAGIC).fixedDamage((int) (target.npc.getHp() * multiplier))
							.delay(delayTicks).setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
					return;
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
					player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
				int minimumHit = 0;
				int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				minimumHit = (int) ((int) (getTridentMaxDamage(swamp) * damageBoost) * multiplier);
				int chance = 20 - (level * 2);
				if (Random.get(chance) == 0)
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage(minimumHit, (int) (getTridentMaxDamage(swamp) * damageBoost)).delay(delayTicks)
							.setAttackWeapon(weaponDef);
				else
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage((int) (getTridentMaxDamage(swamp) * damageBoost)).delay(delayTicks)
							.setAttackWeapon(weaponDef);
			} else
				hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
						.randDamage((int) (getTridentMaxDamage(swamp) * damageBoost)).delay(delayTicks)
						.setAttackWeapon(weaponDef);
			int damage = target.hit(hit);
			if (damage > 0) {
				if (hit.attackStyle == AttackStyle.MAGIC) {
					if (hit.damage > 0) {
						if (target != null && target.isNpc() && target.getHp() > 0) {
							if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FINEST_WIZARDRY)) {
								int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FINEST_WIZARDRY);
								FinestWizardry c = (FinestWizardry) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
										.getPerk(player);
								assert c != null;
								if (Random.rollPercent(c.getExtraHitChance())) {
									doubleAttackEffect = true;
								}
							}
						}
					}
				}
				target.graphics(swamp ? 1042 : 1253, 90, duration);
				if (swamp && Random.rollDie(4, 1))
					target.envenom(6);
			} else {
				hit.nullify();
				target.graphics(85, 92, duration);
			}

			updateLastAttack(doubleAttackEffect ? 1 : baseAttackSpeed + attackTicks);
		}
		if (weaponDef.id == 22555) {
			if (target.player != null) {
				player.sendMessage(Color.RED.wrap("This staff's spell cannot be used against other players."));
				return;
			}
			player.animate(1167);
			int duration = new Projectile(1729, 30, 0, 51, 56, 10, 16, 11).send(player, target);
			Hit hit;
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
					&& target.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				int chance = 25 - (level * 2);
				if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
					int delay = 50 - (level * 3);
					player.soulReaverDelay.delay(delay);
					String name = target.npc.getDef().name;
					if (name.contains("dummy"))
						return;
					target.hit(new Hit(player).fixedDamage((int) (target.npc.getHp() * multiplier)).clientDelay(duration)
							.setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
					return;
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
					player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
				int minimumHit = 0;
				int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				minimumHit = (int) ((int) (getTridentMaxDamage(false) * damageBoost) * multiplier);
				int chance = 20 - (level * 2);
				if (Random.get(chance) == 0)
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage(minimumHit, (int) (getTridentMaxDamage(false) * damageBoost)).ignorePrayer().ignoreDefence()
							.clientDelay(duration).setAttackWeapon(weaponDef);
				else
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage((int) (getTridentMaxDamage(false) * damageBoost)).clientDelay(duration)
							.setAttackWeapon(weaponDef);
			} else
				hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
						.randDamage((int) (getTridentMaxDamage(false) * damageBoost)).clientDelay(duration)
						.setAttackWeapon(weaponDef);
			int damage = target.hit(hit);
			if (damage > 0) {
				if (hit.attackStyle == AttackStyle.MAGIC) {
					if (hit.damage > 0) {
						if (target.isNpc() && target.getHp() > 0) {
							if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FINEST_WIZARDRY)) {
								int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FINEST_WIZARDRY);
								FinestWizardry c = (FinestWizardry) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
										.getPerk(player);
								assert c != null;
								if (Random.rollPercent(c.getExtraHitChance())) {
									doubleAttackEffect = true;
								}
							}
						}
					}
				}
				World.startEvent(e -> {
					e.delay(2);
					if (hit.victim != null)
						hit.victim.graphics(78, 0, 0);
				});
			} else {
				hit.nullify();
				if (hit.victim != null)
					hit.victim.graphics(85, 92, duration);
			}

			updateLastAttack(doubleAttackEffect ? 1 : baseAttackSpeed + attackTicks);
		}
		if (weaponDef.id == 8876) {

			if (target.player != null) {
				player.sendMessage(Color.RED.wrap("This staff's spell cannot be used against other players."));
				return;
			}
			player.animate(1167);
			int duration = new Projectile(1729, 70, 0, 51, 56, 10, 16, 11).send(player, target);
			Hit hit;
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
					&& target.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				int chance = 25 - (level * 2);
				if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
					int delay = 50 - (level * 3);
					player.soulReaverDelay.delay(delay);
					String name = target.npc.getDef().name;
					if (name.contains("dummy"))
						return;
					target.hit(new Hit(player).fixedDamage((int) (target.npc.getHp() * multiplier)).clientDelay(duration)
							.setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
					return;
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
					player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
				int minimumHit = 0;
				int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				minimumHit = (int) ((int) (getTridentMaxDamage(true) * damageBoost) * multiplier);
				int chance = 20 - (level * 2);
				if (Random.get(chance) == 0)
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage(minimumHit, (int) (getTridentMaxDamage(true) * damageBoost)).ignorePrayer().ignoreDefence()
							.clientDelay(duration).setAttackWeapon(weaponDef);
				else
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage((int) (getTridentMaxDamage(true) * damageBoost)).clientDelay(duration)
							.setAttackWeapon(weaponDef);
			} else
				hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
						.randDamage((int) (getTridentMaxDamage(true) * damageBoost)).clientDelay(duration)
						.setAttackWeapon(weaponDef);
			int damage = target.hit(hit);
			if (damage > 0) {
				if (hit.attackStyle == AttackStyle.MAGIC) {
					if (hit.damage > 0) {
						if (target.isNpc() && target.getHp() > 0) {
							if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FINEST_WIZARDRY)) {
								int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FINEST_WIZARDRY);
								FinestWizardry c = (FinestWizardry) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
										.getPerk(player);
								assert c != null;
								if (Random.rollPercent(c.getExtraHitChance())) {
									doubleAttackEffect = true;
								}
							}
						}
					}
				}
				World.startEvent(e -> {
					e.delay(2);
					if (hit.victim != null)
						hit.victim.graphics(78, 0, 0);
				});
			} else {
				hit.nullify();
				if (hit.victim != null)
					hit.victim.graphics(85, 92, duration);
			}

			updateLastAttack(doubleAttackEffect ? 1 : baseAttackSpeed + attackTicks);
		}
		if (weaponDef.id == 30480) {
			if (target.player != null) {
				player.sendMessage(Color.RED.wrap("This staff's spell cannot be used against other players."));
				return;
			}
			player.animate(1167);
			player.graphics(1546, 92, 0);
			int duration = new Projectile(1547, 30, 0, 51, 56, 10, 16, 11).send(player, target);
			Hit hit;
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
					&& target.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				int chance = 25 - (level * 2);
				if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
					int delay = 50 - (level * 3);
					player.soulReaverDelay.delay(delay);
					String name = target.npc.getDef().name;
					if (name.contains("dummy"))
						return;
					target.hit(new Hit(player).fixedDamage((int) (target.npc.getHp() * multiplier)).clientDelay(duration)
							.setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
					return;
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
					player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
				int minimumHit = 0;
				int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				minimumHit = (int) ((int) (getTrainingStaffMaxDamage(player) * damageBoost) * multiplier);
				int chance = 20 - (level * 2);
				if (Random.get(chance) == 0)
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost * 4)
							.randDamage(minimumHit, (int) (getTrainingStaffMaxDamage(player) * damageBoost)).ignoreDefence()
							.ignorePrayer().clientDelay(duration).setAttackWeapon(weaponDef);
				else
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost * 4)
							.randDamage(2, (int) (getTrainingStaffMaxDamage(player) * damageBoost)).clientDelay(duration)
							.setAttackWeapon(weaponDef);
			} else
				hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost * 10)
						.randDamage(1, (int) (getTrainingStaffMaxDamage(player) * damageBoost)).clientDelay(duration)
						.setAttackWeapon(weaponDef);

			var target = this.target;
			// NOTE: hitting can reset the target
			int damage = target.hit(hit);
			if (damage > 0) {
				if (hit.attackStyle == AttackStyle.MAGIC) {
					if (hit.damage > 0) {
						if (target != null && target.isNpc() && target.getHp() > 0) {
							if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FINEST_WIZARDRY)) {
								int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FINEST_WIZARDRY);
								FinestWizardry c = (FinestWizardry) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
										.getPerk(player);
								assert c != null;
								if (Random.rollPercent(c.getExtraHitChance())) {
									doubleAttackEffect = true;
								}
							}
						}
					}
				}
				target.graphics(1548, 90, duration);
			} else {
				hit.nullify();
				target.graphics(85, 92, duration);
			}
			updateLastAttack(doubleAttackEffect ? 1 : baseAttackSpeed + attackTicks);
		}
		if (weaponDef.id == 30312) {
			if (target.player != null) {
				player.sendMessage(Color.RED.wrap("This staff's spell cannot be used against other players."));
				return;
			}
			player.animate(1167);
			player.graphics(1546, 92, 0);
			int duration = new Projectile(1482, 30, 0, 51, 56, 10, 16, 11).send(player, target);
			Hit hit;
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
					&& target.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				int chance = 25 - (level * 2);
				if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
					int delay = 50 - (level * 3);
					player.soulReaverDelay.delay(delay);
					String name = target.npc.getDef().name;
					if (name.contains("dummy"))
						return;
					target.hit(new Hit(player).fixedDamage((int) (target.npc.getHp() * multiplier)).clientDelay(duration)
							.setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
					return;
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
					player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
				int minimumHit = 0;
				int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				minimumHit = (int) ((int) (getTrainingStaffMaxDamage(player) * damageBoost) * multiplier);
				int chance = 20 - (level * 2);
				if (Random.get(chance) == 0)
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost * 4)
							.randDamage(minimumHit, (int) (getTrainingStaffMaxDamage(player) * damageBoost)).ignoreDefence()
							.ignorePrayer().clientDelay(duration).setAttackWeapon(weaponDef);
				else
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost * 4)
							.randDamage(2, (int) (getTrainingStaffMaxDamage(player) * damageBoost)).clientDelay(duration)
							.setAttackWeapon(weaponDef);
			} else
				hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost * 10)
						.randDamage(1, (int) (getTrainingStaffMaxDamage(player) * damageBoost)).clientDelay(duration)
						.setAttackWeapon(weaponDef);
			int damage = target.hit(hit);
			if (damage > 0) {
				if (hit.attackStyle == AttackStyle.MAGIC) {
					if (hit.damage > 0) {
						if (target.isNpc() && target.getHp() > 0) {
							if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FINEST_WIZARDRY)) {
								int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FINEST_WIZARDRY);
								FinestWizardry c = (FinestWizardry) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
										.getPerk(player);
								assert c != null;
								if (Random.rollPercent(c.getExtraHitChance())) {
									doubleAttackEffect = true;
								}
							}
						}
					}
				}
				target.graphics(1548, 90, duration);
			} else {
				hit.nullify();
				target.graphics(85, 92, duration);
			}
			updateLastAttack(doubleAttackEffect ? 1 : baseAttackSpeed + attackTicks);
		}
		if (weaponDef.id == 22516 && target.isNpc() && target.npc.getId() == 8370) {
			AttackStyle style = attackSet.style;
			AttackType type = attackSet.type;
			int maxSpecialDamage = 150;
			if (handleSpecial(style, type, maxSpecialDamage)) {
				if (target.isNpc()) {
					if (target.npc.getId() == 8370) {
						player.animate(1167);
						player.graphics(1546, 92, 0);
						int duration = new Projectile(1547, 30, 0, 51, 56, 10, 16, 11).send(player, target);
						player.publicSound(2541);
						Hit hit = new Hit(player, AttackStyle.MAGIC, player.getCombat().getAttackType()).boostAttack(attackBoost)
								.randDamage(75, 150).clientDelay(duration).ignoreDefence();
						int damage = target.hit(hit);
						if (damage > 0) {
							target.graphics(1548, 90, duration);
						} else {
							hit.nullify();
							target.graphics(85, 92, duration);
						}
					}
				}
			} else {
				player.animate(1167);
				player.graphics(1543, 92, 0);
				int duration = new Projectile(1544, 30, 0, 51, 56, 10, 16, 11).send(player, target);
				int damageCalc = (int) (getTridentMaxDamage(swamp) * damageBoost);
				Hit hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost).randDamage(damageCalc)
						.clientDelay(duration).setAttackWeapon(weaponDef);
				int damage = target.hit(hit);
				if (target.isNpc()) {
					if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ENHANCED_MAGICAL_WEAPONS)) {
						int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ENHANCED_MAGICAL_WEAPONS);
						EnhancedMagicalWeapons c = (EnhancedMagicalWeapons) player.getPlayerPerkHandler().getActivePerks(player)
								.get(perkIndex).getPerk(player);
						assert c != null;
						damage += (damage * c.getDamageBoost());
					}
				}
				if (damage > 0) {
					target.graphics(1545, 90, duration);
				} else {
					hit.nullify();
					target.graphics(85, 92, duration);
				}
			}
			updateLastAttack(baseAttackSpeed + attackTicks);
		}
		if (weaponDef.id == 23852)
			attackWithGauntletStaff(23, true);
		if (weaponDef.id == 23853)
			attackWithGauntletStaff(31, true);
		if (weaponDef.id == 23854)
			attackWithGauntletStaff(39, true);
		if (weaponDef.id == 23898)
			attackWithGauntletStaff(23, false);
		if (weaponDef.id == 23899)
			attackWithGauntletStaff(31, false);
		if (weaponDef.id == 23900)
			attackWithGauntletStaff(39, false);
		if (weaponDef.id == SanguinestiStaff.CHARGED) {
			if (target.player != null) {
				player.sendMessage(Color.RED.wrap("This staff's spell cannot be used against other players."));
				return;
			}
			attackBoost += 0.25;
			player.animate(1167);
			player.graphics(1540, 92, 0);
			int duration = new Projectile(1539, 30, 0, 51, 56, 10, 16, 11).send(player, target);
			Hit hit;
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
					&& target.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				int chance = 25 - (level * 2);
				if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
					int delay = 50 - (level * 3);
					player.soulReaverDelay.delay(delay);
					String name = target.npc.getDef().name;
					if (name.contains("dummy"))
						return;
					target.hit(new Hit(player).fixedDamage((int) (target.npc.getHp() * multiplier)).clientDelay(duration)
							.setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
					return;
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
					player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
				int minimumHit = 0;
				int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				minimumHit = (int) ((int) (getSanguinestiMaxDamage() * damageBoost) * multiplier);
				int chance = 20 - (level * 2);
				if (Random.get(chance) == 0)
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage(minimumHit, (int) (getSanguinestiMaxDamage() * damageBoost)).clientDelay(duration)
							.setAttackWeapon(weaponDef);
				else
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage((int) (getSanguinestiMaxDamage() * damageBoost)).clientDelay(duration)
							.setAttackWeapon(weaponDef);
			} else
				hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
						.randDamage((int) (getSanguinestiMaxDamage() * damageBoost)).clientDelay(duration)
						.setAttackWeapon(weaponDef);
			int damage = target.hit(hit);
			if (target != null && target.isNpc()) {
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ENHANCED_MAGICAL_WEAPONS)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ENHANCED_MAGICAL_WEAPONS);
					EnhancedMagicalWeapons c = (EnhancedMagicalWeapons) player.getPlayerPerkHandler().getActivePerks(player)
							.get(perkIndex).getPerk(player);
					assert c != null;
					damage += (damage * c.getDamageBoost());
				}
			}
			if (damage > 0) {
				if (hit.attackStyle == AttackStyle.MAGIC) {
					if (hit.damage > 0) {
						if (target != null && target.isNpc() && target.getHp() > 0) {
							if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FINEST_WIZARDRY)) {
								int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FINEST_WIZARDRY);
								FinestWizardry c = (FinestWizardry) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
										.getPerk(player);
								assert c != null;
								if (Random.rollPercent(c.getExtraHitChance())) {
									doubleAttackEffect = true;
								}
							}
						}
					}
				}
				if (Random.rollDie(6, 1)) {
					player.incrementHp(hit.damage / 2);
					if (target != null)
						target.graphics(1542, 90, duration);
				} else {
					if (target != null)
						target.graphics(1541, 90, duration);
				}
			} else {
				hit.nullify();
				if (target != null)
					target.graphics(85, 92, duration);
			}
			updateLastAttack(doubleAttackEffect ? 1 : baseAttackSpeed + attackTicks);
		}
		if (weaponDef.id == TumekensShadow.CHARGED || weaponDef.id == 33000) {// 9544 = fang special anim --- 9471 = fang
																																					// normal hit anim
			if (target.player != null) {
				player.sendMessage(Color.RED.wrap("This staff's spell cannot be used against other players."));
				return;
			}
			player.animate(9493);
			player.graphics(weaponDef.id == 33000 ? 4201 : 2125, 92, 0);
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.GOD_WARS_VETERAN)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.GOD_WARS_VETERAN);
				GodWarsVeteran c = (GodWarsVeteran) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				if (target.npc.getDef().id == NpcID.GENERAL_GRAARDOR || target.npc.getId() == NpcID.COMMANDER_ZILYANA
						|| target.npc.getId() == NpcID.KREEARRA || target.npc.getId() == NpcID.KRIL_TSUTSAROTH ||
						target.npc.getDef().name.contains("nex")) {
					attackBoost += c.getAccuracyBonus();
					damageBoost += c.getDamageBonus();
				}
			}
			if (target.isNpc()) {
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ENHANCED_MAGICAL_WEAPONS)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ENHANCED_MAGICAL_WEAPONS);
					EnhancedMagicalWeapons c = (EnhancedMagicalWeapons) player.getPlayerPerkHandler().getActivePerks(player)
							.get(perkIndex).getPerk(player);
					damageBoost += (damageBoost * c.getDamageBoost());
				}
			}
			int duration = new Projectile(weaponDef.id == 33000 ? 4202 : 2126, 62, 31, 56, 56, 10, 32, 11).send(player,
					target);
			Hit hit;
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
					&& target.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				int chance = 25 - (level * 2);
				if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
					int delay = 50 - (level * 3);
					player.soulReaverDelay.delay(delay);
					String name = target.npc.getDef().name;
					if (name.contains("dummy"))
						return;
					target.hit(new Hit(player).fixedDamage((int) (target.npc.getHp() * multiplier)).clientDelay(duration)
							.setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
					return;
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
					player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
				int minimumHit = 0;
				int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				minimumHit = (int) ((int) (getShadowMaxHit(player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 33000)
						* damageBoost) * multiplier);
				int chance = 20 - (level * 2);
				if (Random.get(chance) == 0)
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage(minimumHit,
									(int) ((getShadowMaxHit(player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 33000))
											* damageBoost))
							.ignoreDefence().ignorePrayer()
							.clientDelay(duration).setAttackWeapon(weaponDef);
				else
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage((int) ((getShadowMaxHit(player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 33000))
									* damageBoost))
							.clientDelay(duration).setAttackWeapon(weaponDef);
			} else
				hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
						.randDamage((int) ((getShadowMaxHit(player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 33000))
								* damageBoost))
						.clientDelay(duration).setAttackWeapon(weaponDef);

			// NOTE: target might become null after this
			int damage = target.hit(hit);
			if (damage > 0) {
				if (target != null && target.isNpc() && target.getHp() > 0) {
					if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FINEST_WIZARDRY)) {
						int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FINEST_WIZARDRY);
						FinestWizardry c = (FinestWizardry) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
								.getPerk(player);
						if (Random.rollPercent(c.getExtraHitChance())) {
							doubleAttackEffect = true;
						}
					}
				}
				if (target != null) {
					target.graphics(weaponDef.id == 33000 ? 4200 : 2127, 90, duration);
				}
			} else {
				if (target != null) {
					target.graphics(85, 92, duration);
				}
				hit.nullify();
			}
			updateLastAttack(baseAttackSpeed + attackTicks);
			if (doubleAttackEffect) {
				target.hit(new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
						.randDamage((int) ((getShadowMaxHit(player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 33000))
								* damageBoost))
						.clientDelay(duration).setAttackWeapon(weaponDef));
			}
		}

		if (weaponDef.id == 30593) {
			player.animate(1167);
			player.graphics(2079, 92, 0);
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.GOD_WARS_VETERAN)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.GOD_WARS_VETERAN);
				GodWarsVeteran c = (GodWarsVeteran) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				if (target.npc.getDef().id == NpcID.GENERAL_GRAARDOR || target.npc.getId() == NpcID.COMMANDER_ZILYANA
						|| target.npc.getId() == NpcID.KREEARRA || target.npc.getId() == NpcID.KRIL_TSUTSAROTH ||
						target.npc.getDef().name.contains("nex")) {
					attackBoost += c.getAccuracyBonus();
					damageBoost += c.getDamageBonus();
				}
			}
			if (target.isNpc()) {
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ENHANCED_MAGICAL_WEAPONS)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ENHANCED_MAGICAL_WEAPONS);
					EnhancedMagicalWeapons c = (EnhancedMagicalWeapons) player.getPlayerPerkHandler().getActivePerks(player)
							.get(perkIndex).getPerk(player);
					damageBoost += (damageBoost * c.getDamageBoost());
				}
			}
			int duration = new Projectile(2075, 62, 31, 56, 56, 10, 32, 11).send(player, target);
			Hit hit;
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
					&& target.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				int chance = 25 - (level * 2);
				if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
					int delay = 50 - (level * 3);
					player.soulReaverDelay.delay(delay);
					String name = target.npc.getDef().name;
					if (name.contains("dummy"))
						return;
					target.hit(new Hit(player).fixedDamage((int) (target.npc.getHp() * multiplier)).clientDelay(duration)
							.setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
					return;
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
					player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
				int minimumHit = 0;
				int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				minimumHit = (int) ((int) (getDragonHunterStaffDamage() * damageBoost) * multiplier);
				int chance = 20 - (level * 2);
				if (Random.get(chance) == 0)
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage(minimumHit, (int) ((getDragonHunterStaffDamage()) * damageBoost)).ignorePrayer()
							.ignoreDefence().clientDelay(duration).setAttackWeapon(weaponDef);
				else
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage((int) ((getDragonHunterStaffDamage()) * damageBoost)).clientDelay(duration)
							.setAttackWeapon(weaponDef);
			} else
				hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
						.randDamage((int) ((getDragonHunterStaffDamage()) * damageBoost)).clientDelay(duration)
						.setAttackWeapon(weaponDef);
			int damage = target.hit(hit);

			if (damage > 0) {
				if (target.isNpc() && target.getHp() > 0) {
					if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FINEST_WIZARDRY)) {
						int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FINEST_WIZARDRY);
						FinestWizardry c = (FinestWizardry) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
								.getPerk(player);
						if (Random.rollPercent(c.getExtraHitChance())) {
							doubleAttackEffect = true;
						}
					}
				}
				target.graphics(2076, 90, duration);
			} else {
				hit.nullify();
				target.graphics(85, 92, duration);
			}
			updateLastAttack(doubleAttackEffect ? 1 : baseAttackSpeed + attackTicks);
		}
		if (weaponDef.id == HolySanguinestiStaff.CHARGED) {
			if (target.player != null) {
				player.sendMessage(Color.RED.wrap("This staff's spell cannot be used against other players."));
				return;
			}
			player.animate(1167);
			player.graphics(1540, 92, 0);
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.GOD_WARS_VETERAN)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.GOD_WARS_VETERAN);
				GodWarsVeteran c = (GodWarsVeteran) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				if (target.npc.getDef().id == NpcID.GENERAL_GRAARDOR || target.npc.getId() == NpcID.COMMANDER_ZILYANA
						|| target.npc.getId() == NpcID.KREEARRA || target.npc.getId() == NpcID.KRIL_TSUTSAROTH ||
						target.npc.getDef().name.contains("nex")) {
					attackBoost += c.getAccuracyBonus();
					damageBoost += c.getDamageBonus();
				}
			}
			if (target.isNpc()) {
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ENHANCED_MAGICAL_WEAPONS)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ENHANCED_MAGICAL_WEAPONS);
					EnhancedMagicalWeapons c = (EnhancedMagicalWeapons) player.getPlayerPerkHandler().getActivePerks(player)
							.get(perkIndex).getPerk(player);
					damageBoost += (damageBoost * c.getDamageBoost());
				}
			}
			attackBoost += 0.25;
			int duration = new Projectile(1539, 30, 0, 51, 56, 10, 16, 11).send(player, target);
			Hit hit;
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
					&& target.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				int chance = 25 - (level * 2);
				if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
					int delay = 50 - (level * 3);
					player.soulReaverDelay.delay(delay);
					String name = target.npc.getDef().name;
					if (name.contains("dummy"))
						return;
					target.hit(new Hit(player).fixedDamage((int) (target.npc.getHp() * multiplier)).clientDelay(duration)
							.setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
					return;
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
					player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
				int minimumHit = 0;
				int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				minimumHit = (int) ((int) (getSanguinestiMaxDamage() * damageBoost) * multiplier);
				int chance = 20 - (level * 2);
				if (Random.get(chance) == 0)
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage(minimumHit, (int) (getSanguinestiMaxDamage() * damageBoost)).clientDelay(duration)
							.setAttackWeapon(weaponDef);
				else
					hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
							.randDamage((int) (getSanguinestiMaxDamage() * damageBoost)).clientDelay(duration)
							.setAttackWeapon(weaponDef);
			} else
				hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).boostAttack(attackBoost)
						.randDamage((int) (getSanguinestiMaxDamage() * damageBoost)).clientDelay(duration)
						.setAttackWeapon(weaponDef);
			int damage = target.hit(hit);

			if (damage > 0) {
				if (target != null && target.isNpc() && target.getHp() > 0) {
					if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FINEST_WIZARDRY)) {
						int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FINEST_WIZARDRY);
						FinestWizardry c = (FinestWizardry) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
								.getPerk(player);
						if (Random.rollPercent(c.getExtraHitChance())) {
							doubleAttackEffect = true;
						}
					}
				}

				if (Random.rollDie(6, 1)) {
					player.incrementHp(hit.damage / 2);
					target.graphics(1542, 90, duration);
				} else {
					target.graphics(1541, 90, duration);
				}
			} else {
				hit.nullify();
				target.graphics(85, 92, duration);
			}
			updateLastAttack(doubleAttackEffect ? 1 : baseAttackSpeed + attackTicks);
		}
	}

	private void attackWithGauntletStaff(int maxHit, boolean corrupted) {
		boolean doubleAttackEffect = false;
		ObjType weapon = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
		if (target.player != null) {
			player.sendMessage(Color.RED.wrap("This staff's spell cannot be used against other players."));
			return;
		}
		player.animate(1167);
		player.graphics(corrupted ? 1722 : 1719, 92, 0);
		final int distance = getChebyshevDistance(player, target);
		final int delayTicks = getMagicHitDelay(distance);
		int duration = new Projectile(corrupted ? 1723 : 1720, 30, 0, 51, 56, 10, 16, 11).send(player, target);
		int baseAttackSpeed = 4;
		double attackBoost = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ENHANCED_MAGICAL_WEAPONS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ENHANCED_MAGICAL_WEAPONS);
			EnhancedMagicalWeapons c = (EnhancedMagicalWeapons) player.getPlayerPerkHandler().getActivePerks(player)
					.get(perkIndex).getPerk(player);
			assert c != null;
			attackBoost += c.getAccuracyBoost();
			switch (c.getPerkLevel()) {
				case 1:
					baseAttackSpeed = 4;
					break;
				case 2:
				case 3:
					baseAttackSpeed = 3;
					break;
				case 4:
				case 5:
					baseAttackSpeed = 2;
					break;
			}
		}
		if (player.berserkerRageActive)
			baseAttackSpeed /= 2;

		if (target.isNpc() && target.getHp() > 0) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.FINEST_WIZARDRY)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.FINEST_WIZARDRY);
				FinestWizardry c = (FinestWizardry) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				if (Random.rollPercent(c.getExtraHitChance())) {
					doubleAttackEffect = true;
				}
			}
		}
		Hit hit = new Hit(player, AttackStyle.MAGIC, getAttackType()).randDamage(maxHit).boostAttack(attackBoost)
				.delay(delayTicks).setAttackWeapon(weapon);
		target.hit(hit);
		target.graphics(corrupted ? 1724 : 1721, 90, duration);
		updateLastAttack(doubleAttackEffect ? 1 : baseAttackSpeed);
	}

	public int getSanguinestiMaxDamage() {
		int damage = (player.getStats().get(StatType.Magic).currentLevel / 3) - 1;
		damage *= (1 + (player.getEquipment().bonuses[EquipmentStats.MAGIC_DAMAGE] / 100.0));
		if (player.damageBoostActive())
			damage *= 1.2;
		if (target instanceof NPC) {
			double damageBoost = 1 + target.npc.getDamageBoostFromWeakness();
			if (!target.npc.getDef().name.contains("dummy"))
				damage *= damageBoost;
		}
		if (player.getEquipment().get(12) != null && player.getEquipment().get(12).getId() == 24313
				&& isTargetMythical(target)) {
			damage *= 1.2;
		}
		if (target.isNpc()) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ENHANCED_MAGICAL_WEAPONS)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ENHANCED_MAGICAL_WEAPONS);
				EnhancedMagicalWeapons c = (EnhancedMagicalWeapons) player.getPlayerPerkHandler().getActivePerks(player)
						.get(perkIndex).getPerk(player);
				damage += (damage * c.getDamageBoost());
			}
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ENHANCED_MAGICAL_WEAPONS)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ENHANCED_MAGICAL_WEAPONS);
				EnhancedMagicalWeapons c = (EnhancedMagicalWeapons) player.getPlayerPerkHandler().getActivePerks(player)
						.get(perkIndex).getPerk(player);
				damage += (damage * c.getDamageBoost());
			}

		}
		return damage;
	}

	public int getTridentMaxDamage(boolean swamp) {

		int damage = (player.getStats().get(StatType.Magic).currentLevel / 3) - 5;
		if (swamp)
			damage = (player.getStats().get(StatType.Magic).currentLevel / 3) - 2;
		damage *= (1 + (player.getEquipment().bonuses[EquipmentStats.MAGIC_DAMAGE] / 100.0));
		if (player.damageBoostActive())
			damage *= 1.2;
		if (player.getEquipment().get(12) != null && player.getEquipment().get(12).getId() == 24313
				&& isTargetMythical(target)) {
			damage *= 1.2;
		}
		if (target instanceof NPC) {
			double damageBoost = 1 + target.npc.getDamageBoostFromWeakness();
			if (!target.npc.getDef().name.contains("dummy"))
				damage *= damageBoost;
		}
		if (target.isNpc()) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ENHANCED_MAGICAL_WEAPONS)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ENHANCED_MAGICAL_WEAPONS);
				EnhancedMagicalWeapons c = (EnhancedMagicalWeapons) player.getPlayerPerkHandler().getActivePerks(player)
						.get(perkIndex).getPerk(player);
				damage += (damage * c.getDamageBoost());
			}

		}

		return damage;
	}

	public int getShadowMaxHit(boolean upgraded) {
		int damage = (player.getStats().get(StatType.Magic).currentLevel / 3) + 1;
		damage *= (1 + (player.getEquipment().bonuses[EquipmentStats.MAGIC_DAMAGE] / 100.0));
		if (player.damageBoostActive())
			damage *= 1.2;
		if (player.getEquipment().get(12) != null && player.getEquipment().get(12).getId() == 24313
				&& isTargetMythical(target)) {
			damage *= 1.2;
		}
		if (target instanceof NPC) {
			double damageBoost = 1 + target.npc.getDamageBoostFromWeakness();
			if (!target.npc.getDef().name.contains("dummy"))
				damage *= damageBoost;
		}
		if (target.isNpc()) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ENHANCED_MAGICAL_WEAPONS)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ENHANCED_MAGICAL_WEAPONS);
				EnhancedMagicalWeapons c = (EnhancedMagicalWeapons) player.getPlayerPerkHandler().getActivePerks(player)
						.get(perkIndex).getPerk(player);
				damage += (damage * c.getDamageBoost());
			}

		}
		if (upgraded)
			damage *= 1.2;
		return damage;
	}

	public int getDragonHunterStaffDamage() {
		int damage = (player.getStats().get(StatType.Magic).currentLevel / 3) - 3;
		damage *= (1 + (player.getEquipment().bonuses[EquipmentStats.MAGIC_DAMAGE] / 100.0));
		if (player.damageBoostActive())
			damage *= 1.2;
		if (target instanceof NPC) {
			double damageBoost = 1 + target.npc.getDamageBoostFromWeakness();
			if (!target.npc.getDef().name.contains("dummy"))
				damage *= damageBoost;
		}
		if (player.getEquipment().get(12) != null && player.getEquipment().get(12).getId() == 24313
				&& isTargetMythical(target)) {
			damage *= 1.2;
		}
		if (target.isNpc()) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ENHANCED_MAGICAL_WEAPONS)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ENHANCED_MAGICAL_WEAPONS);
				EnhancedMagicalWeapons c = (EnhancedMagicalWeapons) player.getPlayerPerkHandler().getActivePerks(player)
						.get(perkIndex).getPerk(player);
				damage += (damage * c.getDamageBoost());
			}

		}

		return damage;
	}

	public int getTrainingStaffMaxDamage(Player player) {
		int damage = 5 + (player.getStats().get(StatType.Magic).currentLevel / 3) - 2;
		damage *= (1 + (player.getEquipment().bonuses[EquipmentStats.MAGIC_DAMAGE] / 100.0));
		if (target.isNpc()) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ENHANCED_MAGICAL_WEAPONS)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ENHANCED_MAGICAL_WEAPONS);
				EnhancedMagicalWeapons c = (EnhancedMagicalWeapons) player.getPlayerPerkHandler().getActivePerks(player)
						.get(perkIndex).getPerk(player);
				damage += (damage * c.getDamageBoost());
			}
		}
		return damage;
	}

	public int simulateAttackWithMelee(boolean blockOtherStyles, boolean animate, boolean updateLastAttack,
			AttackStyle bonusStyle, int bonusDamage) {
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		if (blockOtherStyles) {
			if (style == AttackStyle.RANGED || style == AttackStyle.MAGIC) {
				player.sendMessage("You can't attack that with " + Common.formatNoun(style.toString()) + "!");
				return -1;
			}
		}
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		if (bonusStyle != null) {
			if (style.equals(bonusStyle)) {
				maxDamage += bonusDamage; // increasing the maxDamage does give you a better chance of winning this
				// simulated roll.
			}
		}
		if (animate) {
			attackAnim();
		}
		if (updateLastAttack) {
			updateLastAttack(weaponType.attackTicks);
		}

		int randomDamage = Random.get(0, maxDamage);

		return randomDamage;
	}

	private void attackAnim() {
		if (attackSet.attackAnimation != null)
			player.animate(attackSet.attackAnimation);
		else
			player.animate(weaponType.attackAnimation);
		if (weaponType.attackSound != -1)
			player.publicSound(weaponType.attackSound, 1, 1);
	}

	private void ScytheOfVitur() {
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);

		if (player.getEquipment().contains(22325)) {
			if (target.isPlayer()) {
				player.graphics(1231, 100, 16);
			} else {
				player.graphics(1231, 100, 16);
				target.hit(
						new Hit(player, style, type).randDamage(maxDamage / 2)
								.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)),
						new Hit(player, style, type).randDamage(maxDamage / 3)
								.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)));
			}
		}
	}

	private void HolyscytheofVitur() {
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);

		if (player.getEquipment().contains(25971)) {
			if (target.isPlayer()) {
				player.graphics(1232, 100, 16);
			} else {
				player.graphics(1232, 100, 16);
				target.hit(
						new Hit(player, style, type).randDamage(maxDamage / 2)
								.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)),
						new Hit(player, style, type).randDamage(maxDamage / 3)
								.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)));
			}
		}
	}

	private void SanguinescytheofVitur() {
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);

		if (player.getEquipment().contains(25967)) {
			if (target.isPlayer()) {
				player.graphics(1231, 100, 16);
			} else {
				player.graphics(1231, 100, 16);
				target.hit(
						new Hit(player, style, type).randDamage(maxDamage / 2)
								.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)),
						new Hit(player, style, type).randDamage(maxDamage / 3)
								.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)));
			}
		}
	}

	private void ScytheOfViturr() {
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);

		if (player.getEquipment().contains(22325)) {
			if (target.isPlayer()) {
				player.graphics(1231, 100, 16);
			} else if (target.getSize() >= 2) {
				player.graphics(1231, 100, 16);
				target.hit(
						new Hit(player, style, type).randDamage(maxDamage / 2)
								.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)),
						new Hit(player, style, type).randDamage(maxDamage / 3)
								.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)));
			} else if (target.getSize() >= 3) {
				ScytheOfViturMulti();
			}
		}
	}

	private void ScytheOfViturMulti() {
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		double attackBoost = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.TAKE_YOUR_TIME)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.TAKE_YOUR_TIME);
				TakeYourTime c = (TakeYourTime) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				attackBoost += c.getAccuracyBoost();
			}
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_SLASHER)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_SLASHER);
				TheSlasher c = (TheSlasher) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
				if (style == AttackStyle.SLASH)
					attackBoost += c.getAccuracyBoost();
			}
		}
		player.graphics(1231, 100, 16);
		if (target.inMulti()) {
			int entityIndex = player.getClientIndex();
			int targetIndex = target.getClientIndex();
			int targetCount = 0;
			for (Player plr : target.localPlayers()) {
				int playerIndex = plr.getClientIndex();
				if (playerIndex == entityIndex || playerIndex == targetIndex)
					continue;
				if (!plr.getPosition().isWithinDistance(target.getPosition(), 1))
					continue;
				if (!player.getCombat().canAttack(plr, false))
					continue;
				plr.hit(new Hit(player, style, type).randDamage(maxDamage)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)));
				if (player.hit() >= 3)
					break;
			}
			for (NPC npc : target.localNpcs()) {
				int npcIndex = npc.getClientIndex();
				if (npcIndex == entityIndex || npcIndex == targetIndex)
					continue;
				if (!npc.getPosition().isWithinDistance(target.getPosition(), 1))
					continue;
				if (npc.getDef().ignoreMultiCheck)
					continue;
				if (!player.getCombat().canAttack(npc, false))
					continue;
				npc.hit(new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)));
				if (++targetCount >= 3)
					break;
			}
		}

		player.graphics(1231, 100, 16);
		if (target.getSize() == 1) {
			if (target.inMulti()) {
				double finalAttackBoost = attackBoost;
				target.forLocalEntity(entity -> {
					int targetCount = 0;
					for (NPC npc : target.localNpcs()) {
						if (Misc.getDistance(entity.getPosition(), target.getPosition()) > 1)
							return;
						if (!player.getCombat().canAttack(entity, false))
							return;
						npc.hit(new Hit(player, style, type).boostAttack(finalAttackBoost).randDamage(maxDamage)
								.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)));
						if (++targetCount >= 3)
							break;
					}
				});
			}
		}
	}

	/**
	 * Melee
	 */
	private void attackWithScythe(Player player, Entity target) {
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		int attackTicks = weaponType.attackTicks;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SPEEDY_STRIKES) && target.isNpc()) {
			int perkIndex = 0;
			for (int i = 0; i < player.getPlayerPerkHandler().getActivePerks(player).size(); i++) {
				if (player.getPlayerPerkHandler().getActivePerks(player).get(i).getPerk(player).getPerkName()
						.equalsIgnoreCase(Perks.SPEEDY_STRIKES.getPerk(player).getPerkName()))
					perkIndex = i;
			}
			SpeedyStrikes c = (SpeedyStrikes) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			double multiplier = 1 + c.getTickReductionAmount();
			// TODO: a gfx or something
			attackTicks = (int) (attackTicks / multiplier);

		}
		if (player.berserkerRageActive && target.isNpc())
			attackTicks /= 2;
		double attackBoost = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.TAKE_YOUR_TIME) && target.isNpc()) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.TAKE_YOUR_TIME);
				TakeYourTime c = (TakeYourTime) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				attackTicks += 2;
				attackBoost += c.getAccuracyBoost();
			}
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.GOD_WARS_VETERAN)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.GOD_WARS_VETERAN);
			GodWarsVeteran c = (GodWarsVeteran) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			if (target.npc.getDef().id == NpcID.GENERAL_GRAARDOR || target.npc.getId() == NpcID.COMMANDER_ZILYANA
					|| target.npc.getId() == NpcID.KREEARRA || target.npc.getId() == NpcID.KRIL_TSUTSAROTH ||
					target.npc.getDef().name.contains("nex")) {
				double damageBoost = 1;
				damageBoost += c.getDamageBonus();
				maxDamage *= damageBoost;
				attackBoost += c.getAccuracyBonus();
			}
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_SLASHER) && target.isNpc()) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_SLASHER);
				TheSlasher c = (TheSlasher) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
				if (style == AttackStyle.SLASH)
					attackBoost += c.getAccuracyBoost();
			}
		}
		boolean doubleAttackEffect = false;
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.DOUBLE_HIT)
					&& target.isNpc()) {
				if (DoubleHit.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON), target, getAttackStyle()))
					doubleAttackEffect = true;
			}
		}
		updateLastAttack(doubleAttackEffect ? 1 : attackTicks);
		if (handleSpecial(style, type, maxDamage))
			return;
		attackAnim();
		final int secondHitDmg = (int) (maxDamage * 0.5f);
		final int thirdHitDmg = (int) (maxDamage * 0.25f);
		final Hit firstHit;
		final Hit secondHit;
		final Hit thirdHit;

		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
				player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
			int minimumHit = 0;
			int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
					player.getEquipment().get(Equipment.SLOT_WEAPON));
			double multiplier = 0.02 * level;
			minimumHit = (int) ((int) (maxDamage) * multiplier);
			int chance = 20 - (level * 2);
			if (Random.get(chance) == 0) {
				firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, maxDamage)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, secondHitDmg)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, thirdHitDmg)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			} else {
				firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(secondHitDmg)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(thirdHitDmg)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			}
		} else {
			firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(secondHitDmg)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(thirdHitDmg)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
		}

		player.graphics(1231, 100, 20);
		if (target.getSize() >= 2 || target.npc.getId() == 2668 || target.npc.getId() == 10507) {
			target.hit(firstHit, secondHit, thirdHit);
		} else {
			target.hit(firstHit);
		}
	}

	private void attackWithOsmumtensScythe(Player player, Entity target) {
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		int attackTicks = weaponType.attackTicks;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SPEEDY_STRIKES) && target.isNpc()) {
			int perkIndex = 0;
			for (int i = 0; i < player.getPlayerPerkHandler().getActivePerks(player).size(); i++) {
				if (player.getPlayerPerkHandler().getActivePerks(player).get(i).getPerk(player).getPerkName()
						.equalsIgnoreCase(Perks.SPEEDY_STRIKES.getPerk(player).getPerkName()))
					perkIndex = i;
			}
			SpeedyStrikes c = (SpeedyStrikes) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			double multiplier = 1 + c.getTickReductionAmount();
			// TODO: a gfx or something
			attackTicks = (int) (attackTicks / multiplier);

		}
		if (player.berserkerRageActive && target.isNpc())
			attackTicks /= 2;
		double attackBoost = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.TAKE_YOUR_TIME) && target.isNpc()) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.TAKE_YOUR_TIME);
				TakeYourTime c = (TakeYourTime) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				attackTicks += 2;
				attackBoost += c.getAccuracyBoost();
			}
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.GOD_WARS_VETERAN)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.GOD_WARS_VETERAN);
			GodWarsVeteran c = (GodWarsVeteran) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			if (target.npc.getDef().id == NpcID.GENERAL_GRAARDOR || target.npc.getId() == NpcID.COMMANDER_ZILYANA
					|| target.npc.getId() == NpcID.KREEARRA || target.npc.getId() == NpcID.KRIL_TSUTSAROTH ||
					target.npc.getDef().name.contains("nex")) {
				double damageBoost = 1;
				damageBoost += c.getDamageBonus();
				maxDamage *= damageBoost;
				attackBoost += c.getAccuracyBonus();
			}
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_SLASHER) && target.isNpc()) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_SLASHER);
				TheSlasher c = (TheSlasher) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
				if (style == AttackStyle.SLASH)
					attackBoost += c.getAccuracyBoost();
			}
		}
		boolean doubleAttackEffect = false;
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.DOUBLE_HIT)
					&& target.isNpc()) {
				if (DoubleHit.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON), target, getAttackStyle()))
					doubleAttackEffect = true;
			}
		}
		updateLastAttack(doubleAttackEffect ? 1 : attackTicks);
		if (handleSpecial(style, type, maxDamage))
			return;
		attackAnim();
		final int secondHitDmg = (int) (maxDamage * 0.75f);
		final int thirdHitDmg = (int) (maxDamage * 0.5f);
		final int fourthHitDMg = (int) (maxDamage * 0.25f);
		final Hit firstHit;
		final Hit secondHit;
		final Hit thirdHit;
		final Hit fourthHit;

		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
				player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
			int minimumHit = 0;
			int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
					player.getEquipment().get(Equipment.SLOT_WEAPON));
			double multiplier = 0.02 * level;
			minimumHit = (int) ((int) (maxDamage) * multiplier);
			int chance = 20 - (level * 2);
			if (Random.get(chance) == 0) {
				firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, maxDamage)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, secondHitDmg)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, thirdHitDmg)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				fourthHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, fourthHitDMg)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			} else {
				firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(secondHitDmg)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(thirdHitDmg)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				fourthHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(fourthHitDMg)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			}
		} else {
			firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(secondHitDmg)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(thirdHitDmg)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			fourthHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(fourthHitDMg)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
		}

		player.graphics(1231, 100, 20);
		if (target.isPlayer()) {
			target.hit(firstHit);
		} else if (target.getSize() >= 2 || target.npc.getId() == 2668 || target.npc.getId() == 10507) {
			target.hit(firstHit, secondHit, thirdHit, fourthHit);
		} else {
			target.hit(firstHit, secondHit);
		}
	}

	//@formatter:off
	private void attackWithThunderKhopseh(Player player, Entity target) {
		if (target == null) {
			return;
		}

		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		int attackTicks = weaponType.attackTicks;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SPEEDY_STRIKES) && target.isNpc()) {
			int perkIndex = 0;
			for (int i = 0; i < player.getPlayerPerkHandler().getActivePerks(player).size(); i++) {
				if (player.getPlayerPerkHandler().getActivePerks(player).get(i).getPerk(player).getPerkName().equalsIgnoreCase(Perks.SPEEDY_STRIKES.getPerk(player).getPerkName())) {
					perkIndex = i;
				}
			}
			SpeedyStrikes c = (SpeedyStrikes) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
			double multiplier = 1 + c.getTickReductionAmount();
			// TODO: a gfx or something
			attackTicks = (int) (attackTicks / multiplier);

		}

		if (player.berserkerRageActive && target.isNpc()) {
			attackTicks /= 2;
		}

		double attackBoost = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.TAKE_YOUR_TIME) && target.isNpc()) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.TAKE_YOUR_TIME);
				TakeYourTime c = (TakeYourTime) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
				attackTicks += 2;
				attackBoost += c.getAccuracyBoost();
			}
		}

		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.GOD_WARS_VETERAN)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.GOD_WARS_VETERAN);
			GodWarsVeteran c = (GodWarsVeteran) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
			if (target.npc.getDef().id == NpcID.GENERAL_GRAARDOR || target.npc.getId() == NpcID.COMMANDER_ZILYANA
					|| target.npc.getId() == NpcID.KREEARRA || target.npc.getId() == NpcID.KRIL_TSUTSAROTH ||
					target.npc.getDef().name.contains("nex")) {
				double damageBoost = 1;
				damageBoost += c.getDamageBonus();
				maxDamage *= damageBoost;
				attackBoost += c.getAccuracyBonus();
			}
		}

		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_SLASHER) && target.isNpc()) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_SLASHER);
				TheSlasher c = (TheSlasher) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
				if (style == AttackStyle.SLASH) {
					attackBoost += c.getAccuracyBoost();
				}
			}
		}

		boolean doubleAttackEffect = false;
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.DOUBLE_HIT)
					&& target.isNpc()) {
				if (DoubleHit.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON), target, getAttackStyle()))
					doubleAttackEffect = true;
			}
		}
		updateLastAttack(doubleAttackEffect ? 1 : attackTicks);
		if (handleSpecial(style, type, maxDamage)) {
			return;
		}

		attackAnim();
		final int secondHitDmg = (int) (maxDamage * 0.75f);
		final int thirdHitDmg = (int) (maxDamage * 0.5f);
		Hit firstHit;
		final Hit secondHit;
		final Hit thirdHit;

		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
				player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
			int minimumHit;
			int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
					player.getEquipment().get(Equipment.SLOT_WEAPON));
			double multiplier = 0.02 * level;
			minimumHit = (int) (maxDamage * multiplier);
			int chance = 20 - (level * 2);
			if (Random.get(chance) == 0) {
				firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, maxDamage)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, secondHitDmg)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, thirdHitDmg)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			} else {
				firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(secondHitDmg)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(thirdHitDmg)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			}
		} else {
			firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(secondHitDmg)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(thirdHitDmg)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
		}

		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER) && target.isNpc()) {
			int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER, player.getEquipment().get(Equipment.SLOT_WEAPON));
			double multiplier = 0.02 * level;
			int chance = 25 - (level * 2);
			if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
				int delay = 50 - (level * 3);
				player.soulReaverDelay.delay(delay);
				String name = target.npc.getDef().name;
				if (name.contains("dummy")) {
					return;
				}

				firstHit = new Hit(player, style, type)
						.fixedDamage((int) (target.npc.getHp() * multiplier))
						.setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef());
			}
		}

		if (target.isPlayer()) {
			target.hit(firstHit);
		} else if (target.getSize() >= 2 || target.npc.getId() == 2668 || target.npc.getId() == 10507) {
			target.hit(firstHit, secondHit, thirdHit);
		} else {
			target.hit(firstHit, secondHit);
		}
	}

	private void attackWithHolyScythe(Player player, Entity target) {
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		int attackTicks = weaponType.attackTicks;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SPEEDY_STRIKES) && target.isNpc()) {
			int perkIndex = 0;
			for (int i = 0; i < player.getPlayerPerkHandler().getActivePerks(player).size(); i++) {
				if (player.getPlayerPerkHandler().getActivePerks(player).get(i).getPerk(player).getPerkName()
						.equalsIgnoreCase(Perks.SPEEDY_STRIKES.getPerk(player).getPerkName()))
					perkIndex = i;
			}
			SpeedyStrikes c = (SpeedyStrikes) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			double multiplier = 1 + c.getTickReductionAmount();

			// TODO: a gfx or something
			attackTicks = (int) (attackTicks / multiplier);

		}
		if (player.berserkerRageActive && target.isNpc())
			attackTicks /= 2;
		double attackBoost = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.TAKE_YOUR_TIME) && target.isNpc()) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.TAKE_YOUR_TIME);
				TakeYourTime c = (TakeYourTime) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				attackTicks += 2;
				attackBoost += c.getAccuracyBoost();
			}
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.GOD_WARS_VETERAN)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.GOD_WARS_VETERAN);
			GodWarsVeteran c = (GodWarsVeteran) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			if (target.npc.getDef().id == NpcID.GENERAL_GRAARDOR || target.npc.getId() == NpcID.COMMANDER_ZILYANA
					|| target.npc.getId() == NpcID.KREEARRA || target.npc.getId() == NpcID.KRIL_TSUTSAROTH ||
					target.npc.getDef().name.contains("nex")) {
				double damageBoost = 1;
				damageBoost += c.getDamageBonus();
				maxDamage *= damageBoost;
				attackBoost += c.getAccuracyBonus();
			}
		}
		boolean doubleAttackEffect = false;
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.DOUBLE_HIT)
					&& target.isNpc()) {
				if (DoubleHit.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON), target, getAttackStyle()))
					doubleAttackEffect = true;
			}
		}
		updateLastAttack(doubleAttackEffect ? 1 : attackTicks);
		if (handleSpecial(style, type, maxDamage))
			return;
		attackAnim();
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_SLASHER) && target.isNpc()) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_SLASHER);
				TheSlasher c = (TheSlasher) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
				if (style == AttackStyle.SLASH)
					attackBoost += c.getAccuracyBoost();
			}
		}
		final int secondHitDmg = (int) (maxDamage * 0.5f);
		final int thirdHitDmg = (int) (maxDamage * 0.25f);
		final Hit firstHit;
		final Hit secondHit;
		final Hit thirdHit;
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
				player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
			int minimumHit = 0;
			int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
					player.getEquipment().get(Equipment.SLOT_WEAPON));
			double multiplier = 0.02 * level;
			minimumHit = (int) ((int) (maxDamage) * multiplier);
			int chance = 20 - (level * 2);
			if (Random.get(chance) == 0) {
				firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, maxDamage)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, secondHitDmg)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, thirdHitDmg)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			} else {
				firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(secondHitDmg)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(thirdHitDmg)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			}
		} else {
			firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(secondHitDmg)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(thirdHitDmg)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
		}
		player.graphics(1898, 100, 20);
		if (target.getSize() >= 2 || target.npc.getId() == 2668 || target.npc.getId() == 10507) {
			target.hit(firstHit, secondHit, thirdHit);
		} else {
			target.hit(firstHit);
		}
	}

	private void attackWithSanguineScythe(Player player, Entity target) {
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		int attackTicks = weaponType.attackTicks;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SPEEDY_STRIKES) && target.isNpc()) {
			int perkIndex = 0;
			for (int i = 0; i < player.getPlayerPerkHandler().getActivePerks(player).size(); i++) {
				if (player.getPlayerPerkHandler().getActivePerks(player).get(i).getPerk(player).getPerkName()
						.equalsIgnoreCase(Perks.SPEEDY_STRIKES.getPerk(player).getPerkName()))
					perkIndex = i;
			}
			SpeedyStrikes c = (SpeedyStrikes) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			double multiplier = 1 + c.getTickReductionAmount();

			// TODO: a gfx or something
			attackTicks = (int) (attackTicks / multiplier);

		}
		if (player.berserkerRageActive && target.isNpc())
			attackTicks /= 2;
		double attackBoost = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.TAKE_YOUR_TIME) && target.isNpc()) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.TAKE_YOUR_TIME);
				TakeYourTime c = (TakeYourTime) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				attackTicks += 2;
				attackBoost += c.getAccuracyBoost();
			}
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.GOD_WARS_VETERAN)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.GOD_WARS_VETERAN);
			GodWarsVeteran c = (GodWarsVeteran) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			if (target.npc.getDef().id == NpcID.GENERAL_GRAARDOR || target.npc.getId() == NpcID.COMMANDER_ZILYANA
					|| target.npc.getId() == NpcID.KREEARRA || target.npc.getId() == NpcID.KRIL_TSUTSAROTH ||
					target.npc.getDef().name.contains("nex")) {
				double damageBoost = 1;
				damageBoost += c.getDamageBonus();
				maxDamage *= damageBoost;
				attackBoost += c.getAccuracyBonus();
			}
		}
		boolean doubleAttackEffect = false;
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.DOUBLE_HIT)
					&& target.isNpc()) {
				if (DoubleHit.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON), target, getAttackStyle()))
					doubleAttackEffect = true;
			}
		}
		updateLastAttack(doubleAttackEffect ? 1 : attackTicks);
		if (handleSpecial(style, type, maxDamage))
			return;
		attackAnim();
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_SLASHER)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_SLASHER);
				TheSlasher c = (TheSlasher) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
				if (style == AttackStyle.SLASH)
					attackBoost += c.getAccuracyBoost();
			}
		}

		final int secondHitDmg = (int) (maxDamage * 0.5f);
		final int thirdHitDmg = (int) (maxDamage * 0.25f);
		final Hit firstHit;
		final Hit secondHit;
		final Hit thirdHit;
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
				player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
			int minimumHit = 0;
			int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
					player.getEquipment().get(Equipment.SLOT_WEAPON));
			double multiplier = 0.02 * level;
			minimumHit = (int) ((int) (maxDamage) * multiplier);
			int chance = 20 - (level * 2);
			if (Random.get(chance) == 0) {
				firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, maxDamage)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, secondHitDmg)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, thirdHitDmg)
						.ignoreDefence().ignorePrayer().setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			} else {
				firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(secondHitDmg)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
				thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(thirdHitDmg)
						.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			}
		} else {
			firstHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			secondHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(secondHitDmg)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
			thirdHit = new Hit(player, style, type).boostAttack(attackBoost).randDamage(thirdHitDmg)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON));
		}
		player.graphics(1894, 100, 20);
		if (target.getSize() >= 2 || target.npc.getId() == 2668 || target.npc.getId() == 10507) {
			target.hit(firstHit, secondHit, thirdHit);
		} else {
			target.hit(firstHit);
		}
	}

	private void attackWithMelee() {
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		Item wep = player.getEquipment().get(Equipment.SLOT_WEAPON);
		if (wep != null && (wep.getId() == 59527 || wep.getId() == 30388) && target.isPlayer()) {
			player.sendMessage("You can't attack players with this weapon.");
			return;
		}
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		if (player.damageBoostActive())
			maxDamage *= 1.2;
		if (player.getEquipment().get(12) != null && player.getEquipment().get(12).getId() == 24313
				&& isTargetMythical(target)) {
			maxDamage *= 1.2;
		}
		if (target instanceof NPC) {
			double damageBoost = 1 + target.npc.getDamageBoostFromWeakness();
			if (!target.npc.getDef().name.contains("dummy")) {
				maxDamage *= damageBoost;
			}
		}
		int attackTicks = weaponType.attackTicks;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SPEEDY_STRIKES) && target != null
				&& target.isNpc()) {
			int perkIndex = 0;
			for (int i = 0; i < player.getPlayerPerkHandler().getActivePerks(player).size(); i++) {
				if (player.getPlayerPerkHandler().getActivePerks(player).get(i).getPerk(player).getPerkName()
						.equalsIgnoreCase(Perks.SPEEDY_STRIKES.getPerk(player).getPerkName()))
					perkIndex = i;
			}
			SpeedyStrikes c = (SpeedyStrikes) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			double multiplier = 1 + c.getTickReductionAmount();

			// TODO: a gfx or something
			attackTicks = (int) (attackTicks / multiplier);

		}
		if (player.berserkerRageActive && target.isNpc())
			attackTicks /= 2;
		double attackBoost = 0;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.TAKE_YOUR_TIME)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.TAKE_YOUR_TIME);
				TakeYourTime c = (TakeYourTime) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				attackTicks += 2;
				attackBoost += c.getAccuracyBoost();
			}
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.GOD_WARS_VETERAN) && target.isNpc()) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.GOD_WARS_VETERAN);
			GodWarsVeteran c = (GodWarsVeteran) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			if (target.npc.getDef().id == NpcID.GENERAL_GRAARDOR || target.npc.getId() == NpcID.COMMANDER_ZILYANA
					|| target.npc.getId() == NpcID.KREEARRA || target.npc.getId() == NpcID.KRIL_TSUTSAROTH ||
					target.npc.getDef().name.contains("nex")) {
				double damageBoost = 1;
				damageBoost += c.getDamageBonus();
				maxDamage *= damageBoost;
				attackBoost += c.getAccuracyBonus();
			}
		}

		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_CRUSHER) && target.isNpc()) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_CRUSHER);
				TheCrusher c = (TheCrusher) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
				if (style == AttackStyle.CRUSH)
					attackBoost += c.getAccuracyBoost();
			}
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_SLASHER) && target.isNpc()) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_SLASHER);
				TheSlasher c = (TheSlasher) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
				if (style == AttackStyle.SLASH)
					attackBoost += c.getAccuracyBoost();
			}
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_STABBER) && target.isNpc()) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_STABBER);
				TheStabber c = (TheStabber) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
				if (style == AttackStyle.STAB)
					attackBoost += c.getAccuracyBoost();
			}
		}

		boolean doubleAttackEffect = false;
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.DOUBLE_HIT)
					&& target.isNpc()) {
				if (DoubleHit.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON), target, getAttackStyle()))
					doubleAttackEffect = true;
			}
		}

		updateLastAttack(doubleAttackEffect ? 1 : attackTicks);

		ObjType weapon = player.getEquipment().getDef(Equipment.SLOT_WEAPON);

		defendAnim();
		if (handleSpecial(style, type, maxDamage))
			return;
		if (weapon == null) {
			attackAnim();
			target.hit(new Hit(player, style, type).randDamage(maxDamage)
					.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)));
		} else {
			// Khopseh
			if (weapon.id == 30388 && target.isNpc()) {
				attackWithThunderKhopseh(player, target);
				return;
			}

			boolean hasScytheEquipped = weapon.id == 22325;
			if (hasScytheEquipped && target.isNpc()) {
				attackWithScythe(player, target);
				return;
			}

			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 59527) {
				attackWithOsmumtensScythe(player, target);
				return;
			}

			boolean hasHolyScytheEquipped = weapon.id == 25736;
			if (hasHolyScytheEquipped && target.isNpc()) {
				attackWithHolyScythe(player, target);
				return;
			}

			boolean hasSanguineScytheEquipped = weapon.id == 25739;
			if (hasSanguineScytheEquipped && target.isNpc()) {
				attackWithSanguineScythe(player, target);
				return;
			} else if (weaponType != weaponType.UNARMED) {
				attackAnim();
				if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null
						&& AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON),
								AttributeTypes.SOUL_REAVER)
						&& target.isNpc()) {
					int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
							player.getEquipment().get(Equipment.SLOT_WEAPON));
					double multiplier = 0.02 * level;
					int chance = 25 - (level * 2);
					if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
						int delay = 50 - (level * 3);
						player.soulReaverDelay.delay(delay);
						String name = target.npc.getDef().name;
						if (name.contains("dummy")) {
							return;
						}
						target.hit(new Hit(player, style, type).fixedDamage((int) (target.npc.getHp() * multiplier))
								.setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
						return;
					}
				}
				if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
						player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
					int minimumHit = 0;
					int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
							player.getEquipment().get(Equipment.SLOT_WEAPON));
					double multiplier = 0.02 * level;
					minimumHit = (int) (maxDamage * multiplier);
					int chance = 20 - (level * 2);
					if (Random.get(chance) == 0)
						target.hit(new Hit(player, style, type).boostAttack(attackBoost).randDamage(minimumHit, maxDamage)
								.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)).ignorePrayer().ignoreDefence());
					else
						target.hit(new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage)
								.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)));
				} else
					target.hit(new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage)
							.setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)));
			}
		}
	}

	private boolean isTargetMythical(Entity target) {
		boolean mythical = false;
		if (target.isPlayer())
			mythical = false;
		if (target.isNpc() && target.npc.getDef() != null) {
			if (target.npc.getDef().name.toLowerCase().contains("dragon")) {
				mythical = true;
			} else if (target.npc.getId() == 6477) {
				mythical = true;
			} else if (target.npc.getDef().name.toLowerCase().contains("galvek")) {
				mythical = true;
			} else if (target.npc.getDef().name.toLowerCase().contains("gargoyle")) {
				mythical = true;
			} else if (target.npc.getDef().name.toLowerCase().contains("grotesque")) {
				mythical = true;
			} else if (target.npc.getDef().name.toLowerCase().contains("tainted")) {
				mythical = true;
			} else if (target.npc.getDef().name.toLowerCase().contains("nechryael")) {
				mythical = true;
			} else if (target.npc.getDef().name.toLowerCase().contains("bloodveld")) {
				mythical = true;
			} else if (target.npc.getDef().name.toLowerCase().contains("banshee")) {
				mythical = true;
			} else if (target.npc.getDef().name.toLowerCase().contains("basilisk")) {
				mythical = true;
			} else if (target.npc.getDef().name.toLowerCase().contains("hydra")) {
				mythical = true;
			} else if (target.npc.getDef().name.toLowerCase().contains("kraken")) {
				mythical = true;
			} else if (target.npc.getDef().name.toLowerCase().contains("wyvern")) {
				mythical = true;
			}
		}
		return mythical;
	}

	public static int getBowHitDelay(final int distance) {
		return 1 + ((3 + distance) / 6);
	}

	public static int getDarkBowHitDelay(final int distance) {
		return 1 + ((2 + distance) / 3);
	}

	public static int getThrownHitDelay(final int distance) {
		return 1 + (distance / 6);
	}

	public static int getBallistaeHitDelay(final int distance) {
		switch (distance) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				return 2;
			default:
				return 3;
		}
	}

	public static int getMagicHitDelay(final int distance) {
		return 1 + ((1 + distance) / 3);
	}

	public static int getTumekenShadowHitDelay(final int distance) {
		return 1 + getMagicHitDelay(distance);
	}

	public static int getShieldSpecialHitDelay(final int distance) {
		return 2 + ((4 + distance) / 6);
	}

	public static int getChebyshevDistance(final Entity entity, final Entity target) {
		int entityX = entity.getAbsX();
		int entityY = entity.getAbsY();
		int entitySize = entity.getSize();
		if (entitySize > 1) {
			entityX += entitySize / 2;
			entityY += entitySize / 2;
		}
		int targetX = target.getAbsX();
		int targetY = target.getAbsY();
		int targetSize = target.getSize();
		if (targetSize > 1) {
			targetX += targetSize / 2;
			targetY += targetSize / 2;
		}
		return Misc.getDistance(entityX, entityY, targetX, targetY);
	}

	private void attackWithRanged() {
		boolean doubleAttackStarted = false;
		Item wep = player.getEquipment().get(Equipment.SLOT_WEAPON);
		if (wep == null) {
			/* obviously should never happen */
			return;
		}
		ObjType wepDef = wep.getDef();
		RangedWeapon rangedWep = wepDef.rangedWeapon;

		if (rangedWep == null) {
			player.sendMessage("Unhandled ranged weapon: " + wepDef.name);
			reset();
			return;
		}

		// TODO: Maybe add a 'CombatWeaponHook' here so that weapons can be handled
		// modularly instead?

		if (rangedWep == RangedWeapon.TOXIC_BLOWPIPE) {
			attackWithBlowpipe(wep);
			return;
		}
		if (rangedWep == RangedWeapon.MAGMA_BLOWPIPE) {
			attackWithMagmaBlowpipe(wep);
			return;
		}
		if (rangedWep == RangedWeapon.DRYGORE_BLOWPIPE) {
			attackWithDrygoreBlowpipe2(wep);
			return;
		}

		if (wep.getId() == 30515 && target.isPlayer()) {
			player.sendMessage("You can't attack players with this weapon.");
			return;
		}

		if (rangedWep == RangedWeapon.TONALZTICS_OF_RALOS || rangedWep == RangedWeapon.TONALZTICS_OF_RALOS_UNCHARGED) {
			attackWithTonalzticsOfRalos(wep);
			return;
		}

		if (target != null && target.isNpc()) {
			VenatorBow.handlePassiveEffect(player, (NPC) target);
		}

		if (rangedWep == RangedWeapon.CORRUPTED_JAVELIN) {
			attackWithCorruptedJavelin(wep);
			return;
		}

		Item ammo = null;
		RangedAmmo rangedAmmo = null;
		boolean fireDouble = false;

		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.DOUBLE_TAP) && target.isNpc()) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.DOUBLE_TAP);
			DoubleTap c = (DoubleTap) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
			int doubleTapChance = c.getDoubleTapChance();
			if (Random.rollPercent(doubleTapChance) && target.isNpc() && player.doubleTapDelay.finished()) {
				doubleAttackStarted = true;
				updateLastAttack(1);
				player.doubleTapDelay.delay(10);
			}
		}

		if ((rangedData = rangedWep.data) != null) {
			/**
			 * Crystal bow, Knifes, Darts, etc
			 */
			if (rangedWep != RangedWeapon.CRYSTAL_BOW
					&& rangedWep != RangedWeapon.CRAWS_BOW
					&& rangedWep != RangedWeapon.BOW_OF_FAERDHINEN
					&& rangedWep != RangedWeapon.BOW_OF_FAERDHINEN_BLUE
					&& rangedWep != RangedWeapon.BOW_OF_FAERDHINEN_RED
					&& rangedWep != RangedWeapon.BOW_OF_FAERDHINEN_YELLOW
					&& rangedWep != RangedWeapon.BOW_OF_FAERDHINEN_WHITE
					&& rangedWep != RangedWeapon.BOW_OF_FAERDHINEN_CYAN
					&& rangedWep != RangedWeapon.BOW_OF_FAERDHINEN_GREEN
					&& rangedWep != RangedWeapon.BOW_OF_FAERDHINEN_BLACK
					&& rangedWep != RangedWeapon.BOW_OF_FAERDHINEN_PURPLE)
				ammo = wep;
		} else {
			var quivers = List.of(DIZANAS_MAX_CAPE, DIZANAS_QUIVER, DIZANAS_BLESSED_QUIVER);
			/**
			 * Shortbows, Longbows, Crossbows, etc
			 */
			ammo = player.getEquipment().get(Equipment.SLOT_AMMO);
			if (ammo == null) {
				var quiverAmmoId = VarPlayerRepository.DIZANA_QUIVER_AMMO_ID.get(player);
				// Check Quiver
				var capeSlot = player.getEquipment().get(Equipment.SLOT_CAPE);
				// if the cape is not null, and it is in fact a quiver item, with ammo in it
				if (capeSlot != null && quivers.contains(capeSlot.getId()) && quiverAmmoId != -1) {
					// set the ammo, to be that of the quiver
					ammo = new Item(quiverAmmoId, VarPlayerRepository.DIZANA_QUIVER_AMMO_AMOUNT.get(player));
				} else {
					player.sendMessage("You are all out of ammo.");
					reset();
					return;
				}
			}
			if (target instanceof Player && wep.getId() == 27853) {
				player.sendMessage("This cannot be used on players.");
				reset();
				return;
			}
			if ((rangedAmmo = RangedAmmo.getByItemId(ammo.getId(), wep.getId())) == null
					|| !rangedWep.allowAmmo(rangedAmmo) && wep.getId() != 30515) {
				var quiverAmmoId = VarPlayerRepository.DIZANA_QUIVER_AMMO_ID.get(player);
				// Check Quiver
				var capeSlot = player.getEquipment().get(Equipment.SLOT_CAPE);
				if (capeSlot != null && quivers.contains(capeSlot.getId()) && quiverAmmoId != -1) {
					ammo = new Item(quiverAmmoId, VarPlayerRepository.DIZANA_QUIVER_AMMO_AMOUNT.get(player));
					rangedAmmo = RangedAmmo.getByItemId(ammo.getId(), wep.getId());
				} else {
					player.sendMessage("Your weapon can't fire that ammo.");
					reset();
					return;
				}
			}
			ObjType ammoDef = ammo.getDef();
			if (wepDef.rangedLevel < ammoDef.rangedLevel) {
				player.sendMessage("Your weapon is not strong enough to fire that ammo.");
				reset();
				return;
			}
			assert rangedAmmo != null : "RangedAmmo is null";
			rangedData = rangedAmmo.data;
			if (rangedWep == RangedWeapon.DARK_BOW && ammo.getAmount() >= 2)
				fireDouble = true;
		}
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		double accuracyBoost = 0;
		if (player.damageBoostActive())
			maxDamage *= 1.2;
		if (target instanceof NPC) {
			double damageBoost = 1 + target.npc.getDamageBoostFromWeakness();
			if (!target.npc.getDef().name.contains("dummy"))
				maxDamage *= damageBoost;
		}
		if (player.getEquipment().get(12) != null && player.getEquipment().get(12).getId() == 24313
				&& isTargetMythical(target)) {
			maxDamage *= 1.2;
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.GOD_WARS_VETERAN)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.GOD_WARS_VETERAN);
			GodWarsVeteran c = (GodWarsVeteran) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			if (target.npc != null && target.npc.getDef() != null) {
				if (target.npc.getDef().id == NpcID.GENERAL_GRAARDOR || target.npc.getId() == NpcID.COMMANDER_ZILYANA
						|| target.npc.getId() == NpcID.KREEARRA || target.npc.getId() == NpcID.KRIL_TSUTSAROTH ||
						target.npc.getDef().name.contains("Nex")) {
					double damageBoost = 1;
					damageBoost += c.getDamageBonus();
					maxDamage *= damageBoost;
					accuracyBoost += c.getAccuracyBonus();

				}
			}
		}

		if (target.isNpc() && target.npc.getDef().name.equalsIgnoreCase("zulrah")) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SNAKE_CHARMER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SNAKE_CHARMER);
				SnakeCharmer c = (SnakeCharmer) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				double damageBoost = 1;
				damageBoost += c.getDamageMultiplier();
				maxDamage *= damageBoost;
			}
		}

		int attackTicks = type == AttackType.RAPID_RANGED ? weaponType.attackTicks - 1 : weaponType.attackTicks;
		double attackBoost = 0;
		if (rangedWep == RangedWeapon.CRAWS_BOW && target.isNpc())
			attackTicks--;
		if (target.isNpc()) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SKILLED_RANGER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SKILLED_RANGER);
				SkilledRanger c = (SkilledRanger) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				float x = c.getTickReduction();
				double newTicks = x * attackTicks;
				newTicks = Math.ceil(newTicks);
				attackTicks = (int) newTicks;
			}
			if (player.berserkerRageActive && target.isNpc())
				attackTicks /= 2;
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.TAKE_YOUR_TIME)) {
			if (target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.TAKE_YOUR_TIME);
				TakeYourTime c = (TakeYourTime) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				attackTicks += 2;
				attackBoost += c.getAccuracyBoost();
			}
		}
		if (!doubleAttackStarted) {
			boolean doubleAttackEffect = false;
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
				if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON),
						AttributeTypes.DOUBLE_HIT) && target.isNpc()) {
					if (DoubleHit.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON), target,
							getAttackStyle()))
						doubleAttackEffect = true;
				}
			}
			updateLastAttack(doubleAttackEffect ? 1 : attackTicks);
		}
		if (handleSpecial(style, type, maxDamage))
			return;
		attackAnim();
		if (!fireDouble) {
			boolean chins = wep.getId() == 10033 || wep.getId() == 10034 || wep.getId() == 11959 || wep.getId() == 30476;
			if (rangedData.drawbackId != -1)
				player.graphics(rangedData.drawbackId, 96, 0);

			final int distance = getChebyshevDistance(player, target);
			final int delayTicks = chins ? getThrownHitDelay(distance) : getBowHitDelay(distance);
			int delay = rangedData.projectiles[0].send(player, target);
			Hit hit;

			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
					player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
				int minimumHit = 0;
				int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				minimumHit = (int) (maxDamage * multiplier);
				int chance = 20 - (level * 2);
				if (Random.get(chance) == 0)
					hit = new Hit(player, style, type).boostAttack(accuracyBoost).randDamage(minimumHit, maxDamage)
							.ignoreDefence().ignorePrayer().delay(delayTicks).setAttackWeapon(wepDef);
				else
					hit = new Hit(player, style, type).boostAttack(accuracyBoost).randDamage(maxDamage).delay(delayTicks)
							.setAttackWeapon(wepDef);
			} else
				hit = new Hit(player, style, type).boostAttack(accuracyBoost).randDamage(maxDamage).delay(delayTicks)
						.setAttackWeapon(wepDef);
			if (ammo != null) {
				// Chins dont roll for chance of saving ammo or drop on ground so just
				// decrement.
				if (chins) {
					ammo.incrementAmount(-1);
				} else {
					removeAmmo(ammo, hit);
				}

				hit.setRangedAmmo(ammo.getDef());
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
					&& target.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				int chance = 25 - (level * 2);
				if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
					int delay2 = 50 - (level * 3);
					player.soulReaverDelay.delay(delay2);
					String name = target.npc.getDef().name;
					if (name.contains("dummy"))
						return;
					target.hit(new Hit(player, style, type).fixedDamage((int) (target.npc.getHp() * multiplier))
							.delay(delayTicks).setAttackWeapon(wepDef));
					return;
				}
			}
			if (rangedAmmo != null && rangedAmmo.effect != null && rangedAmmo.effect.apply(target, hit))
				return;
			defendAnimTicks(delayTicks - 1);
			target.hit(hit);
			// if (SetEffect.KARIL.hasPieces(player) &&
			// player.getEquipment().contains(12853)) {
			// if (Random.rollDie(4)) {
			// target.hit(new Hit(player, style, type).randDamage(maxDamage /
			// 2).setAttackWeapon(player.getEquipment().getDef(Equipment.SLOT_WEAPON)));
			// }
			// }
			if (chins) {
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.CRIMSON_CHIN) && target.isNpc()) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.CRIMSON_CHIN);
					CrimsonChin c = (CrimsonChin) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
							.getPerk(player);
					attackBoost += c.getChinAccuracyBoost();
					double damageBoost = c.getChinDamageBoost();
					maxDamage *= damageBoost;
				}
				target.graphics(157, 100, delay);
				if (target.inMulti()) {
					int entityIndex = player.getClientIndex();
					int targetIndex = target.getClientIndex();
					int targetCount = 0;
					int maxTargets = 9;
					if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.CRIMSON_CHIN) && target.isNpc()) {
						int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.CRIMSON_CHIN);
						CrimsonChin c = (CrimsonChin) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
								.getPerk(player);
						maxTargets *= c.getExtraTargetAmount();
					}
					for (Player plr : target.localPlayers()) {
						int playerIndex = plr.getClientIndex();
						if (playerIndex == entityIndex || playerIndex == targetIndex)
							continue;
						if (!plr.getPosition().isWithinDistance(target.getPosition(), 1))
							continue;
						if (!player.getCombat().canAttack(plr, false))
							continue;
						plr.hit(new Hit(player, style, type).randDamage(maxDamage).delay(delayTicks).setAttackWeapon(wepDef));
						if (++targetCount >= maxTargets)
							break;
					}
					for (NPC npc : target.localNpcs()) {
						int npcIndex = npc.getClientIndex();
						if (npcIndex == entityIndex || npcIndex == targetIndex)
							continue;
						if (!npc.getPosition().isWithinDistance(target.getPosition(), 1))
							continue;
						if (npc.getDef().ignoreMultiCheck)
							continue;
						if (!player.getCombat().canAttack(npc, false))
							continue;
						npc.hit(new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage).delay(delayTicks)
								.setAttackWeapon(wepDef));
						if (++targetCount >= maxTargets)
							break;
					}
				}
			}
		} else {
			if (rangedData.doubleDrawbackId != -1)
				player.graphics(rangedData.doubleDrawbackId, 96, 0);
			rangedData.projectiles[1].send(player, target);
			rangedData.projectiles[2].send(player, target);

			final int distance = getChebyshevDistance(player, target);
			final int delay1Ticks = getBowHitDelay(distance);
			final int delay2Ticks = getDarkBowHitDelay(distance);

			defendAnimTicks(delay1Ticks - 1);
			Hit[] hits = {
					new Hit(player, style, type).boostAttack(attackBoost).randDamage(maxDamage).delay(delay1Ticks)
							.setRangedAmmo(ammo.getDef()).setAttackWeapon(wepDef),
					new Hit(player, style, type).randDamage(wep.getId() == 27853 ? maxDamage : maxDamage / 2).delay(delay2Ticks)
							.setRangedAmmo(ammo.getDef()).setAttackWeapon(wepDef),
			};
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
					&& target.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				int chance = 25 - (level * 2);
				if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
					int delay3 = 50 - (level * 3);
					player.soulReaverDelay.delay(delay3);
					String name = target.npc.getDef().name;
					if (name.contains("dummy"))
						return;
					hits[0] = new Hit(player, style, type).fixedDamage((int) (target.npc.getHp() * multiplier))
							.delay(delay2Ticks).setAttackWeapon(wepDef);
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
					player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
				int minimumHit = 0;
				int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				minimumHit = (int) (maxDamage * multiplier);
				int chance = 20 - (level * 2);
				if (Random.get(chance) == 0) {
					hits[1] = new Hit(player, style, type).boostAttack(accuracyBoost).randDamage(minimumHit, maxDamage)
							.ignoreDefence().ignorePrayer().delay(delay2Ticks).setAttackWeapon(wepDef);
				}
			}
			removeAmmo(ammo, hits);
			target.hit(hits);
		}
	}

	private void attackWithCorruptedJavelin(Item wep) {
		if (AttributeExtensions.getCharges(wep) <= 0) {
			player.sendMessage("Your javelin isn't charged with any essence.");
			reset();
			return;
		}
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		int attackTicks = type == AttackType.RAPID_RANGED ? weaponType.attackTicks - 2 : weaponType.attackTicks - 1;
		if (target.npc != null)
			attackTicks--;
		updateLastAttack(attackTicks);
		Hit hit = new Hit(player, style, type)
				.setAttackWeapon(wep.getDef())
				.setRangedAmmo(ObjType.get(11230));
		player.graphics(5006, 96, 0);
		attackAnim();
		Projectile proj = Projectile.javelin(5019)[0];
		proj.send(player, target);
		final int distance = getChebyshevDistance(player, target);
		final int delayTicks = getThrownHitDelay(distance);

		hit.randDamage(maxDamage).delay(delayTicks);
		defendAnimTicks(delayTicks - 1);
		target.hit(hit);
		target.envenom(6);
	}

	private void attackWithChinchompa(RangedWeapon rangedWep) {
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		Item wep = player.getEquipment().get(Equipment.SLOT_WEAPON);

		wep.setAmount(wep.getAmount() - 1);
		if (wep.getAmount() <= 0) {
			wep.remove();
			player.sendMessage("That was your last one!");
		}
		int maxDamage = player.damageBoostActive() ? (int) (CombatUtils.getMaxDamage(player, style, type) * 1.2)
				: CombatUtils.getMaxDamage(player, style, type);
		if (player.damageBoostActive())
			maxDamage *= 1.2;
		if (player.getEquipment().get(12) != null && player.getEquipment().get(12).getId() == 24313
				&& isTargetMythical(target)) {
			maxDamage *= 1.2;
		}
		if (target instanceof NPC) {
			double damageBoost = 1 + target.npc.getDamageBoostFromWeakness();
			if (!target.npc.getDef().name.contains("dummy"))
				maxDamage *= damageBoost;
		}
		if (wep.getId() == 30476)
			maxDamage *= 1.8;
		rangedData = rangedWep.data;
		int delay = rangedData.projectiles[0].send(player, target);
		final int distance = getChebyshevDistance(player, target);
		final int delayTicks = getThrownHitDelay(distance);
		Hit hit = new Hit(player, style, type).randDamage(maxDamage).delay(delayTicks).setAttackWeapon(wep.getDef());
		updateLastAttack(type == AttackType.RAPID_RANGED ? weaponType.attackTicks - 1 : weaponType.attackTicks);
		attackAnim();
		int gfxDelay = delay *= 0.75f;
		target.graphics(157, 65, gfxDelay);
		target.hit(hit);
		if (target.inMulti()) {
			int entityIndex = target.getClientIndex();
			int targetIndex = target.getClientIndex();
			int targetCount = 0;
			for (Player players : target.localPlayers()) {
				int playerIndex = player.getClientIndex();
				if (playerIndex == entityIndex || playerIndex == targetIndex)
					continue;
				if (!target.getPosition().isWithinDistance(players.getPosition(), 1)) {
					continue;
				}
				if (!target.npc.getPosition().isWithinDistance(target.getPosition(), 1)) {
					continue;
				}
				if (players == player)
					continue;
				if (target.player != null) {
					if (!target.player.getCombat().canAttack(player, false))
						continue;
				} else {
					if (!target.npc.getCombat().canAttack(player))
						continue;
				}
				Hit multiHit = new Hit(player, style, type).randDamage(maxDamage).delay(delayTicks)
						.setAttackWeapon(wep.getDef());
				players.hit(multiHit);
				players.graphics(157, 130, gfxDelay);
				if (++targetCount >= 9)
					break;
			}
			for (NPC npc : target.localNpcs()) {
				int npcIndex = npc.getClientIndex();
				if (npcIndex == entityIndex || npcIndex == targetIndex)
					continue;
				if (!npc.getPosition().isWithinDistance(target.getPosition(), 1)) {
					continue;
				}
				if (npc.getDef().ignoreMultiCheck)
					continue;
				if (target.player != null) {
					if (!target.player.getCombat().canAttack(npc, false))
						continue;
				} else {
					if (!target.npc.getCombat().canAttack(npc))
						continue;
				}
				Hit multiHit = new Hit(player, style, type).randDamage(maxDamage).delay(delayTicks)
						.setAttackWeapon(wep.getDef());
				npc.hit(multiHit);
				npc.graphics(157, 130, gfxDelay);
				if (++targetCount >= 9)
					break;
			}
		}
	}

	private void attackWithTonalzticsOfRalos(Item item) {
		int charged = 28922;
		int uncharged = 28919;
		double maxDamage = CombatUtils.getMaxDamage(player, getAttackStyle(), getAttackType());
		maxDamage *= 0.75;
		int cappedMaxDamage = (int) maxDamage;
		int attackTicks = getAttackType() == AttackType.RAPID_RANGED ? weaponType.attackTicks - 1 : weaponType.attackTicks;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SKILLED_RANGER) && target.isNpc()) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SKILLED_RANGER);
			SkilledRanger c = (SkilledRanger) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			attackTicks -= c.getTickReduction();
		}
		Entity targ = target;
		player.animate(3353);
		if (item.getId() == charged) {
			updateLastAttack(attackTicks);
			World.startEvent(e -> {
				e.delay(1);
				player.graphics(2734);
				TonalzticsOfRalos.consumeCharge(player, item);
				e.delay(1);
				int delay = new Projectile(2729, 22, 22, 0, 37, 1, 15, 15).send(player, targ);
				e.delay(World.getTicks(delay) + 1);
				Hit hit = new Hit(player, getAttackStyle(), getAttackType())
						.randDamage(0, cappedMaxDamage);
				Hit hit2 = new Hit(player, getAttackStyle(), getAttackType())
						.randDamage(0, cappedMaxDamage);
				targ.hit(hit);
				targ.hit(hit2);
				targ.graphics(2732, 100, 0);
				new Projectile(2729, 22, 22, 0, 37, 1, 15, 15).send(targ, player);
			});

		} else if (item.getId() == uncharged) {
			updateLastAttack(attackTicks);
			World.startEvent(e -> {
				e.delay(1);
				player.graphics(2734);
				e.delay(1);
				int delay = new Projectile(2730, 22, 22, 0, 37, 1, 15, 15).send(player, targ);
				e.delay(World.getTicks(delay) + 1);
				Hit hit = new Hit(player, getAttackStyle(), getAttackType())
						.randDamage(0, cappedMaxDamage);
				targ.hit(hit);
				new Projectile(2730, 22, 22, 0, 37, 1, 15, 15).send(targ, player);
			});
		}

	}

	private void attackWithMagmaBlowpipe(Item blowpipe) {
		Blowpipe.Dart dart = Blowpipe.Dart.AMETHYST;
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		if (target != null && target instanceof Player) {
			player.sendMessage("This cannot be used on players.");
			reset();
			return;
		}
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		maxDamage *= 0.55;

		if (player.damageBoostActive())
			maxDamage *= 1.2;
		if (target != null && target instanceof NPC) {
			double damageBoost = 1 + target.npc.getDamageBoostFromWeakness();
			if (target != null && !target.npc.getDef().name.contains("dummy"))
				maxDamage *= damageBoost;
		}
		int attackTicks = type == AttackType.RAPID_RANGED ? weaponType.attackTicks - 1 : weaponType.attackTicks;
		if (target != null && target.npc != null)
			attackTicks--;
		boolean doubleAttackEffect = false;
		if (target != null && player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON),
					AttributeTypes.DOUBLE_HIT)) {
				if (DoubleHit.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON), target, getAttackStyle()))
					doubleAttackEffect = true;
			}
		}
		if (target != null && target.isNpc()) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SKILLED_RANGER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SKILLED_RANGER);
				SkilledRanger c = (SkilledRanger) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				float x = c.getTickReduction();
				double newTicks = x * attackTicks;
				newTicks = Math.ceil(newTicks);
				attackTicks = (int) newTicks;
			}
			if (player.berserkerRageActive && target.isNpc())
				attackTicks /= 2;
		}
		Hit hit = new Hit(player, style, type)
				.setAttackWeapon(blowpipe.getDef())
				.setRangedAmmo(ObjType.get(dart.id)).boostDamage(0.2).randDamage(maxDamage);
		Hit hit2 = new Hit(player, style, type)
				.setAttackWeapon(blowpipe.getDef())
				.setRangedAmmo(ObjType.get(dart.id)).boostDamage(0.1).randDamage(maxDamage);
		Hit hit3 = new Hit(player, style, type)
				.setAttackWeapon(blowpipe.getDef())
				.setRangedAmmo(ObjType.get(dart.id)).randDamage(maxDamage);
		player.animate(10656);
		updateLastAttack(doubleAttackEffect ? 1 : attackTicks);
		if (handleSpecial(style, type, maxDamage)) {
			final int distance = getChebyshevDistance(player, target);
			final int delayTicks = getThrownHitDelay(distance);
			ToxicBlowpipe.SIPHON_PROJECTILE.send(player, target);
			defendAnimTicks(delayTicks - 1);
			hit.randDamage(maxDamage).boostDamage(1.50).delay(delayTicks);
			int damage = target.hit(hit);
			if (damage >= 2)
				player.incrementHp(damage / 2);
		} else {
			Projectile projectileOne = new Projectile(1122, 70, 36, 0, 37, 5, 15, 105);
			Projectile projectileTwo = new Projectile(1122, 50, 36, 20, 37, 5, 15, 105);
			Projectile projectileThree = new Projectile(1122, 40, 36, 40, 37, 5, 15, 105);
			int delay = projectileOne.send(player, target);
			int delay2 = projectileTwo.send(player, target);
			int delay3 = projectileThree.send(player, target);
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.DOUBLE_TAP) && target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.DOUBLE_TAP);
				DoubleTap c = (DoubleTap) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
				int doubleTapChance = c.getDoubleTapChance();
				if (Random.rollPercent(doubleTapChance) && target.isNpc() && player.doubleTapDelay.finished()) {
					Hit hit4 = new Hit(player, style, type)
							.setAttackWeapon(blowpipe.getDef())
							.setRangedAmmo(ObjType.get(dart.id));
					player.doubleTapDelay.delay(10);
					target.hit(hit4);
					return;
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
					&& target.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				int chance = 25 - (level * 2);
				if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
					int delay4 = 50 - (level * 3);
					player.soulReaverDelay.delay(delay4);
					String name = target.npc.getDef().name;
					if (name.contains("dummy"))
						return;
					target.hit(new Hit(player, style, type).fixedDamage((int) (target.npc.getHp() * multiplier))
							.clientDelay(delay).setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
					player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
				int minimumHit = 0;
				int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				minimumHit = (int) (maxDamage * multiplier);
				int chance = 20 - (level * 2);
				if (Random.get(chance) == 0)
					hit.randDamage(minimumHit, maxDamage).ignoreDefence().ignorePrayer().clientDelay(delay);
			}

			if (target != null) {
				target.hit(hit);
				if (target != null) {
					target.hit(hit2);
				}
				if (target != null) {
					target.hit(hit3);
				}
				if (Random.get(15) == 0) {
					player.sendFilteredMessage("You badly burn your target!");
					if (target != null) {
						target.graphics(78);
						target.hit(new Hit(player, style, type).randDamage(30, 75).clientDelay(delay));
					}
				}
			}
		}
	}

	/*
	 * This for testing, will be the twisted bow upgraded one.
	 */
	private void attackWithDrygoreBlowpipe2(Item blowpipe) {
		var style = attackSet.style;
		var type = attackSet.type;
		var dartOrdinal = blowpipe.getAttributeInt(AttributeTypes.AMMO_ID, 0);
		var dartAmount = blowpipe.getAttributeInt(AttributeTypes.AMMO_AMOUNT, 0);

		if (target != null && target instanceof Player) {
			player.sendMessage("This cannot be used on players.");
			reset();
			return;
		}
		if (dartOrdinal == 0 || dartAmount <= 0) {
			player.sendMessage("Your blowpipe isn't loaded with any darts.");
			reset();
			return;
		}

		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		var dart = Blowpipe.Dart.values()[dartOrdinal];

		if (player.damageBoostActive())
			maxDamage *= 1.2;

		if (target instanceof NPC) {
			double damageBoost = 1 + target.npc.getDamageBoostFromWeakness();
			if (!target.npc.getDef().name.contains("dummy"))
				maxDamage *= damageBoost;
		}
		if (player.getEquipment().get(SLOT_RING) != null
				&& player.getEquipment().get(SLOT_RING).getId() == 24313
				&& isTargetMythical(target)) {
			maxDamage *= 1.2;
		}

		int attackTicks = type == AttackType.RAPID_RANGED ? weaponType.attackTicks - 1 : weaponType.attackTicks;
		if (target.npc != null)
			attackTicks--;

		boolean doubleAttackEffect = false;

		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
				player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.DOUBLE_HIT)) {
			if (DoubleHit.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON), target, getAttackStyle()))
				doubleAttackEffect = true;
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.DOUBLE_TAP) && target.isNpc()) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.DOUBLE_TAP);
			DoubleTap c = (DoubleTap) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
			int doubleTapChance = c.getDoubleTapChance();
			if (Random.rollPercent(doubleTapChance) && target.isNpc() && player.doubleTapDelay.finished()) {
				doubleAttackEffect = true;
				player.doubleTapDelay.delay(10);
			}
		}

		if (target.isNpc()) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SKILLED_RANGER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SKILLED_RANGER);
				SkilledRanger c = (SkilledRanger) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				float x = c.getTickReduction();
				double newTicks = x * attackTicks;
				newTicks = Math.ceil(newTicks);
				attackTicks = (int) newTicks;
			}
			if (player.berserkerRageActive && target.isNpc())
				attackTicks /= 2;
		}

		var hit = new Hit(player, style, type)
				.setAttackWeapon(blowpipe.getDef())
				.setRangedAmmo(ObjType.get(dart.id))
				.boostDamage(0.75)
				.randDamage(maxDamage);

		attackAnim();

		updateLastAttack(attackTicks);
		if (doubleAttackEffect) {
			// NOTE: hit may override the current target in it's onDefend listeners
			target.hit(new Hit(player, style, type)
					.setAttackWeapon(blowpipe.getDef())
					.setRangedAmmo(ObjType.get(dart.id))
					.boostDamage(0.2)
					.randDamage(maxDamage));
		}

		final int distance = getChebyshevDistance(player, target);
		final int delayTicks = getThrownHitDelay(distance);

		Blowpipe.getDart(blowpipe).rangedData.projectiles[0].send(player, target);

		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.DOUBLE_TAP) && target.isNpc()) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.DOUBLE_TAP);
			DoubleTap c = (DoubleTap) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
			int doubleTapChance = c.getDoubleTapChance();
			if (Random.rollPercent(doubleTapChance) && target.isNpc() && player.doubleTapDelay.finished()) {
				Hit hit5 = new Hit(player, style, type)
						.setAttackWeapon(blowpipe.getDef())
						.setRangedAmmo(ObjType.get(dart.id));
				player.doubleTapDelay.delay(10);
				target.hit(hit5);
				return;
			}
		}

		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
				player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER) && target.isNpc()) {
			int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
					player.getEquipment().get(Equipment.SLOT_WEAPON));
			double multiplier = 0.02 * level;
			int chance = 25 - (level * 2);
			if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
				int delay2 = 50 - (level * 3);
				player.soulReaverDelay.delay(delay2);
				String name = target.npc.getDef().name;
				if (name.contains("dummy"))
					return;
				target.hit(new Hit(player, style, type)
						.fixedDamage((int) (target.npc.getHp() * multiplier))
						.delay(delayTicks)
						.setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
				return;
			}
		}

		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
				player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
			int minimumHit = 0;
			int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
					player.getEquipment().get(Equipment.SLOT_WEAPON));
			double multiplier = 0.02 * level;
			minimumHit = (int) (maxDamage * multiplier);
			int chance = 20 - (level * 2);
			if (Random.get(chance) == 0)
				hit.randDamage(minimumHit, maxDamage).ignoreDefence().ignorePrayer().delay(delayTicks);
			else
				hit.randDamage(maxDamage).delay(delayTicks);
		} else
			hit.randDamage(maxDamage).delay(delayTicks);
		if (target != null) {
			if (target.isNpc()) {
				if (Random.get() < 0.33) {
					// TODO: DrygoreVenom
				}
			}
			target.envenom(6);
			target.hit(hit);
		}
		defendAnimTicks(delayTicks - 1);

		int quiverSaveChance = 60;
		int assemblerSaveChance = 40;
		int avaSaveChance = 25;

		if (hasQuiver()) {
			if (!Random.rollPercent(quiverSaveChance)) {
				dartAmount--;
			}
		} else if (hasAvaAssembler()) {
			if (!Random.rollPercent(assemblerSaveChance)) {
				dartAmount--;
			}
		} else if (hasAvaDevice()) {
			if (!Random.rollPercent(avaSaveChance)) {
				dartAmount--;
			}
		} else
			dartAmount--;
		// update the BP
		AttributeExtensions.putAttribute(blowpipe, AttributeTypes.AMMO_AMOUNT, dartAmount);
	}

	private void attackWithDrygoreBlowpipe(Item blowpipe) {
		var style = attackSet.style;
		var type = attackSet.type;
		var dartOrdinal = blowpipe.getAttributeInt(AttributeTypes.AMMO_ID, 0);
		var dartAmount = blowpipe.getAttributeInt(AttributeTypes.AMMO_AMOUNT, 0);

		if (target != null && target instanceof Player) {
			player.sendMessage("This cannot be used on players.");
			reset();
			return;
		}
		if (dartOrdinal == 0 || dartAmount <= 0) {
			player.sendMessage("Your blowpipe isn't loaded with any darts.");
			reset();
			return;
		}

		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		maxDamage *= 0.75;
		var dart = Blowpipe.Dart.values()[dartOrdinal];

		if (player.damageBoostActive())
			maxDamage *= 1.2;

		if (target instanceof NPC) {
			double damageBoost = 1 + target.npc.getDamageBoostFromWeakness();
			if (!target.npc.getDef().name.contains("dummy"))
				maxDamage *= damageBoost;
		}
		if (player.getEquipment().get(SLOT_RING) != null
				&& player.getEquipment().get(SLOT_RING).getId() == 24313
				&& isTargetMythical(target))
			maxDamage *= 1.2;

		int attackTicks = type == AttackType.RAPID_RANGED ? weaponType.attackTicks - 1 : weaponType.attackTicks;
		if (target.npc != null)
			attackTicks--;

		boolean doubleAttackEffect = false;

		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON),
					AttributeTypes.DOUBLE_HIT)) {
				if (DoubleHit.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON), target, getAttackStyle()))
					doubleAttackEffect = true;
			}
		}
		if (target.isNpc()) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SKILLED_RANGER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SKILLED_RANGER);
				SkilledRanger c = (SkilledRanger) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				float x = c.getTickReduction();
				double newTicks = x * attackTicks;
				newTicks = Math.ceil(newTicks);
				attackTicks = (int) newTicks;
			}
			if (player.berserkerRageActive && target.isNpc())
				attackTicks /= 2;
		}

		var hit = new Hit(player, style, type)
				.setAttackWeapon(blowpipe.getDef())
				.setRangedAmmo(ObjType.get(dart.id))
				.boostDamage(0.2)
				.randDamage(maxDamage);
		var hit2 = new Hit(player, style, type)
				.setAttackWeapon(blowpipe.getDef())
				.setRangedAmmo(ObjType.get(dart.id))
				.boostDamage(0.1)
				.randDamage(maxDamage);
		var hit3 = new Hit(player, style, type)
				.setAttackWeapon(blowpipe.getDef())
				.setRangedAmmo(ObjType.get(dart.id))
				.randDamage(maxDamage);

		attackAnim();

		updateLastAttack(doubleAttackEffect ? 1 : attackTicks);

		final int distance = getChebyshevDistance(player, target);
		final int delayTicks = getThrownHitDelay(distance);

		Blowpipe.getDart(blowpipe).rangedData.projectiles[0].send(player, target);

		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.DOUBLE_TAP) && target.isNpc()) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.DOUBLE_TAP);
			DoubleTap c = (DoubleTap) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
			int doubleTapChance = c.getDoubleTapChance();
			if (Random.rollPercent(doubleTapChance) && target.isNpc() && player.doubleTapDelay.finished()) {
				Hit hit5 = new Hit(player, style, type)
						.setAttackWeapon(blowpipe.getDef())
						.setRangedAmmo(ObjType.get(dart.id));
				player.doubleTapDelay.delay(10);
				target.hit(hit5);
				return;
			}
		}

		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
				AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
				&& target.isNpc()) {
			int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
					player.getEquipment().get(Equipment.SLOT_WEAPON));
			double multiplier = 0.02 * level;
			int chance = 25 - (level * 2);
			if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
				int delay2 = 50 - (level * 3);
				player.soulReaverDelay.delay(delay2);
				String name = target.npc.getDef().name;
				if (name.contains("dummy"))
					return;
				target.hit(new Hit(player, style, type)
						.fixedDamage((int) (target.npc.getHp() * multiplier))
						.delay(delayTicks)
						.setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
				return;
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
				player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
			int minimumHit = 0;
			int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
					player.getEquipment().get(Equipment.SLOT_WEAPON));
			double multiplier = 0.02 * level;
			minimumHit = (int) (maxDamage * multiplier);
			int chance = 20 - (level * 2);
			if (Random.get(chance) == 0)
				hit.randDamage(minimumHit, maxDamage).ignoreDefence().ignorePrayer().delay(delayTicks);
			else
				hit.randDamage(maxDamage).delay(delayTicks);
		} else
			hit.randDamage(maxDamage).delay(delayTicks);
		if (target != null) {
			target.envenom(6);
			target.hit(hit, hit2, hit3);
		}
		defendAnimTicks(delayTicks - 1);

		boolean assembler = hasAvaAssembler();
		boolean ava = hasAvaDevice();
		if (hasAvaAssembler()) {
			if (assembler && rollAssemblerChance()) {
			} else if (assembler && rollAssemblerDestroy())
				dartAmount--;
		} else if (hasAvaDevice()) {
			if (ava && rollAvaChance()) {
			} else if (ava && rollAvaDestroy())
				dartAmount--;
		} else
			dartAmount--;
		// update the BP
		AttributeExtensions.putAttribute(blowpipe, AttributeTypes.AMMO_AMOUNT, dartAmount);
	}

	private void attackWithBlowpipe(Item blowpipe) {
		Blowpipe.Dart dart = Blowpipe.getDart(blowpipe);
		int dartAmount = Blowpipe.getDartAmount(blowpipe);
		if (dart == Blowpipe.Dart.NONE || dartAmount <= 0) {
			player.sendMessage("Your blowpipe isn't loaded with any darts.");
			reset();
			return;
		}
		int scalesAmount = Blowpipe.getScalesAmount(blowpipe);
		if (scalesAmount <= 0) {
			player.sendMessage("Your blowpipe isn't charged with any scales.");
			reset();
			return;
		}
		Item ammo = null;
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);

		if (player.damageBoostActive())
			maxDamage *= 1.2;
		if (target instanceof NPC) {
			double damageBoost = 1 + target.npc.getDamageBoostFromWeakness();
			if (!target.npc.getDef().name.contains("dummy"))
				maxDamage *= damageBoost;
		}
		if (player.getEquipment().get(12) != null && player.getEquipment().get(12).getId() == 24313
				&& isTargetMythical(target)) {
			maxDamage *= 1.2;
		}
		int attackTicks = type == AttackType.RAPID_RANGED ? weaponType.attackTicks - 1 : weaponType.attackTicks;
		if (target.npc != null)
			attackTicks--;
		boolean doubleAttackEffect = false;
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON),
					AttributeTypes.DOUBLE_HIT)) {
				if (DoubleHit.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON), target, getAttackStyle()))
					doubleAttackEffect = true;
			}
		}
		if (target.isNpc()) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SKILLED_RANGER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SKILLED_RANGER);
				SkilledRanger c = (SkilledRanger) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				float x = c.getTickReduction();
				double newTicks = x * attackTicks;
				newTicks = Math.ceil(newTicks);
				attackTicks = (int) newTicks;
			}
			if (player.berserkerRageActive && target.isNpc())
				attackTicks /= 2;
		}

		updateLastAttack(doubleAttackEffect ? 1 : attackTicks);
		Hit hit = new Hit(player, style, type)
				.setAttackWeapon(blowpipe.getDef())
				.setRangedAmmo(ObjType.get(dart.id));
		attackAnim();
		if (handleSpecial(style, type, maxDamage)) {
			ToxicBlowpipe.SIPHON_PROJECTILE.send(player, target);
			final int distance = getChebyshevDistance(player, target);
			final int delayTicks = getThrownHitDelay(distance);
			defendAnimTicks(delayTicks - 1);
			hit.randDamage(maxDamage).boostDamage(0.50).delay(delayTicks);
			int damage = target.hit(hit);
			if (damage >= 2)
				player.incrementHp(damage / 2);
		} else {
			final int distance = getChebyshevDistance(player, target);
			final int delayTicks = getThrownHitDelay(distance);
			int delay = dart.rangedData.projectiles[0].send(player, target);
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.DOUBLE_TAP) && target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.DOUBLE_TAP);
				DoubleTap c = (DoubleTap) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex).getPerk(player);
				int doubleTapChance = c.getDoubleTapChance();
				if (Random.rollPercent(doubleTapChance) && target.isNpc() && player.doubleTapDelay.finished()) {
					Hit hit2 = new Hit(player, style, type)
							.setAttackWeapon(blowpipe.getDef())
							.setRangedAmmo(ObjType.get(dart.id));
					player.doubleTapDelay.delay(10);
					target.hit(hit2);
					target.hit(hit);
					return;
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SOUL_REAVER)
					&& target.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				int chance = 25 - (level * 2);
				if (Random.get(chance) == 0 && player.soulReaverDelay.remaining() < 1) {
					int delay2 = 50 - (level * 3);
					player.soulReaverDelay.delay(delay2);
					String name = target.npc.getDef().name;
					if (name.contains("dummy"))
						return;
					target.hit(new Hit(player, style, type).fixedDamage((int) (target.npc.getHp() * multiplier))
							.delay(delayTicks).setAttackWeapon(player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
					return;
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(
					player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE) && target.isNpc()) {
				int minimumHit = 0;
				int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				double multiplier = 0.02 * level;
				minimumHit = (int) (maxDamage * multiplier);
				int chance = 20 - (level * 2);

				if (Random.get(chance) == 0)
					hit.randDamage(minimumHit, maxDamage).ignoreDefence().ignorePrayer().delay(delayTicks);
				else
					hit.randDamage(maxDamage).delay(delayTicks);

			} else {
				hit.randDamage(maxDamage).delay(delayTicks);
			}

			if (target != null) {
				target.envenom(6);
				target.hit(hit);
			}

			defendAnimTicks(delayTicks - 1);
		}

		if (Random.rollDie(3, 2))
			scalesAmount--;

		// Fuck off
		boolean assembler = hasAvaAssembler();
		boolean ava = hasAvaDevice();
		if (hasQuiver()) {

		} else if (hasAvaAssembler()) {
			if (assembler && rollAssemblerChance()) {
				// player.sendMessage("Saved Ammo 80% chance");
			} else if (assembler && rollAssemblerDestroy()) {
				// player.sendMessage("Destroyed Ammo 20% chance");
				dartAmount--;
			}
		} else if (hasAvaDevice()) {
			if (ava && rollAvaChance()) {
				// player.sendMessage("Saved Ammo 72% chance");
			} else if (ava && rollAvaDestroy()) {
				// player.sendMessage("Destroyed Ammo 20% chance");
				dartAmount--;
			}
		} else {
			dartAmount--;
		}
		Blowpipe.update(blowpipe, dart, dartAmount, scalesAmount);
	}

	private boolean hasAvaDevice() {
		int capeID = player.getEquipment().getId(Equipment.SLOT_CAPE);
		return capeID == 10499 || capeID == 13337 || capeID == 9756 || capeID == 9757;
	}

	private boolean hasAvaAssembler() {
		int capeID = player.getEquipment().getId(Equipment.SLOT_CAPE);
		return capeID == 21898 || capeID == 22109 || capeID == 27374;
	}

	private boolean hasQuiver() {
		int capeID = player.getEquipment().getId(Equipment.SLOT_CAPE);
		return capeID == ItemID.DIZANAS_QUIVER_UN
				|| capeID == ItemID.DIZANAS_QUIVER
				|| capeID == ItemID.DIZANAS_BLESSED_QUIVER
				|| capeID == ItemID.DIZANAS_MAX_CAPE;

	}

	private boolean rollAvaChance() {
		return Random.rollPercent(72);
	}

	private boolean rollAvaDestroy() {
		return Random.rollPercent(20);
	}

	/**
	 * Dragonfire shield special attacks
	 */

	private boolean rollAvaDrop() {
		return Random.rollPercent(8);
	}

	private boolean rollAssemblerChance() {
		return Random.rollPercent(80);
	}

	private boolean rollAssemblerDestroy() {
		return Random.rollPercent(20);
	}

	private boolean rollQuiverChance() {
		return Random.rollPercent(90);
	}


	public TargetSpell currentTargetSpell() {
		TargetSpell spell;
		if (queuedSpell == null) {
			spell = autocastSpell;
		} else {
			spell = queuedSpell;
		}
		return spell;
	}

	private void attackWithMagic() {
		AttackStyle style = attackSet.style;
		AttackType type = attackSet.type;
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		int attackTicks = 5;
		Item wep = player.getEquipment().get(Equipment.SLOT_WEAPON);
		if (wep != null && wep.getId() == ItemID.TUMEKENS_SHADOW && target.isPlayer()) {
			player.sendMessage("You can't attack players with this weapon.");
			return;
		}

		if (target.isNpc()) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.ARCANE_ENHANCEMENT)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.ARCANE_ENHANCEMENT);
				ArcaneEnhancement c = (ArcaneEnhancement) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				maxDamage += (maxDamage * c.getDamageBoost());
				switch (c.getPerkLevel()) {
					case 1:
					case 2:
						attackTicks = 4;
						break;
					case 3:
					case 4:
						attackTicks = 3;
						break;
					case 5:
						attackTicks = 2;
						break;

				}
			}
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.GOD_WARS_VETERAN)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.GOD_WARS_VETERAN);
				GodWarsVeteran c = (GodWarsVeteran) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				if (target.npc.getDef().id == NpcID.GENERAL_GRAARDOR || target.npc.getId() == NpcID.COMMANDER_ZILYANA
						|| target.npc.getId() == NpcID.KREEARRA || target.npc.getId() == NpcID.KRIL_TSUTSAROTH ||
						target.npc.getDef().name.contains("nex")) {
					double damageBoost = 1;
					damageBoost += c.getDamageBonus();
					maxDamage *= damageBoost;
				}
			}
		}

		TargetSpell spell;
		boolean autocast;
		if (queuedSpell == null) {
			spell = autocastSpell;
			autocast = true;
		} else {
			spell = queuedSpell;
			autocast = false;
		}

		if (!spell.cast(player, target)) {
			reset();
			return;
		}

		if (specialActive != null) {
			handleSpecial(style, type, maxDamage);
			return;
		}

		boolean doubleAttackEffect = false;
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON),
					AttributeTypes.DOUBLE_HIT)) {
				if (DoubleHit.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON), target, getAttackStyle()))
					doubleAttackEffect = true;
			}
		}

		updateLastAttack(doubleAttackEffect ? 1 : attackTicks);
		if (wearsHarmonisedStaff(player)) {
			updateLastAttack(doubleAttackEffect ? 1 : attackTicks - 1);
		}

		if (!autocast) {
			reset();
		}

	}

	/**
	 * Hits to this player
	 */

	private void preDefend(Hit hit) {
		for (Item item : player.getEquipment().getItems()) {
			if (item != null && item.getDef() != null) {
				item.getDef().preDefend(player, item, hit);
				if (hit.attacker != null && hit.attacker.isNpc()) {
					List<String> upgrades = AttributeExtensions.getEffectUpgrades(item);
					boolean hasEffect = upgrades != null;
					if (hasEffect) {
						for (String s : upgrades) {
							try {
								if (s.equalsIgnoreCase("empty"))
									continue;
								ItemEffect effect = ItemEffect.valueOf(s);
								effect.getUpgrade(effect).preDefend(player, hit.attacker, item, hit);
							} catch (Exception ex) {
								// System.err.println("Unknown upgrade { " + s + " } found!");
								ex.printStackTrace(System.err);
							}
						}
					}
				}
			}
		}
	}

	private void postDefend(Hit hit) {
		if (hit.attackStyle != null && !hit.prayerIgnored) {
			if (hit.attackStyle.isMelee() || hit.attackStyle.isMagicalMelee()) {
				if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MELEE)) {
					if (hit.attacker != null && hit.attacker.player != null)
						hit.damage *= 0.60;
					else
						hit.damage = 0;
				}
			} else if (hit.attackStyle.isRanged() || hit.attackStyle.isMagicalRanged()) {
				if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES)) {
					if (hit.attacker != null && hit.attacker.player != null)
						hit.damage *= 0.60;
					else
						hit.damage = 0;
				}
			} else if (hit.attackStyle.isMagic()) {
				if (player.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC)) {
					if (hit.attacker != null && hit.attacker.player != null)
						hit.damage *= 0.60;
					else
						hit.damage = 0;
				}
			}
		}

		if (hit.attacker != null) {
			/* elysian spirit shield */

			if (target != null && target.isNpc()) {
				List<Integer> equipmentSlots = Arrays.asList(Equipment.SLOT_SHIELD, Equipment.SLOT_WEAPON,
						Equipment.SLOT_AMULET);
				for (Integer equipmentSlot : equipmentSlots) {
					if (player.getEquipment().get(equipmentSlot) != null) {
						if (AttributeExtensions.hasAttribute(player.getEquipment().get(equipmentSlot),
								AttributeTypes.BERSERKER_RAGE)) {
							int level = AttributeExtensions.getCharges(AttributeTypes.BERSERKER_RAGE,
									player.getEquipment().get(equipmentSlot));
							if (player.getHp() <= 50 && player.berkserkerRageDelay.remaining() < 1) {
								player.sendMessage("You unleash your berserker rage!");
								player.graphics(1860);
								player.berkserkerRageDelay.delay(500 - (level * 50));
								player.berserkerRageActive = true;
								World.startEvent(event -> {
									event.delay(16 * level);
									player.berserkerRageActive = false;
								});
							}
							break;
						}
					}
				}
			}

		}
		if (player.vestasSpearSpecial.isDelayed() && hit.attackStyle != null && hit.attackStyle.isMelee()) {
			hit.block();
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.DAMAGE_REFLECT) && hit.attacker != null
				&& hit.attacker.isNpc()) {
			int perkIndex = 0;
			for (int i = 0; i < player.getPlayerPerkHandler().getActivePerks(player).size(); i++) {
				if (player.getPlayerPerkHandler().getActivePerks(player).get(i).getPerk(player).getPerkName()
						.equalsIgnoreCase(Perks.DAMAGE_REFLECT.getPerk(player).getPerkName()))
					perkIndex = i;
			}
			DamageReflectPerk c = (DamageReflectPerk) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			int damageReflect = c.getDeflectAmount(hit.damage);
			if (Random.rollPercent(c.getDeflectChance())) {
				// TODO: a gfx or something
				hit.damage -= damageReflect;
				World.startEvent(e -> {
					e.delay(2);
					if (damageReflect > 0)
						hit.attacker.hit(new Hit(player).fixedDamage(damageReflect));
				});
			}
		}
	}

	private void preDamage(Hit hit) {
		for (Item item : player.getEquipment().getItems()) {
			if (item != null && AttributeExtensions.hasAttribute(item, AttributeTypes.JELLIFIED) && hit.attacker != null
					&& hit.attacker.isNpc()) {
				int level = AttributeExtensions.getCharges(AttributeTypes.JELLIFIED, item);
				int chance = 20 - (level * 2);
				if (Random.get(chance) == 0) {
					hit.type = HitType.HEAL;
				}
			}
		}
		if (player.getEquipment().getId(Equipment.SLOT_SHIELD) == 12817 && Random.rollPercent(70)) {
			hit.damage *= 0.75;
			player.graphics(321);
		}

		if (player.eternalResilienceActive) {
			hit.damage *= 0.5;
		}
		if (target != null && target.isNpc()) {
			for (Item item : player.getEquipment().getItems()) {
				if (item != null && !AttributeExtensions.hasAttribute(item, AttributeTypes.ENHANCED_SOAK)) {
					continue;
				}
				ItemBreakPerkHandler.handleEnhancedSoak(player);
				break;
			}
			List<Integer> equipmentSlots = Arrays.asList(Equipment.SLOT_CHEST, Equipment.SLOT_LEGS, Equipment.SLOT_HAT);
			for (Integer equipmentSlot : equipmentSlots) {
				if (player.getEquipment().get(equipmentSlot) != null) {
					if (AttributeExtensions.hasAttribute(player.getEquipment().get(equipmentSlot),
							AttributeTypes.IMMORTAL_FORTITUDE)) {
						int level = AttributeExtensions.getCharges(AttributeTypes.IMMORTAL_FORTITUDE,
								player.getEquipment().get(equipmentSlot));
						double soak = 0.1 * level;
						if (player.getHp() <= 25 && hit.type != HitType.HEAL) {
							hit.damage *= soak;
						}
						break;
					}
				}
			}
		}
		if (player.getEquipment().getId(Equipment.SLOT_SHIELD) == 30191 && hit.attacker != null && hit.attacker.isNpc()) {
			hit.damage *= 0.75;
			int current = player.getStats().get(StatType.Prayer).currentLevel;
			player.getPrayer().drain((int) (10 + ((current + 1) / 40.0)));
		}
		if (player.getEquipment().getId(Equipment.SLOT_SHIELD) == 30508 && hit.attacker != null && hit.attacker.isNpc()) {
			hit.damage *= 0.65;
		}
		if (player.wildernessLevel > 0) {
			if (hit.attacker != null && hit.attacker.isNpc()) {
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.WILDERNESS_HUNTER)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.WILDERNESS_HUNTER);
					WildernessHunter c = (WildernessHunter) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
							.getPerk(player);
					double calc = 1 - c.getDamageReduction();
					hit.damage *= calc;
				}
			}
		}

		if (hit.attacker != null && hit.attacker.isNpc()) {
			if (hit.attackStyle != null && hit.attackStyle.isRanged()) {
				if (player.getPlayerPerkHandler().getActivePerkSets(player).contains(PerkSets.RANGE_RESISTANCE)
						&& hit.attacker.isNpc()) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkSetIndex(player, PerkSets.RANGE_RESISTANCE);
					RangeResistance c = (RangeResistance) player.getPlayerPerkHandler().getActivePerkSets(player).get(perkIndex)
							.perkSet();
					hit.damage *= c.getRangeDamageReduction();
				}
			}
			if (hit.attackStyle != null && hit.attackStyle.isMagic()) {
				if (player.getPlayerPerkHandler().getActivePerkSets(player).contains(PerkSets.MAGIC_RESISTANCE)
						&& hit.attacker.isNpc()) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkSetIndex(player, PerkSets.MAGIC_RESISTANCE);
					MagicResistance c = (MagicResistance) player.getPlayerPerkHandler().getActivePerkSets(player).get(perkIndex)
							.perkSet();
					hit.damage *= c.getMagicDamageReduction();
				}
			}
			if (hit.attackStyle != null && hit.attackStyle.isMelee()) {
				if (player.getPlayerPerkHandler().getActivePerkSets(player).contains(PerkSets.MELEE_RESISTANCE)
						&& hit.attacker.isNpc()) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkSetIndex(player, PerkSets.MELEE_RESISTANCE);
					MeleeResistance c = (MeleeResistance) player.getPlayerPerkHandler().getActivePerkSets(player).get(perkIndex)
							.perkSet();
					hit.damage *= c.getMeleeDamageReduction();
				}
			}
		}

		if (player.gauntlet != null && player.gauntlet.inGauntlet) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_GOLDEN_GAUNTLET)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_GOLDEN_GAUNTLET);
				TheGoldenGauntlet c = (TheGoldenGauntlet) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				double calc = 1 - c.getDamageReduction();
				hit.damage *= calc;
			}
		}
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.RAIDING_RESTORATIONS)) {
			if (player.insideRaid && hit.type != HitType.HEAL) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.RAIDING_RESTORATIONS);
				RaidingRestorations c = (RaidingRestorations) player.getPlayerPerkHandler().getActivePerks(player)
						.get(perkIndex).getPerk(player);
				double calc = 1 - c.getDamageReduction();
				hit.damage *= calc;
			}
		}
		if (player.sotdDelay.isDelayed() && hit.attackStyle != null && hit.attackStyle.isMelee()) {
			hit.damage *= 0.50;
		}
		if (hit.attacker != null && player.damageReductionActive() && hit.attacker.isNpc()) {
			hit.damage *= 0.8;
		}
		TransformationRing.check(player);
	}

	private void postDamage(Hit hit) {
		Vengeance.check(player, hit);
		RingOfRecoil.check(player, hit);
		RingOfSuffering.check(player, hit);
		Redemption.check(player);
		PhoenixNecklace.check(player);
		if (player.getCurrentToARaid() != null
				&& player.getCurrentToARaid().getInvocations().contains(Invocations.DEADLY_PRAYERS) && hit.attacker != null
				&& hit.attacker.isNpc()) {
			int damage = hit.damage;
			player.getPrayer().drain(damage / 5);
		}
		RingOfLife.check(player);
		DefenceSkillCape.check(player);

		for (Item item : player.getEquipment().getItems()) {
			if (item != null && item.getDef() != null) {
				item.getDef().postDamage(player, item, hit);
				if (target != null && target.isNpc()) {
					List<String> upgrades = AttributeExtensions.getEffectUpgrades(item);
					boolean hasEffect = upgrades != null;
					if (hasEffect) {
						for (String s : upgrades) {
							try {
								if (s.equalsIgnoreCase("empty"))
									continue;
								ItemEffect effect = ItemEffect.valueOf(s);
								effect.getUpgrade().postPlayerDamage(player, target, item, hit);
							} catch (Exception ex) {
								// System.err.println("Unknown upgrade { " + s + " } found!");
								ex.printStackTrace(System.err);
							}
						}
					}
				}
			}
		}
	}

	private boolean TomeOfSiren(Player player, Hit hit, Entity entity) {
		final int TomeOfSiren = 20714;
		if (hit.attackStyle != null && target != null && target.isNpc()) {
			Item book = player.getEquipment().get(Equipment.SLOT_SHIELD);
			if (book == null) {
				return false;
			}
			if (hit.attackStyle.isMagic() && book.getId() == TomeOfSiren) {
				hit.boostDamage(0.75);
				return true;
			}
		}
		return true;
	}

	/**
	 * Hits from this player
	 */

	private void preTargetDefend(Hit hit, Entity target) {
		forinthrySkullBoost(player, hit, target);
		boolean slayerHelmEffectActive = SlayerHelm.boost(player, target, hit);
		boolean BloodFuryEffectActive = SetEffect.BLOOD_FURY.checkAndApply(player, target, hit);
		boolean veracsEffectActive = SetEffect.VERAC.checkAndApply(player, target, hit);
		boolean dharoksEffectActive = SetEffect.DHAROK.checkAndApply(player, target, hit);
		boolean guthansEffectActive = SetEffect.GUTHAN.checkAndApply(player, target, hit);
		boolean toragsEffectActive = SetEffect.TORAG.checkAndApply(player, target, hit);
		boolean ahrimEffectActive = SetEffect.AHRIM.checkAndApply(player, target, hit);
		boolean karilEffectActive = SetEffect.KARIL.checkAndApply(player, target, hit);
		boolean voidMagesEffectActive = SetEffect.VOID_MAGE.checkAndApply(player, target, hit);
		boolean voidRangeEffectActive = SetEffect.VOID_RANGE.checkAndApply(player, target, hit);
		boolean voidMeleeEffectActive = SetEffect.VOID_MELEE.checkAndApply(player, target, hit);
		boolean eliteVoidMageEffectActive = SetEffect.ELITE_VOID_MAGE.checkAndApply(player, target, hit);
		boolean eliteVoidRangeEffectActive = SetEffect.ELITE_VOID_RANGE.checkAndApply(player, target, hit);
		boolean attackingWithHarmStaff = SetEffect.HARMONISED_NIGHTMARESTAFF.checkAndApply(player, target, hit);
		boolean eliteVoidMeleeEffectActive = SetEffect.ELITE_VOID_MELEE.checkAndApply(player, target, hit);
		boolean berserkerNecklaceEffectActive = SetEffect.BERSERKER_NECKLACE.checkAndApply(player, target, hit);
		boolean obsidianEffectActive = SetEffect.OBSIDIAN_ARMOUR.checkAndApply(player, target, hit);
		boolean justiciarEffectActive = SetEffect.JUSTICIAR.checkAndApply(player, target, hit);
		if (target instanceof NPC) {
			switch (CombatAchievementSystem.getTier(player.combatAchievementPoints)) {
				case MEDIUM -> hit.boostDamage(0.02);
				case HARD -> hit.boostDamage(0.04);
				case ELITE -> hit.boostDamage(0.075);
				case MASTER -> hit.boostDamage(0.125);
				case GRANDMASTER -> hit.boostDamage(0.2);

			}
		}
		if (player.soulflameHornBoost.remaining() > 1) {
			hit.boostAttack(1).boostDamage(0.5);
		}
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30479) {
				hit.boostAttack(0.36).boostDamage(0.18);
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_HAT) != null) {
			if (player.getEquipment().get(Equipment.SLOT_HAT).getId() == 30618 && hit.attackStyle == AttackStyle.RANGED) {
				hit.boostAttack(0.05).boostDamage(0.05);
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_HAT) != null && hit.attackStyle != null) {
			if (player.getEquipment().get(Equipment.SLOT_HAT).getId() == 30777 && hit.attackStyle.isMelee()) {
				hit.boostAttack(0.1).boostDamage(0.1);
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_CHEST) != null && hit.attackStyle != null) {
			if (player.getEquipment().get(Equipment.SLOT_CHEST).getId() == 30779 && hit.attackStyle.isMelee()) {
				hit.boostAttack(0.1).boostDamage(0.1);
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_LEGS) != null && hit.attackStyle != null) {
			if (player.getEquipment().get(Equipment.SLOT_LEGS).getId() == 30781 && hit.attackStyle.isMelee()) {
				hit.boostAttack(0.1).boostDamage(0.1);
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30315 && hit.attackStyle != null
					&& hit.attackStyle == AttackStyle.RANGED) {
				float boost = 0.00f;
				int charges = AttributeExtensions.getCharges(player.getEquipment().get(Equipment.SLOT_WEAPON));
				if (charges > 0)
					boost = 0.05f;
				if (target.isNpc() && target.npc.getId() > 8063 && target.npc.getId() < 8087)
					boost += 0.25f;
				hit.boostAttack(boost).boostDamage(boost);
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30309 && hit.attackStyle != null
					&& hit.attackStyle.isMelee()) {
				float boost = 0.00f;
				int charges = AttributeExtensions.getCharges(player.getEquipment().get(Equipment.SLOT_WEAPON));
				if (charges > 0)
					boost = 0.05f;
				if (target.isNpc() && target.npc.getId() > 8063 && target.npc.getId() < 8087)
					boost += 0.25f;
				hit.boostAttack(boost).boostDamage(boost);
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30312 && hit.attackStyle != null
					&& hit.attackStyle.isMagic()) {
				float boost = 0.00f;
				int charges = AttributeExtensions.getCharges(player.getEquipment().get(Equipment.SLOT_WEAPON));
				if (charges > 0)
					boost = 0.05f;
				if (target.isNpc() && target.npc.getId() > 8063 && target.npc.getId() < 8087)
					boost += 0.25f;
				hit.boostAttack(boost).boostDamage(boost);
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_HAT) != null) {
			if (player.getEquipment().get(Equipment.SLOT_HAT).getId() == 27235 && hit.attackStyle == AttackStyle.RANGED) {
				hit.boostAttack(0.10).boostDamage(0.08);
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_CHEST) != null) {
			if (player.getEquipment().get(Equipment.SLOT_CHEST).getId() == 30619 && hit.attackStyle == AttackStyle.RANGED) {
				hit.boostAttack(0.05).boostDamage(0.05);
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
				AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.BERSERKER)
				&& target.isNpc()) {
			int level = AttributeExtensions.getCharges(AttributeTypes.BERSERKER,
					player.getEquipment().get(Equipment.SLOT_WEAPON));
			double multiplier = 0.001 * level;
			int playerHp = player.getHp();
			if (playerHp > player.getMaxHp())
				playerHp = player.getMaxHp();
			int hitpointsMissing = player.getMaxHp() - playerHp;
			double damageBoost = hitpointsMissing * multiplier;
			hit.boostDamage(damageBoost);
		}
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
			if (player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 28338) {
				if (target != null && target.isNpc()) {
					hit.boostDamage(0.05 * player.soulStacks);
				}
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_CHEST) != null) {
			if (player.getEquipment().get(Equipment.SLOT_CHEST).getId() == 27238 && hit.attackStyle == AttackStyle.RANGED) {
				hit.boostAttack(0.10).boostDamage(0.08);
			}
		}
		if (hit.attackStyle == AttackStyle.RANGED) {
			if (target != null && target.isNpc()) {
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SKILLED_RANGER)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SKILLED_RANGER);
					SkilledRanger c = (SkilledRanger) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
							.getPerk(player);
					double boostAttack = 0.125 * c.getPerkLevel();
					double boostDamage = 0.05 * c.getPerkLevel();
					hit.boostAttack(boostAttack).boostDamage(boostDamage);
				}
			}
		}
		if (player.berserkerRageActive && target.isNpc()) {
			int level = 0;
			if (player.wildernessLevel < 1) {
				List<Integer> equipmentSlots = Arrays.asList(Equipment.SLOT_CHEST, Equipment.SLOT_LEGS, Equipment.SLOT_HAT,
						SLOT_RING, Equipment.SLOT_AMULET, Equipment.SLOT_CAPE);
				for (Integer equipmentSlot : equipmentSlots) {
					if (player.getEquipment().get(equipmentSlot) != null) {
						if (AttributeExtensions.hasAttribute(player.getEquipment().get(equipmentSlot),
								AttributeTypes.BERSERKER_RAGE)) {
							level = AttributeExtensions.getCharges(AttributeTypes.BERSERKER_RAGE,
									player.getEquipment().get(equipmentSlot));
							break;
						}
					}
				}
			}
			double damageBoost = 0.1 * level;
			hit.boostAttack(2).boostDamage(damageBoost);
		}
		if (player.getEquipment().get(Equipment.SLOT_LEGS) != null) {
			if (player.getEquipment().get(Equipment.SLOT_LEGS).getId() == 30620 && hit.attackStyle == AttackStyle.RANGED) {
				hit.boostAttack(0.05).boostDamage(0.05);
			}
		}
		if (player.getPrayer().isActive(Prayer.AUGURY)) {
			if (hit.attackStyle == AttackStyle.MAGIC) {
				hit.boostAttack(0.25).boostDamage(0.04);
			}
		}
		if (player.getPrayer().isActive(Prayer.MYSTIC_MIGHT)) {
			if (hit.attackStyle == AttackStyle.MAGIC) {
				hit.boostAttack(0.15).boostDamage(0.02);
			}
		}
		if (player.getPrayer().isActive(Prayer.MYSTIC_LORE)) {
			if (hit.attackStyle == AttackStyle.MAGIC) {
				hit.boostAttack(0.1).boostDamage(0.01);
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_LEGS) != null) {
			if (player.getEquipment().get(Equipment.SLOT_LEGS).getId() == 27241 && hit.attackStyle == AttackStyle.RANGED) {
				hit.boostAttack(0.10).boostDamage(0.08);
			}
		}
		if (target != null && target.isNpc()) {
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& ratBaneWeaponIds.contains(player.getEquipment().get(Equipment.SLOT_WEAPON).getId())) {
				if (target.npc.getDef().rat) {
					hit.boostDamage(0.25).boostAttack(0.25);
				}
			}
			if (Slayer.isTask(player, target.npc)) {
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.A_SLAYING_EXPERIENCE)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.A_SLAYING_EXPERIENCE);
					ASlayingExperience c = (ASlayingExperience) player.getPlayerPerkHandler().getActivePerks(player)
							.get(perkIndex).getPerk(player);
					hit.boostAttack(c.getBoost()).boostDamage(c.getBoost());
				}
			}
		}

		if (target.npc != null && isTargetMythical(target) && hit.attackStyle != null) {
			Item taintedRing = new Item(30473);
			if (player.getEquipment().get(12) != null && player.getEquipment().get(12).getId() == 30473) {
				hit.boostAttack(0.5).boostDamage(0.2);
			}
		}
		if (target.npc != null && target.npc.getDef() != null && target.npc.getDef().undead && hit.attackStyle != null) {
			// Salve
			if (!slayerHelmEffectActive) {
				if (hit.attackStyle.isMelee() && player.getEquipment().hasId(4081))
					hit.boostAttack(0.16).boostDamage(0.16);
				// Salve E
				if (hit.attackStyle.isMelee() && player.getEquipment().hasId(10588))
					hit.boostAttack(0.20).boostDamage(0.20);
				// Salve I
				if (player.getEquipment().hasId(12017))
					hit.boostAttack(0.16).boostDamage(0.16);
				// Salve EI
				if (player.getEquipment().hasId(12018))
					hit.boostAttack(0.2).boostDamage(0.2);
			}
		}

		if (target.npc != null && target.npc.getDef().leafBladed && hit.attackStyle != null) {
			if (player.getEquipment().hasId(20727))
				hit.boostAttack(0.175).boostDamage(0.175);
		}

		/*
		 * if(target.npc != null && target.npc.getDef().dragon && hit.attackStyle != null) { if
		 * (player.getEquipment().hasId(27037) && player.getEquipment().hasId(27036) && player.getEquipment().hasId(27035))
		 * hit.boostAttack(0.20).boostDamage(0.20); }
		 */

		if (player.getEquipment().hasId(CRYSTAL_BOW)

				/**
				 * Bows
				 */
				|| player.getEquipment().hasId(25888)
				|| player.getEquipment().hasId(25865)
				|| player.getEquipment().hasId(25886)
				|| player.getEquipment().hasId(25884)
				|| player.getEquipment().hasId(25890)
				|| player.getEquipment().hasId(25884)
				|| player.getEquipment().hasId(25867)
				|| player.getEquipment().hasId(25892)
				|| player.getEquipment().hasId(25896)
				|| player.getEquipment().hasId(25894)) {
			if (player.getEquipment().hasId(23971) && target.isNpc()) {
				hit.boostDamage(0.025).boostAttack(0.05);
			}
			if (player.getEquipment().hasId(23975) && target.isNpc()) {
				hit.boostDamage(0.075).boostAttack(0.15);
			} else if (target.isPlayer()) {
				hit.boostDamage(-0.075).boostAttack(-0.15);
			}
			if (player.getEquipment().hasId(23979) && target.isNpc()) {
				hit.boostDamage(0.05).boostAttack(0.10);
			} else if (target.isPlayer()) {
				hit.boostDamage(-0.05).boostAttack(-0.15);
			}
		}

		if (target.npc != null && target.npc.getDef().demon && hit.attackStyle != null) {
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 19675) // Arclight
				hit.boostAttack(0.75).boostDamage(0.75);

			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 29589) // Emberlight
				hit.boostAttack(1.25).boostDamage(1.25);

			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 29591) // Scorching bow
				hit.boostAttack(1.25).boostDamage(1.25);

			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 29594) // Purging staff
				hit.boostAttack(1.25).boostDamage(1.25);

			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 29577) // Burning claws
				hit.boostAttack(0.8).boostDamage(0.8);

			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 6746) // Darklight
				hit.boostAttack(0.75).boostDamage(0.5);
		}

		if (target.npc != null && target.npc.getDef().dragon && hit.attackStyle != null) {
			if (player.getEquipment().get(Equipment.SLOT_FEET) != null
					&& player.getEquipment().get(Equipment.SLOT_FEET).getId() == 30505)
				hit.boostAttack(0.05).boostDamage(0.05);
			// dragon hunter crossbow
			if (hit.attackStyle.isMagic() && player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30593)
				hit.boostAttack(0.5).boostDamage(0.40);
			if (hit.attackStyle.isRanged() && player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 21012)
				hit.boostAttack(0.35).boostDamage(0.30);
			if (hit.attackStyle.isRanged() && player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 27853)
				hit.boostAttack(0.35).boostDamage(0.30);
			if (hit.attackStyle.isRanged() && player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 25916)
				hit.boostAttack(0.35).boostDamage(0.30);
			if (hit.attackStyle.isRanged() && player.getEquipment().hasId(25918))
				hit.boostAttack(0.35).boostDamage(0.30);
			// dragon hunter lance
			if (hit.attackStyle.isMelee() && player.getEquipment().hasId(22978))
				hit.boostAttack(0.25).boostDamage(0.25);
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_DRAGON_SLAYER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_DRAGON_SLAYER);
				TheDragonSlayer c = (TheDragonSlayer) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				if (target.npc.getDef().dragon || target.npc.getDef().name.contains("Galvek")) {
					hit.boostAttack(c.getDamageMultiplier()).boostDamage(c.getDamageMultiplier());
				}
			}
		}
		if (target.npc != null) {
			if (target.isNpc()) {
				if (player.wildernessLevel > 0) {
					if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.WILDERNESS_HUNTER)) {
						int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.WILDERNESS_HUNTER);
						WildernessHunter c = (WildernessHunter) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
								.getPerk(player);
						hit.boostAttack(c.getAccuracyBoost()).boostDamage(c.getDamageBoost());
					}
				}
				if (target.npc.getDef().name.equalsIgnoreCase("zulrah")) {
					if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SNAKE_CHARMER)) {
						int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SNAKE_CHARMER);
						SnakeCharmer c = (SnakeCharmer) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
								.getPerk(player);
						hit.boostAttack(c.getDamageMultiplier()).boostDamage(c.getDamageMultiplier());
					}
				}
			}
		}
		if (player.gauntlet != null && player.gauntlet.inGauntlet) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_GOLDEN_GAUNTLET)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_GOLDEN_GAUNTLET);
				TheGoldenGauntlet c = (TheGoldenGauntlet) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				hit.boostDamage(c.getDamageBoost());
			}
		}
		/*
		 * Drygore blowpipe
		 */
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
				player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30374) {
			double strengthLevel = target.getCombat().getLevel(StatType.Strength);
			// if we're fighting the Combat Dummy
			if (target.isNpc() && target.npc.getDef().name.contains("combat dummy"))
				// check if we have a mockNPC to emulate
				if (player.dummyStats != null)
					// set the level to the mocked NPC
					strengthLevel = player.dummyStats[2].currentLevel;

			double accuracy = 140D + ((3 * strengthLevel - 10D) / 100D)
					- (Math.pow(3 * strengthLevel / 10D - 100D, 2) / 100D);
			accuracy = (140 + accuracy);

			double damage = 250D + ((3 * strengthLevel - 14D) / 100D) - (Math.pow(3 * strengthLevel / 10 - 140, 2) / 100D);
			damage = (damage - 100) / 100;
			accuracy = (accuracy - 100) / 100;

			damage = Math.abs(damage);
			accuracy = Math.abs(accuracy);
			if (damage > 0)
				hit.boostDamage(damage);
			if (accuracy > 0)
				hit.boostAttack(accuracy);
		}

		/* twisted bow */
		boolean hasTwistedBow = player.getEquipment().hasId(20997) || player.getEquipment().hasId(30515);
		if (hit.attackStyle != null && hit.attackStyle.isRanged() && hasTwistedBow) {
			double magicLevel = target.getCombat().getLevel(StatType.Magic);
			// if we're fighting the Combat Dummy
			if (target.isNpc() && target.npc.getDef().name.contains("combat dummy"))
				// check if we have a mockNPC to emulate
				if (player.dummyStats != null)
					// set the level to the mocked NPC
					magicLevel = player.dummyStats[4].currentLevel;
			double accuracy = 140D + ((3 * magicLevel - 10D) / 100D) - (Math.pow(3 * magicLevel / 10D - 100D, 2) / 100D);
			accuracy = (140 + accuracy);

			double damage = 250D + ((3 * magicLevel - 14D) / 100D) - (Math.pow(3 * magicLevel / 10 - 140, 2) / 100D);
			damage = (damage - 100) / 100;
			accuracy = (accuracy - 100) / 100;

			damage = Math.abs(damage);
			accuracy = Math.abs(accuracy);
			if (damage > 0)
				hit.boostDamage(damage);
			if (accuracy > 0)
				hit.boostAttack(accuracy);
		}

		for (Item item : player.getEquipment().getItems()) {
			if (item != null && item.getDef() != null) {
				item.getDef().preTargetDefend(player, item, hit, target);
				if (target != null && target.isNpc()) {
					List<String> upgrades = AttributeExtensions.getEffectUpgrades(item);
					boolean hasEffect = upgrades != null;
					if (hasEffect) {
						for (String s : upgrades) {
							try {
								if (s.equalsIgnoreCase("empty"))
									continue;
								ItemEffect effect = ItemEffect.valueOf(s);
								effect.getUpgrade().preTargetDefend(player, target, item, hit);
							} catch (Exception ex) {
								System.err.println("Unknown upgrade { " + s + " } found!");
								ex.printStackTrace(System.err);
							}
						}
					}
				}
			}
		}

	}

	private void postTargetDefend(Hit hit, Entity target) {
		for (Item item : player.getEquipment().getItems()) {
			if (item != null && item.getDef() != null) {
				item.getDef().postTargetDefend(player, item, hit, target);
				if (target != null && target.isNpc()) {
					List<String> upgrades = AttributeExtensions.getEffectUpgrades(item);
					boolean hasEffect = upgrades != null;
					if (hasEffect) {
						for (String s : upgrades) {
							try {
								if (s.equalsIgnoreCase("empty"))
									continue;
								ItemEffect effect = ItemEffect.valueOf(s);
								effect.getUpgrade().postTargetDefend(player, target, item, hit);
							} catch (Exception ex) {
								System.err.println("Unknown upgrade { " + s + " } found!");
								ex.printStackTrace(System.err);
							}
						}
					}
				}
			}
		}
		if (target.npc != null && target.npc.isFullHP()) {
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions
					.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.BERSERKER)) {
				if (hit.isBlocked()) {
					hit.type = HitType.DAMAGE;
				}

				if (hit.type == HitType.DAMAGE)
					hit.damage = hit.maxDamage;
			}
		}

		if (target.npc != null && ChambersOfXeric.isRaiding(player)) {
			if (target.npc.getId() == 7548 || target.npc.getId() == 7549)
				return;
			if (hit.type != HitType.HEAL)
				ChambersOfXeric.addDamagePoints(player, target.npc, hit.damage);
		}
		if (target.npc != null && player.inCox) {
			if (target.npc.getId() == 7548 || target.npc.getId() == 7549)
				return;
			if (hit.type != HitType.HEAL)
				CustomXericRaid.addDamagePoints(player, target.npc, hit.damage);
		}
	}

	private void preTargetDamage(Hit hit, Entity target) {
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
				player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 28338) {
			if (player.soulStacks < 8) {
				player.soulStacks++;
				player.hit(new Hit().fixedDamage(8));
			}
		}
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
				player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 30369) {
			if (VarPlayerRepository.SUNLIGHT_SPEAR_STACKS.get(player) < 20) {
				VarPlayerRepository.SUNLIGHT_SPEAR_STACKS.increment(player, 1);
			}
		}
		TransformationRing.check(player);
	}

	private void postTargetDamage(Hit hit, Entity target) {
		// If you modify damage inside this method it's absolutely pointless. ("POST"
		// DAMAGE)
		// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\\

		if (player.getPrayer().isActive(Prayer.SMITE)) {
			if (target.player != null) {

				int drain = hit.damage / 4;

				target.player.getPrayer().drain(drain);
			}
		}

		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
				AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON),
						AttributeTypes.WEATHER_WIZARD)
				&& target != null && target.isNpc()) {
			int level = AttributeExtensions.getCharges(AttributeTypes.WEATHER_WIZARD,
					player.getEquipment().get(Equipment.SLOT_WEAPON));
			int chance = 60 - (level * 10);
			if (Random.get(chance) == 0 && player.weatherWizardDelay.remaining() < 1) {
				ItemBreakPerkHandler.handleWeatherWizard(player, target.npc);
			}

		}
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
				AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.DRAGON_WRATH)
				&& target != null && target.isNpc()) {
			int level = AttributeExtensions.getCharges(AttributeTypes.DRAGON_WRATH,
					player.getEquipment().get(Equipment.SLOT_WEAPON));
			int chance = 60 - (level * 10);
			if (Random.get(chance) == 0 && player.dragonWrathDelay.remaining() < 1
					|| player.getName().equalsIgnoreCase("Kal")) {
				ItemBreakPerkHandler.handleDragonWrath(player, target.npc);
			}

		}

		for (Item item : player.getEquipment().getItems()) {
			if (item != null && AttributeExtensions.hasAttribute(item, AttributeTypes.SPECTRAL_GUARDIAN)) {
				ItemBreakPerkHandler.handleSpectralGuardian(player, item);
				break;
			}
		}

		if (target != null && target.isNpc()) {
			List<Integer> equipmentSlots = Arrays.asList(Equipment.SLOT_CHEST, Equipment.SLOT_LEGS, Equipment.SLOT_HAT,
					SLOT_RING, Equipment.SLOT_AMULET, Equipment.SLOT_CAPE, Equipment.SLOT_WEAPON);
			for (Integer equipmentSlot : equipmentSlots) {
				if (player.getEquipment().get(equipmentSlot) != null) {
					if (AttributeExtensions.hasAttribute(player.getEquipment().get(equipmentSlot),
							AttributeTypes.TIME_MANIPULATOR)) {
						int level = AttributeExtensions.getCharges(AttributeTypes.TIME_MANIPULATOR,
								player.getEquipment().get(equipmentSlot));
						int chance = (int) (30 - (level * 1.5));
						if (Random.get(chance) == 0 && player.timeManipulatorDelay.remaining() < 1) {
							int delay = 200 - (level * 15);
							player.timeManipulatorDelay.delaySeconds(delay);
							double multiplier = 0.5 * level;
							int originalAttackTicks = target.npc.getCombat().getInfo().attack_ticks;
							// target.npc.getCombat().getInfo().attack_ticks = originalAttackTicks *=
							// multiplier;
							int finalOriginalAttackTicks = originalAttackTicks;
							World.startEvent(e -> {
								e.delay(15 * level);
								// target.npc.getCombat().getInfo().attack_ticks = finalOriginalAttackTicks;
							});
						}
						break;
					}
					if (AttributeExtensions.hasAttribute(player.getEquipment().get(equipmentSlot),
							AttributeTypes.ETERNAL_RESILIENCE)) {
						int level = AttributeExtensions.getCharges(AttributeTypes.ETERNAL_RESILIENCE,
								player.getEquipment().get(equipmentSlot));
						int chance = (int) (30 - (level * 1.5));
						if (Random.get(chance) == 0 && !player.eternalResilienceActive
								&& player.eternalResilienceDelay.remaining() < 1) {
							player.eternalResilienceActive = true;
							player.graphics(2390);
							player.sendMessage("Your eternal resilience perk has been activated!");
							int delay = 500 / level;
							int activeFor = level * 8;
							player.eternalResilienceDelay.delay(delay);
							World.startEvent(e -> {
								e.delay(activeFor);
								player.eternalResilienceActive = false;
							});
						}
						break;
					}
				}
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
					AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON),
							AttributeTypes.BURNING_WRATH)) {
				int level = AttributeExtensions.getCharges(AttributeTypes.BURNING_WRATH,
						player.getEquipment().get(Equipment.SLOT_WEAPON));
				int chance = (int) (15 - (level * 1.5));
				int burns = (3 * level);
				if (Random.get(chance) == 0 && player.burningWrathDelay.remaining() < 1) {
					player.burningWrathDelay.delay(50);
					World.startEvent(e -> {
						e.setCancelCondition(() -> target.npc.getHp() < 1);
						for (int i = 0; i < burns; i++) {
							target.graphics(78);
							target.npc.hit(new Hit(player).fixedDamage(Random.get(1, 4) * level));
							e.delay(2);
						}
					});
				}
			}
		}

		if (target.isNpc()) {
			if (target.npc.getId() == 11772 && hit.attackStyle != null && hit.attackStyle.isMelee()) {
				updateLastAttack(0);
			}
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.RECURRENT_DAMAGE)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.RECURRENT_DAMAGE);
				RecurrentDamage c = (RecurrentDamage) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				if (Random.get(100) < c.getRecurrentDamageChance() && hit.damage > 0) {
					World.startEvent(e -> {
						e.delay(2);
						target.hit(new Hit(HitType.DAMAGE).fixedDamage(c.getRecurrentDamageAmount(hit.damage)));
					});
				}
			}
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_SOULSPLIT) && target.isNpc()) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_SOULSPLIT);
				TheSoulsplit c = (TheSoulsplit) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				if (Random.rollPercent(c.chanceToHeal())) {
					World.startEvent(e -> {
						e.delay(1);
						int healAmount = c.healAmount(hit.damage);
						if (healAmount > 0 && player.getHp() > 0)
							player.hit(new Hit(HitType.HEAL).fixedDamage(c.healAmount(hit.damage)));
					});
				}
			}
		}

		if (player.getPlayerPerkHandler().getActivePerkSets(player).contains(PerkSets.PRAYER_SIPHON) && target.isNpc()) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkSetIndex(player, PerkSets.PRAYER_SIPHON);
			PrayerSiphon c = (PrayerSiphon) player.getPlayerPerkHandler().getActivePerkSets(player).get(perkIndex).perkSet();
			if (Random.rollPercent(c.getPrayerDrainChance())) {
				player.getStats().get(StatType.Prayer).alter(
						(int) (player.getStats().get(StatType.Prayer).currentLevel + (hit.damage * c.getSiphonMultiplier())));
			}
		}
		if (player.getPlayerPerkHandler().getActivePerkSets(player).contains(PerkSets.BLEED_THEM_DRY) && target.isNpc()) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkSetIndex(player, PerkSets.BLEED_THEM_DRY);
			BleedThemDry c = (BleedThemDry) player.getPlayerPerkHandler().getActivePerkSets(player).get(perkIndex).perkSet();
			if (Random.rollPercent(c.getBleedChance()) && hit.damage > 0)
				if (target.isNpc())
					c.bleedEffect(player, target.npc);
		}

		SetEffect.GUTHAN.checkAndApply(player, target, hit);
		SetEffect.BLOOD_FURY.checkAndApply(player, target, hit);

		for (Item item : player.getEquipment().getItems()) {
			if (item != null && item.getDef() != null)
				item.getDef().postTargetDamage(player, item, hit, target);
		}

		// Process the 'Thunder Khopesh'
		if (player.getEquipment().contains(30388)) {
			if (hit.damage > 0) {
				if (Random.get() < 0.2) {
					final var LIGHTNING_BOLT = 3031;
					target.graphics(LIGHTNING_BOLT);
					// get the target position
					var centerPos = target.getPosition().copy();
					// get anything within a 3x3 area
					var hotSpots = new ArrayList<Position>();
					for (int x = -1; x <= 1; x++)
						for (int y = -1; y < 1; y++)
							hotSpots.add(Position.of(centerPos.getX() + x, centerPos.getY() + y));
					// for everything in the area, thunder fuk them
					var potentialTargets = new ArrayList<Entity>(target.localNpcs());
					target.localNpcs().stream()
							.filter(Objects::nonNull)
							.forEach(potentialTargets::add);

					World.startEvent(4, event -> {
						potentialTargets.forEach(t -> {
							if (t != null) {
								if (hotSpots.contains(t.getPosition())) {
									target.graphics(LIGHTNING_BOLT);
									t.hit(new Hit(HitType.DAMAGE).randDamage(hit.maxDamage));
								}
							}
						});
					});
				}
			}
		}
	}

	private void updateAttackSet() {
		int setIndex = VarPlayerRepository.ATTACK_SET.get(player);
		if (this.weaponType == null) {
			return;
		}
		if ((attackSet = weaponType.attackSets[setIndex]) == null) {
			for (int i = setIndex; i >= 0; i--) {
				if (weaponType.attackSets[i] != null) {
					attackSet = weaponType.attackSets[i];
					VarPlayerRepository.ATTACK_SET.set(player, i);
					break;
				}
			}
		}
	}

	private void deactivateSpecial() {
		specialActive = null;
		specialDistance = weaponType.maxDistance;
		VarPlayerRepository.SPECIAL_ACTIVE.set(player, 0);
	}

	private boolean handleSpecial(AttackStyle attackStyle, AttackType attackType, int maxDamage) {
		Special special = specialActive;

		if (special == null)
			return false;
		int amount = special.getDrainAmount();
		int energy = VarPlayerRepository.SPECIAL_ENERGY.get(player);
		if (player.wildernessLevel < 1) {
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_SPECIAL_ATTACK)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_SPECIAL_ATTACK);
				TheSpecialAttacker c = (TheSpecialAttacker) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
						.getPerk(player);
				amount *= c.getSpecialAttackSaveMultiplier();
			}
			if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
				if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON),
						AttributeTypes.SPECIAL_ENERGY_LOWERER)) {
					if (AttributeExtensions.getCharges(AttributeTypes.SPECIAL_ENERGY_LOWERER,
							player.getEquipment().get(Equipment.SLOT_WEAPON)) > 0) {
						amount *= 0.75;
						SpecialEnergyManagement.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON),
								getAttackStyle());
					}

				}
			}

		}
		if (player.getCurrentToARaid() != null
				&& player.getCurrentToARaid().getInvocations().contains(Invocations.OVERLY_DRAINING)) {
			amount = 100;
		}
		if (player.liquidAdrenalineDelay.remaining() > 0)
			amount /= 2;
		if (amount > energy / 10) {
			player.sendMessage("You need at least " + amount + "% special attack energy to use this.");
			deactivateSpecial();
			return false;
		}

		if (!special.handle(player, target, attackStyle, attackType, maxDamage)) {
			deactivateSpecial();
			return false;
		}

		if (target != null && target.player != null) {
			player.specTeleportDelay.delaySeconds(5);
		}

		boolean ignoresSpecialDrain = (target != null && target.npc != null && target.isNpc()
				&& target.npc.getId() == 2668);
		if (!ignoresSpecialDrain) {
			if (player.wildernessLevel < 1) {
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_SPECIAL_ATTACK)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_SPECIAL_ATTACK);
					TheSpecialAttacker c = (TheSpecialAttacker) player.getPlayerPerkHandler().getActivePerks(player)
							.get(perkIndex).getPerk(player);
					amount *= c.getSpecialAttackSaveMultiplier();
				}
				if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
					if (AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON),
							AttributeTypes.SPECIAL_ENERGY_LOWERER)) {
						if (AttributeExtensions.getCharges(AttributeTypes.SPECIAL_ENERGY_LOWERER,
								player.getEquipment().get(Equipment.SLOT_WEAPON)) > 0) {
							amount *= 0.75;
							SpecialEnergyManagement.consumeCharge(player, player.getEquipment().get(Equipment.SLOT_WEAPON),
									getAttackStyle());
						}

					}
				}
			}

			if (player.getCurrentToARaid() != null
					&& player.getCurrentToARaid().getInvocations().contains(Invocations.OVERLY_DRAINING)) {
				amount = 100;
			}
			if (player.liquidAdrenalineDelay.remaining() > 0)
				amount /= 2;
			int drainAmount = energy - (amount * 10);
			VarPlayerRepository.SPECIAL_ENERGY.set(player, drainAmount); // drain special energy
		}
		this.attackedWithSpecial = true;
		deactivateSpecial();
		return true;
	}

	private void queueGraniteMaulSpecial() {
		graniteMaulSpecials++;
		graniteMaulTimeoutTicks = 5;
	}

	private void checkGraniteMaul() {
		if (graniteMaulTimeoutTicks > 0) {
			if (--graniteMaulTimeoutTicks == 0)
				graniteMaulSpecials = 0;
			else if (graniteMaulTimeoutTicks == 4) // 1 tick less than 5 because it was subtracted
				autoAttackGraniteMaul();
		}
	}

	private void autoAttackGraniteMaul() {
		if (target != null || lastTarget == null)
			return;
		if (player.getHeight() != lastTarget.getHeight())
			return;
		int x = player.getAbsX();
		int y = player.getAbsY();
		if (lastTarget.getSize() == 1) {
			int targetX = lastTarget.getAbsX();
			int targetY = lastTarget.getAbsY();
			int diffX = Math.abs(x - targetX);
			int diffY = Math.abs(y - targetY);
			if ((diffX + diffY) != 1)
				return;
		} else {
			Position closestPos = Misc.getClosestPosition(player, lastTarget);
			int targetX = closestPos.getX();
			int targetY = closestPos.getY();
			int diffX = Math.abs(x - targetX);
			int diffY = Math.abs(y - targetY);
			if (diffX > 1 || diffY > 1)
				return;
		}
		player.face(lastTarget);
		setTarget(lastTarget);
	}

	private boolean specialGraniteMaul() {
		int graniteMaulSpecials = this.graniteMaulSpecials;
		if (graniteMaulSpecials == 0)
			return false;
		this.graniteMaulSpecials = 0;

		ObjType wep = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
		if (wep == null || !wep.graniteMaul)
			return false;

		int energy = VarPlayerRepository.SPECIAL_ENERGY.get(player);
		graniteMaulSpecials = Math.min(graniteMaulSpecials, energy / 500);
		if (graniteMaulSpecials == 0)
			return false;

		AttackStyle style = getAttackStyle();
		AttackType type = getAttackType();
		int maxDamage = CombatUtils.getMaxDamage(player, style, type);
		for (int i = 0; i < graniteMaulSpecials; i++) {
			wep.special.handle(player, target, style, type, maxDamage);
			if (target != null && target.player != null) {
				player.specTeleportDelay.delaySeconds(5);
			}
			energy -= 500;
		}
		VarPlayerRepository.SPECIAL_ENERGY.set(player, energy);
		if (specialActive != null)
			deactivateSpecial();
		return true;
	}

	private void depleteSkull() {
		if (!highRiskSkull && skullDelay > 0 && --skullDelay == 0)
			player.getAppearance().setSkullIcon(-1);
		else if (highRiskSkull && skullDelay > 0 && --skullDelay == 0)
			player.getAppearance().setSkullIcon(-1);
		else if (!highRiskSkull && forinthySkullDelay > 0 && --forinthySkullDelay == 0)
			player.getAppearance().setSkullIcon(-1);
		WildernessKeyHandler.setSkull(player);
	}

	private void resetSkullers() {
		if (skullers != null)
			skullers.clear();
	}

	private void depleteTb() {
		if (tbTicks > 0 && --tbTicks == 0) {
			player.sendMessage("<col=4f006f>The teleblock spell cast on you fades away."); // custom lul
			tbImmunityTicks = 100;
		}
		if (tbImmunityTicks > 0)
			--tbImmunityTicks;
	}

	private void depleteCharge() {
		if (chargeTicks > 0)
			chargeTicks--;
	}
}

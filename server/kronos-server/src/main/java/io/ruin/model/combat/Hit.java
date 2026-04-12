package io.ruin.model.combat;

import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.cache.NpcID;
import io.ruin.cache.ObjType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.AoESwipe;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.ArmourBreaker;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.CriticalHit;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.DamageForHireHigh;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.DamageForHireLow;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.FreezeChance;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.HealthSiphon;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.RespectTheDead;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.SiphonTheDead;
import io.ruin.model.item.actions.impl.itemeffects.itemhandlers.VenomTipped;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.skills.magic.spells.TargetSpell;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Slf4j
@ToString
public class Hit {

	public static double PVP_MAGIC_ACCURACY_MODIFIER = 1.22;

	public static double PVP_MELEE_ACCURACY_MODIFIER = 1.12;

	public HitType type;
	@Getter
	public Entity attacker;
	@Getter
	@Setter
	public Entity victim;
	public AttackStyle attackStyle;
	public AttackType attackType;
	private boolean hidden;

	public int minDamage;
	public int maxDamage;

	public int damage;
	public int damageCap = -1;
	public int staticDamage = -1;

	private Consumer<Entity> preDefend, postDefend;
	private Consumer<Entity> preDamage, postDamage, afterPostDamage;
	@Getter
	private int ticks = 0;
	public boolean resetActions = true;
	public boolean removed;

	public Hit setResetActions(boolean resetActions) {
		this.resetActions = resetActions;
		return this;
	}

	public Hit() {
		this.type = HitType.DAMAGE;
	}

	public Hit(HitType type) {
		this.type = type;
	}

	public Hit(Entity attacker) {
		// count damage for attacker!
		this(attacker, null, null);
	}

	public Hit(Entity attacker, AttackStyle attackStyle) {
		this(attacker, attackStyle, null);
	}

	public Hit(Entity attacker, AttackStyle attackStyle, AttackType attackType) {
		this.type = HitType.DAMAGE;
		this.attacker = attacker;
		this.attackStyle = attackStyle;
		this.attackType = attackType;
	}

	public HitType getHitType(Entity entity) {
		if (entity == victim || entity == attacker) {
			switch (type) {
				case BLOCKED:
					return HitType.BLOCK_OTHER;
				case DAMAGE:
					return damage == maxDamage ? HitType.MAX_HIT : HitType.DAMAGE_OTHER;
				case DAMAGE_SHIELD_ME:
					return HitType.DAMAGE_SHIELD_OTHER;
				case DAMAGE_ZALCANO_ME:
					return HitType.DAMAGE_ZALCANO_OTHER;
				case HEAL_TOTEM_ME:
					return HitType.HEAL_TOTEM_OTHER;
				case DAMAGE_TOTEM_ME:
					return HitType.DAMAGE_TOTEM_OTHER;
			}
		}
		return type;
	}

	public Hit type(HitType type) {
		this.type = type;
		return this;
	}

	public Hit damage(int damage) {
		this.damage = damage;
		return this;
	}

	public Hit min(int damage) {
		this.minDamage = damage;
		return this;
	}

	public Hit max(int damage) {
		this.maxDamage = damage;
		return this;
	}

	public Hit delay(int ticks) {
		this.ticks = ticks;
		if (this.ticks > 30) {
			log.error("", new IllegalStateException("High delay for hit: " + ticks));
			this.nullify();
		}
		return this;
	}

	public Hit clientDelay(int delay, int cycleRate) {
		this.ticks = Math.max(1, (delay * cycleRate) / Server.tickMs());
		if (this.ticks > 30) {
			log.error("", new IllegalStateException("High delay for hit: " + ticks));
		}
		return this;
	}

	public Hit clientDelay(int delay) {
		return clientDelay(delay, 16);
	}

	public Hit nullify() {
		this.ticks = -1;
		return this;
	}

	public boolean isNullified() {
		return ticks == -1;
	}

	/**
	 * Hide (Still hits, just doesn't show on the player)
	 */

	public Hit hide() {
		hidden = true;
		return this;
	}

	public boolean isHidden() {
		return hidden;
	}

	public Hit randDamage(int maxDamage) {
		return randDamage(0, maxDamage);
	}

	public Hit randDamage(int minDamage, int maxDamage) {
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
		return this;
	}

	public Hit fixedDamage(int damage) {
		this.minDamage = damage;
		this.maxDamage = damage;
		return this;
	}

	public Hit capDamage(final int damageCap) {
		this.damageCap = damageCap;
		return this;
	}

	public Hit capDamagePvP(
			final int damageCap,
			@Nullable final Entity target) {
		if (target instanceof Player
				|| (target instanceof NPC npc
						&& (npc.getId() == NpcID.COMBAT_DUMMY
								|| npc.getId() == NpcID.UNDEAD_COMBAT_DUMMY
								|| npc.getId() == 10507))) {
			this.damageCap = damageCap;
		}
		return this;
	}

	/**
	 * Damage boost
	 */

	private double damageBoost;

	public Hit boostDamage(double damageBoost) {
		this.damageBoost += damageBoost;
		return this;
	}

	public double getDamageBoost() {
		return damageBoost;
	}

	/**
	 * Attack boost
	 */

	private double attackBoost;

	public Hit boostAttack(double boost) {
		attackBoost += boost;
		return this;
	}

	public double getAttackBoost() {
		return attackBoost;
	}

	/**
	 * Defence boost
	 */

	private double defenceBoost;

	public Hit boostDefence(double boost) {
		defenceBoost += boost;
		return this;
	}

	public double getDefenceBoost() {
		return defenceBoost;
	}

	/**
	 * Ignores
	 */

	public Hit absorptionIgnored() {
		absorptionIgnored = true;
		return this;
	}

	public boolean defenceIgnored, prayerIgnored, absorptionIgnored;

	public Hit ignoreDefence() {
		defenceIgnored = true;
		return this;
	}

	public Hit ignorePrayer() {
		prayerIgnored = true;
		return this;
	}

	/**
	 * Dragonfire
	 */

	private double dragonfireResistancePenetration;

	public Hit penetrateDragonfireResistance(double amount) {
		dragonfireResistancePenetration += amount;
		return this;
	}

	public double getDragonfireResistancePenetration() {
		return dragonfireResistancePenetration;
	}

	/**
	 * Blocking
	 */

	private boolean blocked;
	public boolean cannotBlock = false;

	public Hit block() {
		if (this.cannotBlock) {
			return this;
		}

		blocked = true;
		damage = 0;
		return this;
	}

	public boolean isBlocked() {
		return blocked;
	}

	/**
	 * Drop
	 */

	private GroundItem dropItem;

	public Hit drop(GroundItem dropItem) {
		this.dropItem = dropItem;
		return this;
	}

	/**
	 * Attacking data
	 */

	public ObjType attackWeapon, rangedWeapon, rangedAmmo;

	public TargetSpell attackSpell;

	public Hit setAttackWeapon(ObjType attackWeapon) {
		this.attackWeapon = attackWeapon;
		return this;
	}

	public Hit setRangedWeapon(ObjType rangedWeapon) {
		this.rangedWeapon = rangedWeapon;
		return this;
	}

	public Hit setRangedAmmo(ObjType rangedAmmo) {
		this.rangedAmmo = rangedAmmo;
		return this;
	}

	public Hit setAttackSpell(TargetSpell attackSpell) {
		this.attackSpell = attackSpell;
		return this;
	}

	public Hit preDefend(Consumer<Entity> preDefend) {
		this.preDefend = preDefend;
		return this;
	}

	public Hit postDefend(Consumer<Entity> postDefend) {
		this.postDefend = postDefend;
		return this;
	}

	public Hit preDamage(Consumer<Entity> preDamage) {
		this.preDamage = preDamage;
		return this;
	}

	public Hit postDamage(Consumer<Entity> postDamage) {
		this.postDamage = postDamage;
		return this;
	}

	public Hit afterPostDamage(Consumer<Entity> afterPostDamage) {
		this.afterPostDamage = afterPostDamage;
		return this;
	}

	public boolean defend(Entity target) {
		if (preDefend != null)
			preDefend.accept(target);
		if (target.hitListener != null && target.hitListener.preDefend != null)
			target.hitListener.preDefend.accept(this);
		if (attacker != null && attacker.hitListener != null && attacker.hitListener.preTargetDefend != null)
			attacker.hitListener.preTargetDefend.accept(this, target);

		if (isNullified()) {
			return false;
		}

		if (!blocked) {
			this.calculateInitialDamage(target);

			if (attackStyle != null && !defenceIgnored) {
				if (attackStyle == AttackStyle.DRAGONFIRE) {
					if (damage <= 0) {
						block();
					}
				} else if (attacker != null) {
					double attackBonus = getAttackBonus() * (1D + attackBoost);
					if (attacker.isPlayer() && target.isPlayer()) {
						if (PVP_MAGIC_ACCURACY_MODIFIER != 0 && attackStyle.isMagic())
							attackBonus *= PVP_MAGIC_ACCURACY_MODIFIER;
						if (PVP_MELEE_ACCURACY_MODIFIER != 0 && attackStyle.isMelee())
							attackBonus *= PVP_MELEE_ACCURACY_MODIFIER;
					}
					if (attackWeapon != null) {
						if (attackWeapon.id == 861) {
							attacker.player.magicShortbowAttacks++;
							if (attacker.player.magicShortbowAttacks == Achievements.ITS_MAGIC_YOU_KNOW.getCompletionAmount())
								attacker.player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
										+ Achievements.ITS_MAGIC_YOU_KNOW.getAchievementName());

						}
					}
					double defenceBonus = getDefenceBonus(target) * (1D + defenceBoost);
					double hitChance;
					if (attackBonus > defenceBonus)
						hitChance = 1D - (defenceBonus + 2D) / (2D * (attackBonus + 1D));
					else
						hitChance = attackBonus / (2D * (defenceBonus + 1D));
					if (attacker.player != null && attacker.player.debug) {
						attacker.player.sendFilteredMessage("Hit:");
						attacker.player.sendFilteredMessage("Chance: " + hitChance);
						attacker.player.sendFilteredMessage("Max Damage: " + maxDamage);
					}
					// added this here cause in the combat handlers it was not setting a hit to guaranteed
					// and we're supposed to have 100% accuracy when Mokhaiotl is these 2 models
					if (target instanceof NPC npc) {
						if (npc.getId() == 14708 || npc.getId() == 14709)
							hitChance = 2;
					}
					if (Random.get() > hitChance)
						block();
				}
			}
		}

		boolean critHit = false;
		if (postDefend != null)
			postDefend.accept(target);
		if (target.hitListener != null && target.hitListener.postDefend != null)
			target.hitListener.postDefend.accept(this);
		if (attacker != null && attacker.hitListener != null && attacker.hitListener.postTargetDefend != null)
			attacker.hitListener.postTargetDefend.accept(this, target);

		if (attacker instanceof Player && target instanceof NPC) {
			if (attacker.player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
				if (AttributeExtensions.hasAttribute(
						attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.HEALTH_SIPHON)) {
					HealthSiphon.consumeCharge(attacker.player, attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), damage,
							target, attackStyle);
				}
				if (AttributeExtensions.hasAttribute(
						attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.CRITICAL_HIT)) {
					if (Random.get(10) == 0) {
						attacker.player.graphics(482);
						critHit = true;
					}
					CriticalHit.consumeCharge(attacker.player, attacker.player.getEquipment().get(Equipment.SLOT_WEAPON),
							attackStyle);
				}
				if (AttributeExtensions.hasAttribute(
						attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.RESPECT_FOR_THE_DEAD)) {
					RespectTheDead.consumeCharge(attacker.player, attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), 0,
							attackStyle);
				}
				if (AttributeExtensions.hasAttribute(
						attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.SIPHON_THE_DEAD)) {
					SiphonTheDead.consumeCharge(attacker.player, attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), 0,
							attackStyle);
				}
				if (AttributeExtensions.hasAttribute(
						attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.DAMAGE_FOR_HIRE_LOW)) {
					DamageForHireLow.consumeCharge(attacker.player,
							attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), attackStyle, damage, target);
				}
				if (AttributeExtensions.hasAttribute(
						attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.DAMAGE_FOR_HIRE_HIGH)) {
					DamageForHireHigh.consumeCharge(attacker.player, attackStyle,
							attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), damage, target);
				}
				if (AttributeExtensions.hasAttribute(
						attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.AOE_SWING)) {
					AoESwipe.consumeCharge(attacker.player,
							attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), target, attackStyle);
				}
				if (AttributeExtensions.hasAttribute(
						attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.FREEZE)) {
					FreezeChance.consumeCharge(attacker.player, attacker.player.getEquipment().get(Equipment.SLOT_WEAPON),
							(NPC) target, attackStyle);
				}
				if (AttributeExtensions.hasAttribute(
						attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.VENOM_TIPPED)) {
					VenomTipped.consumeCharge(attacker.player, attacker.player.getEquipment().get(Equipment.SLOT_WEAPON),
							attackStyle, target);
				}
				if (AttributeExtensions.hasAttribute(
						attacker.player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.ARMOUR_BREAKER)) {
					ArmourBreaker.consumeCharge(attacker.player, attacker.player.getEquipment().get(Equipment.SLOT_WEAPON),
							target, attackStyle);
				}
			}
		}

		if (critHit) {
			damage *= 1.5;
		}

		if (target.npc != null && damage > target.getHp() && type != HitType.HEAL) {
			damage = target.getHp();
		}

		return true;
	}

	public double getAttackBonus() {
		return CombatUtils.getAttackBonus(attacker, attackStyle, attackType);
	}

	public double getDefenceBonus(Entity target) {
		return CombatUtils.getDefenceBonus(target, attackStyle);
	}

	/**
	 * Finish
	 */
	public boolean finish(Entity target) {
		try {
			if (this.ticks > 0) {
				this.ticks -= 1;
				return false;
			}

			if (target.isLocked(LockType.FULL_DELAY_DAMAGE)) {
				return false;
			}

			if (target.isInvincible()) {
				return false;
			}

			if (preDamage != null) {
				preDamage.accept(target);
			}

			if (target.hitListener != null && target.hitListener.preDamage != null) {
				target.hitListener.preDamage.accept(this);
			}

			if (attacker != null && attacker.hitListener != null && attacker.hitListener.preTargetDamage != null) {
				attacker.hitListener.preTargetDamage.accept(this, target);
			}

			// once source of this hit dies, it wont be applied
			if (attacker != null && attacker instanceof Player pAttacker) {
				if (pAttacker.getHp() <= 0) {
					this.removed = true;
					return false;
				}
			}

			if (attacker != null && attacker.isPlayer() && (target != null && target.isPlayer())) {
				this.damage = CombatUtils.normalizeMaxHit(attacker.player, target.player, this.damage);
			}

			if (this.staticDamage != -1) {
				this.damage = this.staticDamage;
			}

			if (damage > 0) {
				if (attacker != null) {
					target.getCombat().addKiller(target, attacker, damage);
				}
				if (type == HitType.HEAL || type == HitType.HEAL_TOTEM_ME || type == HitType.HEAL_TOTEM_OTHER) {
					target.incrementHp(damage);
				} else {
					if (target.incrementHp(-damage) == 0) {
						if (target.isPlayer() && target.player.inTob) {
							target.player.getTheatreRoom().startDeathEvent(target.player);
						} else if (target.isNpc()) {
							target.getCombat().startDeath(this);
						} else {
							target.getCombat().startDeath(this);
						}
					}
				}

				if (shouldBeMaxHit(damage, maxDamage, type)) {
					type = HitType.MAX_HIT;
				}
			} else {
				if (type == HitType.DAMAGE) {
					type = HitType.BLOCKED;
				}
			}

			if (postDamage != null) {
				postDamage.accept(target);
			}

			if (afterPostDamage != null) {
				afterPostDamage.accept(target);
			}

			if (target.hitListener != null && target.hitListener.postDamage != null) {
				target.hitListener.postDamage.accept(this);
			}

			if (attacker != null && attacker.hitListener != null && attacker.hitListener.postTargetDamage != null) {
				attacker.hitListener.postTargetDamage.accept(this, target);
			}

			if (dropItem != null) {
				dropItem.spawn();
			}
		} catch (Exception e) {
			log.error("Error while processing hit: " + this, e);
			return true;
		}

		return true;
	}

	private boolean shouldBeMaxHit(int damage, int maxDamage, HitType type) {
		if (damage < maxDamage) {
			return false;
		}
		return type == HitType.DAMAGE;
	}

	private void calculateInitialDamage(Entity target) {
		if (minDamage == maxDamage) {
			damage = minDamage;
		} else {
			maxDamage *= 1D + damageBoost;
			damage = Random.get(minDamage, maxDamage);
		}
		if (damageCap >= 0 && damage > damageCap) {
			damage = damageCap;
		}

		if (attackStyle != null && attackStyle == AttackStyle.DRAGONFIRE && !defenceIgnored && target.isPlayer()) {
			var targetDragonfireResistance = target.getCombat().getDragonfireResistance();
			damage *= (1 - (Math.max(0, Math.min(1, targetDragonfireResistance) - dragonfireResistancePenetration)));
		}
	}

}

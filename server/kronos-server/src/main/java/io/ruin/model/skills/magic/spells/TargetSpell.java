package io.ruin.model.skills.magic.spells;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.NpcID;
import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ArcaneEnhancement;
import io.ruin.model.activities.perktree.perks.FinestWizardry;
import io.ruin.model.activities.perktree.perks.GodWarsVeteran;
import io.ruin.model.activities.perktree.perks.TakeYourTime;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.AttackType;
import io.ruin.model.combat.CombatUtils;
import io.ruin.model.combat.Hit;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Widget;
import io.ruin.model.inter.handlers.EquipmentStats;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.chargable.TomeOfFire;
import io.ruin.model.item.actions.impl.chargable.TomeOfWater;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.RuneRemoval;
import io.ruin.model.skills.magic.spells.ancient.BloodSpell;
import io.ruin.model.skills.magic.spells.ancient.IceBarrage;
import io.ruin.model.skills.magic.spells.ancient.IceBlitz;
import io.ruin.model.skills.magic.spells.ancient.IceBurst;
import io.ruin.model.skills.magic.spells.ancient.IceRush;
import io.ruin.model.skills.magic.spells.ancient.ShadowSpell;
import io.ruin.model.skills.magic.spells.ancient.SmokeSpell;
import io.ruin.model.skills.magic.spells.modern.EarthBlast;
import io.ruin.model.skills.magic.spells.modern.EarthBolt;
import io.ruin.model.skills.magic.spells.modern.EarthStrike;
import io.ruin.model.skills.magic.spells.modern.EarthSurge;
import io.ruin.model.skills.magic.spells.modern.EarthWave;
import io.ruin.model.skills.magic.spells.modern.FireBlast;
import io.ruin.model.skills.magic.spells.modern.FireBolt;
import io.ruin.model.skills.magic.spells.modern.FireStrike;
import io.ruin.model.skills.magic.spells.modern.FireSurge;
import io.ruin.model.skills.magic.spells.modern.FireWave;
import io.ruin.model.skills.magic.spells.modern.WaterBlast;
import io.ruin.model.skills.magic.spells.modern.WaterBolt;
import io.ruin.model.skills.magic.spells.modern.WaterStrike;
import io.ruin.model.skills.magic.spells.modern.WaterSurge;
import io.ruin.model.skills.magic.spells.modern.WaterWave;
import io.ruin.model.skills.magic.spells.modern.WindBlast;
import io.ruin.model.skills.magic.spells.modern.WindBolt;
import io.ruin.model.skills.magic.spells.modern.WindStrike;
import io.ruin.model.skills.magic.spells.modern.WindSurge;
import io.ruin.model.skills.magic.spells.modern.WindWave;
import io.ruin.model.stat.StatType;

import java.util.Objects;
import java.util.function.BiPredicate;

import static io.ruin.model.entity.player.PlayerCombat.getChebyshevDistance;
import static io.ruin.model.entity.player.PlayerCombat.getMagicHitDelay;

public class TargetSpell extends Spell {

	public static final TargetSpell[] AUTOCASTS = new TargetSpell[53];

	protected int lvlReq;

	boolean ancientSpell = false;

	public double baseXp;

	private int maxDamage;

	protected int animationId;

	protected int[] castGfx;
	protected int[] castSound;

	protected int[] hitGfx;

	protected int hitSoundId = -1;

	private boolean multiTarget;

	protected Projectile projectile;

	public int getLvlReq() {
		return lvlReq;
	}

	public double getBaseXp() {
		return baseXp;
	}

	public int getMaxDamage() {
		return maxDamage;
	}

	public int getAnimationId() {
		return animationId;
	}

	public int[] getCastGfx() {
		return castGfx;
	}

	public int[] getCastSound() {
		return castSound;
	}

	public Projectile getProjectile() {
		return projectile;
	}

	public Item[] getRuneItems() {
		return runeItems;
	}

	protected Item[] runeItems;

	protected BiPredicate<Entity, Entity> castCheck;

	public void setLvlReq(int lvlReq) {
		this.lvlReq = lvlReq;
	}

	public void setBaseXp(double baseXp) {
		this.baseXp = baseXp;
	}

	public void setMaxDamage(int maxDamage) {
		this.maxDamage = maxDamage;
	}

	public void setAnimationId(int animationId) {
		this.animationId = animationId;
	}

	public void setCastGfx(int id, int height, int delay) {
		this.castGfx = new int[] {id, height, delay};
	}

	public void setCastSound(int id, int type, int delay) {
		this.castSound = new int[] {id, type, delay};
	}

	public void setHitGfx(int id, int height) {
		this.hitGfx = new int[] {id, height};
	}

	public void setHitSound(int id) {
		this.hitSoundId = id;
	}

	public void setMultiTarget() {
		this.multiTarget = true;
	}

	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}

	public void setRunes(Item... runeItems) {
		this.runeItems = runeItems;
	}

	public int BlightedSack = -1;

	public void setBlightedSack(int id) {
		this.BlightedSack = id;
	}

	public void setCastCheck(BiPredicate<Entity, Entity> castCheck) {
		this.castCheck = castCheck;
	}

	public void setAutoCast(int index) {
		AUTOCASTS[index] = this;
	}

	public TargetSpell() {
		entityAction = (p, e) -> p.getCombat().queueSpell(this, e);
	}

	public boolean cast(Entity entity, Entity target) {
		return cast(entity, target, -1, maxDamage);
	}

	private boolean cast(Entity source, Entity target, int projectileDuration, int maxDamage) {
		boolean fireSpell = this instanceof FireSurge || this instanceof FireWave || this instanceof FireBlast
				|| this instanceof FireBolt || this instanceof FireStrike;
		boolean waterSpell = this instanceof WaterSurge || this instanceof WaterWave || this instanceof WaterBlast
				|| this instanceof WaterBolt || this instanceof WaterStrike;
		boolean earthSpell = this instanceof EarthSurge || this instanceof EarthWave || this instanceof EarthBlast
				|| this instanceof EarthBolt || this instanceof EarthStrike;
		boolean windSpell = this instanceof WindSurge || this instanceof WindWave || this instanceof WindBlast
				|| this instanceof WindBolt || this instanceof WindStrike;

		double accuracyBoost = 0.0;
		if (source.player != null && target.player != null) {
			if (!source.player.getCombat().canAttack(target.player, true))
				return false;
		} else if (source.npc != null && target.player != null) {
			if (!source.npc.getCombat().canAttack(target.player))
				return false;
		} else if (source.player != null && target.npc != null) {
			if (!source.player.getCombat().canAttack(target.npc, true))
				return false;
		}
		if (target.isNpc() && target.getHp() > 0) {
			if (source.player.getEquipment().get(Equipment.SLOT_WEAPON) != null
					&& AttributeExtensions.hasAttribute(source.player.getEquipment().get(Equipment.SLOT_WEAPON),
							AttributeTypes.ELEMENTAL_CONVERGENCE)) {
				if (fireSpell || waterSpell || windSpell || earthSpell) {
					int level = AttributeExtensions.getCharges(AttributeTypes.ELEMENTAL_CONVERGENCE,
							source.player.getEquipment().get(Equipment.SLOT_WEAPON));
					int chance = 10 - level;
					if (Random.get(chance) == 0 && source.player.elementalConvergenceDelay.remaining() < 1) {
						int delay = 10 - level;
						source.player.elementalConvergenceDelay.delay(delay);
						cast(source, target, projectileDuration + 2, maxDamage);
					}
				}
			}
			if (source.player.getPlayerPerkHandler().getActivePerks(source.player).contains(Perks.ARCANE_ENHANCEMENT)) {
				int perkIndex = source.player.getPlayerPerkHandler().getActivePerkIndex(source.player,
						Perks.ARCANE_ENHANCEMENT);
				ArcaneEnhancement c = (ArcaneEnhancement) source.player.getPlayerPerkHandler().getActivePerks(source.player)
						.get(perkIndex).getPerk(source.player);
				assert c != null;
				maxDamage += (maxDamage * c.getDamageBoost());
			}
			if (TomeOfFire.consumeCharge(source.player) && fireSpell) {

			}
			if (TomeOfWater.consumeCharge(source.player) && waterSpell) {

			}
			if (source.player.getEquipment().get(Equipment.SLOT_SHIELD) != null
					&& source.player.getEquipment().get(Equipment.SLOT_SHIELD).getId() == 20714) {
				if (fireSpell) {
					maxDamage *= 1.1;
				}
			}
			if (source.player.getEquipment().get(Equipment.SLOT_SHIELD) != null
					&& source.player.getEquipment().get(Equipment.SLOT_SHIELD).getId() == 25574) {
				if (waterSpell) {
					maxDamage *= 1.1;
				}
			}
			if (source.player.getPlayerPerkHandler().getActivePerks(source.player).contains(Perks.GOD_WARS_VETERAN)) {
				int perkIndex = source.player.getPlayerPerkHandler().getActivePerkIndex(source.player, Perks.GOD_WARS_VETERAN);
				GodWarsVeteran c = (GodWarsVeteran) source.player.getPlayerPerkHandler().getActivePerks(source.player)
						.get(perkIndex).getPerk(source.player);
				if (target.npc.getDef().id == NpcID.GENERAL_GRAARDOR || target.npc.getId() == NpcID.COMMANDER_ZILYANA
						|| target.npc.getId() == NpcID.KREEARRA || target.npc.getId() == NpcID.KRIL_TSUTSAROTH ||
						target.npc.getDef().name.contains("nex")) {
					double damageBoost = 1;
					assert c != null;
					damageBoost += c.getDamageBonus();
					maxDamage *= damageBoost;
					accuracyBoost += c.getAccuracyBonus();
				}
			}
			boolean ancientSpell = this instanceof BloodSpell || this instanceof SmokeSpell || this instanceof ShadowSpell;

			boolean strikeSpell = this instanceof WindStrike || this instanceof WaterStrike || this instanceof EarthStrike
					|| this instanceof FireStrike;
			if (strikeSpell) {
				if (source.player.getStats().get(StatType.Magic).currentLevel >= 13)
					this.setMaxDamage(8);
				else if (source.player.getStats().get(StatType.Magic).currentLevel >= 9)
					this.setMaxDamage(6);
				else if (source.player.getStats().get(StatType.Magic).currentLevel >= 5)
					this.setMaxDamage(4);
				else
					this.setMaxDamage(2);
			}
			boolean boltSpell = this instanceof WindBolt || this instanceof WaterBolt || this instanceof EarthBolt
					|| this instanceof FireBolt;
			if (boltSpell) {
				if (source.player.getStats().get(StatType.Magic).currentLevel >= 35)
					this.setMaxDamage(12);
				else if (source.player.getStats().get(StatType.Magic).currentLevel >= 29)
					this.setMaxDamage(11);
				else if (source.player.getStats().get(StatType.Magic).currentLevel >= 23)
					this.setMaxDamage(10);
				else
					this.setMaxDamage(9);
			}
			boolean blastSpell = this instanceof WindBlast || this instanceof WaterBlast || this instanceof EarthBlast
					|| this instanceof FireBlast;
			if (blastSpell) {
				if (source.player.getStats().get(StatType.Magic).currentLevel >= 59)
					this.setMaxDamage(16);
				else if (source.player.getStats().get(StatType.Magic).currentLevel >= 53)
					this.setMaxDamage(15);
				else if (source.player.getStats().get(StatType.Magic).currentLevel >= 47)
					this.setMaxDamage(14);
				else
					this.setMaxDamage(13);
			}
			boolean waveSpell = this instanceof WindWave || this instanceof WaterWave || this instanceof EarthWave
					|| this instanceof FireWave;
			if (waveSpell) {
				if (source.player.getStats().get(StatType.Magic).currentLevel >= 75)
					this.setMaxDamage(20);
				else if (source.player.getStats().get(StatType.Magic).currentLevel >= 70)
					this.setMaxDamage(19);
				else if (source.player.getStats().get(StatType.Magic).currentLevel >= 65)
					this.setMaxDamage(18);
				else
					this.setMaxDamage(17);
			}
			boolean surgeSpell = this instanceof WindSurge || this instanceof WaterSurge || this instanceof EarthSurge
					|| this instanceof FireSurge;
			if (surgeSpell) {
				if (source.player.getStats().get(StatType.Magic).currentLevel >= 95)
					this.setMaxDamage(24);
				else if (source.player.getStats().get(StatType.Magic).currentLevel >= 90)
					this.setMaxDamage(23);
				else if (source.player.getStats().get(StatType.Magic).currentLevel >= 85)
					this.setMaxDamage(22);
				else
					this.setMaxDamage(21);
			}
			if (fireSpell) {
				if (target instanceof NPC) {
					if (target.npc.getDef().name.contains("ice") || target.npc.getDef().name.contains("Ice")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("spider")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("troll")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("scarab") || target.npc.getDef().name.contains("Scarab")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("totem") || target.npc.getDef().name.contains("Totem")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Zulrah") || target.npc.getDef().name.contains("Kephri")) {
						maxDamage *= 2;
					}
				}
			}
			if (windSpell || waterSpell || fireSpell || earthSpell) {
				if (source.player.getEquipment().get(Equipment.SLOT_WEAPON) != null
						&& AttributeExtensions.hasAttribute(source.player.getEquipment().get(Equipment.SLOT_WEAPON),
								AttributeTypes.ELEMENTAL_FURY)) {
					int level = AttributeExtensions.getCharges(AttributeTypes.ELEMENTAL_FURY,
							source.player.getEquipment().get(Equipment.SLOT_WEAPON));
					double multiplier = 1 + (0.05 * level);
					maxDamage *= multiplier;
				}
			}
			if (windSpell) {
				if (target instanceof NPC) {
					if (target.npc.getDef().name.contains("Ahrim")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Torag")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Guthan")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Karil")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Verac")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Dharok")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Aviansie")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Kree'arra")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Wingman Skree")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Flockleader Geerin")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Flight Kilisa")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Ghost")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Skeleton")) {
						maxDamage *= 2;
					}
				}
			}
			if (earthSpell) {
				if (target instanceof NPC) {
					if (target.npc.getDef().name.equalsIgnoreCase("Bronze Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Iron Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Steel Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Mithril Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Adamant Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Rune Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Waterfiend")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Wyrm")) {
						maxDamage *= 2;
					}
				}
			}
			if (waterSpell) {
				if (target instanceof NPC) {
					if (target.npc.getDef().name.equalsIgnoreCase("Green Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Red Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Blue Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Black Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Baby Green Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Baby Red Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Baby Blue Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Baby Black Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("King Black Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Lava Dragon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Demonic Gorilla")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Greater demon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Lesser demon")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Hellhound")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Cerberus")) {
						maxDamage *= 2;
					}
					for (int i = 3129; i < 3132; i++) {
						if (target.npc.getId() == i)
							maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Fire giant")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Pyrefiend")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Giant Mole")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.equalsIgnoreCase("Drake")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("sutsaroth")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("pyrelord") || target.npc.getDef().name.contains("Pyrelord")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("troll")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("scarab") || target.npc.getDef().name.contains("Scarab")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("totem") || target.npc.getDef().name.contains("Totem")) {
						maxDamage *= 2;
					}
					if (target.npc.getDef().name.contains("Zulrah") || target.npc.getDef().name.contains("Kephri")) {
						maxDamage *= 2;
					}
				}
			}
			if (source.player.getEquipment().hasId(30522) && ancientSpell) {
				maxDamage *= 1.03;
			}
			if (source.player.getEquipment().hasId(30521) && ancientSpell) {
				maxDamage *= 1.03;
			}
			if (source.player.getEquipment().hasId(30523) && ancientSpell) {
				maxDamage *= 1.03;
			}
		}

		final int distance = getChebyshevDistance(source, target);

		// NOTE: target might teleported out
		if (distance > 64) {
			if (source.player != null) {
				source.player.sendMessage("Target flew.");
			}
			return false;
		}

		final int delayTicks = getMagicHitDelay(distance);

		boolean primaryCast = projectileDuration == -1;
		double percentageBonus = source.getCombat().getBonus(EquipmentStats.MAGIC_DAMAGE);
		if (percentageBonus > 0) {
			maxDamage *= (1D + percentageBonus * 0.01);
		}

		if (primaryCast) {
			if (!removeRunesAndSendProjectile(source.player, target)) {
				return false;
			}
		}

		if (projectile != null) {
			projectileDuration = projectile.duration(source, target);
		}

		if (source.player != null) {
			if (source.player.getPlayerPerkHandler().getActivePerks(source.player).contains(Perks.TAKE_YOUR_TIME)) {
				if (target != null) {
					if (target.isNpc()) {
						int perkIndex = source.player.getPlayerPerkHandler().getActivePerkIndex(source.player,
								Perks.TAKE_YOUR_TIME);
						TakeYourTime c = (TakeYourTime) source.player.getPlayerPerkHandler().getActivePerks(source.player)
								.get(perkIndex).getPerk(source.player);
						assert c != null;
						accuracyBoost += c.getAccuracyBoost();
					}
				}
			}
		}

		Hit hit;
		if (source.player.getEquipment().get(Equipment.SLOT_WEAPON) != null &&
				AttributeExtensions.hasAttribute(source.player.getEquipment().get(Equipment.SLOT_WEAPON),
						AttributeTypes.SOUL_REAVER)
				&& target.isNpc()) {
			int level = AttributeExtensions.getCharges(AttributeTypes.SOUL_REAVER,
					source.player.getEquipment().get(Equipment.SLOT_WEAPON));
			double multiplier = 0.02 * level;
			int chance = 25 - (level * 2);
			if (Random.get(chance) == 0 && source.player.soulReaverDelay.remaining() < 1) {
				int delay = 50 - (level * 3);
				source.player.soulReaverDelay.delay(delay);
				String name = target.npc.getDef().name;
				if (name.contains("dummy")) {
					return false;
				}

				target.hit(new Hit(source.player).fixedDamage((int) (target.npc.getHp() * multiplier)).delay(delayTicks)
						.setAttackWeapon(source.player.getEquipment().get(Equipment.SLOT_WEAPON).getDef()));
				return true;
			}
		}
		if (source.player != null && source.player.getEquipment().get(Equipment.SLOT_WEAPON) != null
				&& AttributeExtensions.hasAttribute(
						source.player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.PRECISION_STRIKE)
				&& target.isNpc()) {
			int minimumHit = 0;
			int level = AttributeExtensions.getCharges(AttributeTypes.PRECISION_STRIKE,
					source.player.getEquipment().get(Equipment.SLOT_WEAPON));
			double multiplier = 0.02 * level;
			minimumHit = (int) (maxDamage * multiplier);
			int chance = 20 - (level * 2);
			if (Random.get(chance) == 0) {
				hit = new Hit(source, AttackStyle.MAGIC, AttackType.ACCURATE)
						.ignoreDefence()
						.ignorePrayer()
						.boostAttack(accuracyBoost)
						.randDamage(minimumHit, maxDamage)
						.delay(delayTicks)
						.setAttackSpell(this);
			} else {
				hit = new Hit(source, AttackStyle.MAGIC, AttackType.ACCURATE)
						.boostAttack(accuracyBoost)
						.randDamage(maxDamage)
						.delay(delayTicks)
						.setAttackSpell(this);
			}
		} else {
			hit = new Hit(source, AttackStyle.MAGIC, AttackType.ACCURATE)
					.boostAttack(accuracyBoost)
					.randDamage(maxDamage)
					.delay(delayTicks)
					.setAttackSpell(this);
		}

		if (hit != null) {
			if (hit.attackSpell != null) {
				if (hit.attackSpell.castGfx != null) {
					if (hit.attackSpell.castGfx.length > 0) {
						if (hit.attackSpell.castGfx[0] == 87) {
							source.player.ibanBlastsCast++;
							if (source.player.ibanBlastsCast == Achievements.IBANT_BELIEVE_THIS.getCompletionAmount())
								source.player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
										+ Achievements.IBANT_BELIEVE_THIS.getAchievementName());
						}

						if (hit.attackSpell.runeItems[0].getId() == 558) {
							source.player.strikeSpellCounter++;
							if (source.player.strikeSpellCounter == Achievements.MIND_GAMES.getCompletionAmount())
								source.player.sendMessage("<col=000080>You have completed the achievement: <col=800000>"
										+ Achievements.MIND_GAMES.getAchievementName());
						}
					}
				}
			}
		}

		hit.postDamage(t -> {
			if (!hit.isBlocked()) {
				if (hit.attackStyle == AttackStyle.MAGIC) {
					if (hit.damage > 0) {
						if (target.isNpc()) {
							if (source.player.getPlayerPerkHandler().getActivePerks(source.player).contains(Perks.FINEST_WIZARDRY)) {
								int perkIndex = source.player.getPlayerPerkHandler().getActivePerkIndex(source.player,
										Perks.FINEST_WIZARDRY);
								FinestWizardry c = (FinestWizardry) source.player.getPlayerPerkHandler().getActivePerks(source.player)
										.get(perkIndex).getPerk(source.player);
								assert c != null;
								if (Random.rollPercent(c.getExtraHitChance()) && source.getCombat().isAttacking(4)) {
									cast(source, target);
								}
							}
						}
					}
				}
			}
		});

		if (target.selfHooks.handle(new Entity.Hook.AttackWithSpellBeforeHit(source, target, this, hit))) {
		}

		beforeHit(hit, target);
		int damage = target.hit(hit);
		if (baseXp > 0 && source.isPlayer()) {
			if (target.isPlayer() || (target.isNpc() && target.npc.getId() != 2668 && target.npc.getId() != 10507)) {
				CombatUtils.addMagicXp(source.player, baseXp, damage, target.isNpc());
			}
		}

		afterHit(hit, target);

		performEndHitEffects(hit, source.player, target);

		if (primaryCast && multiTarget && target.inMulti()) {
			int entityIndex = source.getClientIndex();
			int targetIndex = target.getClientIndex();
			int targetCount = 0;
			int nylocas = 0;
			for (Player player : target.localPlayers()) {
				int playerIndex = player.getClientIndex();
				if (playerIndex == entityIndex || playerIndex == targetIndex)
					continue;
				if (!player.getPosition().isWithinDistance(target.getPosition(), 1))
					continue;
				if (source.player != null) {
					if (!source.player.getCombat().canAttack(player, false))
						continue;
				} else {
					if (!source.npc.getCombat().canAttack(player))
						continue;
				}
				cast(source, player, projectileDuration, maxDamage);
				if (++targetCount >= 9)
					break;
			}

			for (NPC npc : target.localNpcs()) {
				int npcIndex = npc.getClientIndex();
				if (npcIndex == entityIndex || npcIndex == targetIndex) {
					continue;
				}

				if (!npc.getPosition().isWithinDistance(target.getPosition(), 1)) {
					continue;
				}

				if (npc.getDef().ignoreMultiCheck) {
					continue;
				}

				if (source.player != null) {
					if (!source.player.getCombat().canAttack(npc, false))
						continue;
				} else {
					if (!source.npc.getCombat().canAttack(npc))
						continue;
				}

				if (this instanceof IceBarrage) {
					if (npc.getDef().name.toLowerCase().contains("nylocas")) {
						nylocas++;
					}
				}

				if (nylocas >= 4 && source.isPlayer()) {
					Objects.requireNonNull(source.player.combatAchievementsList
							.get(source.player.getCombatAchievementIndexByOrdinal(CombatAchievements.NYLOCAS_ON_THE_ROCKS.ordinal()))
							.getCombatAchievement()).check(source.player);
				}

				cast(source, npc, projectileDuration, maxDamage);
				if (++targetCount >= 9) {
					break;
				}
			}
		}
		return true;
	}

	public void performEndHitEffects(Hit hit, Player source, Entity target) {
		var projectileDuration = this.projectile != null ? this.projectile.duration(source, target) : 1;
		if (hit.isBlocked()) {
			performEndHitBlockedEffects(source, target, projectileDuration);
			hit.hide();
		} else {
			performEndHitEffects(source, target, projectileDuration);
		}
	}

	public void performEndHitBlockedEffects(Player source, Entity target, int projectileDuration) {
		target.graphics(85, 124, projectileDuration);
		target.publicSound(227, 1, projectileDuration);
	}

	public void performEndHitEffects(Player source, Entity target, int projectileDuration) {
		if (hitGfx != null) {
			target.graphics(hitGfx[0], hitGfx[1], projectileDuration);
		}
		if (hitSoundId != -1) {
			target.publicSound(hitSoundId, 1, projectileDuration);
		}
	}

	public boolean removeRunesAndSendProjectile(Player source, Entity target) {
		var duration = this.projectile == null ? 1 : this.projectile.duration(source, target);
		return removeRunesAndSendProjectile(source, target, duration);
	}

	public boolean removeRunes(Player source, Entity target) {
		RuneRemoval r = null;
		if (source.player != null) {
			if (!source.player.getStats().check(StatType.Magic, lvlReq, "cast this spell"))
				return false;
			if (DuelRule.NO_MAGIC.isToggled(source.player)) {
				source.player.sendMessage("Magic has been disabled for this duel!");
				return false;
			}
			if (runeItems != null && (r = RuneRemoval.get(source.player, runeItems)) == null
					&& !source.player.getInventory().hasItem(BlightedSack, 1)) {
				source.player.sendMessage("You don't have enough runes to cast this spell.");
				return false;
			}
		}

		if (castCheck != null && !castCheck.test(source, target)) {
			return false;
		}

		if (BlightedSack != -1 && source.player.getInventory().hasItem(BlightedSack, 1)) {
			source.player.getInventory().remove(BlightedSack, 1);
		} else if (r != null) {
			r.remove();
		}
		return true;

	}

	public boolean removeRunesAndSendProjectile(Player source, Entity target, int duration) {
		if (!removeRunes(source, target)) {
			return false;
		}
		this.sendProjectile(source, target, duration);
		return true;
	}

	public void sendProjectile(Player source, Entity target, int duration) {
		source.animate(animationId);
		if (castGfx != null) {
			source.graphics(castGfx[0], castGfx[1], castGfx[2]);
		}
		if (castSound != null) {
			source.publicSound(castSound[0], castSound[1], castSound[2]);
		}

		// tb should be the only spell this is true for
		if (projectile != null) {
			projectile.send(source, target, duration);
		}
	}

	protected void beforeHit(Hit hit, Entity target) {
		// override if needed lol
	}

	protected void afterHit(Hit hit, Entity target) {
		// override if needed lol
	}

	public boolean isModernSpell(TargetSpell spell) {
		return spell instanceof WindStrike || spell instanceof WaterStrike || spell instanceof EarthStrike
				|| spell instanceof FireStrike
				|| spell instanceof WindBolt || spell instanceof WaterBolt || spell instanceof EarthBolt
				|| spell instanceof FireBolt
				|| spell instanceof WindBlast || spell instanceof WaterBlast || spell instanceof EarthBlast
				|| spell instanceof FireBlast
				|| spell instanceof WindWave || spell instanceof WaterWave || spell instanceof EarthWave
				|| spell instanceof FireWave
				|| spell instanceof WindSurge || spell instanceof WaterSurge || spell instanceof EarthSurge
				|| spell instanceof FireSurge;
	}

	public boolean isAncientSpell(TargetSpell spell) {
		return spell instanceof BloodSpell || spell instanceof SmokeSpell || spell instanceof ShadowSpell
				|| spell instanceof IceBarrage || spell instanceof IceBlitz || spell instanceof IceBurst
				|| spell instanceof IceRush;
	}

	/**
	 * Misc
	 */

	protected static boolean allowHold(Entity entity, Entity target) {
		if (target.hasFreezeImmunity()) {
			if (entity.player != null) {
				if (target.isFrozen())
					entity.player.sendMessage("Your target is already held by a magical force.");
				else
					entity.player.sendMessage("Your target is currently immune to that spell.");
			}
			return false;
		}
		return true;
	}

	protected static void hold(Hit hit, Entity target, int seconds, boolean ice) {
		if (hit.isBlocked() || target.hasFreezeImmunity())
			return;
		target.freeze(seconds, hit.attacker);
		if (ice && target.player != null) {
			target.player.sendMessage(Color.RED, "You have been frozen.");
			target.player.getPacketSender().sendWidgetTimerCustom(Widget.BARRAGE, seconds);
			target.player.startEvent(e -> {
				e.delay(12);
				target.player.sendMessage(Color.RED, "The freeze cast upon you is beginning to thaw.");
			});
		}
	}

}

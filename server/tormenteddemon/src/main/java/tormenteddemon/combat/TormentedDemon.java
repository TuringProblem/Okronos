package tormenteddemon.combat;

import core.api.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;
import io.ruin.utility.TickDelay;
import lombok.Setter;
import tormenteddemon.combat.attacks.autoattacks.AutoAttack;
import tormenteddemon.combat.attacks.autoattacks.MagicAttack;
import tormenteddemon.combat.attacks.autoattacks.MeleeAttack;
import tormenteddemon.combat.attacks.autoattacks.RangedAttack;
import tormenteddemon.combat.attacks.specials.FireBomb;
import tormenteddemon.combat.util.Overhead;
import tormenteddemon.combat.util.WeaponChecker;

import java.util.Arrays;
import java.util.List;

public class TormentedDemon extends NPCCombat {

	AutoAttack currentAttack;
	int currentAttackIndex;
	Overhead currentOverhead;
	boolean shieldActive = false;
	boolean beenHit = false;
	TickDelay flamesDistinguishDelay = new TickDelay();
	int hitsUntilFireBomb = 3;

	int damageSincePrayerSwap = 0;
	@Override
	public void init() {
		npc.hitListener = new HitListener().preDamage(this::preDamage)
				.postDamage(this::postDamage);
		npc.deathEndListener  = (entity, killer, killHit) -> beenHit = false;
		flamesDistinguishDelay.delay(20);
		World.startEvent(3, e -> initAttackStyle());
	}

	private void postDamage(Hit hit) {
		damageSincePrayerSwap += hit.damage;
		if (hit.attacker != null && hit.attacker.isPlayer()) {
			if (!hit.attacker.player.getHealthHud().isOpened())
				hit.attacker.player.getHealthHud().open(true, npc.getId(), npc.getMaxHp());
			hit.attacker.player.getHealthHud().updateValue(npc.getHp());
		}
		if(!beenHit) {
			beenHit = true;
			shieldActive = false;
		}
		if(damageSincePrayerSwap >= 500) {
			if(hit.attacker != null && hit.attacker.player != null && hit.attackStyle != null) {
				if(hit.attackStyle.isMelee()) {
					currentOverhead = Overhead.MELEE;
				} else if(hit.attackStyle.isRanged()) {
					currentOverhead = Overhead.RANGE;
				} else if(hit.attackStyle.isMagic()) {
					currentOverhead = Overhead.MAGIC;
				}
				setCurrentOverhead();
				damageSincePrayerSwap = 0;
			}
		}
	}

	private void preDamage(Hit hit) {
		if (hit.attackStyle != null && hit.attackStyle.isMelee()) {
			if (currentOverhead == Overhead.MELEE) {
				hit.block();
			}
		}
		else if (hit.attackStyle != null && hit.attackStyle.isRanged()) {
			if (currentOverhead == Overhead.RANGE) {
				hit.block();
			}
		}
		else if (hit.attackStyle != null && hit.attackStyle.isMagic()) {
			if (currentOverhead == Overhead.MAGIC) {
				hit.block();
			}
		}
		if(hit.attacker != null && hit.attacker.player != null) {
			if(!shieldActive) {
				if(hit.attacker.player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
					int attackSpeed = hit.attacker.player.getEquipment().get(Equipment.SLOT_WEAPON).getDef().weaponType.attackTicks;
					double damageBoost = ((attackSpeed * attackSpeed) - 16) / 100.0;
					hit.boostDamage(damageBoost);
					shieldActive = true;
					lastShieldGfx = 4;
					npc.transform(13600);
					flamesDistinguishDelay.delay(20);
				} else {
					shieldActive = true;
					lastShieldGfx = 4;
				}
			} else {
				if (hit.attacker.player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
					Item weapon = hit.attacker.player.getEquipment().get(Equipment.SLOT_WEAPON);
					if (weapon != null) {
						if (!WeaponChecker.isDemonbaneOrAbyssal(weapon)) {
							hit.damage *= 0.8;
						}
					}
				}
			}
		}
		if(npc.getId() == 13601)
			hit.ignoreDefence();
	}

	@Override
	public void follow() {
		if(currentAttack instanceof MeleeAttack)
			follow(2);
		else follow(6);
	}

	int triesUntilResetAttack = 3;

	@Setter
	private boolean canAttack = true;

	@Override
	public boolean attack() {
		if(!canAttack)
			return false;
		if(hitsUntilFireBomb <= 0) {
			new FireBomb().send(npc, target.player);
			hitsUntilFireBomb = 5;
			shieldActive = false;
			getNewAttackStyle();
			canAttack = false;
			return true;
		}
		if(currentAttack instanceof MeleeAttack) {
			if(withinDistance(2)) {
				currentAttack.send(npc, (Player) target);
			} else {
				if(triesUntilResetAttack-- <= 0) {
					getNewAttackStyle();
					currentAttack.send(npc, (Player) target);
					triesUntilResetAttack = 3;
					hitsUntilFireBomb = 5;
					return true;
				}
			}
		} else
			currentAttack.send(npc, (Player) target);
		hitsUntilFireBomb--;
		return true;
	}
	int lastShieldGfx = 0;
	@Override
	public void process() {
		if(flamesDistinguishDelay.remaining() < 1 && npc.getId() == 13600) {
			npc.transform(13601);
		}
		if(lastShieldGfx++ >= 2 && shieldActive) {
			lastShieldGfx = 0;
			npc.graphics(2849);
		}
	}

	private void getNewAttackStyle() {
		List<Integer> validAttackStyles = Arrays.asList(0, 1, 2);
		List<Integer> validAttackStylesToUse = validAttackStyles.stream()
				.filter(i -> i != currentAttackIndex)
				.toList();
		int newAttackStyle = validAttackStylesToUse.get(Random.get(validAttackStylesToUse.size() - 1));
		currentAttack = attackStyleList().get(newAttackStyle);
	}

	private void initAttackStyle() {
		int attackStyle = Random.get(2);
		int overhead = Random.get(2);
		currentAttack = attackStyleList().get(attackStyle);
		currentAttackIndex = attackStyle;
		currentOverhead = Overhead.values()[overhead];
		setCurrentOverhead();
	}

	private void setCurrentOverhead() {
		switch (currentOverhead) {
			case MAGIC -> npc.setHeadIcon(NPC.DefaultHeadIconIndex.ProtectFromMagic);
			case RANGE -> npc.setHeadIcon(NPC.DefaultHeadIconIndex.ProtectFromRanged);
			default -> npc.setHeadIcon(NPC.DefaultHeadIconIndex.ProtectFromMelee);
		}
	}

	private List<AutoAttack> attackStyleList() {
		return Arrays.asList(new MeleeAttack(), new RangedAttack(), new MagicAttack());
	}

}

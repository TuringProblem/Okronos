package com.reasonps.dominion.bosses.sol_heredit;

import com.reasonps.dominion.bosses.IDirectional;
import com.reasonps.dominion.bosses.sol_heredit.attacks.*;
import com.reasonps.dominion.bosses.sol_heredit.attacks.types.ShieldAttackType;
import com.reasonps.dominion.bosses.sol_heredit.attacks.types.SpearAttackType;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.TickDelay;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.reasonps.dominion.bosses.sol_heredit.Constants.*;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-28
 */
public class EchoSolHeredit extends NPCCombat implements IDirectional {

	public boolean usedNonSolWeapon = false;
	public boolean damagedPlayer = false;
	public boolean playerRan = false;

	private Bounds bossBounds;

	private final TickDelay comboDelay = new TickDelay();
	@Getter
	private final List<Position> moltenPositions = new ArrayList<>();
	@Getter
	private final List<Position> currentMoltenPositions = new ArrayList<>();
	@Getter
	private final List<NPC> prisms = new ArrayList<>();

	private ShieldAttackType lastShieldAttack = ShieldAttackType.SECONDARY;
	private SpearAttackType lastSpearAttack = SpearAttackType.PRIMARY;

	private int phase = 1;
	private int moltenTicks = 0;
	@Getter @Setter
	private int prismTicksUntilLight = 30;
	@Getter @Setter
	private int prismsSpawned = 0;

	@Setter
	private boolean canAttack = true;

	@Override
	public void init() {
		var region = getNpc().getPosition().getRegion();
		bossBounds = new Bounds(
			region.baseX + 24, region.baseY + 26,
			region.baseX + 41, region.baseY + 43,
			0
		);

		npc.hitsUpdate.hpbarId = 1;
		npc.hitListener = new HitListener()
			.preDamage(this::preDamage)
			.postDamage(this::postDamage);
	}

	@Override
	public boolean allowRespawn() {
		return false;
	}

	@Override
	public void follow() {
		follow(2);
	}

	@Override
	public boolean attack() {
		if (!canAttack)
			return false;
		new MoltenSandAttack(bossBounds).invoke(target.player, this);
		// Try to perform combo attack if in phase 2 or higher
		if (phase > 1 && tryComboAttack())
			return true;
		// else perform a special
		if (Random.get() < SHIELD_ATTACK_PROBABILITY)
			performShieldAttack();
		else
			performSpearAttack();
		return true;
	}

	@Override
	public void process() {
		if (target != null && VarPlayerRepository.RUNNING.get(target.player) == 1)
			playerRan = true;

		if (prismTicksUntilLight > 0)
			prismTicksUntilLight--;

		if (phase == 6) {
			if (moltenTicks++ == 10) {
				moltenTicks = 0;
				if (target != null)
					new MoltenSandAttack(bossBounds).invoke(target.player, this);
			}
		}
		for (var pos : moltenPositions) {
			World.sendGraphics(-1, 0, 0, pos);
			World.sendGraphics(2510, 0, 0, pos);

			getNpc().getPosition().getRegion().players.forEach(p -> {
				if (pos.distance(p.getPosition()) < 1 && npc.getHp() > 0) {
					p.hit(new Hit().randDamage(6, 8));
					damagedPlayer = true;
				}
			});
		}
	}

	private void preDamage(Hit hit) {
		if (hit.damage > 120)
			hit.damage = 120;
	}

	private void postDamage(Hit hit) {
		if (hit.attacker != null && hit.attacker.isPlayer()) {
			if (!hit.attacker.player.getHealthHud().isOpened())
				hit.attacker.player.getHealthHud().open(true, npc.getId(), npc.getMaxHp());
			hit.attacker.player.getHealthHud().updateValue(npc.getHp());
		}
		if (hit.attacker != null && hit.attackStyle != null) {
			if (!hit.attackStyle.isMelee())
				usedNonSolWeapon = true;
			if (hit.attacker.isPlayer() && hit.attacker.player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
				if (!usingSpear(hit.attacker.player))
					usedNonSolWeapon = true;
			}
			else usedNonSolWeapon = true;
		}
		if (phase < 2)
			npc.getCombat().getInfo().attack_ticks = 10;
		if (hit.attacker != null && hit.attacker.isPlayer()) {
			if (getHpPercentage() < 90 && phase == 1) {
				new MoltenSandAttack(bossBounds).invoke(hit.attacker.player, this);
				new PrismSpawnAttack().invoke(hit.attacker.player, this);
				npc.forceText("Not bad. Let's try something else...");
				new HealingTotemAttack().setPhase(phase).invoke(hit.attacker.player, this);
				phase = 2;
			}
			else if (getHpPercentage() < 75 && phase == 2) {
				new MoltenSandAttack(bossBounds).invoke(hit.attacker.player, this);
				new PrismSpawnAttack().invoke(hit.attacker.player, this);
				npc.getCombat().getInfo().attack_ticks = 8;
				npc.forceText("Impressive. Let's see how you handle this...");
				new HealingTotemAttack().setPhase(phase).invoke(hit.attacker.player, this);
				phase = 3;
			}
			else if (getHpPercentage() < 50 && phase == 3) {
				new MoltenSandAttack(bossBounds).invoke(hit.attacker.player, this);
				new PrismSpawnAttack().invoke(hit.attacker.player, this);
				npc.forceText("You can't win!");
				new HealingTotemAttack().setPhase(phase).invoke(hit.attacker.player, this);
				phase = 4;
			}
			else if (getHpPercentage() < 25 && phase == 4) {
				new MoltenSandAttack(bossBounds).invoke(hit.attacker.player, this);
				new PrismSpawnAttack().invoke(hit.attacker.player, this);
				npc.forceText("Ralos guides my hand!");
				new HealingTotemAttack().setPhase(phase).invoke(hit.attacker.player, this);
				phase = 5;
			}
			else if (getHpPercentage() <= 10 && phase == 5) {
				npc.forceText("LET'S END THIS!");
				new MoltenSandAttack(bossBounds).invoke(hit.attacker.player, this);
				new HealingTotemAttack().setPhase(phase).invoke(hit.attacker.player, this);
				phase = 6;
			}
		}
	}

	private int getHpPercentage() {
		return (npc.getHp() * 100) / npc.getMaxHp();
	}

	/**
	 * Attempts to execute a combo attack if certain conditions are met, such as the cooldown period
	 * having finished and a random chance check succeeding. When a combo attack is successfully
	 * initiated, the cooldown is reset, the ability to attack is temporarily disabled,
	 * and a new combo attack sequence is invoked targeting the specified player.
	 *
	 * @return true if the combo attack was successfully executed, false otherwise
	 */
	private boolean tryComboAttack() {
		boolean comboReady = comboDelay.finished() && Random.get(COMBO_ATTACK_CHANCE) == 0;
		if (comboReady) {
			comboDelay.delay(COMBO_COOLDOWN_TICKS);
			canAttack = false;
			new ComboAttack(phase).invoke(target.player, this);
			return true;
		}
		return false;
	}

	/**
	 * Executes a shield-based attack by alternating between primary and secondary shield attack types.
	 * If the last shield attack was a secondary attack, it switches to a primary attack, and vice versa.
	 * Invokes the execution of the chosen shield attack type through the {@code executeShieldAttack} method.
	 * Utilizes the {@code lastShieldAttack} field to track and determine the current attack type.
	 */
	private void performShieldAttack() {
		lastShieldAttack = (lastShieldAttack == ShieldAttackType.SECONDARY) ?
			ShieldAttackType.PRIMARY :
			ShieldAttackType.SECONDARY;
		new ShieldAttack(lastShieldAttack.getPower(), bossBounds).invoke(target.player, this);
	}

	/**
	 * Executes a spear-based attack by alternating between primary and secondary spear attack types.
	 * If the last spear attack was a secondary attack, it switches to a primary attack, and vice versa.
	 * The specific type of spear attack is determined using the {@code lastSpearAttack} field, and the
	 * corresponding attack is invoked.
	 * <p>
	 * The attack alternates between two types:
	 * <l>
	 * 	<li> Primary attack: Triggered if the last spear attack was secondary.
	 * 	<li> Secondary attack: Triggered if the last spear attack was primary.
	 * </l>
	 * </p>
	 * <p>
	 * Uses {@code SpearPrimaryAttack} and {@code SpearSecondaryAttack} classes to execute the
	 * respective spear attack on the target player.
	 */
	private void performSpearAttack() {
		lastSpearAttack = (lastSpearAttack == SpearAttackType.SECONDARY) ?
			SpearAttackType.PRIMARY :
			SpearAttackType.SECONDARY;

		if (lastSpearAttack.equals(SpearAttackType.PRIMARY))
			new SpearPrimaryAttack().invoke(target.player, this);
		else
			new SpearSecondaryAttack().invoke(target.player, this);
	}
}

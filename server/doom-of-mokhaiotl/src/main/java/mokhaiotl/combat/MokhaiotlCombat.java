package mokhaiotl.combat;

import core.api.Random;
import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.object.GameObject;
import io.ruin.utility.TickDelay;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mokhaiotl.combat.attacks.flows.AttackFlow;
import mokhaiotl.combat.attacks.flows.impl.*;
import mokhaiotl.combat.attacks.impl.ChargeBlastAttack;
import mokhaiotl.loot.RewardGenerator;
import mokhaiotl.npc.DoomOfMokhaiotl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.ruin.model.inter.handlers.EquipmentStats.MELEE_STRENGTH;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-29
 */
@Slf4j
public class MokhaiotlCombat extends NPCCombat {

	/**
	 * Animation List
	 * 12405 -> standSeq
	 * 12408 -> Entering Shielded Charge
	 * 12409 -> Shielded standSeq
	 * 12410 -> Shielded ending/breaking free
	 * 12411 -> Standing back after changing
	 * 12412 -> performing an attack... throwing the rocks?
	 * 12413 -> performing an attack... buzzing?
	 * 12414 -> performing a melee stomp
	 * 12415 -> returning from said melee stoping
	 * 12416 -> throwing head up... maybe throwing the rocks?
	 * 12417 -> buried and buzzing
	 * 12418 -> leaving the buried attack/phase
	 * 12419 -> Car Slam
	 * 12420 -> Digging herself into the ground for the burred transformation
	 * 12421 -> buried mode standSeq
	 * 12422 -> Death, orr burying herself into the depth of the dungeon
	 * <p>
	 * Graphics List:
	 * 3384 -> Rock projectile
	 * 3385 -> Blue rock projectile
	 * 3386 -> Rock blowing up
	 * 3387 -> Blue rock blowing up
	 * 3388 - 3403 -> Debris after the rock explodes in the air
	 * 3404 -> Debris
	 * 3412 -> Burrowed charge
	 * 3413 -> Charge Attack full
	 * 3414 -> Shield charge
	 * 3415 -> Eye for burrowed phase
	 * 3416 -> Eye leaving
	 * 3417 -> Shadow of falling larvae
	 * 3427 - 3444 -> Acid splatches
	 * 3445 -> Acid projectile
	 * 3446 -> graphic of the hole spawning in?
	 * 3452 -> Some form of attack... magic?
	 * 3453 -> puff of smoke, moving
	 */
	private final TickDelay shockwaveCooldown = new TickDelay();

	@Getter
	private final TickDelay shieldedPhaseTimer = new TickDelay();

	private int shieldedTickCounter = 0;

	@Getter @Setter
	private boolean burrowed = false;
	@Getter @Setter
	private boolean charging = false;
	@Getter @Setter
	private boolean shielded = false;

	private Player targetedPlayer;

	private final Map<Integer, AttackFlow> attackFlows = Map.of(
		1, new DelveOneAttackFlow(),
		2, new DelveTwoAttackFlow(),
		3, new DelveThreeAttackFlow(),
		4, new DelveFourAttackFlow(),
		5, new DelveFiveAttackFlow(),
		6, new DelveSixAttackFlow(),
		7, new DelveSevenAttackFlow(),
		8, new DelveEightAttackFlow()
	);

	private AttackFlow currentFlow;

	@Override
	public void init() {
		// setup the larger health bar
		getNpc().hitsUpdate.hpbarId = 22;
		// Delay the initial shockwave by 1 min
		shockwaveCooldown.delay(100);
		// setup and initialize the attack flow for this delveLevel
		currentFlow = attackFlows.get(Math.min(8, getDoomOfMokhaiotl().getDelveLevel()));
		if (currentFlow != null)
			currentFlow.init();
		// load up hitListeners
		getDoomOfMokhaiotl().hitListener = new HitListener()
			.preDamage(this::preDamage)
			.postDamage(this::postDamage);
	}

	@Override
	public boolean attack() {
		if (targetedPlayer == null && target != null && target.isPlayer())
			targetedPlayer = target.player;
		if (currentFlow != null) {
			currentFlow.handleAttackFlow(target, this);
			return true;
		}
		return false;
	}

	public void resetBurrowed() {
		getInfo().attack_ticks = 6;
		getDoomOfMokhaiotl().animate(12410);
		getDoomOfMokhaiotl().startEvent(event -> {
			event.delay(3);
			getDoomOfMokhaiotl().transform(14707);
			// Remember to unlock!
			getDoomOfMokhaiotl().unlock();
		});
	}

	private void sprayAcidBlood(Player player) {
		var projectileCount = Random.get(1, 2);
		for (int i = 0; i < projectileCount; i++) {
			var bossPos = getDoomOfMokhaiotl().getCentrePosition();
			var offsetX = Random.get(-getDoomOfMokhaiotl().getSize(), getDoomOfMokhaiotl().getSize());
			var offsetY = Random.get(-getDoomOfMokhaiotl().getSize(), getDoomOfMokhaiotl().getSize());

			if (offsetX < -1 || offsetX > 1 && offsetY < -1 || offsetY > 1) {
				var targetPosition = Position.of(
					bossPos.getX() + offsetX,
					bossPos.getY() + offsetY,
					bossPos.getZ()
				);

				var projectile = new Projectile(3445, 40, 0, 10, 30, 5, 0, 0);
				var delay = projectile.send(getDoomOfMokhaiotl(), targetPosition);
				World.startEvent(World.getTicks(delay), _ -> {
					World.sendGraphics(getAcidHitGraphic(), 0, 0, targetPosition);
					var acidPool = GameObject.spawnUnclipped(getAcidObject(), targetPosition, 10, Random.get(0, 3));
					if (Objects.nonNull(player.currentDynamicMap))
						player.currentDynamicMap.addSpawnedObject(acidPool);
				});
			}
		}
	}

	private int getAcidHitGraphic() {
		return Random.get(List.of(3427, 3428, 3429, 3430, 3431, 3432, 3433, 3434, 3435, 3436));
	}

	private int getAcidObject() {
		return Random.get(List.of(57283, 57284));
	}

	private void preDamage(Hit hit) {
		// Charging/Shield Phase
		if (getDoomOfMokhaiotl().getId() == 14708) {
			// If it's not melee, block it
			if (hit.attackStyle == AttackStyle.MAGIC ||
				hit.attackStyle == AttackStyle.RANGED)
				hit.block();
			else {
				getDoomOfMokhaiotl().animate(12410);
				// Melee Punish
				if (hit.attacker != null && hit.attacker.isPlayer()) {
					var s = hit.attacker.player.getEquipment().bonuses[MELEE_STRENGTH];
					var damage = (1.0 / 5.0) * s;
					getNpc().hit((int) damage);
				}
				// reset the ShieldTickCounter
				shieldedTickCounter = 0;
			}
		}
	}

	private void postDamage(Hit hit) {
		// Unlike OSRS, we have no thralls, thus we're only checking if the attacker is a player
		if (hit.attacker instanceof Player p) {
			if (p.getHealthHud().isOpened())
				p.getHealthHud().updateValue(getNpc().getHp());

			if (p.get("MOKHAIOTL_DELVE_LEVEL", 1) >= 3) {
				if (getDoomOfMokhaiotl().getId() == 14707 && hit.damage > 0) {
					sprayAcidBlood(p);
				}
			}
		}
	}

	@Override
	public void startDeath(Hit killHit) {
		if (targetedPlayer != null) {
			targetedPlayer.delveTimer.stop(targetedPlayer, getTimer(targetedPlayer));
			// generate the loot
			var rewards = new RewardGenerator().generateLoot(targetedPlayer);
			targetedPlayer.mokhaiotlRewardItems.addAll(rewards);
		}

		World.sendGraphics(3446, 0, 0, getDoomOfMokhaiotl().getCentrePosition());
		World.startEvent(getInfo().death_ticks, _ ->
			GameObject.spawn(57285, getDoomOfMokhaiotl().getPosition(), 10, 0));

		super.startDeath(killHit);
	}

	private long getTimer(Player player) {
		var delveLevel = player.get("MOKHAIOTL_DELVE_LEVEL", 1);
		return switch (delveLevel) {
			case 1 -> player.delveOneBestTime;
			case 2 -> player.delveTwoBestTime;
			case 3 -> player.delveThreeBestTime;
			case 4 -> player.delveFourBestTime;
			case 5 -> player.delveFiveBestTime;
			case 6 -> player.delveSixBestTime;
			case 7 -> player.delveSevenBestTime;
			case 8 -> player.delveEightBestTime;
			default -> 0;
		};
	}

	public DoomOfMokhaiotl getDoomOfMokhaiotl() {
		return ((DoomOfMokhaiotl) getNpc());
	}

	@Override
	public void process() {
		if (!isDead() && getDoomOfMokhaiotl().getId() == 14708) {
			getNpc().animate(12409);
			getNpc().graphics(3412);
			// if the charge phase is over, reset the state
			if (shieldedPhaseTimer.finished()) {
				resetShieldedState();
				return;
			}
			// update the yellow health bar with the shieldTickCounter
			getNpc().hitsUpdate.add(8, shieldedTickCounter++, 25);
			// Charge is at full, blast the Player
			if (shieldedTickCounter >= 25) {
				new ChargeBlastAttack().invoke(target.player, this);
				resetShieldedState();
			}
		}
	}

	private void resetShieldedState() {
		getDoomOfMokhaiotl().animate(12410);
		getDoomOfMokhaiotl().transform(14707);
		getDoomOfMokhaiotl().removeHeadIcon();
		shieldedTickCounter = 0;
	}

	@Override
	public void follow() {}

	@Override
	public boolean allowRespawn() {
		return false;
	}

	@Override
	public int getAggressionRange() {
		return 12;
	}

	@Override
	public int getAttackBoundsRange() {
		return 16;
	}
}

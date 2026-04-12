package io.ruin.model.skills.magic.spells.arceuus;

import io.ruin.cache.NPCType;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.content.combatachievements.CombatAchievement;
import io.ruin.model.content.combatachievements.CombatAchievementSystem;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.route.routes.DumbRoute;
import io.ruin.model.map.route.routes.TargetRoute;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.stat.StatType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public class Resurrection extends Spell {


	private static final int DESPAWN_SOUND = 5054;
	private static final int RESURRECT_GHOSTLY_THRALL_SOUND = 5061;
	private static final int RESURRECT_SKELETAL_THRALL_SOUND = 5025;
	private static final int RESURRECT_ZOMBIE_THRALL_SOUND = 5040;
	private static final int RESURRECT_GHOSTLY_THRALL_NPC_GFX = 1903;
	private static final int RESURRECT_SKELETAL_THRALL_NPC_GFX = 1904;
	private static final int RESURRECT_ZOMBIE_THRALL_NPC_GFX = 1905;
	private static final int RESURRECT_GHOSTLY_THRALL_PLAYER_GFX = 1873;
	private static final int RESURRECT_SKELETAL_THRALL_PLAYER_GFX = 1874;
	private static final int RESURRECT_ZOMBIE_THRALL_PLAYER_GFX = 1875;
	private static final int RESURRECT_GHOSTLY_THRALL_NPC_ANIMATION = 9047;
	private static final int RESURRECT_SKELETAL_THRALL_NPC_ANIMATION = 9048;
	private static final int RESURRECT_ZOMBIE_THRALL_NPC_ANIMATION = 9046;
	private static final int RESURRECT_THRALL_PLAYER_ANIMATION = 8973;
	private static final int GHOSTLY_THRALL_PROJECTILE = 1907;
	private static final int SKELETAL_THRALL_PROJECTILE = 1906;
	//TODO: Add guardian thrall from leagues, grandmaster reward so can come later.

	boolean disabled = false;

	public Resurrection(Thralls thralls) {
		registerClick(thralls.getLevelReq(), thralls.getMageExp(), true, thralls.runes, (p, i) -> {
			if (CombatAchievementSystem.getPointsForTier(CombatAchievement.Tier.ELITE) > p.combatAchievementPoints) {
				p.sendMessage("You must be have the elite combat achievements unlocked for this spell.");
				return false;
			}
			if (p.thrallSpawnDelay.isDelayed()) {
				p.sendMessage("You must wait 10 seconds after spawning a thrall.");
				return false;
			}
			if (p.getStats().get(StatType.Prayer).currentLevel < thralls.prayerPointCost) {
				p.sendMessage("You do not have enough prayer points to summon this thrall.");
				return false;
			}
			if (p.thrall != null) {
				returnToGrave(p, p.thrall);
				spawn(p, thralls);
				return false;
			}

			spawn(p, thralls);

			return true;
		});
	}


	public void spawn(Player player, Thralls thralls) {
		Thrall thrall = new Thrall(thralls.getNpcId(), player);
		player.getStats().get(StatType.Prayer).drain(thralls.getPrayerPointCost());
		resurrect(player, thrall);
		thrall.addEvent(e -> {
			while (player.isOnline()) {
				if (player.getCombat().isDead() || player.getMovement().isTeleportQueued()) {
					e.delay(1);
					continue;
				}
				if (!thrall.getPosition().isWithinDistance(player.getPosition())) {
					returnToPlayer(player, thrall);
					e.delay(1);
					continue;
				}
				if (player.getCombat().getTarget() != null && !thrall.getPosition().isWithinDistance(player.getPosition(), 3)) {
					returnToPlayer(player, thrall);
					e.delay(1);
					continue;
				}
				if (player.thrallDespawnDelay.finished()) {
					returnToGrave(player, thrall);
					e.delay(1);
					continue;
				}
				follow(player, thrall);
				e.delay(1);
			}
			returnToGrave(player, thrall);
		});
	}

	private void follow(Player player, NPC npc) {
		if (TargetRoute.inTarget(npc.getAbsX(), npc.getAbsY(), npc.getSize(), player.getAbsX(), player.getAbsY(), player.getSize())) {
			npc.getRouteFinder().routeEntity(player);
		} else {
			DumbRoute.step(npc, player, 1);
		}
	}

	private void resurrect(Player player, Thrall thrall) {
		String thrallName = NPCType.get(thrall.getId()).name;
		int thrallLifeTime = (int) (0.6 * player.getStats().get(StatType.Magic).currentLevel);
		thrall.spawn(player.getAbsX(), player.getAbsY(), player.getHeight());
		player.thrall = thrall;
		thrall.ownerId = player.getUserId();
		if (!Thralls.forNpcId(thrall.getId()).isPresent())
			return;
		Thralls.forNpcId(thrall.getId()).get().getSpawn().start(player, thrall);
		thrall.face(player);
		player.thrallSpawnDelay.delaySeconds(10);
		player.thrallDespawnDelay.delaySeconds(thrallLifeTime);
		player.sendMessage("<col=a60380> You resurrect a " + thrallName + ".");
	}

	private void returnToGrave(Player player, Thrall thrall) {
		String thrallName = NPCType.get(thrall.getId()).name;
		thrall.remove();
		player.thrall = null;
		player.publicSound(DESPAWN_SOUND);
		player.sendMessage("<col=a60380> Your " + thrallName + " has returned to the grave.");
	}

	private void returnToPlayer(Player player, Thrall thrall) {
		thrall.getMovement().teleport(player.getAbsX(), player.getAbsY(), player.getHeight());
	}

	@Getter
	public enum Thralls {
		LESSER_GHOST(10878, new ThrallSpawn(RESURRECT_THRALL_PLAYER_ANIMATION, RESURRECT_GHOSTLY_THRALL_PLAYER_GFX, RESURRECT_GHOSTLY_THRALL_NPC_GFX, RESURRECT_GHOSTLY_THRALL_NPC_ANIMATION, RESURRECT_GHOSTLY_THRALL_SOUND), 2, 38, 55.0, Rune.AIR.toItem(2), Rune.COSMIC.toItem(1), Rune.MIND.toItem(5)),
		LESSER_SKELETON(10881, new ThrallSpawn(RESURRECT_THRALL_PLAYER_ANIMATION, RESURRECT_SKELETAL_THRALL_PLAYER_GFX, RESURRECT_SKELETAL_THRALL_NPC_GFX, RESURRECT_SKELETAL_THRALL_NPC_ANIMATION, RESURRECT_SKELETAL_THRALL_SOUND), 2, 38, 55.0, Rune.AIR.toItem(2), Rune.COSMIC.toItem(1), Rune.MIND.toItem(5)),
		LESSER_ZOMBIE(10884, new ThrallSpawn(RESURRECT_THRALL_PLAYER_ANIMATION, RESURRECT_ZOMBIE_THRALL_PLAYER_GFX, RESURRECT_ZOMBIE_THRALL_NPC_GFX, RESURRECT_ZOMBIE_THRALL_NPC_ANIMATION, RESURRECT_ZOMBIE_THRALL_SOUND), 2, 38, 55.0, Rune.AIR.toItem(2), Rune.COSMIC.toItem(1), Rune.MIND.toItem(5)),

		SUPERIOR_GHOST(10879, new ThrallSpawn(RESURRECT_THRALL_PLAYER_ANIMATION, RESURRECT_GHOSTLY_THRALL_PLAYER_GFX, RESURRECT_GHOSTLY_THRALL_NPC_GFX, RESURRECT_GHOSTLY_THRALL_NPC_ANIMATION, RESURRECT_GHOSTLY_THRALL_SOUND), 4, 57, 70.0, Rune.EARTH.toItem(2), Rune.COSMIC.toItem(1), Rune.DEATH.toItem(5)),
		SUPERIOR_SKELETON(10882, new ThrallSpawn(RESURRECT_THRALL_PLAYER_ANIMATION, RESURRECT_SKELETAL_THRALL_PLAYER_GFX, RESURRECT_SKELETAL_THRALL_NPC_GFX, RESURRECT_SKELETAL_THRALL_NPC_ANIMATION, RESURRECT_SKELETAL_THRALL_SOUND), 4, 57, 70.0, Rune.EARTH.toItem(2), Rune.COSMIC.toItem(1), Rune.DEATH.toItem(5)),
		SUPERIOR_ZOMBIE(10885, new ThrallSpawn(RESURRECT_THRALL_PLAYER_ANIMATION, RESURRECT_ZOMBIE_THRALL_PLAYER_GFX, RESURRECT_ZOMBIE_THRALL_NPC_GFX, RESURRECT_ZOMBIE_THRALL_NPC_ANIMATION, RESURRECT_ZOMBIE_THRALL_SOUND), 4, 57, 70.0, Rune.EARTH.toItem(2), Rune.COSMIC.toItem(1), Rune.DEATH.toItem(5)),

		GREATER_GHOST(10880, new ThrallSpawn(RESURRECT_THRALL_PLAYER_ANIMATION, RESURRECT_GHOSTLY_THRALL_PLAYER_GFX, RESURRECT_GHOSTLY_THRALL_NPC_GFX, RESURRECT_GHOSTLY_THRALL_NPC_ANIMATION, RESURRECT_GHOSTLY_THRALL_SOUND), 6, 76, 88.0, Rune.FIRE.toItem(2), Rune.COSMIC.toItem(1), Rune.BLOOD.toItem(5)),
		GREATER_SKELETON(10883, new ThrallSpawn(RESURRECT_THRALL_PLAYER_ANIMATION, RESURRECT_SKELETAL_THRALL_PLAYER_GFX, RESURRECT_SKELETAL_THRALL_NPC_GFX, RESURRECT_SKELETAL_THRALL_NPC_ANIMATION, RESURRECT_SKELETAL_THRALL_SOUND), 6, 76, 88.0, Rune.FIRE.toItem(2), Rune.COSMIC.toItem(1), Rune.BLOOD.toItem(5)),
		GREATER_ZOMBIE(10886, new ThrallSpawn(RESURRECT_THRALL_PLAYER_ANIMATION, RESURRECT_ZOMBIE_THRALL_PLAYER_GFX, RESURRECT_ZOMBIE_THRALL_NPC_GFX, RESURRECT_ZOMBIE_THRALL_NPC_ANIMATION, RESURRECT_ZOMBIE_THRALL_SOUND), 6, 76, 88.0, Rune.FIRE.toItem(2), Rune.COSMIC.toItem(1), Rune.BLOOD.toItem(5));

		private final ThrallSpawn spawn;
		public final int prayerPointCost, levelReq, npcId;
		public final double mageExp;
		public final Item[] runes;

		Thralls(int npcId, ThrallSpawn spawn, int prayerPointCost, int levelReq, double mageExp, Item... runes) {
			this.spawn = spawn;
			this.prayerPointCost = prayerPointCost;
			this.levelReq = levelReq;
			this.mageExp = mageExp;
			this.npcId = npcId;
			this.runes = runes;
		}

		private static Optional<Thralls> forNpcId(int id) {
			return Arrays.stream(values()).filter(t -> t.npcId == id).findFirst();
		}
	}

	public static class Thrall extends NPC {
		private final Player owner;

		public Thrall(int id, Player owner) {
			super(id);
			this.owner = owner;
		}

		protected void follow(int distance) {
			DumbRoute.step(npc, owner.getCombat().getTarget(), distance);
		}

		@Override
		public void process() {
			super.process();
			Entity lastAttacked = owner.getCombat().getTarget();
			if (lastAttacked != null && lastAttacked.isNpc()) {

				this.addEvent(e ->
				{
					follow(1);
					animate(5578);
					Hit hit = new Hit(this, AttackStyle.CRUSH, null).randDamage(3);
					lastAttacked.hit(hit);
					e.delay(4);
				});
			}
		}

	}

	@AllArgsConstructor
	static class ThrallSpawn {

		int playerAnimation;
		int playerGraphic;
		int npcSpawnGraphic;
		int npcSpawnAnimation;

		int spawnSound;

		void start(Player player, Thrall thrall) {
			thrall.animate(npcSpawnAnimation);
			thrall.graphics(npcSpawnGraphic);
			player.animate(playerAnimation);
			player.graphics(playerGraphic);
			player.publicSound(spawnSound);
		}
	}

}

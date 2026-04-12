package npc.nex.attacks;

import npc.nex.scripts.NexCombat;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.route.routes.ProjectileRoute;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public interface Attack {

	void invoke(Player target, NexCombat combat);

	default List<Player> getPossibleTargets(NexCombat combat) {
		return combat.getNpc().getPosition().getRegion().players.stream()
			.filter(t -> ProjectileRoute.allow(combat.getNpc(), t))
			.collect(Collectors.toList());
	}

	default Boolean targetIsNotInBossRegion(NPC npc, Player target) {
		if (target == null)
			return true;
		var playerRegion = target.player.getPosition().region();
		var bossRegion = npc.getPosition().region();
		return playerRegion != bossRegion;
	}

	int STALAGMITE_MIN_DAMAGE = 10;
	int STALAGMITE_BONUS_DAMAGE = 10;
	String PRAYER_DRAIN = "Your prayer has been drained slightly.";
	// special attacks below
	String BLOOD_SACRIFICE = "I demand a blood sacrifice!";
	int BLOOD_SACRIFICE_GFX_SM = 1986;
	int BLOOD_SACRIFICE_GFX_MED = 1987;
	int BLOOD_SACRIFICE_GFX_LG = 1988;
	int BLOOD_SACRIFICE_GFX_POP = 1911;
	String CONTAIN = "Contain this!";
	String ICE_PRISON = "Die now, in a prison of ice!";
	int SIPHON_DURATION = 7; // 7 ticks
	String SIPHON = "A siphon will solve this!";
	String CHOKE = "Let the virus flow through you!";

	String SHADOW_SMASH = "Fear the shadow!";
	int SHADOW_SMASH_GFX = 383;
	int SHADOW_SMASH_DELAY = 3; // ticks
	int SHADOW_SMASH_MAX_HIT = 50;
	int SHADOW_SMASH_OBJECT_ID = 13721;

	// Stalagmite
	int UNATTACKABLE_STALAGMITE = 42943;
	int ATTACKABLE_STALAGMITE = 42944;
	int STALAGMITE_DIRECTION = 1;

	// normal attacks below
	int MELEE_MAX_HIT = 18;
	double MELEE_PRAYER_EFFECTIVENESS = 1;

	int SMOKE_MAGIC_MAX_HIT = 18;
	double SMOKE_MAGIC_PRAYER_EFFECTIVENESS = 1;
	int SHADOW_RANGE_MAX_HIT = 18;
	double SHADOW_RANGE_PRAYER_EFFECTIVENESS = 1;
	int SHADOW_MAX_PRAYER_DRAIN = 4;

	int BLOOD_MAGIC_MAX_HIT = 18;
	double BLOOD_MAGIC_PRAYER_EFFECTIVENESS = 1;

	int ICE_MAGIC_MAX_HIT = 18;
	double ICE_MAGIC_PRAYER_EFFECTIVENESS = 1;
	double ICE_MAGIC_PRAYER_DRAIN = 0.2; // 0.5 * damage
	double ICE_MAGIC_PRAYER_DRAIN_SPECTRAL = 0.3; // 0.3 * damage
	int ICE_MAGIC_FREEZE_DURATION = 18;

	int ZAROS_MAGIC_MAX_HIT = 18;
	double ZAROS_MAGIC_PRAYER_EFFECTIVENESS = 0.8;
	int ZAROS_MAGIC_PRAYER_DRAIN = 5; // 0.5 * damage
	double ZAROS_MAGIC_PRAYER_DRAIN_SPECTRAL = 0.8;

	int CENTER_POSITION_X = 2925;
	int CENTER_POSITION_Y = 5203;

	// Projectiles
	Projectile SOUL_SPLIT_PROJ = new Projectile(
		2009,
		0,
		43,
		41,
		46,
		7,
		16,
		64
	).regionBased();
	Projectile MINION_SPAWN_PROJ = new Projectile(
		2010,
		43,
		31,
		51,
		56,
		10,
		16,
		64
	).regionBased();
	Projectile WRATH_PROJ = new Projectile(
		2012,
		43,
		0,
		51,
		56,
		10,
		16,
		64
	).regionBased();
	Projectile SMOKE_MAGIC_PROJECTILE = new Projectile(1997,
		43,
		31,
		51,
		56,
		10,
		16,
		64
	).regionBased();
	Projectile SHADOW_RANGE_PROJECTILE = new Projectile(1999,
		43,
		31,
		51,
		56,
		10,
		16,
		64
	).regionBased();
	Projectile BLOOD_MAGIC_PROJECTILE = new Projectile(2000,
		43,
		31,
		51,
		56,
		10,
		16,
		64
	).regionBased();
	Projectile ICE_MAGIC_PROJECTILE = new Projectile(2004,
		43,
		31,
		51,
		56,
		10,
		16,
		64
	).regionBased();
	Projectile ZAROS_MAGIC_PROJECTILE = new Projectile(2007,
		43,
		31,
		51,
		56,
		10,
		16,
		64
	).regionBased();

}

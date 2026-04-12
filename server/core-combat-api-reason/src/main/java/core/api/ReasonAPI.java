// package core.reason;
//
// import io.ruin.model.World;
// import io.ruin.model.combat.Hit;
// import io.ruin.model.combat.HitType;
// import io.ruin.model.entity.Entity;
// import io.ruin.model.map.ClipUtils;
// import io.ruin.model.map.dynamic.DynamicMap;
// import io.ruin.model.map.route.routes.DumbRoute;
// import static io.ruin.model.map.ClipConstants.*;
//
// import java.util.HashSet;
//
// import core.api.API;
// import core.api.common.Animation;
// import core.api.common.Graphic;
// import core.api.npc.*;
// import core.api.player.*;
// import core.api.pos.*;
// import core.task.*;
//
// import static core.reason.ReasonMappings.*;
// import static core.task.api.API.*;
//
// public interface ReasonAPI extends API {
// 	static ReasonAPI INSTANCE = new ReasonAPI() {
// 	};
//
// 	@Override
// 	default NPC npc(int id) {
// 		return new ReasonNPC(id);
// 	}
//
// 	@Override
// 	default void spawn(NPC npc, Position pos) {
// 		((ReasonNPC) npc).spawn(into(pos));
// 	}
//
// 	@Override
// 	default void remove(NPC npc) {
// 		((ReasonNPC) npc).remove();
// 	}
//
// 	@Override
// 	default boolean removed(NPC npc) {
// 		return ((ReasonNPC) npc).isRemoved();
// 	}
//
// 	@Override
// 	default Position pos(NPC npc) {
// 		return into(((ReasonNPC) npc).getPosition());
// 	}
//
// 	@Override
// 	default Position pos(Player player) {
// 		return into(((ReasonPlayer) player).player.getPosition());
// 	}
//
// 	@Override
// 	default Position pos(int x, int y, int z) {
// 		return new Position(x, y, z);
// 	}
//
// 	@Override
// 	default void say(NPC npc, String text) {
// 		into(npc).forceText(text);
// 	}
//
// 	@Override
// 	default void deathAnim(NPC npc, int animation) {
// 		var combat = ((BaseCombat) into(npc).getCombat());
// 		combat.info.death_animation = animation;
// 		combat.info.death_ticks = 2;
// 		combat.update();
// 	}
//
// 	@Override
// 	default StepResult step(NPC _npc, Position towards, StepCheck clips) {
// 		var npc = (ReasonNPC) _npc;
//
// 		if (pos(npc).equals(towards)) {
// 			return StepResult.AtTarget;
// 		}
//
// 		var clip = new ClipUtils() {
// 			@Override
// 			public int getAbs(int absX, int absY, int z) {
// 				if (clips == null) {
// 					return 0;
// 				}
//
// 				if (!clips.allow(pos(absX, absY, z))) {
// 					return UNMOVABLE_MASK;
// 				}
//
// 				return 0;
// 			}
// 		};
//
// 		npc.getMovement().reset();
// 		npc.getRouteFinder().setClip(clip);
// 		var step = DumbRoute.step(npc, towards.x(), towards.y());
// 		if (step) {
// 			return StepResult.Ok;
// 		} else {
// 			return StepResult.Blocked;
// 		}
// 	}
//
// 	@Override
// 	default Players players(NPC npc) {
// 		return new ReasonPlayers(((ReasonNPC) npc).localPlayers());
// 	}
//
// 	@Override
// 	default void teleport(Player player, Position target) {
// 		into(player).getMovement().teleport(into(target));
// 	}
//
// 	@Override
// 	default Players players(Positions positions) {
// 		// FIXME: too expensive
// 		var result = new HashSet<io.ruin.model.entity.player.Player>();
// 		for (var p : World.players()) {
// 			for (var tile : positions.tiles()) {
// 				if (into(tile).equals(p.getPosition())) {
// 					result.add(p);
// 					break;
// 				}
// 			}
// 		}
// 		return new ReasonPlayers(result);
// 	}
//
// 	@Override
// 	default void graphic(Position pos, Graphic gfx) {
// 		World.sendGraphics(gfx.id, gfx.height, gfx.delay, into(pos));
// 	}
//
// 	@Override
// 	default void graphic(NPC npc, Graphic gfx) {
// 		((ReasonNPC) npc).graphics(gfx.id, gfx.height, gfx.delay);
// 	}
//
// 	@Override
// 	default void graphic(Player player, Graphic gfx) {
// 		((ReasonPlayer) player).player.graphics(gfx.id, gfx.height, gfx.delay);
// 	}
//
// 	@Override
// 	default void animate(NPC npc, Animation animation) {
// 		((ReasonNPC) npc).animate(animation.id, animation.delay);
// 	}
//
// 	@Override
// 	default void animate(Player player, Animation animation) {
// 		((ReasonPlayer) player).player.animate(animation.id, animation.delay);
// 	}
//
// 	@Override
// 	default void faceNone(NPC src) {
// 		((ReasonNPC) src).face((Entity) null);
// 	}
//
// 	@Override
// 	default void face(NPC src, Direction target) {
// 		((ReasonNPC) src).face(into(target));
// 	}
//
// 	@Override
// 	default void face(NPC src, Position target) {
// 		((ReasonNPC) src).face(into(target));
// 	}
//
// 	@Override
// 	default void face(NPC src, Player target) {
// 		((ReasonNPC) src).face(((ReasonPlayer) target).player);
// 	}
//
// 	@Override
// 	default void allowCombat(NPC npc) {
// 		((ReasonNPC) npc).setCombat(new BaseCombat());
// 	}
//
// 	@Override
// 	default void noRespawn(NPC npc) {
// 		var combat = ((BaseCombat) ((ReasonNPC) npc).getCombat());
// 		combat.allowRespawn = false;
// 	}
//
// 	@Override
// 	default void stats(NPC npc, int hitpoints, int attack, int strength, int defence, int magic, int ranged) {
// 		var combat = ((BaseCombat) ((ReasonNPC) npc).getCombat());
// 		combat.info.hitpoints = hitpoints;
// 		combat.info.attack = attack;
// 		combat.info.strength = strength;
// 		combat.info.defence = defence;
// 		combat.info.magic = magic;
// 		combat.info.ranged = ranged;
// 		combat.update();
// 		into(npc).setMaxHp(hitpoints);
// 		into(npc).setHp(hitpoints);
// 	}
//
// 	@Override
// 	default void statsAggresive(NPC npc, int attack, int strength, int magic, int magicStrength, int ranged,
// 			int rangedStrength) {
// 		var combat = ((BaseCombat) ((ReasonNPC) npc).getCombat());
// 		combat.info.stab_attack = attack;
// 		combat.info.slash_attack = attack;
// 		combat.info.crush_attack = attack;
// 		combat.info.magic_attack = magicStrength;
// 		combat.info.ranged_attack = rangedStrength;
// 		combat.update();
// 	}
//
// 	@Override
// 	default void statsMagicDefence(NPC npc, int magic) {
// 		var combat = ((BaseCombat) ((ReasonNPC) npc).getCombat());
// 		combat.info.magic_defence = magic;
// 		combat.update();
// 	}
//
// 	@Override
// 	default void statsRangedDefence(NPC npc, int dart, int arrows, int bolts) {
// 		var combat = ((BaseCombat) ((ReasonNPC) npc).getCombat());
// 		combat.info.ranged_defence = dart;
// 		combat.update();
// 	}
//
// 	@Override
// 	default void statsMeleeDefence(NPC npc, int stab, int slash, int crush) {
// 		var combat = ((BaseCombat) ((ReasonNPC) npc).getCombat());
// 		combat.info.stab_defence = stab;
// 		combat.info.slash_defence = slash;
// 		combat.info.crush_defence = crush;
// 		combat.update();
// 	}
//
// 	@Override
// 	default int hpMax(NPC npc) {
// 		return ((ReasonNPC) npc).getMaxHp();
// 	}
//
// 	@Override
// 	default Continuation<FollowResult> follow(NPC src, Player target, int timeout) {
// 		return follow(src, target, Time.server(timeout), null);
// 	}
//
// 	@Override
// 	default Continuation<FollowResult> follow(NPC src, Player target, int timeout, StepCheck check) {
// 		return follow(src, target, Time.server(timeout), check);
// 	}
//
// 	@Override
// 	default Continuation<FollowResult> follow(NPC _src, Player _target, Time timeout, StepCheck check) {
// 		var src = ((ReasonNPC) _src);
// 		var target = ((ReasonPlayer) _target).player;
// 		return new Continuation<>() {
//
// 			@Override
// 			public FollowResult await() {
// 				if (isWithinMeleeDistance(src, target)) {
// 					return FollowResult.Ok;
// 				}
//
// 				var remainingTime = timeout.server().ticks();
// 				while (remainingTime-- != 0) {
// 					switch (step(_src, into(target.getPosition()), check)) {
// 						case AtTarget -> {
// 							return FollowResult.Ok;
// 						}
// 						case Blocked -> {
// 							return FollowResult.Blocked;
// 						}
// 						case Ok -> {
// 						}
// 					}
// 					sleep(1);
//
// 					if (isWithinMeleeDistance(src, target)) {
// 						return FollowResult.Ok;
// 					}
// 				}
// 				return FollowResult.Timeout;
// 			}
// 		};
// 	}
//
// 	@Override
// 	default Continuation<FollowResult> walk(NPC src, Player target, int timeout, StepCheck check) {
// 		return walk(src, ((ReasonPlayer) target).player, Time.server(timeout), check);
// 	}
//
// 	default Continuation<FollowResult> walk(NPC src, Entity target, int timeout, StepCheck check) {
// 		return walk(src, target, Time.server(timeout), check);
// 	}
//
// 	default Continuation<FollowResult> walk(NPC src, Entity target, Time timeout, StepCheck check) {
// 		return walk(src, into(target.getPosition()), timeout, check);
// 	}
//
// 	@Override
// 	default Continuation<FollowResult> walk(NPC _src, Position targetPosition, Time timeout, StepCheck check) {
// 		var src = ((ReasonNPC) _src);
// 		return new Continuation<>() {
// 			@Override
// 			public FollowResult await() {
// 				var target = targetPosition;
//
// 				if (isWithinMeleeDistance(src, target)) {
// 					return FollowResult.Ok;
// 				}
//
// 				var remainingTime = timeout.server().ticks();
// 				while (remainingTime-- != 0) {
// 					switch (step(src, target, check)) {
// 						case AtTarget -> {
// 							return FollowResult.Ok;
// 						}
// 						case Blocked -> {
// 							return FollowResult.Blocked;
// 						}
// 						case Ok -> {
// 						}
// 					}
// 					sleep(1);
//
// 					if (isWithinMeleeDistance(src, target)) {
// 						return FollowResult.Ok;
// 					}
// 				}
// 				return FollowResult.Timeout;
// 			}
//
// 		};
// 	}
//
// 	public static Position pos(DynamicMap map, int x, int y) {
// 		return pos(map, x, y, 0);
// 	}
//
// 	public static Position pos(DynamicMap map, int x, int y, int z) {
// 		var baseRegion = map.swRegion;
// 		var regionX = baseRegion.baseX;
// 		var regionY = baseRegion.baseY;
// 		var localX = x;
// 		var localY = y;
// 		if (localX > 64) {
// 			localX = x - baseRegion.dynamicRegionBaseX;
// 		}
// 		if (localY > 64) {
// 			localY = y - baseRegion.dynamicRegionBaseY;
// 		}
// 		return INSTANCE.pos(regionX + localX, regionY + localY, z);
// 	}
//
// 	public static boolean isWithinMeleeDistance(Entity npc, Position target) {
// 		final int distanceX = npc.getPosition().x() - target.x();
// 		final int distanceY = npc.getPosition().y() - target.y();
// 		final int npcSize = npc.getSize();
// 		final int targetSize = 1;
// 		if (distanceX == -npcSize && distanceY == -npcSize || distanceX == targetSize
// 				&& distanceY == targetSize
// 				|| distanceX == -npcSize && distanceY == targetSize || distanceX == targetSize && distanceY == -npcSize) {
// 			return false;
// 		}
// 		return distanceX >= -npcSize && distanceX <= 1 && distanceY >= -npcSize &&
// 				distanceY <= 1;
// 	}
//
// 	public static boolean isWithinMeleeDistance(Entity npc, Entity target) {
// 		final int distanceX = npc.getPosition().x() - target.getPosition().x();
// 		final int distanceY = npc.getPosition().y() - target.getPosition().y();
// 		final int npcSize = npc.getSize();
// 		final int targetSize = target.getSize();
// 		if (distanceX == -npcSize && distanceY == -npcSize || distanceX == targetSize
// 				&& distanceY == targetSize
// 				|| distanceX == -npcSize && distanceY == targetSize || distanceX == targetSize && distanceY == -npcSize) {
// 			return false;
// 		}
// 		return distanceX >= -npcSize && distanceX <= 1 && distanceY >= -npcSize &&
// 				distanceY <= 1;
// 	}
//
// 	// // TODO: move to generic api
// 	// public static void hit(NPC source, Player target, int damage) {
// 	// 	hit(source, target, damage, HitType.DAMAGE);
// 	// }
// 	//
// 	// public static void hit(NPC source, Player target, int damage, HitType type) {
// 	// 	var hit = new Hit().damage(damage).type(type);
// 	// 	hit.setVictim(into(target));
// 	// 	hit.attacker = into(source);
// 	// 	into(target).hit(new Hit[] { hit });
// 	// }
//
// }

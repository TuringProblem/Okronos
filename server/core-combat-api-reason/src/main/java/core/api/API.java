package core.api;

import io.ruin.model.World;
import io.ruin.model.entity.Entity;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.route.routes.DumbRoute;
import io.ruin.network.incoming.handlers.CommandHandler;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import static core.api.ReasonMappings.*;

// Purpose of having everything within one class is simple:
// import static core.api.API.*;
//
// Each class here is considered 'core', meaning everything else needs
// the be extended/written in the scripting language.
// Each method is also considered 'core', minimal subset to control server entities
// hooks and state.
//
// NOTE: using snake_case because of future's rust-syntax scripting.
@Slf4j
public final class API {

	// ---------------------------------------------------
	// |SPECIAL_CASE: just expose the task api from here |
	// ---------------------------------------------------
	public static interface Continuation extends core.task.Continuation.Void {

	}

	public static void queue(core.task.Continuation<?> task) {
		core.task.api.API.queue(task);
	}

	public static void queue(Continuation.Void task) {
		core.task.Continuations.schedule(task);
	}

	public static core.task.Executor<?> fork(Continuation.Void task) {
		return core.task.Executor.current().spawn(task);
	}

	public static core.task.Executor<?> fork(Continuation.Void continuation, Runnable whenStopped) {
		return fork(new Continuation.Void() {

			@Override
			public void call() {
				continuation.call();
			}

			@Override
			public void started() {
				continuation.started();
			}

			@Override
			public void stopped(StopReason reason) {
				continuation.stopped(reason);
				if (whenStopped != null) {
					whenStopped.run();
				}
			}
		});
	}

	public static void loop(Continuation.Loop.Action action) {
		core.task.Executor.current().await(new Continuation.Loop(action));
	}

	public static core.task.Executor<?> forkLoop(Continuation.Loop.Action action) {
		return fork(new Continuation.Loop(action));
	}

	public static void mark_infinite_loop() {
		core.task.Executor.current().markInfiniteLoop();
	}

	public static void await_children() {
		core.task.Executor.current().awaitChildren();
	}

	public static void sleep(int ticks) {
		core.task.Executor.current().yield(ticks);
	}

	// |----------------|
	// |SPECIAL_CASE_END|
	// |----------------|

	public static enum Direction {
		North, South, East, West, NorthEast, NorthWest, SouthEast, SouthWest
	}

	public static List<Player> players(NPC origin) {
		var result = new ArrayList<Player>();
		origin.ref.localPlayers().stream().map(it -> into(it)).forEach(result::add);
		return result;
	}

	public static List<Player> players(Area origin) {
		var result = new ArrayList<Player>();
		origin.ref.forPlayers(it -> result.add(into(it)));
		return result;
	}

	public static record Area(DynamicMap ref) {
		public void destroy() {
			this.ref.destroy();
		}
	}

	public static Area area(int region) {
		try {
			var map = new DynamicMap().build(region, 1);
			return new Area(map);
		} catch (Exception e) {
			log.error("Unable to create area: ", e);
			return null;
		}
	}

	public static class NPC {
		io.ruin.model.entity.npc.NPC ref;

		NPC(io.ruin.model.entity.npc.NPC ref) {
			this.ref = ref;
		}

		public void spawn(Coord coord) {
			this.ref.spawn(into(coord));
		}

		public void remove() {
			this.ref.remove();
		}

		public void allow_combat() {
			this.ref.setCombat(new BaseCombat());
		}

		public void allow_respawn(boolean allow) {
			var combat = ((BaseCombat) this.ref.getCombat());
			combat.allowRespawn = allow;
		}

		public void stats(int hitpoints, int attack, int strength, int defence, int magic, int ranged) {
			var combat = ((BaseCombat) this.ref.getCombat());
			combat.info.hitpoints = hitpoints;
			combat.info.attack = attack;
			combat.info.strength = strength;
			combat.info.defence = defence;
			combat.info.magic = magic;
			combat.info.ranged = ranged;
			combat.update();
			this.ref.setMaxHp(hitpoints);
			this.ref.setHp(hitpoints);
		}

		public void stats_aggresive(int attack, int str, int magic, int magicStr, int range, int rangeStr) {
			var combat = ((BaseCombat) this.ref.getCombat());
			combat.info.stab_attack = attack;
			combat.info.slash_attack = attack;
			combat.info.crush_attack = attack;
			combat.info.magic_attack = magicStr;
			combat.info.ranged_attack = rangeStr;
			combat.update();
		}

		public void stats_magic_defence(int magic) {
			var combat = ((BaseCombat) this.ref.getCombat());
			combat.info.magic_defence = magic;
			combat.update();
		}

		public void stats_ranged_defence(int dart, int arrows, int bolts) {
			var combat = ((BaseCombat) this.ref.getCombat());
			combat.info.ranged_defence = dart;
			combat.update();
		}

		public void stats_melee_defence(int stab, int slash, int crush) {
			var combat = ((BaseCombat) this.ref.getCombat());
			combat.info.stab_defence = stab;
			combat.info.slash_defence = slash;
			combat.info.crush_defence = crush;
			combat.update();
		}

		public void face_none() {
			this.ref.face((Entity) null);
		}

		public void face(Direction target) {
			this.ref.face(into(target));
		}

		public void face(NPC src, Coord target) {
			this.ref.face(into(target));
		}

		public void face(Player target) {
			this.ref.face(target.ref());
		}

		public void animate(int id) {
			this.ref.animate(id);
		}

		public void graphics(int id) {
			this.ref.graphics(id);
		}

		public void graphics(NPC src, Graphic gfx) {
			this.ref.graphics(gfx.id, 0, gfx.delay);
		}

		public void set_models(int id) {
			this.ref.setModelCustomization(id);
		}

		public void teleport(Coord coord) {
			this.ref.getMovement().teleport(into(coord));
		}

		public void say(String text) {
			this.ref.forceText(text);
		}

		public float hp_perc() {
			return this.ref.getHpPercentage();
		}

		public boolean removed() {
			return this.ref.isRemoved();
		}

		public StepResult step(Coord towards) {
			return step(towards, null);
		}

		public StepResult step(Coord towards, StepCheck clips) {
			var npc = this;
			if (coord(npc).equals(towards)) {
				return StepResult.AtTarget;
			}

			var clip = new io.ruin.model.map.ClipUtils() {
				@Override
				public int getAbs(int absX, int absY, int z) {
					if (clips == null) {
						return 0;
					}

					if (!clips.allow(coord(absX, absY, z))) {
						return io.ruin.model.map.ClipConstants.UNMOVABLE_MASK;
					}

					return 0;
				}
			};

			npc.ref.getMovement().reset();
			npc.ref.getRouteFinder().setClip(clip);
			var step = DumbRoute.step(npc.ref, towards.x(), towards.y());
			if (step) {
				return StepResult.Ok;
			} else {
				return StepResult.Blocked;
			}
		}
	}

	public static NPC npc(int id) {
		var ref = new io.ruin.model.entity.npc.NPC(id);
		return new NPC(ref);
	}



	public static record Player(io.ruin.model.entity.player.Player ref) {

		public boolean is_admin() {
			return this.ref.isAdmin();
		}

		public void teleport(Coord coord) {
			this.ref.getMovement().teleport(into(coord));
		}
	}

	public static record Graphic(int id, int delay) {
	}

	public static Graphic gfx(int id, int delay) {
		return new Graphic(id, delay);
	}

	public static Graphic gfx(int id) {
		return new Graphic(id, 0);
	}

	public static record Coord(int hash) {

		public Coord(final int x, final int y, final int z) {
			this(y | x << 14 | z << 28);
		}

		public int x() {
			return (hash >> 14) & 16383;
		}

		public int y() {
			return hash & 16383;
		}

		public int z() {
			return (hash >> 28) & 3;
		}

		public Coord translate(int dx, int dy) {
			return coord(this.x() + dx, this.y() + dy, this.z());
		}
	}

	public static Coord coord(Area area, int x, int y) {
		return coord(area, x, y, 0);
	}

	public static Coord coord(NPC origin) {
		return into(origin.ref.getPosition().copy());
	}

	public static Coord coord(Player origin) {
		return into(origin.ref.getPosition().copy());
	}

	public static Coord coord(Area area, int x, int y, int z) {
		var baseRegion = area.ref.swRegion;
		var regionX = baseRegion.baseX;
		var regionY = baseRegion.baseY;
		var localX = x;
		var localY = y;
		if (localX > 64) {
			localX = x - baseRegion.dynamicRegionBaseX;
		}
		if (localY > 64) {
			localY = y - baseRegion.dynamicRegionBaseY;
		}
		return coord(regionX + localX, regionY + localY, z);
	}

	public static Coord coord(int x, int y) {
		return new Coord(x, y, 0);
	}

	public static Coord coord(int x, int y, int z) {
		return new Coord(x, y, z);
	}

	public static void graphics(Coord src, Graphic gfx) {
		World.sendGraphics(gfx.id, 0, gfx.delay, into(src));
	}

	public static record GameObject(io.ruin.model.map.object.GameObject ref) {

		public void spawn() {
			this.ref.spawn();
		}
	}

	public static GameObject obj(int id, int type, Coord position) {
		return new GameObject(new io.ruin.model.map.object.GameObject(id, type, into(position)));
	}


	// -------------------
	// |Random generators|
	// -------------------
	public static int rng(int... entries) {
		return io.ruin.api.utils.Random.get(entries);
	}

	public static <T> T rng(List<T> entries) {
		return io.ruin.api.utils.Random.get(entries);
	}

	// ----------
	// |Movement|
	// ----------

	public static StepResult StepOk = StepResult.Ok;
	public static StepResult StepBlocked = StepResult.Blocked;
	public static StepResult StepAtTarget = StepResult.AtTarget;

	public enum StepResult {
		Ok, AtTarget, Blocked
	}

	@FunctionalInterface
	public interface StepCheck {
		boolean allow(Coord coord);
	}


	// -------
	// |Hooks|
	// -------

	public static HookResult Pass = HookResult.Pass;
	public static HookResult Return = HookResult.Return;
	public static HookResult Break = HookResult.Break;

	public static enum HookResult {
		Pass, Break, Return
	}

	public static interface Hook<Ctx> {
		HookResult handle(Ctx ctx);
	}

	public static record OnCmd(Player player, String command) {
	}

	public static void hook_cmd(Hook<OnCmd> hook) {
		CommandHandler.hooks.register(CommandHandler.Hook.Handle.class, (ctx) -> {
			return into(hook.handle(new OnCmd(into(ctx.player()), ctx.command())));
		});
	}

}

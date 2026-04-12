package io.ruin.model.skills.agility.courses.rooftop;

import io.ruin.api.utils.Random;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.Renders;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.agility.courses.AgilityPet;
import io.ruin.model.skills.agility.courses.MarkOfGrace;
import io.ruin.model.stat.StatType;

import java.util.Arrays;
import java.util.List;

public class PollnivneachCourse {

	private static final List<Position> MARK_SPAWNS = Arrays.asList(
		new Position(3349, 2967, 1),
		new Position(3354, 2974, 1),
		new Position(3362, 2979, 1),
		new Position(3369, 2974, 1),
		new Position(3365, 2985, 1),
		new Position(3361, 2981, 2),
		new Position(3362, 2994, 2),
		new Position(3356, 3004, 2),
		new Position(3297, 3168, 3),
		new Position(3301, 3164, 3),
		new Position(3316, 3161, 1)
	);

	public static void register() {
		/**
		 * Basket climb
		 */
		ObjectAction.register(14935, "climb-on", (p, obj) -> p.startEvent(e -> {
			if (!p.getStats().check(StatType.Agility, 20, "attempt this"))
				return;
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.animate(828, 15);
			e.delay(2);
			p.getMovement().teleport(3351, 2964, 1);
			p.resetAnimation();
			p.getStats().addXp(StatType.Agility, 10.0, true);
			p.getMovement().restoreEnergy(Random.get(1, 2));
			p.unlock();
		}));
		/**
		 * Market stall jump on
		 */
		ObjectAction.register(14936, "jump-on", (p, obj) -> p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.animate(2586, 15);
			p.privateSound(2462, 0, 15);
			e.delay(1);
			p.animate(2588);
			p.getMovement().teleport(3350, 2972, 1);
			p.face(Direction.NORTH);
			e.delay(3);
			p.animate(2588);
			p.getMovement().teleport(3352, 2973, 1);
			p.face(Direction.NORTH_EAST);
			p.getStats().addXp(StatType.Agility, 45.0, true);
			p.getMovement().restoreEnergy(Random.get(1, 2));
			p.unlock();
		}));
		Tile.getObject(14936, 3349, 2970, 1).walkTo = new Position(3350, 2968, 1);
		/**
		 * flag grab
		 */
		ObjectAction.register(14937, "grab", (p, obj) -> p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.animate(751);//find right animation
			p.getMovement().teleport(3357, 2978, 1);
			e.delay(2);
			p.getMovement().teleport(3360, 2978, 1);
			p.getStats().addXp(StatType.Agility, 65.0, true);
			p.getMovement().restoreEnergy(Random.get(1, 2));
			p.unlock();
		}));
		Tile.getObject(14937, 3356, 2978, 1).walkTo = new Position(3355, 2976, 1);
		/**
		 * leap gap
		 */
		ObjectAction.register(14938, "leap", (p, obj) -> p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.animate(1995, 15);
			e.delay(1);
			p.animate(1603);
			p.getMovement().force(4, -1, 0, 0, 8, 50, Direction.EAST);
			e.delay(2);
			p.getStats().addXp(StatType.Agility, 35.0, true);
			p.getMovement().restoreEnergy(Random.get(1, 2));
			p.unlock();
		}));
		Tile.getObject(14938, 3363, 2976, 1).walkTo = new Position(3362, 2977, 1);
		/**
		 * Palm tree swing
		 */
		ObjectAction.register(14939, "jump-to", (p, obj) -> p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.privateSound(2468, 1, 10);
			p.animate(2583);
			p.getMovement().force(0, 3, 0, 0, 35, 49, Direction.NORTH);
			p.face(Direction.EAST);
			e.delay(2);
			p.privateSound(2459, 1, 35);
			p.animate(1122);
			p.getMovement().force(0, 1, 0, 0, 34, 42, Direction.NORTH);
			p.face(Direction.EAST);
			e.delay(2);
			p.animate(1124);
			p.getMovement().force(-1, 2, 0, 0, 34, 52, Direction.NORTH_WEST);
			e.delay(1);
			p.privateSound(2455);
			p.animate(2588);
			e.delay(1);
			p.getStats().addXp(StatType.Agility, 75, true);
			p.getMovement().restoreEnergy(Random.get(1, 2));
			p.unlock();
		}));
		Tile.getObject(14939, 3367, 2977, 1).walkTo = new Position(3367, 2976, 1);

		/**
		 * Wall climb
		 */
		ObjectAction.register(14940, "climb", (p, obj) -> p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.animate(828, 15);
			e.delay(2);
			p.getMovement().teleport(3365, 2983, 2);
			p.resetAnimation();
			p.getStats().addXp(StatType.Agility, 5, true);
			p.getMovement().restoreEnergy(Random.get(1, 2));
			p.unlock();
		}));
		/**
		 *  monkey bars
		 */
		ObjectAction.register(14941, "cross", (p, obj) -> p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.animate(742);
			e.delay(1);
			p.getAppearance().setCustomRenders(Renders.MONKEY_BARS);
			p.stepAbs(obj.x, obj.y, StepType.WALK);
			p.getMovement().force(0, 1, 0, 0, 1, 52, Direction.NORTH);
			e.delay(2);
			p.getMovement().force(0, 1, 0, 0, 1, 52, Direction.NORTH);
			e.delay(2);
			p.getMovement().force(0, 1, 0, 0, 1, 52, Direction.NORTH);
			e.delay(2);
			p.getMovement().force(0, 1, 0, 0, 1, 52, Direction.NORTH);
			e.delay(2);
			p.getMovement().force(0, 1, 0, 0, 1, 52, Direction.NORTH);
			e.delay(2);
			p.getMovement().force(0, 1, 0, 0, 1, 52, Direction.NORTH);
			e.delay(2);
			p.getMovement().force(0, 1, 0, 0, 1, 52, Direction.NORTH);
			e.delay(1);
			p.getAppearance().removeCustomRenders();
			p.animate(743);
			p.getStats().addXp(StatType.Agility, 55, true);
			p.unlock();
		}));
		Tile.getObject(14409, 3313, 3186, 3).walkTo = new Position(3314, 3186, 3);

		ObjectAction.register(14944, "jump-on", (p, obj) -> p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.animate(1603, 10);
			p.getMovement().force(0, 2, 0, 0, 35, 49, Direction.NORTH);
			e.delay(3);
			p.animate(1603, 10);
			p.getMovement().force(0, 3, 0, 0, 34, 42, Direction.NORTH);
			e.delay(5);
			p.getStats().addXp(StatType.Agility, 70.0, true);
			p.getMovement().restoreEnergy(Random.get(1, 2));
			p.unlock();
		}));
		Tile.getObject(14944, 3359, 2996, 2).walkTo = new Position(3359, 2995, 2);

		ObjectAction.register(14945, "jump-to", (p, obj) -> p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.animate(2586, 15);
			p.privateSound(2462, 0, 15);
			e.delay(1);
			p.animate(2588);
			p.getMovement().teleport(3364, 3000, 2);
			e.delay(5);
			p.animate(2588);
			p.getMovement().teleport(3364, 2998, 0);
			p.getStats().addXp(StatType.Agility, 540.0, true);
			p.getMovement().restoreEnergy(Random.get(1, 2));
			PlayerCounter.POLLNIVNEACH_ROOFTOP.increment(p, 1);
			PlayerCounter.TOTAL.increment(p, 1);
			RooftopLapCounter.notify(p, "Pollnivneach rooftop", PlayerCounter.POLLNIVNEACH_ROOFTOP.get(p));
			DailyTasks.handleTaskDecrement(p, "pollnivneachRooftop");
			AgilityPet.rollForPet(p, 480);
			PerkTaskHandler.handleCompleteActivity(p, 9);
			MarkOfGrace.rollMark(p, 20, MARK_SPAWNS);
//            Benefits.rollSkillTicket(p);
			p.unlock();
		}));
		Tile.getObject(14945, 3363, 3000, 2).walkTo = new Position(3362, 3002, 2);
	}
}

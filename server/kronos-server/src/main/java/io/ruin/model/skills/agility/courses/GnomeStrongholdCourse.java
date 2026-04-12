package io.ruin.model.skills.agility.courses;

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
import io.ruin.model.skills.agility.courses.rooftop.RooftopLapCounter;
import io.ruin.model.stat.StatType;

public class GnomeStrongholdCourse {

	public static void register() {
		/**
		 * Log balance
		 */
		ObjectAction.register(23145, "walk-across", (p, obj) -> p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.sendFilteredMessage("You walk carefully across the slippery log...");
			p.getAppearance().setCustomRenders(Renders.AGILITY_BALANCE);
			p.stepAbs(2474, 3429, StepType.WALK);
			e.delay(4);
			e.waitForMovement(p);
			p.getAppearance().removeCustomRenders();
			p.sendFilteredMessage("...You make it safely to the other side.");
			p.getStats().addXp(StatType.Agility, 7.5, true);

			p.lastAgilityObjId = obj.getId();
			p.unlock();
		}));
		Tile.get(2474, 3430, 0).flagUnmovable();
		/**
		 * Obstacle net
		 */
		ObjectAction.register(23134, "climb-over", (p, obj) -> p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.sendFilteredMessage("You climb the netting...");
			p.animate(828);
			e.delay(1);
			p.getMovement().teleport(p.getAbsX(), 3424, 1);
			p.getStats().addXp(StatType.Agility, 7.5, true);

			if (p.lastAgilityObjId == 23145)
				p.lastAgilityObjId = obj.getId();
			p.unlock();
		}));
		/**
		 * Tree branch
		 */
		ObjectAction.register(23559, "climb", (p, obj) -> p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.sendFilteredMessage("You climb the tree...");
			p.animate(828);
			e.delay(1);
			p.sendFilteredMessage("...To the platform above.");
			p.getMovement().teleport(2473, 3420, 2);
			p.getStats().addXp(StatType.Agility, 5.0, true);

			if (p.lastAgilityObjId == 23134)
				p.lastAgilityObjId = obj.getId();
			p.unlock();
		}));
		/**
		 * Balancing rope
		 */
		ObjectAction.register(23557, "walk-on", (p, obj) -> p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.sendFilteredMessage("You carefully cross the tightrope.");
			p.getAppearance().setCustomRenders(Renders.AGILITY_BALANCE);
			p.stepAbs(2483, 3420, StepType.WALK);
			e.delay(4);
			e.waitForMovement(p);
			p.getAppearance().removeCustomRenders();
			p.getStats().addXp(StatType.Agility, 7.5, true);

			if (p.lastAgilityObjId == 23559)
				p.lastAgilityObjId = obj.getId();
			p.unlock();
		}));
		/**
		 * Tree branch
		 */
		ObjectAction.register(23560, "climb-down", (p, obj) -> p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.sendFilteredMessage("You climb down the tree...");
			p.animate(828);
			e.delay(1);
			p.sendFilteredMessage("You land on the ground.");
			p.getMovement().teleport(2485, 3419, 0);
			p.getStats().addXp(StatType.Agility, 5.0, true);

			if (p.lastAgilityObjId == 23557)
				p.lastAgilityObjId = obj.getId();
			p.unlock();
		}));
		/**
		 * Obstacle net
		 */
		ObjectAction.register(23135, "climb-over", (p, obj) -> {
			if (p.getAbsY() != 3425) {
				p.sendMessage("You can not do that from here.");
				return;
			}
			p.startEvent(e -> {
				p.lock(LockType.FULL_DELAY_DAMAGE);
				p.sendMessage("You climb the netting...");
				p.animate(828);
				e.delay(2);
				p.getMovement().teleport(p.getAbsX(), 3428, 0);
				p.getStats().addXp(StatType.Agility, 4.0, true);

				if (p.lastAgilityObjId == 23560)
					p.lastAgilityObjId = obj.getId();
				p.unlock();
			});
		});
		/**
		 * Obstacle pipe
		 */
		ObjectAction pipeAction = (p, obj) -> {
			if (obj.y >= 3435) {
				p.sendMessage("You can't enter the pipe from this side.");
				return;
			}
			p.startEvent(e -> {
				p.lock(LockType.FULL_DELAY_DAMAGE);
				p.animate(749, 30);
				p.getMovement().force(0, 3, 0, 0, 33, 126, Direction.NORTH);
				e.delay(3);
				p.getMovement().teleport(obj.x, obj.y + 2, 0);
				e.delay(3);
				p.stepAbs(p.getAbsX(), p.getAbsY() + 1, StepType.WALK);
				e.delay(1);
				p.animate(749, 30);
				p.getMovement().force(0, 3, 0, 0, 33, 126, Direction.NORTH);
				e.delay(3);
				p.getMovement().teleport(obj.x, obj.y + 6, 0);
				if (p.lastAgilityObjId == 23135) {
					p.getStats().addXp(StatType.Agility, 46.5, true);
					PlayerCounter.GNOME_STRONGHOLD_COURSE.increment(p, 1);
					PlayerCounter.TOTAL.increment(p, 1);
					RooftopLapCounter.notify(p, "Gnome course", PlayerCounter.GNOME_STRONGHOLD_COURSE.get(p));
					DailyTasks.handleTaskDecrement(p, "gnomeCourse");
					AgilityPet.rollForPet(p, 1000);
					PerkTaskHandler.handleCompleteActivity(p, 1);
				} else {
					p.getStats().addXp(StatType.Agility, 7.5, true);

				}
				p.lastAgilityObjId = -1;
				p.unlock();
			});
		};
		ObjectAction.register(23138, "squeeze-through", pipeAction);
		Tile.getObject(23138, 2484, 3431, 0).walkTo = new Position(2484, 3430, 0);

		ObjectAction.register(23139, "squeeze-through", pipeAction);
		Tile.getObject(23139, 2487, 3431, 0).walkTo = new Position(2487, 3430, 0);
	}

}

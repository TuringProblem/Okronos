package io.ruin.model.skills.agility.courses;

import io.ruin.api.utils.Random;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.Renders;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;

public class Prifddinas {


	public static void register() {
		ObjectAction.register(36221, 1, (player, obj) -> player.startEvent(event -> {
			if (!player.getStats().check(StatType.Agility, 75, "attempt this"))
				return;
			player.lock(LockType.FULL_DELAY_DAMAGE);
			event.delay(1);
			player.animate(828);
			event.delay(2);
			player.getMovement().teleport(3255, 6109, 2);
			player.unlock();
			player.getStats().addXp(StatType.Agility, 11.5, true);
			player.getInventory().add(23962, Random.get(1, 4));
		}));

		ObjectAction.register(36225, 1, (player, obj) -> player.startEvent(event -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.getAppearance().setCustomRenders(Renders.AGILITY_BALANCE);
			player.privateSound(2495, 6, 0);
			player.stepAbs(3272, 6105, StepType.WALK);
			event.delay(1);
			event.waitForMovement(player);
			player.getAppearance().removeCustomRenders();
			event.delay(1);
			player.getStats().addXp(StatType.Agility, 20, true);
			player.getInventory().add(23962, Random.get(1, 4));
			player.stepAbs(3273, 6107, StepType.WALK);
			player.unlock();
		}));

		ObjectAction.register(36227, 1, (player, obj) -> player.startEvent(event -> {
			player.getMovement().teleport(3269, 6112, 2);
			player.getStats().addXp(StatType.Agility, 28.1, true);
			player.getInventory().add(23962, Random.get(1, 4));
			player.unlock();
		}));

		ObjectAction.register(36228, 1, (player, obj) -> player.startEvent(event -> {
			player.getMovement().teleport(3269, 6116, 0);
			player.getStats().addXp(StatType.Agility, 23, true);
			player.getInventory().add(23962, Random.get(1, 4));
			player.unlock();
		}));

		ObjectAction.register(36229, 1, (player, obj) -> player.startEvent(event -> {
			player.getPacketSender().fadeOut();
			event.delay(2);
			player.getMovement().teleport(3293, 6141, 0);
			event.delay(1);
			player.getPacketSender().fadeIn();
			player.getStats().addXp(StatType.Agility, 11.5, true);
			player.getInventory().add(23962, Random.get(1, 4));
			player.unlock();
		}));

		ObjectAction.register(36231, 1, (player, obj) -> player.startEvent(event -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			event.delay(1);
			player.animate(828);
			event.delay(2);
			player.getMovement().teleport(3293, 6145, 2);
			player.unlock();
		}));

		ObjectAction.register(36233, 1, (player, obj) -> player.startEvent(event -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.getAppearance().setCustomRenders(Renders.AGILITY_BALANCE);
			player.privateSound(2495, 6, 0);
			player.stepAbs(3281, 6142, StepType.WALK);
			event.delay(1);
			event.waitForMovement(player);
			player.getAppearance().removeCustomRenders();
			event.delay(1);
			player.getStats().addXp(StatType.Agility, 25.6, true);
			player.getInventory().add(23962, Random.get(1, 4));
			player.stepAbs(3278, 6142, StepType.WALK);
			player.unlock();
		}));

		ObjectAction.register(36234, 1, (player, obj) -> player.startEvent(event -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.getAppearance().setCustomRenders(Renders.AGILITY_BALANCE);
			player.privateSound(2495, 6, 0);
			player.stepAbs(3271, 6149, StepType.WALK);
			event.delay(1);
			event.waitForMovement(player);
			player.getAppearance().removeCustomRenders();
			event.delay(1);
			player.getStats().addXp(StatType.Agility, 30.7, true);
			player.getInventory().add(23962, Random.get(1, 4));
			player.unlock();
		}));

		ObjectAction.register(36235, 1, (player, obj) -> player.startEvent(event -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.getAppearance().setCustomRenders(Renders.AGILITY_BALANCE);
			player.privateSound(2495, 6, 0);
			player.stepAbs(3270, 6158, StepType.WALK);
			event.delay(1);
			event.waitForMovement(player);
			player.getAppearance().removeCustomRenders();
			event.delay(1);
			player.getStats().addXp(StatType.Agility, 25.6, true);
			player.getInventory().add(23962, Random.get(1, 4));
			player.stepAbs(3267, 6161, StepType.WALK);
			player.unlock();
		}));

		ObjectAction.register(36236, 1, (player, obj) -> player.startEvent(event -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.getAppearance().setCustomRenders(Renders.AGILITY_BALANCE);
			player.animate(2462);
			player.privateSound(2495, 6, 0);
			player.stepAbs(3274, 6168, StepType.WALK);
			player.resetAnimation();
			event.delay(1);
			event.waitForMovement(player);
			player.getAppearance().removeCustomRenders();
			event.delay(1);
			player.getStats().addXp(StatType.Agility, 30.7, true);
			player.getInventory().add(23962, Random.get(1, 4));
			player.stepAbs(3277, 6170, StepType.WALK);
			player.unlock();
		}));

		ObjectAction.register(36237, 1, (player, obj) -> player.startEvent(event -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.getAppearance().setCustomRenders(Renders.AGILITY_BALANCE);
			player.animate(2462);
			player.privateSound(2495, 6, 0);
			player.stepAbs(3284, 6177, StepType.WALK);
			player.resetAnimation();
			event.delay(1);
			event.waitForMovement(player);
			player.getAppearance().removeCustomRenders();
			event.delay(1);
			player.getStats().addXp(StatType.Agility, 30.7, true);
			player.getInventory().add(23962, Random.get(1, 4));
			player.getMovement().teleport(3284, 6177, 0);
			player.unlock();
		}));

		ObjectAction.register(36238, 1, (player, obj) -> player.startEvent(event -> {
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.getPacketSender().fadeOut();
			event.delay(2);
			player.getMovement().teleport(3240, 6109, 0);
			event.delay(1);
			player.getPacketSender().fadeIn();
			player.unlock();
			player.resetAnimation();
			player.getStats().addXp(StatType.Agility, 1037.0, true);
			player.getInventory().add(23962, Random.get(200, 600));
			player.getMovement().restoreEnergy(Random.get(1, 2));
			PlayerCounter.PRIFDDINAS_COURSE.increment(player, 1);
			PlayerCounter.TOTAL.increment(player, 1);
			AgilityPet.rollForPet(player, 400);
		}));

	}

}


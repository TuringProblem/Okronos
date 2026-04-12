package io.ruin.model.skills.slayer;

import io.ruin.cache.ObjType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.skills.slayer.master.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Slayer {
	private static List<SlayerTask> turaelTasks = new ArrayList<>();
	private static List<SlayerTask> krystiliaTasks = new ArrayList<>();
	private static List<SlayerTask> mazchaTasks = new ArrayList<>();
	private static List<SlayerTask> vanakaTasks = new ArrayList<>();
	private static List<SlayerTask> chaeldarTasks = new ArrayList<>();
	private static List<SlayerTask> konarTasks = new ArrayList<>();
	private static List<SlayerTask> steveTasks = new ArrayList<>();
	private static List<SlayerTask> duradelTasks = new ArrayList<>();

	public static boolean isTask(Player player1, NPC npc) {
		if (npc == null || npc.getDef() == null)
			return false;
		boolean isBossTask = player1.bossSlayerName != null && player1.bossSlayerName.equalsIgnoreCase(npc.getDef().name);
		return SlayerMaster.isTask(player1, npc) || isBossTask;
	}

	public static boolean hasSlayerHelmEquipped(Player player) {
		ObjType def = player.getEquipment().getDef(Equipment.SLOT_HAT);
		return def != null && def.slayerHelm;
	}

	public static void loadTasks() {
		SlayerMaster.masters = new SlayerMaster[]{
			new SlayerMaster(Vannaka.VANNAKA),
			new SlayerMaster(Turael.TURAEL),
			new SlayerMaster(Mazchna.MAZCHNA),
			new SlayerMaster(Konar.KONAR),
			new SlayerMaster(Chaeldar.CHAELDAR),
			new SlayerMaster(Krystilia.KRYSTILIA),
			new SlayerMaster(Duradel.DURADEL),
			new SlayerMaster(Nieve.NIEVE)
		};
		Arrays.stream(TuraelTask.values()).forEach(task -> turaelTasks.add(new SlayerTask(
			task.getTaskId(),
			task.getTaskName(),
			task.getLocation(),
			task.getRequiredItems(),
			task.getNpcIds(),
			task.getCombatRequirement(),
			task.getSlayerRequirement(),
			task.getMinAmount(),
			task.getMaxAmount()
		)));

		Arrays.stream(KrystiliaTask.values()).forEach(task -> krystiliaTasks.add(new SlayerTask(
			task.getTaskId(),
			task.getTaskName(),
			task.getLocation(),
			task.getRequiredItems(),
			task.getNpcIds(),
			task.getCombatRequirement(),
			task.getSlayerRequirement(),
			task.getMinAmount(),
			task.getMaxAmount()
		)));

		Arrays.stream(MazchnaTask.values()).forEach(task -> mazchaTasks.add(new SlayerTask(
			task.getTaskId(),
			task.getTaskName(),
			task.getLocation(),
			task.getRequiredItems(),
			task.getNpcIds(),
			task.getCombatRequirement(),
			task.getSlayerRequirement(),
			task.getMinAmount(),
			task.getMaxAmount()
		)));

		Arrays.stream(VannakaTask.values()).forEach(task -> vanakaTasks.add(new SlayerTask(
			task.getTaskId(),
			task.getTaskName(),
			task.getLocation(),
			task.getRequiredItems(),
			task.getNpcIds(),
			task.getCombatRequirement(),
			task.getSlayerRequirement(),
			task.getMinAmount(),
			task.getMaxAmount()
		)));

		Arrays.stream(ChaeldarTask.values()).forEach(task -> chaeldarTasks.add(new SlayerTask(
			task.getTaskId(),
			task.getTaskName(),
			task.getLocation(),
			task.getRequiredItems(),
			task.getNpcIds(),
			task.getCombatRequirement(),
			task.getSlayerRequirement(),
			task.getMinAmount(),
			task.getMaxAmount()
		)));

		Arrays.stream(KonarQuoMatenaTask.values()).forEach(task -> konarTasks.add(new SlayerTask(
			task.getTaskId(),
			task.getTaskName(),
			task.getLocation(),
			task.getRequiredItems(),
			task.getNpcIds(),
			task.getCombatRequirement(),
			task.getSlayerRequirement(),
			task.getMinAmount(),
			task.getMaxAmount()
		)));

		Arrays.stream(NieveTask.values()).forEach(task -> steveTasks.add(new SlayerTask(
			task.getTaskId(),
			task.getTaskName(),
			task.getLocation(),
			task.getRequiredItems(),
			task.getNpcIds(),
			task.getCombatRequirement(),
			task.getSlayerRequirement(),
			task.getMinAmount(),
			task.getMaxAmount()
		)));

		Arrays.stream(DuradelTask.values()).forEach(task -> duradelTasks.add(new SlayerTask(
			task.getTaskId(),
			task.getTaskName(),
			task.getLocation(),
			task.getRequiredItems(),
			task.getNpcIds(),
			task.getCombatRequirement(),
			task.getSlayerRequirement(),
			task.getMinAmount(),
			task.getMaxAmount()
		)));
	}

	public static List<SlayerTask> getTasksForMaster(int id) {
		if (id == 1) {
			return turaelTasks;
		} else if (id == 2) {
			return mazchaTasks;
		} else if (id == 3) {
			return vanakaTasks;
		} else if (id == 4) {
			return chaeldarTasks;
		} else if (id == 5) {
			return steveTasks;
		} else if (id == 6) {
			return duradelTasks;
		} else if (id == 7) {
			return krystiliaTasks;
		} else {
			return konarTasks;
		}
	}

	public static void register() {
		ObjType.forEach(def -> {
			if (def.equipSlot != Equipment.SLOT_HAT)
				return;
			String name = def.name.toLowerCase();
			if (def.id == 25910)
				def.slayerHelm = true;
			if (name.contains("slayer") && name.contains("helmet"))
				def.slayerHelm = true;
		});
	}
}

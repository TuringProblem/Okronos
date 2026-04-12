package io.ruin.model.map.object.actions.impl.prifddinas.iorwerthdistrict;

import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.SpawnListener;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.map.Direction;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.slayer.Slayer;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Broadcast;

public class IorwerthDungeon {


	private static final int HUNLLEF = 9021;

	public static void register() {
		ObjectAction.register(36690, "enter", (player, obj) -> enterIorwerthDungeon(player));
		ObjectAction.register(36691, "exit", (player, obj) -> exitIorwerthDungeon(player));
		ObjectAction.register(36692, "pass", (player, obj) -> shortcutOne(player));
		ObjectAction.register(36693, "pass", (player, obj) -> shortcutOneBack(player));
		ObjectAction.register(36694, "pass", (player, obj) -> shortcutTwo(player));
		ObjectAction.register(36695, "pass", (player, obj) -> shortcutTwoBack(player));
		SpawnListener.register(new String[]{"crystalline spider", "crystalline rat", "crystalline bat", "crystalline unicorn", "crystalline dragon", "crystalline bear", "crystalline dark beast", "crystalline wolf", "crystalline scorpion"}, npc -> {
			if (npc.getPosition().getRegion().id == 12994 || npc.getPosition().getRegion().id == 12738 || npc.getPosition().getRegion().id == 12993) {
				npc.deathEndListener = (DeathListener.SimpleKiller) killer -> {
					if (Random.rollDie(250, 1)) {
						Broadcast.WORLD.sendNews(Color.RED.wrap(("<shad>") + killer.player.getName() + " Has recieved a hunllef spawn!"));
						new NPC(HUNLLEF).spawn(killer.player.getPosition().getX(), killer.player.getPosition().getY(), killer.player.getPosition().getZ(), 5).getCombat().setAllowRespawn(false);
					}
				};
				npc.attackNpcListener = (player, npc1, message) -> {
					if (!Slayer.isTask(player, npc)) {
						if (message) {
							player.dialogue(new NPCDialogue(404, "Bahaha nice try! Come find me to get a slayer task!"));
							player.sendMessage("Chaeldar wants you to find her to get a task.");
						}
						return false;

					}
					return true;
				};
			}
		});
	}


	public static void enterIorwerthDungeon(Player player) {
		player.lock();
		player.getPacketSender().fadeOut();
		player.dialogue(
			new MessageDialogue("Welcome to the Iorwerth Dungeon.")
		);
		player.getMovement().teleport(3225, 12445, 0);
		player.getPacketSender().fadeIn();
		player.unlock();

	}

	public static void exitIorwerthDungeon(Player player) {
		player.lock();
		player.getPacketSender().fadeOut();
		player.dialogue(
			new MessageDialogue("Welcome to the Iorwerth District.")
		);
		player.getMovement().teleport(3225, 6046, 0);
		player.getPacketSender().fadeIn();
		player.unlock();
	}

	public static void shortcutOne(Player player) {
		player.startEvent(event -> {
			if (!player.getStats().check(StatType.Agility, 78, "use this shortcut"))
				return;
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.animate(749, 30);
			player.getMovement().force(-3, 0, 0, 0, 33, 126, Direction.WEST);
			event.delay(3);
			player.getMovement().force(-3, 0, 0, 0, 33, 126, Direction.WEST);
			event.delay(1);
			player.animate(749);
			event.delay(2);
			player.unlock();
		});
	}

	public static void shortcutOneBack(Player player) {
		player.startEvent(event -> {
			if (!player.getStats().check(StatType.Agility, 78, "use this shortcut"))
				return;
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.animate(749, 30);
			player.getMovement().force(3, 0, 0, 0, 33, 126, Direction.WEST);
			event.delay(3);
			player.getMovement().force(3, 0, 0, 0, 33, 126, Direction.WEST);
			event.delay(1);
			player.animate(749);
			event.delay(2);
			player.unlock();
		});
	}

	public static void shortcutTwo(Player player) {
		player.startEvent(event -> {
			if (!player.getStats().check(StatType.Agility, 84, "use this shortcut"))
				return;
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.animate(749, 30);
			player.getMovement().force(-3, 0, 0, 0, 33, 126, Direction.WEST);
			event.delay(3);
			player.getMovement().force(-3, 0, 0, 0, 33, 126, Direction.WEST);
			event.delay(1);
			player.animate(749);
			event.delay(2);
			player.unlock();
		});
	}

	public static void shortcutTwoBack(Player player) {
		player.startEvent(event -> {
			if (!player.getStats().check(StatType.Agility, 84, "use this shortcut"))
				return;
			player.lock(LockType.FULL_DELAY_DAMAGE);
			player.animate(749, 30);
			player.getMovement().force(3, 0, 0, 0, 33, 126, Direction.WEST);
			event.delay(3);
			player.getMovement().force(3, 0, 0, 0, 33, 126, Direction.WEST);
			event.delay(1);
			player.animate(749);
			event.delay(2);
			player.unlock();
		});
	}

}

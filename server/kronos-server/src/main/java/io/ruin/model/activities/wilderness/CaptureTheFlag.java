package io.ruin.model.activities.wilderness;

import io.ruin.Server;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Broadcast;

public class CaptureTheFlag {

	private static boolean DISABLED = true;

	private static CaptureTheFlag ACTIVE;

	private static long spawnTicks = 0;

	private static int FLAG = 50005;

	private static GameObject flag;


	private static final CaptureTheFlag[] SPAWNS = {
		new CaptureTheFlag(new Position(3244, 3791, 0)),
		new CaptureTheFlag(new Position(3125, 3984, 0)),
		new CaptureTheFlag(new Position(2996, 3867, 0)),
		new CaptureTheFlag(new Position(3017, 3808, 0)),
		new CaptureTheFlag(new Position(3255, 3867, 0)),
		new CaptureTheFlag(new Position(3307, 3925, 0))
	};

	private final Position flagSpawn;

	public CaptureTheFlag(Position flagSpawn) {
		this.flagSpawn = flagSpawn;
	}

	public static void register() {
		/**
		 * Event
		 */
		if (!DISABLED) {
			/* event runs every 45 minutes */
			World.startEvent(e -> {
				while (true) {
					spawnTicks = Server.getEnd(5 * 100);
					CaptureTheFlag next = Random.get(SPAWNS);
					if (next == ACTIVE) {
						e.delay(100);
						continue;
					}
					ACTIVE = next;
					String eventMessage = "Capture the flag has started!";
					Broadcast.WORLD.sendNews(Icon.WILDERNESS, "News", eventMessage);
					broadcastEvent(eventMessage);
					addFlag();
				}
			});
			ObjectAction.register(50005, "Capture", (player, obj) ->
				player.dialogue(
					new MessageDialogue("Are you sure you want to take the flag? It will redskull and teleblock you!"),
					new OptionsDialogue(
						new Option("Yes", () -> claimFlag(player)),
						new Option("No")
					)));

		}
	}

	private static void claimFlag(Player p) {
		if (p.getCombat().getLevel() < 85) {
			p.sendMessage(Color.RED.wrap("You must be combat level 85 or higher to particpate!"));
			return;
		}
		flag.setId(-1);
		p.getStats().get(StatType.Attack).drain(0.40);
		p.getStats().get(StatType.Strength).drain(0.40);
		p.getStats().get(StatType.Ranged).drain(0.40);
		p.getStats().get(StatType.Magic).drain(0.40);
		p.getStats().get(StatType.Defence).drain(0.40);
		p.getMovement().drainEnergy(100);
		p.getInventory().add(8970, 1);
		p.getCombat().teleblock();
		p.sendMessage(Color.RED.wrap("You have been skulled and Teleblocked!"));
		Broadcast.WORLD.sendNews(p.getName() + " Has captured the flag! At - " + p.wildernessLevel + " Wilderness!");
	}


	public static void addFlag() {
		flag = GameObject.spawn(FLAG, ACTIVE.flagSpawn.getX(), ACTIVE.flagSpawn.getY(), 0, 10, 0);
	}

	private static void broadcastEvent(String eventMessage) {
		for (Player p : World.players()) {
			if (p.broadcastCaptureTheFlag)
				p.getPacketSender().sendBroadcast(eventMessage);
		}
	}

}

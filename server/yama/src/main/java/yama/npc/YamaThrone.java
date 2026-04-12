package yama.npc;

import io.ruin.model.World;
import io.ruin.model.activities.ActivityTimer;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Direction;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.StatType;
import yama.combat.util.YamaMapHandler;

public class YamaThrone {
	private static void talkTo(Player player, NPC npc) {
		player.dialogue(
			new NPCDialogue(npc.getId(), "Would you like to start the fight?"),
			new OptionsDialogue("Start Yama fight?",
				new Option("Yes", () -> startFight(player, npc)),
				new Option("No."))
		);
	}

	private static final StatType[] SCALED_STATS = { StatType.Defence, StatType.Attack, StatType.Strength, StatType.Magic,
		StatType.Ranged };
	public static void scaleNPC(NPC npc) {
		if (npc.getCombat() == null) {
			return;
		}
		double factor;
		factor = 1.5;
			for (StatType type : SCALED_STATS) {
				double newLevel = npc.getCombat().getStat(type).fixedLevel * factor;
				npc.getCombat().getStat(type).fixedLevel = (int) newLevel;
				npc.getCombat().getStat(type).restore();
			}
		scaleHP(npc);
	}

	private static void scaleHP(NPC npc) {
		if (npc.getCombat() == null) {
			return;
		}
		double factor = 1.8;
			double newLevel = npc.getCombat().getStat(StatType.Hitpoints).fixedLevel * factor;
			npc.getCombat().getStat(StatType.Hitpoints).fixedLevel = (int) newLevel;
			npc.getCombat().getStat(StatType.Hitpoints).restore();
	}

	private static void startFight(Player player, NPC npc) {
		World.startEvent(e -> {
			YamaMapHandler mapHandler = (YamaMapHandler) player.currentMapHandler;
			mapHandler.setStarted(true);
			npc.graphics(3278);
			e.delay(3);
			GameObject throne = new GameObject(56265,
				npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 42, 0, 10, 0).spawn();
			if(!npc.isRemoved()) {
				npc.remove();
				e.delay(1);
				NPC yama = new NPC(14176).spawn(throne.getPosition().getRegion().baseX + 28,
					throne.getPosition().getRegion().baseY + 31, 0, Direction.NORTH, 0);
				if(yama.getPosition().getRegion().players.size() > 1)
					scaleNPC(yama);
				yama.getPosition().getRegion().players.forEach(p -> {
					p.solHereditTimer = new ActivityTimer();
					p.getHealthHud().open(true, yama.getId(), yama.getMaxHp());
				});
				player.currentMapHandler.getNpcs().add(yama);
				yama.animate(12156);
				yama.getCombat().setTarget(player);
			}
		});
	}

	public static void register() {
		NPCAction.register(123, "talk-to", YamaThrone::talkTo);
		NPCAction.register(123, "challenge", YamaThrone::startFight);

	}
}

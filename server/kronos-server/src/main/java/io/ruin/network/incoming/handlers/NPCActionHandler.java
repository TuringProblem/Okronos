package io.ruin.network.incoming.handlers;

import io.ruin.api.buffer.InBuffer;
import io.ruin.cache.NPCType;
import io.ruin.model.World;

import io.ruin.model.activities.raids.toa.bosses.zebak.Jug;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.handlers.DropViewer;
import io.ruin.model.inter.handlers.LootsTables;
import io.ruin.model.map.route.routes.TargetRoute;
import io.ruin.network.incoming.Incoming;
import io.ruin.utility.DebugMessage;
import io.ruin.utility.IdHolder;
import net.rsprot.protocol.game.incoming.npcs.OpNpc;
import net.rsprot.protocol.game.incoming.npcs.OpNpc6;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import java.util.Arrays;
import java.util.HashSet;

import static io.ruin.network.ClientProt204.*;

@IdHolder(ids = {OPNPC1, OPNPC2, OPNPC3, OPNPC4, OPNPC5, OPNPC6})
public class NPCActionHandler implements MessageConsumer<Player, OpNpc> {

	public static class Examine implements MessageConsumer<Player, OpNpc6> {
		@Override
		public void consume(Player player, OpNpc6 msg) {
			handleExamine(player, msg.getId());
		}
	}

	@Override
	public void consume(Player player, OpNpc msg) {
		handle(player, msg.getOp(), msg.getIndex(), msg.getControlKey() ? 1 : 0);
	}

	private static void handle(Player player, int option, int targetIndex, int ctrlRun) {
		if (player.isLocked())
			return;
		player.resetActions(true, true, true);

		if (option == 1) {
			handleAction(player, option, targetIndex, ctrlRun);
			return;
		}
		if (option == 2) {
			handleAction(player, option, targetIndex, ctrlRun);
			return;
		}
		if (option == 3) {
			handleAction(player, option, targetIndex, ctrlRun);
			return;
		}
		if (option == 4) {
			handleAction(player, option, targetIndex, ctrlRun);
			return;
		}
		if (option == 5) {
			handleAction(player, option, targetIndex, ctrlRun);
			return;
		}
		player.sendFilteredMessage("Unhandled npc action: option=" + option);
	}

	private static void handleExamine(Player player, int id) {
		NPCType def = NPCType.get(id);
		if (def == null)
			return;

		player.openInterface(ToplevelComponent.MAINMODAL, 855);
		DropViewer.loot.calculate(def.id, player);
		player.getPacketSender().setHidden(855, 21, true);

		if (def.name.equalsIgnoreCase("malakar")) {
			player.getLootsViewer().updateInterface(player, LootsTables.MALAKAR);
		}

		if (def.name.equalsIgnoreCase("sol heredit")) {
			player.getLootsViewer().updateInterface(player, LootsTables.SOL_HEREDIT);
		}
		if (def.name.equalsIgnoreCase("summer boss")) {
			player.getLootsViewer().updateInterface(player, LootsTables.SUMMER_BOSS);
		}
		if(def.id == 17021) {
			player.getLootsViewer().updateInterface(player, LootsTables.SUMMER_IMP);
		}
		if(def.id == 17022) {
			player.getLootsViewer().updateInterface(player, LootsTables.SUMMER_FROG);
		}

		if (def.name.equalsIgnoreCase("nightmare")) {
			player.getLootsViewer().updateInterface(player, LootsTables.NIGHTMARE);
		}

		if (def.name.equalsIgnoreCase("araxxor")) {
			player.getLootsViewer().updateInterface(player, LootsTables.ARAXXOR);
		}

		player.sendMessage(def.name);
		if (player.debug)
			debug(player, null, def, -1);
	}

	private static void handleAction(Player player, int option, int npcIndex, int ctrlRun) {
		NPC npc = World.getNpc(npcIndex);
		if (npc == null)
			return;
		NPCType def = npc.getDef();
		if (def == null)
			return;
		if (player.debug)
			debug(player, npc, def, option);
		player.face(npc);
		player.getMovement().setCtrlRun(ctrlRun == 1);
		if (option == def.attackOption) {
			player.getCombat().setTarget(npc);
			return;
		}
		if (npc.getId() == 11735) {
			if(option == 1)
				Jug.pushJug(player, npc);
			else if(option == 3)
				Jug.pullJug(player, npc);
			return;
		} else
		if (npc.skipMovementCheck) {
			player.face(npc);
			int i = option - 1;
			if (i < 0 || i >= 5)
				return;
			NPCAction action = null;
			NPCAction[] actions = npc.actions;
			if (actions != null)
				action = actions[i];
			if (action == null && (actions = def.defaultActions) != null)
				action = actions[i];
			if (action != null) {
				action.handle(player, npc);
				return;
			}
			return;
		}
		TargetRoute.set(player, npc, () -> {
			int i = option - 1;
			if (i < 0 || i >= 5)
				return;
			NPCAction action = null;
			NPCAction[] actions = npc.actions;
			if (actions != null)
				action = actions[i];
			// if(def.cryptic != null && def.cryptic.advance(player))
			// return;
			// if(def.anagram != null && def.anagram.advance(player))
			// return;
			if (action == null && (actions = def.defaultActions) != null)
				action = actions[i];
			if (action != null) {
				action.handle(player, npc);
				player.face(npc);
				return;
			}
			/* default to a dialogue */
			// player.dialogue(
			// new NPCDialogue(npc, "Beautiful day today, isn't it?")//.onDialogueOpened(()
			// -> npc.faceTemp(player)),
			// new PlayerDialogue("Uhh.. yeah I guess.")
			// );
		});
	}

	private static void debug(Player player, NPC npc, NPCType def, int option) {
		HashSet<Integer> showIds = new HashSet<>();
		if (def.showIds != null) {
			for (int id : def.showIds)
				showIds.add(id);
			showIds.remove(-1);
		}
		DebugMessage debug = new DebugMessage();
		if (option != -1)
			debug.add("option", option);
		debug.add("id", def.id + (showIds.isEmpty() ? "" : (" " + showIds.toString())));
		debug.add("name", def.name);
		if (npc != null) {
			debug.add("index", npc.getIndex());
			debug.add("x", npc.getAbsX());
			debug.add("y", npc.getAbsY());
			debug.add("z", npc.getHeight());
		}
		debug.add("options", Arrays.toString(def.options));
		debug.add("varpbitId", def.varpbitId);
		debug.add("varpId", def.varpId);
		debug.add("models", Arrays.toString(def.models));
		if (def.varpbitId != -1 || def.varpId != -1)
			debug.add("variants", Arrays.toString(def.showIds));
		player.sendFilteredMessage("[NpcAction] " + debug.toString());
	}

}

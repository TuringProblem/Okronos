package io.ruin.model.activities;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.route.routes.DumbRoute;
import io.ruin.model.map.route.routes.TargetRoute;

public class SecurityGuard {


	public void rollGuardSpawnChance(Player player, int spawnChance) {
		if (player.guardSpawnCoolDown.remaining() > 0) {
			return;
		}
		if(!canPerformAction(player))
			return;
		boolean spawnGuard = Random.get(spawnChance) == 0;
		if (spawnGuard) {
			spawnHonourGuard(player);
		}
	}

	private boolean canPerformAction(Player player) {
		if (player.wildernessLevel > 0)
			return false;
		else if (player.gauntlet != null && player.gauntlet.inGauntlet)
			return false;
		else if (player.raidsParty != null)
			return false;
		else if (player.inTob)
			return false;
		else if (player.inCox)
			return false;
		else if (player.getCurrentToARaid() != null)
			return false;
		else if (player.insideRaid)
			return false;
		else if(player.teleportListener != null && !player.teleportListener.allow(player))
			return false;
		return true;

	}

	public void spawnHonourGuard(Player player) {
		NPC guard = new NPC(1891).spawn(getSpawnTile(player));
		guard.forceText(player.getName() + " talk to me for a reward or pay the consequences!");
		guard.face(player);
		//player.getPacketSender().sendHintIcon(guard);
		followEvent(guard, player);

		guard.ownerId = player.getUserId();
		player.sendMessage("A security guard has been spawned to protect you.");
		player.guardSpawnCoolDown.delay(1000);
		World.startEvent(e -> {
			e.setCancelCondition(guard::isRemoved);
			for (int i = 0; i < 10; i++) {
				e.delay(10);
				if (player == null) {
					guard.remove();
					return;
				}
				guard.forceText(player.getName() + " talk to me for a reward or pay the consequences!");
				guard.face(player);
				if (i == 9) {
					if(canPerformAction(player)) {
						// interrupt the player by sending them home - assuming they're botting and not responding to this Anti-Bot Check
						player.getMovement().teleport(World.HOME);
						player.breakAction = true;
						player.resetActions(true, true, true);
						player.getCombat().reset();
						World.checkCannon(player);
						player.sendMessage("The security guard has teleported you to home.");
					}
					guard.remove();
				}
			}
		});
	}

	private void followEvent(NPC npc, Player player) {
		World.startEvent(e -> {
			while (player.isOnline() && !npc.isRemoved()) {
				// System.out.println("Following player");
				if (npc.getPosition().distance(player.getPosition()) > 5) {
					npc.getMovement().teleport(getSpawnTile(player));
					e.delay(1);
					continue;
				}
				if (player.getCombat().isDead() || player.getMovement().isTeleportQueued()) {
					e.delay(1);
					continue;
				}
				if (TargetRoute.inTarget(npc.getAbsX(), npc.getAbsY(), npc.getSize(), player.getAbsX(), player.getAbsY(), player.getSize())) {
					npc.getRouteFinder().routeEntity(player);
				} else {
					DumbRoute.step(npc, player, 1);
				}
				e.delay(1);
			}
		});
	}

	Position getSpawnTile(Player player) {
		Position playerPos = player.getPosition().copy();
		for (int xOffset = -1; xOffset <= 1; xOffset++) {
			for (int yOffset = -1; yOffset <= 1; yOffset++) {
				if (xOffset == 0 && yOffset == 0) continue;
				Position candidateTile = playerPos.copy().translate(xOffset, yOffset, 0);
				if (Tile.get(candidateTile, true).clipping == 0) {
					return candidateTile;
				}
			}
		}
		return playerPos;
	}


	public static void talkToSecurityGuard(Player player, NPC guard) {
		if (guard.ownerId != player.getUserId()) {
			player.sendMessage("This guard is not assigned to you.");
			return;
		}
		player.dialogue(new NPCDialogue(1891, "Ah, " + player.getName() + ", thank you for confirming your activity, take this reward as a token of my appreciation."));
		int reasonPoints = Random.get(2500, 5000);
		player.updateReasonPoints(reasonPoints);
		player.sendMessage("You have been awarded " + NumberUtils.formatNumber(reasonPoints) + " reason points.");
		player.getInventory().addOrDrop(new Item(995, Random.get(750_000, 1_500_000)));
		guard.remove();
	}

	public static void register() {
		NPCAction.register(1891, "talk-to", SecurityGuard::talkToSecurityGuard);
	}

}

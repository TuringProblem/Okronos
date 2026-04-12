package io.ruin.model.activities.clanwars;

import io.ruin.data.impl.teleports;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerAction;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.object.actions.ObjectAction;

public class FFAClanWars {

	public static final Bounds FFA_BOUNDS = new Bounds(3264, 4736, 3391, 4863, -1);
	public static final Bounds FFA_FIGHT_BOUNDS = new Bounds(3264, 4760, 3391, 4863, -1);
	private static final Bounds FFA_DEATH_BOUNDS = new Bounds(3326, 4753, 3330, 4756, 0);

	private static final int ENTRANCE_PORTAL = 26645;
	private static final int PVP_ENTRANCE_PORTAL = 28925;
	private static final int EXIT_PORTAL = 26646;

	public static void register() {
		/**
		 * Entrance/exit portals
		 */

		ObjectAction.register(ENTRANCE_PORTAL, "enter", (player, obj) -> player.getMovement().teleport(3327, 4751));
		ObjectAction.register(EXIT_PORTAL, "exit", (player, obj) -> player.getMovement().teleport(3128, 3627, 0));

		/**
		 * Enter/exit bounds
		 */
		MapListener.registerBounds(FFA_BOUNDS)
			.onEnter(player -> {
				player.openInterface(ToplevelComponent.OVERLAY, Interface.FFA_CLAN_WARS);
				//player.getPacketSender().sendVarp(20003, 0);
				player.openInterface(ToplevelComponent.WILDERNESS_OVERLAY, Interface.WILDERNESS_OVERLAY);
				player.getPacketSender().setHidden(Interface.WILDERNESS_OVERLAY, 26, true);
			})
			.onExit((player, logout) -> {
				if (!logout) {
					player.closeInterface(ToplevelComponent.OVERLAY);
					player.closeInterface(ToplevelComponent.WILDERNESS_OVERLAY);
				}
			});

		MapListener.registerBounds(FFA_FIGHT_BOUNDS)
			.onEnter(player -> {
				player.setAction(1, PlayerAction.ATTACK);
				player.attackPlayerListener = FFAClanWars::allowAttack;
				player.deathEndListener = (DeathListener.Simple) () -> {
					player.getMovement().teleport(FFA_DEATH_BOUNDS.randomPosition());
					player.sendMessage("Oh dear, you have died!");
				};
				VarPlayerRepository.IN_PVP_AREA.set(player, 0);
				player.getPacketSender().setHidden(90, 57, true);

			})
			.onExit((player, logout) -> {
				if (!logout) {
					player.setAction(1, null);
					player.getCombat().resetTb();
					player.getCombat().resetKillers();
					player.getPacketSender().setHidden(90, 57, false);
					VarPlayerRepository.IN_PVP_AREA.set(player, 0);
					player.clearHits();
					player.pvpAttackZone = false;
					player.attackPlayerListener = null;
					player.deathEndListener = null;
				}
			});
	}

	private static boolean allowAttack(Player player, Player pTarget, boolean message) {
		if (!player.getPosition().inBounds(FFA_FIGHT_BOUNDS)) {
			player.sendMessage("You can't attack players from where you're standing.");
			return false;
		}
		if (!pTarget.getPosition().inBounds(FFA_FIGHT_BOUNDS)) {
			player.sendMessage("You can't attack players who aren't who haven't stepped over the line.");
			return false;
		}
		return true;
	}

}

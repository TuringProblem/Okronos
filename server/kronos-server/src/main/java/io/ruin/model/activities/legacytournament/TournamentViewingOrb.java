package io.ruin.model.activities.legacytournament;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.actions.ObjectAction;

public class TournamentViewingOrb {

	public static void reset(Player player) {
		if (player.usingTournamentOrb)
			player.getMovement().teleport(player.viewingOrbLocation);
		player.closeInterface(ToplevelComponent.SKILLS_TAB_AREA);
		player.getAppearance().setNpcId(-1);
		player.usingTournamentOrb = false;
		player.usingTournamentOrbFromHome = false;
		player.setHidden(false);
		player.hidePet = false;
		player.unlock();
	}

	private static void move(Player player, Position orbPosition) {
		if (player.isLocked() && !player.usingTournamentOrb)
			return;
		if (player.getPosition().equals(orbPosition)) {
			player.sendFilteredMessage("You're already viewing that orb.");
			return;
		}
		if (!player.isHidden() || player.getAppearance().getNpcId() == -1) {
			player.lock();
			player.setHidden(true);
			player.hidePet = true;
			player.usingTournamentOrb = true;
			player.getAppearance().setNpcId(7177);
		}
		player.getMovement().teleport(orbPosition);
	}

	public static void register() {
		/**
		 * Viewing orb objects
		 */
//        ObjectAction.register(26741, "use", (player, obj) -> {
//            player.usingTournamentOrbFromHome = false;
//            player.viewingOrbLocation = player.getPosition().copy();
//            player.openInterface(InterfaceType.INVENTORY, Interface.VIEWING_ORB_INTERFACE);
//        });
		//Green Viewing Orb for Tourney - 26749
		ObjectAction.register(26741, "use", (player, obj) -> {

			player.usingTournamentOrbFromHome = true;
			player.viewingOrbLocation = player.getPosition().copy();
			player.openInterface(ToplevelComponent.SKILLS_TAB_AREA, Interface.VIEWING_ORB_INTERFACE);
		});

		/**
		 * Viewing orb interface handler
		 */
		InterfaceHandler.register(Interface.VIEWING_ORB_INTERFACE, h -> {
			h.actions[5] = (SimpleAction) TournamentViewingOrb::reset;
			h.actions[11] = (SimpleAction) player -> move(player, new Position(3616, 8987, 0));//center = final 3616 8987 // invisible Viewing Posts
			h.actions[12] = (SimpleAction) player -> move(player, new Position(3638, 9097, 0));//nw
			h.actions[13] = (SimpleAction) player -> move(player, new Position(3657, 9097, 0));//ne
			h.actions[14] = (SimpleAction) player -> move(player, new Position(3657, 9078, 0));//se
			h.actions[15] = (SimpleAction) player -> move(player, new Position(3638, 9078, 0));//sw
		});
	}

}

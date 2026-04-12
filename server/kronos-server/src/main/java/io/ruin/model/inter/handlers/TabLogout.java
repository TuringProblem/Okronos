package io.ruin.model.inter.handlers;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.inter.utils.Unlock;

public class TabLogout {

	public static void register() {
		InterfaceHandler.register(Interface.LOGOUT, h -> {
			h.actions[3] = (SimpleAction) WorldSwitcher::open;
			h.actions[8] = (SimpleAction) Player::attemptLogout;
		});

		WorldSwitcher.register();
	}

	private static final class WorldSwitcher {

		public static void register() {
			InterfaceHandler.register(Interface.WORLD_SWITCHER, h -> {
				h.actions[5] = (SimpleAction) p -> {
					p.closeInterface(ToplevelComponent.LOGOUT_TAB_AREA);
					p.openInterface(ToplevelComponent.LOGOUT_TAB_AREA);
				};
				h.actions[25] = (SimpleAction) Player::attemptLogout;
				h.actions[12] = (SimpleAction) p -> toggleSettings(p, 2, 3);
				h.actions[13] = (SimpleAction) p -> toggleSettings(p, 1, 0);
				h.actions[14] = (SimpleAction) p -> toggleSettings(p, 4, 5);
				h.actions[15] = (SimpleAction) p -> toggleSettings(p, 8, 9);
				h.actions[16] = (SimpleAction) p -> toggleSettings(p, 6, 7);
			});
		}

		public static void open(Player player) {
			player.openInterface(ToplevelComponent.LOGOUT_TAB_AREA, Interface.WORLD_SWITCHER);
			new Unlock(Interface.WORLD_SWITCHER, 14).children(0, 420).unlockWith(player, 6);
		}

		private static void toggleSettings(Player p, int set, int elseSet) {
			if (VarPlayerRepository.WORLD_SWITCHER_SETTINGS.get(p) == set)
				VarPlayerRepository.WORLD_SWITCHER_SETTINGS.set(p, elseSet);
			else
				VarPlayerRepository.WORLD_SWITCHER_SETTINGS.set(p, set);
		}

	}

}

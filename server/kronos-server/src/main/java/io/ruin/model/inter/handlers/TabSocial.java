package io.ruin.model.inter.handlers;

import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.var.VarPlayerRepository;

public class TabSocial {

	public static void register() {
		InterfaceHandler.register(Interface.FRIENDS_LIST, h -> h.actions[1] = (SimpleAction) p -> {
			p.openInterface(ToplevelComponent.FRIENDLIST_TAB_AREA, Interface.IGNORE_LIST);
			VarPlayerRepository.FRIENDS_AND_IGNORE_TOGGLE.set(p, 1);
		});

		InterfaceHandler.register(Interface.IGNORE_LIST, h -> h.actions[1] = (SimpleAction) p -> {
			p.openInterface(ToplevelComponent.FRIENDLIST_TAB_AREA, Interface.FRIENDS_LIST);
			VarPlayerRepository.FRIENDS_AND_IGNORE_TOGGLE.set(p, 0);
		});
	}

}

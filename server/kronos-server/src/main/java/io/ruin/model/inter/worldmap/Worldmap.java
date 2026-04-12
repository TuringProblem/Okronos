package io.ruin.model.inter.worldmap;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerDisplayMode;
import io.ruin.model.inter.ComponentID;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.ToplevelInterfaceType;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.network.incoming.handlers.DisplayHandler;
import io.ruin.utility.CS2Script;

import static io.ruin.model.inter.InterfaceEventMask.ClickOp1;
import static io.ruin.model.inter.InterfaceEventMask.getMask;

public class Worldmap {

	public static void openWorldmapFull(Player p) {
		DisplayHandler.sendTopLevel(p, ToplevelInterfaceType.FULLSCREEN_INTERFACE);
		openWorldmap(p);
	}

	public static void openWorldmap(Player p) {
		CS2Script.WORLDMAP_TRANSMITDATA.sendScript(p, p.getPosition().getTileHash(), -1);
		p.openInterface(ToplevelComponent.WORLD_MAP, Interface.WORLD_MAP);
		p.getPacketSender().sendIfEvents(595, 21, 0, 4, getMask(ClickOp1));
		//IfSetEvents(interfaceId = 595, componentId = 21, startIndex = 0, endIndex = 4, events = ClickOp1)
	}

	public static void register() {
		InterfaceHandler.register(Interface.WORLD_MAP, h -> h.actions[ComponentID.WorldMap.EXIT] = (SimpleAction) p -> {

			if (p.getToplevelType() == ToplevelInterfaceType.FULLSCREEN_INTERFACE) {
				p.getPacketSender().removeInterface(ToplevelInterfaceType.FULLSCREEN_INTERFACE, 37);//378);
				p.getPacketSender().removeInterface(ToplevelInterfaceType.FULLSCREEN_INTERFACE, 38);//378);
				PlayerDisplayMode.refresh(p);
			} else {
				p.closeInterface(ToplevelComponent.WORLD_MAP);
			}
		});
	}
}

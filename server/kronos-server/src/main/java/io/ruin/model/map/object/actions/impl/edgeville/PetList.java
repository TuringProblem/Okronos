package io.ruin.model.map.object.actions.impl.edgeville;

import io.ruin.api.utils.Random;
import io.ruin.cache.EnumType;
import io.ruin.cache.ObjType;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.map.object.actions.ObjectAction;

public class PetList {

	public static void register() {
		EnumType menageriePets = EnumType.get(985);
		StringBuilder sb = new StringBuilder();
		for (int key : menageriePets.getIntValues().keySet()) {
			int itemId = menageriePets.getIntValues().get(key);
			ObjType def = ObjType.get(itemId);
			if (def.pet == null) {
				System.err.println(def.name + " (" + itemId + ") pet not supported!");
				continue;
			}
			sb.append(def.name).append("|");
		}
		String s = sb.toString();
		ObjectAction.register(29226, "read", (player, obj) -> {
			player.openInterface(ToplevelComponent.MAINMODAL, 210);
			player.getPacketSender().sendClientScript(647, "s", s);
		});
	}

}

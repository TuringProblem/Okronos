package io.ruin.model.inter.handlers;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;

public class TabJournal {

	public static void register() {
		/**
		 * This is where the current quest tab is called from
		 */

           /* InterfaceHandler.register(Interface.SETTINGS, h -> {
                h.widgetActions.put(76, (SlotAction) (p, slot) -> p.settings.select(p, slot));//Whats this?
                for(int i = 8; i <= 39; i++) {
                    int finalI = i;
                    h.actions[i] = (SimpleAction) p -> p.settings.select(p, finalI - 7);
                }

            });

*/

//            InterfaceHandler.register(Interface.SERVER_TAB, h -> {
//                h.actions[46] = (SimpleAction) QuestTab.MAIN::send;
//                h.actions[51] = (SimpleAction) QuestTab.INFO::send;
//                h.actions[56] = (SimpleAction) QuestTab.BESTIARY::send;
//                h.actions[61] = (SimpleAction) QuestTab.STATISTICS::send;
////                h.actions[66] = (SimpleAction) QuestTab.LEAGUES::send;
//                h.widgetActions.put(76, (SlotAction) (p, slot) -> p.journal.select(p, slot));//Whats this?
//                for(int i = 6; i <= 44; i++) {
//                    int finalI = i;
//                    h.actions[i] = (SimpleAction) p -> p.journal.select(p, finalI - 5);
//                }
//
//            });
	}

	public static void swap(Player player, int interfaceId) {
		player.getPacketSender().sendInterface(interfaceId, ToplevelComponent.QUEST_TAB_AREA);

//        if(player.isFixedScreen())
//            player.getPacketSender().sendInterface(interfaceId, 548, 68, ClientInterfaceType.OVERLAY);
//        else if(player.getGameFrameId() == ToplevelInterfaceType.RESIZABLE_TABS)
//            player.getPacketSender().sendInterface(interfaceId, 164, 70, ClientInterfaceType.OVERLAY);
//        else
//            player.getPacketSender().sendInterface(interfaceId, 161, 70, ClientInterfaceType.OVERLAY);
	}

	public static void restore(Player player) {
//        swap(player, Interface.NOTICEBOARD);
		//TabQuest.send(player);
//        QuestTab.MAIN.send(player);
		//player.journal.send(player);
	}

}

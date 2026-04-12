package io.ruin.model.inter.handlers;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.OptionAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.questtab.QuestTab;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.inter.worldmap.Worldmap;

public class MainFrame {

	public static void register() {
		InterfaceHandler.register(Interface.ORBS, h -> {
			h.actions[5] = (OptionAction) XpCounter::select;
			h.actions[19] = (OptionAction) (p, option) -> {
				if (option == 1) {
					p.getPrayer().toggleQuickPrayers();
				} else {
					TabPrayer.setupQuickPrayers(p, true);
				}
			};
			h.actions[27] = (SimpleAction) p -> p.getMovement().toggleRunning();
			h.actions[45] = (SimpleAction) p -> p.openUrl("https://reasonps.com/store");
			h.actions[53] = new InterfaceAction() {
				@Override
				public void handleClick(Player player, int option, int slot, int itemId) {
					if (option == 3) {
						Worldmap.openWorldmapFull(player);
					} else {
						Worldmap.openWorldmap(player);

					}
				}
			};
			h.actions[35] = (SimpleAction) p -> p.getCombat().toggleSpecial();// 45 is wiki
		});
		InterfaceHandler.register(Interface.CHAT_BAR, h -> {
			h.actions[7] = (OptionAction) (player, option) -> {
				if (option == 2) {
					VarPlayerRepository.GAME_FILTER.toggle(player);
				} else if (option == 3) {
					player.dialogue(
							new MessageDialogue("Would you like to filter yells from non-staff members?"),
							new OptionsDialogue(
									new Option("Yes", () -> {
										player.yellFilter = true;
										player.sendMessage(
												"Yells from non-staff members will now be hidden when your game chat is set to filtered.");
									}),
									new Option("No", () -> {
										player.yellFilter = false;
										player.sendMessage(
												"Yells from non-staff members will now show even when your game chat is set to filtered.");
									})));
				}
			};
		});
		InterfaceHandler.register(Interface.FIXED_SCREEN, actions -> {
			actions.actions[69] = (DefaultAction) (player, option, slot, itemId) -> {
				if (option == 2)
					VarPlayerRepository.DISABLE_SPELL_FILTERING.toggle(player);
			};
			InterfaceHandler.register(Interface.FIXED_SCREEN, player -> {
				actions.actions[65] = (SimpleAction) QuestTab.MAIN::send;
			});
		});
		InterfaceHandler.register(Interface.RESIZED_SCREEN, actions -> {
			actions.actions[65] = (DefaultAction) (player, option, slot, itemId) -> {
				if (option == 2)
					VarPlayerRepository.DISABLE_SPELL_FILTERING.toggle(player);
			};
			InterfaceHandler.register(Interface.RESIZED_SCREEN, player -> {
				actions.actions[61] = (SimpleAction) QuestTab.MAIN::send;
			});
		});
		InterfaceHandler.register(Interface.RESIZED_STACKED_SCREEN, actions -> {
			InterfaceHandler.register(Interface.RESIZED_STACKED_SCREEN, player -> {
				actions.actions[52] = (SimpleAction) QuestTab.MAIN::send;
			});
		});
		InterfaceHandler.register(Interface.RESIZED_STACKED_SCREEN, actions -> {
			actions.actions[57] = (DefaultAction) (player, option, slot, itemId) -> {
				if (option == 2)
					VarPlayerRepository.DISABLE_SPELL_FILTERING.toggle(player);
			};
		});
	}

}

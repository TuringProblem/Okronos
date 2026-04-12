package io.ruin.model;

import io.ruin.model.activities.newshop.NewShopHandler;
import io.ruin.model.activities.newshop.shops.AchievementPointStore;
import io.ruin.model.entity.npc.actions.edgeville.VoteGambler;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.item.actions.ItemAction;

public class VoteRaffleTicket {

	public static void register() {
		ItemAction.registerInventory(30602, "Enter-raffle", VoteGambler::handleEntry);

	}
}

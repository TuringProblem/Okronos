package io.ruin.model.entity.npc.actions.edgeville;

import static io.ruin.cache.ItemID.COINS_995;
import static io.ruin.content.share.Plank.*;

import io.ruin.content.share.Plank;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.SpawnListener;

import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.ActionDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.shop.Currency;
import io.ruin.model.shop.Shop;
import io.ruin.model.map.Position;
import io.ruin.model.shop.ShopManager;

public class SawmillOperator {

	private static final int SAWMILL_OPERATOR_NPC_ID = 3101;

	private static void convertPlank(Player player, Plank plank, int amount) {
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.startEvent(event -> {
			int amt = amount;
			while (amt-- > 0) {
				Item coins = player.getInventory().findItem(COINS_995);
				if (coins == null || coins.getAmount() < plank.cost) {
					player.dialogue(new NPCDialogue(3101, "You'll need to bring me some more coins."));
					return;
				}

				if (!player.getInventory().contains(plank.woodId)) {
					player.dialogue(
						new NPCDialogue(
							SAWMILL_OPERATOR_NPC_ID, "You'll need to bring me some more logs."));
					return;
				}

				player.getInventory().remove(COINS_995, plank.cost);
				player.getInventory().remove(plank.woodId, 1);
				player.getInventory().add(plank.plankId, 1);
			}
		});
	}

	private static void openInterface(Player player) {
		SkillItem[] items = new SkillItem[4];

		items[0] =
			new SkillItem(WOOD.woodId)
				.addAction(
					((player1, amount, event) -> {
						convertPlank(player1, WOOD, amount);
					}));

		items[1] =
			new SkillItem(OAK.woodId)
				.addAction(
					((player1, amount, event) -> {
						convertPlank(player1, OAK, amount);
					}));

		items[2] =
			new SkillItem(TEAK.woodId)
				.addAction(
					((player1, amount, event) -> {
						convertPlank(player1, TEAK, amount);
					}));

		items[3] =
			new SkillItem(MAHOGANY.woodId)
				.addAction(
					((player1, amount, event) -> {
						convertPlank(player1, MAHOGANY, amount);
					}));

		SkillDialogue.make(player, items);
	}

	public static void register() {
		NPCAction.register(
			SAWMILL_OPERATOR_NPC_ID,
			1,
			(player, npc) ->
				player.dialogue(
					new NPCDialogue(
						npc,
						"Do you want me to make some planks for you? Or would you be interested in some other housing supplies?"),
					new OptionsDialogue(
						new Option(
							"Planks please!",
							() ->
								player.dialogue(
									new PlayerDialogue("Planks please!"),
									new NPCDialogue(npc, "What kind of planks do you want?"),
									new ActionDialogue(() -> openInterface(player)))),
						new Option(
							"What kind of planks can you make?",
							() ->
								player.dialogue(
									new PlayerDialogue("What kind of planks can you make?"),
									new NPCDialogue(
										npc,
										"I can make planks from wood, oak, teak and mahogany. I don't make planks from other woods "
											+ "as they're no good for making furniture."),
									new NPCDialogue(
										npc,
										"Wood and oak are all over the place, but teak and mahogany can only be found in a few places "
											+ "like Karamja and Etceteria."))),
						new Option("Can I buy some housing supplies?", () -> ShopManager.openIfExists(player, "ReasonConstruction")),
						new Option(
							"Nothing, thanks.",
							() ->
								player.dialogue(
									new PlayerDialogue("Nothing, thanks."),
									new NPCDialogue(
										npc,
										"Well come back when you want some. You can't get good quality planks anywhere but here!"))))));
		NPCAction.register(
			SAWMILL_OPERATOR_NPC_ID,
			"buy-plank",
			(player, npc) -> {
				openInterface(player);
			});
		NPCAction.register(
			SAWMILL_OPERATOR_NPC_ID,
			"trade",
			(player, npc) -> {
				ShopManager.openIfExists(player, "ReasonConstruction");
			});
		NPCAction.register(
			1501,
			"trade",
			(player, npc) -> {
				ShopManager.openIfExists(player, "ReasonHunter");
			});
		NPCAction.register(
			1501,
			1,
			(player, npc) -> {
				ShopManager.openIfExists(player, "ReasonHunter");
			});

		SpawnListener.register(
			SAWMILL_OPERATOR_NPC_ID,
			(npc -> {
				if (npc.spawnPosition.equals(1623, 3500, 0)) {
					npc.walkTo = new Position(1624, 3500, 0);
				}
			}));
	}
}

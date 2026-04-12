package io.ruin.model.activities.nightmarezone;

import io.ruin.api.utils.NumberUtils;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.var.VarPlayerRepository;

/**
 * A class that handles Nightmare Zone pregame functionalities.
 */
public final class NightmareZonePregame {

	public static boolean enabled = true;

	private static final Bounds ENCLOSURE = new Bounds(2601, 3113, 2606, 3118, 0);

	private static void prepareDream(Player player, NightmareZoneDreamDifficulty difficulty) {

		Tile.getObject(26291, 2605, 3117, 0, 10, 0).setId(26269);
	}

	private static void depositCoins(Player player, int numCoins) {
		if (numCoins > player.getInventory().getAmount(995)) {
			player.dialogue(new MessageDialogue("There are not enough coins in your inventory to deposit"));
			return;
		}

		player.getInventory().remove(995, numCoins);
		player.nmzCofferCoins += numCoins;
	}

	private static void withdrawCoins(Player player, int numCoins) {
		if (numCoins > player.nmzCofferCoins) {
			player.sendMessage("You don't have enough coins in your coffer to remove that amount!");
		} else if (numCoins <= player.nmzCofferCoins) {
			player.getInventory().addOrDrop(995, numCoins);
			player.nmzCofferCoins -= numCoins;
			player.sendMessage("You removed " + numCoins + "coins from your the coffer!");
		}
	}

	private static OptionsDialogue depositWithdraw(Player player) {
		return new OptionsDialogue("Dominic's Coffer (" + NumberUtils.formatNumber(player.nmzCofferCoins) + " coins)",
				new Option("Deposit Money.", () -> {
					player.integerInput(
							"How much do you wish to deposit? (" + NumberUtils.formatNumber(player.nmzCofferCoins) + ")", (p) -> {
								depositCoins(player, p);
							});
				}),
				new Option("Withdraw Money.", () -> {
					player.integerInput(
							"How much do you wish to deposit? (" + NumberUtils.formatNumber(player.nmzCofferCoins) + ")", (p) -> {
								withdrawCoins(player, p);
							});
				}),
				new Option("Cancel."));
	}

	private static OptionsDialogue depositOnly(Player player) {
		return new OptionsDialogue("Dominic's Coffer (" + NumberUtils.formatNumber(player.nmzCofferCoins) + " coins)",
				new Option("Deposit Money.", () -> {
					player.integerInput(
							"How much do you wish to deposit? (" + NumberUtils.formatNumber(player.nmzCofferCoins) + ")", (p) -> {
								depositCoins(player, p);
							});
				}),
				new Option("Cancel."));
	}

	private static OptionsDialogue withdrawOnly(Player player) {
		return new OptionsDialogue("Dominic's Coffer (" + NumberUtils.formatNumber(player.nmzCofferCoins) + " coins)",
				new Option("Withdraw Money.", () -> {
					player.integerInput(
							"How much do you wish to deposit? (" + NumberUtils.formatNumber(player.nmzCofferCoins) + ")", (p) -> {
								withdrawCoins(player, p);
							});
				}),
				new Option("Cancel."));
	}

	public static void register() {
		NPCAction.register(1120, "talk-to", (player, npc) -> {
			if (!enabled) {
				player.dialogue(new MessageDialogue("Nightmare Zone is currently under maintenance"));
				return;
			}
			player.dialogue(
					new NPCDialogue(1120, "Welcome to The Nightmare Zone! Would you like me to create a dream for you?"),
					new OptionsDialogue("Select a dream difficulty.",
							new Option("Normal", () -> {
								NightmareZoneDream.enter(player, NightmareZoneDreamDifficulty.NORMAL);
							}),
							new Option("Hard", () -> {
								NightmareZoneDream.enter(player, NightmareZoneDreamDifficulty.HARD);
							})));
		});

		NPCAction.register(1120, "dream", (player, npc) -> {
			if (!enabled) {
				player.dialogue(new MessageDialogue("Nightmare Zone is currently under maintenance"));
				return;
			}
			player.dialogue(new OptionsDialogue("Select a dream difficulty.",
					new Option("Normal", () -> {
						NightmareZoneDream.enter(player, NightmareZoneDreamDifficulty.NORMAL);
					}),
					new Option("Hard", () -> {
						NightmareZoneDream.enter(player, NightmareZoneDreamDifficulty.HARD);
					})));
		});

		/* Coffer */
		ObjectAction.register(26292, 1, (player, obj) -> {
			if (player.nmzCofferCoins > 0) {
				player.dialogue(
						new ItemDialogue().one(995,
								"You have " + NumberUtils.formatNumber(player.nmzCofferCoins) + " coins stored in the coffer.<br>"
										+ (player.getInventory().contains(995, 1_000) ? ""
												: "You need at least 1,000 coins to make a deposit")),
						player.getInventory().contains(995, 1_000) ? depositWithdraw(player) : withdrawOnly(player));
			} else {
				if (player.getInventory().contains(995, 1_000)) {
					player.dialogue(
							new ItemDialogue().one(995, "The coffer is empty.<br>"),
							depositOnly(player));

				} else {
					player.dialogue(
							new ItemDialogue().one(995, "The coffer is empty.<br>"
									+ (player.getInventory().contains(995, 1_000) ? ""
											: "You need at least 1,000 coins to make a deposit")));
				}
			}

		});

		/* Filled vial */
		ObjectAction.register(26269, "drink", (player, obj) -> {

		});

		/* Lobby enclosure map listener */
		MapListener.registerBounds(ENCLOSURE).onEnter(player -> {
			VarPlayerRepository.NMZ_COFFER_STATUS.set(player, 1); // 1 means unlocked coffer
			VarPlayerRepository.NMZ_COFFER_AMT.set(player, player.nmzCofferCoins / 1000);
			player.openInterface(ToplevelComponent.WILDERNESS_OVERLAY, 207);
			player.getPacketSender().sendClientScript(264, "i", 0);
		}).onExit((player, logout) -> {
			player.closeInterface(ToplevelComponent.WILDERNESS_OVERLAY);
		});
	}

}

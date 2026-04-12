package io.ruin.model.activities.duelarena;

import io.ruin.Server;
import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerAction;
import io.ruin.model.inter.AccessMasks;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.ItemContainerG;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatList;
import io.ruin.model.stat.StatType;
import io.ruin.services.Loggers;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;

import static io.ruin.cache.ItemID.BLOOD_MONEY;

@Slf4j
public class Duel extends ItemContainer {

	public static ArrayList<Player> players = new ArrayList<>(500);

	private static final Bounds[][] REGULAR_SPAWNS = {
		{new Bounds(new Position(3340, 3251, 0), 4), new Bounds(new Position(3350, 3251, 0), 4)},
		{new Bounds(new Position(3371, 3232, 0), 4), new Bounds(new Position(3381, 3232, 0), 4)},
		{new Bounds(new Position(3340, 3213, 0), 4), new Bounds(new Position(3350, 3213, 0), 4)},
	};

	private static final Bounds[][] OBSTACLE_SPAWNS = {
		{new Bounds(new Position(3366, 3251, 0), 1), new Bounds(new Position(3386, 3251, 0), 1)},
		{new Bounds(new Position(3335, 3232, 0), 1), new Bounds(new Position(3355, 3232, 0), 1)},
		{new Bounds(new Position(3366, 3213, 0), 1), new Bounds(new Position(3387, 3213, 0), 1)},
	};

	private static final Bounds END_BOUNDS = new Bounds(3362, 3265, 3372, 3268, 0);
	private static final Bounds CUSTOM_END_BOUNDS = new Bounds(3084, 3470, 3086, 3473, 0);

	static LinkedList<String> FIGHT_HISTORY = new LinkedList<>();

	/**
	 * Buttons
	 */

	private static final int[] RULES = {
		37, 38, 39, 40, 41, 42, 43, 44, 45,
		46, 47, 48, 49, 56, 57, 58, 59, 60,
		61, 62, 63, 64, 65, 66
	};

	public static void register() {
		try {
			InterfaceHandler.register(Interface.DUEL_ARENA_RULES, h -> {
				h.actions[92] = (SimpleAction) p -> {
					if (p.acceptDelay.isDelayed())
						return;
					p.getDuel().accept(true, false);
				};
				h.actions[93] = (SimpleAction) p -> p.getDuel().close();
				for (int rule : RULES) {
					DuelRule duelRule = DuelRule.get(rule);
					h.actions[rule] = (SimpleAction) p -> p.getDuel().toggle(duelRule);
				}
				h.actions[97] = (SimpleAction) p -> p.getDuel().saveSettings();
				h.actions[100] = (SimpleAction) p -> p.getDuel().loadSettings(false);
				h.actions[98] = (SimpleAction) p -> p.getDuel().loadSettings(true);

			});

			InterfaceHandler.register(Interface.DUEL_ARENA_STAGE_TWO, h -> {
				h.actions[20] = (DefaultAction) (p, option, slot, itemId) -> {
					int plat = p.getDuel().getAmount(13204) * 1000;
					if (slot == 0) {
						if (p.getDuel().count(995) <= 0)
							return;
						p.getDuel().remove(995, 1);
						p.getInventory().add(995, 1);

						int total = p.getDuel().count(995);
						if (total >= Integer.MAX_VALUE)
							return;

						p.getDuel().targetDuel.player.getPacketSender().sendClientScript(1450, 31522846, 995, total + plat,
							31522896, 31522906, 31522910);

						p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 27,
							"<col=00ff00>" + formatPrice(total + plat) + " GP");
						p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 17,
							"<col=00ff00>" + formatPrice(total + plat) + " GP");
						return;
					}
					if (slot == 1) {
						if (1 > p.getInventory().count(995))
							return;
						p.getDuel().add(995, 1);
						p.getInventory().remove(995, 1);

						int total = p.getDuel().count(995);
						if (total >= Integer.MAX_VALUE)
							return;

						p.getDuel().targetDuel.player.getPacketSender().sendClientScript(1450, 31522846, 995, total + plat,
							31522896, 31522906, 31522910);
						p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 27,
							"<col=00ff00>" + formatPrice(total + plat) + " GP");
						p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 17,
							"<col=00ff00>" + formatPrice(total + plat) + " GP");
						return;
					}
					if (slot == 2) {
						if (100_000 > p.getInventory().count(995))
							return;
						p.getDuel().add(995, 100_000);
						p.getInventory().remove(995, 100_000);

						int total = p.getDuel().count(995);
						if (total >= Integer.MAX_VALUE)
							return;

						p.getDuel().targetDuel.player.getPacketSender().sendClientScript(1450, 31522846, 995, total + plat,
							31522896, 31522906, 31522910);
						p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 27,
							"<col=00ff00>" + formatPrice(total + plat) + " GP");
						p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 17,
							"<col=00ff00>" + formatPrice(total + plat) + " GP");
						return;
					}
					if (slot == 3) {
						if (1_000_000 > p.getInventory().count(995))
							return;
						p.getDuel().add(995, 1_000_000);
						p.getInventory().remove(995, 1_000_000);

						int total = p.getDuel().count(995);
						if (total >= Integer.MAX_VALUE)
							return;

						p.getDuel().targetDuel.player.getPacketSender().sendClientScript(1450, 31522846, 995, total + plat,
							31522896, 31522906, 31522910);
						p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 27,
							"<col=00ff00>" + formatPrice(total + plat) + " GP");
						p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 17,
							"<col=00ff00>" + formatPrice(total + plat) + " GP");
						return;
					}
					if (slot == 4) {
						if (10_000_000 > p.getInventory().count(995))
							return;
						p.getDuel().add(995, 10_000_000);
						p.getInventory().remove(995, 10_000_000);

						int total = p.getDuel().count(995);
						if (total >= Integer.MAX_VALUE)
							return;

						p.getDuel().targetDuel.player.getPacketSender().sendClientScript(1450, 31522846, 995, total + plat,
							31522896, 31522906, 31522910);
						p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 27,
							"<col=00ff00>" + formatPrice(total + plat) + " GP");
						p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 17,
							"<col=00ff00>" + formatPrice(total + plat) + " GP");
						return;
					}
					if (slot == 5) {
						p.integerInput("How much would you like to stake.", value -> {
							if (value > p.getInventory().count(995)) {
								p.sendMessage("You can't stake that amount");
								return;
							}
							if (value == 0) {
								p.getInventory().add(995, p.getDuel().count(13204));
								p.getDuel().remove(995, p.getDuel().count(13204));
							} else {
								p.getDuel().add(995, value);
								p.getInventory().remove(995, value);
							}

							int total = p.getDuel().count(995);

							p.getDuel().targetDuel.player.getPacketSender().sendClientScript(1450, 31522846, 995, total + plat,
								31522896, 31522906, 31522910);
							p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 27,
								"<col=00ff00>" + formatPrice(total + plat) + " GP");
							p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 17,
								"<col=00ff00>" + formatPrice(total + plat) + " GP");
						});
					}
				};
				h.actions[19] = (DefaultAction) (p, option, slot, itemId) -> {
					int coins = p.getDuel().getAmount(995);
					if (slot == 0) {
						if (!p.getInventory().contains(13204))
							return;
						p.getDuel().remove(13204, 1);
						p.getInventory().add(13204, 1);

						int total = p.getDuel().count(13204);
						if (total >= Integer.MAX_VALUE)
							return;

						p.getDuel().targetDuel.player.getPacketSender().sendClientScript(1450, 31522846, 13204, (total) + coins,
							31522896, 31522906, 31522910);
						p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 27,
							"<col=00ff00>" + formatPrice((total * 1000L) + coins) + " GP");
						p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 17,
							"<col=00ff00>" + formatPrice((total * 1000L) + coins) + " GP");
						return;
					}
					if (slot == 1) {
						if (!p.getInventory().contains(13204))
							return;
						p.getDuel().add(13204, 1);
						p.getInventory().remove(13204, 1);

						int total = p.getDuel().count(13204);
						if (total >= Integer.MAX_VALUE)
							return;

						p.getDuel().targetDuel.player.getPacketSender().sendClientScript(1450, 31522846, 13204, (total) + coins,
							31522896, 31522906, 31522910);
						p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 27,
							"<col=00ff00>" + formatPrice((total * 1000L) + coins) + " GP");
						p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 17,
							"<col=00ff00>" + formatPrice((total * 1000L) + coins) + " GP");
						return;
					}
					if (slot == 2) {
						if (100 > p.getInventory().count(13204))
							return;
						p.getDuel().add(13204, 100);
						p.getInventory().remove(13204, 100);

						int total = p.getDuel().count(13204);
						if (total >= Integer.MAX_VALUE)
							return;

						p.getDuel().targetDuel.player.getPacketSender().sendClientScript(1450, 31522846, 13204, (total) + coins,
							31522896, 31522906, 31522910);
						p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 27,
							"<col=00ff00>" + formatPrice((total * 1000L) + coins) + " GP");
						p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 17,
							"<col=00ff00>" + formatPrice((total * 1000L) + coins) + " GP");
						return;
					}
					if (slot == 3) {
						if (1_000 > p.getInventory().count(13204))
							return;
						p.getDuel().add(13204, 1_000);
						p.getInventory().remove(13204, 1_000);

						int total = p.getDuel().count(13204);
						if (total >= Integer.MAX_VALUE)
							return;

						p.getDuel().targetDuel.player.getPacketSender().sendClientScript(1450, 31522846, 13204, (total) + coins,
							31522896, 31522906, 31522910);
						p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 27,
							"<col=00ff00>" + formatPrice((total * 1000L) + coins) + " GP");
						p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 17,
							"<col=00ff00>" + formatPrice((total * 1000L) + coins) + " GP");
						return;
					}
					if (slot == 4) {
						if (10_000_000 > p.getInventory().count(13204))
							return;
						p.getDuel().add(13204, 10_000_000);
						p.getInventory().remove(13204, 10_000_000);

						int total = p.getDuel().count(13204);
						if (total >= Integer.MAX_VALUE)
							return;

						p.getDuel().targetDuel.player.getPacketSender().sendClientScript(1450, 31522846, 13204, (total) + coins,
							31522896, 31522906, 31522910);
						p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 27,
							"<col=00ff00>" + formatPrice((total * 1000L) + coins) + " GP");
						p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 17,
							"<col=00ff00>" + formatPrice((total * 1000L) + coins) + " GP");
						return;
					}
					if (slot == 5) {
						p.integerInput("How much would you like to stake.", value -> {
							if (value > p.getInventory().count(13204)) {
								p.sendMessage("You can't stake that amount");
								return;
							}
							if (value == 0) {
								p.getInventory().add(13204, p.getDuel().count(13204));
								p.getDuel().remove(13204, p.getDuel().count(13204));
								return;
							}
							p.getDuel().add(13204, value);
							p.getInventory().remove(13204, value);

							int total = p.getDuel().count(13204);
							if (total >= Integer.MAX_VALUE)
								return;

							p.getDuel().targetDuel.player.getPacketSender().sendClientScript(1450, 31522846, 13204, (total) + coins,
								31522896, 31522906, 31522910);
							p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 27,
								"<col=00ff00>" + formatPrice((total * 1000L) + coins) + " GP");
							p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 17,
								"<col=00ff00>" + formatPrice((total * 1000L) + coins) + " GP");
						});
					}
				};

				h.actions[74] = (SimpleAction) p -> {
					if (p.acceptDelay.isDelayed())
						return;
					p.getDuel().accept(false, true);
				};
				h.actions[75] = (SimpleAction) p -> p.getDuel().close();
			});

			InterfaceHandler.register(Interface.DUEL_ARENA_STAGE_THREE, h -> {

				h.actions[72] = (SimpleAction) p -> p.getDuel().accept(false, false);
				h.actions[74] = (SimpleAction) p -> p.getDuel().close();
			});

			/**
			 * Trapdoor
			 */
			ObjectAction.register(3203, "forfeit", (player, obj) -> {
				if (player.getDuel().isToggled(DuelRule.NO_FORFEIT)) {
					player.sendMessage("Forfeit has been disabled for this duel!");
					return;
				}
				if (player.getDuel().stage < 4) {
					/* not in duel */
					return;
				}
				player.dialogue(new OptionsDialogue("Are you sure you want to forfeit this duel?",
					new Option("Yes", () -> player.getDuel().lose(true)),
					new Option("No", player::closeDialogue)));
			});

			/**
			 * Scoreboard
			 */
			World.startEvent(event -> {
				for (int i = 0; i < 31; i++)
					FIGHT_HISTORY.add("" + i);
			});

			ObjectAction.register(3192, "view", (player, obj) -> {

				// TODO
				// int x = 48;
				// for(String history : FIGHT_HISTORY)
				// player.getPacketSender().sendString(108, x++, history);

				// player.getPacketSender().sendString(108, 16, "My Wins: " + player.duelWins);
				// player.getPacketSender().sendString(108, 17, "My Losses: " +
				// player.duelLosses);
				// player.openInterface(ToplevelComponent.MAINMODAL, 108);
			});

		} catch (Exception e) {
			log.warn("TODO(polish): new dueal area interface.");
		}
	}

	public static void sendStageThreeStrings(Player p) {
		int coins = p.getDuel().getAmount(995);
		int amount = p.getDuel().getAmount(13204);
		p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_THREE, 86,
			"<col=00ff00>" + formatPrice(coins) + " GP");
		p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_THREE, 95, "<col=00ff00>" + formatPrice(coins) + " GP");
		p.getDuel().targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_THREE, 81,
			"<col=00ff00>" + formatPrice(amount * 1000L) + " GP");
		p.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_THREE, 90,
			"<col=00ff00>" + formatPrice(amount * 1000L) + " GP");
	}

	public static String formatPrice(long price) {
		if (price > 999_999_999) {
			return NumberUtils.formatNumber(price / 1_000_000_000) + "B";
		} else if (price > 9_999_999) {
			return NumberUtils.formatNumber(price / 1_000_000) + "M";
		} else if (price > 99_999) {
			return NumberUtils.formatNumber(price / 1_000) + "K";
		}

		return NumberUtils.formatNumber(price);
	}

	private static void sendItems(Player player, ItemContainer container, int scriptId) {
		Object[] ids = new Object[container.getItems().length];
		StringBuilder sb = new StringBuilder(ids.length);
		for (int i = 0; i < ids.length; i++) {
			ids[i] = container.getId(i);
			sb.append("i");
		}
		player.getPacketSender().sendClientScript(scriptId, sb.toString(), ids);
	}

	/**
	 * Separator
	 */

	// 1 = first screen, 2 = second screen, 3 third screen, 4 = countdown, 5 = fight
	public transient int stage = 0;
	private transient boolean accepted;
	public transient Duel targetDuel;
	private transient int settings;
	private transient int requestUserId = -1;

	public Duel(Player player) {
		init(player, 28, -1, 64168, 134, false);
		mirror(-2, 60937, 134);
	}

	public void request(Player target) {
		Duel targetDuel = target.getDuel();
		if (stage >= 4 || targetDuel.stage >= 4) // already in a duel
			return;
		if (targetDuel.requestUserId == player.getUserId()) {
			if (!target.getPosition().inBounds(DuelArena.BOUNDS) && !target.getPosition().inBounds(DuelArena.CUSTOM_EDGE)) {
				player.sendMessage("Your target isn't inside the duel arena.");
				return;
			}
			if (!player.getPosition().inBounds(DuelArena.BOUNDS) && !player.getPosition().inBounds(DuelArena.CUSTOM_EDGE)) {
				player.sendMessage("You're not inside the duel arena.");
				return;
			}
			if (target.isLocked()) {
				player.sendMessage("This player is currently busy.");
				return;
			}
			init(targetDuel);
			targetDuel.init(this);
			return;
		}
		requestUserId = target.getUserId();
		target.getPacketSender().sendMessage(player.getName() + " wishes to duel with you.", player.getName(), 103);
		player.getPacketSender().sendMessage("Challenging " + target.getName() + "...", null, 102);
	}

	public void close() {
		if (targetDuel == null) {
			/* no duel active */
			return;
		}
		if (stage >= 4) {
			/* in a fight */
			return;
		}
		targetDuel.player.sendMessage("The other player declined the duel.");
		targetDuel.returnItems();
		targetDuel.closeInterfaces();
		targetDuel.destroy();

		player.sendMessage("You declined the duel.");
		returnItems();
		closeInterfaces();
		destroy();
	}

	public boolean isToggled(DuelRule rule) {
		return (settings & rule.bitValue) != 0;
	}

	public boolean draw() {
		if (stage >= 3) {
			targetDuel.handleDraw();
			handleDraw();
			return true;
		}
		return true;
	}

	public void handleDraw() {
		if (player.getCombat().isDead()) {
			player.getCombat().setDead(false);
			player.animate(-1, 0);
		}
		player.getMovement().teleport(CUSTOM_END_BOUNDS.randomX(), CUSTOM_END_BOUNDS.randomY(), 0);
		player.getPacketSender().resetHintIcon(false);
		// player.setAction(1, PlayerAction.CHALLENGE);
		player.getCombat().restore();
		player.getCombat().restoreSpecial(100);
		player.getCombat().resetKillers();
		ItemContainer container = new ItemContainer();
		container.init(player, 28, -1, 63761, 541, false);
		container.sendAll = true;
		for (Item item : getItems()) {
			if (item != null) {
				player.getInventory().addOrDrop(item.getId(), item.getAmount());
				container.add(item);
			}
		}
		player.getPacketSender().sendString(1085, 18, "Your Items:");
		player.getPacketSender().sendString(1085, 15, "You have tied!");
		player.getPacketSender().sendString(1085, 19, "Opponent:");
		player.sendFilteredMessage("The duel resulted in a tie!");
		player.getPacketSender().sendString(1085, 17, "Close");
		player.getPacketSender().sendString(1085, 23, targetDuel.player.getName());
		player.getPacketSender().sendString(1085, 22, "" + targetDuel.player.getCombat().getLevel());
		player.getPacketSender().sendClientScript(917, "ii", -1, -1);
		player.openInterface(ToplevelComponent.MAINMODAL, 1085);
		player.getPacketSender().sendClientScript(149, "iiiiiisssss", 852 << 16 | 33, 541, 6, 6, 0, -1, "", "", "", "", "");
		player.getPacketSender().sendIfEvents(0, 35, 110, 33, 1024);
		container.sendUpdates();
		destroy();
	}

	public boolean lose(boolean forfeit) {
		if (stage >= 3) {
			targetDuel.leave(true);
			if (forfeit)
				targetDuel.player.sendMessage("Your opponent forfeit the duel.");
			leave(false);
			return true;
		}
		return false;
	}

	/**
	 * Equipment
	 */

	public boolean isBlocked(ObjType def) {
		if (stage < 4)
			return false;
		switch (def.equipSlot) {
			case Equipment.SLOT_HAT:
				return isToggled(DuelRule.NO_HELMS);
			case Equipment.SLOT_CAPE:
				return isToggled(DuelRule.NO_CAPES);
			case Equipment.SLOT_AMULET:
				return isToggled(DuelRule.NO_AMULETS);
			case Equipment.SLOT_AMMO:
				return isToggled(DuelRule.NO_AMMO);
			case Equipment.SLOT_WEAPON:
				return isToggled(DuelRule.NO_WEAPON) || (def.twoHanded && isToggled(DuelRule.NO_SHIELD));
			case Equipment.SLOT_CHEST:
				return isToggled(DuelRule.NO_BODY);
			case Equipment.SLOT_SHIELD:
				return isToggled(DuelRule.NO_SHIELD);
			case Equipment.SLOT_LEGS:
				return isToggled(DuelRule.NO_LEGS);
			case Equipment.SLOT_RING:
				return isToggled(DuelRule.NO_RING);
			case Equipment.SLOT_FEET:
				return isToggled(DuelRule.NO_BOOTS);
			case Equipment.SLOT_HANDS:
				return isToggled(DuelRule.NO_GLOVES);
		}
		return false;
	}

	private void init(Duel targetDuel) {
		close();
		this.targetDuel = targetDuel;
		this.requestUserId = -1;
		update(null);
	}

	private void destroy() {
		stage = 0;
		accepted = false;
		targetDuel = null;
		settings = 0;
		player.getPacketSender().sendClientScript(209, "s", "");
		clear();
	}

	/**
	 * Interfaces
	 */

	private void update(ItemContainerG mirrorContainer) {
		if (mirrorContainer == null) {
			mirrorContainer = this;
			open();
		} else {
			accepted = false;
			targetDuel.accepted = false;
			targetDuel.updateState();
			updateState();
			targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 81,
				"<col=ff0000> Stake has changed - check before accepting!");
		}
		sendUpdates(mirrorContainer);
	}

	private void open() {
		for (int slot = 0; slot < 28; slot++) {
			update(slot);
		}
		sendUpdates(targetDuel);
		stage = 1;
		sendAll = true;
		updateSettings();

		/* target name & combat */
		int targetLevel = targetDuel.player.getCombat().getLevel();
		player.getPacketSender().sendString(482, 35, "Dueling with: " + targetDuel.player.getName());
		player.getPacketSender().sendString(482, 34,
			levelWarning(player.getCombat().getLevel(), targetLevel) + "Combat level: " + targetLevel);
		player.getPacketSender().sendClientScript(209, "s", targetDuel.player.getName());

		/* target stats */
		StatList targetStats = targetDuel.player.getStats();
		StatList playerStats = player.getStats();
		player.getPacketSender().sendString(482, 8,
			levelWarning(playerStats.get(StatType.Attack).currentLevel, targetStats.get(StatType.Attack).currentLevel)
				+ targetStats.get(StatType.Attack).currentLevel);
		player.getPacketSender().sendString(482, 9,
			"" + levelWarning(playerStats.get(StatType.Attack).fixedLevel, targetStats.get(StatType.Attack).fixedLevel)
				+ targetStats.get(StatType.Attack).fixedLevel);
		player.getPacketSender().sendString(482, 12, ""
			+ levelWarning(playerStats.get(StatType.Strength).currentLevel, targetStats.get(StatType.Strength).currentLevel)
			+ targetStats.get(StatType.Strength).currentLevel);
		player.getPacketSender()
			.sendString(482, 13, ""
				+ levelWarning(playerStats.get(StatType.Strength).fixedLevel, targetStats.get(StatType.Strength).fixedLevel)
				+ targetStats.get(StatType.Strength).fixedLevel);
		player.getPacketSender().sendString(482, 16, ""
			+ levelWarning(playerStats.get(StatType.Defence).currentLevel, targetStats.get(StatType.Defence).currentLevel)
			+ targetStats.get(StatType.Defence).currentLevel);
		player.getPacketSender()
			.sendString(482, 17, ""
				+ levelWarning(playerStats.get(StatType.Defence).fixedLevel, targetStats.get(StatType.Defence).fixedLevel)
				+ targetStats.get(StatType.Defence).fixedLevel);
		player.getPacketSender().sendString(482, 20, "" + levelWarning(playerStats.get(StatType.Hitpoints).currentLevel,
			targetStats.get(StatType.Hitpoints).currentLevel) + targetStats.get(StatType.Hitpoints).currentLevel);
		player.getPacketSender().sendString(482, 21, ""
			+ levelWarning(playerStats.get(StatType.Hitpoints).fixedLevel, targetStats.get(StatType.Hitpoints).fixedLevel)
			+ targetStats.get(StatType.Hitpoints).fixedLevel);
		player.getPacketSender()
			.sendString(482, 24, ""
				+ levelWarning(playerStats.get(StatType.Prayer).currentLevel, targetStats.get(StatType.Prayer).currentLevel)
				+ targetStats.get(StatType.Prayer).currentLevel);
		player.getPacketSender().sendString(482, 25,
			"" + levelWarning(playerStats.get(StatType.Prayer).fixedLevel, targetStats.get(StatType.Prayer).fixedLevel)
				+ targetStats.get(StatType.Prayer).fixedLevel);
		player.getPacketSender()
			.sendString(482, 28, ""
				+ levelWarning(playerStats.get(StatType.Ranged).currentLevel, targetStats.get(StatType.Ranged).currentLevel)
				+ targetStats.get(StatType.Ranged).currentLevel);
		player.getPacketSender().sendString(482, 29,
			"" + levelWarning(playerStats.get(StatType.Ranged).fixedLevel, targetStats.get(StatType.Ranged).fixedLevel)
				+ targetStats.get(StatType.Ranged).fixedLevel);
		player.getPacketSender()
			.sendString(482, 32, ""
				+ levelWarning(playerStats.get(StatType.Magic).currentLevel, targetStats.get(StatType.Magic).currentLevel)
				+ targetStats.get(StatType.Magic).currentLevel);
		player.getPacketSender().sendString(482, 33,
			"" + levelWarning(playerStats.get(StatType.Magic).fixedLevel, targetStats.get(StatType.Magic).fixedLevel)
				+ targetStats.get(StatType.Magic).fixedLevel);

		/* misc */
		player.getPacketSender().sendString(482, 90,
			"Choose the options for your duel on the left. Restrict unusable worn slots on the right.");

		/* display and update duel state */
		player.openInterface(ToplevelComponent.MAINMODAL, 482);
		player.openInterface(ToplevelComponent.SIDEMODAL, 301);
		updateState();
	}

	private String levelWarning(int playerLevel, int targetLevel) {

		int levelDifference = playerLevel - targetLevel;
		if (levelDifference < -9)
			return "<col=ff0000>";
		if (levelDifference < -6)
			return "<col=ff3000>";
		if (levelDifference < -3)
			return "<col=ff7000>";
		if (levelDifference < 0)
			return "<col=ffb000>";
		if (levelDifference > 9)
			return "<col=ff00>";
		if (levelDifference > 6)
			return "<col=40ff00>";
		if (levelDifference > 3)
			return "<col=80ff00>";
		if (levelDifference > 0)
			return "<col=c0ff00>";
		return "<col=ffff00>";
	}

	private void updateSettings() {
		player.getPacketSender().sendVarp(286, settings);

		/**
		 * Preset rules
		 */
		if (settings == player.presetDuelVarp)
			player.getPacketSender().setHidden(Interface.DUEL_ARENA_RULES, 99, false);
		else
			player.getPacketSender().setHidden(Interface.DUEL_ARENA_RULES, 99, true);

		/**
		 * Last duel rules
		 */
		if (settings == player.lastDuelVarp)
			player.getPacketSender().setHidden(Interface.DUEL_ARENA_RULES, 101, false);
		else
			player.getPacketSender().setHidden(Interface.DUEL_ARENA_RULES, 101, true);

		/**
		 * Last duel rules
		 */
		if (settings == player.Whip)
			player.getPacketSender().setHidden(Interface.DUEL_ARENA_RULES, 103, false);
		else
			player.getPacketSender().setHidden(Interface.DUEL_ARENA_RULES, 103, true);
		/**
		 * Last duel rules
		 */
		if (settings == player.Boxing)
			player.getPacketSender().setHidden(Interface.DUEL_ARENA_RULES, 105, false);
		else
			player.getPacketSender().setHidden(Interface.DUEL_ARENA_RULES, 105, true);
	}

	private void updateState() {
		String s = null;
		if (accepted)
			s = (stage == 1 ? "<col=ff0000>" : "<col=aaaaaa>") + "Waiting for other player...";
		else if (targetDuel.accepted)
			s = (stage == 1 ? "<col=ff0000>" : "<col=aaaaaa>") + "Other player has accepted.";
		if (stage == 1)
			player.getPacketSender().sendString(482, 91, s == null ? "" : s);
		else if (stage == 2)
			player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_TWO, 81, s == null ? "" : s);
		else if (stage == 3)
			player.getPacketSender().sendString(476, 51, s == null ? "" : s);
	}

	private void closeInterfaces() {
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.closeInterface(ToplevelComponent.SIDEMODAL);
	}

	private void accept(boolean firstScreen, boolean secondScreen) {
		if (accepted)
			return;

		if (firstScreen) {
			if (stage != 1)
				return;
			if (isToggled(DuelRule.NO_MELEE) && isToggled(DuelRule.NO_MAGIC) && isToggled(DuelRule.NO_RANGED)) {
				player.sendMessage("You can't have no melee, no magic, no ranged, how would you fight?");
				return;
			}
			if (isToggled(DuelRule.NO_MELEE)) {
				ObjType playerWeapon = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
				ObjType targetWeapon = targetDuel.player.getEquipment().getDef(Equipment.SLOT_WEAPON);
				if (playerWeapon == null) {
					player.sendMessage("You don't have a weapon equipped!");
					return;
				}
				if (targetWeapon == null) {
					player.sendMessage("Your opponent doesn't have a weapon equipped and no weapon switching is enabled!");
					return;
				}
				if (playerWeapon.id != targetWeapon.id) {
					player.sendMessage("You don't have the same weapon as your opponent and no weapon switching is enabled.");
					return;
				}
			}
			if (targetDuel.accepted) {
				second();
				targetDuel.second();
				return;
			}
		} else if (secondScreen) {
			if (stage != 2)
				return;
			ArrayList<Item> items = getBlockedEquipment();
			for (Item item : getItems()) {
				if (item != null)
					items.add(item);
			}
			for (Item item : targetDuel.getItems()) {
				if (item != null)
					items.add(item);
			}
			int slotsRequired = 0;
			for (Item item : items) {
				if (item.getDef().stackable && player.getInventory().hasId(item.getId()))
					continue;
				slotsRequired++;
			}
			if (player.getInventory().getFreeSlots() < slotsRequired) {
				player.sendMessage("You don't have enough inventory space to accept this duel.");
				return;
			}
			if (targetDuel.accepted) {
				third();
				targetDuel.third();
				return;
			}
		} else {
			if (stage != 3)
				return;
			if (World.updating && (getCount() > 0 || targetDuel.getCount() > 0)) {
				player.sendMessage("You cannot stake items while the server is updating.");
				return;
			}
			if (targetDuel.accepted) {
				start();
				return;
			}
		}
		accepted = true;
		targetDuel.updateState();
		updateState();
	}

	private void offer(Item item, int amount) {
		if (stage != 2) {
			/* not on the second screen! */
			return;
		}
		if (player.getGameMode().isIronMan()) {
			player.sendMessage("Ironmen cannot stake items.");
			return;
		}
		if (targetDuel.player.getGameMode().isIronMan()) {
			player.sendMessage("Your opponent is an ironman and cannot participate in staked duels.");
			return;
		}
		if (!item.getDef().tradeable && item.getId() != BLOOD_MONEY) {
			player.sendMessage("You can't stake this item.");
			return;
		} else if (item.getAttributeHash() != 0) {
			player.sendMessage("You can't stake this item.");
			return;
		}
		int moved = item.move(item.getId(), amount, this);
		if (moved <= 0) {
			player.sendMessage("Not enough space in your duel.");
			return;
		}
		targetDuel.player.sendMessage("Duel Stake addition: " + amount + "x " + item.getDef().name + " added!");
		update(targetDuel);
	}

	private void remove(Item item, int amount) {
		if (stage != 2) {
			/* not on the second screen! */
			return;
		}

		int moved = item.move(item.getId(), amount, player.getInventory());
		if (moved <= 0) {
			Server.logWarning(player.getName() + " failed to remove item (" + item.getId() + ", " + item.getAmount()
				+ ") from duel, this should NEVER happen!");
			return;
		}

		targetDuel.player.getPacketSender().sendClientScript(10060, "iii", 46137372, item.getSlot(), 46137417);
		targetDuel.player.acceptDelay.delaySeconds(3);

		targetDuel.player.sendMessage("Duel Stake removal: " + amount + "x " + item.getDef().name + " removed!");
		update(targetDuel);
	}

	private void second() {
		stage = 2;
		accepted = false;
		targetDuel.accepted = false;

		player.openInterface(ToplevelComponent.SIDEMODAL, 421);
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.DUEL_ARENA_STAGE_TWO);

		player.getPacketSender().sendString(481, 24, targetDuel.player.getName() + "'s stake:");
		// player.getPacketSender().sendString(481, 33, "To stake
		// <col=CCCCCC>platinum</col> or <col=FFFF00>coins</col> use the buttons on the
		// panel in the middle.<br><br>");
		// for (int i = 95; i <= 102; i++) {
		// player.getPacketSender().sendString(481, i, "");
		// }

		player.getPacketSender().sendClientScript(917, "ii", -1, -1);
		player.getPacketSender().sendClientScript(149, "IviiiIsssss", 27590656, 93, 4, 7, 0, -1, "Use", "", "", "", "");

		player.getPacketSender().sendIfEvents(481, 19, 0, 5, AccessMasks.ClickOp1);
		player.getPacketSender().sendIfEvents(481, 34, 0, 13, AccessMasks.ClickOp10);
		player.getPacketSender().sendIfEvents(481, 20, 0, 5, AccessMasks.ClickOp1);
		player.getPacketSender().sendIfEvents(421, 1, 0, 27, AccessMasks.ClickOp1, AccessMasks.ClickOp10);
		player.getPacketSender().sendIfEvents(481, 31, 0, 27, AccessMasks.ClickOp10);

		player.getPacketSender().sendItems(-1, 64168, 134, this.getItems());
		player.getPacketSender().sendItems(-2, 60937, 134, this.getItems());

		if (isToggled(DuelRule.SHOW_INVENTORIES)) {
			player.getPacketSender().setHidden(481, 9, false);
			player.getPacketSender().setHidden(481, 32, false);
			player.getPacketSender().setHidden(481, 34, true);
			player.getPacketSender().setHidden(481, 33, true);
			player.getPacketSender().sendIfEvents(481, 29, 0, 27, 1024);
			player.getPacketSender().sendIfEvents(481, 30, 0, 13, 1024);
			sendItems(player, targetDuel.player.getEquipment(), 1447);
			sendItems(player, targetDuel.player.getInventory(), 1452);
		} else {
			player.getPacketSender().setHidden(481, 9, false);
			player.getPacketSender().setHidden(481, 33, false);
		}
		updateState();
	}

	private void third() {
		stage = 3;
		accepted = false;

		player.openInterface(ToplevelComponent.MAINMODAL, Interface.DUEL_ARENA_STAGE_THREE);
		player.getPacketSender().setHidden(476, 71, false);

		player.getPacketSender().sendItems(-1, 64168, 134, this.getItems());
		targetDuel.player.getPacketSender().sendItems(-2, 60937, 134, this.getItems());

		sendStageThreeStrings(player);

		player.getPacketSender().sendString(Interface.DUEL_ARENA_STAGE_THREE, 66,
			targetDuel.player.getName() +
				"<br> Combat Level: " + targetDuel.player.getCombat().getLevel() +
				"<br> Attack: " + targetDuel.player.getStats().get(StatType.Attack).currentLevel + "/"
				+ targetDuel.player.getStats().get(StatType.Attack).fixedLevel +
				"<br> Defence: " + targetDuel.player.getStats().get(StatType.Defence).currentLevel + "/"
				+ targetDuel.player.getStats().get(StatType.Defence).fixedLevel +
				"<br> Strength: " + targetDuel.player.getStats().get(StatType.Strength).currentLevel + "/"
				+ targetDuel.player.getStats().get(StatType.Strength).fixedLevel +
				"<br> Hitpoints: " + targetDuel.player.getStats().get(StatType.Hitpoints).currentLevel + "/"
				+ targetDuel.player.getStats().get(StatType.Hitpoints).fixedLevel +
				"<br> Prayer: " + targetDuel.player.getStats().get(StatType.Prayer).currentLevel + "/"
				+ targetDuel.player.getStats().get(StatType.Prayer).fixedLevel +
				"<br> Ranged: " + targetDuel.player.getStats().get(StatType.Ranged).currentLevel + "/"
				+ targetDuel.player.getStats().get(StatType.Ranged).fixedLevel +
				"<br> Magic: " + targetDuel.player.getStats().get(StatType.Magic).currentLevel + "/"
				+ targetDuel.player.getStats().get(StatType.Magic).fixedLevel);

		// player.getPacketSender().sendClientScript(1451, "ii",
		// Interface.DUEL_ARENA_STAGE_THREE << 16 | 79, -1);
	}

	/**
	 * Settings / Rules
	 */

	private boolean prepSettings() {
		if (stage != 1)
			return false;
		accepted = targetDuel.accepted = false;
		targetDuel.updateState();
		updateState();
		return true;
	}

	private void toggle(DuelRule rule) {
		boolean toggleable = checkToggle(rule);
		if (!prepSettings())
			return;
		if (isToggled(rule) || !toggleable)
			settings &= ~rule.bitValue;
		else
			settings |= rule.bitValue;

		targetDuel.settings = settings;
		targetDuel.updateSettings();
		updateSettings();
		if (!toggleable)
			return;
		player.getPacketSender().sendClientScript(1441, "ii", 1588458, 5);
		targetDuel.player.getPacketSender().sendClientScript(1441, "ii", 1588458, 5);

		/* accept button status */
		player.acceptDelay.delaySeconds(3);
		player.getPacketSender().sendClientScript(968, "ii", Interface.DUEL_ARENA_RULES << 16 | 106, -1);

		targetDuel.player.acceptDelay.delaySeconds(3);
		targetDuel.player.getPacketSender().sendClientScript(968, "ii", Interface.DUEL_ARENA_RULES << 16 | 106, -1);

		/* rule changed warning */
		targetDuel.player.sendMessage("Duel Option change - " + rule.message + (isToggled(rule) ? " ON!" : " OFF!"));
		targetDuel.player.getPacketSender().sendString(Interface.DUEL_ARENA_RULES, 91,
			"<col=ff0000>An option has changed - check before accepting!");
		targetDuel.player.getPacketSender().sendClientScript(968, "Ii", 31588458, rule.bitPos);
	}

	private boolean checkToggle(DuelRule rule) {
		if (rule == DuelRule.NO_MOVEMENT && isToggled(DuelRule.OBSTACLES)) {
			player.sendMessage("You can't have no movement with obstacles.");
			return false;
		}
		if (rule == DuelRule.OBSTACLES && isToggled(DuelRule.NO_MOVEMENT)) {
			player.sendMessage("You can't have obstacles with no movement.");
			return false;
		}
		if (rule == DuelRule.NO_MELEE) {
			if (isToggled(DuelRule.NO_RANGED) && isToggled(DuelRule.NO_MAGIC)) {
				player.sendMessage("You can't have no melee, no magic, no ranged, how would you fight?");
				return false;
			}
		}
		if (rule == DuelRule.NO_MAGIC) {
			if (isToggled(DuelRule.NO_RANGED) && isToggled(DuelRule.NO_MELEE)) {
				player.sendMessage("You can't have no melee, no magic, no ranged, how would you fight?");
				return false;
			}
		}
		if (rule == DuelRule.NO_RANGED) {
			if (isToggled(DuelRule.NO_MELEE) && isToggled(DuelRule.NO_MAGIC)) {
				player.sendMessage("You can't have no melee, no magic, no ranged, how would you fight?");
				return false;
			}
		}
		if (rule == DuelRule.FUN_WEAPONS) {
			player.sendMessage("This rule is currently not supported.");
			return false;
		}
		if (rule == DuelRule.NO_WEAPON && isToggled(DuelRule.NO_SHIELD) && !isToggled(DuelRule.NO_WEAPON)) {
			player.sendMessage("Beware: You won't be able to use two-handed weapons such as bows.");
			targetDuel.player.sendMessage("Beware: You won't be able to use two-handed weapons such as bows.");
			return true;
		}
		if (rule == DuelRule.NO_SHIELD && isToggled(DuelRule.NO_WEAPON) && !isToggled(DuelRule.NO_SHIELD)) {
			player.sendMessage("Beware: You won't be able to use two-handed weapons such as bows.");
			targetDuel.player.sendMessage("Beware: You won't be able to use two-handed weapons such as bows.");
			return true;
		}
		if (rule == DuelRule.NO_WEAPON_SWITCH) {
			ObjType playerWeapon = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
			ObjType targetWeapon = targetDuel.player.getEquipment().getDef(Equipment.SLOT_WEAPON);
			if (playerWeapon == null) {
				player.sendMessage("You don't have a weapon equipped!");
				return false;
			}
			if (targetWeapon == null) {
				player.sendMessage("Your opponent doesn't have a weapon equipped!");
				return false;
			}
			if (playerWeapon.id != targetWeapon.id) {
				player.sendMessage("You don't have the same weapon as your opponent.");
				return false;
			}
		}

		return true;
	}

	private void saveSettings() {
		player.presetDuelVarp = settings;
		player.sendMessage("Stored preset settings overwritten.");
	}

	private void loadSettings(boolean preset) {
		if (!prepSettings())
			return;
		if (preset) {
			if (settings == player.presetDuelVarp) {
				player.sendMessage("Preset duel settings are identical to those already selected.");
				return;
			}
			settings = targetDuel.settings = player.presetDuelVarp;
			player.sendMessage("Preset duel settings loaded.");
			targetDuel.player.sendMessage("Duel Option change - Opponent's last duel options loaded!");
		} else {
			if (settings == player.lastDuelVarp) {
				player.sendMessage("Previous duel settings are identical to those already selected.");
				return;
			}
			settings = targetDuel.settings = player.lastDuelVarp;
			player.sendMessage("Previous duel settings loaded.");
			targetDuel.player.sendMessage("Duel Option change - Opponent's preset options loaded!");
		}
		updateSettings();
		targetDuel.updateSettings();
	}

	private void Whip(boolean preset) {
		if (!prepSettings())
			return;
		if (preset) {
			if (settings == player.presetDuelVarp) {
				player.sendMessage("Preset duel settings are identical to those already selected.");
				return;
			}
			settings = targetDuel.settings = player.presetDuelVarp;
			player.sendMessage("Preset duel settings loaded.");
			targetDuel.player.sendMessage("Duel Option change - Opponent's last duel options loaded!");
		} else {
			if (settings == player.lastDuelVarp) {
				player.sendMessage("Previous duel settings are identical to those already selected.");
				return;
			}
			settings = targetDuel.settings = player.lastDuelVarp;
			player.sendMessage("Previous duel settings loaded.");
			targetDuel.player.sendMessage("Duel Option change - Opponent's preset options loaded!");
		}
		updateSettings();
		targetDuel.updateSettings();
	}

	private void Box(boolean preset) {
		if (!prepSettings())
			return;
		if (preset) {
			if (settings == player.Boxing) {
				player.sendMessage("Preset duel settings are identical to those already selected.");
				return;
			}
			settings = targetDuel.settings = player.Boxing;
			player.sendMessage("Preset duel settings loaded.");
			targetDuel.player.sendMessage("Duel Option change - Opponent's last duel options loaded!");
		} else {
			if (settings == player.Boxing) {
				player.sendMessage("Previous duel settings are identical to those already selected.");
				return;
			}
			settings = targetDuel.settings = player.lastDuelVarp;
			player.sendMessage("Previous duel settings loaded.");
			targetDuel.player.sendMessage("Duel Option change - Opponent's preset options loaded!");
		}
		updateSettings();
		targetDuel.updateSettings();
	}

	/**
	 * Mechanics
	 */

	private void start() {
		stage = 4;
		targetDuel.stage = 4;

		player.lastDuelVarp = settings;
		targetDuel.player.lastDuelVarp = settings;

		closeInterfaces();
		targetDuel.closeInterfaces();

		Bounds[] spawnBounds;
		if (isToggled(DuelRule.OBSTACLES))
			spawnBounds = OBSTACLE_SPAWNS[Random.get(OBSTACLE_SPAWNS.length - 1)];
		else
			spawnBounds = REGULAR_SPAWNS[Random.get(REGULAR_SPAWNS.length - 1)];
		Bounds b1, b2;
		if (Random.rollDie(2)) {
			b1 = spawnBounds[0];
			b2 = spawnBounds[1];
		} else {
			b1 = spawnBounds[1];
			b2 = spawnBounds[0];
		}
		int x1, y1;
		player.getMovement().teleport(x1 = b1.randomX(), y1 = b1.randomY(), 0);
		if (isToggled(DuelRule.NO_MOVEMENT))
			targetDuel.player.getMovement().teleport(x1, y1 - 1, 0);
		else
			targetDuel.player.getMovement().teleport(b2.randomX(), b2.randomY(), 0);

		player.getCombat().restore();
		player.getCombat().restoreSpecial(100);

		targetDuel.player.getCombat().restore();
		targetDuel.player.getCombat().restoreSpecial(100);

		player.vengeanceActive = false;
		targetDuel.player.vengeanceActive = false;

		player.setAction(1, PlayerAction.FIGHT);
		targetDuel.player.setAction(1, PlayerAction.FIGHT);

		player.getPacketSender().sendHintIcon(targetDuel.player);
		targetDuel.player.getPacketSender().sendHintIcon(player);

		player.sotdDelay.reset();
		targetDuel.player.sotdDelay.reset();

		player.vestasSpearSpecial.reset();
		targetDuel.player.vestasSpearSpecial.reset();

		for (Item item : getBlockedEquipment())
			player.getEquipment().unequip(item);
		for (Item item : targetDuel.getBlockedEquipment())
			targetDuel.player.getEquipment().unequip(item);

		player.sendFilteredMessage("Accepted stake and duel options.");

		World.startEvent(e -> {
			int seconds = 3;
			while (stage == 4 && targetDuel != null && targetDuel.stage == 4) {
				if (seconds == 0) {
					player.forceText("FIGHT!");
					targetDuel.player.forceText("FIGHT!");
					stage = targetDuel.stage = 5;
					return;
				}
				player.forceText("" + seconds);
				targetDuel.player.forceText("" + seconds);
				e.delay(3);
				seconds--;
			}
		});
	}

	private void leave(boolean winner) {
		if (player.getCombat().isDead()) {
			player.getCombat().setDead(false);
			player.animate(-1, 0);
		}
		player.getMovement().teleport(3366, 3267, 0);
		player.getPacketSender().resetHintIcon(false);
		// player.setAction(1, PlayerAction.CHALLENGE);
		player.getCombat().restore();
		player.getCombat().resetKillers();
		player.getCombat().restoreSpecial(100);

		ItemContainer container = new ItemContainer();
		container.init(player, 28, -1, 63761, 541, false);
		container.sendAll = true;
		if (winner) {
			returnItems();
			for (Item item : targetDuel.getItems()) {
				if (item != null) {
					if ((item.getAmount() + player.getInventory().count(item.getId())) > Integer.MAX_VALUE) {
						item.setId(13204);

					}
					player.getInventory().addOrDrop(item.getId(), item.getAmount());
					container.add(item);
				}
			}
			player.getPacketSender().sendString(1085, 18, "The Spoils:");
			player.getPacketSender().sendString(1085, 15, "You have won!");
			player.getPacketSender().sendString(1085, 19, "The Defeated:");
			player.duelWins++;
			player.sendFilteredMessage(
				"You have won! You have won " + player.duelWins + " duel" + (player.duelWins == 1 ? "." : "s."));
			player
				.sendFilteredMessage("You have lost " + player.duelLosses + " duel" + (player.duelLosses == 1 ? "." : "s."));
			registerResults(player, targetDuel.player);
			if (this.getCount() > 0 || targetDuel.getCount() > 0)
				Loggers.logDuelStake(player.getUserId(), player.getName(), player.getIp(), targetDuel.player.getUserId(),
					targetDuel.player.getName(), targetDuel.player.getIp(), items, targetDuel.items, player.getUserId());
		} else {
			for (Item item : getItems()) {
				if (item != null)
					container.add(item);
			}
			player.getPacketSender().sendString(1085, 18, "You Lost:");
			player.getPacketSender().sendString(1085, 15, "You lost!");
			player.getPacketSender().sendString(1085, 19, "The Winner:");
			player.duelLosses++;
			player.sendFilteredMessage(
				"You were defeated! You have won " + player.duelWins + " duel" + (player.duelWins == 1 ? "." : "s."));
			player.sendFilteredMessage(
				"You have now lost " + player.duelLosses + " duel" + (player.duelLosses == 1 ? "." : "s."));
			registerResults(targetDuel.player, player);
		}
		player.getPacketSender().sendString(1085, 17, "Close");
		player.getPacketSender().sendString(1085, 23, targetDuel.player.getName());
		player.getPacketSender().sendString(1085, 22, "" + targetDuel.player.getCombat().getLevel());
		player.getPacketSender().sendClientScript(917, "ii", -1, -1);
		player.openInterface(ToplevelComponent.MAINMODAL, 1085);
		player.getPacketSender().sendClientScript(149, "iiiiiisssss", 852 << 16 | 33, 541, 6, 6, 0, -1, "", "", "", "", "");
		player.getPacketSender().sendIfEvents(0, 35, 110, 33, 1024);
		container.sendUpdates();

		destroy();
	}

	private void registerResults(Player winner, Player loser) {
		FIGHT_HISTORY.add(winner.getName() + " (" + winner.getCombat().getLevel() + ") beat "
			+ loser.getName() + " (" + loser.getCombat().getLevel() + ")");
		if (FIGHT_HISTORY.size() > 50)
			FIGHT_HISTORY.poll();
	}

	private void returnItems() {
		for (Item item : getItems()) {
			if (item != null)
				player.getInventory().addOrDrop(item.getId(), item.getAmount());
		}
	}

	private ArrayList<Item> getBlockedEquipment() {
		Equipment equip = player.getEquipment();
		ArrayList<Item> items = new ArrayList<>();
		Item item;
		if (isToggled(DuelRule.NO_HELMS) && (item = equip.get(Equipment.SLOT_HAT)) != null)
			items.add(item);
		if (isToggled(DuelRule.NO_CAPES) && (item = equip.get(Equipment.SLOT_CAPE)) != null)
			items.add(item);
		if (isToggled(DuelRule.NO_AMULETS) && (item = equip.get(Equipment.SLOT_AMULET)) != null)
			items.add(item);
		if (isToggled(DuelRule.NO_AMMO) && (item = equip.get(Equipment.SLOT_AMMO)) != null)
			items.add(item);
		if (isToggled(DuelRule.NO_WEAPON) && (item = equip.get(Equipment.SLOT_WEAPON)) != null)
			items.add(item);
		if (isToggled(DuelRule.NO_BODY) && (item = equip.get(Equipment.SLOT_CHEST)) != null)
			items.add(item);
		if (isToggled(DuelRule.NO_SHIELD)) {
			if ((item = equip.get(Equipment.SLOT_SHIELD)) != null)
				items.add(item);
			if ((item = equip.get(Equipment.SLOT_WEAPON)) != null && item.getDef().twoHanded)
				items.add(item);
		}
		if (isToggled(DuelRule.NO_LEGS) && (item = equip.get(Equipment.SLOT_LEGS)) != null)
			items.add(item);
		if (isToggled(DuelRule.NO_RING) && (item = equip.get(Equipment.SLOT_RING)) != null)
			items.add(item);
		if (isToggled(DuelRule.NO_BOOTS) && (item = equip.get(Equipment.SLOT_FEET)) != null)
			items.add(item);
		if (isToggled(DuelRule.NO_GLOVES) && (item = equip.get(Equipment.SLOT_HANDS)) != null)
			items.add(item);
		return items;
	}
}

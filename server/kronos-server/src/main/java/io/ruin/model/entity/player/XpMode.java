package io.ruin.model.entity.player;

import io.ruin.cache.Color;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import lombok.Getter;

/*
 * @project Kronos
 * @author Patrity - https://github.com/Patrity
 * Created on - 7/22/2020
 */

/**
 * @author Shinatobe - 24/05/2021
 * www.rune-server.ee/members/Shinatobe/
 * @project Gauntlet
 * @project Starter Interface
 */

public enum XpMode {

	OSRS("OSRS", 1, 1, 1, 0, 4),
	IRON("IRONMAN", 25, 15, 10, 10, 3),
	HARD("Hard", 5, 5, 5, 15, 2),
	MEDIUM("Medium", 50, 25, 10, 5, 1),
	EASY("Easy", 200, 50, 20, 0, 0),
	BlANK("Blank", 0, 0, 0, 0, 5),
	NORMAL("Normal", 25, 25, 25, 10, 6),
	EASYIRON("Easy", 100, 25, 25, 0, 7),
	MEDIRON("Medium", 50, 25, 25, 5, 8),
	INSANE("Insane", 10, 10, 10, 15, 9),
	KROTA("Krota", 5, 5, 5, 25, 10),
	HARDIRON("HARD", 25, 25, 25, 10, 11);


	@Getter
	private final String name;


	@Getter
	private final int combatRate, skillRate, after99Rate, dropBonus, Modetype;

	XpMode(String name, int combatRate, int skillRate, int after99Rate, int dropBonus, int Modetype) {
		this.name = name;
		this.combatRate = combatRate;
		this.skillRate = skillRate;
		this.after99Rate = after99Rate;
		this.dropBonus = dropBonus;
		this.Modetype = Modetype;
	}

	public static boolean isXpMode(Player player, XpMode mode) {
		return player.xpMode == mode;
	}

	private static final int[] ITEMS = {38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57};

	private static Item[] getItems() {
		return new Item[]{
			new Item(995, 500_000),
			new Item(558, 500),
			new Item(556, 500),
			new Item(554, 500),
			new Item(555, 500),
			new Item(557, 500),
			new Item(362, 50),
			new Item(863, 250),
			new Item(11978, 1),
			new Item(1323, 1),
			new Item(1333, 1),
			new Item(23846, 1),
			new Item(23843, 1),
			new Item(23840, 1),
			new Item(-1),
			new Item(-1),
			new Item(-1),
			new Item(-1),
			new Item(-1),
			new Item(-1)
		};
	}

	private static Item[] getIronItems() {
		return new Item[]{
			new Item(995, 125_000),
			new Item(1351),
			new Item(590),
			new Item(303),
			new Item(1323),
			new Item(1171),
			new Item(841),
			new Item(882, 100),
			new Item(558, 250),
			new Item(556, 100),
			new Item(555, 100),
			new Item(557, 100),
			new Item(554, 100),
			new Item(362, 25),
			new Item(12810),
			new Item(12811),
			new Item(12812),
			new Item(-1),
			new Item(-1),
			new Item(-1)
		};
	}

	private static Item[] getHcItems() {
		return new Item[]{
			new Item(995, 125_000),
			new Item(1351),
			new Item(590),
			new Item(303),
			new Item(1323),
			new Item(1171),
			new Item(841),
			new Item(882, 100),
			new Item(558, 250),
			new Item(556, 100),
			new Item(555, 100),
			new Item(557, 100),
			new Item(554, 100),
			new Item(362, 25),
			new Item(20792),
			new Item(20794),
			new Item(20796),
			new Item(-1),
			new Item(-1),
			new Item(-1)
		};
	}

	private static Item[] getUltItems() {
		return new Item[]{
			new Item(995, 125_000),
			new Item(1351),
			new Item(590),
			new Item(303),
			new Item(1323),
			new Item(1171),
			new Item(841),
			new Item(882, 100),
			new Item(558, 250),
			new Item(556, 100),
			new Item(555, 100),
			new Item(557, 100),
			new Item(554, 100),
			new Item(362, 25),
			new Item(12813),
			new Item(12814),
			new Item(12815),
			new Item(-1),
			new Item(-1),
			new Item(-1)
		};
	}

	private static Item[] getGroupItems() {
		return new Item[]{
			new Item(995, 125_000),
			new Item(1351),
			new Item(590),
			new Item(303),
			new Item(1323),
			new Item(1171),
			new Item(841),
			new Item(882, 100),
			new Item(558, 250),
			new Item(556, 100),
			new Item(555, 100),
			new Item(557, 100),
			new Item(554, 100),
			new Item(362, 25),
			new Item(25950),
			new Item(25951),
			new Item(25952),
			new Item(-1),
			new Item(-1),
			new Item(-1)
		};
	}

	private static Item[] getItems(Player player) {
		if (player.getGameMode() == GameMode.STANDARD) {
			return getItems();
		} else if (player.getGameMode() == GameMode.IRONMAN) {
			return getIronItems();
		} else if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			return getHcItems();
		} else if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
			return getUltItems();
		} else if (player.getGameMode() == GameMode.GROUP_IRONMAN) {
			return getGroupItems();
		} else {
			return getItems();
		}
	}

	private static void modeItems(Player player) {
		int component = 0;
		for (Item item : getItems(player)) {
			player.getPacketSender().sendItems(Interface.STARTER_INTERFACE, ITEMS[component++], 0, new Item(item.getId(), item.getAmount()));
		}
	}

	private static String getCombatRate(Player player) {
		if (player.xpMode == EASY) {
			return "Combat Rate: 200x";
		} else if (player.xpMode == MEDIUM) {
			return "Combat Rate: 50x";
		} else if (player.xpMode == HARD) {
			return "Combat Rate: 5x";
		} else if (player.xpMode == BlANK) {
			return "You need to select";
		} else {
			return "Combat Rate: ";
		}
	}

	private static String getSkillRate(Player player) {
		if (player.xpMode == EASY) {
			return "Skill Rate: 50x";
		} else if (player.xpMode == MEDIUM) {
			return "Skill Rate: 25x";
		} else if (player.xpMode == HARD) {
			return "Skill Rate: 5x";
		} else if (player.xpMode == BlANK) {
			return "Your xp mode after";
		} else {
			return "Skill Rate: ";
		}
	}

	private static String getDropRate(Player player) {
		if (player.xpMode == EASY) {
			return "";
		} else if (player.xpMode == MEDIUM) {
			return "Drop Rate: 5%";
		} else if (player.xpMode == HARD) {
			return "Drop Rate: 15%";
		} else if (player.xpMode == BlANK) {
			return "Choosing your playstyle.";
		} else {
			return "Drop Rate: ";
		}
	}

	private static String getRestrictions(Player player) {
		if (player.getGameMode() == GameMode.IRONMAN) {
			return "No Trading";
		} else if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			return "No Trading";
		} else if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
			return "No Trading/Banking";
		} else if (player.getGameMode() == GameMode.GROUP_IRONMAN) {
			return "Trading within Group only";
		} else {
			return "";
		}
	}

	private static String getDescription(Player player) {
		if (player.getGameMode() == GameMode.STANDARD) {
			return "Without restrictions you will be unstoppable, Show us what you got!";
		} else if (player.getGameMode() == GameMode.IRONMAN) {
			return "With this mode you will stand alone.";
		} else if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			return "You will only have one life and you will stand alone.";
		} else if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
			return "With this mode you will stand alone and have no Bank.";
		} else if (player.getGameMode() == GameMode.GROUP_IRONMAN) {
			return "25x XP 10% Drop 5 Team Members!";
		} else {
			return "";
		}
	}

	private static void getInfo(Player player) {
		player.getPacketSender().sendString(Interface.STARTER_INTERFACE, 31, getCombatRate(player));
		player.getPacketSender().sendString(Interface.STARTER_INTERFACE, 32, getSkillRate(player));
		player.getPacketSender().sendString(Interface.STARTER_INTERFACE, 33, getDropRate(player));
		player.getPacketSender().sendString(Interface.STARTER_INTERFACE, 34, getRestrictions(player));
		player.getPacketSender().sendString(Interface.STARTER_INTERFACE, 35, getDescription(player));
	}

	public static void setXpMode(Player player, XpMode xpMode) {
		player.xpMode = xpMode;
	}

	public static void register() {
		InterfaceHandler.register(Interface.STARTER_INTERFACE, h -> {
			h.actions[10] = (SimpleAction) p -> {
				if (p.xpMode == OSRS) {
					p.sendMessage(Color.RED.wrap("Please select a Game Mode!"));
				} else if (p.xpMode == BlANK) {
					// XP Modes
					p.dialogue(new OptionsDialogue("Select your XP Mode! This can be changed! Contact Luke!",
						new Option("Easy - 100x Combat 25x skill 0% dropbonus", () -> {
							p.xpMode = EASYIRON;
						}),
						new Option("Medium - 50x Combat 25x skill 5% dropbonus", () -> {
							p.xpMode = MEDIRON;
							p.modifiedDropBonus += 5;
						}),
						new Option("Hard - 25x Combat 10x skill 10% dropbonus", () -> {
							p.xpMode = HARDIRON;
							p.modifiedDropBonus += 10;
						}),
						new Option("Insane - 10x Combat 10x Skill - 15% dropbonus", () -> {
							p.xpMode = INSANE;
							p.modifiedDropBonus += 15;
						}),
						new Option("KROTA - 5x Combat 5x Skill 25% drop & a Special Weapon", () -> {
							p.xpMode = KROTA;
							p.modifiedDropBonus += 25;
							p.getInventory().add(22316, 1);
						})
					));
				} else {
					p.closeInterface(ToplevelComponent.MAINMODAL);
				}
			};

			h.actions[14] = (SimpleAction) p -> { // Normal
				p.xpMode = BlANK;
				VarPlayerRepository.IRONMAN_MODE.set(p, 0);
				p.modifiedDropBonus = 0;
				modeItems(p);
				getInfo(p);
			};

			h.actions[15] = (SimpleAction) p -> { // Ironman
				p.xpMode = BlANK;
				VarPlayerRepository.IRONMAN_MODE.set(p, 1);
				p.modifiedDropBonus = 10;
				modeItems(p);
				getInfo(p);
			};

			h.actions[16] = (SimpleAction) p -> { // Hardcore Ironman
				p.xpMode = BlANK;
				VarPlayerRepository.IRONMAN_MODE.set(p, 3);
				p.modifiedDropBonus = 15;
				modeItems(p);
				getInfo(p);
			};

			h.actions[17] = (SimpleAction) p -> { //Ultimate Ironman
				VarPlayerRepository.IRONMAN_MODE.set(p, 2);
				p.xpMode = BlANK;
				p.modifiedDropBonus = 15;
				modeItems(p);
				getInfo(p);
			};

			h.actions[18] = (SimpleAction) p -> { // Group Ironman
				VarPlayerRepository.IRONMAN_MODE.set(p, 4);//gim
				p.checkGIM = true;
				p.xpMode = HARDIRON;
				p.modifiedDropBonus = 10;
				p.setAction(1, PlayerAction.GIM_Invite);
				modeItems(p);
				getInfo(p);
			};

			h.actions[19] = (SimpleAction) p -> {
//                Config.IRONMAN_MODE.set(p, 4);//gim
//                p.xpMode = IRON;
//                modeItems(p);
//                getInfo(p);

			};
//            h.actions[20] = (SimpleAction) p -> {
////                Config.IRONMAN_MODE.set(p, 4);//gim
////                p.xpMode = IRON;
////                modeItems(p);
////                getInfo(p);
//
//            };
		});
	}

}

package io.ruin.model.item.actions.impl.switcher;

import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemObjectAction;

public class ItemSwitching {

	private static final int STAFFOFFIRE = 1387;
	private static final int STAFFOFAIR = 1381;
	private static final int STAFFOFWATER = 1383;
	private static final int STAFFOFEARTH = 1385;

	private static final int BATTLESTAFFOFFIRE = 1393;
	private static final int BATTLESTAFFOFAIR = 1397;
	private static final int BATTLESTAFFOFWATER = 1395;
	private static final int BATTLESTAFFOFEARTH = 1399;

	private static final int MYSTICSTAFFOFFIRE = 1401;
	private static final int MYSTICSTAFFOFAIR = 1405;
	private static final int MYSTICSTAFFOFWATER = 1403;
	private static final int MYSTICSTAFFOFEARTH = 1407;

	private static final int CRYSTALOFPOWER = 13661;

	public static void register() {

		ItemObjectAction.register(STAFFOFAIR, CRYSTALOFPOWER, ((player, item, obj) -> {
			player.dialogue(new OptionsDialogue("Which staff would you like to switch your " + item.getDef().name + " for?",
				new Option("Staff of fire", () -> {
					item.setId(STAFFOFFIRE);
				}),
				new Option("Staff of water", () -> {
					item.setId(STAFFOFWATER);
				}),
				new Option("Staff of earth", () -> {
					item.setId(STAFFOFEARTH);
				})
			));
		}));
		ItemObjectAction.register(STAFFOFEARTH, CRYSTALOFPOWER, ((player, item, obj) -> {
			player.dialogue(new OptionsDialogue("Which staff would you like to switch your " + item.getDef().name + " for?",
				new Option("Staff of fire", () -> {
					item.setId(STAFFOFFIRE);
				}),
				new Option("Staff of water", () -> {
					item.setId(STAFFOFWATER);
				}),
				new Option("Staff of air", () -> {
					item.setId(STAFFOFAIR);
				})
			));
		}));
		ItemObjectAction.register(STAFFOFFIRE, CRYSTALOFPOWER, ((player, item, obj) -> {
			player.dialogue(new OptionsDialogue("Which staff would you like to switch your " + item.getDef().name + " for?",
				new Option("Staff of air", () -> {
					item.setId(STAFFOFAIR);
				}),
				new Option("Staff of water", () -> {
					item.setId(STAFFOFWATER);
				}),
				new Option("Staff of earth", () -> {
					item.setId(STAFFOFEARTH);
				})
			));
		}));
		ItemObjectAction.register(STAFFOFWATER, CRYSTALOFPOWER, ((player, item, obj) -> {
			player.dialogue(new OptionsDialogue("Which staff would you like to switch your " + item.getDef().name + " for?",
				new Option("Staff of fire", () -> {
					item.setId(STAFFOFFIRE);
				}),
				new Option("Staff of air", () -> {
					item.setId(STAFFOFAIR);
				}),
				new Option("Staff of earth", () -> {
					item.setId(STAFFOFEARTH);
				})
			));
		}));

		ItemObjectAction.register(BATTLESTAFFOFAIR, CRYSTALOFPOWER, ((player, item, obj) -> {
			player.dialogue(new OptionsDialogue("Which staff would you like to switch your " + item.getDef().name + " for?",
				new Option("Fire Battlestaff", () -> {
					item.setId(BATTLESTAFFOFFIRE);
				}),
				new Option("Water Battlestaff", () -> {
					item.setId(BATTLESTAFFOFWATER);
				}),
				new Option("Earth Battlestaff", () -> {
					item.setId(BATTLESTAFFOFEARTH);
				})
			));
		}));
		ItemObjectAction.register(BATTLESTAFFOFEARTH, CRYSTALOFPOWER, ((player, item, obj) -> {
			player.dialogue(new OptionsDialogue("Which staff would you like to switch your " + item.getDef().name + " for?",
				new Option("Fire Battlestaff", () -> {
					item.setId(BATTLESTAFFOFFIRE);
				}),
				new Option("Water Battlestaff", () -> {
					item.setId(BATTLESTAFFOFWATER);
				}),
				new Option("Air Battlestaff", () -> {
					item.setId(BATTLESTAFFOFAIR);
				})
			));
		}));
		ItemObjectAction.register(BATTLESTAFFOFFIRE, CRYSTALOFPOWER, ((player, item, obj) -> {
			player.dialogue(new OptionsDialogue("Which staff would you like to switch your " + item.getDef().name + " for?",
				new Option("Air Battlestaff", () -> {
					item.setId(BATTLESTAFFOFAIR);
				}),
				new Option("Water Battlestaff", () -> {
					item.setId(BATTLESTAFFOFWATER);
				}),
				new Option("Earth Battlestaff", () -> {
					item.setId(BATTLESTAFFOFEARTH);
				})
			));
		}));
		ItemObjectAction.register(BATTLESTAFFOFWATER, CRYSTALOFPOWER, ((player, item, obj) -> {
			player.dialogue(new OptionsDialogue("Which staff would you like to switch your " + item.getDef().name + " for?",
				new Option("Fire Battlestaff", () -> {
					item.setId(BATTLESTAFFOFFIRE);
				}),
				new Option("Air Battlestaff", () -> {
					item.setId(BATTLESTAFFOFAIR);
				}),
				new Option("Earth Battlestaff", () -> {
					item.setId(BATTLESTAFFOFEARTH);
				})
			));
		}));

		ItemObjectAction.register(MYSTICSTAFFOFAIR, CRYSTALOFPOWER, ((player, item, obj) -> {
			player.dialogue(new OptionsDialogue("Which staff would you like to switch your " + item.getDef().name + " for?",
				new Option("Mystic Fire Staff", () -> {
					item.setId(MYSTICSTAFFOFFIRE);
				}),
				new Option("Mystic Water Staff", () -> {
					item.setId(MYSTICSTAFFOFWATER);
				}),
				new Option("Mystic Earth Staff", () -> {
					item.setId(MYSTICSTAFFOFEARTH);
				})
			));
		}));
		ItemObjectAction.register(MYSTICSTAFFOFEARTH, CRYSTALOFPOWER, ((player, item, obj) -> {
			player.dialogue(new OptionsDialogue("Which staff would you like to switch your " + item.getDef().name + " for?",
				new Option("Mystic Fire Staff", () -> {
					item.setId(MYSTICSTAFFOFFIRE);
				}),
				new Option("Mystic Water Staff", () -> {
					item.setId(MYSTICSTAFFOFWATER);
				}),
				new Option("Mystic Air Staff", () -> {
					item.setId(MYSTICSTAFFOFAIR);
				})
			));
		}));
		ItemObjectAction.register(MYSTICSTAFFOFFIRE, CRYSTALOFPOWER, ((player, item, obj) -> {
			player.dialogue(new OptionsDialogue("Which staff would you like to switch your " + item.getDef().name + " for?",
				new Option("Mystic Air Staff", () -> {
					item.setId(BATTLESTAFFOFAIR);
				}),
				new Option("Mystic Water Staff", () -> {
					item.setId(MYSTICSTAFFOFWATER);
				}),
				new Option("Mystic Earth Staff", () -> {
					item.setId(MYSTICSTAFFOFEARTH);
				})
			));
		}));
		ItemObjectAction.register(MYSTICSTAFFOFWATER, CRYSTALOFPOWER, ((player, item, obj) -> {
			player.dialogue(new OptionsDialogue("Which staff would you like to switch your " + item.getDef().name + " for?",
				new Option("Mystic Fire Staff", () -> {
					item.setId(MYSTICSTAFFOFFIRE);
				}),
				new Option("Mystic Air Staff", () -> {
					item.setId(MYSTICSTAFFOFAIR);
				}),
				new Option("Mystic Earth Staff", () -> {
					item.setId(MYSTICSTAFFOFEARTH);
				})
			));
		}));

	}
}

package io.ruin.model.content.upgradesystem;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.AccessMasks;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.map.object.actions.ObjectAction;

public class UpgradeSystemInterface {
	final int INTERFACE_ID = 1110;
	UpgradeItems currentSection;

	public void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);
		sendCategories(player);
		sendItemSection(player, UpgradeItems.VALUES[0]);
	}

	private void sendCategories(Player player) {
		int startingParentComponent = 26;
		int startingNameComponent = 32;
		int startingContainerComponent = 31;

		for (int i = 26; i < 376; i += 7) {
			player.getPacketSender().setHidden(INTERFACE_ID, i, true);
		}
		int loops = 0;
		for (UpgradeItems section : UpgradeItems.VALUES) {
			var displayedText = "%s<br>%s".formatted(section.upgradeTo.getDef().name, section.getSuccessRate(player) + "% Base success");
			player.getPacketSender().setHidden(INTERFACE_ID, startingParentComponent, false);
			player.getPacketSender().sendString(INTERFACE_ID, startingNameComponent, displayedText);
			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					INTERFACE_ID << 16 | startingContainerComponent, 8000 + loops,
					4, 7, 1, -1, "", "", "", "", "");

			player.getPacketSender().sendItems(
					-1,
					startingContainerComponent,
					8000 + loops,
					section.upgradeTo);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, startingContainerComponent, 0, 27, 1024);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, startingContainerComponent, 0, 27, 1086);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, startingContainerComponent, 0, 5, AccessMasks.ClickOp1);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, startingContainerComponent, 0, 27, 1086);

			loops++;
			startingContainerComponent += 7;
			startingNameComponent += 7;
			startingParentComponent += 7;
		}
	}

	private void sendItemSection(Player player, UpgradeItems section) {
		// notify player of the success rate
		player.sendMessage(Color.CRIMSON, "Your success rate: " + section.getSuccessRate(player) + "%");
		currentSection = section;
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				INTERFACE_ID << 16 | 414, 7000,
				4, 7, 1, -1, "", "", "", "", "");
		player.getPacketSender().sendString(INTERFACE_ID, 415, section.description);
		player.getPacketSender().sendItems(
				-1,
				414,
				7000,
				section.upgradeTo);
		player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				INTERFACE_ID << 16 | 405, 7001,
				4, 7, 1, -1, "", "", "", "", "");

		player.getPacketSender().sendItems(
				-1,
				405,
				7001,
				section.bindingItem);
		player.getPacketSender().sendIfEvents(INTERFACE_ID, 414, 0, 27, 1024);
		player.getPacketSender().sendIfEvents(INTERFACE_ID, 414, 0, 27, 1086);
		player.getPacketSender().sendIfEvents(INTERFACE_ID, 414, 0, 5, AccessMasks.ClickOp1);
		player.getPacketSender().sendIfEvents(INTERFACE_ID, 414, 0, 27, 1086);
		player.getPacketSender().sendIfEvents(INTERFACE_ID, 405, 0, 27, 1024);
		player.getPacketSender().sendIfEvents(INTERFACE_ID, 405, 0, 27, 1086);
		player.getPacketSender().sendIfEvents(INTERFACE_ID, 405, 0, 5, AccessMasks.ClickOp1);
		player.getPacketSender().sendIfEvents(INTERFACE_ID, 405, 0, 27, 1086);

		int loops = 0;
		for (int i = 393; i < 402; i += 4) {

			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					INTERFACE_ID << 16 | i, 7002 + loops,
					4, 7, 1, -1, "", "", "", "", "");

			player.getPacketSender().sendItems(
					-1,
					i,
					7002 + loops,
					new Item(-1));
			player.getPacketSender().sendIfEvents(INTERFACE_ID, i, 0, 27, 1024);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, i, 0, 27, 1086);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, i, 0, 5, AccessMasks.ClickOp1);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, i, 0, 27, 1086);

			loops++;

		}
		loops = 0;
		for (var ingridient : section.ingredients) {
			var item = ingridient.variants()[0];

			player.getPacketSender().sendClientScript(
					149, "IviiiIsssss",
					INTERFACE_ID << 16 | 393 + (loops * 4), 7002 + loops,
					4, 7, 1, -1, "", "", "", "", "");

			player.getPacketSender().sendItems(
					-1,
					393 + (loops * 4),
					7002 + loops,
					item);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, 393 + (loops * 4), 0, 27, 1024);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, 393 + (loops * 4), 0, 27, 1086);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, 393 + (loops * 4), 0, 5, AccessMasks.ClickOp1);
			player.getPacketSender().sendIfEvents(INTERFACE_ID, 393 + (loops * 4), 0, 27, 1086);

			loops++;
		}
	}

	public static void register() {
		ObjectAction.register(46241, "operate", (player, obj) -> {
			player.getUpgradeSystemInterface().open(player);
		});
		InterfaceHandler.register(1110, h -> {
			h.actions[405] = (DefaultAction) (player, option, slot, itemId) -> {
				switch (option) {
					case 10:
						player.sendMessage("" + new Item(itemId).getDef().examine);
						break;
				}
			};
			h.actions[393] = (DefaultAction) (player, option, slot, itemId) -> {
				switch (option) {
					case 10:
						player.sendMessage("" + new Item(itemId).getDef().examine);
						break;
				}
			};
			h.actions[397] = (DefaultAction) (player, option, slot, itemId) -> {
				switch (option) {
					case 10:
						player.sendMessage("" + new Item(itemId).getDef().examine);
						break;
				}
			};
			h.actions[401] = (DefaultAction) (player, option, slot, itemId) -> {
				switch (option) {
					case 10:
						player.sendMessage("" + new Item(itemId).getDef().examine);
						break;
				}
			};
			h.actions[414] = (DefaultAction) (player, option, slot, itemId) -> {
				switch (option) {
					case 10:
						player.sendMessage("" + new Item(itemId).getDef().examine);
						break;
				}
			};
			for (int i = 31; i < 382; i++) {
				h.actions[i] = (DefaultAction) (player, option, slot, itemId) -> {
					switch (option) {
						case 10:
							player.sendMessage("" + new Item(itemId).getDef().examine);
							break;
					}
				};
			}
			int loops = 0;
			for (int i = 26; i < 377; i += 7) {
				int finalLoops = loops;
				h.actions[i] = (SimpleAction) p -> {
					if (finalLoops >= UpgradeItems.VALUES.length) {
						return;
					}
					p.getUpgradeSystemInterface().sendItemSection(p, UpgradeItems.VALUES[finalLoops]);
				};
				loops++;
			}
			h.actions[416] = (SimpleAction) p -> {
				var selectedItem = p.getUpgradeSystemInterface().currentSection;
				if (selectedItem.getSuccessRate(p) < 100) {
					p.dialogue(new YesNoDialogue(
						"Warning: There is a chance to fail!",
						("You have a %s%% chance of successfully upgrading this item." +
							"<br>Failure will result in loss of your `%s`." +
							"<br>Are you sure you want to proceed?")
							.formatted(
								selectedItem.getSuccessRate(p),
								Color.CANDY_PINK.wrap(selectedItem.bindingItem.getDef().name.toLowerCase())
							),
						selectedItem.upgradeTo,
						() -> selectedItem.createItem(p)
					));
				}
				else selectedItem.createItem(p);
			};

		});
	}
}

package io.ruin.model.item.actions.impl.scrolls;

import io.ruin.central.utility.CentralSaves;
import io.ruin.model.World;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.network.central.CentralSender;

public class NameChangeScroll {
	private static final int NAME_SCROLL = 10512;

	public static void register() {
		ItemAction.registerInventory(NAME_SCROLL, "read", (player, item) -> {
			player.dialogue(new OptionsDialogue("You're about to change your display name. Are you sure?",
				new Option("Yes, I'd like to change my name.", () -> {
					player.stringInput("Enter your new display name:", name -> {
						if (name.length() < 1 || name.length() > 12) {
							player.sendMessage("Your new display name must be between 1 and 12 characters.");
							return;
						}
						if (name.equalsIgnoreCase(player.getName())) {
							player.sendMessage("Your new display name must be different from your current one.");
							return;
						}
						if (CentralSaves.characterFileExists(name)) {
							player.sendMessage("That name is already taken.");
							return;
						}
						player.dialogue(
							new YesNoDialogue("Are you sure you want to change your name to " + name + "?", "You will be logged and need to login with your new name.", item, () -> {
								String oldCharFile = player.getName().toLowerCase().replaceAll(" ", "_");
								item.remove(1);
								player.nameChange = name;
								player.setName(name);
								player.sendMessage("Your display name has been changed to " + name + ".");
								player.forceLogout();
								World.startEvent(e -> {
									e.delay(1);
									CentralSaves.deleteCharacterFile(oldCharFile);
								});
							})
						);
					});
				}),
				new Option("I'd like to keep my name.")));
		});


	}
}

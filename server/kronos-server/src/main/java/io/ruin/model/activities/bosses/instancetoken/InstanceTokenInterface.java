package io.ruin.model.activities.bosses.instancetoken;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.network.incoming.handlers.CS2TickButtonHandler;
import io.ruin.model.inter.ToplevelComponent;

public class InstanceTokenInterface {
	private static final int INTERFACE_ID = 1093;
	public InstanceMaps selectedBoss;
	private int tokenCost = 0;
	private static final int TOKEN_ID = 7478;

	private String password;
	private boolean passwordSet = false;

	private boolean allowVisitors = false;

	public void open(Player player) {
		if (player.wildernessLevel > 0 && player.getPosition().getRegion().id != 12192) {
			player.sendMessage("You cannot create an instance in the wilderness.");
			return;
		}
		player.openInterface(ToplevelComponent.MAINMODAL, INTERFACE_ID);
		player.getPacketSender().sendString(INTERFACE_ID, 32, "Cerberus");
		updateTokensCost(player);
	}

	private void selectBoss(Player player, InstanceMaps selection) {
		if (tokenCost == 0)
			tokenCost++;
		selectedBoss = selection;
		updateTokensCost(player);
		player.getPacketSender().sendString(INTERFACE_ID, 37, selection.name);
	}

	private void toggleSetPassword(Player player) {
		if (!passwordSet)
			setPassword(player);
		else {
			passwordSet = false;
			player.getPacketSender().sendString(INTERFACE_ID, 16, "Set Instance Password");
		}
	}

	private void updateTokensCost(Player player) {
		player.getPacketSender().sendString(INTERFACE_ID, 35, tokenCost + " tokens");
	}

	private void setPassword(Player player) {
		player.stringInput("Enter a password for your instance", s -> {
			password = s;
			passwordSet = true;
			player.getPacketSender().sendString(INTERFACE_ID, 16, "Remove Password");
			player.sendMessage("Your password for the instance has been set to: " + password);
		});
	}

	private void toggleAllowVisitors(Player player) {
		allowVisitors = !allowVisitors;
		if (!allowVisitors) {
			tokenCost--;
			CS2TickButtonHandler.resetButton(player, INTERFACE_ID, 18);
		} else {
			tokenCost++;
			CS2TickButtonHandler.tickButton(player, INTERFACE_ID, 18);
		}
		updateTokensCost(player);
	}

	public void startInstance(Player player, boolean tokenSkip) {
		if (player.wildernessLevel > 0 && player.getPosition().getRegion().id != 12192) {
			player.sendMessage("You cannot create an instance in the wilderness.");
			return;
		}
		if (selectedBoss == null) {
			player.sendMessage("You must select a boss to instance before starting.");
			return;
		}
		if (player.teleportListener != null && !player.teleportListener.allow(player)) {
			return;
		}

		if (player.getInventory().contains(TOKEN_ID, tokenCost) || tokenSkip) {
			player.dialogue(
					new OptionsDialogue("Are you sure you want to create this instance?",
							new Option("Yes.", () -> {
								player.closeInterfaces();
								if (!tokenSkip) {
									player.getInventory().remove(TOKEN_ID, tokenCost);
								}
								player.sendMessage("Initializing instance...");
								InstanceManager.init(player);
								InstanceHandler instance = InstanceManager.getInstances().get(player.getName().toLowerCase());
								if (instance != null) {
									player.sendMessage("Building map for instance...");

									try {
										instance.buildMap(player, selectedBoss);
									} catch (DynamicMap.DynamicMapBuildException e) {
										player.sendMessage("Unable to build dynamic map.");
										return;
									}

									instance.setAllowVisitors(allowVisitors);
									if (passwordSet) {
										instance.mapHandler.setPassword(password);
									}
									player.sendMessage("Instance created successfully.");
								} else {
									player.sendMessage("Failed to create instance.");
								}
							}),
							new Option("No.", player::closeDialogue)));
		} else {
			player.sendMessage("You don't have enough tokens to start this instance.");
		}
	}

	public static void register() {
		InterfaceHandler.register(INTERFACE_ID, h -> {
			h.actions[18] = (SimpleAction) (player) -> player.getInstanceTokenInterface().toggleAllowVisitors(player);
			h.actions[20] = (SimpleAction) (player) -> player.getInstanceTokenInterface().startInstance(player, false);
			h.actions[16] = (SimpleAction) (player) -> player.getInstanceTokenInterface().toggleSetPassword(player);
			h.actions[22] = (SimpleAction) (player) -> player.getInstanceTokenInterface().selectBoss(player,
					InstanceMaps.BANDOS);
			h.actions[23] = (SimpleAction) (player) -> player.getInstanceTokenInterface().selectBoss(player,
					InstanceMaps.SARA);
			h.actions[24] = (SimpleAction) (player) -> player.getInstanceTokenInterface().selectBoss(player,
					InstanceMaps.ARMA);
			h.actions[25] = (SimpleAction) (player) -> player.getInstanceTokenInterface().selectBoss(player,
					InstanceMaps.ZAMMY);
			h.actions[26] = (SimpleAction) (player) -> player.getInstanceTokenInterface().selectBoss(player,
					InstanceMaps.NEX);
			h.actions[27] = (SimpleAction) (player) -> player.getInstanceTokenInterface().selectBoss(player,
					InstanceMaps.ARGENTAVIS);
			h.actions[28] = (SimpleAction) (player) -> player.getInstanceTokenInterface().selectBoss(player,
					InstanceMaps.MOLE);
			h.actions[29] = (SimpleAction) (player) -> player.getInstanceTokenInterface().selectBoss(player,
					InstanceMaps.CORP);
			h.actions[30] = (SimpleAction) (player) -> player.getInstanceTokenInterface().selectBoss(player, InstanceMaps.KQ);
			h.actions[31] = (SimpleAction) (player) -> player.getInstanceTokenInterface().selectBoss(player,
					InstanceMaps.DKS);
			h.actions[32] = (SimpleAction) (player) -> player.getInstanceTokenInterface().selectBoss(player,
					InstanceMaps.CERBY);
		});
	}
}

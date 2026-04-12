package io.ruin.model.activities.raids.toa;

import io.ruin.cache.Color;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;

import java.util.ArrayList;
import java.util.List;

public class ToaInvocationsInterface {
	boolean presetsActive = false;

	public void open(Player player) {
		if (TombsOfAmascutManager.getRaidParty(player) == null) {
			player.sendMessage("You must join or create a party first.");
			return;
		}
		player.openInterface(ToplevelComponent.MAINMODAL, 1117);
		updatePresets(player);
		updateInvocations(player);


	}

	public void updatePresets(Player player) {
		player.getPacketSender().setHidden(1117, 240, !presetsActive);
		player.getPacketSender().setHidden(1117, 242, !presetsActive);
		player.getPacketSender().setHidden(1117, 227, !presetsActive);
		int buttonTextComponentId = 231;
		for (int i = 1; i < 6; i++) {
			List<Invocations> favouritesList = getInvocationFavouritesList(player, i);
			if (favouritesList == null) {
				player.getPacketSender().sendString(1117, buttonTextComponentId, "Empty");
			} else {
				String raidLevelText;
				int invocationValue = 0;
				for (Invocations invocations : favouritesList) {
					invocationValue += invocations.invocationLevel;
				}
				if (invocationValue < 150)
					raidLevelText = "Entry";
				else if (invocationValue >= 150 && invocationValue < 300)
					raidLevelText = "Normal";
				else
					raidLevelText = "Expert";
				player.getPacketSender().sendString(1117, buttonTextComponentId, raidLevelText + " - " + invocationValue);
			}
			buttonTextComponentId += 2;
		}


	}

	public void selectInvocation(Player player, int componentId) {
		if (TombsOfAmascutManager.getRaidParty(player) == null) {
			player.sendMessage("You must join or create a party first.");
			player.closeInterface(ToplevelComponent.MAINMODAL);
			return;
		}
		if (TombsOfAmascutManager.getRaidParty(player).getLeader().equalsIgnoreCase(player.getName())) {
			int index = (componentId - 24) / 4;
			Invocations invocation = Invocations.values()[index];
			TombsOfAmascutManager.getRaidParty(player).toggleInvocation(invocation);
			updateInvocations(player);
		} else {
			player.sendMessage("Only the party leader can select invocations.");
		}
	}

	public void selectPreset(Player player, int index) {
		List<Invocations> favouritesList = getInvocationFavouritesList(player, index);
		if (favouritesList == null) {
			player.sendMessage("You do not have a preset saved at this index.");
			return;
		}
		if (TombsOfAmascutManager.getRaidParty(player) == null) {
			player.sendMessage("You must join or create a party first.");
			player.closeInterface(ToplevelComponent.MAINMODAL);
			return;
		}
		int invocationLevel = 0;
		for (Invocations invocations : favouritesList) {
			invocationLevel += invocations.invocationLevel;
		}
		if (TombsOfAmascutManager.getRaidParty(player).getLeader().equalsIgnoreCase(player.getName())) {
			if (invocationLevel < 150) {
				player.getPacketSender().setHidden(1117, 224, false);
				player.getPacketSender().setHidden(1117, 223, true);
				player.getPacketSender().setHidden(1117, 222, true);
			} else if (invocationLevel < 300) {
				player.getPacketSender().setHidden(1117, 223, false);
				player.getPacketSender().setHidden(1117, 224, true);
				player.getPacketSender().setHidden(1117, 222, true);
			} else {
				player.getPacketSender().setHidden(1117, 223, true);
				player.getPacketSender().setHidden(1117, 224, true);
				player.getPacketSender().setHidden(1117, 222, false);
			}
			presetToggledToLoad = favouritesList;
			player.getPacketSender().sendString(1117, 221, "" + invocationLevel);
			player.getPacketSender().sendString(1117, 226, "" + favouritesList.size());
		} else {
			player.sendMessage("Only the party leader can select a preset.");
		}
	}

	public void togglePresets(Player player) {
		presetsActive = !presetsActive;
		updatePresets(player);
	}

	List<Invocations> presetToggledToLoad;

	private void loadPreset(Player player) {
		if (presetToggledToLoad == null || presetToggledToLoad.isEmpty()) {
			player.sendMessage("Cannot load an empty preset.");
			return;
		}

		if (TombsOfAmascutManager.getRaidParty(player) == null) {
			player.sendMessage("You must join or create a party first.");
			player.closeInterface(ToplevelComponent.MAINMODAL);
			return;
		}

		if (TombsOfAmascutManager.getRaidParty(player).getLeader().equalsIgnoreCase(player.getName())) {
			TombsOfAmascutManager.getRaidParty(player).getActiveInvocations().clear();
			TombsOfAmascutManager.getRaidParty(player).setCurrentInvocationValue(0);
			for (Invocations invocations : presetToggledToLoad) {
				TombsOfAmascutManager.getRaidParty(player).addInvocation(invocations);
			}
			updateInvocations(player);
		} else {
			player.sendMessage("Only the party leader can load a preset.");
		}
	}

	public void refresh(Player player) {
		updateInvocations(player);
		updatePresets(player);
		player.getPacketSender().sendString(1117, 208, "Members (" + TombsOfAmascutManager.getRaidParty(player).getMembers().size() + ")");
	}

	public void clearAll(Player player) {
		if (TombsOfAmascutManager.getRaidParty(player) == null) {
			player.sendMessage("You must join or create a party first.");
			player.closeInterface(ToplevelComponent.MAINMODAL);
			return;
		}
		if (TombsOfAmascutManager.getRaidParty(player).getLeader().equalsIgnoreCase(player.getName())) {
			TombsOfAmascutManager.getRaidParty(player).getActiveInvocations().clear();
			TombsOfAmascutManager.getRaidParty(player).setCurrentInvocationValue(0);
			updateInvocations(player);
		} else {
			player.sendMessage("Only the party leader can clear all invocations.");
		}
	}

	private boolean hasFavouritesFree(Player player) {
		for (int i = 1; i < 6; i++) {
			if (player.toaInvocationFavourites.get(i) == null || player.toaInvocationFavourites.get(i).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private int getFreeFavouriteIndex(Player player) {
		for (int i = 1; i < 6; i++) {
			if (player.toaInvocationFavourites.get(i) == null || player.toaInvocationFavourites.get(i).isEmpty()) {
				return i;
			}
		}
		return -1;
	}

	public void saveInvocationLayoutToFavourites(Player player) {
		List<Invocations> invocationsToSave = TombsOfAmascutManager.getRaidParty(player).getActiveInvocations();
		if (!hasFavouritesFree(player)) {
			player.sendMessage("You can only save up to 5 presets.");
			return;
		}
		int index = getFreeFavouriteIndex(player);
		List<Integer> invocationOrdinals = new ArrayList<>();
		for (Invocations invocations : invocationsToSave) {
			invocationOrdinals.add(invocations.ordinal());
		}
		player.toaInvocationFavourites.put(index, invocationOrdinals);
		player.sendMessage("Saved preset " + index + ".");
		updatePresets(player);

	}

	public List<Invocations> getInvocationFavouritesList(Player player, int index) {
		if (player.toaInvocationFavourites.get(index) == null) {
			return null;
		}
		List<Invocations> favouritesList = new ArrayList<>();
		List<Integer> invocationOrdinals = player.toaInvocationFavourites.get(index);
		if (invocationOrdinals != null) {
			for (Integer ordinal : invocationOrdinals) {
				favouritesList.add(Invocations.values()[ordinal]);
			}
		}
		return favouritesList;
	}

	private void removePreset(Player player, int index) {
		if (player.toaInvocationFavourites.get(index) == null) {
			player.sendMessage("You do not have a preset saved at this index.");
			return;
		}
		player.toaInvocationFavourites.remove(index);
		player.sendMessage("Removed preset " + index + ".");
		updatePresets(player);
	}

	public void updateInvocations(Player player) {
		if (TombsOfAmascutManager.getRaidParty(player) == null) {
			player.sendMessage("You must join or create a party first.");
			player.closeInterface(ToplevelComponent.MAINMODAL);
			return;
		}
		int startingInvocationParentComponentId = 24;
		for (Invocations invocation : Invocations.values()) {
			boolean hasInvocation = TombsOfAmascutManager.getRaidParty(player).getActiveInvocations().contains(invocation);
			int onComponentId = startingInvocationParentComponentId + 2;
			int nameComponentId = startingInvocationParentComponentId + 3;
			player.getPacketSender().setHidden(1117, onComponentId, !hasInvocation);
			if (hasInvocation) {
				player.getPacketSender().sendString(1117, nameComponentId, "<col=e6b34d>" + invocation.invocationName);
			} else {
				player.getPacketSender().sendString(1117, nameComponentId, "<col=999999>" + invocation.invocationName);
			}

			startingInvocationParentComponentId += 4;

		}
		String invocationText;
		if (TombsOfAmascutManager.getRaidParty(player).getCurrentInvocationValue() < 150) {
			invocationText = "Entry";
			player.getPacketSender().setHidden(1117, 224, false);
			player.getPacketSender().setHidden(1117, 223, true);
			player.getPacketSender().setHidden(1117, 222, true);
		} else if (TombsOfAmascutManager.getRaidParty(player).getCurrentInvocationValue() >= 150 && TombsOfAmascutManager.getRaidParty(player).getCurrentInvocationValue() < 300) {
			invocationText = "Normal";
			player.getPacketSender().setHidden(1117, 223, false);
			player.getPacketSender().setHidden(1117, 224, true);
			player.getPacketSender().setHidden(1117, 222, true);
		} else {
			invocationText = "Expert";
			player.getPacketSender().setHidden(1117, 223, true);
			player.getPacketSender().setHidden(1117, 224, true);
			player.getPacketSender().setHidden(1117, 222, false);
		}
		player.getPacketSender().sendString(1117, 213, invocationText);
		player.getPacketSender().sendString(1117, 215, TombsOfAmascutManager.getRaidParty(player).getCurrentInvocationValue() + " <col=ffffff>(" + TombsOfAmascutManager.getRaidParty(player).getActiveInvocations().size() + ")");
		player.getPacketSender().sendString(1117, 221, "" + TombsOfAmascutManager.getRaidParty(player).getCurrentInvocationValue());
		player.getPacketSender().sendString(1117, 226, "" + TombsOfAmascutManager.getRaidParty(player).getActiveInvocations().size());
		player.getPacketSender().sendString(1117, 14, "Party of " + TombsOfAmascutManager.getRaidParty(player).getLeader());

	}

	public static void register() {
		InterfaceHandler.register(1117, h -> {
			for (int i = 24; i <= 192; i += 4) {
				int finalI = i;
				h.actions[i] = (SimpleAction) p -> {
					p.getToaInvocationsInterface().selectInvocation(p, finalI);
				};
			}
			for (int i = 244; i < 249; i++) {
				int finalI = i;
				h.actions[i] = (SimpleAction) p -> {
					p.getToaInvocationsInterface().removePreset(p, finalI - 243);
				};
			}
			h.actions[202] = (SimpleAction) p -> {
				p.getToaInvocationsInterface().clearAll(p);
			};
			h.actions[199] = (SimpleAction) p -> {
				p.getToaInvocationsInterface().togglePresets(p);
			};
			h.actions[197] = (SimpleAction) p -> {
				p.getToaInvocationsInterface().refresh(p);
			};
			h.actions[204] = (SimpleAction) p -> {
				p.getMembersInterface().open(p);
			};
			h.actions[205] = (SimpleAction) p -> {
				p.getToaApplicantsInterface().open(p);
			};
			h.actions[230] = (SimpleAction) p -> {
				p.getToaInvocationsInterface().selectPreset(p, 1);
			};
			h.actions[232] = (SimpleAction) p -> {
				p.getToaInvocationsInterface().selectPreset(p, 2);
			};
			h.actions[234] = (SimpleAction) p -> {
				p.getToaInvocationsInterface().selectPreset(p, 3);
			};
			h.actions[236] = (SimpleAction) p -> {
				p.getToaInvocationsInterface().selectPreset(p, 4);
			};
			h.actions[238] = (SimpleAction) p -> {
				p.getToaInvocationsInterface().selectPreset(p, 5);
			};
			h.actions[242] = (SimpleAction) p -> {
				p.getToaInvocationsInterface().saveInvocationLayoutToFavourites(p);
			};
			h.actions[240] = (SimpleAction) p -> {
				p.getToaInvocationsInterface().loadPreset(p);
			};
		});
	}
}

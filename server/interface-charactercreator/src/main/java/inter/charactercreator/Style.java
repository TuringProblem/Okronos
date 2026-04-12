package inter.charactercreator;

import io.ruin.cache.IdentityKit;
import io.ruin.model.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public enum Style {
	HAIR(0, 15, 16),
	JAW(1, 20, 19),
	TORSO(2, 24, 23),
	ARMS(3, 28, 27),
	HANDS(4, 32, 31),
	LEGS(5, 36, 35),
	FEET(6, 40, 39);

	@SuppressWarnings("unchecked")
	private static final List<IdentityKit>[] bodyParts = new ArrayList[14];

	static {
		for (int i = 0; i < bodyParts.length; i++) {
			bodyParts[i] = new ArrayList<>();
		}
		for (IdentityKit kit : IdentityKit.LOADED) {
			if (!kit.nonSelectable) {
				bodyParts[kit.bodyPartId].add(kit);
			}
		}
	}

	public static final Style[] values = values();

	public static void updateAll(Player player) {
		boolean male = player.getAppearance().isMale();
		for (Style value : values) {
			if (value.isAvailable(male)) {
				Integer index = value.selectedAvailableIdentityKitIndex(player);
				if (index == null)
					index = 0;
				value.updateAppearance(player, index);
			} else {
				value.markAppearanceEmpty(player);
			}
		}
	}

	public final int appearanceIndex;

	public final int childNext;

	public final int childPrevious;

	Style(int appearanceIndex, int childNext, int childPrevious) {
		this.appearanceIndex = appearanceIndex;
		this.childNext = childNext;
		this.childPrevious = childPrevious;
	}

	public int bodyPart(boolean male) {
		return male ? this.appearanceIndex : 7 + this.appearanceIndex;
	}

	public void change(Player player, int increment) {
		var currentIdentityKitIndex = this.selectedAvailableIdentityKitIndex(player);
		if (currentIdentityKitIndex == null) {
			return;
		}

		var male = player.getAppearance().isMale();
		var availableIdentityKits = this.availableIdentityKits(male);

		int nextIdentityKitIndex = currentIdentityKitIndex + increment;
		if (nextIdentityKitIndex >= availableIdentityKits.size() - 1) {
			nextIdentityKitIndex = 0;
		} else if (nextIdentityKitIndex < 0) {
			nextIdentityKitIndex = availableIdentityKits.size() - 1;
		}

		updateAppearance(player, nextIdentityKitIndex);
		player.getAppearance().update();
	}

	public void updateAppearance(Player player, int identityKitIndex) {
		var male = player.getAppearance().isMale();
		var available = this.availableIdentityKits(male);
		player.getAppearance().styles[appearanceIndex] = available.get(identityKitIndex).id;
	}

	public void markAppearanceEmpty(Player player) {
		player.getAppearance().styles[appearanceIndex] = 255;
	}

	public Integer selectedAvailableIdentityKitIndex(Player player) {
		boolean male = player.getAppearance().isMale();
		if (!this.isAvailable(male)) {
			return null;
		}

		List<IdentityKit> availableIdentityKits = this.availableIdentityKits(male);
		if (availableIdentityKits.isEmpty()) {
			return null;
		}

		int currentIdentityKitID = player.getAppearance().styles[appearanceIndex];
		int indexOfFirst = -1;
		for (int i = 0; i < availableIdentityKits.size(); i++) {
			if (availableIdentityKits.get(i).id == currentIdentityKitID) {
				indexOfFirst = i;
				break;
			}
		}
		return indexOfFirst == -1 ? null : indexOfFirst;
	}

	public List<IdentityKit> availableIdentityKits(boolean male) {
		var bodyPart = this.bodyPart(male);
		return this.allAvailableKits(bodyPart);
	}

	public List<IdentityKit> allAvailableKits(int bodyPart) {
		return new ArrayList<>(bodyParts[bodyPart]);
	}

	public boolean isAvailable(boolean male) {
		return true;
	}

	public void next(Player player) {
		change(player, 1);
	}

	public void previous(Player player) {
		change(player, -1);
	}
}

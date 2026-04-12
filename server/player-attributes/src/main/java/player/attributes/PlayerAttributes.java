package player.attributes;

import java.util.ArrayList;
import java.util.List;

/**
 * PlayerAttributes
 */
public class PlayerAttributes {
	private final List<PlayerAttribute<?>> attributes = new ArrayList<>();

	public <T> int register() {
		var index = 0;
		var freeIndex = -1;
		for (var attribute : this.attributes) {
			if (attribute == null) {
				freeIndex = index;
				break;
			}
			index += 1;
		}
		if (freeIndex == -1) {
			this.attributes.add(new PlayerAttribute<T>());
			return this.attributes.size() - 1;
		}
		this.attributes.set(freeIndex, new PlayerAttribute<T>());
		return freeIndex;

	}

	public PlayerAttribute<?> get(int key) {
		return this.attributes.get(key);
	}

	public void remove(int key) {
		this.attributes.set(key, null);
	}

	public void unload(int index) {
		for (var attribute : this.attributes) {
			if (attribute == null) {
				continue;
			}
			attribute.set(index, null);
		}
	}
}

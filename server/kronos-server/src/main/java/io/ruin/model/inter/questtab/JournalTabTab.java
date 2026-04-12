package io.ruin.model.inter.questtab;

import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum JournalTabTab {
	SUMMARY(Interface.QUEST),
	QUEST(Interface.SERVER_JOURNAL),
	ACHIEVEMENT(Interface.ACHIEVEMENT),
	ACTIVITIES(Interface.ACTIVITIES),
	MISCELLANEOUS(Interface.MISCELLANEOUS);

	private final int id;

	JournalTabTab(int id) {
		this.id = id;
	}

	/**
	 * Returns the components of the specified {@link JournalTabTab}.
	 *
	 * @return A list of components.
	 */
	public List<JournalTabComponent> getComponents() {
		List<JournalTabComponent> list = new ArrayList<>();

		for (JournalTabComponent component : JournalTabComponent.VALUES) {
			if (!component.getTab().equals(this)) {
				continue;
			}

			list.add(component);
		}

		return list;
	}

	public static void init() {
		for (JournalTabTab tab : values()) {
			Map<Integer, InterfaceAction> actions = new HashMap<>();

			for (JournalTabComponent c : tab.getComponents()) {
				if (c.getAction() == null) {
					continue;
				}

				actions.put(c.getComponentId(), c.getAction());
			}

			if (actions.isEmpty()) {
				continue;
			}

			InterfaceHandler.register(tab.getId(), interfaceHandler -> {
				for (int component : actions.keySet()) {
					interfaceHandler.actions[component] = actions.get(component);
				}
			});
		}
	}
}

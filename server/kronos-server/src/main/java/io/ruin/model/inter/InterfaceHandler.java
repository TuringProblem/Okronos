package io.ruin.model.inter;

import com.google.common.collect.Maps;
import io.ruin.cache.InterfaceDef;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class InterfaceHandler {

	public static final InterfaceHandler[] HANDLERS = new InterfaceHandler[InterfaceDef.COUNTS == null ? 712
		: InterfaceDef.COUNTS.length];

	public static final InterfaceHandler EMPTY_HANDLER = new InterfaceHandler();

	public final Map<Integer, InterfaceAction> widgetActions = Maps.newConcurrentMap();

	public final int id;

	public final InterfaceAction[] actions;

	public BiConsumer<Player, Integer> closedAction;
	public Consumer<Player> openAction;

	public void action(int childID, InterfaceAction action) {
		actions[childID] = action;
	}

	public void action(WidgetInfo widgetInfo, InterfaceAction action) {
		action(widgetInfo.getChildId(), action);
	}

	public void simpleAction(int childID, SimpleAction action) {
		action(childID, action);
	}

	public void simpleAction(WidgetInfo widgetInfo, SimpleAction action) {
		simpleAction(widgetInfo.getChildId(), action);
	}

	public InterfaceHandler() {
		this.id = -1;
		this.actions = new InterfaceAction[0];
	}

	protected InterfaceHandler(int id) {
		this.id = id;
		this.actions = new InterfaceAction[InterfaceDef.COUNTS[id]];
	}

	public static void register(int root, int component, SimpleAction action) {
		register(root, (h) -> {
			h.actions[component] = action;
		});
	}

	public static void register(int root, int component, DefaultAction action) {
		register(root, (h) -> {
			h.actions[component] = action;
		});
	}

	public static void register(int interfaceId, Consumer<InterfaceHandler> consumer) {
		var handler = HANDLERS[interfaceId] != null ? HANDLERS[interfaceId] : new InterfaceHandler(interfaceId);
		HANDLERS[interfaceId] = handler;
		consumer.accept(HANDLERS[interfaceId]);
	}

	public static InterfaceAction getAction(Player player, int interfaceHash) {
		int interfaceId = interfaceHash >> 16;
		int childId = interfaceHash & 0xffff;
		if (childId == 65535)
			childId = -1;
		return getAction(player, interfaceId, childId);
	}

	public static InterfaceAction getAction(Player player, int interfaceId, int childId) {
		if (interfaceId < 0 || interfaceId >= HANDLERS.length) {
			return null;
		}
		if (!player.isVisibleInterface(interfaceId)) {
			return null;
		}
		InterfaceHandler handler = HANDLERS[interfaceId];
		if (handler == null) {
			return null;
		}
		if (childId < 0 || childId >= handler.actions.length) {
			return null;
		}
		return handler.actions[childId];
	}

	public void registerButton(int buttonId, InterfaceAction action) {
		if (action == null)
			return;
		if (widgetActions.containsKey(buttonId)) {

		}
		widgetActions.put(buttonId, action);
	}
}

package io.ruin.model.inter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InterfaceEventMask {
	Continue(1 << 0),
	ClickOp1(1 << 1),
	ClickOp2(1 << 2),
	ClickOp3(1 << 3),
	ClickOp4(1 << 4),
	ClickOp5(1 << 5),
	ClickOp6(1 << 6),
	ClickOp7(1 << 7),
	ClickOp8(1 << 8),
	ClickOp9(1 << 9),
	ClickOp10(1 << 10),
	UseOnGroundItem(1 << 11),
	UseOnNpc(1 << 12),
	UseOnObject(1 << 13),
	UseOnPlayer(1 << 14),
	UseOnInventory(1 << 15),
	UseOnComponent(1 << 16),
	DragDepth1(1 << 17),
	DragDepth2(2 << 17),
	DragDepth3(3 << 17),
	DragDepth4(4 << 17),
	DragDepth5(5 << 17),
	DragDepth6(6 << 17),
	DragDepth7(7 << 17),
	DragTargetable(1 << 20),
	ComponentTargetable(1 << 21);

	private final int value;

	//(depth & 7) << 17
	public static int getMask(InterfaceEventMask... masks) {
		int val = 0;
		for (InterfaceEventMask mask : masks) {
			val |= mask.value;
		}
		return val;
	}

	private static String sanitize(final int packedEvents) {
		final StringBuilder events = new StringBuilder();
		for (InterfaceEventMask event : values()) {
			if ((packedEvents & event.value) != event.value) continue;
			events.append(event).append(", ");
		}
		if (events.length() >= 2) {
			events.delete(events.length() - 2, events.length());
		}
		return events.toString();
	}
}

package io.ruin.model.inter;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum AccessMasks implements AccessMask {

	None(0),
	Continue(1),
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

	private final int mask;

	AccessMasks(int mask) {
		this.mask = mask;
	}

	@Override
	public int mask() {
		return this.mask;
	}

	public static java.util.List<AccessMasks> determine(int mask) {
		return Arrays.stream(AccessMasks.values())
			.filter(value -> (mask & value.getMask()) != 0)
			.collect(Collectors.toList());
	}

	public static String determineString(int mask) {
		return determineString(mask, AccessMasks.class.getSimpleName() + '.', ", ");
	}

	public static String determineString(int mask, String prefix, String separator) {
		java.util.List<AccessMasks> determined = determine(mask);
		List<String> names = determined.stream()
			.map(accessMask -> prefix + accessMask.name())
			.collect(Collectors.toList());
		return String.join(separator, names);
	}

	public static int combine(int... masks) {
		return Arrays.stream(masks).sum();
	}

	public static int combine(AccessMask... masks) {
		return Arrays.stream(masks)
			.mapToInt(AccessMask::mask)
			.sum();
	}
}

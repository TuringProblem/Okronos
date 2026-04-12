package io.ruin.model.inter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ToplevelInterfaceType {
	FULLSCREEN_INTERFACE(165, 1132, false),
	FIXED(548, 1129, false),
	RESIZABLE_CLASSIC(161, 1130, true),
	RESIZABLE_MODERN(164, 1131, true),
	MOBILE(601, 1745, true),
	ORB_OF_OCULUS(16, -1, true),
	TOUR(80, 139, true);

	@Getter
	private final int interfaceId;
	@Getter
	private final int topLevelEnum;
	@Getter
	private final boolean resizable;
}

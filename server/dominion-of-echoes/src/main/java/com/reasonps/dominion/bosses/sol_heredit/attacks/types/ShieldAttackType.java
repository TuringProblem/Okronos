package com.reasonps.dominion.bosses.sol_heredit.attacks.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-05-29
 */
@Getter
@RequiredArgsConstructor
public enum ShieldAttackType {
	PRIMARY(5),
	SECONDARY(4);
	private final int power;
}

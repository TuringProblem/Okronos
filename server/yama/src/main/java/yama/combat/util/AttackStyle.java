package yama.combat.util;


import yama.combat.attacks.AutoAttack;
import yama.combat.attacks.MagicAttack;
import yama.combat.attacks.RangeAttack;

public enum AttackStyle {
	RANGED(new RangeAttack()),
	MAGIC(new MagicAttack())
	;
	private final AutoAttack autoAttack;

	AttackStyle(AutoAttack autoAttack) {
		this.autoAttack = autoAttack;
	}

	public AutoAttack getAutoAttack() {
		return autoAttack;
	}
}
package io.ruin.model.skills.agility.shortcut;

import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.StatType;
import io.ruin.utility.PredicateCheck;

public enum JumpShortcut {
	SHEEP_JUMP1(1, 1, Position.of(3197, 3278), Position.of(3197, 3275)),

	SDZ(85, 1, Position.of(1981, 8998, 1), Position.of(1981, 8994, 1)),

	TROLL_W_JUMP1(1, 1, Position.of(2833, 3627), Position.of(2833, 3629)),

	TROLL_E_JUMP1(1, 1, Position.of(2834, 3627), Position.of(2834, 3629)),

	TROLL_FAR_W_JUMP1(1, 1, Position.of(2820, 3635), Position.of(2822, 3635)),

	CABBAGE_JUMP1(1, 1, Position.of(3063, 3281), Position.of(3063, 3284)),

	ARDY_JUMP1(1, 1, Position.of(2649, 3375), Position.of(2646, 3375)),

	BURGH_JUMP1(1, 1, Position.of(3473, 3221), Position.of(3474, 3221)),

	FALADOR_JUMP5(5, 1, Position.of(2936, 3355), Position.of(2934, 3355)),

	CORSAIR_JUMP10(10, 1, Position.of(2546, 2873), Position.of(2546, 2871)),

	VARROCK_JUMP13(13, 1, Position.of(3240, 3334), Position.of(3240, 3335)),

	RELEKKE_JUMP(57, 1, Position.of(2688, 3697), Position.of(2691, 3697)),

	TROLL_S_JUMP44(44, 1, Position.of(2911, 3686), Position.of(2909, 3686)),

	TROLL_N_JUMP44(44, 1, Position.of(2911, 3687), Position.of(2909, 3687)),

	ZEAH_JUMP49(49, 1, Position.of(1776, 3884), Position.of(1776, 3882)),

	ZEAH_JUMP69(69, 1, Position.of(1761, 3874), Position.of(1761, 3872)),

	LUMBERYARD(1, 1, Position.of(3308, 3491), Position.of(3308, 3493));

	private JumpShortcut() {

	}

	private JumpShortcut(int levelReq, int xp, Position startPosition, Position endPosition) {
		this.xp = xp;
		this.levelReq = levelReq;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}

	private JumpShortcut(PredicateCheck canUse, int levelReq, int xp, Position startPosition, Position endPosition) {
		this.canUse = canUse;
		this.xp = xp;
		this.levelReq = levelReq;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}

	private Position startPosition, endPosition;
	private int xp;
	private int levelReq;


	private PredicateCheck canUse;

	public void traverse(Player p, GameObject obj) {
		if (canUse != null) {
			if (!canUse.check(p))
				return;
		}
		if (!p.getStats().check(StatType.Agility, levelReq, "attempt this"))
			return;
		if (p.getPosition() != startPosition) {

		}
		p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			p.animate(839);
			Position pos = p.getPosition().equals(startPosition) ? endPosition : startPosition;
			int xDiff = pos.getX() - p.getPosition().getX();
			int yDiff = pos.getY() - p.getPosition().getY();

			p.getMovement().force(xDiff, yDiff, 0, 0, 5, 80, Direction.getDirection(p.getPosition(), pos));
			e.delay(2);
			p.getStats().addXp(StatType.Agility, xp, true);
			p.unlock();
		});
	}

}

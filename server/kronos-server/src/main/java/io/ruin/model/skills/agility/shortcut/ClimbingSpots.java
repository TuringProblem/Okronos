package io.ruin.model.skills.agility.shortcut;

import com.google.common.collect.Lists;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.stat.StatType;
import io.ruin.utility.PredicateCheck;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ClimbingSpots {

	CAIRN_S_CLIMB1(Direction.WEST, 1, 1,
		Position.of(2795, 2978), Position.of(2791, 2978),
		Position.of(2794, 2978), Position.of(2792, 2978)),

	CAIRN_M_CLIMB1(Direction.WEST, 1, 1,
		Position.of(2795, 2979), Position.of(2791, 2979),
		Position.of(2794, 2979), Position.of(2792, 2979)),

	CAIRN_N_CLIMB1(Direction.WEST, 1, 1,
		Position.of(2795, 2980), Position.of(2791, 2980),
		Position.of(2794, 2980), Position.of(2792, 2980)),

	EAGLES_TOP_E_CLIMB25(Direction.SOUTH, 25, 1,
		Position.of(2324, 3497), Position.of(2322, 3502),
		Position.of(2324, 3498), Position.of(2323, 3500), Position.of(2322, 3501)),

	EAGLES_TOP_W_CLIMB25(Direction.SOUTH, 25, 1,
		Position.of(2323, 3496), Position.of(2322, 3502),
		Position.of(2323, 3497), Position.of(2323, 3500), Position.of(2322, 3501)),

	GNOME_CLIMB37(Direction.SOUTH, 37, 1,
		Position.of(2489, 3521), Position.of(2486, 3515),
		Position.of(2489, 3520), Position.of(2489, 3517), Position.of(2487, 3515)),

	AL_KHARID_CLIMB38(Direction.EAST, 38, 1,
		Position.of(3302, 3315), Position.of(3306, 3315),
		Position.of(3303, 3315), Position.of(3305, 3315)),

	TROLL_CLIMB41(Direction.EAST, 41, 1,
		Position.of(2869, 3671), Position.of(2872, 3671),
		Position.of(2870, 3671), Position.of(2871, 3671)),

	TROLL_1_CLIMB43(Direction.NORTH, 43, 1,
		Position.of(2878, 3665), Position.of(2878, 3668),
		Position.of(2878, 3666), Position.of(2878, 3667)),

	TROLL_2_CLIMB43(Direction.NORTH, 43, 1,
		Position.of(2887, 3660), Position.of(2887, 3662),
		Position.of(2887, 3661)),

	TROLL_3_CLIMB43(Direction.NORTH, 43, 1,
		Position.of(2888, 3660), Position.of(2888, 3662),
		Position.of(2888, 3661)),

	TROLL_4_CLIMB43(Direction.EAST, 43, 1,
		Position.of(2884, 3684), Position.of(2886, 3684),
		Position.of(2885, 3684)),

	TROLL_5_CLIMB43(Direction.EAST, 43, 1,
		Position.of(2884, 3683), Position.of(2886, 3683),
		Position.of(2885, 3683)),

	TROLL_W_CLIMB44(Direction.WEST, 44, 1,
		Position.of(2907, 3682), Position.of(2909, 3682),
		Position.of(2908, 3682)),

	TROLL_S_CLIMB44(Direction.SOUTH, 44, 1,
		Position.of(2909, 3682), Position.of(2909, 3684),
		Position.of(2909, 3683)),

	TROLL_CLIMB47(Direction.WEST, 47, 1,
		Position.of(2903, 3680), Position.of(2900, 3680),
		Position.of(2902, 3680), Position.of(2901, 3680)),

	TROLL_HERB_CLIMB73(Direction.WEST, 73, 1,
		Position.of(2844, 3693), Position.of(2838, 3693),
		Position.of(2842, 3693), Position.of(2840, 3693)),

	ARANDAR_CLIMB59(Direction.NORTH, 59, 1,
		Position.of(2344, 3294), Position.of(2346, 3300),
		Position.of(2344, 3295), Position.of(2345, 3297), Position.of(2346, 3299)),

	ARANDAR_CLIMB68(Direction.SOUTH, 68, 1,
		Position.of(2338, 3286), Position.of(2338, 3281),
		Position.of(2338, 3285), Position.of(2338, 3282)),

	ARANDAR_CLIMB85(Direction.WEST, 85, 1,
		Position.of(2338, 3253), Position.of(2332, 3252),
		Position.of(2337, 3253), Position.of(2335, 3253), Position.of(2333, 3252));

	private ClimbingSpots() {

	}

	private ClimbingSpots(Direction faceDir, int levelReq, int xp, Position startPosition, Position endPosition, Position... steps) {
		this.faceDir = faceDir;
		this.xp = xp;
		this.levelReq = levelReq;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.positions = Arrays.asList(steps);
	}

	private ClimbingSpots(PredicateCheck canUse, Direction faceDir, int levelReq, int xp, Position startPosition, Position endPosition, Position... steps) {
		this.faceDir = faceDir;
		this.canUse = canUse;
		this.xp = xp;
		this.levelReq = levelReq;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.positions = Arrays.asList(steps);
	}

	private List<Position> positions;
	private Position startPosition, endPosition;
	private int xp;
	private int levelReq;

	private Direction faceDir;

	private PredicateCheck canUse;

	public void traverse(Player p, GameObject obj) {
		if (canUse != null) {
			if (!canUse.check(p))
				return;
		}
		if (!p.getStats().check(StatType.Agility, levelReq, " to use this")) {
			return;
		}
		p.startEvent(e -> {
			p.lock(LockType.FULL_DELAY_DAMAGE);
			List<Position> posCopy = Lists.newArrayList(positions);

			Position first = posCopy.get(0);
			if (posCopy.size() == 1) {
				if (startPosition.equals(p.getPosition())) {
					posCopy.add(endPosition);
				} else {
					posCopy.add(startPosition);
				}
			} else if (p.getPosition().equals(first)) {
				posCopy.add(endPosition);
				p.sendMessage("Not reverse");
			} else {
				Collections.reverse(posCopy);
				posCopy.add(startPosition);
				p.sendMessage("Reverse");
			}


			for (int index = 0; index < posCopy.size(); index++) {
				Position pos = posCopy.get(index);
				int xDiff = pos.getX() - p.getPosition().getX();
				int yDiff = pos.getY() - p.getPosition().getY();
				p.animate(740);
				int speed = index == 0 || index == posCopy.size() - 1 ? 35 : 80;
				p.getMovement().force(xDiff, yDiff, 0, 0, 5, speed, faceDir);
				if (index != posCopy.size() - 1) e.delay(Math.max(Math.abs(xDiff), Math.abs(yDiff)));
				//else e.delay(1);
			}

			if (World.isEco())
				p.getStats().addXp(StatType.Agility, xp, true);
			p.unlock();
			p.resetAnimation();
		});

	}
}

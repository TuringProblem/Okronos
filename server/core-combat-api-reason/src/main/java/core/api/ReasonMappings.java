package core.api;

import static core.api.API.*;

/**
 * Various mappinngs between core-internal classes <-> generic api
 */
public interface ReasonMappings {
	static Coord into(io.ruin.model.map.Position reasonPos) {
		return new Coord(reasonPos.x(), reasonPos.y(), reasonPos.z());
	}

	static io.ruin.model.map.Position into(Coord pos) {
		return new io.ruin.model.map.Position(pos.x(), pos.y(), pos.z());
	}

	static io.ruin.model.entity.npc.NPC into(NPC npc) {
		return npc.ref;
	}

	static io.ruin.model.entity.player.Player into(Player player) {
		return player.ref();
	}

	static Player into(io.ruin.model.entity.player.Player player) {
		return new Player(player);
	}

	static Direction into(io.ruin.model.map.Direction direction) {
		switch (direction) {
			case EAST -> {
				return Direction.East;
			}
			case NORTH -> {
				return Direction.North;
			}
			case NORTH_EAST -> {
				return Direction.NorthEast;
			}
			case NORTH_WEST -> {
				return Direction.NorthWest;
			}
			case SOUTH -> {
				return Direction.South;
			}
			case SOUTH_EAST -> {
				return Direction.SouthEast;
			}
			case SOUTH_WEST -> {
				return Direction.SouthWest;
			}
			case WEST -> {
				return Direction.West;
			}

			default -> {
				return null;
			}
		}
	}

	static io.ruin.model.map.Direction into(Direction direction) {
		switch (direction) {
			case East -> {
				return io.ruin.model.map.Direction.EAST;
			}
			case North -> {
				return io.ruin.model.map.Direction.NORTH;
			}
			case NorthEast -> {
				return io.ruin.model.map.Direction.NORTH_EAST;
			}
			case NorthWest -> {
				return io.ruin.model.map.Direction.NORTH_WEST;
			}
			case South -> {
				return io.ruin.model.map.Direction.SOUTH;
			}
			case SouthEast -> {
				return io.ruin.model.map.Direction.SOUTH_EAST;
			}
			case SouthWest -> {
				return io.ruin.model.map.Direction.SOUTH_WEST;
			}
			case West -> {
				return io.ruin.model.map.Direction.WEST;
			}

			default -> {
				return null;
			}
		}
	}

	public static HookResult into(io.ruin.HooksV2.Result from) {
		switch (from) {
			case Break:
				return Break;
			case Pass:
				return Pass;
			case Return:
				return Return;
			default:
				return Pass;
		}
	}

	public static io.ruin.HooksV2.Result into(HookResult from) {
		switch (from) {
			case Break:
				return io.ruin.HooksV2.Result.Break;
			case Pass:
				return io.ruin.HooksV2.Result.Pass;
			case Return:
				return io.ruin.HooksV2.Result.Return;
			default:
				return io.ruin.HooksV2.Result.Pass;
		}
	}
}

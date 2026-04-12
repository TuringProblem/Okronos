package io.ruin.model.map;

import io.ruin.Server;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.utility.Misc;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Projectile {

	private int gfxId;

	private int startHeight, endHeight;

	private int delay;

	private int durationStart, durationIncrement;

	private int curve;

	private int idk;

	private boolean skipTravel;

	private boolean regionBased;

	private int tileOffset = 0;

	public Projectile(int durationStart, int durationIncrement) {
		this(-1, 0, 0, 0, durationStart, durationIncrement, 0, 0);
	}

	public Projectile(int delay, int durationStart, int durationIncrement) {
		this(-1, 0, 0, delay, durationStart, durationIncrement, 0, 0);
	}

	public Projectile(int gfxId, int startHeight, int endHeight, int delay, int durationStart, int durationIncrement,
			int curve, int idk) {
		this.gfxId = gfxId;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.delay = delay;
		this.durationStart = durationStart;
		this.durationIncrement = durationIncrement;
		this.curve = curve;
		this.idk = idk;
	}

	public Projectile skipTravel() {
		this.skipTravel = true;
		return this;
	}

	public Projectile regionBased() { // probably not the BEST name lol
		this.regionBased = true;
		return this;
	}

	public int getGfxId() {
		return gfxId;
	}

	public Projectile setGfxId(int gfxId) {
		this.gfxId = gfxId;
		return this;
	}

	public int getStartHeight() {
		return startHeight;
	}

	public Projectile setStartHeight(int startHeight) {
		this.startHeight = startHeight;
		return this;
	}

	public int getEndHeight() {
		return endHeight;
	}

	public Projectile setEndHeight(int endHeight) {
		this.endHeight = endHeight;
		return this;
	}

	public int getDelay() {
		return delay;
	}

	public Projectile setDelay(int delay) {
		this.delay = delay;
		return this;
	}

	public int getDurationStart() {
		return durationStart;
	}

	public Projectile setDurationStart(int durationStart) {
		this.durationStart = durationStart;
		return this;
	}

	public int getDurationIncrement() {
		return durationIncrement;
	}

	public Projectile setDurationIncrement(int durationIncrement) {
		this.durationIncrement = durationIncrement;
		return this;
	}

	public int getCurve() {
		return curve;
	}

	public Projectile setCurve(int curve) {
		this.curve = curve;
		return this;
	}

	public int getIdk() {
		return idk;
	}

	public Projectile setIdk(int idk) {
		this.idk = idk;
		return this;
	}

	public Projectile setSkipTravel(boolean skipTravel) {
		this.skipTravel = skipTravel;
		return this;
	}

	public Projectile setRegionBased(boolean regionBased) {
		this.regionBased = regionBased;
		return this;
	}

	public int send(Entity entity, Entity target) {
		if (target == null || entity == null) {
			return 1;
		}

		int entityX = entity.getAbsX();
		int entityY = entity.getAbsY();
		int entitySize = entity.getSize();
		if (entitySize > 1) {
			entityX += entitySize / 2;
			entityY += entitySize / 2;
		}
		int targetX = target.getAbsX();
		int targetY = target.getAbsY();
		int targetSize = target.getSize();
		if (targetSize > 1) {
			targetX += targetSize / 2;
			targetY += targetSize / 2;
		}
		if (tileOffset != 0) {
			int diffX = targetX - entityX;
			if (diffX != 0)
				entityX += tileOffset * (diffX / Math.abs(diffX));
			int diffY = targetY - entityY;
			if (diffY != 0)
				entityY += tileOffset * (diffY / Math.abs(diffY));
		}
		int distance = Misc.getDistance(entityX, entityY, targetX, targetY);
		int flightDuration = distance * durationIncrement;
		int duration = durationStart + flightDuration;

		if (gfxId == -1) {
			return duration;
		}
		int targetIndex = target.getIndex() + 1;
		if (target.player != null) {
			targetIndex = -targetIndex;
		}

		Iterable<Player> players = regionBased ? entity.getPosition().getRegion().players : entity.localPlayers();
		for (Player p : players) {
			if (skipTravel) {
				p.getPacketSender().sendProjectile(entity, target, gfxId, targetX, targetY, targetX, targetY, startHeight,
						endHeight, delay, duration, curve, idk);
			} else {
				p.getPacketSender().sendProjectile(entity, target, gfxId, entityX, entityY, targetX, targetY, startHeight,
						endHeight, delay, duration, curve, idk);
			}
		}
		return duration;
	}

	public int send(Entity entity, Entity target, int duration) {
		if (target == null || entity == null) {
			return 1;
		}

		if (gfxId == -1) {
			return duration;
		}

		int entityX = entity.getAbsX();
		int entityY = entity.getAbsY();
		int entitySize = entity.getSize();
		if (entitySize > 1) {
			entityX += entitySize / 2;
			entityY += entitySize / 2;
		}
		int targetX = target.getAbsX();
		int targetY = target.getAbsY();
		int targetSize = target.getSize();
		if (targetSize > 1) {
			targetX += targetSize / 2;
			targetY += targetSize / 2;
		}
		if (tileOffset != 0) {
			int diffX = targetX - entityX;
			if (diffX != 0)
				entityX += tileOffset * (diffX / Math.abs(diffX));
			int diffY = targetY - entityY;
			if (diffY != 0)
				entityY += tileOffset * (diffY / Math.abs(diffY));
		}

		int targetIndex = target.getIndex() + 1;
		if (target.player != null) {
			targetIndex = -targetIndex;
		}

		Iterable<Player> players = regionBased ? entity.getPosition().getRegion().players : entity.localPlayers();
		for (Player p : players) {
			if (skipTravel) {
				p.getPacketSender().sendProjectile(entity, target, gfxId, targetX, targetY, targetX, targetY, startHeight,
						endHeight, delay, duration, curve, idk);
			} else {
				p.getPacketSender().sendProjectile(entity, target, gfxId, entityX, entityY, targetX, targetY, startHeight,
						endHeight, delay, duration, curve, idk);
			}
		}
		return duration;
	}

	public int duration(Entity entity, Entity target) {
		if (target == null || entity == null) {
			return 1;
		}

		int entityX = entity.getAbsX();
		int entityY = entity.getAbsY();
		int entitySize = entity.getSize();
		if (entitySize > 1) {
			entityX += entitySize / 2;
			entityY += entitySize / 2;
		}
		int targetX = target.getAbsX();
		int targetY = target.getAbsY();
		int targetSize = target.getSize();
		if (targetSize > 1) {
			targetX += targetSize / 2;
			targetY += targetSize / 2;
		}
		if (tileOffset != 0) {
			int diffX = targetX - entityX;
			if (diffX != 0)
				entityX += tileOffset * (diffX / Math.abs(diffX));
			int diffY = targetY - entityY;
			if (diffY != 0)
				entityY += tileOffset * (diffY / Math.abs(diffY));
		}
		int distance = Misc.getDistance(entityX, entityY, targetX, targetY);
		int flightDuration = distance * durationIncrement;
		int duration = durationStart + flightDuration;
		// int duration = this.durationStart + (durationIncrement * Math.max(0, distance
		// - 1));
		if (gfxId == -1) {
			return duration;
		}
		int targetIndex = target.getIndex() + 1;
		if (target.player != null) {
			targetIndex = -targetIndex;
		}
		return duration;
	}

	public int send(NPC npc, Entity target) {
		if (target == null) {
			return 1;
		}

		int entityX = npc.getAbsX();
		int entityY = npc.getAbsY();
		int entitySize = npc.getSize();
		if (entitySize > 1) {
			entityX += entitySize / 2;
			entityY += entitySize / 2;
		}
		int targetX = target.getAbsX();
		int targetY = target.getAbsY();
		int targetSize = target.getSize();
		if (targetSize > 1) {
			targetX += targetSize / 2;
			targetY += targetSize / 2;
		}
		if (tileOffset != 0) {
			int diffX = targetX - entityX;
			if (diffX != 0)
				entityX += tileOffset * (diffX / Math.abs(diffX));
			int diffY = targetY - entityY;
			if (diffY != 0)
				entityY += tileOffset * (diffY / Math.abs(diffY));
		}
		int distance = Misc.getDistance(entityX, entityY, targetX, targetY);
		int duration = this.durationStart + (durationIncrement * Math.max(0, distance - 1));
		if (gfxId == -1)
			return /* delay + */duration;
		int targetIndex = target.getIndex() + 1;
		if (target.player != null)
			targetIndex = -targetIndex;
		Iterable<Player> players = regionBased ? npc.getPosition().getRegion().players : npc.localPlayers();
		for (Player p : players) {
			if (skipTravel)
				p.getPacketSender().sendProjectile(npc, target, gfxId, targetX, targetY, targetX, targetY, startHeight,
						endHeight, delay, duration, curve, idk);
			else
				p.getPacketSender().sendProjectile(npc, target, gfxId, entityX, entityY, targetX, targetY, startHeight,
						endHeight, delay, duration, curve, idk);
		}
		return duration;
	}

	public int send(Entity entity, int srcX, int srcY, Entity target) {
		if (entity == null || target == null) {
			return 1;
		}
		int targetX = target.getAbsX();
		int targetY = target.getAbsY();
		int targetSize = target.getSize();
		if (targetSize > 1) {
			targetX += targetSize / 2;
			targetY += targetSize / 2;
		}
		if (tileOffset != 0) {
			int diffX = targetX - srcX;
			if (diffX != 0)
				srcX += tileOffset * (diffX / Math.abs(diffX));
			int diffY = targetY - srcY;
			if (diffY != 0)
				srcY += tileOffset * (diffY / Math.abs(diffY));
		}
		int distance = Misc.getDistance(srcX, srcY, targetX, targetY);
		int flightDuration = distance * durationIncrement;
		int duration = durationStart + flightDuration;
		// int duration = this.durationStart + (durationIncrement * Math.max(0, distance
		// - 1));
		if (gfxId == -1)
			return duration;
		int targetIndex = target.getIndex() + 1;
		if (target.player != null)
			targetIndex = -targetIndex;
		Iterable<Player> players = regionBased ? entity.getPosition().getRegion().players : entity.localPlayers();
		for (Player p : players) {
			if (skipTravel)
				p.getPacketSender().sendProjectile(entity, target, gfxId, targetX, targetY, targetX, targetY, startHeight,
						endHeight, delay, duration, curve, idk);
			else
				p.getPacketSender().sendProjectile(entity, target, gfxId, srcX, srcY, targetX, targetY, startHeight, endHeight,
						delay, duration, curve, idk);
		}
		return duration;
	}

	public int send(Entity entity, Position pos) {
		return send(entity, pos.getX(), pos.getY());
	}

	public int send(Entity entity, int targetX, int targetY) {
		if (entity == null) {
			return 1;
		}

		int entityX = entity.getAbsX();
		int entityY = entity.getAbsY();
		int entitySize = entity.getSize();
		if (entitySize > 1) {
			entityX += entitySize / 2;
			entityY += entitySize / 2;
		}
		if (tileOffset != 0) {
			int diffX = targetX - entityX;
			if (diffX != 0)
				entityX += tileOffset * (diffX / Math.abs(diffX));
			int diffY = targetY - entityY;
			if (diffY != 0)
				entityY += tileOffset * (diffY / Math.abs(diffY));
		}
		int distance = Misc.getDistance(entityX, entityY, targetX, targetY);
		int flightDuration = distance * durationIncrement;
		int duration = durationStart + flightDuration;
		// int duration = this.durationStart + (durationIncrement * Math.max(0, distance
		// - 1));
		if (gfxId == -1)
			return duration;
		int targetIndex = 0;
		Iterable<Player> players = regionBased ? entity.getPosition().getRegion().players : entity.localPlayers();
		for (Player p : players) {
			if (skipTravel)
				p.getPacketSender().sendProjectile(entity, null, gfxId, targetX, targetY, targetX, targetY, startHeight,
						endHeight, delay, duration, curve, idk);
			else
				p.getPacketSender().sendProjectile(entity, null, gfxId, entityX, entityY, targetX, targetY, startHeight,
						endHeight, delay, duration, curve, idk);
		}
		return /* delay + */duration;
	}

	public int send(Position src, Position dest) {
		return send(src.getX(), src.getY(), dest.getX(), dest.getY());
	}

	public int send(int startX, int startY, int destX, int destY) {
		// int duration = durationStart + (durationIncrement * Math.max(0,
		// Misc.getDistance(startX, startY, destX, destY) - 1));

		int distance = Math.max(Math.abs(startX - destX), Math.abs(startY - destY));
		int flightDuration = distance * 5;
		int duration = durationStart + flightDuration;
		if (duration < 15) {
			duration += 15;
		}

		for (Player p : Region.get(startX, startY).players)
			p.getPacketSender().sendProjectile(null, null, gfxId, startX, startY, destX, destY, startHeight, endHeight, delay,
					duration, curve, idk);
		return /* delay + */duration;
	}

	public int send(Position src, Entity target) {
		if (target == null) {
			return 1;
		}

		int entityX = src.getX();
		int entityY = src.getY();
		int entitySize = 1;
		if (entitySize > 1) {
			entityX += entitySize / 2;
			entityY += entitySize / 2;
		}
		if (tileOffset != 0) {
			int diffX = target.getAbsX() - entityX;
			if (diffX != 0)
				entityX += tileOffset * (diffX / Math.abs(diffX));
			int diffY = target.getAbsY() - entityY;
			if (diffY != 0)
				entityY += tileOffset * (diffY / Math.abs(diffY));
		}
		int distance = Misc.getDistance(entityX, entityY, target.getAbsX(), target.getAbsY());
		int flightDuration = distance * durationIncrement;
		int duration = durationStart + flightDuration;
		if (gfxId == -1)
			return duration;
		int targetIndex = target.getIndex() + 1;
		if (target.player != null)
			targetIndex = -targetIndex;
		Iterable<Player> players = regionBased ? target.getPosition().getRegion().players : target.localPlayers();
		for (Player p : players) {
			if (skipTravel)
				p.getPacketSender().sendProjectile(null, target, gfxId, target.getAbsX(), target.getAbsY(), target.getAbsX(),
						target.getAbsY(), startHeight, endHeight, delay, duration, curve, idk);
			else
				p.getPacketSender().sendProjectile(null, target, gfxId, entityX, entityY, target.getAbsX(), target.getAbsY(),
						startHeight, endHeight, delay, duration, curve, idk);
		}
		return /* delay + */duration;
	}

	/**
	 * Misc
	 */

	public static Projectile[] arrow(int gfxId) {
		return new Projectile[] {
				new Projectile(gfxId, 40, 36, 41, 51, 5, 15, 11), // regular
				new Projectile(gfxId, 40, 36, 41, 51, 5, 5, 11), // dark bow arrow 1
				new Projectile(gfxId, 40, 36, 41, 65, 10, 25, 11), // dark bow arrow 2
		};
	}

	public static Projectile thrown(int gfxId, int idk) {
		return new Projectile(gfxId, 40, 36, 32, 37, 5, 15, idk);
	}

	public static Projectile[] javelin(int gfxId) {
		return new Projectile[] {
				new Projectile(gfxId, 38, 36, 42, 50, 2, 1, 120), // regular
				new Projectile(gfxId, 38, 36, 49, 52, 3, 1, 120), // special
		};
	}

	public static final Projectile BOLT = new Projectile(27, 38, 36, 41, 51, 5, 5, 11);

	public static final Projectile DRAGON_BOLT = new Projectile(1468, 38, 36, 41, 51, 5, 5, 11);

	public Projectile tileOffset(int tileOffset) {
		this.tileOffset = tileOffset;
		return this;
	}

}

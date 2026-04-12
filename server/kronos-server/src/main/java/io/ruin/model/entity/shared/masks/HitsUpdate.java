package io.ruin.model.entity.shared.masks;

import io.ruin.cache.HeadbarType;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.UpdateMask;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class HitsUpdate extends UpdateMask {

	private final ArrayList<Splat> splats = new ArrayList<>(4);
	private final ArrayList<Bar> bars = new ArrayList<>(4);

	public int hpbarId;

	public void add(Hit hit, int curHp, int maxHp, Entity entity) {
		if (splats.size() < 6) {
			var typeId = hit.getHitType(entity).activeID;
			var source = hit.attacker == null ? -1 : hit.attacker.getClientIndex();
			splats.add(new Splat(source, typeId, hit.damage, 0));
		}
		bars.add(new Bar(hpbarId, hit.damage, curHp, 0, toRatio(hpbarId, curHp, maxHp)));
	}

	/// This was added so that we can process a "timer" or progress bar above the Entities head
	public void add(int hitBarId, int curHp, int maxHp) {
		bars.add(new Bar(hitBarId, 1, curHp, 0, toRatio(hpbarId, curHp, maxHp)));
	}

	public void add(Hit hit, int curHp, int maxHp, int numHits, Entity entity) {
		for (int i = 0; i < numHits; i++) {
			if (splats.size() < 6) {
				var typeId = hit.getHitType(entity).activeID;
				var source = hit.attacker == null ? -1 : hit.attacker.getClientIndex();
				splats.add(new Splat(source, typeId, hit.damage, 0));
			}
		}
		bars.add(new Bar(hpbarId, hit.damage, curHp, 0, toRatio(hpbarId, curHp, maxHp)));
	}

	public void displayBar(int curHp, int maxHp) {
		bars.add(new Bar(hpbarId, 0, curHp, 0, toRatio(hpbarId, curHp, maxHp)));
	}

	/**
	 * For showing HP bar without any hits
	 */
	public void forceSend(int curHp, int maxHp) {
		bars.add(new Bar(hpbarId, 0, curHp, 0, toRatio(hpbarId, curHp, maxHp)));
	}

	@Override
	public void reset() {
		splats.clear();
		bars.clear();
	}

	@Override
	public boolean hasUpdate(boolean added) {
		return !splats.isEmpty() || !bars.isEmpty();
	}

	@Override
	public void send(NPC npc) {
		for (var bar : this.bars) {
			if (bar.hitbarId < 0 || bar.hitbarId >= 0x7FFF) {
				log.error("Wrong data", new IllegalStateException("Wrong hitbar : " + bar.hitbarId));
				return;
			}

			if (bar.hitbarId < 0 || bar.hitbarId >= 0x7FFF) {
				log.error("Wrong data", new IllegalStateException("Wrong hitbar: " + bar.hitbarId));
				return;
			}

			npc.avatarExtended().addHeadBar(npc.getClientIndex(), bar.hitbarId, bar.perc);
		}

		for (var hit : this.splats) {
			var dmg = Math.min(32767, Math.abs(hit.damage));

			if (hit.delay < 0 || hit.delay >= 0x7FFF) {
				log.error("Wrong data", new IllegalStateException("Wrong delay : " + hit.delay));
				return;
			}

			if (hit.hitmarkId < 0 || hit.hitmarkId >= 0x7FFF) {
				log.error("Wrong data", new IllegalStateException("Wrong hitmark: " + hit.hitmarkId));
				return;
			}

			var sourceIndex = hit.sourceIndex;
			var selfType = hit.hitmarkId;
			var otherType = otherId(hit.hitmarkId);

			npc.avatarExtended().addHitMark(sourceIndex, selfType, otherType, dmg, hit.delay);
		}
	}

	@Override
	public void send(Player player) {

		for (var bar : this.bars) {
			if (bar.hitbarId < 0 || bar.hitbarId >= 0x7FFF) {
				log.error("Wrong data", new IllegalStateException("Wrong hitbar: " + bar.hitbarId));
				return;
			}

			player.avatarExtended().addHeadBar(player.getClientIndex(), bar.hitbarId, bar.perc);
		}

		for (var hit : this.splats) {
			if (hit.delay < 0 || hit.delay >= 0x7FFF) {
				log.error("Wrong data", new IllegalStateException("Wrong delay: " + hit.delay));
				return;
			}

			var dmg = Math.min(32767, Math.abs(hit.damage));

			if (hit.hitmarkId < 0 || hit.hitmarkId >= 0x7FFF) {
				log.error("Wrong data", new IllegalStateException("Wrong hitmark : " + hit.hitmarkId));
				return;
			}

			var sourceIndex = hit.sourceIndex;
			var selfType = hit.hitmarkId;
			var otherType = otherId(hit.hitmarkId);

			player.avatarExtended().addHitMark(
					sourceIndex,
					selfType,
					otherType,
					dmg,
					hit.delay);
		}
	}

	private static int otherId(int selfId) {
		return switch (selfId) {
			// normal damage
			case 16 -> {
				yield 17;
			}

			// defend
			case 12 -> {
				yield 13;
			}

			// max hit
			case 43 -> {
				yield 48;
			}

			default -> {
				yield selfId;
			}
		};
	}

	@Override
	public int get(boolean playerUpdate) {
		return playerUpdate ? 4 : 128;
	}

	@ToString
	private static final class Splat {

		private final int sourceIndex;
		private int hitmarkId;
		private int damage;
		private int delay;

		private boolean secondary = false;

		public Splat(int sourceIndex, int type, int damage, int delay) {
			this.hitmarkId = type;
			this.damage = damage;
			this.delay = delay;
			this.sourceIndex = sourceIndex;
		}
	}

	private static final class Bar {

		private int hitbarId, currentLifepoints, newLifepoints, perc;

		public Bar(int type, int damage, int currentHp, int delay, int perc) {
			this.hitbarId = type;
			this.currentLifepoints = currentHp;
			this.newLifepoints = Math.max(0, currentHp - damage);
			this.perc = perc;
		}
	}

	private static int toRatio(int type, int curHp, int maxHp) {
		var barRatio = 100;
		var barType = HeadbarType.get(type);
		if (barType == null) {
			barRatio = 100;
		} else {
			barRatio = barType.width;
		}

		var hp = (Math.min(curHp, maxHp) * barRatio);
		if (hp == 0 || maxHp == 0) {
			return 0;
		}

		var ratio = hp / maxHp;
		if (ratio == 0 && curHp > 0) {
			ratio = 1;
		}
		return ratio;
	}

}

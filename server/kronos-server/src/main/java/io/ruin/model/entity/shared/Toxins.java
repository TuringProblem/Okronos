package io.ruin.model.entity.shared;

import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.var.VarPlayerRepository;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public class Toxins {
	// Represents a type of toxin.
	@Getter
	public enum ToxinType {

		POSION(HitType.POISON, "You have been poisoned!", 6),
		VENOM(HitType.VENOM, "You have been venomed!", 6, ToxinType.POSION);

		public HitType hitType;
		public String message;
		public int baseDamage;
		public ToxinType[] children;

		ToxinType(HitType hitType, String message, int baseDamage, ToxinType... children) {
			this.hitType = hitType;
			this.message = message;
			this.baseDamage = baseDamage;
			this.children = children;
		}

		public boolean isParentOf(ToxinType type) {
			for (ToxinType toxin : values()) {
				if (Objects.nonNull(toxin.children)) {
					for (ToxinType child : toxin.children) {
						if (type == child)
							return true;
					}
				}
			}
			return false;
		}

	}

	private static final Logger log = LoggerFactory.getLogger(Toxins.class);

	private ToxinType toxin;
	private int damage;
	private int duration;
	private Map<ToxinType, Integer> immunities = Maps.newConcurrentMap();

	private transient Entity entity;

	public void init(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Performs processing of toxins.
	 */
	public void tick() {
		immunities.entrySet().stream().forEach(e -> {
			int duration = e.getValue() - 1;
			if (duration <= 0) {
				immunities.remove(e.getKey());
			} else
				immunities.put(e.getKey(), e.getValue() - 1);
		});
		// do immunities before this
		if (Objects.isNull(toxin) || isImmuneTo(toxin)) {
			return;
		}

		if (damage == 0) {
			cure(toxin, 1);
			return;
		}

		duration--;

		if (duration > 0) {
			if (duration % 30 == 0) {
				entity.hit(new Hit(toxin.getHitType()).fixedDamage(damage));
			}
		} else if (duration <= 0) {
			entity.hit(new Hit(toxin.getHitType()).fixedDamage(damage));
			if (toxin.equals(ToxinType.POSION)) {
				if (damage-- > 0) {
					duration = 120;
				}
			} else if (toxin.equals(ToxinType.VENOM)) {
				damage = Math.min(damage + 2, 20);
				duration = 120;
			}
		}

	}

	public void cure(ToxinType toxin, int immuneTime) {
		if (Objects.nonNull(this.toxin) && toxin.equals(ToxinType.VENOM)) {
			this.toxin = ToxinType.POSION;
			this.damage = 6;
			if (Objects.nonNull(entity.player))
				VarPlayerRepository.POISONED.set(entity.player, 1);
		} else {
			this.duration = 0;
			this.damage = 0;
			if (isImmuneTo(ToxinType.VENOM)) {
				if (this.duration < immuneTime) {
					immunities.put(ToxinType.VENOM, immuneTime);
				}
				if (immuneTime > 0) {
					immunities.put(ToxinType.VENOM, immuneTime);
				}
			}
			this.duration = 0;
			this.damage = 0;
			this.toxin = null;
			if (Objects.nonNull(entity.player))
				VarPlayerRepository.POISONED.set(entity.player, 0);
		}
	}

	public boolean inflictToxin(ToxinType toxin, int damage) {
		if (Objects.nonNull(this.toxin) && this.toxin.ordinal() > toxin.ordinal()) {
			log.info("Toxin is parent of existing toxin.");
			return false;
		}
		if (Objects.isNull(toxin) || isImmuneTo(toxin)) {
			log.info("Toxin is null or immunity.");
			return false;
		}
		if (Objects.nonNull(this.toxin) && this.toxin == toxin) {
			log.info("Already has " + toxin.name() + " toxin.");
			return false;
		}
		entity.hit(new Hit(toxin.getHitType()).fixedDamage(damage));
		this.duration = 120;
		this.damage = damage;
		this.toxin = toxin;
		if (Objects.nonNull(entity.player)) {
			entity.player.sendMessage(toxin.message);
			VarPlayerRepository.POISONED.set(entity.player, toxin == ToxinType.VENOM ? 1000000 : 1);
		}
		return true;
	}

	public boolean isImmuneTo(ToxinType toxin) {
		if (Objects.isNull(toxin)) {
			return true;
		}
		return (toxin.equals(ToxinType.POSION) ? entity.isPoisonImmune()
			: (toxin.equals(ToxinType.VENOM) ? entity.isVenomImmune() : immunities.containsKey(toxin)));
	}

	public boolean isInflictedWith(ToxinType type) {
		return Objects.nonNull(toxin) && toxin.equals(type);
	}

	public boolean hasImmunity(ToxinType toxin) {
		return immunities.containsKey(toxin);
	}
}

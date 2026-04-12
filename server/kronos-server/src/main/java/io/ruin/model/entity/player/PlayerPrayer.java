package io.ruin.model.entity.player;

import com.google.gson.annotations.Expose;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ConservativePrayers;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.handlers.EquipmentStats;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.utility.TickDelay;

public class PlayerPrayer {

	public transient Player player;
	public transient boolean[] active = new boolean[Prayer.VALUES.length];

	// Keeps track of draining for each available prayer, when activated/deactivated
	// when prayer gets deactivated it uses stored value to properly decrease
	// total drain value, otherwise applying effects in between activation and
	// deactivation
	// would lose this information and keep total drain inconsistent
	public transient int[] activeDrainSet = new int[Prayer.VALUES.length];

	public transient int drainTotal, drainSkip;
	public transient double attackBoost, strengthBoost, defenceBoost;
	public transient double rangedAttackBoost, rangedStrengthBoost;
	public transient double magicAttackBoost;
	public transient double magicDamageBoost;
	public transient TickDelay slashDelay = new TickDelay();

	private boolean[] quickPrayers = new boolean[active.length];
	private int drainCounter;

	public void init(Player player) {
		this.player = player;
		sendQuickPrayers();

		// update initial varps for prayer
		for (var prayer : Prayer.values()) {
			prayer.config.update(player);
		}
	}

	public void toggle(Prayer prayer) {
		if (delayRequired()) {
			// So on rs it delays but Idk a good way to do that... Idk if people will even
			// notice...
			prayer.config.update(player);
			return;
		}

		if (isActive(prayer)) {
			deactivate(prayer);
			player.privateSound(2663);
			return;
		}

		if (!checkPoints()) {
			/* client sends sound & doesn't light up prayer */
			return;
		}

		if (!allowPrayers()) {
			prayer.config.set(player, 0);
			return;
		}
		activate(prayer);
	}

	public void deactivate(Prayer prayer) {
		active[prayer.ordinal()] = false;
		drainTotal -= activeDrainSet[prayer.ordinal()];
		attackBoost -= prayer.attackBoost;
		strengthBoost -= prayer.strengthBoost;
		defenceBoost -= prayer.defenceBoost;
		rangedAttackBoost -= prayer.rangedAttackBoost;
		rangedStrengthBoost -= prayer.rangedStrengthBoost;
		magicAttackBoost -= prayer.magicAttackBoost;
		magicDamageBoost -= prayer.magicDamageBoost;

		activeDrainSet[prayer.ordinal()] = 0;

		if (drainTotal == 0) {
			drainSkip = 0;
			VarPlayerRepository.QUICK_PRAYING.set(player, 0);
		}

		if (prayer.headIcon != -1) {
			player.getAppearance().setPrayerIcon(-1);
		}

		prayer.config.set(player, 0);
	}

	private int prayerDrain(Player player, Prayer prayer) {
		if (player.prayerBoostActive()) {
			return (int) (prayer.drain * 0.7);
		}
		return prayer.drain;
	}

	public void deactivateAll() {
		for (Prayer prayer : Prayer.VALUES) {
			if (isActive(prayer))
				deactivate(prayer);
		}
	}

	public void deactivateProtectionPrayer() {
		if (isActive(Prayer.PROTECT_FROM_MAGIC))
			deactivate(Prayer.PROTECT_FROM_MAGIC);
		else if (isActive(Prayer.PROTECT_FROM_MISSILES))
			deactivate(Prayer.PROTECT_FROM_MISSILES);
		else if (isActive(Prayer.PROTECT_FROM_MELEE))
			deactivate(Prayer.PROTECT_FROM_MELEE);
	}

	public boolean isActive(Prayer prayer) {
		return active[prayer.ordinal()];
	}

	/**
	 * Quick prayers
	 */

	public void toggleQuickPrayers() {
		if (delayRequired()) {
			VarPlayerRepository.QUICK_PRAYING.update(player);
			return;
		}

		if (VarPlayerRepository.QUICK_PRAYING.get(player) == 1) {
			deactivateAll();
			player.privateSound(2663);
			VarPlayerRepository.QUICK_PRAYING.set(player, 0);
			return;
		}

		if (!checkPoints() || !allowPrayers()) {
			VarPlayerRepository.QUICK_PRAYING.set(player, 0); // orb needs to be refreshed
			return;
		}

		for (int i = 0; i < quickPrayers.length; i++) {
			if (quickPrayers[i]) {
				Prayer prayer = Prayer.VALUES[i];
				if (!isActive(prayer)) {
					activate(prayer);
				}
			}
		}
		VarPlayerRepository.QUICK_PRAYING.set(player, 1);
	}

	public void toggleQuickPrayer(Prayer prayer) {
		if (quickPrayers[prayer.ordinal()]) {
			quickPrayers[prayer.ordinal()] = false;
			return;
		}
		if (!checkReq(prayer)) {
			sendQuickPrayers();
			return;
		}
		if (prayer.disallowed != null) {
			for (Prayer p : prayer.disallowed) {
				if (quickPrayers[p.ordinal()])
					quickPrayers[p.ordinal()] = false;
			}
		}
		quickPrayers[prayer.ordinal()] = true;
		sendQuickPrayers();
	}

	/**
	 * Draining
	 */

	public int drain(int points) {
		if (player.getCombat().isDead()) {
			return 0;
		}

		Stat prayer = player.getStats().get(StatType.Prayer);
		int currentLevel = prayer.currentLevel;
		if (currentLevel == 0) {
			return 0;
		}

		if (currentLevel > points) {
			prayer.alter(prayer.currentLevel - points);
			return points;
		}

		prayer.alter(0);
		if (drainTotal > 0) {
			player.sendMessage("You have run out of prayer points, you must recharge at an altar.");
			deactivateAll();
		}
		drainCounter = 0;
		return currentLevel;
	}

	public void process() {
		if (drainTotal == 0) {
			return;
		}

		if (drainSkip == 0) {
			drainSkip = drainTotal;
			return;
		}

		if (drainSkip != -1) {
			drainCounter += drainSkip;
			drainSkip = -1;
		}

		drainCounter += drainTotal;
		int resistance = 60 + (player.getEquipment().bonuses[EquipmentStats.PRAYER] * 2);

		var pPerks = player.getPlayerPerkHandler();
		var hasConservativePrayers = pPerks.getActivePerks(player).contains(Perks.CONSERVATIVE_PRAYERS);

		if (hasConservativePrayers) {
			var perkIndex = pPerks.getActivePerkIndex(player, Perks.CONSERVATIVE_PRAYERS);
			var c = (ConservativePrayers) pPerks.getActivePerks(player).get(perkIndex).getPerk(player);
			var multiplier = 2.0F;
			multiplier += c.getDrainAmount();
			resistance *= multiplier;
		}

		if (drainCounter > resistance) {
			drain(drainCounter / resistance);
			drainCounter %= resistance;
		}

		// this should never be possible. must be a bug somewhere else.
		if (drainCounter < 0) {
			drainCounter = 0;
		}
	}

	public void slashPrayers() {
		slashDelay.delay(9);
		if (isActive(Prayer.PROTECT_FROM_MAGIC))
			deactivate(Prayer.PROTECT_FROM_MAGIC);
		else if (isActive(Prayer.PROTECT_FROM_MISSILES))
			deactivate(Prayer.PROTECT_FROM_MISSILES);
		else if (isActive(Prayer.PROTECT_FROM_MELEE))
			deactivate(Prayer.PROTECT_FROM_MELEE);
	}

	/**
	 * Regular prayers
	 */

	private boolean delayRequired() {
		return player.isLocked() || player.isStunned();
	}

	private boolean checkPoints() {
		if (player.getStats().get(StatType.Prayer).currentLevel == 0) {
			player.sendMessage("You need to recharge your Prayer at an altar.");
			return false;
		}
		return true;
	}

	private boolean checkReq(Prayer prayer) {
		if (player.getStats().get(StatType.Prayer).fixedLevel < prayer.level) {
			player.dialogue(new MessageDialogue("You need a <col=000080>Prayer</col> level of " + prayer.level
					+ " to use <col=000080>" + prayer.name + "</col>."));
			return false;
		}
		return prayer.activationCheck == null || prayer.activationCheck.test(player);
	}

	private boolean allowPrayers() {
		return player.allowPrayerListener == null || player.allowPrayerListener.allow(player);
	}

	private boolean allowPrayer(Prayer prayer) {
		return player.activatePrayerListener == null || player.activatePrayerListener.allow(player, prayer);
	}

	private void activate(Prayer prayer) {
		if (!allowPrayer(prayer) || !checkReq(prayer)) {
			prayer.config.set(player, 0);
			return;
		}

		if (prayer.disallowed != null) {
			for (Prayer p : prayer.disallowed) {
				if (isActive(p))
					deactivate(p);
			}
		}

		active[prayer.ordinal()] = true;
		activeDrainSet[prayer.ordinal()] = prayerDrain(player, prayer);
		drainTotal += activeDrainSet[prayer.ordinal()];
		attackBoost += prayer.attackBoost;
		strengthBoost += prayer.strengthBoost;
		defenceBoost += prayer.defenceBoost;
		rangedAttackBoost += prayer.rangedAttackBoost;
		rangedStrengthBoost += prayer.rangedStrengthBoost;
		magicAttackBoost += prayer.magicAttackBoost;
		magicDamageBoost += prayer.magicDamageBoost;
		if (prayer.headIcon != -1)
			player.getAppearance().setPrayerIcon(prayer.headIcon);
		prayer.config.set(player, 1);
		player.privateSound(prayer.soundId);
	}

	private void sendQuickPrayers() {
		int value = 0;
		for (int i = 0; i < Prayer.QUICK_PRAYER_ORDER.length; i++) {
			Prayer prayer = Prayer.QUICK_PRAYER_ORDER[i];
			if (quickPrayers[prayer.ordinal()])
				value |= (1 << i);
		}
		VarPlayerRepository.QUICK_PRAYERS_ACTIVE.set(player, value);
	}

}

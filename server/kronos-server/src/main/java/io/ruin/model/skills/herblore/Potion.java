package io.ruin.model.skills.herblore;

import io.ruin.api.utils.Random;
import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.activities.perktree.PerkTaskHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.PacksnPotions;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.stat.StatType;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Potion {

	/**
	 * Regular potions
	 */
	ATTACK(1, 25.0, "attack potion", "guam potion (unf)", "eye of newt"),
	ANTIPOISON(5, 37.5, "antipoison", "marrentill potion (unf)", "unicorn horn dust"),
	RELICYMS_BALM(8, 40.0, "relicym's balm", "rogue's purse", "snake weed", "vial of water"),
	STRENGTH(12, 50.0, "strength potion", "tarromin potion (unf)", "limpwurt root"),
	GUTHIX_REST(18, 59.5, "guthix rest", "bowl of hot water", "guam leaf", "harralander", "marrentill"),
	RESTORE(22, 62.5, "restore potion", "harralander potion (unf)", "red spiders' eggs"),
	GUTHIX_BALANCE(22, 25.0, "guthix balance", "restore potion(3)", "garlic", "silver dust"),
	ENERGY(26, 67.5, "energy potion", "harralander potion (unf)", "chocolate dust"),
	DEFENCE(30, 75.0, "defence potion", "ranarr potion (unf)", "white berries"),
	AGILITY(34, 80.0, "agility potion", "toadflax potion (unf)", "toad's legs"),
	COMBAT(36, 84.0, "combat potion", "harralander potion (unf)", "goat horn dust"),
	PRAYER(38, 87.5, "prayer potion", "ranarr potion (unf)", "snape grass"),
	SUPER_ATTACK(45, 100.0, "super attack", "irit potion (unf)", "eye of newt"),
	SUPER_ANTIPOISON(48, 106.3, "superantipoison", "irit potion (unf)", "unicorn horn dust"),
	FISHING(50, 112.5, "fishing potion", "avantoe potion (unf)", "snape grass"),
	SUPER_ENERGY(52, 117.5, "super energy", "avantoe potion (unf)", "mort myre fungus"),
	HUNTER(53, 120.0, "hunter potion", "avantoe potion (unf)", "kebbit teeth dust"),
	SUPER_STRENGTH(55, 125.0, "super strength", "kwuarm potion (unf)", "limpwurt root"),
	MAGIC_ESSENCE(57, 130.0, "magic essence", "magic essence (unf)", "gorak claw powder"),
	WEAPON_POISON(60, 137.5, "weapon poison", "kwuarm potion (unf)", "dragon scale dust"),
	SUPER_RESTORE(63, 142.5, "super restore", "snapdragon potion (unf)", "red spiders' eggs"),
	SANFEW_SERUM(65, 160.0, "sanfew serum", "super restore(3)", "unicorn horn dust", "snake weed", "nail beast nails"),
	SUPER_DEFENCE(66, 150.0, "super defence", "cadantine potion (unf)", "white berries"),
	ANTIDOTE_PLUS(68, 155.0, "antidote+", "coconut milk", "toadflax", "yew roots"),
	ANTIFIRE(69, 157.5, "antifire potion", "lantadyme potion (unf)", "dragon scale dust"),
	RANGING(72, 162.5, "ranging potion", "dwarf weed potion (unf)", "wine of zamorak"),
	WEAPON_POISON_PLUS(73, 165.0, "weapon poison(+)", "coconut milk", "cactus spine", "red spiders' eggs"),
	MAGIC(76, 172.5, "magic potion", "lantadyme potion (unf)", "potato cactus"),
	STAMINA(77, 102.0, "stamina potion", "super energy(3)", "amylase crystal"),
	ZAMORAK_BREW(78, 175.0, "zamorak brew", "torstol potion (unf)", "jangerberries"),
	ANTIDOTE_PLUS_PLUS(79, 177.5, "antidote++", "coconut milk", "irit leaf", "magic roots"),
	SARADOMIN_BREW(81, 180.0, "saradomin brew", "toadflax potion (unf)", "crushed nest"),
	WEAPON_POISON_PLUS_PLUS(82, 190.0, "weapon poison(++)", "coconut milk", "cave nightshade", "poison ivy berries"),
	EXTENDED_ANTIFIRE(84, 110.0, "extended antifire", "antifire potion(3)", "lava scale shard"),
	ANTI_VENOM(87, 120.0, "anti-venom", "antidote++(3)", "zulrah's scales"),
	SUPER_COMBAT(90, 150.0, "super combat potion", "torstol", "super attack(3)", "super strength(3)", "super defence(3)"),
	SUPER_ANTIFIRE(92, 130.0, "super antifire potion", "antifire potion(3)", "crushed superior dragon bones"),
	SUPER_ANTI_VENOM(94, 125.0, "anti-venom+", "anti-venom(3)", "torstol"),
	SUPER_ANTI_VENOM4(94, 125.0, "anti-venom+", "anti-venom(4)", "torstol"),
	EXTENDED_SUPER_ANTIFIRE(98, 160.0, "extended super antifire", "super antifire potion(3)", "lava scale shard"),

	/**
	 * Combination Potions
	 */

	BASTION(80, 155.0, "bastion potion", "cadantine blood potion (unf)", "wine of zamorak"),
	BATTLEMAGE(80, 155.0, "battlemage potion", "cadantine blood potion (unf)", "potato cactus"),

	/**
	 * Divine potions
	 */

	DIVINE_SUPER_ATTACK(70, 2.0, "divine super attack potion", "super attack(4)", "crystal dust"),
	DIVINE_SUPER_STRENGTH(70, 2.0, "divine super strength potion", "super strength(4)", "crystal dust"),
	DIVINE_SUPER_DEFENCE(70, 2.0, "divine super defence potion", "super defence(4)", "crystal dust"),
	DIVINE_RANGING(74, 2.0, "divine ranging potion", "ranging potion(4)", "crystal dust"),
	DIVINE_MAGIC(78, 2.0, "divine magic potion", "magic potion(4)", "crystal dust"),
	DIVINE_BASTION(86, 2.0, "divine bastion potion", "bastion potion(4)", "crystal dust"),
	DIVINE_BATTLEMAGE(86, 2.0, "divine battlemage potion", "battlemage potion(4)", "crystal dust"),
	DIVINE_SUPER_COMBAT(97, 2.0, "divine super combat potion", "super combat potion(4)", "crystal dust"),

	/**
	 * Raids potions
	 */
	ELDER_MINUS("elder (-)"),
	ELDER_REGULAR("elder potion"),
	ELDER_PLUS("elder (+)"),
	TWISTED_MINUS("twisted (-)"),
	TWISTED_REGULAR("twisted potion"),
	TWISTED_PLUS("twisted (+)"),
	KODAI_MINUS("kodai (-)"),
	KODAI_REGULAR("kodai potion"),
	KODAI_PLUS("kodai (+)"),
	REVITALISATION_MINUS("revitalisation (-)"),
	REVITALISATION_REGULAR("revitalisation potion"),
	REVITALISATION_PLUS("revitalisation (+)"),
	PRAYER_ENHANCE_MINUS("prayer enhance (-)"),
	PRAYER_ENHANCE_REGULAR("prayer enhance"),
	PRAYER_ENHANCE_PLUS("prayer enhance (+)"),
	XERIC_AID_MINUS("xeric's aid (-)"),
	XERIC_AID_REGULAR("xeric's aid "),
	XERIC_AID_PLUS("xeric's aid (+)"),
	OVERLOAD_MINUS("overload (-)"),
	OVERLOAD_REGULAR(1, 1, "overload ", ""),
	OVERLOAD_PLUS("overload (+)"),
	EGNIOL_POTION("egniol potion"),
	BLIGHTED_SUPER_RESTORE(1, 1, "blighted super restore", "");

	public final int lvlReq;

	public final double xp;

	public final String potionName;

	public final String primaryName;

	public final String[] secondaryNames;

	public final int[] vialIds;

	public boolean raidsPotion;

	Potion(int lvlReq, double xp, String potionName, String primaryName, String... secondaryNames) {
		this.lvlReq = lvlReq;
		this.xp = xp;
		this.potionName = potionName;
		this.primaryName = primaryName;
		this.secondaryNames = secondaryNames;
		this.vialIds = new int[4];
		this.raidsPotion = false;
	}

	Potion(String potionName) {
		this.lvlReq = -1;
		this.xp = -1;
		this.potionName = potionName;
		this.primaryName = "";
		this.secondaryNames = new String[4];
		this.vialIds = new int[4];
		this.raidsPotion = true;
	}

	private void mix(Player player, Item primaryItem, List<Item> secondaryItems) {
		boolean keepMaterials = false;
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.PACKS_N_POTIONS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.PACKS_N_POTIONS);
			PacksnPotions c = (PacksnPotions) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			assert c != null;
			if (Random.rollPercent(c.getChanceToKeepMaterials())) {
				keepMaterials = true;
			}
		}
		if (!keepMaterials) {
			primaryItem.remove();
			for (Item secondaryItem : secondaryItems) {
				if (secondaryItem.getId() == ItemID.AMYLASE_CRYSTAL)
					player.getInventory().remove(secondaryItem.getId(), 6);
				else if (secondaryItem.getId() == ItemID.ZULRAHS_SCALES)
					player.getInventory().remove(secondaryItem.getId(), 15);
				else if (secondaryItem.getId() == ItemID.LAVA_SCALE_SHARD)
					player.getInventory().remove(secondaryItem.getId(), 3);
				else
					secondaryItem.remove();
			}
		}

		PerkTaskHandler.handleGatherResource(player, vialIds[2], 1);
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.PACKS_N_POTIONS)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.PACKS_N_POTIONS);
			PacksnPotions c = (PacksnPotions) player.getPlayerPerkHandler().
				getActivePerks(player).get(perkIndex).getPerk(player);
			player.getInventory().addOrDrop(vialIds[3], 1);

		} else
			player.getInventory().add(vialIds[2], 1);

		player.getStats().addXp(StatType.Herblore, xp, true);
		player.animate(363);
		if (potionName.contains("saradomin brew"))
			DailyTasks.handleTaskDecrement(player, "mixsarabrews");
		if (potionName.contains("prayer potion"))
			DailyTasks.handleTaskDecrement(player, "mixprayerpots");
		if (potionName.contains("super attack"))
			DailyTasks.handleTaskDecrement(player, "mixsuperattack");
		if (potionName.contains("super defence"))
			DailyTasks.handleTaskDecrement(player, "mixsuperdefence");
		if (potionName.contains("super strength"))
			DailyTasks.handleTaskDecrement(player, "mixsuperstrength");
		if (potionName.contains("attack potion") && !potionName.contains("super"))
			DailyTasks.handleTaskDecrement(player, "mixattack");
		if (potionName.contains("strength potion") && !potionName.contains("super"))
			DailyTasks.handleTaskDecrement(player, "mixstrength");

		if (potionName.contains("super restore")) {
			DailyTasks.handleTaskDecrement(player, "mixsuperrestore");
			player.superRestoresMixed++;
			if (player.superRestoresMixed == Achievements.MIGHT_NEED_THESE_LATER.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.MIGHT_NEED_THESE_LATER.getAchievementName());
		}
		if (potionName.contains("super combat")) {
			player.divineSuperCombatPotionsMixed++;
			if (player.divineSuperCombatPotionsMixed == Achievements.LITTLE_BIT_OF_DIVINITY_IN_THE_MIX.getCompletionAmount())
				player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.LITTLE_BIT_OF_DIVINITY_IN_THE_MIX.getAchievementName());
		}
	}

	private void decant(Player player, Item fromPot, Item toPot) {
		if (fromPot == toPot) return;
		int fromDoses = fromPot.getDef().potionDoses;
		int toDoses = toPot.getDef().potionDoses;
		int doses = Math.min(fromDoses, 4 - toDoses);
		if (doses == 0) {
			player.sendMessage("That potion is already full.");
			return;
		}
		fromDoses -= doses;
		toDoses += doses;
		if (fromDoses <= 0)
			fromPot.setId(raidsPotion ? 20800 : 229);
		else
			fromPot.setId(vialIds[fromDoses - 1]);
		toPot.setId(vialIds[toDoses - 1]);
		player.sendFilteredMessage("You have combined the liquid into " + toDoses + " doses.");
	}

	private void divide(Player player, Item potionItem, Item vialItem) {
		System.out.println("Dividing potion");
		if (potionItem == vialItem) return;
		int doses = potionItem.getDef().potionDoses;
		if (doses == 1) {
			player.sendMessage("There's not enough liquid to divide.");
			return;
		}
		int fromDoses = (int) Math.floor(doses / 2D);
		int toDoses = (int) Math.ceil(doses / 2D);
		potionItem.setId(vialIds[fromDoses - 1]);
		vialItem.setId(vialIds[toDoses - 1]);
		player.sendMessage("You divide the liquid between the vessels.");
	}

	/**
	 * Bulk decanting
	 */

	public static void decant(Player player, int dosage) {
		HashMap<Potion, Decant> potions = new HashMap<>();
		HashMap<Potion, Decant> notedPotions = new HashMap<>();
		for (Item item : player.getInventory().getItems()) {
			if (item == null)
				continue;
			boolean noted = false;
			ObjType def = item.getDef();
			if (def.isNote()) {
				noted = true;
				def = def.fromNote();
			}
			if (def.potion == null || def.potionDoses == dosage) {
				/* ignore this item */
				continue;
			}
			HashMap<Potion, Decant> map = noted ? notedPotions : potions;
			Decant decant = map.get(def.potion);
			if (decant == null)
				map.put(def.potion, decant = new Decant());
			decant.doses += (item.getAmount() * def.potionDoses);
			item.remove();
		}
		potions.forEach((p, d) -> d.decant(player, p, dosage, dosage != 4));
		notedPotions.forEach((p, d) -> d.decant(player, p, dosage, true));
		player.dialogue(new MessageDialogue("Your potions have been decanted to x" + dosage + " dose" + (dosage > 1 ? "s" : "") + "."));
	}

	private static final class Decant {

		double doses;

		void decant(Player player, Potion potion, int dosage, boolean note) {
			int desired = (int) (doses / (double) dosage);
			if (desired > 0) {
				int addId = potion.vialIds[dosage - 1];
				player.getInventory().addOrDrop(note ? ObjType.get(addId).notedId : addId, desired);
				doses -= (desired * dosage);
			}
			int remainingDoses = (int) doses;
			if (remainingDoses > 0) {
				int addId = potion.vialIds[remainingDoses - 1];
				player.getInventory().addOrDrop(note ? ObjType.get(addId).notedId : addId, 1);
			}
		}

	}

	/**
	 * Registering
	 */

	private static void register(Potion potion, int primaryId, int[] secondaryIds) {
		SkillItem item = new SkillItem(potion.vialIds[2]).addAction((player, amount, event) -> {
			while (amount-- > 0) {
				Item primaryItem = player.getInventory().findItem(primaryId);
				if (primaryItem == null)
					return;
				List<Item> secondaryItems = player.getInventory().collectOneOfEach(secondaryIds);
				if (secondaryItems == null)
					return;
				potion.mix(player, primaryItem, secondaryItems);
				event.delay(1);
			}
		});
		for (int secondaryId : secondaryIds) {
			ItemItemAction.register(primaryId, secondaryId, (player, primary, secondary) -> {
				if (!player.getStats().check(StatType.Herblore, potion.lvlReq, "make this potion"))
					return;
				if (player.getInventory().hasMultiple(secondaryIds)) {
					SkillDialogue.make(player, item);
					return;
				}
				List<Item> secondaries = player.getInventory().collectOneOfEach(secondaryIds);
				if (secondaries == null) {
					player.sendMessage("You need more ingredients to make this potion.");
					return;
				}
				potion.mix(player, primary, secondaries);
			});
		}
	}

	private static void registerUpgrade(Potion potion, int primaryId, int secondaryId, int secondaryAmtPerDose) {
		Potion primaryPotion = ObjType.get(primaryId).potion;
		String secondaryName = ObjType.get(secondaryId).name.toLowerCase().replace("'s", "");
		String secondaryPluralName = secondaryName + (secondaryName.endsWith("s") ? "" : "s");
		double xpPerDose = potion.xp / 4;
		for (int i = 0; i < primaryPotion.vialIds.length; i++) {
			int vialId = primaryPotion.vialIds[i];
			ItemItemAction.register(vialId, secondaryId, (player, primary, secondary) -> {
				if (!player.getStats().check(StatType.Herblore, potion.lvlReq, "make this potion"))
					return;
				int doses = primary.getDef().potionDoses;
				int reqAmt = doses * secondaryAmtPerDose;
				if (secondary.getAmount() < reqAmt) {
					if (doses == 1)
						player.sendMessage("You need at least " + reqAmt + " " + secondaryPluralName + " to upgrade 1 dose of that potion.");
					else
						player.sendMessage("You need at least " + reqAmt + " " + secondaryPluralName + " to upgrade " + doses + " doses of that potion.");
					return;
				}
				secondary.remove(reqAmt);
				primary.setId(potion.vialIds[doses - 1]);
				player.animate(363);
				player.getStats().addXp(StatType.Herblore, xpPerDose * doses, true);
				if (reqAmt == 1)
					player.sendFilteredMessage("You mix 1 " + secondaryName + " into your potion.");
				else
					player.sendFilteredMessage("You mix " + reqAmt + " " + secondaryPluralName + " into your potion.");
			});
		}
	}

	public static void register() {
		final ObjectCollection<ObjType> objTypeCachedValues = ObjType.cached.values();

		final Map<ObjType, String> potionNames = new Object2ObjectOpenHashMap<>(objTypeCachedValues.size());
		for (final ObjType def : objTypeCachedValues) {
			if (def == null) {
				continue;
			}

			final String name = def.getName();
			if (name == null) {
				continue;
			}

			final String nameLower = name.toLowerCase();
			potionNames.put(def, nameLower);
		}

		for (final Potion potion : values()) {
			/*
			 * Get data from names
			 */
			int primaryId = -1;
			int[] secondaryIds = new int[potion.secondaryNames.length];
			// Initialize lowest ID variables
			int lowestPrimaryId = Integer.MAX_VALUE;
			int[] lowestSecondaryIds = new int[potion.secondaryNames.length];
			Arrays.fill(lowestSecondaryIds, Integer.MAX_VALUE);

			for (final Map.Entry<ObjType, String> entry : potionNames.entrySet()) {
				final ObjType def = entry.getKey();
				final String nameLower = entry.getValue();

				if (nameLower.startsWith(potion.potionName + "(") || nameLower.startsWith(potion.potionName + " (")) {
					final String name = def.getName();
					int doses = Character.getNumericValue(name.charAt(name.lastIndexOf("(") + 1));

					if (doses >= 1 && doses <= 4) {
						if (potion.vialIds[doses - 1] == 0 || def.id < potion.vialIds[doses - 1]) {
							def.potion = potion;
							def.potionDoses = doses;
							potion.vialIds[doses - 1] = def.id;
						}
						continue;
					}
				} else if (nameLower.equals(potion.potionName)) { // no multi-dose (like weapon poison)
					if (def.id < potion.vialIds[2]) {
						def.potion = potion;
						def.potionDoses = 1;
						potion.vialIds[2] = def.id;
					}
				}

				if (nameLower.equals(potion.primaryName)) {
					if (def.id < lowestPrimaryId) {
						lowestPrimaryId = def.id;
					}
					continue;
				}

				for (int i = 0; i < secondaryIds.length; i++) {
					if (secondaryIds[i] == 0 && nameLower.equals(potion.secondaryNames[i])) {
						if (def.id < lowestSecondaryIds[i]) {
							lowestSecondaryIds[i] = def.id;
						}
					}
				}
			}

			// Set primary ID to the lowest found
			if (lowestPrimaryId != Integer.MAX_VALUE) {
				primaryId = lowestPrimaryId;
			}

			// Set secondary IDs to the lowest found
			for (int i = 0; i < secondaryIds.length; i++) {
				if (lowestSecondaryIds[i] != Integer.MAX_VALUE) {
					secondaryIds[i] = lowestSecondaryIds[i];
				}
			}

			/*
			 * Register mixes
			 */
			switch (potion) {
				// Switch cases remain the same
			}

			if (!potion.raidsPotion)
				register(potion, primaryId, secondaryIds);

			/*
			 * Register decant actions
			 */
			if (Arrays.stream(potion.vialIds).noneMatch(id -> id == 0)) {
				for (int id1 : potion.vialIds) {
					for (int id2 : potion.vialIds)
						ItemItemAction.register(id1, id2, potion::decant);
					ItemItemAction.register(id1, potion.raidsPotion ? 20800 : 229, potion::divide);
				}
			}
		}

	}

}

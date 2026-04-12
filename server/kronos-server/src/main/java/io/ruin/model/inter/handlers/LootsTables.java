package io.ruin.model.inter.handlers;

import io.ruin.model.activities.bosses.Malakar;
import io.ruin.model.activities.bosses.SolHeredit;
import io.ruin.model.activities.bosses.araxxor.Araxxor;
import io.ruin.model.activities.bosses.nightmare.NightmareCombat;
import io.ruin.model.activities.moonsofperil.MoonsOfPerilRewards;
import io.ruin.model.activities.tempevents.summerevent.SummerBoss;
import io.ruin.model.activities.tempevents.summerevent.SummerEventHandler;
import io.ruin.model.item.actions.impl.boxes.mystery.MysteryBox;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.object.actions.impl.BrimstoneChest;
import io.ruin.model.map.object.actions.impl.LarransChest;

public enum LootsTables {
	REGULAR_MYSTERY_BOX("Regular Mystery Box", MysteryBox.MYSTERY_BOX_TABLE),
	ARAXXOR("Araxxor", Araxxor.table),
	ADVANCED_MYSTERY_BOX("Advanced Mystery Box", MysteryBox.ADVANCED_MYSTERY_BOX_COMMON_TABLE),
	BRIMSTONE_CHEST("Brimstone Chest", BrimstoneChest.BRIMSTONE_TABLE),
	LARRANS_CHEST("Larran's Chest", LarransChest.COMMON),
	SLAYER_MYSTERY_BOX("Slayer Mystery Box", MysteryBox.SLAYER_MYSTERY_BOX_TABLE),
	MALAKAR("Malakar Group Boss", Malakar.table),
	SOL_HEREDIT("Sol Heredit", SolHeredit.table),
	SUMMER_FROG("Summer Frog", SummerEventHandler.summerFrogLootTable),
	SUMMER_IMP("Summer Imp", SummerEventHandler.summerImpLootTable),
	SUMMER_BOSS("Summer Boss", SummerBoss.table),
	MOONS_OF_PERIL("Moons of Peril", MoonsOfPerilRewards.table),
	NIGHTMARE("Nightmare", NightmareCombat.regularTable),
	HIGH_MYSTERY_BOX("High Tier Mystery Box", MysteryBox.HIGH_TIER_MYSTERY_BOX_TABLE),
	LOW_MYSTERY_BOX("Low Tier Mystery Box", MysteryBox.LOW_TIER_MYSTERY_BOX_TABLE),
	COX_MYSTERY_BOX("CoX Mystery Box", MysteryBox.COX_MYSTERY_BOX_TABLE),
	NIGHTMARE_MYSTERY_BOX("Nightmare Mystery Box", MysteryBox.NIGHTMARE_MYSTERY_BOX_TABLE),
	ARGENTAVIS_MYSTERY_BOX("Argentavis Mystery Box", MysteryBox.ARGENTAVIS_MYSTERY_BOX_TABLE),
	NEX_MYSTERY_BOX("Nex Mystery Box", MysteryBox.NEX_MYSTERY_BOX_TABLE),
	FORGOTTEN_LOCKBOX("Forgotten lockbox", MysteryBox.FORGOTTEN_LOCKBOX),
	DONATOR_WEAPON_BOX("Donator Weapon Box", MysteryBox.DONATOR_WEAPON_BOX),
	HOLIDAY_MYSTERY_BOX("Spring Mystery Box", MysteryBox.HOLIDAY_MYSTERY_BOX),
	SUMMER_MYSTERY_BOX("Summer Mystery Box", MysteryBox.SUMMER_MYSTERY_BOX),
	DONATOR_ARMOUR_BOX("Donator Armour Box", MysteryBox.DONATOR_ARMOUR_BOX),
	VOTE_BOX("Vote Buff Streak Box", MysteryBox.VOTE_BOX),
	WILDERNESS_MYSTERY_BOX("Wilderness Mystery Box", MysteryBox.WILDERNESS_MYSTERY_BOX_TABLE),
	TOB_MYSTERY_BOX("ToB Mystery Box", MysteryBox.TOB_MYSTERY_BOX_TABLE),
	TOB_REFUND_BOX("ToB Refund Box", MysteryBox.TOB_REFUND_TABLE),
	GAUNTLET_MYSTERY_BOX("Gauntlet Mystery Box", MysteryBox.GAUNTLET_MYSTERY_BOX_TABLE),
	GWD_MYSTERY_BOX("GWD Mystery Box", MysteryBox.GWD_MYSTERY_BOX_TABLE),
	GALVEK_MYSTERY_BOX("Galvek Mystery Box", MysteryBox.GALVEK_MYSTERY_BOX_TABLE),
	ENHANCED_SUPERIOR_SLAYER_BOX("Enhanced Superior Box", MysteryBox.ENHANCED_SUPERIOR_SLAYER_BOX),
	REASON_ULTRA_POINT_BOX("Reason Ultra Point Box", MysteryBox.ULTRA_MYSTERY_BOX),

	;

	String tableName;
	LootTable table;

	LootsTables(String name, LootTable table) {
		this.tableName = name;
		this.table = table;
	}

	public static final LootsTables[] VALUES = values();

}

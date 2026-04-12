package io.ruin.model.item.actions.impl.boxes.mystery;

import discord.webhooks.notifications.RareBoxOpenHook;
import io.ruin.api.utils.Random;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.Broadcast;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MysteryBox {

	private static boolean wearingItem(Player player, int itemId, int slot) {
		return player.getEquipment().get(slot) != null && player.getEquipment().get(slot).getId() == itemId;
	}

	private static int wearingChristmasItem(Player player) {
		int boost = 0;
		if (MysteryBox.wearingItem(player, ItemID.SLED, Equipment.SLOT_WEAPON))
			boost += 2;
		if (MysteryBox.wearingItem(player, ItemID.JESTER_SCARF, Equipment.SLOT_AMULET))
			boost += 1;
		if (MysteryBox.wearingItem(player, 30601, Equipment.SLOT_HAT))
			boost += 2;
		if (MysteryBox.wearingItem(player, ItemID.SACK_OF_PRESENTS, 1))
			boost += 1;
		if (MysteryBox.wearingItem(player, 27463, 5))
			boost += 2;
		if (MysteryBox.wearingItem(player, 7927, Equipment.SLOT_RING))
			boost += 2;
		if (MysteryBox.wearingItem(player, 27871, Equipment.SLOT_WEAPON))
			boost += 1;
		if (MysteryBox.wearingItem(player, 4565, Equipment.SLOT_WEAPON))
			boost += 2;
		if (MysteryBox.wearingItem(player, 13182, Equipment.SLOT_FEET))
			boost += 1;
		if (MysteryBox.wearingItem(player, 27471, 5))
			boost += 2;
		if(player.familiar == Pet.SUMMER_DRAGON)
			boost += 3;
		if(player.familiar == Pet.SUMMER_SPIRIT)
			boost += 3;
		if (MysteryBox.wearingItem(player, 33007, Equipment.SLOT_WEAPON)) // Summer Boxing gloves
			boost += 2;
		if (MysteryBox.wearingItem(player, 11705, Equipment.SLOT_WEAPON)) // Beach boxing gloves
			boost += 2;
		if (MysteryBox.wearingItem(player, 11706, Equipment.SLOT_WEAPON)) // Beach boxing gloves
			boost += 2;
		if (MysteryBox.wearingItem(player, 33010, Equipment.SLOT_FEET)) // Summer Flippers
			boost += 2;
		if (MysteryBox.wearingItem(player, 12600, Equipment.SLOT_HAT)) // Druidic wreath
			boost += 2;
		return boost;
	}

	public static final LootTable NEX_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(ItemID.TORVA_FULL_HELM_BROKEN, 1, 3),
		new LootItem(ItemID.TORVA_PLATEBODY_BROKEN, 1, 3),
		new LootItem(ItemID.TORVA_PLATELEGS_BROKEN, 1, 3),
		new LootItem(ItemID.ZARYTE_VAMBRACES, 1, 3),
		new LootItem(ItemID.NIHIL_HORN, 1, 3),
		new LootItem(ItemID.ANCIENT_HILT, 1, 2)
	);

	public static final LootTable FORGOTTEN_LOCKBOX = new LootTable().addTable(1,
		new LootItem(ItemID.DRAGON_BOLTS_UNF, 125, 75),
		new LootItem(ItemID.DRAGON_DART, 125, 75),
		new LootItem(ItemID.DRAGON_DART, 80, 60),
		new LootItem(ItemID.DRAGON_PLATELEGS + 1, 5, 60),
		new LootItem(ItemID.DRAGON_PLATESKIRT + 1, 5, 60),
		new LootItem(ItemID.COINS_995, 10_000_000, 40),
		new LootItem(30750, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30753, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30756, 1, 1).broadcast(Broadcast.GLOBAL)
		);

	public static final LootTable HOLIDAY_MYSTERY_BOX = new LootTable().addTable(1,
		new LootItem(2528, 1, 12),
		new LootItem(59525, 1, 12),
		new LootItem(59524, 1, 7),
		new LootItem(30448, 1, 3),
		new LootItem(30462, 1, 11),
		new LootItem(11670, 1, 6),
		new LootItem(22092, 1, 6),
		new LootItem(21006, 1, 7),
		new LootItem(12424, 1, 7),
		new LootItem(20014, 1, 7),
		new LootItem(12437, 1, 7),
		new LootItem(12426, 1, 7),
		new LootItem(20011, 1, 7),
		new LootItem(12422, 1, 7),
		new LootItem(24417, 1, 7),
		new LootItem(24511, 1, 7),
		new LootItem(4079, 1, 7),
		new LootItem(4566, 1, 7),
		new LootItem(12817, 1, 4).broadcast(Broadcast.GLOBAL),
		new LootItem(20997, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(22486, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(12399, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(11847, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(11862, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(11863, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(27226, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(27229, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(27232, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(27277, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(26219, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(26376, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(26378, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(26380, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(26374, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(26235, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(1053, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(1057, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(1055, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(962, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1046, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1044, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1048, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1040, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1038, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1042, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30625, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(32001, 1, 4).broadcast(Broadcast.GLOBAL),
		new LootItem(27248, 1, 2).broadcast(Broadcast.GLOBAL),
		// Added new items
		new LootItem(7927, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(27871, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(4565, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(13182, 1, 1).broadcast(Broadcast.GLOBAL)
	);
	public static final LootTable SUMMER_MYSTERY_BOX = new LootTable().addTable(1,
		// Added new items
		new LootItem(33009, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(33008, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(11705, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(11706, 1, 2).broadcast(Broadcast.GLOBAL),

		new LootItem(2528, 1, 12),
		new LootItem(59525, 1, 12),
		new LootItem(59524, 1, 7),
		new LootItem(30448, 1, 3),
		new LootItem(30462, 1, 11),
		new LootItem(11670, 1, 6),
		new LootItem(22092, 1, 6),
		new LootItem(21006, 1, 7),
		new LootItem(12424, 1, 7),
		new LootItem(20014, 1, 7),
		new LootItem(12437, 1, 7),
		new LootItem(12426, 1, 7),
		new LootItem(20011, 1, 7),
		new LootItem(12422, 1, 7),
		new LootItem(24417, 1, 7),
		new LootItem(24511, 1, 7),
		new LootItem(4079, 1, 7),
		new LootItem(4566, 1, 7),
		new LootItem(12817, 1, 4).broadcast(Broadcast.GLOBAL),
		new LootItem(20997, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(22486, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(12399, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(11847, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(11862, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(11863, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(27226, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(27229, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(27232, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(27277, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(26219, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(26376, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(26378, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(26380, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(26374, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(26235, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(1053, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(1057, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(1055, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(962, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1046, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1044, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1048, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1040, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1038, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1042, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30625, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(32001, 1, 4).broadcast(Broadcast.GLOBAL),
		new LootItem(27248, 1, 2).broadcast(Broadcast.GLOBAL)
	);
	public static final LootTable DONATOR_WEAPON_BOX = new LootTable().addTable(1,
		new LootItem(ItemID.ABYSSAL_BLUDGEON, 1, 7),
		new LootItem(ItemID.DRAGON_WARHAMMER, 1, 7),
		new LootItem(ItemID.DRAGON_CLAWS, 1, 7),
		new LootItem(ItemID.DRAGON_HUNTER_CROSSBOW, 1, 7),
		new LootItem(ItemID.ARMADYL_GODSWORD, 1, 7),
		new LootItem(ItemID.BANDOS_GODSWORD, 1, 7),
		new LootItem(ItemID.ZAMORAK_GODSWORD, 1, 7),
		new LootItem(ItemID.SARADOMIN_GODSWORD, 1, 7),
		new LootItem(ItemID.ARMADYL_CROSSBOW, 1, 7),
		new LootItem(ItemID.TOXIC_BLOWPIPE, 1, 7),
		new LootItem(ItemID.DRAGON_HUNTER_LANCE, 1, 7),
		new LootItem(ItemID.VIGGORAS_CHAINMACE, 1, 7),
		new LootItem(ItemID.CRAWS_BOW, 1, 7),
		new LootItem(ItemID.THAMMARONS_SCEPTRE, 1, 7),
		new LootItem(ItemID.URSINE_CHAINMACE, 1, 6),
		new LootItem(ItemID.ACCURSED_SCEPTRE, 1, 6),
		new LootItem(ItemID.WEBWEAVER_BOW, 1, 6),
		new LootItem(ItemID.VENATOR_BOW_UNCHARGED, 1, 6).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.NIGHTMARE_STAFF, 1, 7),
		new LootItem(ItemID.ELDRITCH_NIGHTMARE_STAFF, 1, 6),
		new LootItem(ItemID.VOLATILE_NIGHTMARE_STAFF, 1, 6),
		new LootItem(ItemID.SOULREAPER_AXE, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.HARMONISED_NIGHTMARE_STAFF, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.CORRUPTED_WARHAMMER, 1, 8).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.DRAGONHUNTER_STAFF, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(24551, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(25867, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.ZARYTE_CROSSBOW, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.OSMUMTENS_FANG, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.KODAI_WAND, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.INQUISITORS_MACE, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.GHRAZI_RAPIER, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.SANGUINESTI_STAFF_UNCHARGED, 1, 5).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.TWISTED_BOW, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.TUMEKENS_SHADOW, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.SCYTHE_OF_VITUR, 1, 1).broadcast(Broadcast.GLOBAL)
	);
	public static final LootTable DONATOR_ARMOUR_BOX = new LootTable().addTable(1,
		new LootItem(ItemID.BANDOS_TASSETS, 1, 7),
		new LootItem(ItemID.BANDOS_CHESTPLATE, 1, 7),
		new LootItem(ItemID.ARMADYL_CHAINSKIRT, 1, 7),
		new LootItem(ItemID.ARMADYL_CHESTPLATE, 1, 7),
		new LootItem(ItemID.AHRIMS_ARMOUR_SET, 1, 7),
		new LootItem(ItemID.TORAGS_ARMOUR_SET, 1, 7),
		new LootItem(ItemID.VERACS_ARMOUR_SET, 1, 7),
		new LootItem(ItemID.DHAROKS_ARMOUR_SET, 1, 7),
		new LootItem(ItemID.KARILS_ARMOUR_SET, 1, 7),
		new LootItem(ItemID.CRYSTAL_HELM, 1, 7),
		new LootItem(ItemID.CRYSTAL_BODY, 1, 7),
		new LootItem(ItemID.CRYSTAL_LEGS, 1, 7),
		new LootItem(ItemID.GUTHANS_ARMOUR_SET, 1, 7),
		new LootItem(ItemID.ARCANE_SPIRIT_SHIELD, 1, 8),
		new LootItem(ItemID.SPECTRAL_SPIRIT_SHIELD, 1, 8),
		new LootItem(ItemID.DRAGONFIRE_SHIELD, 1, 9),
		new LootItem(ItemID.DRAGONFIRE_WARD, 1, 9),
		new LootItem(ItemID.ANCIENT_WYVERN_SHIELD, 1, 9),
		new LootItem(ItemID.SERPENTINE_HELM, 1, 10),
		new LootItem(ItemID.SERPENTINE_HELM, 1, 10),
		new LootItem(ItemID.FEROCIOUS_GLOVES, 1, 6),
		new LootItem(ItemID.PEGASIAN_BOOTS, 1, 5),
		new LootItem(ItemID.PRIMORDIAL_BOOTS, 1, 5),
		new LootItem(ItemID.ETERNAL_BOOTS, 1, 5),
		new LootItem(ItemID.DEVOUT_BOOTS, 1, 7),
		new LootItem(ItemID.AMULET_OF_TORTURE, 1, 4),
		new LootItem(ItemID.NECKLACE_OF_ANGUISH, 1, 4),
		new LootItem(ItemID.RING_OF_SUFFERING_I, 1, 4),
		new LootItem(ItemID.TORMENTED_BRACELET, 1, 4),
		new LootItem(ItemID.MERLINS_CLOAK, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.VESTAS_CHAINBODY, 1, 7),
		new LootItem(ItemID.VESTAS_PLATESKIRT, 1, 7),
		new LootItem(ItemID.STATIUSS_FULL_HELM, 1, 7),
		new LootItem(ItemID.STATIUSS_PLATEBODY, 1, 7),
		new LootItem(ItemID.STATIUSS_PLATELEGS, 1, 7),
		new LootItem(ItemID.ZURIELS_HOOD, 1, 7),
		new LootItem(ItemID.ZURIELS_ROBE_BOTTOM, 1, 7),
		new LootItem(ItemID.ZURIELS_ROBE_TOP, 1, 7),
		new LootItem(ItemID.MORRIGANS_COIF, 1, 7),
		new LootItem(ItemID.MORRIGANS_LEATHER_BODY, 1, 7),
		new LootItem(ItemID.MORRIGANS_LEATHER_CHAPS, 1, 7),
		new LootItem(ItemID.ELITE_VOID_SET, 1, 7),
		new LootItem(ItemID.MAGUS_RING, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.ULTOR_RING, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.VENATOR_RING, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.BELLATOR_RING, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.ELYSIAN_SPIRIT_SHIELD, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.DIVINE_SPIRIT_SHIELD, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.JUSTICIAR_FACEGUARD, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.JUSTICIAR_CHESTGUARD, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.JUSTICIAR_LEGGUARDS, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.INQUISITORS_GREAT_HELM, 1, 5),
		new LootItem(ItemID.INQUISITORS_HAUBERK, 1, 5),
		new LootItem(ItemID.INQUISITORS_PLATESKIRT, 1, 5),
		new LootItem(ItemID.TORVA_FULL_HELM, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.TORVA_PLATEBODY, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.TORVA_PLATELEGS, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.ZARYTE_VAMBRACES, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.ANCESTRAL_HAT, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.ANCESTRAL_ROBE_TOP, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.ANCESTRAL_ROBE_BOTTOM, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.VIRTUS_MASK, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.VIRTUS_ROBE_TOP, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.VIRTUS_ROBE_BOTTOM, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.MASORI_MASK, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.MASORI_BODY, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.MASORI_CHAPS, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.OPHIDIAN_MASK, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.OPHIDIAN_BODY, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.OPHIDIAN_CHAPS, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.AVERNIC_DEFENDER, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.ELIDINIS_WARD, 1, 3).broadcast(Broadcast.GLOBAL)

	);

	public static final LootTable VOTE_BOX = new LootTable().addTable(1,
		new LootItem(ItemID.DOUBLE_EXP_SCROLL, 1, 15),
		new LootItem(ItemID.DOUBLE_DROP_SCROLL, 1, 12),
		new LootItem(ItemID.PERK_TASK_SKIP_SCROLL, 1, 10),
		new LootItem(ItemID.SLAYER_SKIP_SCROLL, 1, 8),
		new LootItem(ItemID.INSTANCE_TOKEN, 1, 7),
		new LootItem(ItemID.DONATOR_MYSTERY_BOX, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.VOTE_POINT_TICKET, 20, 1).broadcast(Broadcast.GLOBAL)
	);

	public static final LootTable WILDERNESS_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(ItemID.VESTAS_LONGSWORD, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.VESTAS_SPEAR, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.VESTAS_CHAINBODY, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.VESTAS_PLATESKIRT, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.STATIUSS_FULL_HELM, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.STATIUSS_PLATEBODY, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.STATIUSS_PLATELEGS, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.MORRIGANS_COIF, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.MORRIGANS_LEATHER_BODY, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.MORRIGANS_LEATHER_CHAPS, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.ZURIELS_STAFF, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.ZURIELS_HOOD, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.ZURIELS_ROBE_TOP, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.ZURIELS_ROBE_BOTTOM, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.SKULL_OF_VETION, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.CALLISTO_CLAWS, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.VENENATIS_FANGS, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.CRAWS_BOW_U, 1, 3),
		new LootItem(ItemID.THAMMARONS_SCEPTRE_U, 1, 3),
		new LootItem(ItemID.VIGGORAS_CHAINMACE_U, 1, 3),
		new LootItem(ItemID.DRAGON_PICKAXE, 1, 9),
		new LootItem(ItemID.TYRANNICAL_RING, 1, 9),
		new LootItem(ItemID.TREASONOUS_RING, 1, 9),
		new LootItem(ItemID.RING_OF_THE_GODS, 1, 9),
		new LootItem(ItemID.MALEDICTION_WARD, 1, 11),
		new LootItem(ItemID.ODIUM_WARD, 1, 11),
		new LootItem(ItemID.DAGON_HAI_HAT, 1, 11),
		new LootItem(ItemID.DAGON_HAI_ROBE_TOP, 1, 11),
		new LootItem(ItemID.DAGON_HAI_ROBE_BOTTOM, 1, 11)

	);

	public static final LootTable TOB_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(ItemID.SCYTHE_OF_VITUR_UNCHARGED, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.GHRAZI_RAPIER, 1, 5),
		new LootItem(ItemID.SANGUINESTI_STAFF_UNCHARGED, 1, 5),
		new LootItem(ItemID.JUSTICIAR_FACEGUARD, 1, 5),
		new LootItem(ItemID.JUSTICIAR_CHESTGUARD, 1, 7),
		new LootItem(ItemID.JUSTICIAR_LEGGUARDS, 1, 7),
		new LootItem(ItemID.AVERNIC_DEFENDER_HILT, 1, 8)
	);

	public static final LootTable TOB_REFUND_TABLE = new LootTable().addTable(1,
		new LootItem(ItemID.SCYTHE_OF_VITUR_UNCHARGED, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.GHRAZI_RAPIER, 1, 14).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.SANGUINESTI_STAFF_UNCHARGED, 1, 14).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.JUSTICIAR_FACEGUARD, 1, 14).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.JUSTICIAR_CHESTGUARD, 1, 14).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.JUSTICIAR_LEGGUARDS, 1, 14).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.AVERNIC_DEFENDER_HILT, 1, 30).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.DEATH_RUNE, 1500, 30),
		new LootItem(ItemID.BLOOD_RUNE, 1500, 30),
		new LootItem(ItemID.SWAMP_TAR, 1500, 30),
		new LootItem(ItemID.COAL + 1, 1500, 30),
		new LootItem(ItemID.GOLD_ORE + 1, 900, 30),
		new LootItem(ItemID.MOLTEN_GLASS + 1, 600, 30),
		new LootItem(ItemID.ADAMANTITE_ORE + 1, 450, 30),
		new LootItem(ItemID.RUNITE_ORE + 1, 210, 30),
		new LootItem(ItemID.WINE_OF_ZAMORAK + 1, 150, 30),
		new LootItem(ItemID.POTATO_CACTUS + 1, 180, 30),
		new LootItem(ItemID.GRIMY_CADANTINE + 1, 150, 30),
		new LootItem(ItemID.GRIMY_AVANTOE + 1, 150, 30),
		new LootItem(ItemID.GRIMY_TOADFLAX + 1, 120, 30),
		new LootItem(ItemID.GRIMY_KWUARM + 1, 120, 30),
		new LootItem(ItemID.GRIMY_IRIT_LEAF + 1, 120, 30),
		new LootItem(ItemID.GRIMY_RANARR_WEED + 1, 120, 30),
		new LootItem(ItemID.GRIMY_SNAPDRAGON + 1, 90, 30),
		new LootItem(ItemID.GRIMY_LANTADYME + 1, 90, 30),
		new LootItem(ItemID.GRIMY_DWARF_WEED + 1, 85, 30),
		new LootItem(ItemID.GRIMY_TORSTOL + 1, 75, 30),
		new LootItem(ItemID.BATTLESTAFF + 1, 50, 30),
		new LootItem(ItemID.MAHOGANY_SEED, 32, 30),
		new LootItem(ItemID.RUNE_BATTLEAXE + 1, 12, 30),
		new LootItem(ItemID.RUNE_PLATEBODY + 1, 12, 30),
		new LootItem(ItemID.RUNE_CHAINBODY + 1, 12, 30),
		new LootItem(ItemID.PALM_TREE_SEED, 9, 30),
		new LootItem(ItemID.YEW_SEED, 9, 30),
		new LootItem(ItemID.MAGIC_SEED, 9, 30)
	);

	public static final LootTable ULTRA_MYSTERY_BOX = new LootTable().addTable(1,
		new LootItem(ItemID.GRANITE_CANNONBALL, 100000, 20),
		new LootItem(20718, 300, 9), //Burnt page
		new LootItem(23951, 150, 20), //Enhanced crystal key
		new LootItem(23962, 2000, 25), //Crystal shards
		new LootItem(608, 15, 25), //5% scroll
		new LootItem(30459, 12, 25), //Double drop scroll
		new LootItem(30456, 10, 20), //Damage boost scroll
		new LootItem(30457, 10, 25), //Damage reduction scroll
		new LootItem(30460, 12, 20), //Double exp scroll
		new LootItem(2528, 1, 4).broadcast(Broadcast.GLOBAL), //Donator lamp
		new LootItem(30581, 1, 11), //Large exp lamp
		new LootItem(30580, 1, 11), //Med exp lamp
		new LootItem(30531, 1, 6), //Aoe swipe sigil
		new LootItem(30536, 1, 5), //Damage for hire high
		new LootItem(30537, 1, 5), //Venom tipped sigil
		new LootItem(30534, 1, 3), //Double hit sigil
		new LootItem(22092, 1, 4), //Pet token
		new LootItem(30629, 20, 10), //Slay pick scroll
		new LootItem(30430, 2, 8), //Reason pt mystery box 2x
		new LootItem(2677, 100, 5), //Easy clue reward scroll
		new LootItem(2801, 100, 5), //Medium clue reward scroll
		new LootItem(2722, 75, 5), //Hard clue reward scroll
		new LootItem(12073, 50, 5), //Elite clue reward scroll
		new LootItem(19835, 25, 5), //Master clue reward scroll
		new LootItem(30461, 1, 4), //Donator mystery box
		new LootItem(995, 500_000_000, 6), //1b coins
		new LootItem(995, 1_000_000_000, 1).broadcast(Broadcast.GLOBAL)
	);

	public static final LootTable ENHANCED_SUPERIOR_SLAYER_BOX = new LootTable().addTable(1,
		new LootItem(30543, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30541, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30540, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30538, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30534, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30531, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30533, 1, 1).broadcast(Broadcast.GLOBAL)
	);

	public static final LootTable GAUNTLET_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(23995, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(25865, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(23971, 1, 5),
		new LootItem(23975, 1, 5),
		new LootItem(23979, 1, 5)
	);

	public static final LootTable GWD_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(ItemID.BANDOS_BOOTS, 1, 1),
		new LootItem(ItemID.BANDOS_CHESTPLATE, 1, 1),
		new LootItem(ItemID.BANDOS_TASSETS, 1, 1),
		new LootItem(ItemID.BANDOS_HILT, 1, 1),
		new LootItem(ItemID.SARADOMIN_SWORD, 1, 1),
		new LootItem(ItemID.SARADOMIN_HILT, 1, 1),
		new LootItem(ItemID.ARMADYL_CROSSBOW, 1, 1),
		new LootItem(ItemID.SARADOMINS_LIGHT, 1, 1),
		new LootItem(ItemID.STAFF_OF_THE_DEAD, 1, 1),
		new LootItem(ItemID.ZAMORAKIAN_HASTA, 1, 1),
		new LootItem(ItemID.ZAMORAK_HILT, 1, 1),
		new LootItem(ItemID.ARMADYL_HELMET, 1, 1),
		new LootItem(ItemID.ARMADYL_CHESTPLATE, 1, 1),
		new LootItem(ItemID.ARMADYL_CHAINSKIRT, 1, 1),
		new LootItem(ItemID.ARMADYL_HILT, 1, 1)
	);

	public static final LootTable GALVEK_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(30593, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30592, 1, 9),
		new LootItem(30591, 1, 9)
	);
	public static final LootTable NIGHTMARE_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(24417, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(24511, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(24514, 1, 5),
		new LootItem(24517, 1, 5),
		new LootItem(24419, 1, 8),
		new LootItem(24420, 1, 8),
		new LootItem(24421, 1, 8),
		new LootItem(24422, 1, 8)
	);

	public static final LootTable ARGENTAVIS_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(30595, 1, 1),
		new LootItem(30594, 1, 1),
		new LootItem(30513, 1, 1)
	);
	public static final LootTable COX_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(20997, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.KODAI_INSIGNIA, 1, 4),
		new LootItem(ItemID.ELDER_MAUL, 1, 4),
		new LootItem(ItemID.DRAGON_CLAWS, 1, 8),
		new LootItem(ItemID.ANCESTRAL_HAT, 1, 8),
		new LootItem(ItemID.ANCESTRAL_ROBE_TOP, 1, 8),
		new LootItem(ItemID.ANCESTRAL_ROBE_BOTTOM, 1, 8),
		new LootItem(ItemID.DRAGON_HUNTER_CROSSBOW, 1, 8),
		new LootItem(ItemID.TWISTED_BUCKLER, 1, 8),
		new LootItem(ItemID.DINHS_BULWARK, 1, 8),
		new LootItem(ItemID.ARCANE_PRAYER_SCROLL, 1, 12),
		new LootItem(ItemID.DEXTEROUS_PRAYER_SCROLL, 1, 12)
	);
	public static final LootTable MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(23528, 1, 100),
		new LootItem(11926, 1, 100),
		new LootItem(11924, 1, 100),
		new LootItem(13233, 1, 100),
		new LootItem(21031, 1, 100),
		new LootItem(13243, 1, 100),
		new LootItem(13241, 1, 100),
		new LootItem(21793, 1, 100),
		new LootItem(21795, 1, 100),
		new LootItem(21791, 1, 100),
		new LootItem(6570, 1, 100),
		new LootItem(13073, 1, 100),
		new LootItem(13072, 1, 100),
		new LootItem(12863, 1, 100),
		new LootItem(19634, 1, 100),
		new LootItem(11920, 1, 100),
		new LootItem(21028, 1, 100),
		new LootItem(22994, 1, 100),
		new LootItem(6924, 1, 100),
		new LootItem(6916, 1, 100),
		new LootItem(6918, 1, 100),
		new LootItem(6920, 1, 100),
		new LootItem(6889, 1, 100),
		new LootItem(13442, 5000, 100),
		new LootItem(12696, 1000, 100),
		new LootItem(12626, 1000, 100),
		new LootItem(12596, 1, 100),
		new LootItem(11128, 1, 100),
		new LootItem(22557, 1, 100),
		new LootItem(22552, 1, 100),
		new LootItem(12804, 1, 100),
		new LootItem(20716, 1, 100),
		new LootItem(11959, 1000, 100),
		new LootItem(13202, 1, 100),
		new LootItem(12692, 1, 100),
		new LootItem(12691, 1, 100),
		new LootItem(11905, 1, 100),
		new LootItem(19675, 1, 100),
		new LootItem(21902, 1, 100),
		new LootItem(22109, 1, 100),
		new LootItem(19478, 1, 100),
		new LootItem(2579, 1, 100),
		new LootItem(12598, 1, 100),
		new LootItem(12002, 1, 100),
		new LootItem(4151, 1, 100),
		new LootItem(11838, 1, 100),
		new LootItem(10551, 1, 100),
		new LootItem(11770, 1, 100),
		new LootItem(11772, 1, 100),
		new LootItem(22975, 1, 100),
		new LootItem(11771, 1, 100),
		new LootItem(11773, 1, 100),
		new LootItem(6585, 1, 100),
		new LootItem(11826, 1, 100),
		new LootItem(11836, 1, 100),
		new LootItem(22634, 200, 100),
		new LootItem(22636, 150, 100),
		new LootItem(11808, 1, 100),


		new LootItem(22547, 1, 40),
		new LootItem(22328, 1, 40),
		new LootItem(22327, 1, 40),
		new LootItem(19481, 1, 40),
		new LootItem(22954, 1, 40),
		new LootItem(21633, 1, 40),
		new LootItem(22002, 1, 40),
		new LootItem(11283, 1, 40),
		new LootItem(13263, 1, 40),
		new LootItem(13265, 1, 40),
		new LootItem(22981, 1, 40),
		new LootItem(11284, 1, 40),
		new LootItem(11785, 1, 40),
		new LootItem(12004, 1, 40),
		new LootItem(30476, 2000, 40),
		new LootItem(12873, 1, 40),
		new LootItem(12883, 1, 40),
		new LootItem(12875, 1, 40),
		new LootItem(12879, 1, 40),
		new LootItem(12881, 1, 40),
		new LootItem(12877, 1, 40),
		new LootItem(21015, 1, 40),
		new LootItem(13652, 1, 40),
		new LootItem(21079, 1, 40),
		new LootItem(21034, 1, 40),
		new LootItem(21000, 1, 40),
		new LootItem(12927, 1, 40),
		new LootItem(12932, 1, 40),
		new LootItem(12922, 1, 40),
		new LootItem(11791, 1, 40),
		new LootItem(11830, 1, 40),
		new LootItem(11828, 1, 40),
		new LootItem(11832, 1, 40),
		new LootItem(11834, 1, 40),
		new LootItem(11802, 1, 40),
		new LootItem(24777, 1, 40),


		new LootItem(10332, 1, 20),
		new LootItem(10330, 1, 20),
		new LootItem(10334, 1, 20),
		new LootItem(10336, 1, 20),
		new LootItem(10344, 1, 20),
		new LootItem(10342, 1, 20),
		new LootItem(2577, 1, 20),
		new LootItem(24271, 1, 20),
		new LootItem(22644, 1, 20),
		new LootItem(22641, 1, 20),
		new LootItem(22638, 1, 20),
		new LootItem(22647, 1, 20),
		new LootItem(22656, 1, 20),
		new LootItem(22653, 1, 20),
		new LootItem(22650, 1, 20),
		new LootItem(22631, 1, 20),
		new LootItem(22628, 1, 20),
		new LootItem(22625, 1, 20),
		new LootItem(22610, 1, 20),
		new LootItem(22613, 1, 20),
		new LootItem(22619, 1, 20),
		new LootItem(22616, 1, 20),
		new LootItem(22477, 1, 20),
		new LootItem(22481, 1, 20),
		new LootItem(22324, 1, 20),
		new LootItem(22326, 1, 20),
		new LootItem(21295, 1, 20),
		new LootItem(21003, 1, 20),
		new LootItem(13235, 1, 20),
		new LootItem(13237, 1, 20),
		new LootItem(13239, 1, 20),
		new LootItem(24421, 1, 20),
		new LootItem(24420, 1, 20),
		new LootItem(24419, 1, 20),
		new LootItem(24517, 1, 20),
		new LootItem(24514, 1, 20),
		new LootItem(24422, 1, 20),
		new LootItem(20724, 1, 20),
		new LootItem(11806, 1, 20),
		new LootItem(11804, 1, 20),
		new LootItem(13576, 1, 20),
		new LootItem(19710, 1, 20),
		new LootItem(19544, 1, 20),
		new LootItem(19547, 1, 20),
		new LootItem(19553, 1, 20),
		new LootItem(22978, 1, 20),
		new LootItem(21012, 1, 20),
		new LootItem(21024, 1, 20),
		new LootItem(21021, 1, 20),
		new LootItem(21018, 1, 20),
		new LootItem(12821, 1, 20),
		new LootItem(12825, 1, 20),
		new LootItem(5607, 1, 20),
		new LootItem(5609, 1, 20),
		new LootItem(5608, 1, 20),

		new LootItem(21006, 1, 5),
		new LootItem(10340, 1, 5),
		new LootItem(10338, 1, 5),
		new LootItem(10346, 1, 5),
		new LootItem(10348, 1, 5),
		new LootItem(10350, 1, 5),
		new LootItem(10352, 1, 5),
		new LootItem(23339, 1, 5),
		new LootItem(23336, 1, 5),
		new LootItem(23345, 1, 5),
		new LootItem(23342, 1, 5),
		new LootItem(12424, 1, 5),
		new LootItem(20014, 1, 5),
		new LootItem(12437, 1, 5),
		new LootItem(12426, 1, 5),
		new LootItem(20011, 1, 5),
		new LootItem(12422, 1, 5),
		new LootItem(24417, 1, 5),
		new LootItem(24511, 1, 5),
		new LootItem(4079, 1, 5),
		new LootItem(4566, 1, 5),
		new LootItem(24777, 1, 5),

		new LootItem(12817, 1, 4).broadcast(Broadcast.GLOBAL),
		new LootItem(24325, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(20997, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(22486, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(12399, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(11847, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(1053, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(1057, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(1055, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(962, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1046, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1044, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1048, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1040, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1038, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1042, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(11862, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(13343, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(11863, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(27226, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(27229, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(27232, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(27275, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(26219, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(26376, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(26378, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(26380, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(26374, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(26235, 1, 1).broadcast(Broadcast.GLOBAL)

	);
	public static final LootTable ADVANCED_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(11808, 1, 40),
		new LootItem(22547, 1, 40),
		new LootItem(22328, 1, 40),
		new LootItem(22327, 1, 40),
		new LootItem(19481, 1, 40),
		new LootItem(22954, 1, 40),
		new LootItem(21633, 1, 40),
		new LootItem(22002, 1, 40),
		new LootItem(11283, 1, 40),
		new LootItem(13263, 1, 40),
		new LootItem(13265, 1, 40),
		new LootItem(22981, 1, 40),
		new LootItem(11284, 1, 40),
		new LootItem(11785, 1, 40),
		new LootItem(12004, 1, 40),
		new LootItem(30476, 2000, 40),
		new LootItem(12873, 1, 40),
		new LootItem(12883, 1, 40),
		new LootItem(12875, 1, 40),
		new LootItem(12879, 1, 40),
		new LootItem(12881, 1, 40),
		new LootItem(12877, 1, 40),
		new LootItem(21015, 1, 40),
		new LootItem(13652, 1, 40),
		new LootItem(21079, 1, 40),
		new LootItem(21034, 1, 40),
		new LootItem(21000, 1, 40),
		new LootItem(12927, 1, 40),
		new LootItem(12932, 1, 40),
		new LootItem(12922, 1, 40),
		new LootItem(11791, 1, 40),
		new LootItem(11830, 1, 40),
		new LootItem(11828, 1, 40),
		new LootItem(11832, 1, 40),
		new LootItem(11834, 1, 40),
		new LootItem(11802, 1, 40),
		new LootItem(24777, 1, 40),


		new LootItem(10332, 1, 20),
		new LootItem(10330, 1, 20),
		new LootItem(10334, 1, 20),
		new LootItem(10336, 1, 20),
		new LootItem(10344, 1, 20),
		new LootItem(10342, 1, 20),
		new LootItem(2577, 1, 20),
		new LootItem(24271, 1, 20),
		new LootItem(22644, 1, 20),
		new LootItem(22641, 1, 20),
		new LootItem(22638, 1, 20),
		new LootItem(22647, 1, 20),
		new LootItem(22656, 1, 20),
		new LootItem(22653, 1, 20),
		new LootItem(22650, 1, 20),
		new LootItem(22631, 1, 20),
		new LootItem(22628, 1, 20),
		new LootItem(22625, 1, 20),
		new LootItem(22610, 1, 20),
		new LootItem(22613, 1, 20),
		new LootItem(22619, 1, 20),
		new LootItem(22616, 1, 20),
		new LootItem(22477, 1, 20),
		new LootItem(22481, 1, 20),
		new LootItem(22324, 1, 20),
		new LootItem(22326, 1, 20),
		new LootItem(21295, 1, 20),
		new LootItem(21003, 1, 20),
		new LootItem(13235, 1, 20),
		new LootItem(13237, 1, 20),
		new LootItem(13239, 1, 20),
		new LootItem(24421, 1, 20),
		new LootItem(24420, 1, 20),
		new LootItem(24419, 1, 20),
		new LootItem(24517, 1, 20),
		new LootItem(24514, 1, 20),
		new LootItem(24422, 1, 20),
		new LootItem(20724, 1, 20),
		new LootItem(11806, 1, 20),
		new LootItem(11804, 1, 20),
		new LootItem(13576, 1, 20),
		new LootItem(19710, 1, 20),
		new LootItem(19544, 1, 20),
		new LootItem(19547, 1, 20),
		new LootItem(19553, 1, 20),
		new LootItem(22978, 1, 20),
		new LootItem(21012, 1, 20),
		new LootItem(21024, 1, 20),
		new LootItem(21021, 1, 20),
		new LootItem(21018, 1, 20),
		new LootItem(12821, 1, 20),
		new LootItem(12825, 1, 20),
		new LootItem(5607, 1, 20),
		new LootItem(5609, 1, 20),
		new LootItem(5608, 1, 20),

		new LootItem(21006, 1, 5),
		new LootItem(10340, 1, 5),
		new LootItem(10338, 1, 5),
		new LootItem(10346, 1, 5),
		new LootItem(10348, 1, 5),
		new LootItem(10350, 1, 5),
		new LootItem(10352, 1, 5),
		new LootItem(23339, 1, 5),
		new LootItem(23336, 1, 5),
		new LootItem(23345, 1, 5),
		new LootItem(23342, 1, 5),
		new LootItem(12424, 1, 5),
		new LootItem(20014, 1, 5),
		new LootItem(12437, 1, 5),
		new LootItem(12426, 1, 5),
		new LootItem(20011, 1, 5),
		new LootItem(12422, 1, 5),
		new LootItem(24417, 1, 5),
		new LootItem(24511, 1, 5),
		new LootItem(4079, 1, 5),
		new LootItem(4566, 1, 5),
		new LootItem(24777, 1, 5),

		new LootItem(12817, 1, 4).broadcast(Broadcast.GLOBAL),
		new LootItem(24325, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(20997, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(22486, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(12399, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(11847, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(1053, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(1057, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(1055, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(962, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1046, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1044, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1048, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1040, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1038, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(1042, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(11862, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(13343, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(11863, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(27226, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(27229, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(27232, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(27275, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(26219, 1, 2).broadcast(Broadcast.GLOBAL),
		new LootItem(26376, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(26378, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(26380, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(26374, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(26235, 1, 3).broadcast(Broadcast.GLOBAL)

	);
	public static final LootTable LOW_TIER_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(1128, 15, 100),
		new LootItem(1094, 15, 100),
		new LootItem(1080, 15, 100),
		new LootItem(1632, 20, 100),
		new LootItem(1618, 50, 100),
		new LootItem(1620, 100, 100),
		new LootItem(1622, 150, 100),
		new LootItem(1624, 150, 100),
		new LootItem(1776, 500, 100),
		new LootItem(8779, 200, 100),
		new LootItem(8781, 150, 100),
		new LootItem(8783, 100, 100),
		new LootItem(6694, 200, 100),
		new LootItem(212, 100, 100),
		new LootItem(210, 100, 100),
		new LootItem(208, 30, 100),
		new LootItem(3052, 20, 100),
		new LootItem(995, 5000000, 100),
		new LootItem(445, 200, 100),
		new LootItem(454, 500, 100),
		new LootItem(448, 150, 100),
		new LootItem(450, 100, 100),
		new LootItem(452, 10, 100),
		new LootItem(2360, 150, 100),
		new LootItem(2362, 50, 100),
		new LootItem(2364, 25, 100),
		new LootItem(2, 500, 100),
		new LootItem(560, 500, 100),
		new LootItem(562, 500, 100),
		new LootItem(565, 200, 100),
		new LootItem(561, 400, 100),
		new LootItem(823, 500, 100),
		new LootItem(824, 200, 100),
		new LootItem(11232, 200, 100),
		new LootItem(11212, 200, 100),
		new LootItem(232, 200, 100),
		new LootItem(224, 200, 100),
		new LootItem(537, 50, 100),
		new LootItem(4588, 10, 100),
		new LootItem(384, 200, 100),
		new LootItem(30461, 1, 2).broadcast(Broadcast.GLOBAL)
	);

	public static final LootTable HIGH_TIER_MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(995, 10000000, 22), // Coins
		new LootItem(11944, 150, 25), //Lava dragon bones
		new LootItem(30570, 8, 15), //perk point scrolls
		new LootItem(21905, 400, 20), //Dragon bolts
		new LootItem(2722, 15, 21), //Hard clue reward scroll
		new LootItem(2510, 250, 17), //Black dragon leather
		new LootItem(21880, 1500, 25), //Wrath runes
		new LootItem(989, 20, 20), //Crystal keys
		new LootItem(5316, 8, 20), //Magic seed
		new LootItem(537, 250, 20), //Dragon bones
		new LootItem(22781, 200, 20), //Wyrm bones
		new LootItem(3025, 100, 20), //Super restores
		new LootItem(6686, 100, 20), //Saradomin Brews
		new LootItem(2362, 275, 25), //Adamant bars
		new LootItem(2364, 160, 22), //Runite bars
		new LootItem(6694, 150, 21),  //Crushed nest
		new LootItem(8779, 300, 17), //Oak plank
		new LootItem(8783, 250, 18), // Mahog plank
		new LootItem(1632, 250, 21), // Uncut dragonstone
		new LootItem(445, 750, 16), //Gold ore
		new LootItem(11212, 500, 15), //Dragon arrow
		new LootItem(13440, 300, 17), //Raw anglerfish
		new LootItem(390, 350, 17), // Raw manta ray
		new LootItem(452, 250, 19), // Runite ore
		new LootItem(450, 400, 19), // Addy ore
		new LootItem(1514, 400, 19), // Magic logs
		new LootItem(5315, 15, 19), // Yew seed
		new LootItem(22877, 15, 19), // Dragonfruit tree seed
		new LootItem(5304, 30, 16), // Torstol seed
		new LootItem(232, 500, 17), // Snape grass
		new LootItem(3139, 400, 17), // Potato cactus
		new LootItem(224, 600, 18), // Red spiders eggs
		new LootItem(2971, 350, 14), // Mort myre fungus
		new LootItem(23083, 25, 21),  // Brimstone keys
		new LootItem(23490, 12, 20),   // Larran's key
		new LootItem(22092, 1, 7).broadcast(Broadcast.GLOBAL), // Pet token
		new LootItem(20724, 1, 2).broadcast(Broadcast.GLOBAL),   // Imbued Heart
		new LootItem(30503, 1, 2).broadcast(Broadcast.GLOBAL), // Imbued Melee
		new LootItem(30502, 1, 2).broadcast(Broadcast.GLOBAL), // Imbued Range
		new LootItem(30461, 1, 2).broadcast(Broadcast.GLOBAL) // Donator Box
	);

	public static final LootTable SLAYER_MYSTERY_BOX_TABLE = new LootTable().addTable(1,

		new LootItem(6694, 50, 20),
		new LootItem(8779, 100, 20),
		new LootItem(8781, 80, 20),
		new LootItem(8783, 50, 20),
		new LootItem(1618, 100, 20),
		new LootItem(1620, 100, 20),
		new LootItem(1632, 25, 20),
		new LootItem(445, 400, 20),
		new LootItem(11212, 300, 20),
		new LootItem(4088, 30, 20),
		new LootItem(4586, 30, 20),
		new LootItem(13440, 150, 20),
		new LootItem(384, 250, 20),
		new LootItem(390, 200, 20),
		new LootItem(452, 50, 20),
		new LootItem(448, 500, 20),
		new LootItem(450, 300, 20),
		new LootItem(445, 300, 20),
		new LootItem(2354, 800, 20),
		new LootItem(1514, 300, 20),
		new LootItem(11230, 250, 20),
		new LootItem(5289, 10, 20),
		new LootItem(5316, 8, 20),
		new LootItem(5315, 10, 20),
		new LootItem(22877, 4, 20),
		new LootItem(5304, 9, 20),
		new LootItem(5300, 10, 20),
		new LootItem(5295, 12, 20),
		new LootItem(995, 15000000, 20),
		new LootItem(208, 30, 20),
		new LootItem(232, 300, 20),
		new LootItem(3052, 10, 20),
		new LootItem(210, 40, 20),
		new LootItem(212, 40, 20),
		new LootItem(537, 50, 20),
		new LootItem(2486, 40, 20),
		new LootItem(3139, 200, 20),
		new LootItem(224, 200, 20),
		new LootItem(2971, 200, 20),
		new LootItem(5295, 5, 20),
		new LootItem(1776, 500, 20),

		new LootItem(20724, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30503, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30502, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30539, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30537, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30535, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30536, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(30532, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(21270, 1, 1).broadcast(Broadcast.GLOBAL)
	);

	public static final LootTable ADVANCED_MYSTERY_BOX_COMMON_TABLE = new LootTable().addTable(1,
		new LootItem(23528, 1, 20),
		new LootItem(13233, 1, 20),
		new LootItem(21031, 1, 20),
		new LootItem(13243, 1, 20),
		new LootItem(13241, 1, 20),
		new LootItem(12863, 1, 20),
		new LootItem(21028, 1, 20),
		new LootItem(6889, 1, 20),
		new LootItem(22552, 1, 20),
		new LootItem(20716, 1, 20),
		new LootItem(13202, 1, 20),
		new LootItem(12692, 1, 20),
		new LootItem(12691, 1, 20),
		new LootItem(11905, 1, 20),
		new LootItem(19675, 1, 20),
		new LootItem(21902, 1, 20),
		new LootItem(19478, 1, 20),
		new LootItem(12598, 1, 20),
		new LootItem(11838, 1, 20),
		new LootItem(11770, 1, 20),
		new LootItem(11772, 1, 20),
		new LootItem(22975, 1, 20),
		new LootItem(11771, 1, 20),
		new LootItem(11773, 1, 20),
		new LootItem(6585, 1, 20),
		new LootItem(11826, 1, 20),
		new LootItem(11836, 1, 20),
		new LootItem(22634, 200, 20),
		new LootItem(22636, 150, 20),
		new LootItem(22547, 1, 12),
		new LootItem(22328, 1, 12),
		new LootItem(22327, 1, 12),
		new LootItem(19481, 1, 12),
		new LootItem(22954, 1, 12),
		new LootItem(21633, 1, 12),
		new LootItem(22002, 1, 12),
		new LootItem(11283, 1, 12),
		new LootItem(13263, 1, 12),
		new LootItem(13265, 1, 12),
		new LootItem(22981, 1, 12),
		new LootItem(11284, 1, 12),
		new LootItem(11785, 1, 12),
		new LootItem(12004, 1, 12),
		new LootItem(30476, 2000, 12),
		new LootItem(25975, 1, 12),
		new LootItem(12873, 1, 12),
		new LootItem(12883, 1, 12),
		new LootItem(12875, 1, 12),
		new LootItem(12879, 1, 12),
		new LootItem(12881, 1, 12),
		new LootItem(12877, 1, 12),
		new LootItem(21015, 1, 12),
		new LootItem(13652, 1, 12),
		new LootItem(21079, 1, 12),
		new LootItem(21034, 1, 12),
		new LootItem(21000, 1, 12),
		new LootItem(12927, 1, 12),
		new LootItem(12932, 1, 12),
		new LootItem(12922, 1, 12),
		new LootItem(11791, 1, 12),
		new LootItem(11830, 1, 12),
		new LootItem(11828, 1, 12),
		new LootItem(11832, 1, 12),
		new LootItem(11834, 1, 12),
		new LootItem(11802, 1, 12),
		new LootItem(2577, 1, 10),
		new LootItem(24271, 1, 10),
		new LootItem(22644, 1, 10),
		new LootItem(22641, 1, 10),
		new LootItem(22638, 1, 10),
		new LootItem(22647, 1, 10),
		new LootItem(22656, 1, 10),
		new LootItem(22653, 1, 10),
		new LootItem(23975, 1, 10),
		new LootItem(23979, 1, 10),
		new LootItem(23971, 1, 10),
		new LootItem(22650, 1, 10),
		new LootItem(22631, 1, 10),
		new LootItem(22628, 1, 10),
		new LootItem(22625, 1, 10),
		new LootItem(22610, 1, 10),
		new LootItem(22613, 1, 10),
		new LootItem(22619, 1, 10),
		new LootItem(22616, 1, 10),
		new LootItem(22477, 1, 10),
		new LootItem(22481, 1, 10),
		new LootItem(22324, 1, 10),
		new LootItem(22326, 1, 10),
		new LootItem(21295, 1, 10),
		new LootItem(21003, 1, 10),
		new LootItem(13235, 1, 10),
		new LootItem(13237, 1, 10),
		new LootItem(13239, 1, 10),
		new LootItem(24421, 1, 10),
		new LootItem(24420, 1, 10),
		new LootItem(24419, 1, 10),
		new LootItem(24517, 1, 10),
		new LootItem(24514, 1, 10),
		new LootItem(24422, 1, 10),
		new LootItem(20724, 1, 10),
		new LootItem(11806, 1, 10),
		new LootItem(11804, 1, 10),
		new LootItem(13576, 1, 10),
		new LootItem(19710, 1, 10),
		new LootItem(19544, 1, 10),
		new LootItem(19547, 1, 10),
		new LootItem(19553, 1, 10),
		new LootItem(22978, 1, 10),
		new LootItem(21012, 1, 10),
		new LootItem(21024, 1, 10),
		new LootItem(21021, 1, 10),
		new LootItem(21018, 1, 10),
		new LootItem(12821, 1, 10),
		new LootItem(12825, 1, 10),
		new LootItem(5607, 1, 10),
		new LootItem(5609, 1, 10),
		new LootItem(5608, 1, 10),
		new LootItem(24777, 1, 10),
		new LootItem(21006, 1, 7),
		new LootItem(10340, 1, 7),
		new LootItem(10338, 1, 7),
		new LootItem(10346, 1, 7),
		new LootItem(10348, 1, 7),
		new LootItem(10350, 1, 7),
		new LootItem(10352, 1, 7),
		new LootItem(23339, 1, 7),
		new LootItem(23336, 1, 7),
		new LootItem(23997, 1, 7),
		new LootItem(25865, 1, 7),
		new LootItem(23345, 1, 7),
		new LootItem(23342, 1, 7),
		new LootItem(12424, 1, 7),
		new LootItem(20014, 1, 7),
		new LootItem(12437, 1, 7),
		new LootItem(12426, 1, 7),
		new LootItem(20011, 1, 7),
		new LootItem(12422, 1, 7),
		new LootItem(24417, 1, 7),
		new LootItem(24511, 1, 7),
		new LootItem(4079, 1, 7),
		new LootItem(4566, 1, 7),
		new LootItem(24777, 1, 7),
		new LootItem(12817, 1, 4).broadcast(Broadcast.GLOBAL),
		new LootItem(24325, 1, 4),
		new LootItem(20997, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(22486, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(12399, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(11847, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(1053, 1, 4).broadcast(Broadcast.GLOBAL),
		new LootItem(1057, 1, 4).broadcast(Broadcast.GLOBAL),
		new LootItem(1055, 1, 4).broadcast(Broadcast.GLOBAL),
		new LootItem(962, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(1046, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(1044, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(1048, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(1040, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(1038, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(1042, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(11862, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(13343, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(11863, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(27226, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(27229, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(27232, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(27277, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(26219, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(26376, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(26378, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(26380, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(26374, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(26235, 1, 3).broadcast(Broadcast.GLOBAL)

	);


	private static void claimPerkPointScroll(Player player) {
		int randomPoints = Random.get(1, 3);
		switch (player.getSecondaryGroup()) {
			case SUPER_DONATOR -> randomPoints += 1;
			case ELITE_DONATOR, NOBLE_DONATOR -> randomPoints += 2;
			case GOLD_DONATOR -> randomPoints += 3;
			case PLATINUM_DONATOR -> randomPoints += 4;
			case LEGENDARY_DONATOR, SUPREME_DONATOR -> randomPoints += 5;
		}
		player.perkPoints += randomPoints;
		player.sendMessage("You have received " + randomPoints + " perk points from this scroll. You now have " + player.perkPoints + "!");
	}

	private static void openPointBox(Player player) {
		if (player.getInventory().contains(30577)) {
			player.getInventory().remove(30577, 1);
			if (Random.get(250) != 0) {
				int random = Random.get(250);
				if (random < 50) {
					player.updateReasonPoints(5000);
					player.sendMessage("You have received 5,000 Reason points!");
				}
				if (random >= 50 && random < 100) {
					player.updateAchievementPoints(1);
					player.sendMessage("You have received 1 Achievement point!");
				}
				if (random >= 100 && random < 150) {
					player.updateVotePoints(10);
					player.sendMessage("You have received 10 vote points");
				}
				if (random >= 150 && random < 200) {
					VarPlayerRepository.SLAYER_POINTS.set(player, 100 + VarPlayerRepository.SLAYER_POINTS.get(player));
					player.sendMessage("You have received 100 Slayer points!");
				}
				if (random >= 200 && random <= 250) {
					player.perkPoints += 2;
					player.sendMessage("You have received 2 Perk points!");
				}
			} else {
				int random = Random.get(250);
				if (random < 50) {
					player.updateReasonPoints(100000);
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>100,000 reason points </shad> from a point mystery box!");
				}
				if (random >= 50 && random < 100) {
					player.updateAchievementPoints(10);
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>10 Achievement points </shad> from a point mystery box!");

				}
				if (random >= 100 && random < 150) {
					player.updateVotePoints(25);
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>25 vote points </shad> from a point mystery box!");
				}
				if (random >= 150 && random < 200) {
					VarPlayerRepository.SLAYER_POINTS.set(player, 1000 + VarPlayerRepository.SLAYER_POINTS.get(player));
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>1,000 slayer points </shad> from a point mystery box!");

				}
				if (random >= 200 && random <= 250) {
					player.perkPoints += 15;
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>15 perk points </shad> from a point mystery box!");

				}
			}
		}
	}


	public static void register() {
		ItemAction.registerInventory(30461, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			int itemsCalculated = 0;
			while (itemsCalculated == 0) {
				reward = ADVANCED_MYSTERY_BOX_COMMON_TABLE.rollItem();
				if (!player.recentMysteryBoxRewards.contains(reward.getId())) {
					if (player.recentMysteryBoxRewards.size() >= 20)
						player.recentMysteryBoxRewards.remove(0);
					player.recentMysteryBoxRewards.add(reward.getId());
					itemsCalculated++;
				}
			}
			int holidayBoost = MysteryBox.wearingChristmasItem(player);
			if (holidayBoost > 0) {
				if (Random.get(1, 100) > holidayBoost)
					item.remove();
				else player.sendMessage("Your holiday item saves you from losing the box!");
			} else {
				item.remove();
			}
			player.getInventory().addOrDrop(reward);
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the donator mystery box!");

			if (reward.lootBroadcast != null) {
				String message = player.getName() + " just received ";
				message += reward.getDef().descriptiveName;
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a donator mystery box!");

//				RareDropEmbedMessage.sendBoxDiscordMessage(player, message, new Item(30461), reward.getId());

				var jsonObject = new JSONObject();
					jsonObject.put("player", player.getName());
					jsonObject.put("player_x", player.getPosition().getX());
					jsonObject.put("player_y", player.getPosition().getY());
					jsonObject.put("player_z", player.getPosition().getZ());
					jsonObject.put("item", reward.getDef().getName());
					jsonObject.put("item_id", reward.getId());
					jsonObject.put("source", new Item(30461).getDef().name);

				RareBoxOpenHook.sendBoxDiscordMessage(jsonObject);
			}
		});
		ItemAction.registerInventory(30529, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			reward = TOB_MYSTERY_BOX_TABLE.rollItem();
			item.remove();
			player.getInventory().add(reward);
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a ToB mystery box!");
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the ToB mystery box!");
		});
		ItemAction.registerInventory(6199, "Open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			reward = TOB_REFUND_TABLE.rollItem();
			item.remove();
			player.getInventory().add(reward);
			player.theatreOfBloodKills.increment(player);
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a ToB chest!");
			player.addToCollectionLog(reward);
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the ToB chest!");
		});
		ItemAction.registerInventory(30530, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			reward = COX_MYSTERY_BOX_TABLE.rollItem();
			item.remove();
			player.getInventory().add(reward);
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a CoX mystery box!");
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the CoX mystery box!");
		});
		ItemAction.registerInventory(30578, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			reward = WILDERNESS_MYSTERY_BOX_TABLE.rollItem();
			item.remove();
			player.getInventory().add(reward);
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a Wilderness mystery box!");
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Wilderness mystery box!");
		});
		ItemAction.registerInventory(30577, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			openPointBox(player);
			player.unlock();
		});
		ItemAction.registerInventory(30570, "read", (player, item) -> {
			player.dialogue(new MessageDialogue("This will redeem 1 perk point."));
			player.dialogue(new OptionsDialogue("Are you sure you want to claim this?",
				new Option("Yes, I want claim it!", () -> {
					item.remove(1);
					claimPerkPointScroll(player);
				}),
				new Option("Nevermind.")));
		});
		ItemAction.registerInventory(30542, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			reward = ENHANCED_SUPERIOR_SLAYER_BOX.rollItem();
			int holidayBoost = MysteryBox.wearingChristmasItem(player);
			if (holidayBoost > 0) {
				if (Random.get(1, 100) > holidayBoost)
					item.remove();
				else player.sendMessage("Your holiday item saves you from losing the box!");
			} else {
				item.remove();
			}
			player.getInventory().addOrDrop(reward);
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a enhanced superior slayer mystery box!");
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Enhanced superior slayer box!");
		});
		ItemAction.registerInventory(30528, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			reward = GAUNTLET_MYSTERY_BOX_TABLE.rollItem();
			item.remove();
			player.getInventory().add(reward);
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a gauntlet mystery box!");
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Gauntlet mystery box!");
		});
		ItemAction.registerInventory(30430, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			reward = ULTRA_MYSTERY_BOX.rollItem();
			int holidayBoost = MysteryBox.wearingChristmasItem(player);
			if (holidayBoost > 0) {
				if (Random.get(1, 100) > holidayBoost)
					item.remove();
				else player.sendMessage("Your holiday item saves you from losing the box!");
			} else {
				item.remove();
			}
			player.getInventory().addOrDrop(reward);
			if (reward.lootBroadcast != null) {
				String formattedAmount = NumberUtils.formatNumber(reward.getAmount());
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(),
					" received a <shad=D80808>" + formattedAmount + "x " + reward.getDef().name.toLowerCase() + "</shad> reward from a Reason Point Ultra Mystery Box!");
			}
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Reason Point Ultra Mystery Box!");
		});
		ItemAction.registerInventory(30582, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			reward = GWD_MYSTERY_BOX_TABLE.rollItem();
			item.remove();
			player.getInventory().add(reward);
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a GWD mystery box!");
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the GWD mystery box!");
		});
		ItemAction.registerInventory(30526, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			reward = NIGHTMARE_MYSTERY_BOX_TABLE.rollItem();
			item.remove();
			player.getInventory().add(reward);
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a nightmare mystery box!");
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Nightmare mystery box!");
		});
		ItemAction.registerInventory(30527, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			item.remove();
			reward = GALVEK_MYSTERY_BOX_TABLE.rollItem();
			player.getInventory().add(reward);
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a galvek mystery box!");
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Galvek mystery box!");
		});
		ItemAction.registerInventory(30525, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			item.remove();
			reward = ARGENTAVIS_MYSTERY_BOX_TABLE.rollItem();
			player.getInventory().add(reward);
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Argentavis mystery box!");
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from an Argentavis mystery box!");
			player.unlock();
		});
		ItemAction.registerInventory(30524, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			item.remove();
			reward = NEX_MYSTERY_BOX_TABLE.rollItem();
			player.getInventory().add(reward);
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Nex mystery box!");
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a Nex mystery box!");
			player.unlock();
		});
		ItemAction.registerInventory(30763, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			item.remove();
			reward = FORGOTTEN_LOCKBOX.rollItem();
			player.getInventory().add(reward);
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Forgotten lockbox!");
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a Forgotten lockbox!");
			player.unlock();
		});
		ItemAction.registerInventory(59524, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			int itemsCalculated = 0;
			while (itemsCalculated == 0) {
				reward = DONATOR_WEAPON_BOX.rollItem();
				if (!player.recentWeaponMysteryBoxRewards.contains(reward.getId())) {
					if (player.recentWeaponMysteryBoxRewards.size() >= 5)
						player.recentWeaponMysteryBoxRewards.remove(0);
					player.recentWeaponMysteryBoxRewards.add(reward.getId());
					itemsCalculated++;
				}
			}
			if (reward.getId() == ItemID.ABYSSAL_TENTACLE) {
				AttributeExtensions.setCharges(reward, 10000);
			}
			if (reward.getId() == ItemID.ARCLIGHT) {
				AttributeExtensions.setCharges(reward, 1000);
			}
			int holidayBoost = MysteryBox.wearingChristmasItem(player);
			if (holidayBoost > 0) {
				if (Random.get(1, 100) > holidayBoost)
					item.remove();
				else player.sendMessage("Your holiday item saves you from losing the box!");
			} else {
				item.remove();
			}
			player.getInventory().addOrDrop(reward);
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the donator weapon box!");
			player.unlock();
			if (reward.lootBroadcast != null) {
				String message = player.getName() + " just received ";
				message += reward.getDef().descriptiveName;
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a donator weapon box!");
//				RareDropEmbedMessage.sendBoxDiscordMessage(player, message, new Item(59524), reward.getId());

				var jsonObject = new JSONObject();
					jsonObject.put("player", player.getName());
					jsonObject.put("player_x", player.getPosition().getX());
					jsonObject.put("player_y", player.getPosition().getY());
					jsonObject.put("player_z", player.getPosition().getZ());
					jsonObject.put("item", reward.getDef().getName());
					jsonObject.put("item_id", reward.getId());
					jsonObject.put("source", new Item(59524).getDef().name);

				RareBoxOpenHook.sendBoxDiscordMessage(jsonObject);
			}
		});

		ItemAction.registerInventory(32000, "open", (player, item) -> {
			if (!player.getInventory().hasFreeSlots(1)) {
				player.sendMessage("You need at least 1 free inventory space to open this box.");
				return;
			}
			player.lock();
			player.closeDialogue();
			Item reward = HOLIDAY_MYSTERY_BOX.rollItem();
			int holidayBoost = MysteryBox.wearingChristmasItem(player);
			if (holidayBoost > 0) {
				if (Random.get(1, 100) > holidayBoost)
					item.remove(1);
				else player.sendMessage("Your holiday item saves you from losing the box!");
			} else {
				item.remove(1);
			}
			player.getInventory().addOrDrop(reward);
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the spring mystery box!");
			player.unlock();
			if (reward.lootBroadcast != null) {
				String message = player.getName() + " just received ";
				message += reward.getDef().descriptiveName;
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + reward.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a spring mystery box!");
//				RareDropEmbedMessage.sendBoxDiscordMessage(player, message, new Item(32000), reward.getId());

				var jsonObject = new JSONObject();
					jsonObject.put("player", player.getName());
					jsonObject.put("player_x", player.getPosition().getX());
					jsonObject.put("player_y", player.getPosition().getY());
					jsonObject.put("player_z", player.getPosition().getZ());
					jsonObject.put("item", reward.getDef().getName());
					jsonObject.put("item_id", reward.getId());
					jsonObject.put("source", new Item(32000).getDef().name);

				RareBoxOpenHook.sendBoxDiscordMessage(jsonObject);
			}
		});
		ItemAction.registerInventory(ItemID.SUMMER_MYSTERY_BOX, "open", (player, item) -> {
			if (!player.getInventory().hasFreeSlots(1)) {
				player.sendMessage("You need at least 1 free inventory space to open this box.");
				return;
			}
			player.lock();
			player.closeDialogue();
			Item reward = SUMMER_MYSTERY_BOX.rollItem();
			int holidayBoost = MysteryBox.wearingChristmasItem(player);
			if (holidayBoost > 0) {
				if (Random.get(1, 100) > holidayBoost)
					item.remove(1);
				else player.sendMessage("Your holiday item saves you from losing the box!");
			} else {
				item.remove(1);
			}
			player.getInventory().addOrDrop(reward);
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the summer mystery box!");
			player.unlock();
			if (reward.lootBroadcast != null) {
				String message = player.getName() + " just received ";
				message += reward.getDef().descriptiveName;
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + reward.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a summer mystery box!");
//				RareDropEmbedMessage.sendBoxDiscordMessage(player, message, new Item(32000), reward.getId());

				var jsonObject = new JSONObject();
					jsonObject.put("player", player.getName());
					jsonObject.put("player_x", player.getPosition().getX());
					jsonObject.put("player_y", player.getPosition().getY());
					jsonObject.put("player_z", player.getPosition().getZ());
					jsonObject.put("item", reward.getDef().getName());
					jsonObject.put("item_id", reward.getId());
					jsonObject.put("source", new Item(32000).getDef().name);

				RareBoxOpenHook.sendBoxDiscordMessage(jsonObject);
			}
		});
		ItemAction.registerInventory(59525, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			int holidayBoost = MysteryBox.wearingChristmasItem(player);
			if (holidayBoost > 0) {
				if (Random.get(1, 100) > holidayBoost)
					item.remove();
				else player.sendMessage("Your holiday item saves you from losing the box!");
			} else {
				item.remove();
			}
			int itemsCalculated = 0;
			while (itemsCalculated == 0) {
				reward = DONATOR_ARMOUR_BOX.rollItem();
				if (!player.recentArmourMysteryBoxRewards.contains(reward.getId())) {
					if (player.recentArmourMysteryBoxRewards.size() >= 5)
						player.recentArmourMysteryBoxRewards.remove(0);
					player.recentArmourMysteryBoxRewards.add(reward.getId());
					itemsCalculated++;
				}
			}

			player.getInventory().addOrDrop(reward);
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the donator armour box!");
			if (reward.lootBroadcast != null) {
				String message = player.getName() + " just received ";
				message += reward.getDef().descriptiveName;
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a donator armour box!");
//				RareDropEmbedMessage.sendBoxDiscordMessage(player, message, new Item(59525), reward.getId());

				var jsonObject = new JSONObject();
					jsonObject.put("player", player.getName());
					jsonObject.put("player_x", player.getPosition().getX());
					jsonObject.put("player_y", player.getPosition().getY());
					jsonObject.put("player_z", player.getPosition().getZ());
					jsonObject.put("item", reward.getDef().getName());
					jsonObject.put("item_id", reward.getId());
					jsonObject.put("source", new Item(59525).getDef().name);

				RareBoxOpenHook.sendBoxDiscordMessage(jsonObject);
			}
			player.unlock();
		});
		ItemAction.registerInventory(30596, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			reward = VOTE_BOX.rollItem();
			int holidayBoost = MysteryBox.wearingChristmasItem(player);
			if (holidayBoost > 0) {
				if (Random.get(1, 100) > holidayBoost)
					item.remove();
				else player.sendMessage("Your holiday item saves you from losing the box!");
			} else {
				item.remove();
			}
			player.getInventory().addOrDrop(reward);
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Vote Buff Streak Box!");
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + reward.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a Vote Buff Streak Box!");
			player.unlock();
		});
		ItemAction.registerInventory(30452, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward;
			reward = SLAYER_MYSTERY_BOX_TABLE.rollItem();
			int holidayBoost = MysteryBox.wearingChristmasItem(player);
			if (holidayBoost > 0) {
				if (Random.get(1, 100) > holidayBoost)
					item.remove();
				else player.sendMessage("Your holiday item saves you from losing the box!");
			} else {
				item.remove();
			}
			player.getInventory().addOrDrop(reward);
			if (reward.lootBroadcast != null)
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a slayer mystery box!");
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Slayer mystery box!");
		});
		ItemAction.registerInventory(30450, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward;
			reward = LOW_TIER_MYSTERY_BOX_TABLE.rollItem();
			int holidayBoost = MysteryBox.wearingChristmasItem(player);
			if (holidayBoost > 0) {
				if (Random.get(1, 100) > holidayBoost)
					item.remove();
				else player.sendMessage("Your holiday item saves you from losing the box!");
			} else {
				item.remove();
			}
			player.getInventory().addOrDrop(reward);
			if (reward.lootBroadcast != null) {

				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a low tier mystery box!");
			}
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Low tier mystery box!");
		});
		ItemAction.registerInventory(30451, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward;
			reward = HIGH_TIER_MYSTERY_BOX_TABLE.rollItem();
			int holidayBoost = MysteryBox.wearingChristmasItem(player);
			if (holidayBoost > 0) {
				if (Random.get(1, 100) > holidayBoost)
					item.remove();
				else player.sendMessage("Your holiday item saves you from losing the box!");
			} else {
				item.remove();
			}
			player.getInventory().addOrDrop(reward);
			if (reward.lootBroadcast != null) {

				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from a high tier mystery box!");
			}
			player.unlock();
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the High tier mystery box!");
		});
		ItemAction.registerInventory(4810, "open", (player, item) -> {
			if (player.getInventory().getFreeSlots() < 3) {
				player.sendMessage("You need at least 3 inventory spaces to open this!");
				return;
			}
			player.lock();
			player.closeDialogue();
			List<Integer> rewards = new ArrayList<>();
			List<Item> rewardsItemList = new ArrayList<>();
			int itemsCalculated = 0;
			while (itemsCalculated != 3) {
				Item pulledItem = null;
				pulledItem = MYSTERY_BOX_TABLE.rollItem();
				if (!rewards.contains(pulledItem.getId()) && !player.recentMysteryBoxRewards.contains(pulledItem.getId())) {
					rewards.add(pulledItem.getId());
					rewardsItemList.add(pulledItem);
					if (player.recentMysteryBoxRewards.size() >= 20)
						player.recentMysteryBoxRewards.remove(0);
					player.recentMysteryBoxRewards.add(pulledItem.getId());
					itemsCalculated++;
				}
			}
			item.remove();
			for (Item reward :
				rewardsItemList) {
				player.getInventory().add(reward);
				player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the mystery bag!");
				player.unlock();
				if (reward.lootBroadcast != null) {
					String message = player.getName() + " just received ";
					message += reward.getDef().descriptiveName;
//					RareDropEmbedMessage.sendBoxDiscordMessage(player, message, new Item(4810), reward.getId());

					var jsonObject = new JSONObject();
						jsonObject.put("player", player.getName());
						jsonObject.put("player_x", player.getPosition().getX());
						jsonObject.put("player_y", player.getPosition().getY());
						jsonObject.put("player_z", player.getPosition().getZ());
						jsonObject.put("item", reward.getDef().getName());
						jsonObject.put("item_id", reward.getId());
						jsonObject.put("source", new Item(4810).getDef().name);

					RareBoxOpenHook.sendBoxDiscordMessage(jsonObject);

					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
						+ reward.getDef().name.toLowerCase() + "</shad> reward from a donator mystery bag!");
				}
			}
		});
		ItemAction.registerInventory(30446, "Look-in", (player, item) -> {
			if (player.getInventory().getFreeSlots() < 5) {
				player.sendMessage("You need at least 5 inventory spaces to open this!");
				return;
			}
			player.lock();
			player.closeDialogue();
			List<Integer> rewards = new ArrayList<>();
			List<Item> rewardsItemList = new ArrayList<>();
			int itemsCalculated = 0;
			while (itemsCalculated != 5) {
				Item pulledItem = null;
				pulledItem = MYSTERY_BOX_TABLE.rollItem();

				if (!rewards.contains(pulledItem.getId()) && !player.recentMysteryBoxRewards.contains(pulledItem.getId())) {
					rewards.add(pulledItem.getId());
					rewardsItemList.add(pulledItem);
					if (player.recentMysteryBoxRewards.size() >= 20)
						player.recentMysteryBoxRewards.remove(0);
					player.recentMysteryBoxRewards.add(pulledItem.getId());
					itemsCalculated++;
				}
			}
			item.remove();
			for (Item reward :
				rewardsItemList) {
				player.getInventory().add(reward);
				player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the donator mystery chest!");
				if (reward.lootBroadcast != null) {
					String message = player.getName() + " just received ";
					message += reward.getDef().descriptiveName;
//					RareDropEmbedMessage.sendBoxDiscordMessage(player, message, new Item(30446), reward.getId());

					var jsonObject = new JSONObject();
						jsonObject.put("player", player.getName());
						jsonObject.put("player_x", player.getPosition().getX());
						jsonObject.put("player_y", player.getPosition().getY());
						jsonObject.put("player_z", player.getPosition().getZ());
						jsonObject.put("item", reward.getDef().getName());
						jsonObject.put("item_id", reward.getId());
						jsonObject.put("source", new Item(30446).getDef().name);

					RareBoxOpenHook.sendBoxDiscordMessage(jsonObject);

					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
						+ reward.getDef().name.toLowerCase() + "</shad> reward from a donator mystery chest!");
				}
			}
			player.unlock();
		});
		ItemAction.registerInventory(30462, "open", (player, item) -> {
			player.lock();
			player.closeDialogue();
			Item reward = null;
			int chance = Random.get(1, 75);
			int itemsCalculated = 0;
			while (itemsCalculated == 0) {
				reward = ADVANCED_MYSTERY_BOX_COMMON_TABLE.rollItem();
				if (!player.recentMysteryBoxRewards.contains(reward.getId())) {
					if (player.recentMysteryBoxRewards.size() >= 20)
						player.recentMysteryBoxRewards.remove(0);
					player.recentMysteryBoxRewards.add(reward.getId());
					itemsCalculated++;
				}
			}
			int holidayBoost = MysteryBox.wearingChristmasItem(player);
			if (holidayBoost > 0) {
				if (Random.get(1, 100) > holidayBoost)
					item.remove();
				else player.sendMessage("Your holiday item saves you from losing the box!");
			} else {
				item.remove();
			}
			player.getInventory().addOrDrop(reward);
			player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Advanced mystery box!");
			if (reward.lootBroadcast != null) {
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
					+ reward.getDef().name.toLowerCase() + "</shad> reward from an advanced donator mystery box!");
				String message = player.getName() + " just received ";
				message += reward.getDef().descriptiveName;
//				RareDropEmbedMessage.sendBoxDiscordMessage(player, message, new Item(30462), reward.getId());

				var jsonObject = new JSONObject();
					jsonObject.put("player", player.getName());
					jsonObject.put("player_x", player.getPosition().getX());
					jsonObject.put("player_y", player.getPosition().getY());
					jsonObject.put("player_z", player.getPosition().getZ());
					jsonObject.put("item", reward.getDef().getName());
					jsonObject.put("item_id", reward.getId());
					jsonObject.put("source", new Item(30462).getDef().name);

				RareBoxOpenHook.sendBoxDiscordMessage(jsonObject);

			}
			player.unlock();
		});
		ItemAction.registerInventory(30448, "open", (player, item) -> {
			if (player.getInventory().getFreeSlots() < 3) {
				player.sendMessage("You need at least 3 inventory spaces to open this!");
				return;
			}
			player.lock();
			player.closeDialogue();
			List<Integer> rewards = new ArrayList<>();
			List<Item> rewardsItemList = new ArrayList<>();
			int itemsCalculated = 0;
			while (itemsCalculated != 3) {
				Item pulledItem = null;
				pulledItem = ADVANCED_MYSTERY_BOX_COMMON_TABLE.rollItem();

				if (!rewards.contains(pulledItem.getId()) && !player.recentMysteryBoxRewards.contains(pulledItem.getId())) {
					rewards.add(pulledItem.getId());
					rewardsItemList.add(pulledItem);
					if (player.recentMysteryBoxRewards.size() >= 20)
						player.recentMysteryBoxRewards.remove(0);
					player.recentMysteryBoxRewards.add(pulledItem.getId());
					itemsCalculated++;

				}
			}
			item.remove();
			for (Item reward :
				rewardsItemList) {
				player.getInventory().add(reward);
				player.unlock();
				if (reward.lootBroadcast != null) {
					String message = player.getName() + " just received ";
					message += reward.getDef().descriptiveName;
//					RareDropEmbedMessage.sendBoxDiscordMessage(player, message, new Item(30448), reward.getId());

					var jsonObject = new JSONObject();
					jsonObject.put("player", player.getName());
						jsonObject.put("player_x", player.getPosition().getX());
						jsonObject.put("player_y", player.getPosition().getY());
						jsonObject.put("player_z", player.getPosition().getZ());
						jsonObject.put("item", reward.getDef().getName());
						jsonObject.put("item_id", reward.getId());
						jsonObject.put("source", new Item(30448).getDef().name);

					RareBoxOpenHook.sendBoxDiscordMessage(jsonObject);

					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
						+ reward.getDef().name.toLowerCase() + "</shad> reward from an advanced donator mystery bag!");
				}
			}
		});
		ItemAction.registerInventory(30449, "Look-in", (player, item) -> {
			if (player.getInventory().getFreeSlots() < 5) {
				player.sendMessage("You need at least 5 inventory spaces to open this!");
				return;
			}
			player.lock();
			player.closeDialogue();
			List<Integer> rewards = new ArrayList<>();
			List<Item> rewardsItemList = new ArrayList<>();
			int itemsCalculated = 0;
			//2 Common 1 uncommon rest random
			while (itemsCalculated != 5) {
				Item pulledItem = null;
				pulledItem = ADVANCED_MYSTERY_BOX_COMMON_TABLE.rollItem();

				if (!rewards.contains(pulledItem.getId()) && !player.recentMysteryBoxRewards.contains(pulledItem.getId())) {
					rewards.add(pulledItem.getId());
					rewardsItemList.add(pulledItem);
					if (player.recentMysteryBoxRewards.size() >= 20)
						player.recentMysteryBoxRewards.remove(0);
					player.recentMysteryBoxRewards.add(pulledItem.getId());
					itemsCalculated++;
				}
			}
			item.remove();
			for (Item reward :
				rewardsItemList) {
				player.getInventory().add(reward);
				player.sendMessage("You have received a " + reward.getDef().name.toLowerCase() + " from the Advanced mystery chest.");
				player.unlock();
				if (reward.lootBroadcast != null) {
					String message = player.getName() + " just received ";
					message += reward.getDef().descriptiveName;
//					RareDropEmbedMessage.sendBoxDiscordMessage(player, message, new Item(30449), reward.getId());

					var jsonObject = new JSONObject();
						jsonObject.put("player", player.getName());
						jsonObject.put("player_x", player.getPosition().getX());
						jsonObject.put("player_y", player.getPosition().getY());
						jsonObject.put("player_z", player.getPosition().getZ());
						jsonObject.put("item", reward.getDef().getName());
						jsonObject.put("item_id", reward.getId());
						jsonObject.put("source", new Item(30449).getDef().name);

					RareBoxOpenHook.sendBoxDiscordMessage(jsonObject);

					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>" + item.getAmount() + "x "
						+ reward.getDef().name.toLowerCase() + "</shad> reward from an advanced donator mystery chest!");

				}
			}
		});


	}
}

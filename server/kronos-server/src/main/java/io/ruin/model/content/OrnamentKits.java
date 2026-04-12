package io.ruin.model.content;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.item.actions.impl.AchievementLamp;

import java.util.HashMap;
import java.util.Map;

public enum OrnamentKits {
	DRAGON_BOOTS(new Item(ItemID.DRAGON_BOOTS_G), new Item(ItemID.DRAGON_BOOTS), new Item(ItemID.DRAGON_BOOTS_ORNAMENT_KIT)),
	OCCULT(new Item(ItemID.OCCULT_NECKLACE_OR), new Item(ItemID.OCCULT_NECKLACE), new Item(ItemID.OCCULT_ORNAMENT_KIT)),
	ANGUISH(new Item(ItemID.NECKLACE_OF_ANGUISH_OR), new Item(ItemID.NECKLACE_OF_ANGUISH), new Item(ItemID.ANGUISH_ORNAMENT_KIT)),
	TORTURE(new Item(ItemID.AMULET_OF_TORTURE_OR), new Item(ItemID.AMULET_OF_TORTURE), new Item(ItemID.TORTURE_ORNAMENT_KIT)),
	TORMENTED(new Item(ItemID.TORMENTED_BRACELET_OR), new Item(ItemID.TORMENTED_BRACELET), new Item(ItemID.TORMENTED_ORNAMENT_KIT)),
	FURY(new Item(ItemID.AMULET_OF_FURY_OR), new Item(ItemID.AMULET_OF_FURY), new Item(ItemID.FURY_ORNAMENT_KIT)),
	RUNE_SCIM_ZAMMY(new Item(ItemID.RUNE_SCIMITAR_23334), new Item(ItemID.RUNE_SCIMITAR), new Item(ItemID.RUNE_SCIMITAR_ORNAMENT_KIT_ZAMORAK)),
	RUNE_SCIM_SARA(new Item(ItemID.RUNE_SCIMITAR_23332), new Item(ItemID.RUNE_SCIMITAR), new Item(ItemID.RUNE_SCIMITAR_ORNAMENT_KIT_SARADOMIN)),
	RUNE_SCIM_GUTHIX(new Item(ItemID.RUNE_SCIMITAR_23330), new Item(ItemID.RUNE_SCIMITAR), new Item(ItemID.RUNE_SCIMITAR_ORNAMENT_KIT_GUTHIX)),
	DRAGON_PLATELEGS(new Item(ItemID.DRAGON_PLATELEGS_G), new Item(ItemID.DRAGON_PLATELEGS), new Item(ItemID.DRAGON_LEGSSKIRT_ORNAMENT_KIT)),
	DRAGON_PLATESKIRT(new Item(ItemID.DRAGON_PLATESKIRT_G), new Item(ItemID.DRAGON_PLATESKIRT), new Item(ItemID.DRAGON_LEGSSKIRT_ORNAMENT_KIT)),
	DRAGON_FULL_HELM(
		new Item(ItemID.DRAGON_FULL_HELM_G),
		new Item(ItemID.DRAGON_FULL_HELM),
		new Item(ItemID.DRAGON_FULL_HELM_ORNAMENT_KIT)
	),
	DRAGON_SQ_SHIELD(
		new Item(ItemID.DRAGON_SQ_SHIELD_G),
		new Item(ItemID.DRAGON_SQ_SHIELD),
		new Item(ItemID.DRAGON_SQ_SHIELD_ORNAMENT_KIT)
	),
	DRAGON_CHAINBODY(
		new Item(ItemID.DRAGON_CHAINBODY_G),
		new Item(ItemID.DRAGON_CHAINBODY_3140),
		new Item(ItemID.DRAGON_CHAINBODY_ORNAMENT_KIT)
	),
	DRAGON_PLATEBODY(
		new Item(ItemID.DRAGON_PLATEBODY_G),
		new Item(ItemID.DRAGON_PLATEBODY),
		new Item(ItemID.DRAGON_PLATEBODY_ORNAMENT_KIT)
	),
	DRAGON_SCIMITAR(
		new Item(ItemID.DRAGON_SCIMITAR_OR),
		new Item(ItemID.DRAGON_SCIMITAR),
		new Item(ItemID.DRAGON_SCIMITAR_ORNAMENT_KIT)
	),
	DRAGON_DEFENDER(
		new Item(ItemID.DRAGON_DEFENDER_T),
		new Item(ItemID.DRAGON_DEFENDER),
		new Item(ItemID.DRAGON_DEFENDER_ORNAMENT_KIT)
	),
	DRAGON_KITESHIELD(
		new Item(ItemID.DRAGON_KITESHIELD_G),
		new Item(ItemID.DRAGON_KITESHIELD),
		new Item(ItemID.DRAGON_KITESHIELD_ORNAMENT_KIT)
	),
	RUNE_DEFENDER(
		new Item(ItemID.RUNE_DEFENDER_T),
		new Item(ItemID.RUNE_DEFENDER),
		new Item(ItemID.RUNE_DEFENDER_ORNAMENT_KIT)
	),
	PRIMORDIAL_BOOTS(
		new Item(30038),
		new Item(ItemID.PRIMORDIAL_BOOTS),
		new Item(30046)
	),
	PEGASIAN_BOOTS(
		new Item(30498),
		new Item(ItemID.PEGASIAN_BOOTS),
		new Item(30501)
	),
	ETERNAL_BOOTS(
		new Item(30499),
		new Item(ItemID.ETERNAL_BOOTS),
		new Item(30500)
	),
	AGS(
		new Item(ItemID.ARMADYL_GODSWORD_OR),
		new Item(ItemID.ARMADYL_GODSWORD),
		new Item(ItemID.ARMADYL_GODSWORD_ORNAMENT_KIT)
	),
	ZGS(
		new Item(ItemID.ZAMORAK_GODSWORD_OR),
		new Item(ItemID.ZAMORAK_GODSWORD),
		new Item(ItemID.ZAMORAK_GODSWORD_ORNAMENT_KIT)
	),
	BGS(
		new Item(ItemID.BANDOS_GODSWORD_OR),
		new Item(ItemID.BANDOS_GODSWORD),
		new Item(ItemID.BANDOS_GODSWORD_ORNAMENT_KIT)
	),
	SGS(
		new Item(ItemID.SARADOMIN_GODSWORD_OR),
		new Item(ItemID.SARADOMIN_GODSWORD),
		new Item(ItemID.SARADOMIN_GODSWORD_ORNAMENT_KIT)
	),
	OBBY_MAUL(
		new Item(ItemID.TZHAARKETOM_T),
		new Item(ItemID.TZHAARKETOM),
		new Item(ItemID.TZHAARKETOM_ORNAMENT_KIT)
	),
	BERSERKER_NECKLACE(
		new Item(ItemID.BERSERKER_NECKLACE_OR),
		new Item(ItemID.BERSERKER_NECKLACE),
		new Item(ItemID.BERSERKER_NECKLACE_ORNAMENT_KIT)
	),
	DARK_INFINITY_HAT(
		new Item(ItemID.DARK_INFINITY_HAT),
		new Item(ItemID.INFINITY_HAT),
		new Item(ItemID.DARK_INFINITY_COLOUR_KIT)
	),
	DARK_INFINITY_TOP(
		new Item(ItemID.DARK_INFINITY_TOP),
		new Item(ItemID.INFINITY_TOP),
		new Item(ItemID.DARK_INFINITY_COLOUR_KIT)
	),
	DARK_INFINITY_BOTTOMS(
		new Item(ItemID.DARK_INFINITY_BOTTOMS),
		new Item(ItemID.INFINITY_BOTTOMS),
		new Item(ItemID.DARK_INFINITY_COLOUR_KIT)
	),
	LIGHT_INFINITY_HAT(
		new Item(ItemID.LIGHT_INFINITY_HAT),
		new Item(ItemID.INFINITY_HAT),
		new Item(ItemID.LIGHT_INFINITY_COLOUR_KIT)
	),
	LIGHT_INFINITY_TOP(
		new Item(ItemID.LIGHT_INFINITY_TOP),
		new Item(ItemID.INFINITY_TOP),
		new Item(ItemID.LIGHT_INFINITY_COLOUR_KIT)
	),
	LIGHT_INFINITY_BOTTOMS(
		new Item(ItemID.LIGHT_INFINITY_BOTTOMS),
		new Item(ItemID.INFINITY_BOTTOMS),
		new Item(ItemID.LIGHT_INFINITY_COLOUR_KIT)
	),
	TWISTED_ANC_HAT(
		new Item(24664),
		new Item(ItemID.ANCESTRAL_HAT),
		new Item(24670)
	),
	TWISTED_ANC_TOP(
		new Item(24666),
		new Item(ItemID.ANCESTRAL_ROBE_TOP),
		new Item(24670)
	),
	TWISTED_ANC_BOTTOMS(
		new Item(24668),
		new Item(ItemID.ANCESTRAL_ROBE_BOTTOM),
		new Item(24670)
	),
	HOLY_RAPIER(
		new Item(25734),
		new Item(ItemID.GHRAZI_RAPIER),
		new Item(25742)
	),
	HOLY_SANG_U(
		new Item(25733),
		new Item(ItemID.SANGUINESTI_STAFF_UNCHARGED),
		new Item(25742)
	),
	HOLY_SANG(
		new Item(25731),
		new Item(ItemID.SANGUINESTI_STAFF),
		new Item(25742)
	),
	HOLY_SCYTHE(
		new Item(25736),
		new Item(ItemID.SCYTHE_OF_VITUR),
		new Item(25742)
	),
	HOLY_SCYTHE_U(
		new Item(25738),
		new Item(ItemID.SCYTHE_OF_VITUR_UNCHARGED),
		new Item(25742)
	),
	SANGUINE_SCYTHE(
		new Item(25739),
		new Item(ItemID.SCYTHE_OF_VITUR),
		new Item(25744)
	),
	SANGUINE_SCYTHE_U(
		new Item(25741),
		new Item(ItemID.SCYTHE_OF_VITUR_UNCHARGED),
		new Item(25744)
	),

	VOID_MAGE_HOOD(
		new Item(26473),
		new Item(ItemID.VOID_MAGE_HELM),
		new Item(26479)
	),
	VOID_RANGE_HELM(
		new Item(26475),
		new Item(ItemID.VOID_RANGER_HELM),
		new Item(26479)
	),
	VOID_MELEE_HELM(
		new Item(26477),
		new Item(ItemID.VOID_MELEE_HELM),
		new Item(26479)
	),
	VOID_GLOVES(
		new Item(26467),
		new Item(ItemID.VOID_KNIGHT_GLOVES),
		new Item(26479)
	),
	VOID_TOP(
		new Item(26463),
		new Item(ItemID.VOID_KNIGHT_TOP),
		new Item(26479)
	),
	VOID_ROBE(
		new Item(26465),
		new Item(ItemID.VOID_KNIGHT_ROBE),
		new Item(26479)
	),
	ELITE_VOID_TOP(
		new Item(26469),
		new Item(ItemID.ELITE_VOID_TOP),
		new Item(26479)
	),
	ELITE_VOID_ROBE(
		new Item(26471),
		new Item(ItemID.ELITE_VOID_ROBE),
		new Item(26479)
	),
	ABYSSAL_TENT(
		new Item(26484),
		new Item(ItemID.ABYSSAL_TENTACLE),
		new Item(26421)
	),
	ABYSSAL_WHIP(
		new Item(26482),
		new Item(ItemID.ABYSSAL_WHIP),
		new Item(26421)
	),
	RUNE_CROSSBOW(
		new Item(26486),
		new Item(ItemID.RUNE_CROSSBOW),
		new Item(26421)
	),
	HOLY_BOOK(
		new Item(26496),
		new Item(ItemID.HOLY_BOOK),
		new Item(26421)
	),
	UNHOLY_BOOK(
		new Item(26498),
		new Item(ItemID.UNHOLY_BOOK),
		new Item(26421)
	),
	BOOK_OF_BALANCE(
		new Item(26488),
		new Item(ItemID.BOOK_OF_BALANCE),
		new Item(26421)
	),
	BOOK_OF_LAW(
		new Item(26492),
		new Item(ItemID.BOOK_OF_LAW),
		new Item(26421)
	),
	BOOK_OF_WAR(
		new Item(26494),
		new Item(ItemID.BOOK_OF_WAR),
		new Item(26421)
	),
	BOOK_OF_DARKNESS(
		new Item(26490),
		new Item(ItemID.BOOK_OF_DARKNESS),
		new Item(26421)
	),
	///  CORRUPTED SHIT
	DRAGON_CLAWS_CR(
		new Item(28039),
		new Item(ItemID.DRAGON_CLAWS),
		new Item(28017)
	),
	DRAGON_CLAWS_CR2(
		new Item(28039),
		new Item(ItemID.DRAGON_CLAWS_20784),
		new Item(28017)
	),
	FIGHTER_TORSO_CR(
		new Item(28067),
		new Item(ItemID.FIGHTER_TORSO),
		new Item(28017)
	),
	NEITIZNOT_CR(
		new Item(28070),
		new Item(ItemID.HELM_OF_NEITIZNOT),
		new Item(28017)
	),
	DRAGON_2H_SWORD_CR(
		new Item(28051),
		new Item(ItemID.DRAGON_2H_SWORD),
		new Item(28017)
	),
	DRAGON_2H_SWORD_20559_CR(
		new Item(28051),
		new Item(ItemID.DRAGON_2H_SWORD_20559),
		new Item(28017)
	),
	DRAGON_BATTLEAXE_CR(
		new Item(28037),
		new Item(ItemID.DRAGON_BATTLEAXE),
		new Item(28017)
	),
	DRAGON_BOOTS_CR(
		new Item(28055),
		new Item(ItemID.DRAGON_BOOTS),
		new Item(28017)
	),
	DRAGON_CHAINBODY_CR(
		new Item(28065),
		new Item(ItemID.DRAGON_CHAINBODY),
		new Item(28017)
	),
	DRAGON_CHAINBODY_3140_CR(
		new Item(28065),
		new Item(ItemID.DRAGON_CHAINBODY_3140),
		new Item(28017)
	),
	DRAGON_CHAINBODY_20428_CR(
		new Item(28065),
		new Item(ItemID.DRAGON_CHAINBODY_20428),
		new Item(28017)
	),
	DRAGON_CROSSBOW_CR(
		new Item(28053),
		new Item(ItemID.DRAGON_CROSSBOW),
		new Item(28017)
	),
	DRAGON_DAGGER_CR(
		new Item(28019),
		new Item(ItemID.DRAGON_DAGGER),
		new Item(28017)
	),
	DRAGON_DAGGER_20407_CR(
		new Item(28019),
		new Item(ItemID.DRAGON_DAGGER_20407),
		new Item(28017)
	),
	DRAGON_HALBERD_CR(
		new Item(28049),
		new Item(ItemID.DRAGON_HALBERD),
		new Item(28017)
	),
	DRAGON_LONGSWORD_CR(
		new Item(28033),
		new Item(ItemID.DRAGON_LONGSWORD),
		new Item(28017)
	),
	DRAGON_MACE_CR(
		new Item(28027),
		new Item(ItemID.DRAGON_MACE),
		new Item(28017)
	),
	DRAGON_MED_HELM_CR(
		new Item(28057),
		new Item(ItemID.DRAGON_MED_HELM),
		new Item(28017)
	),
	DRAGON_MED_HELM_6967_CR(
		new Item(28057),
		new Item(ItemID.DRAGON_MED_HELM_6967),
		new Item(28017)
	),
	DRAGON_PLATELEGS_CR(
		new Item(28061),
		new Item(ItemID.DRAGON_PLATELEGS),
		new Item(28017)
	),
	DRAGON_PLATELEGS_4180_CR(
		new Item(28061),
		new Item(ItemID.DRAGON_PLATELEGS_4180),
		new Item(28017)
	),
	DRAGON_PLATELEGS_20429_CR(
		new Item(28061),
		new Item(ItemID.DRAGON_PLATELEGS_20429),
		new Item(28017)
	),
	DRAGON_PLATESKIRT_CR(
		new Item(28063),
		new Item(ItemID.DRAGON_PLATESKIRT),
		new Item(28017)
	),
	DRAGON_SCIMITAR_CR(
		new Item(28031),
		new Item(ItemID.DRAGON_SCIMITAR),
		new Item(28017)
	),
	DRAGON_SCIMITAR_20406_CR(
		new Item(28031),
		new Item(ItemID.DRAGON_SCIMITAR_20406),
		new Item(28017)
	),
	DRAGON_SPEAR_CR(
		new Item(28041),
		new Item(ItemID.DRAGON_SPEAR),
		new Item(28017)
	),
	DRAGON_WARHAMMER_CR(
		new Item(28035),
		new Item(ItemID.DRAGON_WARHAMMER),
		new Item(28017)
	),
	DRAGON_WARHAMMER_20785_CR(
		new Item(28035),
		new Item(ItemID.DRAGON_WARHAMMER_20785),
		new Item(28017)
	),
	DRAGON_SWORD_CR(
		new Item(28029),
		new Item(ItemID.DRAGON_SWORD),
		new Item(28017)
	),
	DRAGON_SWORD_21206_CR(
		new Item(28029),
		new Item(ItemID.DRAGON_SWORD_21206),
		new Item(28017)
	),
	DRAGON_SQ_SHIELD_CR(
		new Item(28059),
		new Item(ItemID.DRAGON_SQ_SHIELD),
		new Item(28017)
	),
	///  END OF CORRUPTED SHIT

	DARK_DYE_GRACEFUL_HELM(
		new Item(24745),
		new Item(ItemID.GRACEFUL_HOOD),
		new Item(24729)
	),
	DARK_DYE_GRACEFUL_TOP(
		new Item(24751),
		new Item(ItemID.GRACEFUL_TOP),
		new Item(24729)
	),
	DARK_DYE_GRACEFUL_LEGS(
		new Item(24754),
		new Item(ItemID.GRACEFUL_LEGS),
		new Item(24729)
	),
	DARK_DYE_GRACEFUL_BOOTS(
		new Item(24758),
		new Item(ItemID.GRACEFUL_BOOTS),
		new Item(24729)
	),
	DARK_DYE_GRACEFUL_CAPE(
		new Item(24746),
		new Item(ItemID.GRACEFUL_CAPE),
		new Item(24729)
	),
	DARK_DYE_GRACEFUL_GLOVES(
		new Item(24755),
		new Item(ItemID.GRACEFUL_GLOVES),
		new Item(24729)
	),
	CANNON_BASE(
		new Item(26520),
		new Item(ItemID.CANNON_BASE),
		new Item(26528)
	),
	CANNON_STAND(
		new Item(26522),
		new Item(ItemID.CANNON_STAND),
		new Item(26528)
	),
	CANNON_FURNACE(
		new Item(26526),
		new Item(ItemID.CANNON_FURNACE),
		new Item(26528)
	),
	CANNON_BARREL(
		new Item(26524),
		new Item(ItemID.CANNON_BARREL),
		new Item(26528)
	),
	DRAGON_AXE(
		new Item(25378),
		new Item(ItemID.DRAGON_AXE),
		new Item(25090)
	),
	INFERNAL_AXE(
		new Item(25066),
		new Item(ItemID.INFERNAL_AXE),
		new Item(25090)
	),
	DRAGON_HARPOON(
		new Item(25373),
		new Item(ItemID.DRAGON_HARPOON),
		new Item(25090)
	),
	INFERNAL_HARPOON(
		new Item(25059),
		new Item(ItemID.INFERNAL_HARPOON),
		new Item(25090)
	),
	DRAGON_PICKAXE(
		new Item(25376),
		new Item(ItemID.DRAGON_PICKAXE),
		new Item(25090)
	),
	INFERNAL_PICKAXE(
		new Item(25063),
		new Item(ItemID.INFERNAL_PICKAXE),
		new Item(25090)
	),
	AVAS_ASSEMBLER(
		new Item(27374),
		new Item(ItemID.AVAS_ASSEMBLER),
		new Item(27372)
	),
	ELIDINIS_WARD(
		new Item(27253),
		new Item(27251),
		new Item(27255)
	),
	OSMUMTENS_FANG(
		new Item(27246),
		new Item(26219),
		new Item(27248)
	),

	DRAGON_PICKAXE3(
		new Item(23677),
		new Item(ItemID.DRAGON_PICKAXE),
		new Item(23908)
	),
	DRAGON_HUNTER_CROSSBOW(
		new Item(25918),
		new Item(ItemID.DRAGON_HUNTER_CROSSBOW),
		new Item(7980)
	),
	DRAGON_HUNTER_CROSSBOW2(
		new Item(25916),
		new Item(ItemID.DRAGON_HUNTER_CROSSBOW),
		new Item(21907)
	),
	BOW_OF_FAERDHINEN(
		new Item(25884),
		new Item(25867),
		new Item(23927)
	),
	BLADE_OF_SAELDOR(
		new Item(25870),
		new Item(24551),
		new Item(23927)
	),
	BOW_OF_FAERDHINEN2(
		new Item(25886),
		new Item(25867),
		new Item(23929)
	),
	BLADE_OF_SAELDOR2(
		new Item(25872),
		new Item(24551),
		new Item(23929)
	),
	BOW_OF_FAERDHINEN3(
		new Item(25888),
		new Item(25867),
		new Item(23931)
	),
	BLADE_OF_SAELDOR3(
		new Item(25874),
		new Item(24551),
		new Item(23931)
	),
	BOW_OF_FAERDHINEN4(
		new Item(25890),
		new Item(25867),
		new Item(23933)
	),
	BLADE_OF_SAELDOR4(
		new Item(25876),
		new Item(24551),
		new Item(23933)
	),
	BOW_OF_FAERDHINEN5(
		new Item(25892),
		new Item(25867),
		new Item(23935)
	),
	BLADE_OF_SAELDOR5(
		new Item(25878),
		new Item(24551),
		new Item(23935)
	),
	BOW_OF_FAERDHINEN6(
		new Item(25894),
		new Item(25867),
		new Item(23937)
	),
	BLADE_OF_SAELDOR6(
		new Item(25880),
		new Item(24551),
		new Item(23937)
	),
	BOW_OF_FAERDHINEN7(
		new Item(25896),
		new Item(25867),
		new Item(23941)
	),
	BLADE_OF_SAELDOR7(
		new Item(25882),
		new Item(24551),
		new Item(23941)
	),

	ELDER_CHAOS_HOOD(
		new Item(27119),
		new Item(ItemID.ELDER_CHAOS_HOOD),
		new Item(27113)
	),
	ELDER_CHAOS_TOP(
		new Item(27115),
		new Item(ItemID.ELDER_CHAOS_TOP),
		new Item(27113)
	),
	ELDER_CHAOS_ROBE(
		new Item(27117),
		new Item(ItemID.ELDER_CHAOS_ROBE),
		new Item(27113)
	),
	ELDER_MAUL(
		new Item(27100),
		new Item(ItemID.ELDER_MAUL),
		new Item(27098)
	),
	HEAVY_BALLISTA(
		new Item(26712),
		new Item(ItemID.HEAVY_BALLISTA),
		new Item(26711)
	),
	DAGONHAI_HAT(
		new Item(27123),
		new Item(24288),
		new Item(27121)
	),
	DAGONHAI_TOP(
		new Item(27125),
		new Item(24291),
		new Item(27121)
	),
	DAGONHAI_ROBE(
		new Item(27127),
		new Item(24294),
		new Item(27121)
	),
	DRAGON_CLAWS(
		new Item(26708),
		new Item(ItemID.DRAGON_CLAWS),
		new Item(26707)
	),
	ARMADYL_HELM(
		new Item(26714),
		new Item(ItemID.ARMADYL_HELMET),
		new Item(26713)
	),
	ARMADYL_TOP(
		new Item(26715),
		new Item(ItemID.ARMADYL_CHESTPLATE),
		new Item(26713)
	),
	ARMADYL_CHAINSKIRT(
		new Item(26716),
		new Item(ItemID.ARMADYL_CHAINSKIRT),
		new Item(26713)
	),
	BANDOS_BOOTS(
		new Item(26720),
		new Item(ItemID.BANDOS_BOOTS),
		new Item(26717)
	),
	BANDOS_TASSETS(
		new Item(26719),
		new Item(ItemID.BANDOS_TASSETS),
		new Item(26717)
	),
	BANDOS_CHESTPLATE(
		new Item(26718),
		new Item(ItemID.BANDOS_CHESTPLATE),
		new Item(26717)
	),
	DRAGON_WARHAMMER(
		new Item(26710),
		new Item(ItemID.DRAGON_WARHAMMER),
		new Item(26709)
	),


	;
	Item itemToMake;
	Item itemOne;
	Item itemTwo;

	OrnamentKits(Item itemToMake, Item itemOne, Item itemTwo) {
		this.itemToMake = itemToMake;
		this.itemOne = itemOne;
		this.itemTwo = itemTwo;
	}

	public static final OrnamentKits[] VALUES = values();

	public static void register() {
		for (OrnamentKits kit :
			OrnamentKits.VALUES) {
			ItemAction.registerInventory(kit.itemToMake.getId(), "Dismantle", OrnamentKits::dismantle);
			ItemAction.registerInventory(kit.itemToMake.getId(), "Dismantle kit", OrnamentKits::dismantle);
			ItemItemAction.register(kit.itemOne.getId(), kit.itemTwo.getId(), (player, primary, secondary) -> {
				makeOrnament(player, kit, primary, secondary);
			});
			ItemItemAction.register(kit.itemTwo.getId(), kit.itemOne.getId(), (player, primary, secondary) -> {
				makeOrnament(player, kit, secondary, primary);
			});
		}
	}

	private static OrnamentKits getKitByMadeItem(Item item) {
		for (OrnamentKits kit :
			OrnamentKits.VALUES) {
			if (item.getId() == kit.itemToMake.getId())
				return kit;
		}
		return null;
	}

	private static boolean dismantle(Player player, Item item) {
		OrnamentKits kit = getKitByMadeItem(item);
		if (kit == null) return false;
		if (player.getInventory().getFreeSlots() < 1) {
			player.sendMessage("Not enough inventory space.");
			return false;
		}
		Map<String, String> attributes = new HashMap<>();
		attributes.putAll(item.attributes);
		Item primary = new Item(kit.itemOne.getId());
		primary.attributes.putAll(attributes);
		player.getInventory().remove(item);
		player.getInventory().add(primary);
		player.getInventory().add(kit.itemTwo);
		return true;
	}

	private static void makeOrnament(Player player, OrnamentKits kit, Item primary, Item secondary) {
		Item imbuedItem = new Item(kit.itemToMake.getId());
		Map<String, String> attributes = new HashMap<>();
		if (primary.getId() == kit.itemOne.getId())
			attributes.putAll(primary.attributes);
		if (secondary.getId() == kit.itemOne.getId())
			attributes.putAll(secondary.attributes);
		imbuedItem.attributes.putAll(attributes);
		player.getInventory().remove(kit.itemTwo);
		if (primary.getId() == kit.itemOne.getId())
			primary.remove();
		else
			secondary.remove();
		player.getInventory().add(imbuedItem);
	}
}

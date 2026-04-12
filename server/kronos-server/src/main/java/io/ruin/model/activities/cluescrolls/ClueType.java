package io.ruin.model.activities.cluescrolls;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.api.utils.StringUtils;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.activities.DonationBossHandler;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.combat.Hit;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.ItemContainer;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.utility.Broadcast;

import java.util.ArrayList;
import java.util.Collections;

import static io.ruin.cache.ItemID.COINS_995;

public enum ClueType {

	BEGINNER(
		23182, 23245, 1, 1,
		/**
		 * Common
		 */
		new LootItem(1966, 5, 9, 50),                //Cabbage
		new LootItem(316, 5, 14, 40),                //Shrimps
		new LootItem(326, 5, 17, 40),                //Sardine
		new LootItem(348, 5, 9, 40),                 //Herring
		new LootItem(556, 15, 35, 40),               //Air rune
		new LootItem(558, 15, 35, 40),               //Mind rune
		new LootItem(555, 15, 35, 40),               //Water rune
		new LootItem(557, 15, 35, 40),               //Earth rune
		new LootItem(554, 15, 35, 40),               //Fire rune
		new LootItem(559, 2, 7, 40),                //Body rune
		new LootItem(562, 2, 9, 40),                //Chaos rune
		new LootItem(561, 2, 7, 40),                //Nature rune
		new LootItem(563, 1, 40),                                //Law rune
		new LootItem(882, 1, 40),                                //Bronze arrow
		new LootItem(884, 1, 40),                                //Iron arrow
		new LootItem(841, 1, 40),                                //Shortbow
		new LootItem(839, 1, 40),                                //Longbow
		new LootItem(843, 1, 40),                                //Oak shortbow
		new LootItem(845, 1, 40),                                //Oak longbow
		new LootItem(1267, 1, 40),             //Iron pickaxe
		new LootItem(1381, 1, 40),             //Staff of air
		new LootItem(1383, 1, 40),             //Staff of water
		new LootItem(1385, 1, 40),             //Staff of earth
		new LootItem(1387, 1, 40),             //Staff of fire
		new LootItem(1157, 1, 40),             //Steel full helm
		new LootItem(1119, 1, 40),             //Steel platebody
		new LootItem(1069, 1, 40),             //Steel platelegs
		new LootItem(1083, 1, 40),             //Steel plateskirt
		new LootItem(1295, 1, 40),             //Steel longsword
		new LootItem(1207, 1, 40),             //Steel dagger
		new LootItem(1353, 1, 40),             //Steel axe
		new LootItem(1365, 1, 40),             //Steel battleaxe
		new LootItem(1167, 1, 40),             //Leather cowl
		new LootItem(1129, 1, 40),             //Leather body
		new LootItem(1095, 1, 40),             //Leather chaps
		new LootItem(1063, 1, 40),             //Leather vambraces
		new LootItem(1131, 1, 40),             //Hardleather body
		new LootItem(579, 1, 40),              //Blue wizard hat
		new LootItem(577, 1, 40),              //Blue wizard robe
		new LootItem(1017, 1, 40),             //Wizard hat
		new LootItem(581, 1, 40),              //Black robe

		new LootItem(1313, 1, 1),             //Black 2h sword
		new LootItem(1361, 1, 1),             //Black axe
		new LootItem(1367, 1, 1),             //Black battleaxe
		new LootItem(1217, 1, 1),             //Black dagger
		new LootItem(1165, 1, 1),             //Black full helm
		new LootItem(1195, 1, 1),             //Black kiteshield
		new LootItem(1297, 1, 1),             //Black longsword
		new LootItem(1426, 1, 1),             //Black mace
		new LootItem(1151, 1, 1),             //Black med helm
		new LootItem(12297, 1, 1),            //Black pickaxe
		new LootItem(1125, 1, 1),             //Black platebody
		new LootItem(1089, 1, 1),             //Black plateskirt
		new LootItem(1077, 1, 1),             //Black platelegs
		new LootItem(1179, 1, 1),             //Black sq shield
		new LootItem(1327, 1, 1),             //Black scimitar
		new LootItem(1283, 1, 1),             //Black sword
		new LootItem(1341, 1, 1),             //Black warhammer
		/**
		 * Rare (4 / 25)
		 */
		new LootItem(23285, 1, 5),         //Mole slippers
		new LootItem(23288, 1, 5),         //Frog slippers
		new LootItem(23291, 1, 5),         //Bear feet
		new LootItem(23294, 1, 5),         //Demon feet
		new LootItem(23297, 1, 5),         //Jester cape
		new LootItem(23300, 1, 5),         //Shoulder parrot
		new LootItem(23303, 1, 5),         //Monk's robe top (t)
		new LootItem(23306, 1, 5),         //Monk's robe (t)
		new LootItem(23309, 1, 5),         //Amulet of defence (t)
		new LootItem(23312, 1, 5),         //Sandwich lady hat
		new LootItem(23315, 1, 5),         //Sandwich lady top
		new LootItem(23318, 1, 5),         //Sandwich lady bottom
		new LootItem(23321, 1, 5),         //Rune scimitar ornament kit (guthix)
		new LootItem(23324, 1, 5),         //Rune scimitar ornament kit (saradomin)
		new LootItem(23327, 1, 5)          //Rune scimitar ornament kit (zamorak)
	),
	EASY(
		2677, 20546, 1, 1,
		/**
		 * Common
		 */
		new LootItem(1165, 1, 10),                //Black full helm
		new LootItem(1125, 1, 10),                //Black platebody
		new LootItem(1077, 1, 10),                //Black platelegs
		new LootItem(1089, 1, 10),                //Black plateskirt
		new LootItem(1195, 1, 10),                //Black kiteshield
		new LootItem(1297, 1, 10),                //Black longsword
		new LootItem(1313, 1, 10),                //Black 2h sword
		new LootItem(1367, 1, 10),                //Black battleaxe
		new LootItem(1361, 1, 10),                //Black axe
		new LootItem(1269, 1, 10),                //Steel pickaxe
		new LootItem(1169, 1, 10),                //Coif
		new LootItem(1133, 1, 10),                //Studded body
		new LootItem(1097, 1, 10),                //Studded chaps
		new LootItem(849, 1, 10),                 //Willow shortbow
		new LootItem(857, 1, 10),                 //Yew shortbow
		new LootItem(334, 3, 10, 10),             //Trout
		new LootItem(330, 3, 10, 10),             //Salmon
		/**
		 * Rare (4 / 25)
		 */
		new LootItem(12221, 1, 1),         //Bronze full helm (t)
		new LootItem(12215, 1, 1),         //Bronze platebody (t)
		new LootItem(12217, 1, 1),         //Bronze platelegs (t)
		new LootItem(12219, 1, 1),         //Bronze plateskirt (t)
		new LootItem(12223, 1, 1),         //Bronze kiteshield (t)
		new LootItem(12211, 1, 1),         //Bronze full helm (g)
		new LootItem(12205, 1, 1),         //Bronze platebody (g)
		new LootItem(12207, 1, 1),         //Bronze platelegs (g)
		new LootItem(12209, 1, 1),         //Bronze plateskirt (g)
		new LootItem(12213, 1, 1),         //Bronze kiteshield (g)
		new LootItem(12231, 1, 1),         //Iron full helm (t)
		new LootItem(12225, 1, 1),         //Iron platebody (t)
		new LootItem(12227, 1, 1),         //Iron platelegs (t)
		new LootItem(12229, 1, 1),         //Iron plateskirt (t)
		new LootItem(12233, 1, 1),         //Iron kiteshield (t)
		new LootItem(12241, 1, 1),         //Iron full helm (g)
		new LootItem(12235, 1, 1),         //Iron platebody (g)
		new LootItem(12237, 1, 1),         //Iron platelegs (g)
		new LootItem(12239, 1, 1),         //Iron plateskirt (g)
		new LootItem(12243, 1, 1),         //Iron kiteshield (g)
		new LootItem(20193, 1, 1),         //Steel full helm (t)
		new LootItem(20184, 1, 1),         //Steel platebody (t)
		new LootItem(20187, 1, 1),         //Steel platelegs (t)
		new LootItem(20190, 1, 1),         //Steel plateskirt (t)
		new LootItem(20196, 1, 1),         //Steel kiteshield (t)
		new LootItem(20178, 1, 1),         //Steel full helm (g)
		new LootItem(20169, 1, 1),         //Steel platebody (g)
		new LootItem(20172, 1, 1),         //Steel platelegs (g)
		new LootItem(20175, 1, 1),         //Steel plateskirt (g)
		new LootItem(20181, 1, 1),         //Steel kiteshield (g)
		new LootItem(2587, 1, 1),          //Black full helm (t)
		new LootItem(2583, 1, 1),          //Black platebody (t)
		new LootItem(2585, 1, 1),          //Black platelegs (t)
		new LootItem(3472, 1, 1),          //Black plateskirt (t)
		new LootItem(2589, 1, 1),          //Black kiteshield (t)
		new LootItem(2595, 1, 1),          //Black full helm (g)
		new LootItem(2591, 1, 1),          //Black platebody (g)
		new LootItem(2593, 1, 1),          //Black platelegs (g)
		new LootItem(3473, 1, 1),          //Black plateskirt (g)
		new LootItem(2597, 1, 1),          //Black kiteshield (g)
		new LootItem(2635, 1, 1),          //Black beret
		new LootItem(2633, 1, 1),          //Blue beret
		new LootItem(2637, 1, 1),          //White beret
		new LootItem(12247, 1, 1),         //Red beret
		new LootItem(2631, 1, 1),          //Highwayman mask
		new LootItem(12245, 1, 1),         //Beanie
		new LootItem(7396, 1, 1),          //Blue wizard hat (t)
		new LootItem(7392, 1, 1),          //Blue wizard robe (t)
		new LootItem(7388, 1, 1),          //Blue skirt (t)
		new LootItem(7394, 1, 1),          //Blue wizard hat (g)
		new LootItem(7390, 1, 1),          //Blue wizard robe (g)
		new LootItem(7386, 1, 1),          //Blue skirt (g)
		new LootItem(12455, 1, 1),         //Black wizard hat (t)
		new LootItem(12451, 1, 1),         //Black wizard robe (t)
		new LootItem(12447, 1, 1),         //Black skirt (t)
		new LootItem(12453, 1, 1),         //Black wizard hat (g)
		new LootItem(12449, 1, 1),         //Black wizard robe (g)
		new LootItem(12445, 1, 1),         //Black skirt (g)
		new LootItem(7364, 1, 1),          //Studded body (t)
		new LootItem(7368, 1, 1),          //Studded chaps (t)
		new LootItem(7362, 1, 1),          //Studded body (g)
		new LootItem(7366, 1, 1),          //Studded chaps (g)
		new LootItem(ItemID.LEATHER_BODY_G, 1, 1),
		new LootItem(ItemID.LEATHER_CHAPS_G, 1, 1),
		new LootItem(ItemID.BLACK_HELM_H1, 1, 1),
		new LootItem(ItemID.BLACK_HELM_H2, 1, 1),
		new LootItem(ItemID.BLACK_HELM_H3, 1, 1),
		new LootItem(ItemID.BLACK_HELM_H4, 1, 1),
		new LootItem(ItemID.BLACK_HELM_H5, 1, 1),
		new LootItem(ItemID.BLACK_PLATEBODY_H1, 1, 1),
		new LootItem(ItemID.BLACK_PLATEBODY_H2, 1, 1),
		new LootItem(ItemID.BLACK_PLATEBODY_H3, 1, 1),
		new LootItem(ItemID.BLACK_PLATEBODY_H4, 1, 1),
		new LootItem(ItemID.BLACK_PLATEBODY_H5, 1, 1),
		new LootItem(ItemID.BLACK_SHIELD_H1, 1, 1),
		new LootItem(ItemID.BLACK_SHIELD_H2, 1, 1),
		new LootItem(ItemID.BLACK_SHIELD_H3, 1, 1),
		new LootItem(ItemID.BLACK_SHIELD_H4, 1, 1),
		new LootItem(ItemID.BLACK_SHIELD_H5, 1, 1),
		new LootItem(10408, 1, 1),         //Blue elegant shirt
		new LootItem(10410, 1, 1),         //Blue elegant legs
		new LootItem(10428, 1, 1),         //Blue elegant blouse
		new LootItem(10430, 1, 1),         //Blue elegant skirt
		new LootItem(10412, 1, 1),         //Green elegant shirt
		new LootItem(10414, 1, 1),         //Green elegant legs
		new LootItem(10432, 1, 1),         //Green elegant blouse
		new LootItem(10434, 1, 1),         //Green elegant skirt
		new LootItem(10404, 1, 1),         //Red elegant shirt
		new LootItem(10406, 1, 1),         //Red elegant legs
		new LootItem(10424, 1, 1),         //Red elegant blouse
		new LootItem(10426, 1, 1),         //Red elegant skirt
		new LootItem(10316, 1, 1),         //Bob's red shirt
		new LootItem(10320, 1, 1),         //Bob's green shirt
		new LootItem(10318, 1, 1),         //Bob's blue shirt
		new LootItem(10324, 1, 1),         //Bob's purple shirt
		new LootItem(10322, 1, 1),         //Bob's black shirt
		new LootItem(ItemID.STAFF_OF_BOB_THE_CAT, 1, 1),
		new LootItem(10392, 1, 1),         //A powdered wig
		new LootItem(10394, 1, 1),         //Flared trousers
		new LootItem(10396, 1, 1),         //Pantaloons
		new LootItem(10398, 1, 1),         //Sleeping cap
		new LootItem(10366, 1, 1),         //Amulet of magic (t)
		new LootItem(ItemID.AMULET_OF_POWER_T, 1, 1),
		new LootItem(ItemID.RAIN_BOW, 1, 1),
		new LootItem(ItemID.HAM_JOINT, 1, 1),
		new LootItem(12375, 1, 1),         //Black cane
		new LootItem(12297, 1, 1),         //Black pickaxe
		new LootItem(10462, 1, 1),         //Guthix robe top
		new LootItem(10466, 1, 1),         //Guthix robe legs
		new LootItem(10458, 1, 1),         //Saradomin robe top
		new LootItem(10464, 1, 1),         //Saradomin robe legs
		new LootItem(10460, 1, 1),         //Zamorak robe top
		new LootItem(10468, 1, 1),         //Zamorak robe legs
		new LootItem(12193, 1, 1),         //Ancient robe top
		new LootItem(12195, 1, 1),         //Ancient robe legs
		new LootItem(12265, 1, 1),         //Bandos robe top
		new LootItem(12267, 1, 1),         //Bandos robe legs
		new LootItem(12253, 1, 1),         //Armadyl robe top
		new LootItem(12255, 1, 1),         //Armadyl robe legs
		new LootItem(12249, 1, 1),         //Imp mask
		new LootItem(12251, 1, 1),         //Goblin mask
		new LootItem(20217, 1, 1),         //Team cape i
		new LootItem(20214, 1, 1),         //Team cape x
		new LootItem(20211, 1, 1),         //Team cape zero
		new LootItem(ItemID.CAPE_OF_SKULLS, 1, 1),
		new LootItem(20166, 1, 1),         //Wooden shield (g)
		new LootItem(20205, 1, 1),         //Golden chef's hat
		new LootItem(20208, 1, 1),         //Golden apron
		new LootItem(20199, 1, 1),         //Monk's top (g)
		new LootItem(20202, 1, 1),         //Monk's bottom (g)
		new LootItem(20164, 1, 1)          //Large spade
	),
	MEDIUM(
		2801, 20545, 1, 1,
		/**
		 * Common
		 */
		new LootItem(1161, 1, 1200),                //Adamant full helm
		new LootItem(1123, 1, 1200),                //Adamant platebody
		new LootItem(1073, 1, 1200),                //Adamant platelegs
		new LootItem(1301, 1, 1200),                //Adamant longsword
		new LootItem(1371, 1, 1200),                //Adamant battleaxe
		new LootItem(1357, 1, 1200),                //Adamant axe
		new LootItem(1271, 1, 1200),                //Adamant pickaxe
		new LootItem(ItemID.ADAMANT_DAGGER, 1, 1200),
		new LootItem(1393, 1, 1200),                //Fire battlestaff
		new LootItem(1135, 1, 1200),                //Green d'hide body
		new LootItem(1099, 1, 1200),                //Green d'hide chaps
		new LootItem(857, 1, 1200),                 //Yew shortbow
		new LootItem(ItemID.YEW_LONGBOW, 1, 1200),                 //Yew shortbow
		new LootItem(374, 8, 12, 1200),             //Swordfish
		new LootItem(380, 8, 12, 1200),             //Lobster
		new LootItem(ItemID.YEW_COMP_BOW, 1, 1, 120),
		new LootItem(ItemID.STRENGTH_AMULET_T, 1, 1, 120),
		new LootItem(ItemID.AIR_RUNE, 50, 100, 1200),
		new LootItem(ItemID.MIND_RUNE, 50, 100, 1200),
		new LootItem(ItemID.WATER_RUNE, 50, 100, 1200),
		new LootItem(ItemID.EARTH_RUNE, 50, 100, 1200),
		new LootItem(ItemID.FIRE_RUNE, 50, 100, 1200),
		new LootItem(ItemID.CHAOS_RUNE, 10, 20, 1200),
		new LootItem(ItemID.LAW_RUNE, 10, 20, 1200),
		new LootItem(ItemID.DEATH_RUNE, 10, 20, 1200),
		/**
		 * Rare (4 / 25)
		 */

		new LootItem(12293, 1, 80),         //Mithril full helm (t)
		new LootItem(12287, 1, 80),         //Mithril platebody (t)
		new LootItem(12289, 1, 80),         //Mithril platelegs (t)
		new LootItem(12295, 1, 80),         //Mithril plateskirt (t)
		new LootItem(12291, 1, 80),         //Mithril kiteshield (t)
		new LootItem(12283, 1, 80),         //Mithril full helm (g)
		new LootItem(12277, 1, 80),         //Mithril platebody (g)
		new LootItem(12279, 1, 80),         //Mithril platelegs (g)
		new LootItem(12285, 1, 80),         //Mithril plateskirt (g)
		new LootItem(12281, 1, 80),         //Mithril kiteshield (g)
		new LootItem(2605, 1, 80),          //Adamant full helm (t)
		new LootItem(2599, 1, 80),          //Adamant platebody (t)
		new LootItem(2601, 1, 80),          //Adamant platelegs (t)
		new LootItem(3474, 1, 80),          //Adamant plateskirt (t)
		new LootItem(2603, 1, 80),          //Adamant kiteshield (t)
		new LootItem(2613, 1, 80),          //Adamant full helm (g)
		new LootItem(2607, 1, 80),          //Adamant platebody (g)
		new LootItem(2609, 1, 80),          //Adamant platelegs (g)
		new LootItem(3475, 1, 80),          //Adamant plateskirt (g)
		new LootItem(2611, 1, 80),          //Adamant kiteshield (g)
		new LootItem(2647, 1, 80),          //Black headband
		new LootItem(2645, 1, 80),          //Red headband
		new LootItem(2649, 1, 80),          //Brown headband
		new LootItem(12305, 1, 80),         //Pink headband
		new LootItem(12307, 1, 80),         //Green headband
		new LootItem(12301, 1, 80),         //Blue headband
		new LootItem(12299, 1, 80),         //White headband
		new LootItem(12303, 1, 80),         //Gold headband
		new LootItem(7319, 1, 80),          //Red boater
		new LootItem(7321, 1, 80),          //Orange boater
		new LootItem(7323, 1, 80),          //Green boater
		new LootItem(7325, 1, 80),          //Blue boater
		new LootItem(7327, 1, 80),          //Black boater
		new LootItem(12309, 1, 80),         //Pink boater
		new LootItem(12311, 1, 80),         //Purple boater
		new LootItem(12313, 1, 80),         //White boater
		new LootItem(7372, 1, 80),          //Green d'hide body (t)
		new LootItem(7380, 1, 80),          //Green d'hide chaps (t)
		new LootItem(7370, 1, 80),          //Green d'hide body (g)
		new LootItem(7378, 1, 80),          //Green d'hide chaps (g)
		new LootItem(ItemID.ADAMANT_HELM_H1, 1, 80),
		new LootItem(ItemID.ADAMANT_HELM_H2, 1, 80),
		new LootItem(ItemID.ADAMANT_HELM_H3, 1, 80),
		new LootItem(ItemID.ADAMANT_HELM_H4, 1, 80),
		new LootItem(ItemID.ADAMANT_HELM_H5, 1, 80),
		new LootItem(ItemID.ADAMANT_PLATEBODY_H1, 1, 80),
		new LootItem(ItemID.ADAMANT_PLATEBODY_H2, 1, 80),
		new LootItem(ItemID.ADAMANT_PLATEBODY_H3, 1, 80),
		new LootItem(ItemID.ADAMANT_PLATEBODY_H4, 1, 80),
		new LootItem(ItemID.ADAMANT_PLATEBODY_H5, 1, 80),
		new LootItem(ItemID.ADAMANT_SHIELD_H1, 1, 80),
		new LootItem(ItemID.ADAMANT_SHIELD_H2, 1, 80),
		new LootItem(ItemID.ADAMANT_SHIELD_H3, 1, 80),
		new LootItem(ItemID.ADAMANT_SHIELD_H4, 1, 80),
		new LootItem(ItemID.ADAMANT_SHIELD_H5, 1, 80),
		new LootItem(10400, 1, 80),         //Black elegant shirt
		new LootItem(10402, 1, 80),         //Black elegant legs
		new LootItem(10420, 1, 80),         //White elegant blouse
		new LootItem(10422, 1, 80),         //White elegant skirt
		new LootItem(10416, 1, 80),         //Purple elegant shirt
		new LootItem(10418, 1, 80),         //Purple elegant legs
		new LootItem(10436, 1, 80),         //Purple elegant blouse
		new LootItem(10438, 1, 80),         //Purple elegant skirt
		new LootItem(12315, 1, 80),         //Pink elegant shirt
		new LootItem(12317, 1, 80),         //Pink elegant legs
		new LootItem(12339, 1, 80),         //Pink elegant blouse
		new LootItem(12341, 1, 80),         //Pink elegant skirt
		new LootItem(12347, 1, 80),         //Gold elegant shirt
		new LootItem(12349, 1, 80),         //Gold elegant legs
		new LootItem(12343, 1, 80),         //Gold elegant blouse
		new LootItem(12345, 1, 80),         //Gold elegant skirt
		new LootItem(ItemID.WOLF_CLOAK, 1, 80),
		new LootItem(ItemID.WOLF_MASK, 1, 80),
		new LootItem(ItemID.STRENGTH_AMULET_T, 1, 80),
		new LootItem(12377, 1, 80),         //Adamant cane
		new LootItem(10454, 1, 80),         //Guthix mitre
		new LootItem(10448, 1, 80),         //Guthix cloak
		new LootItem(10452, 1, 80),         //Saradomin mitre
		new LootItem(10446, 1, 80),         //Saradomin cloak
		new LootItem(10456, 1, 80),         //Zamorak mitre
		new LootItem(10450, 1, 80),         //Zamorak cloak
		new LootItem(12203, 1, 80),         //Ancient mitre
		new LootItem(12197, 1, 80),         //Ancient cloak
		new LootItem(12271, 1, 80),         //Bandos mitre
		new LootItem(12273, 1, 80),         //Bandos cloak
		new LootItem(12259, 1, 80),         //Armadyl mitre
		new LootItem(12261, 1, 80),         //Armadyl cloak
		new LootItem(ItemID.ANCIENT_STOLE, 1, 80),
		new LootItem(ItemID.ARMADYL_STOLE, 1, 80),
		new LootItem(ItemID.BANDOS_STOLE, 1, 80),
		new LootItem(ItemID.ANCIENT_CROZIER, 1, 80),
		new LootItem(ItemID.ARMADYL_CROZIER, 1, 80),
		new LootItem(ItemID.BANDOS_CROZIER, 1, 80),
		new LootItem(ItemID.GNOMISH_FIRELIGHTER, 1, 80),
		new LootItem(12361, 1, 80),         //Cat mask
		new LootItem(12428, 1, 80),         //Penguin mask
		new LootItem(12319, 1, 80),         //Crier hat
		new LootItem(20243, 1, 80),         //Crier bell
		new LootItem(20240, 1, 80),         //Crier coat
		new LootItem(12359, 1, 80),         //Leprechaun hat
		new LootItem(20246, 1, 80),         //Black leprechaun hat
		new LootItem(20266, 1, 80),         //Black unicorn mask
		new LootItem(20269, 1, 80),         //White unicorn mask
		new LootItem(20251, 1, 80),         //Arceuus banner
		new LootItem(20254, 1, 80),         //Hosidius banner
		new LootItem(20257, 1, 80),         //Lovakengj banner
		new LootItem(20260, 1, 80),         //Piscarilius banner
		new LootItem(20263, 1, 80),         //Shayzien banner
		new LootItem(20272, 1, 80),         //Cabbage round shield
		new LootItem(20249, 1, 80),         //Clueless scroll
		/**
		 * Mega rare (1 / 293)
		 */
		new LootItem(2577, 1, 80).broadcast(Broadcast.GLOBAL),         //Ranger boots
		new LootItem(23389, 1, 80),         //Spiked Manacles
		new LootItem(12598, 1, 80),        //Holy sandals
		new LootItem(23413, 1, 80),        //Climbing boots (g)
		new LootItem(2579, 1, 80)          //Wizard boots
	),
	HARD(
		2722, 20544, 1, 1,
		/**
		 * Common
		 */
		new LootItem(1163, 1, 1200),                //Rune full helm
		new LootItem(1127, 1, 1200),                //Rune platebody
		new LootItem(1079, 1, 1200),                //Rune platelegs
		new LootItem(1093, 1, 1200),                //Rune plateskirt
		new LootItem(1201, 1, 1200),                //Rune kiteshield
		new LootItem(1303, 1, 1200),                //Rune longsword
		new LootItem(ItemID.RUNE_DAGGER, 1, 1200),
		new LootItem(1373, 1, 1200),                //Rune battleaxe
		new LootItem(1359, 1, 1200),                //Rune axe
		new LootItem(1275, 1, 1200),                //Rune pickaxe
		new LootItem(2503, 1, 1200),                //Black d'hide body
		new LootItem(2497, 1, 1200),                //Black d'hide chaps
		new LootItem(861, 1, 1200),                 //Magic shortbow
		new LootItem(859, 1, 1200),                 //Magic longbow
		new LootItem(ItemID.MAGIC_COMP_BOW, 1, 1200),                 //Magic longbow
		new LootItem(380, 12, 15, 1200),             //Lobster
		new LootItem(386, 12, 15, 1200),             //Shark
		new LootItem(ItemID.NATURE_RUNE, 30, 50, 1200),             //Shark
		new LootItem(ItemID.LAW_RUNE, 30, 50, 1200),             //Shark
		new LootItem(ItemID.BLOOD_RUNE, 20, 30, 1200),             //Shark


		/**
		 * Rare (4 / 25)
		 */
		new LootItem(2627, 1, 80),          //Rune full helm (t)
		new LootItem(2623, 1, 80),          //Rune platebody (t)
		new LootItem(2625, 1, 80),          //Rune platelegs (t)
		new LootItem(3477, 1, 80),          //Rune plateskirt (t)
		new LootItem(2629, 1, 80),          //Rune kiteshield (t)
		new LootItem(2619, 1, 80),          //Rune full helm (g)
		new LootItem(2615, 1, 80),          //Rune platebody (g)
		new LootItem(2617, 1, 80),          //Rune platelegs (g)
		new LootItem(3476, 1, 80),          //Rune plateskirt (g)
		new LootItem(2621, 1, 80),          //Rune kiteshield (g)
		new LootItem(2673, 1, 80),          //Guthix full helm
		new LootItem(2669, 1, 80),          //Guthix platebody
		new LootItem(2671, 1, 80),          //Guthix platelegs
		new LootItem(3480, 1, 80),          //Guthix plateskirt
		new LootItem(2675, 1, 80),          //Guthix kiteshield
		new LootItem(2665, 1, 80),          //Saradomin full helm
		new LootItem(2661, 1, 80),          //Saradomin platebody
		new LootItem(2663, 1, 80),          //Saradomin platelegs
		new LootItem(3479, 1, 80),          //Saradomin plateskirt
		new LootItem(2667, 1, 80),          //Saradomin kiteshield
		new LootItem(2657, 1, 80),          //Zamorak full helm
		new LootItem(2653, 1, 80),          //Zamorak platebody
		new LootItem(2655, 1, 80),          //Zamorak platelegs
		new LootItem(3478, 1, 80),          //Zamorak plateskirt
		new LootItem(2659, 1, 80),          //Zamorak kiteshield
		new LootItem(12466, 1, 80),         //Ancient full helm
		new LootItem(12460, 1, 80),         //Ancient platebody
		new LootItem(12462, 1, 80),         //Ancient platelegs
		new LootItem(12464, 1, 80),         //Ancient plateskirt
		new LootItem(12468, 1, 80),         //Ancient kiteshield
		new LootItem(12486, 1, 80),         //Bandos full helm
		new LootItem(12480, 1, 80),         //Bandos platebody
		new LootItem(12482, 1, 80),         //Bandos platelegs
		new LootItem(12484, 1, 80),         //Bandos plateskirt
		new LootItem(12488, 1, 80),         //Bandos kiteshield
		new LootItem(12476, 1, 80),         //Armadyl full helm
		new LootItem(12470, 1, 80),         //Armadyl platebody
		new LootItem(12472, 1, 80),         //Armadyl platelegs
		new LootItem(12474, 1, 80),         //Armadyl plateskirt
		new LootItem(12478, 1, 80),         //Armadyl kiteshield
		new LootItem(ItemID.RUNE_HELM_H1, 1, 80),
		new LootItem(ItemID.RUNE_HELM_H2, 1, 80),
		new LootItem(ItemID.RUNE_HELM_H3, 1, 80),
		new LootItem(ItemID.RUNE_HELM_H4, 1, 80),
		new LootItem(ItemID.RUNE_HELM_H5, 1, 80),
		new LootItem(ItemID.RUNE_PLATEBODY_H1, 1, 80),
		new LootItem(ItemID.RUNE_PLATEBODY_H2, 1, 80),
		new LootItem(ItemID.RUNE_PLATEBODY_H3, 1, 80),
		new LootItem(ItemID.RUNE_PLATEBODY_H4, 1, 80),
		new LootItem(ItemID.RUNE_PLATEBODY_H5, 1, 80),
		new LootItem(ItemID.RUNE_SHIELD_H1, 1, 80),
		new LootItem(ItemID.RUNE_SHIELD_H2, 1, 80),
		new LootItem(ItemID.RUNE_SHIELD_H3, 1, 80),
		new LootItem(ItemID.RUNE_SHIELD_H4, 1, 80),
		new LootItem(ItemID.RUNE_SHIELD_H5, 1, 80),
		new LootItem(7376, 1, 80),          //Blue d'hide body (t)
		new LootItem(7384, 1, 80),          //Blue d'hide chaps (t)
		new LootItem(7374, 1, 80),          //Blue d'hide body (g)
		new LootItem(7382, 1, 80),          //Blue d'hide chaps (g)
		new LootItem(12331, 1, 80),         //Red d'hide body (t)
		new LootItem(12333, 1, 80),         //Red d'hide chaps (t)
		new LootItem(12327, 1, 80),         //Red d'hide body (g)
		new LootItem(12329, 1, 80),         //Red d'hide chaps (g)
		new LootItem(7400, 1, 80),          //Enchanted hat
		new LootItem(7399, 1, 80),          //Enchanted top
		new LootItem(7398, 1, 80),          //Enchanted robe
		new LootItem(2581, 1, 80),          //Robin hood hat
		new LootItem(2639, 1, 80),          //Tan cavalier
		new LootItem(2641, 1, 80),          //Dark cavalier
		new LootItem(2643, 1, 80),          //Black cavalier
		new LootItem(12323, 1, 80),         //Red cavalier
		new LootItem(12321, 1, 80),         //White cavalier
		new LootItem(12325, 1, 80),         //Navy cavalier
		new LootItem(8950, 1, 80), //Pirate hat
		new LootItem(10362, 1, 80),         //Amulet of glory (t)
		new LootItem(10382, 1, 80),         //Guthix coif
		new LootItem(10378, 1, 80),         //Guthix dragonhide
		new LootItem(10380, 1, 80),         //Guthix chaps
		new LootItem(10376, 1, 80),         //Guthix bracers
		new LootItem(10390, 1, 80),         //Saradomin coif
		new LootItem(10386, 1, 80),         //Saradomin d'hide
		new LootItem(10388, 1, 80),         //Saradomin chaps
		new LootItem(10384, 1, 80),         //Saradomin bracers
		new LootItem(10374, 1, 80),         //Zamorak coif
		new LootItem(10370, 1, 80),         //Zamorak d'hide
		new LootItem(10372, 1, 80),         //Zamorak chaps
		new LootItem(10368, 1, 80),         //Zamorak bracers
		new LootItem(12512, 1, 80),         //Armadyl coif
		new LootItem(12508, 1, 80),         //Armadyl d'hide
		new LootItem(12510, 1, 80),         //Armadyl chaps
		new LootItem(12506, 1, 80),         //Armadyl bracers
		new LootItem(12496, 1, 80),         //Ancient coif
		new LootItem(12492, 1, 80),         //Ancient d'hide
		new LootItem(12494, 1, 80),         //Ancient chaps
		new LootItem(12490, 1, 80),         //Ancient bracers
		new LootItem(12504, 1, 80),         //Bandos coif
		new LootItem(12500, 1, 80),         //Bandos d'hide
		new LootItem(12502, 1, 80),         //Bandos chaps
		new LootItem(12498, 1, 80),         //Bandos bracers
		new LootItem(19927, 1, 80),         //Guthix d'hide boots
		new LootItem(19933, 1, 80),         //Saradomin d'hide boots
		new LootItem(19936, 1, 80),         //Zamorak d'hide boots
		new LootItem(19930, 1, 80),         //Armadyl d'hide boots
		new LootItem(19921, 1, 80),         //Ancient d'hide boots
		new LootItem(19924, 1, 80),         //Bandos d'hide boots
		new LootItem(ItemID.ARMADYL_DHIDE_SHIELD, 1, 80),
		new LootItem(ItemID.BANDOS_DHIDE_SHIELD, 1, 80),
		new LootItem(ItemID.ZAMORAK_DHIDE_SHIELD, 1, 80),
		new LootItem(ItemID.SARADOMIN_DHIDE_SHIELD, 1, 80),
		new LootItem(ItemID.ANCIENT_DHIDE_SHIELD, 1, 80),
		new LootItem(ItemID.GUTHIX_DHIDE_SHIELD, 1, 80),
		new LootItem(10472, 1, 80),         //Guthix stole
		new LootItem(10442, 1, 80),         //Guthix crozier
		new LootItem(10470, 1, 80),         //Saradomin stole
		new LootItem(10440, 1, 80),         //Saradomin crozier
		new LootItem(10474, 1, 80),         //Zamorak stole
		new LootItem(10444, 1, 80),         //Zamorak crozier
		new LootItem(12518, 1, 80),         //Green dragon mask
		new LootItem(12520, 1, 80),         //Blue dragon mask
		new LootItem(12522, 1, 80),         //Red dragon mask
		new LootItem(12524, 1, 80),         //Black dragon mask
		new LootItem(12516, 1, 80),         //Pith helmet
		new LootItem(12514, 1, 80),         //Explorer backpack
		new LootItem(12379, 1, 80),         //Rune cane
		new LootItem(19912, 1, 80),         //Zombie head
		new LootItem(19915, 1, 80),         //Cyclops head
		new LootItem(19918, 1, 80),         //Nunchaku
		new LootItem(ItemID.TZHAARKETOM_ORNAMENT_KIT, 1, 80),
		new LootItem(ItemID.DRAGON_BOOTS_ORNAMENT_KIT, 1, 80),
		new LootItem(23206, 1, 80), //Dual sai
		new LootItem(23224, 1, 80), //Thieving bag
		new LootItem(23227, 1, 80), //Rune defender orn kit
		new LootItem(23237, 1, 80), //Berserker necklace orn kit


		/**
		 * Mega rare (1 / 351)
		 */

		new LootItem(ItemID.GILDED_HASTA, 1, 2),
		new LootItem(ItemID.GILDED_FULL_HELM, 1, 2),
		new LootItem(ItemID.GILDED_PLATEBODY, 1, 2),
		new LootItem(ItemID.GILDED_KITESHIELD, 1, 2),
		new LootItem(ItemID.GILDED_PLATESKIRT, 1, 2),
		new LootItem(ItemID.GILDED_PLATELEGS, 1, 2),
		new LootItem(ItemID.GILDED_CHAINBODY, 1, 2),
		new LootItem(ItemID.GILDED_MED_HELM, 1, 2),
		new LootItem(ItemID.GILDED_SQ_SHIELD, 1, 2),
		new LootItem(ItemID.GILDED_2H_SWORD, 1, 2),
		new LootItem(ItemID.GILDED_SPEAR, 1, 2),


		new LootItem(ItemID.SUPER_DEFENCE4 + 1, 15, 2),
		new LootItem(ItemID.SUPER_STRENGTH4 + 1, 15, 2),
		new LootItem(ItemID.SUPER_ATTACK4 + 1, 15, 2),
		new LootItem(ItemID.SUPER_RESTORE4 + 1, 15, 2),
		new LootItem(ItemID.SUPER_ENERGY4 + 1, 15, 2),
		new LootItem(ItemID.ANTIFIRE_POTION4 + 1, 15, 2),
		/**
		 * Ultra rare (1 / 3510)
		 */
		new LootItem(10350, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age full helmet
		new LootItem(10348, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age platebody
		new LootItem(10346, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age platelegs
		new LootItem(10352, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age kiteshield
		new LootItem(23242, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age plateskirt

		new LootItem(10334, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age range coif
		new LootItem(10330, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age range top
		new LootItem(10332, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age range legs
		new LootItem(10336, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age vambraces
		new LootItem(23185, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age ring

		new LootItem(10342, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age mage hat
		new LootItem(10338, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age robe top
		new LootItem(10340, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age robe
		new LootItem(10344, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age amulet

		new LootItem(20011, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age axe
		new LootItem(20014, 1, 1).broadcast(Broadcast.GLOBAL),          //3rd age pickaxe

		new LootItem(23336, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age druidic robe top
		new LootItem(23339, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age druidic robe bottoms
		new LootItem(23345, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age druidic cloak
		new LootItem(23342, 1, 1).broadcast(Broadcast.GLOBAL)          //3rd age druidic staff
	),
	ELITE(
		12073, 20543, 1, 1,
		/**
		 * Common
		 */
		new LootItem(ItemID.DRAGON_DAGGER, 1, 1500),
		new LootItem(ItemID.DRAGON_MACE, 1, 1500),
		new LootItem(ItemID.DRAGON_LONGSWORD, 1, 1500),
		new LootItem(1127, 1, 1500),                //Rune platebody
		new LootItem(1079, 1, 1500),                //Rune platelegs
		new LootItem(1093, 1, 1500),                //Rune plateskirt
		new LootItem(1201, 1, 1500),                //Rune kiteshield


		new LootItem(1645, 1, 1500),                //Dragonstone ring
		new LootItem(ItemID.DRAGONSTONE_BRACELET, 1, 1500),
		new LootItem(ItemID.DRAGON_NECKLACE, 1, 1500),
		new LootItem(9194, 8, 12, 1500),            //Onyx bolt tips
		new LootItem(5315, 1, 1500),                //Yew seed
		new LootItem(5316, 1, 1500),                //Magic seed
		new LootItem(985, 1, 1500),                 //Tooth half of key
		new LootItem(987, 1, 1500),                 //Loop half of key
		new LootItem(ItemID.PALM_TREE_SEED, 1, 1500),                 //Crystal key
		new LootItem(ItemID.LAW_RUNE, 50, 75, 1500),                 //Crystal key
		new LootItem(ItemID.DEATH_RUNE, 50, 75, 1500),                 //Crystal key
		new LootItem(ItemID.BLOOD_RUNE, 50, 75, 1500),                 //Crystal key
		new LootItem(ItemID.SOUL_RUNE, 50, 75, 1500),                 //Crystal key
		new LootItem(ItemID.RUNITE_BAR + 1, 1, 3, 1500),                 //Crystal key
		new LootItem(ItemID.MAHOGANY_PLANK + 1, 20, 30, 1500),                 //Crystal key
		new LootItem(ItemID.TEAK_PLANK + 1, 40, 50, 1500),                 //Crystal key
		new LootItem(ItemID.OAK_PLANK + 1, 60, 80, 1500),                 //Crystal key
		new LootItem(7061, 15, 20, 1500),             //Tuna potato
		new LootItem(7219, 15, 20, 1500),             //Summer pie

		new LootItem(9185, 1, 1500),                    //Rune crossbow
		/**
		 * Rare (4 / 25)
		 */
		new LootItem(ItemID.DRAGON_FULL_HELM_ORNAMENT_KIT, 1, 105),
		new LootItem(ItemID.DRAGON_CHAINBODY_ORNAMENT_KIT, 1, 105),
		new LootItem(ItemID.DRAGON_LEGSSKIRT_ORNAMENT_KIT, 1, 105),
		new LootItem(ItemID.DRAGON_SQ_SHIELD_ORNAMENT_KIT, 1, 105),
		new LootItem(ItemID.DRAGON_SCIMITAR_ORNAMENT_KIT, 1, 105),
		new LootItem(ItemID.FURY_ORNAMENT_KIT, 1, 105),
		new LootItem(12530, 1, 105),         //Light infinity colour kit
		new LootItem(12528, 1, 105),         //Dark infinity colour kit
		new LootItem(12397, 1, 105),         //Royal crown
		new LootItem(12393, 1, 105),         //Royal gown top
		new LootItem(12395, 1, 105),         //Royal gown bottom
		new LootItem(12439, 1, 105),         //Royal sceptre
		new LootItem(12351, 1, 105),         //Musketeer hat
		new LootItem(12441, 1, 105),         //Musketeer tabard
		new LootItem(12443, 1, 105),         //Musketeer pants
		new LootItem(12385, 1, 105),         //Black d'hide body (t)
		new LootItem(12387, 1, 105),         //Black d'hide chaps (t)
		new LootItem(12381, 1, 105),         //Black d'hide body (g)
		new LootItem(12383, 1, 105),         //Black d'hide chaps (g)
		new LootItem(12596, 1, 75),         //Rangers' tunic
		new LootItem(19994, 1, 105),         //Ranger gloves
		new LootItem(19997, 1, 105),         //Holy wraps
		new LootItem(12363, 1, 105),         //Bronze dragon mask
		new LootItem(12365, 1, 105),         //Iron dragon mask
		new LootItem(12367, 1, 105),         //Steel dragon mask
		new LootItem(12369, 1, 105),         //Mithril dragon mask
		new LootItem(ItemID.ADAMANT_DRAGON_MASK, 1, 105),
		new LootItem(ItemID.RUNE_DRAGON_MASK, 1, 105),
		new LootItem(19943, 1, 105),         //Arceuus house scarf
		new LootItem(19946, 1, 105),         //Hosidius house scarf
		new LootItem(19949, 1, 105),         //Lovakengj house scarf
		new LootItem(19952, 1, 105),         //Piscarilius house scarf
		new LootItem(19955, 1, 105),         //Shayzien house scarf
		new LootItem(12357, 1, 105),         //Katana
		new LootItem(12373, 1, 105),         //Dragon cane
		new LootItem(19991, 1, 105),         //Bucket helm
		new LootItem(19988, 1, 105),         //Blacksmith's helm
		new LootItem(12540, 1, 105),         //Deerstalker
		new LootItem(12430, 1, 105),         //Afro
		new LootItem(12355, 1, 105),         //Big pirate hat
		new LootItem(12432, 1, 105),         //Top hat
		new LootItem(12353, 1, 105),         //Monocle
		new LootItem(12335, 1, 105),         //Briefcase
		new LootItem(12337, 1, 105),         //Sagacious spectacles
		new LootItem(23249, 1, 105),         //Rangers' tights
		new LootItem(23255, 1, 105),         //Uri's hat
		new LootItem(ItemID.GIANT_BOOT, 1, 105),
		new LootItem(ItemID.FREMENNIK_KILT, 1, 105),
		new LootItem(ItemID.DARK_BOW_TIE, 1, 105),
		new LootItem(ItemID.LIGHT_BOW_TIE, 1, 105),
		new LootItem(ItemID.DARK_TUXEDO_JACKET, 1, 105),
		new LootItem(ItemID.LIGHT_TUXEDO_JACKET, 1, 105),
		new LootItem(ItemID.DARK_TUXEDO_CUFFS, 1, 105),
		new LootItem(ItemID.DARK_TUXEDO_SHOES, 1, 105),
		new LootItem(ItemID.LIGHT_TUXEDO_SHOES, 1, 105),
		new LootItem(ItemID.LIGHT_TROUSERS, 1, 105),
		new LootItem(ItemID.DARK_TROUSERS, 1, 105),
		new LootItem(ItemID.LIGHT_TUXEDO_CUFFS, 1, 105),
		new LootItem(20005, 1, 1),         //Ring of nature
		new LootItem(19941, 1, 1),         //Heavy casket
		/**
		 * Mega rare (1 / 206.25)
		 */
		new LootItem(ItemID.GILDED_DHIDE_VAMBS, 1, 2),
		new LootItem(ItemID.GILDED_DHIDE_BODY, 1, 2),
		new LootItem(ItemID.GILDED_PICKAXE, 1, 2),
		new LootItem(ItemID.GILDED_AXE, 1, 2),
		new LootItem(ItemID.GILDED_SPADE, 1, 2),
		new LootItem(ItemID.GILDED_HASTA, 1, 2),
		new LootItem(ItemID.GILDED_FULL_HELM, 1, 2),
		new LootItem(ItemID.GILDED_PLATEBODY, 1, 2),
		new LootItem(ItemID.GILDED_KITESHIELD, 1, 2),
		new LootItem(ItemID.GILDED_PLATESKIRT, 1, 2),
		new LootItem(ItemID.GILDED_PLATELEGS, 1, 2),
		new LootItem(ItemID.GILDED_CHAINBODY, 1, 2),
		new LootItem(ItemID.GILDED_MED_HELM, 1, 2),
		new LootItem(ItemID.GILDED_SQ_SHIELD, 1, 2),
		new LootItem(ItemID.GILDED_2H_SWORD, 1, 2),
		new LootItem(ItemID.GILDED_SPEAR, 1, 2),
		new LootItem(ItemID.GILDED_DHIDE_CHAPS, 1, 2),
		new LootItem(ItemID.GILDED_COIF, 1, 2),
		new LootItem(ItemID.GILDED_SCIMITAR, 1, 2),
		new LootItem(ItemID.GILDED_BOOTS, 1, 2),
		new LootItem(ItemID.CABBAGE, 3, 2),
		new LootItem(ItemID.ANTIVENOM4 + 1, 15, 2),
		new LootItem(ItemID.BATTLESTAFF + 1, 100, 2),
		new LootItem(ItemID.CRYSTAL_KEY, 1, 2),
		new LootItem(ItemID.RANGING_POTION4 + 1, 30, 2),
		new LootItem(ItemID.SARADOMIN_BREW4 + 1, 30, 2),
		new LootItem(ItemID.SUPER_RESTORE4 + 1, 30, 2),
		new LootItem(ItemID.EXTENDED_ANTIFIRE4 + 1, 30, 2),
		new LootItem(ItemID.LAVA_DRAGON_MASK, 1, 2),
		/**
		 * Ultra rare (1 / 3300)
		 */
		new LootItem(10350, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age full helmet
		new LootItem(10348, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age platebody
		new LootItem(10346, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age platelegs
		new LootItem(10352, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age kiteshield
		new LootItem(23242, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age plateskirt

		new LootItem(10342, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age mage hat
		new LootItem(10338, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age robe top
		new LootItem(10340, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age robe
		new LootItem(10344, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age amulet

		new LootItem(10334, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age range coif
		new LootItem(10330, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age range top
		new LootItem(10332, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age range legs
		new LootItem(10336, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age vambraces

		new LootItem(12437, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age cloak
		new LootItem(12422, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age wand
		new LootItem(23185, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age ring
		new LootItem(12424, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age bow
		new LootItem(12426, 1, 1).broadcast(Broadcast.GLOBAL),      //3rd age longsword
		new LootItem(20011, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age axe
		new LootItem(20014, 1, 1).broadcast(Broadcast.GLOBAL),          //3rd age pickaxe

		new LootItem(23336, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age druidic robe top
		new LootItem(23339, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age druidic robe bottoms
		new LootItem(23345, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age druidic cloak
		new LootItem(23342, 1, 1).broadcast(Broadcast.GLOBAL)          //3rd age druidic staff
	),
	MASTER(
		19835, 19836, 1, 1,
		/**
		 * Common
		 */
		new LootItem(ItemID.DRAGON_DAGGER, 1, 150),
		new LootItem(ItemID.DRAGON_MACE + 1, 1, 4, 150),
		new LootItem(ItemID.DRAGON_LONGSWORD + 1, 1, 4, 150),
		new LootItem(ItemID.DRAGON_SCIMITAR + 1, 1, 4, 150),
		new LootItem(ItemID.DRAGON_BATTLEAXE + 1, 1, 4, 150),
		new LootItem(ItemID.DRAGON_HALBERD + 1, 1, 4, 100),
		new LootItem(392, 20, 40, 100),
		new LootItem(ItemID.NATURE_RUNE, 100, 350, 100),
		new LootItem(ItemID.DEATH_RUNE, 100, 350, 150),
		new LootItem(ItemID.BLOOD_RUNE, 100, 350, 100),
		new LootItem(ItemID.SOUL_RUNE, 100, 350, 150),
		new LootItem(ItemID.WINE_OF_ZAMORAK + 1, 35, 75, 150),
		new LootItem(ItemID.ONYX_BOLTS_E, 25, 75, 150),            //Onyx bolt
		new LootItem(5315, 2, 6, 100),                //Yew seed
		new LootItem(5316, 1, 5, 150),                //Magic seed
		new LootItem(985, 1, 150),                 //Tooth half of key
		new LootItem(987, 1, 100),                 //Loop half of key
		new LootItem(ItemID.LIMPWURT_ROOT + 1, 40, 60, 150),
		new LootItem(ItemID.GRIMY_RANARR_WEED + 1, 7, 25, 100),
		new LootItem(ItemID.GRIMY_TOADFLAX + 1, 25, 40, 150),
		new LootItem(ItemID.GRIMY_SNAPDRAGON + 1, 5, 20, 100),
		new LootItem(ItemID.RUNITE_ORE + 1, 8, 20, 150),
		new LootItem(ItemID.RUNITE_BAR + 1, 6, 12, 150),
		new LootItem(ItemID.BLACK_DRAGONHIDE + 1, 9, 25, 150),
		new LootItem(ItemID.PALM_TREE_SEED, 2, 4, 100),
		/**
		 * Rare (4 / 25)
		 */
		new LootItem(20113, 1, 90),         //Arceuus house hood
		new LootItem(20116, 1, 90),         //Hosidius house hood
		new LootItem(20119, 1, 90),         //Lovakengj house hood
		new LootItem(20122, 1, 90),         //Piscarilius house hood
		new LootItem(20125, 1, 90),         //Shayzien house hood
		new LootItem(20020, 1, 90),         //Lesser demon mask
		new LootItem(20023, 1, 90),         //Greater demon mask
		new LootItem(20026, 1, 90),         //Black demon mask
		new LootItem(20032, 1, 90),         //Jungle demon mask
		new LootItem(20029, 1, 90),         //Old demon mask
		new LootItem(20035, 1, 90),         //Samurai kasa
		new LootItem(20038, 1, 90),         //Samurai shirt
		new LootItem(20041, 1, 90),         //Samurai gloves
		new LootItem(20044, 1, 90),         //Samurai greaves
		new LootItem(20047, 1, 90),         //Samurai boots
		new LootItem(20080, 1, 18),         //Mummy's head
		new LootItem(20083, 1, 18),         //Mummy's body
		new LootItem(20086, 1, 18),         //Mummy's hands
		new LootItem(20089, 1, 18),         //Mummy's legs
		new LootItem(20092, 1, 18),         //Mummy's feet
		new LootItem(20095, 1, 18),         //Ankou mask
		new LootItem(20098, 1, 18),         //Ankou top
		new LootItem(20101, 1, 18),         //Ankou gloves
		new LootItem(20104, 1, 18),         //Ankou's leggings
		new LootItem(20107, 1, 18),         //Ankou socks
		new LootItem(20128, 1, 90),         //Hood of darkness
		new LootItem(20131, 1, 90),         //Robe top of darkness
		new LootItem(20134, 1, 90),         //Gloves of darkness
		new LootItem(20137, 1, 90),         //Robe bottom of darkness
		new LootItem(20140, 1, 90),         //Boots of darkness
		new LootItem(ItemID.ARMADYL_GODSWORD_ORNAMENT_KIT, 1, 15),
		new LootItem(ItemID.BANDOS_GODSWORD_ORNAMENT_KIT, 1, 15),
		new LootItem(ItemID.ZAMORAK_GODSWORD_ORNAMENT_KIT, 1, 15),
		new LootItem(ItemID.SARADOMIN_GODSWORD_ORNAMENT_KIT, 1, 15),
		new LootItem(ItemID.TORTURE_ORNAMENT_KIT, 1, 15),
		new LootItem(ItemID.ANGUISH_ORNAMENT_KIT, 1, 15),
		new LootItem(ItemID.TORMENTED_ORNAMENT_KIT, 1, 15),
		new LootItem(ItemID.OCCULT_ORNAMENT_KIT, 1, 15),
		new LootItem(ItemID.DRAGON_DEFENDER_ORNAMENT_KIT, 1, 15),
		new LootItem(ItemID.DRAGON_KITESHIELD_ORNAMENT_KIT, 1, 15),
		new LootItem(ItemID.DRAGON_PLATEBODY_ORNAMENT_KIT, 1, 15),
		/**
		 * Mega rare (1 / 206.25)
		 */
		new LootItem(20017, 1, 2),     //Ring of coins
		new LootItem(19724, 1, 10),     //Left eye patch
		new LootItem(20050, 1, 10),     //Obsidian cape (r)
		new LootItem(20008, 1, 10),     //Fancy tiara
		new LootItem(20053, 1, 10),     //Half moon spectacles
		new LootItem(20056, 1, 10),     //Ale of the gods
		new LootItem(20059, 1, 10),     //Bucket helm (g)
		new LootItem(20110, 1, 10),     //Bowl wig
		new LootItem(19730, 1, 1),     //Bloodhound
		new LootItem(ItemID.GILDED_DHIDE_VAMBS, 1, 2),
		new LootItem(ItemID.GILDED_DHIDE_BODY, 1, 2),
		new LootItem(ItemID.GILDED_PICKAXE, 1, 2),
		new LootItem(ItemID.GILDED_AXE, 1, 2),
		new LootItem(ItemID.GILDED_SPADE, 1, 2),
		new LootItem(ItemID.GILDED_HASTA, 1, 2),
		new LootItem(ItemID.GILDED_FULL_HELM, 1, 2),
		new LootItem(ItemID.GILDED_PLATEBODY, 1, 2),
		new LootItem(ItemID.GILDED_KITESHIELD, 1, 2),
		new LootItem(ItemID.GILDED_PLATESKIRT, 1, 2),
		new LootItem(ItemID.GILDED_PLATELEGS, 1, 2),
		new LootItem(ItemID.GILDED_CHAINBODY, 1, 2),
		new LootItem(ItemID.GILDED_MED_HELM, 1, 2),
		new LootItem(ItemID.GILDED_SQ_SHIELD, 1, 2),
		new LootItem(ItemID.GILDED_2H_SWORD, 1, 2),
		new LootItem(ItemID.GILDED_SPEAR, 1, 2),
		new LootItem(ItemID.GILDED_DHIDE_CHAPS, 1, 2),
		new LootItem(ItemID.GILDED_COIF, 1, 2),
		new LootItem(ItemID.GILDED_SCIMITAR, 1, 2),
		new LootItem(ItemID.GILDED_BOOTS, 1, 2),
		new LootItem(ItemID.CABBAGE, 3, 1),
		new LootItem(ItemID.ANTIVENOM4 + 1, 15, 1),
		new LootItem(ItemID.TORSTOL + 1, 50, 1),
		/**
		 * Ultra rare (1 / 3300)
		 */
		new LootItem(10350, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age full helmet
		new LootItem(10348, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age platebody
		new LootItem(10346, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age platelegs
		new LootItem(10352, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age kiteshield
		new LootItem(23242, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age plateskirt

		new LootItem(10342, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age mage hat
		new LootItem(10338, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age robe top
		new LootItem(10340, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age robe
		new LootItem(10344, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age amulet

		new LootItem(10334, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age range coif
		new LootItem(10330, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age range top
		new LootItem(10332, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age range legs
		new LootItem(10336, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age vambraces

		new LootItem(12437, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age cloak
		new LootItem(23185, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age ring
		new LootItem(12422, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age wand
		new LootItem(12424, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age bow
		new LootItem(12426, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age longsword

		new LootItem(23336, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age druidic robe top
		new LootItem(23339, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age druidic robe bottoms
		new LootItem(23345, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age druidic cloak
		new LootItem(23342, 1, 1).broadcast(Broadcast.GLOBAL),          //3rd age druidic staff

		new LootItem(20011, 1, 1).broadcast(Broadcast.GLOBAL),         //3rd age axe
		new LootItem(20014, 1, 1).broadcast(Broadcast.GLOBAL)          //3rd age pickaxe
	);

	public final int clueId, casketId;

	private final int minStages, maxStages;

	private final String descriptiveName;

	public final LootItem[] loots;

	ClueType(int clueId, int casketId, int minStages, int maxStages, LootItem... loots) {
		this.clueId = clueId;
		this.casketId = casketId;
		this.minStages = minStages;
		this.maxStages = maxStages;
		this.descriptiveName = StringUtils.vowelStart(name()) ? ("an " + name().toLowerCase()) : ("a " + name().toLowerCase());
		/**
		 * Loots
		 */
		ArrayList<LootItem> list = new ArrayList<>();
		double baseChance = 1d / 10d;
		LootItem[] baseLoots = {
			new LootItem(3827, 1, 10),          //Saradomin page 1
			new LootItem(3828, 1, 10),          //Saradomin page 2
			new LootItem(3829, 1, 10),          //Saradomin page 3
			new LootItem(3830, 1, 10),          //Saradomin page 4
			new LootItem(3835, 1, 10),          //Guthix page 1
			new LootItem(3836, 1, 10),          //Guthix page 2
			new LootItem(3837, 1, 10),          //Guthix page 3
			new LootItem(3838, 1, 10),          //Guthix page 4
			new LootItem(3831, 1, 10),          //Zamorak page 1
			new LootItem(3832, 1, 10),          //Zamorak page 2
			new LootItem(3833, 1, 10),          //Zamorak page 3
			new LootItem(3834, 1, 10),          //Zamorak page 4
			new LootItem(12617, 1, 10),         //Armadyl page 1
			new LootItem(12618, 1, 10),         //Armadyl page 2
			new LootItem(12619, 1, 10),         //Armadyl page 3
			new LootItem(12620, 1, 10),         //Armadyl page 4
			new LootItem(12621, 1, 10),         //Ancient page 1
			new LootItem(12622, 1, 10),         //Ancient page 2
			new LootItem(12623, 1, 10),         //Ancient page 3
			new LootItem(12624, 1, 10),         //Ancient page 4
			new LootItem(12613, 1, 10),         //Bandos page 1
			new LootItem(12614, 1, 10),         //Bandos page 2
			new LootItem(12615, 1, 10),         //Bandos page 3
			new LootItem(12616, 1, 10),         //Bandos page 4
			new LootItem(10326, 1, 10),         //Purple firelighter
			new LootItem(7329, 1, 10),          //Red firelighter
			new LootItem(7331, 1, 10),          //Blue firelighter
			new LootItem(7330, 1, 10),          //Green firelighter
			new LootItem(10327, 1, 10),         //White firelighter
			new LootItem(20220, 1, 20),         //Holy blessing
			new LootItem(20223, 1, 20),         //Unholy blessing
			new LootItem(20226, 1, 20),         //Peaceful blessing
			new LootItem(20229, 1, 20),         //Honourable blessing
			new LootItem(20232, 1, 20),         //War blessing
			new LootItem(20235, 1, 20),         //Ancient blessing
			new LootItem(10476, 10, 50, 10),    //Purple sweets
		};
		if (casketId != 23245) {
			Collections.addAll(list, baseLoots);
		}
		Collections.addAll(list, loots);
		this.loots = list.toArray(new LootItem[list.size()]);
	}

	private void open(Player player) {
		if (player == null) {
		} else {
			ClueSave save;
			if (this == MASTER) {
				if (player.masterClue == null)
					player.masterClue = new ClueSave();
				save = player.masterClue;
			} else if (this == ELITE) {
				if (player.eliteClue == null)
					player.eliteClue = new ClueSave();
				save = player.eliteClue;
			} else if (this == HARD) {
				if (player.hardClue == null)
					player.hardClue = new ClueSave();
				save = player.hardClue;
			} else if (this == MEDIUM) {
				if (player.medClue == null)
					player.medClue = new ClueSave();
				save = player.medClue;
			} else if (this == EASY) {
				if (player.easyClue == null)
					player.easyClue = new ClueSave();
				save = player.easyClue;
			} else {
				if (player.beginnerClue == null)
					player.beginnerClue = new ClueSave();
				save = player.beginnerClue;
			}
			if (save.id == -1 || Clue.CLUES[save.id] == null) {
				ArrayList<Clue> clues = new ArrayList<>();
				for (Clue clue : Clue.CLUES) {
					if (clue != null && clue.type.ordinal() <= ordinal())
						clues.add(clue);
				}
				//Collections.shuffle(clues);
				save.id = clues.get(0).id;
				if (save.remaining == 0)
					save.remaining = Random.get(minStages, maxStages);
			}
			Clue.CLUES[save.id].open(player);
		}
	}

	private boolean hasCluePerk(Player player, Item item) {
		if (item == null)
			return false;
		ObjType def = item.getDef();
		if (def.clueType == null)
			return false;
		String message = "Great job, you have completed your clue scroll!";
		if (player.getInventory().hasId(ClueType.MASTER.clueId)) {
			player.getInventory().remove(ClueType.MASTER.clueId, 1);
			player.getInventory().add(ClueType.MASTER.casketId, 1);
			message += " You have now completed a total of " + NumberUtils.formatNumber(++player.masterClueCount) + " master clue scrolls!";
			player.dialogue(new ItemDialogue().one(def.clueType.casketId, message));
			player.sendMessage(message);
			return true;
		} else if (player.getInventory().hasId(ClueType.ELITE.clueId)) {
			player.getInventory().remove(ClueType.ELITE.clueId, 1);
			player.getInventory().add(ClueType.ELITE.casketId, 1);
			message += " You have now completed a total of " + NumberUtils.formatNumber(++player.eliteClueCount) + " elite clue scrolls!";
			player.dialogue(new ItemDialogue().one(def.clueType.casketId, message));
			player.sendMessage(message);
			return true;
		} else if (player.getInventory().hasId(ClueType.HARD.clueId)) {
			player.getInventory().remove(ClueType.HARD.clueId, 1);
			player.getInventory().add(ClueType.HARD.casketId, 1);
			message += " You have now completed a total of " + NumberUtils.formatNumber(++player.hardClueCount) + " hard clue scrolls!";
			player.dialogue(new ItemDialogue().one(def.clueType.casketId, message));
			player.sendMessage(message);
			return true;
		} else if (player.getInventory().hasId(ClueType.MEDIUM.clueId)) {
			player.getInventory().remove(ClueType.MEDIUM.clueId, 1);
			player.getInventory().add(ClueType.MEDIUM.casketId, 1);
			message += " You have now completed a total of " + NumberUtils.formatNumber(++player.medClueCount) + " medium clue scrolls!";
			player.dialogue(new ItemDialogue().one(def.clueType.casketId, message));
			player.sendMessage(message);
			return true;
		} else if (player.getInventory().hasId(ClueType.EASY.clueId)) {
			player.getInventory().remove(ClueType.EASY.clueId, 1);
			player.getInventory().add(ClueType.EASY.casketId, 1);
			message += " You have now completed a total of " + NumberUtils.formatNumber(++player.easyClueCount) + " easy clue scrolls!";
			player.dialogue(new ItemDialogue().one(def.clueType.casketId, message));
			player.sendMessage(message);
			return true;
		} else if (player.getInventory().hasId(ClueType.BEGINNER.clueId)) {
			player.getInventory().remove(ClueType.BEGINNER.clueId, 1);
			player.getInventory().add(ClueType.BEGINNER.casketId, 1);
			message += " You have now completed a total of " + NumberUtils.formatNumber(++player.beginnerClueCount) + " beginner clue scrolls!";
			player.dialogue(new ItemDialogue().one(def.clueType.casketId, message));
			player.sendMessage(message);
			return true;
		}
		return false;
	}

	private double getPetDonatorBoost(Player player) {
		switch (player.getSecondaryGroup()) {
			case SUPER_DONATOR -> {
				return 0.98;
			}
			case ELITE_DONATOR -> {
				return 0.96;
			}
			case NOBLE_DONATOR -> {
				return 0.94;
			}
			case GOLD_DONATOR -> {
				return 0.93;
			}
			case PLATINUM_DONATOR -> {
				return 0.92;
			}
			case LEGENDARY_DONATOR -> {
				return 0.91;
			}
			case SUPREME_DONATOR -> {
				return 0.90;
			}
		}
		return 1;
	}

	public void loot(Player player) {
		ItemContainer container = new ItemContainer();
		container.init(player, 12, -1, 0, 141, true);
		container.sendAll = true;


		LootTable table = new LootTable().addTable(1, loots);
		int minimumValue = 2;
		int maximumValue = 0;
		int coinMin = 0;
		int coinMax = 0;
		int masterClueChance = 100;
		if (this.casketId == ItemID.REWARD_CASKET_MASTER) {
			minimumValue = 5;
			maximumValue = 7;
			coinMin = 900000;
			coinMax = 1800000;
			masterClueChance = 10;
			int basePetChance = 400;
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
				ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				basePetChance *= c.getPetChanceBoost();
			}
			if (player.petDropBonus.isDelayed())
				basePetChance *= 0.8F;
			if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
				basePetChance *= 0.85F;
			basePetChance *= getPetDonatorBoost(player);
			if (Random.get(basePetChance) == 0)
				Pet.BLOODHOUND.unlock(player, 0);
		} else if (this.casketId == ItemID.REWARD_CASKET_ELITE || this.casketId == ItemID.REWARD_CASKET_HARD) {
			minimumValue = 4;
			maximumValue = 6;
			coinMin = 300000;
			coinMax = 550000;
			masterClueChance = 15;
		} else if (this.casketId == ItemID.REWARD_CASKET_MEDIUM) {
			minimumValue = 3;
			maximumValue = 5;
			coinMin = 80000;
			coinMax = 160000;
			masterClueChance = 25;
		} else if (this.casketId == ItemID.REWARD_CASKET_EASY) {
			minimumValue = 2;
			maximumValue = 4;
			coinMin = 20000;
			coinMax = 40000;
			masterClueChance = 30;
		} else if (this.casketId == ItemID.REWARD_CASKET_BEGINNER) {
			minimumValue = 1;
			maximumValue = 3;
			coinMin = 10000;
			coinMax = 20000;
		}
		for (int i = 0; i < Random.get(minimumValue, maximumValue); i++) {
			Item item = table.rollItem();

			if (item.lootBroadcast != null)
				item.lootBroadcast.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " just received " + item.getDef().descriptiveName + " from " + descriptiveName + " clue scroll!");
			container.add(item);
		}
		if (Random.get(masterClueChance) == 0)
			container.add(new Item(19835, 1));
		if (container.getFreeSlots() > 0)
			container.add(COINS_995, Random.get(coinMin, coinMax));
		for (Item item : container.getItems()) {
			if (item != null) {
				if (item.getId() == 19730) {
					Pet.BLOODHOUND.unlock(player, 0);
					continue;
				}
				if (player.getGameMode().isUltimateIronman()) {
					player.getInventory().addOrDrop(item.getId(), item.getAmount());
				} else {
					player.getInventory().addOrSendToBank(item.getId(), item.getAmount());
				}

				player.addToCollectionLog(item);

				if (player.uniqueDrops.get(item.getId()) == null)
					player.uniqueDrops.put(item.getId(), item.getAmount());
				else
					player.uniqueDrops.replace(item.getId(), player.uniqueDrops.get(item.getId()) + item.getAmount());
			}

			player.openInterface(ToplevelComponent.MAINMODAL, 73);
			container.sendUpdates();
		}
	}

	public ClueSave getSave(Player player) {
		if (this == MASTER)
			return player.masterClue;
		if (this == ELITE)
			return player.eliteClue;
		if (this == HARD)
			return player.hardClue;
		if (this == MEDIUM)
			return player.medClue;
		if (this == EASY)
			return player.easyClue;
		return player.beginnerClue;
	}

	public static void register() {
		ItemAction.registerInventory(19835, "read", (player, item) -> {
			player.getInventory().remove(ClueType.MASTER.clueId, 1);
			player.getInventory().addOrDrop(ClueType.MASTER.casketId, 1);
			String message = "Great job, you have completed your clue scroll!";
			message += " You have now completed a total of " + NumberUtils.formatNumber(++player.masterClueCount) + " master clue scrolls!";
			player.dialogue(new ItemDialogue().one(ClueType.MASTER.casketId, message));
			player.sendMessage(message);
		});
		ItemAction.registerInventory(2722, "read", (player, item) -> {
			player.getInventory().remove(ClueType.HARD.clueId, 1);
			player.getInventory().addOrDrop(ClueType.HARD.casketId, 1);
			String message = "Great job, you have completed your clue scroll!";
			message += " You have now completed a total of " + NumberUtils.formatNumber(++player.hardClueCount) + " hard clue scrolls!";
			player.dialogue(new ItemDialogue().one(ClueType.HARD.casketId, message));
			player.sendMessage(message);
		});
		ItemAction.registerInventory(2677, "read", (player, item) -> {
			player.getInventory().remove(ClueType.EASY.clueId, 1);
			player.getInventory().addOrDrop(ClueType.EASY.casketId, 1);
			String message = "Great job, you have completed your clue scroll!";
			message += " You have now completed a total of " + NumberUtils.formatNumber(++player.easyClueCount) + " easy clue scrolls!";
			player.dialogue(new ItemDialogue().one(ClueType.EASY.casketId, message));
			player.sendMessage(message);
		});
		ItemAction.registerInventory(2801, "read", (player, item) -> {
			player.getInventory().remove(ClueType.MEDIUM.clueId, 1);
			player.getInventory().addOrDrop(ClueType.MEDIUM.casketId, 1);
			String message = "Great job, you have completed your clue scroll!";
			message += " You have now completed a total of " + NumberUtils.formatNumber(++player.medClueCount) + " medium clue scrolls!";
			player.dialogue(new ItemDialogue().one(ClueType.MEDIUM.casketId, message));
			player.sendMessage(message);
		});
		ItemAction.registerInventory(12073, "read", (player, item) -> {
			player.getInventory().remove(ClueType.ELITE.clueId, 1);
			player.getInventory().addOrDrop(ClueType.ELITE.casketId, 1);
			String message = "Great job, you have completed your clue scroll!";
			message += " You have now completed a total of " + NumberUtils.formatNumber(++player.eliteClueCount) + " elite clue scrolls!";
			player.dialogue(new ItemDialogue().one(ClueType.ELITE.casketId, message));
			player.sendMessage(message);
		});
		ItemAction.registerInventory(23182, "read", (player, item) -> {
			player.getInventory().remove(ClueType.BEGINNER.clueId, 1);
			player.getInventory().addOrDrop(ClueType.BEGINNER.casketId, 1);
			String message = "Great job, you have completed your clue scroll!";
			message += " You have now completed a total of " + NumberUtils.formatNumber(++player.beginnerClueCount) + " beginner clue scrolls!";
			player.dialogue(new ItemDialogue().one(ClueType.BEGINNER.casketId, message));
			player.sendMessage(message);
		});
		for (ClueType type : values()) {
			/*
			 * Clue scroll
			 */
			if (type.clueId == -1)
				continue;
			ObjType clueDef = ObjType.get(type.clueId);
			clueDef.clueType = type;
			ItemAction.registerInventory(clueDef.id, "check steps", (player, item) -> {
				ClueSave save = type.getSave(player);
				if (save == null)
					player.sendMessage("You haven't started this clue yet.");
				else if (save.remaining == 1)
					player.sendMessage("There is 1 step remaining in this clue.");
				else
					player.sendMessage("There are " + save.remaining + " steps remaining in this clue.");
			});
			/*
			 * Casket
			 */
			ObjType casketDef = ObjType.get(type.casketId);
			ItemAction.registerInventory(22081, "feel", (player, item) -> {
				if (!player.locatorOrbAvailable) return;
				player.locatorOrbAvailable = false;
				player.startEvent(e -> {
					e.delay(1);
					if (player.getHp() > 10)
						player.hit(new Hit().fixedDamage(10));
					else player.hit(new Hit().fixedDamage(player.getHp() - 1));
					player.locatorOrbAvailable = true;
				});
			});
			ItemAction.registerInventory(casketDef.id, "open", (player, item) -> {
				if (DonationBossHandler.map != null) {
					if (player.getPosition().getRegion().id == DonationBossHandler.map.swRegion.id) {
						player.sendMessage("You can't open this here.");
						return;
					}
				}
				int saveCasketChance = 0;
				if (player.totalDonated >= 2500)
					saveCasketChance = 5;
				else if (player.totalDonated >= 500)
					saveCasketChance = 8;
				else if (player.totalDonated >= 100)
					saveCasketChance = 10;
				else if (player.totalDonated >= 10)
					saveCasketChance = 20;
				if (saveCasketChance > 0) {
					if (Random.get(saveCasketChance) != 0)
						item.remove(1);
					else {
						player.sendMessage("Your donator rank allows you to save this casket.");
					}
				} else {
					item.remove(1);
				}
				type.loot(player);
			});
		}
	}

}

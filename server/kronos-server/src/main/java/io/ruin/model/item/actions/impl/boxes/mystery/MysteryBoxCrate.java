package io.ruin.model.item.actions.impl.boxes.mystery;

import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.YesNoDialogue;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.utility.Broadcast;

public class MysteryBoxCrate {

	private static final LootTable MYSTERY_BOX_TABLE = new LootTable().addTable(1,
		new LootItem(4708, 1, 50), //Ahrim's hood
		new LootItem(4712, 1, 50), //Ahrim's robetop
		new LootItem(4714, 1, 50), //Ahrim's robeskirt
		new LootItem(4716, 1, 40), //Dharok's helm
		new LootItem(4720, 1, 40), //Dharok's platebody
		new LootItem(4722, 1, 40), //Dharok's platelegs
		new LootItem(4718, 1, 40), //Dharok's greataxe
		new LootItem(4736, 1, 50), //Karil's leathertop
		new LootItem(13442, 100, 250, 65), //100 Anglerfish
		new LootItem(11235, 1, 45), //Dark bow
		new LootItem(8927, 1, 25), //Bandana eyepatch
		new LootItem(2643, 1, 35), //Dark cavalier
		new LootItem(12301, 1, 25), //Blue headband
		new LootItem(2577, 1, 15), //Ranger boots
		new LootItem(12639, 1, 25), //Guthix halo
		new LootItem(12430, 1, 25), //Afro
		new LootItem(12245, 1, 25), //Beanie
		new LootItem(12638, 1, 25), //Zamorak halo
		new LootItem(12637, 1, 25), //Saraodmin halo
		new LootItem(11899, 1, 25), //Decorative platebody (Melee)
		new LootItem(11900, 1, 25), //Decorative platelegs (Melee)
		new LootItem(11898, 1, 25), //Decorative hat (Magic)
		new LootItem(11896, 1, 25), //Decorative robe top (Magic)
		new LootItem(11897, 1, 25), //Decorative bottoms (Magic)
		new LootItem(12375, 1, 25), //Black cane
		new LootItem(12377, 1, 25), //Adamant cane
		new LootItem(12365, 1, 25), //Iron dragon mask
		new LootItem(12367, 1, 25), //Steel dragon mask
		new LootItem(12369, 1, 25), //Mithril dragon mask
		new LootItem(12518, 1, 25), //Green dragon mask
		new LootItem(12522, 1, 25), //Red dragon mask
		new LootItem(12524, 1, 25), //Black dragon mask
		new LootItem(12763, 1, 25), //White dark bow paint
		new LootItem(12761, 1, 25), //Yellow dark bow paint
		new LootItem(12757, 1, 25), //Blue dark bow paint
		new LootItem(12769, 1, 25), //Frozen whip mix
		new LootItem(12771, 1, 25), //Volcanic whip mix
		new LootItem(12829, 1, 25), //Spirit shield
		new LootItem(6922, 1, 25), //Infinity gloves
		new LootItem(6918, 1, 25), //Infinity hat
		new LootItem(6916, 1, 25), //Infinity top
		new LootItem(6924, 1, 25), //Infinity bottoms
		new LootItem(6528, 1, 25), //Tzhaar-ket-om
		new LootItem(6525, 1, 25), //Toktz-xil-ek
		new LootItem(4151, 1, 25), //Abyssal whip
		new LootItem(4153, 1, 25), //Granite maul
		new LootItem(6920, 1, 25), //Infinity boots
		new LootItem(11128, 1, 25),   //Berseker necklace
		new LootItem(12696, 200, 250, 25), //250 Super combat potions
		new LootItem(13442, 200, 250, 25), //250 Anglerfish
		new LootItem(12696, 400, 500, 25), //500 Super combat potions
		new LootItem(13442, 450, 650, 25), //500 Anglerfish
		new LootItem(12006, 1, 25), //Abyssal tentacle
		new LootItem(11919, 1, 15), //Cow mask
		new LootItem(12956, 1, 15), //Cow top
		new LootItem(12957, 1, 15), //Cow bottoms
		new LootItem(12958, 1, 15), //Cow gloves
		new LootItem(12959, 1, 15), //Cow boots
		new LootItem(12002, 1, 15), //Occult necklace

		new LootItem(3140, 1, 15), //dragon chain
		new LootItem(4207, 1, 15), //crystal weapon seed
		new LootItem(22103, 1, 15), //dragon metal lump
		new LootItem(11798, 1, 15), //godsword blade


		new LootItem(6585, 1, 15), //Amulet of fury
		new LootItem(11284, 1, 15), //Dragonfire shield
		new LootItem(4224, 1, 15), //New crystal shield
		new LootItem(12831, 1, 15), //Blessed spirit shield
		new LootItem(11926, 1, 15), //Odium ward
		new LootItem(11924, 1, 15), //Malediction ward
		new LootItem(12379, 1, 15), //Rune cane
		new LootItem(12373, 1, 15), //Dragon cane
		new LootItem(12363, 1, 15), //Bronze dragon mask
		new LootItem(12371, 1, 15), //Lava dragon mask
		new LootItem(6737, 1, 5), //Berserker ring
		new LootItem(6735, 1, 5), //Warriors ring
		new LootItem(6733, 1, 5), //Archers ring
		new LootItem(6731, 1, 5), //Seers ring
		new LootItem(11773, 1, 5), //Berserker ring (i)
		new LootItem(11772, 1, 5), //Warriors ring (i)
		new LootItem(11771, 1, 5), //Archers ring (i)
		new LootItem(11770, 1, 5), //Seers ring (i))
		new LootItem(20517, 1, 5), //Elder chaos top
		new LootItem(20520, 1, 5), //Elder chaos bottom
		new LootItem(20595, 1, 5), //Elder chaos hood
		new LootItem(12696, 800, 1000, 1), //1000 Super combat potions
		new LootItem(13442, 800, 1000, 1), //1000 Anglerfish
		new LootItem(995, 1000000, 5000000, 1), //1-5m coins
		new LootItem(12696, 1300, 1500, 1), //1500 Super combat potions
		new LootItem(13442, 1200, 1500, 1), //1500 Anglerfish
		new LootItem(2581, 1, 1), //Robin hood hat
		new LootItem(22981, 1, 1), //Ferocious gloves
		new LootItem(22975, 1, 1), //Brimstone ring
		new LootItem(12596, 1, 1), //Rangers' tunic
		new LootItem(12696, 4000, 5000, 1), //5000 Super combat potions
		new LootItem(13442, 4000, 5000, 1), //5000 Anglerfish
		new LootItem(1419, 1, 1), // Scythe
		new LootItem(19988, 1, 1), // Blacksmith's helm
		new LootItem(19991, 1, 1), // Bucket helm
		new LootItem(20059, 1, 1), // Bucket helm (g)
		new LootItem(20050, 1, 1), // Obsidian cape (f)
		new LootItem(20008, 1, 1), // Fancy tiara
		new LootItem(20110, 1, 1), // Bowl wig
		new LootItem(20020, 1, 1), // Lesser demon mask
		new LootItem(20023, 1, 1), // Greater demon mask
		new LootItem(20026, 1, 1), // Black demon mask
		new LootItem(20029, 1, 1), // Old demon mask
		new LootItem(20032, 1, 1) // Jungle demon mask
	);

	private static final LootTable INCENTIVE_TABLE = new LootTable().addTable(1,
		new LootItem(6585, 1, 100), //Amulet of fury
		new LootItem(11284, 1, 100), //Dragonfire shield
		new LootItem(6737, 1, 50), //Berserker ring
		new LootItem(6731, 1, 50), //Seers ring
		new LootItem(2581, 1, 1), //Robin hood hat
		new LootItem(12596, 1, 1), //Rangers' tunic
		new LootItem(1038, 1, 1).broadcast(Broadcast.GLOBAL), //Red party hat
		new LootItem(1040, 1, 1).broadcast(Broadcast.GLOBAL), //Yellow party hat
		new LootItem(1042, 1, 1).broadcast(Broadcast.GLOBAL), //Blue party hat
		new LootItem(1044, 1, 1).broadcast(Broadcast.GLOBAL), //Green party hat
		new LootItem(1046, 1, 1).broadcast(Broadcast.GLOBAL), //Purple party hat
		new LootItem(1048, 1, 1).broadcast(Broadcast.GLOBAL), //White  party hat
		new LootItem(12437, 1, 1).broadcast(Broadcast.GLOBAL), // 3rd age cloak
		new LootItem(1053, 1, 1).broadcast(Broadcast.GLOBAL), //Green halloween mask
		new LootItem(1055, 1, 1).broadcast(Broadcast.GLOBAL), //Blue halloween mask
		new LootItem(1057, 1, 1).broadcast(Broadcast.GLOBAL), //Red halloween mask
		new LootItem(12696, 4000, 5000, 1), //5000 Super combat potions
		new LootItem(13442, 4000, 5000, 1) //5000 Anglerfish
	);


	private static void gift(Player player, Item box) {
		int boxId = box.getId();
		player.stringInput("Enter player's display name:", name -> {
			if (!player.getInventory().hasId(boxId))
				return;
			name = name.replaceAll("[^a-zA-Z0-9\\s]", "");
			name = name.substring(0, Math.min(name.length(), 12));
			if (name.isEmpty()) {
				player.retryStringInput("Invalid username, try again:");
				return;
			}
			if (name.equalsIgnoreCase(player.getName())) {
				player.retryStringInput("Cannot gift yourself, try again:");
				return;
			}
			Player target = World.getPlayer(name);
			if (target == null) {
				player.retryStringInput("Player cannot be found, try again:");
				return;
			}
			if (target.getGameMode().isIronMan()) {
				player.retryStringInput("That player is an ironman and can't receive gives!");
				return;
			}
			player.stringInput("Enter a message for " + target.getName() + ":", message -> {
				player.dialogue(new YesNoDialogue("Are you sure you want to do this?", "Gift your " + box.getDef().name + " to " + target.getName() + "?", box, () -> {
					if (!player.getInventory().hasId(boxId))
						return;
					player.getInventory().remove(boxId, 1);
					if (!target.getInventory().isFull())
						target.getInventory().add(boxId, 1);
					else
						target.getBank().add(boxId, 1);
					target.sendMessage("<img=91> " + Color.DARK_RED.wrap(player.getName() + " has just gifted you " + box.getDef().descriptiveName + "!"));
					player.sendMessage("<img=91> " + Color.DARK_RED.wrap("You have successfully gifted your " + box.getDef().name + " to " + target.getName() + "."));
					if (!message.isEmpty())
						target.sendMessage("<img=91> " + Color.DARK_RED.wrap("[NOTE] " + message));
				}));
			});
		});
	}

	public static void register() {
		int slotsRequired = 3;
		ItemAction.registerInventory(30456, "open", (player, item) -> {
			if (player.getInventory().getFreeSlots() < slotsRequired) {
				player.sendMessage("You need atleast 3 free inventory slots free to open the crate.");
				return;
			}
			player.lock();
			player.closeDialogue();
			Item reward;
			Item reward2;
			Item reward3;
			if (player.firstMysteryBoxReward) {
				reward = INCENTIVE_TABLE.rollItem();
				reward2 = INCENTIVE_TABLE.rollItem();
				reward3 = INCENTIVE_TABLE.rollItem();
				player.firstMysteryBoxReward = false;
			} else if (player.guaranteedMysteryBoxLoot >= 5) {
				reward = INCENTIVE_TABLE.rollItem();
				reward2 = INCENTIVE_TABLE.rollItem();
				reward3 = INCENTIVE_TABLE.rollItem();
				player.guaranteedMysteryBoxLoot = 1;
			} else {
				reward = MYSTERY_BOX_TABLE.rollItem();
				reward2 = MYSTERY_BOX_TABLE.rollItem();
				reward3 = MYSTERY_BOX_TABLE.rollItem();
				player.guaranteedMysteryBoxLoot++;
			}
			item.remove();
			player.getInventory().add(reward);
			player.getInventory().add(reward2);
			player.getInventory().add(reward3);
			if (reward.lootBroadcast != null)
				Broadcast.GLOBAL.sendNews(Icon.MYSTERY_BOX, "Mystery Crate", "" + player.getName() + " just received " + reward.getDef().descriptiveName + "!");
			player.unlock();
		});

		/*
		 * Mystery box gifting
		 */
		ItemAction.registerInventory(6828, "gift", MysteryBoxCrate::gift);
		ItemAction.registerInventory(6829, "gift", MysteryBoxCrate::gift);
		ItemAction.registerInventory(290, "gift", MysteryBoxCrate::gift);
		ItemAction.registerInventory(6831, "gift", MysteryBoxCrate::gift);
		ItemAction.registerInventory(22330, "gift", MysteryBoxCrate::gift);
	}
}

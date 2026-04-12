package io.ruin.model.activities.moonsofperil;

import discord.webhooks.notifications.RareDropHook;
import io.ruin.api.utils.Random;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemItemAction;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Utils;
import org.json.JSONObject;

import java.util.*;

public class MoonsOfPerilHandler {

	boolean bloodMoonDead = false;
	boolean eclipseMoonDead = false;
	boolean blueMoonDead = false;
	DynamicMap bloodMoonRoom;
	DynamicMap eclipseMoonRoom;
	DynamicMap blueMoonRoom;
	NPC blueMoon;
	NPC eclipseMoon;
	NPC bloodMoon;

	static Bounds earthBoundCavernBounds = new Bounds(1345, 9663, 1406, 9725, 0);
	static Bounds streamBoundCavernBounds = new Bounds(1473, 9663, 1535, 9725, 0);
	static Bounds ancientPrisonBounds = new Bounds(1344, 9535, 1406, 9595, 0);
	static Bounds connectorBounds = new Bounds(1408, 9599, 1470, 9660, 1);
	static Bounds connectorBounds2 = new Bounds(1408, 9599, 1470, 9660, 0);
	static Bounds ancientShrineBound = new Bounds(1492, 9557, 1531, 9595, 0);

	public boolean initialized = false;

	List<Integer> regionIds = new ArrayList<>();

	static List<Integer> moonsOfPerilItems = Arrays.asList(
			29076, 29077, 29078, 29079, 29080, 29216, 29217, 28893, 29081, 29082, 29083);

	public void init() {
		try {
			bloodMoonRoom = new DynamicMap().build(5526, 1);
			eclipseMoonRoom = new DynamicMap().build(6038, 1);
			blueMoonRoom = new DynamicMap().build(5783, 1);
		} catch (DynamicMap.DynamicMapBuildException e) {
			return;
		}
		initialized = true;
		blueMoonDead = false;
		eclipseMoonDead = false;
		bloodMoonDead = false;
		addRegionIds();
	}

	private void addRegionIds() {
		regionIds.clear();
		regionIds.add(bloodMoonRoom.swRegion.id);
		regionIds.add(eclipseMoonRoom.swRegion.id);
		regionIds.add(blueMoonRoom.swRegion.id);
		regionIds.add(5527);
		regionIds.add(6039);
		regionIds.add(5782);
		regionIds.add(5525);
		regionIds.add(6037);
	}

	private void onEclipseMoonExit(Player player, boolean b) {
		if (eclipseMoon != null)
			eclipseMoon.remove();
		if (leftMinigame(player)) {
			destroy();
		}
	}

	private void onBloodMoonExit(Player player, boolean b) {
		if (bloodMoon != null)
			bloodMoon.remove();
		if (leftMinigame(player)) {
			destroy();
		}
	}

	void catchMossLizard(Player player) {
		if (!player.getInventory().hasRoomFor(29076)) {
			player.sendMessage("You need more inventory space to catch a moss lizard.");
			return;
		}
		if (player.getInventory().hasId(954)) {
			player.startEvent(e -> {
				player.animate(832);
				player.sendFilteredMessage("You lay the trap...");
				e.delay(1);
				player.getInventory().add(29076, 1);
				player.getStats().addXp(StatType.Hunter, 108, true);
				player.sendFilteredMessage("You catch a moss lizard.");
			});
		} else {
			player.sendMessage("You need a rope to use this trap.");
		}
	}

	void cookMossLizard(Player player) {
		if (player.getInventory().hasId(29076)) {
			player.startEvent(e -> {
				while (player.getInventory().hasId(29076)) {
					player.animate(897);
					e.delay(1);
					player.getInventory().remove(29076, 1);
					player.getInventory().addOrDrop(29077, Random.get(1, 3));
					player.getStats().addXp(StatType.Cooking, 100, true);
					player.sendFilteredMessage("You cook a cooked moss lizard.");
				}
			});
		}
	}

	private void catchMoonlightMoth(Player player) {
		if (player.getInventory().hasId(ItemID.BUTTERFLY_NET) && player.getInventory().hasId(ItemID.BUTTERFLY_JAR)) {
			if (player.getStats().get(StatType.Hunter).currentLevel < 75) {
				player.sendMessage("You need a Hunter level of 75 to catch a moonlight moth.");
				return;
			}
			player.startEvent(e -> {
				player.animate(6605);
				player.sendFilteredMessage("You catch a moonlight moth.");
				e.delay(1);
				player.getInventory().remove(ItemID.BUTTERFLY_JAR, 1);
				player.getInventory().add(28893, 1);
				player.getStats().addXp(StatType.Hunter, 108, true);
			});
		} else {
			if (player.getStats().get(StatType.Hunter).currentLevel < 85) {
				player.sendMessage("You need a Hunter level of 85 to catch a moonlight moth barehanded.");
				return;
			}
			player.startEvent(e -> {
				player.animate(7171);
				player.sendFilteredMessage("You catch a moonlight moth.");
				e.delay(1);
				Stat prayer = player.getStats().get(StatType.Prayer);
				prayer.alter(Math.min(prayer.fixedLevel, prayer.currentLevel + 22));
				player.getStats().addXp(StatType.Hunter, 108, true);
			});
		}
	}

	void pickMoonlightGrub(Player player) {
		player.startEvent(e -> {
			player.animate(2280);
			player.sendFilteredMessage("You pick a moonlight grub.");
			e.delay(2);
			player.getInventory().add(29078, 1);
			player.getStats().addXp(StatType.Hunter, 5, true);
		});
	}

	private void crushMoonlightGrub(Player player) {
		if (player.getInventory().hasId(ItemID.PESTLE_AND_MORTAR)) {
			player.startEvent(e -> {
				while (player.getInventory().hasId(29078)) {
					player.animate(364);
					player.sendFilteredMessage("You crush the moonlight grub...");
					e.delay(1);
					player.getInventory().remove(29078, 1);
					player.getInventory().add(29079, 1);
					player.getStats().addXp(StatType.Herblore, 5, true);
					player.sendFilteredMessage("You crush a moonlight grub.");
				}
			});
		}
	}

	private void mixMoonlightPotion(Player player) {
		if (player.getInventory().hasId(29079) && player.getInventory().hasId(ItemID.VIAL_OF_WATER)) {
			if (player.getStats().get(StatType.Herblore).currentLevel < 38) {
				player.sendMessage("You need a Herblore level of 38 to mix a moonlight potion.");
				return;
			}
			player.startEvent(e -> {
				while (player.getInventory().hasId(29079) && player.getInventory().hasId(ItemID.VIAL_OF_WATER)) {
					player.animate(363);
					player.sendFilteredMessage("You mix the moonlight potion...");
					e.delay(1);
					player.getInventory().remove(29079, 1);
					player.getInventory().remove(ItemID.VIAL_OF_WATER, 1);
					player.getInventory().add(29080, 1);
					player.getStats().addXp(StatType.Herblore, 5, true);
					player.sendFilteredMessage("You mix a moonlight potion.");
				}
			});
		}
	}

	void fishBream(Player player) {
		if (player.getInventory().hasId(ItemID.BIG_FISHING_NET)) {
			if (player.getStats().get(StatType.Fishing).currentLevel < 20) {
				player.sendMessage("You need a Fishing level of 20 to fish here.");
				return;
			}
			player.startEvent(e -> {
				while (player.getInventory().hasRoomFor(29216)) {
					player.animate(621);
					e.delay(2);
					player.getInventory().add(29216, 1);
					player.getStats().addXp(StatType.Fishing, 50, true);
				}
			});
		} else {
			player.sendMessage("You need a big fishing net to fish here.");
		}
	}

	void cookBream(Player player) {
		if (player.getInventory().hasId(29216)) {
			if (player.getStats().get(StatType.Cooking).currentLevel < 30) {
				player.sendMessage("You need a Cooking level of 30 to cook bream.");
				return;
			}
			player.startEvent(e -> {
				while (player.getInventory().hasId(29216)) {
					player.animate(896);
					e.delay(1);
					player.getInventory().remove(29216, 1);
					player.getInventory().addOrDrop(29217, 1);
					player.getStats().addXp(StatType.Cooking, 50, true);
					player.sendFilteredMessage("You cook a cooked bream.");
				}
			});
		}
	}

	private void releaseMoonlightMoth(Player player) {
		if (player.getInventory().hasId(28893)) {
			player.startEvent(e -> {
				player.animate(7172);
				player.sendFilteredMessage("You release the moonlight moth.");
				e.delay(1);
				player.getInventory().remove(28893, 1);
				Stat prayer = player.getStats().get(StatType.Prayer);
				prayer.alter(Math.min(prayer.fixedLevel, prayer.currentLevel + 22));
			});
		}
	}

	private void onBlueMoonExit(Player player, boolean b) {
		if (blueMoon != null)
			blueMoon.remove();
		if (leftMinigame(player)) {
			destroy();
		}
	}

	void lootShrine(Player player) {
		if (bloodMoonDead && eclipseMoonDead && blueMoonDead) {
			player.moonsOfPerilKills.increment(player);
			List<Item> rewards = new ArrayList<>();
			int rolls = Random.get(1, 3);
			for (int i = 0; i < rolls; i++) {
				Item reward = MoonsOfPerilRewards.table.rollItem();
				player.getInventory().addOrDrop(reward.getId(), reward.getAmount());
				player.addToCollectionLog(reward);
				rewards.add(reward);
			}
			player.openInterface(ToplevelComponent.MAINMODAL, 1122);
			int startingItemComponent = 18;
			int startingContainerId = 566;
			for (int i = 0; i < 6; i++) {
				player.getPacketSender().sendClientScript(
						149, "IviiiIsssss",
						1122 << 16 | startingItemComponent + i, startingContainerId + i,
						4, 7, 1, -1, "", "", "", "", "");
				player.getPacketSender().sendItems(
						-1,
						startingItemComponent + i,
						startingContainerId + i,
						new Item(-1));
			}
			for (Item item : rewards) {
				player.getPacketSender().sendClientScript(
						149, "IviiiIsssss",
						1122 << 16 | startingItemComponent, startingContainerId,
						4, 7, 1, -1, "", "", "", "", "");
				player.getPacketSender().sendItems(
						-1,
						startingItemComponent,
						startingContainerId,
						item);
				if (item.lootBroadcast != null) {
					String message = player.getName() + " just received " + item.getDef().name + " ";
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(),
							" just received " + item.getDef().descriptiveName + " from the Moons of Peril!");

					RareDropHook.sendDiscordMessage(() -> {
						var jsonObject = new JSONObject();
						jsonObject.put("player", player.getName());
						jsonObject.put("game_mode", player.getGameMode());
						jsonObject.put("item_id", item.getId());
						jsonObject.put("item_name", item.getDef().name);
						jsonObject.put("source", "Moons of Peril");
						jsonObject.put("total_attempts", Utils.formatMoneyString(player.moonsOfPerilKills.getKills()));
						return jsonObject;
					});
				}
				startingItemComponent++;
				startingContainerId++;
			}
		} else {
			player.sendMessage("You must defeat all three moons to loot the shrine.");
			player.sendMessage("You haven't killed: " + (bloodMoonDead ? "" : "Blood Moon, ")
					+ (eclipseMoonDead ? "" : "Eclipse Moon, ") + (blueMoonDead ? "" : "Blue Moon"));
		}
	}

	private void destroy() {
		bloodMoonRoom.destroy();
		eclipseMoonRoom.destroy();
		blueMoonRoom.destroy();
	}

	public void moveToLobby(Player player) {
		player.getMovement().teleport(1440, 9615, 1);
	}

	public void enterBloodMoon(Player player) {
		if (bloodMoonDead) {
			player.sendMessage("The Blood Moon has already been defeated.");
			return;
		}
		player.getMovement().teleport(bloodMoonRoom.swRegion.baseX + 48, bloodMoonRoom.swRegion.baseY + 32, 0);
		bloodMoon = new NPC(13011).spawn(bloodMoonRoom.convertX(1391), bloodMoonRoom.convertY(9631), 0);
		bloodMoon.deathEndListener = (entity, killer, killHit) -> {
			bloodMoonDead = true;
			bloodMoonRoom.destroy();
			player.getMovement().teleport(1352, 9581, 0);
		};
		bloodMoonRoom.assignListener(player).onExit(this::onBloodMoonExit);
	}

	private boolean leftMinigame(Player player) {
		return !regionIds.contains(player.getPosition().getRegionId());
	}

	public void enterEclipseMoon(Player player) {
		if (eclipseMoonDead) {
			player.sendMessage("The Eclipse Moon has already been defeated.");
			return;
		}
		player.getMovement().teleport(eclipseMoonRoom.swRegion.baseX + 16, eclipseMoonRoom.swRegion.baseY + 32, 0);
		eclipseMoon = new NPC(13012).spawn(eclipseMoonRoom.swRegion.baseX + 20, eclipseMoonRoom.swRegion.baseY + 30, 0);
		eclipseMoon.deathEndListener = (entity, killer, killHit) -> {
			eclipseMoonDead = true;
			eclipseMoonRoom.destroy();
			player.getMovement().teleport(1376, 9710, 0);
		};
		eclipseMoonRoom.assignListener(player).onExit(this::onEclipseMoonExit);
	}

	void leaveEclipseMoon(Player player) {
		player.dialogue(new MessageDialogue("Are you sure you want to leave the boss room?"),
				new OptionsDialogue(
						new Option("Yes.", () -> {
							player.getMovement().teleport(1466, 9632, 0);
						}),
						new Option("No.")));
	}

	void leaveBlueMoon(Player player) {
		player.dialogue(new MessageDialogue("Are you sure you want to leave the boss room?"),
				new OptionsDialogue(
						new Option("Yes.", () -> {
							player.getMovement().teleport(1440, 9658, 0);
						}),
						new Option("No.")));
	}

	void leaveBloodMoon(Player player) {
		player.dialogue(new MessageDialogue("Are you sure you want to leave the boss room?"),
				new OptionsDialogue(
						new Option("Yes.", () -> {
							player.getMovement().teleport(1413, 9632, 0);
						}),
						new Option("No.")));
	}

	public void enterBlueMoon(Player player) {
		if (blueMoonDead) {
			player.sendMessage("The Blue Moon has already been defeated.");
			return;
		}
		player.getMovement().teleport(blueMoonRoom.swRegion.baseX + 32, blueMoonRoom.swRegion.baseY + 16, 0);
		blueMoon = new NPC(13013).spawn(blueMoonRoom.convertX(1440), blueMoonRoom.convertY(9677), 0);
		blueMoon.deathEndListener = (entity, killer, killHit) -> {
			blueMoonDead = true;
			blueMoonRoom.destroy();
			player.getMovement().teleport(1352, 9581, 0);
		};
		blueMoonRoom.assignListener(player).onExit(this::onBlueMoonExit);
	}

	private static void exited(Player player, boolean b) {
		if (player.getMoonsOfPerilHandler().leftMinigame(player)) {
			player.getStats().restore(true);
			player.getMoonsOfPerilHandler().confiscateItems(player);
			player.getMoonsOfPerilHandler().destroy();
			player.sendMessage("You have left the Moons of Peril minigame.");
		}
	}

	private void confiscateItems(Player player) {
		boolean confiscated = false;
		for (int i = 0; i < 28; i++) {
			if (player.getInventory().get(i) != null && moonsOfPerilItems.contains(player.getInventory().get(i).getId())) {
				player.getInventory().get(i).remove();
				confiscated = true;
			}
		}
		if (confiscated) {
			player.sendMessage("The resources from Neypotzli fizzle to dust as they are severed from its magic.");
		}
	}

	public static void register() {
		MapListener.registerBounds(earthBoundCavernBounds)
				.onExit(MoonsOfPerilHandler::exited);
		MapListener.registerBounds(streamBoundCavernBounds)
				.onExit(MoonsOfPerilHandler::exited);
		MapListener.registerBounds(ancientPrisonBounds)
				.onExit(MoonsOfPerilHandler::exited);
		MapListener.registerBounds(connectorBounds)
				.onExit(MoonsOfPerilHandler::exited);
		MapListener.registerBounds(connectorBounds2)
				.onExit(MoonsOfPerilHandler::exited);
		MapListener.registerBounds(ancientShrineBound)
				.onExit(MoonsOfPerilHandler::exited);
		ItemAction.registerInventory(28893, "Release", (player, item) -> {
			player.getMoonsOfPerilHandler().releaseMoonlightMoth(player);
		});
		ItemItemAction.register(ItemID.PESTLE_AND_MORTAR, 29078, (player, primary, secondary) -> {
			player.getMoonsOfPerilHandler().crushMoonlightGrub(player);
		});
		ItemItemAction.register(ItemID.VIAL_OF_WATER, 29079, (player, primary, secondary) -> {
			player.getMoonsOfPerilHandler().mixMoonlightPotion(player);
		});
		NPCAction.register(12772, "catch", (player, npc) -> player.getMoonsOfPerilHandler().catchMoonlightMoth(player));
	}
}

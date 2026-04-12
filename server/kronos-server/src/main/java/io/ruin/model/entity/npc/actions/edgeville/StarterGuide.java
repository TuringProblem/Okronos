package io.ruin.model.entity.npc.actions.edgeville;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import discord.webhooks.logs.PlayerCreation;
import io.ruin.HooksV2;
import io.ruin.Server;
import io.ruin.api.utils.ServerWrapper;
import io.ruin.cache.ItemID;
import io.ruin.data.impl.Help;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Difficulty;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.SecurityPin;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.handlers.XpCounter;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.Broadcast;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.ruin.cache.ItemID.COINS_995;

@Slf4j
public class StarterGuide {
	public static interface Hook {
		record Finished(Player player, GameMode mode) implements Hook {};
	}

	public static HooksV2<Hook> hooks = new HooksV2<>(Hook.class);
	public static List<String> ipsClaimed = new ArrayList<>();

	public static void register() {
		LoginListener.register(player -> {
			if (player.newPlayer) {
				player.hasFreePerkUnlock = true;
				XpCounter.select(player, 1);
				tutorial(player);
			}
		});
	}

	private static void optionsDialogue(Player player, NPC npc) {
		player.dialogue(new NPCDialogue(npc, "Hello " + player.getName() + ", is there something I could assist you with?"),
				new OptionsDialogue(
						new Option("View help pages", () -> Help.open(player)),
						// new Option("Replay tutorial", () -> ecoTutorial(player)),
						new Option("Change home point", () -> {
							npc.startEvent(event -> {
								if (!player.edgeHome) {
									player.dialogue(new NPCDialogue(npc, "I can move your spawn point and <br>" +
											"home teleport location to Edgeville.<br>" +
											"It will cost 5,000,000 GP.<br>" +
											"Would you like to do this?"),
											new OptionsDialogue(
													new Option("No thanks, I like this home.", player::closeDialogue),
													new Option("Certainly! I would like to respawn in Edgeville.", () -> {
														if (player.getInventory().hasItem(995, 5000000)) {
															player.getInventory().remove(995, 5000000);
															player.edgeHome = true;
															player.dialogue(new NPCDialogue(npc, "Your spawn point has been changed<br>" +
																	"to Edgeville! If you'd like to change<br>" +
																	"it back, just speak with me again."));
														} else {
															player.dialogue(new NPCDialogue(npc, "I'm sorry, but it doesn't look like<br>" +
																	"you can afford this."));
														}
													})));
								} else {
									player.dialogue(
											new NPCDialogue(npc, "Are you wanting to move your<br>" +
													"spawn point back to home? It will cost<br>" +
													"another 5,000,000 GP."),
											new OptionsDialogue(
													new Option("No thanks.", player::closeDialogue),
													new Option("Yes please!", () -> {
														if (player.getInventory().hasItem(995, 5000000)) {
															player.getInventory().remove(995, 5000000);
															player.edgeHome = false;
															player.dialogue(new NPCDialogue(npc, "Your spawn point has been changed<br>" +
																	"back to home. If you'd like it changed,<br>" +
																	"just speak with me again!"));
														} else {
															player.dialogue(new NPCDialogue(npc, "I'm sorry, but it doesn't look like<br>" +
																	"you can afford this."));
														}
													})));
								}
							});
						})));
	}

	@SneakyThrows
	public static void ecoTutorial(Player player) {
		boolean actuallyNew = player.newPlayer;
		player.inTutorial = true;
		World.startEvent(event -> {

			player.lock(LockType.FULL_ALLOW_LOGOUT);
			player.getMovement().teleport(3085, 3492, 0);
			if (actuallyNew) {
				player.openInterface(ToplevelComponent.MAINMODAL, Interface.MAKE_OVER_MAGE);
				player.getPacketSender().sendIfEvents(679, 78, 0, 4, new int[] { 1 << 1 });
				while (player.isVisibleInterface(Interface.MAKE_OVER_MAGE)) {
					event.delay(1);
				}
			}

			// EDGE_CASE: player might've logged out
			if (!player.isOnline()) {
				return;
			}

			if (actuallyNew) {
				player.getGameModeInterface().openIronmanSettingsInterface(player);
				while (player.isVisibleInterface(1100)) {
					event.delay(1);
				}
			}

		});
	}

	@SneakyThrows
	public static void continueTutorial(Player player) {
		AtomicBoolean startTutorial = new AtomicBoolean(false);
		player.startEvent(event -> {
			String text = "You've chosen to play an account with no restrictions, good luck!";
			if (player.getGameMode() == GameMode.IRONMAN) {
				text = "You've chosen an account with ironman restrictions; I hope RNG is on your side!";
			} else if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
				text = "You've chosen an account with hardcore ironman restrictions; stay alive at all costs!";
			} else if (player.getGameMode() == GameMode.ULTIMATE_IRONMAN) {
				text = "You've chosen an account with ultimate ironman restrictions; I hope you have a plan!";
			} else if (player.getGameMode() == GameMode.GROUP_IRONMAN) {
				text = "You've chosen group ironman restrictions; I hope your friends are up for the task!";
			} else if (player.getGameMode() == GameMode.HARDCORE_GROUP_IRONMAN) {
				text = "You've chosen hardcore group ironman restrictions; hopefully you picked the right people!";
			}

			if (player.getGameMode().isIronMan()) {
				player.dialogue(new NPCDialogue(3525, text),
						new NPCDialogue(3525, "Before you start, I'll give you items to start your ironman adventure."),
						new NPCDialogue(3525, "And you're all set, good luck on your journey!") {
							@Override
							public void open(Player player) {
								giveEcoStarter(player);
								player.newPlayer = false;
								super.open(player);
							}
						});
			} else {
				player.dialogue(new NPCDialogue(3525, text),
						new NPCDialogue(3525, "Before you start, I'll give you some items to start your adventure."),
						new NPCDialogue(3525, "If you need any other items, be sure to check out the shops!") {
							@Override
							public void open(Player player) {
								giveEcoStarter(player);
								player.newPlayer = false;
								super.open(player);
							}
						});
			}

			event.waitForDialogue(player);
			Broadcast.WORLD.sendNews(player.getName() + " has just joined " + World.type.getWorldName()
					+ ". For an early boost, use code ::winter!");
			startTutorial.set(true);
//			PlayerCreationWebhook.sendAccountCreationHook(player);

			var dto = new JSONObject()
				.put("player", player.getName())
				.put("hwid", player.hwid);

			PlayerCreation.createAnsSendWebhookMessageWithEmbed(dto);

			if (startTutorial.get()) {
				player.dialogue(new NPCDialogue(3531,
						"Ah, I've been expecting you, " + player.getName() + "! Welcome to " + World.type.getWorldName()
								+ " I can give you a quick tutorial, or you can just start playing if you'd prefer!"),
						new OptionsDialogue("Show the tutorial?",
								new Option("Show me the ropes.", () -> introCutscene(player)),
								new Option("I've got this!", () -> {
									player.closeDialogue();
									player.logoutListener = null;
									VarPlayerRepository.ESCAPE_CLOSES.toggle(player);
									player.tutorialStage = 0;
									SecurityPin.CreateSecurityPin(player);
									if (hooks.handle(new Hook.Finished(player, player.getGameMode()))) {
										return;
									}
								})));
			}
		});
	}

	private static void introCutscene(Player player) {

		player.startEvent((e) -> {
			player.getPacketSender().sendClientScript(39, "i", 100);
			VarPlayerRepository.LOCK_CAMERA.set(player, 1);
			player.dialogue(new NPCDialogue(3531,
					"Welcome to Reason, where you'll embark on an unforgettable journey! Expect nothing less than an unparalleled experience. Best of luck on your adventure!"));
			e.waitForDialogue(player);

			player.getMovement().teleport(3092, 3491, 0);
			e.delay(1);

			player.dialogue(new NPCDialogue(3531,
					"Starting off with Reason's main bank! Here, you can take care of all your banking needs."));
			e.waitForDialogue(player);
			player.getMovement().teleport(3087, 3488, 0);
			e.delay(2);
			player.dialogue(new NPCDialogue(3531,
					"Exhibit two, the Trading Post & Upgrade Station! This bustling marketplace is where players can buy and sell all kinds of items.<br>"
							+
							"Think of it as Reason's version of the Grand Exchange."));
			e.waitForDialogue(player);
			player.dialogue(new NPCDialogue(3531,
					"Right next to the Upgrade Station is the Item Workbench, where you will be able to place perks onto your equipment with minerals and enhancers from breaking down items."));
			e.waitForDialogue(player);
			e.delay(2);
			// construction portal
			player.getMovement().teleport(3087, 3507, 0);
			player.dialogue(new NPCDialogue(3531,
					"Now to make you feel at home... the POH portal! This is where you can begin building your dream estate. Visit the estate agent to purchase a house..."));
			e.waitForDialogue(player);
			e.delay(2);

			player.getMovement().teleport(3079, 3511, 0);

			player.dialogue(new NPCDialogue(3531,
					"Reason's central shops! Here you'll find all the basic shops to assist your journey. Ironman players also have access to some of the shops, so be sure to explore everything this marketplace has to offer."));
			e.waitForDialogue(player);
			e.delay(2);

			player.getMovement().teleport(3085, 3497, 0);

			player.dialogue(new NPCDialogue(3531,
					"Here, we have the Reason teleport nexus.<br>" +
							"Interacting with the nexus will open the teleport menu. Once the menu is open, select one of the many options and you'll be transported there."));
			e.waitForDialogue(player);
			e.delay(2);
			player.dialogue(new NPCDialogue(3531,
					"Right behind you is the healing pool! This pool will rid you of any status conditions and heal you completely. You'll also be healed when you teleport home."));
			e.waitForDialogue(player);
			player.getMovement().teleport(3096, 3510, 0);
			e.delay(1);
			player.dialogue(new NPCDialogue(3531,
					"Feeling belligerent already? Look no further than the Slayer Masters, who can assign you tasks. You can also loot the crystal, larran's, brimstone, and slayer chests here."));
			e.waitForDialogue(player);
			player.getMovement().teleport(3094, 3502, 0);
			e.delay(1);
			player.dialogue(new NPCDialogue(3531,
					"This is where you will find the perk master. Talk to him for information about obtaining and equipping perks! Perks play a huge role in Reason, so check them out."));
			e.waitForDialogue(player);
			player.getMovement().teleport(3101, 3492, 0);
			e.delay(1);
			player.dialogue(new NPCDialogue(3531,
					"A little fairy magic here. Take full advantage of this resource!"));
			e.waitForDialogue(player);
			player.getMovement().teleport(3089, 3473, 0);
			e.delay(1);
			player.dialogue(new NPCDialogue(3531,
					"Here is the Camel Statue, where certain boosts can be activated by offering gold. Note: These are global boosts. There is also a thieving area to the left!"));
			e.waitForDialogue(player);
			player.getMovement().teleport(3078, 3501, 0);
			e.delay(1);
			player.dialogue(new NPCDialogue(3531,
					"And finally, here are the point store NPCs! Spend your various points here."));
			e.waitForDialogue(player);
			e.delay(1);

			// player.getPacketSender().moveCameraToLocation(2064, 3583, 1000, 0, 12);
			// player.getPacketSender().turnCameraToLocation(2062, 3570, 0, 0, 30);
			// e.delay(1);
			// player.dialogue(new NPCDialogue(guide,
			// "Last, but not least, there is a skilling area to the east of home just over
			// the bridge. Fishing is also available to the north."));
			// e.delay(10);
			// player.getPacketSender().moveCameraToLocation(2064, 3572, 1200, 0, 12);
			// player.getPacketSender().turnCameraToLocation(2064, 3590, 0, 0, 32);
			// e.waitForDialogue(player);
			VarPlayerRepository.LOCK_CAMERA.set(player, 0);
			player.getPacketSender().resetCamera();
			player.tutorialStage = 1;

			player.getMovement().teleport(3085, 3492, 0);
			player.getPacketSender().moveCameraToLocation(3085, 3492, 450, 0, 12);
			player.getPacketSender().turnCameraToLocation(3085, 3492, 400, 0, 30);
			player.dialogue(new NPCDialogue(3531,
					"Looks like you're ready to begin your adventure, good luck!"));
			e.waitForDialogue(player);
			e.delay(3);

			VarPlayerRepository.ESCAPE_CLOSES.toggle(player);
			SecurityPin.CreateSecurityPin(player);
			player.tutorialStage = 0;
			player.getPacketSender().resetCamera();
		});
	}

	public static boolean alreadyClaimed(Player player) {
		if (ipsClaimed.contains(player.getIp()))
			return true;
		return false;
	}

	public static void addIpsClaimed(String ip) {
		if (ipsClaimed.contains(ip))
			return;
		ipsClaimed.add(ip);
		saveIps();
	}

	public static void saveIps() {
		Server.executeAsync(() -> {
			File file = new File(ServerWrapper.dataFolder, "/runtime/starter_pack_ips.json");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				FileWriter fileWriter = new FileWriter(file);
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String toJson = gson.toJson(ipsClaimed);
				fileWriter.write(toJson);
				fileWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static void loadIps() {
		File file = new File(ServerWrapper.dataFolder, "/runtime/starter_pack_ips.json");
		if (!file.exists()) {
			try {
				file.createNewFile();
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Type type = new TypeToken<ArrayList<String>>() {
		}.getType();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			ArrayList<String> temp = gson.fromJson(new FileReader(file), type);
			if (temp != null)
				ipsClaimed = temp;
			log.info("Loaded " + ipsClaimed.size() + " starter pack ids.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void giveEcoStarter(Player player) {
		boolean alreadyClaimed = alreadyClaimed(player);
		player.getInventory().add(30035);
		player.getInventory().add(30496);
		player.getInventory().add(30477);
		player.getInventory().add(30588);
		player.getInventory().add(30589);
		if (player.getDifficulty() == Difficulty.EXTREME) {
			Item trainingStaff = new Item(30480);
			AttributeExtensions.addCharges(trainingStaff, 10000);
			Item trainingBow = new Item(30478);
			AttributeExtensions.addCharges(trainingBow, 10000);
			Item trainingSword = new Item(30479);
			AttributeExtensions.addCharges(trainingSword, 10000);
			player.getInventory().add(trainingStaff);
			player.getInventory().add(trainingBow);
			player.getInventory().add(trainingSword);
		}
		switch (player.getGameMode()) {
			case IRONMAN:
				player.getInventory().add(COINS_995, 500000);
				player.getInventory().add(562, 500);
				player.getInventory().add(558, 1000);
				player.getInventory().add(554, 10000);
				player.getInventory().add(557, 10000);
				player.getInventory().add(555, 10000);
				player.getInventory().add(556, 10000);
				player.getInventory().add(884, 10000);
				player.getInventory().add(ItemID.STAFF_OF_AIR, 1);
				player.getInventory().add(6108, 1);
				player.getInventory().add(6107, 1);
				player.getInventory().add(6109, 1);
				player.getInventory().add(841, 1);
				player.getInventory().add(1095, 1);
				player.getInventory().add(1129, 1);
				player.getInventory().add(1167, 1);
				player.getInventory().add(12810, 1);
				player.getInventory().add(12811, 1);
				player.getInventory().add(12812, 1);
				player.getInventory().add(1323, 1);
				player.getInventory().add(1067, 1);
				player.getInventory().add(1115, 1);
				player.getInventory().add(1153, 1);
				player.getInventory().add(new Item(ItemID.LOBSTER + 1, 300));
				break;
			case HARDCORE_IRONMAN:
				player.getInventory().add(COINS_995, 500000);
				player.getInventory().add(562, 500);
				player.getInventory().add(558, 1000);
				player.getInventory().add(554, 10000);
				player.getInventory().add(557, 10000);
				player.getInventory().add(555, 10000);
				player.getInventory().add(556, 10000);
				player.getInventory().add(884, 10000);
				player.getInventory().add(ItemID.STAFF_OF_AIR, 1);
				player.getInventory().add(6108, 1);
				player.getInventory().add(6107, 1);
				player.getInventory().add(6109, 1);
				player.getInventory().add(841, 1);
				player.getInventory().add(1095, 1);
				player.getInventory().add(1129, 1);
				player.getInventory().add(1167, 1);
				player.getInventory().add(20792, 1);
				player.getInventory().add(20794, 1);
				player.getInventory().add(20796, 1);
				player.getInventory().add(1323, 1);
				player.getInventory().add(1067, 1);
				player.getInventory().add(1115, 1);
				player.getInventory().add(1153, 1);
				player.getInventory().add(new Item(ItemID.LOBSTER + 1, 300));
				break;
			case ULTIMATE_IRONMAN:
				player.getInventory().add(COINS_995, 500000);
				player.getInventory().add(562, 500);
				player.getInventory().add(558, 1000);
				player.getInventory().add(554, 10000);
				player.getInventory().add(557, 10000);
				player.getInventory().add(555, 10000);
				player.getInventory().add(556, 10000);
				player.getInventory().add(884, 10000);
				player.getInventory().add(ItemID.STAFF_OF_AIR, 1);
				player.getInventory().add(6108, 1);
				player.getInventory().add(6107, 1);
				player.getInventory().add(6109, 1);
				player.getInventory().add(841, 1);
				player.getInventory().add(1095, 1);
				player.getInventory().add(1129, 1);
				player.getInventory().add(1167, 1);
				player.getInventory().add(12813, 1);
				player.getInventory().add(12814, 1);
				player.getInventory().add(12815, 1);
				player.getInventory().add(1323, 1);
				player.getInventory().add(1067, 1);
				player.getInventory().add(1115, 1);
				player.getInventory().add(1153, 1);
				player.getInventory().add(new Item(ItemID.LOBSTER + 1, 300));
				break;
			case GROUP_IRONMAN:
				player.getInventory().add(COINS_995, 500000);
				player.getInventory().add(562, 500);
				player.getInventory().add(558, 1000);
				player.getInventory().add(554, 10000);
				player.getInventory().add(557, 10000);
				player.getInventory().add(555, 10000);
				player.getInventory().add(556, 10000);
				player.getInventory().add(884, 10000);
				player.getInventory().add(ItemID.STAFF_OF_AIR, 1);
				player.getInventory().add(6108, 1);
				player.getInventory().add(6107, 1);
				player.getInventory().add(6109, 1);
				player.getInventory().add(841, 1);
				player.getInventory().add(1095, 1);
				player.getInventory().add(1129, 1);
				player.getInventory().add(1167, 1);
				player.getInventory().add(26156, 1);
				player.getInventory().add(26158, 1);
				player.getInventory().add(26166, 1);
				player.getInventory().add(1323, 1);
				player.getInventory().add(1067, 1);
				player.getInventory().add(1115, 1);
				player.getInventory().add(1153, 1);
				player.getInventory().add(new Item(ItemID.LOBSTER + 1, 300));
				break;
			case HARDCORE_GROUP_IRONMAN:
				player.getInventory().add(COINS_995, 500000);
				player.getInventory().add(562, 500);
				player.getInventory().add(558, 1000);
				player.getInventory().add(554, 10000);
				player.getInventory().add(557, 10000);
				player.getInventory().add(555, 10000);
				player.getInventory().add(556, 10000);
				player.getInventory().add(884, 10000);
				player.getInventory().add(ItemID.STAFF_OF_AIR, 1);
				player.getInventory().add(6108, 1);
				player.getInventory().add(6107, 1);
				player.getInventory().add(6109, 1);
				player.getInventory().add(841, 1);
				player.getInventory().add(1095, 1);
				player.getInventory().add(1129, 1);
				player.getInventory().add(1167, 1);
				player.getInventory().add(26170, 1);
				player.getInventory().add(26172, 1);
				player.getInventory().add(26180, 1);
				player.getInventory().add(1323, 1);
				player.getInventory().add(1067, 1);
				player.getInventory().add(1115, 1);
				player.getInventory().add(1153, 1);
				player.getInventory().add(new Item(ItemID.LOBSTER + 1, 300));
				break;
			case STANDARD:
				// if(alreadyClaimed){
				// break;
				// }
				player.getInventory().add(COINS_995, 500000);
				player.getInventory().add(562, 500);
				player.getInventory().add(558, 1000);
				player.getInventory().add(554, 10000);
				player.getInventory().add(557, 10000);
				player.getInventory().add(555, 10000);
				player.getInventory().add(556, 10000);
				player.getInventory().add(884, 10000);
				player.getInventory().add(ItemID.STAFF_OF_AIR, 1);
				player.getInventory().add(6108, 1);
				player.getInventory().add(6107, 1);
				player.getInventory().add(6109, 1);
				player.getInventory().add(841, 1);
				player.getInventory().add(1095, 1);
				player.getInventory().add(1129, 1);
				player.getInventory().add(1167, 1);
				player.getInventory().add(1067, 1);
				player.getInventory().add(1115, 1);
				player.getInventory().add(1153, 1);
				player.getInventory().add(1323, 1);
				break;
		}

		addIpsClaimed(player.getIp());

	}

	private static NPC find(Player player, int id) {
		for (NPC n : player.localNpcs()) {
			if (n.getId() == id)
				return n;
		}
		throw new IllegalArgumentException();
	}

	private static void setDrag(Player player) {
		player.dialogue(
				new OptionsDialogue("What drag setting would you like to use?",
						new Option("5 (OSRS) (2007) Drag", () -> setDrag(player, 5)),
						new Option("10 (Pre-EoC) (2011) Drag", () -> setDrag(player, 10))));
	}

	private static void setDrag(Player player, int drag) {
		player.dragSetting = drag;
	}

	private static void tutorial(Player player) {
		ecoTutorial(player);
	}

	private static void addPKModeItemToBank(Player player) {
		player.getBank().add(19625, 5); // Home teleport
		player.getBank().add(2550, 3); // Recoils
		player.getBank().add(385, 125); // Sharks
		player.getBank().add(3144, 50); // Karambwans
		player.getBank().add(2436, 5); // attk
		player.getBank().add(2440, 5); // str
		player.getBank().add(2444, 5); // range
		player.getBank().add(3024, 5); // restore
		// Next Line
		player.getBank().add(6685, 10); // brew
		player.getBank().add(560, 2250); // Death runes
		player.getBank().add(565, 1000); // Blood runes
		player.getBank().add(561, 300); // Nature runes
		player.getBank().add(145, 1); // atk
		player.getBank().add(157, 1); // str
		player.getBank().add(169, 1); // range
		player.getBank().add(3026, 1); // restore
		// Next Line
		player.getBank().add(6687, 1); // brew
		player.getBank().add(9075, 400); // Astral runes
		player.getBank().add(555, 6000); // Water runes
		player.getBank().add(557, 1000); // Earth runes
		player.getBank().add(147, 1); // atk
		player.getBank().add(159, 1); // str
		player.getBank().add(171, 1); // range
		player.getBank().add(3028, 1); // restore
		// Next Line
		player.getBank().add(6689, 1); // brew
		player.getBank().add(7458, 100); // mithril gloves for pures
		player.getBank().add(7462, 100); // gloves
		player.getBank().add(3842, 100); // god book
		player.getBank().add(149, 1); // atk
		player.getBank().add(161, 1); // str
		player.getBank().add(173, 1); // range
		player.getBank().add(3030, 1); // restore
		// Next Line
		player.getBank().add(6691, 1); // brew
		player.getBank().add(9144, 500); // bolts
		player.getBank().add(2503, 5); // hides
		player.getBank().add(4099, 5); // Mystic
		player.getBank().add(2414, 100); // zamy god cape
		player.getBank().add(10828, 5); // neit helm
		player.getBank().add(4587, 5); // Scim
		player.getBank().add(1163, 3); // rune full helm
		// Next Line
		player.getBank().add(562, 50); // Chaos rune
		player.getBank().add(892, 400); // rune arrows
		player.getBank().add(2497, 5); // hides
		player.getBank().add(4101, 5); // Mystic
		player.getBank().add(4675, 5); // ancient staff
		player.getBank().add(1201, 5); // rune
		player.getBank().add(5698, 5); // dagger
		player.getBank().add(1127, 3); // rune pl8
		// Next Line
		player.getBank().add(563, 50); // law rune
		player.getBank().add(9185, 5); // crossbow
		player.getBank().add(10499, 100); // avas
		player.getBank().add(4103, 5); // Mystic
		player.getBank().add(4107, 5); // Mystic
		player.getBank().add(3105, 5); // climbers
		player.getBank().add(11978, 3); // glory(6)
		player.getBank().add(1079, 3); // rune legs
		// Next Line
		player.getBank().add(1215, 2); // dagger unpoisoned
		player.getBank().add(3751, 2); // zerker helm
		player.getBank().add(1093, 2); // rune

		// Give the players PK stats
		player.getStats().get(StatType.Attack).set(99);
		player.getStats().get(StatType.Strength).set(99);
		player.getStats().get(StatType.Defence).set(99);
		player.getStats().get(StatType.Hitpoints).set(99);
		player.getStats().get(StatType.Magic).set(99);
		player.getStats().get(StatType.Ranged).set(99);
		player.getStats().get(StatType.Prayer).set(99);
		player.getCombat().updateLevel();
	}

}

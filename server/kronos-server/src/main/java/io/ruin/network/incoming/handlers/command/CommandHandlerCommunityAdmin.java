package io.ruin.network.incoming.handlers.command;

import discord.webhooks.logs.AdministrationHook;
import io.ruin.Server;
import io.ruin.api.utils.IPMute;
import io.ruin.cache.NPCType;
import io.ruin.cache.ObjType;
import io.ruin.data.yaml.YamlLoader;
import io.ruin.data.yaml.impl.ShopLoader;
import io.ruin.db.PlayerDatabase;
import io.ruin.model.VoteHandler;
import io.ruin.model.World;
import io.ruin.model.activities.VoteBossHandler;
import io.ruin.model.activities.inferno.Inferno;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.content.HomeHandler;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.GameMode;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerGroup;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.Widget;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.handlers.TeleInterface;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Locations;
import io.ruin.model.map.Position;
import io.ruin.model.map.Region;
import io.ruin.model.map.Tile;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.dynamic.DynamicMapGuard;
import io.ruin.model.skills.magic.SpellBook;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.network.central.CentralClient;
import io.ruin.services.Punishment;
import io.ruin.utility.CS2Script;
import io.ruin.utility.Utils;
import org.json.JSONObject;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static io.ruin.model.entity.player.GameMode.*;
import static io.ruin.model.entity.player.PlayerGroup.*;
import static io.ruin.network.incoming.handlers.command.CommandHandler.*;

public class CommandHandlerCommunityAdmin {
	public static boolean handle(Player player, String query, String command, String[] args) throws Exception {
		if (!player.isCommunityAdmin()) {
			return false;
		}

		switch (command) {

			case "discord_enable": {
					discord.webhooks.Webhook.disable = false;
					return true;
			}

			case "discord_disable": {
					discord.webhooks.Webhook.disable = true;
					return true;
			}

			case "addtocollectionlog": {
				forPlayerInt(player, query, "::addtocollectionlog playerName itemId", (p2, itemId) -> {
					p2.addToCollectionLog(new Item(itemId, 1));
					player.sendMessage(
						"You've added " + new Item(itemId).getDef().name + " to " + p2.getName() + "'s collection log.");
				});
				return true;
			}

			case "spawn_summer" : {
				new NPC(17020)
					.spawn(new Position(SummerEvent.map.convertX(2912), SummerEvent.map.convertY(3616), 0));

				for (Player p : World.players()) {
					p.getPacketSender().sendBroadcast("</col>[<shad=8A0011>Summer Event</shad>] <shad=9B9B9B> " +
						"The summer boss has spawned, type ::sb to get there!");
				}
				return true;
			}

			case "dms", "dynamic_map_stats": {
				player.sendMessage("FREE_REGIONS: " + DynamicMap.FREE_REGIONS_SIZE());
				player.sendMessage("ACTIVE_DYNAMIC_MAPS: " + DynamicMapGuard.size());
				DynamicMapGuard.dumpActive();
				return true;
			}

			case "npc_spawn_info": {
				player.sendMessage("Total Spawned NPC: %d".formatted(World.getNpcs().size()));
				return true;
			}
			case "getip": {
				var username = query.substring(command.length() + 1);
				var uuid = username.toLowerCase().trim();
				PlayerDatabase.db().getAsync(uuid, p -> {
					if (p == null) {
						player.sendMessage("Unable to find [" + uuid + "]");
						return;
					}
					player.sendMessage("Last ip of [" + uuid + "] is [" + p.ipAddress + "]");
				});
				CommandHandler.logToDiscord(player, command, args);
				return true;
			}

			case "giveref": {
				forPlayer(player, query, "::giveref playerName", p2 -> {
					p2.hasRef = true;
					player.sendMessage("You have given " + p2.getName() + " a referral.");
				});
				return true;
			}

			case "gethwidvoters": {
				VoteHandler.getAllHwids();
				return true;
			}

			case "hwid": {
				player.sendMessage("Your HWID is \"" + player.hwid + "\"");
				return true;
			}

			case "votehwid": {
				boolean bool = VoteHandler.hasVotedRecently(player.hwid);
				// System.out.println("Has voted recently: " + bool);
				if (VoteHandler.hasVotedRecently(player.hwid)) {
					player.sendMessage("You have already voted recently.");
				} else {
					player.sendMessage("You have not voted recently.");
					VoteHandler.addHwid(player.hwid);
				}
				return true;
			}

			case "arceuus": {
				HomeHandler.switchBook(player, SpellBook.ARCEUUS, false);
				return true;
			}

			case "addvote": {
				int amount = Integer.parseInt(args[0]);
				VoteBossHandler.addVote(amount);
				CommandHandler.logToDiscord(player, command, args);
				return true;
			}

			case "getbase": {
				int x;
				int y;
				int z = player.getPosition().getZ();
				int baseX = player.getPosition().getRegion().baseX;
				int baseY = player.getPosition().getRegion().baseY;
				x = player.getPosition().getX() - baseX;
				y = player.getPosition().getY() - baseY;
				player.sendMessage("BaseX: " + baseX + ", BaseY: " + baseY);
				player.sendMessage("X: " + x + ", Y: " + y + ", Z: " + z);
				return true;
			}

			case "simdrop": {
				int amount = Integer.parseInt(args[0]);
				int npcId = Integer.parseInt(args[1]);
				float dropRateBonus = Float.parseFloat(args[2]);
				for (int i = 0; i < amount; i++) {
					LootTable.simulateDrop(npcId, dropRateBonus);
				}
				return true;
			}

			case "t": {
				player.openInterface(ToplevelComponent.MAINMODAL, Interface.ITEM_EXCHANGE);
				for (int i = 19; i <= 28; i++) {
					player.getPacketSender().sendItems(Interface.ITEM_EXCHANGE, i, 0, new Item(22327));
				}
				return true;
			}

			case "checkclip": {
				Tile tile = Tile.get(player.getPosition(), true);
				int clipping = tile.clipping;
				player.sendMessage("Tile Free: " + tile.isTileFree());
				player.sendMessage("Wall Free: " + tile.isWallsFree());
				player.sendMessage("Floor Free: " + tile.isFloorFree());
				player.sendMessage("Decor Free: " + tile.isFloorFreeCheckDecor());
				player.sendMessage("Decor Free: " + tile.isTileFreeCheckDecor());
				player.sendMessage("Raw Clip: " + clipping);
				return true;
			}

			case "reloadmutes": {
				IPMute.refreshMutes();
				return true;
			}

			case "reloadbans": {
				CentralClient.reloadBans();
				return true;
			}

			case "iunderlay": {
				player.setInterfaceUnderlay(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
				return true;
			}

			case "tobfade": {
				CS2Script.TOB_HUD_FADE.sendScript(player, 500, 10, "THIS IS TEXT");
				return true;
			}

			case "gauntlet": {
				Position CHEST_LOCATION = new Position(3031, 6121, 1);
				player.getMovement().teleport(CHEST_LOCATION);
				return true;
			}

			case "uidban": {
				forPlayer(player, query, "::yeet target", p2 -> {
					Punishment.uuidBan(player, p2);
					CentralClient.reloadBans();
					// Broadcast.WORLD.sendNews(Icon.BLUE_INFO_BADGE, "User: " + p2.getName() + "
					// Has been yeeted out of the server!");
				});
				return true;
			}

			case "npc_head_icons": {
				World.startEvent(e -> {
					var npc = new NPC(9036).spawn(player.getPosition(), 0);
					e.delay(2);
					for (var icon : NPC.DefaultHeadIconIndex.values()) {
						npc.setHeadIcon(icon);
						e.delay(2);
					}
					npc.removeHeadIcon();
					e.delay(2);
					npc.remove();
				});
				return true;
			}

			case "img": {
				int id = Integer.parseInt(args[0]);
				player.sendMessage("<img=" + id + ">");
				return true;
			}

			case "newstest": {
				for (int i = 0; i < 400; i++) {
					int finalI = i;
					World.startEvent(event -> {
						event.delay(1);
						player.sendMessage("image: " + finalI + ": <img=" + finalI + ">");
					});
				}
				return true;
			}

			case "cancelperktask": {
				String name = query.substring(command.length() + 1);
				Player p2 = World.getPlayer(name);

				if (p2 == null)
					player.sendMessage("Could not find player: " + name);
				else {
					p2.currentPerkTask = null;
					p2.perkTaskCurrentAmount = 0;
					p2.sendMessage("Your perk task has been cancelled.");
				}
				return true;
			}


			case "resetdiff": {
				forPlayer(player, query, "::resetdiff playerName", targetPlayer -> {
					targetPlayer.resetDifficultyChanges();
					player.sendMessage("You've reset " + targetPlayer.getName() + "'s difficulty change count to 2.");
				});
				return true;
			}
			case "resetvengtimer", "rvt": {
				forPlayer(player, query, "::resetvengtimer playerName", p2 -> {
					VarPlayerRepository.VENG_COOLDOWN.set(p2, 0);
					player.vengeanceActive = false;
					p2.getPacketSender().sendWidgetTimerCustom(Widget.VENGEANCE, 0);
					player.sendMessage("You have reset the veng timer for: %s".formatted(p2.getName()));
					CommandHandler.logToDiscord(player, command, args);
				});
				return true;
			}

			case "adddonordeal": {
				forPlayerInt(player, query, "::adddonordeal playerName amount", (p2, amount) -> {
					if (amount > 500)
						amount = 500;
					p2.donationDealAmount += amount;
					var jsonObject = new JSONObject();
						jsonObject.put("player", player.getName());
						jsonObject.put("amount", Utils.formatMoneyString(amount));
						jsonObject.put("player_receiving", p2.getName());
					AdministrationHook.sendAdminDonorDealAmountLogsToDiscord(jsonObject);
				});
				return true;
			}

			case "removefromgroup", "rfg": {
				forPlayer(player, query, "::removefromgroup playerName", p2 -> {
					p2.newGroupId = 0;
					Instant now = Instant.now();
					p2.groupLeaveInEpoch = now.getEpochSecond();
					player.sendMessage("You have removed " + p2.getName() + " from their group.");
					CommandHandler.logToDiscord(player, command, args);
				});
				return true;
			}
			case "givehcim": {
				forPlayer(player, query, "::givehcim playerName", p2 -> {
					VarPlayerRepository.IRONMAN_MODE.set(p2, 3);
					changeForumsGroup(p2, HARDCORE_IRONMAN.groupId);
					player.sendMessage("Gave hardcore ironman to " + p2.getName() + ".");
				});
				return true;
			}

			case "deironplayer": {
				forPlayer(player, query, "::deiron playerName", p2 -> {
					VarPlayerRepository.IRONMAN_MODE.set(player, 0);
					VarPlayerRepository.CHAT_ICONS.set(player, 0);
					GameMode.changeForumsGroup(player, GameMode.STANDARD.groupId);
					player.sendMessage("Removed ironman status from " + p2.getName() + ".");
				});
				return true;
			}


			case "giveultimate": {
				forPlayer(player, query, "::giveultimate playerName", p2 -> {
					VarPlayerRepository.IRONMAN_MODE.set(p2, 2);
					changeForumsGroup(p2, ULTIMATE_IRONMAN.groupId);
					player.sendMessage("Gave ultimate ironman to " + p2.getName() + ".");
				});
				return true;
			}

			case "giveironman": {
				forPlayer(player, query, "::giveironman playerName", p2 -> {
					VarPlayerRepository.IRONMAN_MODE.set(p2, 1);
					changeForumsGroup(p2, IRONMAN.groupId);
					player.sendMessage("Gave ironman to " + p2.getName() + ".");
				});
				return true;
			}

			case "givegroupiron": {
				forPlayer(player, query, "::givegroupiron playerName", p2 -> {
					VarPlayerRepository.IRONMAN_MODE.set(p2, 4);
					changeForumsGroup(p2, GROUP_IRONMAN.groupId);
					player.sendMessage("Gave group ironman to " + p2.getName() + ".");
				});
				return true;
			}

			case "givehardgroupiron": {
				forPlayer(player, query, "::givehardgroupiron playerName", p2 -> {
					VarPlayerRepository.IRONMAN_MODE.set(p2, 5);
					changeForumsGroup(p2, HARDCORE_GROUP_IRONMAN.groupId);
					player.sendMessage("Gave group ironman to " + p2.getName() + ".");
				});
				return true;
			}

			case "checkgroup": {
				forPlayer(player, query, "::checkgroup playerName", p2 -> {
					int id = VarPlayerRepository.IRONMAN_MODE.get(p2);
					player.sendMessage(p2.getName() + "'s GameMode Id is " + id + ".");
				});
				return true;
			}

			case "skins": {
				player.unlockedGreenSkin = !player.unlockedGreenSkin;
				player.unlockedBlueSkin = !player.unlockedBlueSkin;
				player.unlockedPurpleSkin = !player.unlockedPurpleSkin;
				player.unlockedWhiteSkin = !player.unlockedWhiteSkin;
				player.unlockedBlackSkin = !player.unlockedBlackSkin;
				return true;
			}

			case "skin": {
				int newSkin = Integer.parseInt(args[0]);
				player.getAppearance().colors[4] = newSkin;
				player.getAppearance().update();
				return true;
			}

			case "dialogueanim": {
				int animid = Integer.parseInt(args[0]);
				player.dialogue(new PlayerDialogue("Testing anim: " + animid).animate(animid));
				return true;
			}

			case "resetshrinetimer": {
				player.lastSacrifice = 0;
				return true;
			}

			case "titleunlock": {
				player.hasCustomTitle = !player.hasCustomTitle;
				player.sendMessage("You have " + (player.hasCustomTitle ? "enabled" : "disabled") + " access to custom titles");
				return true;
			}

			case "idef":
			case "itemdef": {
				int itemId = Integer.parseInt(args[0]);
				ObjType def = ObjType.get(itemId);
				if (def == null) {
					player.sendMessage("Invalid item definition for fileId " + itemId + ".");
				} else {
					player.sendMessage(String.format("[ItemDef] id=%d, tradeable=%b", itemId, def.tradeable));
				}
				return true;
			}

			case "infernotest": {
				new Inferno(player, Integer.parseInt(args[0]), false).start(true);
				return true;
			}

			case "broadcast":
			case "bc": {
				String message = String.join(" ", args);
				World.players().forEach(p -> p.getPacketSender().sendBroadcast(message));
				return true;
			}

			case "resetmaster":
				forPlayer(player, query, "::resetmaster playerName", p2 -> {
					p2.currentSlayerMaster = 0;
				});
				return true;

			case "checkmaster": {
				forPlayer(player, query, "::checkmaster playerName", p2 -> {
					player.sendMessage("Slayer Master ID = " + p2.currentSlayerMaster);
				});
				return true;
			}

			case "checkpoints": {
				forPlayer(player, query, "::checkpoints playerName", p2 -> {
					player.sendMessage(p2.getName() + " Slayer Points = " + VarPlayerRepository.SLAYER_POINTS.get(p2));
				});
				return true;
			}

			case "checkxpmode": {
				forPlayer(player, query, "::xpmode playerName", p2 -> {
					player.sendMessage("XPMODE " + player.xpMode.getName().toLowerCase());
				});
				return true;
			}

			case "resetpoints": {
				forPlayer(player, query, "::resetpoints playerName", p2 -> {
					VarPlayerRepository.SLAYER_POINTS.set(p2, 0);
					player.sendMessage(p2.getName() + " Slayer Points have been reset to 0");
				});
				return true;
			}

			case "setslayerpoints": {
				forPlayerInt(player, query, "::setslayerpoints playerName", VarPlayerRepository.SLAYER_POINTS::set);
				return true;
			}

			case "checkclue": {
				Player p2 = World.getPlayer(String.join(" ", args));
				assert p2 != null: "Playre to check is null";
				if (p2.easyClue != null)
					player.sendMessage("Easy[%d]".formatted(p2.easyClue.id));
				if (p2.medClue != null)
					player.sendMessage("Med[%d]".formatted(p2.medClue.id));
				if (p2.hardClue != null)
					player.sendMessage("Hard[%d]".formatted(p2.hardClue.id));
				if (p2.eliteClue != null)
					player.sendMessage("Elite[%d]".formatted(p2.eliteClue.id));
				if (p2.masterClue != null)
					player.sendMessage("Master[%d]".formatted(p2.masterClue.id));
				return true;
			}

			case "attribs": {
				int id = Integer.parseInt(args[0]);
				ObjType def = ObjType.get(id);
				if (def == null) {
					player.sendMessage("Item " + id + " not found!");
					return true;
				}
				if (def.attributes == null) {
					player.sendMessage("Item " + id + " has no attributes!");
					return true;
				}
				System.out.println("Attributes for item " + id + ":");
				def.attributes.forEach((key, value) -> System.out.println("    " + key + "=" + value));
				return true;
			}

			case "pnpc": {
				int npcId = Integer.parseInt(args[0]);
				if (npcId > 0) {
					NPCType def = NPCType.get(npcId);
					if (def == null) {
						player.sendMessage("Invalid npc id: " + npcId);
						return true;
					}
					player.temp.put("LAST_PNPC", npcId);
					player.getAppearance().setNpcId(npcId);
					player.sendMessage(def.name + " " + def.size);
				} else {
					player.getAppearance().setNpcId(-1);
				}
				player.getAppearance().update();
				return true;
			}

			case "pnpcs": {
				Integer lastId = (Integer) player.temp.get("LAST_PNPC");
				if (lastId == null)
					lastId = 0;
				NPCType def = NPCType.get(lastId);
				if (def == null) {
					player.sendMessage("Invalid npc id: " + lastId);
					return true;
				}
				player.getAppearance().setNpcId(lastId);
				player.sendMessage("pnpc: " + lastId);
				player.temp.put("LAST_PNPC", lastId + 1);
				player.getAppearance().update();
				return true;
			}
			case "fn":
			case "findnpc": {
				int l = command.length() + 1;
				if (query.length() > l) {
					String search = query.substring(l).toLowerCase();

					for (NPCType def : NPCType.cached.values()) {
						if (def == null || def.name == null)
							continue;

						if (def.name.toLowerCase().contains(search)) {
							player.sendMessage("    " + def.id + ": " + def.name);
						}
					}
				}
				return true;
			}

			case "reloadshops": {
				YamlLoader.load(List.of(new ShopLoader()));
				return true;
			}


			/*
			 * Map commands
			 */
			case "pos":
			case "coords": {
				player.sendMessage("Abs: %d, %d, %d"
					.formatted(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()));
				return true;
			}

			case "tp":
			case "tele":
			case "teleport": {

				if (args == null || args.length == 0) {
					TeleInterface teleInterface = new TeleInterface();
					teleInterface.open(player);
					return true;
				}
				int x, y, z;
				try {
					x = Integer.parseInt(args[0]);
					y = Integer.parseInt(args[1]);
					if (args.length > 2)
						z = Math.max(0, Math.min(3, Integer.parseInt(args[2])));
					else
						z = player.getPosition().getZ();
				} catch (Exception e) {
					int l = command.length() + 1;
					if (query.length() <= l)
						return true;
					String loc = query.substring(l).trim();
					Locations locations = Locations.find(loc);
					if (locations == null) {
						player.sendMessage("Invalid teleport location: " + loc);
						return true;
					}
					x = locations.x;
					y = locations.y;
					z = locations.z;
				}
				int regionId = Region.getId(x, y);
				if (regionId < 0 || regionId >= Region.LOADED.length) {
					player.sendMessage("Invalid teleport coordinates: " + x + ", " + y + ", " + z);
					return true;
				}
				player.getMovement().teleport(x, y, z);
				return true;
			}

		}
		return false;
	}
}

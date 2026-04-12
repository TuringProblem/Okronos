package io.ruin.network.incoming.handlers.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.gson.GsonBuilder;
import core.task.Continuations;
import io.ruin.Server;
import io.ruin.api.protocol.login.LoginInfo;
import io.ruin.api.utils.JsonUtils;
import io.ruin.api.utils.NumberUtils;
import io.ruin.cache.*;
import io.ruin.cache.runetek4.vartype.bit.VarBitType;
import io.ruin.data.DataFile;
import io.ruin.data.impl.Help;
import io.ruin.data.impl.items.item_info;
import io.ruin.data.impl.items.shield_types;
import io.ruin.data.impl.items.weapon_types;
import io.ruin.data.impl.npcs.npc_combat;
import io.ruin.data.impl.npcs.npc_drops;
import io.ruin.data.impl.npcs.npc_spawns;
import io.ruin.data.impl.objects.object_spawns;
import io.ruin.data.impl.teleports;
import io.ruin.model.World;
import io.ruin.model.activities.SecurityGuard;
import io.ruin.model.activities.raids.tob.party.TheatrePartyManager;
import io.ruin.model.activities.raids.xeric.ChambersOfXeric;
import io.ruin.model.activities.raids.xeric.chamber.Chamber;
import io.ruin.model.activities.raids.xeric.chamber.ChamberDefinition;
import io.ruin.model.activities.raids.xeric.party.Party;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.content.HomeHandler;
import io.ruin.model.content.upgrade.ItemEffect;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.*;
import io.ruin.model.inter.AccessMasks;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.skill.SkillDialogue;
import io.ruin.model.inter.dialogue.skill.SkillItem;
import io.ruin.model.inter.handlers.OptionScroll;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.inter.utils.Unlock;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.ItemBreaking;
import io.ruin.model.item.actions.impl.ItemUpgrading;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.containers.Inventory;
import io.ruin.model.item.containers.bank.Bank;
import io.ruin.model.item.containers.bank.BankItem;
import io.ruin.model.map.*;
import io.ruin.model.map.dynamic.DynamicMap;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.shop.ShopManager;
import io.ruin.model.skills.construction.*;
import io.ruin.model.skills.construction.actions.Costume;
import io.ruin.model.skills.construction.actions.CostumeStorage;
import io.ruin.model.skills.hunter.Impling;
import io.ruin.model.skills.magic.SpellBook;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.skills.mining.Mining;
import io.ruin.model.skills.mining.Pickaxe;
import io.ruin.model.skills.mining.Rock;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.network.incoming.handlers.DevKitHandler;
import io.ruin.services.Punishment;
import io.ruin.utility.AttributePair;
import io.ruin.utility.Broadcast;
import io.ruin.utility.CharacterBackups;
import io.ruin.utility.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import static core.task.api.API.*;
import static io.ruin.cache.ItemID.*;
import static io.ruin.model.entity.player.PlayerGroup.*;
import static io.ruin.network.incoming.handlers.command.CommandHandler.*;

@Slf4j
public class CommandHandlerAdmin {

	public static boolean handle(Player player, String query, String command, String[] args)
			throws Exception {
		if (!player.isAdmin()) {
			return false;
		}

		switch (command) {

			case "test": {
				return true;
			}

			case "sb_force": {
				SummerEvent.teleportToBoss(player);
				SummerEvent.skipBossKilling();
				SummerEvent.skipActivationTimer();
				return true;
			}

			case "backup_enable": {
				CharacterBackups.enabled = true;
				player.sendMessage("Character backups enabled");
				return true;
			}

			case "backup_disable": {
				CharacterBackups.enabled = false;
				player.sendMessage("Character backups disabled");
				return true;
			}

			case "bank_show": {
				var bank = player.getBank();
				for (var item : bank.getItems()) {
					if (item == null) {
						continue;
					}
					player.sendMessage(item.getId() + " : " + item.getAmount());
				}
				return true;
			}

			case "bank_clean": {
				var bank = player.getBank();
				for (var item : bank.getItems()) {
					if (item == null) {
						continue;
					}
					bank.remove(item);
				}
				return true;
			}

			case "bank_fill": {
				var bank = player.getBank();
				player.sendMessage("free_slots_before: " + bank.getFreeSlots());
				var count = 1000;
				var id = 0;

				while (true) {
					if (count == 0) {
						break;
					}
					var def = ObjType.get(id);

					if (def == null) {
						id += 1;
						continue;
					}

					if (def.tradeable && !def.isNote()) {
						bank.add(id);
						count -= 1;
					}

					id += 1;
				}
				player.sendMessage("free_slots_after: " + bank.getFreeSlots());
				return true;
			}

			case "hp": {
				int amount = Integer.parseInt(args[0]);
				player.setHp(amount);
				player.sendMessage("HP set to " + amount + ".");

				return true;
			}

			case "spec": {
				player.getCombat().restoreSpecial(100);
				return true;
			}

			case "maxhp": {
				if (player.getHp() > 120)
					player.setHp(player.getMaxHp());
				else
					player.setHp(30000);

				return true;
			}

			case "clearcostumeroom": {
				for (CostumeStorage s : CostumeStorage.VALUES) {
					Map<Costume, int[]> stored = s.getSets(player);
					stored.clear();
				}
				return true;
			}

			case "raidroom": {
				int rotation = 0;
				int layout = 0;
				if (args != null && args.length > 0)
					rotation = Integer.parseInt(args[0]);
				if (args != null && args.length > 1)
					layout = Integer.parseInt(args[1]);
				int finalRotation = rotation;
				int finalLayout = layout;
				Consumer<ChamberDefinition> run = definition -> {
					Chamber chamber = definition.newChamber();
					if (chamber == null) {
						player.sendMessage("Failed to generate room");
						return;
					}

					DynamicMap map;
					try {
						map = new DynamicMap().build(chamber.getChunks());
					} catch (DynamicMap.DynamicMapBuildException e) {
						player.sendMessage("Unable to build dynamic map.");
						return;
					}

					ChambersOfXeric raid = new ChambersOfXeric();
					Party party = new Party(player);
					player.raidsParty = party;
					raid.setParty(party);
					party.setRaid(raid);
					chamber.setRaid(raid);
					chamber.setRotation(finalRotation);
					chamber.setLayout(finalLayout);
					chamber.setLocation(0, 0, 0);
					raid.setMap(map);
					chamber.setBasePosition(new Position(map.swRegion.baseX, map.swRegion.baseY, 0));
					chamber.onBuild();
					chamber.onRaidStart();
					player.getMovement().teleport(chamber.getPosition(15, 15));
				};
				OptionScroll.open(player, "Select a room type", true,
						Arrays.stream(ChamberDefinition.values())
								.map(cd -> new Option(cd.getName(), () -> run.accept(cd)))
								.collect(Collectors.toList()));
				return true;
			}
			/**
			 * Interface commands
			 */
			case "interface":
			case "inter": {
				int interfaceId = Integer.parseInt(args[0]);
				ToplevelComponent type = ToplevelComponent.MAINMODAL;
				if (args.length == 2)
					type = ToplevelComponent.valueOf(args[1].toUpperCase());
				player.temp.put("last_inter_cmd", interfaceId);
				player.openInterface(type, interfaceId);
				return true;
			}
			case "inters": {
				ToplevelComponent type = ToplevelComponent.MAINMODAL;
				if (args != null && args.length == 1)
					type = ToplevelComponent.valueOf(args[0].toUpperCase());
				int interfaceId = (int) player.temp.getOrDefault("last_inter_cmd", 0);
				if (interfaceId == 548 || interfaceId == 161 || interfaceId == 164) // main screen
					interfaceId++;
				if (interfaceId == Interface.CHAT_BAR) // chat box
					interfaceId++;
				if (interfaceId == 156) // annoying
					interfaceId++;
				player.temp.put("last_inter_cmd", interfaceId + 1);
				player.openInterface(type, interfaceId);
				player.sendFilteredMessage("Interface: " + interfaceId);
				return true;
			}

			case "ic":
			case "iconf": {
				int interfaceId = Integer.parseInt(args[0]);
				boolean recursiveSearch = args.length >= 2 && Integer.parseInt(args[1]) == 1;
				InterfaceDef.printConfigs(interfaceId, recursiveSearch);
				return true;
			}

			case "findinterscript": {
				int scriptId = Integer.parseInt(args[0]);
				boolean recursiveSearch = args.length >= 2 && Integer.parseInt(args[1]) == 1;
				for (int interId = 0; interId < InterfaceDef.COUNTS.length; interId++) {
					Set<ScriptDef> s = InterfaceDef.getScripts(interId, recursiveSearch);
					if (s != null && s.stream().anyMatch(def -> def.id == scriptId)) {
						System.out.println("Inter " + interId + " uses script " + scriptId + "!");
					}
				}
				return true;
			}

			case "v":
			case "varp": {
				int id = Integer.parseInt(args[0]);
				int value = Integer.parseInt(args[1]);
				if (id < 0 || id >= 30000) {
					player.sendFilteredMessage("Varp " + id + " does not exist.");
					return true;
				}
				VarPlayerRepository.create(id, null, false, false).set(player, value);
				player.sendFilteredMessage("Updated varp " + id + "!");
				return true;
			}

			case "slaytest": {
				int UNLOCK_REWARDS_FIRST_VARP = 1076;
				int UNLOCK_REWARDS_SECOND_VARP = 1344;
				long bitpacked = 0;
				bitpacked |= 1 << 1L;
				bitpacked |= 1 << 2L;
				bitpacked |= 1 << 3L;
				bitpacked |= 1 << 4L;
				bitpacked |= 1 << 5L;
				bitpacked |= 1 << 43L;
				bitpacked |= 1 << 44L;
				bitpacked |= 1 << 45L;
				bitpacked |= 1 << 46L;
				bitpacked |= 1 << 47L;
				VarPlayerRepository.create(UNLOCK_REWARDS_FIRST_VARP, null, false, false)
						.set(player, (int) ((bitpacked >> 32) & 0xFFFFFFFFL));

				VarPlayerRepository.create(UNLOCK_REWARDS_SECOND_VARP, null, false, false)
						.set(player, (int) (bitpacked & 0xFFFFFFFFL));
				player.sendFilteredMessage("Updated slayer varps");
				return true;
			}

			case "allaccessmasks":
			case "aam": {
				int componentID = Integer.parseInt(args[0]);
				int mask = args.length > 1 ? Integer.parseInt(args[1]) : AccessMasks.ClickOp1.getMask();
				for (int i = 0; i < 1000; i++) {
					player.getPacketSender().sendIfEvents(false, componentID, i, 0, 1000, mask);
				}
				player.sendMessage(
						"Sent all childs in " + componentID + " mask " + mask + "("
								+ AccessMasks.determineString(mask) + ")");
				return true;
			}

			case "varpbit_set": {
				if (args.length == 3) {
					int id = Integer.parseInt(args[1]);
					int value = Integer.parseInt(args[2]);
					forPlayer(player, query, "::varpbit_set player_name id value",
							p2 -> {
								VarBitType bit = VarBitType.get(id);
								if (bit == null) {
									player.sendFilteredMessage("Varpbit " + id + " does not exist.");
									return;
								}
								VarPlayerRepository.create(id, bit, false, false).set(p2, value);
								player.sendFilteredMessage(
										"Updated varp " + bit.varpId + " with varpbit " + id + "!");
							});
					return true;
				}

				int id = Integer.parseInt(args[1]);
				int value = Integer.parseInt(args[2]);
				VarBitType bit = VarBitType.get(id);
				if (bit == null) {
					player.sendFilteredMessage("Varpbit " + id + " does not exist.");
					return true;
				}
				VarPlayerRepository.create(id, bit, false, false).set(player, value);
				player.sendFilteredMessage("Updated varp " + bit.varpId + " with varpbit " + id + "!");
				return true;
			}

			case "varpbit_get": {
				if (args.length == 3) {
					int id = Integer.parseInt(args[1]);
					forPlayer(player, query, "::varpbit_get player_name id",
							p2 -> {
								var value = VarPlayerRepository.varpbit(id, false).get(p2);
								player.sendFilteredMessage("Varpbit " + id + " value: " + value);
							});
					return true;
				}
				int id = Integer.parseInt(args[0]);
				var value = VarPlayerRepository.varpbit(id, false).get(player);
				player.sendFilteredMessage("Varpbit " + id + " = " + value);
				return true;
			}

			case "varbitdef": {
				int varpbit = Integer.parseInt(args[0]);
				VarBitType def = VarBitType.get(varpbit);
				if (def != null) {
					player.sendMessage("[Varpbit Def] varp=" + def.varpId + ", start=" + def.leastSigBit
							+ ", end="
							+ def.mostSigBit + ", maxValue=" + Math.pow(2, (def.mostSigBit - def.leastSigBit)));
				} else {
					player.sendMessage("No definition entry found for varpbit " + varpbit + ".");
				}
				return false;
			}

			case "vbs":
			case "varpbits": {
				int minId = Integer.parseInt(args[0]);
				int maxId = Integer.parseInt(args[1]);
				int value = Integer.parseInt(args[2]);
				if (minId < 0 || minId >= VarBitType.LOADED.length || maxId < 0
						|| maxId >= VarBitType.LOADED.length) {
					player.sendFilteredMessage(
							"Invalid values! Please use values between 0 and " + (VarBitType.LOADED.length - 1)
									+ "!");
					return true;
				}
				for (int i = minId; i <= maxId; i++) {
					VarBitType bit = VarBitType.get(i);
					if (bit == null)
						continue;
					VarPlayerRepository.create(i, bit, false, false).set(player, value);
				}
				return true;
			}

			case "string": {
				StringBuilder sb = new StringBuilder();
				for (int i = 2; i < args.length; i++)
					sb.append(args[i]).append(" ");
				player.getPacketSender().sendString(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
						sb.toString());
				return true;
			}

			case "strings": {
				int interfaceId = Integer.parseInt(args[0]);
				for (int i = 0; i < InterfaceDef.COUNTS[interfaceId]; i++) {
					player.getPacketSender().sendString(interfaceId, i, "" + i);
					player.getPacketSender().setHidden(interfaceId, i, false);
				}
				return true;
			}

			case "ichide": {
				int parentId = Integer.parseInt(args[0]);
				int minChildId = Integer.parseInt(args[1]);
				int maxChildId = args.length > 2 ? Integer.parseInt(args[2]) : minChildId;
				for (int childId = minChildId; childId <= maxChildId; childId++)
					player.getPacketSender().setHidden(parentId, childId, true);
				return true;
			}

			case "icshow": {
				int parentId = Integer.parseInt(args[0]);
				int minChildId = Integer.parseInt(args[1]);
				int maxChildId = args.length > 2 ? Integer.parseInt(args[2]) : minChildId;
				for (int childId = minChildId; childId <= maxChildId; childId++)
					player.getPacketSender().setHidden(parentId, childId, false);
				return true;
			}

			case "si": {
				int itemId = Integer.parseInt(args[0]);
				SkillDialogue.make(player, new SkillItem(itemId).addReq(p -> false));
				return true;
			}

			case "script": {
				int id = Integer.parseInt(args[0]);
				ScriptDef def = ScriptDef.get(id);
				Object[] params = new Object[args.length - 1];
				System.arraycopy(args, 1, params, 0, params.length);
				for (int i = 0; i < params.length; i++) {
					try {
						var iValue = Integer.parseInt((String) params[i]);
						params[i] = iValue;
					} catch (Exception ignore) {
					}
				}
				if (def == null) {
					System.err.println("Script " + id + " does not exist!");
					return true;
				}
				def.print(System.out);
				player.sendMessage("Sent script: " + id + "( " + Arrays.toString(params) + " )");
				player.getPacketSender().sendClientScript(7927, 1, 1, 1);
				return true;
			}

			case "findintinscript": {
				int search = Integer.parseInt(args[0]);
				for (ScriptDef def : ScriptDef.LOADED) {
					if (def == null)
						continue;
					if (def.intOperands == null)
						continue;
					for (int i : def.intOperands)
						if (i == search)
							System.out.println("Found in " + def.id);
				}
				return true;
			}

			case "findstringinscript": {
				String search = String.join(" ", args).toLowerCase();
				for (ScriptDef def : ScriptDef.LOADED) {
					if (def == null)
						continue;
					if (def.stringOperands == null)
						continue;
					for (String s : def.stringOperands) {
						if (s == null)
							continue;
						if (s.toLowerCase().contains(search))
							System.out.println("Found " + s + " in " + def.id);
					}
				}
				return true;
			}

			case "findvarcinscript": {
				int id = Integer.parseInt(args[0]);
				for (ScriptDef def : ScriptDef.LOADED) {
					if (def == null)
						continue;
					if (def.intOperands == null)
						continue;
					for (int i = 0; i < def.instructions.length; i++) {
						if (def.instructions[i] == 43 && def.intOperands[i] == id) {
							player.sendMessage("Script " + def.id + " sets varc " + id);
						}
					}
				}
				return true;
			}

			/**
			 * Npc commands
			 */
			case "npc": {
				int npcId = Integer.parseInt(args[0]);
				int walkRange = 0;
				if (args.length > 1) {
					walkRange = Integer.parseInt(args[1]);
				}
				NPCType def = NPCType.get(npcId);
				if (def == null) {
					player.sendMessage("Invalid npc id: " + npcId);
					return true;
				}
				var n = new NPC(npcId).spawn(player.getPosition().getX(), player.getPosition().getY(),
						player.getPosition().getZ(), walkRange);
				if (n.getCombat() == null) {
					player.sendMessage("Npc is missing combat definition: " + n.getName());
				} else {
					n.getCombat().setAllowRespawn(false);
				}
				return true;
			}

			case "xnpc": {
				// Validate that there are at least two arguments (npcId and amount)
				if (args.length < 2) {
					player.sendMessage("Usage: xnpc <npcId> <amount>");
					return true;
				}

				// Parse npcId and amount from the command arguments
				int npcId;
				int amount;
				try {
					npcId = Integer.parseInt(args[0]);
					amount = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					player.sendMessage("Invalid npc id or amount. Please enter valid numbers.");
					return true;
				}

				// Check if NPCType with the given ID exists
				NPCType def = NPCType.get(npcId);
				if (def == null) {
					player.sendMessage("Invalid npc id: " + npcId);
					return true;
				}

				// Optional: Check if the amount is reasonable
				if (amount <= 0) {
					player.sendMessage("Amount must be greater than 0.");
					return true;
				}

				// Default walkRange is 0, can be modified if needed
				int walkRange = 0;
				if (args.length > 2) { // Check if there is an additional argument for walkRange
					try {
						walkRange = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						player.sendMessage("Invalid walk range. Using default value: 0.");
					}
				}

				// Spawn the NPCs the specified number of times
				for (int i = 0; i < amount; i++) {
					new NPC(npcId)
							.spawn(player.getPosition().getX(), player.getPosition().getY(),
									player.getPosition().getZ(), walkRange)
							.getCombat()
							.setAllowRespawn(false);
				}

				return true;
			}

			case "calc":
			case "calculate": {
				int id = Integer.parseInt(args[0]);
				NPCType def = NPCType.get(id);
				if (def == null) {
					player.sendMessage("Invalid npc id: " + id);
					return true;
				}
				if (def.lootTable == null) {
					player.sendMessage(def.name + " doesn't have a loot table.");
					return true;
				}
				def.lootTable.calculate(def.name + " Loot Probability Table");
				return true;
			}

			case "ispawn": {
				int id = Integer.parseInt(args[0]);
				ObjType def = ObjType.get(id);
				if (def == null) {
					player.sendMessage("Invalid id!");
					return true;
				}
				System.out.println(
						"  {\"id\": " + id + ", \"x\": " + player.getAbsX() + ", \"y\": " + player.getAbsY()
								+ ", \"z\": " + player.getHeight() + "}, // " + def.name);
				new GroundItem(new Item(id, 1)).position(player.getPosition()).spawnWithRespawn(2);
				return true;
			}

			case "addnpc": { // TODO support more options
				int id = Integer.parseInt(args[0]);
				NPCType def = NPCType.get(id);
				if (def == null) {
					player.sendMessage("Invalid npc id: " + id);
					return true;
				}
				int range = args.length > 1 ? Integer.parseInt(args[1]) : 3;
				System.out.println("{\"id\": " + id + ", \"x\": " + player.getAbsX() + ", \"y\": "
						+ player.getAbsY()
						+ ", \"z\": " + player.getHeight() + ", \"walkRange\": " + range + "}, // " + def.name);
				new NPC(id).spawn(player.getPosition());
				return true;
			}

			case "findspawnednpc": {
				int id = Integer.parseInt(args[0]);
				World.npcsNonNull().forEach(npc -> {
					if (npc.getId() == id) {
						player.sendMessage("Found at " + npc.getPosition());
					}
				});
				return true;
			}

			case "npcanims": {
				int sourceId = Integer.parseInt(args[0]);
				NPCType sourceDef = NPCType.get(sourceId);
				if (sourceDef == null) {
					player.sendMessage("Invalid NPC!");
					return true;
				}
				player.sendMessage(
						"Stand: " + sourceDef.standAnimation + " Walk: " + sourceDef.walkAnimation);
				SortedSet<Integer> results = SeqType.findAnimationsWithSameRigging(sourceDef.walkAnimation,
						sourceDef.standAnimation, sourceDef.walkBackAnimation, sourceDef.walkLeftAnimation,
						sourceDef.walkRightAnimation);
				if (results == null) {
					player.sendMessage("Nothing found!");
					return true;
				}
				System.out.println(Arrays.toString(results.toArray()));
				return true;
			}

			case "similaranims": {
				int sourceId = Integer.parseInt(args[0]);
				SeqType source = SeqType.LOADED[sourceId];
				SortedSet<Integer> results = SeqType.findAnimationsWithSameRigging(sourceId);
				if (results == null) {
					player.sendMessage("Nothing found!");
					return true;
				}
				System.out.println("Same rigging: " + Arrays.toString(results.toArray()));
				results.clear();
				for (int id = 0; id < SeqType.LOADED.length; id++) {
					SeqType def = SeqType.LOADED[id];
					if (def == null || def.frameData == null)
						continue;
					if (def.frameData[0] == source.frameData[0]) { // TODO consider checking other frames and
																													// outputting a %
						// match?
						results.add(id);
					}
				}
				System.out.println("Similar frames: " + Arrays.toString(results.toArray()));
				return true;
			}

			case "reloadnpcs": {
				World.npcsNonNull().forEach(n -> {
					if (n.getCombat() != null)
						n.remove();
				});
				DataFile.reload(player, npc_spawns.class);
				return true;
			}

			case "removelocalnpcs": {
				player.localNpcs().forEach(n -> {
					if (n.getCombat() != null)
						n.remove();
				});
				return true;
			}

			case "reloadobjects": {
				DataFile.reload(player, object_spawns.class);
				return true;
			}

			case "randomitems": {
				List<Item> randomItems = new ArrayList<>();
				RandomUtils insecure = RandomUtils.insecure();
				while (randomItems.size() < 28) {
					Item item = new Item(insecure.randomInt(0, 20000), 1000);
					if (item.getDef().stackable && item.getDef().tradeable && !item.getDef().free) {
						randomItems.add(item);
					}
				}
				randomItems.forEach(player.getInventory()::add);

				return true;
			}

			case "reloaddrops": {
				NPCType.forEach(def -> def.lootTable = null);
				DataFile.reload(player, npc_drops.class);
				return true;
			}

			case "removenpc": {
				int id = Integer.parseInt(args[0]);
				int count = 0;
				for (NPC npc : player.localNpcs()) {
					if (npc.getId() == id && !npc.defaultSpawn) {
						npc.remove();
						count++;
					}
				}
				player.sendMessage("Removed " + count + " NPCs with id " + id + ".");
				return true;
			}

			case "icons": {
				for (int i = Integer.parseInt(args[0]); i < Integer.parseInt(args[1]); i++) {
					player.sendMessage("Icon " + i + " <img=" + i + ">");
				}
				return true;
			}

			case "fnpc": {
				String search = query.substring(5);
				int combat = -1;
				if (search.contains(":")) {
					String[] s = search.split(":");
					search = s[0];
					combat = Integer.parseInt(s[1]);
				}
				for (NPCType def : NPCType.cached.values()) {
					if (def != null && def.name.toLowerCase().contains(search.toLowerCase())
							&& (combat == -1 || def.combatLevel == combat))
						player
								.sendMessage(def.id + " (" + def.name + "): combat=" + def.combatLevel + " options="
										+ Arrays.toString(def.options) + " size=" + def.size);
				}
				return true;
			}

			case "calcmining": {
				Pickaxe pick = Pickaxe.find(player);
				if (pick == null) {
					player.sendMessage("Equip a pickaxe!!");
					return true;
				}
				for (Rock rock : Rock.values()) {
					if (rock.multiOre != null)
						continue;
					double chance = Mining.chance(Mining.getEffectiveLevel(player), rock, pick) / 100;
					double oresPerTick = chance / 2;
					double oresPerHour = oresPerTick * 60 * 100;
					double xpPerHour = oresPerHour * rock.experience * StatType.Mining.defaultXpMultiplier;
					System.out
							.println(rock + ": ores/h=" + NumberUtils.formatNumber((long) oresPerHour) + " xp/h="
									+ NumberUtils.formatNumber((long) xpPerHour) + " chance="
									+ NumberUtils.formatTwoPlaces(chance));
				}
				return true;
			}

			case "sound": {
				int id = Integer.parseInt(args[0]);
				int type = args.length >= 2 ? Integer.parseInt(args[1]) : 1;
				int delay = args.length >= 3 ? Integer.parseInt(args[2]) : 0;
				player.privateSound(id, type, delay);
				return true;
			}

			case "lms": {
				try {
					DynamicMap lmsMap = new DynamicMap()
							.buildSw(13658, 1)
							.buildNw(13659, 1)
							.buildSe(13914, 1)
							.buildNe(13915, 1);
					player.getMovement().teleport(lmsMap.swRegion.baseX, lmsMap.swRegion.baseY, 0);
				} catch (DynamicMap.DynamicMapBuildException e) {
					player.sendMessage("Unable to build dynamic map.");
				}
				return true;
			}

			case "map": {
				player.getPacketSender().sendMapState(Integer.parseInt(args[0]));
				return true;
			}

			case "oldcasket": {
				NPCType def = NPCType.get(Integer.parseInt(args[0]));
				if (def == null) {
					return true;
				}
				double baseChance = 0.3;
				double largeChance = Math.min(def.combatLevel, 100 * 2 * 2) / (250.0 * 2 * 2);
				double medChance = Math.min(def.combatLevel, 100 * 2) / (250.0 * 2) * (1 - largeChance);
				double smallChance = (1 - largeChance) * (1 - medChance);
				largeChance *= baseChance;
				medChance *= baseChance;
				smallChance *= baseChance;
				player.sendMessage("small=" + NumberUtils.formatTwoPlaces(smallChance) + ", medium="
						+ NumberUtils.formatTwoPlaces(medChance) + ", large="
						+ NumberUtils.formatTwoPlaces(largeChance));
				int smallReturn = (int) (((15000 + 35000) / 2) * smallChance);
				int mediumReturn = (int) (((35000 + 5000) / 2) * medChance);
				int largeReturn = (int) (((50000 + 75000) / 2) * largeChance);
				int perKill = (smallReturn + mediumReturn + largeReturn);
				player.sendMessage(
						"smallgp=" + smallReturn + ", medgp=" + mediumReturn + ", largegp=" + largeReturn);
				player.sendMessage(
						"avg/kill=" + perKill + ", with wealth=" + (perKill * 1.5) + ", wealth+wild="
								+ (perKill * 2.25));
				return true;
			}

			case "sproj": {
				new Projectile(Integer.parseInt(args[0]), 60, 60, 0, 300, 20, 55, 64).send(player.getAbsX(),
						player.getAbsY(),
						player.getAbsX() - 5, player.getAbsY());
				return true;
			}

			case "projloop": {
				player.startEvent(event -> {
					int id = 0;
					if (args.length > 0)
						id = Integer.parseInt(args[0]);
					while (id < SpotAnimType.LOADED.length) {
						new Projectile(id, 60, 60, 0, 300, 20, 0, 64).send(player.getAbsX(), player.getAbsY(),
								player.getAbsX() - 5,
								player.getAbsY());
						player.sendMessage("Sending: " + id);
						id++;
						event.delay(1);
					}
				});
				return true;
			}

			case "zulrahdeath": {
				player.getPacketSender().sendItems(-1, 0, 525, new Item(4151, 1));
				Unlock unlock = new Unlock(602, 3, 0, 89);
				unlock.unlockRange(player, 0, 10);
				player.openInterface(ToplevelComponent.MAINMODAL, 602);
				return true;
			}

			case "shake": {
				player.getPacketSender().shakeCamera(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
				return true;
			}

			case "tobparty": {
				TheatrePartyManager.instance().register(player);
				return true;
			}

			case "rune": {
				Rune r = Rune.valueOf(args[0].toUpperCase());
				player.getInventory().add(r.getId(), Integer.MAX_VALUE);
				return true;
			}

			case "hit":
			case "hitme": {
				player.hit(new Hit().fixedDamage(Integer.parseInt(args[0])).delay(0));
				return true;
			}

			case "tutorial": {
				player.newPlayer = true;
				return true;
			}

			case "debug": {
				player.sendMessage("Debug: " + ((player.debug = !player.debug) ? "ON" : "OFF"));
				return true;
			}

			case "xpmode": {
				XpMode mode = XpMode.HARD;
				if (args.length > 0) {
					mode = switch (args[0]) {
						case "hard" -> XpMode.HARD;
						case "medium" -> XpMode.MEDIUM;
						case "easy" -> XpMode.EASY;
						default -> mode;
					};
				}
				XpMode.setXpMode(player, mode);
				player.sendMessage("Your XP mode is now " + player.xpMode.getName() + ". Combat rate: "
						+ player.xpMode.getCombatRate() + "x. Skilling rate: " + player.xpMode.getSkillRate()
						+ "x.");
				return true;
			}

			case "objanim": {
				int id = Integer.parseInt(args[0]);
				LocType def = LocType.get(id);
				if (def == null) {
					player.sendMessage("Invalid id.");
					return true;
				}
				player.sendMessage("Object uses animation " + def.animationID);
				return true;
			}

			case "animateobj": {
				Tile.getObject(-1, player.getAbsX(), player.getAbsY(), player.getHeight(), 10, -1)
						.animate(Integer.parseInt(args[0]));
				return true;
			}

			case "kill": {
				forPlayer(player, query, "::kill playerName",
						p2 -> p2.hit(new Hit().fixedDamage(p2.getHp())));
				return true;

			}

			case "killnpcs": {
				for (NPC npc : player.localNpcs()) {
					if (npc.getCombat() == null)
						continue;
					if (player.getCombat().canAttack(npc, true)) {
						npc.hit(new Hit(player).fixedDamage(npc.getHp()).delay(0));
					}
				}
				return true;
			}

			case "killplayers": {
				for (Player localPlayer : player.localPlayers()) {
					if (localPlayer.getCombat() == null)
						continue;
					if (player.getCombat().canAttack(localPlayer, true)) {
						localPlayer.hit(new Hit(player).fixedDamage(localPlayer.getHp()).delay(0));
					}
				}
				return true;
			}

			case "pvpmagicaccuracy": {
				Hit.PVP_MAGIC_ACCURACY_MODIFIER = Double.valueOf(args[0]);
				player
						.sendMessage("PVP_MAGIC_ACCURACY_MODIFIER = " + Hit.PVP_MAGIC_ACCURACY_MODIFIER + ";");
				return true;
			}

			case "pvpmeleeaccuracy": {
				Hit.PVP_MELEE_ACCURACY_MODIFIER = Double.valueOf(args[0]);
				player
						.sendMessage("PVP_MELEE_ACCURACY_MODIFIER = " + Hit.PVP_MELEE_ACCURACY_MODIFIER + ";");
				return true;
			}

			case "settask2": {
				int[] ids = NumberUtils.toIntArray(args[0]);
				int amount = args.length > 1 ? NumberUtils.intValue(args[1]) : 1;
				for (int id : ids) {
					if (id != -1)
						VarPlayerRepository.SLAYER_TASK.set(player, id);
					VarPlayerRepository.SLAYER_TASK_AMOUNT.set(player, amount);
				}
				return true;
			}

			case "house": {
				player.house = new House();
				return true;
			}

			case "conenum": {
				int id = Integer.parseInt(args[0]);
				for (int i = 0; i < Server.fileStore.get(2).getValidFilesCount(8); i++) {
					EnumType map = EnumType.get(i);
					if ((map.getIntValues().containsValue(id) || map.getIntValues().containsKey(id))) {
						player.sendMessage("Found in enum " + i);
					}
				}
				return true;
			}

			case "construct": {
				int id = Integer.parseInt(args[0]);
				for (int i = 0; i < Server.fileStore.get(2).getValidFilesCount(34); i++) {
					StructType map = StructType.get(i);
					if (map.getParams().containsKey(id) || map.getParams().containsValue(id)) {
						player.sendMessage("Found in struct " + i);
					}
				}
				return true;
			}

			case "enum": {
				EnumType map = EnumType.get(Integer.parseInt(args[0]));
				System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(map));
				return true;
			}

			case "struct": {
				StructType map = StructType.get(Integer.parseInt(args[0]));
				System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(map));
				return true;
			}

			case "param": {
				ParamType map = ParamType.get(Integer.parseInt(args[0]));
				System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(map));
				return true;
			}

			case "setmemdays": {
				int mem = Integer.parseInt(args[0]);
				VarPlayerRepository.MEMBERSHIP_DAYS.set(player, mem);
				player.sendMessage("Membership days set to " + mem);
				return true;
			}

			case "housestyle": {
				player.house.setStyle(HouseStyle.valueOf(String.join("_", args).toUpperCase()));
				player.sendMessage("set!");
				return true;
			}

			case "resethouse": {
				player.house = new House();
				player.sendMessage("House reset");
				return true;
			}

			case "refreshregion": {
				player.getUpdater().updateRegion = true;
				return true;
			}

			case "testbuild": {
				Buildable[] objs = new Buildable[] {
						Buildable.CRUDE_WOODEN_CHAIR,
						Buildable.CRUDE_WOODEN_CHAIR,
						Buildable.CRUDE_WOODEN_CHAIR,
						Buildable.CRUDE_WOODEN_CHAIR,
						Buildable.CRUDE_WOODEN_CHAIR,
						Buildable.CRUDE_WOODEN_CHAIR
				};
				int count = 1;
				for (Buildable b : objs) {
					player.getPacketSender().sendClientScript(1404, "iiisi", count++, b.getItemId(),
							b.getLevelReq(),
							b.getCreationMenuString(), b.hasLevelAndMaterials(player) ? 1 : 0);
				}
				player.getPacketSender().sendClientScript(1406, "ii", count - 1, 0);
				player.openInterface(ToplevelComponent.MAINMODAL,
						Interface.CONSTRUCTION_FURNITURE_CREATION);
				return true;
			}

			case "materials": { // spawns materials for the given object
				Buildable b = Buildable.valueOf(String.join("_", args).toUpperCase());
				b.getMaterials().forEach(player.getInventory()::addOrDrop);
				return true;
			}

			case "allmats": { // spawns materials for all objects in the given room type (last entry in
												// the
				// list, typically the highest level object)
				RoomDefinition def = RoomDefinition.valueOf(String.join("_", args).toUpperCase());
				for (Hotspot hotspot : def.getHotspots()) {
					if (hotspot != Hotspot.EMPTY)
						hotspot.getBuildables()[hotspot.getBuildables().length - 1].getMaterials()
								.forEach(player.getInventory()::addOrDrop);
				}
				return true;
			}

			case "conobj": {
				int id = Integer.parseInt(args[0]);
				ObjType itemDef = ObjType.get(id);
				player.sendMessage("Looking for objects for item " + itemDef.id + "...");
				List<LocType> found = LocType.LOADED.values().stream()
						.filter(objectDef -> objectDef != null && objectDef.modelIds != null
								&& Arrays.stream(objectDef.modelIds)
										.anyMatch(model -> model == itemDef.inventoryModel))
						.collect(Collectors.toList());
				if (found.size() == 0) {
					player.sendMessage("No matches!");
				} else {
					found.forEach(def -> {
						player.sendMessage("Object[" + def.id + "]: \"" + def.name + "\"; Options="
								+ Arrays.toString(def.options));
					});
				}
				return true;
			}

			case "fconobj": {
				String name = String.join(" ", args).toLowerCase();
				LocType.forEach(def -> {
					if (def != null && def.name != null && def.name.toLowerCase().contains(name)
							&& def.options != null
							&& def.options.length >= 5 && def.options[4] != null
							&& def.options[4].equalsIgnoreCase("remove")) {
						player.sendMessage(def.id + " - " + def.name + " " + Arrays.toString(def.options));
					}
				});
				return true;
			}

			case "fillcostumeroom": {
				int count = 0;
				for (CostumeStorage s : CostumeStorage.VALUES) {
					s.getSets(player).clear();
				}
				for (CostumeStorage s : CostumeStorage.VALUES) {
					Map<Costume, int[]> stored = s.getSets(player);
					for (Costume costume : s.getCostumes()) {
						if (stored.get(costume) != null)
							continue;
						int[] set = new int[costume.getPieces().length];
						for (int i = 0; i < costume.getPieces().length; i++) {
							set[i] = costume.getPieces()[i][0];
						}
						stored.put(costume, set);
						count++;
					}
				}
				player.sendMessage("Added " + count + " sets.");
				return true;
			}

			case "dailyreset": {
				player.dailyReset();
				return true;
			}

			case "givebank": {
				forPlayer(player, query, "::givebank username", p2 -> {
					for (Item item : DevKitHandler.DEV_KIT) {
						p2.getBank().add(item.getId(), item.getAmount());
					}
					p2.sendMessage("You have been given the Dev kit that contains all items");
					player.sendMessage("You have given user " + p2.getName() + " The Dev Kit!");
				});
				return true;
			}

			case "controlnpc": {
				if (args == null || args.length == 0) {
					player.remove("CONTROLLING_NPC");
					player.sendMessage("NPC control cleared.");
					return true;
				} else {
					int index = Integer.parseInt(args[0]);
					NPC npc = World.getNpc(index);
					if (npc == null) {
						player.sendMessage("Invalid NPC. Use index");
						return true;
					} else {
						player.set("CONTROLLING_NPC", npc);
						player.sendMessage("You're now controlling " + npc.getDef().name + ".");
					}
					return true;
				}
			}

			case "demote": {
				forPlayer(player, query, "::demote playerName", p2 -> {
					// remove existing groups ?
					Arrays.fill(p2.getGroups(), false);
					// re add their reg rank
					p2.join(PlayerGroup.REGISTERED);
					p2.setPrimaryGroup(PlayerGroup.REGISTERED);
					player.sendMessage("You have demoted " + p2.getName() + ".");
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR,
							"<col=000000>" + p2.getName(),
							" has just been demoted!");
					CommandHandler.logToDiscord(player, command, args);
				});
				return true;
			}

			case "summerpoints": {
				player.summerPoints += 10_000;
				player.sendMessage("You have received 10,000 Summer Points!");
				return true;
			}

			case "softdemote": {
				forPlayer(player, query, "::softdemote playerName", p2 -> {
					// remove existing groups ?
					Arrays.fill(p2.getGroups(), false);
					// re add their reg rank
					p2.join(PlayerGroup.REGISTERED);
					p2.setPrimaryGroup(PlayerGroup.REGISTERED);
					player.sendMessage("You have demoted " + p2.getName() + ".");
					CommandHandler.logToDiscord(player, command, args);
				});
				return true;
			}

			case "givedev": {
				forPlayer(player, query, "::givedev playerName", p2 -> {
					p2.join(DEVELOPER);
					player.sendMessage("Gave Developer to " + p2.getName() + ".");
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR,
							"<col=000000>" + p2.getName(),
							" has just been promoted to Developer!");
				});
				return true;
			}

			case "giveheadadmin": {
				forPlayer(player, query, "::giveheadadmin playerName", p2 -> {
					p2.join(PlayerGroup.ADMINISTRATOR);
					player.sendMessage("Gave Head Admin to " + p2.getName() + ".");
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR,
							"<col=000000>" + p2.getName(),
							" has just been promoted to Head Admin!");
				});
				return true;
			}

			case "giveowner": {
				forPlayer(player, query, "::giveowner playerName", p2 -> {
					p2.join(PlayerGroup.OWNER);
					player.sendMessage("Gave Owner to " + p2.getName() + ".");
				});
				return true;
			}

			case "testinter": {
				player.openInterface(ToplevelComponent.MAINMODAL, 718);

				// Delay the model information to ensure interface is fully loaded
				player.getPacketSender().sendModel(-1, 87, 38615);
				player.getPacketSender().sendModelInformation(-1, 87, 1000, 0, 0);
				return true;
			}

			case "testitem": {
				Item item = new Item(4151, 1);
				item.putAttributes(AttributeExtensions.attributeMapTypes(
						new AttributePair<>(AttributeTypes.UPGRADE_2, ItemEffect.RECOIL),
						new AttributePair<>(AttributeTypes.UPGRADE_3, ItemEffect.SPITEFUL)));
				player.getInventory().add(item);
				return true;
			}

			case "setstr": {
				int str = Integer.parseInt(args[0]);
				player.setStrAdder(str);
				player.sendMessage("Your strength adder is now: " + player.getStrAdder());
				return true;
			}

			case "storedon": {
				forPlayerInt(player, query, "::givedon playerName amount", (p2, amount) -> {
					p2.storeAmountSpent += amount;
					player.sendMessage("Gave $" + amount + " store amount to " + p2.getName() + ".");
				});
				return true;
			}

			case "giveyt": {
				forPlayer(player, query, "::giveyt playerName", p2 -> {
					p2.join(PlayerGroup.YOUTUBER);
					player.sendMessage("Gave YouTuber to " + p2.getName() + ".");
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR,
							"<col=000000>" + p2.getName(),
							" has just been given the youtuber rank!");
				});
				return true;
			}

			case "givesupport": {
				forPlayer(player, query, "::givesupport playerName", p2 -> {
					p2.join(PlayerGroup.SUPPORT);
					player.sendMessage("Gave Support Role to " + p2.getName() + ".");
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR,
							"<col=000000>" + p2.getName(),
							" has just been promoted to Server Support!");
				});
				return true;
			}

			case "givemod": {
				forPlayer(player, query, "::givemod playerName", p2 -> {
					p2.join(MODERATOR);
					player.sendMessage("Gave Moderator to " + p2.getName() + ".");
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR,
							"<col=000000>" + p2.getName(),
							" has just been promoted to Moderator!");
				});
				return true;
			}
			case "giveplat": {
				forPlayer(player, query, "::giveplat playerName", p2 -> {
					p2.join(SecondaryGroup.PLATINUM_DONATOR);
					player.sendMessage("Gave Plat Dono to " + p2.getName());
				});
				return true;
			}
			case "giveheadmod": {
				forPlayer(player, query, "::giveheadmod playerName", p2 -> {
					p2.join(HEAD_MODERATOR);
					player.sendMessage("Gave Head Mod to " + p2.getName() + ".");
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR,
							"<col=000000>" + p2.getName(),
							" has just been promoted to Head Mod!");
				});
				return true;
			}

			case "givesecondarygroup": {
				var group = SecondaryGroup.valueOf(args[0].toUpperCase());
				player.setSecondaryGroups(List.of(group.id));
				player.sendMessage("secondary_group: " + player.getSecondaryGroup() + " " + group.tag());
				return true;
			}

			case "giveadmin": { // community admin
				forPlayer(player, query, "::giveadmin playerName", p2 -> {
					p2.join(PlayerGroup.COMMUNITY_ADMIN);
					player.sendMessage("Gave Player Admin to " + p2.getName() + ".");
					Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR,
							"<col=000000>" + p2.getName(),
							" has just been promoted to an Administrator!");
				});
				return true;
			}

			case "heal": {
				player.hit(new Hit(HitType.HEAL).fixedDamage(99));
				return true;
			}

			case "maxpray": {
				Stat prayer = player.getStats().get(StatType.Prayer);
				if (prayer.currentLevel > 99)
					prayer.alter(prayer.fixedLevel);
				else
					prayer.alter(30000);
				return true;
			}

			case "obj": {
				int id = Integer.valueOf(args[0]);
				int type = 10;
				if (args.length > 1)
					type = Integer.valueOf(args[1]);
				int direction = 0;
				if (args.length > 2)
					direction = Integer.valueOf(args[2]);
				player.getPacketSender().sendCreateObject(id, player.getAbsX(), player.getAbsY(),
						player.getHeight(), type,
						direction);
				player.sendMessage(
						"spawned fake " + id + " " + LocType.get(id).name + " (use ::addobj to interact)");
				return true;
			}

			case "god": {
				if (!player.isInvincible()) {
					player.setInvincible(true);
					player.sendMessage("You have enabled God Mode");
				} else {
					player.setInvincible(false);
					player.sendMessage("You have disabled God Mode");
				}
				return true;
			}

			case "giveperkpoints": {
				forPlayer(player, query, "::giveperkpoints playerName",
						p2 -> p2.perkPoints += args[0] == null ? 10_000 : Integer.parseInt(args[0]));
				return true;
			}

			case "searchi":
			case "searchitem":
			case "finditem":
			case "fi":
			case "fitem": {
				handleFindItem(player, query, command, args);
				return true;
			}

			case "item":
			case "pickup": {
				int[] ids = NumberUtils.toIntArray(args[0]);
				int amount = args.length > 1 ? NumberUtils.intValue(args[1]) : 1;
				int charges = args.length > 2 ? NumberUtils.intValue(args[2]) : 0;
				for (int id : ids) {
					if (id != -1) {
						var item = new Item(id, amount);
						if (charges > 0) {
							AttributeExtensions.setCharges(item, charges);
							player.sendMessage("Your %s now has %s charges"
									.formatted(item.getDef().getName(), Utils.formatMoneyString(charges)));
						}
						player.getInventory().add(item);
					}
				}
				return true;
			}

			case "afkcheck": {
				forPlayer(player, query, "::afkcheck playerName",
						p2 -> new SecurityGuard().spawnHonourGuard(p2));
				return true;
			}

			case "checkuserbank": {
				forPlayer(player, query, "::checkbank name", (target) -> {
					player.dialogue(new OptionsDialogue("Viewing a players bank will clear yours.",
							new Option("View " + target.getName() + " bank.", () -> {
								Bank targetBank = target.getBank();
								player.getBank().clear();
								for (BankItem item : targetBank.getItems()) {
									if (item == null)
										continue;
									player.getBank().add(item);
								}
								player.getBank().open();
							}),
							new Option("No, thanks.")));
				});
				return true;
			}

			case "checkuserinv": {
				forPlayer(player, query, "::checkinventory name", (target) -> {
					player.dialogue(new OptionsDialogue("Viewing a players inventory will clear yours.",
							new Option("View " + target.getName() + " inventory.", () -> {
								Inventory inventory = target.getInventory();
								player.getInventory().clear();
								for (Item item : inventory.getItems()) {
									if (item == null)
										continue;
									player.getInventory().add(item);
								}
							}),
							new Option("No, thanks.")));
				});
				return true;
			}

			case "checkequipment": {
				forPlayer(player, query, "::checkequipment name", (target) -> {
					player.dialogue(new OptionsDialogue("Viewing a players equipment will clear yours.",
							new Option("View " + target.getName() + " equipment.", () -> {
								Equipment equipment = target.getEquipment();
								player.getEquipment().clear();
								for (Item item : equipment.getItems()) {
									if (item == null)
										continue;
									player.getEquipment().equipQueue(item.copy());
								}
							}),
							new Option("No, thanks.")));
				});
				return true;
			}

			case "runes": {
				player.getInventory().add(new Item(MIST_RUNE, 25000));
				player.getInventory().add(new Item(MIND_RUNE, 25000));
				player.getInventory().add(new Item(LAVA_RUNE, 25000));
				player.getInventory().add(new Item(BODY_RUNE, 25000));
				player.getInventory().add(new Item(COSMIC_RUNE, 25000));
				player.getInventory().add(new Item(CHAOS_RUNE, 25000));
				player.getInventory().add(new Item(NATURE_RUNE, 25000));
				player.getInventory().add(new Item(LAW_RUNE, 25000));
				player.getInventory().add(new Item(DEATH_RUNE, 25000));
				player.getInventory().add(new Item(BLOOD_RUNE, 25000));
				player.getInventory().add(new Item(ASTRAL_RUNE, 25000));
				player.getInventory().add(new Item(SOUL_RUNE, 25000));
				player.getInventory().add(new Item(WRATH_RUNE, 25000));
				CommandHandler.logToDiscord(player, command, args);
				return true;
			}

			case "mactest": {
				Process p = Runtime.getRuntime().exec(new String[] {
						"getmac",
						"/fo",
						"csv",
						"/nh"
				});
				java.io.BufferedReader in =
						new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
				String line;
				line = in.readLine();
				String[] result = line.split(",");

				System.out.println(result[0].replace('"', ' ').trim());
				return true;
			}

			case "dropbonus": {
				int droprate = Integer.parseInt(args[0]);
				player.modifiedDropBonus = droprate;
				player.sendMessage("Your drop bonus has been modified");
				return true;
			}

			case "dropcheck": {
				for (int x = 0; x < 64; x++) {
					for (int y = 0; y < 64; y++) {
						var region = player.getPosition().getRegionId();
						var regionX = region >> 8;
						var regionY = region & 0xff;
						regionX *= 64;
						regionY *= 64;
						new GroundItem(new Item(995))
								.position(regionX + x, regionY + y, player.getPosition().z()).spawn(0);
					}
				}
				return true;
			}

			case "setlvls":
			case "setlvl":
			case "setlevels":
			case "setlevel": {
				int id = Integer.parseInt(args[0]);
				int level = Integer.parseInt(args[1]);
				player.getStats().set(StatType.VALUES[id], level);
				return true;
			}

			case "dharok": {
				player.getEquipment().set(Equipment.SLOT_WEAPON, new Item(26219));
				player.getEquipment().set(Equipment.SLOT_CHEST, new Item(DHAROKS_PLATEBODY));
				player.getEquipment().set(Equipment.SLOT_LEGS, new Item(DHAROKS_PLATELEGS));
				player.getEquipment().set(Equipment.SLOT_HAT, new Item(DHAROKS_HELM));
				player.getEquipment().set(Equipment.SLOT_AMULET, new Item(30507));
				player.getEquipment().set(Equipment.SLOT_CAPE, new Item(21295));
				player.getEquipment().set(Equipment.SLOT_HANDS, new Item(30509));
				player.getEquipment().set(Equipment.SLOT_FEET, new Item(30520));
				player.getEquipment().set(Equipment.SLOT_RING, new Item(30516));
				player.getEquipment().set(Equipment.SLOT_SHIELD, new Item(30514));
				player.getEquipment().update(Equipment.SLOT_SHIELD);
				player.getEquipment().update(Equipment.SLOT_RING);
				player.getEquipment().update(Equipment.SLOT_FEET);
				player.getEquipment().update(Equipment.SLOT_HANDS);
				player.getEquipment().update(Equipment.SLOT_CAPE);
				player.getEquipment().update(Equipment.SLOT_AMULET);
				player.getEquipment().update(Equipment.SLOT_HAT);
				player.getEquipment().update(Equipment.SLOT_LEGS);
				player.getEquipment().update(Equipment.SLOT_CHEST);
				player.getEquipment().update(Equipment.SLOT_WEAPON);
				player.getInventory().clear();
				player.getInventory().set(0, new Item(30380));
				player.getInventory().set(1, new Item(3024));
				player.getInventory().set(2, new Item(3024));
				player.getInventory().set(3, new Item(6685));
				player.getInventory().set(4, new Item(13441));
				player.getInventory().set(5, new Item(13441));
				player.getInventory().set(6, new Item(3144));
				player.getInventory().set(7, new Item(13441));
				player.getInventory().set(8, new Item(13441));
				player.getInventory().set(9, new Item(13441));
				player.getInventory().set(10, new Item(3144));
				player.getInventory().set(11, new Item(13441));
				player.getInventory().set(12, new Item(13441));
				player.getInventory().set(13, new Item(13441));
				player.getInventory().set(14, new Item(3144));
				player.getInventory().set(15, new Item(13441));
				player.getInventory().set(16, new Item(13441));
				player.getInventory().set(17, new Item(13441));
				player.getInventory().set(18, new Item(3144));
				player.getInventory().set(19, new Item(13441));
				player.getInventory().set(20, new Item(13652));
				player.getInventory().set(21, new Item(13441));
				player.getInventory().set(22, new Item(3144));
				player.getInventory().set(23, new Item(13441));
				player.getInventory().set(24, new Item(4718));
				player.getInventory().set(25, new Item(9075, 30000));
				player.getInventory().set(26, new Item(557, 30000));
				player.getInventory().set(27, new Item(560, 30000));
				for (int i = 0; i < 28; i++) {
					player.getInventory().update(i);
				}
				HomeHandler.switchBook(player, SpellBook.LUNAR, false);
				return true;
			}
			case "bots": {
				for (var i = 0; i < 100; ++i) {
					var name = "b" + i;
					var loginRequest = new PlayerLogin(
							new LoginInfo(0L, "127.0.0.1", name, "l", "", null, "0", "0", 0, false, 0), null);
					PlayerLoginWorker.queue(loginRequest);
				}
				return true;
			}

			case "giveupgrades": {
				for (ItemUpgrading upgrade : ItemUpgrading.VALUES) {
					player.getInventory().add(upgrade.upgradeId, 1);
				}
				return true;
			}

			case "givebreakables": {
				for (ItemBreaking breaking : ItemBreaking.VALUES) {
					player.getInventory().add(breaking.fixedId, 1);
				}
				return true;
			}

			case "update": {
				World.update(Integer.valueOf(args[0]));
				return true;
			}

			case "update_now": {
				World.update(-1);
				return true;
			}

			case "master": {
				int xp = Stat.xpForLevel(99);
				for (int i = 0; i < StatType.VALUES.length; i++) {
					Stat stat = player.getStats().get(i);
					stat.currentLevel = stat.fixedLevel = 99;
					stat.experience = Double.MAX_VALUE;
					stat.updated = true;
				}

				player.getCombat().updateLevel();
				player.getAppearance().update();
				return true;
			}

			case "lvl": {
				StatType type = StatType.get(args[0]);
				int id = type == null ? Integer.valueOf(args[0]) : type.ordinal();
				int level = Integer.valueOf(args[1]);
				if (level < 1 || level > 255 || (id == 3 && level < 10)) {
					player.sendMessage("Invalid level!");
					return true;
				}
				Stat stat = player.getStats().get(id);
				stat.currentLevel = level;
				stat.fixedLevel = Math.min(99, level);
				stat.experience = Stat.xpForLevel(Math.min(99, level));
				stat.updated = true;

				if (id == 5)
					player.getPrayer().deactivateAll();

				player.getCombat().updateLevel();
				return true;
			}

			case "copyinv": {
				String name = query.substring(query.indexOf(" ") + 1);
				Player p2 = World.getPlayer(name);
				if (p2 == null) {
					player.sendMessage(name + " could not be found.");
					return true;
				}
				for (int slot = 0; slot < player.getInventory().getItems().length; slot++) {
					Item item = p2.getInventory().get(slot);
					if (item == null)
						player.getInventory().set(slot, null);
					else
						player.getInventory().set(slot, item.copy());
				}
				player.sendMessage("You have copied " + name + "'s inventory.");
				return true;
			}

			case "copyarm": {
				String name = query.substring(query.indexOf(" ") + 1);
				Player p2 = World.getPlayer(name);
				if (p2 == null) {
					player.sendMessage(name + " could not be found.");
					return true;
				}
				for (int slot = 0; slot < player.getEquipment().getItems().length; slot++) {
					Item item = p2.getEquipment().get(slot);
					if (item == null)
						player.getEquipment().set(slot, null);
					else
						player.getEquipment().set(slot, item.copy());
				}
				player.getAppearance().update();
				player.sendMessage("You have copied " + name + "'s armor.");
				return true;
			}

			case "copystats": {
				String name = query.substring(query.indexOf(" ") + 1);
				Player p2 = World.getPlayer(name);
				if (p2 == null) {
					player.sendMessage(name + " could not be found.");
					return true;
				}
				for (int statId = 0; statId < StatType.VALUES.length; statId++) {
					Stat stat = player.getStats().get(statId);
					Stat stat2 = p2.getStats().get(statId);
					stat.currentLevel = stat2.currentLevel;
					stat.fixedLevel = stat2.fixedLevel;
					stat.experience = stat2.experience;
					stat.updated = true;
				}
				player.getCombat().updateLevel();
				player.getAppearance().update();
				player.sendMessage("You have copied " + name + "'s stats.");
				return true;
			}

			case "copybank": {
				String name = query.substring(query.indexOf(" ") + 1);
				Player p2 = World.getPlayer(name);
				if (p2 == null) {
					player.sendMessage(name + " could not be found.");
					return true;
				}
				for (int slot = 0; slot < player.getBank().getItems().length; slot++) {
					BankItem item = p2.getBank().getItems()[slot];
					if (item == null)
						player.getBank().set(slot, null);
					else
						player.getBank().set(slot, item.copy());
				}
				player.sendMessage("You have copied " + name + "'s bank.");
				return true;
			}

			case "ix": {
				int increment = Integer.parseInt(args[0]);
				int x = player.getPosition().getX() + increment;
				int y = player.getPosition().getY();
				int z = player.getPosition().getZ();
				player.getMovement().teleport(x, y, z);
				return true;
			}

			case "iy": {
				int increment = Integer.parseInt(args[0]);
				int x = player.getPosition().getX();
				int y = player.getPosition().getY() + increment;
				int z = player.getPosition().getZ();
				player.getMovement().teleport(x, y, z);
				return true;
			}

			case "iz": {
				int increment = Integer.parseInt(args[0]);
				int x = player.getPosition().getX();
				int y = player.getPosition().getY();
				int z = player.getPosition().getZ() + increment;
				player.getMovement().teleport(x, y, z);
				return true;
			}

			/**
			 * Object commands
			 */
			case "loc": {
				int id = Integer.parseInt(args[0]);
				int type = 10;
				if (args.length > 1)
					type = Integer.parseInt(args[1]);
				int direction = 0;
				if (args.length > 2)
					direction = Integer.parseInt(args[2]);
				player.getPacketSender().sendCreateObject(id, player.getAbsX(), player.getAbsY(),
						player.getHeight(), type,
						direction);
				return true;
			}

			case "addloc": {
				int id = Integer.parseInt(args[0]);
				int type = 10;
				if (args.length > 1)
					type = Integer.parseInt(args[1]);
				int direction = 0;
				if (args.length > 2)
					direction = Integer.parseInt(args[2]);
				GameObject.spawn(id, player.getPosition(), type, direction);
				player.sendMessage(
						"spawned real " + id + " " + LocType.get(id).name + " (use ::obj to add fake)");
				return true;
			}

			case "locs": {
				Tile tile = Tile.get(player.getAbsX(), player.getAbsY(), player.getHeight(), false);
				if (tile == null || tile.gameObjects == null) {
					player.sendMessage("No locations.");
					return true;
				}
				if (tile.gameObjects.isEmpty()) {
					player.sendMessage("No locations?");
					return true;
				}
				tile.checkActive();
				player.sendMessage("Tile active: " + tile.isActive());
				for (GameObject object : tile.gameObjects) {
					int varpId;
					int varpbitId;
					if (object.getId() == -1) {
						varpId = -1;
						varpbitId = -1;
					} else {
						varpId = object.getDef().varpId;
						varpbitId = object.getDef().varpBitId;
					}
					player.sendMessage(
							"id=" + object.getId() + "  x=" + object.x + "  y=" + object.y + "  z=" + object.z
									+ "  type=" + object.getType() + "  dir=" + object.getDirection() + " varpbitId="
									+ varpbitId + " varpId="
									+ varpId + " clipType=" + object.getDef().clipType);
					// System.out.println("{" + object.id + ", " + object.x + ", " + object.y + ", "
					// + object.z + ", " + object.type + ", " + object.direction + "},");
					System.out.println(
							"loc(" + object.getId() + ", " + object.x + ", " + object.y + ", " + object.z + ", "
									+ object.getType() + ", " + object.getDirection() + ").remove();");
				}
				return true;
			}

			case "floc": {
				String search = query.substring(5);
				int number = -1;
				try {
					number = Integer.parseInt(search);
				} catch (Exception e) {

				}
				int finalNumber = number;
				LocType.forEach(def -> {
					if (def != null && def.name != null && def.name.toLowerCase().contains(search)) {
						player.sendMessage(
								def.id + " (" + def.name + ") options=" + Arrays.toString(def.options));
						System.out
								.println(def.id + " (" + def.name + ") options=" + Arrays.toString(def.options));
					}
					if (finalNumber != -1 && def != null && def.animationID == finalNumber)
						player.sendMessage(def.id + " uses anim " + search);
					if (finalNumber != -1 && def.modelIds != null
							&& Arrays.stream(def.modelIds).anyMatch(i -> finalNumber == i))
						player.sendMessage(def.id + " uses model " + search);
				});
				return true;
			}

			case "findinregion": {
				int id = Integer.parseInt(args[0]);
				for (Region region : player.getRegions())
					for (int x = 0; x < 64; x++)
						for (int y = 0; y < 64; y++)
							for (int z = 0; z < 4; z++) {
								Tile t = region.getTile(region.baseX + x, region.baseY + y, z, false);
								if (t == null)
									continue;
								if (t.gameObjects != null
										&& t.gameObjects.stream().anyMatch(o -> o.getId() == id)) {
									player.sendMessage(
											"Found at " + (region.baseX + x) + "," + (region.baseY + y) + "," + z);
								}
								if (t.gameObjects != null && t.gameObjects.stream().anyMatch(o -> o.getId() != -1
										&& o.getDef().showIds != null
										&& Arrays.stream(o.getDef().showIds).anyMatch(i -> i == id))) {
									player.sendMessage(
											"Found <col=ff0000>in container</col> at " + (region.baseX + x) + ","
													+ (region.baseY + y) + "," + z);
								}
							}
				return true;
			}

			case "maxplayer": {
				String name = query.substring(command.length() + 1);
				Player p2 = World.getPlayer(name);
				if (p2 == null)
					player.sendMessage("Could not find player: " + name);
				int xp = Stat.xpForLevel(99);
				for (int i = 0; i < StatType.VALUES.length; i++) {
					Stat stat = p2.getStats().get(i);
					stat.currentLevel = stat.fixedLevel = 99;
					stat.experience = xp;
					stat.updated = true;
				}
				p2.getCombat().updateLevel();
				p2.getAppearance().update();
				player.sendMessage("Maxed player: " + p2.getName());
				return true;
			}

			case "containerobjs": {
				LocType def = LocType.get(Integer.parseInt(args[0]));
				if (def == null)
					return true;
				for (int i = 0; i < def.showIds.length; i++) {
					int id = def.showIds[i];
					LocType obj = LocType.get(id);
					if (obj == null)
						continue;
					System.out.println("[" + i + "]: \"" + obj.name + "\" #" + id + "; options="
							+ Arrays.toString(obj.options));
				}
				return true;
			}

			case "poison": {
				player.poison(6);
				return true;
			}
			case "anim":
			case "emote": {
				int id = Integer.parseInt(args[0]);
				// if(id != -1 && AnimationDefinition.get(id) == null) {
				// player.sendMessage("Invalid Animation: " + id);
				// return true;
				// }
				int delay = 0;
				if (args.length > 1)
					delay = Integer.parseInt(args[1]);
				player.animate(id, delay);
				return true;
			}

			case "animloop": {
				player.startEvent(event -> {
					int id = 0;
					if (args.length > 0)
						id = Integer.parseInt(args[0]);
					while (id < SeqType.LOADED.length) {
						player.animate(id);
						player.sendMessage("Sending: " + id);
						id++;
						event.delay(2);
						player.resetAnimation();
						event.delay(1);
					}
				});
				return true;
			}

			case "gfxtest": {
				int start = Integer.parseInt(args[0]);
				int end = Integer.parseInt(args[1]);
				player.startEvent(e -> {
					for (int i = start; i < end; i++) {// 1390 fungi mush
						int height = 0;
						int delay = 0;
						World.sendGraphics(i, height, delay, player.getPosition());
						System.out.println("id is " + i);
						e.delay(3);
					}
				});
				return true;
			}

			case "sgfx":
			case "gfx":
			case "graphics": {
				int id = Integer.parseInt(args[0]);
				// if(id != -1 && GfxDefinition.get(id) == null) {
				// player.sendMessage("Invalid Graphics: " + id + ". max valid: " +
				// (GfxDefinition.LOADED.length - 1));
				// return true;
				// }
				int height = 0;
				if (args.length > 2)
					height = Integer.parseInt(args[1]);
				int delay = 0;
				if (args.length > 1)
					delay = Integer.parseInt(args[2]);
				if (command.startsWith("s"))
					World.sendGraphics(id, height, delay, player.getPosition());
				else
					player.graphics(id, height, delay);
				return true;
			}

			case "iteminfo": {
				ObjType def = ObjType.get(Integer.parseInt(args[0]));
				if (def == null) {
					player.sendMessage("Invalid id!");
					return true;
				}
				player.sendMessage("inventory=" + def.inventoryModel);
				player.sendMessage("origcolors=" + Arrays.toString(def.colorFind));
				player.sendMessage("replacecolors=" + Arrays.toString(def.colorReplace));
				player.sendMessage("model=" + def.anInt1504);
				return true;
			}

			case "gfxanim": {
				SpotAnimType def = SpotAnimType.get(Integer.parseInt(args[0]));
				if (def == null) {
					player.sendMessage("Invalid id.");
					return true;
				}
				player.sendMessage("Gfx " + def.id + " uses animation " + def.animationId);
				return true;
			}

			case "gfxmodel": {
				SpotAnimType def = SpotAnimType.get(Integer.parseInt(args[0]));
				if (def == null) {
					player.sendMessage("Invalid id.");
					return true;
				}
				player.sendMessage("Gfx " + def.id + " uses model " + def.modelId);
				return true;
			}

			case "findgfxa": {
				int animId = Integer.parseInt(args[0]);
				player.sendMessage("Finding gfx using anim " + animId + "...");
				Arrays.stream(SpotAnimType.LOADED)
						.filter(Objects::nonNull)
						.filter(def -> def.animationId == animId)
						.forEachOrdered(def -> player.sendMessage("Found: " + def.id));
				return true;
			}

			case "findgfxm": {
				int model = Integer.parseInt(args[0]);
				player.sendMessage("Finding gfx using model " + model + "...");
				Arrays.stream(SpotAnimType.LOADED)
						.filter(Objects::nonNull)
						.filter(def -> def.modelId == model)
						.forEachOrdered(def -> player.sendMessage("Found: " + def.id));
				return true;
			}

			case "objmodels": {
				LocType obj = LocType.get(Integer.parseInt(args[0]));
				if (obj == null) {
					player.sendMessage("Invalid id!");
					return true;
				}
				player.sendMessage(Arrays.toString(obj.modelIds));
				return true;
			}

			case "findobj": {
				LocType.LOADED.values().stream()
						.filter(Objects::nonNull)
						.filter(def -> !def.name.isEmpty())
						.filter(def -> query.toLowerCase().contains(def.name.toLowerCase()))
						.forEachOrdered(def -> {
							player.sendMessage(def.id + " - " + def.name);
							System.out.printf("%s - %s%n", def.id, def.name);
						});
				return true;
			}

			case "itemanim": {
				int id = Integer.parseInt(args[0]);
				player.sendMessage("Finding animation that uses item " + id + "...");
				Arrays.stream(SeqType.LOADED)
						.filter(Objects::nonNull)
						.filter(def -> def.rightHandItem - 512 == id)
						.forEachOrdered(def -> player.sendMessage("Found: " + def.id));
				return true;
			}

			case "animitem": {
				SeqType anim = SeqType.get(Integer.parseInt(args[0]));
				if (anim.rightHandItem == -1)
					player.sendMessage("Animation does not use an item");
				else
					player.sendMessage("Animation uses item " + (anim.rightHandItem - 512) + ".");
				return true;
			}

			case "ag": {
				int animation = Integer.parseInt(args[0]);
				int gfx = Integer.parseInt(args[1]);
				player.animate(animation);
				player.graphics(gfx, 0, 0);
				return true;
			}

			case "picon": {
				player.getAppearance().setPrayerIcon(Integer.parseInt(args[0]));
				return true;
			}

			case "sicon": {
				player.getAppearance().setSkullIcon(Integer.parseInt(args[0]));
				return true;
			}

			case "implingspawns": {
				player.sendMessage(
						"There are " + Impling.getACTIVE_PURO_PURO_IMPLINGS() + " imps in puropuro");
				player.sendMessage(
						"There are " + Impling.getACTIVE_OVERWORLD_IMPLINGS() + " imps in the overworld");
				return true;
			}

			case "resetcamera": {
				player.getPacketSender().resetCamera();
				return true;
			}
			case "zoomcamera": {
				player.getPacketSender().sendClientScript(39, "i", Integer.parseInt(args[0]));
				return true;
			}

			case "movecamera": {
				player.getPacketSender().moveCameraToLocation(3071, 3515, 400, 0, 15);
				player.getPacketSender().turnCameraToLocation(3068, 3517, 0, 0, 25);
				return true;
			}

			case "movecamera2": {
				player.getPacketSender().moveCameraToLocation(3080, 3499, 800, 0, 15);
				player.getPacketSender().turnCameraToLocation(3084, 3504, 0, 0, 25);
				return true;
			}

			case "rotatecamera": {
				player.getPacketSender().turnCameraToLocation(3079, 3487, 30, 0, 30);
				return true;
			}

			/**
			 * Misc commands
			 */
			case "reloadteles":
			case "reloadteleports": {
				DataFile.reload(player, teleports.class);
				return true;
			}

			case "reloadhelp": {
				DataFile.reload(player, Help.class);
				return true;
			}
			case "reloadcombat": {
				DataFile.reload(player, npc_combat.class);
				return true;
			}

			case "smute": {
				forPlayerTime(player, query, "::smute playerName #d/#h/perm",
						(p2, time) -> Punishment.mute(player, p2, time, true));
				return true;
			}

			case "resetbankpin": {
				forPlayer(player, query, "::resetbankpin playerName", p2 -> {
					p2.getBankPin().setPin(-1);
					player.sendMessage("Reset bankpin for " + p2.getName() + ".");
				});
				return true;
			}

			case "chunk": {
				int chunkX = player.getPosition().getChunkX();
				int chunkY = player.getPosition().getChunkY();
				int chunkAbsX = chunkX << 3;
				int chunkAbsY = chunkY << 3;
				int localX = player.getPosition().getX() - chunkAbsX;
				int localY = player.getPosition().getY() - chunkAbsY;
				Region region = Region.get(chunkAbsX, chunkAbsY);
				int pointX = (player.getPosition().getX() - region.baseX) / 8;
				int pointY = (player.getPosition().getY() - region.baseY) / 8;
				player.sendMessage("Chunk: " + chunkX + ", " + chunkY);
				player.sendMessage("    abs = " + chunkAbsX + ", " + chunkAbsY);
				player.sendMessage("    local = " + localX + ", " + localY);
				player.sendMessage("    points =  " + pointX + ", " + pointY);
				return true;
			}

			case "region": {
				Region region;
				if (args == null || args.length == 0)
					region = player.getPosition().getRegion();
				else
					region = Region.get(Integer.parseInt(args[0]));
				player.sendMessage("Region: " + region.id);
				player.sendMessage("    base = " + region.baseX + "," + region.baseY);
				player.sendMessage("    empty = " + region.empty);
				return true;
			}

			case "toregion": {
				int region = (Integer.parseInt(args[0]));
				int x = ((region << 6) >> 8);
				int y = (region << 6);

				player.getMovement().teleport(x, y, player.getHeight());
				return true;
			}

			case "clipping": {
				Tile tile = Tile.get(player.getAbsX(), player.getAbsY(), player.getHeight(), false);
				player.sendMessage("Clipping: " + (tile == null ? -1 : tile.clipping));
				System.out.println(tile.clipping & ~ClipConstants.UNMOVABLE_MASK);
				return true;
			}

			case "wipe": {
				String name = query.substring(command.length() + 1);
				Player p2 = World.getPlayer(name);
				if (p2 == null) {
					player.sendMessage("Could not find player: " + name);
					return true;
				}
				player.dialogue(
						new MessageDialogue("Are you sure you want to wipe player: " + p2.getName() + "?"),
						new OptionsDialogue(
								new Option("Yes", () -> { // todo log this
									if (!p2.isOnline()) {
										player.sendMessage(p2.getName() + " is no longer online!");
										return;
									}
									p2.getInventory().clear();
									p2.getEquipment().clear();
									p2.getBank().clear();
								}),
								new Option("No", player::closeDialogue)));
				return true;
			}

			case "convertshops": {
				ShopManager.getShops().values().stream().filter(shop -> !shop.generatedByBuilder)
						.forEach(shop -> {
							String fileName = shop.title.replace(" ", "_") + ".yaml";
							try (FileWriter fw = new FileWriter(new File("F:/convshops/" + fileName))) {

								ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

								objectMapper.writeValue(fw, shop);

							} catch (Exception ex) {
								ex.printStackTrace();
							}
						});
				return true;
			}

			case "namespawns": {
				npc_spawns.allSpawns.forEach((file, spawns) -> {
					spawns.forEach(spawn -> spawn.name = NPCType.get(spawn.id).name);
					try {
						JsonUtils.toFile(new File(file), JsonUtils.toPrettyJson(spawns));
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				return true;
			}

			case "pinv": {
				StringBuilder sb = new StringBuilder();
				for (Item item : player.getInventory().getItems()) {
					if (item != null)
						sb.append(item.getId()).append(",");
				}
				System.out.println(sb.substring(0, sb.length() - 1));
				return true;
			}

			case "b":
			case "bank":
			case "openbank": {
				player.getBank().open();
				return true;
			}

			case "reloaditems": {
				Server.executeAsync(() -> {
					player.sendMessage("Reloading item info...");
					DataFile.reload(player, shield_types.class);
					DataFile.reload(player, weapon_types.class);
					DataFile.reload(player, item_info.class);
					player.sendMessage("Done!");
				});
				return true;
			}

			case "todung": {
				player.getMovement().teleport(player.getAbsX(), player.getAbsY() + 6400,
						player.getHeight());
				return true;
			}

			case "fromdung": {
				player.getMovement().teleport(player.getAbsX(), player.getAbsY() - 6400,
						player.getHeight());
				return true;
			}

		}

		return false;
	}
}

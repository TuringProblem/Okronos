package io.ruin.model.inter.handlers;

import io.ruin.data.impl.teleports;
import io.ruin.model.World;
import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.activities.duelarena.DuelArena;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.SpellBook;
import io.ruin.model.skills.magic.spells.ancient.AncientTeleport;
import io.ruin.model.skills.magic.spells.arceuus.ArceuusTeleport;
import io.ruin.model.skills.magic.spells.lunar.LunarTeleport;
import io.ruin.model.skills.magic.spells.modern.ModernTeleport;
import io.ruin.model.var.VarPlayerRepository;

public class TabMagic {

	public static void register() {
		InterfaceHandler.register(Interface.MAGIC_BOOK, h -> {
			for (SpellBook book : SpellBook.VALUES) {
				for (int i = 0; i < book.spells.length; i++) {
					int component = book.spellIdOffset + i;
					if (component == 6) {
						h.actions[component] = (DefaultAction) (player, option, slot, itemId) -> {
							if (option == 1) {
								ModernTeleport.teleport(player, World.HOME);
							} else if (option == 9) {
								if (player.teleportListener != null && !player.teleportListener.allow(player)) {
									return;
								}
								if (player.getCombat().isDefending(17)) {
									player.sendMessage("You can't use this in combat.");
									return;
								}
								if (player.lastServerTeleport == null) {
									player.sendMessage("You don't have a last teleport to teleport to.");
								} else {
									if (player.lastServerTeleport == ServerTeleports.VARDORVIS) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.VARDORVIS;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.LEVIATHAN) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.LEVIATHAN;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.SOL_HEREDIT) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.SOL_HEREDIT;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.DUKE_SUCELLUS) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.DUKE;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.PHANTOM_MUSPAH) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.PHANTOM_MUSPAH;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.WHISPERER) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.WHISPERER;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.BALANCE_ELEMENTAL) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.BALANCE_ELEMENTAL;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.OPHIDIA) {
										player.dialogue(new OptionsDialogue("Which Ophidia's Lair would you like to enter?",
											new Option("Global Ophidia's Lair", () -> {
												Position teleportPosition = new Position(3149, 4658, 0);
												ModernTeleport.teleport(player, teleportPosition);
											}),
											new Option("Instanced Ophidia's Lair", () -> {
												player.getInstanceTokenInterface().selectedBoss = InstanceMaps.OPHIDIA;
												player.getInstanceTokenInterface().startInstance(player, true);
											})
										));
										return;
									} else if (player.lastServerTeleport == ServerTeleports.GALVEK) {
										player.dialogue(new OptionsDialogue("Which Galvek Lair would you like to enter?",
											new Option("Global Galvek Lair", () -> {
												ModernTeleport.teleport(player, player.lastServerTeleport.teleportPos);
											}),
											new Option("Instanced Galvek Lair", () -> {
												player.getInstanceTokenInterface().selectedBoss = InstanceMaps.GALVEK;
												player.getInstanceTokenInterface().startInstance(player, true);
											})
										));
										return;
									} else if (player.lastServerTeleport == ServerTeleports.GROTESQUE_GUARDIANS) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.GROTESQUE_GUARDIANS;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									}
									ModernTeleport.teleport(player, player.lastServerTeleport.teleportPos);
								}
							}
						};
					} else if (component == 100) {
						h.actions[component] = (DefaultAction) (player, option, slot, itemId) -> {
							if (option == 1) {
								AncientTeleport.teleport(player, World.HOME);
							} else if (option == 9) {
								if (player.teleportListener != null && !player.teleportListener.allow(player)) {
									return;
								}
								if (player.getCombat().isDefending(17)) {
									player.sendMessage("You can't use this in combat.");
									return;
								}
								if (player.lastServerTeleport == null) {
									player.sendMessage("You don't have a last teleport to teleport to.");
								} else {
									if (player.lastServerTeleport == ServerTeleports.VARDORVIS) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.VARDORVIS;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.LEVIATHAN) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.LEVIATHAN;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.SOL_HEREDIT) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.SOL_HEREDIT;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.DUKE_SUCELLUS) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.DUKE;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.PHANTOM_MUSPAH) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.PHANTOM_MUSPAH;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.WHISPERER) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.WHISPERER;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.BALANCE_ELEMENTAL) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.BALANCE_ELEMENTAL;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.OPHIDIA) {
										player.dialogue(new OptionsDialogue("Which Ophidia's Lair would you like to enter?",
											new Option("Global Ophidia's Lair", () -> {
												Position teleportPosition = new Position(3149, 4658, 0);
												ModernTeleport.teleport(player, teleportPosition);
											}),
											new Option("Instanced Ophidia's Lair", () -> {
												player.getInstanceTokenInterface().selectedBoss = InstanceMaps.OPHIDIA;
												player.getInstanceTokenInterface().startInstance(player, true);
											})
										));
										return;
									} else if (player.lastServerTeleport == ServerTeleports.GALVEK) {
										player.dialogue(new OptionsDialogue("Which Galvek Lair would you like to enter?",
											new Option("Global Galvek Lair", () -> {
												ModernTeleport.teleport(player, player.lastServerTeleport.teleportPos);
											}),
											new Option("Instanced Galvek Lair", () -> {
												player.getInstanceTokenInterface().selectedBoss = InstanceMaps.GALVEK;
												player.getInstanceTokenInterface().startInstance(player, true);
											})
										));
										return;
									} else if (player.lastServerTeleport == ServerTeleports.GROTESQUE_GUARDIANS) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.GROTESQUE_GUARDIANS;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									}
									AncientTeleport.teleport(player, player.lastServerTeleport.teleportPos);
								}
							}
						};
					} else if (component == 101) {
						h.actions[component] = (DefaultAction) (player, option, slot, itemId) -> {
							if (option == 1) {
								LunarTeleport.teleport(player, World.HOME);
							} else if (option == 9) {
								if (player.teleportListener != null && !player.teleportListener.allow(player)) {
									return;
								}
								if (player.getCombat().isDefending(17)) {
									player.sendMessage("You can't use this in combat.");
									return;
								}
								if (player.lastServerTeleport == null) {
									player.sendMessage("You don't have a last teleport to teleport to.");
								} else {
									if (player.lastServerTeleport == ServerTeleports.VARDORVIS) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.VARDORVIS;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.LEVIATHAN) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.LEVIATHAN;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.SOL_HEREDIT) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.SOL_HEREDIT;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.DUKE_SUCELLUS) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.DUKE;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.PHANTOM_MUSPAH) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.PHANTOM_MUSPAH;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.WHISPERER) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.WHISPERER;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.BALANCE_ELEMENTAL) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.BALANCE_ELEMENTAL;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.OPHIDIA) {
										player.dialogue(new OptionsDialogue("Which Ophidia's Lair would you like to enter?",
											new Option("Global Ophidia's Lair", () -> {
												Position teleportPosition = new Position(3149, 4658, 0);
												ModernTeleport.teleport(player, teleportPosition);
											}),
											new Option("Instanced Ophidia's Lair", () -> {
												player.getInstanceTokenInterface().selectedBoss = InstanceMaps.OPHIDIA;
												player.getInstanceTokenInterface().startInstance(player, true);
											})
										));
										return;
									} else if (player.lastServerTeleport == ServerTeleports.GALVEK) {
										player.dialogue(new OptionsDialogue("Which Galvek Lair would you like to enter?",
											new Option("Global Galvek Lair", () -> {
												ModernTeleport.teleport(player, player.lastServerTeleport.teleportPos);
											}),
											new Option("Instanced Galvek Lair", () -> {
												player.getInstanceTokenInterface().selectedBoss = InstanceMaps.GALVEK;
												player.getInstanceTokenInterface().startInstance(player, true);
											})
										));
										return;
									} else if (player.lastServerTeleport == ServerTeleports.GROTESQUE_GUARDIANS) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.GROTESQUE_GUARDIANS;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									}
									LunarTeleport.teleport(player, player.lastServerTeleport.teleportPos);
								}
							}
						};
					} else if (component == 148) {
						h.actions[component] = (DefaultAction) (player, option, slot, itemId) -> {
							if (option == 1) {
								ArceuusTeleport.teleport(player, World.HOME);
							} else if (option == 9) {
								if (player.teleportListener != null && !player.teleportListener.allow(player)) {
									return;
								}
								if (player.getCombat().isDefending(17)) {
									player.sendMessage("You can't use this in combat.");
									return;
								}
								if (player.lastServerTeleport == null) {
									player.sendMessage("You don't have a last teleport to teleport to.");
								} else {
									if (player.lastServerTeleport == ServerTeleports.VARDORVIS) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.VARDORVIS;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.LEVIATHAN) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.LEVIATHAN;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.SOL_HEREDIT) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.SOL_HEREDIT;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.DUKE_SUCELLUS) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.DUKE;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.PHANTOM_MUSPAH) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.PHANTOM_MUSPAH;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.WHISPERER) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.WHISPERER;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.BALANCE_ELEMENTAL) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.BALANCE_ELEMENTAL;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									} else if (player.lastServerTeleport == ServerTeleports.OPHIDIA) {
										player.dialogue(new OptionsDialogue("Which Ophidia's Lair would you like to enter?",
											new Option("Global Ophidia's Lair", () -> {
												Position teleportPosition = new Position(3149, 4658, 0);
												ModernTeleport.teleport(player, teleportPosition);
											}),
											new Option("Instanced Ophidia's Lair", () -> {
												player.getInstanceTokenInterface().selectedBoss = InstanceMaps.OPHIDIA;
												player.getInstanceTokenInterface().startInstance(player, true);
											})
										));
										return;
									} else if (player.lastServerTeleport == ServerTeleports.GALVEK) {
										player.dialogue(new OptionsDialogue("Which Galvek Lair would you like to enter?",
											new Option("Global Galvek Lair", () -> {
												ModernTeleport.teleport(player, player.lastServerTeleport.teleportPos);
											}),
											new Option("Instanced Galvek Lair", () -> {
												player.getInstanceTokenInterface().selectedBoss = InstanceMaps.GALVEK;
												player.getInstanceTokenInterface().startInstance(player, true);
											})
										));
										return;
									} else if (player.lastServerTeleport == ServerTeleports.GROTESQUE_GUARDIANS) {
										player.getInstanceTokenInterface().selectedBoss = InstanceMaps.GROTESQUE_GUARDIANS;
										player.getInstanceTokenInterface().startInstance(player, true);
										return;
									}
									ArceuusTeleport.teleport(player, player.lastServerTeleport.teleportPos);
								}
							}
						};
					} else {
						h.actions[book.spellIdOffset + i] = createAction(book, book.spells[i]);
					}
				}
			}
			;
			h.actions[198] = (DefaultAction) (player, option, slot, itemId) -> {
				if (slot == 0)
					VarPlayerRepository.SHOW_COMBAT_SPELLS.toggle(player);
				if (slot == 1)
					VarPlayerRepository.SHOW_TELEPORT_SPELLS.toggle(player);
				if (slot == 2)
					VarPlayerRepository.SHOW_UTILITY_SPELLS.toggle(player);
				if (slot == 3)
					VarPlayerRepository.SHOW_SPELLS_LACK_LEVEL.toggle(player);
				if (slot == 4)
					VarPlayerRepository.SHOW_SPELLS_LACK_RUNES.toggle(player);
				if (slot == 5)
					VarPlayerRepository.SHOW_SPELLS_LACK_REQS.toggle(player);
			};
		});
		LoginListener.register(player -> {
			VarPlayerRepository.WORLD_MAP_OBJECT.set(player, 1);
			VarPlayerRepository.ANCIENT_MAGIC_SPELLS.set(player, 15);
			VarPlayerRepository.LUNAR_MAGIC_SPELLS.set(player, 190);
			VarPlayerRepository.ARCEEUS_MAGIC_SPELLS.set(player, 150);
			VarPlayerRepository.HARMONY_ISLAND_TELEPORT.set(player, 130);
		});
	}


	private static InterfaceAction createAction(SpellBook book, Spell spell) {
		return new InterfaceAction() {
			@Override
			public void handleClick(Player player, int option, int slot, int itemId) {
				if (spell.clickAction == null || !book.isActive(player) && !DuelArena.allowMagic(player))
					return;
				spell.clickAction.accept(player, option - 1);
			}

			@Override
			public void handleOnInterface(Player player, int fromSlot, int fromItemId, int toInterfaceId, int toChildId, int toSlot, int toItemId) {
				if (spell.itemAction == null || !book.isActive(player) && !DuelArena.allowMagic(player))
					return;
				Item item = player.getInventory().get(toSlot, toItemId);
				if (item == null)
					return;
				spell.itemAction.accept(player, item);
			}

			@Override
			public void handleOnEntity(Player player, Entity entity, int slot, int itemId) {
				if (spell.entityAction == null || !book.isActive(player) && !DuelArena.allowMagic(player))
					return;
				spell.entityAction.accept(player, entity);
			}

			@Override
			public void handleOnObject(Player player, int slot, int itemId, GameObject obj) {
				if (spell.objectAction == null || !book.isActive(player) && !DuelArena.allowMagic(player))
					return;
				spell.objectAction.accept(player, obj);
			}
		};
	}

}

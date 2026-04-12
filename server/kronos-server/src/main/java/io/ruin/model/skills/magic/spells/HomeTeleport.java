package io.ruin.model.skills.magic.spells;

import io.ruin.model.World;
import io.ruin.model.activities.bosses.instancetoken.InstanceMaps;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.handlers.ServerTeleports;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Position;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.spells.ancient.AncientTeleport;
import io.ruin.model.skills.magic.spells.modern.ModernTeleport;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class HomeTeleport extends Spell {

	public static final HomeTeleport MODERN = new HomeTeleport(p -> ModernTeleport.teleport(p, World.HOME));

	public static final HomeTeleport ANCIENT = new HomeTeleport(p -> ModernTeleport.teleport(p, World.HOME));

	public static final HomeTeleport LUNAR = new HomeTeleport(p -> ModernTeleport.teleport(p, World.HOME));

	public static final HomeTeleport ARCEUUS = new HomeTeleport(p -> ModernTeleport.teleport(p, World.HOME));

	private static final List<HomeTeleportOverride> OVERRIDES = new LinkedList<>();

	private HomeTeleport(Consumer<Player> consumer) {
		clickAction = (p, i) -> {
			if (i == 8) {
				if (p.teleportListener != null && !p.teleportListener.allow(p)) {
					return;
				}
				if (p.getCombat().isDefending(17)) {
					p.sendMessage("You can't use this in combat.");
					return;
				}
				if (p.lastServerTeleport == null) {
					p.sendMessage("You don't have a last teleport to teleport to.");
				} else {
					if (p.lastServerTeleport == ServerTeleports.VARDORVIS) {
						p.getInstanceTokenInterface().selectedBoss = InstanceMaps.VARDORVIS;
						p.getInstanceTokenInterface().startInstance(p, true);
						return;
					} else if (p.lastServerTeleport == ServerTeleports.LEVIATHAN) {
						p.getInstanceTokenInterface().selectedBoss = InstanceMaps.LEVIATHAN;
						p.getInstanceTokenInterface().startInstance(p, true);
						return;
					} else if (p.lastServerTeleport == ServerTeleports.SOL_HEREDIT) {
						p.getInstanceTokenInterface().selectedBoss = InstanceMaps.SOL_HEREDIT;
						p.getInstanceTokenInterface().startInstance(p, true);
						return;
					} else if (p.lastServerTeleport == ServerTeleports.DUKE_SUCELLUS) {
						p.getInstanceTokenInterface().selectedBoss = InstanceMaps.DUKE;
						p.getInstanceTokenInterface().startInstance(p, true);
						return;
					} else if (p.lastServerTeleport == ServerTeleports.PHANTOM_MUSPAH) {
						p.getInstanceTokenInterface().selectedBoss = InstanceMaps.PHANTOM_MUSPAH;
						p.getInstanceTokenInterface().startInstance(p, true);
						return;
					} else if (p.lastServerTeleport == ServerTeleports.WHISPERER) {
						p.getInstanceTokenInterface().selectedBoss = InstanceMaps.WHISPERER;
						p.getInstanceTokenInterface().startInstance(p, true);
						return;
					} else if (p.lastServerTeleport == ServerTeleports.BALANCE_ELEMENTAL) {
						p.getInstanceTokenInterface().selectedBoss = InstanceMaps.BALANCE_ELEMENTAL;
						p.getInstanceTokenInterface().startInstance(p, true);
						return;
					} else if (p.lastServerTeleport == ServerTeleports.OPHIDIA) {
						p.dialogue(new OptionsDialogue("Which Ophidia's Lair would you like to enter?",
							new Option("Global Ophidia's Lair", () -> {
								Position teleportPosition = new Position(3149, 4658, 0);
								ModernTeleport.teleport(p, teleportPosition);
							}),
							new Option("Instanced Ophidia's Lair", () -> {
								p.getInstanceTokenInterface().selectedBoss = InstanceMaps.OPHIDIA;
								p.getInstanceTokenInterface().startInstance(p, true);
							})
						));
						return;
					} else if (p.lastServerTeleport == ServerTeleports.GALVEK) {
						p.dialogue(new OptionsDialogue("Which Galvek Lair would you like to enter?",
							new Option("Global Galvek Lair", () -> {
								ModernTeleport.teleport(p, ServerTeleports.GALVEK.getTeleportPos());
							}),
							new Option("Instanced Galvek Lair", () -> {
								p.getInstanceTokenInterface().selectedBoss = InstanceMaps.GALVEK;
								p.getInstanceTokenInterface().startInstance(p, true);
							})
						));
						return;
					} else if (p.lastServerTeleport == ServerTeleports.GROTESQUE_GUARDIANS) {
						p.getInstanceTokenInterface().selectedBoss = InstanceMaps.GROTESQUE_GUARDIANS;
						p.getInstanceTokenInterface().startInstance(p, true);
						return;
					}
					AncientTeleport.teleport(p, p.lastServerTeleport.getTeleportPos());
				}
			} else {
				if (p.pvpAttackZone) {
					if (p.getCombat().isDefending(16)) {
						p.sendMessage("You can't cast this spell while in combat in a pvp zone.");
						return;
					}
				} else if (p.gauntlet != null) {
					if (p.gauntlet.inGauntlet) {
						p.sendMessage("You can't teleport from the Gauntlet!");
						return;
					}
				}
				Position override = getHomeTeleportOverride(p);
				if (override != null) {
					ModernTeleport.teleport(p, override.getX(), override.getY(), override.getZ());
				} else {
					if (p.edgeHome) {
						ModernTeleport.teleport(p, World.EDGEHOME);
						return;
					}
					consumer.accept(p);
				}
			}
		};
	}

	private static class HomeTeleportOverride {
		Predicate<Player> condition;
		Position destination;

		public HomeTeleportOverride(Predicate<Player> condition, Position destination) {
			this.condition = condition;
			this.destination = destination;
		}
	}

	public static void registerHomeTeleportOverride(Predicate<Player> condition, Position destination) {
		OVERRIDES.add(new HomeTeleportOverride(condition, destination));
	}

	public static Position getHomeTeleportOverride(Player player) {
		for (HomeTeleportOverride teleportOverride : OVERRIDES) {
			if (teleportOverride.condition.test(player))
				return teleportOverride.destination;
		}
		return null;
	}
}

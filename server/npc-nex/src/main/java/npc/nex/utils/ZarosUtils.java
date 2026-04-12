package npc.nex.utils;

import io.ruin.cache.ItemID;
import io.ruin.cache.ObjType;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.object.GameObject;
import io.ruin.utility.Common;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

import static npc.nex.modes.Forms.isNex;


/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
@Slf4j
public class ZarosUtils {

	public static final String ATTRIBUTE_LOOT = "NEX_DMG";
	public static final String INFECTION = "NEX_INFECTION_EFFECT";
	public static final String COUGH = "*Cough*";
	public static final int INFECTION_DURATION_SECONDS = 8;
	public static final int INFECTION_DURATION_TICKS = Common.convertSecondsToTicksInt(INFECTION_DURATION_SECONDS);
	public static final int INFECTION_PRAYER_DRAIN = 4;
	public static final int INFECTION_PRAYER_DRAIN_SPECTRAL = 3;
	public static final Bounds NEX_BOUNDS = new Bounds(2909, 5187, 2941, 5220, 0);

	public static boolean shouldSpawnNex(Player p) {
		var bounds = new Bounds(
			p.getPosition().getRegion().baseX + 29,
			p.getPosition().getRegion().baseY + 3,
			p.getPosition().getRegion().baseX + 61,
			p.getPosition().getRegion().baseY + 36,
			0
		);
		if (World.npcsNonNull()
			.filter(npc -> npc.getPosition().inBounds(bounds))
			.anyMatch(npc -> isNex(npc.getId()))
		) { // System.out.println("Found an alive nex. Returned false.");
			return false;
		}// System.out.println("No live nex found.");
		return true;
	}

	public static boolean hasSpectral(Player player) {
		if (player.getEquipment().contains(ItemID.SPECTRAL_SPIRIT_SHIELD)) {
			player.sendFilteredMessage("Your spectral reduces the prayer drain.");
			return true;
		}
		return false;
	}

	public static boolean anyPlayerInNex(NPC npc) {
		return World.playersNonNull()
			.filter(p -> p.getPosition().regionId() == npc.getPosition().regionId())
			.anyMatch(p -> !p.dead());
	}

	public static long countPlayersInNex(NPC npc) {
		return World.playersNonNull()
			.filter(p -> p.getPosition().regionId() == npc.getPosition().regionId())
			.count();
	}

	public static long countGlobalPlayersInNex() {
		return World.playersNonNull()
			.filter(p -> p.getPosition().inBounds(NEX_BOUNDS)).count();
	}

	public static void removeNpc(List<NPC> toRemove) {
		toRemove.stream()
			.filter(Objects::nonNull)
			.filter(n -> n.alive() && !n.isRemoved())
			.forEach(NPC::remove);
	}

	public static void removeObjects(List<GameObject> i) {
		i.stream()
			.filter(Objects::nonNull)
			.filter(r -> !r.isRemoved())
			.forEach(GameObject::remove);
	}

	public static boolean hasSlayerHelm(Player p) {
		try {
			if (p.getEquipment().get(Equipment.SLOT_HAT) != null) {
				if (ObjType.get(p.getEquipment().get(Equipment.SLOT_HAT).getId()).name.toLowerCase().contains("slayer helm")) {
					p.sendFilteredMessage("Your slayer helm reduces the duration of the infection.");
					return true;
				}
			}
			return false;
		}
		catch (Exception e) {
			log.error("Error checking for slayer helm.", e);
			return false;
		}
	}
}

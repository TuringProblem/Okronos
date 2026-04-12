package io.ruin.model.skills.construction.room.impl;

import com.google.gson.annotations.Expose;
import io.ruin.api.utils.StringUtils;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.Bounds;
import io.ruin.model.skills.construction.room.Room;

import java.util.function.Predicate;

import static io.ruin.model.skills.magic.rune.Rune.*;

public class PortalNexusRoom extends Room {

	public enum NexusDestination {

		VARROCK(25, new Bounds(3211, 3422, 3214, 3424, 0), new int[]{13615, 13622, 13629}, LAW.toItem(100), AIR.toItem(300), FIRE.toItem(100));


		int levelReq;
		Item[] runes;
		int[] portalIds; //teak, mahog, marble
		Predicate<Player> focusReq;
		String name;
		boolean hidden;
		Predicate<Player> alternateReq;
		NexusDestination alternate;
		public Bounds bounds;

		NexusDestination(int levelReq, Bounds bounds, int[] portalIds, Item... runes) {
			this.levelReq = levelReq;
			this.bounds = bounds;
			this.runes = runes;
			this.portalIds = portalIds;
			name = StringUtils.fixCaps(name().replace('_', ' '));
			this.focusReq = p -> true;
		}

		NexusDestination(NexusDestination other, Bounds bounds) {
			this(other.levelReq, bounds, other.portalIds, other.runes);
			other.alternate = this;
			other.alternateReq = p -> true;
			alternate = other;
			alternateReq = p -> true;
			hidden = true;
		}
	}

	private NexusDestination[] nexusDestinations = new NexusDestination[3];
}

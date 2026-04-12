package io.ruin.model.activities.tempevents.hweenevent;

import io.ruin.api.utils.Random;
import io.ruin.cache.Icon;
import io.ruin.model.World;
import io.ruin.model.activities.tempevents.TemporaryEvent;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.utility.Broadcast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HweenEvent extends TemporaryEvent {

	List<String> hweenBossNames = Arrays.asList(
		"Dagannoth Rex", "Dagannoth Prime",
		"Dagannoth Supreme", "Kalphite Queen", "Chaos Elemental",
		"Corporeal Beast", "King Black Dragon", "General Graardor",
		"Commander Zilyana", "Kree'arra", "K'ril Tsutsaroth",
		"Vet'ion", "Venenatis", "Callisto",
		"Chaos Fanatic", "Scorpia", "Cerberus",
		"Thermonuclear Smoke Devil", "Zulrah", "Kraken", "Abyssal Sire",
		"Dusk", "Alchemical Hydra", "The Nightmare", "Theatre of Blood",
		"Vorkath", "Skotizo", "The Gauntlet", "Chambers of Xeric", "Tombs of Amascut", "Argentavis",
		"Sol Heredit", "Galvek", "Nex", "Sarachnis", "Ophidia", "Phantom Muspah", "Leviathan", "Duke Succellus",
		"The Whisperer", "Vardorvis", "Malakar"
	);

	List<String> availableBossNames = new ArrayList<>();
	List<String> cachedBossNames = new ArrayList<>();
	List<String> activeBossNames = new ArrayList<>();


	@Override
	protected void endEvent() {

	}

	private void newEventStart() {
		World.startEvent(e -> {
			e.delay(12000);
			cachedBossNames.clear();
			cachedBossNames.addAll(activeBossNames);
			activeBossNames.clear();
			populateList();

			for (String bossName : cachedBossNames) {
				availableBossNames.remove(bossName);
			}

			for (int i = 0; i < 5; i++) {
				String bossName = Random.get(availableBossNames);
				activeBossNames.add(bossName);
				availableBossNames.remove(bossName);
			}

			StringBuilder sb = new StringBuilder();
			int size = activeBossNames.size();
			int index = 0;

			for (String bossName : activeBossNames) {
				sb.append(bossName);
				if (index < size - 1) {
					sb.append(", ");
				}
				index++;
			}
			Broadcast.WORLD.sendNews(Icon.GRAVE, "</col>[<shad=8A0011>Halloween Event</shad>] <shad=9B9B9B> The following bosses have been selected for the H'ween event:<br> <shad=8A0011>" + sb.toString());

			e.delay(15);
			newEventStart();
		});
	}

	public String getActiveBosses() {
		StringBuilder sb = new StringBuilder();
		int size = activeBossNames.size();
		int index = 0;

		for (String bossName : activeBossNames) {
			sb.append(bossName);
			if (index < size - 1) {
				sb.append(", ");
			}
			index++;
		}
		return sb.toString();
	}


	private void populateList() {
		availableBossNames.clear();
		availableBossNames.addAll(hweenBossNames);
	}

	public void rollPumpkin(Player player, String name) {
		if (isBossActive(name)) {
			if (Random.get(30) == 0) {
				GroundItem loot = new GroundItem(new Item(1959, 1)).position(player.getPosition());
				loot.owner(player);
				loot.spawn();
				player.sendMessage("<shad=000000><col=ff0000>A pumpkin has been dropped at your feet!");
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " received <shad=D80808>1x " + new Item(1959).getDef().name.toLowerCase() + "</shad> from a " + name + "!");
			}
		}
	}

	private boolean isBossActive(String name) {
		for (String bossName : activeBossNames) {
			if (bossName.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void process() {

	}

	public void init() {
		newEventStart();
		cachedBossNames.clear();
		cachedBossNames.addAll(activeBossNames);
		activeBossNames.clear();
		populateList();

		for (String bossName : cachedBossNames) {
			availableBossNames.remove(bossName);
		}

		for (int i = 0; i < 5; i++) {
			String bossName = Random.get(availableBossNames);
			activeBossNames.add(bossName);
			availableBossNames.remove(bossName);
		}

		StringBuilder sb = new StringBuilder();
		int size = activeBossNames.size();
		int index = 0;

		for (String bossName : activeBossNames) {
			sb.append(bossName);
			if (index < size - 1) {
				sb.append(", ");
			}
			index++;
		}
		Broadcast.WORLD.sendNews(Icon.GRAVE, "</col>[<shad=8A0011>Halloween Event</shad>] <shad=9B9B9B> The following bosses have been selected for the H'ween event:<br> <shad=8A0011>" + sb.toString());

		World.hweenEvent = this;
	}

	@Override
	public void start() {

	}
}

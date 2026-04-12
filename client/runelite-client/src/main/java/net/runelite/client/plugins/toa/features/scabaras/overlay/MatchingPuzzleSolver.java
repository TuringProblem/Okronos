package net.runelite.client.plugins.toa.features.scabaras.overlay;

import net.runelite.client.plugins.toa.TombsOfAmascutConfig;
import net.runelite.client.plugins.toa.features.scabaras.ScabarasHelperMode;
import net.runelite.client.plugins.toa.module.PluginLifecycleComponent;
import net.runelite.client.plugins.toa.util.RaidRoom;
import net.runelite.client.plugins.toa.util.RaidState;
import com.google.common.collect.ImmutableMap;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MatchingPuzzleSolver implements PluginLifecycleComponent
{

	private static final Map<Integer, String> TILE_NAMES = ImmutableMap.<Integer, String>builder()
		.put(45345, "Line") // line
		.put(45346, "Knives") // knives
		.put(45351, "Crook") // crook (J)
		.put(45348, "Diamond") // diamond
		.put(45349, "Hand") // hand
		.put(45347, "Star") // triangle
		.put(45350, "Bird") // bird
		.put(45352, "W") // wiggle
		.put(45353, "Boot") // boot
		.build();

	private static final Map<Integer, Color> TILE_COLORS = ImmutableMap.<Integer, Color>builder()
		.put(45345, Color.black) // line
		.put(45346, Color.red) // knives
		.put(45351, Color.magenta) // crook
		.put(45348, Color.blue) // diamond
		.put(45349, Color.lightGray) // hand
		.put(45347, Color.cyan) // triangle
		.put(45350, Color.pink) // bird
		.put(45352, Color.yellow) // wiggle
		.put(45353, Color.green) // boot
		.build();

	private static final Map<Integer, Integer> MATCHED_OBJECT_IDS = ImmutableMap.<Integer, Integer>builder() //object, ground
		.put(45388, 45345) // line
		.put(45389, 45346) // knives
		.put(45386, 45351) // crook
		.put(45387, 45348) // diamond
		.put(45392, 45349) // hand
		.put(45390, 45347) // triangle
		.put(45393, 45350) // bird
		.put(45394, 45352) // wiggle
		.put(45395, 45353) // boot
		.build();

	private final EventBus eventBus;

	@Getter
	private final Map<LocalPoint, MatchingTile> discoveredTiles = new HashMap<>(18);

	@Override
	public boolean isEnabled(TombsOfAmascutConfig config, RaidState raidState)
	{
		return config.scabarasHelperMode() == ScabarasHelperMode.OVERLAY && raidState.getCurrentRoom() == RaidRoom.SCABARAS;
	}

	@Override
	public void startUp()
	{
		eventBus.register(this);
		discoveredTiles.clear();
	}

	@Override
	public void shutDown()
	{
		eventBus.unregister(this);
	}

	@Subscribe
	public void onGroundObjectSpawned(GroundObjectSpawned e)
	{
		int id = e.getGroundObject().getId();
		if (TILE_COLORS.containsKey(id))
		{
			LocalPoint lp = e.getGroundObject().getLocalLocation();
			discoveredTiles.put(lp, new MatchingTile(lp, TILE_NAMES.getOrDefault(id, "Unknown"), TILE_COLORS.getOrDefault(id, Color.black)));
		}
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned e)
	{
		int gameId = e.getGameObject().getId();
		if (MATCHED_OBJECT_IDS.containsKey(gameId))
		{
			MatchingTile match = discoveredTiles.get(e.getGameObject().getLocalLocation());
			if (match == null)
			{
				log.debug("Failed to find discovered tile for game object id {}!", gameId);
				return;
			}

			match.setMatched(true);
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage e)
	{
		if (e.getMessage().startsWith("Your party failed to complete the challenge"))
		{
			discoveredTiles.clear();
		}
	}
}

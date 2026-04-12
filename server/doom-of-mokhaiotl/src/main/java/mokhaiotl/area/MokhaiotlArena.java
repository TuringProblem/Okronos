package mokhaiotl.area;

import core.api.Random;
import core.task.Continuation;
import io.ruin.model.activities.bosses.instancetoken.MapHandler;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Bounds;
import mokhaiotl.combat.MokhaiotlCombat;
import mokhaiotl.npc.DoomOfMokhaiotl;

import static core.combat.api.ReasonUtils.pos;
import static core.task.api.API.sleep;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-07-28
 */
public abstract class MokhaiotlArena extends MapHandler implements Continuation.Void {

	protected DoomOfMokhaiotl dom;

	private MokhaiotlCombat getCombat() {
		return (MokhaiotlCombat) dom.getCombat();
	}

	public Bounds getArenaBounds() {
		var region = map.swRegion;
		return new Bounds(
			region.baseX + 22,
			region.baseY + 28,
			region.baseX + 40,
			region.baseY + 46,
			0
		);
	}

	@Override
	public void init() {
		npcs.add(dom.spawn(pos(map, 29, 35)));
	}

	@Override
	public void movePlayerToInstance(Player player) {
		var delveLevel = player.get("MOKHAIOTL_DELVE_LEVEL", 1);
		dom.setDelveLevel(delveLevel);
		getCombat().init();
		player.getMovement().teleport(pos(map, 31, (delveLevel > 0 ? 28 : 24)));
		player.currentDynamicMap = map;
		player.taggedVolatileEarth = null;
		player.getHealthHud().open(true, dom.getId(), dom.getMaxHp());
		players.add(player);
		map.assignListener(player).onExit(this::defaultMapExit);
	}

	@Override
	public void call() {
		while (this.map.hasPlayers()) {
			this.map.forPlayers(player -> {
				if (dom.getCombat().isDead()) {
					if (player.getHealthHud().isOpened())
						player.getHealthHud().close();
				}

				if (!player.getPosition().getTile().gameObjects.isEmpty()) {
					player.getPosition().getTile().gameObjects.forEach(object -> {
						if (object.getId() == 57283 || object.getId() == 57284) {
							player.hit(new Hit(HitType.VENOM).randDamage(6, 9));
							player.envenom(Random.get(6, 9));
						}
					});
				}
			});
			sleep(1);
		}
	}
}

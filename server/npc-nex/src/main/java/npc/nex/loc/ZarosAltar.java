package npc.nex.loc;

import npc.nex.old.ZarosItems;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.Common;

import java.util.concurrent.TimeUnit;

/**
 * @author Glabay | Glabay-Studios
 * @project reason-server
 * @social Discord: Glabay
 * @since 2025-09-03
 */
public class ZarosAltar {

	private static final int ANCIENT_ALTAR_ID = 42965;

	public static void register() {
		ObjectAction.register(ANCIENT_ALTAR_ID, "pray", ZarosAltar::prayAction);
		ObjectAction.register(ANCIENT_ALTAR_ID, "teleport", ZarosAltar::teleportAction);
	}

	public static void prayAction(Player player, GameObject altar) {
		// if altar delay is less than current time
		if (player.nexAltarDelay > System.currentTimeMillis()) {
			player.sendMessage("You can use this again in %s."
				.formatted(Common.getRemainingTime(player.nexAltarDelay)));
			return;
		}
		if (player.getCombat().isDefending(5) | player.getCombat().isAttacking(5)) {
			player.sendFilteredMessage("You cannot use this altar while in combat.");
			return;
		}
		player.nexAltarDelay = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10); // 10 minutes
		player.getStats().get(StatType.Prayer).restore(); // restore prayer regardless
		player.startEvent(e -> {
			e.delay(1);
			player.animate(645);
			player.privateSound(2674);
			if (ZarosItems.hasZarosItems(player)) { // if has zaros items
				VarPlayerRepository.SPECIAL_ENERGY.set(player, 1000); // restore spec
				player.getMovement().restoreEnergy(100); // restore run
				player.setHp(player.getMaxHp()); // heal to full
			}
			player.sendFilteredMessage("You pray at the altar. It is on cooldown for 10 minutes.");
		});
	}

	public static void teleportAction(Player player, GameObject altar) {
		player.startEvent(event -> {
			player.lock(LockType.FULL_NULLIFY_DAMAGE);
			player.face(altar);
			player.animate(3945);
			player.privateSound(200, 0, 10);
			event.delay(2);
			player.getMovement().teleport(new Position(player.getPosition().getRegion().baseX + 24, player.getPosition().getRegion().baseY + 19, 0));
			player.animate(-1);
			player.unlock();
		});
	}
}

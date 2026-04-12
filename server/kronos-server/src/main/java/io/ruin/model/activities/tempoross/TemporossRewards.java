package io.ruin.model.activities.tempoross;

import io.ruin.model.map.object.actions.ObjectAction;

public class TemporossRewards {

	public static final int REWARD_POOL = 41356;

	// if players points = 0 then pool = empty else pool = reward_pool
	// Varpbit ID for pool = 11936 if varbit(11936) == 0 then show empty pool else if varbit(11936) > 0 then show reward pool. it's controlled automatically just have to setup awarding points.
	//8875
	//10579
	//tempoross 8895
	//41241
	//points varpbit 11897
	//11895 blue overlay
	//37582 = flames
	//'Luke' — Yesterday at 21:03
	//3705 anim, 1845 gfx
	//832 anim 1846 gfx
	//animation 8871

	//Projectiles
	//1842
	//1843

	public static void register() {

		ObjectAction.register(REWARD_POOL, 1, (p, obj) -> {
			p.startEvent(e -> {
				while (true) {
					p.animate(621);
					e.delay(1);
					p.getInventory().addOrDrop(25588, 1); // 6k an hour
				}
			});
		});
	}
}

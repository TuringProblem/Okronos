package royaltitans;

import core.task.Continuation;

import static core.api.API.*;
import static core.task.api.API.*;

public class ElementalFire implements Continuation.Void {
	private final RoyalTitansArea area;

	private final NPC npc;

	public ElementalFire(RoyalTitansArea area) {
		this.area = area;
		this.npc = npc(14150);
		// allowCombat(this.npc);
		// noRespawn(this.npc);
		// stats(this.npc, 30, 35, 20, 35, 20, 20);
		// deathAnim(this.npc, 11957);
	}
	//
	// @Override
	// public void started() {
	// 	hook(this.npc, Entity.Hook.PreAttackWithSpell.class, this::preAttackWithSpell);
	// 	spawn(this.npc, this.area.combatArea().random());
	// }
	//
	@Override
	public void call() {}
	// 	forkLoop(ctx -> {
	// 		var target = players(this.area.combatArea()).random();
	// 		if (target == null) {
	// 			sleep(1);
	// 			return;
	// 		}
	//
	// 		switch (follow(this.npc, target, 5).await()) {
	// 			case Ok -> {
	// 				face(this.npc, target);
	// 				animate(this.npc, 11947);
	// 				hit(this.npc, target, 5);
	// 				sleep(5);
	// 			}
	// 			default -> {
	// 				sleep(1);
	// 			}
	// 		}
	// 	});
	//
	// 	while (!removed(this.npc)) {
	// 		sleep(1);
	// 	}
	// }
	//
	// public Result preAttackWithSpell(Entity.Hook.PreAttackWithSpell ctx) {
	// 	var player = ctx.player();
	// 	var target = ctx.target();
	// 	var spell = ctx.spell();
	// 	if (!(spell instanceof WaterSpell)) {
	// 		return Result.Pass;
	// 	}
	//
	// 	var pCombat = player.getCombat();
	// 	if (pCombat.hasAttackDelay()) {
	// 		return Result.Return;
	// 	}
	//
	// 	if (!spell.removeRunes(player, target)) {
	// 		return Result.Return;
	// 	}
	//
	// 	var intersecting = new HashSet<ElementalFire>();
	//
	// 	for (var tile : tiles().around(pos(npc), 1).tiles()) {
	// 		for (var elemental : this.area.fireElementals) {
	// 			if (pos(elemental.npc).equals(tile)) {
	// 				intersecting.add(elemental);
	// 			}
	// 		}
	// 	}
	// 	for (var npc : intersecting) {
	// 		spell.sendProjectile(player, into(npc.npc), Time.server(2).client().ticks());
	// 		spell.performEndHitEffects(player, into(npc.npc), Time.server(2).client().ticks());
	// 		into(npc.npc).hit(npc.npc.hpMax(), npc.npc.hpMax());
	// 	}
	//
	// 	pCombat.updateLastAttack(6);
	// 	return Result.Return;
	// }
}

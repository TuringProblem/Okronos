package io.ruin.model.activities.miscpvm.slayer;

import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.combine.SlayerHelm;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.object.actions.impl.dungeons.SmokeDevilDungeon;
import io.ruin.model.skills.slayer.Slayer;

public class SmokeDevil extends NPCCombat {

	protected static final Projectile PROJECTILE = new Projectile(643, 65, 31, 15, 56, 10, 0, 0);

	private boolean hasFaceMask(Player player) {
		Item faceMask = player.getEquipment().findFirst(SlayerHelm.PURPLE_SLAYER_HELM, SlayerHelm.SLAYER_HELM, SlayerHelm.SLAYER_HELM_IMBUE, SlayerHelm.BLACK_SLAYER_HELM,
			SlayerHelm.BLACK_HELM_IMBUE, SlayerHelm.RED_HELM_IMBUE, SlayerHelm.RED_SLAYER_HELM, SlayerHelm.GREEN_HELM_IMBUE, SlayerHelm.GREEN_SLAYER_HELM,
			SlayerHelm.PURPLE_HELM_IMBUE, SlayerHelm.FACEMASK, SlayerHelm.HYDRA_SLAYER_HELM, SlayerHelm.HYDRA_HELM_IMBUE,
			SlayerHelm.VORKATH_SLAYER_HELM, SlayerHelm.VORKATH_SLAYER_HELM_IMBUE, SlayerHelm.TWISTED_SLAYER_HELM, SlayerHelm.TWISTED_HELM_IMBUE);
		return faceMask != null;
	}

	@Override
	public void init() {

	}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public boolean attack() {
		if (!withinDistance(8))
			return false;
		if (smokeAttack())
			return true;
		projectileAttack(PROJECTILE, info.attack_animation, info.attack_style, info.max_damage);
		return true;
	}

	@Override
	public void process() {

	}

	Player player;

	protected boolean smokeAttack() {
		if (target.player != null && (target.player.getEquipment().getId(Equipment.SLOT_HAT) != 4164 && !Slayer.hasSlayerHelmEquipped(target.player))) {
			target.hit(new Hit(npc, AttackStyle.MAGIC).fixedDamage(18).ignorePrayer().ignoreDefence());
			target.player.sendMessage("<col=ff0000>The devil's smoke blinds and damages you!");
			target.player.sendMessage("<col=ff0000>A facemask can protect you from this attack.");
			return true;
		}
		return false;
	}
}

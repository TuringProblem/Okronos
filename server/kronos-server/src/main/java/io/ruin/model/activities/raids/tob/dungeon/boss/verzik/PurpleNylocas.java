package io.ruin.model.activities.raids.tob.dungeon.boss.verzik;

import io.ruin.model.World;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Position;

public class PurpleNylocas {
	public NPC npc;
	Entity target;
	boolean healing = false;

	PurpleNylocas(Position spawn, Entity target) {
		this.target = target;
		npc = new NPC(8384).spawn(spawn);
		npc.getCombat().setAllowRetaliate(false);
		npc.getCombat().setAllowRespawn(false);
		npc.getRouteFinder().routeEntity(target);
		npc.hitListener = new HitListener().preDefend(this::preDefend);
	}

	private void preDefend(Hit hit) {
		if (hit.attacker != null && hit.attacker.isPlayer()) {
			if (attackerPoisoned((Player) hit.attacker)) {
				hit.type = HitType.POISON;
				World.startEvent(e -> {
					npc.animate(8078);
					e.delay(2);
					if (npc.getPosition().isWithinDistance(npc.getPosition(), 2))
						npc.hit(new Hit(null, AttackStyle.MAGICAL_MELEE).randDamage(6, 24));
					npc.remove();
				});
			} else
				hit.damage = 0;
		}
	}

	private boolean attackerPoisoned(Player player) {
		if (player.getEquipment().get(Equipment.SLOT_HAT) != null) {
			if (player.getEquipment().get(Equipment.SLOT_HAT).getDef().name.contains("serpentine") || player.getEquipment().get(Equipment.SLOT_HAT).getDef().name.contains("tanzanite") || player.getEquipment().get(Equipment.SLOT_HAT).getDef().name.contains("magma")) {
				return true;
			}
			return true;
		}
		if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && player.getEquipment().get(Equipment.SLOT_WEAPON).getDef().name.contains("(p)")) {
			return true;
		} else if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && player.getEquipment().get(Equipment.SLOT_WEAPON).getDef().name.contains("toxic")) {
			return true;
		} else if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && player.getEquipment().get(Equipment.SLOT_WEAPON).getDef().name.contains("(p+)")) {
			return true;
		} else if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && player.getEquipment().get(Equipment.SLOT_WEAPON).getDef().name.contains("(p++)")) {
			return true;
		} else if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_WEAPON), AttributeTypes.VENOM_TIPPED)) {
			return true;
		}
		return false;
	}

	public void process() {
		if (npc.getPosition().distance(target.getPosition()) < 5) {
			if (healing) return;
			healing = true;
			npc.startEvent(event -> {
				npc.face(target);
				event.delay(3);
				target.hit(new Hit(HitType.HEAL).randDamage(8, 15));
				healing = false;
			});
		} else
			npc.getRouteFinder().routeEntity(target);
	}
}

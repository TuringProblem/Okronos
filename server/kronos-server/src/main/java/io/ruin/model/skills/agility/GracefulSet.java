package io.ruin.model.skills.agility;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;

public class GracefulSet {

	public static double GracefulSet(Player player) {
		double bonus = 1.0;
		Item hood = player.getEquipment().get(Equipment.SLOT_HAT);
		Item garb = player.getEquipment().get(Equipment.SLOT_CHEST);
		Item robe = player.getEquipment().get(Equipment.SLOT_LEGS);
		Item boots = player.getEquipment().get(Equipment.SLOT_FEET);
		Item cape = player.getEquipment().get(Equipment.SLOT_CAPE);
		Item gloves = player.getEquipment().get(Equipment.SLOT_HANDS);

		if (hood != null && (hood.getId() == 11850 || hood.getId() == 13579 || hood.getId() == 13591 || hood.getId() == 13603 || hood.getId() == 13615 || hood.getId() == 13627))
			bonus += 0.4;
		if (garb != null && (garb.getId() == 11854 || garb.getId() == 13583 || garb.getId() == 13595 || garb.getId() == 13607 || garb.getId() == 13619 || garb.getId() == 13631))
			bonus += 0.8;
		if (robe != null && (robe.getId() == 11856 || robe.getId() == 13585 || robe.getId() == 13597 || robe.getId() == 13609 || robe.getId() == 13621 || robe.getId() == 13633))
			bonus += 0.6;
		if (boots != null && (boots.getId() == 11860 || boots.getId() == 13589 || boots.getId() == 13601 || boots.getId() == 13613 || boots.getId() == 13625 || boots.getId() == 13637))
			bonus += 0.2;
		if (cape != null && (cape.getId() == 11852 || cape.getId() == 13581 || cape.getId() == 13593 || cape.getId() == 13605 || cape.getId() == 13617 || cape.getId() == 13629))
			bonus += 0.6;
		if (gloves != null && (gloves.getId() == 11858 || gloves.getId() == 13587 || gloves.getId() == 13599 || gloves.getId() == 13611 || gloves.getId() == 13623 || gloves.getId() == 13635))
			bonus += 0.8;

		/* Whole set gives an additional 0.5% exp bonus */
		if (bonus >= 3.0)
			bonus += 0.6;

		return bonus;
	}

}

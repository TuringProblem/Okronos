package io.ruin.model.entity.npc;

import io.ruin.model.combat.Hit;
import io.ruin.model.entity.player.Player;
import io.ruin.model.item.actions.impl.pet.Pet;
import net.rsprot.protocol.game.outgoing.info.AvatarPriority;

public class PetNPC extends NPC {

	public Player owner;

	public Pet petType;

	public PetNPC(Player owner, Pet pet) {
		super(pet.npcId);
		this.owner = owner;
		this.petType = pet;
		init();
	}

	@Override
	public void process() {
		super.process();
		if (petType.ordinal() < 25) {
			if (getCombat() != null && (owner.wildernessLevel > 1 || getCombat().getTarget() != null && getCombat().getTarget().getPosition().distance(getPosition()) > 28)) {
				getCombat().reset();
			}
			if (owner.wildernessLevel > 1) {

			} else {
				if (getCombat() != null && (getCombat().getTarget() == null || (owner.getCombat().getTarget() != null && getCombat().getTarget() != owner.getCombat().getTarget()))) {
					getCombat().setTarget(owner.getCombat().getTarget());
					getCombat().attack();
				}
				if (getCombat() != null && getCombat().getTarget() != null) {
					face(getCombat().getTarget());
				}
			}
		}
	}

	@Override
	public boolean isRandomWalkAllowed() {
		return false;
	}

	@Override
	public int hit(Hit... hits) {

		return 0;
	}

	@Override
	public AvatarPriority getSpawnAvatarPriority() {
		return AvatarPriority.LOW;
	}

}

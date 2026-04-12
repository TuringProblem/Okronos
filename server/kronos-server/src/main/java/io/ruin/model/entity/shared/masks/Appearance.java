package io.ruin.model.entity.shared.masks;

import io.ruin.cache.NPCType;
import io.ruin.cache.ObjType;
import io.ruin.model.combat.CombatUtils;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.Renders;
import io.ruin.model.entity.shared.UpdateMask;
import io.ruin.model.item.containers.Equipment;
import lombok.Getter;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Appearance extends UpdateMask {
	private static final Logger logger = LoggerFactory.getLogger(Appearance.class);

	private transient Player player;

	/**
	 * Styles
	 * 0=hair
	 * 1=beard
	 * 2=top
	 * 3=arms
	 * 4=wrists
	 * 5=legs
	 * 6=shoes
	 */
	public int[] styles = { 0, 10, 18, 26, 33, 36, 42 };

	/**
	 * Colors
	 * 0=hair
	 * 1=torso
	 * 2=legs
	 * 3=shoes
	 * 4=skin
	 */
	public int[] colors = new int[5];

	/**
	 * Gender
	 * 0=male
	 * 1=female
	 */
	private int gender = 0;

	/**
	 * Skull icon
	 */
	private transient int skullIcon = -1;

	/**
	 * Prayer icon
	 */
	private transient int prayerIcon = -1;

	/**
	 * Npc id
	 */
	@Getter
	private transient int npcId = -1;

	/**
	 * Custom renders
	 */
	private transient int[] customRenders;

	/**
	 * Mask info
	 */
	private transient boolean update = true;

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public boolean isMale() {
		return gender == 0;
	}

	public void setSkullIcon(int skullIcon) {
		this.skullIcon = skullIcon;
		update();
	}

	public int getSkullIcon() {
		return skullIcon;
	}

	public void setPrayerIcon(int prayerIcon) {
		this.prayerIcon = prayerIcon;
		update();
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
		if (npcId == -1)
			removeCustomRenders();
		else {
			var def = NPCType.get(npcId);
			setCustomRenders(
					def.standAnimation, // Standing
					-1, // step forward
					def.walkAnimation, // walking
					def.walkBackAnimation, // Step back
					def.walkLeftAnimation, // step left
					def.walkRightAnimation, // step right
					-1 // run
			);
		}
		update();
	}

	public void setCustomRenders(int... renders) {
		this.customRenders = renders;
	}

	public void removeCustomRenders() {
		this.customRenders = null;
	}

	public void update() {
		update = true;
	}

	@Override
	public void reset() {
		update = false;
	}

	@Override
	public boolean hasUpdate(boolean added) {
		return update || added;
	}

	public void setColor(int index, int color) {
		if (index > this.colors.length - 1) {
			return;
		}

		if (color > 255) {
			logger.warn("Invalid color: " + color, new IllegalStateException());
			return;
		}

		this.colors[index] = color;
	}

	@Override
	public void send(Player player) {
		var avatar = player.avatarExtended();
		avatar.setTransmogrification(this.npcId);
		avatar.setBodyType(this.gender == 0 ? 0 : 1);
		avatar.setPronoun(this.gender);
		avatar.setName(this.player.getNameWithRanks());
		avatar.setCombatLevel(CombatUtils.getCombatLevel(player));
		avatar.setSkullIcon(this.skullIcon);
		avatar.setOverheadIcon(this.prayerIcon);

		for (var i = 0; i < styles.length; i++) {
			avatar.setIdentKit(i, styles[i]);
		}

		for (int i = 0; i < colors.length; i++) {
			avatar.setColour(i, colors[i] & 0xff);
		}

		for (int slot = 0; slot < 11; slot++) {
			var itemId = player.getEquipment().getId(slot);
			var itemDefinition = ObjType.get(itemId);
			if (itemDefinition == null) {
				System.err.println("Unable to find definition for equipment item " + itemId);
				continue;
			}

			var wearPos2 = itemDefinition.wearPos2;
			var wearPos3 = itemDefinition.wearPos3;

			if (itemDefinition.hideArms) {
				wearPos2 = 6;
			}

			if (itemDefinition.hideHair) {
				wearPos2 = 8;
			}

			if (itemDefinition.hideBeard) {
				wearPos3 = 11;
			}

			avatar.setWornObj(slot, itemId, wearPos2, wearPos3);
		}

		var r = Renders.DEFAULT;
		if (this.customRenders != null) {
			r = this.customRenders;
		} else {
			var def = player.getEquipment().getDef(Equipment.SLOT_WEAPON);
			if (def != null && def.weaponType != null) {
				r = def.weaponType.renderAnimations;
			}
		}

		player.avatarExtended().setBaseAnimationSet(r[0], r[1], r[2], r[3], r[4], r[5], r[6]);
	}

	@Override
	public int get(boolean playerUpdate) {
		return 128;
	}

}

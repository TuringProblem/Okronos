package io.ruin.model.inter.handlers;

import io.ruin.api.utils.Random;
import io.ruin.cache.SeqType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.LoginListener;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.SlotAction;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.item.containers.Equipment;

public enum TabEmote {

	YES(0, 855),
	NO(1, 856),
	BOW(2, 858),
	ANGRY(3, 859),
	THINK(4, 857),
	WAVE(5, 863),
	SHRUG(6, 2113),
	CHEER(7, 862),
	BECKON(8, 864),
	LAUGH(9, 861),
	JUMP_FOR_JOY(10, 2109),
	YAWN(11, 2111),
	DANCE(12, 866),
	JIG(13, 2106),
	SPIN(14, 2107),
	HEAD_BANG(15, 2108),
	CRY(16, 860),
	BLOW_KISS(17, 1374),
	PANIC(18, 2105),
	RASPBERRY(19, 2110),
	CLAP(20, 865),
	SALUTE(21, 2112),
	GOBLIN_BOW(22, 2127),
	GOBLIN_SALUTE(23, 2128),
	GLASS_BOX(24, 1131),
	CLIMB_ROPE(25, 1130),
	LEAN(26, 1129),
	GLASS_WALL(27, 1128),
	IDEA(28, 4276),
	STOMP(29, 4278),
	FLAP(30, 4280),
	SLAP_HEAD(31, 4275),
	ZOMBIE_WALK(32, 3544),
	ZOMBIE_DANCE(33, 3543),
	SCARED(34, 2836),
	RABBIT_HOP(35, 6111),
	SIT_UP(36, 2763),
	PUSH_UP(37, 2762),
	STAR_JUMP(38, 2761),
	JOG(39, 2764),
	FLEX(40, 8917),

	ZOMBIE_HAND(41, 1708, 320),
	HYPER_MOBILE_DRINKER(42, 7131),
	SKILL_CAPE(43, -1, -1),
	AIR_GUITAR(44, 4751, 1239),
	URI_TRANSFORM(45, -1, -1),
	SMOOTH_DANCE(46, 7533, -1),
	CRAZY_DANCE(47, -1, -1),
	PREMIER_SHIELD(48, 7751, 1412),

	EXPLORE(49, 8541),

	RELIC_UNLOCK(50, 8524, 1835);

	public int slot, animationID, gfxId;

	TabEmote(int slot, int animationID) {
		this(slot, animationID, -1);
	}

	TabEmote(int slot, int animationId, int gfxId) {
		this.slot = slot;
		this.animationID = animationId;
		this.gfxId = gfxId;
	}

	public static final TabEmote[] VALUES = values();

	public static void register() {
		InterfaceHandler.register(Interface.EMOTE, h -> h.actions[2] = (SlotAction) (p, slot) -> {
			if (p.isLocked() || p.getCombat().isDefending(17)) {
				p.sendMessage("You cannot perform an emote right now!");
				return;
			}
			if (slot < 0 || slot >= VALUES.length)
				return;
			TabEmote emote = VALUES[slot];
			if (emote == null)
				return;
			if (p.emoteDelay.isDelayed())
				return;

			int emoteId = emote.animationID;
			if (slot == TabEmote.SKILL_CAPE.slot) {
				int skillCapeAnimation = skillCapeAnim(p);
				int skillCapeGraphic = skillCapeGraphic(p);
				if (skillCapeAnimation == -1) {
					p.sendMessage("You need to be wearing a Skill Cape to perform this emote.");
					return;
				}
				emoteId = skillCapeAnimation;
				if (skillCapeGraphic != -1) {
					p.graphics(skillCapeGraphic);
				}
				p.startEvent(event -> {
					p.lock();
					SeqType def = SeqType.get(skillCapeAnimation);
					if (def != null)
						event.delay(def.getTickDelay());
					p.unlock();
				});

			} else if (slot == TabEmote.DANCE.slot && p.getEquipment().getId(Equipment.SLOT_LEGS) == 10394) {
				emoteId = 5316;
			} else if (slot == TabEmote.CRAZY_DANCE.slot) {
				if (Random.rollDie(2))
					emoteId = 7536;
				else
					emoteId = 7537;
			} else if (slot == TabEmote.URI_TRANSFORM.slot) {
				new playerUriTransform(p);
			}

			p.animate(emoteId);
			if (emote.gfxId != -1)
				p.graphics(emote.gfxId);

			if (p.yesDelay.isDelayed())
				return;
			if (emote == YES && !p.yesDelay.isDelayed()) {
				p.yesDelay.delay(3);
			}
			if (emote == NO && !p.noDelay.isDelayed()) {
				p.noDelay.delay(2);
			}

			SeqType def = SeqType.get(emote.animationID);
			if (def != null)
				p.emoteDelay.delay(def.getTickDelay());
			p.resetSteps();
		});

		LoginListener.register(player -> {
			//Unlock all the emotes
			VarPlayerRepository.EMOTES.set(player, 16777215);
			VarPlayerRepository.UNLOCK_GOBLIN_BOW_AND_SALUTE_EMOTE.set(player, 7);
			VarPlayerRepository.UNLOCK_FLAP_EMOTE.set(player, 1);
			VarPlayerRepository.UNLOCK_SLAP_HEAD_EMOTE.set(player, 1);
			VarPlayerRepository.UNLOCK_IDEA_EMOTE.set(player, 1);
			VarPlayerRepository.UNLOCK_STAMP_EMOTE.set(player, 1);
		});
	}

	private static int skillCapeAnim(Player player) {
		int capeId = player.getEquipment().getId(Equipment.SLOT_CAPE);
		if (capeId == 9747 || capeId == 9748 || capeId == 30232) //Attack
			return 4959;
		if (capeId == 9753 || capeId == 9754 || capeId == 30240) //Defence
			return 4961;
		if (capeId == 9750 || capeId == 9751 || capeId == 30270) //Strength
			return 4981;
		if (capeId == 9768 || capeId == 9769 || capeId == 30252) //Hitpoints
			return 4971;
		if (capeId == 9756 || capeId == 9757 || capeId == 30262) //Ranged
			return 4973;
		if (capeId == 9762 || capeId == 9763 || capeId == 30256) //Magic
			return 4939;
		if (capeId == 9759 || capeId == 9760 || capeId == 30260) //Prayer
			return 4979;
		if (capeId == 9801 || capeId == 9802 || capeId == 30236) //Cooking
			return 4955;
		if (capeId == 9807 || capeId == 9808 || capeId == 30274) //Woodcutting
			return 4957;
		if (capeId == 9783 || capeId == 9784 || capeId == 30248) //Fletching
			return 4937;
		if (capeId == 9798 || capeId == 9799 || capeId == 30246) //Fishing
			return 4951;
		if (capeId == 9804 || capeId == 9805 || capeId == 30244) //Firemaking
			return 4975;
		if (capeId == 9780 || capeId == 9781 || capeId == 30238) //Crafting
			return 4949;
		if (capeId == 9795 || capeId == 9796 || capeId == 30268) //Smithing
			return 4943;
		if (capeId == 9792 || capeId == 9793 || capeId == 30258) //Mining
			return 4941;
		if (capeId == 9774 || capeId == 9775 || capeId == 30250) //Herblore
			return 4969;
		if (capeId == 9771 || capeId == 9772 || capeId == 30230) //Agility
			return 4977;
		if (capeId == 9777 || capeId == 9778 || capeId == 30272) //Thieving
			return 4965;
		if (capeId == 9786 || capeId == 9787 || capeId == 30266) //Slayer
			return 4967;
		if (capeId == 9810 || capeId == 9811 || capeId == 30242) //Farming
			return 4963;
		if (capeId == 9765 || capeId == 9766 || capeId == 30264) //Runecrafting
			return 4947;
		if (capeId == 9789 || capeId == 9790 || capeId == 30234) //Construction
			return 4953;
		if (capeId == 9948 || capeId == 9949 || capeId == 30254) //Hunter
			return 5158;
		if (capeId == 9813 || capeId == 13068) //Quest
			return 4945;
		if (capeId == 19476 || capeId == 13069) //Achievement
			return 7121;
		if (capeId == 13280 || capeId == 13329 || capeId == 13331 || capeId == 13333 || capeId == 13335
			|| capeId == 13337 || capeId == 13342 || capeId == 20760 || capeId == 21776 || capeId == 21780
			|| capeId == 21784 || capeId == 21898 || capeId == 21285) //Max cape
			return 7121;
		return -1;
	}

	private static int skillCapeGraphic(Player player) {
		int capeId = player.getEquipment().getId(Equipment.SLOT_CAPE);
		if (capeId == 9747 || capeId == 9748 || capeId == 30232) //Attack
			return 823;
		if (capeId == 9753 || capeId == 9754 || capeId == 30240) //Defence
			return 824;
		if (capeId == 9750 || capeId == 9751 || capeId == 30270) //Strength
			return 828;
		if (capeId == 9768 || capeId == 9769 || capeId == 30252) //Hitpoints
			return 833;
		if (capeId == 9756 || capeId == 9757 || capeId == 30262) //Ranged
			return 832;
		if (capeId == 9762 || capeId == 9763 || capeId == 30256) //Magic
			return 813;
		if (capeId == 9759 || capeId == 9760 || capeId == 30260) //Prayer
			return 829;
		if (capeId == 9801 || capeId == 9802 || capeId == 30236) //Cooking
			return 821;
		if (capeId == 9807 || capeId == 9808 || capeId == 30274) //Woodcutting
			return 822;
		if (capeId == 9783 || capeId == 9784 || capeId == 30248) //Fletching
			return 812;
		if (capeId == 9798 || capeId == 9799 || capeId == 30246) //Fishing
			return 819;
		if (capeId == 9804 || capeId == 9805 || capeId == 30244) //Firemaking
			return 831;
		if (capeId == 9780 || capeId == 9781 || capeId == 30238) //Crafting
			return 818;
		if (capeId == 9795 || capeId == 9796 || capeId == 30268) //Smithing
			return 815;
		if (capeId == 9792 || capeId == 9793 || capeId == 30258) //Mining
			return 814;
		if (capeId == 9774 || capeId == 9775 || capeId == 30250) //Herblore
			return 835;
		if (capeId == 9771 || capeId == 9772 || capeId == 30230) //Agility
			return 830;
		if (capeId == 9777 || capeId == 9778 || capeId == 30272) //Thieving
			return 826;
		if (capeId == 9786 || capeId == 9787 || capeId == 30266) //Slayer
			return 827;
		if (capeId == 9810 || capeId == 9811 || capeId == 30242) //Farming
			return 825;
		if (capeId == 9765 || capeId == 9766 || capeId == 30264) //Runecrafting
			return 817;
		if (capeId == 9789 || capeId == 9790 || capeId == 30234) //Construction
			return 820;
		if (capeId == 9948 || capeId == 9949 || capeId == 30254) //Hunter
			return 907;
		if (capeId == 9813 || capeId == 13068) //Quest
			return 816;
		if (capeId == 19476 || capeId == 13069) //Achievement
			return 1286;
		if (capeId == 13280 || capeId == 13329 || capeId == 13331 || capeId == 13333 || capeId == 13335 ||
			capeId == 13337 || capeId == 13342 || capeId == 20760 || capeId == 21776 || capeId == 21780 ||
			capeId == 21784 || capeId == 21898 || capeId == 21285) //Max cape
			return 1286;
		return -1;
	}

}

package io.ruin.model.skills.magic.spells.modern;

import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.TheAlchemist;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.presets.Preset;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.model.inter.dialogue.ItemDialogue;

import java.util.Arrays;

import static io.ruin.cache.ItemID.COINS_995;

public class Alchemy extends Spell {

	public enum Level {
		LOW(712, 763, 21, 31, 3, Rune.NATURE.toItem(1), Rune.FIRE.toItem(3)),
		HIGH(713, 764, 55, 65, 5, Rune.NATURE.toItem(1), Rune.FIRE.toItem(5));

		private final int anim, gfx, levelReq;
		private final double xp;
		private final Item[] runes;
		private final int delay;

		Level(int anim, int gfx, int levelReq, double xp, int delay, Item... runes) {
			this.anim = anim;
			this.gfx = gfx;
			this.levelReq = levelReq;
			this.xp = xp;
			this.runes = runes;
			this.delay = delay;
		}
	}

	public Alchemy(boolean low) {
		Level level = low ? Level.LOW : Level.HIGH;
		registerItem(level.levelReq, level.xp, true, level.runes, (player, item) -> {
			if (player.alchDelay.isDelayed()) {
				// queue next alch
				player.startEvent(event -> {
					event.delay(player.alchDelay.remaining());
					itemAction.accept(player, item);
				});
				return false;
			}

			var value = alchValue(player, item, level);
			if (value >= VarPlayerRepository.MINIMUM_ALCH_TRIGGER_VALUE.get(player)) {
				alchWithWarning(player, item, level, value);
				return false;
			}

			return alch(player, item, level, value);
		});
	}

	public void alchWithWarning(Player player, Item item, Level level, int value) {
		var dialogue = new ItemDialogue().one(
				item.getId(),
				"That item is considered valuable.<br> Really cast Alchemy on it?");
		Runnable onDialogueContinued = () -> {
			cast(player, item, level.levelReq, level.xp, true, level.runes, (p, i) -> {
				return alch(p, i, level, value);
			});
		};
		player.dialogue(dialogue, onDialogueContinued);
	}

	public static boolean alch(Player player, Item item, Level level, int value) {

		if (!alchValidate(player, level, item, value)) {
			return false;
		}

		alchCast(player, level, item, value);
		return true;
	}

	private static int alchValue(Player player, Item item, Level level) {
		var pet = Pet.getByItemId(item.getId());
		if (pet != null)
			return 1;
		var def = item.getDef().isNote() ? item.getDef().fromNote() : item.getDef();
		return level == Level.LOW ? def.lowAlchValue : def.highAlchValue;
	}

	private static boolean alchValidate(Player player, Level level, Item item, int value) {
		if (value == 0 || item.getId() == COINS_995) {
			player.sendMessage("You can't alchemise this item.");
			return false;
		}

		if (item.getAmount() == 1 && item.getDef().stackable && !player.getInventory().hasRoomFor(COINS_995)) {
			player.sendMessage("Not enough space in your inventory.");
			return false;
		}

		if ((item.getId() == Rune.NATURE.getId() && item.getAmount() < level.runes[0].getAmount() + 1)
				|| (item.getId() == Rune.FIRE.getId() && item.getAmount() < level.runes[1].getAmount() + 1)) {
			player.sendMessage("You don't have the required runes to cast this spell.");
			return false;
		}

		if (item.getDef().free) {
			player.sendMessage("You can't alchemise this item.");
			return false;
		}

		if (!item.getDef().tradeable && item.getDef().pet == null ) {
			player.sendMessage("You can't alchemise this item.");
			return false;
		}

		if (player.lastAlchPosition == null) {
			player.lastAlchPosition = player.getPosition().copy();
		} else if (player.lastAlchPosition.distance(player.getPosition()) < 1) {
			if (player.alchsInSamePlace++ >= 50) {
				player.sendMessage("You need to alch on a different tile before you can alch here again.");
				return false;
			}
		} else {
			player.lastAlchPosition = player.getPosition().copy();
			player.alchsInSamePlace = 0;
		}

		return true;
	}

	private static void alchCast(Player player, Level level, Item item, int value) {
		if (item.getDef().stackable) {
			item.remove(1);
		} else {
			item.remove();
		}
		player.animate(level.anim);
		player.graphics(level.gfx, 46, 0);
		if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_ALCHEMIST)) {
			int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_ALCHEMIST);
			TheAlchemist c = (TheAlchemist) player.getPlayerPerkHandler().getActivePerks(player).get(perkIndex)
					.getPerk(player);
			value *= c.alchemyMultiplier();
		}
		player.getInventory().add(COINS_995, value);
		player.getPacketSender().sendClientScript(915, "i", 6);
		player.alchDelay.delay(level.delay);
	}

}

package io.ruin.model.activities.raids.toa;

import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceAction;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.impl.storage.LootingBag;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.stat.Stat;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;

import static io.ruin.model.inter.AccessMasks.*;

public class Supplies {
	public static void open(Player player) {
		player.openInterface(ToplevelComponent.SIDEMODAL, 1125);
		int itemsSent = 0;
		int startingItemComponent = 14;
		int startContainerId = 766;
		for (Item item : player.getToaSupplies()) {
			if (itemsSent == 25)
				break;
			player.getPacketSender().sendClientScript(
				149, "IviiiIsssss",
				1125 << 16 | startingItemComponent, startContainerId,
				4, 7, 1, -1, "Withdraw-1", "Withdraw-5", "Withdraw-10", "Withdraw-X", "Withdraw-All"
			);


			player.getPacketSender().sendItems(
				-1,
				startingItemComponent,
				startContainerId,
				item
			);
			itemsSent++;
			startContainerId++;
			startingItemComponent++;
		}

	}

	private static void drinkAmbrosia(Player player, Item ambrosia) {
		if (player.potDelay.isDelayed() || player.karamDelay.isDelayed())
			return;
		player.animate(829);
		if (ambrosia.getId() == 27347)
			ambrosia.setId(27349);
		else ambrosia.remove();
		player.getStats().restore(false);
		player.getStats().get(StatType.Prayer).restore();
		player.potDelay.delay(2);

	}

	public static void finished(Player player) {
		player.addEvent(event -> {
			event.delay(500);
			player.sendMessage("<col=FF0000>Your crushed salts effect has worn out.");
		});
	}

	private static void crushSalts(Player player, Item salts) {
		if (salts.getId() == 27343)
			salts.setId(27345);
		else salts.remove();
		player.animate(829);
		if (player.saltCooldown.isDelayed()) {
			player.sendMessage("You're still feeling the effects of the salts.");
		} else {
			player.saltCooldown.delay(500);
			World.startEvent(e -> {
				for (int i = 0; i < 20; i++) {
					player.getStats().get(StatType.Attack).boost(11, 0.3);
					player.getStats().get(StatType.Strength).boost(11, 0.3);
					player.getStats().get(StatType.Defence).boost(11, 0.3);
					player.getStats().get(StatType.Ranged).boost(11, 0.3);
					player.getStats().get(StatType.Magic).boost(11, 0.3);
					e.delay(25);
					if (i == 19)
						finished(player);
				}
			});
		}

	}

	private static void drinkTears(Player player, Item tears) {
		if (player.potDelay.isDelayed() || player.karamDelay.isDelayed())
			return;
		player.animate(829);
		if (tears.getId() == 27327)
			tears.setId(27329);
		else if (tears.getId() == 27329)
			tears.setId(27331);
		else if (tears.getId() == 27331)
			tears.setId(27333);
		else if (tears.getId() == 27333)
			tears.remove();
		player.potDelay.delay(2);
		for (StatType type : StatType.VALUES) {
			if (type == StatType.Hitpoints)
				continue;
			Stat stat = player.getStats().get(type);
			if (stat.currentLevel < stat.fixedLevel) {
				if (type == StatType.Prayer && player.getEquipment().getId(Equipment.SLOT_RING) == 13202) // ring of the gods
					stat.restore(8, 0.27);
				else
					stat.restore(8, 0.25);
				continue;
			}
			if (type != StatType.Prayer)
				stat.restore(10, 0.30);
		}
	}

	private static void drinkLiquidAdrenaline(Player p, Item liquid) {
		if (p.liquidAdrenalineDelay.remaining() > 1) {
			p.sendMessage("You're still feeling the effects of the liquid adrenaline.");
			return;
		}
		if (liquid.getId() == 27339)
			liquid.setId(27341);
		else liquid.remove();
		p.liquidAdrenalineDelay.delay(250);
		p.sendMessage("You are now feeling the effects of the liquid adrenaline.");
		p.animate(829);
		p.potDelay.delay(2);

	}

	private static void useSilkDressing(Player player, Item dressing) {
		if (player.silkDressingDelay.isDelayed()) {
			player.sendMessage("You're still feeling the effects of the silk dressing.");
			return;
		}
		if (dressing.getId() == 27323)
			dressing.setId(27325);
		else dressing.remove();
		player.silkDressingDelay.delay(100);
		World.startEvent(e -> {
			for (int i = 0; i < 20; i++) {
				if (player.getHp() > 0)
					player.hit(new Hit(HitType.HEAL).fixedDamage(5));
				e.delay(5);
			}
		});
	}

	private static void useBlessedScarab(Player player, Item scarab) {
		if (player.blessedScarabDelay.isDelayed()) {
			player.sendMessage("You're still feeling the effects of the blessed crystal scarab.");
			return;
		}
		if (scarab.getId() == 27335)
			scarab.setId(27337);
		else scarab.remove();
		player.blessedScarabDelay.delay(100);
		World.startEvent(e -> {
			for (int i = 0; i < 9; i++) {
				Stat stat = player.getStats().get(StatType.Prayer);
				stat.restore(7, 0.05);
				if (stat.currentLevel > player.getStats().get(StatType.Prayer).fixedLevel)
					stat.currentLevel = player.getStats().get(StatType.Prayer).fixedLevel;
				player.getStats().get(StatType.Prayer).alter(stat.currentLevel);
				e.delay(4);
			}
		});
	}

	private static void drinkNectar(Player p, Item tears) {
		if (p.potDelay.isDelayed() || p.karamDelay.isDelayed())
			return;
		p.animate(829);
		if (tears.getId() == 27315)
			tears.setId(27317);
		else if (tears.getId() == 27317)
			tears.setId(27319);
		else if (tears.getId() == 27319)
			tears.setId(27321);
		else if (tears.getId() == 27321)
			tears.remove();

		p.getStats().get(StatType.Hitpoints).boost(2, 0.15);
		p.getStats().get(StatType.Defence).boost(2, 0.20);
		p.getStats().get(StatType.Attack).drain(0.10);
		p.getStats().get(StatType.Strength).drain(0.10);
		p.getStats().get(StatType.Ranged).drain(0.10);
		p.getStats().get(StatType.Magic).drain(0.10);
		p.potDelay.delay(2);
	}

	private static void withdraw(Player player, int slot, int amount) {
		if (slot < 0 || slot >= player.getToaSupplies().size())
			return;
		Item item = player.getToaSupplies().get(slot);
		if (item == null)
			return;
		if (amount < 1)
			amount = 1;
		if (amount > item.getAmount())
			amount = item.getAmount();
		if (player.getInventory().getFreeSlots() == 0 && !player.getInventory().contains(item.getId())) {
			player.sendMessage("You do not have enough inventory space to do that.");
			return;
		}
		player.getToaSupplies().remove(item);
		player.getInventory().add(item.getId(), amount);
	}

	private static void withdrawAll(Player player) {
		boolean canWithdrawAll = true;
		for (int i = player.getToaSupplies().size() - 1; i >= 0; i--) {
			Item item = player.getToaSupplies().get(i);
			if (item == null)
				continue;
			if (player.getInventory().hasRoomFor(item.getId(), item.getAmount())) {
				player.getToaSupplies().remove(item);
				player.getInventory().add(item.getId(), item.getAmount());
			} else {
				canWithdrawAll = false;
			}
		}
		if (!canWithdrawAll)
			player.sendMessage("You do not have enough inventory space to withdraw everything.");
	}

	public static void register() {
		ItemAction.registerInventory(27327, 1, Supplies::drinkTears);
		ItemAction.registerInventory(27329, 1, Supplies::drinkTears);
		ItemAction.registerInventory(27331, 1, Supplies::drinkTears);
		ItemAction.registerInventory(27333, 1, Supplies::drinkTears);
		ItemAction.registerInventory(27339, 1, Supplies::drinkLiquidAdrenaline);
		ItemAction.registerInventory(27341, 1, Supplies::drinkLiquidAdrenaline);
		ItemAction.registerInventory(27343, 1, Supplies::crushSalts);
		ItemAction.registerInventory(27345, 1, Supplies::crushSalts);
		ItemAction.registerInventory(27347, 1, Supplies::drinkAmbrosia);
		ItemAction.registerInventory(27349, 1, Supplies::drinkAmbrosia);
		ItemAction.registerInventory(27315, 1, Supplies::drinkNectar);
		ItemAction.registerInventory(27317, 1, Supplies::drinkNectar);
		ItemAction.registerInventory(27319, 1, Supplies::drinkNectar);
		ItemAction.registerInventory(27321, 1, Supplies::drinkNectar);
		ItemAction.registerInventory(27323, 1, Supplies::useSilkDressing);
		ItemAction.registerInventory(27325, 1, Supplies::useSilkDressing);
		ItemAction.registerInventory(27335, 1, Supplies::useBlessedScarab);
		ItemAction.registerInventory(27337, 1, Supplies::useBlessedScarab);
		ItemAction.registerInventory(27314, "open", (player, item) -> open(player));
		ItemAction.registerInventory(27314, "withdraw 1", (player, item) -> withdraw(player, 0, 1));
		ItemAction.registerInventory(27314, "withdraw all", (player, item) -> withdrawAll(player));
		InterfaceHandler.register(1125, h -> {
			for (int i = 14; i < 39; i++) {
				h.actions[i] = (DefaultAction) (player, option, slot, itemId) -> {
					Item item = player.getToaSupplies().get(slot);
					if (item == null)
						return;
					if (option == 1) {
						withdraw(player, slot, 1);
					} else if (option == 2) {
						withdraw(player, slot, 5);
					} else if (option == 3) {
						withdraw(player, slot, 10);
					} else if (option == 4) {
						player.integerInput("Enter amount:", amt -> withdraw(player, slot, amt));
					} else if (option == 5) {
						withdraw(player, slot, item.getAmount());
					}
					item.examine(player);
				};

			}
		});
	}
}

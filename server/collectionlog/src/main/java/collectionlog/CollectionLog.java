package collectionlog;

import com.google.gson.annotations.Expose;
import io.ruin.cache.ItemID;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.notify.NotificationInterface;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.utility.FormatMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.ExtensionMethod;

import java.util.HashMap;
import java.util.Map;

@ExtensionMethod(Attributes.class)
public class CollectionLog {

	@Setter
	private int currentTab;
	@Setter
	private int currentStruct;
	@Setter
	private int currentEntry;

	@Expose
	@Getter
	public Map<Integer, Integer> collected = new HashMap<>();

	public int getCollected(int itemId) {
		return collected.getOrDefault(itemId, 0);
	}

	public void open(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.COLLECTION_LOG);
		sendTab(player, CollectionLogInfo.BOSS);
	}

	public void sendTab(Player player, CollectionLogInfo info) {
		player.getPacketSender().sendClientScript(2389, "i", currentTab = info.ordinal());
		selectEntry(player, 0, info);
		info.sendAccessMasks(player);
	}

	public void clear(Player player) {
		player.uniqueDrops = new HashMap<>();
		this.clearCollectedItems(player);
	}

	public void remove(Player player, Item item) {
		player.uniqueDrops.remove(item.getId());
		this.collected.remove(item.getId());
	}

	public void addAll(Player player, Item... items) {
		for (var item : items) {
			this.add(player, item);
		}
	}

	public void add(Player player, Item item) {
		boolean unique = false;
		if (player.uniqueDrops.get(item.getId()) == null) {
			player.uniqueDrops.put(item.getId(), item.getAmount());
			unique = true;
		} else {
			player.uniqueDrops.replace(item.getId(), player.uniqueDrops.get(item.getId()) + item.getAmount());
		}

		if (unique && CollectionLogData.isCollectionLogSlotItem(item)) {

			var notifySetting = VarPlayerRepository.COLLECTION_LOG_NOTIFICATION.get(player);

			if ((notifySetting & 0b01) != 0) {
				notifyNewItemAdded(player, item);
			}

			if ((notifySetting & 0b10) != 0) {
				notifyNewItemAddedPopup(player, item);
			}
		}

		player.collectionLog().collect(player, item.getId(), item.getAmount());
	}

	private void notifyNewItemAdded(Player player, Item item) {
		player.sendMessage("New item added to your collection log: <col=ff0000>" + item.getDef().name);
	}

	private void notifyNewItemAddedPopup(Player player, Item item) {
		var msg = "New item:<br><br>";
		msg += FormatMessage.sendColoredMessage(item.getDef().getName(), "FFFFFF");
		NotificationInterface.open(player, "Collection Log", msg);
	}

	public void collect(Player player, int id) {
		collect(player, id, 1);
	}

	/**
	 * Collects an item into the collection log.
	 *
	 * @param id     The item id.
	 * @param amount The amount.
	 */
	public void collect(Player player, int id, int amount) {
		collect(new Item(id, amount), player);
	}

	/**
	 * Collects the items into the collection log.
	 *
	 * @param items The items.
	 */
	public void collect(Player player, Item... items) {
		for (Item item : items) {
			collect(item, player);
		}
	}

	/**
	 * Collects an item into the collection log.
	 *
	 * @param item The item being collected.
	 */
	public void collect(Item item, Player player) {
		if (!item.getDef().collectable) {
			return;
		}

		int amount = collected.getOrDefault(item.getId(), 0);

		collected.put(item.getId(), amount + item.getAmount());
		collected.forEach((integer, integer2) -> {
			if (item.getId() == integer) {
				if (amount >= 1)
					return;
			}
		});
	}

	public void clearCollectedItems(Player player) {
		collected.clear();
		VarPlayerRepository.COLLECTION_COUNT.set(player, 0);
	}

	public void selectEntry(Player player, int slot, CollectionLogInfo info) {
		info.sendKillCount(player, slot);
		info.sendItems(player, slot);
		player.getPacketSender().sendClientScript(2730, "iiiiii", info.getParams()[0], info.getParams()[1],
				info.getParams()[2], info.getParams()[3], currentStruct = info.getCategoryStruct(), currentEntry = slot);
	}

	public static OptionsDialogue get(Player player, NPC npc) {
		return new OptionsDialogue("Would you like a Collection log?",
				new Option("Yes", () -> {
					player.dialogue(
							new PlayerDialogue("Yes, that sounds helpful!"),
							new NPCDialogue(npc,
									"There! Now you will be able to see the true beauty of everything you collect on your adventures.")
									.onDialogueOpened(() -> player.getInventory().add(ItemID.COLLECTION_LOG)),
							new PlayerDialogue("Thanks!"));
				}),
				new Option("No thanks.", () -> {
					player.dialogue(
							new PlayerDialogue("No thanks. I think I'll pass."),
							new NPCDialogue(npc,
									"I should've guessed you wouldn't understand the true beauty of a good collection!"));
				}));
	}

	public static void handleClose(Player player) {
		player.closeInterface(ToplevelComponent.MAINMODAL);
		player.getPacketSender().sendClientScript(101, "i", 11);
	}

	public static int addSumMultipleClues(Player player) {
		int sum = 0;
		int sum1 = player.beginnerClueCount;
		int sum2 = player.easyClueCount;
		int sum3 = player.medClueCount;
		int sum4 = player.hardClueCount;
		int sum5 = player.eliteClueCount;
		int sum6 = player.masterClueCount;
		return sum1 + sum2 + sum3 + sum4 + sum5 + sum6;
	}
}

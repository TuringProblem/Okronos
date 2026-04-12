package collectionlog;

import io.ruin.cache.ItemID;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.item.actions.ItemAction;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
public class CollectionLogInterface {

	public static void register() {
		NPCAction.register(8491, "talk-to", (player, npc) -> {
			player.dialogue(
					new NPCDialogue(npc, "It's beautiful, isn't it?"),
					new PlayerDialogue("What is?"),
					new NPCDialogue(npc,
							"Everything! The wonders right here in this museum, collected from all corners of the land."),
					new PlayerDialogue("I guess it is, you're right."),
					new NPCDialogue(npc,
							"Matter of fact, I consider myself quite the collector. I keep a record of just about everything that I find!"),
					CollectionLog.get(player, npc));
		});

		NPCAction.register(8491, "get log", (player, npc) -> player.dialogue(CollectionLog.get(player, npc)));

		ItemAction.registerInventory(ItemID.COLLECTION_LOG, 1, (player, item) -> {
			player.collectionLog().open(player);
			return;
		});

		InterfaceHandler.register(Interface.COLLECTION_LOG, h -> {
			h.actions[4] = (SimpleAction) p -> p.collectionLog().sendTab(p, CollectionLogInfo.BOSS);
			h.actions[5] = (SimpleAction) p -> p.collectionLog().sendTab(p, CollectionLogInfo.RAIDS);
			h.actions[6] = (SimpleAction) p -> p.collectionLog().sendTab(p, CollectionLogInfo.CLUES);
			h.actions[7] = (SimpleAction) p -> p.collectionLog().sendTab(p, CollectionLogInfo.MINIGAMES);
			h.actions[8] = (SimpleAction) p -> p.collectionLog().sendTab(p, CollectionLogInfo.OTHER);

			h.actions[79] = (SimpleAction) p -> CollectionLog.handleClose(p);

			h.actions[11] = (DefaultAction) (player, option, slot, itemId) -> {
				player.collectionLog().selectEntry(player, slot, CollectionLogInfo.BOSS);
			};

			h.actions[15] = (DefaultAction) (player, option, slot, itemId) -> {
				player.collectionLog().selectEntry(player, slot, CollectionLogInfo.RAIDS);
			};

			h.actions[31] = (DefaultAction) (player, option, slot, itemId) -> {
				player.collectionLog().selectEntry(player, slot, CollectionLogInfo.CLUES);
			};

			h.actions[26] = (DefaultAction) (player, option, slot, itemId) -> {
				player.collectionLog().selectEntry(player, slot, CollectionLogInfo.MINIGAMES);
			};

			h.actions[33] = (DefaultAction) (player, option, slot, itemId) -> {
				player.collectionLog().selectEntry(player, slot, CollectionLogInfo.OTHER);
			};

			h.closedAction = (p, i) -> {
				p.getPacketSender().sendClientScript(101, "i", 11);
			};
		});
	}
}

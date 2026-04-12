package collectionlog.hook;

import collectionlog.Attributes;
import collectionlog.CollectionLogInfo;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.questtab.JournalTab;
import io.ruin.model.inter.questtab.JournalTab.TabComponent;
import io.ruin.model.var.VarPlayerRepository;
import lombok.experimental.ExtensionMethod;
import io.ruin.HooksV2.Result;
import io.ruin.model.entity.player.Player;

@ExtensionMethod(Attributes.class)
public class JournalTabHook {

	public static void register() {
		JournalTab.hooks.register(JournalTab.Hook.OnSummaryClick.class, JournalTabHook::handle);
		JournalTab.hooks.register(JournalTab.Hook.OnSend.class, JournalTabHook::handle);

		InterfaceHandler.register(848, 24, (player) -> {
			player.collectionLogUpdated().open(player);
		});
	}

	private static Result handle(JournalTab.Hook.OnSummaryClick ctx) {
		var player = ctx.player();
		var slot = ctx.slot();
		if (slot == 6) {
			player.collectionLogUpdated().open(player);
		}
		return Result.Pass;
	}

	private static Result handle(JournalTab.Hook.OnSend ctx) {
		var player = ctx.player();
		var component = ctx.component();

		if (component != TabComponent.SUMMARY_COLLECTION) {
			return Result.Pass;
		}
		var totalCollected = player.collectionLog().getCollected().size();
		VarPlayerRepository.COLLECTION_PROGRESS.set(player, totalCollected);
		VarPlayerRepository.COLLECTION_COUNT.set(player, CollectionLogInfo.TOTAL_COLLECTABLES);
		return Result.Pass;
	}

}

package collectionlog.hook;

import collectionlog.Attributes;
import io.ruin.HooksV2.Result;
import io.ruin.model.entity.player.Player;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
public class PlayerHook {

	public static void register() {
		Player.hooks.register(Player.Hook.OnInit.class, PlayerHook::handle);
		Player.hooks.register(Player.Hook.OnAddCollectionLog.class, PlayerHook::handle);
	}

	private static Result handle(Player.Hook.OnInit ctx) {
		var player = ctx.player();

		collectPets(player);
		return Result.Pass;
	}

	private static Result handle(Player.Hook.OnAddCollectionLog ctx) {
		var player = ctx.player();
		var items = ctx.items();

		if (!player.isOnline()) {
			return Result.Pass;
		}
		player.collectionLog().addAll(player, items);
		return Result.Pass;
	}

	private static void collectPets(Player player) {
		if (!player.petsCollected()) {
			var pet = player.pet;
			if (pet != null) {
				player.collectionLog().collect(player, pet.itemId);
			}

			for (var bankItem : player.getBank().getItems()) {
				if (bankItem != null && bankItem.getDef().pet != null) {
					player.collectionLog().collect(player, bankItem.getId());
				}
			}

			for (var item : player.getInventory().getItems()) {
				if (item != null && item.getDef().pet != null) {
					player.collectionLog().collect(player, item.getId());
				}
			}
			player.petsCollected(true);
		}
	}
}

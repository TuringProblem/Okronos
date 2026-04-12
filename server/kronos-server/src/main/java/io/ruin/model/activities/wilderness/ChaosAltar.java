package io.ruin.model.activities.wilderness;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.dailytasks.DailyTasks;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.item.actions.ItemObjectAction;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.skills.prayer.Bone;
import io.ruin.model.stat.StatType;

import static io.ruin.cache.ItemID.COINS_995;

public class ChaosAltar {

	private static final int CHAOS_ALTAR = 411;
	private static final int ELDER_CHAOS_DRUID = 7995;
	private static final int COST_PER_BONE = 3000;

	public static void register() {
		NPCAction.register(ELDER_CHAOS_DRUID, "talk-to", (player, npc) -> player.dialogue(
			new NPCDialogue(npc, "Hello, and welcome to The Dark Lord's temple. Zamorak will reward those who offer the bones of the vanquished on his altar."),
			new NPCDialogue(npc, "I can exchange banknotes for bones, should you require such a service."),
			new NPCDialogue(npc, "My services, however, are not free. I charge " + COST_PER_BONE + " coins per banknote."),
			new OptionsDialogue(
				new Option("Yes please.", () -> player.dialogue(
					new PlayerDialogue("Yes please."),
					new NPCDialogue(npc, "Hand me the banknotes you wish to exchange."))),
				new Option("No thanks.", () -> player.dialogue(new PlayerDialogue("No thanks.")))
			)
		));

		for (Bone bone : Bone.VALUES) {
			ItemObjectAction.register(bone.id, CHAOS_ALTAR, ChaosAltar::boneOnAltar);
			ItemObjectAction.register(bone.notedId, CHAOS_ALTAR, ChaosAltar::boneOnAltarNoted);
			if (bone.notedId == -1)
				continue;
			ItemNPCAction.register(bone.notedId, ELDER_CHAOS_DRUID, ChaosAltar::promptForAmount);
		}
	}

	/**
	 * Elder Chaos Druid
	 */

	private static void promptForAmount(Player player, Item item, NPC npc) {
		Item coins = player.getInventory().findItem(COINS_995);
		if (coins == null || coins.getAmount() < COST_PER_BONE) {
			player.dialogue(new NPCDialogue(npc, "I charge " + COST_PER_BONE + " coins for exchanging each banknote."));
			return;
		}

		player.dialogue(new OptionsDialogue(
			new Option("Exchange '" + item.getDef().name + "': " + COST_PER_BONE + " coins", () -> exchange(player, item, 1)),
			new Option("Exchange All: " + (item.getAmount() * COST_PER_BONE), () -> exchange(player, item, Integer.MAX_VALUE)),
			new Option("Exchange X", () -> player.integerInput("How many would you like to exchange?", amt -> exchange(player, item, amt)))
		));
	}

	private static void exchange(Player player, Item item, int amt) {
		if (amt > item.getAmount())
			amt = item.getAmount();
		while (amt-- > 0) {
			Item coins = player.getInventory().findItem(COINS_995);
			if (coins == null || coins.getAmount() < COST_PER_BONE)
				break;
			if (player.getInventory().isFull())
				break;
			item.remove(1);
			coins.remove(3000);
			player.getInventory().add(item.getDef().fromNote().id, 1);
		}
		player.dialogue(new ItemDialogue().one(item.getDef().fromNote().id, "The chaos druid converts your banknote."));
	}

	/**
	 * Chaos Altar
	 */
	private static void boneOnAltar(Player player, Item item, GameObject obj) {
		player.startEvent(event -> {
			event.setCancelCondition(() -> player.getInventory().findItem(item.getId()) == null || player.getPosition().distance(obj.getPosition()) > 4);
			while (true) {
				Item bone = player.getInventory().findItem(item.getId());
				if (bone == null)
					break;
				boolean effect = player.wildernessLevel >= 1 || player.getPosition().getRegion().id == 4904;
				if (effect && Random.rollPercent(50)) {
					player.sendMessage("The Dark Lord spares your sacrifice but still rewards you for your efforts.");
				} else {
					player.getInventory().remove(item.getId(), 1);
				}
				DailyTasks.handleTaskProgression(player, bone);
				player.regularBonesBuriedOrSarcrificed++;
				if (player.regularBonesBuriedOrSarcrificed == Achievements.DIVINE_SENSES_I.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.DIVINE_SENSES_I.getAchievementName());

				if (bone.getId() == 536) {
					player.dragonBonesBuriedOrSacrificed++;
					if (player.dragonBonesBuriedOrSacrificed == Achievements.DIVINE_SENSES_II.getCompletionAmount())
						player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.DIVINE_SENSES_II.getAchievementName());
				}
				if (bone.getId() == 11943) {
					player.lavaDragonBonesBuriedOrSacrificed++;
					if (player.lavaDragonBonesBuriedOrSacrificed == Achievements.DIVINE_SENSES_III.getCompletionAmount())
						player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.DIVINE_SENSES_III.getAchievementName());
				}
				DailyTasks.handleItemObtained(player, bone.getId(), StatType.Prayer);
				player.animate(3705);
				player.getStats().addXp(StatType.Prayer, Bone.get(item.getId()).exp * 3.5, true);
				World.sendGraphics(624, 10, 0, player.getAbsX() - 1, player.getAbsY(), 0);
				Bone.get(item.getId()).altarCounter.increment(player, 1);
				event.delay(2);
			}
		});
	}

	private static void boneOnAltarNoted(Player player, Item item, GameObject obj) {
		if (player.totalDonated < 100)
			return;
		player.startEvent(event -> {
			event.setCancelCondition(() -> player.getInventory().findItem(item.getId()) == null || player.getPosition().distance(obj.getPosition()) > 4);
			int bones = Math.max(item.getAmount(), 100);
			for (int i = 0; i < bones; i++) {
				boolean effect = player.wildernessLevel >= 1 || player.getPosition().getRegion().id == 4904;
				if (effect && Random.rollPercent(50)) {
					player.sendMessage("The Dark Lord spares your sacrifice but still rewards you for your efforts.");
				} else {
					player.getInventory().remove(item.getId(), 1);
				}
				DailyTasks.handleTaskProgression(player, Bone.get(item.getId() - 1));
				player.regularBonesBuriedOrSarcrificed++;
				if (player.regularBonesBuriedOrSarcrificed == Achievements.DIVINE_SENSES_I.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.DIVINE_SENSES_I.getAchievementName());

				if (item.getId() == 537) {
					player.dragonBonesBuriedOrSacrificed++;
					if (player.dragonBonesBuriedOrSacrificed == Achievements.DIVINE_SENSES_II.getCompletionAmount())
						player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.DIVINE_SENSES_II.getAchievementName());
				}
				if (item.getId() == 11944) {
					player.lavaDragonBonesBuriedOrSacrificed++;
					if (player.lavaDragonBonesBuriedOrSacrificed == Achievements.DIVINE_SENSES_III.getCompletionAmount())
						player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.DIVINE_SENSES_III.getAchievementName());
				}
				DailyTasks.handleItemObtained(player, item.getId() - 1, StatType.Prayer);
				player.animate(3705);
				player.getStats().addXp(StatType.Prayer, Bone.get(item.getId() - 1).exp * 3.5, true);
				World.sendGraphics(624, 10, 0, player.getAbsX() - 1, player.getAbsY(), 0);
				Bone.get(item.getId() - 1).altarCounter.increment(player, 1);
				event.delay(2);
			}
		});
	}
}

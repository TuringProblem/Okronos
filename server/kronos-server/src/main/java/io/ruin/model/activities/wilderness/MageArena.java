package io.ruin.model.activities.wilderness;

import com.google.gson.annotations.Expose;
import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.SpawnListener;
import io.ruin.model.inter.AccessMasks;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.dialogue.*;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Direction;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.skills.Tool;
import io.ruin.model.var.VarPlayerRepository;
import lombok.Getter;
import lombok.Setter;

// TODO(polish) - static field on logic
public class MageArena {

	public enum shopItems {
		BEGINNER_WAND(6908, 30, 0),
		APPRENTICE_WAND(6910, 60, 1),
		TEACHER_WAND(6912, 150, 2),
		MASTER_WAND(6914, 240, 3),
		INFINITY_TOP(6916, 400, 4),
		INFINITY_HAT(6918, 350, 5),
		INFINITY_BOOTS(6920, 120, 6),
		INFINITY_GLOVES(6922, 175, 7),
		INFINITY_ROBE(6924, 450, 8),
		MAGES_BOOK(6889, 750, 9),
		LOOTING_BAG(11941, 200, 10),
		RUNE_POUCH(12791, 500, 11),
		ICE_BARRAGE_SACK(24607, 3, 12),
		BIND_SACK(24609, 2, 13),
		SNARE_SACK(24611, 2, 14),
		ENTANGLE_SACK(24613, 2, 15),
		TELEPORT_SACK(24615, 2, 16),
		VENG_SACK(24621, 2, 17),
		BOOK_OF_LAW(12610, 50, 18),
		BOOK_OF_WAR(12608, 50, 19),
		BOOK_OF_BALANCE(3844, 50, 20),
		HOLY_BOOK(3840, 50, 21),
		UNHOLY_BOOK(3842, 50, 22),
		BOOK_OF_DARKNESS(12612, 50, 23);

		public static shopItems getUpgradable(int id) {
			for (shopItems upgradable : values())
				if (upgradable.id == id)
					return upgradable;
			return null;
		}

		@Getter
		@Setter
		public int id, price, slot;

		shopItems(int id, int price, int slot) {
			this.id = id;
			this.price = price;
			this.slot = slot;
		}
	}

	@Getter
	@Setter
	public static int mageArenaSlotValue = -1;

	public static void openShop(Player player) {
		player.openInterface(ToplevelComponent.MAINMODAL, 197);
		player.getPacketSender().sendIfEvents(197, 11, 0, 23, AccessMasks.ClickOp1, AccessMasks.ClickOp2);
	}

	public static void handleSlots(Player player, int slot) {
		switch (slot) {
			case 0:
				mageArenaSlotValue = 30;
				return;
			case 1:
				mageArenaSlotValue = 60;
				return;
			case 2:
				mageArenaSlotValue = 150;
				return;
			case 3:
				mageArenaSlotValue = 240;
				return;
			case 4:
				mageArenaSlotValue = 400;
				return;
			case 5:
				mageArenaSlotValue = 350;
				return;
			case 6:
				mageArenaSlotValue = 120;
				return;
			case 7:
				mageArenaSlotValue = 450;
				return;
			case 8:
				mageArenaSlotValue = 750;
				return;
			case 9:
				mageArenaSlotValue = 200;
				return;
			case 10:
				mageArenaSlotValue = 500;
				return;
			case 11:
				mageArenaSlotValue = 3;
				return;
			case 12:
				mageArenaSlotValue = 2;
				return;
			case 13:
				mageArenaSlotValue = 2;
				return;
			case 14:
				mageArenaSlotValue = 2;
				return;
			case 15:
				mageArenaSlotValue = 2;
				return;
			case 16:
				mageArenaSlotValue = 2;
				return;
			case 17:
				mageArenaSlotValue = 2;
				return;
			case 18:
				mageArenaSlotValue = 50;
				return;
			case 19:
				mageArenaSlotValue = 50;
				return;
			case 20:
				mageArenaSlotValue = 50;
				return;
			case 21:
				mageArenaSlotValue = 50;
				return;
			case 22:
				mageArenaSlotValue = 50;
				return;
			case 23:
				mageArenaSlotValue = 50;
				return;
		}
	}

	public static void register() {
		InterfaceHandler.register(197, h -> {
			h.actions[11] = (DefaultAction) (p, option, slot, itemId) -> {


			};
		});

		/**
		 * Levers
		 */
		ObjectAction.register(9706, 3104, 3956, 0, "pull", (player, obj) -> {
			pullLever(player, obj, 3951, "into", true);
		});
		ObjectAction.register(9707, 3105, 3952, 0, "pull", (player, obj) -> {
			pullLever(player, obj, 3956, "out of", false);
		});

		/**
		 * Odd unclipped areas around the circle which make you look
		 * like you're floating on lava.
		 */
		Tile.get(3093, 3939, 0).flagUnmovable();
		Tile.get(3094, 3941, 0).flagUnmovable();
		Tile.get(3110, 3946, 0).flagUnmovable();
		Tile.get(3098, 3945, 0).flagUnmovable();
		Tile.get(3100, 3946, 0).flagUnmovable();
		Tile.get(3112, 3945, 0).flagUnmovable();
		Tile.get(3116, 3941, 0).flagUnmovable();
		Tile.get(3117, 3939, 0).flagUnmovable();
		Tile.get(3117, 3928, 0).flagUnmovable();
		Tile.get(3116, 3926, 0).flagUnmovable();
		Tile.get(3112, 3922, 0).flagUnmovable();
		Tile.get(3110, 3921, 0).flagUnmovable();
		Tile.get(3100, 3921, 0).flagUnmovable();
		Tile.get(3098, 3922, 0).flagUnmovable();
		Tile.get(3094, 3926, 0).flagUnmovable();
		Tile.get(3093, 3928, 0).flagUnmovable();

		Tile.get(3091, 3497, 0).flagUnmovable();
		Tile.get(3091, 3493, 0).flagUnmovable();

		SpawnListener.register("battle mage", npc -> npc.deathEndListener = (DeathListener.SimpleKiller) killer -> {
			if (killer.player.mageArena) {
				int randomPoints = Random.get(3, 6);
				killer.player.mageArenaPoints += randomPoints;
				VarPlayerRepository.MAGE_ARENA_POINTS.increment(killer.player, randomPoints);
			}
		});

		MapListener.registerRegion(12349)
			.onExit((p, logout) -> {
				if (!logout)
					p.mageArena = false;
			});

		NPCAction.register(1603, "talk-to", (player, npc) -> player.dialogue(
			new NPCDialogue(npc, "How can I help you?"),
			new OptionsDialogue(
				new Option("How do I get mage arena points?", () -> player.dialogue(
					new PlayerDialogue("How do I get mage arena points?"),
					new NPCDialogue(npc, "Killing a Battle mage inside the Mage Arena will give you anywhere from 1-3 points. Be careful though, as "),
					new NPCDialogue(npc, "it's dangerous out there. Also be sure to bring runes, as you can only use magic based attacks inside the arena."),
					new PlayerDialogue("Okay, thanks.")
				)),
				new Option("Can I see the point exchange?", () -> player.dialogue(
					new PlayerDialogue("Can I see the point exchange?"),
					new ActionDialogue(() -> openShop(player))
				)),
				new Option("I have to go.", () -> player.dialogue(new PlayerDialogue("I have to go.")))
			)
		));
		NPCAction.register(1603, "check-points", (player, npc) -> {
			player.dialogue(new NPCDialogue(npc, "You've currently have " + player.mageArenaPoints + " point" + (player.mageArenaPoints == 1 ? "." : "s. Kill Battle Mage's inside the Mage Arena to get more points.")));
		});

		/**
		 * Sack containing knife
		 */
		ObjectAction.register(14743, 3093, 3956, 0, "search", (player, obj) -> {
			if (player.getInventory().isFull()) {
				player.sendFilteredMessage("Nothing interesting happens.");
				return;
			}

			player.getInventory().add(Tool.KNIFE, 1);
			player.sendFilteredMessage("You search the sack and find a knife.");
		});

		ObjectAction.register(2878, 2541, 4719, 0, "Step-into", (player, obj) -> {
			player.dialogue(new MessageDialogue("You step into the pool of sparkling water. You feel energy rush through your veins."), new ActionDialogue(() -> {
				player.startEvent((event) -> {
					player.lock(LockType.FULL_DELAY_DAMAGE);
					event.path(player, Position.of(2542, 4718));
					event.delay(1);
					player.animate(741);
					player.getMovement().force(0, 2, 0, 0, 5, 40, Direction.getDirection(player.getPosition(), Position.of(2542, 4720)));
					event.delay(1);
					player.animate(804);
					player.graphics(68);
					event.delay(1);
					player.getMovement().teleport(2509, 4689, 0);
					player.animate(-1);
					player.unlock();
					player.getAppearance();
				});
			}));
		});

		ObjectAction.register(2879, 2508, 4686, 0, "Step-into", (player, obj) -> {
			player.dialogue(new MessageDialogue("You step into the pool of sparkling water. You feel energy rush through your veins."), new ActionDialogue(() -> {
				player.startEvent((event) -> {
					player.lock(LockType.FULL_DELAY_DAMAGE);
					event.path(player, Position.of(2509, 4689));
					event.delay(1);
					player.animate(741);
					player.getMovement().force(0, -2, 0, 0, 5, 40, Direction.getDirection(player.getPosition(), Position.of(2509, 4687)));
					event.delay(1);
					player.animate(804);
					player.graphics(68);
					event.delay(1);
					player.getMovement().teleport(2542, 4718, 0);
					player.animate(-1);
					player.unlock();
				});
			}));
		});

		ObjectAction.register(2873, 2500, 4720, 0, "Pray-at", (player, obj) -> {
			player.startEvent((event) -> {
				player.lock(LockType.MOVEMENT);
				player.animate(645);
				player.dialogue(new MessageDialogue("You kneel and chant to Saradomin...").hideContinue());
				event.delay(2);
				player.magicCapesBought++;
				if (player.magicCapesBought == Achievements.THANK_GOD_FOR_THAT.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.THANK_GOD_FOR_THAT.getAchievementName());

				player.dialogue(new MessageDialogue("You kneel and chant to Saradomin...<br>" +
					"You feel a rush of energy charge through your veins.<br>" +
					"Suddenly a cape appears before you."));
				event.delay(1);
				World.sendGraphics(188, 50, 0, 2500, 4720, 0);
				new GroundItem(2412, 1).position(2500, 4720, 0).owner(player).spawnPrivate();
				player.unlock();
			});
		});

		ObjectAction.register(2875, 2507, 4723, 0, "Pray-at", (player, obj) -> {
			player.startEvent((event) -> {
				player.lock(LockType.MOVEMENT);
				player.animate(645);
				player.dialogue(new MessageDialogue("You kneel and chant to Guthix...").hideContinue());
				event.delay(3);
				player.magicCapesBought++;
				if (player.magicCapesBought == Achievements.THANK_GOD_FOR_THAT.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.THANK_GOD_FOR_THAT.getAchievementName());
				World.sendGraphics(188, 0, 0, 2507, 4723, 0);
				new GroundItem(2413, 1).position(2507, 4723, 0).owner(player).spawnPrivate();
				player.dialogue(new MessageDialogue("You kneel and chant to Guthix...<br>" +
					"You feel a rush of energy charge through your veins.<br>" +
					"Suddenly a cape appears before you."));
				player.unlock();
			});
		});

		ObjectAction.register(2874, 2516, 4720, 0, "Pray-at", (player, obj) -> {
			player.startEvent((event) -> {
				player.lock(LockType.MOVEMENT);
				player.animate(645);
				player.dialogue(new MessageDialogue("You kneel and chant to Zamorak...").hideContinue());
				event.delay(3);
				player.magicCapesBought++;
				if (player.magicCapesBought == Achievements.THANK_GOD_FOR_THAT.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.THANK_GOD_FOR_THAT.getAchievementName());
				World.sendGraphics(188, 0, 0, 2516, 4720, 0);
				new GroundItem(2414, 1).position(2516, 4720, 0).owner(player).spawnPrivate();
				player.dialogue(new MessageDialogue("You kneel and chant to Zamorak...<br>" +
					"You feel a rush of energy charge through your veins.<br>" +
					"Suddenly a cape appears before you."));
				player.unlock();
			});
		});
	}

	private static void pullLever(Player player, GameObject lever, int teleportY, String message, boolean enter) {
		if (player.getCombat().checkTb())
			return;
		player.startEvent(event -> {
			player.lock(LockType.FULL_NULLIFY_DAMAGE);
			player.animate(2710);
			lever.animate(2711);
			player.sendMessage("You pull the lever...");
			event.delay(2);
			player.animate(714);
			player.graphics(111, 110, 0);
			event.delay(3);
			player.resetAnimation();
			player.mageArena = enter;
			player.getMovement().teleport(3105, teleportY, 0);
			player.sendMessage("...and get teleported " + message + " the arena!");
			player.unlock();
		});
	}

}

package io.ruin.model.skills.magic.spells.arceuus;

import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.questtab.main.Achievements;
import io.ruin.model.item.Item;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.skills.magic.Spell;
import io.ruin.model.skills.magic.rune.Rune;
import io.ruin.model.stat.StatType;

public class ReLive extends Spell {

	public enum MonsterData {
		GOBLIN(new int[]{13447, 13448}, "Ensouled goblin head", 3, 130.0, 7018),
		MONKEY(new int[]{13450, 13451}, "Ensouled monkey head", 7, 182.0, 7019),
		IMP(new int[]{13453, 13454}, "Ensouled imp head", 12, 286.0, 7020),
		MINOTAUR(new int[]{13453, 13454}, "Ensouled minotaur head", 16, 364.0, 7021),
		SCORPION(new int[]{13459, 13460}, "Ensouled scorpion head", 19, 454.0, 7022),
		BEAR(new int[]{13462, 13463}, "Ensouled bear head", 21, 480.0, 7023),
		UNICORN(new int[]{13465, 13466}, "Ensouled unicorn head", 22, 494.0, 7024),
		DOG(new int[]{13468, 13469}, "Ensouled dog head", 26, 52.0, 7025),
		CHAOS_DRUID(new int[]{13471, 13472}, "Ensouled chaos druid head", 30, 584.0, 7026),
		GIANT(new int[]{13474, 13475}, "Ensouled giant head", 37, 650.0, 7027),
		OGRE(new int[]{13477, 13478}, "Ensouled ogre head", 40, 716.0, 7028),
		ELF(new int[]{13480, 13481}, "Ensouled elf head", 43, 754.0, 7029),
		TROLL(new int[]{13483, 13484}, "Ensouled troll head", 46, 780.0, 7030),
		HORROR(new int[]{13486, 13487}, "Ensouled horror head", 52, 832.0, 7031),
		KALPHITE(new int[]{13489, 13490}, "Ensouled kalphite head", 57, 884.0, 7032),
		DAGANNOTH(new int[]{13492, 13493}, "Ensouled dagannoth head", 62, 936.0, 7033),
		BLOODVELD(new int[]{13495, 13496}, "Ensouled bloodveld head", 65, 1040.0, 7034),
		TZHAAR(new int[]{13498, 13499}, "Ensouled tzhaar head", 69, 1104.0, 7035),
		DEMON(new int[]{13501, 13502}, "Ensouled demon head", 72, 1170.0, 7036),
		AVIANSIE(new int[]{13504, 13505}, "Ensouled aviansie head", 78, 1234.0, 7037),
		ABYSSAL_CREATURE(new int[]{13507, 13508}, "Ensouled abyssal head", 85, 1300.0, 7038),
		DRAGON(new int[]{13510, 13511}, "Ensouled dragon head", 93, 1560.0, 7039);

		public final int[] headId;
		public final int levelReq, reanimatedNPCId;
		public final String headName;
		public final double prayerExp;
		public Item[] runes;

		MonsterData(int[] headId, String headName, int levelReq, double prayerExp, int reanimatedNPCId) {//, Item... runes) {
			this.headId = headId;
			this.headName = headName;
			this.levelReq = levelReq;
			this.prayerExp = prayerExp;
			this.reanimatedNPCId = reanimatedNPCId;

		}

		public static final MonsterData[] VALUES = values();
	}

	private static Bounds SPAWN_AREA = new Bounds(1703, 3874, 1745, 3904, 0);
	private static final Projectile projectile = new Projectile(1289, 40, 10, 5, 75, 15, 10, 64);

	private static void reanimate(Player player, MonsterData monster, Item item) {
		Position oldPosition = new Position(player.getAbsX(), player.getAbsY(), 0);
		player.startEvent(event -> {
			player.lock();
			player.step(-1, 0, StepType.WALK);
			event.delay(1);
			player.face(oldPosition.getX(), oldPosition.getY());
			event.delay(1);
			player.animate(7198);
			player.graphics(1288);
			event.delay(1);
			player.getInventory().remove(item.getId(), 1);
			if (item.getId() == 13510 || item.getId() == 13511) {
				player.dragonsAnimated++;
				if (player.dragonsAnimated == Achievements.THANKFULLY_NOT_ALL_AT_ONCE.getCompletionAmount())
					player.sendMessage("<col=000080>You have completed the achievement: <col=800000>" + Achievements.THANKFULLY_NOT_ALL_AT_ONCE.getAchievementName());
			}
			GroundItem groundItem = new GroundItem(item.getId(), 1).owner(player).position(oldPosition).spawn();
			projectile.send(player.getAbsX(), player.getAbsY(), oldPosition.getX(), oldPosition.getY());
			event.delay(2);
			World.sendGraphics(1290, 0, 0, oldPosition);
			event.delay(1);
			groundItem.remove();
			event.delay(4);
			NPC animated = new NPC(monster.reanimatedNPCId).spawn(oldPosition.getX(), oldPosition.getY(), player.getHeight()).targetPlayer(player, true);
			animated.deathEndListener = (DeathListener.Simple) () -> {
				animated.remove();
				player.getStats().addXp(StatType.Prayer, monster.prayerExp, true);
			};
			animated.attackTargetPlayer(() -> !player.getPosition().isWithinDistance(animated.getPosition()));
			player.unlock();
		});
	}

	public enum MonsterLevel {

		ONE(16, 32.0,
			new Item[]{Rune.BODY.toItem(4), Rune.NATURE.toItem(2)},
			MonsterData.GOBLIN,
			MonsterData.MONKEY,
			MonsterData.IMP,
			MonsterData.MINOTAUR,
			MonsterData.SCORPION,
			MonsterData.BEAR,
			MonsterData.UNICORN),
		TWO(41, 80.0,
			new Item[]{
				Rune.BODY.toItem(4), Rune.NATURE.toItem(3), Rune.SOUL.toItem(1)},
			MonsterData.DOG,
			MonsterData.CHAOS_DRUID,
			MonsterData.OGRE,
			MonsterData.GIANT,
			MonsterData.ELF,
			MonsterData.TROLL,
			MonsterData.HORROR),
		THREE(72, 138.0,
			new Item[]{
				Rune.BLOOD.toItem(1), Rune.NATURE.toItem(3), Rune.SOUL.toItem(2)},
			MonsterData.KALPHITE,
			MonsterData.DAGANNOTH,
			MonsterData.BLOODVELD,
			MonsterData.TZHAAR,
			MonsterData.DEMON),
		//                EnchantData.HELLHOUND,
		FOUR(90, 170.0,
			new Item[]{
				Rune.BLOOD.toItem(2), Rune.NATURE.toItem(4), Rune.SOUL.toItem(4)},
			MonsterData.AVIANSIE,
			MonsterData.ABYSSAL_CREATURE,
			MonsterData.DRAGON);


		public final int levelReq;
		public final double exp;
		public final Item[] runes;
		public final MonsterData[] data;

		MonsterLevel(int levelReq, double exp, Item[] runes, MonsterData... data) {
			this.levelReq = levelReq;
			this.exp = exp;
			this.runes = runes;
			this.data = data;
		}
	}

	public ReLive(MonsterLevel monsterLevel) {
		registerItem(monsterLevel.levelReq, monsterLevel.exp, true, monsterLevel.runes, (player, item) -> {
			for (MonsterData enchantLevelData : monsterLevel.data) {
				if (item.getId() != enchantLevelData.headId[0] && item.getId() != enchantLevelData.headId[1]) {
					continue;
				}
				if (!player.getPosition().inBounds(SPAWN_AREA)) {
					player.dialogue(new ItemDialogue().one(enchantLevelData.headId[0], "The creature cannot be reanimated here. The power<br>of the crystals by the Dark Altar will increase the<br>potency of the spell."));
					return false;
				}
				if (player.npcTarget) {
					player.sendFilteredMessage("You need to kill the monster you already have before spawning another!");
					return false;
				}
				reanimate(player, enchantLevelData, item);
				return true;
			}
			return false;
		});
	}
}

package io.ruin.model.activities.bosses;

import discord.webhooks.notifications.RareDropHook;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.cache.ItemID;
import io.ruin.model.World;
import io.ruin.model.activities.DonationBossHandler;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.item.Item;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Broadcast;
import io.ruin.utility.TickDelay;
import io.ruin.utility.Utils;
import org.json.JSONObject;

import java.util.*;


public class Malakar extends NPCCombat {

	public static final LootTable table = new LootTable().addTable(1,
		new LootItem(ItemID.COINS_995, 2000000, 9),
		new LootItem(ItemID.COINS_995, 5000000, 6),
		new LootItem(ItemID.COINS_995, 10000000, 3),
		new LootItem(ItemID.PERK_POINT_SCROLL, 1, 2, 7),
		new LootItem(ItemID.POINT_MYSTERY_BOX, 1, 2, 6),
		new LootItem(ItemID.DOUBLE_DROP_SCROLL, 1, 1, 4),
		new LootItem(ItemID.DAMAGE_REDUCTION_SCROLL, 1, 2, 5),
		new LootItem(ItemID.BREW_IMMUNITY_SCROLL, 1, 2, 5),
		new LootItem(ItemID.DOUBLE_EXP_SCROLL, 1, 2, 5),
		new LootItem(ItemID.SLAYER_SKIP_SCROLL, 2, 5, 5),
		new LootItem(ItemID.CLUE_SCROLL_BEGINNER, 4, 7, 9),
		new LootItem(ItemID.CLUE_SCROLL_EASY, 4, 7, 9),
		new LootItem(ItemID.CLUE_SCROLL_MEDIUM, 2, 4, 5),
		new LootItem(ItemID.CLUE_SCROLL_HARD, 2, 4, 3),
		new LootItem(ItemID.CLUE_SCROLL_ELITE, 1, 2, 2),
		new LootItem(ItemID.CLUE_SCROLL_MASTER, 1, 2, 1),
		new LootItem(ItemID.DRAGON_PLATELEGS + 1, 4, 7, 10),
		new LootItem(ItemID.DRAGON_PLATESKIRT + 1, 4, 7, 10),
		new LootItem(ItemID.DRAGON_MED_HELM + 1, 4, 7, 10),
		new LootItem(ItemID.RUNE_PLATEBODY + 1, 6, 10, 10),
		new LootItem(ItemID.SHINY_MINERALS, 15, 30, 6),
		new LootItem(ItemID.GLISTENING_MINERALS, 11, 20, 3),
		new LootItem(ItemID.OLD_ENHANCER, 2, 5, 6),
		new LootItem(ItemID.MODERN_ENHANCER, 1, 2, 3),
		new LootItem(ItemID.INNOVATIVE_ENHANCER, 1, 1, 2),
		new LootItem(ItemID.ARMOUR_BREAK_SIGIL, 1, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.SIPHON_DEAD_SIGIL, 1, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.AOE_SWIPE_SIGIL, 1, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.DAMAGE_FOR_HIRE_HIGH_SIGIL, 1, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.DAMAGE_FOR_HIRE_LOW_SIGIL, 1, 1, 3).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.FREEZE_CHANCE_SIGIL, 1, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.RESPECT_DEAD_SIGIL, 1, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.CRITICAL_HIT_SIGIL, 1, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.HEALTH_SIPHON_SIGIL, 1, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.VENOM_TIPPED_SIGIL, 1, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.SPECIAL_SAVER_SIGIL, 1, 1, 1).broadcast(Broadcast.GLOBAL),
		new LootItem(ItemID.STEEL_BAR + 1, 175, 10),
		new LootItem(ItemID.ADAMANTITE_BAR + 1, 50, 14),
		new LootItem(ItemID.RUNITE_BAR + 1, 100, 10),
		new LootItem(ItemID.MANTA_RAY + 1, 100, 75, 10),
		new LootItem(ItemID.BATTLESTAFF + 1, 20, 25, 10),
		new LootItem(ItemID.GRIMY_RANARR_WEED + 1, 15, 25, 14),
		new LootItem(ItemID.GRIMY_SNAPDRAGON + 1, 15, 25, 14),
		new LootItem(ItemID.GRIMY_TORSTOL + 1, 15, 25, 14),
		new LootItem(ItemID.YEW_LOGS + 1, 90, 125, 10),
		new LootItem(ItemID.DRAGON_BONES + 1, 50, 70, 10),
		new LootItem(ItemID.RUNITE_ORE + 1, 30, 50, 10),
		new LootItem(ItemID.MAGIC_LOGS + 1, 50, 100, 10),
		new LootItem(ItemID.ANGLERFISH + 1, 50, 100, 10),
		new LootItem(ItemID.RUNITE_BOLTS_UNF + 1, 50, 100, 10),
		new LootItem(ItemID.SUPER_RESTORE4 + 1, 20, 40, 12),
		new LootItem(ItemID.PRAYER_POTION4 + 1, 30, 50, 12),
		new LootItem(ItemID.SARADOMIN_BREW4 + 1, 20, 25, 12),
		new LootItem(ItemID.INSTANCE_TOKEN, 2, 4, 12)
	);

	Map<Player, Integer> damagingPlayers = new HashMap<>();
	private static final Projectile RANGED_PROJECTILE = new Projectile(1329, 20, 31, 35, 35, 10, 0, 32);
	private static final Projectile MAGIC_PROJECTILE = new Projectile(1729, 120, 31, 25, 56, 10, 15, 220);

	@Override
	public void init() {
		npc.hitListener = new HitListener().postDamage(this::postDamage);
		npc.deathEndListener = (entity, killer, killHit) -> {
			npc.localPlayers().forEach(p -> {
				if (damagingPlayers.containsKey(p)) {
					rewardPlayer(p);
				}
			});
			npc.remove();
			World.startEvent(e -> {
				e.delay(30);
				new NPC(12336).spawn(new Position(DonationBossHandler.malakarMap.convertX(2912), DonationBossHandler.malakarMap.convertY(3616)));
			});
		};
	}

	private void postDamage(Hit hit) {
		if (hit.damage > 0 && hit.attacker != null) {
			if (damagingPlayers.containsKey(hit.attacker.player))
				damagingPlayers.put(hit.attacker.player, damagingPlayers.get(hit.attacker.player) + hit.damage);
			else
				damagingPlayers.put(hit.attacker.player, hit.damage);
		}
	}

	@Override
	public void follow() {

	}

	private void rewardPlayer(Player player) {
		player.malakarKills.increment(player);
		Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.MALAKAR_MASTER.ordinal())).
			getCombatAchievement()).check(player);
		player.sendMessage(Color.RED.wrap("Your reward has been dropped to your feet!"));
		int rolls = calculateRollsFromDamage(player);
		SummerEvent.handleKill(player, "Malakar");
		for (int i = 0; i < 1; i++) {
			Item reward = table.rollItem();
			new GroundItem(reward).owner(player).position(player.getPosition()).spawn();
			if (reward.lootBroadcast != null) {
				Broadcast.WORLD.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " just received " + reward.getDef().descriptiveName + " from the Malakar!");
				String message = player.getName() + " just received ";
				message += reward.getDef().descriptiveName;


			RareDropHook.sendDiscordMessage(() -> {
				var jsonObject = new JSONObject();
				jsonObject.put("player", player.getName());
				jsonObject.put("game_mode", player.getGameMode());
				jsonObject.put("item_id", reward.getId());
				jsonObject.put("item_name", reward.getDef().name);
				jsonObject.put("source", "Malakar");
				jsonObject.put("total_attempts", Utils.formatMoneyString(player.malakarKills.getKills() + 1));
				return jsonObject;
			});
			}
		}
	}

	private int calculateRollsFromDamage(Player player) {
		int damage = damagingPlayers.get(player);
		int rolls = 0;
		if (damage >= 1000)
			rolls = 3;
		else if (damage >= 500)
			rolls = 2;
		else if (damage >= 250)
			rolls = 1;
		return rolls;
	}

	TickDelay portalDelay = new TickDelay();

	@Override
	public boolean attack() {
		if (Random.get(15) == 0 && portalDelay.remaining() < 1) {
			portalDelay.delay(50);
			handlePortalSpecial();
		} else {
			if (Random.get(1) == 0)
				magicAttack();
			else
				rangedAttack();
		}
		return true;
	}

	Position yellowPortalPosition = null;
	Position greenPortalPosition = null;

	private void handlePortalSpecial() {
		assignPortalPositions();
		List<Player> greenPlayers = new ArrayList<>();
		List<Player> yellowPlayers = new ArrayList<>();
		for (Player player : getPlayers()) {
			if (Random.get(1) == 0) {
				yellowPlayers.add(player);
				player.sendMessage(Color.YELLOW2.wrap("<shad=000000>You have been marked YELLOW for death."));
			} else {
				greenPlayers.add(player);
				player.sendMessage(Color.GREEN.wrap("<shad=000000>You have been marked GREEN for death."));
			}
		}
		World.startEvent(e -> {
			e.setCancelCondition(() -> npc.getHp() < 1);
			for (int i = 0; i < 6; i++) {
				World.sendGraphics(1360, 0, 0, greenPortalPosition);
				World.sendGraphics(1361, 0, 0, yellowPortalPosition);
				e.delay(3);
				if (i == 5) {
					for (Player player : greenPlayers) {
						if (player.getPosition().distance(greenPortalPosition) > 0) {
							player.hit(new Hit().randDamage(45, 90));
						}
					}
					for (Player player : yellowPlayers) {
						if (player.getPosition().distance(yellowPortalPosition) > 0) {
							player.hit(new Hit().randDamage(45, 90));
						}
					}
				}
			}
		});

	}

	private void assignPortalPositions() {
		List<Position> possiblePositions = new ArrayList<>();
		for (int x = -9; x < 9; x++) {
			for (int y = -9; y < 9; y++) {
				Position pos = new Position(npc.getPosition().getX() + x, npc.getPosition().getY() + y, npc.getPosition().getZ());
				if (pos.distance(npc.getPosition()) > 2)
					possiblePositions.add(pos);
			}
		}
		Position yellowPos = Random.get(possiblePositions);
		possiblePositions.remove(yellowPos);
		Position greenPos = Random.get(possiblePositions);
		yellowPortalPosition = yellowPos;
		greenPortalPosition = greenPos;
	}


	@Override
	public void process() {

	}

	private List<Player> getPlayers() {
		List<Player> players = new ArrayList<>();
		npc.getPosition().getRegion().players.stream().filter(player -> player.getHp() > 0).forEach(players::add);
		return players;
	}

	private void magicAttack() {
		npc.animate(10402);
		getPlayers().forEach(p -> {
			World.startEvent(e -> {
				e.setCancelCondition(() -> npc.getHp() < 1);
				int delay = MAGIC_PROJECTILE.send(npc, p);
				int maxDamage = 78;
				e.delay(World.getTicks(delay) + 1);
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
					maxDamage = 6;

				p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).ignorePrayer().ignoreDefence());
			});
		});
	}

	private void rangedAttack() {
		npc.animate(10403);
		getPlayers().forEach(p -> {
			World.startEvent(e -> {
				e.setCancelCondition(() -> npc.getHp() < 1);
				int delay = RANGED_PROJECTILE.send(npc, p);
				int maxDamage = 78;
				e.delay(World.getTicks(delay) + 1);
				if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
					maxDamage = 6;

				p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).ignorePrayer().ignoreDefence());
			});
		});
	}
}

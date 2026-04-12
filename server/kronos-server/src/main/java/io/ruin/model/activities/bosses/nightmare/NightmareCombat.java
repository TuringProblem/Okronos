package io.ruin.model.activities.bosses.nightmare;

import discord.webhooks.notifications.RareDropHook;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.SecuringTheBag;
import io.ruin.model.activities.perktree.perks.ThePetHunter;
import io.ruin.model.activities.tempevents.summerevent.SummerEvent;
import io.ruin.model.combat.Hit;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.item.loot.LootItem;
import io.ruin.model.item.loot.LootTable;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.route.routes.ProjectileRoute;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Misc;
import io.ruin.utility.Utils;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class NightmareCombat extends NPCCombat {

	private static final int MAGIC_ANIMATION = 8595, RANGE_ANIMATION = 8596, MAGIC_PROJECTILE = 1764, RANGED_PROJECTILE = 1766;

	private SpecialAttacks special;

	private Nightmare nightmare;

	public NightmareCombat(Nightmare nightmare) {
		this.nightmare = nightmare;
	}

	HashMap<String, Integer> playerDamage = new HashMap<>();
	public List<GameObject> objects = new ArrayList<>();
	public List<NPC> npcs = new ArrayList<>();

	private boolean playerHasDamage(Player player, int minimumDamage) {
		return playerDamage.containsKey(player.getName()) && playerDamage.get(player.getName()) >= minimumDamage;
	}

	@Override
	public void init() {
		if (nightmare == null) return;
		nightmare.hitListener = new HitListener()
			.postDefend(this::postDefend)
			.postDamage(this::afterDamaged);
		nightmare.deathStartListener = (entity, killer, killHit) -> {


			objects.forEach(GameObject::remove);
			npcs.forEach(NPC::remove);
			for (Player player : nightmare.getPosition().getRegion().players) {
				if (!playerHasDamage(player, 100)) {
					player.sendMessage("You have not dealt enough damage to receive loot.");
					continue;
				}
				SummerEvent.handleKill(player, "The Nightmare");
				int petDropAverage = 2000;

				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
					ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
						getActivePerks(player).get(perkIndex).getPerk(player);
					petDropAverage *= (float) c.getPetChanceBoost();
				}
				if (player.petDropBonus.isDelayed())
					petDropAverage *= 0.8f;
				if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
					petDropAverage *= 0.85F;

				petDropAverage *= getPetDonatorBoost(player);
				if (Random.get(petDropAverage) == 0) {
					Pet.LITTLE_NIGHTMARE.unlock(player, npc.getId());
				}
				player.nightmareKills.increment(player);
				if (npc.getPosition().getRegion().players.size() == 1) {
					Objects.requireNonNull(killer.player.combatAchievementsList.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.SLEEP_TIGHT.ordinal())).
						getCombatAchievement()).check(killer.player);
				}
				Objects.requireNonNull(killer.player.combatAchievementsList.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.NIGHTMARE_ADEPT.ordinal())).
					getCombatAchievement()).check(killer.player);
				Objects.requireNonNull(killer.player.combatAchievementsList.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.NIGHTMARE_MASTER.ordinal())).
					getCombatAchievement()).check(killer.player);
				Objects.requireNonNull(killer.player.combatAchievementsList.get(killer.player.getCombatAchievementIndexByOrdinal(CombatAchievements.NIGHTMARE_NOVICE.ordinal())).
					getCombatAchievement()).check(killer.player);
				NightmareCombat t = (NightmareCombat) npc.getCombat();
				if (!t.damagedPlayer) {
					Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_NIGHTMARE.ordinal())).
						getCombatAchievement()).check(player);
				}
				if (!damagedPlayer) {
					Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.PERFECT_NIGHTMARE.ordinal())).
						getCombatAchievement()).check(player);
				}
				Objects.requireNonNull(player.combatAchievementsList
					.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.EXPLOSION.ordinal()))
					.getCombatAchievement()).check(player);
				Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.NIGHTMARE_ADEPT.ordinal())).
					getCombatAchievement()).check(player);
				Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.NIGHTMARE_MASTER.ordinal())).
					getCombatAchievement()).check(player);
				Objects.requireNonNull(player.combatAchievementsList.get(player.getCombatAchievementIndexByOrdinal(CombatAchievements.NIGHTMARE_NOVICE.ordinal())).
					getCombatAchievement()).check(player);


				int rolls = 2;
				if (npc.getDef() != null && npc.getDef().name.equalsIgnoreCase("Dusk"))
					rolls = 2;
				if (player.getEquipment().get(Equipment.SLOT_RING) != null && player.getEquipment().get(Equipment.SLOT_RING).getId() == 30592 && Random.get(3) == 0)
					rolls++;
				if (player.doubleDropBonus.remaining() > 0)
					rolls *= 2;
				if (NPCCombat.rollExtraDonatorDrop(player))
					rolls++;
				if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.ADDITIONAL_ROLL_CHANCE) && Random.get(2) == 0)
					rolls++;
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.SECURING_THE_BAG)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.SECURING_THE_BAG);
					SecuringTheBag c = (SecuringTheBag) player.getPlayerPerkHandler().
						getActivePerks(player).get(perkIndex).getPerk(player);
					assert c != null;
					if (Random.rollPercent(c.getAnotherRollChance())) {
						rolls++;
					}
				}

				for (int i = 0; i < rolls; i++) {
					Item rolled = rollRegular();
					int amount = rolled.getAmount();
					if (amount > 1 && !rolled.getDef().stackable && !rolled.getDef().isNote())
						rolled.setId(rolled.getDef().notedId);
					player.addToCollectionLog(rolled);

					new GroundItem(rolled).owner(player).position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()).spawn();
					if (rolled.getId() == 24420 || rolled.getId() == 24421 || rolled.getId() == 24419 || rolled.getId() == 24417 || rolled.getId() == 24514 || rolled.getId() == 24517 || rolled.getId() == 24511 || rolled.getId() == 24422) {
						Broadcast.GLOBAL.sendNewsDropMessage(player, Icon.ADMINISTRATOR, "<col=000000>" + player.getName(), " has just received <shad=000000>" + Color.DARK_RED.wrap(rolled.getDef().name) + "</shad> from the nightmare!" + "");
						String message = player.getName() + " just received ";
						message += rolled.getDef().descriptiveName;

						RareDropHook.sendDiscordMessage(() -> {
							var jsonObject = new JSONObject();
							jsonObject.put("player", player.getName());
							jsonObject.put("game_mode", player.getGameMode());
							jsonObject.put("item_id", rolled.getId());
							jsonObject.put("item_name", rolled.getDef().name);
							jsonObject.put("source", npc.getDef().descriptiveName);
							jsonObject.put("total_attempts", Utils.formatMoneyString(getNpc().getDef().killCounter.apply(player).getKills() + 1));
							return jsonObject;
						});
					}
				}
				new GroundItem(592, 1).owner(player).position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()).spawn();
				new GroundItem(995, Random.get(100000, 150000)).owner(player).position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()).spawn();
			}

			killer.player.sendMessage("You have defeated The nightmare!");
			killer.player.sendMessage("You can leave by using the energy barrier.");
		};
//		nightmare.hitsUpdate.hpBarType = 8;
		World.startEvent(event -> {
			while (true) {
				if (!npc.getCombat().isDead() && !npc.isHidden() && !npc.isRemoved()
					&& (npc.localPlayers().isEmpty() || npc.localPlayers().stream().noneMatch(p -> ProjectileRoute.allow(npc, p)))) {// no players in sight, reset
					restore();
				}
				event.delay(3);
			}
		});
	}

	public static LootTable regularTable = new LootTable()
		.addTable(55,
			new LootItem(1320, Random.get(2, 5), 3), //Rune 2h sword
			new LootItem(1276, Random.get(2, 5), 3), //Rune pickaxe
			new LootItem(1304, Random.get(2, 5), 2), //Rune longsword
			new LootItem(1290, Random.get(2, 5), 2), //Rune sword
			new LootItem(5316, Random.get(3, 5), 3), //magic seed

			new LootItem(2, 3000, 3), //cannonballs x3000
			new LootItem(6686, 50, 3), //saradomin brew noted x50
			new LootItem(12696, 25, 1), //super combat pooition noted x25
			new LootItem(5730, 1, 3) //dragon spear
		)
		.addTable(55,
			new LootItem(1128, 5, 3),
			new LootItem(4088, Random.get(2, 3), 3),
			new LootItem(22804, Random.get(50, 125), 3),
			new LootItem(19484, Random.get(50, 125), 3)
		)
		.addTable(55,
			new LootItem(561, Random.get(600, 700), 5),
			new LootItem(21880, Random.get(300, 500), 5),
			new LootItem(563, Random.get(500, 600), 5),
			new LootItem(565, Random.get(500, 700), 5),
			new LootItem(560, Random.get(600, 700), 5)
		)
		.addTable(55,
			new LootItem(450, Random.get(25, 50), 4),
			new LootItem(452, Random.get(15, 20), 4),
			new LootItem(454, Random.get(115, 120), 3)
		)
		.addTable(55,
			new LootItem(995, Random.get(200000, 250000), 3),
			new LootItem(5300, 1, 3),
			new LootItem(1514, Random.get(15, 20), 3),
			new LootItem(3024, 3, 1),
			new LootItem(3052, 3, 1),

			new LootItem(12073, 1, 1)
		)
		.addTable(3,
			new LootItem(24420, 1, 1),
			new LootItem(24421, 1, 1),
			new LootItem(24419, 1, 1),
			new LootItem(24417, 1, 1)
		)
		.addTable(1,
			new LootItem(24514, 1, 1),
			new LootItem(24517, 1, 1),
			new LootItem(24511, 1, 1),
			new LootItem(24422, 1, 6)
		);

	private static Item rollRegular() {
		return regularTable.rollItem();
	}

	@Override
	protected boolean canAggro(Player player) {
		return nightmare != null && nightmare.isAttackable();
	}

	private void postDefend(Hit hit) {

	}

	private void afterDamaged(Hit hit) {
		if (isDead()) return;
		if (hit.attacker == null || hit.attacker.player == null) return;
		Player player = hit.attacker.player;
		if (playerDamage.containsKey(player.getName())) {
			playerDamage.put(player.getName(), playerDamage.get(player.getName()) + hit.damage);
		} else {
			playerDamage.put(player.getName(), hit.damage);
		}
	}

	@Override
	public void follow() {
		follow(1);
	}

	@Override
	public void setTarget(Entity target) {
		if (!nightmare.isAttackable()) {
			return;
		}
		super.setTarget(target);
	}

	@Override
	public boolean attack() {
		if (nightmare == null) {
			return true;
		}
		/*
		 * Special attack.
		 */
		if (special != null) {
			nightmare.animate(special.animation);
			special.run(nightmare);
			special = null;
			return true;
		}

		/*
		 * Melee attack.
		 */
//		for (Player p : )

		/*
		 * Default attack.
		 */
		if (Misc.random(10) > 5) {
			for (Entity victim : nightmare.getPossibleTargets(64, true, false)) {
				nightmare.animate(MAGIC_ANIMATION);
				nightmare.getCombat().setTarget(victim);
				World.startEvent(e -> {
					e.delay(1);
					Projectile pr = new Projectile(MAGIC_PROJECTILE, 110, 90, 30, 56, 10, 10, 64);
					pr.send(nightmare, victim);
				});
				final Position dest = victim.getPosition();
				int delay = 60;
				if (nightmare.getCentrePosition().distance(dest) == 1) {
					delay = 60;
				} else if (nightmare.getCentrePosition().distance(dest) <= 5) {
					delay = 80;
				} else if (nightmare.getCentrePosition().distance(dest) <= 8) {
					delay = 100;
				} else {
					delay = 120;
				}
				throttleFarcast(nightmare, (Player) victim, delay, dest, Prayer.PROTECT_FROM_MAGIC);
			}
		} else {
			for (Entity victim : nightmare.getPossibleTargets(64, true, false)) {
				int delay = 60;
				nightmare.animate(RANGE_ANIMATION);
				nightmare.getCombat().setTarget(victim);
				World.startEvent(e -> {
					e.delay(1);
					Projectile pr = new Projectile(RANGED_PROJECTILE, 110, 90, 30, 56, 10, 10, 64);
					pr.send(nightmare, victim);
				});
				final Position dest = victim.getPosition();
				if (nightmare.getCentrePosition().distance(dest) == 1) {
					delay = 60;
				} else if (nightmare.getCentrePosition().distance(dest) <= 5) {
					delay = 80;
				} else if (nightmare.getCentrePosition().distance(dest) <= 8) {
					delay = 100;
				} else {
					delay = 120;
				}
				throttleFarcast(nightmare, (Player) victim, delay, dest, Prayer.PROTECT_FROM_MISSILES);
			}
		}
		return true;
	}

	@Override
	public void process() {

	}

	public boolean damagedPlayer = false;

	private void throttleFarcast(Entity nightmare, Player victim, int delay, Position dest, Prayer prayer) {
		World.startEvent(e -> {
			e.delay((delay / 20) + 1);
			int max = !victim.getPrayer().isActive(prayer) ? 40 : 0;
			if (max != 0)
				damagedPlayer = true;
			victim.hit(new Hit().randDamage(max));
		});
	}

	public void setSpecial(SpecialAttacks attack) {
		special = attack;
	}

}

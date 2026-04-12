package io.ruin.model.skills.hunter.creature;

import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.NPCType;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.*;
import io.ruin.model.content.HomeHandler;
import io.ruin.model.content.camelstatue.CamelStatueHandler;
import io.ruin.model.content.camelstatue.CamelStatueRewards;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.player.PlayerCounter;
import io.ruin.model.entity.shared.listeners.SpawnListener;
import io.ruin.model.item.Item;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.route.routes.ProjectileRoute;
import io.ruin.model.skills.hunter.traps.Trap;
import io.ruin.model.skills.hunter.traps.TrapType;
import io.ruin.model.stat.StatType;
import io.ruin.process.event.Event;
import io.ruin.utility.Misc;
import lombok.Getter;


import java.util.ArrayList;
import java.util.List;

import static io.ruin.model.skills.hunter.Hunter.destroyTrap;

@Getter
public abstract class Creature {

	public Creature(String creatureName, int npcId, int levelReq, double catchXP, double baseCatchChance, int respawnTicks, PlayerCounter counter) {
		this.creatureName = creatureName;
		this.npcId = npcId;
		this.levelReq = levelReq;
		this.respawnTicks = respawnTicks;
		this.baseCatchChance = baseCatchChance;
		this.catchXP = catchXP;
		this.counter = counter;
	}

	private final String creatureName;
	private final int npcId;
	private final int levelReq;
	private final int respawnTicks;
	private final double baseCatchChance;
	private final double catchXP;
	private final PlayerCounter counter;

	public abstract TrapType getTrapType();

	public abstract List<Item> getLoot();

	public boolean hasRoomForLoot(Player player) {
		return player.getInventory().getFreeSlots() >= (getLoot().size() + 1); // + 1 for trap
	}

	/**
	 * Standard behavior just path to the trap's coords
	 */
	public void pathToTrap(NPC npc, GameObject trap) {
		npc.getRouteFinder().routeAbsolute(trap.x, trap.y);
	}


	public void registerSpawnEvent() {
		SpawnListener.register(npcId, npc -> {
			npc.addEvent(event -> {
				while (true) {
					creatureLoop(npc, event);
				}
			});
		});
		NPCType.get(npcId).ignoreOccupiedTiles = true;
	}

	private double getPetDonatorBoost(Player player) {
		return HomeHandler.getPetDonatorBoost(player);
	}


	protected void creatureLoop(NPC npc, Event event) {
		if (npc.localPlayers().isEmpty()) {
			event.delay(3);
			return;
		}
		Trap trap = findTrap(npc);
		if (trap == null) { // no target found
			event.delay(3);
			return;
		}
		trap.setBusy(true); // we found a trap, mark it as busy and path to it
		npc.lock(); // no random movement!
		pathToTrap(npc, trap.getObject());
		event.waitForMovement(npc); // wait for npc to get there
		event.delay(1);
		if (!areConditionsStillValid(npc, trap))
			return;
		prepareForCatchAttempt(npc, trap, event); // do whatever the npc needs to do to prepare for a catch attempt, for example birds have to face north and play descent animation
		if (!areConditionsStillValid(npc, trap))
			return;
		if (rollCatch(npc, trap)) {
			Player player = trap.getOwner();
			if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.HUNTING_IN_ITS_TRUE_FORM)) {
				int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.HUNTING_IN_ITS_TRUE_FORM);
				HuntingInItsTrueForm c = (HuntingInItsTrueForm) player.getPlayerPerkHandler().
					getActivePerks(player).get(perkIndex).getPerk(player);
				assert c != null;
				if (Random.rollPercent(c.getChanceToCollectCapturedCreature())) {
					trap.resetTrap();
					trap.setBusy(false);
					npc.setHidden(true);
					player.getStats().addXp(StatType.Hunter, getCatchXP(), true);
					this.getLoot().forEach(item -> {
						if (item.getDef().name.contains("chinchompa")) {
							if (item.getDef().name.toLowerCase().contains("red")) {
								int baseRate = 4000;
								if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
									int index = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
									ThePetHunter ph = (ThePetHunter) player.getPlayerPerkHandler().
										getActivePerks(player).get(index).getPerk(player);
									baseRate *= ph.getPetChanceBoost();
								}
								if (player.petDropBonus.isDelayed())
									baseRate *= 0.8F;
								baseRate *= getPetDonatorBoost(player);
								if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
									baseRate *= 0.85F;

								if (Random.get(baseRate) == 0) {
									Pet.BABY_CHINCHOMPA_RED.unlock(player, 0);
								}
							} else if (item.getDef().name.toLowerCase().contains("black")) {
								int baseRate = 3000;
								if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
									int index = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
									ThePetHunter ph = (ThePetHunter) player.getPlayerPerkHandler().
										getActivePerks(player).get(index).getPerk(player);
									baseRate *= ph.getPetChanceBoost();
								}
								if (player.petDropBonus.isDelayed())
									baseRate *= 0.8F;
								baseRate *= getPetDonatorBoost(player);
								if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
									baseRate *= 0.85F;
								if (Random.get(baseRate) == 0) {
									Pet.BABY_CHINCHOMPA_BLACK.unlock(player, 0);
								}
							} else if (item.getDef().name.toLowerCase().contains("purple")) {
								int baseRate = 2000;
								if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
									int index = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
									ThePetHunter ph = (ThePetHunter) player.getPlayerPerkHandler().
										getActivePerks(player).get(index).getPerk(player);
									baseRate *= ph.getPetChanceBoost();
								}
								if (player.petDropBonus.isDelayed())
									baseRate *= 0.8F;
								baseRate *= getPetDonatorBoost(player);
								if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
									baseRate *= 0.85F;
								if (Random.get(baseRate) == 0) {
									Pet.BABY_CHINCHOMPA_GREY.unlock(player, 0);
								}
							} else {
								int baseRate = 5000;
								if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
									int index = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
									ThePetHunter ph = (ThePetHunter) player.getPlayerPerkHandler().
										getActivePerks(player).get(index).getPerk(player);
									baseRate *= ph.getPetChanceBoost();
								}
								if (player.petDropBonus.isDelayed())
									baseRate *= 0.8F;
								baseRate *= getPetDonatorBoost(player);
								if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
									baseRate *= 0.85F;
								if (Random.get(baseRate) == 0) {
									Pet.BABY_CHINCHOMPA_GREY.unlock(player, 0);
								}
							}
							if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.TAKING_IT_ON_THE_CHIN)) {
								int pi = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.TAKING_IT_ON_THE_CHIN);
								TakingItOnTheChin d = (TakingItOnTheChin) player.getPlayerPerkHandler().
									getActivePerks(player).get(pi).getPerk(player);
								assert d != null;
								item.setAmount(d.getChinsToCapture());
							}

						}
						if (player.getInventory().hasRoomFor(item)) {
							player.getInventory().add(item);
							player.sendFilteredMessage(NumberUtils.formatNumber(item.getAmount()) + "x " + item.getDef().getName() + " has been added to your inventory.");
						} else {
							player.getBank().add(item.getId(), item.getAmount());
							player.sendFilteredMessage(NumberUtils.formatNumber(item.getAmount()) + "x " + item.getDef().getName() + " has been added to your bank.");
						}
					});

				} else {
					trap.setTrappedCreature(this);
					succeedCatch(npc, trap, event);
				}
			} else {
				trap.setTrappedCreature(this);
				succeedCatch(npc, trap, event);
			}
			//handle respawning
			event.delay(getRespawnTicks());
			npc.getMovement().teleport(npc.spawnPosition);
			npc.face(npc.spawnDirection);
			npc.setHidden(false);
			npc.unlock();
		} else {
			Player player = trap.getOwner();
			if (!player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.HUNTING_IN_ITS_TRUE_FORM)) {
				failCatch(npc, trap, event);
				npc.unlock();
			}
			trap.setBusy(false);
		}
		event.delay(1);

	}

	protected boolean areConditionsStillValid(NPC npc, Trap trap) {
		if ((npc.getCombat() == null || !npc.getCombat().isDead())  // npc hasnt been killed (if it can be killed)
			&& trap.getObject()
			.getId() == trap.getTrapType().getActiveObjectId() && !trap.getObject().isRemoved()) {// trap is still active
			return true;
		}
		abortCatch(npc, trap);
		return false;

	}

	protected void abortCatch(NPC npc, Trap trap) {
		npc.unlock();
		npc.getMovement().reset();
		trap.setBusy(false);
	}


	private Trap findTrap(NPC npc) {
		ArrayList<Trap> viableTraps = new ArrayList<>();
		for (Player player : npc.localPlayers()) {
			player.traps.forEach(trap -> {
				if (Random.get() > ignoreTrapChance(npc, trap) && isViableTrap(npc, trap))
					viableTraps.add(trap);
			});
		}
		if (viableTraps.size() == 0)
			return null;
		return Random.get(viableTraps);
	}

	public double ignoreTrapChance(NPC npc, Trap trap) {
		return 0.75;
	}

	public boolean isViableTrap(NPC npc, Trap trap) {
		return Misc.getDistance(npc.getPosition(), trap.getObject().x, trap.getObject().y) <= 12 // within reasonable distance
			&& trap.getTrapType() == getTrapType() // correct trap type
			&& ProjectileRoute.allow(npc, trap.getObject().x, trap.getObject().y) // npc has line of sight to the trap
			&& trap.getObject()
			.getId() == trap.getTrapType().getActiveObjectId() // trap is in ready to catch state
			&& !trap.isBusy(); // trap not already locked onto by another npc
	}

	protected abstract void prepareForCatchAttempt(NPC npc, Trap trap, Event event);


	public boolean rollCatch(NPC npc, Trap trap) {
		Player owner = trap.getOwner();
		if (owner == null)
			return false;
		double chance = getBaseCatchChance();
		int levelDiff = owner.getStats().get(StatType.Hunter).currentLevel - getLevelReq();
		if (levelDiff < 0) {
			owner.sendMessage("You must have a Hunter level of at least " + getLevelReq() + " to catch " + getCreatureName() + ".");
			return false;
		}
		chance *= 1 + (levelDiff * 0.02); // relative 2% increase per level
		if (ProjectileRoute.allow(npc, owner)) {
			chance *= 0.9; // 10% penalty if hunter is in line of sight of the creature
		}
		return Random.get() <= Math.min(0.90, chance);
	}

	/**
	 * Note that the creature AI loop does NOT hide the NPC on a successful catch, this must be done in this method to ensure the npc goes invisible with the correct timing
	 */
	protected abstract void succeedCatch(NPC npc, Trap trap, Event event);

	/**
	 * There is some weird fuckery going on with this method in my IntelliJ, subclasses keep giving compile errors saying it isn't implemented when it actually is...
	 * Very annoying but deleting the method here and re-adding it seems to get rid of the error until the subclass is changed again, hopefully it only happens for me
	 */
	protected abstract void failCatch(NPC npc, Trap trap, Event event);


	public void check(Player player, GameObject obj) {
		if (!hasRoomForLoot(player)) {
			player.sendMessage("Not enough space in your inventory.");
			return;
		}
		player.startEvent(event -> {
			player.lock();
			player.animate(obj.trap.getTrapType().getDismantleAnimation());
			event.delay(2);
			double catchExp = getCatchXP();
			if (creatureName.contains("chinchompa") || creatureName.contains("Chinchompa")) {
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.TAKING_IT_ON_THE_CHIN)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.TAKING_IT_ON_THE_CHIN);
					TakingItOnTheChin c = (TakingItOnTheChin) player.getPlayerPerkHandler().
						getActivePerks(player).get(perkIndex).getPerk(player);
					assert c != null;
					catchExp *= c.getExperienceBoost();
				}
			}
			addLoot(player);
			player.getStats().addXp(StatType.Hunter, catchExp, true);
			if (counter != null)
				counter.increment(player, 1);
			obj.trap.getTrapType().onRemove(player, obj);
			destroyTrap(obj);
			player.unlock();
		});

	}

	protected void addLoot(Player player) {
		getLoot().forEach(item -> {
			if (item.getDef().getName().contains("chinchompa")) {
				if (item.getDef().name.toLowerCase().contains("red")) {
					int baseRate = 4000;
					if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
						int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
						ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
							getActivePerks(player).get(perkIndex).getPerk(player);
						baseRate *= c.getPetChanceBoost();
					}
					if (player.petDropBonus.isDelayed())
						baseRate *= 0.8F;
					baseRate *= getPetDonatorBoost(player);
					if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
						baseRate *= 0.85F;

					if (Random.get(baseRate) == 0) {
						Pet.BABY_CHINCHOMPA_RED.unlock(player, 0);
					}
				} else if (item.getDef().name.toLowerCase().contains("black")) {
					int baseRate = 3000;
					if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
						int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
						ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
							getActivePerks(player).get(perkIndex).getPerk(player);
						baseRate *= c.getPetChanceBoost();
					}
					if (player.petDropBonus.isDelayed())
						baseRate *= 0.8F;
					baseRate *= getPetDonatorBoost(player);
					if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
						baseRate *= 0.85F;
					if (Random.get(baseRate) == 0) {
						Pet.BABY_CHINCHOMPA_BLACK.unlock(player, 0);
					}
				} else if (item.getDef().name.toLowerCase().contains("purple")) {
					int baseRate = 2000;
					if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
						int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
						ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
							getActivePerks(player).get(perkIndex).getPerk(player);
						baseRate *= c.getPetChanceBoost();
					}
					if (player.petDropBonus.isDelayed())
						baseRate *= 0.8F;
					baseRate *= getPetDonatorBoost(player);
					if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
						baseRate *= 0.85F;
					if (Random.get(baseRate) == 0) {
						Pet.BABY_CHINCHOMPA_GREY.unlock(player, 0);
					}
				} else {
					int baseRate = 5000;
					if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.THE_PET_HUNTER)) {
						int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.THE_PET_HUNTER);
						ThePetHunter c = (ThePetHunter) player.getPlayerPerkHandler().
							getActivePerks(player).get(perkIndex).getPerk(player);
						baseRate *= c.getPetChanceBoost();
					}
					if (player.petDropBonus.isDelayed())
						baseRate *= 0.8F;
					baseRate *= getPetDonatorBoost(player);
					if (CamelStatueHandler.getActiveRewards().contains(CamelStatueRewards.BOOSTED_PET_RATES))
						baseRate *= 0.85F;
					if (Random.get(baseRate) == 0) {
						Pet.BABY_CHINCHOMPA_GREY.unlock(player, 0);
					}
				}
				item.setAmount(1);
				if (player.getPlayerPerkHandler().getActivePerks(player).contains(Perks.TAKING_IT_ON_THE_CHIN)) {
					int perkIndex = player.getPlayerPerkHandler().getActivePerkIndex(player, Perks.TAKING_IT_ON_THE_CHIN);
					TakingItOnTheChin c = (TakingItOnTheChin) player.getPlayerPerkHandler().
						getActivePerks(player).get(perkIndex).getPerk(player);
					assert c != null;
					item.setAmount(c.getChinsToCapture());
				}

			}
			player.collectResource(item);
			if (player.blackChinchompaBoost.isDelayed()) {
				boolean extra = Random.rollPercent(20);
				if (extra) item.incrementAmount(1);
			}
			player.getInventory().add(item.getId(), item.getAmount());
		});
		player.getInventory().add(getTrapType().getItemId(), 1);
	}
}

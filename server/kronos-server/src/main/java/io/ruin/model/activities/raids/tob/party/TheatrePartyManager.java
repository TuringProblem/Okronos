package io.ruin.model.activities.raids.tob.party;

import com.google.common.collect.Lists;
import io.ruin.api.utils.StringUtils;
import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.activities.raids.tob.dungeon.RoomType;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.MapListener;
import io.ruin.model.map.Position;
import io.ruin.model.map.dynamic.DynamicMap.DynamicMapBuildException;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.stat.StatType;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.util.ListenedList;
import io.ruin.utility.CS2Script;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handles everything to do with managing tob parties.
 * <p>
 * applicant cs2 2321 "s" name | splitter
 * member cs2 2317 "is" leaderidx name | splitter
 *
 * @author ReverendDread on 7/15/2020
 *         https://www.rune-server.ee/members/reverenddread/
 * @project Kronos
 */
@Slf4j
public final class TheatrePartyManager {

	private static TheatrePartyManager singleton;
	private static final String SEPERATOR = "|";
	@Getter
	public static final int[] slots = {
			9, 8, 7, 6, 5, 44,
			43, 42, 41, 40, 4,
			39, 38, 37, 36, 35,
			34, 33, 32, 31, 30,
			3, 29, 28, 27, 26,
			25, 24, 23, 22, 21,
			20, 2, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 1, 0
	};
	public static final int IN_PARTY = 1, IS_LEADER = 2, WITHDRAW_APP = 3, APPLY_APP = 4;
	public static final Position OUTSIDE = Position.of(3677, 3219, 0);
	private static final Object LOCK = new Object();
	/**
	 * List of active parties.
	 */
	private final ListenedList<TheatreParty> parties = new ListenedList<>();

	public TheatrePartyManager() {
		parties.postRemove(TheatreParty::disbandNotifiy);
	}

	/**
	 * Registers a party to the active parties.
	 *
	 */
	public synchronized Optional<TheatreParty> register(Player leader) {
		synchronized (LOCK) {
			if (parties.stream().anyMatch(p -> p.getUsers().contains(leader.getName()))) {
				leader.sendMessage("You're already in a party.");
				return Optional.empty();
			}

			int slot = findSlot();
			if (slot == -1) {
				leader.sendMessage("No party slots available.");
				return Optional.empty();
			}

			TheatreParty party = new TheatreParty(leader.getName(), slot);
			parties.add(party);
			leader.currentParty = party;
			return Optional.of(party);
		}
	}

	public synchronized void cleanupParties() {
		parties.removeIf(party -> {
			if (party.getUsers().isEmpty()) {
				party.cleanupRaid();
				return true;
			}
			return false;
		});
	}

	public boolean addMember(Player player, TheatreParty party) {
		synchronized (LOCK) {
			if (player.currentParty != null) {
				player.sendMessage("You're already in a party.");
				return false;
			}

			if (party.getUsers().size() >= 5) {
				player.sendMessage("This party is full.");
				return false;
			}

			party.getUsers().add(player.getName());
			player.currentParty = party;
			return true;
		}
	}

	public void deregister(TheatreParty party) {
		getPartyForSlot(party.getSlot())
				.ifPresent((found) -> {
					if (found.dungeon != null && found.dungeon.isBuilt())
						found.cleanupRaid();

					found.getUsers()
							.forEach(username -> forUsername(username)
									.ifPresent(p -> {
										p.currentParty = null;
										VarPlayerRepository.TOB_PARTY_LEADER.set(p, -1);
										sendBlankPartyMembers(p.getName());
									}));
					found.disbandNotifiy();
					parties.remove(found);
				});
	}

	/**
	 * Called when the party leader leaves.
	 *
	 */
	public void leaderLeave(TheatreParty party) {
		getPartyForSlot(party.getSlot()).ifPresent((found) -> {
			// TODO: make the next person the new leader
		});
	}

	/**
	 * Shows the active parties list.
	 *
	 */
	public void openPartyList(Player player) {
		// System.out.println("are we called");
		player.openInterface(ToplevelComponent.MAINMODAL, Interface.TOB_PARTY_LIST);
		for (int index = 0; index < getSlots().length; index++) {
			int slot = getSlots()[index];
			if (index < parties.size()) {
				TheatreParty party = parties.get(index);
				Player leader = World.getPlayer(party.getLeaderName());
				if (leader == null)
					continue;
				var partyDetails = StringUtils.capitalizeFirst(leader.getName()) + SEPERATOR +
						party.getUsers().size() + SEPERATOR +
						party.getPreferedSize() + SEPERATOR +
						party.getPreferedLevel() + SEPERATOR;
				CS2Script.TOB_PARTYLIST_ADDLINE.sendScript(player, slot, partyDetails);
			} else {
				CS2Script.TOB_PARTYLIST_ADDLINE.sendScript(player, slot, "");
			}
		}
	}

	/**
	 * Opens the party details info for the desired slot.
	 *
	 */
	public void openPartyDetailsViaSlot(Player player) {
		getPartyForSlot(player.viewingTheatreSlot).ifPresent((party) -> {
			player.openInterface(ToplevelComponent.MAINMODAL, Interface.TOB_PARTY_DETAILS);
			preparePartyDetails(player, party);
		});
	}

	/**
	 * Opens the party details info for the players party.
	 *
	 */
	public void openPartyDetails(Player player) {
		getPartyForPlayer(player.getName()).ifPresent((party) -> {
			player.openInterface(ToplevelComponent.MAINMODAL, Interface.TOB_PARTY_DETAILS);
			player.viewingTheatreSlot = party.getSlot();
			preparePartyDetails(player, party);
		});
	}

	private void preparePartyDetails(Player player, @NotNull TheatreParty party) {
		for (int index = 0; index < 5; index++) {
			if (index < party.getUsers().size()) {
				String username = party.getUsers().get(index);
				forUsername(username).ifPresent(member -> CS2Script.TOB_PARTYDETAILS_ADDMEMBER.sendScript(player,
						party.isLeader(player) ? IS_LEADER : IN_PARTY, buildMember(member)));
			} else
				CS2Script.TOB_PARTYDETAILS_ADDMEMBER.sendScript(player, party.isLeader(player) ? IS_LEADER : IN_PARTY,
						buildBlank());
		}
		party.getApplicants()
				.forEach(username -> forUsername(username).ifPresent(
						applicant -> CS2Script.TOB_PARTYDETAILS_ADDAPPLICANT.sendScript(player, buildApplicant(applicant))));

		refreshPartyDetails(player, party);
	}

	public void refreshPartyDetails(Player player, TheatreParty party) {
		// Check if any party members are still online
		boolean anyOnline = party.getUsers().stream()
				.map(World::getPlayerByName)
				.anyMatch(p -> p != null && player.isOnline()); // Changed from player to p

		if (!anyOnline) {
			party.cleanupRaid();
			deregister(party);
			openPartyList(player);
			return;
		}

		CS2Script.TOB_PARTYDETAILS_REFRESH.sendScript(player, getPartyStatus(player, party), party.getPreferedSize(),
				party.getPreferedLevel());
	}

	private boolean anyMembersOnline(TheatreParty party) {
		return party.getUsers().stream()
				.map(World::getPlayerByName)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.anyMatch(Player::isOnline);
	}

	public synchronized void cleanupAndValidateParties() {
		// Remove empty/dead parties
		parties.removeIf(party -> {
			if (party.getUsers().isEmpty() || !anyMembersOnline(party)) {
				party.cleanupRaid();
				return true;
			}
			return false;
		});

		// Fix any duplicate memberships
		for (Player player : World.players()) {
			if (player == null)
				continue;

			TheatreParty firstParty = null;
			for (TheatreParty party : parties) {
				if (party.getUsers().contains(player.getName())) {
					if (firstParty == null) {
						firstParty = party;
					} else {
						// Remove from all but first party found
						party.leave(player.getName(), false);
						player.sendMessage("You were removed from an invalid party state.");
					}
				}
			}
			player.currentParty = firstParty;
		}
	}

	public int getPartyStatus(Player player, TheatreParty party) {
		// Check if player is in ANY party before allowing application
		if (player.currentParty != null && player.currentParty != party) {
			player.sendMessage("You cannot join/apply - you are already in a party.");
			return IN_PARTY;
		}

		return party.isLeader(player) ? IS_LEADER
				: party.getUsers().contains(player.getName()) ? IN_PARTY
						: party.getApplicants().contains(player.getName()) ? WITHDRAW_APP : APPLY_APP;
	}

	/**
	 * Builds a formatted string for a member.
	 *
	 * @param member The member.
	 */
	private String buildMember(Player member) {
		return buildApplicant(member);
	}

	/**
	 * Builds a formatted string for a member or applicant.
	 *
	 * @param member The member.
	 */
	private String buildApplicant(Player member) {
		return StringUtils.capitalizeFirst(member.getName()) + SEPERATOR +
				member.getCombat().getLevel() + SEPERATOR +
				member.getStats().get(StatType.Attack).fixedLevel + SEPERATOR +
				member.getStats().get(StatType.Strength).fixedLevel + SEPERATOR +
				member.getStats().get(StatType.Ranged).fixedLevel + SEPERATOR +
				member.getStats().get(StatType.Magic).fixedLevel + SEPERATOR +
				member.getStats().get(StatType.Defence).fixedLevel + SEPERATOR +
				member.getStats().get(StatType.Hitpoints).fixedLevel + SEPERATOR +
				member.getStats().get(StatType.Prayer).fixedLevel + SEPERATOR +
				member.theatreOfBloodCompleted + SEPERATOR;
	}

	/**
	 * Builds a blank party member for the party details.
	 *
	 */
	private String buildBlank() {
		return "-%s-%s-%s-%s-%s-%s-%s-%s-%s-%s"
				.formatted(
						SEPERATOR,
						SEPERATOR,
						SEPERATOR,
						SEPERATOR,
						SEPERATOR,
						SEPERATOR,
						SEPERATOR,
						SEPERATOR,
						SEPERATOR,
						SEPERATOR);
	}

	/**
	 * Gets a party optional for the desired userId.
	 *
	 */
	public Optional<TheatreParty> getPartyForPlayer(String user) {
		TheatreParty party = null;
		for (TheatreParty p : parties) {
			if (p.getUsers().contains(user))
				party = p;
		}
		return Optional.ofNullable(party);
	}

	/**
	 * Gets a party optional for the desired slot.
	 *
	 */
	public Optional<TheatreParty> getPartyForSlot(int slot) {
		return parties.stream().filter(party -> party.getSlot() == slot).findFirst();
	}

	/**
	 * Finds an empty slot for a party.
	 *
	 */
	private int findSlot() {
		for (int slot : getSlots()) {
			boolean isFree = parties.stream().noneMatch(theatreParty -> theatreParty.getSlot() == slot);
			if (isFree)
				return slot;
		}
		return -1;
	}

	/**
	 * Sends blank party members on the party overlay.
	 *
	 */
	public void sendBlankPartyMembers(String username) {
		forUsername(username).ifPresent((player) -> {
			StringBuilder sb = new StringBuilder();
			for (int index = 0; index < 5; index++) {
				sb.append("-");
				if (index < 4)
					sb.append("<br>");
			}
			player.getPacketSender().sendString(Interface.TOB_PARTY_MEMBERS_OVERLAY, 12, sb.toString());
		});
	}

	/**
	 * Gets the player assosiated with the desired userName.
	 *
	 */
	public Optional<Player> forUsername(String userName) {
		return World.getPlayerByName(userName);
	}

	/**
	 * Returns a singleton of the theatre party manager.
	 *
	 */
	public static TheatrePartyManager instance() {
		if (singleton == null)
			singleton = new TheatrePartyManager();
		return singleton;
	}

	public static void register() {

		MapListener.registerBounds(
				new Bounds(
						new Position(3642, 3204, 0),
						new Position(3683, 3234, 0), 0))
				.onEnter(player -> {
					player.openInterface(ToplevelComponent.OVERLAY, Interface.TOB_PARTY_MEMBERS_OVERLAY);
					TheatrePartyManager.instance().sendBlankPartyMembers(player.getName());
				})
				.onExit((player, logout) -> {
					if (!player.inTob)
						TheatrePartyManager.instance()
								.getPartyForPlayer(player.getName())
								.ifPresent(party -> {
									party.leave(player.getName(), false);
									party.getUsers().remove(player.getName());
									player.currentParty = null;
								});
				});

		ObjectAction.register(32653, "enter", (player, obj) -> {
			boolean partyPresentForPlayer = TheatrePartyManager.instance().getPartyForPlayer(player.getName()).isPresent();
			if (partyPresentForPlayer) {

				TheatrePartyManager.instance()
					.getPartyForPlayer(player.getName())
					.ifPresent(party -> {
						if (!player.acceptedTheatreRisk) {
							player.dialogue(
									new MessageDialogue("Warning: The Theatre of Blood is dangerous. Once you enter, " +
											"you are at risk of death. The only method of escape is to resign or defeat the Theatre." +
											"Teleporting is restricted and logging out is considered a death. Your items will be lost if the whole party dies."),
									new MessageDialogue("You will not see the warning again should you accept."),
									new OptionsDialogue(
											new Option("Yes - proceed.", () -> {
												player.acceptedTheatreRisk = true;
												crystals(player, party);
											}),
											new Option("No - stay out.")));
						}
						else
							crystals(player, party);
					});
			}
			else {
				player.dialogue(new OptionsDialogue(Color.MAROON.wrap("You are not in a party..."),
						new Option("Create a party?", () -> {
							TheatrePartyManager.instance().register(player);
						}),
						new Option("<str>Observe a specific party</str>", () -> {
							/* TODO */ }),
						new Option("<str>Observe recent party</str>", () -> {
							/* TODO */ }),
						new Option("Cancel")));
			}
		});

		ObjectAction.register(32756, "resign", ((player, obj) -> {
			TheatrePartyManager.instance().getPartyForPlayer(player.getName()).ifPresent(party -> {
				party.leave(player.getName(), true);
			});
		}));
		ObjectAction.register(32756, "talk-to", ((player, obj) -> {
			player.dialogue(new NPCDialogue(8323, "Lady Verzik Litur lets people in here to perform, not to chat."),
					new OptionsDialogue(
							new Option("What am I supposed to do in here?", () -> {
								player.dialogue(
										new NPCDialogue(8323,
												"Pass through the barrier and face your challenge. If you survive, and your struggle entertains Verzik, she will grant you freedom from the blood tithes."),
										new PlayerDialogue("Okay."));
							}),
							new Option("I want to resign from the party.", () -> player.dialogue(
									new PlayerDialogue("I want to resign from the party."),
									new OptionsDialogue("There is no penalty for resigning now.",
											new Option("Resign and leave the Theatre.", () -> {
												TheatrePartyManager.instance().getPartyForPlayer(player.getName()).ifPresent(party -> {
													party.leave(player.getName(), true);
												});
											}),
											new Option("Do not resign.", () -> player.dialogue(
													new PlayerDialogue("Actually, I'll stay in for now."),
													new NPCDialogue(8323, "As you wish.")))))),
							new Option("Sorry, I'll get on with it.",
									() -> player.dialogue(new PlayerDialogue("Sorry I'll get on with it.")))));
		}));
		// ObjectAction.register(32756, "resign", ((player, obj) -> {
		//
		// }));

	}

	/**
	 * Starts the buying crystals dialogue
	 *
	 */
	private static void crystals(Player player, TheatreParty party) {
		if (!player.acceptedTheatreCrystals) {
			player.dialogue(new OptionsDialogue(Color.MAROON.wrap("Only Verzik's crystals can teleport out of the Theatre."),
					new Option("Go and buy teleport crystals.", () -> {
						player.startEvent(e -> {
							player.getRouteFinder().routeAbsolute(3673, 3222);
							e.waitForMovement(player);
							// TODO open shop
						});
					}),
					new Option("Enter the Theatre without any teleport crystals.", () -> enterTheatre(player, party)),
					new Option("Enter the Theatre, and don't ask this again.", () -> {
						player.acceptedTheatreCrystals = true;
						enterTheatre(player, party);
					})));
		} else {
			enterTheatre(player, party);
		}
	}

	/**
	 * Starts the enter tob dialogue
	 *
	 */
	public static boolean disabled = false;

	private static void enterTheatre(Player player, TheatreParty party) {
		if (disabled) {
			player.sendMessage("Temporarily disabled.");
			return;
		}
		if (party.isLeader(player)) {
			player.dialogue(new OptionsDialogue("Is your party ready? (Members: %d)".formatted(party.getUsers().size()),
				new Option("Yes, let's go!", () -> {
					StringBuilder unclaimedLootPlayers = new StringBuilder();
					AtomicBoolean hasUnclaimedLoot = new AtomicBoolean(false);
					party.forPlayers(p -> {
						if(p != null) {
							if(!p.theatreReward.isEmpty()) {
								hasUnclaimedLoot.set(true);
								unclaimedLootPlayers.append(p.getName()).append(", ");
								p.sendMessage("Your party leader tried to start a raid.");
								p.sendMessage("You have unclaimed loot from your previous raid, loot it from the chest towards the bank.");
							}
						}
					});
					if(hasUnclaimedLoot.get()) {
						player.sendMessage("The following players in your party have unclaimed loot: "
							+ (unclaimedLootPlayers.substring(0, unclaimedLootPlayers.length() - 2)));
						return;
					}
					party.forPlayers(p -> {
						TheatreParty.updatePartyStatus(p, PartyStatus.PARTY_INSIDE);
						p.theatreOfBloodStage = 0;
						p.theatreroom = "";
						p.insideRaid = true;
					});

					try {
						party.getDungeon().build();
					} catch (DynamicMapBuildException e) {
						player.sendMessage("Unable to build dynamic map.");
					}

					startDungeon(player, party);

				}),
				new Option("Cancel.")
			));
		} else {
			if (!party.getDungeon().isBuilt()) {
				player.sendMessage(Color.RED.wrap("The party leader has to start the dungeon first."));
				return;
			}
			startDungeon(player, party);
		}
	}

	/**
	 * Starts the dungeon for the desired player, taking in their party.
	 *
	 */
	private static void startDungeon(Player player, TheatreParty party) {
		var isPlayerInParty = party.getUsers().contains(player.getName());
		if (!isPlayerInParty) {
			log.error("Player {} attempted to start the dungeon without being in the party.", player.getName());
			return;
		}
		party.getDungeon().enterRoom(player, RoomType.MAIDEN);
		if (!party.playersInRaid.contains(player))
			party.playersInRaid.add(player);

		if (party.playersInRaid.size() > party.getPreferedSize()) {
			player.getMovement().teleport(TheatrePartyManager.OUTSIDE);
			party.leave(player.getName(), false);
			player.sendMessage("The party has reached the maximum number of players.");
			return;
		}

		VarPlayerRepository.THEATRE_HUD_STATE.set(player, PartyStatus.PARTY_INSIDE.ordinal());
		List<String> names = Lists.newArrayList();
		for (int i = 0; i < 5; i++) {
			if (i < party.getUsers().size()) {
				if (i == 0)
					names.add("Me");
				else
					names.add(TheatrePartyManager.instance().forUsername(party.getUsers().get(i)).get().getName());
			} else {
				names.add("");
			}
		}

		CS2Script.TOB_HUD_STATUSNAMES.sendScript(player, names.toArray());
		player.inTob = true;
	}

}

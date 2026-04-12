package io.ruin.model.entity.player;

import io.ruin.HooksV2;
import io.ruin.model.activities.gamble.GambleManager;
import io.ruin.model.activities.raids.chambersrework.CustomXericRaid;
import io.ruin.model.activities.raids.chambersrework.XericParty;
import io.ruin.model.activities.raids.tob.party.TheatrePartyManager;
import io.ruin.model.activities.raids.xeric.party.RecruitingBoard;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.map.object.actions.impl.edgeville.Christmas;
import io.ruin.model.map.route.routes.TargetRoute;

import java.util.function.BiConsumer;

public enum PlayerAction {

	ATTACK("Attack", true, (p1, p2) -> {
		p1.face(p2);
		p1.getCombat().setTarget(p2);
	}),
	GAMBLE("Gamble", false, (p1, p2) -> {
		p1.face(p2);
		TargetRoute.set(p1, p2, () -> {
			if (p2.getGamble().stage > 0) {
				p1.sendMessage("That player is busy.");
				return;
			}
			GambleManager.init(p1, p2);
			p1.getGamble().request(p2);
			p1.faceNone(true);
		});
	}),
	FOLLOW("Follow", false, (p1, p2) -> {
		p1.face(p2);
		p1.getMovement().following = p2;
	}),
	// MODERATE("Moderate", false, StaffModerationInterface::openStaffModeration),
	TRADE("Trade with", false, (p1, p2) -> {
		p1.face(p2);
		TargetRoute.set(p1, p2, () -> {
			p1.getTrade().request(p2);
			p1.faceNone(true);
		});
	}),
	CHALLENGE("Challenge", false, (p1, p2) -> {
		p1.face(p2);
		TargetRoute.set(p1, p2, () -> {
			// p1.getDuel().request(p2);
			// p1.faceNone(true);
		});
	}),
	PELT("Pelt", true, (p1, p2) -> {
		p1.face(p2);
		Christmas.throwSnow(p1, p2);
	}),
	FIGHT("Fight", true, (p1, p2) -> {
		p1.face(p2);
		p1.getCombat().setTarget(p2);
	}),
	GIM_Invite("Invite-GIM", false, (p1, p2) -> {
	}),
	INVITE("Invite", false, (p1, p2) -> {
		p1.face(p2);
		TargetRoute.set(p1, p2, () -> {
			if (p1.getPosition().getRegion().id == 14642) {
				TheatrePartyManager.instance().getPartyForPlayer(p1.getName()).ifPresent(party -> {
					party.invite(p1, p2);
				});
			} else {
				if (p1.party == null) {
					p1.dialogue(new OptionsDialogue("You are not in a party.", new Option("Create Party", () -> {
						p1.party = new XericParty(p1.getName());
						p1.setCustomXericRaid(new CustomXericRaid());
						p1.sendMessage("You have created a party.");
					}), new Option("Cancel.", p1::closeDialogue)));
				} else {
					p1.sendMessage("You have invited " + p2.getName() + " to your party.");
					p1.party.invite(p1, p2);
					p1.faceNone(true);
				}
			}
		});
	});

	public static interface Hook {
		record Handle(PlayerAction action, Player player, Player target) implements Hook {
		};
	}

	public static HooksV2<Hook> hooks = new HooksV2<>(Hook.class);

	public final String name;
	public final boolean top;
	public final BiConsumer<Player, Player> consumer;

	PlayerAction(String name, boolean top, BiConsumer<Player, Player> consumer) {
		this.name = name;
		this.top = top;
		this.consumer = consumer;
	}

	public void accept(Player player, Player target) {
		this.consumer.accept(player, target);
		if (hooks.handle(new Hook.Handle(this, player, target))) {
			return;
		}
	}
}

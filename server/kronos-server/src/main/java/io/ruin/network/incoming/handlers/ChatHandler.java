package io.ruin.network.incoming.handlers;

import io.ruin.HooksV2;
import io.ruin.api.utils.IPMute;
import io.ruin.model.entity.player.KillCounter;
import io.ruin.model.entity.player.Player;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.services.Loggers;
import io.ruin.services.Punishment;
import io.ruin.utility.BadWords;
import io.ruin.utility.IdHolder;
import net.rsprot.protocol.game.incoming.messaging.MessagePublic;
import net.rsprot.protocol.message.codec.incoming.MessageConsumer;

import static io.ruin.network.ClientProt204.MESSAGE_PUBLIC;

@IdHolder(ids = { MESSAGE_PUBLIC })
public class ChatHandler implements MessageConsumer<Player, MessagePublic> {
	public interface Hook {
		public record OnMessage(Player player, MessagePublic msg) implements Hook {
		};
	}

	public static HooksV2<Hook> hooks = new HooksV2<>(Hook.class);

	@Override
	public void consume(Player player, MessagePublic msg) {
		handle(player, msg.getType(), msg.getEffect(), msg.getMessage());
		var ctx = new Hook.OnMessage(player, msg);
		if (hooks.handle(ctx)) {
			return;
		}
	}

	static void handle(Player player, int type, int effects, String message) {
		boolean shadow = false;
		if (IPMute.isIPMuted(player.getIp()) || Punishment.isMuted(player)) {
			if (!player.shadowMute) {
				player.sendMessage("You're muted and can't talk.");
				return;
			}
			shadow = true;
		}

		if (message.isEmpty()) {
			return;
		}

		if (type == 1) {
			if (player.lastAutoChatMessageElapsed() < 20_000) {
				return;
			}
			player.lastAutochatMessage = System.currentTimeMillis();
		}

		if (VarPlayerRepository.PROFANITY_FILTER.get(player) == 0) {
			message = BadWords.filterBadWords(message);
		}

		if (type == 2) {
			message = message.substring(1);
			if (message.equalsIgnoreCase("kc bandos")) {
				message = "I currently have " + KillCounter.getKills("General Graardor", player) + " General Graardor kills.";
			}
			if (message.equalsIgnoreCase("kc zammy")) {
				message = "I currently have " + KillCounter.getKills("K'ril Tsutsaroth", player) + " K'ril Tsutsaroth kills.";
			}
			if (message.equalsIgnoreCase("kc sara")) {
				message = "I currently have " + KillCounter.getKills("Commander Zilyana", player) + " Commander Zilyana kills.";
			}
			if (message.equalsIgnoreCase("kc arma")) {
				message = "I currently have " + KillCounter.getKills("Kree'arra", player) + " Kree'arra kills.";
			}
			if (message.equalsIgnoreCase("kc araxxor")) {
				message = "I currently have " + player.araxxorKills.getKills() + " Araxxor kills.";
			}
			if (message.equalsIgnoreCase("kc yama")) {
				message = "I currently have " + player.yamaKills.getKills() + " Yama kills.";
			}
			if (message.equalsIgnoreCase("kc tormented demon")) {
				message = "I currently have " + player.tormentedDemonKills.getKills() + " Tormented Demon kills.";
			}
			if (message.equalsIgnoreCase("kc corp")) {
				message = "I currently have " + KillCounter.getKills("Corporeal Beast", player) + " Corporeal Beast kills.";
			}
			if (message.equalsIgnoreCase("kc cerb")) {
				message = "I currently have " + KillCounter.getKills("Cerberus", player) + " Cerberus kills.";
			}
			if (message.equalsIgnoreCase("kc thermy")) {
				message = "I currently have " + player.thermonuclearSmokeDevilKills.getKills()
						+ " Thermonuclear Smoke Devil kills.";
			}
			if (message.equalsIgnoreCase("kc sire")) {
				message = "I currently have " + KillCounter.getKills("Abyssal Sire", player) + " Abyssal Sire kills.";
			}
			if (message.equalsIgnoreCase("kc hydra")) {
				message = "I currently have " + player.alchemicalHydraKills.getKills() + " Alchemical Hydra kills.";
			}
			if (message.equalsIgnoreCase("kc vorkath")) {
				message = "I currently have " + KillCounter.getKills("Vorkath", player) + " Vorkath kills.";
			}
			if (message.equalsIgnoreCase("kc zulrah")) {
				message = "I currently have " + KillCounter.getKills("Zulrah", player) + " Zulrah kills.";
			}
			if (message.equalsIgnoreCase("kc kraken")) {
				message = "I currently have " + KillCounter.getKills("Kraken", player) + " Kraken kills.";
			}
			if (message.equalsIgnoreCase("kc leviathan")) {
				message = "I currently have " + player.leviathanKills.getKills() + " Leviathan kills.";
			}
			if (message.equalsIgnoreCase("kc whisperer")) {
				message = "I currently have " + player.whispererKills.getKills() + " The Whisperer kills.";
			}
			if (message.equalsIgnoreCase("kc duke")) {
				message = "I currently have " + player.dukeKills.getKills() + " Duke Sucellus kills.";
			}
			if (message.equalsIgnoreCase("kc vardorvis")) {
				message = "I currently have " + player.vardorvisKills.getKills() + " Vardorvis kills.";
			}
			if (message.equalsIgnoreCase("kc phantom muspah")) {
				message = "I currently have " + player.phantomMuspahKills.getKills() + " Phantom Muspah kills.";
			}
			if (message.equalsIgnoreCase("kc galvek")) {
				message = "I currently have " + KillCounter.getKills("Galvek", player) + " Galvek kills.";
			}
			if (message.equalsIgnoreCase("kc jad")) {
				message = "I currently have " + KillCounter.getKills("Jad", player) + " Jad kills.";
			}
			if (message.equalsIgnoreCase("kc inferno")) {
				message = "I currently have " + KillCounter.getKills("Inferno", player) + " Inferno kills.";
			}
			if (message.equalsIgnoreCase("kc nex")) {
				message = "I currently have " + player.nexKills.getKills() + " Nex kills.";
			}
			if (message.equalsIgnoreCase("kc cox")) {
				message = "I have completed " + player.chambersofXericKills.getKills() + " Chambers of Xeric raids.";
			}
			if (message.equalsIgnoreCase("kc tob")) {
				message = "I have completed " + player.theatreOfBloodKills.getKills() + " Theatre of Blood raids.";
			}
			if (message.equalsIgnoreCase("kc toa")) {
				message = "I have completed " + player.tombsOfAmascutKills.getKills() + " Tombs of Amascut raids.";
			}
			if (message.equalsIgnoreCase("kc doe")) {
				message = "I have completed " + player.dominionOfEchoesKills.getKills() + " Dominion of Echoes raids.";
			}
			if (message.equalsIgnoreCase("kc kq")) {
				message = "I currently have " + KillCounter.getKills("Kalphite Queen", player) + " Kalphite Queen kills.";
			}
			if (message.equalsIgnoreCase("kc chaos ele")) {
				message = "I currently have " + KillCounter.getKills("Chaos Elemental", player) + " Chaos Elemental kills.";
			}
			if (message.equalsIgnoreCase("kc chaos fan")) {
				message = "I currently have " + KillCounter.getKills("Chaos Fanatic", player) + " Chaos Fanatic kills.";
			}
			if (message.equalsIgnoreCase("kc crazy arch")) {
				message = "I currently have " + KillCounter.getKills("Crazy Archaeologist", player)
						+ " Crazy Archaeologist kills.";
			}
			if (message.equalsIgnoreCase("kc scorpia")) {
				message = "I currently have " + KillCounter.getKills("Scorpia", player) + " Scorpia kills.";
			}
			if (message.equalsIgnoreCase("kc venenatis")) {
				message = "I currently have " + KillCounter.getKills("Venenatis", player) + " Venenatis kills.";
			}
			if (message.equalsIgnoreCase("kc vetion")) {
				message = "I currently have " + KillCounter.getKills("Vet'ion", player) + " Vet'ion kills.";
			}
			if (message.equalsIgnoreCase("kc callisto")) {
				message = "I currently have " + KillCounter.getKills("Callisto", player) + " Callisto kills.";
			}
			if (message.equalsIgnoreCase("kc dagannoth rex")) {
				message = "I currently have " + KillCounter.getKills("Dagannoth Rex", player) + " Dagannoth Rex kills.";
			}
			if (message.equalsIgnoreCase("kc dagannoth supreme")) {
				message = "I currently have " + KillCounter.getKills("Dagannoth Supreme", player) + " Dagannoth Supreme kills.";
			}
			if (message.equalsIgnoreCase("kc dagannoth prime")) {
				message = "I currently have " + KillCounter.getKills("Dagannoth Prime", player) + " Dagannoth Prime kills.";
			}
			if (message.equalsIgnoreCase("kc barrows")) {
				message = "I currently have " + KillCounter.getKills("Barrows", player) + " Barrows kills.";
			}
			if (message.equalsIgnoreCase("kc mole")) {
				message = "I currently have " + KillCounter.getKills("Giant Mole", player) + " Giant Mole kills.";
			}
			if (message.equalsIgnoreCase("kc ophidia")) {
				message = "I currently have " + KillCounter.getKills("Ophidia", player) + " Ophidia kills.";
			}
			if (message.equalsIgnoreCase("kc skotizo")) {
				message = "I currently have " + KillCounter.getKills("Skotizo", player) + " Skotizo kills.";
			}
			if (message.equalsIgnoreCase("kc kbd")) {
				message = "I currently have " + player.kingBlackDragonKills.getKills() + " King Black Dragon kills.";
			}
			if (message.equalsIgnoreCase("kc sarachnis")) {
				message = "I currently have " + player.sarachnisKills.getKills() + " Sarachnis kills.";
			}
			if (message.equalsIgnoreCase("kc nightmare")) {
				message = "I currently have " + player.nightmareKills.getKills() + " Nightmare kills.";
			}
			if (message.equalsIgnoreCase("kc sol heredit")) {
				message = "I currently have " + player.solHereditKills.getKills() + " Sol Heredit kills.";
			}
			if (message.equalsIgnoreCase("kc argentavis")) {
				message = "I currently have " + KillCounter.getKills("Argentavis", player) + " Argentavis kills.";
			}
			if (message.equalsIgnoreCase("kc demonic gorilla")) {
				message = "I currently have " + player.demonicGorillaKills.getKills() + " Demonic gorilla kills.";
			}
			Loggers.logClanChat(player.getUserId(), player.getName(), player.getIp(), message);
			return;
		}

		if (message.length() >= 3) {
			char c1 = message.charAt(0);
			char c2 = message.charAt(1);
			if ((c1 == ':' && c2 == ':') || (c1 == ';' && c2 == ';')) {
				CommandHandler.handle(player, message.substring(2));
				return;
			}
			if (c1 == '!' && player.isAdmin()) {
				player.forceText(message.substring(1));
				return;
			}
		}

		if (message.equalsIgnoreCase("kc bandos")) {
			message = "I currently have " + KillCounter.getKills("General Graardor", player) + " General Graardor kills.";
		}
		if (message.equalsIgnoreCase("kc zammy")) {
			message = "I currently have " + KillCounter.getKills("K'ril Tsutsaroth", player) + " K'ril Tsutsaroth kills.";
		}
		if (message.equalsIgnoreCase("kc sara")) {
			message = "I currently have " + KillCounter.getKills("Commander Zilyana", player) + " Commander Zilyana kills.";
		}
		if (message.equalsIgnoreCase("kc arma")) {
			message = "I currently have " + KillCounter.getKills("Kree'arra", player) + " Kree'arra kills.";
		}
		if (message.equalsIgnoreCase("kc araxxor")) {
			message = "I currently have " + player.araxxorKills.getKills() + " Araxxor kills.";
		}
		if (message.equalsIgnoreCase("kc yama")) {
			message = "I currently have " + player.yamaKills.getKills() + " Yama kills.";
		}
		if (message.equalsIgnoreCase("kc tormented demon")) {
			message = "I currently have " + player.tormentedDemonKills.getKills() + " Tormented Demon kills.";
		}
		if (message.equalsIgnoreCase("kc corp")) {
			message = "I currently have " + KillCounter.getKills("Corporeal Beast", player) + " Corporeal Beast kills.";
		}
		if (message.equalsIgnoreCase("kc cerb")) {
			message = "I currently have " + KillCounter.getKills("Cerberus", player) + " Cerberus kills.";
		}
		if (message.equalsIgnoreCase("kc thermy")) {
			message = "I currently have " + player.thermonuclearSmokeDevilKills.getKills()
					+ " Thermonuclear Smoke Devil kills.";
		}
		if (message.equalsIgnoreCase("kc sire")) {
			message = "I currently have " + KillCounter.getKills("Abyssal Sire", player) + " Abyssal Sire kills.";
		}
		if (message.equalsIgnoreCase("kc hydra")) {
			message = "I currently have " + player.alchemicalHydraKills.getKills() + " Alchemical Hydra kills.";
		}
		if (message.equalsIgnoreCase("kc vorkath")) {
			message = "I currently have " + KillCounter.getKills("Vorkath", player) + " Vorkath kills.";
		}
		if (message.equalsIgnoreCase("kc zulrah")) {
			message = "I currently have " + KillCounter.getKills("Zulrah", player) + " Zulrah kills.";
		}
		if (message.equalsIgnoreCase("kc kraken")) {
			message = "I currently have " + KillCounter.getKills("Kraken", player) + " Kraken kills.";
		}
		if (message.equalsIgnoreCase("kc leviathan")) {
			message = "I currently have " + player.leviathanKills.getKills() + " Leviathan kills.";
		}
		if (message.equalsIgnoreCase("kc whisperer")) {
			message = "I currently have " + player.whispererKills.getKills() + " The Whisperer kills.";
		}
		if (message.equalsIgnoreCase("kc duke")) {
			message = "I currently have " + player.dukeKills.getKills() + " Duke Sucellus kills.";
		}
		if (message.equalsIgnoreCase("kc vardorvis")) {
			message = "I currently have " + player.vardorvisKills.getKills() + " Vardorvis kills.";
		}
		if (message.equalsIgnoreCase("kc phantom muspah")) {
			message = "I currently have " + player.phantomMuspahKills.getKills() + " Phantom Muspah kills.";
		}
		if (message.equalsIgnoreCase("kc galvek")) {
			message = "I currently have " + KillCounter.getKills("Galvek", player) + " Galvek kills.";
		}
		if (message.equalsIgnoreCase("kc jad")) {
			message = "I currently have " + KillCounter.getKills("Jad", player) + " Jad kills.";
		}
		if (message.equalsIgnoreCase("kc inferno")) {
			message = "I currently have " + KillCounter.getKills("Inferno", player) + " Inferno kills.";
		}
		if (message.equalsIgnoreCase("kc nex")) {
			message = "I currently have " + player.nexKills.getKills() + " Nex kills.";
		}
		if (message.equalsIgnoreCase("kc cox")) {
			message = "I have completed " + player.chambersofXericKills.getKills() + " Chambers of Xeric raids.";
		}
		if (message.equalsIgnoreCase("kc tob")) {
			message = "I have completed " + player.theatreOfBloodKills.getKills() + " Theatre of Blood raids.";
		}
		if (message.equalsIgnoreCase("kc toa")) {
			message = "I have completed " + player.tombsOfAmascutKills.getKills() + " Tombs of Amascut raids.";
		}
		if (message.equalsIgnoreCase("kc doe")) {
			message = "I have completed " + player.dominionOfEchoesKills.getKills() + " Dominion of Echoes raids.";
		}
		if (message.equalsIgnoreCase("kc kq")) {
			message = "I currently have " + KillCounter.getKills("Kalphite Queen", player) + " Kalphite Queen kills.";
		}
		if (message.equalsIgnoreCase("kc chaos ele")) {
			message = "I currently have " + KillCounter.getKills("Chaos Elemental", player) + " Chaos Elemental kills.";
		}
		if (message.equalsIgnoreCase("kc chaos fan")) {
			message = "I currently have " + KillCounter.getKills("Chaos Fanatic", player) + " Chaos Fanatic kills.";
		}
		if (message.equalsIgnoreCase("kc crazy arch")) {
			message = "I currently have " + KillCounter.getKills("Crazy Archaeologist", player)
					+ " Crazy Archaeologist kills.";
		}
		if (message.equalsIgnoreCase("kc scorpia")) {
			message = "I currently have " + KillCounter.getKills("Scorpia", player) + " Scorpia kills.";
		}
		if (message.equalsIgnoreCase("kc venenatis")) {
			message = "I currently have " + KillCounter.getKills("Venenatis", player) + " Venenatis kills.";
		}
		if (message.equalsIgnoreCase("kc vetion")) {
			message = "I currently have " + KillCounter.getKills("Vet'ion", player) + " Vet'ion kills.";
		}
		if (message.equalsIgnoreCase("kc callisto")) {
			message = "I currently have " + KillCounter.getKills("Callisto", player) + " Callisto kills.";
		}
		if (message.equalsIgnoreCase("kc dagannoth rex")) {
			message = "I currently have " + KillCounter.getKills("Dagannoth Rex", player) + " Dagannoth Rex kills.";
		}
		if (message.equalsIgnoreCase("kc dagannoth supreme")) {
			message = "I currently have " + KillCounter.getKills("Dagannoth Supreme", player) + " Dagannoth Supreme kills.";
		}
		if (message.equalsIgnoreCase("kc dagannoth prime")) {
			message = "I currently have " + KillCounter.getKills("Dagannoth Prime", player) + " Dagannoth Prime kills.";
		}
		if (message.equalsIgnoreCase("kc barrows")) {
			message = "I currently have " + KillCounter.getKills("Barrows", player) + " Barrows kills.";
		}
		if (message.equalsIgnoreCase("kc mole")) {
			message = "I currently have " + KillCounter.getKills("Giant Mole", player) + " Giant Mole kills.";
		}
		if (message.equalsIgnoreCase("kc ophidia")) {
			message = "I currently have " + KillCounter.getKills("Ophidia", player) + " Ophidia kills.";
		}
		if (message.equalsIgnoreCase("kc skotizo")) {
			message = "I currently have " + KillCounter.getKills("Skotizo", player) + " Skotizo kills.";
		}
		if (message.equalsIgnoreCase("kc kbd")) {
			message = "I currently have " + player.kingBlackDragonKills.getKills() + " King Black Dragon kills.";
		}
		if (message.equalsIgnoreCase("kc sarachnis")) {
			message = "I currently have " + player.sarachnisKills.getKills() + " Sarachnis kills.";
		}
		if (message.equalsIgnoreCase("kc nightmare")) {
			message = "I currently have " + player.nightmareKills.getKills() + " Nightmare kills.";
		}
		if (message.equalsIgnoreCase("kc sol heredit")) {
			message = "I currently have " + player.solHereditKills.getKills() + " Sol Heredit kills.";
		}
		if (message.equalsIgnoreCase("kc argentavis")) {
			message = "I currently have " + KillCounter.getKills("Argentavis", player) + " Argentavis kills.";
		}
		if (message.equalsIgnoreCase("kc demonic gorilla")) {
			message = "I currently have " + player.demonicGorillaKills.getKills() + " Demonic gorilla kills.";
		}

		player.getChatUpdate().set(
				shadow, effects,
				player.getMessagingRank(),
				player.getTitleEnumId(),
				type,
				message);
	}

}

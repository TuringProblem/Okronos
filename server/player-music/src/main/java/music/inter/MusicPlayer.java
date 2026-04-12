package music.inter;

import io.ruin.api.utils.StringUtils;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.var.VarPlayerRepository;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import core.task.Continuation;
import core.task.Continuations;

import static io.ruin.model.inter.InterfaceEventMask.*;

public class MusicPlayer {

	private static List<Track> tracks = new ArrayList<>();

	public static void register() {

		InterfaceHandler.register(Interface.MUSIC_PLAYER, h -> {
			h.openAction = (player) -> {
				player.getPacketSender().sendIfEvents(239, 6, 0, 794, getMask(ClickOp1, ClickOp2));
			};

			h.actions[10] = (DefaultAction) (p, option, slot, item) -> {
				VarPlayerRepository.MUSIC_PREFERENCE.set(p, 1);
				p.sendMessage("You are now using the area music mode");
			};

			h.actions[13] = (DefaultAction) (p, option, slot, item) -> {
				VarPlayerRepository.MUSIC_PREFERENCE.set(p, 0);
				p.sendMessage("You are now using the manual music mode");
			};

			h.actions[16] = (DefaultAction) (p, option, slot, item) -> {
				VarPlayerRepository.MUSIC_PREFERENCE.set(p, 2);
				p.sendMessage("You are now using the random music mode");
			};

			h.actions[6] = (DefaultAction) (p, option, slot, item) -> {
				var track = tracks.get(slot);
				if (track == null) {
					return;
				}

				switch (option) {
					case 1 -> {
						track.play(p);
					}
					case 2 -> {
						track.unlock(p);
					}
				}
			};
		});
	}

	public static void registerTracks(List<Track> tracks) {
		MusicPlayer.tracks.addAll(tracks);
	}

	public static List<Track> tracks() {
		return tracks;
	}

	@Data
	public static final class Track {

		public final String name;
		public final String nameOrder;
		public final int varpIndex;
		public final int varpBitIndex;
		public final int midiId;

		// if (data != null) {
		// Object d = data[1];
		// // if(d instanceof Integer)
		// // archiveId = (int) d;
		//
		// if (data.length > 2) {
		// List<Integer> regionIds = new ArrayList<>();
		// for (int i = 2; i < data.length; i++)
		// regionIds.add((int) data[i]);
		// MapListener.registerRegions(regionIds)
		// .onEnter(this::autoPlay);
		// }
		// return;
		// }

		public void autoPlay(Player player) {
			// if (varpPos < 1) {
			// Server.logError("Error playing track for region " +
			// player.getPosition().regionId() + ". (varpPos=" + varpPos
			// + ", varpShift=" + varpShift + ")");
			// return;
			// }
			//
			// try {
			// VarPlayerRepository unlock = VarPlayerRepository.MUSIC_UNLOCKS[varpPos - 1];
			// int value = unlock.get(player);
			// if ((unlock.get(player) & (1 << varpShift)) == 0) {
			// /* unlock track */
			// unlock.set(player, value | (1 << varpShift));
			// if (!name.equals("Spirit")) {
			// player.sendFilteredMessage("<col=ff0000>You have unlocked a new music track:
			// " + name);
			// }
			// }
			// // if(Config.MUSIC_PREFERENCE.get(player) == 1) {
			// /* auto-play track */
			// player.getPacketSender().sendString(Interface.MUSIC_PLAYER, 6, name);
			// player.getPacketSender().sendMusic(archiveId);
			// // }
			// } catch (Throwable t) {
			// Server.logError("Error playing music track for region " +
			// player.getPosition().regionId() + " (varp pos:"
			// + varpPos + "): " + t.getMessage()); // todo remove after find bug
			// }
		}

		public void unlock(Player player) {
			if (alwaysUnlocked()) {
				return;
			}
			var config = VarPlayerRepository.MUSIC_UNLOCKS[this.varpIndex - 1];
			var value = config.get(player);
			value |= (1 << this.varpBitIndex);
			config.set(player, value);
		}

		private void play(Player player) {
			if (!isUnlocked(player)) {
				player.sendMessage("You have not unlocked this music track yet.");
				return;
			}

			VarPlayerRepository.MUSIC_PREFERENCE.set(player, 0);
			player.getPacketSender().sendString(Interface.MUSIC_PLAYER, 9, name);
			player.getPacketSender().sendMusic(this.midiId);
			player.sendMessage("Now playing " + this.name);
		}

		public boolean isUnlocked(Player player) {
			if (this.alwaysUnlocked()) {
				return true;
			}
			var config = VarPlayerRepository.MUSIC_UNLOCKS[this.varpIndex - 1];
			var value = config.get(player);
			if ((value & (1 << this.varpBitIndex)) == 0) {
				return false;
			}
			return true;
		}

		private boolean alwaysUnlocked() {
			return this.varpBitIndex == -1 && this.varpIndex == -1;
		}

	}

}

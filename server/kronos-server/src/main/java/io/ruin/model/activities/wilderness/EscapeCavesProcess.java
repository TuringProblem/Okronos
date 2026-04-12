package io.ruin.model.activities.wilderness;

import java.util.HashSet;

import core.task.Continuation;
import core.task.Continuations;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import lombok.AllArgsConstructor;
import lombok.experimental.ExtensionMethod;

import static core.task.api.API.*;

// Note, player context process, exits when logs out
@ExtensionMethod(EscapeCaves.Attributes.class)
@AllArgsConstructor
public class EscapeCavesProcess implements Continuation.Void {
	private final Player player;

	@Override
	public void call() {
		var disabledSet = new HashSet<Position>();
		while (true) {
			var pRegion = player.getPosition().region();
			var cavePlayer = player.cave();

			// re-enable all entrances after 10 seconds
			var elapsed = System.currentTimeMillis() - cavePlayer.lastCaveDisabledMs;
			if (elapsed > 20_000) {
				cavePlayer.disabledEntrances.clear();
			}

			for (var entrance : EscapeCaves.ENTRANCES) {
				var entrancePos = entrance.entranceObj.pos();
				if (entrancePos.region() != pRegion) {
					continue;
				}

				var disabled = cavePlayer.disabledEntrances.contains(entrancePos);
				var wasDisabled = disabledSet.contains(entrancePos);
				if (disabled && !wasDisabled) {
					entrance.entranceObj.sendRemove(player);
					disabledSet.add(entrance.entranceObj.pos());
					player.sendMessage("One of the escape caves collapsed.");
				} else if (!disabled && wasDisabled) {
					entrance.entranceObj.sendCreate(player);
					disabledSet.remove(entrancePos);
					player.sendMessage("One of the escape caves has been repaired.");
				}
			}

			sleep(1);
		}
	}

	public static void start(Player player) {
		queue(() -> {
			fork(new EscapeCavesProcess(player));
			// exit when player logs out
			while (true) {
				if (!player.isOnline()) {
					return;
				}
				sleep(1);
			}
		});
	}
}

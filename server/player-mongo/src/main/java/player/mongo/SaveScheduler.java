package player.mongo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import io.ruin.Server;
import io.ruin.model.entity.player.Player;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

// Schedules player saves to the mongo db
@Slf4j
public class SaveScheduler {

	private static final Scheduler scheduler = Schedulers.fromExecutor(Server.asyncWorker);
	private static final Set<String> pendingSavesUuid = Collections.synchronizedSet(new HashSet<>());

	public static void queue(Player player) {
		if (pendingSavesUuid.contains(player.uuid())) {
			return;
		}

		try {
			pendingSavesUuid.add(player.uuid());
			var collection = Connection.collection();
			var replace =
					collection.replaceOne(Filters.eq("uuid", player.uuidReal()), player, new ReplaceOptions().upsert(true));
			var mono = Mono.from(replace)
					.doOnTerminate(() -> {
						pendingSavesUuid.remove(player.uuid());
					});
			mono.subscribeOn(scheduler).subscribe();
		} catch (Exception e) {
			log.error("Unable to save player", e);
		}
	}

	public static int pendingSaves() {
		return pendingSavesUuid.size();
	}
}

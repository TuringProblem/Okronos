package logging.sentry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.sentry.Hint;
import io.sentry.SentryEnvelope;
import io.sentry.transport.ITransport;
import io.sentry.transport.RateLimiter;

// when sentry used locally, we just save some data to files
public class SentryLocalTransport implements ITransport {

	@Override
	public void close() throws IOException {}

	@Override
	public void send(@NotNull SentryEnvelope envelope, @NotNull Hint hint) throws IOException {
		for (var item : envelope.getItems()) {
			try {
				if (!item.getHeader().getType().name().equalsIgnoreCase("transaction")) {
					return;
				}
				var data = item.getData();
				Files.write(Path.of("data/runtime/logs/transaction.json"), data, StandardOpenOption.CREATE,
						StandardOpenOption.TRUNCATE_EXISTING);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void flush(long timeoutMillis) {}

	@Override
	public @Nullable RateLimiter getRateLimiter() {
		return null;
	}

	@Override
	public void close(boolean isRestarting) throws IOException {}


}

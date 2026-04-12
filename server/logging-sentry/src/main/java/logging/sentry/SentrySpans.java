package logging.sentry;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.sentry.ISpan;
import io.sentry.Sentry;
import io.sentry.SentryLongDate;
import io.sentry.SpanOptions;
import io.sentry.SpanStatus;

public class SentrySpans {

	private static final ScopedValue<ISpan> CURRENT_SPAN = ScopedValue.newInstance();

	public static void customFinished(String name, long elapsednano, Map<String, Object> data) {
		var root = CURRENT_SPAN.get();
		var opt = new SpanOptions();
		var endNano = TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis());
		var startNano = TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis()) - elapsednano;
		opt.setStartTimestamp(new SentryLongDate(startNano));
		var span = root.startChild(name, name, opt);
		for (var entry : data.entrySet()) {
			span.setData(entry.getKey(), entry.getValue());
		}
		span.finish(SpanStatus.OK, new SentryLongDate(endNano));
	}

	public static void start(String name, Runnable runnable) {
		var root = CURRENT_SPAN.orElse(null);
		if (root != null) {
			startChild(root, name, runnable);
			return;
		}
		var tx = Sentry.startTransaction(name, name);
		ScopedValue.where(CURRENT_SPAN, tx).run(() -> {
			try {
				runnable.run();
			} catch (Exception e) {
				tx.setStatus(SpanStatus.INTERNAL_ERROR);
				tx.setThrowable(e);
				throw e;
			} finally {
				tx.finish();
			}
		});
	}

	private static void startChild(ISpan span, String name, Runnable runnable) {
		var tx = span.startChild(name, name);
		ScopedValue.where(CURRENT_SPAN, tx).run(() -> {
			try {
				runnable.run();
			} catch (Exception e) {
				tx.setStatus(SpanStatus.INTERNAL_ERROR);
				tx.setThrowable(e);
				throw e;
			} finally {
				tx.finish();
			}
		});
	}
}

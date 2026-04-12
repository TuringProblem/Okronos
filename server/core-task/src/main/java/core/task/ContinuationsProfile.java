package core.task;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ContinuationsProfile {
	public static record Entry(String origin, int executions, long time) {
	}

	public final List<Entry> entries;

	private ContinuationsProfile(List<Entry> entries) {
		this.entries = entries;
	}

	public static ContinuationsProfile compute(List<Executor<?>> executors) {
		var entries = new HashMap<String, int[]>();
		for (var cont : executors) {
			entries.compute(cont.origin, (_k, v) -> {
				if (v == null) {
					return new int[] {1, (int) cont.lastExecutionTimeNano};
				}
				v[0] += 1;
				v[1] += cont.lastExecutionTimeNano;
				return v;
			});
		}
		var sorted = entries.entrySet().stream()
				.sorted(Comparator.comparingInt(e -> e.getValue()[1]))
				.map(it -> {
					var count = it.getValue()[0];
					var execTime = it.getValue()[1];
					var entry = new Entry(it.getKey(), count, execTime);
					return entry;
				}).toList();
		return new ContinuationsProfile(sorted);
	}

}

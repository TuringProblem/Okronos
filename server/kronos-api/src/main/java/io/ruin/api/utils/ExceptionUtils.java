package io.ruin.api.utils;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Jak Shadowrs tardisfan121@gmail.com
 */
public class ExceptionUtils {

    /**
     * auto trim to discard common hardcoded specific elements from {@link #discardAfter(StackTraceElement)}
     * @param t
     * @return
     */
    public static void trim(Throwable t) {
        var trimmed = t.getStackTrace();
        for (int i = 0; i < trimmed.length; i++) {
            var st = trimmed[i];
            //System.out.printf("hello %s and %s%n", st.getClassName(), st.getMethodName());
            if (discardAfter(st)) {
                var temp = new StackTraceElement[i];
                System.arraycopy(trimmed, 0, temp, 0, i);
				//var before = new Throwable("before");
				//before.setStackTrace(trimmed);
                t.setStackTrace(temp);
				//System.out.printf("skipped %s/%s%n", trimmed.length - i, trimmed.length);
				//before.printStackTrace();
                break;
            }
        }
    }

    public static List<StackWalker.StackFrame> trimFrames(List<StackWalker.StackFrame> frames , Predicate<StackWalker.StackFrame> p, boolean discardAfter) {
        return trimFrames(frames, p, discardAfter, false);
    }

    public static List<StackWalker.StackFrame> trimFrames(List<StackWalker.StackFrame> frames , Predicate<StackWalker.StackFrame> p, boolean discardAfter, boolean ignoreMatched) {
        for (int i = 0; i < frames.size(); i++) {
            var st = frames.get(i);
            //System.out.printf("hello %s and %s%n", st.getClassName(), st.getMethodName());
            if (p.test(st)) {
                if (discardAfter) {
                    return frames.subList(0, ignoreMatched ? Math.max(0, Math.min(frames.size(), i-1)) : i);
                } else {
                    return frames.subList(ignoreMatched ? Math.max(0, Math.min(frames.size(), i + 1)) : i, frames.size());
                }
            }
        }
        return frames;
    }

    public static void trim(Throwable e, Predicate<StackTraceElement> p, boolean discardAfter) {
        var trimmed = e.getStackTrace();
        for (int i = 0; i < trimmed.length; i++) {
            var st = trimmed[i];
            //System.out.printf("hello %s and %s%n", st.getClassName(), st.getMethodName());
            if (p.test(st)) {
                if (discardAfter) {
                    var temp = new StackTraceElement[i];
                    System.arraycopy(trimmed, 0, temp, 0, i);
                    e.setStackTrace(temp);
                } else {
                    // discard before
                    var temp = new StackTraceElement[trimmed.length - i];
                    System.arraycopy(trimmed, i, temp, 0, trimmed.length - i);
                    e.setStackTrace(temp);
                }
                break;
            }
        }
    }

	private static final String WORKER = "io.ruin.process.CoreWorker";

    /**
     * yeet after npc.sequence, TaskManager.seq, PlayerSession.Packetss, Chain.Attempt
     * @param e
     * @return
     */
    public static boolean discardAfter(StackTraceElement e) {
        var skip = e.getClassName().equals(WORKER) && (e.getMethodName().equals("index0")
			|| e.getMethodName().equals("logic0")
			|| e.getMethodName().equals("update0"));
        return skip;
    }
}

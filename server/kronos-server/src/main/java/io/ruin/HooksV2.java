package io.ruin;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

public class HooksV2<M> {
	public enum Result {
		Pass, Break, Return
	}

	public static interface Hook<P> {
		Result handle(P ctx);
	}

	// silent handler (always pass)
	public static interface HookSilent<P> {
		void handle(P ctx);
	}

	// returning handler (always return)
	public static interface HookReturn<P> {
		boolean handle(P ctx);
	}

	@Data
	public static class Key {
		private final Class<?> cls;
		private final int index;
	}

	private final Map<Class<?>, Hook<?>[]> hooks = new HashMap<>();
	private final Class<M> root;

	public HooksV2(Class<M> cls) {
		this.root = cls;
	}

	public void clear() {
		this.hooks.clear();
	}

	public int size() {
		return hooks.size();
	}

	public <C extends M> Key registerReturn(Class<C> cls, HookReturn<C> hook) {
		return register(cls, (ctx) -> {
			if (hook.handle(ctx)) {
				return Result.Return;
			}
			return Result.Pass;
		});
	}

	public <C extends M> Key registerSilent(Class<C> cls, HookSilent<C> hook) {
		return register(cls, (ctx) -> {
			hook.handle(ctx);
			return Result.Pass;
		});
	}

	public void registerSilentAll(HookSilent<M> hook) {
		for (var _cls : this.root.getPermittedSubclasses()) {
			@SuppressWarnings("unchecked")
			var cls = (Class<? extends M>) _cls;
			register(cls, (ctx) -> {
				hook.handle(ctx);
				return Result.Pass;
			});
		}
	}

	public <C extends M> Key register(Class<C> cls, Hook<C> hook) {
		var slot = this.hooks.computeIfAbsent(cls, (key) -> new Hook<?>[100]);
		var emptyIndex = -1;
		for (int i = 0; i < slot.length; i++) {
			if (slot[i] == null) {
				emptyIndex = i;
				break;
			}
		}
		slot[emptyIndex] = hook;

		return new Key(cls, emptyIndex);
	}

	public void unregister(Key key) {
		var slots = this.hooks.get(key.cls);
		if (slots != null) {
			slots[key.index] = null;
		}
	}

	public boolean handle(M context) {
		var contextCls = context.getClass();
		var slot = this.hooks.get(contextCls);
		if (slot == null) {
			return false;
		}

		for (var hookRaw : slot) {
			@SuppressWarnings("unchecked")
			var hook = (Hook<M>) hookRaw;
			if (hookRaw == null) {
				continue;
			}
			switch (hook.handle(context)) {
				case Break -> {
					return false;
				}
				case Return -> {
					return true;
				}
				case Pass -> {
				}
			}
		}

		return false;
	}
}

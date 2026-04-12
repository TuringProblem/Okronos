package io.ruin.model.entity.shared.listeners;

import io.ruin.model.World;
import io.ruin.model.entity.npc.NPC;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface SpawnListener {

	void handle(NPC npc);

	static void register(int npcId, SpawnListener action) {
		forEach(npc -> {
			if (npc.getId() == npcId)
				action.handle(npc);
		});
	}

	static void register(int[] npcIds, SpawnListener action) {
		forEach(npc -> {
			for (int id : npcIds) {
				if (npc.getId() == id)
					action.handle(npc);
			}
		});
	}

	static void register(String npcName, SpawnListener action) {
		forEach(npc -> {
			if (npc.getDef().name.equalsIgnoreCase(npcName))
				action.handle(npc);
		});
	}

	static void register(String[] npcNames, SpawnListener action) {
		forEach(npc -> {
			for (String name : npcNames) {
				if (npc.getDef().name.equalsIgnoreCase(name))
					action.handle(npc);
			}
		});
	}

	static void forEach(Consumer<NPC> npcConsumer) {
		for (Object o : World.npcsSlots()) {
			if (o == null) {
				continue;
			}
			npcConsumer.accept((NPC) o);
		}
	}

	static List<NPC> find(int npcId) {
		List<NPC> list = new ArrayList<>();
		for (Object o : World.npcsSlots()) {
			if (o != null) {
				final NPC npc = (NPC) o;
				if (npc.getId() == npcId)
					list.add(npc);
			}
		}
		return list;
	}

	static NPC first(int npcId) {
		return find(npcId).getFirst();
	}

}

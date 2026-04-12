package io.ruin.cache.iftype;

import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;

public final class Interface {

	public boolean entity;
	public Component[] components;
	public Component[] ordered_components;

	public Interface(Component[] components) {
		this.components = components;
		this.entity = entity;
	}

	public Component get_component(int component_id) {
		if (component_id >>> 16 != components[0].id >>> 16) {
			throw new IllegalArgumentException();
		}
		return components[component_id & 0xffff];
	}

	public Component[] get_components() {
		if (ordered_components == null) {
			return components;
		}
		return ordered_components;
	}

	public Component[] get_ordered_components() {
		if (ordered_components == null) {
			int count = components.length;
			ordered_components = new Component[count];
			System.arraycopy(components, 0, ordered_components, 0, components.length);
		}
		return ordered_components;
	}

	public static Interface getInterface(int id) {
		IndexFile index = Server.fileStore.get(3);
		int num_components = index.getLastFileId(id);
		Component[] components;
		if (num_components == 0) {
			components = new Component[0];
		} else {
			components = new Component[num_components];
		}
		Interface inter = new Interface(components);

		for (int component_id = 0; component_id < num_components; component_id++) {
			if (inter.components[component_id] != null) {
				continue;
			}
			byte[] data = index.getFile(id, component_id);
			if (data != null) {
				Component component = inter.components[component_id] = new Component();
				component.id = (id << 16) + component_id;
				if (data[0] == -1) {
					component.decode(new InBuffer(data));
				} else {
					component.decodeLegacy(new InBuffer(data));
				}
			}
		}
		return inter;
	}
}

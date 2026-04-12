package io.ruin.model.entity.player.presets;

import com.google.gson.*;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.skills.magic.SpellBook;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.model.stat.StatType;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PresetDeserializer implements JsonDeserializer<Preset> {

	@Override
	public Preset deserialize(JsonElement element, Type t, JsonDeserializationContext context) throws JsonParseException {

		try {

			JsonObject object = element.getAsJsonObject();

			/*
			 * Check if the JSON object has the variable name
			 */
			if (!object.has("name")) {
				Logger.getAnonymousLogger().log(Level.WARNING, "A pre-defined preset requires a name!");
				return null;
			}

			/*
			 * Create a new preset
			 */
			Preset preset = new Preset(object.get("name").getAsString(), PresetType.DEFAULT_PRESET);

			if (object.has("inventory")) {

				/*
				 * Get the JSON array of the inventory
				 */
				JsonArray inventory = object.get("inventory").getAsJsonArray();

				Item[][] container = null;

				/*
				 * Loop through the inventory JSON array
				 */
				for (int i = 0; i < inventory.size(); i++) {

					/*
					 * Get the next inventory entry
					 */
					JsonObject entry = inventory.get(i).getAsJsonObject();

					/*
					 * Check if the entry has item and amount defined
					 */
					if (!entry.has("item") || !entry.has("amount")) {
						continue;
					}

					if (!entry.get("item").isJsonArray()) {
						continue;
					}

					JsonArray items = entry.get("item").getAsJsonArray();
					int amount = entry.get("amount").getAsInt();

					if (container == null) {
						container = new Item[items.size()][28];
					}

					for (int k = 0; k < items.size(); k++) {

						/*
						 * Set the preset's inventory slot
						 */
						container[k][i] = new Item(items.get(k).getAsInt(), amount);

					}

				}

				if (container != null) {

					for (int i = 0; i < container.length; i++) {
						preset.getInventory().add(i, container[i]);
					}

				}

			}

			if (object.has("equipment")) {

				/*
				 * Get the JSON array of the equipment
				 */
				JsonArray equipment = object.get("equipment").getAsJsonArray();

				Item[][] container = null;

				/*
				 * Loop through the equipment JSON array
				 */
				loop:
				for (int i = 0; i < equipment.size(); i++) {

					/*
					 * Get the next equipment entry
					 */
					JsonObject entry = equipment.get(i).getAsJsonObject();

					/*
					 * Check if the entry has slot and item defined
					 */
					if (!entry.has("slot") || !entry.has("item")) {
						continue;
					}

					if (!entry.get("item").isJsonArray()) {
						continue;
					}

					JsonArray items = entry.get("item").getAsJsonArray();

					if (container == null) {
						container = new Item[items.size()][28];
					}

					String slot = entry.get("slot").getAsString();
					int slot_id = -1;
					switch (slot) {
						case "hat":
							slot_id = Equipment.SLOT_HAT;
							break;
						case "cape":
							slot_id = Equipment.SLOT_CAPE;
							break;
						case "amulet":
							slot_id = Equipment.SLOT_AMULET;
							break;
						case "weapon":
							slot_id = Equipment.SLOT_WEAPON;
							break;
						case "body":
							slot_id = Equipment.SLOT_CHEST;
							break;
						case "shield":
							slot_id = Equipment.SLOT_SHIELD;
							break;
						case "legs":
							slot_id = Equipment.SLOT_LEGS;
							break;
						case "gloves":
							slot_id = Equipment.SLOT_HANDS;
							break;
						case "boots":
							slot_id = Equipment.SLOT_FEET;
							break;
						case "ring":
							slot_id = Equipment.SLOT_RING;
							break;
						case "arrow":
							slot_id = Equipment.SLOT_AMMO;
							break;
					}


					if (slot_id > -1) {
						int amount = entry.has("amount") ? entry.get("amount").getAsInt() : 1;

						for (int k = 0; k < items.size(); k++) {

							/*
							 * Set the preset's equipment slot
							 */
							container[k][slot_id] = new Item(items.get(k).getAsInt(), amount);

						}
					}
					Logger.getAnonymousLogger().log(Level.WARNING, "The equipment of " + preset.getName() + " has an invalid slot: " + slot);

				}

				if (container != null) {

					for (int i = 0; i < container.length; i++) {
						preset.getEquipment().add(i, container[i]);
					}

				}

			}

			if (object.has("levels")) {

				/*
				 * Get the JSON array of the skills
				 */
				JsonArray skills = object.get("levels").getAsJsonArray();

				/*
				 * Loop through the skills JSON array
				 */
				loop:
				for (int i = 0; i < skills.size(); i++) {

					/*
					 * Get the next skills entry
					 */
					JsonObject entry = skills.get(i).getAsJsonObject();

					/*
					 * Check if the entry has skill and level defined
					 */
					if (!entry.has("skill") || !entry.has("level")) {
						continue;
					}

					String skill = entry.get("skill").getAsString();

					/*
					 * Loop through each equipment type
					 */
					for (int k = 0; k < StatType.VALUES.length; k++) {

						if (!StatType.VALUES[k].name().equalsIgnoreCase(skill)) {
							continue;
						}

						/*
						 * Set the preset's combat skill level
						 */
						preset.getLevels()[k] = entry.get("level").getAsInt();

						/*
						 * Continue with the next equipment entry
						 */
						continue loop;

					}

					Logger.getAnonymousLogger().log(Level.WARNING, "The levels of " + preset.getName() + " has an invalid name: " + skill);

				}

			}

			if (object.has("quickprayers")) {

				/*
				 * Get the JSON array of the quick prayers
				 */
				JsonArray prayers = object.get("quickprayers").getAsJsonArray();

				/*
				 * Loop through the prayers JSON array
				 */
				loop:
				for (int i = 0; i < prayers.size(); i++) {

					String name = prayers.get(i).getAsString();

					/*
					 * Loop through each prayer type
					 */
					for (Prayer prayer : Prayer.VALUES) {

						if (!prayer.name().equalsIgnoreCase(name)) {
							continue;
						}

						/*
						 * Set the preset's quick prayer state
						 */
						preset.getQuickPrayers()[prayer.ordinal()] = true;

						/*
						 * Continue with the next quick prayer entry
						 */
						continue loop;

					}

					Logger.getAnonymousLogger().log(Level.WARNING, "The quick prayers of " + preset.getName() + " has an invalid name: " + name);

				}

			}

			if (object.has("spellbook")) {

				String name = object.get("spellbook").getAsString().toUpperCase();
				Optional<SpellBook> book = Arrays.stream(SpellBook.VALUES).filter((b) -> b.name().startsWith(name)).findFirst();

				if (book.isPresent()) {
					preset.setSpellBook(book.get().ordinal());
				} else {
					Logger.getAnonymousLogger().log(Level.WARNING, "The spell book of " + preset.getName() + " has an invalid name: " + name);
				}
			}

			return preset;

		} catch (JsonParseException e) {
			e.printStackTrace();
		}

		return null;

	}

}

package io.ruin.model.entity.player.presets;

import io.ruin.model.item.Item;
import io.ruin.model.skills.magic.SpellBook;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Telopya telopya@frostblades.org
 * @version 16th of January 2017
 */
public class Preset {

	/**
	 * The name of the {@link Preset}
	 */
	private String name;

	/**
	 * The {@link PresetType} of the {@link Preset}
	 */
	private PresetType type;

	/**
	 * The inventory of the {@link Preset}
	 */
	private List<Item[]> inventory = new ArrayList<Item[]>();

	/**
	 * The equipment of the {@link Preset}
	 */
	private List<Item[]> equipment = new ArrayList<Item[]>();

	/**
	 * The combat skill levels of the {@link Preset}
	 */
	private int[] levels = new int[]{1, 1, 1, 10, 1, 1, 1};

	/**
	 * The spell book of the {@link Preset}
	 */
	private int book = SpellBook.MODERN.ordinal();

	/**
	 * The quick prayers of the {@link Preset}
	 */
	private boolean[] prayers = new boolean[26];

	public Preset(String name, PresetType type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Returns the name of the {@link Preset}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the {@link PresetType} of the {@link Preset}
	 */
	public PresetType getType() {
		return this.type;
	}

	/**
	 * Returns the inventory of the {@link Preset}
	 */
	public List<Item[]> getInventory() {
		return this.inventory;
	}

	/**
	 * Returns the equipment of the {@link Preset}
	 */
	public List<Item[]> getEquipment() {
		return this.equipment;
	}

	/**
	 * Returns the combat skill levels of the {@link Preset}
	 */
	public int[] getLevels() {
		return this.levels;
	}

	public void setLevels(int[] levels) {
		this.levels = levels;
	}

	/**
	 * Returns the spell book of the {@link Preset}
	 */
	public int getSpellBook() {
		return this.book;
	}

	/**
	 * Sets the spell book of the {@link Preset}
	 *
	 * @param book the spell book
	 */
	public void setSpellBook(int book) {
		this.book = book;
	}

	/**
	 * Returns the quick prayer states of the {@link Preset}
	 */
	public boolean[] getQuickPrayers() {
		return this.prayers;
	}

	public void setName(String pass) {
		this.name = pass;
	}
}

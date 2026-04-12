package io.ruin.model.entity.player.presets;

/**
 * @author Telopya <telopya@gmail.com>
 * @version 16th of January 2017
 */
public class PresetManager {
	/*
	 * private static final int HIGHLIGHT_SPELLBOOK_CLIENTSCRIPT = 10904;
	 * private static final int BUILD_EQUIPMENT_AND_INVENTORY_CONTAINER_CLIENTSCRIPT
	 * = 10901;
	 * private static final int SELECT_PRESET_CLIENTSCRIPT = 10909;
	 * private static final int HIDE_SPELLBOOK_BUTTONS_CLIENTSCRIPT = 10902;
	 * private static final int BUILD_PRESET_INTERFACE_BASE_CLIENTSCRIPT = 10900;
	 * private static final int REFRESH_PRESET_LIST_CLIENTSCRIPT = 10906;
	 * private static final int SELECT_SPELLBOOK_SOUND_EFFECT = 2266;
	 * private static final int MAX_WORD_LENGTH = 14;
	 * private static final int CAPACITY_TOOLTIP_SCRIPT = 1495;
	 * private static final String NAME_FILTER_REGEX =
	 * "[^a-zA-Z0-9$#€£()+\\-/*!,.'_ \\[\\]@{}]";
	 * private static final String[] equipmentSlotLabels = {
	 * "Helm", "Cape", "Amulet", "Body", "Shield",
	 * "Legs", "Hands", "Feet", "Ring", "Ammunition"
	 * };
	 *
	 * private static final Map<Integer, String> componentMap = new HashMap<>();
	 *
	 * public static void register() {
	 *
	 * componentMap.put(8, "Preset");
	 * for (int index = 0; index < equipmentSlotLabels.length; index++) {
	 * componentMap.put(17 + index, equipmentSlotLabels[index]);
	 * }
	 * componentMap.put(28, "Inventory");
	 * componentMap.put(33, "Normal Spellbook");
	 * componentMap.put(34, "Ancient Spellbook");
	 * componentMap.put(35, "Lunar Spellbook");
	 * componentMap.put(36, "Arceuus Spellbook");
	 * componentMap.put(39, "Create Preset");
	 * componentMap.put(41, "Apply");
	 * componentMap.put(43, "Rename");
	 * componentMap.put(45, "Delete");
	 * componentMap.put(47, "Always set placeholders");
	 * componentMap.put(49, "Bank");
	 * componentMap.put(50, "Move preset up");
	 * componentMap.put(51, "Move preset down");
	 * componentMap.put(52, "Preset count");
	 * componentMap.put(55, "Preset cap");
	 * componentMap.put(56, "Capacity tooltip");
	 *
	 * InterfaceHandler.register(711, h -> {
	 * h.actions[8] = (SlotAction) (p, s) -> {
	 * p.getPresetManager().preview(p, p.getPresetManager().presets.get(s), false);
	 *
	 * };
	 * h.actions[41] = (SimpleAction) player -> {
	 * player.closeInterfaces();
	 * player.getPresetManager().load(player, PresetType.CUSTOM_PRESET,
	 * player.getPresetManager().currentIndex);
	 * };
	 * h.actions[45] = (SimpleAction) player -> {
	 * player.dialogue(new OptionsDialogue(
	 * new Option("Delete " + player.getPresetManager().current.getName() +
	 * " preset.", () ->
	 * player.getPresetManager().delete(player.getPresetManager().currentIndex)),
	 * new Option("Cancel.", () -> {})
	 * ));
	 * };
	 * h.actions[39] = (SimpleAction) player -> {
	 * player.stringInput("Set a new name for the preset " +
	 * player.getPresetManager().current.getName() + ":", pass -> {
	 * if (pass.length() < 3 && pass.length() > 20) {
	 * player.
	 * sendMessage("The name of the preset has to be between 3 and 20 characters");
	 * return;
	 * }
	 * player.getPresetManager().current.setName(pass);
	 * player.getPresetManager().preview(player, player.getPresetManager().current);
	 * });
	 * };
	 * h.actions[39] = (SimpleAction) player -> {
	 * player.stringInput("Set the name of the preset:", pass -> {
	 * if (pass.length() < 3 && pass.length() > 20) {
	 * player.
	 * sendMessage("The name of the preset has to be between 3 and 20 characters");
	 * return;
	 * }
	 * player.getPresetManager().save(player, pass);
	 * return;
	 * });
	 * };
	 * });
	 * }
	 *
	 *
	 *
	 * private final Player player;
	 *
	 *
	 * public static final List<Preset> DEFAULT_PRESETS = new ArrayList<>();
	 *
	 *
	 * @Expose private final List<Preset> presets = new ArrayList<>();
	 *
	 * private int currentIndex;
	 * private Preset current;
	 *
	 * public PresetManager(Player player) {
	 * this.player = player;
	 * }
	 *
	 *
	 * public void save(Player player, String name) {
	 * try {
	 *
	 * if (presets.stream().anyMatch(p -> p.getName().equalsIgnoreCase(name))) {
	 * player.dialogue(new
	 * MessageDialogue("You already have a preset with this name."));
	 * return;
	 * }
	 *
	 *
	 * Preset preset = new Preset(name, PresetType.CUSTOM_PRESET);
	 * ItemContainer allowed = new ItemContainer();
	 * allowed.init(30, false);
	 *
	 *
	 * Arrays.stream(player.getInventory().getItems()).forEach(item -> {
	 * boolean approve = false;
	 * if (item != null) {
	 * if (!item.getDef().stackable || ((item.getId() >= 554 && item.getId() <= 566)
	 * || item.getId() == 11669 || item.getId() == 9075) ||
	 * item.getDef().name.toLowerCase().contains("teleport")) {
	 * approve = true;
	 * }
	 * }
	 * if (approve) {
	 * allowed.add(new Item(item.getId(), item.getAmount()));
	 * } else {
	 * if (item != null)
	 * player.sendMessage("Penalty - " + (item == null ? "some[" + item.getId() +
	 * "]" : item.getDef().name) + " was not saved.");
	 * }
	 * });
	 *
	 * preset.getInventory().add(allowed.clone().getItems());
	 *
	 *
	 * preset.getEquipment().add(player.getEquipment().clone().getItems());
	 *
	 *
	 * for (int i = 0; i < preset.getLevels().length; i++) {
	 *
	 *
	 * preset.getLevels()[i] = player.getStats().get(i).fixedLevel;
	 *
	 * }
	 *
	 * //
	 * // for (int i = 0; i < preset.getQuickPrayers().length; i++) {
	 * //
	 * //
	 * // preset.getQuickPrayers()[i] = player.getCombatState().getQuickPrayer(i);
	 * //
	 * // }
	 * SpellBook book = null;
	 * for (SpellBook b : SpellBook.VALUES) {
	 * if (b.isActive(player)) {
	 * book = b;
	 * }
	 * }
	 *
	 *
	 * preset.setSpellBook(book.ordinal());
	 *
	 * this.current = preset;
	 * save(player, preset);
	 * preview(player, preset);
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * player.sendMessage("Unable to save current gear as preset: " + name);
	 * Logger.getAnonymousLogger().log(Level.WARNING,
	 * "Unable to save current gear as preset: " + name, e);
	 * }
	 *
	 * }
	 *
	 * public void save(Player player, Preset preset) {
	 * presets.add(preset);
	 * player.sendMessage("Your current setup has been saved as " +
	 * current.getName() + ".");
	 * // TabQuest.updateQuestTab(player);
	 * }
	 *
	 * public void preview(Player player, Preset preset) {
	 * preview(player, preset, true);
	 * }
	 *
	 * public void preview(Player player, Preset preset, boolean send) {
	 * current = preset;
	 * for (int i = 0; i < presets.size(); i++) {
	 * if (current == presets.get(i)) {
	 * currentIndex = i;
	 * }
	 * }
	 * if (send) {
	 * player.openInterface(ToplevelComponent.MAINMODAL, 711);
	 * }
	 * player.getPacketSender().sendClientScript(3281, "ii", 2520, 1);
	 * player.getPacketSender().sendClientScript(
	 * BUILD_PRESET_INTERFACE_BASE_CLIENTSCRIPT);
	 * refresh(player, Optional.of(preset), send);
	 * player.getPacketSender().sendIfEvents(711, getComponent("Inventory"), 0, 28,
	 * AccessMasks.ClickOp1, AccessMasks.DragDepth1, AccessMasks.DragTargetable);
	 * player.getPacketSender().sendIfEvents(711, getComponent("Preset"), 0, 20,
	 * AccessMasks.ClickOp1, AccessMasks.ClickOp2, AccessMasks.ClickOp3,
	 * AccessMasks.ClickOp4, AccessMasks.ClickOp5);
	 * if (send) {
	 * refreshPresetsList(player);
	 * refreshSize(player);
	 * }
	 * player.getPacketSender().sendClientScript(CAPACITY_TOOLTIP_SCRIPT, "sII",
	 * "Default slots: 2<br><br>" + "Purchasable slots:<br>" +
	 * "Non-member slots: 3<br>" + "Member slots: 8<br>" +
	 * "Additional member slots: 1 per rank", getComponent("Preset cap") | (711 <<
	 * 16), getComponent("Capacity tooltip") | (711 << 16));
	 * String[] SPELLBOOK_VALUES = { "Normal", "Ancient", "Lunar", "Arceuus" };
	 * int index = 0;
	 * for (String spellbook : SPELLBOOK_VALUES) {
	 * int componentHash = 711 << 16 | getComponent(spellbook + " Spellbook");
	 * player.getPacketSender().sendClientScript(HIGHLIGHT_SPELLBOOK_CLIENTSCRIPT,
	 * "iI", preset.getSpellBook() == index ? 1 : 0, componentHash);
	 * index++;
	 * }
	 * }
	 *
	 * private void refreshSize( final Player player) {
	 * player.getPacketSender().sendString(711, getComponent("Preset count"), "1");
	 * player.getPacketSender().sendString(711, getComponent("Preset cap"), "10");
	 * }
	 *
	 * private void refreshPresetsList(final Player player) {
	 * StringBuilder builder = new StringBuilder("");
	 * int size = presets.size();
	 * for (int i = 0; i < presets.size(); i++) {
	 * Preset preset = presets.get(i);
	 * builder.append(preset.getName()).append("|");
	 * }
	 * player.getPacketSender().sendClientScript(REFRESH_PRESET_LIST_CLIENTSCRIPT,
	 * "IIs", size, 10, builder.toString());
	 * }
	 *
	 * private int getComponent(String text) {
	 * for(Map.Entry<Integer, String> entry : componentMap.entrySet()) {
	 * if(entry.getValue().equals(text))
	 * return entry.getKey();
	 * }
	 * return 0;
	 * }
	 *
	 * private static void refresh(final Player player, final Optional<Preset>
	 * currentPreset, final boolean reselectPreset) {
	 * currentPreset.ifPresent(preset -> {
	 * Map<String, Integer> format = new HashMap<String, Integer>();
	 * ItemContainer container = new ItemContainer();
	 * container.init(28 + 14, false);
	 * final List<Item[]> inventoryMap = preset.getInventory();
	 * final List<Item[]> equipmentMap = preset.getEquipment();
	 * for (int i = 14 + 28 - 1; i >= 0; i--) {
	 * container.set(i, i < 14 ? equipmentMap.get(0)[i] : inventoryMap.get(0)[i -
	 * 14]);
	 * }
	 * player.getPacketSender().sendItems(-1, 207, container.getItems(), 28 + 14);
	 * player.getPacketSender().sendClientScript(
	 * BUILD_EQUIPMENT_AND_INVENTORY_CONTAINER_CLIENTSCRIPT);
	 * if (reselectPreset) {
	 * player.getPacketSender().sendClientScript(SELECT_PRESET_CLIENTSCRIPT,
	 * player.getPresetManager().currentIndex, 1, 1);
	 * }
	 * VarPlayerRepository.ACTIVE_SELECTED_PRESET_VARP.set(player, 0);
	 * VarPlayerRepository.ACTIVE_SELECTED_PRESET_VARP.forceSend();
	 * });
	 * }
	 *
	 * public void preview2(Player player, Preset preset) {
	 * player.openInterface(ToplevelComponent.MAINMODAL, 51);
	 * for (int component = 122; component < 140; component++) {
	 * player.getPacketSender().sendString(51, component, "");
	 * }
	 *
	 * Map<String, Integer> format = new HashMap<String, Integer>();
	 * ItemContainer container = new ItemContainer();
	 * container.init(28, true);
	 * for (Item[] items : preset.getInventory()) {
	 * for (Item item : items) {
	 * if (item == null)
	 * continue;
	 * container.add(item);
	 * format.put(item.getDef().name, format.get(item.getDef().name) == null ?
	 * item.getAmount()
	 * : format.get(item.getDef().name) + item.getAmount());
	 * }
	 * }
	 *
	 * int pointer = 0;
	 * Iterator<Entry<String, Integer>> iterator = format.entrySet().iterator();
	 * while (iterator.hasNext() && pointer < 18) {
	 * Entry<String, Integer> entry = (Entry<String, Integer>) iterator.next();
	 * if (entry.getKey() != null) {
	 * boolean sub = entry.getKey().length() > 20;
	 * player.getPacketSender().sendString(51, 123 + pointer,
	 * entry.getValue() + "x " + entry.getKey().substring(0, sub ? 11 :
	 * entry.getKey().length()) + (sub ? "." : ""));
	 * pointer++;
	 * }
	 * }
	 *
	 * player.openInterface(ToplevelComponent.MAINMODAL, 51);
	 * for (int i = 50; i < 63; i++) {
	 *
	 * }
	 * for (int i = 109; i < 115; i++) {
	 *
	 * }
	 * player.getPacketSender().sendString(51, 35, "Preset:");
	 * player.getPacketSender().sendString(51, 122, "<col=99ff33>-Inventory-");
	 * player.getPacketSender().sendString(51, 34, "<col=c31a0f>" +
	 * preset.getName());
	 * player.getPacketSender().sendString(51, 108,
	 * "Prepare your inventory & equip all the items for your loadout.");
	 * player.getPacketSender().sendString(51, 107,
	 * "Confirm your loadout by selecting the options below.");
	 * player.getPacketSender().sendString(51, 8, "" +
	 * preset.getLevels()[StatType.Attack.ordinal()]);
	 * player.getPacketSender().sendString(51, 9, "" +
	 * preset.getLevels()[StatType.Attack.ordinal()]);
	 * player.getPacketSender().sendString(51, 12, "" +
	 * preset.getLevels()[StatType.Strength.ordinal()]);
	 * player.getPacketSender().sendString(51, 13, "" +
	 * preset.getLevels()[StatType.Strength.ordinal()]);
	 * player.getPacketSender().sendString(51, 16, "" +
	 * preset.getLevels()[StatType.Defence.ordinal()]);
	 * player.getPacketSender().sendString(51, 17, "" +
	 * preset.getLevels()[StatType.Defence.ordinal()]);
	 * player.getPacketSender().sendString(51, 20, "" +
	 * preset.getLevels()[StatType.Hitpoints.ordinal()]);
	 * player.getPacketSender().sendString(51, 21, "" +
	 * preset.getLevels()[StatType.Hitpoints.ordinal()]);
	 * player.getPacketSender().sendString(51, 24, "" +
	 * preset.getLevels()[StatType.Prayer.ordinal()]);
	 * player.getPacketSender().sendString(51, 25, "" +
	 * preset.getLevels()[StatType.Prayer.ordinal()]);
	 * player.getPacketSender().sendString(51, 28, "" +
	 * preset.getLevels()[StatType.Ranged.ordinal()]);
	 * player.getPacketSender().sendString(51, 29, "" +
	 * preset.getLevels()[StatType.Ranged.ordinal()]);
	 * player.getPacketSender().sendString(51, 32, "" +
	 * preset.getLevels()[StatType.Magic.ordinal()]);
	 * player.getPacketSender().sendString(51, 33, "" +
	 * preset.getLevels()[StatType.Magic.ordinal()]);
	 * player.getPacketSender().sendString(51, 106, "Save");
	 * player.getPacketSender().sendString(51, 105, "Cancel");
	 * player.getPacketSender().sendString(51, 117, "");
	 *
	 * player.getPacketSender().setHidden(51, 107, true);
	 * int[] slots = { 92, 93, 94, 95, 96, 97, -1, 98, -1, 99, 100, -1, 101, 102 };
	 * for (int i = 0; i <= 13; i++) {
	 * if (slots[i] != -1) {
	 * player.getPacketSender().setHidden(51, slots[i] - 12,
	 * player.getEquipment().get(i) != null);
	 * }
	 * player.getPacketSender().sendItems(51, slots[i], 0,
	 * player.getEquipment().get(i) == null ? new Item[] { } : new Item[] {
	 * player.getEquipment().get(i)});
	 * }
	 *
	 * }
	 *
	 *
	 * public void load(Player player, PresetType type, int index) {
	 *
	 * // if (player.isTemporaryCharacter()) return;
	 *
	 * try {
	 *
	 * final Preset preset = getPreset(type, index);
	 * List<Integer> missing = null;
	 * if (preset == null) {
	 * player.sendMessage("Preset not found " + type + ", " + index + " !");
	 * return;
	 * }
	 * if (type == PresetType.DEFAULT_PRESET) {
	 * return;
	 * } else {
	 * if ((missing = check(preset)) == null) {
	 * return;
	 * }
	 * }
	 *
	 * player.getPrayer().deactivateAll();
	 * player.getCombat().resetSkull();
	 *
	 * player.getInventory().clear();
	 *
	 * int maximum = preset.getInventory().size() > preset.getEquipment().size() ?
	 * preset.getEquipment().size() : preset.getInventory().size();
	 * int random = Misc.random(0, maximum - 1);
	 *
	 * for (Item item : preset.getInventory().get(random)) {
	 * if (item == null || missing.contains(item.getId())) {
	 * continue;
	 * }
	 * if (item != null && item.getDef().id != 995) {
	 * player.getInventory().add(item == null ? null : new Item(item.getId(),
	 * item.getAmount()));
	 * player.getBank().remove(item.getId(), item.getAmount());
	 * } else if (item != null) {
	 * player.sendMessage("Penalty - " + item.getId() + " was not loaded.");
	 * }
	 * }
	 *
	 * player.getEquipment().clear();
	 * for (Item item : preset.getEquipment().get(random)) {
	 * if (item == null || missing.contains(item.getId())) {
	 * continue;
	 * }
	 * player.getEquipment().set(item.getDef().equipSlot, new Item(item.getId(),
	 * item.getAmount()));
	 * player.getBank().remove(item.getId(), item.getAmount());
	 * }
	 *
	 * // if (type != PresetType.CUSTOM_PRESET) {
	 * //// for (int i = 0; i < preset.getLevels().length; i++) {
	 * //// final int level = preset.getLevels()[i];
	 * ////// player.getStats().setSkillPVP(i, level);
	 * //// }
	 * // }
	 *
	 * player.getPacketSender().sendString(593, 3, "Combat lvl: " +
	 * player.getCombat().getLevel());
	 * SpellBook.VALUES[preset.getSpellBook()].setActive(player);
	 *
	 * player.dialogue(new MessageDialogue("Your setup '" + preset.getName() +
	 * "' has been loaded; your previous items have been banked."));
	 * } catch (Exception e) {
	 * player.dialogue(new MessageDialogue("2This preset cannot be found."));
	 * e.printStackTrace();
	 *
	 * }
	 *
	 * }
	 *
	 * public void delete(int index) {
	 *
	 * try {
	 *
	 * final Preset preset = getPreset(PresetType.CUSTOM_PRESET, index);
	 *
	 * if (preset == null) {
	 * player.dialogue(new MessageDialogue(index + "This preset cannot be found."));
	 * throw new NullPointerException();
	 * }
	 *
	 * getPresets().remove(index);
	 * player.dialogue(new MessageDialogue(index + "Your preset '" +
	 * preset.getName() + "' has been deleted"));
	 *
	 * } catch (NullPointerException | IndexOutOfBoundsException e) {
	 * player.sendMessage("Unable to delete the preset...");
	 * }
	 *
	 * }
	 *
	 * private List<Integer> check(Preset preset) {
	 *
	 *
	 * int distance = Misc.getDistance(player.getPosition(), new Position(3093,
	 * 3496));
	 * if (distance > 50) {
	 * player.sendMessage("You can only do this in Edgeville");
	 * return null;
	 * }
	 *
	 * final List<Item> missing = new ArrayList<>();
	 * final List<Integer> missing_ids = new ArrayList<>();
	 *
	 * if (preset.getType() == PresetType.CUSTOM_PRESET) {
	 *
	 * final ItemContainer required = new ItemContainer();
	 * required.init(15 + 28, true);
	 *
	 *
	 * // Check if the preset's inventory can be added to the required
	 * // container
	 * for (Item i : preset.getInventory().get(0)) {
	 * if (i == null) continue;
	 * required.add(i);
	 * }
	 *
	 * // Check if the preset's equipment can be added to the required
	 * // container
	 * for (Item i : preset.getEquipment().get(0)) {
	 * if (i == null) continue;
	 * required.add(i);
	 * }
	 *
	 * player.getBank().deposit(player.getInventory(), false);
	 * player.getBank().deposit(player.getEquipment(), false);
	 *
	 * for (Item i : required.getItems()) {
	 * if (i != null && (i.getDef().stackable || (i.getId() >= 554 && i.getId() <=
	 * 566) || i.getId() == 11669)) {
	 * int count = player.getBank().getAmount(i.getId()) > 0 ?
	 * player.getBank().remove(i) : 0;
	 * if (count < i.getAmount()) {
	 * missing.add(new Item(i.getId(), i.getAmount() - count));
	 * missing_ids.add(i.getId());
	 * }
	 * } else {
	 * if (i != null) {
	 * if (!player.getBank().contains(i)) {
	 * missing.add(new Item(i.getId(), i.getAmount()));
	 * missing_ids.add(i.getId());
	 * }
	 * }
	 * }
	 * }
	 * }
	 * if (!missing.isEmpty()) {
	 * if (!missing.isEmpty()) {
	 * player.sendMessage("You don't have the following items:");
	 * for (Item item : missing) {
	 * player.sendMessage(item.getAmount() + " x " + item.getDef().name);
	 * }
	 * }
	 * }
	 * return missing_ids;
	 *
	 * }
	 *
	 * public Preset getPreset(PresetType type, int index) {
	 *
	 * switch (type) {
	 *
	 * case CUSTOM_PRESET:
	 *
	 * if (presets.size() <= index) {
	 * return null;
	 * }
	 *
	 * return presets.get(index);
	 *
	 * case DEFAULT_PRESET:
	 *
	 * if (DEFAULT_PRESETS.size() <= index) {
	 * return null;
	 * }
	 *
	 * return DEFAULT_PRESETS.get(index);
	 *
	 * default:
	 * return null;
	 *
	 * }
	 *
	 * }
	 *
	 *
	 * public Player getPlayer() {
	 * return this.player;
	 * }
	 *
	 * public List<Preset> getPresets() {
	 * return this.presets;
	 * }
	 *
	 * // public static void register() {
	 * //
	 * // try {
	 * //
	 * // Gson gson = new GsonBuilder().registerTypeAdapter(Preset.class, new
	 * PresetDeserializer()).create();
	 * // Preset[] presets = gson.fromJson(new FileReader(new
	 * File("data/items/presets.json")), Preset[].class);
	 * // int j = 0;
	 * // for (Preset p : presets) {
	 * // DEFAULT_PRESETS.add(p);
	 * // j++;
	 * // }
	 * //
	 * // } catch (Exception e) {
	 * // e.printStackTrace();
	 * //
	 * // }
	 * // }
	 *
	 * public void confirmPreset(boolean accept) {
	 * if (accept & this.current != null) {
	 * presets.add(current);
	 * }
	 * player.dialogue(new MessageDialogue("Your current setup has been saved as " +
	 * current.getName() + "."));
	 * player.closeInterfaces();
	 *
	 *
	 * }
	 *
	 * public String getValue() {
	 *
	 * final List<String> list = new ArrayList<>();
	 *
	 * presets.stream().forEach(p -> {
	 *
	 * final List<String> parts = new ArrayList<>();
	 *
	 * parts.add(p.getName());
	 *
	 * final List<String> inventory = new ArrayList<>();
	 * Arrays.stream(p.getInventory().get(0)).forEach(i -> {
	 *
	 * if (i == null) {
	 * inventory.add("-1|0");
	 * } else {
	 * inventory.add(i.getId() + "|" + i.getAmount());
	 * }
	 *
	 * });
	 *
	 * parts.add(inventory.stream().collect(Collectors.joining(",")));
	 *
	 * final List<String> equipment = new ArrayList<>();
	 * Arrays.stream(p.getEquipment().get(0)).forEach(i -> {
	 *
	 * if (i == null) {
	 * equipment.add("-1|0");
	 * } else {
	 * equipment.add(i.getId() + "|" + i.getAmount());
	 * }
	 *
	 * });
	 *
	 * parts.add(equipment.stream().collect(Collectors.joining(",")));
	 *
	 * parts.add(Arrays.stream(p.getLevels()).mapToObj(l -> l +
	 * "").collect(Collectors.joining(",")));
	 * parts.add(p.getSpellBook() + "");
	 *
	 * list.add(parts.stream().collect(Collectors.joining("/")));
	 *
	 * });
	 *
	 * return list.stream().collect(Collectors.joining("||"));
	 *
	 * }
	 *
	 * public void parse(String data) {
	 *
	 * if (data == null || data.isEmpty()) {
	 * return;
	 * }
	 *
	 * final List<String> presets = new
	 * ArrayList<>(Arrays.asList(data.split(Pattern.quote("||"))));
	 *
	 * presets.stream().forEach(p -> {
	 *
	 * final String[] parts = p.split(Pattern.quote("/"));
	 * final String name = parts[0];
	 *
	 * Preset preset = new Preset(name, PresetType.CUSTOM_PRESET);
	 *
	 * final List<Item> inventory = new ArrayList<>();
	 *
	 * Arrays.stream(parts[1].split(",")).forEach(i -> {
	 *
	 * int[] item =
	 * Stream.of(i.split(Pattern.quote("|"))).mapToInt(Integer::parseInt).toArray();
	 *
	 * if (item[0] == -1) {
	 * inventory.add(null);
	 * } else {
	 * inventory.add(new Item(item[0], item[1]));
	 * }
	 *
	 * });
	 *
	 * preset.getInventory().add(inventory.toArray(new Item[inventory.size()]));
	 *
	 * final List<Item> equipment = new ArrayList<>();
	 *
	 * Arrays.stream(parts[2].split(",")).forEach(i -> {
	 *
	 * int[] item =
	 * Stream.of(i.split(Pattern.quote("|"))).mapToInt(Integer::parseInt).toArray();
	 *
	 * if (item[0] == -1) {
	 * equipment.add(null);
	 * } else {
	 * equipment.add(new Item(item[0], item[1]));
	 * }
	 *
	 * });
	 *
	 * preset.getEquipment().add(equipment.toArray(new Item[equipment.size()]));
	 *
	 * final int[] levels =
	 * Stream.of(parts[3].split(",")).mapToInt(Integer::parseInt).toArray();
	 * preset.setLevels(levels);
	 * final int spellbook = Integer.parseInt(parts[4]);
	 * preset.setSpellBook(spellbook);
	 *
	 * this.presets.add(preset);
	 *
	 * });
	 *
	 * }
	 *
	 * public Preset getCurrent() {
	 * return current;
	 * }
	 *
	 * public void setCurrent(Preset current) {
	 * this.current = current;
	 * }
	 *
	 */
}

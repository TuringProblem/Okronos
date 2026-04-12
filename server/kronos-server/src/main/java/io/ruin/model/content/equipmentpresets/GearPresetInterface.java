package io.ruin.model.content.equipmentpresets;

import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.Item;
import io.ruin.model.item.containers.Equipment;

import java.util.Map;

public class GearPresetInterface {
    /*
    public GearPreset currentPreset;

    public void open(Player player) {
        player.openInterface(ToplevelComponent.MAINMODAL, 832);
        update(player);
    }

    private void sendPresets(Player player) {
        for(int i = 0; i < 13; i++) {
            int component = 72 + (i * 3);
            if(i >= getUnlockedPresets(player))
                player.getPacketSender().sendString(832, component, "<col=990000>Locked");
            else
                player.getPacketSender().sendString(832, component, "<col=e6804d>Empty");
        }
        int i = 0;
        for (GearPreset preset : player.gearPresets) {
            int component = 72 + (i * 3);
            player.getPacketSender().sendString(832, component, "<col=e6804d>" +preset.presetName);
            i++;
        }
    }

    private void update(Player player) {
        sendEmptyEquipmentAndInventory(player);
        sendPresets(player);
        System.out.println("Current preset: " + currentPreset);
        if(currentPreset != null) {
            sendEquipment(player);
            sendInventory(player);
            player.getPacketSender().sendString(832, 117, currentPreset.presetName);
        } else {
            player.getPacketSender().sendString(832, 117, "None");
            sendEmptyEquipmentAndInventory(player);
        }
    }

    private void sendEmptyEquipmentAndInventory(Player player) {
        for(int i = 34; i < 45; i++) {
            player.getPacketSender().setHidden(832, i, false);
        }
        int loops = 0;
        for(int i = 161; i < 172; i++) {
            sendItemToContainer(player, 4732 + loops, i, new Item(-1));
            loops++;
        }
        int loops2 = 0;
        for(int i = 133; i < 161; i++) {
            sendItemToContainer(player, 2732 + loops2, i, new Item(-1));
            loops2++;
        }
    }
    private void selectPreset(Player player, GearPreset preset) {
        currentPreset = preset;
        update(player);
    }
    private void sendEquipment(Player player) {
        for(int i = 34; i < 45; i++) {
            player.getPacketSender().setHidden(832, i, false);
        }
        int loops = 0;
        for(int i = 161; i < 172; i++) {
            sendItemToContainer(player, 4732 + loops, i, new Item(-1));
            loops++;
        }
        int startingContainerId = 4732;
        System.out.println("Equipment in current size: " + currentPreset.equipment.size());
        for(int i = 0; i < 14; i++) {
            if(i == 6 || i == 8 || i == 11) {
                continue;
            }
            if(!currentPreset.equipment.containsKey(i) || currentPreset.equipment.get(i) == null) {
                sendItemToContainer(player, startingContainerId, getItemComponentIdFromSlot(i), new Item(-1));
                startingContainerId++;
            } else {
                player.getPacketSender().setHidden(832, getItemSpriteComponentIdFromSlot(i), true);
                int componentId = getItemComponentIdFromSlot(i);
                sendItemToContainer(player, startingContainerId, componentId, currentPreset.equipment.get(i));
                startingContainerId++;
            }
        }
    }

    private int getItemComponentIdFromSlot(int slot) {
        switch (slot) {
            case Equipment.SLOT_AMMO -> {
                return 168;
            }
            case Equipment.SLOT_CAPE -> {
                return 169;
            }
            case Equipment.SLOT_HAT -> {
                return 161;
            }
            case Equipment.SLOT_WEAPON -> {
                return 171;
            }
            case Equipment.SLOT_AMULET -> {
                return 162;
            }
            case Equipment.SLOT_CHEST -> {
                return 163;
            }
            case Equipment.SLOT_LEGS -> {
                return 164;
            }
            case Equipment.SLOT_FEET -> {
                return 165;
            }
            case Equipment.SLOT_HANDS -> {
                return 166;
            }
            case Equipment.SLOT_RING -> {
                return 167;
            }
            case Equipment.SLOT_SHIELD -> {
                return 170;
            }
            default -> {
                return -1;
            }
        }
    }

    private int getItemSpriteComponentIdFromSlot(int slot) {
        switch (slot) {
            case Equipment.SLOT_AMMO -> {
                return 44;
            }
            case Equipment.SLOT_CAPE -> {
                return 35;
            }
            case Equipment.SLOT_HAT -> {
                return 34;
            }
            case Equipment.SLOT_WEAPON -> {
                return 37;
            }
            case Equipment.SLOT_AMULET -> {
                return 36;
            }
            case Equipment.SLOT_CHEST -> {
                return 38;
            }
            case Equipment.SLOT_LEGS -> {
                return 40;
            }
            case Equipment.SLOT_FEET -> {
                return 42;
            }
            case Equipment.SLOT_HANDS -> {
                return 41;
            }
            case Equipment.SLOT_RING -> {
                return 43;
            }
            case Equipment.SLOT_SHIELD -> {
                return 39;
            }
            default -> {
                return -1;
            }
        }
    }

    private void sendInventory(Player player) {
        int componentId = 133;
        int startingContainerId = 2732;
       for(Item item : currentPreset.inventory) {
            if (item == null) {
                sendItemToContainer(player, startingContainerId, componentId, new Item(-1));
                continue;
            }
            sendItemToContainer(player, startingContainerId, componentId, item);
            componentId++;
            startingContainerId++;
        }
    }

    private int getUnlockedPresets(Player player) {
       if(player.totalDonated > 10 && player.totalDonated < 50)
            return 3;
        else if(player.totalDonated > 50 && player.totalDonated < 100)
            return 4;
        else if(player.totalDonated > 100 && player.totalDonated < 200)
            return 7;
        else if(player.totalDonated > 200 && player.totalDonated < 500)
            return 9;
        else if(player.totalDonated > 500 && player.totalDonated < 1000)
            return 11;
        else if(player.totalDonated > 1000)
            return 13;
        return 2;
    }

    private void sendItemToContainer(Player player, int containerId, int componentId, Item item) {
        System.out.println("Sending item to container: " + item.getId());
        System.out.println("Container ID: " + containerId);
        System.out.println("Component ID: " + componentId);
        player.getPacketSender().sendClientScript(
                149, "IviiiIsssss",
                832 << 16 | componentId, containerId,
                4, 7, 1, -1, "", "", "", "", ""
        );
        player.getPacketSender().sendItems(
                -1,
                componentId,
                containerId,
                new Item(item.getId(), item.getAmount())
        );
    }
    public static void register() {
        InterfaceHandler.register(832, h -> {
            h.actions[131] = (SimpleAction) (player) -> {
                if(player.getGearPresetInterface().currentPreset == null) {
                    player.sendMessage("You don't have a preset selected to delete.");
                    return;
                }
                player.dialogue(new OptionsDialogue("Are you sure you want to delete this preset?",
                        new Option("Yes, delete this preset.", () -> {
                            GearPresetHandler.deleteGearPreset(player, player.getGearPresetInterface().currentPreset);
                        }),
                        new Option("Nevermind.")));
            };
            h.actions[47] = (SimpleAction) (player) -> {
                if(player.gearPresets.size() >= player.getGearPresetInterface().getUnlockedPresets(player)) {
                    player.sendMessage("You don't have anymore preset slots to save this to.");
                    return;
                }
                player.dialogue(new OptionsDialogue("Are you sure you want to save your current equipment and inventory to this preset slot?",
                        new Option("Yes, save my preset.", () -> {
                            GearPresetHandler.saveGearPreset(player);
                        }),
                        new Option("Nevermind.")));
            };
            h.actions[109] = (SimpleAction) (player) -> {
                if(player.getGearPresetInterface().currentPreset == null) {
                    player.sendMessage("You don't have a preset in this slot.");
                    return;
                }
              GearPresetHandler.equipPreset(player, player.getGearPresetInterface().currentPreset);
            };
            for(int i = 0; i < 13; i++) {
                int component = 71 + (i * 3);
                int finalI = i;
                h.actions[component] = (SimpleAction) (player) -> {
                    if(finalI > player.getGearPresetInterface().getUnlockedPresets(player)) {
                        player.sendMessage("Preset locked, you'll get access to more preset slots depending on your donator rank.");
                        return;
                    }
                    if(finalI >= player.gearPresets.size()) {
                        player.sendMessage("You don't have a preset in this slot.");
                        return;
                    }
                    player.getGearPresetInterface().selectPreset(player, player.gearPresets.get(finalI));
                };
            }
        });
    }

     */
}

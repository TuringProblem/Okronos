package io.ruin.model.entity.npc.actions;

import io.ruin.cache.ObjectID;
import io.ruin.model.World;
import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.inter.Interface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.dialogue.ItemDialogue;
import io.ruin.model.inter.dialogue.MessageDialogue;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.object.owned.impl.DwarfCannon;

import java.util.stream.IntStream;


public class DrunkenDwarf {
	public static void register() {
		ObjectAction.register(ObjectID.UPGRADE_MACHINE, "Upgrade", (player, object) -> player.upgradeManager.sendInterface());

        /*NPCAction.register(2408, "Talk-to", (player, npc) -> {
             World.doCannonReclaim(player.getName(), (reclaim) -> {
                if (reclaim) {
                    boolean hasSpace = player.getInventory().hasFreeSlots(DwarfCannon.CANNON_PARTS.length);
                    player.dialogue(
                            new PlayerDialogue("I've lost my cannon, can i have another one?"),
                            !hasSpace ? new NPCDialogue(npc, "Come back when you at least 4 inventory spaces.") : new NPCDialogue(npc, "Yeah sure, here you go. Try not to do it again."),
                            !hasSpace ? new MessageDialogue("You need at least 4 inventory spaces to claim your cannon back.") : new ItemDialogue().one(DwarfCannon.BARRELS, "The Drunken dwarf gives you another cannon.").action(() -> {
                                IntStream.of(DwarfCannon.CANNON_PARTS).forEach(player.getInventory()::add);
                                World.removeCannonReclaim(player.getName());
                            })
                    );
                } else {
                    player.dialogue(
                            new PlayerDialogue("Hello there! Are you alright?"),
                            new NPCDialogue(npc, "Of courshe! Why why why hic* why shouldn't I be?"),
                            new PlayerDialogue("I don't know... You look a bit drunk."),
                            new NPCDialogue(npc, "Noooooo, hic* that's the liquor doing the talking."),
                            new PlayerDialogue("Ok... Hey, do you know what this machine next to you does?"),
                            new NPCDialogue(npc, "That old thing? That's jusht my hic* upgrader manager thingy."),
                            new NPCDialogue(npc, "It makes items do hic* unique things after they've been shoved into it."),
                            new NPCDialogue(npc, "Sometimes you hic* can get pretty cool stuff, but for at a cost."),
                            new PlayerDialogue("What kind of cost are we talking here?"),
                            new NPCDialogue(npc, "Well, it takes a few items. hic*"),
                            new PlayerDialogue("That's pretty cool, how do i use it?"),
                            new NPCDialogue(npc, "All you have to do, is hic* open it up, " +
                                    "and make sure you have the hic* required items to upgrade."),
                            new PlayerDialogue("What do i get after i upgrade?"),
                            new NPCDialogue(npc, "You can just look at the hic* bensh, put your items into it, and KABAM! You might get cool hic* stuff."),
                            new PlayerDialogue("Okay, thanks. I'll give it a try some time.")
                    );
                }
            });

        });

         */
	}
}

package io.ruin.model.item.actions.impl;

import io.ruin.model.entity.npc.NPCAction;
import io.ruin.model.inter.dialogue.NPCDialogue;
import io.ruin.model.inter.dialogue.OptionsDialogue;
import io.ruin.model.inter.dialogue.PlayerDialogue;
import io.ruin.model.inter.utils.Option;
import io.ruin.model.item.actions.ItemAction;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.stat.StatType;
import io.ruin.utility.Broadcast;

public class GetPissed {

	public static void register() {

		ItemAction.registerInventory(2084, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Oi, I asked for a mans drink!");
			} else {
				player.forceText("Oo, this cocktail is lovely!");
			}
			Consumable.animDrink(player);
			item.setId(2026);
		});

		ItemAction.registerInventory(1917, "drink", (player, item) -> {
			Consumable.animDrink(player);
			item.setId(1919);
		});

		ItemAction.registerInventory(2048, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Oi, I asked for a mans drink!");
				//restore 9 hit points
				player.getStats().get(StatType.Hitpoints).restore(9);
			} else {
				player.forceText("Oo, this cocktail is lovely!");
				//restore 9 hit points
				player.getStats().get(StatType.Hitpoints).restore(9);
			}
			Consumable.animDrink(player);
			item.setId(2026);
		});

		ItemAction.registerInventory(1913, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Now that's what I'm talking about!");
			} else {
				player.forceText("This tastes like an old man!");
			}
			Consumable.animDrink(player);
			item.setId(1919);
		});

		ItemAction.registerInventory(1905, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Straight from keg lovely!");
			} else {
				player.forceText("Who makes this crap? a dwarf with 1 eye?!");
			}
			Consumable.animDrink(player);
			item.setId(1919);
		});

		ItemAction.registerInventory(1907, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("WOW! These wizards know what they're doing!");
				//increase magic
			} else {
				player.forceText("Omg, this is my new fave! wizzy another one over here please!");
				player.getInventory().add(1907, 1);
			}
			Consumable.animDrink(player);
			item.setId(1919);
		});

		ItemAction.registerInventory(22430, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("That'll put hair on your chest! or not! wait what's happening!");
				player.addEvent(event -> {
					player.getAppearance().setNpcId(8334); // male vamp
					event.delay(10);
					player.getAppearance().setNpcId(-1);
				});
			} else {
				player.forceText("There's blood in my drink! I don't feel so good!");
				player.addEvent(event -> {
					player.getAppearance().setNpcId(8251); // female vamp
					event.delay(10);
					player.getAppearance().setNpcId(-1);
				});
			}
			Consumable.animDrink(player);
			item.setId(1919);

		});

		ItemAction.registerInventory(24774, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Oh lord! not this again please not a vampire again!");
				player.addEvent(event -> {
					player.getAppearance().setNpcId(2599); // werewolf
					event.delay(10);
					player.getAppearance().setNpcId(-1);
				});
			} else {
				player.forceText("I mean I liked being a vampire last time, I killed my ex and nobody knew!");
				player.addEvent(event -> {
					player.getAppearance().setNpcId(2599); // werewolf
					event.delay(10);
					player.getAppearance().setNpcId(-1);
				});
			}
			Consumable.animDrink(player);
			item.setId(1919);
		});

		ItemAction.registerInventory(23948, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Ahh lovely and elven beer! goes down a treat!");
				player.addEvent(event -> {
					player.getAppearance().setNpcId(9157); // elven male
					event.delay(10);
					player.getAppearance().setNpcId(-1);
				});
			} else {
				player.forceText("Ooo I love the colour, I wonder if this will turn me into an elven?");
				player.addEvent(event -> {
					player.getAppearance().setNpcId(9094); // elven female
					event.delay(10);
					player.getAppearance().setNpcId(-1);
				});
			}
			Consumable.animDrink(player);
			item.setId(1919);
		});

		ItemAction.registerInventory(1911, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Oh that's got a kick to it alright! I can feel it burning my throat! I'll have another!");
				player.getInventory().add(1911, 1);
			} else {
				player.forceText("Oh Oh Oh, Quick Wa... Wat... Waterrrrrrr!!");
				player.animate(4280);
			}
			Consumable.animDrink(player);
			item.setId(1919);
		});

		ItemAction.registerInventory(1909, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Ahhh freshly brewed, still has the yeast at the bottom!");
			} else {
				player.forceText("Not the greatest, but I suppose it will do! Thanks.");
			}
			Consumable.animDrink(player);
			item.setId(1919);
		});

		ItemAction.registerInventory(5763, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Cider, I remember the days when we used to go scrumping for apples!");
			} else {
				player.forceText("Eww, taste like someone pissed in my glass!");
			}
			Consumable.animDrink(player);
			item.setId(1919);
		});

		ItemAction.registerInventory(3803, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Ahh, A proper stout from the lads up north! they know what they're doing right here!");
			} else {
				player.forceText("Wait I have to drink the entire thing? Seriously?! Ok...");
			}
			Consumable.animDrink(player);
			item.setId(1919);
		});

		ItemAction.registerInventory(1915, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Grog, from the land of the giants I think?");
				player.addEvent(event -> {
					player.getAppearance().setNpcId(2098); // elven male
					event.delay(10);
					player.getAppearance().setNpcId(-1);
				});
			} else {
				player.forceText("Ooo a Grog, lovely colour. Wait a minute something tastes funny about this!");
				player.addEvent(event -> {
					player.getAppearance().setNpcId(2098); // elven male
					event.delay(10);
					player.getAppearance().setNpcId(-1);
				});
			}
			Consumable.animDrink(player);
			item.setId(1919);
		});

		ItemAction.registerInventory(4627, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Bandit's brew? I bet this is stolen from somewhere!");
			} else {
				player.forceText("Oo the colour of this looks lovely! I wonder where it come from!");
			}
			Consumable.animDrink(player);
			item.setId(1919);
		});

		ItemAction.registerInventory(2955, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Moonlight? Sounds like a girls drink!");
			} else {
				player.forceText("Oh I love the name! and the taste is amazing!");
			}
			Consumable.animDrink(player);
			item.setId(1919);
		});

		ItemAction.registerInventory(7157, "drink", (player, item) -> {
			player.forceText("who are you? who am I? where am I?");
			Consumable.animDrink(player);
			item.remove();
		});

		ItemAction.registerInventory(2017, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Not bad, I've had better. I can tell this is the cheap stuff!");
			} else {
				player.forceText("Ahh Feels like a fire in my throat!");
			}
			Consumable.animDrink(player);
			item.remove();
		});

		ItemAction.registerInventory(2015, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Who want's to drink bleach?!");
			} else {
				player.forceText("Vodka makes the pain go away!");
			}
			Consumable.animDrink(player);
			item.remove();
		});

		ItemAction.registerInventory(2021, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Luke had no clue this stuff tasted and smelt so bad!");
			} else {
				player.forceText("Oh reminds me of being round at my nan's she was a piss head alright.");
			}
			Consumable.animDrink(player);
			item.remove();
		});

		ItemAction.registerInventory(2032, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Oh lovely this gave me a great boost!");
				player.getStats().get(StatType.Hitpoints).restore(5);
				player.getStats().get(StatType.Strength).boost(7, 0.25);
				player.getStats().get(StatType.Attack).drain(4);
			} else {
				player.forceText("Ah freshing and lively!");
				player.getStats().get(StatType.Hitpoints).restore(5);
				player.getStats().get(StatType.Strength).boost(7, 0.25);
				player.getStats().get(StatType.Attack).drain(4);
			}
			Consumable.animDrink(player);
			item.setId(2026);
		});

		ItemAction.registerInventory(2034, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Oh it gives me shivers in my bones!");
				player.getStats().get(StatType.Hitpoints).restore(9);
			} else {
				player.forceText("Oo, I feel all tingly inside!");
				player.getStats().get(StatType.Hitpoints).restore(9);
			}
			Consumable.animDrink(player);
			item.setId(2026);
		});

		ItemAction.registerInventory(2036, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Oh it gives me shivers in my bones!");
				player.getStats().get(StatType.Hitpoints).restore(9);
			} else {
				player.forceText("Oo, I feel all tingly inside!");
				player.getStats().get(StatType.Hitpoints).restore(9);
			}
			Consumable.animDrink(player);
			item.setId(2026);
		});

		ItemAction.registerInventory(2038, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("I'm feeling a little bit green today guys!");
				player.getStats().get(StatType.Hitpoints).restore(5);
				player.getStats().get(StatType.Strength).boost(4, 0.25);
				player.getStats().get(StatType.Attack).drain(3);
			} else {
				player.forceText("Omg I look amazing! Check out my skin colour! Fabulousssss!");
				player.getStats().get(StatType.Hitpoints).restore(5);
				player.getStats().get(StatType.Strength).boost(4, 0.25);
				player.getStats().get(StatType.Attack).drain(3);
			}
			Consumable.animDrink(player);
			item.setId(2026);
		});

		ItemAction.registerInventory(2040, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("This is so cold it's giving me shivers!");
				player.getStats().get(StatType.Hitpoints).restore(5);
				player.getStats().get(StatType.Strength).boost(6, 0.25);
				player.getStats().get(StatType.Attack).drain(4);
			} else {
				player.forceText("Ooo... it's so refreshing while it's cold!");
				player.getStats().get(StatType.Hitpoints).restore(5);
				player.getStats().get(StatType.Strength).boost(6, 0.25);
				player.getStats().get(StatType.Attack).drain(4);
			}
			Consumable.animDrink(player);
			item.setId(2026);
		});

		ItemAction.registerInventory(2030, "drink", (player, item) -> {
			if (player.getAppearance().isMale()) {
				player.forceText("Chocolate's Ok, I guess but I still prefer a Cold beer!");
				player.getStats().get(StatType.Hitpoints).restore(5);
				player.getStats().get(StatType.Strength).boost(7, 0.25);
				player.getStats().get(StatType.Attack).drain(4);
			} else {
				player.forceText("Two words ladies... Chocolate Fundayyyssss!!!");
				player.getStats().get(StatType.Hitpoints).restore(5);
				player.getStats().get(StatType.Strength).boost(7, 0.25);
				player.getStats().get(StatType.Attack).drain(4);
			}
			Consumable.animDrink(player);
			item.setId(2026);
		});

		//TODO Barcrawl Card Reading
		ItemAction.registerInventory(455, "read", (player, item) -> {
			if (player.BlueMoonPub == 0) {
				player.sendMessage("Looks like I'm missing the Blue Moon Inn!");
			}
			if (player.BarbarianVillage == 0) {
				player.sendMessage("Looks like I'm missing the Barbarians in the village!");
			}
			if (player.KayleeFally == 0) {
				player.forceText("Looks like I'm missing that fit bird Kaylee in Falador!");
			}
			if (player.BartPortSarim == 0) {
				player.sendMessage("Looks like I'm missing the Sea dogs in port sarim!");
			}
			if (player.MalakCanafis == 0) {
				player.sendMessage("I feel a little bit odd about the bar in canafis, there's no bar keep!");
			}
			if (player.BartSeers == 0) {
				player.sendMessage("Ahh I'm missing seers village bar!");
			}
			if (player.BartBandit == 0) {
				player.sendMessage("Now this should be fun, Bandits and a bar. what could go wrong?!");
			}
			if (player.BartEastArdy == 0) {
				player.sendMessage("I'm missing the bar in east ardy.");
			}
			if (player.TostigBurthorpe == 0) {
				player.sendMessage("Ahh I've not seen Tostig since I was training over that way at the warriors guild!");
			}
			if (player.BrawlCardSigs == 9) {
				player.forceText("Fucking aye right, I've completed the bar crawl!!");
			}

		});

		NPCAction.register(3534, "talk-to", (player, npc) -> {
			player.dialogue(
				new NPCDialogue(npc, "Ahh the birds and the bees!"),
				new OptionsDialogue(
					new Option("Hey, what are you doing here?", () -> {
						player.dialogue(
							new PlayerDialogue("Hey, what are you doing here?"),
							new NPCDialogue(npc, "Getting pissed what does it look like? here take this and join me!"),
							new PlayerDialogue("Sounds good!")

						);
						player.getInventory().add(455, 1);
					}))
			);
		});

		ItemNPCAction.register(455, 3534, (player, item, npc) -> {
			//NPC you turn the brawl card into and get it from
			if (player.BrawlCardSigs == 9) {
				player.dialogue(
					new NPCDialogue(npc, "Sun, beer, and bitches! what could go wrong?!"),
					new OptionsDialogue(
						new Option("I have completed the bar crawl card!", () ->

							player.dialogue(
								new PlayerDialogue("I have completed the bar crawl card!"),
								new NPCDialogue(npc, "Let me see your bar crawl card then lad."),
								new PlayerDialogue("Here you go 9 signatures 1 from each pub!"),
								new NPCDialogue(npc, "Looks like you're missing one mate! here take this drink it then and I'll sign your card!"),
								new PlayerDialogue("Oh ffs, fine! I'll be back in a sec!")
							))));

				player.BrawlCardSigs = 10;
				player.getInventory().add(2048, 1);

			}
			if (player.BrawlCardSigs == 10) {
				player.dialogue(
					new NPCDialogue(npc, "Oh those wizards tricked me! this is just a normal beer!"),
					new OptionsDialogue(
						new Option("I've done it!", () -> player.dialogue(
							new PlayerDialogue("There ya go I've finished the card now!"),
							new NPCDialogue(npc, "Hand it over let me check it!"),
							new PlayerDialogue("Here you go 1 card 10 signatures!"),
							new NPCDialogue(npc, "Congratulations! your prizes are a choice off..."),
							new PlayerDialogue("What's the prizes? Hey! I'm talking to you!!")
						))));
				player.BrawlCardSigs = 11;
				player.getInventory().addOrDrop(2030, 1);
				player.getInventory().addOrDrop(2040, 1);
				player.getInventory().addOrDrop(2034, 1);
				player.getInventory().addOrDrop(2048, 1);
				player.getInventory().addOrDrop(1907, 1);
				Broadcast.WORLD.sendNews(player.getName() + " Has just completed the bar crawl event!!");

			}

		});


		ItemNPCAction.register(455, 1312, (player, item, npc) -> {
			if (player.BlueMoonPub == 0) {
				//Blue moon inn Varrock South gate!
				player.dialogue(
					new NPCDialogue(npc, "How can I help you?"),
					new OptionsDialogue(
						new Option("I'm here about a bar crawl?", () -> player.dialogue(
							new PlayerDialogue("I'm here about a bar crawl?"),
							new NPCDialogue(npc, "Ahh the good old bar crawl, I remember a time when I was younger doing this!"),
							new PlayerDialogue("I don't mean to be funny, but I've got a lot of shit on at the moment!"),
							new NPCDialogue(npc, "Alright calm ya horse, here's ya drink and I'll sign ya card."),
							new PlayerDialogue("Cheers mate!")
						))));
				player.getInventory().add(1917, 1);
				player.BlueMoonPub = 1;
				player.BrawlCardSigs += 1;
			}
		});

		ItemNPCAction.register(455, 299, (player, item, npc) -> {
			//Barbarian's village!
			if (player.BarbarianVillage == 0) {
				player.dialogue(
					new NPCDialogue(npc, "What do you want!?"),
					new OptionsDialogue(
						new Option("I'm here for a beer!", () -> player.dialogue(
							new PlayerDialogue("I just want to a beer and my card signing!"),
							new NPCDialogue(npc, "Fair enough then show me your card!"),
							new PlayerDialogue("Here take it!"),
							new NPCDialogue(npc, "Fine, ok Wench! 2 beers over here!"),
							new PlayerDialogue("Thanks!")
						))));
				player.getInventory().add(1905, 1);
				player.BarbarianVillage = 1;
				player.BrawlCardSigs += 1;
			}

		});

		ItemNPCAction.register(455, 1316, (player, item, npc) -> {
			//Kaylee Falador!
			if (player.KayleeFally == 0) {
				player.dialogue(
					new NPCDialogue(npc, "How can I help you?"),
					new OptionsDialogue(
						new Option("I'm here about a bar crawl?", () -> player.dialogue(
							new PlayerDialogue("I'm here about a bar crawl?"),
							new NPCDialogue(npc, "Oh yeah, ok show me the card then."),
							new PlayerDialogue("Here you go sweet thing!"),
							new NPCDialogue(npc, "Call me that again and I'll have your head on a slab! now here take your beers and piss off!"),
							new PlayerDialogue("I'm erm sorry miss.")
						))));
				player.getInventory().add(5763, 1);
				player.KayleeFally = 1;
				player.BrawlCardSigs += 1;
			}
		});

		ItemNPCAction.register(455, 1313, (player, item, npc) -> {
			//Bartender in Port Sarim
			if (player.BartPortSarim == 0) {
				player.dialogue(
					new NPCDialogue(npc, "How can I help you?"),
					new OptionsDialogue(
						new Option("I'm here about a bar crawl?", () -> player.dialogue(
							new PlayerDialogue("I'm here about a bar crawl?"),
							new NPCDialogue(npc, "Oh the bar crawl so glad that's finally returned!"),
							new PlayerDialogue("Here's my card fella, can I get a beer as well mate"),
							new NPCDialogue(npc, "Aye sure I've signed ya card so drink up lad!"),
							new PlayerDialogue("Cheers mate!")
						))));
				player.getInventory().add(1915, 1);
				player.BartPortSarim = 1;
				player.BrawlCardSigs += 1;
			}
		});

		ItemNPCAction.register(455, 686, (player, item, npc) -> {
			//Malak in canafis
			if (player.MalakCanafis == 0) {
				player.dialogue(
					new NPCDialogue(npc, "What do you want human!?"),
					new OptionsDialogue(
						new Option("God damn it I forgot garlic!", () -> player.dialogue(
							new PlayerDialogue("Shit my bed, I forgot the garlic, but I'm here about the bar crawl?"),
							new NPCDialogue(npc, "If you had garlic on you I'd of killed you!"),
							new PlayerDialogue("Good thing I always carry something silver with then isn't it!, now sign my card and give me a beer!"),
							new NPCDialogue(npc, "As you wish human! Drink up. muhahahahahaha"),
							new PlayerDialogue("Pale faced creep!")
						))));
				player.getInventory().add(24774, 1);
				//add beer to inventory
				player.MalakCanafis = 1;
				player.BrawlCardSigs += 1;
			}
		});

		ItemNPCAction.register(455, 4102, (player, item, npc) -> {
			//Tostig in Burthorpe
			if (player.TostigBurthorpe == 0) {
				player.dialogue(
					new NPCDialogue(npc, "How can I help you?"),
					new OptionsDialogue(
						new Option("I'm here about a bar crawl?", () -> player.dialogue(
							new PlayerDialogue("I'm here about a bar crawl?"),
							new NPCDialogue(npc, "Ahh the good old bar crawl, I remember a time when I was younger doing this!"),
							new PlayerDialogue("I don't mean to be funny, but I've got a lot of shit on at the moment!"),
							new NPCDialogue(npc, "Alright calm ya horse, here's ya drink and I'll sign ya card."),
							new PlayerDialogue("Cheers mate!")
						))));
				player.getInventory().add(1911, 1);
				player.TostigBurthorpe = 1;
				player.BrawlCardSigs += 1;
			}
		});

		ItemNPCAction.register(455, 1318, (player, item, npc) -> {
			//Bartender in seers village
			if (player.BartSeers == 0) {
				player.dialogue(
					new NPCDialogue(npc, "How can I help you?"),
					new OptionsDialogue(
						new Option("I'm here about a bar crawl?", () -> player.dialogue(
							new PlayerDialogue("I'm here about a bar crawl?"),
							new NPCDialogue(npc, "Welcome to seers village! Hand me your card."),
							new PlayerDialogue("Here you go mate, can I get a beer as well pal!"),
							new NPCDialogue(npc, "Sure here's your beer mate and I've signed your card!"),
							new PlayerDialogue("Cheers mate!")
						))));
				player.getInventory().add(2955, 1);
				player.BartSeers = 1;
				player.BrawlCardSigs += 1;
			}
		});

		ItemNPCAction.register(455, 687, (player, item, npc) -> {
			//Bartender in Bandit Camp
			if (player.BartBandit == 0) {
				player.dialogue(
					new NPCDialogue(npc, "How can I help you?"),
					new OptionsDialogue(
						new Option("I'm here about a bar crawl?", () -> player.dialogue(
							new PlayerDialogue("I'm here about a bar crawl?"),
							new NPCDialogue(npc, "Well you best be quick as we don't like strangers around here!"),
							new PlayerDialogue("Just sign it give me a beer and I'm gone!"),
							new NPCDialogue(npc, "Here ya go, good bye! Lads!!"),
							new PlayerDialogue("Oh shit!")
						))));
				player.getInventory().add(4627, 1);
				player.BartBandit = 1;
				player.BrawlCardSigs += 1;
			}
		});

		ItemNPCAction.register(455, 1319, (player, item, npc) -> {
			//Bartender in East Ardy
			if (player.BartEastArdy == 0) {
				player.dialogue(
					new NPCDialogue(npc, "How can I help you?"),
					new OptionsDialogue(
						new Option("I'm here about a bar crawl?", () -> player.dialogue(
							new PlayerDialogue("I'm here about a bar crawl?"),
							new NPCDialogue(npc, "Oh I remember the bar crawl before ardy was split in two!"),
							new PlayerDialogue("You know that the other side of the wall is fine? and it's just run by morons?"),
							new NPCDialogue(npc, "Oh really, who's running it now then?"),
							new PlayerDialogue("A guy name CutBowl, guy's a right moron!"),
							new NPCDialogue(npc, "Ahh yes CutBowl, he has always been a prick! anyways here's your beer!"),
							new PlayerDialogue("Cheers mate!")
						))));
				player.getInventory().add(2021, 1);
				player.BartEastArdy = 1;
				player.BrawlCardSigs += 1;
			}
		});
	}
}

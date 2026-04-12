package io.ruin.model.content;

import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.model.World;
import io.ruin.utility.Broadcast;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Announcements {

	public static void register() {
		World.startEvent(e -> {
			List<String> announcements = Arrays.asList(
				"Did you know that you receive Reason Points for gaining level 99 in a skill, as well as achieving 200M, 500M, and 1B experience in a stat?",
				"Have you completed your Daily Tasks today? Check them out by typing ::dailies",
				"Did you know that there are ::newcomer tasks available to complete to obtain a Ring of Reason for a 2% drop rate increase!",
				"Did you know Reason Points are also rewarded for killing monsters over level 100?",
				"Did you know you can kill slayer creatures on task in order to get Slayer keys? Tier 3 and 4 keys can be used to pull the coveted Corrupted Slayer Helm Kit!",
				"Did you know you can unlock Perks by talking to the Perk Master at home? Speedy Strikes is a good one to start off with for melee!",
				"Did you know every 10 votes for Reason will spawn a Vote Boss that can help with some scrolls and other helpful drops!",
				"Did you know you can upgrade specific gear at the Upgrade Station at home? You may also type ::upgrade!",
				"Attach Perks to your equipment by getting minerals and enhancers for the Item Workbench at home!",
				"Did you know that new players have access to a special drop table at Catalysts, including a weapon for each combat style?",
				"Did you know you can join the friends chat 'Kal' for help?",
				"Did you know you can right click Examine NPCs to see their drop table?",
				"Did you know you can get free bonds each month by keeping your vote streak going?",
				"Make sure to claim your ::fall referral code for some goodies to help you get started on your Reason adventure!",
				"Did you know our Top Donators and Top Voters get rewarded every month with bonds, unique Discord titles, and even a chance to pitch your own ideas!",
				"Did you know you can check out the Collection Logs offered by typing ::clog, and while you're there don't forget to claim your rewards!",
				"Did you know Reason has an awesome Wiki page put together by our incredible staff team? Check it out on Discord via #reason-wiki",
				"Did you know you can right click the Home Teleport and teleport to your previous location?",
				"Have you checked out the various perks you can unlock from the Perk Master?"
			);
			AtomicInteger index = new AtomicInteger();
            /*while (true) {
                e.delay(500);
                Broadcast.WORLD.sendNews(Icon.ANNOUNCEMENT2, "<col=000000><shad=000000><col=1D7AE7>Info:</shad><col=000000><shad=579DF0> "+announcements.get(index.get()));
                index.getAndIncrement();
                if (index.intValue() == announcements.size())
                    index.set(0);
            }

             */
		});
	}
}

package io.ruin.model.inter.questtab.main.UsefulLinks;

import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.handlers.OptionScroll;
import io.ruin.model.inter.questtab.QuestTabEntry;
import io.ruin.model.inter.utils.Option;

/**
 * @author Greco
 */
public class UsefulLinks extends QuestTabEntry {

	public static final UsefulLinks INSTANCE = new UsefulLinks();

	public static String USEFUL_LINKS = "Useful Links";

	public static String DISCORD_LINK = "https://discord.gg/WeYBAV7S3G";
	public static String DONATION_LINK_NAME = World.type.getWorldName() + " Store";
	public static String DONATION_LINK = World.type.getWebsiteUrl() + "/store/";
	public static String FORUMS_LINK = World.type.getWebsiteUrl() + "/forums/";
	public static String VOTE_LINK = World.type.getWebsiteUrl() + "/vote/";

	public static String DONATE = "Open Store";
	public static String FORUMS = "Open Forums";
	public static String VOTE = "Open Voting";
	public static String DISCORD = "Join Discord";

	@Override
	public void send(Player player) {
		send(player, USEFUL_LINKS);
	}

	@Override
	public void select(Player player) {
		openUsefulLinksMenu(player);
	}

	public static void openUsefulLinksMenu(Player player) {
        /*
        OptionScroll.open(player, "Select a link",
                new Option(DISCORD, () -> player.openUrl(World.type.getWorldName() + " Discord", "https://discord.gg/FphC83tEF8")),
                new Option(FORUMS, () -> player.openUrl(World.type.getWorldName() + " Forums", FORUMS_LINK)),
                new Option(VOTE, () -> player.openUrl(World.type.getWorldName() + " Voting", VOTE_LINK)),
                new Option(DONATE, () -> player.openUrl(DONATION_LINK_NAME, DONATION_LINK))
        );

         */
	}

}

package io.ruin.services.discord;

import io.ruin.model.World;
import io.ruin.services.discord.listeners.CommandsListener;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

@Slf4j
public class Discord {

	public static JDA builder;
	public static Guild guild;

	static String token = "";

	public static void initialize() throws LoginException, InterruptedException {
       /* log.info("Initializing Discord Bot");
        builder = JDABuilder.createDefault(token)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .setBulkDeleteSplittingEnabled(false)
                .setActivity(Activity.playing("Prifdinnas"))
                .addEventListeners(new CommandsListener())
                .build().awaitReady();
        TaskWorker.startTask(t -> {
            while (true) {
                t.sleep(60000L);
                if (builder != null)
                    builder.getPresence().setActivity(Activity.playing("Prif with " + World.players.count() + " players!"));
            }
        });
        if (builder != null) {
            guild = builder.getGuildById(808974175380439070L);
            log.info("Discord bot started, {} found.", guild.getName());
            CommandsListener.updateCommands(); //You don't need to call this everytime just whenever you modify commands (discord caches it)
        }

        */
	}

}

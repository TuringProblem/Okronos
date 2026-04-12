//package io.ruin.services.discord.old.impl;
/// *
// * @project Kronos
// * @author Patrity - https://github.com/Patrity
// * Created on - 1/14/2020
// */
//
//import io.ruin.Server;
//import io.ruin.api.utils.TimeUtils;
//import io.ruin.model.World;
//import io.ruin.model.activities.wilderness.Hotspot;
//import io.ruin.model.activities.wilderness.Wilderness;
//import io.ruin.model.entity.player.Player;
//import lombok.SneakyThrows;
//import net.dv8tion.jda.api.EmbedBuilder;
//import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//
//import java.awt.*;
//
//public class UserCommands extends ListenerAdapter {
//
//    private String[] onlineAliases = {"::online", "::players", "::info", "::server"};
//
//    public static Player player;
//    @SneakyThrows
//    @Override
//    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
//
//        if (e.getAuthor().isBot())
//            return;
//        if(World.isBeta())
//            return;
//
//
//        String message = e.getMessage().getContentRaw().toLowerCase();
//
//        for (int x = 0; x < onlineAliases.length; x++) {
//            if (e.getMessage().getContentRaw().equalsIgnoreCase(onlineAliases[x])) {
//                if (!e.getChannel().getId().equalsIgnoreCase("990329469627682936")) {
//                    e.getChannel().sendMessage("Please only use this command in #bot-controls!").queue();
//                    return;
//                }
//                EmbedBuilder embed = new EmbedBuilder()
//                        .setTitle("Prifddinas Server Status", "https://Prifddinas.io")
//                        .setColor(new Color(0xB00D03))
//                        .addField("Players Online:", String.valueOf(World.players.count()), true)
//                        .addField("Uptime:", TimeUtils.fromMs(Server.currentTick() * Server.tickMs(), false), true)
//                        .addField("Players in Wild:", String.valueOf(Wilderness.players.size()), true)
//                        .addField("**Active Bonuses:**", "", false)
//                        .addField("XP Boost:", World.xpMultiplier+"x", true)
//                        .addField("Weekend XP:", World.weekendExpBoost ? "Enabled" : "Disabled", true)
//                        .addField("Double Wintertodt:", World.doubleWintertodt ? "Enabled" : "Disabled", true)
//                        .addField("Double Slayer:", World.doubleSlayer ? "Enabled" : "Disabled", true)
//                        .addField("Double PestControl:", World.doublePest ? "Enabled" : "Disabled", true)
//                        .addField("Double BloodMoney:", World.doublePkp ? "Enabled" : "Disabled", true)
//                        .addField("Hotspot:", Hotspot.ACTIVE.name, true);
//                e.getChannel().sendMessage(embed.build()).queue();
//            }
//        }
//        if(e.getChannel().getId().equalsIgnoreCase("990329469627682936")){
//            if (e.getMessage().getContentRaw().contains("::doublewinter")) {
//                World.toggleDoubleWintertodt();
//            }
//            if (e.getMessage().getContentRaw().contains("::doubleslayer")) {
//                World.toggleDoubleSlayer();
//            }
//            if (e.getMessage().getContentRaw().contains("::doublexp")) {
//                World.toggleWeekendExpBoost();
//            }
//            if (e.getMessage().getContentRaw().contains("::doublepc")) {
//                World.toggleDoublePest();
//            }
//            if (e.getMessage().getContentRaw().contains("::doublepk")) {
//                World.toggleDoublePkp();
//            }
//        }
//
//    }
//}

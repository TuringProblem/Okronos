//package io.ruin.model.inter.questtab.main;
//
//import io.ruin.cache.Color;
//import io.ruin.model.World;
//import io.ruin.model.entity.player.Player;
//import io.ruin.model.inter.questtab.QuestTabEntry;
//
//public class DoubleDrops extends QuestTabEntry {
//
//    public static final DoubleDrops INSTANCE = new DoubleDrops();
//
//    @Override
//    public void send(Player player) {
//        if (World.doubleDrops) {
//            send(player, "Double Drops", "Enabled", Color.GREEN);
//        } else {
//            send(player, "Double Drops", "Disabled", Color.RED);
//        }
//    }
//
//    @Override
//    public void select(Player player) {
//        if(World.doubleDrops) {
//            player.sendMessage("Double Drops is active!");
//        } else {
//            player.sendMessage("Double Drops are disabled!");
//        }
//    }
//
//}
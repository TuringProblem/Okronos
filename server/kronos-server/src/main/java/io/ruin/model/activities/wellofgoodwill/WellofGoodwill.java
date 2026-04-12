package io.ruin.model.activities.wellofgoodwill;

import io.ruin.Server;
import io.ruin.api.database.DatabaseUtils;
import io.ruin.model.World;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Misc;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.ruin.model.inter.Interface.WELL_OF_GOODWILL;

public class WellofGoodwill {
/*
    public static final int OBJECT_ID = 884;

    private static final HashMap<String, Long> contributors = new LinkedHashMap<>();


    public static final double BOOST_MODIFIER = 2.0D;

    public static boolean enabled = true;

    private static long[] amounts = new long[Type.values().length];

    private static long[] expiry = new long[Type.values().length];

    private static boolean expired(Type type) {
        return System.currentTimeMillis() > expiry[type.ordinal()];
    }

    public static void init() {
        Server.gameDb.execute(connection -> {
            PreparedStatement statement = null;
            ResultSet rs = null;

            try {
                statement = connection.prepareStatement("SELECT * FROM wogw");
                rs = statement.executeQuery();
                while (rs.next()) {
                    Type type = Type.values()[rs.getInt("type")];
                    expiry[type.ordinal()] = rs.getLong("expiry");
                    amounts[type.ordinal()] = rs.getLong("amount");
                }
            } finally {
                DatabaseUtils.close(statement, rs);
            }
        });
    }

    private static void extend(Type type, long hours) {
        if (expired(type)) {
            expiry[type.ordinal()] = System.currentTimeMillis();
        }

        expiry[type.ordinal()] += TimeUnit.HOURS.toMillis(hours);
    }

    @AllArgsConstructor
    public enum Type {
        DOUBLE_EXPERIENCE(40_000_000),
        SLAYER_POINTS(35_000_000),
        DOUBLE_PEST_POINTS(15_000_000),
        DOUBLE_DROPS(250_000_000);

        private int cost;
    }

    public static boolean isDoubleXp() {
        return !expired(Type.DOUBLE_EXPERIENCE);
    }

    public static boolean isDoubleSlayer() {
        return !expired(Type.SLAYER_POINTS);
    }

    public static boolean isDoublePest() {
        return !expired(Type.DOUBLE_PEST_POINTS);
    }

    public static boolean isDoubleDrops() {
        return !expired(Type.DOUBLE_DROPS);
    }

    public static void contribute(Player player, Type type) {
        if (!enabled) {
            player.sendMessage("<col=ff0000>The Well of Goodwill is currently disabled.");
            return;
        }

        if (!player.getInventory().hasId(995)) {
            player.sendMessage("You need to have coins in your inventory to continue.");
            return;
        }
        player.integerInput("How much would you like to contribute?", amount -> {
            if (amount > player.getInventory().getAmount(995)) {
                amount = player.getInventory().getAmount(995);
            }

            if (amount < 1) {
                return;
            }


            contributors.put(player.getName(), (long) amount + contributors.getOrDefault(player.getName(), 0L));
            player.getInventory().remove(995, amount);
            amounts[type.ordinal()] += amount;

            long hours = amounts[type.ordinal()] / type.cost;

            if (hours > 0) {
                amounts[type.ordinal()] -= hours * type.cost;

                if (expired(type)) {
                    if (type.equals(Type.DOUBLE_EXPERIENCE)) {
                        Broadcast.WORLD_NOTIFICATION.sendPlain("The Well of Goodwill is now granting everyone Double Experience!");
                        World.boostXp(2);
                        World.sendGraphics(1388, 130, 1, 3079, 3501, 0);
                    } else if (type.equals(Type.SLAYER_POINTS)) {
                        World.doubleSlayer = true;
                        Broadcast.WORLD_NOTIFICATION.sendPlain("The Well of Goodwill is now granting everyone Double Slayer points!");
                        World.sendGraphics(1388, 130, 1, 3079, 3501, 0);
                    } else if (type.equals(Type.DOUBLE_PEST_POINTS)) {
                        World.doublePest = true;
                        Broadcast.WORLD_NOTIFICATION.sendPlain("The Well of Goodwill is now granting everyone Double Pest points!");
                        World.sendGraphics(1388, 130, 1, 3079, 3501, 0);
                    } else if (type.equals(Type.DOUBLE_DROPS)) {
                        World.doubleDrops = true;
                        Broadcast.WORLD_NOTIFICATION.sendPlain("The Well of Goodwill is now granting everyone Double drops!");
                        World.sendGraphics(1388, 130, 1, 3079, 3501, 0);
                    }
                }
                extend(type, hours);
                save(type);
                updateInterface(player, type);
            }
            save(type);
            updateInterface(player, type);
        });
    }

    private static void save(Type type) {
        Server.gameDb.execute(connection -> {
//            String typeName = type.name().toLowerCase();
//            String amt = typeName.substring(0, typeName.indexOf("_")) + "amount";
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO wogw(type, expiry, amount) VALUES (?,?,?) ON DUPLICATE KEY UPDATE type=VALUES(type), expiry=VALUES(expiry), amount=VALUES(amount)")) {
                statement.setLong(1, type.ordinal());
                statement.setLong(2, expiry[type.ordinal()]);
                statement.setLong(3, amounts[type.ordinal()]);
                statement.execute();
            }
        });
    }

    public static void open(Player player) {
//        for (Type type : Type.values()) {
//            updateInterface(player, type);
//        }
        updateInterface(player, Type.values()[0]);
        player.openInterface(ToplevelComponent.MAINMODAL, WELL_OF_GOODWILL);
    }

    public static void updateInterface(Player player, Type type) {
        player.getPacketSender().sendString(WELL_OF_GOODWILL, 13, StringUtils.capitalize(StringUtils.replace(type.name().toLowerCase(), "_", " ")));
        String currentAmount = Misc.currencyLowercase(amounts[type.ordinal()]);
        String requiredAmount = Misc.currencyLowercase(type.cost);
        player.getPacketSender().sendString(WELL_OF_GOODWILL, 14, currentAmount + " / " + requiredAmount);
        String string = timeLeft(type);
        player.getPacketSender().sendClientScript(10531, WELL_OF_GOODWILL << 16 | 19, string.equalsIgnoreCase("Inactive") ? 0 : 1);
        player.getPacketSender().sendString(WELL_OF_GOODWILL, 20, string);
        updateDonators(player);
        int index = 57;
        for (Type value : Type.values()) {
            Long amo = amounts[value.ordinal()];
            int i = amo.intValue();
            Double d = (16354 * (i / (double) type.cost));
            player.getPacketSender().sendClientScript(10301, "ii", WELL_OF_GOODWILL << 16 | index + 4, d.intValue());
            player.getPacketSender().sendString(WELL_OF_GOODWILL, index, Misc.currencyLowercase(value.cost));
            player.getPacketSender().sendString(WELL_OF_GOODWILL, index + 5, Misc.currencyLowercase(amounts[value.ordinal()]) + " / " + Misc.currencyLowercase(value.cost));
            index = index + 11;
        }

//          boolean active = !expired(type);
//
//        long curr = active ? TimeUnit.MILLISECONDS.toMinutes(expiry[type.ordinal()] - System.currentTimeMillis()) : 0;
//        int max = 25;
//        int width = (int) ((double) curr / (double) max * (double) 20);
//        player.getPacketSender().sendClientScript(10301, "iii", Interface.WELL_OF_GOODWILL << 16 | 19, width, 16);
//        player.getPacketSender().sendString(Interface.WELL_OF_GOODWILL, 14, " " +  Misc.currency((amounts[type.ordinal()])));
//        player.getPacketSender().sendString(Interface.WELL_OF_GOODWILL, 58 + 6 * type.ordinal(), "<col=ffffff>" + timeLeft(type));
//        player.getPacketSender().sendString(Interface.WELL_OF_GOODWILL, 28 + 2 * type.ordinal(), Contributors + player.getName());
    }

    private static void updateDonators(Player player) {
        int j = 29;
        int size = contributors.size() - 1;
        List<String> contributorNames = new ArrayList<>();
        for (Map.Entry<String, Long> stringLongEntry : contributors.entrySet()) {
            if (stringLongEntry != null && stringLongEntry.getKey() != null)
                contributorNames.add(stringLongEntry.getKey());
        }
        for (int i = 0; i < 9; i++) {
            if (i > size) {
                player.getPacketSender().sendString(WELL_OF_GOODWILL, j++, "-");
                player.getPacketSender().sendString(WELL_OF_GOODWILL, j++, "-");
            } else {
                String string = contributorNames.get(i);
                player.getPacketSender().sendString(WELL_OF_GOODWILL, j++, string);
                player.getPacketSender().sendString(WELL_OF_GOODWILL, j++, Misc.currencyLowercase(contributors.get(string)));
            }
        }
    }

    public static String timeLeft(Type type) {
        if (expired(type)) {
            return "Inactive";
        }

        long remaining = expiry[type.ordinal()] - System.currentTimeMillis();

        long days = TimeUnit.MILLISECONDS.toDays(remaining);
        long hours = TimeUnit.MILLISECONDS.toHours(remaining);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining);

        if (hours > 0) {
            return String.format(
                    "%2d hours, %2d minutes",
                    hours - TimeUnit.DAYS.toHours(days),
                    minutes - TimeUnit.HOURS.toMinutes(hours)
            );
        }

        return String.format(
                "%2d minutes",
                minutes - TimeUnit.HOURS.toMinutes(hours)
        );
    }

    public static void register() {
//        ObjectAction.register(OBJECT_ID, "View", (player, obj) -> open(player));
        InterfaceHandler.register(WELL_OF_GOODWILL, h -> {
            h.actions[2] = (SimpleAction) p -> p.closeInterface(ToplevelComponent.MAINMODAL);
            h.actions[53] = (SimpleAction) p -> updateInterface(p, Type.DOUBLE_EXPERIENCE);
            h.actions[56] = (SimpleAction) p -> contribute(p, Type.DOUBLE_EXPERIENCE);

            h.actions[64] = (SimpleAction) p -> updateInterface(p, Type.SLAYER_POINTS);
            h.actions[67] = (SimpleAction) p -> contribute(p, Type.SLAYER_POINTS);

            h.actions[75] = (SimpleAction) p -> updateInterface(p, Type.DOUBLE_PEST_POINTS);
            h.actions[78] = (SimpleAction) p -> contribute(p, Type.DOUBLE_PEST_POINTS);

            h.actions[86] = (SimpleAction) p -> updateInterface(p, Type.DOUBLE_DROPS);
            h.actions[89] = (SimpleAction) p -> contribute(p, Type.DOUBLE_DROPS);
        });
    }

 */

}

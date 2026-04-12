//package io.ruin.model.inter.loyalty_program;
//
//import io.ruin.Server;
//import io.ruin.api.database.DatabaseUtils;
//import io.ruin.model.entity.player.Player;
//import io.ruin.model.item.Item;
//import io.ruin.utility.Misc;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.time.*;
//import java.util.*;
//
//public class VoteLottery {
//
//    public long end = -1;
//    public Item[] prizes = new Item[3];
//    public String[] top5Voters = new String[5];
//    public LinkedHashMap<String, Integer> ranked = new LinkedHashMap<>();
//
//    public VoteLottery() {
//        LocalDate today = LocalDate.now();
//        LocalDateTime sunday = today.atStartOfDay();
//        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
//            sunday = sunday.plusDays(1);
//        }
//        ZonedDateTime zdt = sunday.atZone(ZoneId.of("America/Los_Angeles"));
//        end = zdt.toInstant().toEpochMilli();
//
//        Server.gameDb.execute(connection -> {
//            PreparedStatement statement = null;
//            ResultSet rs = null;
//            try {
//                statement = connection.prepareStatement("SELECT * FROM community_vote_lottery WHERE ends = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//                statement.setLong(1,end);
//                rs = statement.executeQuery();
//                while (rs.next()) {
//                    if (!rs.getString("grand_prize").equals("null")) {
//                        String[] grandPrize = rs.getString("grand_prize").split("/");
//                        prizes[0] = new Item(Integer.parseInt(grandPrize[0]), Integer.parseInt(grandPrize[1]));
//                    }
//                    if (!rs.getString("second_prize").equals("null")) {
//                        String[] grandPrize = rs.getString("second_prize").split("/");
//                        prizes[1] = new Item(Integer.parseInt(grandPrize[0]), Integer.parseInt(grandPrize[1]));
//                    }
//                    if (!rs.getString("third_prize").equals("null")) {
//                        String[] grandPrize = rs.getString("third_prize").split("/");
//                        prizes[2] = new Item(Integer.parseInt(grandPrize[0]), Integer.parseInt(grandPrize[1]));
//                    }
//                    rs.updateRow();
//                }
//            } finally {
//                DatabaseUtils.close(statement, rs);
//            }
//        });
//    }
//
//    void update() {
//        Server.gameDb.executeAwait(con -> {
//            Statement statement = null;
//            ResultSet resultSet = null;
//            LocalDate today = LocalDate.now();
//            LocalDateTime monday = today.atStartOfDay();
//            int totalVotes = 0, thisWeek = 0;
//            while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
//                monday = monday.minusDays(1);
//            }
//            ZonedDateTime zdt = monday.atZone(ZoneId.of("America/Los_Angeles"));
//            Map<String, Integer> votes = new HashMap<>();
//            try {
//                statement = con.createStatement();
//                resultSet = statement.executeQuery("SELECT * FROM community_votes");
//                while(resultSet.next()) {
//                    if (resultSet.getTimestamp("entry_time").after(Date.from(zdt.toInstant()))) {
//                        String name = resultSet.getString("account_name").toLowerCase();
//                        int count = votes.get(name) == null ? 1 : votes.get(name) + 1;
//                        votes.put(name, count);
//                    }
//                }
//            } finally {
//                DatabaseUtils.close(statement, resultSet);
//                votes.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
//                        .forEachOrdered(x -> ranked.put(x.getKey(), x.getValue()));
//                Iterator<Map.Entry<String, Integer>> iterator = ranked.entrySet().iterator();
//                int idx = 0;
//                while (iterator.hasNext()) {
//                    Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iterator.next();
//                    if (idx < 5) {
//                        top5Voters[idx] = Misc.formatPlayerNameForDisplay(entry.getKey());
//                    }
//                    idx++;
//                }
//            }
//        });
//    }
//
//    public int getRank(Player player) {
//        Iterator<Map.Entry<String, Integer>> iterator = ranked.entrySet().iterator();
//        int idx = 0;
//        while (iterator.hasNext()) {
//            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iterator.next();
//            idx++;
//            if (entry.getKey().equalsIgnoreCase(player.getName())) {
//                return idx;
//            }
//        }
//        return 0;
//    }
//
//    public void save() {
//        Server.gameDb.execute(connection -> {
//            PreparedStatement statement = null;
//            ResultSet resultSet = null;
//            try {
//                statement = connection.prepareStatement("SELECT * FROM community_vote_lottery WHERE ends = ? LIMIT = 1", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//                statement.setLong(1, end);
//                resultSet = statement.executeQuery();
//                while (resultSet.next()) {
//                    String grandPrize = prizes[0] == null ? "null" : prizes[0].getId() + "/" + prizes[0].getAmount();
//                    String secondPrize = prizes[1] == null ? "null" : prizes[1].getId() + "/" + prizes[1].getAmount();
//                    String thirdPrize = prizes[2] == null ? "null" : prizes[2].getId() + "/" + prizes[2].getAmount();
//                    if (resultSet.next()) {
//                        Server.gameDb.execute(c -> {
//                            PreparedStatement s = null;
//                            ResultSet r = null;
//                            try {
//                                s = c.prepareStatement("UPDATE community_vote_lottery SET grand_prize = ?, second_prize = ?, third_prize = ? WHERE ends = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//                                s.setString(1, grandPrize);
//                                s.setString(2, secondPrize);
//                                s.setString(3, thirdPrize);
//                                s.setLong(4, end);
//                                r = s.executeQuery();
//                            } finally {
//                                DatabaseUtils.close(s, r);
//                            }
//                        });
//                    } else {
//                        Server.gameDb.execute(c -> {
//                            PreparedStatement s = null;
//                            ResultSet r = null;
//                            try {
//                                s = c.prepareStatement("INSERT INTO community_vote_lottery ROWS(`grand_prize`,`second_prize`,`third_prize`,`end`) VALUES(?,?,?,?)", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//                                s.setString(1, grandPrize);
//                                s.setString(2, secondPrize);
//                                s.setString(3, thirdPrize);
//                                s.setLong(4, end);
//                                r = s.executeQuery();
//                            } finally {
//                                DatabaseUtils.close(s, r);
//                            }
//                        });
//                    }
//                    resultSet.updateRow();
//                }
//            } finally {
//                DatabaseUtils.close(statement, resultSet);
//            }
//        });
//    }
//}

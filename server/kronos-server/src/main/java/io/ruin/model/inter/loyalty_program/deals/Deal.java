//package io.ruin.model.inter.loyalty_program.deals;
//
//import io.ruin.model.entity.player.PlayerGroup;
//import io.ruin.model.inter.loyalty_program.LoyaltyProgram;
//import io.ruin.model.item.Item;
//import io.ruin.services.StoreItems;
//import io.ruin.utility.Misc;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class Deal extends Item {
//
//    private PlayerGroup rank;
//
//    private StoreItems reward, requisite;
//
//    private String title, icon = "", selectedOption = "Select an Option", rewardName = "";
//
//    private int slot, model, sprite = -1;
//
//    private int quantity;
//
//    private long end;
//
//    private DealCategories category;
//
//    private boolean spendThreshold;
//
//    private boolean paused = false;
//
//    private int days = 0, hours = 1, minutes = 0, usdThreshold;
//
//    public Deal(int id, int count, long end, DealCategories category, int slot) {
//        super(id, count);
//        this.end = end;
//        this.category = category;
//        this.slot = slot;
//    }
//
//    public static void saveAll() {
//        String cleared = clearDeals();
//        if (cleared != null) {
//            for (Deal d : LoyaltyProgram.getDeals()) {
//                if (!d.isActive() || d.isPaused()) {
//                    continue;
//                }
//                persistDeal(d);
//            }
//        }
//    }
//
//    private static String clearDeals() {
//        String line = null;
//        try {
//            URL tmp = new URL("http://localhost:43592/api/v1/loyalty/clear-deals?code=3GSD4f56hgs23g1fr3wr54gw314FD3GSDFqTH3FDG5f");
//            HttpURLConnection conn = (HttpURLConnection) tmp.openConnection();
//            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
//            conn.setDefaultUseCaches( false );
//            conn.setReadTimeout(3000);
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            line = br.readLine();
//            String temp = "";
//            while((temp = br.readLine()) != null) {
//                line = line + " " + temp;
//            }
//            conn.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return line.contains("done") ? line : null;
//    }
//
//    private static boolean persistDeal(Deal d) {
//        String line = null;
//        try {
//            URL tmp = new URL("http://localhost:43592/api/v1/loyalty/create-deal?code=3GSD4f56hgs23g1fr3wr54gw314FD3GSDFqTH3FDG5f&title=" + d.getTitle().replaceAll(" ", "_") + "&reward=" + d.rewardName.replaceAll(" ", "_") + "&requisite=" + d.requisite.name() + "&spend_threshold=" + (d.spendThreshold ? d.usdThreshold : 0) + "&quantity=" + d.quantity + "&end=" + ((d.end - System.currentTimeMillis()) / 1000));
//            HttpURLConnection conn = (HttpURLConnection) tmp.openConnection();
//            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
//            conn.setDefaultUseCaches( false );
//            conn.setReadTimeout(3000);
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            line = br.readLine();
//            String temp = "";
//            while((temp = br.readLine()) != null) {
//                line = line + " " + temp;
//            }
//            conn.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return line.contains("done");
//    }
//
//
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof Deal)) {
//            return false;
//        }
//        Deal other = (Deal) o;
//        if (other.slot == this.slot && other.category == this.category) {
//            return true;
//        }
//        return false;
//    }
//
//    public boolean isActive() {
//        System.out.println(title + " Active check -> end: " + (end > System.currentTimeMillis()) + " paused: " + paused);
//        return end > System.currentTimeMillis() && !paused;
//    }
//
//    public DealCategories getCategory() {
//        return category;
//    }
//
//    public void setCategory(DealCategories category) {
//        this.category = category;
//    }
//
//    public long getDelta() {
//        return end - System.currentTimeMillis();
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//        setAmount(quantity);
//    }
//
//    public int getSlot() {
//        return slot;
//    }
//
//    public void setSlot(int slot) {
//        this.slot = slot;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public int getModel() {
//        return model;
//    }
//
//    public void setModel(int model) {
//        this.model = model;
//    }
//
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public long getEnd() {
//        return end;
//    }
//
//    public boolean isSpendThreshold() {
//        return spendThreshold;
//    }
//
//    public void setSpendThreshold(boolean spendThreshold) {
//        this.spendThreshold = spendThreshold;
//    }
//
//    public int getDays() {
//        return days;
//    }
//
//    public void setDays(int days) {
//        this.days = days;
//    }
//
//    public int getHours() {
//        return hours;
//    }
//
//    public void setHours(int hours) {
//        this.hours = hours;
//    }
//
//    public int getMinutes() {
//        return minutes;
//    }
//
//    public void setMinutes(int minutes) {
//        this.minutes = minutes;
//    }
//
//    public int getUsdThreshold() {
//        return usdThreshold;
//    }
//
//    public void setUsdThreshold(int usdThreshold) {
//        this.usdThreshold = usdThreshold;
//    }
//
//    public StoreItems getReward() {
//        return reward;
//    }
//
//    public void setReward(StoreItems reward) {
//        this.reward = reward;
//        setId(reward.getReward());
//        this.model = getId();
//        this.rank = null;
//        selectedOption = rewardName =  Misc.formatPlayerNameForDisplay(reward.name().replaceAll("_", " "));
//    }
//
//    public void setReward(PlayerGroup rank) {
//        this.rank = rank;
//        this.icon = "<img=" + rank.clientImgId + ">";
//        this.reward = null;
//        selectedOption = "($10) " + rank.title;
//        rewardName = rank.title;
//    }
//
//    public void setReward(int cosmetic) {
////        this.rank = rank;
////        this.icon = "<img=" + rank.getSpriteIndex() + ">";
////        selectedOption = "($" + Utils.formatNumber((int)rank.getSuccessBuff()) + ") " + rank.getTitle();
//    }
//
//    public void setEnd(long l) {
//        this.end = l;
//    }
//
//    public boolean isPaused() {
//        return paused;
//    }
//
//    public void setPaused(boolean paused) {
//        this.paused = paused;
//    }
//
//    public int getSprite() {
//        return sprite;
//    }
//
//    public void setSprite(int sprite) {
//        this.sprite = sprite;
//    }
//
//    public String getIcon() {
//        return icon;
//    }
//
//    public void setIcon(String icon) {
//        this.icon = icon;
//    }
//
//    public String getSelectedOption() {
//        return selectedOption;
//    }
//
//    public void setSelectedOption(String selectedOption) {
//        this.selectedOption = selectedOption;
//    }
//
//    public StoreItems getRequisite() {
//        return requisite;
//    }
//
//    public void setRequisite(StoreItems requisite) {
//        this.requisite = requisite;
//    }
//
//    public String getRewardName() {
//        return rewardName;
//    }
//
//    public void setRewardName(String rewardName) {
//        this.rewardName = rewardName;
//    }
//
//    public PlayerGroup getRank() {
//        return rank;
//    }
//
//    public void setRank(PlayerGroup rank) {
//        this.rank = rank;
//    }
//}

package inter.ikod.hook;

import inter.ikod.IKOD;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.questtab.toggles.RiskProtection;
import io.ruin.model.item.Item;

import java.util.ArrayList;

public class RiskProtectionHook implements RiskProtection.Hook {

	public static void register() {
		RiskProtection.hook = new RiskProtectionHook();
	}

	@Override
	public int getKeepCount(boolean skulled, boolean ultimateIronMan, boolean highRiskSkull) {
		return IKOD.getKeepCount(skulled, ultimateIronMan, highRiskSkull);
	}

	@Override
	public ArrayList<Item> itemsSortedByProtectValue(Player player) {
		return IKOD.itemsSortedByProtectValue(player);
	}

}

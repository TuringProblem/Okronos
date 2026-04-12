package tormenteddemon.combat.util;

import io.ruin.cache.ItemID;
import io.ruin.model.item.Item;

import java.util.List;

public class WeaponChecker {
	static List<Integer> demonbaneWeapons = List.of(
		ItemID.SILVERLIGHT, // Abyssal whip
			ItemID.DARKLIGHT, // Abyssal tentacle
			ItemID.ARCLIGHT, // Abyssal dagger
			29589, //Emberlight
			29577, //Burning claws
			29591,
			29594  //Purging staff
	);
	public static boolean isDemonbaneOrAbyssal(Item item) {
		if(item.getDef().name.toLowerCase().contains("abyssal"))
			return true;
		if(demonbaneWeapons.contains(item.getId()))
			return true;
		return false;
	}
}

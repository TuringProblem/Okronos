package npc.nex.old;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.Item;

import java.util.Arrays;

/**
 * @author R-Y-M-R
 * @date 3/15/2022
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 */
// https://oldschool.runescape.wiki/w/Zaros#Affiliated_items
public class ZarosItems {
	/**
	 * Checks if the player has any Zaros items.
	 *
	 * @param i
	 * @return
	 */
	public static boolean isZaros(Item i) {
		if (i == null) {
			return false;
		}
		if (i.getDef() == null) {
			return false;
		}
		if (i.getDef().name == null) {
			return false;
		}
		String name = i.getDef().name.toLowerCase();
		if (name.contains("ancient"))
			return true;
		if (name.contains("torva"))
			return true;
		if (name.contains("zaryte"))
			return true;
		if (name.contains("of darkness"))
			return true;
        /*if (name.contains("zaros")) // at the time of writing, no items in the game have "zaros" in the name.
            return true;*/
		return false;
	}

	/**
	 * Has zaros item on the player in inv or gear
	 *
	 * @param player
	 * @return
	 */
	public static boolean hasZarosItems(Player player) {
		final boolean zarosItems = Arrays.stream(player.getInventory().getItems()).anyMatch(ZarosItems::isZaros);
		if (zarosItems)
			return true;
		final boolean zarosEquip = Arrays.stream(player.getEquipment().getItems()).anyMatch(ZarosItems::isZaros);
		if (zarosEquip)
			return true;
		return false;
	}
}

package npc.nex.old;

import discord.webhooks.notifications.RareDropHook;
import io.ruin.api.utils.NumberUtils;
import io.ruin.api.utils.Random;
import io.ruin.cache.Color;
import io.ruin.cache.Icon;
import io.ruin.cache.NPCType;
import io.ruin.model.World;
import npc.nex.modes.Phase;
import npc.nex.utils.NexDropI;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.TheSoulsplit;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.combat.Killer;
import io.ruin.model.content.combatachievements.CombatAchievements;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.inter.handlers.EquipmentStats;
import io.ruin.model.item.Item;
import io.ruin.model.map.*;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.object.actions.ObjectAction;
import io.ruin.model.map.route.routes.ProjectileRoute;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.utility.Broadcast;
import io.ruin.utility.Common;
import io.ruin.utility.Misc;
import io.ruin.utility.Utils;
import org.json.JSONObject;
import npc.nex.attacks.Attack;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * This class contains the Nex combat script. For Nex utilities, see NexUtils
 * <p>
 * Last updated to Nex v1.2 by Riley on 6/8/2022.
 * <p>
 * Changes: 1.1: Fixling drop issue 1.2: Change drop mechanics to drop loot to the entire group
 *
 * @author R-Y-M-R
 * @version 1.2
 * @see <a href="https://www.rune-server.ee/members/necrotic/">RuneServer</a>
 * @since 2/25/2022
 */
public class Nex {

	public static class Forms {
		static final int DEFAULT_NEX = 11278;
		static final int UNATTACKABLE_NEX = 11279;
		static final int SOUL_SPLIT_NEX = 11280;
		static final int REFLECT_MELEE_NEX = 11281;
		static final int WRATH_NEX = 11282;

		static boolean isNex(int npcId) {
			if (npcId == DEFAULT_NEX)
				return true;
			if (npcId == SOUL_SPLIT_NEX) {
				return true;
			}
			if (npcId == REFLECT_MELEE_NEX) {
				return true;
			}
			if (npcId == WRATH_NEX) {
				return true;
			}
			if (npcId == UNATTACKABLE_NEX) {
				return true;
			}
			return false;
		}
	}

	public static final Bounds NEX_BOUNDS = new Bounds(2909, 5187, 2941, 5220, 0);

	public static void register() {
		NPCType def1 = NPCType.get(Forms.DEFAULT_NEX);
		def1.ignoreMultiCheck = true;

		NPCType def2 = NPCType.get(Forms.UNATTACKABLE_NEX);
		def2.ignoreMultiCheck = true;

		NPCType def3 = NPCType.get(Forms.SOUL_SPLIT_NEX);
		def3.ignoreMultiCheck = true;

		NPCType def4 = NPCType.get(Forms.WRATH_NEX);
		def4.ignoreMultiCheck = true;

		NPCType def5 = NPCType.get(Forms.REFLECT_MELEE_NEX);
		def5.ignoreMultiCheck = true;

		ObjectAction.register(Attack.ATTACKABLE_STALAGMITE, "attack", (player, obj) -> {
			var simulatedAtkDmg = player.getCombat().simulateAttackWithMelee(true, true, true, AttackStyle.CRUSH,
					Attack.STALAGMITE_BONUS_DAMAGE);
			if (simulatedAtkDmg > -1) {
				player.lock();
				player.addEvent(event -> {
					event.setCancelCondition(() -> player.currentDynamicMap != null);
					if (simulatedAtkDmg >= Attack.STALAGMITE_MIN_DAMAGE) {
						event.delay(1);
						player.sendFilteredMessage("Your hit smashes the stalagmite to pieces!");
						if (!obj.isRemoved())
							obj.remove();
					} else
						player.sendFilteredMessage(
								"Your hit " + Color.DARK_RED.wrap("wasn't") + " hard enough to shatter the Stalagmite.");

					player.unlock();
				});
			}
		});
	}
}

package io.ruin.data.impl.npcs;

import com.google.gson.annotations.Expose;
import io.ruin.api.utils.FileUtils;
import io.ruin.api.utils.JsonUtils;
import io.ruin.api.utils.ServerWrapper;
import io.ruin.api.utils.StringUtils;
import io.ruin.api.utils.ThreadUtils;
import io.ruin.cache.NPCType;
import io.ruin.data.DataFile;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.item.actions.impl.pet.Pet;
import lombok.ToString;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class npc_combat extends DataFile {

	@Override
	public String path() {
		return "npcs/combat/*.json";
	}

	@Override
	public int priority() {
		return 5;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object fromJson(String fileName, String json) {
		List<Info> list = JsonUtils.fromJson(json, List.class, Info.class);
		list.forEach(info -> {
			for (int id : info.ids) {
				NPCType def = NPCType.get(id);
				if (def == null) continue;

				def.combatInfo = info;

				if (info.handler != null && !info.handler.isEmpty()) {
					try {
						def.combatHandlerClass = (Class<? extends NPCCombat>) Class.forName(info.handler);
					} catch (ClassNotFoundException e) {
						//just in case someone hasn't commit, we'll print a little warning and move on!
						System.err.println("Warning, combat handler class not found: " + info.handler + " for npc #" + id + " \"" + def.name + "\"!");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});


		return list;
	}

	@ToString
	public static final class Info {

		@Expose
		public int[] ids;

		@Expose
		public String handler;

		@Expose
		public Pet pet;

		/**
		 * Combat Info
		 */

		@Expose
		public int hitpoints;

		@Expose
		public int aggressive_level;

		@Expose
		public int max_damage;

		@Expose
		public AttackStyle attack_style;

		/**
		 * Slayer Info
		 */

		@Expose
		public int slayer_level;

		@Expose
		public double slayer_xp;

		@Expose
		public String[] slayer_tasks;

		/**
		 * Combat Stats
		 */

		@Expose
		public int attack, strength, defence, ranged, magic;

		/**
		 * Aggressive Stats
		 */

		@Expose
		public int stab_attack, slash_attack, crush_attack, magic_attack, ranged_attack;

		/**
		 * Defensive Stats
		 */

		@Expose
		public int stab_defence, slash_defence, crush_defence, magic_defence, ranged_defence;

		/**
		 * Immunities
		 */

		@Expose
		public boolean poison_immunity, venom_immunity;

		/**
		 * Speeds
		 */

		@Expose
		public int attack_ticks, death_ticks, respawn_ticks;

		/**
		 * Anims
		 */

		@Expose
		public int spawn_animation, attack_animation, defend_animation, death_animation;

		/**
		 * Sounds
		 */

		@Expose
		public Integer attack_sound, defend_sound, death_sound;

		/**
		 * Broadcast loot to all nearby players
		 */
		@Expose
		public boolean local_loot = false;

		/**
		 * Combat XP modifier.
		 * Combat XP earned through hitting this monster will be multiplied by this value
		 */

		@Expose
		public double combat_xp_modifier = 1.0;

		@Expose
		public int random_drop_count = 1;

	}

	/**
	 * Dump combat info from wiki..
	 */

	public static void dump(String wikiName) {
		new WikiDumper(wikiName).run();
	}

	public static void dumpAll() {
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY); //will this help speed this along??

		Set<String> dumped = new HashSet<>();
		AtomicLong done = new AtomicLong(0);
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(FileUtils.get("%HOME%/Dropbox/Runite/Server/data/wiki_dumps/combat/temp/~FAILED.txt")))) {
			bw.write("# id     name     combat");
			bw.newLine();
			NPCType.forEach(def -> {
				if (def.combatLevel == 0)
					return;
				String name = def.name.trim();
				if (name.isEmpty() || name.equalsIgnoreCase("null"))
					return;
				/**
				 * Try to dump if an npc with the given name/combat hasn't already been dumped..
				 */
				String dumpKey = name.toLowerCase() + "-" + def.combatLevel;
				if (!dumped.contains(dumpKey)) {
					//tried to think of every potential wiki url name..
					//which makes this entire method execute VERY slow because it uses connections..
					String[] names = {
						name + def.combatLevel,
						name + "(" + def.combatLevel + ")",

						name + " " + def.combatLevel,
						name + " " + "(" + def.combatLevel + ")",

						name.replace(" ", "_") + def.combatLevel,
						name.replace(" ", "_") + "(" + def.combatLevel + ")",

						name.replace(" ", "_") + "_" + def.combatLevel,
						name.replace(" ", "_") + "_" + "(" + def.combatLevel + ")",

						name + " " + "(monster)",
						name + "_" + "(monster)",

						name + " " + "(Monster)",
						name + "_" + "(Monster)",

						name + " " + "(common)",
						name + "_" + "(common)",

						name + " " + "(Common)",
						name + "_" + "(Common)",

						name.replace(" ", "_"),
						name
					};
					for (String n : names) {
						WikiDumper d = new WikiDumper(n).run();
						if (!d.success)
							d = new WikiDumper(n.toLowerCase()).run();
						if (!d.success)
							d = new WikiDumper(StringUtils.fixCaps(n.toUpperCase())).run();
						if (!d.success && n.contains("(")) {
							String s = n.replace("(", "[").replace(")", "]");
							d = new WikiDumper(s).run();
							if (!d.success)
								d = new WikiDumper(s.toLowerCase()).run();
							if (!d.success)
								d = new WikiDumper(StringUtils.fixCaps(s.toUpperCase())).run();
						}
						if (d.success) {
							dumped.add(dumpKey);
							break;
						}
					}
				}
				/**
				 * Every attempt to dump failed, let's log it..
				 */
				if (!dumped.contains(dumpKey)) {
					try {
						bw.write(def.id + "     " + def.name + "     " + def.combatLevel);
						bw.newLine();
					} catch (IOException e) {
						/* ignored */
					}
				}
				/**
				 * This is to follow progress, lol!
				 */
				System.err.println(done.incrementAndGet());
			});
		} catch (Exception e) {
			//This really shouldn't ever even happen..
			ServerWrapper.logError("Failed to dump", e);
		}
		System.exit(1);
	}

	public static final class WikiDumper {

		private String wikiName;

		private boolean success;

		public WikiDumper(String wikiName) {
			this.wikiName = wikiName;
		}

		public WikiDumper run() {
			File file = null;
			try {
				Connection.Response response = Jsoup.connect("http://oldschoolrunescape.wikia.com/wiki/" + URLEncoder.encode(wikiName, "UTF-8"))
					.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
					.referrer("http://www.google.com")
					.timeout(12000)
					.execute();
				String url = response.url().toString();
				wikiName = URLDecoder.decode(url.substring(url.lastIndexOf("/") + 1), "UTF-8");
				//^^ prevents multiple files for redirects ^^
				Document doc = response.parse();
				if (doc == null)
					throw new IOException("Failed to connect to wiki page!");
				Elements infos = doc.getElementsByClass("wikitable infobox");
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(file = FileUtils.get("D:/Dropbox/Vorkath/Server/data/wiki_dumps/combat/temp/" + wikiName + ".txt")))) {
					int count = 0;
					for (Element info : infos) {
						String name = info.child(0).text();
						Element body = info.child(1);

						int bodyOffset = 0;

						if (body.child(1).text().toLowerCase().startsWith("also called"))
							bodyOffset++;

						Element combatLevel = body.child(3 + bodyOffset);
						Elements combatInfo = body.child(7 + bodyOffset).select("td > table > tbody > tr");

						Element hitpoints = combatInfo.get(1);
						Element aggressive = combatInfo.get(2);
						Element poisonous = combatInfo.get(3);
						Element maxHit = combatInfo.get(4);
						Element attackStyles = combatInfo.get(7);

						Element slayerLevel = combatInfo.get(9);
						Element slayerXp = null, slayerCat = null;
						int skip = 4;
						if (!slayerLevel.text().equalsIgnoreCase("not assigned")) {
							slayerXp = combatInfo.get(10);
							slayerCat = combatInfo.get(11);
							skip = 0;
						}

						Element combatStats = combatInfo.get(16 - skip);
						Element aggressiveStats = combatInfo.get(19 - skip);
						Element defensiveStats = combatInfo.get(22 - skip);
						Element other = combatInfo.get(25 - skip);
						Element atkSpeed = combatInfo.get(27 - skip);

						if (count != 0) {
							bw.newLine();
							bw.newLine();
						}
						bw.write(name);
						bw.newLine();
						bw.write(combatLevel.text());
						bw.newLine();
						bw.write(hitpoints.text());
						bw.newLine();
						bw.write(aggressive.text());
						bw.newLine();
						bw.write(poisonous.text());
						bw.newLine();
						bw.write(maxHit.text());
						bw.newLine();
						bw.write(attackStyles.text());
						bw.newLine();
						bw.write(slayerLevel.text());
						bw.newLine();
						bw.write(slayerXp == null ? "null" : slayerXp.text());
						bw.newLine();
						bw.write(slayerCat == null ? "null" : slayerCat.text());
						bw.newLine();
						bw.write(combatStats.text());
						bw.newLine();
						bw.write(aggressiveStats.text());
						bw.newLine();
						bw.write(defensiveStats.text());
						bw.newLine();
						bw.write(other.text());
						bw.newLine();
						try {
							Element img = atkSpeed.select("img").get(1);
							String src = img.absUrl("src");
							if (src == null || src.isEmpty())
								src = img.attr("src");
							if (src == null || src.isEmpty())
								throw new IOException("Failed to find img src!");
							bw.write(src);
						} catch (Exception e) {
							String text = atkSpeed.text();
							if (text == null || text.isEmpty())
								text = "Unknown";
							bw.write(text);
						}
						String[] check = {
							name,
							combatLevel.text(),
							hitpoints.text(),
							aggressive.text(),
							poisonous.text(),
							maxHit.text(),
							slayerLevel.text(),
							slayerXp == null ? "" : slayerXp.text(),
							slayerCat == null ? "" : slayerCat.text(),
						};
						for (String s : check) {
							if (s.contains(",")) {
								System.err.println("Multiple legacy entries for: \"" + wikiName + "\"");
								break;
							}
						}
						count++;
					}
				}
				success = true;
				return this;
			} catch (Exception e) {
				success = false;
				if (file != null && file.exists()) {
					//failed so if there's a file delete it!
					if (!file.delete())
						file.deleteOnExit();
				}
				if (e.getMessage().contains("HTTP error fetching URL")) {
					//uhh, page does not exist??
					return this;
				}
				if (e.getMessage().contains("timed out")) {
					//timed out, try again!
					ThreadUtils.sleep(10L);
					return run();
				}
				ServerWrapper.logError("Error while parsing: " + wikiName, e);
				return this;
			}
		}
	}
}
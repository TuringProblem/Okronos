package io.ruin.model.content.itembreaking;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.item.Item;
import io.ruin.model.item.attributes.AttributeExtensions;
import io.ruin.model.item.attributes.AttributeTypes;
import io.ruin.model.item.containers.Equipment;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.map.Tile;
import io.ruin.model.stat.StatType;

import java.util.ArrayList;
import java.util.List;

public class ItemBreakPerkHandler {
	public static void handleQuickStepPerk(Player player, double experience) {
		if (player.getEquipment().get(Equipment.SLOT_FEET) != null &&
			AttributeExtensions.hasAttribute(player.getEquipment().get(Equipment.SLOT_FEET), AttributeTypes.QUICK_STEP)) {
			int level = AttributeExtensions.getCharges(AttributeTypes.QUICK_STEP, player.getEquipment().get(Equipment.SLOT_FEET));
			if (level > 0) {
				if (experience > 500)
					experience = 500;
				double multiplier = 0.1 + (level * 0.1);
				int chance = 20 - (level * 3);
				int agilityExperience = (int) (experience * multiplier);
				if (Random.get(chance) == 0)
					player.getStats().addXp(StatType.Agility, agilityExperience, false);
			}
		}
	}

	public static void handleSpectralGuardian(Player player, Item item) {
		if (player.wildernessLevel > 0 || player.getPosition().getRegion().id == 12342)
			return;

		int level = AttributeExtensions.getCharges(AttributeTypes.SPECTRAL_GUARDIAN, item);
		int chance = 150 - (level * 10);
		if (Random.get(chance) == 0 && player.spectralGuardianDelay.remaining() < 1) {
			player.spectralGuardianDelay.delay(500 - (level * 50));
			Position spawnPosition = null;
			Position playerPosition = player.getPosition().copy();
			for (int dx = -3; dx <= 3; dx++) {
				for (int dy = -3; dy <= 3; dy++) {
					Position potentialPosition = playerPosition.copy().translate(dx, dy, 0);
					if (!potentialPosition.equals(playerPosition) && Tile.get(potentialPosition, true).clipping == 0) {
						spawnPosition = potentialPosition;
						break;
					}
				}
				if (spawnPosition != null) {
					break;
				}
			}
			if (spawnPosition == null) {
				return;
			}

			NPC spectralGuardian = new NPC(11413).spawn(spawnPosition);
			int heals = 3 + (level * 3);
			World.startEvent(e -> {
				spectralGuardian.animate(9380);
				e.delay(2);
				spectralGuardian.animate(-1);
				for (int j = 0; j < heals; j++) {
					int delay = 0;
					List<Player> playersToHeal = new ArrayList<>();
					int MAX_HEALS = 25;
					spectralGuardian.animate(9383);
					spectralGuardian.face(player);
					e.delay(1);
					for (Player p : spectralGuardian.localPlayers()) {
						delay = new Projectile(1483, 22, 43, 35, 40, 6, 16, 192).send(spectralGuardian, p);
						playersToHeal.add(p);
						if (playersToHeal.size() >= MAX_HEALS)
							break;
					}
					e.delay(World.getTicks(delay) + 1);
					for (Player p : playersToHeal) {
						if (p != null)
							p.hit(new Hit(HitType.HEAL).fixedDamage(level * 5));
					}
					if (j == heals - 1) {
						spectralGuardian.animate(9381);
						e.delay(2);
						spectralGuardian.remove();
					}

				}
			});
		}
	}

	public static void handleWeatherWizard(Player player, NPC target) {
		if (player.weatherWizardDelay.remaining() > 0)
			return;
		int level = AttributeExtensions.getCharges(AttributeTypes.WEATHER_WIZARD, player.getEquipment().get(Equipment.SLOT_WEAPON));
		player.weatherWizardDelay.delay(500 - (level * 50));
		Position spawnPosition = null;
		while (spawnPosition == null) {
			spawnPosition = new Position(Random.get(target.getPosition().getX() - 3, target.getPosition().getX() + 3), Random.get(target.getPosition().getY() - 3, target.getPosition().getY() + 3), target.getPosition().getZ());
			if (Tile.get(spawnPosition, true).clipping != 0)
				spawnPosition = null;
		}

		NPC tornado = new NPC(4593).spawn(spawnPosition);
		int ticks = 8 + (level * 4);
		int endLoop = ticks - 1;
		World.startEvent(e -> {
			for (int i = 0; i < ticks; i++) {
				tornado.step(Random.get(-1, 1), Random.get(-1, 1), StepType.RUN);
				tornado.graphics(1608);
				tornado.localNpcs().forEach(p -> {
					if (tornado.getPosition().distance(p.getPosition()) < 1)
						p.hit(new Hit(HitType.DAMAGE).randDamage(level * 35, level * 50));
				});
				e.delay(1);
				if (i == endLoop) {
					tornado.remove();
				}


			}
		});
	}

	public static List<Position> getTriangleTiles(Player player, int levels, Direction direction) {
		List<Position> tiles = new ArrayList<>();
		Position startTile = player.getPosition().copy();
		int dx = direction.deltaX;
		int dy = direction.deltaY;

		for (int level = 1; level <= levels; level++) {
			int baseX = startTile.getX() + level * dx;
			int baseY = startTile.getY() + level * dy;

			for (int offset = -level; offset <= level; offset++) {
				// Handle diagonal directions
				if (dx != 0 && dy != 0) {
					// Diagonal directions (NE, NW, SE, SW)
					if (dx == dy) {
						// Southeast or Northwest
						tiles.add(new Position(baseX + offset, baseY + offset));
					} else {
						// Northeast or Southwest
						tiles.add(new Position(baseX + offset, baseY - offset));
					}
				} else if (dx != 0) {
					// Horizontal directions (E, W)
					tiles.add(new Position(baseX, baseY + offset));
				} else if (dy != 0) {
					// Vertical directions (N, S)
					tiles.add(new Position(baseX + offset, baseY));
				}
			}
		}

		return tiles;
	}

	public static List<Position> getDiagonalTriangleTiles(Player player, int levels, Direction direction) {
		List<Position> tiles = new ArrayList<>();
		Position startTile = player.getPosition().copy();
		int dx = direction.deltaX;
		int dy = direction.deltaY;

		for (int level = 1; level <= levels; level++) {
			for (int offset = -level; offset <= level; offset++) {
				int baseX = startTile.getX() + level * dx;
				int baseY = startTile.getY() + level * dy;

				// Offset for diagonal direction to form a triangle
				int offsetX = baseX + offset * dx;
				int offsetY = baseY + offset * dy;

				tiles.add(new Position(offsetX, offsetY));
			}
		}

		return tiles;
	}


	public static void handleDragonWrath(Player player, NPC target) {
		//if(player.dragonWrathDelay.remaining() > 0)
		// return;
		int level = AttributeExtensions.getCharges(AttributeTypes.DRAGON_WRATH, player.getEquipment().get(Equipment.SLOT_WEAPON));
		player.dragonWrathDelay.delay(500 - (level * 50));
		Direction direction = Direction.getDirection(player.getPosition(), target.getPosition());
		List<Position> coneTiles = new ArrayList<>();
		if (direction == Direction.NORTH || direction == Direction.SOUTH || direction == Direction.EAST || direction == Direction.WEST)
			coneTiles = getTriangleTiles(player, level * 2, direction);
		else coneTiles = getDiagonalTriangleTiles(player, level * 2, direction);
		coneTiles.forEach(pos -> {
			World.sendGraphics(78, 0, 0, pos);
			for (NPC npc : target.localNpcs()) {
				if (npc.getPosition().distance(pos) < 1) {
					npc.hit(new Hit().randDamage(level * 75, level * 145));
				}
			}
		});
	}

	public static double handleEnhancedSoak(Player player) {
		double soak = 0;
		for (Item item : player.getEquipment().getItems()) {
			if (item == null || !AttributeExtensions.hasAttribute(item, AttributeTypes.ENHANCED_SOAK))
				continue;
			int level = AttributeExtensions.getCharges(AttributeTypes.ENHANCED_SOAK, item);
			soak += (level * 0.01);
		}
		return soak;
	}
}

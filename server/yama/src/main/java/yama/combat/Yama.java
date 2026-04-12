package yama.combat;


import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.item.actions.impl.pet.Pet;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.utility.Random;
import io.ruin.utility.TickDelay;
import lombok.Getter;
import lombok.Setter;
import yama.combat.attacks.MeleeAttack;
import yama.combat.hazards.FireStreaks;
import yama.combat.hazards.ShadowWaves;
import yama.combat.hazards.VoidFlares;
import yama.combat.specials.*;
import yama.combat.util.AttackStyle;
import yama.combat.util.Direction;
import yama.combat.util.Hazard;
import yama.combat.util.Phase;

import java.util.*;

public class Yama extends NPCCombat {
	@Getter List<Glyph> glyphs = new ArrayList<>();

	AttackStyle attackStyle;
	int attacksUntilSpecial = 1;
	public Phase phase;
	int specialCycle = 0;

	@Getter boolean inIntermission = false;

	TickDelay shadowCrashDelay = new TickDelay();
	TickDelay voidFlareDelay = new TickDelay();
	TickDelay hazardDelay = new TickDelay();
	List<Hazard> hazards = new ArrayList<>();

	@Getter @Setter
	private int shadowImmunityTicks = 0;

	@Getter @Setter
	private int fireImmunityTicks = 0;

	@Getter @Setter
	private boolean canAttack = true;

	private boolean dead = false;

	@Getter private final Map<Direction, Integer> occupiedVoidflarePositions = new EnumMap<>(Direction.class);



	@Override
	public void init() {
		List<Position> positions = glyphPositions();
		for(int i = 0; i < 5; i++) {
			GameObject glyphGameObject = Tile.getObject(56337, positions.get(i).x(), positions.get(i).y(), positions.get(i).plane());
			if(glyphGameObject != null) {
				Glyph glyph = new Glyph(glyphGameObject, npc);
				glyphs.add(glyph);
			} else {
				System.out.println("Yama glyph object not found at position: " + positions.get(i));
			}
		}
		npc.attackNpcListener = (player, npc1, message) -> !inIntermission;
		setInitialAttackStyle();
		phase = Phase.ONE;
		hazardDelay.delay(10);
		hazards.add(new FireStreaks());
		hazards.add(new ShadowWaves());
		hazards.add(new VoidFlares());
		npc.hitsUpdate.hpbarId = 22;
		npc.hitListener = new HitListener()
			.postDamage(hit -> {
				if (hit.attacker != null && hit.attacker.isPlayer()) {
					if (!hit.attacker.player.getHealthHud().isOpened())
						hit.attacker.player.getHealthHud().open(true, npc.getId(), npc.getMaxHp());
					hit.attacker.player.getHealthHud().updateValue(npc.getHp());
				}
			}).preDamage(this::preDamage);
	}

	public void endIntermission() {
		World.startEvent(e -> {
			npc.getPosition().getRegion().players.forEach(p -> p.graphics(3275));
			e.delay(1);
			npc.getPosition().getRegion().players.forEach(p -> p.animate(12149));
			e.delay(1);
			npc.getPosition().getRegion().players.forEach(p -> p.animate(-1));
			e.delay(1);
			if(npc.getPosition().getRegion().players.size() > 1) {
				for (int i = 0; i < 2; i++) {
					Player p = npc.getPosition().getRegion().players.get(i);
					p.getHealthHud().open(true, npc.getId(), npc.getMaxHp());
					p.getMovement().teleport(endIntermissionPosition().get(i));
					p.animate(12152);
				}
			} else {
				Player p = npc.getPosition().getRegion().players.getFirst();
				p.getHealthHud().open(true, npc.getId(), npc.getMaxHp());
				p.getMovement().teleport(endIntermissionPosition().getFirst());
				p.animate(12152);
			}
			e.delay(1);
			npc.setHidden(false);
			npc.animate(npc.getCombat().getInfo().spawn_animation);
			e.delay(2);
			inIntermission = false;
			attacksUntilSpecial = 1;
			canAttack = true;
			if(phase != Phase.THREE) {
				setInitialAttackStyle();
			}
			else {
				getGlyphs().forEach(glyph -> glyph.getGameObject().setId(56337));
			}
		});
	}

	private List<Position> endIntermissionPosition() {
		return Arrays.asList(
			new Position(npc.getPosition().getRegion().baseX + 29, npc.getPosition().getRegion().baseY + 39, 0),
			new Position(npc.getPosition().getRegion().baseX + 32, npc.getPosition().getRegion().baseY + 39, 0)
		);
	}

	boolean attackedWithMelee = false;

	private void preDamage(Hit hit) {
		if(inIntermission) {
			hit.block();
		}
		if(target != null && hit.attacker != null && hit.attacker.isPlayer() && hit.attackStyle != null) {
			attackedWithMelee = hit.attacker.player.getName().equalsIgnoreCase(target.getName()) && hit.attackStyle.isMelee();
		}
		int boss66Percent = (npc.getMaxHp() * 66) / 100;
		int boss33Percent = (npc.getMaxHp() * 33) / 100;
		if (npc.getHp() - hit.damage < boss66Percent && phase == Phase.ONE) {
			hit.damage = npc.getHp() - boss66Percent;
			phase = Phase.TWO;
			new Intermission().start(npc);
			npc.getPosition().getRegion().players.forEach(p -> p.getCombat().reset());
			inIntermission = true;
			canAttack = false;
		}
		else if (npc.getHp() - hit.damage < boss33Percent && phase == Phase.TWO) {
			hit.damage = npc.getHp() - boss33Percent;
			phase = Phase.THREE;
			npc.getCombat().getInfo().attack_ticks = 7;
			new Intermission().start(npc);
			npc.getPosition().getRegion().players.forEach(p -> p.getCombat().reset());
			inIntermission = true;
			canAttack = false;
		}
	}

	private void setInitialAttackStyle() {
		int order = 0;
		if(Random.get(1) == 0) {
			for(int i = 0; i < 2; i++) {
				GameObject glyph = glyphs.get(i).getGameObject();
				glyph.setId(56336);
				attackStyle = AttackStyle.MAGIC;
				specialCycle = 0;
			}
		} else {
			for(int i = 0; i < 2; i++) {
				GameObject glyph = glyphs.get(i).getGameObject();
				glyph.setId(56335);
				attackStyle = AttackStyle.RANGED;
				specialCycle = 1;
				order = 1;
			}
		}
		for(int i = 2; i < 5; i++ ) {
			GameObject glyph = glyphs.get(i).getGameObject();
			glyph.setId(order == 0 ? 56335 : 56336);
			order = order == 0 ? 1 : 0;
		}
	}

	@Override
	public void follow() {
		if(attackedWithMelee && phase != Phase.THREE) {
			follow(2);
		}
	}





	@Override
	public boolean attack() {
		if(!canAttack || inIntermission) {
			return false;
		}
		if(attacksUntilSpecial == 0) {
			Glyph availableGlyph = getAnyAvailableGlyph();
			Glyph availableFireGlyphs = getAnyAvailableFireGlyphs();
			Glyph availableShadowGlyphs = getAnyAvailableShadowGlyphs();
			if(specialCycle == 0) {
				if(availableFireGlyphs == null) {
					if(availableGlyph != null) {
						availableGlyph.getGameObject().setId(56336);
					}
				}
			} else {
				if(availableShadowGlyphs == null) {
					if(availableGlyph != null) {
						availableGlyph.getGameObject().setId(56335);
					}
				}
			}
			if(phase == Phase.ONE) {
				new GlyphSpecial(npc).sendRandomSpecial(specialCycle);
				canAttack = false;
				specialCycle = specialCycle == 0 ? 1 : 0;

			} else if(phase == Phase.TWO) {
				if(specialCycle == 0) {
					new MeteorStrike().send(npc, target.player);
					specialCycle = 1;
				} else if(specialCycle == 1) {
					new ShadowStomp().send(npc);
					specialCycle = 0;
				}
			}
			attacksUntilSpecial = 3;
			return true;
		}
		if(phase != Phase.THREE) {
			if (withinDistance(2) && attackedWithMelee) {
				new MeleeAttack().attack(npc);
				attacksUntilSpecial--;
				return true;
			}
		} else {
			if(withinDistance(2)) {
				new MeleeAttack().attack(npc);
				new VoidFlares().spawnOneVoidflare(npc);
				return true;
			} else {
				if (shadowCrashDelay.remaining() < 1) {
					new ShadowCrash().send(npc, target.player);
					shadowCrashDelay.delay(60);
				}
				attackStyle.getAutoAttack().attack(npc);
				attackStyle = attackStyle == AttackStyle.RANGED ? AttackStyle.MAGIC : AttackStyle.RANGED;
				return true;
			}
		}
		attackStyle.getAutoAttack().attack(npc);
		attackStyle = getRandomAttackStyle();
		attacksUntilSpecial--;
		return true;
	}


	private AttackStyle getRandomAttackStyle() {
		return Random.get(1) == 0 ? AttackStyle.RANGED : AttackStyle.MAGIC;
	}

	private Glyph getAnyAvailableGlyph() {
		return glyphs.stream()
				.filter(glyph -> glyph.getGameObject().getId() == 56337)
				.findAny()
				.orElse(null);
	}

	private Glyph getAnyAvailableShadowGlyphs() {
		return glyphs.stream()
				.filter(glyph -> glyph.getGameObject().getId() == 56335)
				.findAny()
				.orElse(null);
	}

	private Glyph getAnyAvailableFireGlyphs() {
		return glyphs.stream()
				.filter(glyph -> glyph.getGameObject().getId() == 56336)
				.findAny()
				.orElse(null);
	}

	@Override
	public void process() {
		if(phase == Phase.THREE) {
			if(voidFlareDelay.remaining() < 1) {
				new VoidFlares().spawnOneVoidflare(npc);
				new VoidFlares().spawnOneVoidflare(npc);
				voidFlareDelay.delay(Random.get(22, 30));
			}
			if(shadowCrashDelay.remaining() < 1 && withinDistance(2)) {
				new ShadowWaves().sendHazard(npc, phase);
				shadowCrashDelay.delay(Random.get(15));
			}
			return;
		}
		for(Glyph glyph : glyphs) {
			glyph.process();
		}
		if(shadowImmunityTicks > 0) {
			shadowImmunityTicks--;
		}
		if(fireImmunityTicks > 0) {
			fireImmunityTicks--;
		}
		if(inIntermission)
			return;
		if(hazardDelay.remaining() < 1 && Random.get(15) == 0) {
			Hazard hazard = Random.get(hazards);
			hazard.sendHazard(npc, phase);
			hazardDelay.delay(10);
		}
	}

	@Override
	public void startDeath(Hit killHit) {
		if(dead)
			return;
		dead = true;
		World.startEvent(e -> {
			npc.animate(12161);
			e.delay(3);
			npc.getPosition().getRegion().players.forEach(p -> {
				if (p.yamaTimer != null)
					p.yamaBestTime = p.yamaTimer.stop(p, p.yamaBestTime);
				p.yamaKills.increment(p);
				if (p.getHealthHud().isOpened())
					p.getHealthHud().close();
				handleNewDrop(p, npc.getId(), npc.getPosition());
				if(Random.get(100_000) == 0)
					Pet.YAMI_SHINY.unlock(p, npc.getId());
			});
			new NPC(123).spawn(new Position(npc.getPosition().getRegion().baseX + 28,
				npc.getPosition().getRegion().baseY + 41, 0));
			npc.remove();
		});
	}

	private List<Position> glyphPositions() {
		return List.of(
			new Position(npc.getPosition().getRegion().baseX + 25, npc.getPosition().getRegion().baseY + 36, 0),
			new Position(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 36, 0),
			new Position(npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 29, 0),
			new Position(npc.getPosition().getRegion().baseX + 30, npc.getPosition().getRegion().baseY + 24, 0),
			new Position(npc.getPosition().getRegion().baseX + 23, npc.getPosition().getRegion().baseY + 29, 0)
		);
	}
}

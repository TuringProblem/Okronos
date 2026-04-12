package io.ruin.model.activities.raids.tob.dungeon.boss.vasilias;

import io.ruin.api.utils.Random;
import io.ruin.model.World;
import io.ruin.model.activities.perktree.Perks;
import io.ruin.model.activities.perktree.perks.TheSoulsplit;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.npc.NPCCombat;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Projectile;
import io.ruin.model.skills.prayer.Prayer;
import io.ruin.network.PacketSender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class VasiliasCombat extends NPCCombat {

	private static final int START_HEIGHT = 25;
	private static final int END_HEIGHT = 30;
	private static final int DELAY = 30;
	private static final int DURATION_START = 46;
	private static final int DURATION_INCREMENT = 8;
	private static final int CURVE = 15;
	private static final int OFFSET = 255;
	private static final int TILE_OFFSET = 1;
	public boolean perfectNylocasFailed = false;
	int transitionTicks = 0;

	private static final Projectile RANGED_PROJECTILE = new Projectile(1560, START_HEIGHT, END_HEIGHT, DELAY, DURATION_START, DURATION_INCREMENT, CURVE, OFFSET).tileOffset(TILE_OFFSET).regionBased();
	private static final Projectile MAGIC_PROJECTILE = new Projectile(1580, START_HEIGHT, END_HEIGHT, DELAY, DURATION_START, DURATION_INCREMENT, CURVE, OFFSET).tileOffset(TILE_OFFSET).regionBased();
	private Bounds bossBounds;
	private Bounds areaBounds;

	public static final int ANIM = 7989;
	public static final int ANIM_RANGED = 7999;

	private static final int MELEE = 8355;
	private static final int MAGIC = 8356;
	private static final int RANGED = 8357;

	private int spawn = 0;
	private boolean sleep = false;
	private boolean initialised = false;
	private boolean waveInProgress = false;

	boolean waveStarted = false;


	LinkedList<NPC> leftPillars = new LinkedList<>();
	LinkedList<NPC> rightPillars = new LinkedList<>();

	LinkedList<Nylo> minions = new LinkedList<>();


	@Override
	public void init() {
		//System.out.println("npc pos " + npc.getPosition());
		bossBounds = new Bounds(npc.getPosition().getRegion().baseX + 27, npc.getPosition().getRegion().baseY + 19, npc.getPosition().getRegion().baseX + 38, npc.getPosition().getRegion().baseY + 31, 0);
		areaBounds = new Bounds(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 19, npc.getPosition().getRegion().baseX + 37, npc.getPosition().getRegion().baseY + 30, 0);
		leftPillars.add(new NPC(8379).spawn(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 19, 0, Direction.EAST, 0));
		leftPillars.add(new NPC(8379).spawn(npc.getPosition().getRegion().baseX + 26, npc.getPosition().getRegion().baseY + 28, 0, Direction.EAST, 0));
		rightPillars.add(new NPC(8379).spawn(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 28, 0, Direction.WEST, 0));
		rightPillars.add(new NPC(8379).spawn(npc.getPosition().getRegion().baseX + 35, npc.getPosition().getRegion().baseY + 19, 0, Direction.WEST, 0));
		rightPillars.forEach(pil -> {
			pil.getCombat().setAllowRetaliate(false);
			pil.deathStartListener = (DeathListener.SimpleKiller) killer -> {
				perfectNylocasFailed = true;
				pil.remove();
				rightPillars.remove(pil);
				if (rightPillars.isEmpty() && leftPillars.isEmpty())
					roomCollapsed();
			};
		});
		leftPillars.forEach(pil -> {
			pil.getCombat().setAllowRetaliate(false);
			pil.deathStartListener = (DeathListener.SimpleKiller) killer -> {
				perfectNylocasFailed = true;
				pil.remove();
				leftPillars.remove(pil);
				if (rightPillars.isEmpty() && leftPillars.isEmpty())
					roomCollapsed();
			};
		});
		npc.attackNpcListener = (player, n, message) -> player.getPosition().inBounds(areaBounds);
		npc.hitListener = new HitListener().postDamage(this::postDamage).preDefend(this::preHitDefend);
		initialised = true;
	}

	private void preHitDefend(Hit hit) {
		if (hit.attackStyle == null)
			return;
		switch (npc.getId()) {
			case MELEE:
				if (!hit.attackStyle.isMelee()) {
					hit.attacker.hit(new Hit(HitType.DAMAGE).fixedDamage(hit.damage));
					hit.block();
					perfectNylocasFailed = true;
				}
				break;
			case RANGED:
				if (!hit.attackStyle.isRanged()) {
					hit.attacker.hit(new Hit(HitType.DAMAGE).fixedDamage(hit.damage));
					hit.block();
					perfectNylocasFailed = true;
				}
				break;
			case MAGIC:
				if (!hit.attackStyle.isMagic()) {
					hit.attacker.hit(new Hit(HitType.DAMAGE).fixedDamage(hit.damage));
					hit.block();
					perfectNylocasFailed = true;
				}
				break;
		}
	}

	private void postDamage(Hit hit) {
		if (hit.attacker != null && hit.attacker.isPlayer()) {
			if (hit.attacker.isPlayer())
				hit.attacker.player.tobDamageDealt += hit.damage;
		}
	}

	private void roomCollapsed() {
		for (Player player :
			npc.getPosition().getRegion().players) {
			player.startEvent(event -> {
				player.sendMessage("The room is collapsing!");
				event.delay(3);
				player.hit(new Hit(npc, AttackStyle.MAGICAL_MELEE).fixedDamage(player.getHp()));
			});
		}
	}

	int getNextForm(int currentForm) {
		List<Integer> forms = new ArrayList<>();
		if (currentForm != MAGIC)
			forms.add(MAGIC);
		if (currentForm != RANGED)
			forms.add(RANGED);
		if (currentForm != MELEE)
			forms.add(MELEE);
		return Random.get(forms);
	}

	private LinkedList<NPC> getLeftPillars() {
		return leftPillars;
	}

	private LinkedList<NPC> getRightPillars() {
		return rightPillars;
	}


	@Override
	public void follow() {
		follow(30);
	}

	@Override
	public boolean attack() {
		if (npc.isHidden())
			return false;
		if (npc.getId() == MELEE) {
			basicAttack(8004, AttackStyle.CRUSH, 72);
		} else if (npc.getId() == RANGED) {
			rangedAttack();
		} else if (npc.getId() == MAGIC) {
			magicAttack();
		}
		return true;
	}

	public boolean wearingBarrows = true;

	@Override
	public void process() {
		if (!npc.getPosition().getRegion().players.isEmpty()) {
			for (Player p : npc.getPosition().getRegion().players) {
				if (!p.wearingBarrows())
					wearingBarrows = false;
			}
		}
		if (initialised) {
			if (!npc.isHidden()) {
				transitionTicks++;
				if (transitionTicks >= 30) {
					npc.transformNPC(getNextForm(npc.getId()));
					transitionTicks = 0;
				}
			}
			for (Player player :
				npc.getPosition().getRegion().players) {
				Position middlePosition = bossBounds.getMiddlePosition();
				if (player.getPosition().getDistance(middlePosition) < 8 && !waveStarted && player.getPosition().getZ() == npc.getPosition().getZ()) {
					waveStarted = true;
					wave();
				}
			}

			if (minions != null) {
				for (int i = minions.size() - 1; i >= 0; i--) {
					minions.get(i).process();
					if (minions.get(i).getCombat().getTarget() == null) {
						LinkedList<Entity> possibleTargets = new LinkedList<>();
						for (Player player :
							minions.get(i).getNPC().getPosition().getRegion().players) {
							if (player != null)
								possibleTargets.add(player);
						}
						if (!possibleTargets.isEmpty() && minions.get(i).beenToMiddle()) {
							Entity newTarget = possibleTargets.get(Random.get(0, possibleTargets.size() - 1));
							minions.get(i).getNPC().face(newTarget);
							minions.get(i).getCombat().setTarget(newTarget);
						}
					} else
						minions.get(i).setBeenToMiddle(true);
					if (minions.get(i).getNPC().getHp() <= 0 || minions.get(i).getNPC().dead())
						removeFromList(minions.get(i));


				}
				if (!waveInProgress && minions.isEmpty() && waveStarted && npc.isHidden())
					npc.setHidden(false);
			}
			if (npc.getCombat().isDead() || npc.isRemoved())
				minions.stream().filter(Objects::nonNull).forEach(minion -> minion.getNPC().remove());
		}
	}

	private void magicAttack() {
		npc.animate(ANIM);
		npc.localPlayers().forEach(p -> {
			World.startEvent(e -> {
				if (p.currentParty != null && !p.currentParty.deadPlayers.contains(p)) {
					int delay = MAGIC_PROJECTILE.send(npc, p);
					int maxDamage = 78;
					if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MAGIC))
						maxDamage = 22;
					e.delay(World.getTicks(delay));
					p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).ignorePrayer().ignoreDefence());
				}
			});
		});
	}

	private void rangedAttack() {
		npc.animate(7999);
		npc.localPlayers().forEach(p -> {
			World.startEvent(e -> {
				if (p.currentParty != null && !p.currentParty.deadPlayers.contains(p)) {
					int delay = RANGED_PROJECTILE.send(npc, p);
					int maxDamage = 77;
					if (p.getPrayer().isActive(Prayer.PROTECT_FROM_MISSILES))
						maxDamage = 22;
					e.delay(World.getTicks(delay));
					p.hit(new Hit(npc, AttackStyle.RANGED).randDamage(maxDamage).ignorePrayer().ignoreDefence());
				}
			});
		});
	}

	public Position getAbsolute(int localX, int localY) {
		return new Position(npc.getPosition().getRegion().baseX + localX, npc.getPosition().getRegion().baseY + localY, npc.getPosition().getZ());
	}

	private void removeFromList(Nylo npc) {
		minions.remove(npc);
	}

	private void addToList(Nylo nylo) {
		minions.add(nylo);
	}

	private void wave() {
		waveInProgress = true;
		sleep = false;
		World.startEvent(e -> {
			while (!sleep) {
				int[] hagios = new int[]{8344, 8347, 8353, 8383};
				int[] ischyros = new int[]{8345, 8348, 8351, 8381};
				int[] toxobolos = new int[]{8343, 8349, 8352, 8382};
				e.delay(2);
				NylocasHagiosCombat hagiosOne = new NylocasHagiosCombat(Random.get(hagios), getAbsolute(46, Random.get(24, 25)), getRightPillars(), npc);
				hagiosOne.npc.attackNpcListener = (player, n, message) -> {
					return player.getPosition().inBounds(areaBounds);
				};
				addToList(hagiosOne);
				moveToCenter(hagiosOne);
				e.delay(4);
				NylocasIschyrosCombat ischyrosOne = new NylocasIschyrosCombat(Random.get(ischyros), getAbsolute(46, Random.get(24, 25)), getRightPillars(), npc);
				ischyrosOne.npc.attackNpcListener = (player, n, message) -> {
					return player.getPosition().inBounds(areaBounds);
				};
				addToList(ischyrosOne);
				moveToCenter(ischyrosOne);
				e.delay(3);
				NylocasToxobolosCombat toxobolosOne = new NylocasToxobolosCombat(Random.get(toxobolos), getAbsolute(46, Random.get(24, 25)), getRightPillars(), npc);
				toxobolosOne.npc.attackNpcListener = (player, n, message) -> {
					return player.getPosition().inBounds(areaBounds);
				};
				addToList(toxobolosOne);
				moveToCenter(toxobolosOne);
				e.delay(4);
				NylocasHagiosCombat hagiosTwo = new NylocasHagiosCombat(Random.get(hagios), getAbsolute(17, Random.get(24, 25)), getLeftPillars(), npc);
				hagiosTwo.npc.attackNpcListener = (player, n, message) -> {
					return player.getPosition().inBounds(areaBounds);
				};
				addToList(hagiosTwo);
				moveToCenter(hagiosTwo);
				e.delay(4);
				NylocasIschyrosCombat ischyrosTwo = new NylocasIschyrosCombat(Random.get(ischyros), getAbsolute(17, Random.get(24, 25)), getLeftPillars(), npc);
				ischyrosTwo.npc.attackNpcListener = (player, n, message) -> {
					return player.getPosition().inBounds(areaBounds);
				};
				addToList(ischyrosTwo);
				moveToCenter(ischyrosTwo);
				e.delay(3);
				NylocasToxobolosCombat toxobolosTwo = new NylocasToxobolosCombat(Random.get(toxobolos), getAbsolute(17, Random.get(24, 25)), getLeftPillars(), npc);
				toxobolosTwo.npc.attackNpcListener = (player, n, message) -> {
					return player.getPosition().inBounds(areaBounds);
				};
				addToList(toxobolosTwo);
				moveToCenter(toxobolosTwo);
				e.delay(4);
				spawn += 6;
				if (spawn >= 18) {
					sleep = true;
					waveInProgress = false;
				}
			}
		});
	}


	private void moveToCenter(Nylo n) {
		n.getNPC().getDef().ignoreOccupiedTiles = true;
		addToList(n);
	}
}

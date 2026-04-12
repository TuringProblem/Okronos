package io.ruin.model.entity;

import io.ruin.HooksV2;
import io.ruin.Server;
import io.ruin.cache.LocType;
import io.ruin.cache.SeqType;
import io.ruin.model.World;
import io.ruin.model.activities.duelarena.DuelRule;
import io.ruin.model.combat.AttackStyle;
import io.ruin.model.combat.Combat;
import io.ruin.model.combat.CombatUtils;
import io.ruin.model.combat.Hit;
import io.ruin.model.combat.HitType;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.LockType;
import io.ruin.model.entity.shared.Movement;
import io.ruin.model.entity.shared.StepType;
import io.ruin.model.entity.shared.Toxins;
import io.ruin.model.entity.shared.UpdateMask;
import io.ruin.model.entity.shared.listeners.AttackNpcListener;
import io.ruin.model.entity.shared.listeners.AttackPlayerListener;
import io.ruin.model.entity.shared.listeners.DeathListener;
import io.ruin.model.entity.shared.listeners.DropListener;
import io.ruin.model.entity.shared.listeners.HitListener;
import io.ruin.model.entity.shared.masks.AnimationUpdate;
import io.ruin.model.entity.shared.masks.Appearance;
import io.ruin.model.entity.shared.masks.EntityDirectionUpdate;
import io.ruin.model.entity.shared.masks.ForceMovementUpdate;
import io.ruin.model.entity.shared.masks.ForceTextUpdate;
import io.ruin.model.entity.shared.masks.GraphicsUpdate;
import io.ruin.model.entity.shared.masks.HitsUpdate;
import io.ruin.model.entity.shared.masks.MapDirectionUpdate;
import io.ruin.model.entity.shared.masks.ModelTintUpdate;
import io.ruin.model.inter.Widget;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Graphic;
import io.ruin.model.map.Position;
import io.ruin.model.map.Region;
import io.ruin.model.map.Tile;
import io.ruin.model.map.object.GameObject;
import io.ruin.model.map.route.RouteFinder;
import io.ruin.model.skills.magic.spells.TargetSpell;
import io.ruin.model.var.VarPlayerRepository;
import io.ruin.process.event.Event;
import io.ruin.process.event.EventConsumer;
import io.ruin.process.event.EventType;
import io.ruin.process.event.EventWorker;
import io.ruin.process.event.LegacyEvent;
import io.ruin.utility.TickDelay;
import lombok.extern.slf4j.Slf4j;
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcAvatar;
import org.jetbrains.annotations.Nullable;
import org.rsmod.game.entity.PathingEntity;
import core.task.Continuation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public abstract class Entity {
	public static interface Hook {
		record PreAttackWithSpell(Player player, Entity target, TargetSpell spell) implements Hook {
		}

		record AttackWithSpellBeforeHit(Entity source, Entity target, TargetSpell spell, Hit hit) implements Hook {
		}
	}

	private static final int MAX_EVENTS_PER_ENTITY = 50;
	public static final long MAX_LOCK_TIME_TICKS = 30;

	private transient boolean backgroundEventsErrored = false;
	public transient HooksV2<Hook> selfHooks = new HooksV2<>(Hook.class);
	public transient NpcAvatar avatar;
	public transient Player player;
	public transient NPC npc;
	public transient final HashMap<Object, Object> temp = new HashMap<>();
	public transient boolean processed;
	private transient volatile int index = -1;
	private transient int size = 1;
	private transient boolean hidden;
	private transient boolean invincible;
	private transient int strAdder;
	protected Position position;
	public transient Position lastPosition;
	public transient boolean occupyingTiles;
	public transient boolean multi;
	private transient boolean ignoreMulti;
	private transient RouteFinder routeFinder;
	public transient AttackNpcListener attackNpcListener;
	public transient AttackPlayerListener attackPlayerListener;
	public transient DeathListener deathStartListener, deathEndListener;
	public transient DropListener dropListener;
	public transient HitListener hitListener;
	protected transient AnimationUpdate animationUpdate;
	private transient long animTick = 0;
	private transient boolean boulderDamageImmume = false;
	protected transient ModelTintUpdate modelTintUpdate;
	protected transient GraphicsUpdate graphicsUpdate;
	protected transient MapDirectionUpdate mapDirectionUpdate;
	public transient ForceMovementUpdate forceMovementUpdate;
	protected transient EntityDirectionUpdate entityDirectionUpdate;
	protected transient ForceTextUpdate forceTextUpdate;
	private transient LegacyEvent activeEvent, nextEvent;
	protected final transient ArrayList<LegacyEvent> backgroundEvents = new ArrayList<>();
	private transient LockType lock = LockType.NONE;
	protected transient long lockTimeoutTicks = 0L;
	public int poisonTicks;
	public int poisonDamage;
	public int poisonImmunity;
	// 1 = poison, 2 = venom
	public int poisonLevel;
	public int poisonImmunityLevel; // ^
	public Toxins toxins;
	public final transient ArrayList<Hit> queuedHits = new ArrayList<>();
	private transient boolean queuedHitsErrored = false;
	public transient HitsUpdate hitsUpdate;
	private transient Entity freezer;
	public transient TickDelay freezeDelay = new TickDelay();
	private transient TickDelay stunDelay = new TickDelay();
	private transient TickDelay rootDelay = new TickDelay();
	public transient int firstChunkX, firstChunkY;
	private transient int lastChunkId;

	private transient List<Player> cachedLocalPlayers;

	private transient long lastLocalPlayersUpdateTick = -1;

	private transient List<NPC> cachedLocalNpcs;

	private transient long lastLocalNpcsUpdateTick = -1;

	public abstract String getName();

	public void setIndex(final int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public int getClientIndex() {
		if (player != null)
			return index + 65536;
		return index;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
		if (avatar != null) {
			avatar.setInaccessible(this.hidden);
		}
		Tile.occupy(this);
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setInvincible(boolean invincible) {
		clearHits();
		this.invincible = invincible;
	}

	public boolean isInvincible() {
		return invincible;
	}

	public void setStrAdder(int strAdder) {
		this.strAdder = strAdder;
	}

	public int getStrAdder() {
		return this.strAdder;
	}

	public boolean hasStrAdder() {
		return (this.strAdder != 0);
	}

	public boolean isAt(Position pos) {
		return isAt(pos.getX(), pos.getY());
	}

	public boolean isAt(int x, int y) {
		return position.getX() == x && position.getY() == y;
	}

	public int getAbsX() {
		return position.getX();
	}

	public int getAbsY() {
		return position.getY();
	}

	public int getHeight() {
		return position.getZ();
	}

	public Position getPosition() {
		return position;
	}

	public Bounds getBounds() {
		return new Bounds(getPosition().getX(), getPosition().getY(), getPosition().getX() + getSize() - 1,
				getPosition().getY() + getSize() - 1, getPosition().getZ());
	}

	public void updateLastPosition() {
		lastPosition.set(getPosition());
	}

	public Position getLastPosition() {
		return lastPosition;
	}

	public boolean isNearBank() {
		Tile tile = Tile.get(getAbsX(), getAbsY(), getHeight());
		return tile != null && tile.nearBank;
	}

	public void setIgnoreMulti(boolean ignoreMulti) {
		this.ignoreMulti = ignoreMulti;
		checkMulti();
	}

	public void checkMulti() {
		Tile tile = Tile.get(getAbsX(), getAbsY(), getHeight());
		boolean inMulti = ignoreMulti || (tile != null && tile.multi);
		if (multi == inMulti)
			return;
		multi = inMulti;
		if (player != null)
			VarPlayerRepository.MULTI_ZONE.set(player, multi ? 1 : 0);
	}

	public boolean inMulti() {
		return multi;
	}

	public RouteFinder getRouteFinder() {
		if (routeFinder == null)
			routeFinder = new RouteFinder(this);
		return routeFinder;
	}

	/**
	 * Step - todo - honestly move all this to Movement class lol..
	 */

	public void step(int diffX, int diffY, StepType stepType) {
		stepAbs(getAbsX() + diffX, getAbsY() + diffY, stepType);
	}

	public void stepAbs(int absX, int absY, StepType stepType) {
		/* forces a step without route finding */
		Movement movement = getMovement();
		movement.readOffset = 0;
		movement.getStepsX()[0] = absX;
		movement.getStepsY()[0] = absY;
		movement.writeOffset = 1;
		movement.stepType = stepType;
	}

	public void resetSteps() {
		Movement movement = getMovement();
		movement.readOffset = 0;
		movement.writeOffset = 0;
		movement.stepType = StepType.NORMAL;
	}

	public void burnEvent(Entity en, int loops, int minDamage, int maxDamage, int delay) {
		World.startEvent(e -> {
			e.setCancelCondition(() -> en == null || en.getHp() < 1);
			for (int i = 0; i < loops; i++) {
				en.graphics(78);
				en.hit(new Hit().randDamage(minDamage, maxDamage));
				e.delay(delay);
			}
		});
	}

	public boolean addStep(int absX, int absY) {
		Movement movement = getMovement();
		if (movement.writeOffset < 50) {
			movement.getStepsX()[movement.writeOffset] = absX;
			movement.getStepsY()[movement.writeOffset] = absY;
			movement.writeOffset++;
			return true;
		}
		return false;
	}

	/**
	 * Masks
	 */

	public abstract UpdateMask[] getMasks();

	public int getUpdateMaskData(boolean playerUpdate, boolean justAdded) {
		int maskData = 0;
		for (UpdateMask updateMask : getMasks()) {
			if (!updateMask.hasUpdate(justAdded))
				continue;
			maskData |= updateMask.get(playerUpdate);
		}
		return maskData;
	}

	// Because we are moving from external to local we 100% need apperance and
	// movement
	// otherwise you would be invisible and also movement_mode bc if theyre running
	// into
	// view of you they would appear initially walking and then next tick appear
	// running
	public int getAppearanceUpdateMaskData(boolean playerUpdate, boolean justAdded) {
		int maskData = 0;
		for (UpdateMask updateMask : getMasks()) {
			if (!updateMask.hasUpdate(justAdded) && !(updateMask instanceof Appearance))
				continue;
			maskData |= updateMask.get(playerUpdate);
		}
		return maskData;
	}

	public void resetUpdates() {
		for (final UpdateMask updateMask : getMasks()) {
			if (updateMask.isSent()) {
				updateMask.setSent(false);
				updateMask.reset();
			}
		}
	}

	public void setBoulderDamageImmune(boolean boulderDamageImmune) {
		this.boulderDamageImmume = boulderDamageImmune;
	}

	public boolean isBoulderDamageImmune() {
		return boulderDamageImmume;
	}

	public int animate(int id) {
		return animate(id, 0);
	}

	/*
	 * Whether we are currently animating or not
	 */
	public boolean isAnimating() {
		return animationUpdate.id >= 0 && Server.currentTick() < animTick;
	}

	public int resetAnimation() {
		return animate(-1, 0);
	}

	public int animate(int id, int delay) {
		this.animationUpdate.set(id, delay);
		if (id == -1) {
			return 0;
		}

		var def = SeqType.get(id);
		if (def != null) {
			animTick = Server.currentTick() + def.getDuration();
			return def.getDuration() / 600;
		}

		return 0;
	}

	public boolean hasAnimationUpdate() {
		return animationUpdate.hasUpdate(false);
	}

	public void recolour(int start, int end, int hue, int saturation, int luminance, int amount) {
		modelTintUpdate.set(start, end, hue, saturation, luminance, amount);
	}

	public void graphics(int id) {
		graphics(id, 0, 0);
	}

	public void graphics(int id, int height, int delay) {
		graphicsUpdate.set(id, height, delay);
	}

	public void graphics(Graphic graphic) {
		graphicsUpdate.set(graphic.getId(), graphic.getHeight(), graphic.getDelay());
	}

	public void face(Direction direction) {
		if (npc != null) {
			var centre = this.npc.getCentrePosition();
			int faceX = centre.x() + direction.deltaX;
			int faceY = centre.y() + direction.deltaY;
			face(faceX, faceY);
			return;
		}
		mapDirectionUpdate.set(direction.clientValue);
	}

	public void markFaceInstant() {
		this.mapDirectionUpdate.markInstant();
	}

	public void face(Position position) {
		face(position.x(), position.y(), getSize(), getSize());
	}

	public void face(int absX, int absY) {
		face(absX, absY, getSize(), getSize());
	}

	public void face(GameObject gameObject) {
		if (gameObject.getId() == -1) {
			/* object was removed */
			return;
		}
		LocType def = gameObject.getDef();
		int x = gameObject.x, y = gameObject.y;
		if (isAt(x, y) && gameObject.getType() <= 9) {// (gameObject.type == 0 || gameObject.type == 5)) {
			if (gameObject.getDirection() == 0)
				x--;
			else if (gameObject.getDirection() == 1)
				y++;
			else if (gameObject.getDirection() == 2)
				x++;
			else if (gameObject.getDirection() == 3)
				y--;
		}
		int xLength = 1, yLength = 1;

		if (def != null) {
			if (gameObject.getDirection() == 1 || gameObject.getDirection() == 3) {
				xLength = def.yLength;
				yLength = def.xLength;
			} else {
				xLength = def.xLength;
				yLength = def.yLength;
			}
		} else {
			log.warn("Unable to find object def: " + gameObject.getId());
		}

		face(x, y, xLength, yLength);
	}

	public void face(Entity target) {
		face(target, false);
	}

	public void faceTemp(Entity target) {
		face(target, true);
	}

	public void faceNone() {
		this.face((Entity) null);
	}

	public void faceNone(boolean delay) {
		if (entityDirectionUpdate == null) {
			return;
		}
		int direction = entityDirectionUpdate.direction;
		if (direction == -1) {
			return;
		}
		entityDirectionUpdate.remove(delay);
	}

	public void forceText(String text) {
		forceTextUpdate.set(text);
	}

	public final Event startEvent(EventConsumer eventConsumer) {
		stopEvent(true);
		var evt = new LegacyEvent(EventWorker.createEvent(eventConsumer));
		this.nextEvent = evt;
		return evt.event;
	}

	public final void finishEvents() {
		this.activeEvent = null;
		this.nextEvent = null;
		this.backgroundEvents.clear();
	}

	public final Event startPersistableEvent(EventConsumer consumer) {
		stopEvent(true);
		Event event = EventWorker.createEvent(consumer);
		event.eventType = EventType.PERSISTENT;
		var evt = new LegacyEvent(event);
		this.nextEvent = evt;
		return evt.event;
	}

	public final void queue(Continuation.Void continuation) {
		addEvent((_e) -> {
			continuation.await();
		});
	}

	public final void addEvent(EventConsumer eventConsumer) {
		if (backgroundEvents.size() > MAX_EVENTS_PER_ENTITY) {
			if (!this.backgroundEventsErrored) {
				this.backgroundEventsErrored = true;
				log.error("Too many background events for entity " + this.captureState(), new IllegalStateException());
			}
		}
		backgroundEvents.add(new LegacyEvent(EventWorker.createEvent(eventConsumer)));
	}

	public final void stopEvent(boolean resetCombat) {
		if (activeEvent != null && activeEvent.event.persistent()) {
			return;
		}

		if (activeEvent != null) {
			if (resetCombat || !activeEvent.event.isIgnoreCombatReset())
				activeEvent = null;
		}
		if (nextEvent != null) {
			if (resetCombat || !nextEvent.event.isIgnoreCombatReset())
				nextEvent = null;
		}
	}

	public void unlock() {
		lock = LockType.NONE;
		lockTimeoutTicks = MAX_LOCK_TIME_TICKS;
	}

	public void lock() {
		lock(LockType.FULL);
	}

	public void lock(LockType type) {
		lock = type;
		lockTimeoutTicks = MAX_LOCK_TIME_TICKS;
	}

	public boolean isLocked() {
		return lock != LockType.NONE || getMovement().teleporting();
	}

	public boolean isLocked(LockType type) {
		return lock == type;
	}

	public boolean isLockedExclude(LockType type) {
		return lock != LockType.NONE && lock != type;
	}

	public void curePoison(int immuneFor) {
		if (poisonLevel > 1) { // envenomed, downgrade to poison
			poisonLevel--;
			poisonDamage = 6;
			VarPlayerRepository.POISONED.set(player, 1);
		} else { // regular poison, cure
			this.poisonTicks = 0;
			this.poisonDamage = 0;
			if (poisonImmunityLevel < 2) { // if we have venom immunity already, we should keep that instead
				if (poisonImmunity < immuneFor)
					this.poisonImmunity = immuneFor;
				if (immuneFor > 0)
					poisonImmunityLevel = 2;
			}
			poisonLevel = 0;
			if (player != null)
				VarPlayerRepository.POISONED.set(player, 0);
		}
	}

	public boolean poison(int damage) {
		if (poisonLevel > 0 || isPoisonImmune())
			return false;
		poisonTicks = 120;
		poisonDamage = damage;
		poisonLevel = 1;
		if (player != null) {
			player.sendMessage("You have been poisoned!");
			VarPlayerRepository.POISONED.set(player, 1);
		}
		return true;
	}

	public boolean envenom(int damage) {
		if (poisonLevel >= 2 || isVenomImmune())
			return false;
		poisonTicks = 120000;
		poisonDamage = damage;
		poisonLevel = 2;
		if (player != null) {
			player.sendMessage("You have been venomed!");
			VarPlayerRepository.POISONED.set(player, 1000000);
		}
		return true;
	}

	/**
	 * Note: curing venom also cures poison
	 */
	public void cureVenom(int immuneFor) {
		if (poisonLevel == 0)
			return;
		this.poisonTicks = 0;
		this.poisonDamage = 0;
		if (poisonImmunity < immuneFor)
			this.poisonImmunity = immuneFor;
		if (immuneFor > 0)
			poisonImmunityLevel = 2;
		poisonLevel = 0;
		if (player != null)
			VarPlayerRepository.POISONED.set(player, 0);
	}

	public boolean isPoisoned() {
		return poisonDamage > 0;
	}

	public boolean isEnvenomed() {
		return poisonDamage > 0 && poisonLevel >= 2;
	}

	public boolean isPoisonImmune() {
		return poisonImmunity > 0;
	}

	public boolean isVenomImmune() {
		return poisonImmunityLevel > 1 && poisonImmunity > 0;
	}

	public int queuedHitsCount() {
		return this.queuedHits.size();
	}

	public void hitUnconditionally(Hit... hits) {
		for (Hit hit : hits) {
			if (hit.defend(this)) {
				queuedHits.add(hit);
			}
		}
	}

	public int hit(int min, int max) {
		return this.hit(new Hit[] {new Hit().damage(min).min(min).max(max)});
	}

	public int hit(int damage) {
		return this.hit(new Hit[] {new Hit().damage(damage)});
	}

	// NOTE: may override the current entity combat target in it's onDefend listeners
	public int hit(Hit... hits) {
		if (hits == null) {
			return 0;
		}

		if (hits.length == 0) {
			return 0;
		}

		if (queuedHits.size() >= 250) {
			if (!this.queuedHitsErrored) {
				this.queuedHitsErrored = true;
				log.error("Too many queued hits.",
						new IllegalStateException("Too many queued hits at this tick for: " + this.captureState()));
			}
			return 0;
		}

		int totalDamage = 0;
		var baseHit = hits[0];

		if (shouldDiscardHit(baseHit)) {
			return 0;
		}

		for (Hit hit : hits) {
			if (shouldDiscardHit(hit)) {
				continue;
			}

			if (!hit.defend(this)) {
				continue;
			}
			if (!isLocked(LockType.FULL_NULLIFY_DAMAGE) || isPlayer()) {
				queuedHits.add(hit);
				baseHit = hit;
			}
			totalDamage += hit.damage;
		}

		if (baseHit.type.resetActions && baseHit.resetActions) {
			if (player != null) {
				if (player.gauntlet != null && player.gauntlet.inGauntlet) {
				} else {
					player.resetActions(true, false, false);
				}
			} else {
				npc.resetActions(false, false);
			}
		}

		if (baseHit.attacker != null) {
			if (baseHit.attacker.player != null && baseHit.attackStyle != null) {
				if (player != null) // important that this happens here for things that hit multiple targets
					baseHit.attacker.player.getCombat().skull(player);
				if (baseHit.attackSpell == null && baseHit.attackStyle != AttackStyle.CANNON)
					CombatUtils.addXp(baseHit.attacker.player, this, baseHit.attackStyle, baseHit.attackType, totalDamage);
			}

			if (getCombat() != null) {
				getCombat().updateLastDefend(baseHit.attacker);
			}
		}

		return totalDamage;
	}

	public void clearHits() {
		queuedHits.clear();
	}

	public abstract int getHp();

	public abstract int getMaxHp();

	public abstract int setHp(int newHp);

	public abstract int setMaxHp(int newHp);

	public int incrementHp(int increment) {
		int hp = getHp();
		int maxHp = getMaxHp();
		if (hp > maxHp) {
			if (increment >= 0)
				return hp;
			hp += increment;
			if (hp < 0)
				hp = 0;
		} else {
			hp += increment;
			if (hp < 0)
				hp = 0;
			else if (hp > maxHp)
				hp = maxHp;
		}
		return setHp(hp);
	}

	/**
	 * Sounds
	 */

	public void privateSound(int id) {
		privateSound(id, 1, 0);
	}

	public void privateSound(int id, int type, int delay) {
		if (player == null) {
			return;
		}
		player.getPacketSender().sendSoundEffect(id, type, delay);
	}

	public void publicSound(int id) {
		publicSound(id, 1, 0);
	}

	public void publicSound(int id, int type, int delay) {
		int x = getAbsX();
		int y = getAbsY();
		int distance = 15; // idk how to calc
		for (var p : localPlayers()) {
			p.getPacketSender().sendAreaSound(id, type, delay, x, y, distance);
		}
	}

	public void freeze(int seconds, Entity entity) {
		freezeDelay.delaySeconds(seconds);
		freezer = entity;
		if (player != null && !player.isLocked())
			getMovement().reset();
	}

	public void resetFreeze() {
		freezeDelay.reset();
		freezer = null;
		if (player != null)
			player.getPacketSender().sendWidgetTimerCustom(Widget.BARRAGE, 0);
	}

	public boolean isFrozen() {
		return freezeDelay.isDelayed();
	}

	public boolean hasFreezeImmunity() {
		return freezeDelay.isDelayed(5);
	}

	public void stun(int seconds, boolean resetMovement) {
		stunDelay.delaySeconds(seconds);
		if (player != null) {
			player.privateSound(2727);
			player.sendMessage("You have been stunned!");
		}
		graphics(245, 124, 0);
		if (resetMovement)
			getMovement().reset();
	}

	public void resetStun() {
		stunDelay.reset();
	}

	public boolean isStunned() {
		return stunDelay.isDelayed();
	}

	public boolean isStunned(int gracePeriod) {
		return stunDelay.isDelayed(gracePeriod);
	}

	public void root(int ticks, boolean resetMovement) {
		rootDelay.delay(ticks);
		if (resetMovement)
			getMovement().reset();
	}

	public void resetRoot() {
		rootDelay.reset();
	}

	public boolean isRooted() {
		return rootDelay.isDelayed();
	}

	/**
	 * Movement check
	 */

	public boolean isMovementBlocked(boolean message, boolean ignoreFreeze) {
		if (!ignoreFreeze && isFrozen()) {
			if (freezer != null) {
				if (freezer.player != null && World.getPlayer(freezer.player.getUserId(), true) == null) {
					resetFreeze();
					return false;
				}
				if (!freezer.getPosition().isWithinDistance(getPosition(), false, 12)) {
					resetFreeze();
					return false;
				}
				if (message && player != null)
					player.sendMessage("A magical force stops you from moving.");
				return true;
			}
		}
		if (isStunned() || isRooted()) {
			if (message && player != null)
				player.sendMessage("You're stunned!");
			return true;
		}

		if (player != null && DuelRule.NO_MOVEMENT.isToggled(player)) {
			if (message)
				player.sendMessage("Movement has been disabled for this duel!");
			return true;
		}
		return false;
	}

	/**
	 * Abstract
	 */

	public void forLocalEntity(Consumer<Entity> entityConsumer) {
		for (Player player : localPlayers()) {
			if (player != this)
				entityConsumer.accept(player);
		}
		for (NPC npc : localNpcs()) {
			if (npc != this)
				entityConsumer.accept(npc);
		}
	}

	public abstract boolean isLocal(final Player player);

	public abstract List<Player> computeLocalPlayers();

	public List<Player> localPlayers() {
		final long currentTick = Server.currentTick();
		if (currentTick != lastLocalPlayersUpdateTick) {
			cachedLocalPlayers = computeLocalPlayers();
			lastLocalPlayersUpdateTick = currentTick;
		}
		cachedLocalPlayers.removeIf(it -> !it.isOnline());
		return cachedLocalPlayers;
	}

	public abstract List<NPC> computeLocalNpcs();

	public List<NPC> localNpcs() {
		final long currentTick = Server.currentTick();
		if (currentTick != lastLocalNpcsUpdateTick) {
			cachedLocalNpcs = computeLocalNpcs();
			lastLocalNpcsUpdateTick = currentTick;
		}
		return cachedLocalNpcs;
	}

	public abstract Movement getMovement();

	public abstract Combat getCombat();

	public void set(String key, Object value) {
		temp.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Object key) {
		Object value = temp.get(key);
		return value == null ? null : (T) value;
	}

	public <T> T get(Object key, T nullValue) {
		T attribute = get(key);
		return attribute == null ? nullValue : attribute;
	}

	public <T> T getOrPut(String key, T nullValue) {
		T attribute = get(key);
		if (attribute == null) {
			set(key, nullValue);
		}
		return attribute == null ? nullValue : attribute;
	}

	@SuppressWarnings("unchecked")
	public <T> T remove(Object key) {
		Object value = temp.remove(key);
		return value == null ? null : (T) value;
	}

	public boolean isPlayer() {
		return false;
	}

	public boolean isNpc() {
		return false;
	}

	public boolean dead() {
		// FIXME: non combat npcs never die?
		if (getCombat() == null) {
			return false;
		}
		return getCombat().isDead();
	}

	public boolean alive() {
		return !dead();
	}

	public int getDirection() {
		return entityDirectionUpdate.direction;
	}

	public int getFirstChunkX() {
		return firstChunkX;
	}

	public int getFirstChunkY() {
		return firstChunkY;
	}

	public int getBaseLocalX() {
		return position.getX() - 8 * (firstChunkX - 6);
	}

	public int getBaseLocalY() {
		return position.getY() - 8 * (firstChunkY - 6);
	}

	public int getSceneX(Position pos) {
		return position.getX() - ((getFirstChunkX() - 6) << 3);
	}

	public int getSceneY(Position pos) {
		return position.getY() - ((getFirstChunkY() - 6) * 8);
	}

	public boolean updateRegion() {
		int diffX = firstChunkX - position.getChunkX();
		int diffY = firstChunkY - position.getChunkY();
		int size = Region.CLIENT_SIZE;
		int updateSize = ((size >> 3) / 2) - 1;
		if (Math.abs(diffX) >= updateSize || Math.abs(diffY) >= updateSize) {
			updateFirstChunk();
			return true;
		}
		return false;
	}

	public void updateFirstChunk() {
		firstChunkX = position.getX() >> 3;
		firstChunkY = position.getY() >> 3;
	}

	public int getLastChunkId() {
		return lastChunkId;
	}

	public void setLastChunkId(int lastChunkId) {
		this.lastChunkId = lastChunkId;
	}

	public abstract String captureState();

	@Nullable
	public abstract PathingEntity rsmod();

	protected final void processEvent() {
		if (nextEvent != null) {
			if (activeEvent != null) {
				activeEvent.stop();
			}
			activeEvent = nextEvent;
			nextEvent = null;
		}

		if (activeEvent != null) {
			if (!activeEvent.tick()) {
				activeEvent = null;
			}
		}

		var events = new ArrayList<>(this.backgroundEvents);
		for (var evt : events) {
			if (!evt.tick()) {
				this.backgroundEvents.remove(evt);
			}
		}
	}

	protected void processHits() {
		if (isStunned())
			return;
		if (getHp() < 1)
			return;
		checkPoison();

		if (queuedHits.isEmpty()) {
			return;
		}

		// noinspection ForLoopReplaceableByForEach (foreach will cause
		// concurrentmodification exceptions!)
		for (int i = 0; i < queuedHits.size(); i++) {
			Hit hit = queuedHits.get(i);

			if (shouldDiscardHit(hit)) {
				hit.removed = true;
				continue;
			}

			if (!hit.finish(this)) {
				continue;
			}

			hit.removed = true;

			if (!hit.isHidden()) {
				hitsUpdate.add(hit, getHp(), getMaxHp(), this);
			}

			var combat = getCombat();
			if (hit.attacker != null && hit.attackStyle != null && combat != null) {
				// todo - honestly this retaliate system is so bad...
				if (!isLocked() && !combat.retaliating && combat.allowRetaliate(hit.attacker)) {
					queueRetaliating(hit.attacker);
				}
			}
		}
		queuedHits.removeIf(hit -> hit.removed);
	}

	private boolean shouldDiscardHit(Hit hit) {
		var combat = this.getCombat();
		if (combat != null && combat.isDead()) {
			return true;
		}

		if (hit.isNullified()) {
			return true;
		}

		if (isLocked(LockType.FULL_NULLIFY_DAMAGE)) {
			return true;
		}

		if (hit.attacker != null && hit.attacker.dead()) {
			return true;
		}

		return false;
	}

	private void queueRetaliating(Entity attacker) {
		if (attacker == null) {
			return;
		}
		var cmb = getCombat();
		cmb.retaliating = true;
		addEvent(e -> {
			if (!getCombat().isDead() && !attacker.getCombat().isDead()) {
				cmb.setTarget(attacker);
				cmb.faceTarget();
			}
			getCombat().retaliating = false;
		});
	}

	private void face(int absX, int absY, int xLength, int yLength) {
		if (player != null) {
			int faceX = (absX * 2) + xLength;
			int faceY = (absY * 2) + yLength;
			int anInt1783 = (getBaseLocalX() << 7) | (size << 6);
			int anInt1770 = (getBaseLocalY() << 7) | (size << 6);
			int baseRegionX = (this.getFirstChunkX() - 6) * 8;
			int baseRegionY = (this.getFirstChunkY() - 6) * 8;
			int i_35_ = (anInt1783 - (faceX - baseRegionX - baseRegionX) * 64);
			int i_36_ = (anInt1770 - (faceY - baseRegionY - baseRegionY) * 64);
			if (i_35_ != 0 || i_36_ != 0)
				mapDirectionUpdate.set((int) (Math.atan2((double) i_35_, (double) i_36_) * 325.949) & 0x7ff);
			return;
		}

		mapDirectionUpdate.set(absX, absY);
	}

	private void face(Entity target, boolean temp) {
		int direction = entityDirectionUpdate.direction;
		int newDirection = target == null ? -1 : target.getClientIndex();
		if (direction == newDirection && !temp)
			return;
		entityDirectionUpdate.set(newDirection, temp);
	}

	private void checkPoison() {
		if (poisonImmunity > 0 && --poisonImmunity == 0) {
			poisonImmunityLevel = 0;
			return;
		}
		if (poisonDamage == 0) {
			/* not poisoned */
			return;
		}
		if (poisonTicks == 0 || --poisonTicks % 30 != 0) {
			/* no hit required */
			return;
		}
		hit(new Hit(poisonLevel == 2 ? HitType.VENOM : HitType.POISON).fixedDamage(poisonDamage));
		if (poisonLevel == 1) { // regular poison, check decay
			if (poisonTicks > 0) {
				/* maintain same damage */
				return;
			}
			if (--poisonDamage > 0)
				poisonTicks = 120;
			else if (player != null) {
				poisonLevel = 0;
				VarPlayerRepository.POISONED.set(player, 0);
			}
		} else { // venom, increase damage
			poisonDamage = Math.min(poisonDamage + 2, 20);
		}
	}

	public boolean preAttackWithSpell(Player player, TargetSpell spell) {
		return this.selfHooks.handle(new Hook.PreAttackWithSpell(player, this, spell));
	}

}

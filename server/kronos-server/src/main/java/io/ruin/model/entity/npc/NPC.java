package io.ruin.model.entity.npc;

import io.ruin.Server;
import io.ruin.cache.NPCType;
import io.ruin.model.World;
import io.ruin.model.activities.miscpvm.BasicCombat;
import io.ruin.model.activities.wilderness.Wilderness;
import io.ruin.model.content.combatachievements.CombatAchievementSystem;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.player.Player;
import io.ruin.model.entity.shared.UpdateMask;
import io.ruin.model.entity.shared.listeners.RespawnListener;
import io.ruin.model.entity.shared.masks.AnimationUpdate;
import io.ruin.model.entity.shared.masks.Appearance;
import io.ruin.model.entity.shared.masks.CombatLevelUpdate;
import io.ruin.model.entity.shared.masks.EntityDirectionUpdate;
import io.ruin.model.entity.shared.masks.ForceMovementUpdate;
import io.ruin.model.entity.shared.masks.ForceTextUpdate;
import io.ruin.model.entity.shared.masks.GraphicsUpdate;
import io.ruin.model.entity.shared.masks.HitsUpdate;
import io.ruin.model.entity.shared.masks.MapDirectionUpdate;
import io.ruin.model.entity.shared.masks.ModelTintUpdate;
import io.ruin.model.entity.shared.masks.TransformUpdate;
import io.ruin.model.item.actions.ItemNPCAction;
import io.ruin.model.map.Bounds;
import io.ruin.model.map.Direction;
import io.ruin.model.map.Position;
import io.ruin.model.map.Tile;
import io.ruin.model.map.route.routes.TargetRoute;
import io.ruin.model.stat.StatType;
import io.ruin.rsprot.RSProtService;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rsprot.protocol.game.outgoing.info.AvatarPriority;
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcAvatar;
import net.rsprot.protocol.game.outgoing.info.npcinfo.NpcAvatarExtendedInfo;
import org.rsmod.game.entity.EntityList;
import org.rsmod.game.entity.Npc;
import org.rsmod.game.entity.NpcList;
import org.rsmod.util.EntityHelperKt;
import org.rsmod.util.NPCHelperKt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
public class NPC extends NPCAttributes {

	public transient NPCAction[] actions;
	public transient HashMap<Integer, ItemNPCAction> itemActions;
	public transient ItemNPCAction defaultItemAction;
	public transient Position walkTo;
	public transient Predicate<Position> skipReachCheck;
	public transient boolean skipMovementCheck;
	public transient Bounds spawnBounds;
	public transient RespawnListener respawnListener;
	protected transient NPCCombat combat;
	private transient int id;
	private transient UpdateMask[] masks;
	@Setter
	private transient int weakenedDamage = 0;
	private transient volatile boolean removed = true;
	private transient NPCMovement movement;
	private transient TransformUpdate transformUpdate;
	public transient CombatLevelUpdate cbLvlUpdate;
	protected transient Entity _targetPlayer;
	private transient boolean targetIcon;
	private transient Runnable targetRemovalAction;
	public transient Position spawnPosition;
	public transient Direction spawnDirection;
	public transient Bounds walkBounds, attackBounds;
	public transient boolean defaultSpawn;
	public transient int walkRange;
	private transient BodyCustomization bodyCustomization;

	public DefaultHeadIconIndex overheadPrayer() {
		return overheadPrayer;
	}

	@Setter
	private DefaultHeadIconIndex overheadPrayer = null;

	public NPC(final int id) {
		this.npc = this;

		this.id = id;
		this.weakenedDamage = 0;

		final NPCType def = getDef();
		if (def != null) {
			setSize(def.size);
		}
	}

	public void addWeakenedDamage(Player player, int weakenedDamage) {
		float multiplier = 1;
		switch (CombatAchievementSystem.getTier(player.combatAchievementPoints)) {
			case EASY -> multiplier = 1.02f;
			case MEDIUM -> multiplier = 1.04f;
			case HARD -> multiplier = 1.06f;
			case ELITE -> multiplier = 1.10f;
			case MASTER -> multiplier = 1.20f;
			case GRANDMASTER -> multiplier = 1.35f;
		}
		float damageToAdd = (weakenedDamage * multiplier);
		this.weakenedDamage += (int) damageToAdd;
	}

	public double getDamageBoostFromWeakness() {
		return Math.min((weakenedDamage / 5.0) / 100, 0.5);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Setter
	private NPCType defOverride;

	public NPCType getDef() {
		return NPCType.cached.get(id);
	}

	@Override
	public String getName() {
		final NPCType def = getDef();
		return def == null ? "?" : def.name;
	}

	@Override
	public boolean isLocal(final Player player) {
		log.error("isLocal not for use by NPC");
		return false;
	}

	@Override
	public List<Player> computeLocalPlayers() {
		return EntityHelperKt.searchZonePlayers(this);
	}

	@Override
	public List<NPC> computeLocalNpcs() {
		return EntityHelperKt.searchZoneNpcs(this);
	}

	@Override
	public UpdateMask[] getMasks() {
		return masks;
	}

	@Override
	public NPCMovement getMovement() {
		return movement;
	}

	public boolean isRandomWalkAllowed() {
		if (this.walkBounds == null) {
			return false;
		}

		if (this.isHidden() || this.isLocked()) {
			return false;
		}

		if (position.getRegion().players.isEmpty()) {
			return false;
		}

		if (!getMovement().isAtDestination()) {
			return false;
		}

		return !isMovementBlocked(false, false);
	}

	public void setCombat(NPCCombat combat) {
		this.combat = combat;
		this.combat.npc = this;
	}

	public boolean setCombat() {
		NPCType def = getDef();
		if (def == null || def.combatInfo == null) {
			/* not a combat npc */
			return false;
		}
		if (def.combatHandlerClass != null) {
			try {
				combat = def.combatHandlerClass.getDeclaredConstructor().newInstance().init(this, def.combatInfo);
				return true;
			} catch (Exception e) {
				log.error("Unable to instantiate combat handler for npc: " + id, e);
			}
		}
		combat = new BasicCombat().init(this, def.combatInfo);
		return true;
	}

	public Position getCentrePosition() {
		return new Position(getPosition().getX() + getDef().size / 2, getPosition().getY() + getDef().size / 2,
				getPosition().getZ());
	}

	public NPCCombat getCombat() {
		return combat;
	}

	@Override
	public int setHp(int newHp) {
		return combat == null ? 0 : combat.stats[StatType.Hitpoints.ordinal()].alter(newHp);
	}

	@Override
	public int setMaxHp(int newHp) {
		return combat == null ? 0 : combat.stats[StatType.Hitpoints.ordinal()].set(newHp);
	}

	@Override
	public int getHp() {
		return combat == null ? 0 : combat.stats[StatType.Hitpoints.ordinal()].currentLevel;
	}

	public float getHpPercentage() {
		return (float) this.getHp() / this.getMaxHp();
	}

	@Override
	public int getMaxHp() {
		return combat == null ? 0 : combat.stats[StatType.Hitpoints.ordinal()].fixedLevel;
	}

	/**
	 * Poison
	 */

	@Override
	public boolean isPoisonImmune() {
		if (getCombat() != null && getCombat().getInfo().poison_immunity)
			return true;
		return super.isPoisonImmune();
	}

	@Override
	public boolean isVenomImmune() {
		if (getCombat() != null && getCombat().getInfo().venom_immunity)
			return true;
		return super.isVenomImmune();
	}

	@Override
	public boolean envenom(int damage) {
		if (isVenomImmune() && !isPoisonImmune()) { // npcs that are immune to venom but not to poison are poisoned instead
			// of envenomed
			return poison(6);
		} else {
			return super.envenom(damage);
		}
	}

	public void transform(int id) {
		this.id = id;
		NPCType def = getDef();
		if (def == null) {
			log.error("Unable to find npc def: " + id, new IllegalStateException());
			return;
		}
		transformUpdate.set(this.id);
		setSize(def.size);
		if (combat != null && def.combatInfo != null)
			combat.updateInfo(def.combatInfo);
	}

	public void transformNPC(int id) {
		transformUpdate.set(this.id = id);
		NPCType def = getDef();
		setSize(def.size);
	}

	public void init() {
		this.movement = new NPCMovement(this);
		this.masks = new UpdateMask[] {
				mapDirectionUpdate = new MapDirectionUpdate(),
				cbLvlUpdate = new CombatLevelUpdate(),
				graphicsUpdate = new GraphicsUpdate(),
				hitsUpdate = new HitsUpdate(),
				forceMovementUpdate = new ForceMovementUpdate(),
				forceTextUpdate = new ForceTextUpdate(),
				entityDirectionUpdate = new EntityDirectionUpdate(),
				transformUpdate = new TransformUpdate(),
				animationUpdate = new AnimationUpdate(),
				modelTintUpdate = new ModelTintUpdate()
		};
	}

	/**
	 * Spawn
	 */

	public NPC spawn(Position position) {
		return spawn(position.getX(), position.getY(), position.getZ(), Direction.SOUTH, 0);
	}

	public NPC spawn(Position position, Direction spawnDirection) {
		return spawn(position.getX(), position.getY(), position.getZ(), spawnDirection, 0);
	}

	public NPC spawn(Position position, int walkRange) {
		return spawn(position.getX(), position.getY(), position.getZ(), Direction.SOUTH, walkRange);
	}

	public NPC spawn(int x, int y, int z) {
		return spawn(x, y, z, Direction.SOUTH, 0);
	}

	public NPC spawn(int x, int y, int z, int walkRange) {
		return spawn(x, y, z, Direction.SOUTH, walkRange);
	}

	public NPC spawn(int x, int y, int z, Direction spawnDirection, int walkRange) {
		init();
		this.position = new Position(x, y, z);
		updateFirstChunk();
		this.lastPosition = position.copy();
		this.spawnPosition = new Position(x, y, z);
		this.spawnDirection = spawnDirection;
		this.walkRange = walkRange;
		this.wildernessSpawnLevel = Wilderness.getLevel(spawnPosition);
		Tile.occupy(this);
		checkMulti();
		if (combat != null && combat.info != null && combat.info.spawn_animation != -1) {
			animate(combat.info.spawn_animation);
		}
		if (walkRange > 0)
			this.walkBounds = new Bounds(spawnPosition, walkRange);
		if (setCombat()) {
			this.attackBounds = new Bounds(spawnPosition, getCombat().getAttackBoundsRange());
		}
		this.addToWorld();
		return this;
	}

	public NPC targetPlayer(Player player, boolean showIcon) {
		this._targetPlayer = player;
		this.targetIcon = showIcon;
		player.npcTarget = true;
		if (showIcon)
			player.getPacketSender().sendHintIcon(this);
		return this;
	}

	public NPC targetNPC(NPC npc) {
		this._targetPlayer = npc;
		this.targetIcon = false;
		return this;
	}

	private void removeTarget() {
		if (_targetPlayer != null && _targetPlayer instanceof Player) {
			Player targetPlayer = (Player) _targetPlayer;
			targetPlayer.npcTarget = false;
			if (targetIcon)
				targetPlayer.getPacketSender().resetHintIcon(true);
			targetPlayer = null;
		}
		if (targetRemovalAction != null) {
			targetRemovalAction.run();
			targetRemovalAction = null;
		}
	}

	public void attackTargetPlayer() {
		attackTargetPlayer(null, null);
	}

	public void attackTargetPlayer(Supplier<Boolean> removalCheck) {
		attackTargetPlayer(removalCheck, null);
	}

	public void attackTargetPlayer(Supplier<Boolean> removalCheck, Runnable removalAction) {
		if (combat == null) {
			// todo remove after we find the broken npc lol
			Server.logWarning("NPC " + id + " HAS NO COMBAT SCRIPT SET!");
			return;
		}
		addEvent(e -> {
			int attackAttempts = 0;
			while (true) {
				if (combat.isDead()) {
					/**
					 * Death will automatically remove this npc.
					 * THIS MEANS ANY NPC THAT TARGETS A PLAYER SHOULD HAVE "respawn_ticks": -1 !
					 */
					e.delay(1);
					continue;
				}
				if (removalCheck == null || !removalCheck.get()) {
					if (combat.getTarget() != null) {
						/**
						 * Npc is already attacking target.
						 */
						attackAttempts = 0;
						e.delay(1);
						continue;
					}
					if (++attackAttempts < 5) {
						/**
						 * We're not attacking? Lets try again.
						 * If attack fails 5 times in a row, npc will remove itself!
						 */
						face(_targetPlayer);
						combat.setTarget(_targetPlayer);
						e.delay(1);
						continue;
					}
				}
				remove();
				return;
			}
		});
		this.targetRemovalAction = removalAction;
	}

	public boolean isTargeting(Player player) {
		return _targetPlayer == player;
	}

	public boolean hasTarget() {
		return _targetPlayer != null;
	}

	public void addToWorld() {
		if (!removed) {
			log.error("Tried to add not-removed npc: " + captureState(), new IllegalStateException());
			return;
		}

		this.clearHits();

		final NpcList npcs = World.getNpcs();

		final Integer slot = npcs.nextFreeSlot();
		if (slot == null || slot == EntityList.INVALID_SLOT) {
			log.error("Tried to add NPC to world but no index left: " + captureState(), new IllegalStateException());
			return;
		}

		final Npc occupiedNPC = npcs.get(slot);
		if (occupiedNPC != null) {
			log.error("Tried to add NPC to world but slot is occupied: " + captureState() + " occupied: "
					+ occupiedNPC.getKronos().captureState(), new IllegalStateException());
			return;
		}

		final Npc rsmod = new Npc(this, NPCHelperKt.getRsmod(NPCType.cached.get(id)));
		setRsmodNpc(rsmod);

		npcs.set(slot, rsmod);
		setIndex(slot);

		try {
			final NpcAvatar avatar = RSProtService.createNPCAvatar(this);
			assignAvatar(avatar);
		} catch (final Exception e) {
			final NPC occupied = World.getNpc(getIndex());
			log.error("Unable to allocate avatar for npc. npc: " + captureState() + "occupied: "
					+ (occupied == null ? "null" : occupied.captureState()), e);
		}

		rsmod.doInit();

		this.removed = false;
	}

	public void remove() {
		if (this.removed) {
			return;
		}

		this.removed = true;
		this.clearHits();
		setHidden(true);
		removeTarget();
		World.removeNPC(this);
		this.avatar = null;
	}

	public boolean isRemoved() {
		return removed;
	}

	/**
	 * Reset Actions
	 */

	public void resetActions(boolean resetMovement, boolean resetCombat) {
		if (!isLocked())
			stopEvent(resetCombat);
		if (resetMovement) {
			movement.reset();
			faceNone(false);
			TargetRoute.reset(this);
		}
		if (resetCombat && combat.getTarget() != null)
			combat.reset();
	}

	/**
	 * Processing
	 */

	public void process() {
		if (this.removed) {
			return;
		}

		this.hotifxTargetLost();

		processEvent();

		if (combat != null) {
			combat.process();
		}
		if (combat != null) {
			combat.follow0();
		}
		movement.process();
		if (combat != null) {
			combat.attack0();
		}

		processHits();
	}

	// HOTFIX: whenever player goes offline, it might've been still held
	// as npc target, we do roundtrip to check for it;
	private void hotifxTargetLost() {
		if (this.combat == null) {
			return;
		}

		var target = combat.getTarget();
		if (target == null || target.player == null) {
			return;
		}

		if (!target.player.isOnline()) {
			combat.reset();
		}
	}

	public Position getSpawnPosition() {
		return spawnPosition;
	}

	@Override
	public boolean isNpc() {
		return true;
	}

	public Appearance getAppearance() {
		if (appearance == null)
			appearance = new Appearance();
		return appearance;
	}

	private Appearance appearance;

	public boolean isFullHP() {
		return getHp() == getMaxHp();
	}

	public void assignAvatar(NpcAvatar avatar) {
		this.avatar = avatar;
	}

	public NpcAvatar avatar() {
		return this.avatar;
	}

	public NpcAvatarExtendedInfo avatarExtended() {
		return this.avatar().getExtendedInfo();
	}

	@AllArgsConstructor
	public enum DefaultHeadIconIndex {
		ProtectFromMelee(0),
		ProtectFromRanged(1),
		ProtectFromMagic(2),
		Retribution(3),
		Smite(4),
		Redemption(5),
		RangeAndMage(6),
		RangeAndMelee(7),
		MageAndMelee(8),
		MageRangeMelee(9),
		Wrath(10),
		Soulsplit(11);

		public final int id;
	}

	public void setHeadIcon(DefaultHeadIconIndex icon) {
		if (this.avatar == null) {
			log.warn("NPC not initialized: " + this.captureState());
			return;
		}
		this.setOverheadPrayer(icon);
		this.avatarExtended().setHeadIconChange(0, 440, icon.id);
	}

	public void removeHeadIcon() {
		if (this.avatar == null) {
			log.warn("NPC not initialized: " + this.captureState());
			return;
		}
		this.setOverheadPrayer(null);
		this.avatarExtended().setHeadIconChange(0, -1, -1);
	}

	@Override
	public String captureState() {
		return String.format("%s pos=%s pos_spawn=%s id=%s index=%s hp=%s, tasks=%s, hits=%s target=%s size=%s removed=%s",
				getName(),
				getPosition().getX() + "," + getPosition().getY() + "," + getPosition().getPlane(),
				getSpawnPosition().getX() + "," + getSpawnPosition().getY() + "," + getSpawnPosition().getPlane(),
				getId(),
				getIndex(),
				this.getHp(),
				backgroundEvents == null ? 0 : backgroundEvents.size(),
				queuedHits,
				combat == null ? "none" : combat.getTarget(),
				getSize(),
				this.removed);
	}

	public int getSpawnAvatarDirection() {
		return spawnDirection.ordinal();
	}

	public AvatarPriority getSpawnAvatarPriority() {
		final NPCType def = getDef();
		return def == null || !def.lowPriorityFollowerOps
				? AvatarPriority.NORMAL
				: AvatarPriority.LOW;
	}

	public boolean isSpawnAvatarSpecific() {
		return false;
	}

	private transient Npc rsmodNpc;

	@Override
	public Npc rsmod() {
		return rsmodNpc;
	}

	public void setRsmodNpc(final Npc rsmodNpc) {
		this.rsmodNpc = rsmodNpc;
	}

	public void resetBodyCustomization() {
		setBodyCustomization(null);
	}

	public void setModelCustomization(int... models) {
		setBodyCustomization(BodyCustomization.build(id, models, null, null, null));
	}

	public void setColourCustomization(int... colours) {
		var coloursAsShort = new short[colours.length];
		for (int index = 0; index < coloursAsShort.length; index++) {
			coloursAsShort[index] = (short) colours[index];
		}
		setBodyCustomization(BodyCustomization.build(id, null, coloursAsShort, null, null));
	}

	public void setBodyCustomization(BodyCustomization bodyCustomization) {
		// NOTE: any npc with an id of > 16383 must not use body or head customisation
		// extended info;
		if (this.getId() > 16383) {
			return;
		}

		if (this.avatar == null) {
			return;
		}

		this.bodyCustomization = bodyCustomization;

		if (bodyCustomization == null || bodyCustomization.getEmpty()) {
			this.avatar.getExtendedInfo().resetBodyCustomisations();
			return;
		}
		if (Boolean.TRUE.equals(bodyCustomization.getLocalPlayer())) {
			avatar.getExtendedInfo().setBodyCustomisationMirrored();
			return;
		}
		List<Integer> models = Collections.emptyList();
		if (bodyCustomization.getModels() != null) {
			models = new ArrayList<>();
			for (int model : bodyCustomization.getModels()) {
				models.add(model);
			}
		}
		List<Integer> recolours = Collections.emptyList();
		if (bodyCustomization.getRecolors() != null) {
			recolours = new ArrayList<>();
			for (int model : bodyCustomization.getRecolors()) {
				recolours.add(model);
			}
		}
		List<Integer> retextures = Collections.emptyList();
		if (bodyCustomization.getRetextures() != null) {
			retextures = new ArrayList<>();
			for (int model : bodyCustomization.getRetextures()) {
				retextures.add(model);
			}
		}

		this.avatar.getExtendedInfo().setBodyCustomisation(models, recolours, retextures);
	}
}

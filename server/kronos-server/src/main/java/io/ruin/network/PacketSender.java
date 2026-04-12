package io.ruin.network;

import io.ruin.api.buffer.OutBuffer;
import io.ruin.api.utils.StringUtils;
import io.ruin.cache.InterfaceDef;
import io.ruin.data.impl.teleports;
import io.ruin.model.World;
import io.ruin.model.entity.Entity;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.inter.AccessMask;
import io.ruin.model.inter.AccessMasks;
import io.ruin.model.inter.ClientInterfaceType;
import io.ruin.model.inter.ComponentInfo;
import io.ruin.model.inter.Subcomponent;
import io.ruin.model.inter.ToplevelComponent;
import io.ruin.model.inter.ToplevelInterfaceType;
import io.ruin.model.inter.Widget;
import io.ruin.model.inter.WidgetInfo;
import io.ruin.model.inter.questtab.QuestTabCategory;
import io.ruin.model.item.Item;
import io.ruin.model.map.Position;
import io.ruin.model.map.Region;
import io.ruin.model.map.ground.GroundItem;
import io.ruin.model.shop.ShopItem;
import io.ruin.network.incoming.handlers.DisplayHandler;
import io.ruin.rsprot.RSProtService;
import io.ruin.utility.Random;
import lombok.extern.slf4j.Slf4j;
import net.rsprot.crypto.xtea.XteaKey;
import net.rsprot.protocol.api.Session;
import net.rsprot.protocol.common.game.outgoing.inv.InventoryObject;
import net.rsprot.protocol.game.outgoing.camera.CamLookAt;
import net.rsprot.protocol.game.outgoing.camera.CamMoveTo;
import net.rsprot.protocol.game.outgoing.camera.CamReset;
import net.rsprot.protocol.game.outgoing.camera.CamShake;
import net.rsprot.protocol.game.outgoing.interfaces.IfCloseSub;
import net.rsprot.protocol.game.outgoing.interfaces.IfMoveSub;
import net.rsprot.protocol.game.outgoing.interfaces.IfOpenSub;
import net.rsprot.protocol.game.outgoing.interfaces.IfOpenTop;
import net.rsprot.protocol.game.outgoing.interfaces.IfSetAngle;
import net.rsprot.protocol.game.outgoing.interfaces.IfSetAnim;
import net.rsprot.protocol.game.outgoing.interfaces.IfSetEventsV1;
import net.rsprot.protocol.game.outgoing.interfaces.IfSetEventsV2;
import net.rsprot.protocol.game.outgoing.interfaces.IfSetHide;
import net.rsprot.protocol.game.outgoing.interfaces.IfSetModel;
import net.rsprot.protocol.game.outgoing.interfaces.IfSetNpcHead;
import net.rsprot.protocol.game.outgoing.interfaces.IfSetObject;
import net.rsprot.protocol.game.outgoing.interfaces.IfSetPlayerHead;
import net.rsprot.protocol.game.outgoing.interfaces.IfSetPlayerModelBodyType;
import net.rsprot.protocol.game.outgoing.interfaces.IfSetPosition;
import net.rsprot.protocol.game.outgoing.interfaces.IfSetText;
import net.rsprot.protocol.game.outgoing.inv.UpdateInvFull;
import net.rsprot.protocol.game.outgoing.inv.UpdateInvPartial;
import net.rsprot.protocol.game.outgoing.inv.UpdateInvStopTransmit;
import net.rsprot.protocol.game.outgoing.logout.Logout;
import net.rsprot.protocol.game.outgoing.map.RebuildLogin;
import net.rsprot.protocol.game.outgoing.map.RebuildNormal;
import net.rsprot.protocol.game.outgoing.map.RebuildRegion;
import net.rsprot.protocol.game.outgoing.map.util.RebuildRegionZone;
import net.rsprot.protocol.game.outgoing.map.util.XteaProvider;
import net.rsprot.protocol.game.outgoing.misc.client.HintArrow;
import net.rsprot.protocol.game.outgoing.misc.client.HintArrow.HintArrowType;
import net.rsprot.protocol.game.outgoing.misc.client.MinimapToggle;
import net.rsprot.protocol.game.outgoing.misc.client.SendPing;
import net.rsprot.protocol.game.outgoing.misc.client.UpdateRebootTimer;
import net.rsprot.protocol.game.outgoing.misc.client.UrlOpen;
import net.rsprot.protocol.game.outgoing.misc.player.MessageGame;
import net.rsprot.protocol.game.outgoing.misc.player.RunClientScript;
import net.rsprot.protocol.game.outgoing.misc.player.SetMapFlag;
import net.rsprot.protocol.game.outgoing.misc.player.SetPlayerOp;
import net.rsprot.protocol.game.outgoing.misc.player.UpdateRunEnergy;
import net.rsprot.protocol.game.outgoing.misc.player.UpdateRunWeight;
import net.rsprot.protocol.game.outgoing.misc.player.UpdateStatV2;
import net.rsprot.protocol.game.outgoing.social.MessagePrivate;
import net.rsprot.protocol.game.outgoing.social.MessagePrivateEcho;
import net.rsprot.protocol.game.outgoing.sound.MidiSongV2;
import net.rsprot.protocol.game.outgoing.sound.SynthSound;
import net.rsprot.protocol.game.outgoing.varp.VarpLarge;
import net.rsprot.protocol.game.outgoing.varp.VarpSmall;
import net.rsprot.protocol.game.outgoing.zone.header.UpdateZoneFullFollows;
import net.rsprot.protocol.game.outgoing.zone.payload.LocAddChangeV2;
import net.rsprot.protocol.game.outgoing.zone.payload.LocAnim;
import net.rsprot.protocol.game.outgoing.zone.payload.LocDel;
import net.rsprot.protocol.game.outgoing.zone.payload.MapAnim;
import net.rsprot.protocol.game.outgoing.zone.payload.MapProjAnimV1;
import net.rsprot.protocol.game.outgoing.zone.payload.ObjAdd;
import net.rsprot.protocol.game.outgoing.zone.payload.ObjCount;
import net.rsprot.protocol.game.outgoing.zone.payload.ObjDel;
import net.rsprot.protocol.game.outgoing.zone.payload.SoundArea;
import net.rsprot.protocol.message.OutgoingGameMessage;
import org.jetbrains.annotations.NotNull;
import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.ruin.api.protocol.ServerProt204.*;

@Slf4j
public final class PacketSender {

	private static final XteaProvider DEFAULT_XTEA_PROVIDER = new XteaProvider() {
		@Override
		public @NotNull XteaKey provide(final int mapsquareId) {
			return XteaKey.Companion.getZERO();
		}
	};

	private static final int DEFAULT_WORLD_AREA_ID = -1;

	private final Player player;

	public PacketSender(final Player player) {
		this.player = player;
	}

	public void write(final OutgoingGameMessage out) {
		if (out == null) {
			return;
		}

		final Session<Player> rsprotSession = player.rsprotSession;
		if (rsprotSession == null) {
			return;
		}
		rsprotSession.queue(out);
	}

	public void sendReceivePM(String fromName, int fromRank, String message) {
		if (fromName.isEmpty() || fromName.length() > 15) {
			return;
		}
		if (message.length() > 50) {
			return;
		}
		var msg = new MessagePrivate(fromName, -1, Random.getInt(), fromRank, message);
		write(msg);
	}

	public void sendPM(String toUsername, String message) {
		if (toUsername.isEmpty() || toUsername.length() > 15) {
			return;
		}
		if (message.length() > 50) {
			return;
		}
		var msg = new MessagePrivateEcho(toUsername, message);
		write(msg);
	}

	public static void sendPrivateMessage(Player player, String name, String message) {
		Player receiver = World.getPlayer(name);
		if (receiver == null) {
			return;
		}
		if (message.length() > 50) {
			return;
		}
		if (name == null || name.length() > 15) {
			return;
		}
		message = StringUtils.fixCaps(message);
		player.getPacketSender().sendPM(name, message);
		var icon = player.getMessagingRank().raw;
		receiver.getPacketSender().sendReceivePM(player.getName(), icon, message);
	}

	public void sendLogout() {
		this.write(Logout.INSTANCE);
		if (this.player.rsprotSession != null) {
			this.player.rsprotSession.flush();
		}
	}

	public void sendRegion(boolean login) {
		player.removeFromRegions();
		Position position = player.getPosition();
		Region region = player.lastRegion = position.getRegion();
		int chunkX = position.getChunkX();
		int chunkY = position.getChunkY();
		int depth = Region.CLIENT_SIZE >> 4;
		boolean dynamic = region.dynamicData != null;
		if (login || !dynamic) {
			if (login) {
				player.rsprotPlayerInfo.updateCoord(
						position.getZ(),
						position.getX(),
						position.getY());

				player.rsprotSession.queue(
						new RebuildLogin(
								chunkX, chunkY,
								DEFAULT_WORLD_AREA_ID, DEFAULT_XTEA_PROVIDER,
								player.rsprotPlayerInfo));
			} else {
				player.rsprotSession.queue(
						new RebuildNormal(
								chunkX, chunkY,
								DEFAULT_WORLD_AREA_ID, DEFAULT_XTEA_PROVIDER));
			}
			if (dynamic) {
				// Dynamic region must be sent after regular region on login.
				// this is still the case in 171 since they changed how this packet is sent
				player.getUpdater().updateRegion = true;
				chunkX = chunkY = 0;
			}

			for (int xCalc = (chunkX - depth) / 8; xCalc <= (chunkX + depth) / 8; xCalc++) {
				for (int yCalc = (chunkY - depth) / 8; yCalc <= (chunkY + depth) / 8; yCalc++) {
					int regionId = yCalc + (xCalc << 8);
					Region r = Region.get(regionId);
					player.addRegion(r);
				}
			}
		} else {

			ArrayList<Integer> chunkRegionIds = new ArrayList<>();
			var zoneProvider = new RebuildRegion.RebuildRegionZoneProvider() {
				@Override
				public RebuildRegionZone provide(int x, int y, int z) {
					var regionIndex = (x / 8) << 8 | (y / 8);
					var region = Region.LOADED[regionIndex];
					if (region.dynamicData == null) {
						return null;
					}
					var data = region.dynamicData[z][x % 8][y % 8];
					var rotation = data[0] >>> 1 & 0x3;
					var zoneY = data[0] >>> 3 & 0x7FF;
					var zoneX = data[0] >>> 14 & 0x3FF;
					var zoneZ = data[0] >>> 24 & 0x3;
					var key = XteaKey.Companion.getZERO();
					return new RebuildRegionZone(zoneX, zoneY, zoneZ, rotation, key);
				}

				@Override
				public int getMapsquareId(int x, int y) {
					return ((x & 0x7FF >>> 3) << 8) | (y & 0x7FF >>> 3);
				}
			};

			var msg = new RebuildRegion(chunkX, chunkY, true, zoneProvider);
			write(msg);

			for (int pointZ = 0; pointZ < 4; pointZ++) {
				for (int xCalc = (chunkX - depth); xCalc <= (chunkX + depth); xCalc++) {
					for (int yCalc = (chunkY - depth); yCalc <= (chunkY + depth); yCalc++) {
						int regionIndex = (xCalc / 8) << 8 | (yCalc / 8);
						Region r;
						if (regionIndex >= 0 && regionIndex < Region.LOADED.length) {
							r = Region.LOADED[regionIndex];
						} else {
							player.sendMessage("Failed to load region, try again.");
							break;
						}
						if (r == null || r.dynamicData == null || r.dynamicIndex != region.dynamicIndex) {
							continue;
						}
						int[] chunkData = r.dynamicData[pointZ][xCalc % 8][yCalc % 8];
						int chunkHash = chunkData[0];
						int chunkRegionId = chunkData[1];
						if (chunkHash == 0 || chunkRegionId == 0) {
							continue;
						}
						if (!chunkRegionIds.contains(chunkRegionId))
							chunkRegionIds.add(chunkRegionId);
						if (!player.getRegions().contains(r))
							player.addRegion(r);
					}
				}
			}
		}
		Region.update(player);
	}

	public void sendModelInformation(int parentId, int childId, int zoom, int rotationX, int rotationY) {
		OutBuffer out = new OutBuffer(11).sendFixedPacket(IF_SETANGLE)
				.addLEShortA(zoom).addShortA(rotationY).addShortA(rotationX).addIntME2(parentId << 16 | childId);
		var msg = new IfSetAngle(parentId, childId, rotationX, rotationY, zoom);
		write(msg);
	}

	public void sendPing() {
		var msg = new SendPing(69, 420);
		write(msg);
	}

	public void sendToplevel(ToplevelInterfaceType id) {
		player.setToplevelType(id);
		var msg = new IfOpenTop(id.getInterfaceId());
		write(msg);
	}

	public void sendUrl(String url, boolean copy) {
		var msg = new UrlOpen(url);
		write(msg);
	}

	/**
	 * Sends the default interface of a {@link ToplevelComponent} to the supplied toplevel using the supplied overlay type
	 * rather than inheriting it from {@link ToplevelComponent}. The given {@code ToplevelComponent} must have an
	 * interfaceId set!
	 *
	 * @param mode The toplevel to send the interface to
	 * @param pos The toplevel child component containing the default interface
	 * @param overlayType The client overlay type modal/overlay
	 */
	public void sendInterface(ToplevelInterfaceType mode, ToplevelComponent pos, ClientInterfaceType overlayType) {
		int interfaceId = pos.getInterface(mode);
		int parentId = mode.getInterfaceId();
		int childId = pos.getComponent(mode);
		openSubInterface(interfaceId, parentId, childId, overlayType);
	}

	/**
	 * Sends the default interface of a {@link ToplevelComponent} to the supplied toplevel. The given
	 * {@code ToplevelComponent} must have an interfaceId set!
	 *
	 * @param mode The toplevel to send the interface to
	 * @param pos The toplevel child component containing the default interface
	 */
	public void sendInterface(ToplevelInterfaceType mode, ToplevelComponent pos) {
		int interfaceId = pos.getInterface(mode);
		int parentId = mode.getInterfaceId();
		int childId = pos.getComponent(mode);
		openSubInterface(interfaceId, parentId, childId, pos.getInterfaceType());
	}

	/**
	 * Sends a sub interface on the supplied toplevel at the child determined by the {@link ToplevelComponent} and using
	 * the supplied overlay type rather than inheriting it from {@link ToplevelComponent}
	 *
	 * @param interfaceId The interface ID to send
	 * @param mode The toplevel to send the interface on
	 * @param pos The child component to draw the sub interface on
	 * @param overlayType The client overlay type modal/overlay
	 */
	public void sendInterface(int interfaceId, ToplevelInterfaceType mode, ToplevelComponent pos,
			ClientInterfaceType overlayType) {
		int parentId = mode.getInterfaceId();
		int childId = pos.getComponent(mode);
		openSubInterface(interfaceId, parentId, childId, overlayType);
	}

	/**
	 * Sends a sub interface on the supplied toplevel at the child determined by the {@link ToplevelComponent}
	 *
	 * @param interfaceId The interface ID to send
	 * @param mode The toplevel to send the interface on
	 * @param pos The child component to draw the sub interface on
	 */
	public void sendInterface(int interfaceId, ToplevelInterfaceType mode, ToplevelComponent pos) {
		if (!player.hasDisplay()) {
			return;
		}
		int parentId = mode.getInterfaceId();
		int childId = pos.getComponent(mode);
		openSubInterface(interfaceId, parentId, childId, pos.getInterfaceType());
	}

	/**
	 * Sends a sub interface on the player's current toplevel at the child determined by the {@link ToplevelComponent} but
	 * with custom overlay type
	 *
	 * @param interfaceId The interface ID to send
	 * @param pos The component to send the interface at
	 * @param overlayType The client overlay type modal/overlay
	 */
	public void sendInterface(int interfaceId, ToplevelComponent pos, ClientInterfaceType overlayType) {
		if (!player.hasDisplay()) {
			log.warn("Display not initialized. " + player.getName());
			return;
		}
		int parentId = player.getToplevelType().getInterfaceId();
		int childId = pos.getComponent(player.getToplevelType());
		openSubInterface(interfaceId, parentId, childId, overlayType);
	}

	/**
	 * Sends a sub interface on the player's current toplevel at the child determined by the {@link ToplevelComponent}
	 *
	 * @param interfaceId The interface ID to send
	 * @param pos The component to send the interface at
	 */
	public void sendInterface(int interfaceId, ToplevelComponent pos) {
		if (!player.hasDisplay()) {
			log.warn("Display not initialized. " + player.getName());
			return;
		}
		int parentId = player.getToplevelType().getInterfaceId();
		int childId = pos.getComponent(player.getToplevelType());
		openSubInterface(interfaceId, parentId, childId, pos.getInterfaceType());
	}

	/**
	 * Sends a sub interface {@param interfaceId} at the child component {@param childId} on the default interface id of
	 * the supplied {@link ToplevelComponent}. The given {@code ToplevelComponent} must have an interfaceId set!
	 *
	 * @param interfaceId The sub interface to open
	 * @param childId The child component in the parent interface to draw the sub interface at
	 * @param parentToplevel The parent interface supplied by the default interface in {@link ToplevelComponent}
	 */
	public void sendToplevelSubInterface(int interfaceId, int childId, ToplevelComponent parentToplevel) {
		int parentId = parentToplevel.getInterfaceId();
		openSubInterface(interfaceId, parentId, childId, parentToplevel.getInterfaceType());
	}

	/**
	 * Sends a sub interface at the child component defined in {@link Subcomponent} on the default interface id of the
	 * supplied {@link ToplevelComponent}. The given {@code ToplevelComponent} must have an interfaceId set!
	 *
	 * @param interfaceId The sub interface to open
	 * @param childId The child component in the parent interface to draw the sub interface at
	 * @param parentToplevel The parent interface supplied by the default interface in {@link ToplevelComponent}
	 */
	public void sendToplevelSubInterface(int interfaceId, Subcomponent childId, ToplevelComponent parentToplevel) {
		int parentId = parentToplevel.getInterfaceId();
		openSubInterface(interfaceId, parentId, childId.getComponent(), parentToplevel.getInterfaceType());
	}

	/**
	 * Sends a sub interface {@param interfaceId} at the child component {@param childId} on the player's current
	 * toplevel.
	 *
	 * @param interfaceId The sub interface to open
	 * @param childId The child component in the parent interface to draw the sub interface at
	 * @param type The overlay type to send (modal/overlay)
	 */
	public void sendToplevelSubInterface(int interfaceId, int childId, ClientInterfaceType type) {
		if (!player.hasDisplay()) {
			log.warn("Display not initialized. " + player.getName());
			return;
		}
		int parentId = player.getToplevelType().getInterfaceId();
		openSubInterface(interfaceId, parentId, childId, type);

	}

	/**
	 * Sends a sub interface at the child component defined in {@link Subcomponent} on the player's current toplevel.
	 *
	 * @param interfaceId The sub interface to open
	 * @param childId The child component in the parent interface to draw the sub interface at
	 * @param type The overlay type to send (modal/overlay)
	 */
	public void sendToplevelSubInterface(int interfaceId, Subcomponent childId, ClientInterfaceType type) {
		if (!player.hasDisplay()) {
			log.warn("Display not initialized. " + player.getName());
			return;
		}
		int parentId = player.getToplevelType().getInterfaceId();
		openSubInterface(interfaceId, parentId, childId.getComponent(), type);
	}

	/**
	 * Sends the default sub interface of a {@code ToplevelComponent} to the player's current
	 * {@link Player#getToplevelType()}. The given {@code ToplevelComponent} must have an interfaceId set!
	 *
	 * @param component supplies the interface ID and child component
	 * @param overlayType The type of client interface to draw (modal or overlay)
	 */
	public void sendInterface(ToplevelComponent component, ClientInterfaceType overlayType) {
		if (!player.hasDisplay()) {
			log.warn("Display not initialized. " + player.getName());
			return;
		}
		int interfaceId = component.getInterface(player);
		int parentId = player.getToplevelType().getInterfaceId();
		int childId = component.getComponent(player);
		openSubInterface(interfaceId, parentId, childId, overlayType);
	}

	/**
	 * Sends the default sub interface of a {@code ToplevelComponent} to the top level interface using
	 * {@link Player#getToplevelType()} for the parent interface. The given {@code ToplevelComponent} must have an
	 * interfaceId set!
	 *
	 * @param component supplies the interface ID and child component
	 */
	public void sendToplevelSubInterface(ToplevelComponent component) {
		if (!player.hasDisplay()) {
			log.warn("Display not initialized. " + player.getName());
			return;
		}
		int parentId = player.getToplevelType().getInterfaceId();
		int interfaceId = component.getInterface(player);
		int childId = component.getComponent(player);
		openSubInterface(interfaceId, parentId, childId, component.getInterfaceType());
	}

	/**
	 * Sends IF_OPENSUB packet. Opens a sub interface on the given parent interface and child component.
	 * <p>
	 * For example if you draw the lobby interface (378) on the full screen toplevel (165) at child 37, you would call
	 * openSubInterface(378, 165, 37, MODAL)
	 *
	 * @param interfaceId The interface ID to draw
	 * @param parentId The parent interface to draw the sub interface on
	 * @param childId The child in the parent interface at which to draw the sub interface
	 * @param overlayType The type of client interface to draw (modal or overlay)
	 */
	public void openSubInterface(int interfaceId, int parentId, int childId, ClientInterfaceType overlayType) {
		player.setVisibleInterface(interfaceId, parentId, childId);
		if (InterfaceDef.COUNTS.length < interfaceId) {
			return;
		}
		if (InterfaceDef.COUNTS.length < parentId) {
			return;
		}
		if (InterfaceDef.COUNTS[parentId] < childId) {
			return;
		}
		var msg = new IfOpenSub(parentId << 16 | childId, interfaceId, overlayType == ClientInterfaceType.OVERLAY ? 1 : 0);
		write(msg);
	}

	public void sendModel(int parentId, int childId, int modelId) {
		if (InterfaceDef.COUNTS[parentId] < childId) {
			return;
		}
		var msg = new IfSetModel(parentId, childId, modelId);
		write(msg);
	}

	public void removeSubInterface(int parentId, int childId) {
		if (!InterfaceDef.valid(parentId, childId)) {
			return;
		}
		player.removeVisibleInterface(parentId, childId);
		var msg = new IfCloseSub(parentId, childId);
		write(msg);
	}

	/**
	 * Removes a child subcomponent from a parent interface defined in ToplevelComponent
	 *
	 * @param parent Must have an interfaceId set!
	 * @param child
	 */
	public void removeSubtoplevelInterface(ToplevelComponent parent, Subcomponent child) {
		var parentId = parent.getInterfaceId();
		var childId = child.getComponent();
		if (!InterfaceDef.valid(parentId, childId)) {
			return;
		}
		player.removeVisibleInterface(parent.getInterfaceId(), child.getComponent());
		var msg = new IfCloseSub(parent.getInterfaceId(), child.getComponent());
		write(msg);
	}

	public void removeInterface(ToplevelInterfaceType mode, int childId) {
		if (!player.hasDisplay()) {
			log.warn("Display not initialized. " + player.getName());
			return;
		}
		if (!InterfaceDef.valid(mode.getInterfaceId(), childId)) {
			return;
		}
		player.removeVisibleInterface(mode.getInterfaceId(), childId);
		var msg = new IfCloseSub(mode.getInterfaceId(), childId);
		write(msg);
	}

	public void removeInterface(ToplevelComponent position) {
		if (!player.hasDisplay()) {
			log.warn("Display not initialized. " + player.getName());
			return;
		}

		int interfaceId = player.getToplevelType().getInterfaceId();
		int childId = position.getComponent(player);
		if (!InterfaceDef.valid(interfaceId, childId)) {
			return;
		}
		player.removeVisibleInterface(interfaceId, childId);
		var msg = new IfCloseSub(interfaceId, childId);
		write(msg);
	}

	public void moveInterface(int fromParentId, int fromChildId, int toParentId, int toChildId) {
		if (!InterfaceDef.valid(fromParentId, fromChildId)) {
			return;
		}
		if (!InterfaceDef.valid(toParentId, toChildId)) {
			return;
		}
		player.moveVisibleInterface(fromParentId, fromChildId, toParentId, toChildId);
		var msg = new IfMoveSub(fromParentId, fromChildId, toParentId, toChildId);
		write(msg);
	}

	public void sendString(int interfaceId, int childId, String string) {
		if (!InterfaceDef.valid(interfaceId, childId)) {
			return;
		}
		var msg = new IfSetText(interfaceId, childId, string);
		write(msg);
	}

	public void setHidden(int interfaceId, int childId, boolean hide) {
		if (!InterfaceDef.valid(interfaceId, childId)) {
			return;
		}
		var msg = new IfSetHide(interfaceId, childId, hide);
		write(msg);
	}

	public void sendItem(int parentId, int childId, int itemId, int amount) {
		if (!InterfaceDef.valid(parentId, childId)) {
			return;
		}
		var msg = new IfSetObject(parentId, childId, itemId, amount);
		write(msg);
	}

	public void setAlignment(int parentId, int childId, int x, int y) {
		if (!InterfaceDef.valid(parentId, childId)) {
			return;
		}
		var msg = new IfSetPosition(parentId, childId, x, y);
		write(msg);
	}

	public void sendIfEvents(WidgetInfo widgetInfo, int minChildID, int maxChildID, AccessMask... accessMask) {
		sendIfEvents(widgetInfo, minChildID, maxChildID, AccessMasks.combine(accessMask));
	}

	public void sendIfEvents(WidgetInfo widgetInfo, int minChildID, int maxChildID, int... masks) {
		sendIfEvents(widgetInfo.getGroupId(), widgetInfo.getChildId(), minChildID, maxChildID, masks);
	}

	public void sendIfEvents(boolean debug, int interfaceId, int childParentId, int minChildId, int maxChildId,
			AccessMask... accessMask) {
		sendIfEvents(debug, interfaceId, childParentId, minChildId, maxChildId, AccessMasks.combine(accessMask));
	}

	public void sendIfEvents(int interfaceId, int childParentId, int minChildId, int maxChildId,
			AccessMask... accessMask) {
		sendIfEvents(true, interfaceId, childParentId, minChildId, maxChildId, accessMask);
	}

	public void sendIfEvents(int interfaceId, int childParentId, int minChildId, int maxChildId, int... masks) {
		sendIfEvents(true, interfaceId, childParentId, minChildId, maxChildId, masks);
	}

	public void sendIfEvents(boolean debug, int interfaceId, int childParentId, int minChildId, int maxChildId,
			int... masks) {
		if (!InterfaceDef.valid(interfaceId, childParentId)) {
			return;
		}
		int mask = AccessMasks.combine(masks);
		var msg = new IfSetEventsV1(interfaceId, childParentId, minChildId, maxChildId, mask);
		write(msg);
	}

	public void sendClientScript(int id, Object... params) {
		var msg = new RunClientScript(id, Arrays.asList(params));
		write(msg);
	}

	public void sendClientScript(int id, String type, Object... params) {
		if (params.length != type.length()) {
			log.warn("warn: mismatch cs2 attempted: {} vs {}", type, params);
			for (int i = 0; i < type.length() - 1; i++) {
				if (i < params.length) {
					params[i] = "";
				}
			}
		}

		var msg = new RunClientScript(id, Arrays.asList(params));
		write(msg);
	}

	public void sendSystemUpdate(int time) {
		var msg = new UpdateRebootTimer(time * 50 / 30);
		write(msg);
	}

	public void setTextStyle(int parentId, int childId, int horizontalAlignment, int verticalAlignment, int lineHeight) {
		sendClientScript(600, "iiiI", horizontalAlignment, verticalAlignment, lineHeight, parentId << 16 | childId);
	}

	public void fadeIn() {
		sendClientScript(948, "iiiii", 0, 0, 0, 255, 50);
	}

	public void fadeOut() {
		this.sendInterface(174, ToplevelComponent.OVERLAY);
		sendClientScript(951, "");
	}

	public void clearFade() {
		sendClientScript(948, "iiiii", 0, 0, 0, 255, 0);
	}

	public void sendMessage(String message, String sender, int type) {
		var msg = new MessageGame(type, sender, message);
		write(msg);
	}

	public void sendVarp(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			var msg = new VarpLarge(id, value);
			write(msg);
		} else {
			var msg = new VarpSmall(id, value);
			write(msg);
		}
	}

	public void sendItems(int containerId, Item... items) {
		sendItems(-1, containerId, items, items.length);
	}

	public void sendItems(int parentId, int childId, int containerId, Item... items) {
		sendItems(parentId << 16 | childId, containerId, items, items.length);
	}

	public void sendItems(int parentId, int childId, int containerId, Item[] items, int length) {
		sendItems(parentId << 16 | childId, containerId, items, length);
	}

	public void sendItems(ComponentInfo componentInfo, int containerId, Item[] items, int length) {
		sendItems(componentInfo.getPackedId(), containerId, items, length);
	}

	public void ifSetBodyType(int inter, int component, int bodyType) {
		var msg = new IfSetPlayerModelBodyType(inter, component, bodyType);
		write(msg);
	}

	public void sendItems(int containerId, List<Item> items) {
		var msg = new UpdateInvFull(containerId, items.size(), (slot) -> {
			var item = items.get(slot);
			if (item == null) {
				return InventoryObject.NULL;
			}
			var amount = item.getAmount();
			if (amount < 0) {
				log.warn("Item " + item + " amount overflow for a player: " + player.captureState(),
						new IllegalStateException());
				amount = Integer.MAX_VALUE;
			}
			return InventoryObject.pack(item.getId(), amount);
		});
		write(msg);
	}

	public void sendItems(int interfaceHash, int containerId, Item[] items, int length) {
		var msg = new UpdateInvFull(interfaceHash, containerId, items.length, (slot) -> {
			var item = items[slot];
			if (item == null) {
				return InventoryObject.NULL;
			}
			var amount = item.getAmount();
			if (amount < 0) {
				log.warn("Item " + item + " amount overflow for a player: " + player.captureState());
				amount = Integer.MAX_VALUE;
			}
			return InventoryObject.pack(item.getId(), amount);
		});
		write(msg);
	}

	public void sendShopItems(int interfaceHash, int containerId, ShopItem[] items, int length) {
		var msg = new UpdateInvFull(interfaceHash, containerId, items.length, (slot) -> {
			var item = items[slot];
			if (item == null) {
				return InventoryObject.NULL;
			}
			var amount = item.getAmount();
			if (amount < 0) {
				log.warn("Item " + item + " amount overflow for a player: " + player.captureState());
				amount = Integer.MAX_VALUE;
			}
			return InventoryObject.pack(item.getId(), amount);
		});
		write(msg);
	}

	public void updateItems(int interfaceHash, int containerId, Item[] items, boolean[] updatedSlots, int updatedCount) {
		var updatedIndicies = new ArrayList<Integer>();
		for (var slot = 0; slot < updatedSlots.length; slot++) {
			if (updatedSlots[slot]) {
				updatedIndicies.add(slot);
			}
		}
		var indicies = new UpdateInvPartial.IndexedObjectProvider(updatedIndicies.iterator()) {
			@Override
			public long provide(int slot) {
				var item = items[slot];
				if (item == null) {
					return InventoryObject.pack(slot, -1, -1);
				}

				var amount = item.getAmount();
				if (amount < 0) {
					log.warn("Item " + item + " amount overflow for a player: " + player.captureState());
					amount = Integer.MAX_VALUE;
				}

				return InventoryObject.pack(slot, item.getId(), amount);
			}
		};
		var msg = new UpdateInvPartial(interfaceHash, containerId, indicies);
		write(msg);
	}

	public void updateShopItems(int interfaceHash, int containerId, ShopItem[] items, boolean[] updatedSlots,
			int updatedCount) {
		var updatedIndicies = new ArrayList<Integer>();
		for (var slot = 0; slot < updatedSlots.length; slot++) {
			if (updatedSlots[slot]) {
				updatedIndicies.add(slot);
			}
		}
		var indicies = new UpdateInvPartial.IndexedObjectProvider(updatedIndicies.iterator()) {
			@Override
			public long provide(int slot) {
				var item = items[slot];
				if (item == null) {
					return InventoryObject.pack(slot, -1, -1);
				}

				var amount = item.getAmount();
				if (amount < 0) {
					log.warn("Item " + item + " amount overflow for a player: " + player.captureState());
					amount = Integer.MAX_VALUE;
				}

				return InventoryObject.pack(slot, item.getId(), amount);
			}
		};
		var msg = new UpdateInvPartial(interfaceHash, containerId, indicies);
		write(msg);
	}

	public void unlinkItems(int containerId) {
		var msg = new UpdateInvStopTransmit(containerId);
		write(msg);
	}

	public void sendStat(int id, int currentLevel, int experience) {
		var msg = new UpdateStatV2(id, currentLevel, currentLevel, experience);
		write(msg);
	}

	public void sendRunEnergy(int energy) {
		var msg = new UpdateRunEnergy(energy * 100);
		write(msg);
	}

	public void sendWeight(int weight) {
		var msg = new UpdateRunWeight(weight);
		write(msg);
	}

	public void sendPlayerAction(String name, boolean top, int option) {
		var msg = new SetPlayerOp(option, top, name);
		write(msg);
	}

	public void resetMapFlag() {
		var msg = new SetMapFlag(-1, -1);
		write(msg);
	}

	public void clearChunk(int chunkAbsX, int chunkAbsY) {
		int x = Position.getLocal(chunkAbsX, player.getFirstChunkX());
		int y = Position.getLocal(chunkAbsY, player.getFirstChunkY());
		var msg = new UpdateZoneFullFollows(x, y, player.getPosition().getZ());
		write(msg);
	}

	public void sendGroundItem(GroundItem groundItem) {
		write(RSProtService.encodeZonePacket(player, groundItem.x(), groundItem.y(), groundItem.z(),
				(x, y) -> new ObjAdd(groundItem.id, groundItem.amount, x, y, (byte) 31)));
	}

	public void sendGroundItemUpdate(GroundItem groundItem, int previousAmount) {
		write(RSProtService.encodeZonePacket(player, groundItem.x(), groundItem.y(), groundItem.z(),
				(x, y) -> new ObjCount(groundItem.id, previousAmount, groundItem.amount, x, y)));
	}

	public void sendRemoveGroundItem(int id, int amount, int x, int y, int z) {
		write(RSProtService.encodeZonePacket(player, x, y, z,
				(localX, localY) -> new ObjDel(id, amount, localX, localY)));
	}

	public void sendRemoveGroundItem(GroundItem groundItem) {
		write(RSProtService.encodeZonePacket(player, groundItem.x(), groundItem.y(), groundItem.z(),
				(x, y) -> new ObjDel(groundItem.id, groundItem.amount, x, y)));
	}

	public void sendCreateObject(int id, int x, int y, int z, int type, int dir) {
		write(RSProtService.encodeZonePacket(player, x, y, z,
				(xx, yy) -> new LocAddChangeV2(id, xx, yy, type, dir, (byte) 31)));
	}

	public void sendRemoveObject(int x, int y, int z, int type, int dir) {
		write(RSProtService.encodeZonePacket(player, x, y, z,
				(xx, yy) -> new LocDel(xx, yy, type, dir)));
	}

	public void sendObjectAnimation(int x, int y, int z, int type, int dir, int animationId) {
		write(RSProtService.encodeZonePacket(player, x, y, z,
				(xx, yy) -> new LocAnim(animationId, xx, yy, type, dir)));
	}

	public void sendProjectile(
			Entity source,
			Entity target,
			int projectileId,
			int startX, int startY, int destX, int destY,
			int startHeight, int endHeight, int delay, int duration, int curve, int something) {

		write(RSProtService.encodeZonePacket(player, startX, startY, player.getPosition().getZ(),
				(xx, yy) -> {
					var sourceIndex = 0;
					if (source instanceof Player) {
						sourceIndex = -(source.getIndex() + 1);
					} else if (source instanceof NPC) {
						sourceIndex = source.getIndex() + 1;
					}

					var targetIndex = 0;
					if (target instanceof Player) {
						targetIndex = -(target.getIndex() + 1);
					} else if (target instanceof NPC) {
						targetIndex = target.getIndex() + 1;
					}

					var dx = (destX - startX);
					var dy = (destY - startY);
					return new MapProjAnimV1(
							projectileId,
							startHeight, endHeight,
							delay, duration,
							curve, something,
							sourceIndex, targetIndex,
							xx, yy, dx, dy);
				}));
	}

	public void sendGraphics(int id, int height, int delay, int x, int y, int z) {
		write(RSProtService.encodeZonePacket(player, x, y, z,
				(xx, yy) -> new MapAnim(id, delay, height, xx, yy)));
	}

	public void sendAreaSound(int id, int type, int delay, int x, int y, int distance) {
		var plane = this.player.getPosition().getPlane();
		write(RSProtService.encodeZonePacket(player, x, y, plane,
				(xx, yy) -> new SoundArea(id, delay, 1, distance, distance, xx, yy)));
	}

	public void sendSoundEffect(int id, int type, int delay) {
		var msg = new SynthSound(id, type, delay);
		write(msg);
	}

	public void sendMusic(int id) {
		var msg = new MidiSongV2(id, 0, 0, 0, 0);
		write(msg);
	}

	// TODO: 184 Revision Fix Custom Packet
	public void sendJournal(int type, List<QuestTabCategory> categories) {}

	// TODO: 184 Revision Fix Custom Packet
	public void sendJournalEntry(int childId, String text, int colorIndex) {}

	// TODO: 184 Revision Fix Custom Packet
	public void sendAccountManagement(String donatorStatus, String username, int unreadPMs) {}

	// TODO: 184 Revision Fix Custom Packet
	public void sendSkillinterface(String donatorStatus) {}

	// TODO: 184 Revision Fix Custom Packet
	public void sendDiscordPresence(String discordStatus) {}

	// TODO: 184 Revision Fix Custom Packet
	public void sendTeleports(String title,
			int selectedCategoryIndex, teleports.Category[] categories,
			int selectedSubcategoryIndex, teleports.Subcategory[] subcategories,
			teleports.Teleport[] teleports) {}

	// TODO: 184 Revision Fix Custom Packet
	public void sendDropTable(String name, int petId, int petAverage, List<Integer[]> drops) {}

	// TODO: 184 Revision Fix Custom Packet
	public void sendLoyaltyRewards(int dayReward, int currentSpree, int highestSpree, int totalClaimedRewards,
			Item... loyaltyRewards) {}

	// TODO CUSTOM
	public void sendWidgetTimerCustom(Widget widget, int seconds) {}

	public void sendPlayerHead(int parentId, int childId) {
		var msg = new IfSetPlayerHead(parentId, childId);
		write(msg);
	}

	public void sendNpcHead(int parentId, int childId, int npcId) {
		var msg = new IfSetNpcHead(parentId, childId, npcId);
		write(msg);
	}

	public void animateInterface(int parentId, int childId, int animationId) {
		var mes = new IfSetAnim(parentId, childId, animationId);
		write(mes);
	}

	public void sendMapState(int state) {
		var msg = new MinimapToggle(state);
		write(msg);
	}

	public void sendHintIcon(Entity target) {
		HintArrowType ty;
		if (target.player == null) {
			ty = new HintArrow.NpcHintArrow(target.getIndex());
		} else {
			ty = new HintArrow.PlayerHintArrow(target.getIndex());
		}
		var msg = new HintArrow(ty);
		write(msg);
	}

	public void sendHintIcon(int x, int y) {
		var position = HintArrow.TileHintArrow.HintArrowTilePosition.CENTER;
		var ty = new HintArrow.TileHintArrow(x, y, 0, position);
		var msg = new HintArrow(ty);
		write(msg);
	}

	public void sendHintIcon(Position pos) {
		var position = HintArrow.TileHintArrow.HintArrowTilePosition.CENTER;
		var ty = new HintArrow.TileHintArrow(pos.getX(), pos.getY(), 0, position);
		var msg = new HintArrow(ty);
		write(msg);
	}

	public void resetHintIcon(boolean npcType) {
		var ty = HintArrow.ResetHintArrow.INSTANCE;
		var msg = new HintArrow(ty);
		write(msg);
	}

	public void turnCameraToLocation(int x, int y, int cameraHeight, int constantSpeed, int variableSpeed) {
		Position pos = new Position(x, y, 0);
		int posX = pos.getSceneX();
		int posY = pos.getSceneY();
		var msg = new CamLookAt(posX, posY, cameraHeight, constantSpeed, variableSpeed);
		write(msg);
	}

	public void moveCameraToLocation(int x, int y, int cameraHeight, int constantSpeed, int variableSpeed) {
		Position pos = new Position(x, y, 0);
		int posX = pos.getSceneX();
		int posY = pos.getSceneY();
		var msg = new CamMoveTo(posX, posY, cameraHeight, constantSpeed, variableSpeed);
		write(msg);
	}

	/**
	 * @param shakeType 0 shakes X, 1 shakes Z, 2 shakes Y, 3 shakes Yaw, 4 shakes Pitch
	 */
	public void shakeCamera(int shakeType, int intensity) {
		OutBuffer out = new OutBuffer(5).sendFixedPacket(CAM_SHAKE)
				.addByte(shakeType)
				.addByte(intensity)
				.addByte(intensity)
				.addByte(intensity);
		var msg = new CamShake(shakeType, intensity, intensity, intensity);
		write(msg);
	}

	public void sendBroadcast(String msg) {
		sendMessage(msg, null, 14);
	}

	public void resetCamera() {
		write(CamReset.INSTANCE);
	}

	public void setGraphic(int parent, int child, int gfx) {
		sendClientScript(44, "", parent << 16 | child, gfx);
	}
}

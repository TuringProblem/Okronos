package io.ruin.rsprot.handler;

import io.ruin.model.entity.player.Player;
import io.ruin.network.incoming.handlers.*;
import net.rsprot.protocol.game.incoming.buttons.If1Button;
import net.rsprot.protocol.game.incoming.buttons.If3Button;
import net.rsprot.protocol.game.incoming.buttons.IfButtonD;
import net.rsprot.protocol.game.incoming.buttons.IfButtonT;
import net.rsprot.protocol.game.incoming.buttons.IfSubOp;
import net.rsprot.protocol.game.incoming.friendchat.FriendChatSetRank;
import net.rsprot.protocol.game.incoming.locs.OpLoc;
import net.rsprot.protocol.game.incoming.locs.OpLoc6;
import net.rsprot.protocol.game.incoming.locs.OpLocT;
import net.rsprot.protocol.game.incoming.messaging.MessagePrivate;
import net.rsprot.protocol.game.incoming.messaging.MessagePublic;
import net.rsprot.protocol.game.incoming.misc.client.MapBuildComplete;
import net.rsprot.protocol.game.incoming.misc.client.WindowStatus;
import net.rsprot.protocol.game.incoming.misc.user.*;
import net.rsprot.protocol.game.incoming.npcs.OpNpc;
import net.rsprot.protocol.game.incoming.npcs.OpNpc6;
import net.rsprot.protocol.game.incoming.npcs.OpNpcT;
import net.rsprot.protocol.game.incoming.objs.OpObj;
import net.rsprot.protocol.game.incoming.objs.OpObj6;
import net.rsprot.protocol.game.incoming.objs.OpObjT;
import net.rsprot.protocol.game.incoming.players.OpPlayer;
import net.rsprot.protocol.game.incoming.players.OpPlayerT;
import net.rsprot.protocol.game.incoming.resumed.*;
import net.rsprot.protocol.game.incoming.social.FriendListAdd;
import net.rsprot.protocol.game.incoming.social.FriendListDel;
import net.rsprot.protocol.game.incoming.social.IgnoreListAdd;
import net.rsprot.protocol.game.incoming.social.IgnoreListDel;
import net.rsprot.protocol.message.codec.incoming.GameMessageConsumerRepositoryBuilder;

public class RSProtHandlers {

	public static void register(GameMessageConsumerRepositoryBuilder<Player> builder) {
		builder.addListener(WindowStatus.class, new DisplayHandler());
		builder.addListener(MapBuildComplete.class, new RegionUpdateHandler());
		builder.addListener(MoveGameClick.class, new WalkHandler.Game());
		builder.addListener(MoveMinimapClick.class, new WalkHandler.Minimap());

		builder.addListener(If3Button.class, new ActionButtonHandler.If3());
		builder.addListener(IfSubOp.class, new ActionButtonHandler.IfSub());
		builder.addListener(If1Button.class, new ActionButtonHandler.If1());
		builder.addListener(IfButtonD.class, new InterfaceDragHandler());
		builder.addListener(IfButtonT.class, new InterfaceOnInterfaceHandler.OnItem());
		builder.addListener(CloseModal.class, new CloseInterfaceHandler());
		builder.addListener(ResumePauseButton.class, new ActionButtonHandler.DialogueResume());

		builder.addListener(MessagePublic.class, new ChatHandler());

		// builder.addListener(MessagePrivate.class, new FriendsHandler.Message());
		// builder.addListener(FriendListAdd.class, new FriendsHandler.FriendAdd());
		// builder.addListener(FriendListDel.class, new FriendsHandler.FriendRemove());
		// builder.addListener(IgnoreListAdd.class, new FriendsHandler.IgnoreAdd());
		// builder.addListener(IgnoreListDel.class, new FriendsHandler.IgnoreRemove());
		// builder.addListener(FriendChatSetRank.class, new FriendsHandler.Rank());

		builder.addListener(OpObj.class, new GroundItemActionHandler());
		builder.addListener(OpObj6.class, new GroundItemActionHandler.Examine());

		builder.addListener(ResumePNameDialog.class, new InputHandler.Name());
		builder.addListener(ResumePCountDialog.class, new InputHandler.Count());
		builder.addListener(ResumePObjDialog.class, new InputHandler.Obj());
		builder.addListener(ResumePStringDialog.class, new InputHandler.String());

		builder.addListener(OpPlayerT.class, new InterfaceOnEntityHandler.OnPlayer());
		builder.addListener(OpNpcT.class, new InterfaceOnEntityHandler.OnNPC());
		builder.addListener(OpObjT.class, new InterfaceOnObjectHandler.FromInterface());

		builder.addListener(OpNpc.class, new NPCActionHandler());
		builder.addListener(OpNpc6.class, new NPCActionHandler.Examine());

		builder.addListener(OpLoc.class, new ObjectActionHandler());
		builder.addListener(OpLoc6.class, new ObjectActionHandler.Examine());
		builder.addListener(OpLocT.class, new InterfaceOnObjectHandler.FromInterfaceLoc());

		builder.addListener(OpPlayer.class, new PlayerActionHandler());

		builder.addListener(SetChatFilterSettings.class, new PrivacyHandler());

		builder.addListener(Teleport.class, new TeleportHandler());

		builder.addListener(ClientCheat.class, new CommandHandler());
	}
}

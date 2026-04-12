package io.ruin.model.shop;

import io.ruin.model.entity.player.Player;
import io.ruin.model.item.containers.ShopItemContainer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ShopContainerListener {

	private final Player player;
	private ShopItemContainer shopContainer;


	public void open() {
        /*
        SEND ITEM -1258, 24
SEND ITEM -1327, 93
         */
		shopContainer.send(player, -1258, 24);
	}


	public void update() {
		shopContainer.sendUpdates(player, -1258, 24);
	}


}

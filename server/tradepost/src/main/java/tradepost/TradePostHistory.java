package tradepost;

import java.math.BigInteger;

import io.ruin.model.item.Item;
import lombok.Data;

@Data
public class TradePostHistory {

	private int offerType;
	private Item item;
	private BigInteger price;
	private String seller;
	private long age;

	public TradePostHistory(int offerType, Item item, BigInteger price, String seller, long age) {
		this.offerType = offerType;
		this.item = item;
		this.price = price;
		this.seller = seller;
		this.age = age;
	}

}

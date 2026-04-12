package tradepost;

import java.math.BigInteger;

import io.ruin.api.utils.MathUtils;
import lombok.Data;

@Data
public final class TradePostOffer {

	public enum Kind {
		Buy, Sell;

		public boolean isBuy() {
			return this == Kind.Buy;
		}
	}

	public long id;
	public int itemId;
	public int slot;
	public BigInteger amount;
	public BigInteger price;
	public long timeStamp;
	public String owner;
	public BigInteger startAmount;
	public Kind kind;

	public boolean isSell() {
		return this.kind == Kind.Sell;
	}

	public boolean isBuy() {
		return this.kind == Kind.Buy;
	}

	public boolean isEmpty() {
		return MathUtils.lt(this.amount, BigInteger.ZERO) || this.amount.equals(BigInteger.ZERO);
	}

	public BigInteger priceEach() {
		if (this.isEmpty()) {
			return BigInteger.valueOf(Long.MAX_VALUE);
		}
		return this.price.divide(this.startAmount);
	}

	public void remove() {
		this.amount = BigInteger.ZERO;
	}

	public int getAmount() {
		return this.amount.intValueExact();
	}

	public int getStartAmount() {
		return this.startAmount.intValueExact();
	}
}

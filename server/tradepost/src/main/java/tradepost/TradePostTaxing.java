package tradepost;

import java.math.BigDecimal;
import java.math.BigInteger;

import properties.ServerProperties;

public class TradePostTaxing {

	private static BigDecimal TAX_PERCENTAGE = BigDecimal.valueOf(ServerProperties.get("trade_post_tax", 2.0));

	public static BigInteger apply(BigInteger totalCoinsAmount) {
		var temp = new BigDecimal(totalCoinsAmount);
		var taxed = temp.multiply(TAX_PERCENTAGE.divide(BigDecimal.valueOf(100)));
		return totalCoinsAmount.subtract(taxed.toBigInteger());
	}
}

package io.ruin.services.http.models.store;

public class StoreBasketModel {

	public String id;
	public String username;
	public String stripePrivatePaymentId;
	public long transactionAmount;
	public String paymentStatus;
	public String stripePaymentId;
	public String datePaid;
	public String dateCreated;
	public boolean hasClaimed;
	public String generatedFromIp;
	public String claimedFromIp;
	public String stripeCheckoutUrl;
	public String referralCodeAttatched;


}

package io.ruin.api.protocol;

public class CentralOutgoingPackets {

	public static final int PING = 0;
	public static final int CONFIRM_RESPONSE = 1;
	public static final int LOGIN_REQUEST_RESPONSE = 2;
	public static final int SAVE_REQUEST_RESPONSE = 3;
	public static final int FORCE_LOGOUT = 4;
	public static final int GE_UPDATE_OFFERS = 5;

	public static final int FORWARD_CLIENT_PACKET_BYTE = 98;
	public static final int FORWARD_CLIENT_PACKET_SHORT = 99;

	public static final int CHARACTER_SAVE = 20;

	public static final int CHARACTER_CREATE_RESPONSE = 25;
	public static final int CHARACTER_LIST_UPDATE = 26;

	public static final int GE_TRANSACTION_RESPONSE = 40;

}

package io.ruin.api.protocol;

public class CentralIncomingPackets {

	public static final int PING = 0;


	public static final int LOGIN = 1;
	public static final int LOGOUT = 2;
	public static final int SAVE = 3;

	public static final int GLOBAL_MESSAGE = 4;

	public static final int PRIVACY_UPDATE = 5;
	public static final int SOCIAL_REQUEST = 6;
	public static final int PRIVATE_MESSAGE = 7;


	public static final int CLAN_NAME = 8;
	public static final int CLAN_SETTING = 9;
	public static final int CLAN_RANK = 10;
	public static final int CLAN_REQUEST = 11;
	public static final int CLAN_KICK = 12;
	public static final int CLAN_MESSAGE = 13;


	public static final int GE_INIT_OFFER = 14;
	public static final int GE_ABORT_OFFER = 15;
	public static final int GE_SEND_OFFERS = 16;
	public static final int GE_REMOVE_OFFER = 17;
	public static final int GE_UPDATE_OFFER = 18;
	public static final int GE_COLLECT_OFFER = 19;


	public static final int UPDATE_GAMEMODE = 21;
	public static final int UPDATE_RIGHTS = 22;
	public static final int UPDATE_DISPLAY_NAME = 23;
	public static final int CREATE_NEW_CHARACTER = 24;
	public static final int UPDATE_CHARACTER_LIST = 25;

	public static final int REQUEST_LOAD_CHARACTER = 30;

	public static final int GE_TRANSACTION_REQUEST = 40;

}

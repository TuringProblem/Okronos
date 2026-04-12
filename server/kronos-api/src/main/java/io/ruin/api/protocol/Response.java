package io.ruin.api.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

public enum Response {

	SUCCESS(2),
	INVALID_LOGIN(3),
	DISABLED_ACCOUNT(4),
	ALREADY_LOGGED_IN(5),
	GAME_UPDATED(6),
	WORLD_FULL(7),
	LOGIN_SERVER_OFFLINE(8),
	CONNECTION_LIMIT(9),
	BAD_SESSION_ID(10),
	VULNERABLE_PASSWORD(11),
	MEMBERSHIP_REQUIRED(12),
	COULD_NOT_LOGIN(13),
	WORLD_DOWN(13),
	UPDATING(14),
	ERROR(15),
	LOGIN_LIMIT(16),
	UNREGISTERED_ACCOUNT(17),
	ACCOUNT_LOCKED(18),
	CLOSED_BETA(19),
	LOGIN_SERVER_NO_REPLY(23),
	ERROR_LOADING_ACCOUNT(24),
	LOGIN_SERVER_UNEXPECTED_RESPONSE(25),
	COMPUTER_BLOCKED(26),
	CHANGE_DISPLAY_NAME(31),
	EMAIL_VALIDATION(32),
	PRIVATE_ACCESS(55),
	TWO_FACTOR(56),
	TWO_FACTOR_RETRY(57),
	DOB(61),
	ATTEMPT_TIMEOUT(62),
	KICK(63),
	FAILED_UNKNOWN(65),
	FAILED_UNKNOWN2(67),
	DOB_ERROR(71),

	// NEED OFFSET to 80 = id+22
   /* EMAIL_REQUIRED(58),
    EMAIL_IN_USE(59),
    USERNAME_TOO_LONG(60),
    USERNAME_BAD_WORDS(61),
    USERNAME_BAD_LETTERS(62),
    USERNAME_DUPLICATE(63),
    USERNAME_ON_HOLD(64),
    PROXY_LOGIN_ATTEMPT(65),
    IP_BANNED(66),
    USERNAME_IN_USE(67),
    EMAIL_BANNED(68),
    EMAIL_INVALID(69),*/
	UNEXPECTED(Byte.MAX_VALUE),
	;

	private final int responseCodeId;

	Response(final int responseCodeId) {
		this.responseCodeId = responseCodeId;
	}

	public void send(final Channel channel) {
		final ByteBuf buffer = channel.alloc()
			.buffer(1, 1)
			.writeByte(responseCodeId);
		channel.writeAndFlush(buffer)
			.addListener(ChannelFutureListener.CLOSE);
	}

	public static final Response[] values = values();

	public static Response valueOf(
		final int ordinal
	) {
		if (ordinal < 0
			|| ordinal >= values.length) {
			return null;
		}
		return values[ordinal];
	}

}

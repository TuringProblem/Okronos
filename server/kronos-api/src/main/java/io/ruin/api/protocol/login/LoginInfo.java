package io.ruin.api.protocol.login;

import io.ruin.api.protocol.PlatformInfo;
import io.ruin.api.utils.IPAddress;

import java.util.List;

public class LoginInfo {

	public final String ipAddress;
	public final int ipAddressInt;
	public PlatformInfo platformInfo;
	public final String password;
	public final String email;
	public final String uuid;
	public final int tfaCode;
	public final boolean tfaTrust;
	public final int tfaTrustValue;
	public final int worldId;
	public final long sessionId;
	public final int[] keys;

	public int userId = -1;
	public String name;
	public String saved;
	public int unreadPMs;
	public List<Integer> groupIds;
	public List<Integer> sgroupIds;

	public final String hwid;

	public LoginInfo(long sessionId, String ip, String name, String password, String email, PlatformInfo info, String uuid,
			String hwid, int tfaCode, boolean tfaTrust, int tfaTrustValue) {
		this.keys = new int[0];
		this.sessionId = sessionId;
		this.ipAddress = ip;
		this.ipAddressInt = IPAddress.toInt(ipAddress);
		this.name = name;
		this.password = password;
		this.email = email;
		this.tfaCode = tfaCode;
		this.tfaTrust = tfaTrust;
		this.tfaTrustValue = tfaTrustValue;
		this.worldId = -1;
		this.unreadPMs = 0;
		this.platformInfo = info;
		this.uuid = uuid;
		this.hwid = hwid;
	}

	public LoginInfo(
			int[] keys,
			long sessionId, String ip, String name, String password, String email, PlatformInfo info, String uuid,
			String hwid, int tfaCode, boolean tfaTrust, int tfaTrustValue) {
		this.keys = keys;
		this.sessionId = sessionId;
		this.ipAddress = ip;
		this.ipAddressInt = IPAddress.toInt(ipAddress);
		this.name = name;
		this.password = password;
		this.email = email;
		this.tfaCode = tfaCode;
		this.tfaTrust = tfaTrust;
		this.tfaTrustValue = tfaTrustValue;
		this.worldId = -1;
		this.unreadPMs = 0;
		this.platformInfo = info;
		this.uuid = uuid;
		this.hwid = hwid;
	}
}

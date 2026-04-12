package io.ruin.api.protocol.world;

public enum WorldType {
	ECO("Reason", "https://reasonps.com/"),
	BETA("ReasonBeta", "https://reasonps.com/"),
	PVP("ReasonPvP", "https://reasonps.com/"),

	DEADMAN("ReasonDMM", "https://reasonps.com/"),
	DEV("ReasonDev", "https://reasonps.com/");

	WorldType(String worldName, String websiteUrl) {
		this.worldName = worldName;
		this.websiteUrl = websiteUrl;
	}

	public boolean isDeadman() {
		return this == DEADMAN;
	}

	private String worldName, websiteUrl;

	public String getWorldName() {
		return worldName;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}
}
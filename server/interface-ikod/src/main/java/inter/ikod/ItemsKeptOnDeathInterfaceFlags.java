package inter.ikod;

class ItemsKeptOnDeathInterfaceFlags {
	public final boolean protect;
	public final boolean skull;
	public final boolean wilderness;
	public final boolean killedByAPlayer;

	ItemsKeptOnDeathInterfaceFlags(boolean protect, boolean skull, boolean wilderness, boolean killedByAPlayer) {
		this.protect = protect;
		this.skull = skull;
		this.wilderness = wilderness;
		this.killedByAPlayer = killedByAPlayer;
	}
}

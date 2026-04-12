package inter.ikod;

enum ItemOnDeathItemKind {
	Deleted(-1),
	OtherKept(343),
	OtherKeptDowngraded(323),
	Lost(367),
	LostDowngraded(369),
	LostGraveyardCoins(7531),
	LostToThePlayerWhoKillsYou(1991);

	public final int configItem;

	ItemOnDeathItemKind(int configItem) {
		this.configItem = configItem;
	}
}

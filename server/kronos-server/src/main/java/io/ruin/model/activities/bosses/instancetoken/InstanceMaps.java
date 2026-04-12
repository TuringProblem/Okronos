package io.ruin.model.activities.bosses.instancetoken;

import io.ruin.model.activities.bosses.instancetoken.maphandlers.*;
import io.ruin.model.activities.bosses.sarachnis.SarachnisMapHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum InstanceMaps {
	/*
	 * Chunks go from southwest corner being the first chunk going east to the south
	 * east corner
	 * then it will go to the chunk north of the first tile and go to the east again
	 * and so on
	 */
	BANDOS("General Graardor",
		new int[][]{
			{357, 668}, {358, 668}, {359, 668},
			{357, 669}, {358, 669}, {359, 669},
			{357, 670}, {358, 670}, {359, 670},
			{357, 671}, {358, 671}, {359, 671}
		},
		0, 4, 3, false, 2, BandosMapHandler.class),
	ZAMMY("K'ril Tsutsaroth", new int[][]{
		{364, 664}, {365, 664}, {366, 664}, {367, 664},
		{364, 665}, {365, 665}, {366, 665}, {367, 665},
		{364, 666}, {365, 666}, {366, 666}, {367, 666},
		{364, 667}, {365, 667}, {366, 667}, {367, 667}
	}, 0, 4, 4, false, 2, ZamorakMapHandler.class),
	ARMA("Kree'arra", new int[][]{
		{352, 660}, {353, 660}, {354, 660}, {355, 660}, {356, 660},
		{352, 661}, {353, 661}, {354, 661}, {355, 661}, {356, 661},
		{352, 662}, {353, 662}, {354, 662}, {355, 662}, {356, 662},
		{352, 663}, {353, 663}, {354, 663}, {355, 663}, {356, 663},

	}, 0, 4, 5, false, 2, ArmadylMapHandler.class),
	SARA("Commander Zilyana", new int[][]{
		{360, 656}, {361, 656}, {362, 656}, {363, 656}, {364, 656},
		{360, 657}, {361, 657}, {362, 657}, {363, 657}, {364, 657},
		{360, 658}, {361, 658}, {362, 658}, {363, 658}, {364, 658},
		{360, 659}, {361, 659}, {362, 659}, {363, 659}, {364, 659},
	}, 0, 4, 5, false, 0, SaradominMapHandler.class),
	KBD("King Black Dragon", null, 9033, 0, 0, true, 0, KBDMapHandler.class),
	BALANCE_ELEMENTAL("Balance Elemental", null, 10127, 0, 0, true, 1, BalanceElementalMapHandler.class),
	OPHIDIA("Ophidia", null, 12616, 0, 0, true, 0, OphidiaMapHandler.class),
	SARACHNIS("Sarachnis", null, 7322, 0, 0, true, 0, SarachnisMapHandler.class),
	ALCHEMICAL_HYDRA("Alchemical Hydra", null, 5536, 0, 0, true, 0, AlchemicalHydraMapHandler.class),
	SOTETSEG("Sotetseg", null, 13123, 0, 0, true, 0, SotetsegMapHandler.class),
	PHANTOM_MUSPAH("Phantom Muspah", null, 11330, 0, 0, true, 0, PhantomMuspahMapHandler.class),
	LEVIATHAN("Leviathan", null, 8291, 0, 0, true, 1, LeviathanMapHandler.class),
	SOL_HEREDIT("Sol Heredit", null, 7216, 0, 0, true, 1, SolHereditMapHandler.class),
	SCURRIUS("Scurrius", null, 13210, 0, 0, true, 0, ScurriusMapHandler.class),
	WHISPERER("Whisperer", null, 10595, 0, 0, true, 1, WhispererMapHandler.class),
	DUKE("Duke Sucellus", null, 12132, 0, 0, true, 1, DukeMapHandler.class),
	VARDORVIS("Vardorvis", null, 4405, 0, 0, true, 1, VardorvisMapHandler.class),
	KEPHRI("Kephri", null, 14164, 0, 0, true, 0, KephriMapHandler.class),
	ZEBAK("Zebak", null, 15700, 0, 0, true, 1, ZebakMapHandler.class),
	AKKHA("Akkha", null, 14676, 0, 0, true, 1, AkkhaMapHandler.class),
	BABA("Ba-Ba", null, 15188, 0, 0, true, 1, BabaMapHandler.class),
	ELIDINIS("Elidinis", null, 15184, 0, 0, true, 1, ElidinisMapHandler.class),
	TUMEKEN("Tumeken", null, 15696, 0, 0, true, 1, TumekensMapHandler.class),
	CORP("Corporeal Beast", null, 11844, 0, 0, true, 2, CorpMapHandler.class),
	DKS("Dagannoth Kings", null, 11589, 0, 0, true, 0, DKSMapHandler.class),
	GALVEK("Galvek", null, 6489, 0, 0, true, 2, GalvekMapHandler.class),
	KQ("Kalphite Queen", null, 13972, 0, 0, true, 0, KQMapHandler.class),
	ARGENTAVIS("Argentavis", null, 8534, 0, 0, true, 3, ArgentavisMapHandler.class),
	ARAXXOR("Araxxor", null, 14489, 0, 0, true, 0, AraxxorMapHandler.class),
	MOLE("Giant Mole", null, 6992, 0, 0, true, 0, MoleMapHandler.class),
	NEX("Nex", null, 11601, 0, 0, true, 0, NexMapHandler.class),
	GROTESQUE_GUARDIANS("Grotesque Guardians", null, 6727, 0, 0, true, 0, GrotesqueGuardiansMapHandler.class),
	CERBY("Cerberus", null, 5140, 0, 0, true, 0, CerbMapHandler.class),

	;

	final String name;
	final int[][] bossMapChunks;
	@Getter
	final int regionId;
	@Getter
	final int chunksX;
	@Getter
	final int chunksY;
	@Getter
	final boolean usingRegionId;
	@Getter
	final int height;

	private final Class<? extends MapHandler> mapHandlerClass;

	public int[][] getChunks() {
		return bossMapChunks;
	}

	public MapHandler createMapHandler() {
		try {
			// Create a new instance of the associated MapHandler class
			return mapHandlerClass.getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Failed to create a new instance of MapHandler", e);
		}
	}

}

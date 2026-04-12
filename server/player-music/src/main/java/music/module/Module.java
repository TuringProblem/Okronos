package music.module;

import java.util.ArrayList;

import core.module.api.IModule;
import io.ruin.cache.DbTable;
import io.ruin.model.entity.player.Player;
import music.hook.PlayerHook;
import music.inter.MusicPlayer;

public class Module implements IModule {

	@Override
	public void start() {
		var musicTable = DbTable.get(44);
		var tracks = new ArrayList<MusicPlayer.Track>();
		for (var row : musicTable.rowsSorted()) {
			var _midiId = row.columns.get(4);
			var _name_order = row.columns.get(0);
			var _name = row.columns.get(1);
			var unlock = row.columns.get(5);
			var _disabled = row.columns.get(8);
			if (_disabled != null) {
				continue;
			}
			if (_midiId == null || _name_order == null || _name == null) {
				continue;
			}
			var name = (String) _name[0][0];
			var nameOrder = (String) _name_order[0][0];
			var midiId = (Integer) _midiId[0][0];

			if (unlock == null) {
				unlock = new Object[][] { { -1, -1 } };
			}
			var unlockVarpIndex = (Integer) unlock[0][0];
			var unlockVarpBitIndex = (Integer) unlock[0][1];
			var track = new MusicPlayer.Track(name, nameOrder, unlockVarpIndex, unlockVarpBitIndex, midiId);
			tracks.add(track);
		}
		MusicPlayer.registerTracks(tracks);
		MusicPlayer.register();
		PlayerHook.register();
	}
}

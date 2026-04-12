package io.ruin.network;

import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.FileStore;

public class LoginCacheCrc {

	int jingles_crc;
	int worldmapground_crc;
	int samples_crc;
	int sprites_crc;
	int config_crc;
	int worldmap_crc;
	int worldmap_legacy_crc;
	int synths_crc;
	int tex_crc;
	int dll_crc;
	int fonts_crc;
	int worldmapgeo_crc;
	int instruments_crc;
	int models_crc;
	int music_crc;
	int maps_crc;
	int if_crc;
	int bases_crc;
	int defaults_crc;
	int frames_crc;
	int scripts_crc;


	public LoginCacheCrc(InBuffer in) {
		jingles_crc = in.readInt();
		worldmapground_crc = in.readIntLE();
		samples_crc = in.readIntME();
		sprites_crc = in.readInt();
		config_crc = in.readIntME();
		worldmap_crc = in.readIntME();
		worldmap_legacy_crc = in.readIntLE();
		synths_crc = in.readIntME2();
		tex_crc = in.readInt();
		dll_crc = in.readIntME();
		fonts_crc = in.readInt();
		worldmapgeo_crc = in.readIntLE();
		instruments_crc = in.readIntLE();
		models_crc = in.readIntME2();
		music_crc = in.readIntME2();
		maps_crc = in.readInt();
		if_crc = in.readIntLE();
		bases_crc = in.readIntME2();
		defaults_crc = in.readIntLE();
		frames_crc = in.readIntME();
		scripts_crc = in.readInt();
	}

	public boolean valid(FileStore cache) {
		boolean frames_valid = cache.files[0].table.crc == frames_crc;
		boolean bases_valid = cache.files[1].table.crc == bases_crc;
		boolean config_valid = cache.files[2].table.crc == config_crc;
		boolean if_valid = cache.files[3].table.crc == if_crc;
		boolean synths_valid = cache.files[4].table.crc == synths_crc;
		boolean maps_valid = cache.files[5].table.crc == maps_crc;
		boolean music_valid = cache.files[6].table.crc == music_crc;
		boolean models_valid = cache.files[7].table.crc == models_crc;
		boolean sprites_valid = cache.files[8].table.crc == sprites_crc;
		boolean tex_valid = cache.files[9].table.crc == tex_crc;
		boolean dll_valid = cache.files[10].table.crc == dll_crc;
		boolean jingles_valid = cache.files[11].table.crc == jingles_crc;
		boolean scripts_valid = cache.files[12].table.crc == scripts_crc;
		boolean fonts_valid = cache.files[13].table.crc == fonts_crc;
		boolean samples_valid = cache.files[14].table.crc == samples_crc;
		boolean instruments_valid = cache.files[15].table.crc == instruments_crc;
		boolean worldmap_legacy_valid = worldmap_legacy_crc == 0;
		boolean defaults_valid = cache.files[17].table.crc == defaults_crc;
		boolean worldmapgeo_valid = cache.files[18].table.crc == worldmapgeo_crc;
		boolean worldmap_valid = cache.files[19].table.crc == worldmap_crc;
		boolean worldmapground_valid = cache.files[20].table.crc == worldmapground_crc;

		return frames_valid && bases_valid && config_valid
			&& if_valid && synths_valid && maps_valid
			&& music_valid && models_valid && sprites_valid
			&& tex_valid && dll_valid && jingles_valid
			&& scripts_valid && fonts_valid && samples_valid
			&& instruments_valid && worldmap_legacy_valid
			&& defaults_valid && worldmapgeo_valid
			&& worldmap_valid && worldmapground_valid;
	}
}

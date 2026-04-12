package io.ruin.data.impl;

import io.ruin.api.utils.JsonUtils;
import io.ruin.data.DataFile;
import io.ruin.model.map.MultiZone;
import io.ruin.model.map.Region;
import io.ruin.model.map.dynamic.DynamicMap;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class region_keys extends DataFile {

	public static final int[] NULL_KEYS = new int[4];

	@Override
	public String path() {
		return "region_keys.json";
	}

	@Override
	public int priority() {
		return 2;
	}

	@Override
	public Object fromJson(String fileName, String json) {
		List<XTEA> xteas = JsonUtils.fromJson(json, List.class, XTEA.class);
		Map<Integer, int[]> keys = new HashMap<>(xteas.size());

		for (XTEA xtea : xteas) {
			keys.put(xtea.getMapsquare(), Region.NO_KEYS ? XTEA.DEFAULT : xtea.getKey());
		}

		for (int regionId = 0; regionId < Region.LOADED.length; regionId++) {
			Region region = new Region(regionId);
			Region.LOADED[regionId] = region;
		}

		for (Region region : Region.LOADED) {
			try {
				region.init();
				if (!region.empty) {
					Region.regions.putIfAbsent(region.id, region);
				}
			} catch (Exception ex) {
				System.err.println("Error loading region " + region.id);
				ex.printStackTrace();
			}
		}
		log.info("Total {} regions with valid xtea.", Region.regions.size());

		MultiZone.load();
		DynamicMap.load();
		return keys;
	}

}

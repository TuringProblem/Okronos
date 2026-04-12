package io.ruin.api.protocol.web.login;

import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import io.ruin.api.utils.InvisionPowerAPI;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Data
public class InvisionGroup {

	public static final Predicate<InvisionGroup> IS_ADMIN = (group) -> group.getId() == 7 || group.getId() == 8 || group.getId() == 4;//TODO Set these to correct values
	public static final Predicate<InvisionGroup> IS_STAFF = (group) -> IS_ADMIN.test(group) || group.getId() == 9 || group.getId() == 10 || group.getId() == 11 || group.getId() == 6;
	public static final Predicate<InvisionGroup> IS_BETA = (group) -> group.getId() == 18;

	private static Map<Integer, InvisionGroup> groups = Maps.newConcurrentMap();

	public static Optional<InvisionGroup> getById(int id) {
		return Optional.ofNullable(groups.get(id));
	}

	public static Optional<InvisionGroup> getByName(String name, boolean ignoreCase) {
		return groups.values().stream().filter(group -> ignoreCase ? group.name.equalsIgnoreCase(name) : group.name.equals(name)).findFirst();
	}

	public static Optional<InvisionGroup> getByName(String name) {
		return getByName(name, false);
	}

	@SuppressWarnings("unchecked")
	public static void poll() throws IOException {
		Map<String, String> arguments = Maps.newConcurrentMap();
		arguments.put("perPage", "1000");
		Map<String, Object> response = InvisionPowerAPI.getRequest("/core/groups", arguments);

		List<ArrayMap<String, Object>> jsonGroups = (List<ArrayMap<String, Object>>) response.getOrDefault("results", Lists.newArrayList());


		if (!jsonGroups.isEmpty()) {
			List<InvisionGroup> responseGroups = jsonGroups
				.stream()
				.map(arrayMap -> new InvisionGroup(Integer.valueOf(String.valueOf(arrayMap.get("id"))), String.valueOf(arrayMap.get("name")), String.valueOf(arrayMap.get("formattedName"))))
				.collect(Collectors.toList());
			//= jsonGroups.stream().map(groupJson -> (InvisionGroup) JsonUtils.fromJson(groupJson, InvisionGroup.class)).collect(Collectors.toList());
			groups.clear();
			responseGroups.forEach(group -> groups.put(group.getId(), group));
		}
	}

	private final int id;
	private final String name, formattedName;

	public static Stream<InvisionGroup> stream() {
		return groups.values().stream();
	}


	public static InvisionGroup[] getByArrayMapList(List<ArrayMap<String, Object>> arrayMapList) {
		if (arrayMapList == null)
			return new InvisionGroup[0];
		return arrayMapList.stream().map(InvisionGroup::getByArrayMap).collect(Collectors.toList()).toArray(new InvisionGroup[0]);
	}

	public static InvisionGroup getByArrayMap(ArrayMap<String, Object> arrayMap) {
		if (arrayMap == null)
			return null;
		return new InvisionGroup(Integer.valueOf(String.valueOf(arrayMap.get("id"))), String.valueOf(arrayMap.get("name")), String.valueOf(arrayMap.get("formattedName")));
	}
}

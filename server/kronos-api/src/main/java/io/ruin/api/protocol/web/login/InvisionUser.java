package io.ruin.api.protocol.web.login;

import com.google.api.client.util.ArrayMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.ruin.api.utils.InvisionPowerAPI;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Data
/**
 * Represents a single user on Invision Power Suite
 */
public class InvisionUser {

	/**
	 * The ID of the user on the IPS
	 */
	private final int id;
	/**
	 *
	 */
	private String username;

	private String email;
	private String password;

	private InvisionGroup primaryGroup;
	private InvisionGroup[] secondaryGroups;

	private long lastUpdate = -1;


	public boolean hasRight(Predicate<InvisionGroup> predicate) {
		return getGroups().stream().anyMatch(predicate);
	}

	@SuppressWarnings("unchecked")
	public void update() throws IOException {
		Map<String, String> arguments = Maps.newConcurrentMap();
		Map<String, Object> response = InvisionPowerAPI.getRequest("/core/members/" + id, arguments);


		this.username = (String) response.getOrDefault("name", username);
		this.email = (String) response.getOrDefault("email", email);

		this.primaryGroup = InvisionGroup.getByArrayMap((ArrayMap<String, Object>) response.getOrDefault("primaryGroup", null));
		this.secondaryGroups = InvisionGroup.getByArrayMapList((List<ArrayMap<String, Object>>) response.getOrDefault("secondaryGroups", null));

		this.lastUpdate = System.currentTimeMillis();
		/*ArrayMap<String, Object> customFields = (ArrayMap<String, Object>) response.get("customFields");

		ArrayMap<String, Object> gameData = (ArrayMap<String, Object>) ((ArrayMap<String, Object>) customFields.get("2")).get("fields");

		gameData.forEach((key, value) -> {
			ArrayMap<String, Object> keyMap = (ArrayMap<String, Object>) value;
			if(keyMap.containsKey("player-characters") && keyMap.get("player-characters") instanceof String){
					String characterList = (String) keyMap.get("player-characters");
					if(!characterList.isEmpty()){
						characters = JsonUtils.fromJson(characterList, List.class, Character.class);
					}
			}
		});*/

	}


	public List<InvisionGroup> getGroups() {
		List<InvisionGroup> groups = Lists.newArrayList();
		groups.add(primaryGroup);
		Stream.of(secondaryGroups).forEach(groups::add);
		return groups;
	}


	public List<Integer> getGroupIds() {
		List<InvisionGroup> groups = Lists.newArrayList();
		groups.add(primaryGroup);
		Stream.of(secondaryGroups).forEach(groups::add);
		return groups.stream().mapToInt(InvisionGroup::getId).boxed().collect(Collectors.toList());
	}


}

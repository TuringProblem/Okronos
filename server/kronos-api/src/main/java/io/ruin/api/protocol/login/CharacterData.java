package io.ruin.api.protocol.login;

import com.google.gson.annotations.SerializedName;
import io.ruin.api.item.ItemSql;
import lombok.Data;

import java.util.List;

@Data
public class CharacterData {
	public final int id;
	@SerializedName("name")
	public final String displayName;
	@SerializedName("previous_name")
	public final String lastName;
	public boolean online;
	public int world;
	public final int rights;
	public final int gamemode;

	public List<ItemSql> equipment;
	public String stats;
	public int[] colours;
	public int[] styles;
}

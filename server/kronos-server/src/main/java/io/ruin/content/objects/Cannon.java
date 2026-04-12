package io.ruin.content.objects;

import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Cannon extends GameObject {
	public int loaded = 0;
	public boolean firing = false;
	public String userId = "-1";
	public Position pos;
	public int id;
	public int type;
	public int rot;

	public Cannon(int id, Position pos, int type, int rot) {
		super(id, pos, type, rot);
		this.id = id;
		this.pos = pos;
		this.type = type;
		this.rot = rot;
	}
}

package yama.combat;

import io.ruin.cache.Color;
import io.ruin.model.World;
import io.ruin.model.combat.Hit;
import io.ruin.model.entity.npc.NPC;
import io.ruin.model.entity.player.Player;
import io.ruin.model.map.Position;
import io.ruin.model.map.object.GameObject;
import lombok.Getter;

public class Glyph {
	@Getter
	private final GameObject gameObject;
	private final NPC yama;
	private Position middleGround;

	Glyph(GameObject gameObject, NPC yama) {
		this.gameObject = gameObject;
		this.yama = yama;
		this.middleGround = new Position(gameObject.getX() + 1, gameObject.getY() + 1, gameObject.getZ());
	}

	private void glyphEvent(Player target) {
			int yamaDistanceToGlyph = yama.getPosition().distance(gameObject.getPosition());
			Yama yc = (Yama) yama.getCombat();
			if(gameObject.getId() == 56335) {
				target.getPrayer().drain(getDamage(yamaDistanceToGlyph) * 2);
				target.graphics(3281);
				target.sendMessage("<col=6800bf>You absorb a Glyph of Shadow.");
				yc.setShadowImmunityTicks(5);
				World.startEvent(e -> {
					e.delay(5);
					yama.getPosition().getRegion().players.forEach(p -> p.sendMessage(Color.RED.wrap("The Glyph of Shadow fades away.")));
				});
			} else if(gameObject.getId() == 56336) {
				target.hit(new Hit().fixedDamage(getDamage(yamaDistanceToGlyph) * 3));
				target.graphics(3280);
				target.sendMessage("<col=b25000>You absorb a Glyph of Fire.");
				yc.setFireImmunityTicks(5);
				World.startEvent(e -> {
					e.delay(5);
					yama.getPosition().getRegion().players.forEach(p -> p.sendMessage(Color.RED.wrap("The Glyph of Fire fades away.")));
				});
			}
			getGameObject().setId(56337);
	}




	private int getDamage(int yamaDistanceToGlyph) {
		if(yamaDistanceToGlyph <= 1) {
			return 12;
		} else if(yamaDistanceToGlyph == 2) {
			return 9;
		} else if(yamaDistanceToGlyph == 3) {
			return 6;
		} else {
			return 2;
		}
	}

	protected void process() {
		if(gameObject.getId() == 56335 || gameObject.getId() == 56336) {
			yama.getPosition().getRegion().players.forEach(p -> {
				if(p.getPosition().distance(middleGround) < 1) {
					glyphEvent(p);
				}
			});
		}
	}

}

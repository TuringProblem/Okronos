package io.ruin.utility;

import io.ruin.model.entity.player.Player;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class PredicateCheck {

	private final Predicate<Player> check;


	private Consumer<Player> onFailure;

	public PredicateCheck setOnFailure(Consumer<Player> onFailure) {
		this.onFailure = onFailure;
		return this;
	}

	public boolean check(Player player) {
		if (!check.test(player)) {
			if (onFailure != null)
				onFailure.accept(player);
			return false;
		}
		return true;
	}
}

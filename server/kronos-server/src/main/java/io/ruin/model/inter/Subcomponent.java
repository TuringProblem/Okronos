package io.ruin.model.inter;

public enum Subcomponent {

	DIALOGUE(566, ToplevelComponent.CHATBOX),
	YES_NO_DIALOGUE(565, ToplevelComponent.CHATBOX);

	public int getComponent() {
		return component;
	}

	public ToplevelComponent getParent() {
		return parent;
	}

	private final int component;
	private final ToplevelComponent parent;

	Subcomponent(int component, ToplevelComponent parent) {
		this.component = component;
		this.parent = parent;
	}

}

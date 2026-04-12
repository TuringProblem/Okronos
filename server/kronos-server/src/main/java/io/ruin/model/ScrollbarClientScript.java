package io.ruin.model;

public class ScrollbarClientScript extends ClientScriptBuilder {

	private int interfaceId;
	private int containerId;
	private int childrenCount = 0;
	private int scrollbarChildId;
	private boolean scrollToEnd = false;

	private int fillGraphicId = 0;
	private int topGraphicId;
	private int bottomGraphicId;

	private int scrollTopGraphicId = 0;
	private int scrollSidesGraphicId = 0;
	private int scrollBottomGraphicId = 0;

	public static ScrollbarClientScript create() {
		return new ScrollbarClientScript();
	}

	private ScrollbarClientScript() {
		super(10532);
	}

	public ScrollbarClientScript interfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
		return this;
	}

	public ScrollbarClientScript containerId(int containerId) {
		this.containerId = containerId;
		return this;
	}

	public ScrollbarClientScript scrollbarChildId(int scrollbarChildId) {
		this.scrollbarChildId = scrollbarChildId;
		return this;
	}

	public ScrollbarClientScript childrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
		return this;
	}

	public ScrollbarClientScript scrollToTheEnd(boolean value) {
		this.scrollToEnd = value;
		return this;
	}

	public ScrollbarClientScript withDefaultGraphics() {
		this.fillGraphicId = 798;
		this.scrollTopGraphicId = 795;
		this.scrollSidesGraphicId = 796;
		this.scrollBottomGraphicId = 797;
		this.topGraphicId = 793;
		this.bottomGraphicId = 794;
		return this;
	}

	public ScrollbarClientScript withDarkGraphics() {
		this.fillGraphicId = 792;
		this.scrollTopGraphicId = 789;
		this.scrollSidesGraphicId = 790;
		this.scrollBottomGraphicId = 791;
		this.topGraphicId = 773;
		this.bottomGraphicId = 788;
		return this;
	}

	public ScrollbarClientScript build() {
		this.addInt(this.scrollToEnd ? 0 : 1);
		this.addInt(this.childrenCount);
		this.addInt(this.interfaceId << 16 | this.containerId);
		this.addInt(this.interfaceId << 16 | this.scrollbarChildId);
		this.addInt(this.fillGraphicId);
		this.addInt(this.scrollTopGraphicId);
		this.addInt(this.scrollSidesGraphicId);
		this.addInt(this.scrollBottomGraphicId);
		this.addInt(this.topGraphicId);
		this.addInt(this.bottomGraphicId);
		return this;
	}

}


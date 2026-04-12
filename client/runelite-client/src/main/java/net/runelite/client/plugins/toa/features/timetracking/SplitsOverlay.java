package net.runelite.client.plugins.toa.features.timetracking;

import net.runelite.client.plugins.toa.TombsOfAmascutConfig;
import net.runelite.client.plugins.toa.module.PluginLifecycleComponent;
import net.runelite.client.plugins.toa.util.RaidRoom;
import net.runelite.client.plugins.toa.util.RaidState;
import net.runelite.client.plugins.toa.util.RaidStateTracker;
import net.runelite.client.plugins.toa.util.TimerMode;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

@Slf4j
@Singleton
public class SplitsOverlay extends OverlayPanel implements PluginLifecycleComponent
{

	private final OverlayManager overlayManager;

	private final TargetTimeManager targetTimeManager;
	private final SplitsTracker splitsTracker;
	private final RaidStateTracker raidStateTracker;
	private final Client client;

	private SplitsMode splitsMode;

	@Inject
	public SplitsOverlay(OverlayManager overlayManager, TargetTimeManager targetTimeManager, SplitsTracker splitsTracker, RaidStateTracker raidStateTracker, Client client)
	{
		this.overlayManager = overlayManager;
		this.targetTimeManager = targetTimeManager;
		this.splitsTracker = splitsTracker;
		this.raidStateTracker = raidStateTracker;
		this.client = client;

		setPosition(OverlayPosition.BOTTOM_LEFT);
	}

	@Override
	public boolean isEnabled(TombsOfAmascutConfig config, RaidState raidState)
	{
		this.splitsMode = config.splitsOverlay();
		return splitsMode != SplitsMode.OFF && raidState.isInRaid();
	}

	@Override
	public void startUp()
	{
		overlayManager.add(this);
	}

	@Override
	public void shutDown()
	{
		overlayManager.remove(this);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		getPanelComponent().getChildren()
			.add(TitleComponent.builder()
				.text("ToA Splits")
				.build());

		RaidRoom currentRoom = raidStateTracker.getCurrentState().getCurrentRoom();
		boolean hasCurrentTime = false;
		for (Split s : splitsTracker.getSplits())
		{
			if (splitsMode.includesRoom(s.getRoom()))
			{
				hasCurrentTime = hasCurrentTime || currentRoom == s.getRoom();
				addLine(s.getRoom().toString(), s.getSplit());
			}
		}

		String currentName;
		if (!hasCurrentTime && (currentName = splitsMode.nextSplit(currentRoom)) != null)
		{
			addLine(currentName, currentTimerValue());
		}

		String targetTime = targetTimeManager.getTargetTime();
		if (targetTime != null)
		{
			addLine("Target", targetTime);
		}

		return super.render(graphics);
	}

	private void addLine(String left, String right)
	{
		getPanelComponent()
			.getChildren()
			.add(LineComponent.builder()
				.left(left)
				.right(right)
				.build());
	}

	private String currentTimerValue()
	{
		Widget w = client.getWidget(TargetTimeManager.WIDGET_TIMER);
		String text;
		if (w == null || (text = w.getText()) == null)
		{
			return TimerMode.fromClient(client) == TimerMode.PRECISE
				? "--:--.--"
				: "--:--";
		}

		return text.split("/")[0].trim();
	}
}

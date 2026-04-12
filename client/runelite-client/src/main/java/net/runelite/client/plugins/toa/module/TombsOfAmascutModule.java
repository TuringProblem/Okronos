package net.runelite.client.plugins.toa.module;

import net.runelite.client.plugins.toa.TombsOfAmascutConfig;
import net.runelite.client.plugins.toa.features.CameraShakeDisabler;
import net.runelite.client.plugins.toa.features.FadeDisabler;
import net.runelite.client.plugins.toa.features.InvocationScreenshot;
import net.runelite.client.plugins.toa.features.LeftClickBankAll;
import net.runelite.client.plugins.toa.features.SmellingSaltsCooldown;
import net.runelite.client.plugins.toa.features.boss.akkha.AkkhaShadowHealth;
import net.runelite.client.plugins.toa.features.boss.akkha.AkkhaShadowHealthOverlay;
import net.runelite.client.plugins.toa.features.apmeken.ApmekenBaboonIndicator;
import net.runelite.client.plugins.toa.features.apmeken.ApmekenBaboonIndicatorOverlay;
import net.runelite.client.plugins.toa.features.het.pickaxe.DepositPickaxeOverlay;
import net.runelite.client.plugins.toa.features.het.pickaxe.DepositPickaxePreventEntry;
import net.runelite.client.plugins.toa.features.het.solver.HetSolver;
import net.runelite.client.plugins.toa.features.het.solver.HetSolverOverlay;
import net.runelite.client.plugins.toa.features.hporbs.HpOrbManager;
import net.runelite.client.plugins.toa.features.QuickProceedSwaps;
import net.runelite.client.plugins.toa.features.apmeken.ApmekenWaveInstaller;
import net.runelite.client.plugins.toa.features.het.beamtimer.BeamTimerOverlay;
import net.runelite.client.plugins.toa.features.het.beamtimer.BeamTimerTracker;
import net.runelite.client.plugins.toa.features.het.pickaxe.DepositPickaxeSwap;
import net.runelite.client.plugins.toa.features.invocationpresets.InvocationPresetsManager;
import net.runelite.client.plugins.toa.features.PathLevelTracker;
import net.runelite.client.plugins.toa.features.pointstracker.PartyPointsTracker;
import net.runelite.client.plugins.toa.features.pointstracker.PointsOverlay;
import net.runelite.client.plugins.toa.features.pointstracker.PointsTracker;
import net.runelite.client.plugins.toa.features.scabaras.SkipObeliskOverlay;
import net.runelite.client.plugins.toa.features.scabaras.overlay.AdditionPuzzleSolver;
import net.runelite.client.plugins.toa.features.scabaras.overlay.LightPuzzleSolver;
import net.runelite.client.plugins.toa.features.scabaras.overlay.MatchingPuzzleSolver;
import net.runelite.client.plugins.toa.features.scabaras.overlay.ObeliskPuzzleSolver;
import net.runelite.client.plugins.toa.features.scabaras.overlay.ScabarasOverlayManager;
import net.runelite.client.plugins.toa.features.scabaras.overlay.SequencePuzzleSolver;
import net.runelite.client.plugins.toa.features.scabaras.panel.ScabarasPanelManager;
import net.runelite.client.plugins.toa.features.timetracking.SplitsOverlay;
import net.runelite.client.plugins.toa.features.timetracking.SplitsTracker;
import net.runelite.client.plugins.toa.features.timetracking.TargetTimeManager;
import net.runelite.client.plugins.toa.features.tomb.CursedPhalanxDetector;
import net.runelite.client.plugins.toa.features.tomb.DryStreakTracker;
import net.runelite.client.plugins.toa.features.tomb.SarcophagusRecolorer;
import net.runelite.client.plugins.toa.features.tomb.SarcophagusOpeningSoundPlayer;
import net.runelite.client.plugins.toa.features.updatenotifier.UpdateNotifier;
import net.runelite.client.plugins.toa.util.RaidStateTracker;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;

@Slf4j
public class TombsOfAmascutModule extends AbstractModule
{

	@Override
	protected void configure()
	{
		Multibinder<PluginLifecycleComponent> lifecycleComponents = Multibinder.newSetBinder(binder(), PluginLifecycleComponent.class);
		lifecycleComponents.addBinding().to(AdditionPuzzleSolver.class);
		lifecycleComponents.addBinding().to(AkkhaShadowHealth.class);
		lifecycleComponents.addBinding().to(AkkhaShadowHealthOverlay.class);
		lifecycleComponents.addBinding().to(ApmekenBaboonIndicator.class);
		lifecycleComponents.addBinding().to(ApmekenBaboonIndicatorOverlay.class);
		lifecycleComponents.addBinding().to(ApmekenWaveInstaller.class);
		lifecycleComponents.addBinding().to(BeamTimerOverlay.class);
		lifecycleComponents.addBinding().to(BeamTimerTracker.class);
		lifecycleComponents.addBinding().to(CameraShakeDisabler.class);
		lifecycleComponents.addBinding().to(CursedPhalanxDetector.class);
		lifecycleComponents.addBinding().to(DepositPickaxeOverlay.class);
		lifecycleComponents.addBinding().to(DepositPickaxePreventEntry.class);
		lifecycleComponents.addBinding().to(DepositPickaxeSwap.class);
		lifecycleComponents.addBinding().to(DryStreakTracker.class);
		lifecycleComponents.addBinding().to(FadeDisabler.class);
		lifecycleComponents.addBinding().to(HetSolver.class);
		lifecycleComponents.addBinding().to(HetSolverOverlay.class);
		lifecycleComponents.addBinding().to(HpOrbManager.class);
		lifecycleComponents.addBinding().to(InvocationPresetsManager.class);
		lifecycleComponents.addBinding().to(InvocationScreenshot.class);
		lifecycleComponents.addBinding().to(LeftClickBankAll.class);
		lifecycleComponents.addBinding().to(LightPuzzleSolver.class);
		lifecycleComponents.addBinding().to(MatchingPuzzleSolver.class);
		lifecycleComponents.addBinding().to(ObeliskPuzzleSolver.class);
		lifecycleComponents.addBinding().to(PartyPointsTracker.class);
		lifecycleComponents.addBinding().to(PathLevelTracker.class);
		lifecycleComponents.addBinding().to(PointsOverlay.class);
		lifecycleComponents.addBinding().to(PointsTracker.class);
		lifecycleComponents.addBinding().to(QuickProceedSwaps.class);
		lifecycleComponents.addBinding().to(RaidStateTracker.class);
		lifecycleComponents.addBinding().to(SarcophagusOpeningSoundPlayer.class);
		lifecycleComponents.addBinding().to(SarcophagusRecolorer.class);
		lifecycleComponents.addBinding().to(ScabarasOverlayManager.class);
		lifecycleComponents.addBinding().to(ScabarasPanelManager.class);
		lifecycleComponents.addBinding().to(SequencePuzzleSolver.class);
		lifecycleComponents.addBinding().to(SkipObeliskOverlay.class);
		lifecycleComponents.addBinding().to(SmellingSaltsCooldown.class);
		lifecycleComponents.addBinding().to(SplitsOverlay.class);
		lifecycleComponents.addBinding().to(SplitsTracker.class);
		lifecycleComponents.addBinding().to(TargetTimeManager.class);
		lifecycleComponents.addBinding().to(UpdateNotifier.class);
	}

	@Provides
	@Singleton
	TombsOfAmascutConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TombsOfAmascutConfig.class);
	}

}

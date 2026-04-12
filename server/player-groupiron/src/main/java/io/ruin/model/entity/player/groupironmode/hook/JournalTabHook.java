package io.ruin.model.entity.player.groupironmode.hook;

import io.ruin.HooksV2.Result;
import io.ruin.model.entity.player.groupironmode.GroupSettingsInterface;
import io.ruin.model.entity.player.groupironmode.GroupSettingsTabInterface;
import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.SimpleAction;
import io.ruin.model.inter.questtab.JournalTab;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(Attributes.class)
public class JournalTabHook {

	public static void register() {
		JournalTab.hooks.register(JournalTab.Hook.UpdateCurrentSection.class, JournalTabHook::handle);
		InterfaceHandler.register(1086, interfaceHandler -> {
			interfaceHandler.actions[34] = (SimpleAction) GroupSettingsInterface::open;
		});
	}

	private static Result handle(JournalTab.Hook.UpdateCurrentSection ctx) {
		var section = ctx.section();
		if (section == null) {
			return Result.Pass;
		}
		var player = ctx.player();

		switch (section) {
			case JournalTab.Section.GROUP_IRON -> {
				GroupSettingsTabInterface.open(player);
			}
			default -> {
			}
		}
		return Result.Pass;
	}
}

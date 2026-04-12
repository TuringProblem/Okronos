package inter.charactercreator;

import io.ruin.model.inter.InterfaceHandler;
import io.ruin.model.inter.actions.DefaultAction;
import io.ruin.model.var.VarPlayerRepository;

public class CharacterCreator {
	public static final int INTERFACE_ID = 679;

	public static void register() {
		class State {
			int hairc = 0;
			int torsoc = 0;
			int legsc = 0;
			int feetc = 0;
			int skinc = 0;
		}

		InterfaceHandler.register(INTERFACE_ID, h -> {
			for (Style value : Style.values()) {
				h.simpleAction(value.childNext, value::next);
				h.simpleAction(value.childPrevious, value::previous);
			}

			h.actions[46] = (DefaultAction) ((p, ignore, ignore2, ignore3) -> {
				var state = p.getOrPut("character_creator_state", new State());
				if (state.hairc == 0) {
					state.hairc = 25;
				}
				p.getAppearance().colors[0] = state.hairc--;
				p.getAppearance().update();
			});

			h.actions[47] = (DefaultAction) ((p, ignore, ignore2, ignore3) -> {
				var state = p.getOrPut("character_creator_state", new State());
				if (state.hairc == 25) {
					state.hairc = 0;
				}
				p.getAppearance().colors[0] = state.hairc++;
				p.getAppearance().update();
			});

			h.actions[50] = (DefaultAction) ((p, ignore, ignore2, ignore3) -> {
				var state = p.getOrPut("character_creator_state", new State());
				if (state.torsoc == 0) {
					state.torsoc = 29;
				}
				p.getAppearance().colors[1] = state.torsoc--;
				p.getAppearance().update();
			});

			h.actions[51] = (DefaultAction) ((p, ignore, ignore2, ignore3) -> {
				var state = p.getOrPut("character_creator_state", new State());
				if (state.torsoc == 29) {
					state.torsoc = 0;
				}
				p.getAppearance().colors[1] = state.torsoc++;
				p.getAppearance().update();
			});

			h.actions[54] = (DefaultAction) ((p, ignore, ignore2, ignore3) -> {
				var state = p.getOrPut("character_creator_state", new State());
				if (state.legsc == 0) {
					state.legsc = 29;
				}
				p.getAppearance().colors[2] = state.legsc--;
				p.getAppearance().update();
			});

			h.actions[55] = (DefaultAction) ((p, ignore, ignore2, ignore3) -> {
				var state = p.getOrPut("character_creator_state", new State());
				if (state.legsc == 29) {
					state.legsc = 0;
				}
				p.getAppearance().colors[2] = state.legsc++;
				p.getAppearance().update();
			});

			h.actions[58] = (DefaultAction) ((p, ignore, ignore2, ignore3) -> {
				var state = p.getOrPut("character_creator_state", new State());
				if (state.feetc == 0) {
					state.feetc = 7;
				}
				p.getAppearance().colors[3] = state.feetc--;
				p.getAppearance().update();
			});

			h.actions[59] = (DefaultAction) ((p, ignore, ignore2, ignore3) -> {
				var state = p.getOrPut("character_creator_state", new State());
				if (state.feetc == 7) {
					state.feetc = 0;
				}
				p.getAppearance().colors[3] = state.feetc++;
				p.getAppearance().update();
			});

			h.actions[62] = (DefaultAction) ((p, ignore, ignore2, ignore3) -> {
				var state = p.getOrPut("character_creator_state", new State());
				if (state.skinc == 0) {
					state.skinc = 8;
				}
				p.getAppearance().colors[4] = state.skinc--;
				p.getAppearance().update();
			});

			h.actions[63] = (DefaultAction) ((p, ignore, ignore2, ignore3) -> {
				var state = p.getOrPut("character_creator_state", new State());
				if (state.skinc == 8) {
					state.skinc = 0;
				}
				p.getAppearance().colors[4] = state.skinc++;
				p.getAppearance().update();
			});

			h.actions[69] = (DefaultAction) ((p, ignore, ignore2, ignore3) -> {
				VarPlayerRepository.GENDER_VAR.set(p, 1);
				VarPlayerRepository.BODY_TYPE_BUTTON.set(p, 1);
				p.getAppearance().setGender(1);
				Style.updateAll(p);
				p.getAppearance().update();
			});

			h.actions[68] = (DefaultAction) ((p, ignore, ignore2, ignore3) -> {
				VarPlayerRepository.GENDER_VAR.set(p, 0);
				VarPlayerRepository.BODY_TYPE_BUTTON.set(p, 0);
				p.getAppearance().setGender(0);
				Style.updateAll(p);
				p.getAppearance().update();
			});

			h.actions[78] = (DefaultAction) ((p, v0, v1, v2) -> {
				VarPlayerRepository.GENDER_PRONOUN.set(p, v1 - 1);
			});

			h.actions[74] = (DefaultAction) ((p, ignore, ignore2, ignore3) -> {
				p.getAppearance().update();
				p.closeInterfaces();
			});
		});
	}
}

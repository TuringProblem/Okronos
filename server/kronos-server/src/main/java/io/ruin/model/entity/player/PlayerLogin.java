package io.ruin.model.entity.player;

import io.ruin.api.protocol.Response;
import io.ruin.api.protocol.login.LoginInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlayerLogin {

	public record Cause(Response response, String text) {

	}

	public interface Handler {
		void denied(Cause cause);

		void accepted(PlayerLoginWorker.AcceptedPlayerLogin login);
	}

	public final LoginInfo info;
	private final Handler handler;

	public PlayerLogin(LoginInfo info, Handler handler) {
		this.info = info;
		this.handler = handler;
	}

	public final void accepted(PlayerLoginWorker.AcceptedPlayerLogin index) {
		if (this.handler == null) {
			return;
		}
		this.handler.accepted(index);
	}

	public void deny(Response response) {
		if (this.handler == null) {
			return;
		}
		this.handler.denied(new Cause(response, null));
	}

	public void deny(String cause) {
		if (this.handler == null) {
			return;
		}
		this.handler.denied(new Cause(null, cause));
	}
}

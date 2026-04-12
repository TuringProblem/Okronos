package core.task;

public interface Time {
	int TIME_MS_CLIENT = 20;
	int TIME_MS_SERVER = 600;

	static Client client(int ticks) {
		return new Client(ticks);
	}

	static Server server(int ticks) {
		return new Server(ticks);
	}

	static Time zero() {
		return new Client(0);
	}

	int ticks();

	Server server();

	Client client();

	Time add(Time other);

	default Time half() {
		return this.div(2);
	}

	Time div(int div);

	class Client implements Time {
		private final int ticks;

		public Client(int ticks) {
			this.ticks = ticks;
		}

		@Override
		public Server server() {
			return Time.server(this.ticks * TIME_MS_CLIENT / TIME_MS_SERVER);
		}

		@Override
		public Client client() {
			return this;
		}

		@Override
		public int ticks() {
			return this.ticks;
		}

		@Override
		public Time add(Time other) {
			return Time.client(this.ticks + other.client().ticks());
		}

		@Override
		public Time div(int div) {
			if (this.ticks == 0 || div == 0) {
				return Time.zero();
			}
			return new Client(this.ticks / div);
		}
	}

	class Server implements Time {
		private final int ticks;

		public Server(int ticks) {
			this.ticks = ticks;
		}

		@Override
		public Server server() {
			return this;
		}

		@Override
		public Client client() {
			return Time.client(this.ticks * TIME_MS_SERVER / TIME_MS_CLIENT);
		}

		@Override
		public int ticks() {
			return this.ticks;
		}

		@Override
		public Time add(Time other) {
			return Time.server(this.ticks + other.server().ticks());
		}

		@Override
		public Time div(int div) {
			if (this.ticks == 0 || div == 0) {
				return Time.zero();
			}
			return new Server(this.ticks / div);
		}
	}
}

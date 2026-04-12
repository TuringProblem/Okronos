package player.attributes;

/**
 * PlayerAttribute
 */
public class PlayerAttribute<T> {
	private final T[] values;

	@SuppressWarnings("unchecked")
	public PlayerAttribute() {
		this.values = (T[]) new Object[2048];
	}

	@SuppressWarnings("unchecked")
	void setRaw(int index, Object value) {
		this.set(index, (T) value);
	}

	public void set(int index, T value) {
		this.values[index] = value;
	}

	public T get(int index) {
		return this.values[index];
	}
}

package io.ruin.network.incoming;

import io.ruin.api.buffer.InBuffer;
import io.ruin.api.utils.PackageLoader;
import io.ruin.model.entity.player.Player;
import io.ruin.utility.IdHolder;

public interface Incoming {

	Incoming[] HANDLERS = new Incoming[256];

	int[] OPTIONS = new int[256];

	boolean[] IGNORED = new boolean[256];

	int[] SIZES = new int[256];

	static void load() throws Exception {
		for (final Class<Incoming> c : PackageLoader.loadImplementing(
			Incoming.class,
			"io.ruin.network.incoming.handlers")
		) {
			final Incoming incoming = c.getDeclaredConstructor().newInstance();
			final IdHolder idHolder = c.getAnnotation(IdHolder.class);
			if (idHolder == null) {
				/* handler is disabled, most likely for upgrading */
				continue;
			}
			int option = 1;
			for (int id : idHolder.ids()) {
				HANDLERS[id] = incoming;
				OPTIONS[id] = option++;
			}
		}

		/**
		 * Ignored
		 */
		int[] ignored = {
			72,
			57,
			6,
			85,
			62,
			82,
			99,
			42

		};
		for (int opcode : ignored)
			IGNORED[opcode] = true;
		/**
		 * Sizes
		 */
		for (int i = 0; i < SIZES.length; i++)
			SIZES[i] = Byte.MIN_VALUE;
		SIZES[0] = 8;
		SIZES[1] = 15;
		SIZES[2] = 9;
		SIZES[3] = 8;
		SIZES[4] = 7;
		SIZES[5] = 4;
		SIZES[6] = -2;
		SIZES[7] = 15;
		SIZES[8] = 7;
		SIZES[9] = 3;
		SIZES[10] = 3;
		SIZES[11] = -2;
		SIZES[12] = 0;
		SIZES[13] = 8;
		SIZES[14] = 3;
		SIZES[15] = 8;
		SIZES[16] = 8;
		SIZES[17] = -1;
		SIZES[18] = -1;
		SIZES[19] = -1;
		SIZES[20] = 3;
		SIZES[21] = 7;
		SIZES[22] = 7;
		SIZES[23] = 3;
		SIZES[24] = 8;
		SIZES[25] = 8;
		SIZES[26] = 16;
		SIZES[27] = 3;
		SIZES[28] = -1;
		SIZES[29] = 7;
		SIZES[30] = 3;
		SIZES[31] = 15;
		SIZES[32] = 3;
		SIZES[33] = 5;
		SIZES[34] = 7;
		SIZES[35] = -1;
		SIZES[36] = -1;
		SIZES[37] = 11;
		SIZES[38] = -1;
		SIZES[39] = 16;
		SIZES[40] = 7;
		SIZES[41] = 7;
		SIZES[42] = -1;
		SIZES[43] = -1;
		SIZES[44] = 4;
		SIZES[45] = 8;
		SIZES[46] = 3;
		SIZES[47] = 3;
		SIZES[48] = 8;
		SIZES[49] = 7;
		SIZES[50] = 8;
		SIZES[51] = 8;
		SIZES[52] = 8;
		SIZES[53] = -1;
		SIZES[54] = 11;
		SIZES[55] = 8;
		SIZES[56] = 2;
		SIZES[57] = 0;
		SIZES[58] = 7;
		SIZES[59] = -1;
		SIZES[60] = -1;
		SIZES[61] = -1;
		SIZES[62] = 6;
		SIZES[63] = -1;
		SIZES[64] = 3;
		SIZES[65] = 6;
		SIZES[66] = 14;
		SIZES[67] = 8;
		SIZES[68] = -1;
		SIZES[69] = 2;
		SIZES[70] = -1;
		SIZES[71] = 3;
		SIZES[72] = -1;
		SIZES[73] = 8;
		SIZES[74] = -1;
		SIZES[75] = -1;
		SIZES[76] = 8;
		SIZES[77] = -1;
		SIZES[78] = 16;
		SIZES[79] = 3;
		SIZES[80] = 0;
		SIZES[81] = -2;
		SIZES[82] = 4;
		SIZES[83] = 11;
		SIZES[84] = 7;
		SIZES[85] = 1;
		SIZES[86] = 0;
		SIZES[87] = 4;
		SIZES[88] = 3;
		SIZES[89] = 8;
		SIZES[90] = 10;
		SIZES[91] = 13;
		SIZES[92] = 11;
		SIZES[93] = -1;
		SIZES[94] = -1;
		SIZES[95] = 2;
		SIZES[96] = 0;
		SIZES[97] = 2;
		SIZES[98] = -1;
		SIZES[99] = 4;
		SIZES[100] = 2;
		SIZES[101] = 15;
		SIZES[102] = 8;
		SIZES[103] = 9;
		SIZES[104] = 8;
		SIZES[105] = 8;
		SIZES[106] = 22;
	}

	/**
	 * Separator
	 */

	void handle(Player player, InBuffer in, int opcode);

}

package io.ruin.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Maps;

import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

public class DbTable {

	private static Map<Integer, DbTable> cached = Maps.newConcurrentMap();

	public final Map<Integer, DBRow> rows = new HashMap<>();

	public static DbTable get(int id) {
		if (cached.containsKey(id)) {
			return cached.get(id);
		}

		if (id < 0) {
			return null;
		}

		var value = new DbTable();
		var dbIndexIndex = Server.fileStore.get(21);
		var dbRowsIndex = Server.fileStore.get(2);
		var indexData = dbIndexIndex.getFile(id, 0);
		var index = new DBTableIndex(0);
		index.decode(new InBuffer(indexData));

		for (int j = 0; j < index.rowIndicies.length; j++) {
			for (var rowIdx : index.rowIndicies[j]) {
				var row = new DBRow(rowIdx);
				row.decode(new InBuffer(dbRowsIndex.getFile(38, rowIdx)));
				value.rows.put(rowIdx, row);
			}
		}

		cached.put(id, value);
		return value;
	}

	public List<DBRow> rowsSorted() {
		return this.rows.entrySet().stream()
				.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
				.map(it -> it.getValue())
				.toList();
	}

	@EqualsAndHashCode
	@RequiredArgsConstructor
	public static class DBTableIndex {
		public final int column;
		// [col][row]
		public int[][] rowIndicies = new int[0][0];

		void decode(InBuffer buffer) {
			var colLen = buffer.readIntBits();
			this.rowIndicies = new int[colLen][];
			for (var colIdx = 0; colIdx < colLen; colIdx++) {
				var colMetaTy = buffer.readUnsignedByte();
				var valueLen = buffer.readIntBits();

				for (var i = 0; i < valueLen; i++) {
					switch (colMetaTy) {
						case 0 -> {
							buffer.readInt();
						}
						case 1 -> {
							buffer.readLong();
						}
						case 2 -> {
							buffer.readString();
						}
					}
				}

				var rowCnt = buffer.readIntBits();
				this.rowIndicies[colIdx] = new int[rowCnt];
				for (var rowIdx = 0; rowIdx < rowIndicies[colIdx].length; rowIdx++) {
					this.rowIndicies[colIdx][rowIdx] = buffer.readIntBits();
				}

			}
		}
	}

	@EqualsAndHashCode
	@RequiredArgsConstructor
	public static class DBRow {
		public final int id;
		public Map<Integer, Object[][]> columns = new HashMap<>();

		void decode(InBuffer buffer) {
			while (true) {
				var op = buffer.readUnsignedByte();
				switch (op) {
					case 0 -> {
						return;
					}
					case 3 -> {
						var colLen = buffer.readUnsignedByte();

						while (true) {
							var id = buffer.readUnsignedByte();
							if (id == 0xff) {
								break;
							}
							var typesLen = buffer.readUnsignedByte();
							boolean[] types = new boolean[typesLen];
							for (int i = 0; i < typesLen; i++) {
								var typeId = buffer.readUSmart();
								var isString = typeId == 36;
								types[i] = isString;
							}

							var fieldsLen = buffer.readUSmart();
							var fields = new Object[fieldsLen][];
							for (int i = 0; i < fieldsLen; i++) {
								fields[i] = new Object[types.length];
								for (int j = 0; j < types.length; j++) {
									Object value = null;
									if (types[j]) {
										value = buffer.readSafeString();
									} else {
										value = buffer.readInt();
									}
									fields[i][j] = value;
								}
							}

							this.columns.put(id, fields);
						}
					}

					case 4 -> {
						var tableId = buffer.readIntBits();
					}

					default -> {
						throw new IllegalStateException("invalid op: " + op);
					}
				}

			}
		}
	}
}

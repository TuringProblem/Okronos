package io.ruin.cache;

import io.ruin.Server;
import io.ruin.api.buffer.InBuffer;
import io.ruin.api.filestore.IndexFile;

public class HitmarkType {
	int fontId;
	public int textColor;
	public int duration;
	int attackTypeIcon;
	int hitmarkCenterId;
	int hitmarkLeftId;
	int hitmarkRightId;
	String amountTextTemplate;
	public int offsetX;
	public int offsetY;
	public int fade;
	public int comparisonType;
	public int field2021;
	public int[] transforms;
	int transformVarbit;
	int transformVarp;


	public static HitmarkType get(int id) {
		IndexFile index = Server.fileStore.get(2);
		byte[] data = index.getFile(32, id);
		HitmarkType map = new HitmarkType();
		if (data != null)
			map.decode(new InBuffer(data));
		return map;
	}

	HitmarkType() {
		this.fontId = -1;
		this.textColor = 16777215;
		this.duration = 70;
		this.attackTypeIcon = -1;
		this.hitmarkCenterId = -1;
		this.hitmarkLeftId = -1;
		this.hitmarkRightId = -1;
		this.offsetX = 0;
		this.offsetY = 0;
		this.fade = -1;
		this.amountTextTemplate = "";
		this.comparisonType = -1;
		this.field2021 = 0;
		this.transformVarbit = -1;
		this.transformVarp = -1;
	}

	void decode(InBuffer in) {
		for (; ; ) {
			int var3 = in.readUnsignedByte();
			if (var3 == 0)
				break;
			decode(in, var3);
		}
	}

	void decode(InBuffer var1, int var2) {
		if (var2 == 1) {
			this.fontId = var1.gSmart2or4null();
		} else if (var2 == 2) {
			this.textColor = var1.readMedium();
		} else if (var2 == 3) {
			this.attackTypeIcon = var1.gSmart2or4null();
		} else if (var2 == 4) {
			this.hitmarkLeftId = var1.gSmart2or4null();
		} else if (var2 == 5) {
			this.hitmarkCenterId = var1.gSmart2or4null();
		} else if (var2 == 6) {
			this.hitmarkRightId = var1.gSmart2or4null();
		} else if (var2 == 7) {
			this.offsetX = var1.readShort();
		} else if (var2 == 8) {
			this.amountTextTemplate = var1.readStringCp1252NullCircumfixed();
		} else if (var2 == 9) {
			this.duration = var1.readUnsignedShort();
		} else if (var2 == 10) {
			this.offsetY = var1.readShort();
		} else if (var2 == 11) {
			this.fade = 0;
		} else if (var2 == 12) {
			this.comparisonType = var1.readUnsignedByte();
		} else if (var2 == 13) {
			this.field2021 = var1.readShort();
		} else if (var2 == 14) {
			this.fade = var1.readUnsignedShort();
		} else if (var2 == 17 || var2 == 18) {
			this.transformVarbit = var1.readUnsignedShort();
			if (this.transformVarbit == 65535) {
				this.transformVarbit = -1;
			}

			this.transformVarp = var1.readUnsignedShort();
			if (this.transformVarp == 65535) {
				this.transformVarp = -1;
			}

			int var3 = -1;
			if (var2 == 18) {
				var3 = var1.readUnsignedShort();
				if (var3 == 65535) {
					var3 = -1;
				}
			}

			int var4 = var1.readUnsignedByte();
			this.transforms = new int[var4 + 2];

			for (int var5 = 0; var5 <= var4; ++var5) {
				this.transforms[var5] = var1.readUnsignedShort();
				if (this.transforms[var5] == 65535) {
					this.transforms[var5] = -1;
				}
			}

			this.transforms[var4 + 1] = var3;
		}

	}


}

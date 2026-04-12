package io.ruin.api.protocol;

import io.ruin.api.buffer.InBuffer;
import lombok.Getter;
import lombok.ToString;

@ToString
public class PlatformInfo {


	@Getter
	int osType;
	@Getter
	int os64bit;
	@Getter
	int osVersionType;
	@Getter
	int javaVendor;
	@Getter
	int javaVersionMajor;
	@Getter
	int javaVersionMinor;
	@Getter
	int javaVersionPatch;
	@Getter
	boolean isConsole;
	@Getter
	int maxMemory;
	@Getter
	int availCpus;
	@Getter
	int availRam;
	@Getter
	int cpuClockspeed;
	@Getter
	String gpuName;
	@Getter
	String glVersion;
	@Getter
	String directXVersion;
	@Getter
	String gpuDriverName;
	@Getter
	int gpuDriverMonth;
	@Getter
	int gpuDriverYear;
	@Getter
	String cpuVendor;
	@Getter
	String cpuBrand;
	@Getter
	int cpuCount;
	@Getter
	int cpuBrandId;
	@Getter
	int[] cpuFeatures;
	@Getter
	int cpuModel;
	@Getter
	String unknown;
	@Getter
	int formatVersion;


	public PlatformInfo(InBuffer in) {
		formatVersion = in.readUnsignedByte();
		cpuFeatures = new int[3];
		osType = in.readUnsignedByte();
		os64bit = in.readUnsignedByte();
		osVersionType = in.readUnsignedShort();
		javaVendor = in.readUnsignedByte();
		javaVersionMajor = in.readUnsignedByte();
		javaVersionMinor = in.readUnsignedByte();
		javaVersionPatch = in.readUnsignedByte();
		isConsole = in.readUnsignedByte() == 1;
		maxMemory = in.readUnsignedShort();
		availCpus = in.readUnsignedByte();
		availRam = in.readMedium();
		cpuClockspeed = in.readUnsignedShort();
		gpuName = in.readStringCp1252NullCircumfixed();
		glVersion = in.readStringCp1252NullCircumfixed();
		directXVersion = in.readStringCp1252NullCircumfixed();
		gpuDriverName = in.readStringCp1252NullCircumfixed();
		gpuDriverMonth = in.readUnsignedByte();
		gpuDriverYear = in.readUnsignedShort();
		cpuVendor = in.readStringCp1252NullCircumfixed();
		cpuBrand = in.readStringCp1252NullCircumfixed();
		cpuCount = in.readUnsignedByte();
		cpuBrandId = in.readUnsignedByte();
		for (int i = 0; i < cpuFeatures.length; i++) {
			cpuFeatures[i] = in.readInt();
		}
		cpuModel = in.readInt();
		unknown = in.readStringCp1252NullCircumfixed();
	}

	public void update(InBuffer in) {
		formatVersion = in.readUnsignedByte();
		cpuFeatures = new int[3];
		osType = in.readUnsignedByte();
		os64bit = in.readUnsignedByte();
		osVersionType = in.readUnsignedShort();
		javaVendor = in.readUnsignedByte();
		javaVersionMajor = in.readUnsignedByte();
		javaVersionMinor = in.readUnsignedByte();
		javaVersionPatch = in.readUnsignedByte();
		isConsole = in.readUnsignedByte() == 1;
		maxMemory = in.readUnsignedShort();
		availCpus = in.readUnsignedByte();
		availRam = in.readMedium();
		cpuClockspeed = in.readUnsignedShort();
		gpuName = in.readStringCp1252NullCircumfixed();
		glVersion = in.readStringCp1252NullCircumfixed();
		directXVersion = in.readStringCp1252NullCircumfixed();
		gpuDriverName = in.readStringCp1252NullCircumfixed();
		gpuDriverMonth = in.readUnsignedByte();
		gpuDriverYear = in.readUnsignedShort();
		cpuVendor = in.readStringCp1252NullCircumfixed();
		cpuBrand = in.readStringCp1252NullCircumfixed();
		cpuCount = in.readUnsignedByte();
		cpuBrandId = in.readUnsignedByte();
		for (int i = 0; i < cpuFeatures.length; i++) {
			cpuFeatures[i] = in.readInt();
		}
		cpuModel = in.readInt();
		unknown = in.readStringCp1252NullCircumfixed();
	}


}


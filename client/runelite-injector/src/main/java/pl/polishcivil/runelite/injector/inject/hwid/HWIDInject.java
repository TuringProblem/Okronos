package pl.polishcivil.runelite.injector.inject.hwid;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import pl.polishcivil.runelite.injector.Injector;
import pl.polishcivil.runelite.injector.Injector.ClassGroup;

// TODO: hardcoded, update per rev
public class HWIDInject {

	private static String LAST_STR_FIELD_NAME = "ct";

	public static void apply(ClassGroup group) throws Exception {
		var hwidBytecode = getClassBytes(HWID.class);
		var hwid = Injector.read(hwidBytecode);
		group.add(hwid);
		var platformNode = platformInfoNode(group);
		var platformNodeWriteMethod = platformInit(platformNode);

		var insList = platformNodeWriteMethod.instructions;
		for (var ins : insList.toArray()) {
			if (ins instanceof FieldInsnNode fIns) {
				if (fIns.name.equals(LAST_STR_FIELD_NAME) && fIns.owner.equals(platformNode.name)
						&& fIns.getOpcode() == Opcodes.PUTFIELD) {
					var instructions = new InsnList();
					instructions.add(new InsnNode(Opcodes.POP));
					instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "pl/polishcivil/runelite/injector/inject/hwid/HWID",
							"hwid", "()Ljava/lang/String;"));
					insList.insertBefore(fIns, instructions);
					System.out.println("HWID injected " + platformNode.name);
					break;
				}
			}
		}
	}

	static ClassNode platformInfoNode(ClassGroup group) {
		var nodes = group.nodes.values().stream().filter(it -> {
			return it.methods.stream().anyMatch(m -> Injector.hasLdc(m, "12345678-0000-0000-0000-123456789012"));
		}).collect(Collectors.toList());

		if (nodes.size() != 1) {
			throw new IllegalStateException();
		}
		return nodes.get(0);
	}

	static MethodNode platformInit(ClassNode node) {
		return node.methods.stream().filter(it -> it.name.equals("<init>"))
				.findFirst()
				.orElseThrow();
	}

	public static byte[] getClassBytes(Class<?> clazz) throws IOException {
		String className = clazz.getName();
		String classAsPath = className.replace('.', '/') + ".class";
		try (InputStream is = clazz.getClassLoader().getResourceAsStream(classAsPath)) {
			if (is == null) {
				throw new IOException("Could not find class file for " + className);
			}
			return is.readAllBytes();
		}
	}
}

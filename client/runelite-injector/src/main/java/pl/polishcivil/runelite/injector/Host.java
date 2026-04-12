package pl.polishcivil.runelite.injector;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.*;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import pl.polishcivil.runelite.injector.Injector.ClassGroup;

/**
 * Host
 */
public class Host {

	public static int port = 43594;

	static void apply(ClassGroup group) {
		group.nodes.values().stream().flatMap(it -> {
			return it.methods.stream();
		}).filter(it -> {
			return constants(it.instructions, (cst) -> cst.equals("192.168.1.")).count() != 0;
		}).forEach(it -> {
			it.instructions.clear();
			it.tryCatchBlocks.clear();
			it.instructions.add(new InsnNode(Opcodes.ICONST_1));
			it.instructions.add(new InsnNode(Opcodes.IRETURN));
		});

		applyPort(group);
	}

	static void applyPort(ClassGroup group) {
		var node = group.nodes.get("client.class");
		node.methods.forEach(it -> {
			constantsInt(it.instructions, (cst) -> cst == 43594).forEach(ins -> {
				ins.cst = port;
			});
		});
	}

	static Stream<LdcInsnNode> constants(InsnList list, Function<String, Boolean> constant) {
		return instructions(list).filter(it -> {
			if (it instanceof LdcInsnNode && ((LdcInsnNode) it).cst instanceof String) {
				return constant.apply((String) ((LdcInsnNode) it).cst);
			} else {
				return false;
			}
		}).map(it -> (LdcInsnNode) it);
	}

	static Stream<LdcInsnNode> constantsInt(InsnList list, Function<Integer, Boolean> constant) {
		return instructions(list).filter(it -> {
			if (it.getOpcode() == Opcodes.LDC && ((LdcInsnNode) it).cst instanceof Number) {
				return constant.apply(((Number) ((LdcInsnNode) it).cst).intValue());
			} else {
				return false;
			}
		}).map(it -> (LdcInsnNode) it);
	}

	static Stream<AbstractInsnNode> instructions(InsnList list) {
		return Arrays.stream(list.toArray());
	}
}

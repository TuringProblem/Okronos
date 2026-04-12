package pl.polishcivil.runelite.injector;

import java.util.List;
import java.util.function.Function;
import java.util.stream.*;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import pl.polishcivil.runelite.injector.Injector.ClassGroup;

/**
 * Varp
 */
public class Varp {
	static void apply(ClassGroup group) {
		group.nodes.values().stream().flatMap(cls -> {
			return cls.methods.stream().map(it -> new Pair(cls, it));
		}).flatMap(it -> {
			return filter(it);
		}).forEach(it -> {
			var v0 = it.constant.getNext();
			var v1 = v0.getNext();
			if (v0.getOpcode() == Opcodes.NEWARRAY && v1 instanceof FieldInsnNode && v1.getOpcode() == Opcodes.PUTSTATIC) {
				it.constant.operand = 30_000;
				System.out.println("varps injected");
			}
		});
	}

	static Stream<Result> filter(Pair node) {
		return instructions(node.method.instructions).filter(it -> {
			if (it instanceof IntInsnNode && it.getOpcode() == Opcodes.SIPUSH) {
				return ((IntInsnNode) it).operand == 5000;
			} else {
				return false;
			}
		}).map(it -> new Result(node, (IntInsnNode) it));
	}

	static Stream<AbstractInsnNode> instructions(InsnList list) {
		return StreamSupport.stream(list.spliterator(), false);
	}

	static class Pair {
		ClassNode cls;
		MethodNode method;

		Pair(ClassNode cls, MethodNode method) {
			this.cls = cls;
			this.method = method;
		}
	}

	static class Result {
		Pair node;
		IntInsnNode constant;

		Result(Pair node, IntInsnNode constant) {
			this.node = node;
			this.constant = constant;
		}
	}
}

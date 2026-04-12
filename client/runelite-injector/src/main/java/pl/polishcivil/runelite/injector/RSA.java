package pl.polishcivil.runelite.injector;

import java.util.stream.*;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import pl.polishcivil.runelite.injector.Injector.ClassGroup;

/**
 * Host
 */
public class RSA {
	private static String RSA = "981d7e7e92f6676be104d3f7ff1b11e8f654534512f4d7b94ba6c5b7cfeffabf0eb2798e9b5437da5a50b73413491a8b47d5d3a091709d7aafba2caf0ba66760a54f654801ed2e3fcafaafb25db7cce7b96f30cc213555689026b523b1d1a27aa6d5ab4cbe1b1e7fe5c3b7b93006060c334db3c8aa2c5a85f83f3dd50b6a2ff9";

	static void apply(ClassGroup group) {
		group.nodes.values().stream().flatMap(cls -> {
			return cls.methods.stream().map(it -> new Pair(cls, it));
		}).flatMap(it -> {
			return filter(it);
		}).forEach(it -> {
			if (!it.keyNode.cst.equals("10001")) {
				System.out.println("FOUND RSA " + it.keyNode.cst);
				it.keyNode.cst = RSA;
			}
		});
	}

	static Stream<Result> filter(Pair node) {
		return instructions(node.method.instructions).filter(it -> {
			if (it instanceof MethodInsnNode && ((MethodInsnNode) it).name.equals("<init>")
					&& ((MethodInsnNode) it).owner.endsWith("BigInteger")) {
				var v0 = it.getPrevious();
				var v1 = it.getPrevious().getPrevious();
				return v0.getOpcode() == Opcodes.BIPUSH && v1 instanceof LdcInsnNode;
			} else {
				return false;
			}
		}).map(it -> new Result(node, (LdcInsnNode) it.getPrevious().getPrevious()));
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
		LdcInsnNode keyNode;

		Result(Pair node, LdcInsnNode keyNode) {
			this.node = node;
			this.keyNode = keyNode;
		}
	}
}

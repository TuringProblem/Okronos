package pl.polishcivil.runelite.injector;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import pl.polishcivil.runelite.injector.Injector.ClassGroup;

import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Host
 */
public class ClanChatCount {

	static void apply(ClassGroup group) {
		group.nodes.values().stream()
				.filter(it -> it.interfaces.stream().anyMatch(cls -> cls.contains("FriendsChatManager")))
				.flatMap(it -> {
					return it.methods.stream().filter(m -> m.name.contains("init"));
				}).forEach(method -> {
					constants(method.instructions, (cst) -> cst == 500).forEach(ins -> {
						System.out.println("clan count found");
						ins.operand = 2048;
					});
				});
	}

	static Stream<IntInsnNode> constants(InsnList list, Function<Integer, Boolean> constant) {
		return instructions(list).filter(it -> {
			if (it.getOpcode() == Opcodes.SIPUSH && it instanceof IntInsnNode) {
				var v = (IntInsnNode) it;
				return constant.apply(v.operand);
			}
			return false;
		}).map(it -> (IntInsnNode) it);
	}

	static Stream<AbstractInsnNode> instructions(InsnList list) {
		return StreamSupport.stream(list.spliterator(), false);
	}
}

package pl.polishcivil.runelite.injector;

import java.util.Arrays;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import pl.polishcivil.runelite.injector.Injector.ClassGroup;

// TODO(polish) - do i really need it?
public class UsernameInject {
	private static final String USERNAME_OWNER = "xm";
	private static final String USERNAME_CLEAN_NAME = "aj";
	private static final String USERNAME_NAME = "ap";
	private static final String USERNAME_DESC = "Ljava/lang/String;";

	static void apply(ClassGroup group) {
		group.node(USERNAME_OWNER).methods.forEach(mNode -> {
			var instructions = mNode.instructions;
			Arrays.stream(mNode.instructions.toArray()).forEach(ins -> {
				if (ins instanceof FieldInsnNode fIns && isPutField(fIns)) {
					var removeTagsCall = new MethodInsnNode(Opcodes.INVOKESTATIC, "net/runelite/client/util/Text", "removeTags", "(Ljava/lang/String;)Ljava/lang/String;");
					var insList = new InsnList();
					insList.add(new InsnNode(Opcodes.POP));
					insList.add(new VarInsnNode(Opcodes.ALOAD, 0));
					insList.add(new FieldInsnNode(Opcodes.GETFIELD, USERNAME_OWNER, USERNAME_NAME, "Ljava/lang/String;"));
					insList.add(removeTagsCall);
					//insList.add(new FieldInsnNode(Opcodes.PUTFIELD, USERNAME_OWNER, USERNAME_CLEAN_NAME, "Ljava/lang/String;"));
					instructions.insertBefore(ins, insList);
					System.out.println("Username injected");
				}
			});
		});
	}

	private static boolean isPutField(FieldInsnNode fIns) {
		return fIns.name.equals(USERNAME_CLEAN_NAME)
				&& fIns.owner.equals(USERNAME_OWNER)
				&& fIns.desc.equals(USERNAME_DESC)
				&& fIns.getOpcode() == Opcodes.PUTFIELD;
	}
}

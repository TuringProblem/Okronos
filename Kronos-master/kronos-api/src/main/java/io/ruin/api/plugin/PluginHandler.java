package io.ruin.api.plugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Marks a class as a content plugin. Classes annotated with @PluginHandler
 * are discovered by PluginManager and initialized via their static blocks.
 *
 * Core content lives in io.ruin.content.* and is loaded automatically.
 * Asset pack content lives in external JARs dropped into the plugins/ directory.
 *
 * Usage:
 *   @PluginHandler
 *   public class FlaxKeeper {
 *       static {
 *           NPCAction.register(5522, "talk-to", (player, npc) -> { ... });
 *       }
 *   }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PluginHandler {

    /**
     * Human-readable name for logging. Defaults to the class simple name.
     */
    String value() default "";

}

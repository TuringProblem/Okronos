package io.ruin.api.plugin;

import io.ruin.api.utils.ServerWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Discovers and initializes content plugins.
 *
 * Two sources:
 *   1. Built-in core content at io.ruin.content.* — loaded via the standard
 *      PackageLoader pass during server startup (no extra work needed).
 *   2. External asset pack JARs dropped into the plugins/ directory — loaded
 *      here at startup after core content.
 *
 * Plugins declare themselves with @PluginHandler. Their static blocks run on
 * Class.forName(), which triggers NPCAction.register / ObjectAction.register
 * etc. exactly as core content does.
 */
public class PluginManager {

    private static int loadedCount = 0;
    private static int failedCount = 0;

    /**
     * Scans the given directory for JAR files and loads every class inside
     * that is annotated with @PluginHandler. Call this after core content has
     * been initialized via PackageLoader.
     *
     * @param pluginsDir  directory to scan (created if it doesn't exist)
     */
    public static void loadExternalPlugins(File pluginsDir) {
        if (!pluginsDir.exists()) {
            pluginsDir.mkdirs();
            ServerWrapper.println("[PluginManager] plugins/ directory created. Drop asset pack JARs here.");
            return;
        }

        File[] jars = pluginsDir.listFiles(f -> f.getName().endsWith(".jar"));
        if (jars == null || jars.length == 0) {
            ServerWrapper.println("[PluginManager] No external plugin JARs found in " + pluginsDir.getPath());
            return;
        }

        ServerWrapper.println("[PluginManager] Loading " + jars.length + " external plugin JAR(s)...");

        for (File jar : jars) {
            loadJar(jar);
        }

        ServerWrapper.println("[PluginManager] External plugins loaded: " + loadedCount + " ok, " + failedCount + " failed.");
    }

    private static void loadJar(File jar) {
        ServerWrapper.println("[PluginManager] Loading: " + jar.getName());
        try {
            URL jarUrl = jar.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, Thread.currentThread().getContextClassLoader());

            List<String> classNames = findClassesInJar(jar);
            for (String className : classNames) {
                try {
                    Class<?> clazz = Class.forName(className, true, classLoader);
                    if (clazz.isAnnotationPresent(PluginHandler.class)) {
                        // Static block already ran via Class.forName with initialize=true
                        PluginHandler annotation = clazz.getAnnotation(PluginHandler.class);
                        String name = annotation.value().isEmpty() ? clazz.getSimpleName() : annotation.value();
                        ServerWrapper.println("[PluginManager]   + " + name);
                        loadedCount++;
                    }
                } catch (Throwable t) {
                    ServerWrapper.println("[PluginManager]   ! Failed to load class: " + className + " — " + t.getMessage());
                    failedCount++;
                }
            }
        } catch (Exception e) {
            ServerWrapper.println("[PluginManager] Failed to open JAR: " + jar.getName() + " — " + e.getMessage());
        }
    }

    private static List<String> findClassesInJar(File jar) {
        List<String> names = new ArrayList<>();
        try (ZipInputStream zip = new ZipInputStream(new FileInputStream(jar))) {
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                String name = entry.getName();
                if (name.endsWith(".class") && !name.contains("$")) {
                    // Convert path to class name: com/example/Foo.class -> com.example.Foo
                    names.add(name.replace('/', '.').substring(0, name.length() - 6));
                }
            }
        } catch (Exception e) {
            ServerWrapper.println("[PluginManager] Error reading JAR entries: " + jar.getName());
        }
        return names;
    }

    /**
     * Returns total external plugins successfully loaded this session.
     */
    public static int getLoadedCount() {
        return loadedCount;
    }

}

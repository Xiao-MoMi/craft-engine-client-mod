package net.momirealms.craftengine.fabric.logger;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.File;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public interface ModLogger {
    void info(String s);

    void warn(String s);

    default void warn(File file, String s) {
        warn("Error in file: " + file.getAbsolutePath() + " - " + s);
    }

    default void warn(Path file, String s) {
        warn("Error in file: " + file.toAbsolutePath() + " - " + s);
    }

    default void warn(Path file, String s, Throwable t) {
        warn("Error in file: " + file.toAbsolutePath() + " - " + s, t);
    }

    void warn(String s, Throwable t);

    void severe(String s);

    void severe(String s, Throwable t);
}


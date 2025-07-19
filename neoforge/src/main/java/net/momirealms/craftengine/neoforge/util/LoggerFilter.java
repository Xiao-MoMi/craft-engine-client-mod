package net.momirealms.craftengine.neoforge.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;

public class LoggerFilter {

    public static void filter() {
        Logger rootLogger = (Logger) LogManager.getRootLogger();
        rootLogger.addFilter(new AbstractFilter() {
            @Override
            public Result filter(LogEvent event) {
                if (event.getMessage().getFormattedMessage().contains("Missing model for variant: 'Block{craftengine:")) {
                    return Result.DENY;
                }
                return Result.NEUTRAL;
            }
        });
    }
}

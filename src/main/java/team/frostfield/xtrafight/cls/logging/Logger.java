package team.frostfield.xtrafight.cls.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.util.StackLocatorUtil;

public interface Logger extends org.apache.logging.log4j.Logger {

    static Logger getLogger() {
        return new LoggerImpl(StackLocatorUtil.getCallerClass(2).getSimpleName());
    }

    static Logger getLogger(String name) {
        return new LoggerImpl(name);
    }

    void infoIfDevSide(String message, Object... args);

    void logIfDevSide(String message, Level level, Object... args);

    void errorIfDevSide(String message, Object... args);

    void debugIfDevSide(String message, Object... args);
}

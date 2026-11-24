package team.frostfield.xtrafight.cls.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;
import team.frostfield.xtrafight.shared.XtraFightAPISharedConstants;

public class LoggerImpl extends ExtendedLoggerWrapper implements Logger {

    public LoggerImpl(String name) {
        this((ExtendedLogger) LogManager.getLogger(name), name);
    }

    private LoggerImpl(ExtendedLogger delegate, String name) {
        super(delegate, name, delegate.getMessageFactory());
    }

    @Override
    public void logIfDevSide(String message, Level level, Object... args) {
        if (isDevSide()) {
            super.log(level, message, args);
        }
    }

    @Override
    public void errorIfDevSide(String message, Object... args) {
        if (isDevSide()) {
            super.error(message, args);
        }
    }

    @Override
    public void infoIfDevSide(String message, Object... args) {
        if (isDevSide()) {
            super.info(message, args);
        }
    }

    @Override
    public void debugIfDevSide(String message, Object... args) {
        if (isDevSide()) {
            super.debug(message, args);
        }
    }

    private static boolean isDevSide() {
        return XtraFightAPISharedConstants.isDeveloperEnvironment();
    }
}
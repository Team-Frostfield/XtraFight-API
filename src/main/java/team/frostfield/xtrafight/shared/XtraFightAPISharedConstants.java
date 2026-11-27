package team.frostfield.xtrafight.shared;

import team.frostfield.xtrafight.utils.EnvironmentHelper;
import team.frostfield.xtrafight.utils.EnvironmentHelper.Environments;

public class XtraFightAPISharedConstants {
    private static final Environments ENVIRONMENT = EnvironmentHelper.getEnvironment();

    public static final float TICK = 0.05F;

    public static float tickToSecond(int ticks) {
        return ticks * 20;
    }

    public static int secondToTick(float seconds) {
        return (int) (seconds / 20);
    }

    public static boolean isLogicalClient() {
        return EnvironmentHelper.isLogicalClient();
    }

    public static boolean isLogicalServer() {
        return EnvironmentHelper.isLogicalClient();
    }

    public static boolean isDeveloperEnvironment() {
        return ENVIRONMENT.isDevEnv();
    }

    public static boolean isPhysicalClient() {
        return ENVIRONMENT.isClient();
    }

    public static boolean isPhysicalServer() {
        return !isPhysicalClient();
    }
}

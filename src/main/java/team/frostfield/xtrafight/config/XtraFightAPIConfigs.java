package team.frostfield.xtrafight.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import team.frostfield.xtrafight.cls.loadable.ILoadableClass;
import team.frostfield.xtrafight.cls.loadable.marks.Invoke;
import team.frostfield.xtrafight.main.XtraFightAPI;

public class XtraFightAPIConfigs implements ILoadableClass {
    public static class Common {
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        private static final ForgeConfigSpec.BooleanValue ENABLE_CAMERA_ANIMATIONS = BUILDER.comment("Whether to enable Camera Animations").define("enableCamAnimations", true);
        private static final ForgeConfigSpec.BooleanValue CAMERA_HEAD_CLIPPING_TOGGLE = BUILDER.comment("Whether to enable Head to be hidden if camera goes inside player head while camera animation").define("clipCamToHead", true);

        public static final ForgeConfigSpec SPEC = BUILDER.build();

        public static boolean isCamAnimationsEnabled() {
            return ENABLE_CAMERA_ANIMATIONS.get();
        }

        public static boolean shouldClipToHead() {
            return CAMERA_HEAD_CLIPPING_TOGGLE.get();
        }
    }

    public static class Client {
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        public static final ForgeConfigSpec SPEC = BUILDER.build();

        private static final ForgeConfigSpec.BooleanValue ENABLE_CAMERA_FOV_ANIMATION = BUILDER.comment("Whether to enable Camera Fov Animations").define("enableCamFovAnimations", true);
       
        public static boolean isCamFovAnimationEnabled(){
            return ENABLE_CAMERA_FOV_ANIMATION.get();
        }


    }

    public static class Server {
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        public static final ForgeConfigSpec SPEC = BUILDER.build();
    }

    @Invoke
    private XtraFightAPIConfigs(FMLJavaModLoadingContext context) {
        this.register(context);
        XtraFightAPI.LOGGER.debugIfDevSide("Registered XtraFight Configs!");
    }

    private void register(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, Common.SPEC);
        context.registerConfig(ModConfig.Type.CLIENT, Client.SPEC);
        context.registerConfig(ModConfig.Type.SERVER, Server.SPEC);
    }
}

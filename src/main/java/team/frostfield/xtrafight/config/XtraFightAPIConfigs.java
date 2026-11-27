package team.frostfield.xtrafight.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import team.frostfield.xtrafight.cls.loadable.ILoadableClass;
import team.frostfield.xtrafight.cls.loadable.marks.Invoke;

public class XtraFightAPIConfigs implements ILoadableClass {
    public static class Common {
        private static final ModConfigSpec.Builder BUILDER = new Builder();
        private static final ModConfigSpec.BooleanValue ENABLE_CAMERA_ANIMATIONS = BUILDER.comment("Whether to enable Camera Animations").define("enableCamAnimations", true);
        private static final ModConfigSpec.BooleanValue CAMERA_HEAD_CLIPPING_TOGGLE = BUILDER.comment("Whether to enable Head to be hidden if camera goes inside player head while camera animation").define("clipCamToHead", true);

        public static final ModConfigSpec SPEC = BUILDER.build();

        public static boolean isCamAnimationsEnabled() {
            return ENABLE_CAMERA_ANIMATIONS.getAsBoolean();
        }

        public static boolean shouldClipToHead() {
            return CAMERA_HEAD_CLIPPING_TOGGLE.getAsBoolean();
        }
    }

    public static class Client {
        private static final ModConfigSpec.Builder BUILDER = new Builder();

        public static final ModConfigSpec SPEC = BUILDER.build();

        private static final ModConfigSpec.BooleanValue ENABLE_CAMERA_FOV_ANIMATION = BUILDER.comment("Whether to enable Camera Fov Animations").define("enableCamFovAnimations", true);
       
        public static boolean isCamFovAnimationEnabled(){
            return ENABLE_CAMERA_FOV_ANIMATION.getAsBoolean();
        }


    }

    public static class Server {
        private static final ModConfigSpec.Builder BUILDER = new Builder();

        public static final ModConfigSpec SPEC = BUILDER.build();
    }

    @Invoke
    private XtraFightAPIConfigs(ModContainer container) {
        this.register(container);
    }

    private void register(ModContainer modContainer) {
        modContainer.registerConfig(Type.COMMON, Common.SPEC);
        modContainer.registerConfig(Type.CLIENT, Client.SPEC);
        modContainer.registerConfig(Type.SERVER, Server.SPEC);
    }
}

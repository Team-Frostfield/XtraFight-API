package team.frostfield.xtrafight.animation.client.camera;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.frostfield.xtrafight.animation.client.CameraAnimatorAPI;
import team.frostfield.xtrafight.events.xtrafight_api.registry.CameraAnimationRegistryDispatchEvent;
import team.frostfield.xtrafight.main.XtraFightAPI;
import team.frostfield.xtrafight.main.XtraFightAPIMod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = XtraFightAPIMod.MOD_ID)
public final class ExampleCameraAnimation {

    private static final List<CameraAnimatorAPI.CamAnim> CAM_ANIM_LIST = new ArrayList<>();

    private static CameraAnimatorAPI.CamAnim register(String name, ResourceLocation path) {
        // ResourceLocation, ResourceLocation?
        CameraAnimatorAPI.CamAnim anim = new CameraAnimatorAPI.CamAnim(XtraFightAPI.getInstance().identifier(name), path);
        if (CAM_ANIM_LIST.contains(anim)) {
            throw new IllegalStateException("CamAnimation " + anim.animationName() + " is already registered.");
        }
        CAM_ANIM_LIST.add(anim);

        return anim;
    }

    private static CameraAnimatorAPI.CamAnim register(ResourceLocation id) {
        return register(id.getPath(), ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "camera/" + id.getPath() + ".json"));
    }

    private static final CameraAnimatorAPI.CamAnim TEST2 = register(XtraFightAPI.getInstance().identifier("testcam"));


    private static final CameraAnimatorAPI.CamAnim TEST = new CameraAnimatorAPI.CamAnim(XtraFightAPI.getInstance().identifier("testcamera_2"));


    @SubscribeEvent
    public static void registerExampleCamAnim(CameraAnimationRegistryDispatchEvent event) {
        // you can register the animation in these 3 ways:
        // First, is the array registry
        event.register(TEST2, TEST);
        // Second, register from the class, reading all of its static fields
        event.registerFromClass(ExampleCameraAnimation.class);
        // Third, registering from a list, like so:
        event.registerFromList(CAM_ANIM_LIST);
    }

}

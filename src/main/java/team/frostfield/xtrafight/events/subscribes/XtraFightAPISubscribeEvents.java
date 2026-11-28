package team.frostfield.xtrafight.events.subscribes;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.frostfield.xtrafight.animation.client.CameraAnimatorAPI;
import team.frostfield.xtrafight.client.gui.InteractionAPI;
import team.frostfield.xtrafight.config.XtraFightAPIConfigs;
import team.frostfield.xtrafight.events.xtrafight_api.XtraFightAPIDispatchableEvents;
import team.frostfield.xtrafight.events.xtrafight_api.registry.CameraAnimationRegistryDispatchEvent;
import team.frostfield.xtrafight.main.XtraFightAPI;
import yesman.epicfight.api.client.event.EpicFightClientHooks;

public class XtraFightAPISubscribeEvents {
    @Mod.EventBusSubscriber(
            modid = XtraFightAPI.MOD_ID,
            value = Dist.CLIENT
    )
    public static class Client {
        private static long lastFrameNanos = 0L;
        @SubscribeEvent
        public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
            if (event.getHand() == InteractionHand.MAIN_HAND && event.getLevel().isClientSide()) {
                InteractionAPI.getInstance().interactWithEntity(event.getEntity(), event.getTarget());
                event.setCanceled(true);
            }
        }

     /*   @SubscribeEvent
        public static void onRenderFrame(RenderFrameEvent.Pre event) {
            InteractionAPI api = InteractionAPI.getInstance();
            if (!api.isInteracting()) {
                lastFrameNanos = 0L;
                return;
            }

            long now = System.nanoTime();
            if (lastFrameNanos == 0L) {
                lastFrameNanos = now;
                return;
            }

            float deltaSeconds = (float) ((now - lastFrameNanos) / 1_000_000_000.0);
            lastFrameNanos = now;

            api.updateInteractionLock(deltaSeconds);
        }

      */

        @SubscribeEvent
        public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
            InteractionAPI api = InteractionAPI.getInstance();
            if (!api.isInteracting()) return;

            Entity target = api.getCurrentTarget();
            Player player = event.getRenderer().getMainCamera().getEntity() instanceof Player p ? p : null;
            if (player == null || target == null || !target.isAlive()) return;

            float partialTick = (float) event.getPartialTick();
            float interpolatedYaw = Mth.rotLerp(partialTick, player.yRotO, player.getYRot());
            float interpolatedPitch = Mth.lerp(partialTick, player.xRotO, player.getXRot());

            event.setYaw(interpolatedYaw);
            event.setPitch(interpolatedPitch);
        }
        
        @SubscribeEvent
        public static void computeFOV(ViewportEvent.ComputeFov event) {
            if (CameraAnimatorAPI.getInstance().isPlaying()) {
                CameraAnimatorAPI api = CameraAnimatorAPI.getInstance();
                CameraAnimatorAPI.CamAnim current = api.getCurrentCamAnim();
                if (current != null && CameraAnimatorAPI.getInstance().hasFovTrack() && XtraFightAPIConfigs.Client.isCamFovAnimationEnabled()) {
                
                    event.setFOV(api.getCamAnimFovModifier());
                }
            }
        }

        @SubscribeEvent
        public static void onCameraBuild(FMLClientSetupEvent evt) {
            EpicFightClientHooks.Camera.BUILD_TRANSFORM_PRE.registerEvent(event -> {
                if (!CameraAnimatorAPI.getInstance().isPlaying()) return;

                // Apply Camera animation
                CameraAnimatorAPI.getInstance().applyToCamera(
                        event.getCamera(),
                        event.getPartialTick()
                );

                // Take full control of camera during animation
                event.setVanillaCameraSetupCanceled(true);

            });

            evt.enqueueWork(() -> XtraFightAPIDispatchableEvents.Registry.CAMERA_ANIMATIONS.postWithParams(new CameraAnimationRegistryDispatchEvent(), param -> param.onRegister(CameraAnimatorAPI.getInstance())));

        }
    }
}
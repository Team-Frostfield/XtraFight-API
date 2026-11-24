package team.frostfield.xtrafight.events.xtrafight_api.registry;

import team.frostfield.xtrafight.animation.client.CameraAnimatorAPI;
import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;
import team.frostfield.xtrafight.events.dispatch.marks.HybridEvent;
import team.frostfield.xtrafight.main.XtraFightAPI;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

@HybridEvent
public class CameraAnimationRegistryDispatchEvent extends DispatchEvent implements ParameterizedEvent {

    public void register(CameraAnimatorAPI.CamAnim... camAnims) {
        for (CameraAnimatorAPI.CamAnim camAnim : camAnims) {
            CameraAnimatorAPI.getInstance().registerAnimation(camAnim);
        }
    }

    public void registerFromList(List<CameraAnimatorAPI.CamAnim> camAnimations) {
        camAnimations.forEach(this::register);
    }

    public void registerFromClass(Class<?> cls) {
        // Check if the field is static and matches or extends CamAnim
        for (Field field : cls.getDeclaredFields())
            if (Modifier.isStatic(field.getModifiers()) && CameraAnimatorAPI.CamAnim.class.isAssignableFrom(field.getType())) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(null); // null because it's static

                    if (value instanceof CameraAnimatorAPI.CamAnim camAnim) {
                        this.register(camAnim);
                    }
                } catch (Exception e) {
                    XtraFightAPI.LOGGER.error("Failed to register animation from field [{}] in class [{}]: {}",
                            field.getName(), cls.getName(), e.getMessage());
                    e.printStackTrace();
                }
            }
    }

    @FunctionalInterface
    public interface CameraRegistryParams extends EventParameters {
        void onRegister(CameraAnimatorAPI api);
    }
}

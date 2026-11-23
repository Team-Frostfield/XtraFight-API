package team.frostfield.xtrafight.events.xtrafight_api.player;

import team.frostfield.xtrafight.events.dispatch.abstracts.DispatchEvent;
import team.frostfield.xtrafight.events.dispatch.dispatchers.extras.EventParameters;
import team.frostfield.xtrafight.events.dispatch.dispatchers.impls.ParameterizedEvent;
import team.frostfield.xtrafight.events.dispatch.marks.HybridEvent;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@HybridEvent
public class AddPlayerMotionsDispatchEvent extends DispatchEvent implements ParameterizedEvent {

    private final Animator animator;
    private final PlayerPatch<?> playerPatch;

    public AddPlayerMotionsDispatchEvent(Animator animator, PlayerPatch<?> playerPatch) {
        this.animator = animator;
        this.playerPatch = playerPatch;
    }

    @FunctionalInterface
    public interface AddPlayerMotionParams extends EventParameters {
        void onAddMotions(Animator animator, PlayerPatch<?> playerPatch);
    }

    public void addAnimation(LivingMotion motion, AnimationAccessor<? extends StaticAnimation> animation) {
        this.animator.addLivingAnimation(motion, animation);
    }

    public Animator getAnimator() {
        return this.animator;
    }

    public PlayerPatch<?> getPlayerPatch() {
        return this.playerPatch;
    }

}

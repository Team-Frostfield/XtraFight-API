package team.frostfield.xtrafight.events.xtrafight_api;

import team.frostfield.xtrafight.events.dispatch.dispatchers.parameterized.CancelableParameterizedEventDispatch;
import team.frostfield.xtrafight.events.dispatch.dispatchers.parameterized.ParameterizedEventDispatch;
import team.frostfield.xtrafight.events.xtrafight_api.animation.GrabAttackDispatchEvent;
import team.frostfield.xtrafight.events.xtrafight_api.entity.EntityInteractDispatchEvent;
import team.frostfield.xtrafight.events.xtrafight_api.entity.EntityInteractDispatchEvent.EntityInteractParams;
import team.frostfield.xtrafight.events.xtrafight_api.player.AddPlayerMotionsDispatchEvent;
import team.frostfield.xtrafight.events.xtrafight_api.player.AddPlayerMotionsDispatchEvent.AddPlayerMotionParams;
import team.frostfield.xtrafight.events.xtrafight_api.player.CounterAttackDispatchEvent;
import team.frostfield.xtrafight.events.xtrafight_api.player.CounterAttackDispatchEvent.CounterAttackParams;
import team.frostfield.xtrafight.events.xtrafight_api.player.HeavyAttackDispatchEvent;
import team.frostfield.xtrafight.events.xtrafight_api.player.HeavyAttackDispatchEvent.HeavyAttackParams;
import team.frostfield.xtrafight.events.xtrafight_api.registry.CameraAnimationRegistryDispatchEvent;
import team.frostfield.xtrafight.events.xtrafight_api.registry.CameraAnimationRegistryDispatchEvent.CameraRegistryParams;
import team.frostfield.xtrafight.events.xtrafight_api.skill.AddGuardMotionsDispatchEvent;
import team.frostfield.xtrafight.events.xtrafight_api.skill.AddGuardMotionsDispatchEvent.AddGuardMotionParams;

public class XtraFightAPIDispatchableEvents {

    public static class Registry {
        private Registry(){}
        // registers camera animations
        public static final ParameterizedEventDispatch<CameraAnimationRegistryDispatchEvent, CameraRegistryParams> CAMERA_ANIMATIONS = ParameterizedEventDispatch.createParameterized();
    }
    public XtraFightAPIDispatchableEvents() {}

    public static class Player {
        private Player(){}

        // Adds more default motions for the player, specifically in the tail of PlayerPatch#initAnimation();
        public static final ParameterizedEventDispatch<AddPlayerMotionsDispatchEvent, AddPlayerMotionParams> ADD_MOTIONS = ParameterizedEventDispatch.createParameterized();
        // fires when the player heavy attacks if a heavy combo is present
        public static final CancelableParameterizedEventDispatch<HeavyAttackDispatchEvent, HeavyAttackParams> HEAVY_ATTACK = CancelableParameterizedEventDispatch.createCancelable();
        // fires when the player counter-attacks if a counter is present
        public static final CancelableParameterizedEventDispatch<CounterAttackDispatchEvent, CounterAttackParams> COUNTER_ATTACK = CancelableParameterizedEventDispatch.createCancelable();
    }

    public static class Skill {
        private Skill() {}
        // adds guard motions and guard break motions for your weapon
        public static final ParameterizedEventDispatch<AddGuardMotionsDispatchEvent.Normal, AddGuardMotionParams> ADD_NORMAL_GUARD_MOTIONS = ParameterizedEventDispatch.createParameterized();
        // adds parrying motions for your weapon
        public static final ParameterizedEventDispatch<AddGuardMotionsDispatchEvent.Parry, AddGuardMotionParams> ADD_PARRY_GUARD_MOTIONS = ParameterizedEventDispatch.createParameterized();

    }

    public static class Entity {
        public static final ParameterizedEventDispatch<EntityInteractDispatchEvent, EntityInteractParams> ENTITY_INTERACT = ParameterizedEventDispatch.createParameterized();
    }

    public static class Animation {
        public static final CancelableParameterizedEventDispatch<GrabAttackDispatchEvent, GrabAttackDispatchEvent.GrabAttackParams> GRAB_ATTACK = CancelableParameterizedEventDispatch.createCancelable();
    }


}

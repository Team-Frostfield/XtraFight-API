package team.frostfield.xtrafight.mixins.epicfight.common;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.frostfield.xtrafight.events.xtrafight_api.XtraFightAPIDispatchableEvents;
import team.frostfield.xtrafight.events.xtrafight_api.player.AddPlayerMotionsDispatchEvent;
import team.frostfield.xtrafight.gameassets.XtraFightAPISkills;
import team.frostfield.xtrafight.skills.XtraFightSkillSlots;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.skill.CapabilitySkill;

@Mixin(value = PlayerPatch.class, remap = false)
public abstract class PlayerPatchMixin<T extends Player> extends LivingEntityPatch<T> {
    @Unique
    private final PlayerPatch<?> self = (PlayerPatch<?>) (Object) this;

    private PlayerPatchMixin() {
        super();
    }


    @Shadow
    public abstract CapabilitySkill getSkillCapability();

    @Inject(
            at = @At(value = "HEAD"),
            method = "onJoinWorld(Lnet/minecraft/world/entity/player/Player;Lnet/minecraftforge/event/entity/EntityJoinLevelEvent;)V",
            remap = false
    )
    private void xtrafight$onJoinWorld(T entity, EntityJoinLevelEvent event, CallbackInfo ci) {
        // add HeavyAttack and CounterAttack as a default skill
        CapabilitySkill skillCapability = this.getSkillCapability();
        skillCapability.getSkillContainerFor(XtraFightSkillSlots.HEAVY_ATTACK).setSkill(XtraFightAPISkills.HEAVY_ATTACK);
        skillCapability.getSkillContainerFor(XtraFightSkillSlots.COUNTER_ATTACK).setSkill(XtraFightAPISkills.COUNTER_ATTACK);
    }

    @Inject(
            at = @At("TAIL"),
            method = "initAnimator",
            remap = false
    )
    private void injectAddMotionsEH(Animator animator, CallbackInfo ci) {
        AddPlayerMotionsDispatchEvent addPlayerMotionsEH = new AddPlayerMotionsDispatchEvent(animator, this.self);
        XtraFightAPIDispatchableEvents.Player.ADD_MOTIONS.postWithParams(addPlayerMotionsEH, params -> params.onAddMotions(animator, this.self));
    }
}

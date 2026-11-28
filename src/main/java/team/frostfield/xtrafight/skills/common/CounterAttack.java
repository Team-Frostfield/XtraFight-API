package team.frostfield.xtrafight.skills.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import team.frostfield.xtrafight.animation.XtraFightAPIAnimationStates;
import team.frostfield.xtrafight.builders.epicfight.excap.deferred.DeferredCapabilityBuilder;
import team.frostfield.xtrafight.events.xtrafight_api.XtraFightAPIDispatchableEvents;
import team.frostfield.xtrafight.events.xtrafight_api.player.CounterAttackDispatchEvent;
import team.frostfield.xtrafight.gameassets.XtraFightAPISkillDataKeys;
import team.frostfield.xtrafight.network.XtraFightAPINetworkManager;
import team.frostfield.xtrafight.skills.XtraFightAPISkillCategories;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.network.EntityPairingPacketTypes;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.guard.ParryingSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.eventlistener.SkillConsumeEvent;

import java.util.List;

public class CounterAttack extends Skill {
    private float counterConsumption;

    public static SkillBuilder<CounterAttack> createCounterAttackBuilder() {
        return new SkillBuilder<CounterAttack>()
                .setCategory(XtraFightAPISkillCategories.COUNTER_ATTACK)
                .setResource(Resource.STAMINA);
    }

    public CounterAttack(SkillBuilder<CounterAttack> builder) {
        super(builder);
    }

    @Override
    public void setParams(CompoundTag params) {
        this.counterConsumption = params.getFloat("stamina_consumption");
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf tag) {
        PlayerPatch<?> executor = container.getExecutor();
        ServerPlayerPatch serverExecutor = container.getServerExecutor();

        CapabilityItem mainHand = executor.getHoldingItemCapability(InteractionHand.MAIN_HAND);

        CounterAttackDispatchEvent counterAttackEvent = new CounterAttackDispatchEvent(container);
        SkillConsumeEvent skillConsumeEvent = new SkillConsumeEvent(executor, this, this.resource, null);
        ParryingSkill parryingSkill = (ParryingSkill) EpicFightSkills.PARRYING;
        boolean canParry = parryingSkill.isHoldingWeaponAvailable(executor, mainHand, GuardSkill.BlockType.ADVANCED_GUARD);

        CounterType counterType = canParry ? CounterType.PARRY : CounterType.NORMAL;

        AnimationManager.AnimationAccessor<? extends StaticAnimation> animation = this.getCounterMotion(container, mainHand, counterType);
        if (animation == null) {
            return;
        }

        if (!XtraFightAPIDispatchableEvents.Player.COUNTER_ATTACK.postWithParamsAndCheckCanceled(counterAttackEvent, params -> params.onCounter(container))) {
            if (!skillConsumeEvent.isCanceled()) {
                skillConsumeEvent.getResourceType().consumer.consume(container, serverExecutor, this.counterConsumption);
            }
            container.getExecutor().playSound(EpicFightSounds.HYPERVITALITY.get(), 0.5F, 0, 0);
            XtraFightAPINetworkManager.sendPairingPacket(serverExecutor.getOriginal(), EntityPairingPacketTypes.FLASH_WHITE, 4, 15, 8, false);
            executor.playAnimationSynchronized(animation, 0.0F);
        }
    }

    protected AnimationManager.AnimationAccessor<? extends StaticAnimation> getCounterMotion(SkillContainer container, CapabilityItem itemCapability, CounterType counterType) {
        WeaponCategory category = itemCapability.getWeaponCategory();

        if (counterType == CounterType.PARRY) {
            List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> parryList = DeferredCapabilityBuilder.getParryingCounterAttacks(category);

            if (parryList != null && !parryList.isEmpty()) {
                SkillDataManager dataManager = container.getDataManager();
                int motionCounter = dataManager.getDataValue(XtraFightAPISkillDataKeys.MOTION_COUNTER.get());
                dataManager.setDataF(XtraFightAPISkillDataKeys.MOTION_COUNTER.get(), this::increment);

                return parryList.get(Math.abs(motionCounter) % parryList.size());
            }
        }

        return DeferredCapabilityBuilder.getNormalCounterAttack(category);
    }

    private int increment(int original) {
        return original + 1;
    }

    @Override
    public boolean isExecutableState(PlayerPatch<?> executor) {
        boolean baseExecutable = super.isExecutableState(executor);
        boolean hasStamina = executor.getStamina() >= this.counterConsumption;
        boolean canCounter = executor.getEntityState().getState(XtraFightAPIAnimationStates.CAN_COUNTER_ATTACK);

        return baseExecutable && hasStamina && canCounter;
    }


    public enum CounterType {
        NORMAL,
        PARRY
    }
}
package team.frostfield.xtrafight.skills.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import team.frostfield.xtrafight.animation.XtraFightAPIAnimationStates;
import team.frostfield.xtrafight.builders.epicfight.excap.deferred.DeferredCapabilityBuilder;
import team.frostfield.xtrafight.events.xtrafight_api.player.CounterAttackDispatchEvent;
import team.frostfield.xtrafight.gameassets.XtraFightAPISkillDataKeys;
import team.frostfield.xtrafight.network.XtraFightAPINetworkManager;
import team.frostfield.xtrafight.skills.XtraFightAPISkillCategories;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.event.EpicFightEventHooks;
import yesman.epicfight.api.event.types.player.SkillConsumeEvent;
import yesman.epicfight.network.EntityPairingPacketTypes;
import yesman.epicfight.registry.entries.EpicFightSkills;
import yesman.epicfight.registry.entries.EpicFightSounds;
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

import java.util.List;

public class CounterAttack extends Skill {
    private float counterConsumption;

    public static SkillBuilder<?> createCounterAttackBuilder() {
        return new SkillBuilder<>(CounterAttack::new)
                .setCategory(XtraFightAPISkillCategories.COUNTER_ATTACK)
                .setResource(Resource.STAMINA);
    }

    public CounterAttack(SkillBuilder<?> builder) {
        super(builder);
    }

    @Override
    public void loadDatapackParameters(CompoundTag params) {
        this.counterConsumption = params.getFloat("stamina_consumption");
    }

    @Override
    public void executeOnServer(SkillContainer container, CompoundTag tag) {
        PlayerPatch<?> executor = container.getExecutor();
        ServerPlayerPatch serverExecutor = container.getServerExecutor();

        CapabilityItem mainHand = executor.getHoldingItemCapability(InteractionHand.MAIN_HAND);

        CounterAttackDispatchEvent counterAttackEvent = new CounterAttackDispatchEvent(container);
        SkillConsumeEvent skillConsumeEvent = new SkillConsumeEvent(executor, this, this.resource, null);
        ParryingSkill parryingSkill = EpicFightSkills.PARRYING.get();
        boolean canParry = parryingSkill.isHoldingWeaponAvailable(executor, mainHand, GuardSkill.BlockType.ADVANCED_GUARD);

        CounterType counterType = canParry ? CounterType.PARRY : CounterType.NORMAL;

        AnimationManager.AnimationAccessor<? extends StaticAnimation> animation = this.getCounterMotion(container, mainHand, counterType);
        if (animation == null) {
            return;
        }

        if (!counterAttackEvent.isCanceledAfterPosting()) {
            if (!EpicFightEventHooks.Player.CONSUME_SKILL.postWithListener(skillConsumeEvent, executor.getEventListener()).isCanceled()) {
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
            // Fetch list via getParryingCounterAttacks getter
            List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> parryList = DeferredCapabilityBuilder.getParryingCounterAttacks(category);

            if (parryList != null && !parryList.isEmpty()) {
                SkillDataManager dataManager = container.getDataManager();
                int motionCounter = dataManager.getDataValue(XtraFightAPISkillDataKeys.MOTION_COUNTER);
                dataManager.setDataF(XtraFightAPISkillDataKeys.MOTION_COUNTER, this::increment);

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
        boolean canCounter = executor.getEntityState().getState(XtraFightAPIAnimationStates.CAN_COUNTER);

        return baseExecutable && hasStamina && canCounter;
    }


    public enum CounterType {
        NORMAL,
        PARRY
    }
}
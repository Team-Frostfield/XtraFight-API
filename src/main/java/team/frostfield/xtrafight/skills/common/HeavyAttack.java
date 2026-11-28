package team.frostfield.xtrafight.skills.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.frostfield.xtrafight.animation.XtraFightAPIAnimationStates;
import team.frostfield.xtrafight.builders.epicfight.excap.deferred.DeferredCapabilityBuilder;
import team.frostfield.xtrafight.events.xtrafight_api.XtraFightAPIDispatchableEvents;
import team.frostfield.xtrafight.events.xtrafight_api.player.HeavyAttackDispatchEvent;
import team.frostfield.xtrafight.exception.DeveloperException;
import team.frostfield.xtrafight.gameassets.XtraFightAPISkillDataKeys;
import team.frostfield.xtrafight.network.XtraFightAPINetworkManager;
import team.frostfield.xtrafight.skills.XtraFightAPISkillCategories;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationManager.AnimationAccessor;
import yesman.epicfight.api.animation.AnimationVariables;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.network.common.AnimatorControlPacket;
import yesman.epicfight.network.server.SPAnimatorControl;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKeys;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.eventlistener.ComboCounterHandleEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.SkillConsumeEvent;
import yesman.epicfight.world.gamerule.EpicFightGameRules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class HeavyAttack extends Skill {
    public double MIN_ATTACK_Y = 0.1;
    private final Map<WeaponCategory, Map<Style, BiFunction<CapabilityItem, PlayerPatch<?>, List<AnimationManager.AnimationAccessor<? extends AttackAnimation>>>>> heavyMotionMap = new HashMap<>();
    public static final AnimationVariables.IndependentAnimationVariableKey<Boolean> HEAVY_COMBO = AnimationVariables.independent((animator) -> false, false);

    protected float heavyAttackConsumption;
    protected float heavyDashAttackConsumption;
    protected float heavyAirAttackConsumption;

    protected int attackRecovery;

    // Scaling Parameters
    protected float damageMultiplier;
    protected float impactMultiplier;
    protected float armorNegation;

    // standard builder
    public static SkillBuilder<HeavyAttack> createHeavyAttackBuilder() {
        return new SkillBuilder<HeavyAttack>()
                .setCategory(XtraFightAPISkillCategories.HEAVY_ATTACK)
                .setActivateType(ActivateType.ONE_SHOT)
                .setResource(Resource.STAMINA);
    }

    public HeavyAttack(SkillBuilder<? extends HeavyAttack> builder) {
        super(builder);
    }

    @Override
    public void setParams(CompoundTag parameters) {
        // Attack consumptions
        this.heavyAttackConsumption = parameters.getFloat("heavy_attack_consumption");
        this.heavyDashAttackConsumption = parameters.getFloat("heavy_dash_consumption");
        this.heavyAirAttackConsumption = parameters.getFloat("heavy_airslash_consumption");
        // Recovery timing
        this.attackRecovery = parameters.contains("attack_recovery") ? parameters.getInt("attack_recovery") : 16;
        // Attributes multipliers
        this.damageMultiplier = parameters.contains("damage_multiplier") ? parameters.getFloat("damage_multiplier") : 0.5F;
        this.impactMultiplier = parameters.contains("impact_multiplier") ? parameters.getFloat("impact_multiplier") : 1.0F;
        this.armorNegation = parameters.contains("armor_negation") ? parameters.getFloat("armor_negation") : 15.0F;
    }

    @Override
    public void executeOnServer(SkillContainer skillContainer, FriendlyByteBuf args) {
        ServerPlayerPatch executor = skillContainer.getServerExecutor();
        SkillConsumeEvent skillConsumeEvent = new SkillConsumeEvent(executor, this, this.resource, null);
        HeavyAttackDispatchEvent heavyAttackEvent = new HeavyAttackDispatchEvent(skillContainer);

        if (!executor.getEntityState().getState(XtraFightAPIAnimationStates.CAN_HEAVY_ATTACK)) {
            return;
        }

        if (!skillConsumeEvent.isCanceled()) {
            skillConsumeEvent.getResourceType().consumer.consume(skillContainer, executor, skillConsumeEvent.getAmount());
        }

        if (!XtraFightAPIDispatchableEvents.Player.HEAVY_ATTACK.postWithParamsAndCheckCanceled(heavyAttackEvent, heavyAttackParams -> heavyAttackParams.onHeavyAttack(skillContainer))) {
            CapabilityItem cap = executor.getHoldingItemCapability(InteractionHand.MAIN_HAND);
            AnimationAccessor<? extends AttackAnimation> attackMotion;
            ServerPlayer player = executor.getOriginal();
            SkillDataManager dataManager = skillContainer.getDataManager();
            int comboCounter = dataManager.getDataValue(XtraFightAPISkillDataKeys.HEAVY_COUNTER.get());
            Vec3f blocksToDelta = this.deltaToBlocks(new Vec3f(player.getDeltaMovement()));
            boolean dashAttack = player.isSprinting();
            boolean airAttack = !player.isInWater() && !player.onGround() && blocksToDelta.y > this.MIN_ATTACK_Y && !player.getBlockStateOn().is(Block.byItem(Items.DIRT_PATH));

            List<AnimationAccessor<? extends AttackAnimation>> combo = this.applyMotionsForMaps(executor, cap);

            if (combo == null || combo.isEmpty()) return;

            int comboSize = combo.size();
            if (comboSize < 3) {
                // even for a non-developer environment, it'd still throw
                DeveloperException.throwExBoth("Heavy combo for " + cap.getWeaponCategory() + " needs at least 3 animations (Regular, Dash, Air).");
            }

            if (airAttack) {
                attackMotion = combo.get(comboSize - 1);
            } else if (dashAttack) {
                attackMotion = combo.get(comboSize - 2);
            } else {
                int maxRegularIdx = comboSize - 3;
                attackMotion = combo.get(Math.min(comboCounter, maxRegularIdx));
                comboCounter = (comboCounter + 1) % (maxRegularIdx + 1);
            }

            if (attackMotion != null && this.checkConsumption(executor, dashAttack, airAttack)) {
                this.applyWeaponScaling(attackMotion.get());

                setHeavyCounter(ComboCounterHandleEvent.Causal.ANOTHER_ACTION_ANIMATION, executor, skillContainer, attackMotion, comboCounter);
                // do Animator#playAnimationSynchronized() instead of Animator#play(), for safer animation play handling
                executor.playAnimationSynchronized(attackMotion, 0.0F);
                executor.getAnimator().getVariables().put(HEAVY_COMBO, attackMotion, true);

                boolean stiffAttack = EpicFightGameRules.STIFF_COMBO_ATTACKS.getRuleValue(player.level());
                SPAnimatorControl animatorControlPacket = getAnimatorControl(skillContainer, stiffAttack, attackMotion);
                XtraFightAPINetworkManager.sendToPlayer(player, animatorControlPacket);
            }
            executor.updateEntityState();
        }
    }


    private Vec3f deltaToBlocks(Vec3f delta) {
        float dx = delta.x * 2.12453F;
        float dz = delta.z * 2.12453F;
        float dy = 0;
        if (delta.y > 0) {
            dy = (float) (Math.sqrt(delta.y * 0.16F) + 0.02F);
        } else if (delta.y < 0) {
            dy = delta.y * 0.1F;
        }
        return new Vec3f(dx, dy, dz);
    }


    private void applyWeaponScaling(AttackAnimation animation) {
        for (AttackAnimation.Phase phase : animation.phases) {
            // if the multiply number is below 1, we add, so it doesn't decrease the damage nor the impact
            phase.addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, this.damageMultiplier < 1 ? ValueModifier.adder(this.damageMultiplier) : ValueModifier.multiplier(this.damageMultiplier));
            phase.addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, this.impactMultiplier < 1 ? ValueModifier.adder(this.impactMultiplier) : ValueModifier.multiplier(this.impactMultiplier));
            phase.addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(this.armorNegation));
        }
    }

    @Override
    public void updateContainer(SkillContainer container) {
        if (!container.getExecutor().isLogicalClient() && container.getExecutor().getTickSinceLastAction() > this.attackRecovery && container.getDataManager().getDataValue(SkillDataKeys.COMBO_COUNTER.get()) > 0) {
            setHeavyCounter(ComboCounterHandleEvent.Causal.TIME_EXPIRED, container.getServerExecutor(), container, null, 0);
        }
    }

    @Nullable
    private List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> applyMotionsForMaps(PlayerPatch<?> playerpatch, CapabilityItem itemCapability) {
        WeaponCategory currentCategory = itemCapability.getWeaponCategory();
        Style currentStyle = itemCapability.getStyle(playerpatch);

        List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> dynamicCombo = DeferredCapabilityBuilder.getHeavyCombo(currentCategory, currentStyle);

        if (dynamicCombo != null) return dynamicCombo;

        Map<Style, BiFunction<CapabilityItem, PlayerPatch<?>, List<AnimationManager.AnimationAccessor<? extends AttackAnimation>>>> styleMap = this.heavyMotionMap.get(currentCategory);
        if (styleMap != null) {
            BiFunction<CapabilityItem, PlayerPatch<?>, List<AnimationManager.AnimationAccessor<? extends AttackAnimation>>> comboProvider = styleMap.get(currentStyle);
            if (comboProvider != null) return comboProvider.apply(itemCapability, playerpatch);
        }
        return null;
    }

    private static void setHeavyCounter(ComboCounterHandleEvent.Causal reason, ServerPlayerPatch playerpatch, SkillContainer container, AnimationManager.AnimationAccessor<? extends StaticAnimation> causalAnimation, int value) {
        int prevValue = container.getDataManager().getDataValue(XtraFightAPISkillDataKeys.HEAVY_COUNTER.get());
        ComboCounterHandleEvent comboResetEvent = new ComboCounterHandleEvent(reason, playerpatch, causalAnimation, prevValue, value);
        container.getExecutor().getEventListener().triggerEvents(PlayerEventListener.EventType.COMBO_COUNTER_HANDLE_EVENT, comboResetEvent);
        container.getDataManager().setData(XtraFightAPISkillDataKeys.HEAVY_COUNTER.get(), comboResetEvent.getNextValue());
    }

    public boolean checkConsumption(PlayerPatch<?> playerPatch, boolean dash, boolean air) {
        float consumption = air ? this.heavyAirAttackConsumption : (dash ? this.heavyDashAttackConsumption : this.heavyAttackConsumption);
        return playerPatch.consumeForSkill(this, this.resource, consumption);
    }

    private static @NotNull SPAnimatorControl getAnimatorControl(SkillContainer skillContainer, boolean stiffAttack, AnimationManager.AnimationAccessor<? extends AttackAnimation> attackMotion) {
        return stiffAttack
                ? new SPAnimatorControl(AnimatorControlPacket.Action.PLAY, attackMotion, 0.0F, skillContainer.getExecutor())
                : new SPAnimatorControl(AnimatorControlPacket.Action.PLAY_CLIENT, attackMotion, 0.0F, skillContainer.getExecutor(), AnimatorControlPacket.Layer.COMPOSITE_LAYER, AnimatorControlPacket.Priority.HIGHEST);
    }

}
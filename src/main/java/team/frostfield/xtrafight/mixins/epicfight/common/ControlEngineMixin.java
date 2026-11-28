package team.frostfield.xtrafight.mixins.epicfight.common;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.frostfield.xtrafight.input.XtraFightAPIInputActions;
import team.frostfield.xtrafight.skills.XtraFightSkillSlots;
import yesman.epicfight.api.client.input.InputManager;
import yesman.epicfight.api.client.input.action.EpicFightInputAction;
import yesman.epicfight.api.client.input.action.InputAction;
import yesman.epicfight.api.client.input.action.MinecraftInputAction;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.SkillSlot;
import yesman.epicfight.world.entity.eventlistener.SkillCastEvent;

import static yesman.epicfight.client.input.InputUtils.checkInteractionKeyUsable;

@Mixin(value = ControlEngine.class, remap = false)
public abstract class ControlEngineMixin {

    @Shadow
    protected abstract boolean isCurrentHoldingAction(@NotNull InputAction other);

    @Shadow
    private static void consumeVanillaAttackKeyClicks() {}

    @Shadow
    private LocalPlayer player;

    @Shadow
    private boolean attackLightPressToggle;

    @Shadow
    protected abstract void reserveKey(SkillSlot slot, InputAction action);

    @Shadow
    private boolean weaponInnatePressToggle;

    @Shadow
    private int weaponInnatePressCounter;

    @Shadow
    private LocalPlayerPatch playerPatch;

    @Unique
    private void epicAPI$maybeHeavyAttack() {
        if (!this.playerPatch.isEpicFightMode() || isCurrentHoldingAction(XtraFightAPIInputActions.HEAVY_ATTACK)) {
            return;
        }
        final MinecraftInputAction vanillaAttack = MinecraftInputAction.ATTACK_DESTROY;
        final XtraFightAPIInputActions heavyAttack = XtraFightAPIInputActions.HEAVY_ATTACK;

        boolean shouldPlayAttackAnimation = this.playerPatch.canPlayAttackAnimation();
        if (vanillaAttack.keyMapping().getKey() == heavyAttack.keyMapping().getKey() && Minecraft.getInstance().hitResult != null && shouldPlayAttackAnimation) {
            consumeVanillaAttackKeyClicks();
        }

        if (shouldPlayAttackAnimation) {
            if (!InputManager.isBoundToSamePhysicalInput(heavyAttack, EpicFightInputAction.SWITCH_MODE)) {
                SkillCastEvent skillCastEvent = this.playerPatch.getSkill(XtraFightSkillSlots.HEAVY_ATTACK).sendCastRequest(this.playerPatch, ClientEngine.getInstance().controlEngine);

                if (skillCastEvent.isExecutable()) {
                    this.player.resetAttackStrengthTicker();
                    this.attackLightPressToggle = false;
                    ClientEngine.getInstance().controlEngine.releaseAllServedKeys();
                } else {
                    if (!this.player.isSpectator()) {
                        this.reserveKey(XtraFightSkillSlots.HEAVY_ATTACK, heavyAttack);
                    }
                }

                ClientEngine.getInstance().controlEngine.lockHotkeys();
                this.attackLightPressToggle = false;
                this.weaponInnatePressToggle = false;
                this.weaponInnatePressCounter = 0;
            } else {
                if (!this.weaponInnatePressToggle) {
                    this.weaponInnatePressToggle = true;
                }
            }
        }
    }

    @Unique
    private void epicAPI$maybeCounterAttack() {
        if (!this.playerPatch.isEpicFightMode() || this.isCurrentHoldingAction(XtraFightAPIInputActions.COUNTER_ATTACK)) {
            return;
        }
        final MinecraftInputAction vanillaAttack = MinecraftInputAction.ATTACK_DESTROY;
        final XtraFightAPIInputActions counterAttack = XtraFightAPIInputActions.COUNTER_ATTACK;

        boolean shouldPlayAttackAnimation = this.playerPatch.canPlayAttackAnimation();
        if (vanillaAttack.keyMapping().getKey() == counterAttack.keyMapping().getKey() && Minecraft.getInstance().hitResult != null && shouldPlayAttackAnimation) {
            consumeVanillaAttackKeyClicks();
        }

        if (shouldPlayAttackAnimation) {
            if (!InputManager.isBoundToSamePhysicalInput(counterAttack, EpicFightInputAction.SWITCH_MODE)) {
                SkillCastEvent skillCastEvent = this.playerPatch.getSkill(XtraFightSkillSlots.COUNTER_ATTACK).sendCastRequest(this.playerPatch, ClientEngine.getInstance().controlEngine);

                if (skillCastEvent.isExecutable()) {
                    this.player.resetAttackStrengthTicker();
                    this.attackLightPressToggle = false;
                    ClientEngine.getInstance().controlEngine.releaseAllServedKeys();
                } else {
                    if (!this.player.isSpectator()) {
                        this.reserveKey(XtraFightSkillSlots.COUNTER_ATTACK, counterAttack);
                    }
                }

                ClientEngine.getInstance().controlEngine.lockHotkeys();
                this.attackLightPressToggle = false;
                this.weaponInnatePressToggle = false;
                this.weaponInnatePressCounter = 0;
            } else {
                if (!this.weaponInnatePressToggle) {
                    this.weaponInnatePressToggle = true;
                }
            }
        }
    }

    @Unique
    private void xtraFightAPI$runKeyboardMouseEvent(InputAction action, Runnable task) {
        InputManager.triggerOnPress(action, () -> xtraFightAPI$runKME(action, task));
    }

    @Unique
    private static void xtraFightAPI$runKME(@NotNull InputAction action, @NotNull Runnable handler) {
        final KeyMapping keyMapping = action.keyMapping();

        final InputConstants.Key key = keyMapping.getKey();
        final boolean isMouse = InputConstants.Type.MOUSE == key.getType();

        final int mouseButton = isMouse ? key.getValue() : -1;

        if (checkInteractionKeyUsable(mouseButton, keyMapping)) {
            handler.run();
        }
    }

    @Inject(
            at = @At("HEAD"),
            remap = false,
            method = "handleEpicFightKeyMappings"
    )
    private void injectHeavyAttack(CallbackInfo ci) {
        if (this.playerPatch != null) {
            this.xtraFightAPI$runKeyboardMouseEvent(XtraFightAPIInputActions.HEAVY_ATTACK, this::epicAPI$maybeHeavyAttack);
            this.xtraFightAPI$runKeyboardMouseEvent(XtraFightAPIInputActions.COUNTER_ATTACK, this::epicAPI$maybeCounterAttack);
        }

    }
}

package team.frostfield.xtrafight.utils.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class RenderUtil {

    public static void renderFakeItem(Entity holdingEntity, Item item, EquipmentSlot slot, ItemDisplayContext context) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        LivingEntity livingHolder = holdingEntity instanceof LivingEntity living ? living : null;
        Level level = holdingEntity != null && holdingEntity.level() instanceof ClientLevel clientLevel
                ? clientLevel
                : mc.level;
        int seed = holdingEntity != null ? holdingEntity.getId() : 0;

        ItemStack itemStack;
        if (item != null) {
            itemStack = new ItemStack(item);
        } else if (livingHolder != null && slot != null) {
            itemStack = livingHolder.getItemBySlot(slot);
        } else {
            itemStack = ItemStack.EMPTY;
        }

        if (itemStack.isEmpty()) {
            return;
        }

        BakedModel model = itemRenderer.getItemModelShaper().getItemModel(itemStack);
        BakedModel overriddenModel = level instanceof ClientLevel clientLevel ? model.getOverrides().resolve(model, itemStack, clientLevel, livingHolder, seed) : model;

        int packedLight = LightTexture.FULL_BRIGHT;
        if (holdingEntity != null && level != null) {
            BlockPos pos = holdingEntity.blockPosition();
            int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
            int skyLight = level.getBrightness(LightLayer.SKY, pos);
            packedLight = LightTexture.pack(blockLight, skyLight);
        }

        boolean applyLeftHandTransform = slot == EquipmentSlot.OFFHAND;

        PoseStack poseStack = new PoseStack();
        itemRenderer.render(
                itemStack,
                context,
                applyLeftHandTransform,
                poseStack,
                bufferSource,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                overriddenModel
        );
        bufferSource.endBatch();
    }

}

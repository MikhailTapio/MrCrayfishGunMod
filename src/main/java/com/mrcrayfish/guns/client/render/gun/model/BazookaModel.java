package com.mrcrayfish.guns.client.render.gun.model;

import com.mrcrayfish.guns.util.GunItemData;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.GunModel;
import com.mrcrayfish.guns.client.handler.AimingHandler;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@Deprecated(since = "1.3.0", forRemoval = true)
public class BazookaModel extends SimpleModel
{
    private static final ResourceLocation RED_DOT_RETICLE = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/effect/red_dot_reticle.png");
    private static final ResourceLocation RED_DOT_RETICLE_GLOW = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/effect/red_dot_reticle_glow.png");
    private static final ResourceLocation VIGNETTE = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/effect/scope_vignette.png");

    public BazookaModel(Supplier<BakedModel> modelSupplier)
    {
        super(modelSupplier);
    }

    @Override
    public void render(float partialTicks, ItemDisplayContext display, ItemStack stack, ItemStack parent, @Nullable LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay)
    {
        Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.NONE, false, poseStack, buffer, light, overlay, GunModel.wrap(this.modelSupplier.get()));

        if(display.firstPerson() && entity != null && entity.equals(Minecraft.getInstance().player))
        {
            poseStack.pushPose();
            {
                Matrix4f matrix = poseStack.last().pose();
                PoseStack.Pose normal = poseStack.last();

                double size = 1.2 / 16.0;
                poseStack.translate(-size / 2 - 3.5 * 0.0625, -3.7 * 0.0625 - size / 2, -7 * 0.0625);

                VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucent(VIGNETTE));
                builder.addVertex(matrix, 0, 0, 0).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(1.0F, 1.0F).setOverlay(overlay).setLight(15728880).setNormal(normal, 0.0F, 1.0F, 0.0F);
                builder.addVertex(matrix, (float) size, 0, 0).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(0, 1.0F).setOverlay(overlay).setLight(15728880).setNormal(normal, 0.0F, 1.0F, 0.0F);
                builder.addVertex(matrix, (float) size, (float) size, 0).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(0, 0).setOverlay(overlay).setLight(15728880).setNormal(normal, 0.0F, 1.0F, 0.0F);
                builder.addVertex(matrix, 0, (float) size, 0).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(1.0F, 0).setOverlay(overlay).setLight(15728880).setNormal(normal, 0.0F, 1.0F, 0.0F);

                double invertProgress = (1.0 - AimingHandler.get().getNormalisedAdsProgress());
                poseStack.translate(-0.04 * invertProgress, 0.01 * invertProgress, 0);

                double scale = 6.0;
                poseStack.translate(size / 2, size / 2, 0);
                poseStack.translate(-(size / scale) / 2, -(size / scale) / 2, 0);
                poseStack.translate(0, 0, 0.0001);

                int reticleGlowColor = RenderUtil.getItemStackColor(stack, parent, 0);
                CompoundTag tag = GunItemData.getTag(stack);
                if(tag != null && tag.contains("ReticleColor", Tag.TAG_INT))
                {
                    reticleGlowColor = tag.getInt("ReticleColor");
                }

                float red = ((reticleGlowColor >> 16) & 0xFF) / 255F;
                float green = ((reticleGlowColor >> 8) & 0xFF) / 255F;
                float blue = ((reticleGlowColor >> 0) & 0xFF) / 255F;
                float alpha = (float) (1.0F * AimingHandler.get().getNormalisedAdsProgress());

                builder = buffer.getBuffer(RenderType.entityTranslucent(RED_DOT_RETICLE_GLOW));
                builder.addVertex(matrix, 0, (float) (size / scale), 0).setColor(red, green, blue, alpha).setUv(0.0F, 0.9375F).setOverlay(overlay).setLight(15728880).setNormal(normal, 0.0F, 1.0F, 0.0F);
                builder.addVertex(matrix, 0, 0, 0).setColor(red, green, blue, alpha).setUv(0.0F, 0.0F).setOverlay(overlay).setLight(15728880).setNormal(normal, 0.0F, 1.0F, 0.0F);
                builder.addVertex(matrix, (float) (size / scale), 0, 0).setColor(red, green, blue, alpha).setUv(0.9375F, 0.0F).setOverlay(overlay).setLight(15728880).setNormal(normal, 0.0F, 1.0F, 0.0F);
                builder.addVertex(matrix, (float) (size / scale), (float) (size / scale), 0).setColor(red, green, blue, alpha).setUv(0.9375F, 0.9375F).setOverlay(overlay).setLight(15728880).setNormal(normal, 0.0F, 1.0F, 0.0F);

                alpha = (float) (0.75F * AimingHandler.get().getNormalisedAdsProgress());

                builder = buffer.getBuffer(RenderType.entityTranslucent(RED_DOT_RETICLE));
                builder.addVertex(matrix, 0, (float) (size / scale), 0).setColor(1.0F, 1.0F, 1.0F, alpha).setUv(0.0F, 0.9375F).setOverlay(overlay).setLight(15728880).setNormal(normal, 0.0F, 1.0F, 0.0F);
                builder.addVertex(matrix, 0, 0, 0).setColor(1.0F, 1.0F, 1.0F, alpha).setUv(0.0F, 0.0F).setOverlay(overlay).setLight(15728880).setNormal(normal, 0.0F, 1.0F, 0.0F);
                builder.addVertex(matrix, (float) (size / scale), 0, 0).setColor(1.0F, 1.0F, 1.0F, alpha).setUv(0.9375F, 0.0F).setOverlay(overlay).setLight(15728880).setNormal(normal, 0.0F, 1.0F, 0.0F);
                builder.addVertex(matrix, (float) (size / scale), (float) (size / scale), 0).setColor(1.0F, 1.0F, 1.0F, alpha).setUv(0.9375F, 0.9375F).setOverlay(overlay).setLight(15728880).setNormal(normal, 0.0F, 1.0F, 0.0F);
            }
            poseStack.popPose();
        }
    }
}

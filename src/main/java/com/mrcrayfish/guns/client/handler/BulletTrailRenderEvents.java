package com.mrcrayfish.guns.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.guns.client.handler.BulletTrailRenderingHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;

/**
 * Author: MrCrayfish
 */
@net.neoforged.fml.common.EventBusSubscriber(modid = "cgm", value = net.neoforged.api.distmarker.Dist.CLIENT)
public class BulletTrailRenderEvents
{
    @net.neoforged.bus.api.SubscribeEvent
    public static void renderBullets(net.neoforged.neoforge.client.event.RenderLevelStageEvent event)
    {
        if(event.getStage() == net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage.AFTER_PARTICLES)
        {
            PoseStack stack = new PoseStack();
            stack.mulPose(event.getModelViewMatrix());
            BulletTrailRenderingHandler.get().render(stack, event.getPartialTick().getGameTimeDeltaPartialTick(false));
        }
    }
}

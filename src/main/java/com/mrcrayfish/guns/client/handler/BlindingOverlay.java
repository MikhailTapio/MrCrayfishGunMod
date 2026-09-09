package com.mrcrayfish.guns.client.handler;

import com.mojang.blaze3d.platform.Window;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.init.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

@net.neoforged.fml.common.EventBusSubscriber(modid = "cgm", value = net.neoforged.api.distmarker.Dist.CLIENT)
public class BlindingOverlay {
    @net.neoforged.bus.api.SubscribeEvent
    public static void render(net.neoforged.neoforge.client.event.RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null)
        {
            return;
        }

        MobEffectInstance effect = player.getEffect(ModEffects.BLINDED);
        if (effect != null)
        {
            // Render white screen-filling overlay at full alpha effect when duration is above threshold
            // When below threshold, fade to full transparency as duration approaches 0
            float percent = Math.min((effect.getDuration() / (float) Config.SERVER.alphaFadeThreshold.get()), 1);
            Window window = minecraft.getWindow();
            GuiGraphics graphics = event.getGuiGraphics();
            graphics.fill(0, 0, window.getGuiScaledWidth(), window.getGuiScaledHeight(), ((int) (percent * Config.SERVER.alphaOverlay.get() + 0.5) << 24) | 16777215);
        }
    }
}

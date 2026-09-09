package com.mrcrayfish.guns.client;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.particle.BloodParticle;
import com.mrcrayfish.guns.client.particle.BulletHoleParticle;
import com.mrcrayfish.guns.client.particle.TrailParticle;
import com.mrcrayfish.guns.init.ModParticleTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * Author: MrCrayfish
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ParticleFactoryRegistry
{
    @SubscribeEvent
    public static void onRegisterParticleFactory(RegisterParticleProvidersEvent event)
    {
        event.registerSpecial(ModParticleTypes.BULLET_HOLE.get(), (typeIn, worldIn, x, y, z, xSpeed, ySpeed, zSpeed) -> new BulletHoleParticle(worldIn, x, y, z, typeIn.getDirection(), typeIn.getPos()));
        event.registerSpriteSet(ModParticleTypes.BLOOD.get(), BloodParticle.Factory::new);
        event.registerSpriteSet(ModParticleTypes.TRAIL.get(), TrailParticle.Factory::new);
    }
}

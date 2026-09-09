package com.mrcrayfish.guns.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;
import java.util.Set;

import java.util.Collections;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class IncurableEffect extends MobEffect
{
    public IncurableEffect(MobEffectCategory typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effect)
    {
        cures.clear();
    }


}

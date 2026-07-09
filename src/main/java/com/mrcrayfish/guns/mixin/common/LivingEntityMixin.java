package com.mrcrayfish.guns.mixin.common;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.entity.ProjectileEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Author: MrCrayfish
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
    @Unique
    private DamageSource cgm$source;

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"))
    private void capture(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
    {
        this.cgm$source = source;
    }

    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"), require = 0)
    private void modifyApplyKnockbackArgs(LivingEntity instance, double strength, double x, double z)
    {
        double modified = strength;
        if(this.cgm$source.getDirectEntity() instanceof ProjectileEntity)
        {
            if(!Config.COMMON.gameplay.enableKnockback.get())
            {
                modified = 0;
            }
            else
            {
                double configStrength = Config.COMMON.gameplay.knockbackStrength.get();
                if(configStrength > 0)
                {
                    modified = configStrength;
                }
            }
        }
        instance.knockback(modified, x, z);
    }
}

package com.mrcrayfish.guns.mixin.common.simpleplanes;

import com.google.common.collect.ImmutableSet;
import com.mrcrayfish.guns.compat.SimplePlanesHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.upgrades.Upgrade;
import xyz.przemyk.simpleplanes.upgrades.UpgradeType;
import xyz.przemyk.simpleplanes.upgrades.shooter.ShooterUpgrade;

import java.util.Set;

@Mixin(value = ShooterUpgrade.class, remap = false)
public abstract class ShooterUpgradeMixin extends Upgrade {
    @Shadow @Final public ItemStackHandler itemStackHandler;
    @Unique
    private static final Set<Item> cgm$excludedItems = ImmutableSet.of(
            Items.FIREWORK_ROCKET, Items.FIRE_CHARGE,
            Items.ARROW, Items.TIPPED_ARROW,
            Items.SPECTRAL_ARROW, Items.ENDER_EYE
    );

    public ShooterUpgradeMixin(UpgradeType type, PlaneEntity planeEntity) {
        super(type, planeEntity);
    }

    @Inject(method = "use", at = @At("TAIL"), remap = false)
    private void inject$use(Player player, CallbackInfo ci) {
        final ItemStack stack = itemStackHandler.getStackInSlot(0);
        final Item item = stack.getItem();
        if (cgm$excludedItems.contains(item)) return;
        Vec3 motion = new Vec3(planeEntity.transformPos(new Vector3f(
                0,
                -0.25f,
                (float) (1 + planeEntity.getDeltaMovement().length())
        )));
        Vector3f pos = planeEntity.transformPos(new Vector3f(0.0f, 1.8f, 2.0f));
        SimplePlanesHelper.shooterBehaviour(
                item,
                itemStackHandler,
                player.level(),
                player, motion,
                pos.x() + planeEntity.getX(),
                pos.y() + planeEntity.getY(),
                pos.z() + planeEntity.getZ()
        );
    }
}

package com.mrcrayfish.guns.util;

import com.mrcrayfish.guns.init.ModDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;

/**
 * Snapshot adapter for the existing extensible gun definition/visual properties.
 * Ammo, colour and flags live in typed components; attachments and models use item codecs.
 * Call setTag after editing a snapshot. Reading never mutates an ItemStack.
 */
public final class GunItemData
{
    private GunItemData() {}

    public static CompoundTag getTag(ItemStack stack)
    {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if(stack.has(ModDataComponents.AMMO_COUNT)) tag.putInt("AmmoCount", stack.get(ModDataComponents.AMMO_COUNT));
        if(stack.has(ModDataComponents.IGNORE_AMMO)) tag.putBoolean("IgnoreAmmo", stack.get(ModDataComponents.IGNORE_AMMO));
        if(stack.has(ModDataComponents.COLOR)) tag.putInt("Color", stack.get(ModDataComponents.COLOR));
        if(stack.has(ModDataComponents.ADDITIONAL_DAMAGE)) tag.putFloat("AdditionalDamage", stack.get(ModDataComponents.ADDITIONAL_DAMAGE));
        if(stack.has(ModDataComponents.CUSTOM)) tag.putBoolean("Custom", stack.get(ModDataComponents.CUSTOM));
        if(stack.has(ModDataComponents.GUN)) tag.put("Gun", stack.get(ModDataComponents.GUN).copyTag());
        return tag;
    }

    public static CompoundTag getOrCreateTag(ItemStack stack) { return getTag(stack); }
    public static boolean hasTag(ItemStack stack) { return !getTag(stack).isEmpty(); }

    public static void setTag(ItemStack stack, CompoundTag snapshot)
    {
        CompoundTag tag = snapshot.copy();
        if(tag.contains("AmmoCount")) stack.set(ModDataComponents.AMMO_COUNT, Math.max(0, tag.getInt("AmmoCount")));
        else stack.remove(ModDataComponents.AMMO_COUNT);
        if(tag.contains("IgnoreAmmo")) stack.set(ModDataComponents.IGNORE_AMMO, tag.getBoolean("IgnoreAmmo"));
        else stack.remove(ModDataComponents.IGNORE_AMMO);
        if(tag.contains("Color")) stack.set(ModDataComponents.COLOR, tag.getInt("Color"));
        else stack.remove(ModDataComponents.COLOR);
        if(tag.contains("AdditionalDamage")) stack.set(ModDataComponents.ADDITIONAL_DAMAGE, tag.getFloat("AdditionalDamage"));
        else stack.remove(ModDataComponents.ADDITIONAL_DAMAGE);
        if(tag.contains("Custom")) stack.set(ModDataComponents.CUSTOM, tag.getBoolean("Custom"));
        else stack.remove(ModDataComponents.CUSTOM);
        if(tag.contains("Gun")) stack.set(ModDataComponents.GUN, CustomData.of(tag.getCompound("Gun")));
        else stack.remove(ModDataComponents.GUN);
        for(String key : new String[]{"AmmoCount", "IgnoreAmmo", "Color", "AdditionalDamage", "Custom", "Gun"}) tag.remove(key);
        if(tag.isEmpty()) stack.remove(DataComponents.CUSTOM_DATA);
        else stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static ItemStack getModel(ItemStack stack)
    {
        return stack.getOrDefault(ModDataComponents.MODEL, ItemContainerContents.EMPTY).copyOne();
    }
}

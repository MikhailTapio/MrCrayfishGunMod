package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.enchantment.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.LinkedHashMap;
import java.util.Map;

/** Author: MrCrayfish */
public class ModEnchantments
{
    public static final Map<ResourceKey<Enchantment>, GunEnchantment> DEFINITIONS = new LinkedHashMap<>();
    public static final ResourceKey<Enchantment> QUICK_HANDS = register("quick_hands", new QuickHandsEnchantment());
    public static final ResourceKey<Enchantment> TRIGGER_FINGER = register("trigger_finger", new TriggerFingerEnchantment());
    public static final ResourceKey<Enchantment> LIGHTWEIGHT = register("lightweight", new LightweightEnchantment());
    public static final ResourceKey<Enchantment> COLLATERAL = register("collateral", new CollateralEnchantment());
    public static final ResourceKey<Enchantment> OVER_CAPACITY = register("over_capacity", new OverCapacityEnchantment());
    public static final ResourceKey<Enchantment> RECLAIMED = register("reclaimed", new ReclaimedEnchantment());
    public static final ResourceKey<Enchantment> ACCELERATOR = register("accelerator", new AcceleratorEnchantment());
    public static final ResourceKey<Enchantment> PUNCTURING = register("puncturing", new PuncturingEnchantment());
    public static final ResourceKey<Enchantment> FIRE_STARTER = register("fire_starter", new FireStarterEnchantment());

    private static ResourceKey<Enchantment> register(String name, GunEnchantment definition)
    {
        ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, name));
        DEFINITIONS.put(key, definition);
        return key;
    }
}

package com.mrcrayfish.guns.enchantment;

import com.mrcrayfish.guns.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/** Author: MrCrayfish */
public class EnchantmentTypes
{
    public static final TagKey<Item> GUN = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "enchantable/gun"));
    public static final TagKey<Item> SEMI_AUTO_GUN = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "enchantable/semi_auto_gun"));
}

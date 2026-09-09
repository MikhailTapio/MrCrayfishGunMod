package com.mrcrayfish.guns.crafting;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

/** Author: MrCrayfish */
public class WorkbenchIngredient
{
    public static final Codec<WorkbenchIngredient> CODEC = SizedIngredient.FLAT_CODEC.xmap(WorkbenchIngredient::new, value -> value.ingredient);
    public static final StreamCodec<RegistryFriendlyByteBuf, WorkbenchIngredient> STREAM_CODEC =
        SizedIngredient.STREAM_CODEC.map(WorkbenchIngredient::new, value -> value.ingredient);
    private final SizedIngredient ingredient;

    private WorkbenchIngredient(SizedIngredient ingredient) { this.ingredient = ingredient; }
    public int getCount() { return this.ingredient.count(); }
    public Ingredient getIngredient() { return this.ingredient.ingredient(); }
    public ItemStack[] getItems() { return this.ingredient.ingredient().getItems(); }

    // Counts are summed across inventory slots by InventoryUtil, not checked per stack.
    public boolean test(ItemStack stack) { return this.ingredient.ingredient().test(stack); }

    public static WorkbenchIngredient of(ItemLike item, int count) { return new WorkbenchIngredient(SizedIngredient.of(item, count)); }
    public static WorkbenchIngredient of(ItemStack stack, int count) { return new WorkbenchIngredient(new SizedIngredient(Ingredient.of(stack), count)); }
    public static WorkbenchIngredient of(TagKey<Item> tag, int count) { return new WorkbenchIngredient(SizedIngredient.of(tag, count)); }
    public static WorkbenchIngredient of(ResourceLocation id, int count)
    {
        return of(BuiltInRegistries.ITEM.getOptional(id).orElseThrow(() -> new IllegalArgumentException("Unknown ingredient: " + id)), count);
    }
}

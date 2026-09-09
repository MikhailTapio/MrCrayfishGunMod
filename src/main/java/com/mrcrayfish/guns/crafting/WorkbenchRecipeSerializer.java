package com.mrcrayfish.guns.crafting;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

/** Author: MrCrayfish */
public class WorkbenchRecipeSerializer implements RecipeSerializer<WorkbenchRecipe>
{
    private static final MapCodec<WorkbenchRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(WorkbenchRecipe::getItem),
        WorkbenchIngredient.CODEC.listOf().fieldOf("materials").forGetter(recipe -> recipe.getMaterials())
    ).apply(instance, (item, materials) -> new WorkbenchRecipe(item, ImmutableList.copyOf(materials))));

    private static final StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> STREAM_CODEC = StreamCodec.composite(
        ItemStack.STREAM_CODEC, WorkbenchRecipe::getItem,
        WorkbenchIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.getMaterials(),
        (item, materials) -> new WorkbenchRecipe(item, ImmutableList.copyOf(materials)));

    @Override
    public MapCodec<WorkbenchRecipe> codec() { return CODEC; }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> streamCodec() { return STREAM_CODEC; }
}

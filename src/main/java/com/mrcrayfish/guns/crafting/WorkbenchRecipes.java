package com.mrcrayfish.guns.crafting;

import com.mrcrayfish.guns.init.ModRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

/**
 * Author: MrCrayfish
 */
public class WorkbenchRecipes
{
    public static boolean isEmpty(Level world)
    {
        return world.getRecipeManager().getRecipes().stream()
                .noneMatch(recipe -> recipe.value().getType() == ModRecipeTypes.WORKBENCH.get());
    }

    public static NonNullList<WorkbenchRecipe> getAll(Level world)
    {
        return world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.value().getType() == ModRecipeTypes.WORKBENCH.get())
                .map(recipe -> (WorkbenchRecipe) recipe.value())
                .collect(Collectors.toCollection(NonNullList::create));
    }

    @Nullable
    public static WorkbenchRecipe getRecipeById(Level world, ResourceLocation id)
    {
        return world.getRecipeManager().byKey(id)
                .filter(recipe -> recipe.value().getType() == ModRecipeTypes.WORKBENCH.get())
                .map(recipe -> (WorkbenchRecipe) recipe.value()).orElse(null);
    }
    public static ResourceLocation getId(Level world, WorkbenchRecipe recipe)
    {
        return world.getRecipeManager().getRecipes().stream().filter(holder -> holder.value() == recipe)
            .findFirst().orElseThrow().id();
    }
}

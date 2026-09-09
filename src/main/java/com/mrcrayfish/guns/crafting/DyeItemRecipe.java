package com.mrcrayfish.guns.crafting;

import com.mrcrayfish.guns.init.ModRecipeSerializers;
import com.mrcrayfish.guns.item.IColored;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class DyeItemRecipe extends CustomRecipe
{
    public DyeItemRecipe(CraftingBookCategory category)
    {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inventory, Level worldIn)
    {
        ItemStack item = ItemStack.EMPTY;
        List<ItemStack> dyes = new ArrayList<>();

        for(int i = 0; i < inventory.size(); ++i)
        {
            ItemStack stack = inventory.getItem(i);
            if(!stack.isEmpty())
            {
                if(stack.getItem() instanceof IColored)
                {
                    if(!item.isEmpty())
                    {
                        return false;
                    }
                    item = stack;
                }
                else
                {
                    if(!(stack.getItem() instanceof DyeItem))
                    {
                        return false;
                    }
                    dyes.add(stack);
                }
            }
        }

        return !item.isEmpty() && !dyes.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider access)
    {
        ItemStack item = ItemStack.EMPTY;
        List<DyeItem> dyes = new ArrayList<>();

        for(int i = 0; i < inventory.size(); ++i)
        {
            ItemStack stack = inventory.getItem(i);
            if(!stack.isEmpty())
            {
                if(stack.getItem() instanceof IColored)
                {
                    if(!item.isEmpty())
                    {
                        return ItemStack.EMPTY;
                    }
                    item = stack.copy();
                }
                else
                {
                    if(!(stack.getItem() instanceof DyeItem))
                    {
                        return ItemStack.EMPTY;
                    }
                    dyes.add((DyeItem) stack.getItem());
                }
            }
        }

        return !item.isEmpty() && !dyes.isEmpty() ? IColored.dye(item, dyes) : ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider access)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipeSerializers.DYE_ITEM.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput inventory)
    {
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);
        for(int i = 0; i < remainingItems.size(); ++i)
        {
            ItemStack stack = inventory.getItem(i);
            remainingItems.set(i, stack.getCraftingRemainingItem());
        }
        return remainingItems;
    }
}

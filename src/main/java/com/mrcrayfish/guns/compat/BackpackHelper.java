package com.mrcrayfish.guns.compat;

import com.mrcrayfish.backpacked.common.augment.Augments;
import com.mrcrayfish.backpacked.core.ModAugmentTypes;
import com.mrcrayfish.backpacked.inventory.BackpackInventory;
import com.mrcrayfish.backpacked.inventory.BackpackedInventoryAccess;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.AmmoContext;
import com.mrcrayfish.guns.common.Gun;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Author: MrCrayfish
 */
public class BackpackHelper
{
    public static AmmoContext findAmmo(Player player, ResourceLocation id)
    {
        List<BackpackInventory> inventories = ((BackpackedInventoryAccess) player)
                .backpacked$streamNonNullBackpackInventories()
                .toList();

        boolean needsAugment = Config.COMMON.compatibilities.backpackedNeedsQuiverLink2ReloadFromBackpack.get();

        for(BackpackInventory inventory : inventories)
        {
            ItemStack backpack = inventory.getBackpackStack();

            if(backpack.isEmpty())
                continue;

            if(needsAugment && !Augments.cached(backpack).has(ModAugmentTypes.QUIVERLINK.get()))
                continue;

            for(int i = 0; i < inventory.getContainerSize(); i++)
            {
                ItemStack stack = inventory.getItem(i);
                if(Gun.isAmmo(stack, id))
                {
                    return new AmmoContext(stack, inventory);
                }
            }
        }

        return AmmoContext.NONE;
    }
}

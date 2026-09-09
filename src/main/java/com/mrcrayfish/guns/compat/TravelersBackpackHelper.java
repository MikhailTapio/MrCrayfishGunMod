package com.mrcrayfish.guns.compat;

import com.mrcrayfish.guns.common.AmmoContext;
import com.mrcrayfish.guns.common.Gun;
import com.tiviacz.travelersbackpack.capability.AttachmentUtils;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;


public class TravelersBackpackHelper {
    public static AmmoContext findAmmo(Player player, ResourceLocation id)
    {
        final BackpackWrapper wrapper = AttachmentUtils.getBackpackWrapper(player);
        if (wrapper == null) return AmmoContext.NONE;
        final IItemHandlerModifiable handler = wrapper.getStorage();
        final int size = handler.getSlots();
        for (int i = 0; i < size; i++)
        {
            final ItemStack stack = handler.getStackInSlot(i);
            if(!Gun.isAmmo(stack, id)) continue;
            int finalI = i;
            return new AmmoContext(stack.copy(), s -> handler.setStackInSlot(finalI, s));
        }
        return AmmoContext.NONE;
    }
}

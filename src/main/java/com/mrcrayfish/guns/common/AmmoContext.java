package com.mrcrayfish.guns.common;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

/**
 * Author: MrCrayfish
 */
public record AmmoContext(ItemStack stack, Consumer<ItemStack> onConsume)
{
    private static final Consumer<ItemStack> NOOP = s -> {};
    public static final AmmoContext NONE = new AmmoContext(ItemStack.EMPTY, NOOP);

    public AmmoContext(ItemStack stack, Container container) {
        this(stack, s -> container.setChanged());
    }

    public AmmoContext(ItemStack stack) {
        this(stack, NOOP);
    }
}

package com.mrcrayfish.guns.compat;

import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.common.AmmoContext;
import com.mrcrayfish.guns.common.Gun;
import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2backpack.content.remote.player.EnderBackpackItem;
import dev.xkmc.l2backpack.content.remote.dimensional.DimensionalItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class L2BackpackHelper {
    private static final Map<Predicate<ItemStack>, SearchResult> search = ImmutableMap.of(
            s -> s.getItem() instanceof BaseBagItem, (p, s, i) -> handleBaseBagItem(s, i),
            s -> s.getItem() instanceof EnderBackpackItem, (p, s, i) -> handleEnderBackpackItem(p, i),
            s -> s.getItem() instanceof DimensionalItem, L2BackpackHelper::handleWorldChest
    );

    public static AmmoContext findAmmo(Player player, ResourceLocation id)
    {
        for (final var entry : search.entrySet()) {
            final AmmoContext searched = searchFor(player, id, entry.getKey(), entry.getValue());
            if (!searched.equals(AmmoContext.NONE)) return searched;
        }
        return AmmoContext.NONE;
    }

    private static AmmoContext searchFor(Player player, ResourceLocation id, Predicate<ItemStack> filter, SearchResult result) {
        final AtomicReference<AmmoContext> ctx = new AtomicReference<>(AmmoContext.NONE);
        if (GunMod.curiosLoaded) CuriosHelper.runOnCurios(player, h -> h.findFirstCurio(filter)
                .ifPresent(r -> ctx.set(result.get(player, r.stack(), id))));
        if (!ctx.get().equals(AmmoContext.NONE)) return ctx.get();
        final ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (filter.test(chest)) ctx.set(result.get(player, chest, id));
        return ctx.get();
    }

    interface SearchResult {
        AmmoContext get(Player player, ItemStack stack, ResourceLocation id);
    }

    private static AmmoContext handleBaseBagItem(ItemStack bagStack, ResourceLocation id) {
        if (!((bagStack.getItem()) instanceof BaseBagItem)) return AmmoContext.NONE;
        final List<ItemStack> inv = BaseBagItem.getItems(bagStack);
        if (inv.isEmpty()) return AmmoContext.NONE;
        for (int i = 0; i < inv.size(); i++) {
            final ItemStack stack = inv.get(i);
            if(!Gun.isAmmo(stack, id)) continue;
            return new AmmoContext(stack, s -> BaseBagItem.setItems(bagStack, inv));
        }
        return AmmoContext.NONE;
    }

    private static AmmoContext handleEnderBackpackItem(Player player, ResourceLocation id) {
        final PlayerEnderChestContainer ender = player.getEnderChestInventory();
        for (int i = 0; i < ender.getContainerSize(); i++) {
            ItemStack stack = ender.getItem(i);
            if(Gun.isAmmo(stack, id))
            {
                return new AmmoContext(stack, ender);
            }
        }
        return AmmoContext.NONE;
    }

    private static AmmoContext handleWorldChest(Player player, ItemStack chestStack, ResourceLocation id) {
        if (!((chestStack.getItem()) instanceof DimensionalItem w)) return AmmoContext.NONE;
        if (!(player.level() instanceof ServerLevel l)) return AmmoContext.NONE;
        final AtomicReference<AmmoContext> atomic = new AtomicReference<>(AmmoContext.NONE);
        w.getContainer(chestStack, l).ifPresent(c -> {
            final Container container = c.get();
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                if(Gun.isAmmo(stack, id))
                {
                    atomic.set(new AmmoContext(stack, container));
                }
            }
        });
        return atomic.get();
    }
}

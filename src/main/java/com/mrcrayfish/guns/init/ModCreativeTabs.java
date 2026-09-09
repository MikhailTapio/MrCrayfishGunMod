package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.mrcrayfish.guns.Reference.MOD_ID;

/**
 * Author: MrCrayfish
 */
public class ModCreativeTabs {
    public static void register(IEventBus bus) {
        DeferredRegister<CreativeModeTab> register = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
        CreativeModeTab.Builder builder = CreativeModeTab.builder();
        builder.title(Component.translatable("itemGroup." + MOD_ID));
        builder.icon(() -> {
            ItemStack stack = new ItemStack(ModItems.PISTOL.get());
            stack.set(ModDataComponents.IGNORE_AMMO, true);
            return stack;
        });
        builder.displayItems((flags, output) ->
        {
            ModItems.REGISTER.getEntries().forEach(registryObject ->
            {
                if (registryObject.get() instanceof GunItem item) {
                    ItemStack stack = new ItemStack(item);
                    stack.set(ModDataComponents.AMMO_COUNT, item.getGun().getGeneral().getMaxAmmo());
                    output.accept(stack);
                    return;
                }
                output.accept(registryObject.get());
            });
            ModEnchantments.DEFINITIONS.keySet().forEach(key -> {
                flags.holders().lookupOrThrow(Registries.ENCHANTMENT).get(key).ifPresent(enchantment ->
                    output.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, enchantment.value().getMaxLevel())), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY));
            });
        });
        register.register("creative_tab", builder::build);
        register.register(bus);
    }

}

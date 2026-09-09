package com.mrcrayfish.guns.client;

import com.mrcrayfish.guns.init.ModDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import java.util.List;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.common.CustomGun;
import com.mrcrayfish.guns.common.CustomGunLoader;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.network.message.S2CMessageUpdateGuns;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.Optional;

/**
 * Author: MrCrayfish
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class CustomGunManager
{
    private static Map<ResourceLocation, CustomGun> customGunMap;

    public static boolean updateCustomGuns(S2CMessageUpdateGuns message)
    {
        return updateCustomGuns(message.getCustomGuns());
    }

    private static boolean updateCustomGuns(Map<ResourceLocation, CustomGun> customGunMap)
    {
        CustomGunManager.customGunMap = customGunMap;
        return true;
    }

    public static void fill(CreativeModeTab.Output output)
    {
        if(customGunMap != null)
        {
            customGunMap.forEach((id, gun) ->
            {
                ItemStack stack = new ItemStack(ModItems.PISTOL.get());
                stack.set(DataComponents.CUSTOM_NAME, Component.translatable("item." + id.getNamespace() + "." + id.getPath() + ".name"));
                stack.set(ModDataComponents.MODEL, ItemContainerContents.fromItems(List.of(gun.getModel())));
                stack.set(ModDataComponents.GUN, CustomData.of(gun.getGun().serializeNBT()));
                stack.set(ModDataComponents.CUSTOM, true);
                stack.set(ModDataComponents.AMMO_COUNT, gun.getGun().getGeneral().getMaxAmmo());
                output.accept(stack);
            });
        }
    }

    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event)
    {
        customGunMap = null;
    }

}

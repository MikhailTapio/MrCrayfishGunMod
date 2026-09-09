package com.mrcrayfish.guns.client.render.gun;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ModelOverrides
{
    private static final Map<Item, IOverrideModel> MODEL_MAP = new HashMap<>();

    /**
     * Registers an override model to the given item.
     *
     * @param item  the item to override it's model
     * @param model a custom IOverrideModel implementation
     */
    public static void register(Item item, IOverrideModel model)
    {
        if(MODEL_MAP.putIfAbsent(item, model) == null)
        {
            // NeoForge rejects objects without event listeners; retain automatic registration
            // for animated models that actually subscribe to events.
            if(java.util.Arrays.stream(model.getClass().getMethods())
                    .anyMatch(method -> method.isAnnotationPresent(SubscribeEvent.class)))
            {
                NeoForge.EVENT_BUS.register(model);
            }
        }
    }

    /**
     * Checks if the given ItemStack has an overridden model
     *
     * @param stack the stack to check
     * @return True if overridden model exists
     */
    public static boolean hasModel(ItemStack stack)
    {
        return MODEL_MAP.containsKey(stack.getItem());
    }

    /**
     * Gets the overridden model for the given ItemStack.
     *
     * @param stack the stack of the overriden model
     * @return The overridden model for the stack or null if no overridden model exists.
     */
    @Nullable
    public static IOverrideModel getModel(ItemStack stack)
    {
        return MODEL_MAP.get(stack.getItem());
    }

    @SubscribeEvent
    public static void onClientPlayerTick(PlayerTickEvent.Pre event)
    {
        if(event.getEntity().level().isClientSide)
        {
            tick(event.getEntity());
        }
    }

    private static void tick(Player player)
    {
        ItemStack heldItem = player.getMainHandItem();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            IOverrideModel model = ModelOverrides.getModel(heldItem);
            if(model != null)
            {
                model.tick(player);
            }
        }
    }
}

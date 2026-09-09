package com.mrcrayfish.guns.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.ICancellableEvent;

/**
 * <p>Fired when a player shoots a gun.</p>
 *
 * @author Ocelot
 */
public class GunFireEvent extends PlayerEvent
{
    private final ItemStack stack;

    public GunFireEvent(Player player, ItemStack stack)
    {
        super(player);
        this.stack = stack;
    }

    /**
     * @return The stack the player was holding when firing the gun
     */
    public ItemStack getStack()
    {
        return stack;
    }

    /**
     * @return Whether or not this event was fired on the client side
     */
    public boolean isClient()
    {
        return this.getEntity().getCommandSenderWorld().isClientSide();
    }

    /**
     * <p>Fired when a player is about to shoot a bullet.</p>
     *
     * @author Ocelot
     */
    public static class Pre extends GunFireEvent implements ICancellableEvent
    {
        public Pre(Player player, ItemStack stack)
        {
            super(player, stack);
        }
    }

    /**
     * <p>Fired after a player has shot a bullet.</p>
     *
     * @author Ocelot
     */
    public static class Post extends GunFireEvent
    {
        public Post(Player player, ItemStack stack)
        {
            super(player, stack);
        }
    }
}

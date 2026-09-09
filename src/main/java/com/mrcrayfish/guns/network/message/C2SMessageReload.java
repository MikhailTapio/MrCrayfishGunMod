package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.event.GunReloadEvent;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;


/**
 * Author: MrCrayfish
 */
public class C2SMessageReload
{
    private boolean reload;

    public C2SMessageReload() {}

    public C2SMessageReload(boolean reload)
    {
        this.reload = reload;
    }

    public static void encode(C2SMessageReload message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBoolean(message.reload);
    }

    public static C2SMessageReload decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageReload(buffer.readBoolean());
    }

    public static void handle(C2SMessageReload message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = context.getPlayer().filter(ServerPlayer.class::isInstance).map(ServerPlayer.class::cast).orElse(null);
            if(player != null && !player.isSpectator())
            {
                ModSyncedDataKeys.RELOADING.setValue(player, message.reload); // This has to be set in order to verify the packet is sent if the event is cancelled
                if(!message.reload)
                    return;

                ItemStack gun = player.getMainHandItem();
                if(NeoForge.EVENT_BUS.post(new GunReloadEvent.Pre(player, gun)).isCanceled())
                {
                    ModSyncedDataKeys.RELOADING.setValue(player, false);
                    return;
                }
                NeoForge.EVENT_BUS.post(new GunReloadEvent.Post(player, gun));
            }
        });
        context.setHandled(true);
    }
}

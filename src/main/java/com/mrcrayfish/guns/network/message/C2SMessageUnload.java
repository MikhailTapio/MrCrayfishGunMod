package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.common.network.ServerPlayHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;


/**
 * Author: MrCrayfish
 */
public class C2SMessageUnload
{
    public static void encode(C2SMessageUnload message, RegistryFriendlyByteBuf buffer) {}

    public static C2SMessageUnload decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageUnload();
    }

    public static void handle(C2SMessageUnload message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = context.getPlayer().filter(ServerPlayer.class::isInstance).map(ServerPlayer.class::cast).orElse(null);
            if(player != null && !player.isSpectator())
            {
                ServerPlayHandler.handleUnload(player);
            }
        });
        context.setHandled(true);
    }
}

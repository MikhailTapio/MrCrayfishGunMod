package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.common.network.ServerPlayHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

/**
 * Author: MrCrayfish
 */
public class C2SMessageAttachments
{
    public C2SMessageAttachments() {}

    public static void encode(C2SMessageAttachments message, RegistryFriendlyByteBuf buffer) {}

    public static C2SMessageAttachments decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageAttachments();
    }

    public static void handle(C2SMessageAttachments message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = context.getPlayer().filter(ServerPlayer.class::isInstance).map(ServerPlayer.class::cast).orElse(null);
            if(player != null)
            {
                ServerPlayHandler.handleAttachments(player);
            }
        });
        context.setHandled(true);
    }
}

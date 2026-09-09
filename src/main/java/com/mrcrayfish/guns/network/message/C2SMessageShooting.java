package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;


/**
 * Author: MrCrayfish
 */
public class C2SMessageShooting
{
    private boolean shooting;

    public C2SMessageShooting() {}

    public C2SMessageShooting(boolean shooting)
    {
        this.shooting = shooting;
    }

    public static void encode(C2SMessageShooting message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBoolean(message.shooting);
    }

    public static C2SMessageShooting decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageShooting(buffer.readBoolean());
    }

    public static void handle(C2SMessageShooting message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = context.getPlayer().filter(ServerPlayer.class::isInstance).map(ServerPlayer.class::cast).orElse(null);
            if(player != null)
            {
                ModSyncedDataKeys.SHOOTING.setValue(player, message.shooting);
            }
        });
        context.setHandled(true);
    }
}

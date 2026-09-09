package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;

/**
 * Author: MrCrayfish
 */
public class S2CMessageRemoveProjectile
{
    private int entityId;

    public S2CMessageRemoveProjectile() {}

    public S2CMessageRemoveProjectile(int entityId)
    {
        this.entityId = entityId;
    }

    public static void encode(S2CMessageRemoveProjectile message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeInt(message.entityId);
    }

    public static S2CMessageRemoveProjectile decode(RegistryFriendlyByteBuf buffer)
    {
        return new S2CMessageRemoveProjectile(buffer.readInt());
    }

    public static void handle(S2CMessageRemoveProjectile message, MessageContext context)
    {
        context.execute(() -> ClientPlayHandler.handleRemoveProjectile(message));
        context.setHandled(true);
    }


    public int getEntityId()
    {
        return this.entityId;
    }
}

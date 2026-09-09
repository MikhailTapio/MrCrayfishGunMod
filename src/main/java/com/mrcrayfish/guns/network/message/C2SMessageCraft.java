package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.common.network.ServerPlayHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Author: MrCrayfish
 */
public class C2SMessageCraft
{
    private ResourceLocation id;
    private BlockPos pos;

    public C2SMessageCraft() {}

    public C2SMessageCraft(ResourceLocation id, BlockPos pos)
    {
        this.id = id;
        this.pos = pos;
    }

    public static void encode(C2SMessageCraft message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeResourceLocation(message.id);
        buffer.writeBlockPos(message.pos);
    }

    public static C2SMessageCraft decode(RegistryFriendlyByteBuf buffer)
    {
        return new C2SMessageCraft(buffer.readResourceLocation(), buffer.readBlockPos());
    }

    public static void handle(C2SMessageCraft message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = context.getPlayer().filter(ServerPlayer.class::isInstance).map(ServerPlayer.class::cast).orElse(null);
            if(player != null)
            {
                ServerPlayHandler.handleCraft(player, message.id, message.pos);
            }
        });
        context.setHandled(true);
    }
}

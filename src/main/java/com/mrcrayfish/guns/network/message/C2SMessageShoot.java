package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.common.network.ServerPlayHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;


/**
 * Author: MrCrayfish
 */
public class C2SMessageShoot
{
    private float rotationYaw;
    private float rotationPitch;

    public C2SMessageShoot() {}

    public C2SMessageShoot(Player player)
    {
        this.rotationYaw = player.getYRot();
        this.rotationPitch = player.getXRot();
    }

    public C2SMessageShoot(float rotationYaw, float rotationPitch)
    {
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
    }

    public static void encode(C2SMessageShoot message, RegistryFriendlyByteBuf buffer)
    {
        buffer.writeFloat(message.rotationYaw);
        buffer.writeFloat(message.rotationPitch);
    }

    public static C2SMessageShoot decode(RegistryFriendlyByteBuf buffer)
    {
        float rotationYaw = buffer.readFloat();
        float rotationPitch = buffer.readFloat();
        return new C2SMessageShoot(rotationYaw, rotationPitch);
    }

    public static void handle(C2SMessageShoot message, MessageContext context)
    {
        context.execute(() ->
        {
            ServerPlayer player = context.getPlayer().filter(ServerPlayer.class::isInstance).map(ServerPlayer.class::cast).orElse(null);
            if(player != null)
            {
                ServerPlayHandler.handleShoot(message, player);
            }
        });
        context.setHandled(true);
    }

    public float getRotationYaw()
    {
        return this.rotationYaw;
    }

    public float getRotationPitch()
    {
        return this.rotationPitch;
    }
}

package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2SMessageAim
{
	private boolean aiming;

	public C2SMessageAim() {}

	public C2SMessageAim(boolean aiming)
	{
		this.aiming = aiming;
	}

	public static void encode(C2SMessageAim message, RegistryFriendlyByteBuf buffer)
	{
		buffer.writeBoolean(message.aiming);
	}

	public static C2SMessageAim decode(RegistryFriendlyByteBuf buffer)
	{
		return new C2SMessageAim(buffer.readBoolean());
	}

	public static void handle(C2SMessageAim message, MessageContext context)
	{
		context.execute(() ->
		{
			ServerPlayer player = context.getPlayer().filter(ServerPlayer.class::isInstance).map(ServerPlayer.class::cast).orElse(null);
			if(player != null && !player.isSpectator())
			{
				ModSyncedDataKeys.AIMING.setValue(player, message.aiming);
			}
		});
		context.setHandled(true);
	}
}

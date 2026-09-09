package com.mrcrayfish.guns.network;

import com.mrcrayfish.framework.api.FrameworkAPI;
import com.mrcrayfish.framework.api.network.FrameworkNetwork;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.network.message.*;
import net.minecraft.resources.ResourceLocation;

public class PacketHandler
{
    private static FrameworkNetwork playChannel;

    public static void init()
    {
        playChannel = FrameworkAPI.createNetworkBuilder(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "play"), 1)
                .registerPlayMessage("aim", C2SMessageAim.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, C2SMessageAim message) -> C2SMessageAim.encode(message, buffer), C2SMessageAim::decode), C2SMessageAim::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("reload", C2SMessageReload.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, C2SMessageReload message) -> C2SMessageReload.encode(message, buffer), C2SMessageReload::decode), C2SMessageReload::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("shoot", C2SMessageShoot.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, C2SMessageShoot message) -> C2SMessageShoot.encode(message, buffer), C2SMessageShoot::decode), C2SMessageShoot::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("unload", C2SMessageUnload.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, C2SMessageUnload message) -> C2SMessageUnload.encode(message, buffer), C2SMessageUnload::decode), C2SMessageUnload::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("stun_grenade", S2CMessageStunGrenade.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, S2CMessageStunGrenade message) -> S2CMessageStunGrenade.encode(message, buffer), S2CMessageStunGrenade::decode), S2CMessageStunGrenade::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("craft", C2SMessageCraft.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, C2SMessageCraft message) -> C2SMessageCraft.encode(message, buffer), C2SMessageCraft::decode), C2SMessageCraft::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("bullet_trail", S2CMessageBulletTrail.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, S2CMessageBulletTrail message) -> S2CMessageBulletTrail.encode(message, buffer), S2CMessageBulletTrail::decode), S2CMessageBulletTrail::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("attachments", C2SMessageAttachments.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, C2SMessageAttachments message) -> C2SMessageAttachments.encode(message, buffer), C2SMessageAttachments::decode), C2SMessageAttachments::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("update_guns", S2CMessageUpdateGuns.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, S2CMessageUpdateGuns message) -> S2CMessageUpdateGuns.encode(message, buffer), S2CMessageUpdateGuns::decode), S2CMessageUpdateGuns::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("blood", S2CMessageBlood.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, S2CMessageBlood message) -> S2CMessageBlood.encode(message, buffer), S2CMessageBlood::decode), S2CMessageBlood::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("shooting", C2SMessageShooting.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, C2SMessageShooting message) -> C2SMessageShooting.encode(message, buffer), C2SMessageShooting::decode), C2SMessageShooting::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("gun_sound", S2CMessageGunSound.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, S2CMessageGunSound message) -> S2CMessageGunSound.encode(message, buffer), S2CMessageGunSound::decode), S2CMessageGunSound::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("projectile_hit_block", S2CMessageProjectileHitBlock.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, S2CMessageProjectileHitBlock message) -> S2CMessageProjectileHitBlock.encode(message, buffer), S2CMessageProjectileHitBlock::decode), S2CMessageProjectileHitBlock::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("projectile_hit_entity", S2CMessageProjectileHitEntity.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, S2CMessageProjectileHitEntity message) -> S2CMessageProjectileHitEntity.encode(message, buffer), S2CMessageProjectileHitEntity::decode), S2CMessageProjectileHitEntity::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("remove_projectile", S2CMessageRemoveProjectile.class, StreamCodec.of((RegistryFriendlyByteBuf buffer, S2CMessageRemoveProjectile message) -> S2CMessageRemoveProjectile.encode(message, buffer), S2CMessageRemoveProjectile::decode), S2CMessageRemoveProjectile::handle, PacketFlow.CLIENTBOUND)
                .build();
    }

    /**
     * Gets the play network channel for MrCrayfish's Gun Mod
     */
    public static FrameworkNetwork getPlayChannel()
    {
        return playChannel;
    }
}

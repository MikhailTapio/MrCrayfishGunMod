package com.mrcrayfish.guns.init;

import com.mojang.serialization.Codec;
import com.mrcrayfish.guns.Reference;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Persistent, synchronized per-stack gun state. Component values are never mutated in place. */
public final class ModDataComponents
{
    public static final DeferredRegister.DataComponents REGISTER = DeferredRegister.createDataComponents(Reference.MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> AMMO_COUNT = REGISTER.registerComponentType("ammo_count", b -> b.persistent(Codec.intRange(0, Integer.MAX_VALUE)).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> IGNORE_AMMO = REGISTER.registerComponentType("ignore_ammo", b -> b.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COLOR = REGISTER.registerComponentType("color", b -> b.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> ADDITIONAL_DAMAGE = REGISTER.registerComponentType("additional_damage", b -> b.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CUSTOM = REGISTER.registerComponentType("custom", b -> b.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    // The extensible gun definition keeps its existing schema, wrapped in immutable CustomData.
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> GUN = REGISTER.registerComponentType("gun", b -> b.persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> ATTACHMENTS = REGISTER.registerComponentType("attachments", b -> b.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> MODEL = REGISTER.registerComponentType("model", b -> b.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC));

    private ModDataComponents() {}
}

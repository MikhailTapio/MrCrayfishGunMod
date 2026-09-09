package com.mrcrayfish.guns.common;

import com.google.gson.JsonDeserializer;
import com.mrcrayfish.guns.client.util.Easings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.HolderLookup;
import com.mojang.serialization.JsonOps;

/**
 * Author: MrCrayfish
 */
public class JsonDeserializers
{
    public static JsonDeserializer<ItemStack> itemStack(HolderLookup.Provider registries)
    {
        return (json, typeOfT, context) -> ItemStack.CODEC.parse(registries.createSerializationContext(JsonOps.INSTANCE), json).getOrThrow();
    }
    public static final JsonDeserializer<ResourceLocation> RESOURCE_LOCATION = (json, typeOfT, context) -> ResourceLocation.parse(json.getAsString());
    public static final JsonDeserializer<GripType> GRIP_TYPE = (json, typeOfT, context) -> GripType.getType(ResourceLocation.tryParse(json.getAsString()));
    public static final JsonDeserializer<Easings> EASING = (json, typeOfT, context) -> Easings.byName(json.getAsString());
}

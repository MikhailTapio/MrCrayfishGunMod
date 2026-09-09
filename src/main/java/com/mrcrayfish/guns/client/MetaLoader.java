package com.mrcrayfish.guns.client;

import com.mrcrayfish.framework.api.serialize.DataObject;
import com.mrcrayfish.framework.client.resources.IDataLoader;
import com.mrcrayfish.framework.client.resources.IResourceSupplier;
import com.mrcrayfish.guns.item.IMeta;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public final class MetaLoader implements IDataLoader<MetaLoader.ItemResource>
{
    private static MetaLoader instance;

    public static MetaLoader getInstance()
    {
        if(instance == null)
        {
            instance = new MetaLoader();
        }
        return instance;
    }

    private final it.unimi.dsi.fastutil.objects.Reference2ObjectMap<Item, DataObject> itemToData = Util.make(new Reference2ObjectOpenHashMap<>(), map -> map.defaultReturnValue(DataObject.EMPTY));

    private MetaLoader() {}

    public DataObject getData(Item item)
    {
        return this.itemToData.get(item);
    }

    @Override
    public List<ItemResource> getResourceSuppliers()
    {
        List<ItemResource> resources = new ArrayList<>();
        BuiltInRegistries.ITEM.stream().filter(item -> item instanceof IMeta).forEach(item ->
        {
            ResourceLocation key = item.builtInRegistryHolder().key().location();
            ResourceLocation location = ResourceLocation.fromNamespaceAndPath(key.getNamespace(), "models/item/" + key.getPath() + ".cgmmeta");
            resources.add(new ItemResource(item, location));
        });
        return resources;
    }

    @Override
    public void process(List<Pair<ItemResource, DataObject>> list)
    {
        this.itemToData.clear();
        list.forEach(pair ->
        {
            DataObject object = pair.getRight();
            if(!object.isEmpty())
            {
                ItemResource resource = pair.getLeft();
                this.itemToData.put(resource.item(), object);
            }
        });
    }

    @Override
    public boolean ignoreMissing()
    {
        return true;
    }

    public record ItemResource(Item item, ResourceLocation location) implements IResourceSupplier
    {
        @Override
        public ResourceLocation getLocation()
        {
            return this.location;
        }
    }
}

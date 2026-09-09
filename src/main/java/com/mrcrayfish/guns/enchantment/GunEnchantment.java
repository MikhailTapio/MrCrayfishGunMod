package com.mrcrayfish.guns.enchantment;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

/**
 * Data-generation definition for the data-driven 1.21 enchantment registry.
 * Runtime effects remain in GunEnchantmentHelper and ProjectileEntity.
 * Author: MrCrayfish
 */
public abstract class GunEnchantment
{
    private final Rarity rarity;
    private final TagKey<Item> supportedItems;
    private final EquipmentSlot[] slots;
    private final Type type;

    protected GunEnchantment(Rarity rarity, TagKey<Item> supportedItems, EquipmentSlot[] slots, Type type)
    {
        this.rarity = rarity;
        this.supportedItems = supportedItems;
        this.slots = slots;
        this.type = type;
    }

    public int getMaxLevel() { return 1; }
    public int getMinCost(int level) { return 1 + level * 10; }

    public abstract int getMaxCost(int level);

    public JsonObject toJson(String id)
    {
        JsonObject json = new JsonObject();
        JsonObject description = new JsonObject();
        description.addProperty("translate", "enchantment.cgm." + id);
        json.add("description", description);
        json.addProperty("supported_items", "#" + this.supportedItems.location());
        json.addProperty("weight", this.rarity == Rarity.RARE ? 2 : 1);
        json.addProperty("max_level", this.getMaxLevel());
        json.add("min_cost", cost(this.getMinCost(1), this.getMinCost(2)));
        json.add("max_cost", cost(this.getMaxCost(1), this.getMaxCost(2)));
        json.addProperty("anvil_cost", this.rarity == Rarity.RARE ? 4 : 8);
        JsonArray slots = new JsonArray();
        for(EquipmentSlot slot : this.slots) slots.add(slot.getName());
        json.add("slots", slots);
        json.addProperty("exclusive_set", "#cgm:exclusive_set/" + this.type.name().toLowerCase(java.util.Locale.ROOT));
        json.add("effects", new JsonObject());
        return json;
    }

    private static JsonObject cost(int first, int second)
    {
        JsonObject json = new JsonObject();
        json.addProperty("base", first);
        json.addProperty("per_level_above_first", second - first);
        return json;
    }

    public enum Rarity { RARE, VERY_RARE }
    public enum Type { WEAPON, AMMO, PROJECTILE }
}

package com.mrcrayfish.guns.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mrcrayfish.guns.init.ModRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.CraftingHelper;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.minecraft.core.registries.BuiltInRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Ocelot
 */
public class WorkbenchRecipeBuilder
{
    @Nullable
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final List<WorkbenchIngredient> ingredients;
    private final Advancement.Builder advancementBuilder;
    private boolean hasCriteria;
    private final List<ICondition> conditions = new ArrayList<>();

    private WorkbenchRecipeBuilder(@Nullable RecipeCategory category, ItemLike item, int count)
    {
        this.category = category;
        this.result = item.asItem();
        this.count = count;
        this.ingredients = new ArrayList<>();
        this.advancementBuilder = Advancement.Builder.advancement();
    }

    public static WorkbenchRecipeBuilder crafting(ItemLike item)
    {
        return new WorkbenchRecipeBuilder(null, item, 1);
    }

    public static WorkbenchRecipeBuilder crafting(ItemLike item, int count)
    {
        return new WorkbenchRecipeBuilder(null, item, count);
    }

    public static WorkbenchRecipeBuilder crafting(@Nullable RecipeCategory category, ItemLike item)
    {
        return new WorkbenchRecipeBuilder(category, item, 1);
    }

    public static WorkbenchRecipeBuilder crafting(@Nullable RecipeCategory category, ItemLike item, int count)
    {
        return new WorkbenchRecipeBuilder(category, item, count);
    }

    public WorkbenchRecipeBuilder addIngredient(ItemLike item, int count)
    {
        this.ingredients.add(WorkbenchIngredient.of(item, count));
        return this;
    }

    public WorkbenchRecipeBuilder addIngredient(WorkbenchIngredient ingredient)
    {
        this.ingredients.add(ingredient);
        return this;
    }

    public WorkbenchRecipeBuilder addCriterion(String name, Criterion<?> criterionIn)
    {
        this.advancementBuilder.addCriterion(name, criterionIn);
        this.hasCriteria = true;
        return this;
    }

    public WorkbenchRecipeBuilder addCondition(ICondition condition)
    {
        this.conditions.add(condition);
        return this;
    }

    public void build(RecipeOutput consumer)
    {
        ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(this.result);
        this.build(consumer, resourcelocation);
    }

    public void build(RecipeOutput consumer, ResourceLocation id)
    {
        this.validate(id);
        this.advancementBuilder.parent(ResourceLocation.withDefaultNamespace("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
        ResourceLocation advancementId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "recipes/" + (this.category != null ? this.category.getFolderName() + "/" : "") + id.getPath());
        consumer.accept(id, new WorkbenchRecipe(new ItemStack(this.result, this.count), ImmutableList.copyOf(this.ingredients)),
            this.advancementBuilder.build(advancementId), this.conditions.toArray(ICondition[]::new));
    }

    /**
     * Makes sure that this recipe is valid and obtainable.
     */
    private void validate(ResourceLocation id)
    {
        if(!this.hasCriteria)
        {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

}

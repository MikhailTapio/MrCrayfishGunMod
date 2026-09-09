package com.mrcrayfish.guns.datagen;

import com.mrcrayfish.guns.init.ModEnchantments;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import java.util.concurrent.CompletableFuture;

/** Generates the original gun enchantments using the Minecraft 1.21 data format. */
public class EnchantmentGen implements DataProvider
{
    private final PackOutput.PathProvider paths;

    public EnchantmentGen(PackOutput output)
    {
        this.paths = output.createPathProvider(PackOutput.Target.DATA_PACK, "enchantment");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        return CompletableFuture.allOf(ModEnchantments.DEFINITIONS.entrySet().stream()
            .map(entry -> DataProvider.saveStable(output, entry.getValue().toJson(entry.getKey().location().getPath()), this.paths.json(entry.getKey().location())))
            .toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() { return "CGM enchantments"; }
}

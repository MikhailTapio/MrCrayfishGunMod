package com.mrcrayfish.guns.datagen;

import com.mrcrayfish.guns.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ItemTagGen extends ItemTagsProvider
{
    public ItemTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, blockTagProvider, Reference.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        var guns = this.tag(com.mrcrayfish.guns.enchantment.EnchantmentTypes.GUN);
        com.mrcrayfish.guns.init.ModItems.REGISTER.getEntries().forEach(item -> {
            if(item.get() instanceof com.mrcrayfish.guns.item.GunItem) guns.add(item.get());
        });
        this.tag(com.mrcrayfish.guns.enchantment.EnchantmentTypes.SEMI_AUTO_GUN)
            .add(com.mrcrayfish.guns.init.ModItems.PISTOL.get(), com.mrcrayfish.guns.init.ModItems.SHOTGUN.get(),
                com.mrcrayfish.guns.init.ModItems.RIFLE.get(), com.mrcrayfish.guns.init.ModItems.HEAVY_RIFLE.get(),
                com.mrcrayfish.guns.init.ModItems.BAZOOKA.get(), com.mrcrayfish.guns.init.ModItems.GRENADE_LAUNCHER.get());
    }
}

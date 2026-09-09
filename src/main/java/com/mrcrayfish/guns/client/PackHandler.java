package com.mrcrayfish.guns.client;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforgespi.locating.IModFile;

import java.nio.file.Path;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class PackHandler {
    @SubscribeEvent
    public static void onAddPackFindersEvent(AddPackFindersEvent event) {
        if (!event.getPackType().equals(PackType.CLIENT_RESOURCES)) return;
        event.addPackFinders(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("cgm", "packs/cgm_pbr"),
            PackType.CLIENT_RESOURCES, Component.translatable("pack.cgm.pbr.title"), PackSource.FEATURE, false, Pack.Position.TOP);
    }
}

package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.blockentity.WorkbenchBlockEntity;
import com.mrcrayfish.guns.common.container.AttachmentContainer;
import com.mrcrayfish.guns.common.container.WorkbenchContainer;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Author: MrCrayfish
 */
public class ModContainers
{
    public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.MENU, Reference.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<WorkbenchContainer>> WORKBENCH = register("workbench", (IContainerFactory<WorkbenchContainer>) (windowId, playerInventory, data) -> {
        WorkbenchBlockEntity workstation = (WorkbenchBlockEntity) playerInventory.player.level().getBlockEntity(data.readBlockPos());
        return new WorkbenchContainer(windowId, playerInventory, workstation);
    });

    public static final DeferredHolder<MenuType<?>, MenuType<AttachmentContainer>> ATTACHMENTS = register("attachments", AttachmentContainer::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String id, MenuType.MenuSupplier<T> factory)
    {
        return REGISTER.register(id, () -> new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS));
    }
}

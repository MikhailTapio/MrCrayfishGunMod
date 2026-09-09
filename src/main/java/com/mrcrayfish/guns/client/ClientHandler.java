package com.mrcrayfish.guns.client;

import com.mrcrayfish.guns.util.GunItemData;

import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.client.handler.*;
import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.render.gun.model.GrenadeLauncherModel;
import com.mrcrayfish.guns.client.render.gun.model.MiniGunModel;
import com.mrcrayfish.guns.client.render.gun.model.SimpleModel;
import com.mrcrayfish.guns.client.screen.AttachmentScreen;
import com.mrcrayfish.guns.client.screen.WorkbenchScreen;
import com.mrcrayfish.guns.client.util.PropertyHelper;
import com.mrcrayfish.guns.debug.IEditorMenu;
import com.mrcrayfish.guns.debug.client.screen.EditorScreen;
import com.mrcrayfish.guns.enchantment.EnchantmentTypes;
import com.mrcrayfish.guns.init.ModBlocks;
import com.mrcrayfish.guns.init.ModContainers;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.IColored;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.C2SMessageAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.components.OptionsList;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.gui.screens.options.MouseSettingsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;

import static com.mrcrayfish.guns.Reference.MOD_ID;

/**
 * Author: MrCrayfish
 */
@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class ClientHandler {
    private static Field mouseOptionsField;

    public static void setup() {
        NeoForge.EVENT_BUS.register(AimingHandler.get());
        NeoForge.EVENT_BUS.register(BulletTrailRenderingHandler.get());
        NeoForge.EVENT_BUS.register(CrosshairHandler.get());
        NeoForge.EVENT_BUS.register(GunRenderingHandler.get());
        NeoForge.EVENT_BUS.register(RecoilHandler.get());
        NeoForge.EVENT_BUS.register(ReloadHandler.get());
        NeoForge.EVENT_BUS.register(ShootingHandler.get());
        NeoForge.EVENT_BUS.register(SoundHandler.get());
        NeoForge.EVENT_BUS.register(new PlayerModelHandler());

        // TODO: restore Controllable integration for NeoForge 1.21.1.
        setupRenderLayers();
        registerColors();
        registerModelOverrides();
    }

    private static void setupRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WORKBENCH.get(), RenderType.cutout());
    }

    private static void registerColors() {
        ItemColor color = (stack, index) -> {
            if (!IColored.isDyeable(stack)) {
                return -1;
            }
            if (index == 0 && GunItemData.hasTag(stack) && GunItemData.getTag(stack).contains("Color", Tag.TAG_INT)) {
                return GunItemData.getTag(stack).getInt("Color");
            }
            if (index == 0 && stack.getItem() instanceof IAttachment) {
                ItemStack renderingWeapon = GunRenderingHandler.get().getRenderingWeapon();
                if (renderingWeapon != null) {
                    return Minecraft.getInstance().getItemColors().getColor(renderingWeapon, index);
                }
            }
            if (index == 2) // Reticle colour
            {
                return PropertyHelper.getReticleColor(stack);
            }
            return -1;
        };
        BuiltInRegistries.ITEM.forEach(item -> {
            if (item instanceof IColored) {
                Minecraft.getInstance().getItemColors().register(color, item);
            }
        });
    }

    private static void registerModelOverrides() {
        /* Weapons */
        ModelOverrides.register(ModItems.ASSAULT_RIFLE.get(), new SimpleModel(SpecialModels.ASSAULT_RIFLE::getModel));
        ModelOverrides.register(ModItems.BAZOOKA.get(), new SimpleModel(SpecialModels.BAZOOKA::getModel));
        ModelOverrides.register(ModItems.GRENADE_LAUNCHER.get(), new GrenadeLauncherModel());
        ModelOverrides.register(ModItems.HEAVY_RIFLE.get(), new SimpleModel(SpecialModels.HEAVY_RIFLE::getModel));
        ModelOverrides.register(ModItems.MACHINE_PISTOL.get(), new SimpleModel(SpecialModels.MACHINE_PISTOL::getModel));
        ModelOverrides.register(ModItems.MINI_GUN.get(), new MiniGunModel());
        ModelOverrides.register(ModItems.PISTOL.get(), new SimpleModel(SpecialModels.PISTOL::getModel));
        ModelOverrides.register(ModItems.RIFLE.get(), new SimpleModel(SpecialModels.RIFLE::getModel));
        ModelOverrides.register(ModItems.SHOTGUN.get(), new SimpleModel(SpecialModels.SHOTGUN::getModel));
    }

    public static void registerScreenFactories(RegisterMenuScreensEvent event) {
        event.register(ModContainers.WORKBENCH.get(), WorkbenchScreen::new);
        event.register(ModContainers.ATTACHMENTS.get(), AttachmentScreen::new);
    }

    public static void registerItemExtensions(RegisterClientExtensionsEvent event) {
        IClientItemExtensions extensions = new IClientItemExtensions() {
            private final GunItemStackRenderer renderer = new GunItemStackRenderer();
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() { return renderer; }
        };
        ModItems.REGISTER.getEntries().forEach(item -> {
            if(item.get() instanceof GunItem) event.registerItem(extensions, item.get());
        });
    }

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof MouseSettingsScreen screen) {
            if (mouseOptionsField == null) {
                mouseOptionsField = ObfuscationReflectionHelper.findField(net.minecraft.client.gui.screens.options.OptionsSubScreen.class, "list");
                mouseOptionsField.setAccessible(true);
            }
            try {
                OptionsList list = (OptionsList) mouseOptionsField.get(screen);
                //list.addBig(OptionInstance.createBoolean("t", true));
                //list.addSmall(GunOptions.ADS_SENSITIVITY, GunOptions.CROSSHAIR);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.screen == null && event.getAction() == GLFW.GLFW_PRESS) {
            if (KeyBinds.KEY_ATTACHMENTS.isDown()) {
                PacketHandler.getPlayChannel().sendToServer(new C2SMessageAttachments());
            }
            /*else if(event.getKey() == GLFW.GLFW_KEY_KP_9)
            {
                mc.setScreen(new EditorScreen(null, new Debug.Menu()));
            }*/
        }
    }

    public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) manager -> {
            PropertyHelper.resetCache();
        });
    }

    public static void registerAdditional(ModelEvent.RegisterAdditional event) {
        event.register(net.minecraft.client.resources.model.ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(MOD_ID, "special/test")));
    }

    public static void onCreativeTabContents(net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().location().equals(ResourceLocation.fromNamespaceAndPath(MOD_ID, "creative_tab"))) {
            CustomGunManager.fill(event);
        }
    }

    public static Screen createEditorScreen(IEditorMenu menu) {
        return new EditorScreen(Minecraft.getInstance().screen, menu);
    }

    /* Uncomment for debugging headshot hit boxes */

    /*@SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onRenderLiving(RenderLivingEvent.Post event)
    {
        LivingEntity entity = event.getEntity();
        IHeadshotBox<LivingEntity> headshotBox = (IHeadshotBox<LivingEntity>) BoundingBoxManager.getHeadshotBoxes(entity.getType());
        if(headshotBox != null)
        {
            AxisAlignedBB box = headshotBox.getHeadshotBox(entity);
            if(box != null)
            {
                WorldRenderer.drawBoundingBox(event.getMatrixStack(), event.getBuffers().getBuffer(RenderType.getLines()), box, 1.0F, 1.0F, 0.0F, 1.0F);

                AxisAlignedBB boundingBox = entity.getBoundingBox().offset(entity.getPositionVec().inverse());
                boundingBox = boundingBox.grow(Config.COMMON.gameplay.growBoundingBoxAmount.get(), 0, Config.COMMON.gameplay.growBoundingBoxAmount.get());
                WorldRenderer.drawBoundingBox(event.getMatrixStack(), event.getBuffers().getBuffer(RenderType.getLines()), boundingBox, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }*/
}

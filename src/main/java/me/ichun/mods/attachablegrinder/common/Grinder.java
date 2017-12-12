package me.ichun.mods.attachablegrinder.common;

import me.ichun.mods.attachablegrinder.common.core.Config;
import me.ichun.mods.attachablegrinder.common.core.ProxyCommon;
import me.ichun.mods.attachablegrinder.common.item.ItemGrinder;
import me.ichun.mods.ichunutil.common.core.config.ConfigHandler;
import me.ichun.mods.ichunutil.common.iChunUtil;
import me.ichun.mods.ichunutil.common.module.update.UpdateChecker;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Grinder.MOD_ID, name = Grinder.MOD_NAME,
        version = Grinder.VERSION,
        guiFactory = iChunUtil.GUI_CONFIG_FACTORY,
        dependencies = "required-after:ichunutil@[" + iChunUtil.VERSION_MAJOR +".0.2," + (iChunUtil.VERSION_MAJOR + 1) + ".0.0)",
        acceptableRemoteVersions = "[" + iChunUtil.VERSION_MAJOR +".0.0," + iChunUtil.VERSION_MAJOR + ".1.0)",
        acceptedMinecraftVersions = iChunUtil.MC_VERSION_RANGE
)
public class Grinder
{
    public static final String MOD_NAME = "AttachableGrinder";
    public static final String MOD_ID = "attachablegrinder";
    public static final String VERSION = iChunUtil.VERSION_MAJOR + ".0.0";

    @Instance(Grinder.MOD_ID)
    public static Grinder instance;

    public static Item itemGrinder;

    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite grinderSides;
    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite grinderBlades;

    @SidedProxy(clientSide = "me.ichun.mods.attachablegrinder.client.core.ProxyClient", serverSide = "me.ichun.mods.attachablegrinder.common.core.ProxyCommon")
    public static ProxyCommon proxy;

    public static Config config;

    @EventHandler
    public void preLoad(FMLPreInitializationEvent event)
    {
        config = ConfigHandler.registerConfig(new Config(event.getSuggestedConfigurationFile()));

        UpdateChecker.registerMod(new UpdateChecker.ModVersionInfo("Grinder", iChunUtil.VERSION_OF_MC, Grinder.VERSION, false));

        proxy.preInitMod();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterItem(RegistryEvent.Register<Item> event)
    {
        Grinder.itemGrinder = new ItemGrinder();

        event.getRegistry().register(Grinder.itemGrinder);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelRegistry(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(Grinder.itemGrinder, 0, new ModelResourceLocation("attachablegrinder:grinder", "inventory"));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event)
    {
        grinderSides = event.getMap().registerSprite(new ResourceLocation("attachablegrinder", "model/grinder_ent_sides"));
        grinderBlades = event.getMap().registerSprite(new ResourceLocation("attachablegrinder", "model/grinder_ent_blade"));
    }
}

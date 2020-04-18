package me.ichun.mods.attachablegrinder.common;

import me.ichun.mods.attachablegrinder.client.render.GrinderRenderer;
import me.ichun.mods.attachablegrinder.common.core.Config;
import me.ichun.mods.attachablegrinder.common.entity.GrinderEntity;
import me.ichun.mods.attachablegrinder.common.grinder.GrinderProperties;
import me.ichun.mods.attachablegrinder.common.item.GrinderItem;
import me.ichun.mods.attachablegrinder.common.packet.PacketGrinderProperties;
import me.ichun.mods.ichunutil.common.network.PacketChannel;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(AttachableGrinder.MOD_ID)
public class AttachableGrinder
{
    public static final String MOD_ID = "attachablegrinder";
    public static final String MOD_NAME = "Attachable Grinder";
    public static final String PROTOCOL = "1";

    public static final Logger LOGGER = LogManager.getLogger();

    public static Config config;

    public static GrinderProperties grinderProperties;
    public static PacketChannel channel;

    public AttachableGrinder() //TODO add iChunUtil dependency, ask the server for data pack info.
    {
        config = new Config(MOD_ID + "-server.toml").init();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Items.REGISTRY.register(bus);
        EntityTypes.REGISTRY.register(bus);
        bus.addListener(this::onClientSetup);

        grinderProperties = new GrinderProperties();
        channel = new PacketChannel(new ResourceLocation(MOD_ID, "channel"), PROTOCOL, true, true, PacketGrinderProperties.class);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityTypes.GRINDER.get(), new GrinderRenderer.RenderFactory());
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> me.ichun.mods.ichunutil.client.core.EventHandlerClient::getConfigGui);
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        channel.sendTo(new PacketGrinderProperties(false), (ServerPlayerEntity)event.getPlayer());
    }

    public static class Items
    {
        private static final DeferredRegister<Item> REGISTRY = new DeferredRegister<>(ForgeRegistries.ITEMS, MOD_ID);

        public static final RegistryObject<GrinderItem> GRINDER = REGISTRY.register("grinder", () -> new GrinderItem(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS)));
    }

    public static class EntityTypes
    {
        private static final DeferredRegister<EntityType<?>> REGISTRY = new DeferredRegister<>(ForgeRegistries.ENTITIES, MOD_ID);

        public static final RegistryObject<EntityType<GrinderEntity>> GRINDER = REGISTRY.register("grinder", () -> EntityType.Builder.create(GrinderEntity::new, EntityClassification.MISC)
                .size(0.1F, 0.1F)
                .build("from " + MOD_NAME + ". Ignore this.")
        );
    }
}

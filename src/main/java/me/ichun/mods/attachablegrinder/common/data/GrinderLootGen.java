package me.ichun.mods.attachablegrinder.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ichun.mods.attachablegrinder.common.AttachableGrinder;
import me.ichun.mods.attachablegrinder.common.grinder.GrinderProperties;
import me.ichun.mods.ichunutil.common.data.LootTableGen;
import me.ichun.mods.ichunutil.common.iChunUtil;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.functions.Smelt;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = AttachableGrinder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GrinderLootGen
{
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();

        if (event.includeServer()) {
            gen.addProvider(new LootTableGen(gen,
                    new LootTableGen.Entities()
                            .add(EntityType.PIG, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.PORKCHOP)
                                                        .acceptFunction(Smelt.func_215953_b()
                                                                .acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.THIS, EntityLootTables.field_218586_a))))));
                            })
                            .add(EntityType.CHICKEN, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.FEATHER)
                                                )
                                        )
                                );
                            })
                            .add(EntityType.ZOMBIE, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.ROTTEN_FLESH)
                                                )
                                        )
                                );
                            })
                            .add(EntityType.CREEPER, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.GUNPOWDER)
                                                )
                                        )
                                );
                            })
                            .add(EntityType.SKELETON, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.BONE_MEAL)
                                                )
                                        )
                                );
                            })
                            .add(EntityType.WITHER_SKELETON, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.BONE_MEAL)
                                                        .weight(4)
                                                )
                                                .addEntry(ItemLootEntry.builder(Items.COAL)
                                                        .weight(1)
                                                )
                                        ));
                            })
                            .add(EntityType.COW, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.BEEF)
                                                        .weight(4)
                                                        .acceptFunction(Smelt.func_215953_b()
                                                                .acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.THIS, EntityLootTables.field_218586_a))))
                                                .addEntry(ItemLootEntry.builder(Items.LEATHER)
                                                        .weight(1)
                                                )
                                        ));
                            })
                            .add(EntityType.CAVE_SPIDER, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.STRING)
                                                )
                                        )
                                );
                            })
                            .add(EntityType.SPIDER, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.STRING)
                                                )
                                        )
                                );
                            })
                            .add(EntityType.DROWNED, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.ROTTEN_FLESH)
                                                )
                                        )
                                );
                            })
                            .add(EntityType.ENDERMAN, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.ENDER_PEARL)
                                                )
                                        )
                                );
                            })
                            .add(EntityType.HUSK, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.ROTTEN_FLESH)
                                                )
                                        )
                                );
                            })
                            .add(EntityType.LLAMA, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.LEATHER)
                                                )
                                        )
                                );
                            })
                            .add(EntityType.TRADER_LLAMA, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.LEATHER)
                                                )
                                        )
                                );
                            })
                            .add(EntityType.MOOSHROOM, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.BEEF)
                                                        .weight(4)
                                                        .acceptFunction(Smelt.func_215953_b()
                                                                .acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.THIS, EntityLootTables.field_218586_a))))
                                                .addEntry(ItemLootEntry.builder(Items.LEATHER)
                                                        .weight(1)
                                                )
                                        ));
                            })
                            .add(EntityType.STRAY, (tables, type) -> {
                                tables.func_218582_a(type, LootTable.builder()
                                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                                .addEntry(ItemLootEntry.builder(Items.BONE_MEAL)
                                                )
                                        )
                                );
                            })
            ) {
                @Override
                public void act(DirectoryCache cache)
                {
                    super.act(cache);

                    HashMap<EntityType<?>, GrinderProperties.Properties> properties = new HashMap<>();
                    properties.put(EntityType.PIG, new GrinderProperties.Properties(EntityType.PIG.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/pig").toString(), false, false, 0.9D, 0.0D, 0.0D, 0.0D, 0.6D));
                    properties.put(EntityType.CHICKEN, new GrinderProperties.Properties(EntityType.CHICKEN.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/chicken").toString(), false, false, 0.7D, 0.0D, 0.0D, -0.5D, 0.3D));
                    properties.put(EntityType.COW, new GrinderProperties.Properties(EntityType.COW.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/cow").toString(), false, false, 1.4D, 0.0D, 0.0D, 0.0D, 0.6D));
                    properties.put(EntityType.LLAMA, new GrinderProperties.Properties(EntityType.LLAMA.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/llama").toString(), false, false, 1.4D, 0.0D, 0.0D, 0.0D, 0.7D));
                    properties.put(EntityType.TRADER_LLAMA, new GrinderProperties.Properties(EntityType.TRADER_LLAMA.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/trader_llama").toString(), false, false, 1.5D, 0.0D, 0.0D, 0.0D, 0.7D));
                    properties.put(EntityType.MOOSHROOM, new GrinderProperties.Properties(EntityType.MOOSHROOM.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/mooshroom").toString(), false, false, 1.4D, 0.0D, 0.0D, 0.0D, 0.6D));

                    properties.put(EntityType.CREEPER, new GrinderProperties.Properties(EntityType.CREEPER.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/creeper").toString(), true, false, 0.8D, 0.0D, 0.1275D, 1.5D, 0.45D));
                    properties.put(EntityType.ZOMBIE, new GrinderProperties.Properties(EntityType.ZOMBIE.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/zombie").toString(), true, false, 1.275D, 0.0D, 0.1275D, 0.0D, 0.45D));
                    properties.put(EntityType.DROWNED, new GrinderProperties.Properties(EntityType.DROWNED.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/drowned").toString(), true, false, 1.275D, 0.0D, 0.1275D, 0.0D, 0.45D));
                    properties.put(EntityType.HUSK, new GrinderProperties.Properties(EntityType.HUSK.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/husk").toString(), true, false, 1.275D * 1.0625F, 0.0D, 0.1275D * 1.0625F, 0.0D, 0.45D * 1.0625F));
                    properties.put(EntityType.SKELETON, new GrinderProperties.Properties(EntityType.SKELETON.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/skeleton").toString(), true, false, 1.275D, 0.0D, 0.1275D, 0.0D, 0.45D));
                    properties.put(EntityType.STRAY, new GrinderProperties.Properties(EntityType.STRAY.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/stray").toString(), true, false, 1.275D, 0.0D, 0.1275D, 0.0D, 0.45D));
                    properties.put(EntityType.WITHER_SKELETON, new GrinderProperties.Properties(EntityType.WITHER_SKELETON.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/wither_skeleton").toString(), true, false, 1.275D * 1.2D, 0.0D, 0.1275D * 1.2D, 0.0D, 0.45D * 1.2D));
                    properties.put(EntityType.ENDERMAN, new GrinderProperties.Properties(EntityType.ENDERMAN.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/enderman").toString(), true, false, 2D, 0.0D, 0.13D, 0.0D, 0.45D));

                    properties.put(EntityType.CAVE_SPIDER, new GrinderProperties.Properties(EntityType.CAVE_SPIDER.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/cave_spider").toString(), false, false, (13/16D + 0.025D) * 0.7D, 0.0D, -10/16D * 0.7D, 0D, 0.45D * 0.7D));
                    properties.put(EntityType.SPIDER, new GrinderProperties.Properties(EntityType.SPIDER.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/spider").toString(), false, false, 13/16D + 0.025D, 0.0D, -12/16D * 0.7D, 0D, 0.45D));

                    properties.put(EntityType.PLAYER, new GrinderProperties.Properties(EntityType.PLAYER.getRegistryName().toString(), new ResourceLocation(AttachableGrinder.MOD_ID, "entities/player").toString(), true, false, 1.275D, 0.0D, 0.1275D, 1.5D, 0.45D));

                    //test

                    Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

                    properties.forEach((key, value) -> {
                        Path target = gen.getOutputFolder().resolve("data/" + AttachableGrinder.MOD_ID + "/grinder/" + key.getRegistryName().getPath() + ".json");
                        try {
                            IDataProvider.save(GSON, cache, GSON.toJsonTree(value), target);
                            iChunUtil.LOGGER.info("Saved grinder info {}", target);
                        } catch (IOException ioexception) {
                            iChunUtil.LOGGER.error("Couldn't save grinder info {}", target, ioexception);
                        }
                    });

                }
            });
        }
    }
}


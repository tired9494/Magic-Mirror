package github.tired9494.magic_mirror;

import github.tired9494.magic_mirror.config.MagicMirrorConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MagicMirror implements ModInitializer {

    public static final MagicMirrorItem MAGIC_MIRROR = new MagicMirrorItem(new Item.Settings().group(ItemGroup.TRANSPORTATION).maxCount(1));
    private static final Identifier DUNGEON_CHEST_ID = LootTables.SIMPLE_DUNGEON_CHEST;
    private static final Identifier MINESHAFT_CHEST_ID = LootTables.ABANDONED_MINESHAFT_CHEST;
    private static final Identifier DESERT_CHEST_ID = LootTables.DESERT_PYRAMID_CHEST;
    private static final Identifier END_CHEST_ID = LootTables.END_CITY_TREASURE_CHEST;
    private static final Identifier BASTION_CHEST_ID = LootTables.BASTION_OTHER_CHEST;

    public static final ConfigHolder<MagicMirrorConfig> CONFIG = MagicMirrorConfig.init();

    @Override
    public void onInitialize() {
        if (getConfig().recipe) {
            Registry.register(Registry.ITEM, new Identifier("magic_mirror", "magic_mirror"), MAGIC_MIRROR);
        }
        else {
            Registry.register(Registry.ITEM, new Identifier("magic_mirror", "magic_mirror_treasure"), MAGIC_MIRROR);
            if (getConfig().chestLoot) {
                LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
                    if (DUNGEON_CHEST_ID.equals(id)) {
                        LootPool pool = FabricLootPoolBuilder.builder()
                                .withEntry(ItemEntry.builder(MagicMirror.MAGIC_MIRROR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .build();
                        supplier.withPool(pool);
                    } else if (MINESHAFT_CHEST_ID.equals(id)) {
                        LootPool pool = FabricLootPoolBuilder.builder()
                                .withEntry(ItemEntry.builder(MagicMirror.MAGIC_MIRROR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .build();
                        supplier.withPool(pool);
                    } else if (DESERT_CHEST_ID.equals(id)) {
                        LootPool pool = FabricLootPoolBuilder.builder()
                                .withEntry(ItemEntry.builder(MagicMirror.MAGIC_MIRROR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .build();
                        supplier.withPool(pool);
                    } else if (END_CHEST_ID.equals(id)) {
                        LootPool pool = FabricLootPoolBuilder.builder()
                                .withEntry(ItemEntry.builder(MagicMirror.MAGIC_MIRROR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .build();
                        supplier.withPool(pool);
                    } else if (BASTION_CHEST_ID.equals(id)) {
                        LootPool pool = FabricLootPoolBuilder.builder()
                                .withEntry(ItemEntry.builder(MagicMirror.MAGIC_MIRROR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .withEntry(ItemEntry.builder(Items.AIR).build())
                                .build();
                        supplier.withPool(pool);
                    }
                });
            }
        }
    }
    public static  MagicMirrorConfig getConfig() {return CONFIG.getConfig();}
}

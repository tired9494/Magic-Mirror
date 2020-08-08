package github.tired9494;

import github.tired9494.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MagicMirror implements ModInitializer {

    public static final MagicMirrorItem MAGIC_MIRROR = new MagicMirrorItem(new Item.Settings().group(ItemGroup.TRANSPORTATION).maxCount(1));

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("magic_mirror", "magic_mirror"), MAGIC_MIRROR);
        ModConfig.initialise();
    }
}

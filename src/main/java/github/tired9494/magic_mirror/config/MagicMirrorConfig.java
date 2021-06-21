package github.tired9494.magic_mirror.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import github.tired9494.magic_mirror.MagicMirror;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

@Config(name = "Magic Mirror")
public class MagicMirrorConfig implements ConfigData
{
    /**
     * mirror options
     */
    //@Comment("")
    //@ConfigEntry.Gui.CollapsibleObject
    //public mirrorOptions mirrorOptions = new mirrorOptions();


    //public static class mirrorOptions
    //{
        @Comment("Changing this option §c§ldeletes all existing mirrors. §rRequires restart")
        public boolean recipe = true;

        @Comment("Enables the mirror to spawn in chest loot. Requires restart")
        public boolean chestLoot = false;

        @Comment("Mirror works between dimensions")
        public boolean interdimensional = false;

        @Comment("XP cost (in levels) of mirror")
        public int xpCost = 2;

        @Comment("Damage inflicted by mirror in half-hearts")
        public float damage = 5.0F;

        @Comment("Length (in ticks) of weakness inflicted by mirror")
        public int weaknessLength = 1200;

        @Comment("Length (in ticks) of mining fatigue inflicted by mirror")
        public int fatigueLength = 100;

        @Comment("Length (in ticks) of blindness inflicted by mirror")
        public int blindnessLength = 100;

        @Comment("Length (in ticks) of hunger inflicted by mirror")
        public int hungerLength = 0;

        @Comment("Length (in ticks) of nausea inflicted by mirror")
        public int nauseaLength = 0;

    //}



    /**
     * Registers and prepares a new configuration instance.
     *
     * @return registered config holder
     * @see AutoConfig#register
     */
    public static ConfigHolder<MagicMirrorConfig> init()
    {
        // Register the config
        ConfigHolder<MagicMirrorConfig> holder = AutoConfig.register(MagicMirrorConfig.class, JanksonConfigSerializer::new);

        // Listen for when the server is reloading (i.e. /reload), and reload the config
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((s, m) ->
                AutoConfig.getConfigHolder(MagicMirrorConfig.class).load());

        return holder;
    }

    /**
     * Mod Menu integration.
     */
    @Environment(EnvType.CLIENT)
    public static class ModMenuIntegration implements ModMenuApi
    {
        @Override
        public ConfigScreenFactory<?> getModConfigScreenFactory()
        {
            return screen -> AutoConfig.getConfigScreen(MagicMirrorConfig.class, screen).get();
        }
    }
}

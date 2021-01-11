package com.anonymoushacker1279.immersiveweapons.util;

import java.nio.file.Path;

import com.anonymoushacker1279.immersiveweapons.ImmersiveWeapons;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

	private static final ForgeConfigSpec.Builder CONFIG = new ForgeConfigSpec.Builder();
    private static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.ConfigValue<Integer> MAX_ABANDONED_FACTORY_DISTANCE;
    public static ForgeConfigSpec.ConfigValue<Integer> MIN_ABANDONED_FACTORY_DISTANCE;
    public static ForgeConfigSpec.ConfigValue<Boolean> TESLA_ARMOR_EFFECT_SOUND;

    static {
        initConfig();
    }


    private static void initConfig() {
        CONFIG.push(ImmersiveWeapons.MOD_ID);
        MAX_ABANDONED_FACTORY_DISTANCE = CONFIG.comment("Maximum distance in chunks between Abandoned Factories - Default 180").define("max_abandoned_factory_distance", 180);
        MIN_ABANDONED_FACTORY_DISTANCE = CONFIG.comment("Minimum distance in chunks between Abandoned Factories - Default 130").define("min_abandoned_factory_distance", 130);
        TESLA_ARMOR_EFFECT_SOUND = CONFIG.comment("Enable/Disable the Tesla Armor effect sound - Default true").define("tesla_armor_effect_sound", true);

        CONFIG.pop();
        COMMON_CONFIG = CONFIG.build();

    }

    public static void setup(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        COMMON_CONFIG.setConfig(configData);
    }

}
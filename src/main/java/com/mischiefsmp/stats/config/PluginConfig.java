package com.mischiefsmp.stats.config;

import com.mischiefsmp.core.config.ConfigFile;
import com.mischiefsmp.core.config.ConfigManager;
import com.mischiefsmp.core.config.ConfigValue;

import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PluginConfig extends ConfigFile {

    @Getter
    @ConfigValue(path = "language")
    private String defaultLanguage;

    @Getter
    @ConfigValue(path = "languages")
    private ArrayList<String> languages;

    @Getter
    @ConfigValue(path = "disabled-weapons")
    private ArrayList<String> disabledWeapons;

    @Getter
    @ConfigValue(path = "disabled-causes")
    private ArrayList<String> disabledCauses;

    public PluginConfig(Plugin plugin) {
        super(plugin, "config.yml", "config.yml");
        ConfigManager.init(this);
    }
}

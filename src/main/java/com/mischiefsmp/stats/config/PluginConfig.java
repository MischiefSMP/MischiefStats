package com.mischiefsmp.stats.config;

import com.mischiefsmp.core.config.ConfigFile;
import com.mischiefsmp.core.config.ConfigManager;
import com.mischiefsmp.core.config.ConfigValue;

import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

@Getter
public class PluginConfig extends ConfigFile {
    @ConfigValue(path = "language")
    private String defaultLanguage;

    @ConfigValue(path = "languages")
    private ArrayList<String> languages;

    @ConfigValue(path = "disabled-weapons")
    private ArrayList<String> disabledWeapons;

    @ConfigValue(path = "disabled-causes")
    private ArrayList<String> disabledCauses;

    public PluginConfig(Plugin plugin) {
        super(plugin, "config.yml", "config.yml");
        ConfigManager.init(this);
    }
}

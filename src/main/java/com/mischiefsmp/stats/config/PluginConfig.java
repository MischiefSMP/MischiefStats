package com.mischiefsmp.stats.config;

import com.mischiefsmp.core.utils.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class PluginConfig {
    private static FileConfiguration fc;

    public static void init(Plugin plugin) {
        fc = FileUtils.loadConfig(plugin, "config.yml");
    }

    public static String getDefaultLanguage() {
        return fc.getString("language");
    }

    public static List<String> getLanguages() {
        return fc.getStringList("languages");
    }

    public static List<String> getDisabledWeapons() {
        return fc.getStringList("disabled-weapons");
    }

    public static List<String> getDisabledCauses() {
        return fc.getStringList("disabled-causes");
    }

}

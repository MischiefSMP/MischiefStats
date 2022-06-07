package com.mischiefsmp.stats;

import com.mischiefsmp.core.LangManager;
import com.mischiefsmp.core.LogManager;
import com.mischiefsmp.stats.config.PluginConfig;
import com.mischiefsmp.stats.events.EntityDeath;
import org.bukkit.plugin.java.JavaPlugin;

public class MischiefStats extends JavaPlugin {
    private static LogManager logManager;
    private static LangManager langManager;

    @Override
    public void onEnable() {
        PluginConfig.init(this);
        logManager = new LogManager(this);
        langManager = new LangManager(this, PluginConfig.getLanguages(), PluginConfig.getDefaultLanguage());

        getServer().getPluginManager().registerEvents(new EntityDeath(), this);
    }

    public static LogManager getLogManager() {
        return logManager;
    }

    public static LangManager getLangManager() {
        return langManager;
    }
}

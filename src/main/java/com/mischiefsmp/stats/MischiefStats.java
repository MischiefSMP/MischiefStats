package com.mischiefsmp.stats;

import com.mischiefsmp.core.LangManager;
import com.mischiefsmp.core.LogManager;
import com.mischiefsmp.stats.config.PluginConfig;
import com.mischiefsmp.stats.events.EntityDeath;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class MischiefStats extends JavaPlugin {
    @Getter
    private static MischiefStats instance;
    @Getter
    private static LogManager logManager;
    @Getter
    private static LangManager langManager;
    @Getter
    private static PluginConfig pluginConfig;

    @Override
    public void onEnable() {
        instance = this;
        pluginConfig = new PluginConfig(this);
        logManager = new LogManager(this);
        langManager = new LangManager(this, pluginConfig.getLanguages(), pluginConfig.getDefaultLanguage());

        getServer().getPluginManager().registerEvents(new EntityDeath(), this);
    }
}

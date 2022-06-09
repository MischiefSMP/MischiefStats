package com.mischiefsmp.stats;

import com.mischiefsmp.core.LangManager;
import com.mischiefsmp.core.LogManager;
import com.mischiefsmp.core.cmdinfo.CMDInfoManager;
import com.mischiefsmp.core.utils.MCUtils;
import com.mischiefsmp.stats.commands.StatsCommand;
import com.mischiefsmp.stats.config.PlayerStatsManager;
import com.mischiefsmp.stats.config.PluginConfig;
import com.mischiefsmp.stats.events.EntityDamage;
import com.mischiefsmp.stats.events.EntityDeath;
import com.mischiefsmp.stats.events.PlayerDeath;
import lombok.Getter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
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
    @Getter
    private static CMDInfoManager cmdInfoManager;

    @Override
    public void onEnable() {
        instance = this;
        pluginConfig = new PluginConfig(this);
        logManager = new LogManager(this);
        langManager = new LangManager(this, pluginConfig.getLanguages(), pluginConfig.getDefaultLanguage());
        cmdInfoManager = new CMDInfoManager(this);

        getServer().getPluginManager().registerEvents(new EntityDeath(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new EntityDamage(), this);

        registerCommand("stats", new StatsCommand());

        MCUtils.registerPermissions(cmdInfoManager);

        int delay = 20 * 60 * pluginConfig.getSaveInterval();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, PlayerStatsManager::save, delay, delay);
    }

    @Override
    public void onDisable() {
        PlayerStatsManager.save();
    }

    private void registerCommand(String name, CommandExecutor exec) {
        PluginCommand c = getServer().getPluginCommand(name);
        if(c != null)
            c.setExecutor(exec);
    }
}

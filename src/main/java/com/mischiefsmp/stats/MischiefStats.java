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

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

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

    public void ensureCore(boolean required) {
        if(getServer().getPluginManager().getPlugin("MischiefCore") != null) return;
        getLogger().log(Level.INFO, "Downloading MischiefCore...");
        File pluginFile = new File(new File("").getAbsolutePath() + File.separator + "plugins" + File.separator + "MischiefCore.jar");
        try {
            InputStream in = new URL("https://github.com/MischiefSMP/MischiefCore/releases/latest/download/MischiefCore.jar").openStream();
            Files.copy(in, pluginFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            getServer().getPluginManager().enablePlugin(getServer().getPluginManager().loadPlugin(pluginFile));
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error downloading MischiefCore! Message: " + e.getMessage());
            if(required) getServer().shutdown();
        }
        getLogger().log(Level.INFO, "Done downloading MischiefCore!");
    }

    @Override
    public void onEnable() {
        ensureCore(true);

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

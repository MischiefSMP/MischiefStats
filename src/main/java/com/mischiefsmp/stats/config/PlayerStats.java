package com.mischiefsmp.stats.config;

import com.mischiefsmp.core.config.ConfigFile;
import com.mischiefsmp.core.config.ConfigManager;
import com.mischiefsmp.core.config.ConfigValue;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

@Getter
public class PlayerStats extends ConfigFile {

    @ConfigValue(path = "suicides")
    private int suicides;

    public PlayerStats(Plugin plugin, UUID uuid) {
        super(plugin, String.format("players/%s.yml", uuid.toString()), null);
        ConfigManager.init(this);
    }

    public void addSuicide() {
        suicides++;
    }
}

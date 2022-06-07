package com.mischiefsmp.stats.config;

import com.mischiefsmp.stats.MischiefStats;
import com.mischiefsmp.stats.Utils;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerStatsManager {
    private static HashMap<UUID, PlayerStats> stats = new HashMap<>();

    public static void addStat(UUID player, String entity, String cause, String weapon, double finalDamage) {
        if(Utils.containsStringIgnoreCase(PluginConfig.getDisabledWeapons(), weapon))
            return;
        if(Utils.containsStringIgnoreCase(PluginConfig.getDisabledCauses(), cause))
            return;

        //TODO: remove before release
        MischiefStats.getLogManager().logF("Adding stat for player %s, killed entity %s, cause %s with weapon %s", Level.INFO, Bukkit.getPlayer(player).getName(), entity, cause, weapon, finalDamage);

    }
}

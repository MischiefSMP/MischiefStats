package com.mischiefsmp.stats.config;

import com.mischiefsmp.core.config.ConfigManager;
import com.mischiefsmp.stats.MischiefStats;
import com.mischiefsmp.stats.Utils;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerStatsManager {
    private static HashMap<UUID, PlayerStats> stats = new HashMap<>();

    private static void ensurePlayerStat(UUID uuid) {
        if(!stats.containsKey(uuid)) {
            stats.put(uuid, new PlayerStats(MischiefStats.getInstance(), uuid));
        }
    }

    public static void addSuicideStat(UUID uuid) {
        ensurePlayerStat(uuid);
        stats.get(uuid).addSuicide();
        save();
    }

    //if entity is player write it down separately
    public static void addKilledEntStat(UUID player, String entity, String cause, String weapon, double finalDamage) {
        ensurePlayerStat(player);

        if(Utils.containsStringIgnoreCase(MischiefStats.getPluginConfig().getDisabledWeapons(), weapon))
            return;
        if(Utils.containsStringIgnoreCase(MischiefStats.getPluginConfig().getDisabledCauses(), cause))
            return;

        //TODO: remove before release
        MischiefStats.getLogManager().logF("Adding stat for player %s, killed entity %s, cause %s with weapon %s", Level.INFO, Bukkit.getPlayer(player).getName(), entity, cause, weapon, finalDamage);
    }

    //TODO: Save every x minutes
    public static void save() {
        for(UUID uuid : stats.keySet()) {
            ConfigManager.save(stats.get(uuid));
        }
    }
}

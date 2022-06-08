package com.mischiefsmp.stats.config;

import com.mischiefsmp.core.config.ConfigManager;
import com.mischiefsmp.stats.MischiefStats;
import com.mischiefsmp.stats.Utils;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerStatsManager {
    private static HashMap<UUID, PlayerStats> allStats = new HashMap<>();
    //TODO: create a config for the server, and log total things as well maybe?

    private static void ensurePlayerStat(UUID uuid) {
        if(!allStats.containsKey(uuid)) {
            allStats.put(uuid, new PlayerStats(MischiefStats.getInstance(), uuid));
        }
    }

    public static void addSuicideStat(UUID uuid) {
        ensurePlayerStat(uuid);
        allStats.get(uuid).addSuicide();
        save();
    }

    //if entity is player write it down separately
    public static void addKilledEntStat(UUID uuid, String entity, String cause, String weapon, double finalDamage) {
        ensurePlayerStat(uuid);

        //Invalid cause or weapon
        if(Utils.containsStringIgnoreCase(MischiefStats.getPluginConfig().getDisabledWeapons(), weapon)
        || Utils.containsStringIgnoreCase(MischiefStats.getPluginConfig().getDisabledCauses(), cause))
            return;

        PlayerStats stats = allStats.get(uuid);
        stats.addKilledMob(entity);
        stats.addUsedCause(cause);
        if(weapon != null)
            stats.addUsedWeapon(weapon);
        stats.addMostDamage(finalDamage);
        save();
    }

    //TODO: Save every x minutes
    public static void save() {
        for(UUID uuid : allStats.keySet()) {
            ConfigManager.save(allStats.get(uuid));
        }
    }
}

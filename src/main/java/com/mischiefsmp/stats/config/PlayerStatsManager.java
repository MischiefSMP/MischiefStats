package com.mischiefsmp.stats.config;

import com.mischiefsmp.core.config.ConfigManager;
import com.mischiefsmp.stats.MischiefStats;
import com.mischiefsmp.stats.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerStatsManager {
    private static HashMap<UUID, PlayerStats> allStats = new HashMap<>();
    //TODO: create a config for the server, and log total things as well maybe?

    private static void ensurePlayerStat(Player player) {
        UUID uuid = player.getUniqueId();
        if(!allStats.containsKey(uuid)) {
            allStats.put(uuid, new PlayerStats(MischiefStats.getInstance(), uuid));
        }
    }

    public static void addSuicideStat(Player player) {
        ensurePlayerStat(player);
        allStats.get(player.getUniqueId()).addSuicide();
        save();
    }

    //if entity is player write it down separately
    public static void addKilledEntStat(Player player, LivingEntity entity, EntityDamageEvent cause, ItemStack weapon) {
        ensurePlayerStat(player);

        //Invalid cause or weapon
        if(Utils.checkIfAllowedWeapon(weapon) || Utils.checkIfAllowedCause(cause))
            return;

        PlayerStats stats = allStats.get(player.getUniqueId());
        stats.addKilledMob(Utils.livingEntityToString(entity));
        stats.addUsedCause(Utils.damageCauseToString(cause));
        stats.addUsedWeapon(Utils.weaponToString(weapon));
        stats.addMostDamage(cause.getFinalDamage());

        save();
    }

    //TODO: Save every x minutes
    public static void save() {
        for(UUID uuid : allStats.keySet()) {
            ConfigManager.save(allStats.get(uuid));
        }
    }
}

package com.mischiefsmp.stats.config;

import com.mischiefsmp.core.config.ConfigManager;
import com.mischiefsmp.stats.MischiefStats;
import com.mischiefsmp.stats.Utils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class PlayerStatsManager {
    private static final HashMap<UUID, PlayerStats> allStats = new HashMap<>();
    //TODO: create a config for the server, and log total things as well maybe?

    private static void ensurePlayerStat(Player... players) {
        for(Player player : players) {
            UUID uuid = player.getUniqueId();
            if (!allStats.containsKey(uuid))
                allStats.put(uuid, new PlayerStats(MischiefStats.getInstance(), uuid));
        }
    }

    public static void addTotalDamage(Player player, Double damage) {
        ensurePlayerStat(player);
        if (Utils.checkIfAllowedCreative(player)) {
            allStats.get(player.getUniqueId()).addTotalDamage(damage);
        }
        save();
    }

    //"player" committed suicide
    public static void addSuicideStat(Player player) {
        ensurePlayerStat(player);
        allStats.get(player.getUniqueId()).addSuicide();
        allStats.get(player.getUniqueId()).addDeath();
        save();
    }

    public static void addNonPlayerDeath(Player player) {
        ensurePlayerStat(player);
        allStats.get(player.getUniqueId()).addDeath();
        save();
    }

    public static void kdEvent(Player killed, Player killer, EntityDamageEvent cause, ItemStack weapon) {
        ensurePlayerStat(killed, killer);
        if(!Utils.checkIfAllowed(null, weapon, cause))
            return;

        if(Utils.checkIfAllowedCreative(killed)) {
            PlayerStats killedStats = allStats.get(killed.getUniqueId());
            killedStats.addPlayerDeath();
            killedStats.addDeath();
        }

        if(Utils.checkIfAllowedCreative(killer)) {
            PlayerStats killerStats = allStats.get(killer.getUniqueId());
            killerStats.addPlayerKill();
            killerStats.addUsedCause(cause);
            killerStats.addUsedWeapon(weapon);
            killerStats.addMostDamage(cause.getFinalDamage());
        }

        save();
    }

    //if entity is player write it down separately
    public static void addKilledEntStat(Player player, LivingEntity entity, EntityDamageEvent cause, ItemStack weapon) {
        ensurePlayerStat(player);
        if(!Utils.checkIfAllowed(player, weapon, cause))
            return;

        PlayerStats stats = allStats.get(player.getUniqueId());
        stats.addKilledMob(entity);
        stats.addUsedCause(cause);
        stats.addUsedWeapon(Utils.getIfWeaponCause(weapon, cause));
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

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

    //"player" committed suicide
    public static void addSuicideStat(Player player) {
        ensurePlayerStat(player);
        allStats.get(player.getUniqueId()).addSuicide();
        save();
    }

    public static void kdEvent(Player killed, Player killer, EntityDamageEvent cause, ItemStack weapon) {
        ensurePlayerStat(killed, killer);
        Utils.checkIfAllowed(weapon, cause);

        PlayerStats killedStats = allStats.get(killed.getUniqueId());
        PlayerStats killerStats = allStats.get(killer.getUniqueId());

        killedStats.addPlayerDeath();
        killedStats.addDeath();

        killerStats.addPlayerKill();
        killerStats.addUsedCause(cause);
        killerStats.addUsedWeapon(weapon);
        killerStats.addMostDamage(cause.getFinalDamage());

        save();
    }

    //if entity is player write it down separately
    public static void addKilledEntStat(Player player, LivingEntity entity, EntityDamageEvent cause, ItemStack weapon) {
        ensurePlayerStat(player);
        Utils.checkIfAllowed(weapon, cause);

        PlayerStats stats = allStats.get(player.getUniqueId());
        stats.addKilledMob(entity);
        stats.addUsedCause(cause);
        stats.addUsedWeapon(weapon);
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

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

    private static void ensurePlayerStat(Player player) {
        UUID uuid = player.getUniqueId();
        if(!allStats.containsKey(uuid)) {
            allStats.put(uuid, new PlayerStats(MischiefStats.getInstance(), uuid));
        }
    }

    //"player" committed suicide
    public static void addSuicideStat(Player player) {
        ensurePlayerStat(player);
        allStats.get(player.getUniqueId()).addSuicide();
        save();
    }

    //"player" killed another player, increase that stat
    public static void addKilledPlayer(Player player) {
        ensurePlayerStat(player);
        allStats.get(player.getUniqueId()).addPlayerKill();
        save();
    }

    //"player" died. Possibly by other player
    public static void addDeath(Player player, Player killer) {
        ensurePlayerStat(player);
        if(killer == null)  allStats.get(player.getUniqueId()).addDeath();
        else                allStats.get(player.getUniqueId()).addPlayerDeath();
        save();
    }

    //if entity is player write it down separately
    public static void addKilledEntStat(Player player, LivingEntity entity, EntityDamageEvent cause, ItemStack weapon) {
        ensurePlayerStat(player);

        //Invalid cause or weapon
        if(Utils.checkIfAllowedWeapon(weapon) || Utils.checkIfAllowedCause(cause))
            return;

        PlayerStats stats = allStats.get(player.getUniqueId());
        if(entity instanceof Player) addKilledPlayer(player);
        else stats.addKilledMob(entity);
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

package com.mischiefsmp.stats.config;

import com.mischiefsmp.core.config.ConfigFile;
import com.mischiefsmp.core.config.ConfigManager;
import com.mischiefsmp.core.config.ConfigValue;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerStats extends ConfigFile {
    @ConfigValue(path = "playerKills")
    private int playerKills;

    @ConfigValue(path = "playerDeaths")
    private int playerDeaths;

    @ConfigValue(path = "deaths")
    private int deaths;

    @ConfigValue(path = "suicides")
    private int suicides;

    @ConfigValue(path = "totalDamage")
    private double totalDamage;

    @ConfigValue(path = "mostDamage")
    private double mostDamage;

    @ConfigValue(path = "killedMobs")
    private final Map<String, Integer> killedMobs = new HashMap<>();

    @ConfigValue(path = "usedWeapons")
    private final Map<String, Integer> usedWeapons = new HashMap<>();

    @ConfigValue(path = "usedCauses")
    private final Map<String, Integer> usedCauses = new HashMap<>();

    public PlayerStats(Plugin plugin, UUID uuid) {
        super(plugin, String.format("players/%s.yml", uuid.toString()), "playertemplate.yml");
        ConfigManager.init(this);
    }

    public void addPlayerKill() {
        playerKills++;
    }

    public void addPlayerDeath() {
        playerDeaths++;
    }

    public void addDeath() {
        deaths++;
    }

    public void addSuicide() {
        suicides++;
    }

    public void addTotalDamage(double damage) {
        totalDamage += damage;
    }

    public void addMostDamage(double damage) {
        if(damage > mostDamage)
            mostDamage = damage;
    }

    public void addKilledMob(String entity) {
        if(entity == null)
            return;

        if(killedMobs.containsKey(entity)) {
            int current = killedMobs.get(entity) + 1;
            killedMobs.put(entity, current);
            return;
        }
        killedMobs.put(entity, 1);
    }

    public void addUsedWeapon(String weapon) {
        if(weapon == null)
            return;

        if(usedWeapons.containsKey(weapon)) {
            int current = usedWeapons.get(weapon) + 1;
            usedWeapons.put(weapon, current);
            return;
        }
        usedWeapons.put(weapon, 1);
    }

    public void addUsedCause(String cause) {
        if(cause == null)
            return;

        if(usedCauses.containsKey(cause)) {
            int current = usedCauses.get(cause) + 1;
            usedCauses.put(cause, current);
            return;
        }
        usedCauses.put(cause, 1);
    }
}

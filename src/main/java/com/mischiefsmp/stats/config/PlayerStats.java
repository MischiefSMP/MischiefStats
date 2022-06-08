package com.mischiefsmp.stats.config;

import com.mischiefsmp.core.config.ConfigFile;
import com.mischiefsmp.core.config.ConfigManager;
import com.mischiefsmp.core.config.ConfigValue;
import com.mischiefsmp.stats.Utils;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
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

    @ConfigValue(path = "deathCauses")
    private final Map<String, Integer> deathCauses = new HashMap<>();

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

    public void addKilledMob(LivingEntity entity) {
        increaseInHashMap(killedMobs, Utils.livingEntityToString(entity));
    }

    public void addUsedWeapon(ItemStack weapon) {
        increaseInHashMap(usedWeapons, Utils.weaponToString(weapon));
    }

    public void addUsedCause(EntityDamageEvent cause) {
        increaseInHashMap(usedCauses, Utils.damageCauseToString(cause));
    }

    public void addDeathCause(EntityDamageEvent cause) {
        increaseInHashMap(deathCauses, Utils.damageCauseToString(cause));
    }

    private void increaseInHashMap(Map<String, Integer> map, String index) {
        if(index == null)
            return;

        if(map.containsKey(index)) {
            int current = map.get(index) + 1;
            map.put(index, current);
            return;
        }
        map.put(index, 1);
    }
}

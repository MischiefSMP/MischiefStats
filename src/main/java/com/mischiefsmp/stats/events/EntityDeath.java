package com.mischiefsmp.stats.events;

import com.mischiefsmp.stats.config.PlayerStatsManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDeath implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if(killer == null)
            return;

        LivingEntity killedEnt = event.getEntity();

        if(killedEnt instanceof Player) {
            if(killedEnt.getUniqueId().equals(killer.getUniqueId())) {
                PlayerStatsManager.addSuicideStat(killer.getUniqueId());
                return;
            }
        }

        EntityDamageEvent cause = killedEnt.getLastDamageCause();
        if(cause != null) {
            boolean usedWeapon = switch (cause.getCause()) {
                case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK, PROJECTILE, MAGIC -> true;
                default -> false;
            };

            String weaponString = null;
            if(usedWeapon) {
                ItemStack w = killer.getInventory().getItemInMainHand();
                weaponString = w.getType() == Material.AIR ? "hand" : w.getType().toString().toLowerCase();
            }

            String entityString = killedEnt.getType().toString().toLowerCase();
            String causeString = cause.getCause().toString().toLowerCase();
            PlayerStatsManager.addKilledEntStat(killer.getUniqueId(), entityString, causeString, weaponString, cause.getFinalDamage());
        }
    }
}

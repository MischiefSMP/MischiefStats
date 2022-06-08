package com.mischiefsmp.stats.events;

import com.mischiefsmp.stats.config.PlayerStatsManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
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
                PlayerStatsManager.addSuicideStat(killer);
                return;
            }
        }

        EntityDamageEvent cause = killedEnt.getLastDamageCause();
        if(cause != null) {
            boolean usedWeapon = switch (cause.getCause()) {
                case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK, PROJECTILE, MAGIC -> true;
                default -> false;
            };

            ItemStack weapon = usedWeapon ? killer.getInventory().getItemInMainHand() : null;
            PlayerStatsManager.addKilledEntStat(killer, killedEnt, cause, weapon);
        }
    }
}

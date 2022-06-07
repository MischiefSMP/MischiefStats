package com.mischiefsmp.stats.events;

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
        EntityDamageEvent cause = killedEnt.getLastDamageCause();
        ItemStack weapon = null;
        if(cause != null) {
            double finalDamage = cause.getFinalDamage();
            if(cause.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || cause.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
                weapon = killer.getInventory().getItemInMainHand();
            }
        }

    }
}

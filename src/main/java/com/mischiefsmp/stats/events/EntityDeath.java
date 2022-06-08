package com.mischiefsmp.stats.events;

import com.mischiefsmp.stats.Utils;
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
        LivingEntity killed = event.getEntity();

        //Triggered in PlayerDeath.java
        if(killer == null || killed instanceof Player)
            return;

        EntityDamageEvent cause = killed.getLastDamageCause();
        if(cause != null)
            PlayerStatsManager.addKilledEntStat(killer, killed, cause, killer.getInventory().getItemInMainHand());
    }
}

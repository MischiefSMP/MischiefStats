package com.mischiefsmp.stats.events;

import com.mischiefsmp.stats.Utils;
import com.mischiefsmp.stats.config.PlayerStatsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killed = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if(killer == null) {
            PlayerStatsManager.addNonPlayerDeath(killed, event.getEntity().getLastDamageCause());
            return;
        }

        if(killed == killer) {
            PlayerStatsManager.addSuicideStat(killed);
            return;
        }

        EntityDamageEvent cause = killed.getLastDamageCause();
        if(cause != null) {
            ItemStack weapon = Utils.getWeapon(killer);
            PlayerStatsManager.kdEvent(killed, killer, cause, weapon);
        }
    }

}

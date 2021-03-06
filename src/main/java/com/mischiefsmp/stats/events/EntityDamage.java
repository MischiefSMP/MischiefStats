package com.mischiefsmp.stats.events;

import com.mischiefsmp.stats.Utils;
import com.mischiefsmp.stats.config.PlayerStatsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamage implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(!Utils.isPlayer(event.getDamager()))
            return;

        Player player = (Player) event.getDamager();
        double damage = event.getFinalDamage();
        ItemStack weapon = Utils.getWeapon(player);
        PlayerStatsManager.addTotalDamage(player, weapon,damage);
    }

}

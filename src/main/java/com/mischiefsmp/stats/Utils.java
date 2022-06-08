package com.mischiefsmp.stats;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Utils {
    public static boolean containsStringIgnoreCase(List<String> list, String string) {
        for(String str : list) {
            if(str.equalsIgnoreCase(string))
                return true;
        }
        return false;
    }

    public static boolean checkIfAllowedWeapon(ItemStack weapon) {
        return Utils.containsStringIgnoreCase(MischiefStats.getPluginConfig().getDisabledWeapons(), weaponToString(weapon));
    }

    public static boolean checkIfAllowedCause(EntityDamageEvent cause) {
        return Utils.containsStringIgnoreCase(MischiefStats.getPluginConfig().getDisabledCauses(), damageCauseToString(cause));
    }

    public static String livingEntityToString(LivingEntity ent) {
        return ent.getType().toString().toLowerCase();
    }

    public static String damageCauseToString(EntityDamageEvent cause) {
        return cause.getCause().toString().toLowerCase();
    }

    public static String weaponToString(ItemStack item) {
        return item.getType() == Material.AIR ? "hand" : item.getType().toString().toLowerCase();
    }
}

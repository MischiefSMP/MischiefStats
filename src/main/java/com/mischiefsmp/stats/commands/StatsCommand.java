package com.mischiefsmp.stats.commands;

import com.mischiefsmp.core.LangManager;
import com.mischiefsmp.core.cmdinfo.CMDInfo;
import com.mischiefsmp.core.utils.MCUtils;
import com.mischiefsmp.core.utils.MathUtils;
import com.mischiefsmp.stats.MischiefStats;
import com.mischiefsmp.stats.config.PlayerStats;
import com.mischiefsmp.stats.config.PlayerStatsManager;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class StatsCommand implements CommandExecutor {
    private final HashMap<String, CMDInfo> cmdInfo;
    private final LangManager lm;

    public StatsCommand() {
        cmdInfo = MischiefStats.getCmdInfoManager().getAllCMDs();
        lm = MischiefStats.getLangManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            // -> /stats
            if(!isAllowed(sender, "stats.view-self"))
                return true;

            if(sender instanceof ConsoleCommandSender) {
                sender.sendMessage(lm.getString("no-console"));
                return true;
            }

            sendInfo((Player)sender, sender);
            return true;
        }
        return true;
    }

    private void sendInfo(Player player, CommandSender requester) {
        PlayerStats stats = PlayerStatsManager.getStats(player.getUniqueId());
        if(stats == null) {
            lm.sendString(requester, "no-stats", player.getName());
            return;
        }

        double playerDeaths = stats.getPlayerDeaths();
        double kd = playerDeaths != 0 ? stats.getPlayerKills() / playerDeaths : 1;
        String kdString = lm.getString(requester, "stats-view-kd", String.valueOf(MathUtils.round(kd, 2)));
        String kdStringDetail = lm.getString(requester, "stats-view-kd-hover", String.valueOf(stats.getPlayerKills()), String.valueOf(stats.getPlayerDeaths()));

        lm.sendString(requester, "stats-view-title", player.getName());

        TextComponent kdText = new TextComponent(kdString);
        kdText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(kdStringDetail)));
        requester.spigot().sendMessage(kdText);

        lm.sendString(requester, "stats-view-deaths", String.valueOf(stats.getDeaths()));
        lm.sendString(requester, "stats-view-suicides", String.valueOf(stats.getSuicides()));
        lm.sendString(requester, "stats-view-totaldmg", String.valueOf(MathUtils.round(stats.getTotalDamage(), 2)));
        lm.sendString(requester, "stats-view-mostdmg", String.valueOf(MathUtils.round(stats.getMostDamage(), 2)));

        String mostUsedWeapon = getMostUsed(stats.getUsedWeapons());
        lm.sendString(requester, "stats-view-mostwpn", mostUsedWeapon);

        String mostUsedType = getMostUsed(stats.getUsedCauses());
        lm.sendString(requester, "stats-view-mosttype", mostUsedType);

        String mostKilledBy = getMostUsed(stats.getDeathCauses());
        lm.sendString(requester, "stats-view-mostkilledby", mostKilledBy);

        lm.sendString(requester, "stats-view-killedmobs");

        Map<String, Integer> killedMobs = stats.getKilledMobs();
        for(String mob : killedMobs.keySet()) {
            lm.sendString(requester, "stats-view-killedmob", mob, String.valueOf(killedMobs.get(mob)));
        }
    }

    private static String getMostUsed(Map<String, Integer> map) {
        String item = null;
        int used = 0;
        for(String key : map.keySet()) {
            if(map.get(key) > used) {
                item = key;
                used = map.get(key);
            }
        }
        return item;
    }

    private boolean isAllowed(CommandSender sender, String id) {
        if(MCUtils.isAllowed(sender, cmdInfo.get(id).permission())) return true;
        else sender.sendMessage(lm.getString(sender, "no-perm"));
        return false;
    }
}

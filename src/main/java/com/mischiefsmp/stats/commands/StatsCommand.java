package com.mischiefsmp.stats.commands;

import com.mischiefsmp.core.LangManager;
import com.mischiefsmp.core.cmdinfo.CMDInfo;
import com.mischiefsmp.core.utils.MCUtils;
import com.mischiefsmp.core.utils.MathUtils;
import com.mischiefsmp.stats.MischiefStats;
import com.mischiefsmp.stats.Utils;
import com.mischiefsmp.stats.config.PlayerStats;
import com.mischiefsmp.stats.config.PlayerStatsManager;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public class StatsCommand implements CommandExecutor, TabCompleter {
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

            if(!isPlayer(sender))
                return true;

            sendInfo((Player)sender, sender);
            return true;
        } else if(args.length == 1) {
            if(args[0].equals("reset")) {
                // -> /stats reset
                if(!isAllowed(sender, "stats.reset-self"))
                    return true;

                if(!isPlayer(sender))
                    return true;

                PlayerStatsManager.reset(((Player)sender).getUniqueId());
                lm.sendString(sender, "stats-reset-self");
                return true;
            } else if(args[0].equals("help")) {
                if(!isAllowed(sender, "stats.view-self"))
                    return true;

                sendHelp(sender);
                return true;
            } else {
                // -> /stats <playername>
                if(!isAllowed(sender, "stats.view-others"))
                    return true;

                String username = args[0];
                Player p = Bukkit.getPlayer(username);
                if(p != null) {
                    sendInfo(p, sender);
                    return true;
                }

                UUID pUUID = MCUtils.getUserUUID(username);
                if(pUUID != null) {
                    OfflinePlayer pOffline = Bukkit.getOfflinePlayer(pUUID);
                    sendInfo(pOffline, username, sender);
                    return true;
                }

                lm.sendString(sender, "player-nf", username);
                return true;
            }
        } else if(args.length == 2) {
            if(args[0].equals("reset")) {
                if(!isAllowed(sender, "stats.reset-others"))
                    return true;

                String username = args[1];
                Player p = Bukkit.getPlayer(username);
                UUID uuid = p != null ? p.getUniqueId() : MCUtils.getUserUUID(username);

                if(uuid == null) {
                    lm.sendString(sender, "player-nf", username);
                    return true;
                }

                String resetMsg = PlayerStatsManager.reset(uuid) ? lm.getString(sender, "stats-reset-other", username) : lm.getString(sender, "stats-reset-nf", username);
                sender.sendMessage(resetMsg);
                return true;
            }
        }

        lm.sendString(sender, "wrong-usage");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
         List<String> list = new ArrayList<>();

        if(args.length == 1) {
            if(isAllowed(sender, "stats.reset-others", false) || isAllowed(sender, "stats.reset-self", false))
                list.add("reset");

            if(isAllowed(sender, "stats.view-others", false))
                for (Player p : Bukkit.getServer().getOnlinePlayers())
                    list.add(p.getName());
        } else if(args.length == 2 && args[0].equals("reset")) {
            if(isAllowed(sender, "stats.reset-others", false))
                for (Player p : Bukkit.getServer().getOnlinePlayers())
                    list.add(p.getName());
        }

         return list;
    }

    private void sendHelp(CommandSender sender) {
        lm.sendString(sender, "help-allcmds");
        for(CMDInfo info : MischiefStats.getCmdInfoManager().getCMDHelp(sender, "stats")) {
            sender.sendMessage(info.usage());
        }
    }

    private void sendInfo(OfflinePlayer player, String playerName, CommandSender requester) {
        String name = player.getName() != null ? player.getName() : playerName;
        sendInfo(player.getUniqueId(), name, requester);
    }

    private void sendInfo(Player player, CommandSender requester) {
        sendInfo(player.getUniqueId(), player.getName(), requester);
    }

    private void sendInfo(UUID uuid, String playerName, CommandSender requester) {
        PlayerStats stats = PlayerStatsManager.getStats(uuid);
        if(stats == null) {
            lm.sendString(requester, "no-stats", playerName);
            return;
        }

        double playerDeaths = stats.getPlayerDeaths();
        double kd = playerDeaths != 0 ? stats.getPlayerKills() / playerDeaths : stats.getPlayerKills();
        String kdString = lm.getString(requester, "stats-view-kd", String.valueOf(MathUtils.round(kd, 2)));
        String kdStringDetail = lm.getString(requester, "stats-view-kd-hover", String.valueOf(stats.getPlayerKills()), String.valueOf(stats.getPlayerDeaths()));

        lm.sendString(requester, "stats-view-title", playerName);

        TextComponent kdText = new TextComponent(kdString);
        kdText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(kdStringDetail)));
        requester.spigot().sendMessage(kdText);

        lm.sendString(requester, "stats-view-deaths", String.valueOf(stats.getDeaths()));
        lm.sendString(requester, "stats-view-suicides", String.valueOf(stats.getSuicides()));
        lm.sendString(requester, "stats-view-totaldmg", String.valueOf(MathUtils.round(stats.getTotalDamage(), 2)));
        lm.sendString(requester, "stats-view-mostdmg", String.valueOf(MathUtils.round(stats.getMostDamage(), 2)));

        String mostUsedWeapon = Utils.prettyPrint(getMostUsed(stats.getUsedWeapons()));
        lm.sendString(requester, "stats-view-mostwpn", mostUsedWeapon);

        String mostUsedType = Utils.prettyPrint(getMostUsed(stats.getUsedCauses()));
        lm.sendString(requester, "stats-view-mosttype", mostUsedType);

        String mostKilledBy = Utils.prettyPrint(getMostUsed(stats.getDeathCauses()));
        lm.sendString(requester, "stats-view-mostkilledby", mostKilledBy);

        lm.sendString(requester, "stats-view-killedmobs");

        Map<String, Integer> killedMobs = stats.getKilledMobs();
        if(killedMobs.keySet().size() == 0) {
            lm.sendString(requester, "stats-view-killedmobs-none");
            return;
        }

        for(String mob : killedMobs.keySet()) {
            String mobString = Utils.prettyPrint(mob);
            lm.sendString(requester, "stats-view-killedmob", mobString, String.valueOf(killedMobs.get(mob)));
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

    private boolean isPlayer(CommandSender sender) {
        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage(lm.getString("no-console"));
            return false;
        }
        return true;
    }

    private boolean isAllowed(CommandSender sender, String id, boolean printOnDeny) {
        if(MCUtils.isAllowed(sender, cmdInfo.get(id).permission()))
            return true;
        else
            if(printOnDeny)
                sender.sendMessage(lm.getString(sender, "no-perm"));
            return false;
    }

    private boolean isAllowed(CommandSender sender, String id) {
        return isAllowed(sender, id, true);
    }
}

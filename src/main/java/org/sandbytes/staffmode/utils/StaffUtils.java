package org.sandbytes.staffmode.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.sandbytes.staffmode.StaffMode;
import org.sandbytes.staffmode.config.StaffConfig;

import java.util.*;

public class StaffUtils {

    public static final Map<Player, Location> savedLocations = new HashMap<>();
    public static final Map<Player, GameMode> savedGameModes = new HashMap<>();
    public static final Set<Player> staffModePlayers = new HashSet<>();
    public static final Map<String, CommandInfo> commands = new LinkedHashMap<>();

    private static LuckPerms luckPerms = null;

    static {
        try {
            luckPerms = LuckPermsProvider.get();
        } catch (IllegalStateException e) {
            luckPerms = null;
        }
    }

    public static void toggleStaffMode(Player player) {
        if (savedLocations.containsKey(player)) {
            player.teleport(savedLocations.remove(player));
            player.setGameMode(savedGameModes.remove(player));
            staffModePlayers.remove(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    StaffConfig.getMessage("messages.staffmode-disabled")));

            if (luckPerms != null) {
                User user = luckPerms.getUserManager().getUser(player.getUniqueId());
                if (user != null) {
                    for (String permission : StaffConfig.getAllowedPermissions()) {
                        user.data().remove(Node.builder(permission).build());
                    }
                    luckPerms.getUserManager().saveUser(user);
                }
            }

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(StaffMode.getInstance(), player);
            }
        } else {
            savedLocations.put(player, player.getLocation());
            savedGameModes.put(player, player.getGameMode());
            staffModePlayers.add(player);
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    StaffConfig.getMessage("messages.staffmode-enabled")));

            if (luckPerms != null) {
                User user = luckPerms.getUserManager().getUser(player.getUniqueId());
                if (user != null) {
                    for (String permission : StaffConfig.getAllowedPermissions()) {
                        user.data().add(Node.builder(permission).build());
                    }
                    luckPerms.getUserManager().saveUser(user);
                }
            }

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.hidePlayer(StaffMode.getInstance(), player);
            }
        }
    }

    public static void addPermission(String[] args, CommandSender player) {
        if (args.length < 3) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    StaffConfig.getMessage("messages.add-permission-usage")));
            return;
        }

        String permission = args[2];

        if (StaffConfig.getAllowedPermissions().contains(permission)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    StaffConfig.getMessage("messages.permission-exists")
                            .replace("%permission%", permission)));
        } else {
            StaffConfig.addAllowedPermission(permission);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    StaffConfig.getMessage("messages.permission-added")
                            .replace("%permission%", permission)));
        }
    }

    public static void removePermission(String[] args, CommandSender player) {
        if (args.length < 3) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    StaffConfig.getMessage("messages.remove-permission-usage")));
            return;
        }

        String permission = args[2];

        if (!StaffConfig.getAllowedPermissions().contains(permission)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    StaffConfig.getMessage("messages.permission-not-found")
                            .replace("%permission%", permission)));
        } else {
            StaffConfig.removeAllowedPermission(permission);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    StaffConfig.getMessage("messages.permission-removed")
                            .replace("%permission%", permission)));
        }
    }
}

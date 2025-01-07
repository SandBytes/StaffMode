package org.sandbytes.staffmode.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import org.sandbytes.staffmode.config.StaffConfig;
import org.sandbytes.staffmode.utils.CommandInfo;
import org.sandbytes.staffmode.utils.StaffUtils;

import java.util.*;

public class StaffCommand implements CommandExecutor {

    public static final Map<Player, Location> savedLocations = new HashMap<>();
    public static final Map<Player, GameMode> savedGameModes = new HashMap<>();
    public static final Set<Player> staffModePlayers = new HashSet<>();
    public static final Map<String, CommandInfo> commands = new LinkedHashMap<>();

    public StaffCommand() {
        commands.put("staffmode", new CommandInfo("Toggle staffmode", "staffmode.use"));
        commands.put("staffmode reload", new CommandInfo("Reload the plugin configuration", "staffmode.reload"));
        commands.put("staffmode permission add", new CommandInfo("Add a permission to staffmode", "staffmode.permission.add"));
        commands.put("staffmode permission remove", new CommandInfo("Remove a permission from staffmode", "staffmode.permission.remove"));
        commands.put("staffmode help", new CommandInfo("Display the help message", "staffmode.help"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 0 && command.getName().equalsIgnoreCase("staffmode")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        StaffConfig.getMessage("messages.only-players")));
                return true;
            }

            Player player = (Player) sender;
            if (!player.hasPermission("staffmode.use")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        StaffConfig.getMessage("messages.no-permission")
                                .replace("%permission%", "staffmode.use")));
                return true;
            }
            StaffUtils.toggleStaffMode(player);
            return true;
        }

        if (args.length == 1) {
            String subCommand = args[0].toLowerCase();

            if (subCommand.equals("reload")) {
                if (!sender.hasPermission("staffmode.reload")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            StaffConfig.getMessage("messages.no-permission")
                                    .replace("%permission%", "staffmode.reload")));
                    return true;
                }
                StaffConfig.reloadConfig();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        StaffConfig.getMessage("messages.reload-message")));
                return true;
            }

            if (subCommand.equals("help")) {
                if (!sender.hasPermission("staffmode.help")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            StaffConfig.getMessage("messages.no-permission")
                                    .replace("%permission%", "staffmode.help")));
                    return true;
                }
                showHelp(sender);
                return true;
            }

            if (subCommand.equals("permission")) {
                if (!sender.hasPermission("staffmode.permission")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            StaffConfig.getMessage("messages.no-permission")
                                    .replace("%permission%", "staffmode.permission")));
                    return true;
                }

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        StaffConfig.getMessage("messages.staffmode-permission-usage")));
                return true;
            }
        }

        if (args.length > 1 && args[0].equalsIgnoreCase("permission")) {
            if (!sender.hasPermission("staffmode.permission")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        StaffConfig.getMessage("messages.no-permission")
                                .replace("%permission%", "staffmode.permission")));
                return true;
            }

            if (args[1].equalsIgnoreCase("add")) {
                if (!sender.hasPermission("staffmode.permission.add")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            StaffConfig.getMessage("messages.no-permission")
                                    .replace("%permission%", "staffmode.permission.add")));
                    return true;
                }
                StaffUtils.addPermission(args, sender);
            } else if (args[1].equalsIgnoreCase("remove")) {
                if (!sender.hasPermission("staffmode.permission.remove")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            StaffConfig.getMessage("messages.no-permission")
                                    .replace("%permission%", "staffmode.permission.remove")));
                    return true;
                }
                StaffUtils.removePermission(args, sender);
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        StaffConfig.getMessage("messages.staffmode-permission-usage")));
            }
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                StaffConfig.getMessage("messages.unknown-command")));
        return true;
    }

    private void showHelp(CommandSender player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                StaffConfig.getMessage("messages.help-header")));

        for (Map.Entry<String, CommandInfo> entry : commands.entrySet()) {
            String command = entry.getKey();
            CommandInfo info = entry.getValue();

            if (player.hasPermission(info.getPermission())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        ChatColor.translateAlternateColorCodes('&',
                                StaffConfig.getMessage("messages.help-commands")
                        .replace("%command%", command)
                        .replace("%description%", info.getDescription()))));
            }
        }
    }
}
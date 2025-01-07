package org.sandbytes.staffmode.listeners;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.sandbytes.staffmode.config.StaffConfig;

import java.util.List;

public class CommandListener implements Listener {

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if (StaffConfig.isStaffMode(event.getPlayer())) {
            String command = event.getMessage().substring(1).split(" ")[0].toLowerCase();

            List<String> allowedPermissions = StaffConfig.getAllowedPermissions();

            boolean hasAllowedPermission = allowedPermissions.stream()
                    .anyMatch(permission -> event.getPlayer().hasPermission(permission) || hasLuckPermsPermission(event.getPlayer(), permission));

            if (!command.equalsIgnoreCase("staffmode") && !hasAllowedPermission) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        StaffConfig.getMessage("messages.command-not-allowed")));
            }
        }
    }

    private boolean hasLuckPermsPermission(Player player, String permission) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        return luckPerms.getUserManager().getUser(player.getUniqueId()).getNodes().stream()
                .map(Node::getKey)
                .anyMatch(nodePermission -> nodePermission.equalsIgnoreCase(permission));
    }
}

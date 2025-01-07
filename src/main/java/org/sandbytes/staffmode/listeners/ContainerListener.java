package org.sandbytes.staffmode.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.sandbytes.staffmode.config.StaffConfig;

public class ContainerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onContainerOpen(InventoryOpenEvent e) {
        if (StaffConfig.isStaffMode((Player) e.getPlayer())) {
            if (!e.getPlayer().hasPermission("staffmode.containers.open")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        StaffConfig.getMessage("messages.no-containers-access")));
            } else {
                e.setCancelled(false);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onContainerClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            if (StaffConfig.isStaffMode(player)) {
                if (!player.hasPermission("staffmode.containers.modify")) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            StaffConfig.getMessage("messages.no-modify-access")));
                } else {
                    e.setCancelled(false);
                }
            }
        }
    }

}

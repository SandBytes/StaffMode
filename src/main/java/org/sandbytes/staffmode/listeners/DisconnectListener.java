package org.sandbytes.staffmode.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.sandbytes.staffmode.config.StaffConfig;

public class DisconnectListener implements Listener {

    @EventHandler
    public void onLogOff(PlayerQuitEvent e) {
        if (StaffConfig.isStaffMode(e.getPlayer())) {
            e.getPlayer().teleport(StaffConfig.getSavedLocation(e.getPlayer()));
            StaffConfig.removeStaffmode(e.getPlayer());
            e.getPlayer().setGameMode(StaffConfig.getSavedGamemode(e.getPlayer()));
        }
    }
}

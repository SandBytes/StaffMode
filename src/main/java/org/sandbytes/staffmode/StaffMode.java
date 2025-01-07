package org.sandbytes.staffmode;

import org.bukkit.plugin.java.JavaPlugin;
import org.sandbytes.staffmode.commands.StaffCommand;
import org.sandbytes.staffmode.config.StaffConfig;
import org.sandbytes.staffmode.listeners.CommandListener;
import org.sandbytes.staffmode.listeners.ContainerListener;
import org.sandbytes.staffmode.listeners.DisconnectListener;

public final class StaffMode extends JavaPlugin {

    private static StaffMode instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        StaffConfig.getInstance().loadConfig();
        loadLuckPerms();
        loadCommands();
        loadEvents();

        getLogger().info("StaffMode has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("StaffMode has been disabled!");
    }

    private void loadLuckPerms() {
        if (getServer().getPluginManager().getPlugin("LuckPerms") == null) {
            getLogger().warning("LuckPerms plugin is not found! Permission features will be disabled");
        }
    }

    private void loadCommands() {
        getCommand("staffmode").setExecutor(new StaffCommand());
    }

    private void loadEvents() {
        getServer().getPluginManager().registerEvents(new CommandListener(), this);
        getServer().getPluginManager().registerEvents(new DisconnectListener(), this);
        getServer().getPluginManager().registerEvents(new ContainerListener(), this);
    }

    public static StaffMode getInstance() {
        return instance;
    }
}

package org.sandbytes.staffmode.config;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.sandbytes.staffmode.commands.StaffCommand;
import org.sandbytes.staffmode.StaffMode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class StaffConfig {

    private final static StaffConfig instance = new StaffConfig();

    private File file;
    private static YamlConfiguration config;

    public void updateConfig() {
        InputStream inputStream = StaffMode.getInstance().getResource("config.yml");
        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));

        File configFile = new File(StaffMode.getInstance().getDataFolder(), "config.yml");
        FileConfiguration currentConfig = YamlConfiguration.loadConfiguration(configFile);

        for (String key : defaultConfig.getKeys(true)) {
            if (currentConfig.contains(key)) {
                defaultConfig.set(key, currentConfig.get(key));
            }
        }

        try {
            defaultConfig.save(configFile);
        } catch (IOException e) {
            StaffMode.getInstance().getLogger().severe("Could not save updated config.yml: " + e.getMessage());
        }
    }

    public void loadConfig() {
        file = new File(StaffMode.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            StaffMode.getInstance().saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
        config.options().parseComments(true);

        InputStream defConfigStream = StaffMode.getInstance().getResource("config.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            config.setDefaults(defConfig);
            config.options().copyDefaults(true);
        }

        saveConfig();
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (Exception e) {
            StaffMode.getInstance().getLogger().severe("An error occured while saving the config file!");
        }
    }

    public static void reloadConfig() {
        StaffConfig.getInstance().loadConfig();
    }

    public static List<String> getAllowedPermissions() {
        return config.getStringList("allowed-permissions");
    }

    public static void addAllowedPermission(String command) {
        List<String> allowedPermissions = getAllowedPermissions();
        allowedPermissions.add(command);
        config.set("allowed-permissions", allowedPermissions);
        StaffConfig.getInstance().saveConfig();
    }

    public static void removeAllowedPermission(String command) {
        List<String> allowedPermissions = getAllowedPermissions();
        allowedPermissions.remove(command);
        config.set("allowed-permissions", allowedPermissions);
        StaffConfig.getInstance().saveConfig();
    }

    public static boolean isStaffMode(Player player) {
        return StaffCommand.staffModePlayers.contains(player);
    }

    public static void removeStaffmode(Player player) {
        StaffCommand.staffModePlayers.remove(player);
    }

    public static GameMode getSavedGamemode(Player player) {
        return StaffCommand.savedGameModes.get(player);
    }

    public static Location getSavedLocation(Player player) {
        return StaffCommand.savedLocations.get(player);
    }

    public static String getMessage(String message) {
        return config.getString(message);
    }

    public static StaffConfig getInstance() {
        return instance;
    }

}

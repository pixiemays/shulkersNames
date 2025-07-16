package org.pixiemays.shulkersNames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShulkerNameStorage {
    private final File file;
    private final FileConfiguration config;
    private final JavaPlugin plugin;

    public ShulkerNameStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "shulkers.yml");
        if (!file.exists()) plugin.saveResource("shulkers.yml", false);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveShulker(Location loc, String name) {
        String key = locationToKey(loc);
        config.set("shulkers." + key + ".name", name);
        save();
    }

    public void removeShulker(Location loc) {
        config.set("shulkers." + locationToKey(loc), null);
        save();
    }

    public Map<Location, String> getAllShulkers() {
        Map<Location, String> result = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("shulkers");
        if (section == null) return result;

        for (String key : section.getKeys(false)) {
            String name = section.getString(key + ".name");
            Location loc = keyToLocation(key);
            result.put(loc, name);
        }
        return result;
    }

    private String locationToKey(Location loc) {
        return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    private Location keyToLocation(String key) {
        String[] parts = key.split(",");
        World world = Bukkit.getWorld(parts[0]);
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        int z = Integer.parseInt(parts[3]);
        return new Location(world, x, y, z);
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


package org.pixiemays.shulkersNames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class ShulkersNames extends JavaPlugin {
    public static NamespacedKey ARMOR_STAND_KEY;
    private ShulkerNameStorage storage;

    public ShulkersNames() {

    }

    @Override
    public void onEnable() {
        storage = new ShulkerNameStorage(this);
        ARMOR_STAND_KEY = new NamespacedKey(this, "shulker_name");

        /*for (Map.Entry<Location, String> entry : storage.getAllShulkers().entrySet()) {
            Location loc = entry.getKey().add(0, 0.6, 0);
            String name = entry.getValue();

            ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class);
            stand.setCustomName(name);
            stand.setCustomNameVisible(true);
            stand.setInvisible(true);
            stand.setMarker(true);
            stand.setGravity(false);
            stand.getPersistentDataContainer().set(ARMOR_STAND_KEY, PersistentDataType.BYTE, (byte) 1);
        }*/

        Bukkit.getPluginManager().registerEvents(new EventListener(this, storage), this);
    }

    @Override
    public void onDisable() {

    }
}

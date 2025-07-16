package org.pixiemays.shulkersNames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class EventListener implements Listener {

    private final JavaPlugin plugin;
    private final ShulkerNameStorage shulkerNameStorage;

    public EventListener(JavaPlugin plugin, ShulkerNameStorage shulkerNameStorage) {
        this.plugin = plugin;
        this.shulkerNameStorage = shulkerNameStorage;
    }

    @EventHandler
    public void placeShulken(BlockPlaceEvent event) {

        if (!event.getBlockPlaced().getType().toString().endsWith("SHULKER_BOX")) return;

        ItemStack shulker = event.getItemInHand();
        if (!shulker.hasItemMeta()) return;

        ItemMeta shulkerMeta = shulker.getItemMeta();
        if (!shulkerMeta.hasDisplayName()) return;

        String shulkerName = shulkerMeta.getDisplayName();
        shulkerNameStorage.saveShulker(event.getBlockPlaced().getLocation(), shulkerName);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Location loc = event.getBlockPlaced().getLocation().add(0.5, 1, 0.5);

            ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class);
            stand.setCustomName(shulkerName);
            stand.setCustomNameVisible(true);
            stand.setInvisible(true);
            stand.setMarker(true);
            stand.setGravity(false);
            stand.getPersistentDataContainer().set(ShulkersNames.ARMOR_STAND_KEY, PersistentDataType.BYTE, (byte) 1);

        }, 1L);
    }

    @EventHandler
    public void breakShulker(BlockBreakEvent event) {
        if (!event.getBlock().getType().toString().endsWith("SHULKER_BOX")) return;

        Location loc = event.getBlock().getLocation();

        for (Entity e : loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 1, 0.5), 0.8, 1, 1)) {
            if (!(e instanceof ArmorStand stand)) continue;

            Byte tag = stand.getPersistentDataContainer().get(ShulkersNames.ARMOR_STAND_KEY, PersistentDataType.BYTE);
            if (tag != null && tag == 1) {
                stand.remove();
            }
        }

        shulkerNameStorage.removeShulker(loc);
    }

}

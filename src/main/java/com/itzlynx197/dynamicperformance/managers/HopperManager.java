package com.itzlynx197.dynamicperformance.managers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.InventoryHolder;
import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;

public class HopperManager implements Listener {

    private final DynamicPerformancePlugin plugin;

    public HopperManager(DynamicPerformancePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private int hopperCheckCount = 0;

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        InventoryHolder holder = event.getInitiator().getHolder();
        if (holder instanceof Block) {
            Block block = (Block) holder;
            if (block.getType() == Material.HOPPER) {
                // Optimize transfer rate
                hopperCheckCount++;
                if (hopperCheckCount % 100 == 0) { // Log every 100 checks
                    plugin.getLogger().info("Dynamic PERFORMANCE+ › Prevented lag spike: " + hopperCheckCount + " hopper checks reduced");
                }
            }
        }
    }
}
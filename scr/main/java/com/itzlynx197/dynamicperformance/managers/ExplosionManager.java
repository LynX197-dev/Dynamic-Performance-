package com.itzlynx197.dynamicperformance.managers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;

public class ExplosionManager implements Listener {

    private final DynamicPerformancePlugin plugin;

    public ExplosionManager(DynamicPerformancePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (plugin.getConfigManager().isExplosionOptimizationEnabled()) {
            // Disable particles, limit blocks
            event.setCancelled(true); // Placeholder for optimization
        }
    }
}
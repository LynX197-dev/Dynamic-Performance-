package com.itzlynx197.dynamicperformance.managers;

import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RedstoneManager implements Listener {

    private final DynamicPerformancePlugin plugin;
    private final Map<String, Integer> observerActivations = new HashMap<>();
    private final Map<String, Integer> pistonEvents = new HashMap<>();
    private int blockUpdatesThisTick = 0;
    private int currentTick = 0;

    public RedstoneManager(DynamicPerformancePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startLimiterTasks();
    }

    private void startLimiterTasks() {
        // Reset counters every second for per-second limits
        new BukkitRunnable() {
            @Override
            public void run() {
                observerActivations.clear();
                pistonEvents.clear();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second

        // Reset block updates per tick
        new BukkitRunnable() {
            @Override
            public void run() {
                blockUpdatesThisTick = 0;
                currentTick++;
            }
        }.runTaskTimer(plugin, 0L, 1L); // Every tick
    }

    @EventHandler
    public void onObserverActivate(BlockRedstoneEvent event) {
        if (!plugin.getConfigManager().isRedstoneUpdateLimiterEnabled()) return;

        // Check if it's an observer (observers trigger redstone updates)
        if (event.getBlock().getType() == Material.OBSERVER) {
            String key = event.getBlock().getWorld().getName() + ":" + event.getBlock().getX() + "," + event.getBlock().getY() + "," + event.getBlock().getZ();
            int count = observerActivations.getOrDefault(key, 0);
            if (count >= plugin.getConfigManager().getObserverActivationsPerSecond()) {
                event.setNewCurrent(0); // Cancel the activation
                return;
            }
            observerActivations.put(key, count + 1);
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if (!plugin.getConfigManager().isRedstoneUpdateLimiterEnabled()) return;

        String key = event.getBlock().getWorld().getName();
        int count = pistonEvents.getOrDefault(key, 0);
        if (count >= plugin.getConfigManager().getPistonEventsPerSecond()) {
            event.setCancelled(true);
            return;
        }
        pistonEvents.put(key, count + 1);
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (!plugin.getConfigManager().isRedstoneUpdateLimiterEnabled()) return;

        String key = event.getBlock().getWorld().getName();
        int count = pistonEvents.getOrDefault(key, 0);
        if (count >= plugin.getConfigManager().getPistonEventsPerSecond()) {
            event.setCancelled(true);
            return;
        }
        pistonEvents.put(key, count + 1);
    }

    public void minimizeRedstoneTicking() {
        // Temporarily disable redstone updates
        // This is a placeholder; actual implementation may require event cancellation or TPS-based limiting
        plugin.getLogger().info("Minimizing redstone ticking temporarily.");
    }

    // Note: Block updates per tick are harder to limit precisely without performance impact.
    // For simplicity, this is a placeholder. In a real implementation, you might need to hook into NMS or use BlockPhysicsEvent carefully.
}
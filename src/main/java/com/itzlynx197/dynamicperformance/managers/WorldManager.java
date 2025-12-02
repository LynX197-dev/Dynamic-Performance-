package com.itzlynx197.dynamicperformance.managers;

import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldManager {
    private final DynamicPerformancePlugin plugin;
    private int taskId = -1;

    public WorldManager(DynamicPerformancePlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (plugin.getConfigManager().isWorldUnloadEnabled()) {
            int intervalTicks = plugin.getConfigManager().getWorldUnloadCheckInterval() * 20 * 60; // minutes to ticks
            taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::checkAndUnloadWorlds, intervalTicks, intervalTicks);
        }
    }

    public void stop() {
        if (taskId != -1) {
            plugin.getServer().getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }

    private void checkAndUnloadWorlds() {
        for (World world : Bukkit.getWorlds()) {
            // Skip default worlds like world, world_nether, world_the_end
            if (world.getName().equals("world") || world.getName().equals("world_nether") || world.getName().equals("world_the_end")) {
                continue;
            }

            // Check conditions: no players, no loaded chunks (assuming no ticking chunks if no loaded)
            if (world.getPlayers().isEmpty() && world.getLoadedChunks().length == 0) {
                // Unload the world
                plugin.getLogger().info("Unloading idle world: " + world.getName());
                if (Bukkit.unloadWorld(world, true)) {
                    plugin.getLogger().info("Successfully unloaded world: " + world.getName());
                } else {
                    plugin.getLogger().warning("Failed to unload world: " + world.getName());
                }
            }
        }
    }
}
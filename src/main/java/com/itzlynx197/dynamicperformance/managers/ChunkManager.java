package com.itzlynx197.dynamicperformance.managers;

import org.bukkit.Chunk;
import org.bukkit.World;
import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.CompletableFuture;

public class ChunkManager {

    private final DynamicPerformancePlugin plugin;

    public ChunkManager(DynamicPerformancePlugin plugin) {
        this.plugin = plugin;
    }

    public void loadChunkAsync(Chunk chunk) {
        if (plugin.getConfigManager().isAsyncChunkLoadingEnabled()) {
            CompletableFuture.runAsync(() -> {
                // Async chunk loading logic
            });
        }
    }

    public void unloadChunks() {
        int unloaded = 0;
        for (World world : plugin.getServer().getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                if (shouldUnload(chunk)) {
                    chunk.unload();
                    unloaded++;
                }
            }
        }
        if (unloaded > 0) {
            plugin.getLogger().info("Dynamic PERFORMANCE+ › Optimized " + unloaded + " chunk sections due to TPS drop");
        }
    }

    public void unloadDeadChunks() {
        int unloaded = 0;
        for (World world : plugin.getServer().getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                if (shouldUnload(chunk)) {
                    chunk.unload();
                    unloaded++;
                }
            }
        }
        plugin.getLogger().info("Unloaded " + unloaded + " dead chunks.");
    }

    private boolean shouldUnload(Chunk chunk) {
        // Logic to determine if chunk should be unloaded
        return chunk.getEntities().length == 0; // Example
    }
}
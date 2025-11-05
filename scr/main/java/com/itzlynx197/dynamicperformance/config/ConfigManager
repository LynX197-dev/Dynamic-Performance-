package com.itzlynx197.dynamicperformance.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    // Example config sections
    public boolean isAsyncChunkLoadingEnabled() {
        return config.getBoolean("optimizations.chunks.async_loading", true);
    }

    public int getEntityCullDistance() {
        return config.getInt("optimizations.entities.cull_distance", 50);
    }

    public boolean isExplosionOptimizationEnabled() {
        return config.getBoolean("optimizations.explosions.enabled", true);
    }

    public double getTargetTPS() {
        return config.getDouble("performance.target_tps", 20.0);
    }

    // Cleaner features
    public boolean isHostileMobCleanerEnabled() {
        return config.getBoolean("cleaner.hostile_mob_cleaner.enabled", true);
    }

    public int getHostileMobCleanerInterval() {
        return config.getInt("cleaner.hostile_mob_cleaner.interval", 600);
    }

    public boolean shouldBroadcastHostileMobCleanup() {
        return config.getBoolean("cleaner.hostile_mob_cleaner.broadcast", true);
    }

    public boolean isDroppedItemCleanerEnabled() {
        return config.getBoolean("cleaner.dropped_item_cleaner.enabled", true);
    }

    public int getDroppedItemCleanerInterval() {
        return config.getInt("cleaner.dropped_item_cleaner.interval", 1200);
    }

    public boolean shouldBroadcastDroppedItemCleanup() {
        return config.getBoolean("cleaner.dropped_item_cleaner.broadcast", true);
    }

    public boolean shouldExcludeValuableItems() {
        return config.getBoolean("cleaner.dropped_item_cleaner.exclude_valuable", true);
    }

    public boolean isCountdownEnabled() {
        return config.getBoolean("cleaner.countdown.enabled", true);
    }

    public String getCountdownFormat(int seconds) {
        return config.getString("cleaner.countdown.format." + seconds, "");
    }
}

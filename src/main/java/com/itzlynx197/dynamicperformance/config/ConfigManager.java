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

    public int getMaxBlocksAffected() {
        return config.getInt("explosions.max_blocks_affected", 100);
    }

    public boolean isAutoOptimizationEnabled() {
        return config.getBoolean("auto-optimization", true);
    }



    // Boost mode settings
    public int getBoostDuration() {
        return config.getInt("boost.duration", 30);
    }

    public boolean isBoostFreezePassiveAI() {
        return config.getBoolean("boost.freeze_passive_ai", true);
    }

    public boolean isBoostSlowHoppers() {
        return config.getBoolean("boost.slow_hoppers", true);
    }

    public boolean isBoostReduceRedstone() {
        return config.getBoolean("boost.reduce_redstone", true);
    }

    public boolean isBoostPurgeEntities() {
        return config.getBoolean("boost.purge_entities", true);
    }

    public boolean isBoostCleanGC() {
        return config.getBoolean("boost.clean_gc", true);
    }

    // TNT Optimization settings
    public boolean isTntOptimizationEnabled() {
        return config.getBoolean("explosions.tnt_optimization.enabled", true);
    }

    public int getMaxTntPerBatch() {
        return config.getInt("explosions.tnt_optimization.max_tnt_per_batch", 50);
    }

    public double getPowerScalingFactor() {
        return config.getDouble("explosions.tnt_optimization.power_scaling_factor", 1.0);
    }

    public boolean isLimitBlockUpdates() {
        return config.getBoolean("explosions.tnt_optimization.limit_block_updates", true);
    }

    public boolean isSafetySystemEnabled() {
        return config.getBoolean("explosions.tnt_optimization.safety_system.enabled", true);
    }

    public boolean shouldShowSafetyMessages() {
        return config.getBoolean("explosions.tnt_optimization.safety_system.show_messages", true);
    }

    // Redstone Update Limiter settings
    public boolean isRedstoneUpdateLimiterEnabled() {
        return config.getBoolean("redstone.update_limiter.enabled", true);
    }

    public int getObserverActivationsPerSecond() {
        return config.getInt("redstone.update_limiter.observer_activations_per_second", 100);
    }

    public int getPistonEventsPerSecond() {
        return config.getInt("redstone.update_limiter.piston_events_per_second", 50);
    }

    public int getBlockUpdatesPerTick() {
        return config.getInt("redstone.update_limiter.block_updates_per_tick", 1000);
    }

    // Mob Farm Density Controller settings
    public boolean isMobFarmControllerEnabled() {
        return config.getBoolean("entities.mob_farm_controller.enabled", true);
    }

    public int getMaxMobsPerRadius() {
        return config.getInt("entities.mob_farm_controller.max_mobs_per_radius", 10);
    }

    public int getMobFarmRadius() {
        return config.getInt("entities.mob_farm_controller.radius", 7);
    }

    public String getMobFarmAction() {
        return config.getString("entities.mob_farm_controller.action", "freeze_ai");
    }

    public boolean isMobFarmDisablePathfinding() {
        return config.getBoolean("entities.mob_farm_controller.disable_pathfinding", true);
    }

    // World Unload
    public boolean isWorldUnloadEnabled() {
        return config.getBoolean("world_unload.enable", true);
    }

    public int getWorldUnloadCheckInterval() {
        return config.getInt("world_unload.check_interval_minutes", 5);
    }

    // TNT Cannon Stabilizer
    public boolean isTntCannonStabilizerEnabled() {
        return config.getBoolean("explosions.tnt_cannon_stabilizer.enabled", true);
    }

    public boolean isAsyncSimulationsEnabled() {
        return config.getBoolean("explosions.tnt_cannon_stabilizer.async_simulations", true);
    }

    public int getExplosionDelayTicks() {
        return config.getInt("explosions.tnt_cannon_stabilizer.explosion_delay_ticks", 1);
    }

    public int getMaxExplosionsPerTick() {
        return config.getInt("explosions.tnt_cannon_stabilizer.max_explosions_per_tick", 10);
    }

    // TNT Fusion settings
    public boolean isTntFusionEnabled() {
        return config.getBoolean("explosions.tnt_fusion.enabled", true);
    }

    public double getTntFusionRadius() {
        return config.getDouble("explosions.tnt_fusion.fusion_radius", 3.0);
    }

    public double getTntFusionPowerMultiplier() {
        return config.getDouble("explosions.tnt_fusion.power_multiplier", 1.0);
    }

    public boolean isTntFusionNotificationsEnabled() {
        return config.getBoolean("explosions.tnt_fusion.enable_notifications", true);
    }

    // Time Dilation Mode
    public boolean isTimeDilationEnabled() {
        return config.getBoolean("time_dilation.enabled", true);
    }

    public double getTimeDilationTpsThreshold() {
        return config.getDouble("time_dilation.tps_threshold", 15.0);
    }

    public boolean isTimeDilationSlowGrowth() {
        return config.getBoolean("time_dilation.slow_growth", true);
    }



    public boolean isTimeDilationDisableMobAI() {
        return config.getBoolean("time_dilation.disable_mob_ai", true);
    }

    // Farm Optimizer settings
    public boolean isFarmOptimizerEnabled() {
        return config.getBoolean("farm_optimizer.enabled", true);
    }

    public int getMobGrinderThreshold() {
        return config.getInt("farm_optimizer.mob_grinder_threshold", 10);
    }

    public int getVillagerBreederThreshold() {
        return config.getInt("farm_optimizer.villager_breeder_threshold", 5);
    }

    public int getCropFarmThreshold() {
        return config.getInt("farm_optimizer.crop_farm_threshold", 100);
    }

    public int getSugarcaneThreshold() {
        return config.getInt("farm_optimizer.sugarcane_threshold", 50);
    }

    public int getBambooThreshold() {
        return config.getInt("farm_optimizer.bamboo_threshold", 50);
    }

    public int getSorterSystemThreshold() {
        return config.getInt("farm_optimizer.sorter_system_threshold", 20);
    }
}
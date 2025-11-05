package com.itzlynx197.dynamicperformance;

import com.itzlynx197.dynamicperformance.commands.DPCommand;
import com.itzlynx197.dynamicperformance.config.ConfigManager;
import com.itzlynx197.dynamicperformance.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class DynamicPerformancePlugin extends JavaPlugin {

    private ConfigManager configManager;
    private ChunkManager chunkManager;
    private EntityManager entityManager;
    private ExplosionManager explosionManager;
    private HopperManager hopperManager;
    private PerformanceMonitor performanceMonitor;

    @Override
    public void onEnable() {
        // Initialize config
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        // Initialize managers
        performanceMonitor = new PerformanceMonitor(this);
        chunkManager = new ChunkManager(this);
        entityManager = new EntityManager(this);
        explosionManager = new ExplosionManager(this);
        hopperManager = new HopperManager(this);

        // Register commands
        getCommand("dp").setExecutor(new DPCommand(this));

        // Start monitoring
        performanceMonitor.startMonitoring();

        getLogger().info("Dynamic PERFORMANCE+ enabled!");
    }

    @Override
    public void onDisable() {
        if (performanceMonitor != null) {
            performanceMonitor.stopMonitoring();
        }
        if (entityManager != null) {
            entityManager.stopAutoCleaners();
        }
        getLogger().info("Dynamic PERFORMANCE+ disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ChunkManager getChunkManager() {
        return chunkManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public ExplosionManager getExplosionManager() {
        return explosionManager;
    }

    public HopperManager getHopperManager() {
        return hopperManager;
    }

    public PerformanceMonitor getPerformanceMonitor() {
        return performanceMonitor;
    }
}
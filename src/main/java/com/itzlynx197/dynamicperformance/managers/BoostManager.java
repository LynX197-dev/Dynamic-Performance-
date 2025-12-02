package com.itzlynx197.dynamicperformance.managers;

import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

public class BoostManager {

    private final DynamicPerformancePlugin plugin;
    private boolean isBoostActive = false;
    private BukkitTask revertTask;
    private Set<Entity> frozenEntities = new HashSet<>();

    public BoostManager(DynamicPerformancePlugin plugin) {
        this.plugin = plugin;
    }

    public void startBoost() {
        if (isBoostActive) {
            return; // Already active
        }
        isBoostActive = true;

        // Freeze passive AI
        if (plugin.getConfigManager().isBoostFreezePassiveAI()) {
            freezePassiveAI();
        }

        // Slow hoppers (temporarily adjust hopper settings if possible, or log)
        if (plugin.getConfigManager().isBoostSlowHoppers()) {
            // Placeholder: Assume hopper manager handles it
            plugin.getLogger().info("Dynamic PERFORMANCE+ › Slowing hoppers for boost mode");
        }

        // Reduce redstone (placeholder: throttle redstone updates)
        if (plugin.getConfigManager().isBoostReduceRedstone()) {
            plugin.getLogger().info("Dynamic PERFORMANCE+ › Reducing redstone processing for boost mode");
        }

        // Purge unused entities
        if (plugin.getConfigManager().isBoostPurgeEntities()) {
            plugin.getEntityManager().cleanupEntities();
            plugin.getLogger().info("Dynamic PERFORMANCE+ › Purged unused entities for boost mode");
        }

        // Clean GC
        if (plugin.getConfigManager().isBoostCleanGC()) {
            System.gc();
            plugin.getLogger().info("Dynamic PERFORMANCE+ › Forced garbage collection for boost mode");
        }

        // Schedule reversion
        revertTask = new BukkitRunnable() {
            @Override
            public void run() {
                revertBoost();
            }
        }.runTaskLater(plugin, plugin.getConfigManager().getBoostDuration() * 20L);
    }

    private void freezePassiveAI() {
        for (org.bukkit.World world : plugin.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (isPassive(entity.getType())) {
                    if (entity instanceof org.bukkit.entity.LivingEntity) {
                        ((org.bukkit.entity.LivingEntity) entity).setAI(false);
                        frozenEntities.add(entity);
                    }
                }
            }
        }
    }

    private boolean isPassive(EntityType type) {
        switch (type) {
            case COW:
            case PIG:
            case SHEEP:
            case CHICKEN:
            case HORSE:
            case DONKEY:
            case MULE:
            case LLAMA:
            case CAT:
            case OCELOT:
            case WOLF:
            case FOX:
            case RABBIT:
            case TURTLE:
            case VILLAGER:
            case WANDERING_TRADER:
            case BAT:
            case PARROT:
                return true;
            default:
                return false;
        }
    }

    private void revertBoost() {
        isBoostActive = false;

        // Unfreeze passive AI
        for (Entity entity : frozenEntities) {
            if (entity instanceof org.bukkit.entity.LivingEntity && entity.isValid()) {
                ((org.bukkit.entity.LivingEntity) entity).setAI(true);
            }
        }
        frozenEntities.clear();

        // Revert hopper speeds (placeholder)
        if (plugin.getConfigManager().isBoostSlowHoppers()) {
            plugin.getLogger().info("Dynamic PERFORMANCE+ › Restored hopper speeds after boost mode");
        }

        // Revert redstone (placeholder)
        if (plugin.getConfigManager().isBoostReduceRedstone()) {
            plugin.getLogger().info("Dynamic PERFORMANCE+ › Restored redstone processing after boost mode");
        }

        plugin.getLogger().info("Dynamic PERFORMANCE+ › Boost mode ended");
    }

    public boolean isBoostActive() {
        return isBoostActive;
    }
}
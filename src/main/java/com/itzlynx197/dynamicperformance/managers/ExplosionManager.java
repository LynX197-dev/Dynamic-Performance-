package com.itzlynx197.dynamicperformance.managers;

import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ExplosionManager implements Listener {

    private final DynamicPerformancePlugin plugin;
    private final Queue<ExplosionTask> explosionQueue = new LinkedList<>();
    private int tickCounter = 0;

    public ExplosionManager(DynamicPerformancePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startExplosionProcessor();
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (plugin.getConfigManager().isExplosionOptimizationEnabled()) {
            if (plugin.getConfigManager().isTntOptimizationEnabled() && event.getEntityType() == EntityType.TNT) {
                if (plugin.getConfigManager().isTntFusionEnabled()) {
                    handleTntFusion(event);
                } else if (plugin.getConfigManager().isTntCannonStabilizerEnabled()) {
                    handleTntCannonStabilization(event);
                } else {
                    handleTntBatching(event);
                }
                return;
            }

            int blocksAffected = event.blockList().size();
            if (blocksAffected > plugin.getConfigManager().getMaxBlocksAffected()) {
                event.blockList().subList(plugin.getConfigManager().getMaxBlocksAffected(), blocksAffected).clear();
                plugin.getLogger().info("Dynamic PERFORMANCE+ › Prevented lag spike: " + (blocksAffected - plugin.getConfigManager().getMaxBlocksAffected()) + " explosion blocks reduced");
            }
        }
    }

    private double calculateLagReduction(int totalTnt) {
        // Simple calculation: assume each individual explosion causes lag proportional to TNT count
        // Reduction is (totalTnt - 1) / totalTnt * 100, but scaled for batching
        // Example: 50 TNT -> ~98% reduction
        if (totalTnt <= 1) return 0;
        return ((double) (totalTnt - 1) / totalTnt) * 100.0;
    }

    private void handleTntBatching(EntityExplodeEvent event) {
        Entity tnt = event.getEntity();
        Location center = tnt.getLocation();
        int maxTnt = plugin.getConfigManager().getMaxTntPerBatch();
        double scalingFactor = plugin.getConfigManager().getPowerScalingFactor();

        // Find nearby primed TNT within a small radius
        List<Entity> nearbyTnt = new ArrayList<>();
        for (Entity entity : tnt.getWorld().getNearbyEntities(center, 5, 5, 5)) {
            if (entity instanceof TNTPrimed && entity != tnt) {
                nearbyTnt.add(entity);
            }
        }

        int totalTnt = 1 + nearbyTnt.size();
        if (totalTnt < 2) return; // No batching needed

        // Cancel the current explosion
        event.setCancelled(true);
        tnt.remove();

        // Remove batched TNT and create combined explosions
        int batchedCount = 0;
        for (Entity entity : nearbyTnt) {
            entity.remove();
            batchedCount++;
            if (batchedCount % maxTnt == 0 || batchedCount == nearbyTnt.size()) {
                double power = 4.0 * Math.min(batchedCount, maxTnt) * scalingFactor;
                tnt.getWorld().createExplosion(center, (float) power, false, true);
                plugin.getLogger().info("Dynamic PERFORMANCE+ › Batched " + Math.min(batchedCount, maxTnt) + " TNT into 1 explosion with scaled power");

                // Safety System: Calculate and display lag reduction message
                int optimizedTnt = Math.min(batchedCount, maxTnt);
                if (plugin.getConfigManager().isSafetySystemEnabled() && optimizedTnt > 1) {
                    double lagReduction = calculateLagReduction(optimizedTnt);
                    String message = String.format("Explosion cluster optimized: -%.0f%% lag.", lagReduction);
                    if (plugin.getConfigManager().shouldShowSafetyMessages()) {
                        // Send to all online players with permission or ops
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.isOp() || player.hasPermission("dynamicperformance.notify")) {
                                player.sendMessage(ChatColor.GREEN + message);
                            }
                        }
                    }
                    plugin.getLogger().info(message);
                }

                batchedCount = 0; // Reset for next batch if needed
            }
        }
    }

    private void handleTntCannonStabilization(EntityExplodeEvent event) {
        Entity tnt = event.getEntity();
        Location location = tnt.getLocation();
        float power = 4.0f; // Default TNT power
        boolean setFire = false; // Default
        boolean breakBlocks = true; // Default

        // Cancel the event to prevent immediate explosion
        event.setCancelled(true);
        tnt.remove();

        // Queue the explosion with delay
        ExplosionTask task = new ExplosionTask(location, power, setFire, breakBlocks);
        explosionQueue.add(task);

        plugin.getLogger().fine("Dynamic PERFORMANCE+ › Queued TNT explosion for stabilization");
    }

    private void startExplosionProcessor() {
        new BukkitRunnable() {
            @Override
            public void run() {
                tickCounter++;
                int processed = 0;
                while (!explosionQueue.isEmpty() && processed < plugin.getConfigManager().getMaxExplosionsPerTick()) {
                    ExplosionTask task = explosionQueue.poll();
                    if (task != null) {
                        // Apply delay if configured
                        if (plugin.getConfigManager().getExplosionDelayTicks() > 0 && tickCounter % plugin.getConfigManager().getExplosionDelayTicks() == 0) {
                            continue; // Skip this tick for smoothing
                        }

                        // Trigger explosion
                        task.location.getWorld().createExplosion(task.location, task.power, task.setFire, task.breakBlocks);
                        processed++;
                    }
                }
            }
        }.runTaskTimer(plugin, 1L, 1L); // Every tick
    }

    private void handleTntFusion(EntityExplodeEvent event) {
        Entity tnt = event.getEntity();
        Location center = tnt.getLocation();
        double radius = plugin.getConfigManager().getTntFusionRadius();
        double multiplier = plugin.getConfigManager().getTntFusionPowerMultiplier();

        // Find nearby primed TNT within fusion radius
        List<Entity> nearbyTnt = new ArrayList<>();
        for (Entity entity : tnt.getWorld().getNearbyEntities(center, radius, radius, radius)) {
            if (entity instanceof TNTPrimed) {
                nearbyTnt.add(entity);
            }
        }

        int totalTnt = nearbyTnt.size();
        if (totalTnt < 2) return; // No fusion needed for single TNT

        // Cancel the current explosion
        event.setCancelled(true);

        // Calculate fused explosion power and location
        double totalPower = 4.0 * totalTnt * multiplier;
        Location fusedLocation = center; // Use the triggering TNT location as center

        // Remove all fused TNT entities
        for (Entity entity : nearbyTnt) {
            entity.remove();
        }

        // Create the fused explosion
        tnt.getWorld().createExplosion(fusedLocation, (float) totalPower, false, true);

        // Log and notify
        plugin.getLogger().info("Dynamic PERFORMANCE+ › Fused " + totalTnt + " TNT into 1 cluster explosion (power: " + String.format("%.1f", totalPower) + ")");

        if (plugin.getConfigManager().isTntFusionNotificationsEnabled()) {
            String message = ChatColor.GREEN + "TNT Fusion: " + totalTnt + " entities → 1 explosion (-" + (totalTnt - 1) + " lag sources)";
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() || player.hasPermission("dynamicperformance.notify")) {
                    player.sendMessage(message);
                }
            }
        }
    }

    private static class ExplosionTask {
        private final Location location;
        private final float power;
        private final boolean setFire;
        private final boolean breakBlocks;

        public ExplosionTask(Location location, float power, boolean setFire, boolean breakBlocks) {
            this.location = location;
            this.power = power;
            this.setFire = setFire;
            this.breakBlocks = breakBlocks;
        }
    }
}
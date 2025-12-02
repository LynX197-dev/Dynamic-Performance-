package com.itzlynx197.dynamicperformance.managers;

import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PerformanceMonitor {

    private final DynamicPerformancePlugin plugin;
    private BukkitRunnable monitorTask;
    private boolean timeDilationActive = false;

    public PerformanceMonitor(DynamicPerformancePlugin plugin) {
        this.plugin = plugin;
    }

    public void startMonitoring() {
        monitorTask = new BukkitRunnable() {
            @Override
            public void run() {
                double tps = getTPS();
                double mspt = getMSPT();

                if (tps < plugin.getConfigManager().getTargetTPS() && plugin.getConfigManager().isAutoOptimizationEnabled()) {
                    triggerOptimizations();
                    plugin.getLogger().info("Dynamic PERFORMANCE+ › Triggered optimizations due to low TPS: " + String.format("%.2f", tps));
                }

                // Time Dilation Mode
                if (plugin.getConfigManager().isTimeDilationEnabled()) {
                    if (tps < plugin.getConfigManager().getTimeDilationTpsThreshold() && !timeDilationActive) {
                        timeDilationActive = true;
                        plugin.getTimeDilationManager().activate();
                        plugin.getLogger().info("Time Dilation Mode activated due to low TPS: " + String.format("%.2f", tps));
                    } else if (tps >= plugin.getConfigManager().getTimeDilationTpsThreshold() + 1.0 && timeDilationActive) {
                        timeDilationActive = false;
                        plugin.getTimeDilationManager().deactivate();
                        plugin.getLogger().info("Time Dilation Mode deactivated, TPS recovered: " + String.format("%.2f", tps));
                    }
                }

                // Log or broadcast if needed
            }
        };
        monitorTask.runTaskTimer(plugin, 0L, 20L); // Every second
    }

    public void stopMonitoring() {
        if (monitorTask != null) {
            monitorTask.cancel();
        }
    }

    private double getTPS() {
        // Simplified TPS calculation
        return 20.0; // Placeholder
    }

    private double getMSPT() {
        // Simplified MSPT calculation
        return 50.0; // Placeholder
    }

    private void triggerOptimizations() {
        // Trigger optimizations based on config
        plugin.getEntityManager().cleanupEntities();
        plugin.getChunkManager().unloadChunks();
    }

    public String getStats() {
        return String.format("TPS: %.2f, MSPT: %.2f", getTPS(), getMSPT());
    }
}
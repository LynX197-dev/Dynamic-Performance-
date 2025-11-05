package com.itzlynx197.dynamicperformance.managers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;

import java.util.Arrays;
import java.util.List;

public class EntityManager {

    private final DynamicPerformancePlugin plugin;
    private BukkitRunnable hostileMobCleanerTask;
    private BukkitRunnable droppedItemCleanerTask;

    // Valuable items to exclude from cleanup
    private final List<String> valuableItems = Arrays.asList("DIAMOND", "EMERALD", "GOLD_INGOT", "IRON_INGOT", "NETHERITE_INGOT");

    public EntityManager(DynamicPerformancePlugin plugin) {
        this.plugin = plugin;
        startAutoCleaners();
    }

    private void startAutoCleaners() {
        if (plugin.getConfigManager().isHostileMobCleanerEnabled()) {
            hostileMobCleanerTask = new BukkitRunnable() {
                @Override
                public void run() {
                    performHostileMobCleanup();
                }
            };
            hostileMobCleanerTask.runTaskTimer(plugin, plugin.getConfigManager().getHostileMobCleanerInterval(),
                                              plugin.getConfigManager().getHostileMobCleanerInterval());
        }

        if (plugin.getConfigManager().isDroppedItemCleanerEnabled()) {
            droppedItemCleanerTask = new BukkitRunnable() {
                @Override
                public void run() {
                    performDroppedItemCleanup();
                }
            };
            droppedItemCleanerTask.runTaskTimer(plugin, plugin.getConfigManager().getDroppedItemCleanerInterval(),
                                                plugin.getConfigManager().getDroppedItemCleanerInterval());
        }
    }

    public void stopAutoCleaners() {
        if (hostileMobCleanerTask != null) {
            hostileMobCleanerTask.cancel();
        }
        if (droppedItemCleanerTask != null) {
            droppedItemCleanerTask.cancel();
        }
    }

    private void performHostileMobCleanup() {
        if (plugin.getConfigManager().isCountdownEnabled()) {
            startCountdown(() -> {
                int removed = 0;
                for (org.bukkit.World world : plugin.getServer().getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof Monster) {
                            entity.remove();
                            removed++;
                        }
                    }
                }
                if (plugin.getConfigManager().shouldBroadcastHostileMobCleanup()) {
                    plugin.getServer().broadcastMessage(ChatColor.GREEN + "Cleaned up " + removed + " hostile mobs!");
                }
            });
        } else {
            int removed = 0;
            for (org.bukkit.World world : plugin.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof Monster) {
                        entity.remove();
                        removed++;
                    }
                }
            }
            if (plugin.getConfigManager().shouldBroadcastHostileMobCleanup()) {
                plugin.getServer().broadcastMessage(ChatColor.GREEN + "Cleaned up " + removed + " hostile mobs!");
            }
        }
    }

    private void performDroppedItemCleanup() {
        if (plugin.getConfigManager().isCountdownEnabled()) {
            startCountdown(() -> {
                int removed = 0;
                for (org.bukkit.World world : plugin.getServer().getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof Item) {
                            Item item = (Item) entity;
                            if (!shouldExcludeItem(item)) {
                                entity.remove();
                                removed++;
                            }
                        }
                    }
                }
                if (plugin.getConfigManager().shouldBroadcastDroppedItemCleanup()) {
                    plugin.getServer().broadcastMessage(ChatColor.GREEN + "Cleaned up " + removed + " dropped items!");
                }
            });
        } else {
            int removed = 0;
            for (org.bukkit.World world : plugin.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof Item) {
                        Item item = (Item) entity;
                        if (!shouldExcludeItem(item)) {
                            entity.remove();
                            removed++;
                        }
                    }
                }
            }
            if (plugin.getConfigManager().shouldBroadcastDroppedItemCleanup()) {
                plugin.getServer().broadcastMessage(ChatColor.GREEN + "Cleaned up " + removed + " dropped items!");
            }
        }
    }

    private boolean shouldExcludeItem(Item item) {
        if (!plugin.getConfigManager().shouldExcludeValuableItems()) {
            return false;
        }
        String materialName = item.getItemStack().getType().name();
        return valuableItems.contains(materialName);
    }

    private void startCountdown(Runnable cleanupAction) {
        new BukkitRunnable() {
            int countdown = 6;

            @Override
            public void run() {
                String message = plugin.getConfigManager().getCountdownFormat(countdown);
                if (!message.isEmpty()) {
                    plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
                }

                if (countdown == 0) {
                    cleanupAction.run();
                    this.cancel();
                }
                countdown--;
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second (20 ticks)
    }

    public void cleanupEntities() {
        // Legacy method, now handled by auto-cleaners
        performHostileMobCleanup();
        performDroppedItemCleanup();
    }

    public void optimizeMobs() {
        // AI throttling logic - placeholder
    }
}
package com.itzlynx197.dynamicperformance.managers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Mob;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;

import java.util.Arrays;
import java.util.List;

public class EntityManager implements Listener {

    private final DynamicPerformancePlugin plugin;
    private BukkitRunnable hostileMobCleanerTask;
    private BukkitRunnable droppedItemCleanerTask;
    private BukkitRunnable mobFarmControllerTask;

    // Valuable items to exclude from cleanup
    private final List<String> valuableItems = Arrays.asList("DIAMOND", "EMERALD", "GOLD_INGOT", "IRON_INGOT", "NETHERITE_INGOT");

    public EntityManager(DynamicPerformancePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startAutoCleaners();
        startMobFarmController();
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

    private void startMobFarmController() {
        if (plugin.getConfigManager().isMobFarmControllerEnabled()) {
            mobFarmControllerTask = new BukkitRunnable() {
                @Override
                public void run() {
                    performMobFarmDensityControl();
                }
            };
            mobFarmControllerTask.runTaskTimer(plugin, 0L, 100L); // Every 5 seconds
        }
    }

    public void stopAutoCleaners() {
        if (hostileMobCleanerTask != null) {
            hostileMobCleanerTask.cancel();
        }
        if (droppedItemCleanerTask != null) {
            droppedItemCleanerTask.cancel();
        }
        if (mobFarmControllerTask != null) {
            mobFarmControllerTask.cancel();
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

    public void clearLagSafely() {
        int removed = 0;
        for (org.bukkit.World world : plugin.getServer().getWorlds()) {
            // Remove excess dropped items
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Item) {
                    entity.remove();
                    removed++;
                }
            }
            // Add more safe clearing if needed
        }
        plugin.getLogger().info("Cleared " + removed + " dropped items for lag reduction.");
    }

    public void reduceTickingLoads() {
        // Temporarily reduce entity ticking
        // This is a placeholder; actual implementation may vary
        plugin.getLogger().info("Reducing ticking loads temporarily.");
    }

    public void slowMobAITemporarily() {
        for (org.bukkit.World world : plugin.getServer().getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if (entity instanceof Mob) {
                    ((Mob) entity).setAI(false);
                    // Schedule to re-enable after 30 seconds
                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        if (entity.isValid()) {
                            ((Mob) entity).setAI(true);
                        }
                    }, 600L); // 30 seconds
                }
            }
        }
        plugin.getLogger().info("Slowed mob AI temporarily.");
    }

    public void optimizeMobs() {
        // AI throttling logic - placeholder
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!plugin.getConfigManager().isMobFarmControllerEnabled()) return;

        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Mob)) return;

        // Check density around spawn location
        int radius = plugin.getConfigManager().getMobFarmRadius();
        int nearbyMobs = 0;
        for (Entity e : entity.getNearbyEntities(radius, radius, radius)) {
            if (e instanceof Mob) {
                nearbyMobs++;
            }
        }

        if (nearbyMobs > plugin.getConfigManager().getMaxMobsPerRadius()) {
            String action = plugin.getConfigManager().getMobFarmAction();
            switch (action.toLowerCase()) {
                case "freeze_ai":
                    ((Mob) entity).setAI(false);
                    break;
                case "kill_extras":
                    entity.remove();
                    break;
                case "disable_pathfinding":
                    // Disable pathfinding by setting AI false (simplified)
                    ((Mob) entity).setAI(false);
                    break;
            }
        }
    }

    private void performMobFarmDensityControl() {
        if (!plugin.getConfigManager().isMobFarmControllerEnabled()) return;

        for (org.bukkit.World world : plugin.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof Mob)) continue;

                Mob mob = (Mob) entity;
                int radius = plugin.getConfigManager().getMobFarmRadius();
                int nearbyMobs = 0;
                for (Entity e : mob.getNearbyEntities(radius, radius, radius)) {
                    if (e instanceof Mob) {
                        nearbyMobs++;
                    }
                }

                if (nearbyMobs > plugin.getConfigManager().getMaxMobsPerRadius()) {
                    if (plugin.getConfigManager().isMobFarmDisablePathfinding()) {
                        mob.setAI(false);
                    }
                    // Additional actions can be added here
                } else {
                    // Re-enable AI if density is normal (optional)
                    if (!mob.hasAI()) {
                        mob.setAI(true);
                    }
                }
            }
        }
    }
}
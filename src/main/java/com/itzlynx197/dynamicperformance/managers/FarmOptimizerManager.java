package com.itzlynx197.dynamicperformance.managers;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;

import java.util.HashMap;
import java.util.Map;

public class FarmOptimizerManager implements Listener {

    private final DynamicPerformancePlugin plugin;

    public FarmOptimizerManager(DynamicPerformancePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!plugin.getConfigManager().isFarmOptimizerEnabled()) return;

        Chunk chunk = event.getChunk();

        // Count entities
        Map<String, Integer> entityCounts = new HashMap<>();
        for (Entity entity : chunk.getEntities()) {
            String type = entity.getType().name();
            entityCounts.put(type, entityCounts.getOrDefault(type, 0) + 1);
        }

        int monsterCount = 0;
        for (String key : entityCounts.keySet()) {
            if (key.contains("ZOMBIE") || key.contains("SKELETON") || key.contains("CREEPER") || key.contains("SPIDER") || key.contains("ENDERMAN") || key.contains("BLAZE") || key.contains("WITHER_SKELETON")) {
                monsterCount += entityCounts.get(key);
            }
        }

        if (monsterCount > plugin.getConfigManager().getMobGrinderThreshold()) {
            // Mob grinder: disable AI for monsters
            for (Entity entity : chunk.getEntities()) {
                if (entity instanceof Monster) {
                    ((Monster) entity).setAI(false);
                }
            }
            plugin.getLogger().info("Optimized mob grinder in chunk " + chunk.getX() + "," + chunk.getZ());
        }

        int villagerCount = entityCounts.getOrDefault("VILLAGER", 0);
        int ironGolemCount = entityCounts.getOrDefault("IRON_GOLEM", 0);

        if (villagerCount > plugin.getConfigManager().getVillagerBreederThreshold() || ironGolemCount > 0) {
            // Iron farm or villager breeder: disable AI for villagers and golems
            for (Entity entity : chunk.getEntities()) {
                if (entity instanceof Villager) {
                    ((Villager) entity).setAI(false);
                } else if (entity instanceof IronGolem) {
                    ((IronGolem) entity).setAI(false);
                }
            }
            plugin.getLogger().info("Optimized iron farm/villager breeder in chunk " + chunk.getX() + "," + chunk.getZ());
        }

        // Detect crop farm
        int cropCount = 0;
        for (int x = 0; x < 16; x += 4) {
            for (int z = 0; z < 16; z += 4) {
                for (int y = 60; y <= 80; y += 10) {
                    Block block = chunk.getBlock(x, y, z);
                    Material mat = block.getType();
                    if (mat == Material.WHEAT || mat == Material.CARROTS || mat == Material.POTATOES || mat == Material.BEETROOTS) {
                        cropCount++;
                    }
                }
            }
        }
        if (cropCount > plugin.getConfigManager().getCropFarmThreshold()) {
            plugin.getLogger().info("Detected crop farm in chunk " + chunk.getX() + "," + chunk.getZ());
        }

        // Sugarcane farm
        int sugarcaneCount = 0;
        for (int x = 0; x < 16; x += 4) {
            for (int z = 0; z < 16; z += 4) {
                for (int y = 60; y <= 80; y += 10) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block.getType() == Material.SUGAR_CANE) {
                        sugarcaneCount++;
                    }
                }
            }
        }
        if (sugarcaneCount > plugin.getConfigManager().getSugarcaneThreshold()) {
            plugin.getLogger().info("Detected sugarcane farm in chunk " + chunk.getX() + "," + chunk.getZ());
        }

        // Bamboo farm
        int bambooCount = 0;
        for (int x = 0; x < 16; x += 4) {
            for (int z = 0; z < 16; z += 4) {
                for (int y = 60; y <= 80; y += 10) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block.getType() == Material.BAMBOO) {
                        bambooCount++;
                    }
                }
            }
        }
        if (bambooCount > plugin.getConfigManager().getBambooThreshold()) {
            plugin.getLogger().info("Detected bamboo farm in chunk " + chunk.getX() + "," + chunk.getZ());
        }

        // Sorter system: count hoppers
        int hopperCount = 0;
        for (int x = 0; x < 16; x += 4) {
            for (int z = 0; z < 16; z += 4) {
                for (int y = 0; y < 256; y += 10) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block.getType() == Material.HOPPER) {
                        hopperCount++;
                    }
                }
            }
        }
        if (hopperCount > plugin.getConfigManager().getSorterSystemThreshold()) {
            plugin.getLogger().info("Detected sorter system in chunk " + chunk.getX() + "," + chunk.getZ());
        }
    }
}
package com.itzlynx197.dynamicperformance.managers;

import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.HashMap;
import java.util.Map;

public class TimeDilationManager {

    private final DynamicPerformancePlugin plugin;
    private boolean active = false;
    private final Map<World, Integer> originalRandomSpeeds = new HashMap<>();

    public TimeDilationManager(DynamicPerformancePlugin plugin) {
        this.plugin = plugin;
    }

    public void activate() {
        if (active) return;
        active = true;
        for (World world : Bukkit.getWorlds()) {
            if (plugin.getConfigManager().isTimeDilationSlowGrowth()) {
                originalRandomSpeeds.put(world, world.getGameRuleValue(GameRule.RANDOM_TICK_SPEED));
                world.setGameRule(GameRule.RANDOM_TICK_SPEED, 6); // Slower growth
            }
            if (plugin.getConfigManager().isTimeDilationDisableMobAI()) {
                for (LivingEntity entity : world.getLivingEntities()) {
                    if (entity instanceof Mob) {
                        ((Mob) entity).setAI(false);
                    }
                }
            }
        }
    }

    public void deactivate() {
        if (!active) return;
        active = false;
        for (World world : originalRandomSpeeds.keySet()) {
            world.setGameRule(GameRule.RANDOM_TICK_SPEED, originalRandomSpeeds.get(world));
        }
        originalRandomSpeeds.clear();
        if (plugin.getConfigManager().isTimeDilationDisableMobAI()) {
            for (World world : Bukkit.getWorlds()) {
                for (LivingEntity entity : world.getLivingEntities()) {
                    if (entity instanceof Mob) {
                        ((Mob) entity).setAI(true);
                    }
                }
            }
        }
    }

    public boolean isActive() {
        return active;
    }
}
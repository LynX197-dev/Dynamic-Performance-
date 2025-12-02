package com.itzlynx197.dynamicperformance.commands;

import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

public class DPCommand implements CommandExecutor {

    private final DynamicPerformancePlugin plugin;

    public DPCommand(DynamicPerformancePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("dynamicperformance.admin")) {
            sender.sendMessage("No permission.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /dp <optimize|entities|chunks|stats|reload|boost>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "optimize":
                performOptimization(sender);
                break;
            case "entities":
                plugin.getEntityManager().cleanupEntities();
                sender.sendMessage("Entities cleaned up.");
                break;
            case "chunks":
                plugin.getChunkManager().unloadChunks();
                sender.sendMessage("Chunks managed.");
                break;
            case "stats":
                sender.sendMessage(plugin.getPerformanceMonitor().getStats());
                break;
            case "reload":
                plugin.getConfigManager().reloadConfig();
                sender.sendMessage("Config reloaded.");
                break;
            case "boost":
                if (plugin.getBoostManager().isBoostActive()) {
                    sender.sendMessage("Boost mode is already active.");
                } else {
                    plugin.getBoostManager().startBoost();
                    sender.sendMessage("Boost mode activated for " + plugin.getConfigManager().getBoostDuration() + " seconds.");
                }
                break;
            default:
                sender.sendMessage("Unknown subcommand.");
        }

        return true;
    }

    private void performOptimization(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Applying one-click optimizations...");

        // Clear lag safely
        plugin.getEntityManager().clearLagSafely();

        // Unload dead chunks
        plugin.getChunkManager().unloadDeadChunks();

        // Reduce ticking loads
        plugin.getEntityManager().reduceTickingLoads();

        // Minimize redstone ticking
        plugin.getRedstoneManager().minimizeRedstoneTicking();

        // Slow mob AI temporarily
        plugin.getEntityManager().slowMobAITemporarily();

        sender.sendMessage(ChatColor.GREEN + "Optimizations applied! Server performance should improve.");
    }
}
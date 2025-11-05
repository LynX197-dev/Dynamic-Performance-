package com.itzlynx197.dynamicperformance.commands;

import com.itzlynx197.dynamicperformance.DynamicPerformancePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
            sender.sendMessage("Usage: /dp <optimize|entities|chunks|stats|reload>");
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
            default:
                sender.sendMessage("Unknown subcommand.");
        }

        return true;
    }

    private void performOptimization(CommandSender sender) {
        // Full optimization routine
        sender.sendMessage("Performing full optimization...");
    }
}

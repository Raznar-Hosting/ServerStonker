package id.raznar.serveroptimizer;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class WorldSaver {
    public void schedule(Main plugin) {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ConsoleCommandSender console = plugin.getServer().getConsoleSender();
            plugin.getLogger().info(Utils.color("&aSaving all game progress.."));
            Bukkit.dispatchCommand(console, "save-all");
        }, 0, 10 * 1200);
    }
}

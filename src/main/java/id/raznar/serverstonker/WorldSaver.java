package id.raznar.serverstonker;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class WorldSaver {
    private final Main plugin;

    public WorldSaver(Main plugin) {
        this.plugin = plugin;
    }
    int Delay = Config.get().getInt("auto-save");
    public void schedule() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ConsoleCommandSender console = plugin.getServer().getConsoleSender();
            plugin.getLogger().info(Utils.color("&aSaving all game progress.."));
            Bukkit.dispatchCommand(console, "save-all");
        }, 0, Delay * 1200);
    }
}

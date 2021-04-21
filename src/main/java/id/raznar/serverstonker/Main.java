package id.raznar.serverstonker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {
    public static Main plugin;
    @Override
    public void onEnable() {
        plugin = this;
        this.loadConfig();
        this.registerCommand();
        this.loadListener();
        this.getLogger().info("--------------------------------");
        this.getLogger().info("Server Stonker made by Raznar Lab Successfully loaded!");
        this.getLogger().info("Try our hosting! https://hosting.raznar.id/discord");
        this.getLogger().info("--------------------------------");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("--------------------------------");
        this.getLogger().info("Server Stonker made by Raznar Lab Successfully unloaded!");
        this.getLogger().info("Try our hosting! https://hosting.raznar.id/discord");
        this.getLogger().info("--------------------------------");
    }
    private void loadListener() {
        PluginManager load = this.getServer().getPluginManager();
        // Listeners
        load.registerEvents(new EntityListener(this), this);

        // Schedules
        EntityListener entityListener = new EntityListener(this);
        entityListener.entitySchedule();

    }
    private void loadConfig() {
        Config.setup();
        saveDefaultConfig();
    }
    private void registerCommand() {
        Bukkit.getPluginCommand("serverstonker").setExecutor(new Commands());
    }
}

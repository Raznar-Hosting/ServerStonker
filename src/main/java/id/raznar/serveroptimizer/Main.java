package id.raznar.serveroptimizer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        this.loadConfig();
        this.loadListener();
        this.getLogger().info("--------------------------------");
        this.getLogger().info("Server Optimizer made by Raznar Lab Successfully loaded!");
        this.getLogger().info("Try our hosting! https://hosting.raznar.id/discord");
        this.getLogger().info("--------------------------------");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("--------------------------------");
        this.getLogger().info("Server Optimizer made by Raznar Lab Successfully unloaded!");
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

        WorldSaver worldSaver = new WorldSaver(this);
        worldSaver.schedule();
    }
    private void loadConfig() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        Config.setup();
        Config.get().options().copyDefaults(true);
        Config.save();
    }
}

package id.raznar.mobdespawner;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic]
        this.getLogger().info("--------------------------------");
        this.getLogger().info("Loading Events..");
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("Mob Despawner made by Raznar Lab Successfully loaded!");
        this.getLogger().info("Try our hosting! https://raznar.id");
        this.getLogger().info("--------------------------------");
        new BukkitRunnable() {
            public void run() {
                for(World world: getServer().getWorlds()) {
                    for(Entity entity: world.getEntities()) {
                        if(entity == null)
                            return;
                        if(!entity.isCustomNameVisible()) {
                            if (entity instanceof Wolf || entity instanceof Horse) {
                                if (!((Tameable) entity).isTamed()) {
                                    if (!(entity instanceof Villager)) {
                                        for (Entity nearby : entity.getNearbyEntities(2d, 2d, 2d)) {
                                            if (nearby instanceof Player)
                                                return;
                                            Chunk chunk = entity.getLocation().getChunk();
                                            if (chunk.isLoaded()) {
                                                entity.remove();
                                                chunk.unload(true);
                                            } else {
                                                entity.remove();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0, 200);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info("--------------------------------");
        this.getLogger().info("Disabling Events..");
        this.getLogger().info("Mob Despawner made by Raznar Lab Successfully unloaded!");
        this.getLogger().info("Try our hosting! https://raznar.id");
        this.getLogger().info("--------------------------------");
    }
    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        Entity entity = e.getEntity();
        if(entity.isCustomNameVisible()) {
            if (entity instanceof Wolf || entity instanceof Horse) {
                if (!((Tameable) entity).isTamed()) {
                    if (!(entity instanceof Villager)) {
                        e.setCancelled(true);
                    }
                } else {
                    ((Tameable) entity).setAI(true);
                }
            }
        } else {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.001);
            }
        }
    }
}

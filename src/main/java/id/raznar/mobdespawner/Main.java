package id.raznar.mobdespawner;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getLogger().info("--------------------------------");
        this.getLogger().info("Loading Events..");
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("Mob Despawner made by Raznar Lab Successfully loaded!");
        this.getLogger().info("Try our hosting! https://raznar.id");
        this.getLogger().info("--------------------------------");

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (World world : this.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof Wolf || entity instanceof Horse) {
                        // only removes wild wolfs and horses
                        Tameable tameable = (Tameable) entity;
                        if (!tameable.isTamed()) {
                            this.removeEntity(tameable);
                        }
                    } else if (entity instanceof Villager) {
                        return;
                    } else {
                        this.removeEntity(entity);
                    }
                }
            }
        }, 0, 200);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("--------------------------------");
        this.getLogger().info("Disabling Events..");
        this.getLogger().info("Mob Despawner made by Raznar Lab Successfully unloaded!");
        this.getLogger().info("Try our hosting! https://raznar.id");
        this.getLogger().info("--------------------------------");
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.0000000001);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        Entity entity = e.getEntity();
        boolean nearbyPlayer = entity.getNearbyEntities(48, 48, 48).stream().anyMatch(nearby -> nearby instanceof Player);
        CreatureSpawnEvent.SpawnReason spawnReason = e.getSpawnReason();
        if(spawnReason == CreatureSpawnEvent.SpawnReason.NATURAL)
            if(!entity.isCustomNameVisible() && !nearbyPlayer)
                e.setCancelled(true);
    }
    private void removeEntity(Entity entity) {
        // let's not remove entities if it is near a player
        boolean nearbyPlayer = entity.getNearbyEntities(48, 48, 48).stream().anyMatch(nearby -> nearby instanceof Player);
        if (!nearbyPlayer) {
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

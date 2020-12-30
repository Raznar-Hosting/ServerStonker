package id.raznar.serveroptimizer;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityListener implements Listener {
    private final Main plugin;
    public EntityListener(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Scheduler
     */
    public void entitySchedule() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (World world : plugin.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof Player) {
                        return;
                    } else if (entity instanceof Wolf || entity instanceof Horse) {
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
        }, 0, 10 * 20);
    }

    /**
     * Attributes changes
     *
     */
    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        Entity entity = e.getEntity();
        // Cancel if it's a custom mob
        if(entity.isCustomNameVisible()) {
            return;
        }
        // Disable Mob AI by changing their movement speed
        if (entity instanceof LivingEntity) {
            if(entity instanceof Wolf || entity instanceof Horse) {
                Tameable tameable = (Tameable) entity;
                if (!tameable.isTamed())
                    ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
            } else {
                ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
            }
        }
    }

    /**
     *
     * Despawn Creature
     */
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        // Disable Mobs Spawn naturally
        if (!e.getEntity().isCustomNameVisible()) {
            if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CHUNK_GEN || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL)
                e.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        // Disable Mob to interact
        if(e.getEntity().isCustomNameVisible()) {
            return;
        }
        if(e.getTarget() instanceof LivingEntity) {
            e.setCancelled(true);
        }
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

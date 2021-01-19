package id.raznar.serverstonker;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.List;

public class EntityListener implements Listener {
    private final Main plugin;
    public EntityListener(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Scheduler
     */
    public void entitySchedule() {
        int Delay = Config.get().getInt("schedule-duration");
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if(Config.get().getString("schedule-log").equals("true")) {
                plugin.getLogger().info(Utils.color("&bExecuting scheduled repeating task"));
            }
            for (World world : plugin.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    EntityType type = entity.getType();
                    boolean entityTamedList = Config.get().getStringList("whitelist-tamed-mob").stream().map(EntityType::valueOf).anyMatch(type::equals);
                    boolean entityList = Config.get().getStringList("whitelist-mob").stream().map(EntityType::valueOf).anyMatch(type::equals);
                    boolean worlds = Config.get().getStringList("worlds").contains(entity.getWorld().getName());
                    if (worlds) {
                        if (entityTamedList) {
                            Tameable tameable = (Tameable) entity;
                            if (!tameable.isTamed()) {
                                removeEntity(tameable);
                            }
                        } else if (!entityList) {
                            removeEntity(entity);
                        }
                    }
                }
            }
        }, 0, Delay * 20);
    }

    /**
     * Attributes changes
     *
     */
    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        if(Config.get().getBoolean("mob-movement", true)) {
            Entity entity = e.getEntity();
            EntityType type = entity.getType();
            // Cancel if it's a custom mob
            if (entity.isCustomNameVisible()) {
                return;
            }
            // Disable Mob AI by changing their movement speed
            if (entity instanceof LivingEntity) {
                boolean entityTamedList = Config.get().getStringList("whitelist-tamed-mob-ai").stream().map(EntityType::valueOf).anyMatch(type::equals);
                boolean entityList = Config.get().getStringList("whitelist-mob-ai").stream().map(EntityType::valueOf).anyMatch(type::equals);
                if (entityTamedList) {
                    Tameable tameable = (Tameable) entity;
                    if (!tameable.isTamed())
                        ((LivingEntity) tameable).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
                } else if (!entityList) {
                    ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0);
                }
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
        Entity entity = e.getEntity();
        if (!entity.isCustomNameVisible()) {
            EntityType type = e.getEntityType();
            boolean entityList = Config.get().getStringList("whitelist-mob").stream().map(EntityType::valueOf).anyMatch(type::equals);
            if(entityList) {
                return;
            }
            if (Config.get().getBoolean("natural-spawn", true)) {
                if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL)
                    e.setCancelled(true);
            }
            if (Config.get().getBoolean("chunk-spawn", true)) {
                if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CHUNK_GEN)
                    e.setCancelled(true);
            }
            if (Config.get().getBoolean("spawner-spawn", true)) {
                if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER)
                    e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        Entity entity = e.getEntity();
        Entity target = e.getTarget();
        EntityType type = entity.getType();
        // Disable Mob to interact
        if(entity.isCustomNameVisible()) {
            return;
        }
        if(target instanceof LivingEntity) {
            boolean whitelistMob = Config.get().getStringList("whitelist-mob-ai").stream().map(EntityType::valueOf).anyMatch(type::equals);
            boolean whitelistTamedMob = Config.get().getStringList("whitelist-tamed-mob-ai").stream().map(EntityType::valueOf).anyMatch(type::equals);
            if(!whitelistMob)
                e.setCancelled(true);
            else if(whitelistTamedMob) {
                if(!((Tameable) entity).isTamed())
                    e.setCancelled(true);
            }
        }
    }

    public void removeEntity(Entity entity) {
        // let's not remove entities if it is near a player
        double radius = Config.get().getInt("block-radius");
        List<Entity> nearbyEntity = entity.getNearbyEntities(radius, radius, radius);
        if (nearbyEntity instanceof Player || entity.isCustomNameVisible() || entity instanceof Player) {
            return;
        }
        Chunk chunk = entity.getLocation().getChunk();
        entity.remove();
        if(Config.get().getBoolean("unload-chunk", true)) {
            if (chunk.isLoaded()) {
                chunk.unload();
            }
        }
    }
}

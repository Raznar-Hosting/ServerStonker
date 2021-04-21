package id.raznar.serverstonker;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;

import java.util.List;

class EntityUtils {

    public void clearEntityList() {
        for (World world : Bukkit.getServer().getWorlds()) {
            for (org.bukkit.entity.Entity entity : world.getEntities()) {
                boolean worlds = Config.get().getStringList("worlds").contains(entity.getWorld().getName());
                if (worlds) {
                    if (!(entity instanceof LivingEntity)) {
                        removeEntity(entity);
                    } else {
                        removeEntityListAI((LivingEntity) entity);
                    }
                }
            }
        }
    }
    public void removeEntity(Entity entity) {
        EntityType entityType = entity.getType();
        boolean entityType2 = Config.get().getStringList("mob-list").stream().map(EntityType::valueOf).anyMatch(entityType::equals);
        boolean tamedEntity = Config.get().getBoolean("mob-list." + entityType.name() + ".tamed");
        if(!entityType2) {
            if(tamedEntity) {
                Tameable tameable = (Tameable) entity;
                boolean tamed = tameable.isTamed();
                if(!tamed) {
                    removeEntities(entity);
                }
            } else {
                removeEntities(entity);
            }
        }
    }
    public void removeEntities(Entity entity) {
        double radius = Config.get().getInt("block-radius");
        List<org.bukkit.entity.Entity> nearbyEntity = entity.getNearbyEntities(radius, radius, radius);
        if (nearbyEntity instanceof Player || entity.isCustomNameVisible() || entity instanceof Player) {
            return;
        }
        Chunk chunk = entity.getLocation().getChunk();
        entity.remove();
        Bukkit.getLogger().info("Removed" + entity.getName());
        if (Config.get().getBoolean("unload-chunk")) {
            if (chunk.isLoaded()) {
                chunk.unload();
            }
        }
    }
    public void removeEntityListAI(LivingEntity entity) {
        EntityType entityType = entity.getType();
        boolean entityType2 = Config.get().getStringList("mob-list").stream().map(EntityType::valueOf).anyMatch(entityType::equals);
        boolean entityAI2 = Config.get().getBoolean("mob-list." + entityType.name() + ".ai");
        boolean tamedEntity = Config.get().getBoolean("mob-list." + entityType.name() + ".tamed");
        if(entityType2 && entityAI2 && !tamedEntity) {
            Bukkit.getLogger().info("Removed AI from" + entity.getName());
            entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.00000001);
            entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0);
        } else if(entityType2 && entityAI2) {
            Bukkit.getLogger().info("Removed AI from" + entity.getName());
            Tameable tameable = (Tameable) entity;
            boolean tamed = tameable.isTamed();
            if(!tamed) {
                entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.000000001);
                entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0);
            }
        }
    }
}

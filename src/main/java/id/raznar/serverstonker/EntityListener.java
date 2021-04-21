package id.raznar.serverstonker;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;


public class EntityListener extends EntityUtils implements Listener {
    private final Main plugin;
    public EntityListener(Main plugin) {
        this.plugin = plugin;
    }
    /**
     * Scheduler
     */
    public void entitySchedule() {
        int Delay = Config.get().getInt("schedule.duration");
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if(Config.get().getBoolean("schedule.log")) {
                plugin.getLogger().info(Utils.colorize("&bExecuting scheduled repeating task"));
            }
            this.clearEntityList();

        }, 0, Delay * 20);
    }

    /**
     * Attributes changes
     *
     */
    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        if(e.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) e.getEntity();
            removeEntityListAI(entity);
        }
    }

    /**
     *
     * Despawn Creature
     */
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        EntityType type = e.getEntityType();
        boolean entityList = Config.get().getStringList("mob-list").stream().map(EntityType::valueOf).anyMatch(type::equals);
        if (entityList && !e.getEntity().isCustomNameVisible()) {
            return;
        }
        if (Config.get().getBoolean("spawn.natural")) {
            if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL)
                e.setCancelled(true);
        }
        if (Config.get().getBoolean("spawn.chunk")) {
            if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CHUNK_GEN)
                e.setCancelled(true);
        }
        if (Config.get().getBoolean("spawn.spawner")) {
            if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER)
                e.setCancelled(true);
        }
    }
}

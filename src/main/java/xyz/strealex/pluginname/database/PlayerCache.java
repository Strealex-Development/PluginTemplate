package xyz.strealex.pluginname.database;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalNotification;
import org.bukkit.Bukkit;
import xyz.strealex.pluginname.models.DatabasePlayer;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

public class PlayerCache {

    private final Cache<UUID, DatabasePlayer> cache = CacheBuilder.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(2))
        .removalListener(this::handlePlayerRemoval)
        .build();

    private void handlePlayerRemoval(RemovalNotification<UUID, DatabasePlayer> notification) {
        if (notification.getCause() == RemovalCause.EXPIRED) {
            UUID key = notification.getKey();

            if (key == null) {
                return;
            }

            // Check if the user is still online
            if (Bukkit.getPlayer(key) != null && notification.getValue() != null) {
                // Still online, validate them in the cache
                cache.put(key, notification.getValue());
            }
        }
    }

    public DatabasePlayer get(UUID uuid) {
        return cache.getIfPresent(uuid);
    }

    public void put(UUID uuid, DatabasePlayer player) {
        cache.put(uuid, player);
    }

    public void remove(UUID uuid) {
        cache.invalidate(uuid);
    }

    public void clear() {
        cache.invalidateAll();
    }

    public Map<UUID, DatabasePlayer> getAll() {
        return cache.asMap();
    }
}

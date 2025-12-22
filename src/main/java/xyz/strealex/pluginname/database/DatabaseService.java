package xyz.strealex.pluginname.database;

import lombok.Getter;
import xyz.strealex.pluginname.PluginName;
import xyz.strealex.pluginname.config.ConfigManager;
import xyz.strealex.pluginname.database.impl.MySQL;
import xyz.strealex.pluginname.database.impl.SQLite;
import xyz.strealex.pluginname.models.DatabasePlayer;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseService {

    private final PluginName plugin;
    private final ConfigManager config;
    @Getter
    private DatabaseProvider provider;
    @Getter
    private Database database;
    @Getter
    private PlayerCache cache;

    public DatabaseService(PluginName plugin, ConfigManager config) {
        this.plugin = plugin;
        this.config = config;
        reload();
    }

    private Database getImplementation(DatabaseProvider provider) {
        DatabaseType type = provider.getType();

        try {
            return switch (type) {
                case MYSQL -> new MySQL(provider);
                case SQLITE -> new SQLite(provider);
            };
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create database implementation", e);
            return null;
        }
    }

    public void reload() {
        try {
            if (provider != null) {
                provider.close();
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error closing previous database provider", e);
        }

        try {
            this.provider = new DatabaseProvider(plugin, config);
            this.database = getImplementation(provider);

            if (database == null) {
                plugin.getLogger().severe("Database implementation could not be created.");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                return;
            }

            database.initialize();

            this.cache = new PlayerCache();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize database (" + provider.getType() + ")", e);
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Unexpected error during database reload", e);
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public DatabasePlayer getPlayer(UUID uuid) {
        try {
            DatabasePlayer player = cache.get(uuid);
            if (player == null) {
                player = database.getDatabasePlayer(uuid.toString());
                if (player == null) {
                    player = new DatabasePlayer(uuid);
                    database.createDatabasePlayer(player);
                }
                player.markClean();
                cache.put(uuid, player);
            }
            return player;
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load player " + uuid, e);
            return null;
        }
    }

    public void savePlayer(UUID uuid) {
        DatabasePlayer player = cache.get(uuid);
        if (player == null) {
            plugin.getLogger().warning("Tried to save player " + uuid + " but they were not in cache.");
            return;
        }

        if (player.isDirty()) {
            try {
                database.updateDatabasePlayer(player);
                player.markClean();
            } catch (SQLException e) {
                plugin.getLogger().severe("Failed to save player " + uuid);
            }
        }
    }

    public int saveAllPlayers() {
        int count = 0;
        for (Map.Entry<UUID, DatabasePlayer> entry : cache.getAll().entrySet()) {
            if (entry.getValue().isDirty()) {
                savePlayer(entry.getKey());
                count++;
            }
        }
        return count;
    }

    public void removePlayerFromCache(UUID uuid) {
        cache.remove(uuid);
    }


    public void clearCache() {
        cache.clear();
    }
}

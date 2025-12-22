package xyz.strealex.pluginname;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.strealex.pluginname.commands.TestCommand;
import xyz.strealex.pluginname.config.ConfigManager;
import xyz.strealex.pluginname.database.DatabaseService;
import xyz.strealex.pluginname.listeners.TestListener;

import java.util.Optional;

public final class PluginName extends JavaPlugin {

    @Getter
    private static PluginName INSTANCE;
    public ConfigManager configManager;
    public DatabaseService databaseService;

    @Override
    public void onEnable() {
        INSTANCE = this;
        configManager = new ConfigManager(this);

        if (!loadConfig()) {
            INSTANCE.getServer().getPluginManager().disablePlugin(INSTANCE);
            return;
        }

        if (!initDatabaseService()) {
            INSTANCE.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling plugin...");
        if (databaseService != null) {
            getLogger().info("Disabling database...");
            long start = System.currentTimeMillis();
            int saved = databaseService.saveAllPlayers();
            long duration = System.currentTimeMillis() - start;

            if (saved != 0) {
                getLogger().info("Saved " + saved + " dirty player(s) in " + duration + "ms.");
            }

            databaseService.clearCache();
        }
    }

    private void registerCommands() {
        new TestCommand();
    }

    private void registerListeners() {
        new TestListener(INSTANCE).register();
    }

    /**
     * Loads the plugin configuration.
     *
     * @return true if the configuration was loaded successfully, false otherwise.
     */
    private boolean loadConfig() {
        final Optional<Throwable> error = configManager.loadConfig();
        if (error.isPresent()) {
            INSTANCE.getLogger().log(java.util.logging.Level.SEVERE, "Failed to load configuration", error.get());
            return false;
        }
        return true;
    }

    private boolean initDatabaseService() {
        try {
            databaseService = new DatabaseService(INSTANCE, configManager);
            return true;
        } catch (Exception e) {
            getLogger().severe("Failed to initialize database service: " + e.getMessage());
            return false;
        }
    }
}

package xyz.strealex.pluginname;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.strealex.pluginname.commands.TestCommand;
import xyz.strealex.pluginname.config.ConfigManager;

import java.util.Optional;

public final class PluginName extends JavaPlugin {

    @Getter
    private static PluginName instance;
    public ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);

        if (!loadConfig()) {
            instance.getServer().getPluginManager().disablePlugin(instance);
            return;
        }

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling plugin...");
    }

    private void registerCommands() {
        new TestCommand();
    }

    private void registerListeners() {

    }

    /**
     * Loads the plugin configuration.
     *
     * @return true if the configuration was loaded successfully, false otherwise.
     */
    private boolean loadConfig() {
        final Optional<Throwable> error = configManager.loadConfig();
        if (error.isPresent()) {
            instance.getLogger().log(java.util.logging.Level.SEVERE, "Failed to load configuration", error.get());
            return false;
        }
        return true;
    }
}

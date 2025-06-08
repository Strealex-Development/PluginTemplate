package xyz.strealex.pluginname.listeners;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.strealex.pluginname.PluginName;

public class TestListener implements Listener {

    private final PluginName plugin;

    public TestListener(PluginName plugin) {
        this.plugin = plugin;
    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(MiniMessage.miniMessage().deserialize("<yellow>" + event.getPlayer().getName() + " left the game."));
    }
}
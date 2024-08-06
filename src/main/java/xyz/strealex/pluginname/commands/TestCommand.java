package xyz.strealex.pluginname.commands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TestCommand {

    private void registerTestCommand() {
        new CommandAPICommand("repair")
                .withRequirement(sender -> ((Player) sender).getLevel() >= 30)
                .executesPlayer((player, args) -> {

                    // Repair the item back to full durability
                    ItemStack is = player.getInventory().getItemInMainHand();
                    ItemMeta itemMeta = is.getItemMeta();
                    if (itemMeta instanceof Damageable damageable) {
                        damageable.damage(0);
                        is.setItemMeta(itemMeta);
                    }

                    // Subtract 30 levels
                    player.setLevel(player.getLevel() - 30);
                })
                .register();

    }
}

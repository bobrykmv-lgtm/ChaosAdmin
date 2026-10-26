package com.chaossmp.chaosadmin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class ChaosAdmin extends JavaPlugin implements Listener {

```
private static final String MENU_TITLE = ChatColor.DARK_RED + "ChaosAdmin";

@Override
public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
    getLogger().info("ChaosAdmin включён!");
}

@Override
public boolean onCommand(
        CommandSender sender,
        Command command,
        String label,
        String[] args
) {
    if (!command.getName().equalsIgnoreCase("admin")) {
        return false;
    }

    if (!(sender instanceof Player player)) {
        sender.sendMessage("Эту команду может использовать только игрок.");
        return true;
    }

    if (!player.isOp()) {
        player.sendMessage(ChatColor.RED + "У тебя нет доступа к этой панели.");
        return true;
    }

    openAdminMenu(player);
    return true;
}

private void openAdminMenu(Player player) {
    Inventory inventory = Bukkit.createInventory(null, 27, MENU_TITLE);

    inventory.setItem(10, createItem(
            Material.PLAYER_HEAD,
            ChatColor.YELLOW + "Игроки",
            "Просмотр игроков онлайн"
    ));

    inventory.setItem(12, createItem(
            Material.COMPASS,
            ChatColor.GREEN + "Телепортация",
            "Телепортация к игрокам"
    ));

    inventory.setItem(14, createItem(
            Material.CLOCK,
            ChatColor.AQUA + "Время",
            "Установить день"
    ));

    inventory.setItem(16, createItem(
            Material.WEATHERED_COPPER,
            ChatColor.BLUE + "Погода",
            "Изменить погоду"
    ));

    inventory.setItem(22, createItem(
            Material.BARRIER,
            ChatColor.RED + "Закрыть",
            "Закрыть админ-панель"
    ));

    player.openInventory(inventory);
}

private ItemStack createItem(Material material, String name, String lore) {
    ItemStack item = new ItemStack(material);
    ItemMeta meta = item.getItemMeta();

    if (meta != null) {
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(ChatColor.GRAY + lore));
        item.setItemMeta(meta);
    }

    return item;
}

@EventHandler
public void onMenuClick(InventoryClickEvent event) {
    if (!MENU_TITLE.equals(event.getView().getTitle())) {
        return;
    }

    event.setCancelled(true);

    if (!(event.getWhoClicked() instanceof Player player)) {
        return;
    }

    if (!player.isOp()) {
        player.closeInventory();
        return;
    }

    switch (event.getRawSlot()) {
        case 10 -> {
            player.sendMessage(ChatColor.GOLD + "Игроков онлайн: "
                    + Bukkit.getOnlinePlayers().size());

            for (Player online : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.YELLOW + "- " + online.getName());
            }
        }
        case 12 -> player.sendMessage(
                ChatColor.GREEN + "Для телепортации используй /tp <игрок>"
        );
        case 14 -> {
            player.getWorld().setTime(1000);
            player.sendMessage(ChatColor.AQUA + "Установлен день.");
        }
        case 16 -> {
            boolean storm = !player.getWorld().hasStorm();
            player.getWorld().setStorm(storm);
            player.sendMessage(ChatColor.BLUE
                    + (storm ? "Дождь включён." : "Дождь выключен."));
        }
        case 22 -> player.closeInventory();
        default -> {
        }
    }
}
```

}

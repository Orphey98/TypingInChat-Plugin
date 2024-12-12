package me.orphey.areyoutypingplugin;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 1 && strings[0].equalsIgnoreCase("reload")) {
            cmdReload((Player) commandSender);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1)
            return List.of("reload");
        return new ArrayList<>();
    }

    public static void cmdReload(Player player) {
        Config.getInstance().load(); // Update data from config.yml
        player.sendMessage("AreYouTyping Config reloaded");
    }
}

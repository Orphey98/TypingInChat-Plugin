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
            cmdReload(commandSender);
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

    public static void cmdReload(CommandSender sender) {
        if (sender instanceof Player player) {
            if (AreYouTypingPlugin.perms.has(player, "ayt.admin")) {
                ConfigLoader.getInstance().load(); // Update data from config.yml
                sender.sendMessage("AreYouTyping Config reloaded");
            } else {
                sender.sendMessage("You do not have ayt.admin permission");
            }
        } else {
            ConfigLoader.getInstance().load();
            AreYouTypingPlugin.getInstance().getPluginLogger().info("Configuration reloaded");
        }
    }
}

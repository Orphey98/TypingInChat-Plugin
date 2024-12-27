package me.orphey.typinginchat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {

        if (strings.length == 1 && strings[0].equalsIgnoreCase("reload")) {
            cmdReload(commandSender);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (strings.length == 1)
            return List.of("reload");
        return new ArrayList<>();
    }

    public static void cmdReload(CommandSender sender) {
        if (sender instanceof Player player) {
            if (TypingInChat.checkPermission(player, "tic.admin")) {
                try {
                    ConfigLoader.getInstance().load(); // Update data from config.yml
                    Holograms.getHologramAPI().removeAll();
                    sender.sendMessage(ChatColor.YELLOW + "[TypingInChat] Configuration reloaded");
                    TypingInChat.getInstance().getPluginLogger().info("Configuration reloaded.");
                } catch (FileNotFoundException e) {
                    ConfigLoader.createConfig();
                }
                catch (IOException | InvalidConfigurationException e) {
                    sender.sendMessage(ChatColor.RED + "Error while TypingInChat Config reloading. Check console.");
                    TypingInChat.getInstance().getPluginLogger().severe("config.yml reading error: " + e.getMessage());
                }
            } else {
                sender.sendMessage("You do not have tic.admin permission");
                TypingInChat.getInstance().getPluginLogger().warning("Not enough permission for command.");
            }
        } else {
            try {
                ConfigLoader.getInstance().load();
                Holograms.getHologramAPI().removeAll();
                TypingInChat.getInstance().getPluginLogger().info("Configuration reloaded");
            } catch (IOException | InvalidConfigurationException e) {
                TypingInChat.getInstance().getPluginLogger().severe("config.yml reading error." + e.getMessage());
            }
        }
    }
}

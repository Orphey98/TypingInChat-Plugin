package me.orphey.typinginchat;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.maximde.hologramlib.HologramLib;
import com.maximde.hologramlib.hologram.HologramManager;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;


public final class TypingInChat extends JavaPlugin {

    private final Logger logger = getLogger(); // Create a Logger instance
    private HologramManager hologramManager;
    private static boolean vaultInit = false;

    @Override
    public void onEnable() {
        // VAULT INIT -------------------------------------------------
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            setupPermissions();
            logger.info("VaultAPI hooked. Use ayt.display permission to control holograms spawn.");
            vaultInit = true;
        }
        // END VAULT INIT

        // PACKET EVENTS
        PacketEvents.getAPI().getEventManager().registerListener(
                new PacketEventsListener(), PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().init();
        // END PACKET EVENTS

        // HOLOGRAM API
        hologramManager = HologramLib.getManager().orElse(null);
        if (hologramManager == null) {
            getLogger().severe("Failed to initialize HologramAPI manager.");
            return;
        }
        // END HOLOGRAM API

        // COMMAND
        Objects.requireNonNull(getCommand("typinginchat")).setExecutor(new Command());

        // CONFIGURATION YAML
        try {
            ConfigLoader.getInstance().load();
        } catch (FileNotFoundException e) {
            ConfigLoader.createConfig();
        }
        catch (IOException | InvalidConfigurationException e) {
            TypingInChat.getInstance().getPluginLogger().severe("config.yml reading error: " + e.getMessage());
        }
    }

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }

    // VAULT INITIALIZATION -----------------------------------------------------------------------------------
    @SuppressWarnings("FieldCanBeLocal")
    public static Permission perms = null;
    @SuppressWarnings({"DataFlowIssue", "ConstantValue", "UnusedReturnValue"})
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    // END OF VAULT INITIALIZATION ------------------------------------------------------------------------------

    public HologramManager getHologramManager() {
        return hologramManager;
    }

    public static boolean checkPermission(Player player, String node) {
        if (vaultInit) {
            return TypingInChat.perms.has(player, node);
        } else {
            return player.hasPermission(node);
        }
    }

    public static TypingInChat getInstance() {
        return getPlugin(TypingInChat.class);
    }
    public Logger getPluginLogger() {
        return logger;
    }
}

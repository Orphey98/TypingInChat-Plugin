package me.orphey.areyoutypingplugin;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.maximde.hologramapi.HologramAPI;
import com.maximde.hologramapi.hologram.HologramManager;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;


public final class AreYouTypingPlugin extends JavaPlugin {

    private final Logger logger = getLogger(); // Create a Logger instance
    private HologramManager hologramManager;

    @Override
    public void onEnable() {
        // VAULT INIT -------------------------------------------------
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            setupPermissions();
            logger.info("VaultAPI detected. Use permission ayt.display to control holograms spawn.");
        }
        // END VAULT INIT

        // PACKET EVENTS
        PacketEvents.getAPI().getEventManager().registerListener(
                new PacketEventsListener(), PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().init();
        // END PACKET EVENTS

        // HOLOGRAM API
        hologramManager = HologramAPI.getManager().orElse(null);
        if (hologramManager == null) {
            getLogger().severe("Failed to initialize HologramAPI manager.");
            return;
        }
        // END HOLOGRAM API

        // COMMAND
        Objects.requireNonNull(getCommand("areyoutyping")).setExecutor(new Command());

        // CONFIGURATION YAML
        try {
            ConfigLoader.getInstance().load();
        } catch (FileNotFoundException e) {
            ConfigLoader.createConfig();
        }
        catch (IOException | InvalidConfigurationException e) {
            AreYouTypingPlugin.getInstance().getPluginLogger().severe("config.yml reading error: " + e.getMessage());
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

    public static AreYouTypingPlugin getInstance() {
        return getPlugin(AreYouTypingPlugin.class);
    }
    public Logger getPluginLogger() {
        return logger;
    }
}

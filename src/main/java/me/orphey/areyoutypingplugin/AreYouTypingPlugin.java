package me.orphey.areyoutypingplugin;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;


public final class AreYouTypingPlugin extends JavaPlugin {

    private final Logger logger = getLogger(); // Create a Logger instance

    @Override
    public void onEnable() {
        // VAULT INIT -------------------------------------------------
        setupPermissions();
        // END VAULT INIT
        // PACKET EVENTS
        PacketEvents.getAPI().getEventManager().registerListener(
                new PacketEventsListener(), PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().init();
        // END PACKET EVENTS

        getCommand("areyoutyping").setExecutor(new Command());
        Config.getInstance().load(); // Load data from config.yml
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

    public static AreYouTypingPlugin getInstance() {
        return getPlugin(AreYouTypingPlugin.class);
    }
    public Logger getPluginLogger() {
        return logger;
    }
}

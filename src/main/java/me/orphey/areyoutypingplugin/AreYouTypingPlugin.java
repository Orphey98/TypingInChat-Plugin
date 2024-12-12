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
//        if (!setupEconomy() ) {
//            getLogger().severe("Disabled due to no Vault dependency found!");
//            getServer().getPluginManager().disablePlugin(this);
//            return;
//        }
        setupPermissions();
        //setupChat();
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
    private static Economy econ = null;
    @SuppressWarnings("FieldCanBeLocal")
    private static Permission perms = null;
    @SuppressWarnings("FieldCanBeLocal")
    private static Chat chat = null;
    @SuppressWarnings("ConstantValue")
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public static Economy getEconomy() {
        return econ;
    }
    @SuppressWarnings({"unused", "DataFlowIssue", "ConstantValue"})
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }
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

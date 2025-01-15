package me.orphey.typinginchat;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import org.bukkit.entity.Player;

public class PacketEventsListener implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = event.getPlayer();
        if (player == null || !player.isOnline()) {
            return;
        }
        if (!TypingInChat.checkPermission(player, "tic.display")) {
            if (Holograms.getHologramAPI().getHologramsMap().containsKey(player.getUniqueId().toString())) {
                Holograms.remove(player);
            }
            return;
        }
        WrapperPlayClientPluginMessage packet = verifyPacket(event);
        if (packet != null) {
            manageHolo(player, readPacket(packet));
        }
    }

    private WrapperPlayClientPluginMessage verifyPacket(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE) {
            WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(event);
            String channel = packet.getChannelName(); // Get the channel name

            if (channel.equals("typinginchatmod:typing_status")) {
                return packet;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private byte readPacket(WrapperPlayClientPluginMessage packet) {
        byte[] data = packet.getData();

        if (data.length == 2) { //Forge
            return data[1];
        } else if (data.length == 1) { //Fabric
            return data[0];
        } else {
            return 0;
        }


    }

    private void manageHolo(Player player, byte b) {
        if (b == 1) {
            if (!Holograms.getHologramAPI().getHologramsMap().containsKey(player.getUniqueId().toString())) {
                Holograms.create(player);
            }
        } else {
            Holograms.remove(player);
        }
    }
}

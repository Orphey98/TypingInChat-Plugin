package me.orphey.typinginchat;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import org.bukkit.entity.Player;

import static me.orphey.typinginchat.Holograms.handlePassengers;

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
            //System.out.println("Received packet " + channel);
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
        //System.out.println("Data Length: " + data.length);
        if (data.length == 2) { //Forge
            //System.out.println("Forge " + data[1]);
            return data[1];
        } else if (data.length == 1) { //Fabric
            //System.out.println("Fabric " + data[0]);
            return data[0];
        } else {
            //System.out.println("Empty");
            return 0;
        }


    }

    private void manageHolo(Player player, byte b) {
        if (b == 1) {
            if (!Holograms.getHologramAPI().getHologramsMap().containsKey(player.getUniqueId().toString())) {
                if (!player.getPassengers().isEmpty()) {
                    handlePassengers(player);
                } else {
                    Holograms.create(player, player);
                }
            }
        } else {
            Holograms.remove(player);
        }
    }
}

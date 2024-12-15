package me.orphey.areyoutypingplugin;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import com.maximde.hologramapi.HologramAPI;
import com.maximde.hologramapi.hologram.TextAnimation;
import com.maximde.hologramapi.hologram.TextHologram;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Holograms {

    public static void create(Player player) {
        Vector offset = Config.getTranslation();
        int[] background = backgroundColor();

        String[] frames = animationFrame(player);
        String name = textBuilder(player);
        TextHologram hologram = new TextHologram(player.getUniqueId().toString())
                .setMiniMessageText(name + frames[0])
                .setSeeThroughBlocks(Config.isVisibleThroughBlocks())
                .setBillboard(Display.Billboard.CENTER)
                .setShadow(Config.isTextShadow())
                .setViewRange(Config.getViewRange())
                .setBackgroundColor(Color.fromARGB(background[0], background[1], background[2], background[3]).asARGB())
                .setTranslation((float)offset.getX(), (float)offset.getY(), (float)offset.getZ());

        TextAnimation animation = new TextAnimation()
                .addFrame( name + frames[1])
                .addFrame( name + frames[2])
                .addFrame( name + frames[3])
                .setSpeed(20 / 2);

        HologramAPI.getHologram().spawn(hologram, player.getLocation().add(Config.getLocation()));
        HologramAPI.getHologram().applyAnimation(hologram, animation);
        HologramAPI.getHologram().attach(hologram, player.getEntityId());
    }

    private static String textBuilder(Player player) {
        String playerName = "";
        String indentation = "";
        if (Config.isShowNames()) {
            playerName = "<color:" + Config.getNamesColor() + ">" + player.getName();
            if (Config.isIndentation()) {
                indentation = "\n";
            } else {
                playerName += " ";
            }
        }
        return playerName + indentation;
    }
    //TODO check StringBuilder
    private static String[] animationFrame(Player player) {
        String[] strings = new String[4];
        String typingChar = Config.getTypingChar();
        String typingColor = "<color:" + Config.getTypingIconColor() + ">";
        strings[0] = typingColor + "[" + typingChar + "]";
        strings[1] = typingColor + "[" + typingChar + ".]";
        strings[2] = typingColor + "[" + typingChar + "..]";
        strings[3] = typingColor + "[" + typingChar + "...]";
        return strings;
    }

    private static int[] backgroundColor() {
        String hex = Config.getBackgroundColor().substring(1);
        int[] array = new int[4];
        array[0] = Config.getBackgroundTransparency(); //A
        array[1] = Integer.parseInt(hex.substring(0, 2), 16); //R
        array[2] = Integer.parseInt(hex.substring(2, 4), 16); //G
        array[3] = Integer.parseInt(hex.substring(4, 6), 16); //B
        return array;
    }

    public static void remove(Player player) {
        TextHologram hologram = HologramAPI.getHologram().getHologramsMap().get(player.getUniqueId().toString());
        if (hologram != null) {
            HologramAPI.getHologram().cancelAnimation(hologram);
            HologramAPI.getHologram().remove(player.getUniqueId().toString());
        }
    }
}

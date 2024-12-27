package me.orphey.typinginchat;

import com.maximde.hologramlib.hologram.HologramManager;
import com.maximde.hologramlib.hologram.TextHologram;
import com.maximde.hologramlib.hologram.TextAnimation;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Holograms {
    private Holograms() {}

    public static HologramManager getHologramAPI() {
        return TypingInChat.getInstance().getHologramManager();
    }
    public static void create(Player player) {
        Vector offset = ConfigLoader.getTranslation();
        int[] background = backgroundColor();

        String[] frames = animationFrame();
        String name = textBuilder(player);
        TextHologram hologram = new TextHologram(player.getUniqueId().toString())
                .setMiniMessageText(name + frames[0])
                .setSeeThroughBlocks(ConfigLoader.isVisibleThroughBlocks())
                .setBillboard(Display.Billboard.CENTER)
                .setShadow(ConfigLoader.isTextShadow())
                .setViewRange(ConfigLoader.getViewRange())
                .setBackgroundColor(Color.fromARGB(background[0], background[1], background[2], background[3]).asARGB())
                .setTranslation((float)offset.getX(), (float)offset.getY(), (float)offset.getZ());

        TextAnimation animation = new TextAnimation()
                .addFrame( name + frames[1])
                .addFrame( name + frames[2])
                .addFrame( name + frames[3])
                .setSpeed(20 / 2);

        getHologramAPI().spawn(hologram, player.getLocation().add(ConfigLoader.getLocation()));
        getHologramAPI().applyAnimation(hologram, animation);
        getHologramAPI().attach(hologram, player.getEntityId());
    }

    private static String textBuilder(Player player) {
        String playerName = "";
        String indentation = "";
        if (ConfigLoader.isShowNames()) {
            playerName = "<color:" + ConfigLoader.getNamesColor() + ">" + player.getName();
            if (ConfigLoader.isIndentation()) {
                indentation = "\n";
            } else {
                playerName += " ";
            }
        }
        return playerName + indentation;
    }

    private static String[] animationFrame() {
        String[] strings = new String[4];
        String typingChar = ConfigLoader.getTypingChar();
        String typingColor = "<color:" + ConfigLoader.getTypingIconColor() + ">";
        strings[0] = typingColor + "[" + typingChar + "]";
        strings[1] = typingColor + "[" + typingChar + ".]";
        strings[2] = typingColor + "[" + typingChar + "..]";
        strings[3] = typingColor + "[" + typingChar + "...]";
        return strings;
    }

    private static int[] backgroundColor() {
        String hex = ConfigLoader.getBackgroundColor().substring(1);
        int[] array = new int[4];
        array[0] = ConfigLoader.getBackgroundTransparency(); //A
        array[1] = Integer.parseInt(hex.substring(0, 2), 16); //R
        array[2] = Integer.parseInt(hex.substring(2, 4), 16); //G
        array[3] = Integer.parseInt(hex.substring(4, 6), 16); //B
        return array;
    }

    public static void remove(Player player) {
        TextHologram hologram = (TextHologram) getHologramAPI().getHologramsMap().get(player.getUniqueId().toString());
        if (hologram != null) {
            getHologramAPI().cancelAnimation(hologram);
            getHologramAPI().remove(player.getUniqueId().toString());
        }
    }
}

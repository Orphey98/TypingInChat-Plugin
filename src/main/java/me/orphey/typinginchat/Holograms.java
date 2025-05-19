package me.orphey.typinginchat;

import com.maximde.hologramlib.hologram.HologramManager;
import com.maximde.hologramlib.hologram.TextHologram;
import com.maximde.hologramlib.hologram.TextAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public class Holograms {
    private Holograms() {}
    private static final Set<UUID> holosActive = new HashSet<>();
    private static final HashMap<Player, Entity> holosQueue = new HashMap<>();

    public static HologramManager getHologramAPI() {
        return TypingInChat.getInstance().getHologramManager();
    }

    public static void create(Player player, Entity entity) {
        if (holoExist(player)) {
            holosQueue.put(player, entity);
            return;
        }

        Vector offset = ConfigLoader.getTranslation();
        int[] background = backgroundColor();

        String[] frames = animationFrame();
        String name = textBuilder(player);
        TextHologram hologram = new TextHologram(player.getUniqueId().toString())
                .setSeeThroughBlocks(ConfigLoader.isVisibleThroughBlocks())
                .setBillboard(Display.Billboard.CENTER)
                .setShadow(ConfigLoader.isTextShadow())
                .setViewRange(ConfigLoader.getViewRange())
                .setBackgroundColor(Color.fromARGB(background[0], background[1], background[2], background[3]).asARGB())
                .setTranslation((float)offset.getX(), (float)offset.getY(), (float)offset.getZ());

        // Smooth display in motion
        Bukkit.getScheduler().runTaskLater(TypingInChat.getInstance(), () -> {
            hologram.setMiniMessageText(name + frames[0]);
        }, 1L);

        TextAnimation animation = new TextAnimation()
                .addFrame( name + frames[1])
                .addFrame( name + frames[2])
                .addFrame( name + frames[3])
                .setSpeed(20 / 2);

        getHologramAPI().spawn(hologram, entity.getLocation().add(ConfigLoader.getLocation()));
        getHologramAPI().applyAnimation(hologram, animation);
        getHologramAPI().attach(hologram, entity.getEntityId());
        holosActive.add(player.getUniqueId());
    }

    public static void handlePassengers(Player player) {
        Entity current = player;
        while (!current.getPassengers().isEmpty()) {
            current = current.getPassengers().get(0); // Assumes single passenger chain
        }
        create(player, current);
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
            holosActive.remove(player.getUniqueId());
            holoQueue();
        }
    }

    private static boolean holoExist(Player player) {
        Set<UUID> visited = new HashSet<>();
        Entity current = player;
        while (current.getVehicle() != null) {
            current = current.getVehicle(); // Assumes single vehicle chain
            if (!visited.add(current.getUniqueId())) break; // Prevent infinite loop
            if (holosActive.contains(current.getUniqueId())) {
                return true;
            }
        }
        current = player;
        while (!current.getPassengers().isEmpty()) {
            current = current.getPassengers().get(0); // Assumes single passenger chain
            if (!visited.add(current.getUniqueId())) break; // Prevent infinite loop
            if (holosActive.contains(current.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    private static void holoQueue() {
        if (holosQueue.isEmpty()) return;
        Iterator<Map.Entry<Player, Entity>> iterator = holosQueue.entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry<Player, Entity> entry = iterator.next();
            Player player = entry.getKey();
            Entity target = entry.getValue();
            create(player, target);
            iterator.remove();
        }
    }
}

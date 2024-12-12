package me.orphey.areyoutypingplugin;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.List;

public class Config {
    private Config() {
    }
    private static final Config instance = new Config();
    private static YamlConfiguration config;
    private static List<Double> location;
    private static List<Float> translation;
    private static boolean showNames;
    private static boolean isIndentation;
    private static int viewRange;
    private static boolean visibleThroughBlocks;
    private static String typingChar;
    private static String backgroundColor;
    private static int backgroundTransparency;
    private static String namesColor;
    private static String typingIconColor;
    private static boolean textShadow;

    public static Vector getLocation() {
        double x = location.get(0);
        double y = location.get(1);
        double z = location.get(2);
        return new Vector(x, y, z);
    }
    public static Vector getTranslation() {
        float x = translation.get(0);
        float y = translation.get(1);
        float z = translation.get(2);
        return new Vector(x, y, z);
    }
    public static boolean isShowNames() {
        return showNames;
    }
    public static boolean isIndentation() {
        return isIndentation;
    }
    public static int getViewRange() {
        return viewRange;
    }
    public static boolean isVisibleThroughBlocks() {
        return visibleThroughBlocks;
    }
    public static String getTypingChar() {
        return typingChar;
    }
    public static boolean isTextShadow() {
        return textShadow;
    }

    public static String getNamesColor() {
        return namesColor;
    }
    public static String getTypingIconColor() {
        return typingIconColor;
    }
    public static String getBackgroundColor() {
        return backgroundColor;
    }
    public static int getBackgroundTransparency() {
        return backgroundTransparency;
    }

    public void load() {
        File file = new File(AreYouTypingPlugin.getInstance().getDataFolder(), "config.yml");
        if (!file.exists())
            AreYouTypingPlugin.getInstance().saveResource("config.yml", false);
        config = new YamlConfiguration();
        try {
            config.options().parseComments(true);
        } catch (Exception e) {
            //No catch yet
        }
        try {
            config.load(file);
            loadOptions();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void loadOptions() {
        location = config.getDoubleList("location");
        translation = config.getFloatList("transformation");
        showNames = config.getBoolean("show-names");
        isIndentation = config.getBoolean("icon-indentation");
        viewRange = config.getInt("view-range");
        visibleThroughBlocks = config.getBoolean("visible-through-blocks");
        typingChar = config.getString("typing-char");
        textShadow = config.getBoolean("text-shadow");
        namesColor = config.getString("names-color");
        typingIconColor = config.getString("typing-icon-color");
        backgroundColor = config.getString("background-color");
        backgroundTransparency = config.getInt("background-transparency");
    }

    public static Config getInstance() {
        return instance;
    }
}

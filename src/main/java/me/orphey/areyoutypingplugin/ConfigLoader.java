package me.orphey.areyoutypingplugin;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Map.entry;

public class ConfigLoader {
    private ConfigLoader() {
    }
    private static final ConfigLoader instance = new ConfigLoader();
    private static final YamlConfiguration config = new YamlConfiguration();
    private static final Map<String, Boolean> booleanParameters = new HashMap<>(Map.ofEntries(
            entry("show-names", true),
            entry("icon-indentation", true),
            entry("visible-through-blocks", false),
            entry("text-shadow", false)
    ));
    private static List<Double> location;
    private static List<Float> translation;
    private static int viewRange;
    private static String typingChar;
    private static String backgroundColor;
    private static int backgroundTransparency;
    private static String namesColor;
    private static String typingIconColor;

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
        return booleanParameters.get("show-names");
    }
    public static boolean isIndentation() {
        return booleanParameters.get("icon-indentation");
    }
    public static int getViewRange() {
        return viewRange;
    }
    public static boolean isVisibleThroughBlocks() {
        return booleanParameters.get("visible-through-blocks");
    }
    public static String getTypingChar() {
        return typingChar;
    }
    public static boolean isTextShadow() {
        return booleanParameters.get("text-shadow");
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

    public void load() throws InvalidConfigurationException, IOException {
        File file = new File(AreYouTypingPlugin.getInstance().getDataFolder(), "config.yml");
        config.load(file);
        loadOptions();
    }

    // TODO validate config params
    // Data types:
    // boolean
    // view-range: int from 0 to 20
    // typing-char: string, max length 16 chars
    // hex color
    // background-transparency: int from 0 to 255

    private static void validateBoolean() {
        for (String parameter : booleanParameters.keySet()) {
            if (config.isBoolean(parameter)) {
                booleanParameters.put(parameter, config.getBoolean(parameter));
            } else {
                logger.warning(String.format("Can't load %s parameter from config. Using default value.", parameter));
            }
        }
    }

    private static void loadOptions() {
        validateBoolean();
        location = config.getDoubleList("location");
        translation = config.getFloatList("transformation");

        typingChar = config.getString("typing-char");
        namesColor = config.getString("names-color");
        typingIconColor = config.getString("typing-icon-color");
        backgroundColor = config.getString("background-color");
        backgroundTransparency = config.getInt("background-transparency");
    }

    private static final Logger logger = AreYouTypingPlugin.getInstance().getPluginLogger();
    public static ConfigLoader getInstance() {
        return instance;
    }
}

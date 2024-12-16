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
    private static final Map<String, Integer> intParameters = new HashMap<>(Map.ofEntries(
            entry("view-range", 16),
            entry("background-transparency", 50)
    ));
    private static List<Double> location;
    private static List<Float> translation;
    private static String typingChar;
    private static String backgroundColor;
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
        return intParameters.get("view-range");
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
        return intParameters.get("background-transparency");
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

    private static void validateRange(String parameter, int min, int max) {
        if (config.isInt(parameter)) {
            int value = config.getInt(parameter);
            if (value >= min && value <= max) {
                intParameters.put(parameter, value);
            } else if (value > max) {
                intParameters.put(parameter, max);
                logger.warning(String.format("Value for %s parameter is too big. Using max value (%d).", parameter, max));
            } else {
                intParameters.put(parameter, min);
                logger.warning(String.format("Value for %s parameter is too small. Using min value (%d).", parameter, min));
            }
        } else {
            logger.warning(String.format("Can't load %s parameter from config. Using default value (%d).", parameter, intParameters.get(parameter)));
        }
    }

    private static void loadOptions() {
        validateBoolean();
        validateRange("view-range", 1, 20);
        validateRange("background-transparency", 0, 255);

        location = config.getDoubleList("location");
        translation = config.getFloatList("transformation");
        typingChar = config.getString("typing-char");
        namesColor = config.getString("names-color");
        typingIconColor = config.getString("typing-icon-color");
        backgroundColor = config.getString("background-color");
    }

    private static final Logger logger = AreYouTypingPlugin.getInstance().getPluginLogger();
    public static ConfigLoader getInstance() {
        return instance;
    }
}

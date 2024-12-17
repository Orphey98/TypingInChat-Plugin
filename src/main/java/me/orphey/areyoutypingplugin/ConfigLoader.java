package me.orphey.areyoutypingplugin;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import static java.util.Map.entry;

public class ConfigLoader {
    // TODO create enum with parameter names and logger messages

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
    private static final List<Double> location = new ArrayList<>(List.of(0.0, 0.35, 0.0));
    private static final List<Float> translation = new ArrayList<>(List.of(0F, 0.6F, 0.1F));
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
    public static boolean isVisibleThroughBlocks() {
        return booleanParameters.get("visible-through-blocks");
    }
    public static boolean isTextShadow() {
        return booleanParameters.get("text-shadow");
    }
    public static String getTypingChar() {
        return typingChar;
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
    public static int getViewRange() {
        return intParameters.get("view-range");
    }
    public static int getBackgroundTransparency() {
        return intParameters.get("background-transparency");
    }

    public void load() throws InvalidConfigurationException, IOException {
        File file = new File(AreYouTypingPlugin.getInstance().getDataFolder(), "config.yml");
        try {
            config.load(file);
        } finally {
            loadOptions();
        }
    }

    // TODO validate config params
    // Data types:
    // boolean
    // ranged int
    // double list
    // float list
    // typing-char: string, max length 16 chars
    // hex color

    private static void validateBoolean() {
        for (String parameter : booleanParameters.keySet()) {
            if (config.isBoolean(parameter)) {
                booleanParameters.put(parameter, config.getBoolean(parameter));
            } else {
                logger.warning(String.format("Can't load %s parameter from config. Changing value to %b.", parameter, booleanParameters.get(parameter)));
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
            logger.warning(String.format("Can't load %s parameter from config. Changing value to %d.", parameter, intParameters.get(parameter)));
        }
    }
    private static void validateDoubleList(String parameter, List<Double> list) {
        if (config.isList(parameter)) {
            List<Double> listTemp = config.getDoubleList(parameter);
            if (listTemp.size() == 3) {
                list.clear();
                list.addAll(listTemp);
            } else {
                logger.warning(String.format("Bad values in %s parameter. Using last saved or default values.", parameter));
            }
        } else {
            logger.warning(String.format("Bad values in %s parameter. Using last saved or default values.", parameter));
        }
    }
    private static void validateFloatList(String parameter, List<Float> list) {
        if (config.isList(parameter)) {
            List<Float> listTemp = config.getFloatList(parameter);
            if (listTemp.size() == 3) {
                list.clear();
                list.addAll(listTemp);
            } else {
                logger.warning(String.format("Bad values in %s parameter. Using last saved or default values.", parameter));
            }
        } else {
            logger.warning(String.format("Bad values in %s parameter. Using last saved or default values.", parameter));
        }
    }

    private static void loadOptions() {
        validateBoolean();
        validateRange("view-range", 1, 20);
        validateRange("background-transparency", 0, 255);
        validateDoubleList("location", location);
        validateFloatList("transformation", translation);
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

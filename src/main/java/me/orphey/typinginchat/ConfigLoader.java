package me.orphey.typinginchat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Map.entry;

public class ConfigLoader {

    private ConfigLoader() {
    }
    private static final File file = new File(TypingInChat.getInstance().getDataFolder(), "config.yml");
    private static final String bukkitVersion = Bukkit.getBukkitVersion().split("-")[0];
    private static final int bukkitComparison = compareVersions(bukkitVersion, "1.20.4");
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
    private static final List<Double> locationV19 = new ArrayList<>(List.of(0.0, 1.35, 0.0));
    private static final List<Float> translationV19 = new ArrayList<>(List.of(0F, 0.72F, 0F));
    private static final List<Double> locationV20 = new ArrayList<>(List.of(0.0, 1.8, 0.0));
    private static final List<Float> translationV20 = new ArrayList<>(List.of(0F, 0.275F, 0F));
    private static final List<Double> location = new ArrayList<>(List.copyOf(locationV19));
    private static final List<Float> translation = new ArrayList<>(List.copyOf(translationV19));
    private static String typingChar = "✎";
    private static String backgroundColor = "#000000";
    private static String namesColor = "#ffffff";
    private static String typingIconColor = "#ffffff";

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
        if (!file.exists()) {
            createConfig();
            //If server version is 1.20.5 or higher
            defaultLocation();
            return;
        }
        try {
            config.load(file);
        } finally {
            loadOptions();
        }

    }

    // Data types:
    // // boolean
    // // ranged int
    // // double list
    // // float list
    // // typing-char: string, max length 12 chars
    // // hex color

    public static void createConfig() {
        TypingInChat.getInstance().getPluginLogger().warning("Configuration not found. Creating new file with default values.");
        TypingInChat.getInstance().saveResource("config.yml", false);
    }
    private static int compareVersions(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        int maxLength = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < maxLength; i++) {
            int num1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int num2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;

            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }
        return 0; // Equal
    }

    private static void defaultLocation() throws IOException {
        String header1 = "#Custom value need to be adjusted to offset value.";
        String header2 = "#Hologram entity position offset, relative to player position.";
        String key1 = "location";
        String key2 = "offset";
        String value1;
        String value2;
        if (bukkitComparison >= 0) {
            location.clear();
            location.addAll(locationV20);
            translation.clear();
            translation.addAll(translationV20);
            value1 = "[0, 1.8, 0]";
            value2 = "[0, 0.275, 0]";
        } else {
            TypingInChat.getInstance().getPluginLogger().info("Adjusting config values for old version.");
            value1 = "[0, 1.35, 0]";
            value2 = "[0, 0.72, 0]";
        }
        appendYaml(file, header1, key1, value1);
        appendYaml(file, header2, key2, value2);
    }

    private static void appendYaml(File file, String header, String key, String value) throws IOException {
        // Open the file in append mode
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // Write a new key-value pair to the end of the file
            writer.newLine();
            writer.write(header);
            writer.newLine();
            writer.write(key + ": " + value);
        }
    }

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
    private static String validateString(String parameter, String string, int max) {
        if (config.isString(parameter)) {
            String stringTemp = config.getString(parameter);
            assert stringTemp != null;
            if (stringTemp.length() >= max) {
                logger.warning(String.format("Value for %s parameter is too big. Size limit is %d chars.", parameter, max));
                return stringTemp.substring(0, max);
            } else {
                return stringTemp;
            }
        } else {
            logger.warning(String.format("Bad values in %s parameter. Using last saved or default values.", parameter));
            return string;
        }
    }
    private static String validateHexColor(String parameter, String string) {
        if (config.isString(parameter)) {
            String stringTemp = config.getString(parameter);
            String regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
            Pattern pattern = Pattern.compile(regex);
            assert stringTemp != null;
            Matcher matcher = pattern.matcher(stringTemp);
            if (matcher.matches()) {
                return stringTemp;
            } else {
                logger.warning(String.format("Bad values in %s parameter. Using last saved or default values.", parameter));
                return string;
            }
        } else {
            logger.warning(String.format("Bad values in %s parameter. Using last saved or default values.", parameter));
            return string;
        }
    }

    private static void loadOptions() {
        validateBoolean();
        validateRange("view-range", 1, 20);
        validateRange("background-transparency", 0, 255);
        validateDoubleList("location", location);
        validateFloatList("offset", translation);
        typingChar = validateString("typing-char", typingChar, 12);
        namesColor = validateHexColor("names-color", namesColor);
        typingIconColor = validateHexColor("typing-icon-color", typingIconColor);
        backgroundColor = validateHexColor("background-color", backgroundColor);
    }

    private static final Logger logger = TypingInChat.getInstance().getPluginLogger();
    public static ConfigLoader getInstance() {
        return instance;
    }
}

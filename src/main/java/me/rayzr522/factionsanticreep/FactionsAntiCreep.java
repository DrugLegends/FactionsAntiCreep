package me.rayzr522.factionsanticreep;

import me.rayzr522.factionsanticreep.command.CommandFactionsAntiCreep;
import me.rayzr522.factionsanticreep.command.CommandHomeWrapper;
import me.rayzr522.factionsanticreep.command.CommandSetHomeWrapper;
import me.rayzr522.factionsanticreep.listeners.PlayerListener;
import me.rayzr522.factionsanticreep.listeners.PluginListener;
import me.rayzr522.factionsanticreep.utils.CommandWrapper;
import me.rayzr522.factionsanticreep.utils.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @author Rayzr
 */
public class FactionsAntiCreep extends JavaPlugin {
    public static final String BYPASS_PERM = "FactionsAntiCreep.bypass";

    private static FactionsAntiCreep instance;
    private MessageHandler messages = new MessageHandler();
    private final Set<CommandWrapper> commandWrapperSet = new HashSet<>();

    private long maxOfflineTime = Long.MAX_VALUE;

    /**
     * @return The current instance of FactionsAntiCreep.
     */
    public static FactionsAntiCreep getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // Load configs
        reload();

        // Set up commands
        getCommand("factionsanticreep").setExecutor(new CommandFactionsAntiCreep(this));

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new PluginListener(this), this);

        hookCommandWrappers();
    }

    @Override
    public void onDisable() {
        unhookCommandWrappers();

        instance = null;
    }

    public void hookCommandWrappers() {
        if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            commandWrapperSet.add(new CommandHomeWrapper(this, Bukkit.getPluginCommand("home")));
            commandWrapperSet.add(new CommandSetHomeWrapper(this, Bukkit.getPluginCommand("sethome")));
        }

        commandWrapperSet.forEach(CommandWrapper::hook);
    }

    public void unhookCommandWrappers() {
        commandWrapperSet.forEach(CommandWrapper::unhook);
        commandWrapperSet.clear();
    }

    /**
     * (Re)loads all configs from the disk
     */
    public void reload() {
        saveDefaultConfig();
        reloadConfig();

        messages.load(getConfig("messages.yml"));

        maxOfflineTime = TimeUnit.SECONDS.toMillis(getConfig().getInt("max-offline-time"));
    }

    /**
     * If the file is not found and there is a default file in the JAR, it saves the default file to the plugin data folder first
     *
     * @param path The path to the config file (relative to the plugin data folder)
     * @return The {@link YamlConfiguration}
     */
    public YamlConfiguration getConfig(String path) {
        if (!getFile(path).exists() && getResource(path) != null) {
            saveResource(path, true);
        }
        return YamlConfiguration.loadConfiguration(getFile(path));
    }

    /**
     * Attempts to save a {@link YamlConfiguration} to the disk, with any {@link IOException}s being printed to the console.
     *
     * @param config The config to save
     * @param path   The path to save the config file to (relative to the plugin data folder)
     */
    public void saveConfig(YamlConfiguration config, String path) {
        try {
            config.save(getFile(path));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to save config", e);
        }
    }

    /**
     * @param path The path of the file (relative to the plugin data folder)
     * @return The {@link File}
     */
    public File getFile(String path) {
        return new File(getDataFolder(), path.replace('/', File.separatorChar));
    }

    /**
     * Translates a message from the language file.
     *
     * @param key     The key of the message to translate
     * @param objects The formatting objects to use
     * @return The formatted message
     */
    public String tr(String key, Object... objects) {
        return messages.tr(key, objects);
    }

    /**
     * Checks a target {@link CommandSender} for a given permission, and optionally sends a message if they don't.
     * <br>
     * This will automatically prefix any permission with the name of the plugin.
     *
     * @param target      The target {@link CommandSender} to check
     * @param permission  The permission to check, excluding the permission base (which is the plugin name)
     * @param sendMessage Whether or not to send a no-permission message to the target
     * @return Whether or not the target has the given permission
     */
    public boolean checkPermission(CommandSender target, String permission, boolean sendMessage) {
        String fullPermission = String.format("%s.%s", getName(), permission);

        if (!target.hasPermission(fullPermission)) {
            if (sendMessage) {
                target.sendMessage(tr("no-permission", fullPermission));
            }

            return false;
        }

        return true;
    }

    /**
     * @return The {@link MessageHandler} instance for this plugin
     */
    public MessageHandler getMessages() {
        return messages;
    }

    public long getMaxOfflineTime() {
        return maxOfflineTime;
    }
}

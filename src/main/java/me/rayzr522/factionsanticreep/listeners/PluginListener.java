package me.rayzr522.factionsanticreep.listeners;

import me.rayzr522.factionsanticreep.FactionsAntiCreep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginListener implements Listener {
    private final FactionsAntiCreep plugin;

    public PluginListener(FactionsAntiCreep plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent e) {
        if (e.getPlugin().getName().equals("Essentials")) {
            plugin.unhookCommandWrappers();
        }
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent e) {
        if (e.getPlugin().getName().equals("Essentials")) {
            plugin.hookCommandWrappers();
        }
    }
}

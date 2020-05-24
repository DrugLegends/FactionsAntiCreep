package me.rayzr522.factionsanticreep.listeners;

import me.rayzr522.factionsanticreep.FactionsAntiCreep;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.rayzr522.factionsanticreep.utils.FactionUtil.isEnemyLand;
import static me.rayzr522.factionsanticreep.utils.FactionUtil.isWarzone;

public class PlayerListener implements Listener {
    private final FactionsAntiCreep plugin;

    public PlayerListener(FactionsAntiCreep plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        long lastPlayed = player.getLastPlayed();
        long now = System.currentTimeMillis();

        if (now - lastPlayed < plugin.getMaxOfflineTime()) {
            // safe -- haven't been offline long enough
            return;
        }

        Location location = player.getLocation();

        if (isEnemyLand(player, location) || isWarzone(location)) {
            player.teleport(player.getWorld().getSpawnLocation());
            player.sendMessage(plugin.tr("event.teleported"));
        }
    }
}

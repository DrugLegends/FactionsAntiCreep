package me.rayzr522.factionsanticreep.listeners;

import com.massivecraft.factions.*;
import me.rayzr522.factionsanticreep.FactionsAntiCreep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    private final FactionsAntiCreep plugin;

    public PlayerListener(FactionsAntiCreep plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        long lastPlayed = e.getPlayer().getLastPlayed();
        long now = System.currentTimeMillis();

        if (now - lastPlayed < plugin.getMaxOfflineTime()) {
            // safe -- haven't been offline long enough
            return;
        }

        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(e.getPlayer());
        Faction faction = Board.getInstance().getFactionAt(new FLocation(e.getPlayer().getLocation()));

        if (fPlayer == null || faction == null || !faction.isNormal()) {
            // either player isn't in a faction or there is no (real) faction
            return;
        }

        switch (fPlayer.getRelationTo(faction)) {
            case ALLY:
            case TRUCE:
                // only ally & truce are safe
                break;
            default:
                e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
                e.getPlayer().sendMessage(plugin.tr("event.teleported"));
        }
    }
}

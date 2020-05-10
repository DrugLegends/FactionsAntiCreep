package me.rayzr522.factionsanticreep.listeners;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
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

        MPlayer factionPlayer = MPlayer.get(e.getPlayer());
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(e.getPlayer().getLocation()));

        if (factionPlayer == null || faction == null || !faction.isNormal() || FactionColl.get().getWarzone().equals(faction)) {
            // either player isn't in a faction or there is no (real) faction
            return;
        }

        switch (factionPlayer.getRelationTo(faction)) {
            case ALLY:
            case TRUCE:
            case MEMBER:
                // only ally, truce & members are safe
                break;
            default:
                e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
                e.getPlayer().sendMessage(plugin.tr("event.teleported"));
        }
    }
}

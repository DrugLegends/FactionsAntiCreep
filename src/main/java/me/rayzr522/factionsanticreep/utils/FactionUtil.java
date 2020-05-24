package me.rayzr522.factionsanticreep.utils;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FactionUtil {
    public static boolean isEnemyLand(Player player, Location location) {
        MPlayer factionPlayer = MPlayer.get(player);
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(location));

        if (factionPlayer == null || faction == null || !faction.isNormal()) {
            // either player isn't in a faction or there is no (real) faction
            return false;
        }

        switch (factionPlayer.getRelationTo(faction)) {
            case ENEMY:
            case NEUTRAL:
                return true;
            default:
                // only ally, truce & members are safe
                return false;
        }
    }

    public static boolean isWarzone(Location location) {
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(location));

        if (faction == null) {
            return false;
        }

        return FactionColl.get().getWarzone().equals(faction);
    }
}

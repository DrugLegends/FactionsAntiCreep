package me.rayzr522.factionsanticreep.command;

import me.rayzr522.factionsanticreep.FactionsAntiCreep;
import me.rayzr522.factionsanticreep.utils.CommandWrapper;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import static me.rayzr522.factionsanticreep.utils.FactionUtil.isEnemyLand;
import static me.rayzr522.factionsanticreep.utils.FactionUtil.isWarzone;

public class CommandSetHomeWrapper extends CommandWrapper {
    private final FactionsAntiCreep plugin;

    public CommandSetHomeWrapper(FactionsAntiCreep plugin, PluginCommand pluginCommand) {
        super(pluginCommand);

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return super.onCommand(sender, command, label, args);
        }

        Player player = (Player) sender;
        Location location = player.getLocation();

        if (isEnemyLand(player, location) || isWarzone(location)) {
            player.sendMessage(plugin.tr("event.sethome-prevented"));
            return true;
        }

        return super.onCommand(sender, command, label, args);
    }
}

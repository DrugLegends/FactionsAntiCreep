package me.rayzr522.factionsanticreep.command;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import me.rayzr522.factionsanticreep.FactionsAntiCreep;
import me.rayzr522.factionsanticreep.struct.EssentialsHome;
import me.rayzr522.factionsanticreep.utils.CommandWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static me.rayzr522.factionsanticreep.utils.FactionUtil.isEnemyLand;
import static me.rayzr522.factionsanticreep.utils.FactionUtil.isSystemFaction;

public class CommandHomeWrapper extends CommandWrapper {
    private final FactionsAntiCreep plugin;

    public CommandHomeWrapper(FactionsAntiCreep plugin, PluginCommand pluginCommand) {
        super(pluginCommand);

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || sender.hasPermission(FactionsAntiCreep.BYPASS_PERM)) {
            return super.onCommand(sender, command, label, args);
        }

        Player player = (Player) sender;
        User user = Essentials.getPlugin(Essentials.class).getUser(player);

        List<EssentialsHome> homesToRemove = user.getHomes().stream()
                .map(name -> EssentialsHome.fromUserHome(user, name).orElse(null))
                .filter(Objects::nonNull)
                .filter(home -> isSystemFaction(home.getLocation()) || isEnemyLand(player, home.getLocation()))
                .collect(Collectors.toList());

        homesToRemove.forEach(home -> {
            try {
                user.delHome(home.getName());
                player.sendMessage(plugin.tr("event.home-removed", home.getName()));
            } catch (Exception ignored) {

            }
        });

        return super.onCommand(sender, command, label, args);
    }
}

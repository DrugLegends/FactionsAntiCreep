package me.rayzr522.factionsanticreep.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public class CommandWrapper implements CommandExecutor {
    private final PluginCommand pluginCommand;
    private CommandExecutor originalExecutor;

    public CommandWrapper(PluginCommand pluginCommand) {
        this.pluginCommand = pluginCommand;
    }

    public void hook() {
        if (originalExecutor != null) {
            throw new IllegalStateException("Command wrapper has already been hooked!");
        }

        originalExecutor = pluginCommand.getExecutor();
        pluginCommand.setExecutor(this);
    }

    public void unhook() {
        if (originalExecutor == null) {
            throw new IllegalStateException("Command wrapper hasn't been hooked yet!");
        }

        pluginCommand.setExecutor(originalExecutor);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return originalExecutor.onCommand(sender, command, label, args);
    }
}

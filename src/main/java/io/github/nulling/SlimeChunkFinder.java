package io.github.nulling;

import io.github.nulling.commands.SCFCommand;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class SlimeChunkFinder extends JavaPlugin {
    @Override
    public void onEnable() {
        // Setup handling of "/scf" command
        PluginCommand scfCommand = getCommand("scf");
        SCFCommand scfCommandHandler = new SCFCommand(); // Instantiate handler for this command
        scfCommand.setExecutor(scfCommandHandler);
    }

    @Override
    public void onDisable() {

    }
}

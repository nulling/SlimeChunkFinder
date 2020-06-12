package io.github.nulling;

import io.github.nulling.appl.SCFManager;
import io.github.nulling.commands.SCFCommand;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class SlimeChunkFinder extends JavaPlugin {

    private SCFManager manager;

    @Override
    public void onEnable() {
        manager = new SCFManager(this);

        // Setup handling of "/scf" command
        PluginCommand scfCommand = getCommand("scf");
        SCFCommand scfCommandHandler = new SCFCommand(manager, getLogger()); // Instantiate handler for this command
        scfCommand.setExecutor(scfCommandHandler);
    }

    @Override
    public void onDisable() {
        manager.clearInstances(); // Cancel all ongoing scanning
        getLogger().info("Cleared all slime chunk scanning instances");
    }
}

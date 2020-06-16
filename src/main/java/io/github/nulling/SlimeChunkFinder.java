package io.github.nulling;

import io.github.nulling.appl.SCFManager;
import io.github.nulling.commands.SCFCommand;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
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

    /** Checks if a given player is within a slime-spawning chunk
     * @param player Player to check if in slime chunk
     * @return true if in a slime chunk, false otherwise
     */
    public static boolean isInSlimeChunk(Player player) {
        Location position = player.getLocation();
        Chunk chunkIn = position.getChunk();
        return chunkIn.isSlimeChunk();
    }
}

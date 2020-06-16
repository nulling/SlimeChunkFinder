package io.github.nulling.appl;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

/** Application-tier classes that manages the tasks responsible for player scanning
 * of slime chunks */
public class SCFManager {

    /** Time between slime chunk checking, in ticks (20 ticks = 1 second) */
    private final static long TICKS_BETWEEN_SCANS = 10;

    /** Maintains record of ongoing slime chunk scanners */
    private final Map<Player, BukkitTask> activeInstances;

    /** Reference to the plugin that instantiates the manager */
    private final JavaPlugin plugin;

    /** Instantiates an instance of the manager that regulates the tasks
     * responsible for scanning
     * @param plugin JavaPlugin class of plugin to associate manager with
     */
    public SCFManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.activeInstances = new HashMap<>();
    }

    /** Checks if the player passed in is currently scanning
     * @param player Player to check if they are scanning
     * @return True if they are currently scanning a slime chunk, false otherwise
     */
    public boolean isScanning(Player player) {
        return activeInstances.containsKey(player);
    }

    /** Handles toggling input from command
     * @param player Player to toggle slime chunk scanning
     * @return false if turned off, true if turned on
     */
    public boolean toggle(Player player) {
        if(isScanning(player)) {
            turnOff(player);
            return false;
        } else {
            turnOn(player);
            return true;
        }
    }

    /** Turns off slime chunk finding for a player. Does nothing if player
     * is already not scanning.
     * @param player Player to turn scanning off for
     */
    public void turnOff(Player player) {
        if(!isScanning(player)) return;
        BukkitTask taskInstance = activeInstances.get(player);
        taskInstance.cancel();
        activeInstances.remove(player);
    }

    /** Turns on slime chunk finding for a player. Does nothing if player
     * is already scanning.
     * @param player Player to turn scanning on for
     */
    public void turnOn(Player player) {
        if(isScanning(player)) return;
        SCFScannerTask scannerInstance = new SCFScannerTask(player);
        BukkitTask taskInstance = scannerInstance.runTaskTimer(plugin, 0, TICKS_BETWEEN_SCANS);
        activeInstances.put(player, taskInstance);
    }

    /** Cancels all ongoing scanning tasks, typically in cases where the server is being
     * shut down or when the plugin is unloaded
     */
    public void clearInstances() {
        for(BukkitTask taskInstance : activeInstances.values()) {
            taskInstance.cancel();
        }
        activeInstances.clear();
    }

}

package io.github.nulling.appl;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

/** Application-tier classes that manages the tasks responsible for player scanning
 * of slime chunks
 */
public class SCFManager {

    /** Maintains record of ongoing slime chunk scanners */
    private final Map<Player, BukkitTask> activeInstances;

    /** Reference to the plugin that instantiates the manager */
    private final JavaPlugin plugin;

    public SCFManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.activeInstances = new HashMap<>();
    }

    /** Handles toggling input from command
     * @param player Player to toggle slime chunk scanning
     * @return false if turned off, true if turned on
     */
    public boolean toggle(Player player) {
        if(activeInstances.containsKey(player)) {
            turnOff(player);
            return false;
        } else {
            turnOn(player);
            return true;
        }
    }

    /** Helper method that turns off slime chunk finding by canceling
     * the task that scans for slime chunk
     * @param player Player to turn scanning off for
     */
    private void turnOff(Player player) {
        if(!activeInstances.containsKey(player)) return;
        BukkitTask taskInstance = activeInstances.get(player);
        taskInstance.cancel();
        activeInstances.remove(player);
    }

    /** Helper method that turns on slime chunk finding. Instantiates appropriate
     * task class and schedules it with Bukkit's scheduler
     * @param player Player to turn scanning on for
     */
    private void turnOn(Player player) {
        if(activeInstances.containsKey(player)) return;
        SCFScannerTask scannerInstance = new SCFScannerTask(player);
        BukkitTask taskInstance = scannerInstance.runTaskTimer(plugin, 0, 20); // 20 ticks = 1 second
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

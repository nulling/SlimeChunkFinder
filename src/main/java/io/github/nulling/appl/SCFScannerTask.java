package io.github.nulling.appl;

import io.github.nulling.SlimeChunkFinder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/** Runnable class allowing scanning functionality to work with a specific player */
public class SCFScannerTask extends BukkitRunnable {

    /** Message displayed to the user when they are in a slime chunk */
    private static final String IN_CHUNK = ChatColor.GREEN + "" + ChatColor.BOLD + "Slime chunk!";
    /** Message displayed to the user when they are not in a slime chunk */
    private static final String NOT_IN_CHUNK = ChatColor.RED + "" + ChatColor.BOLD + "Not a slime chunk!";

    /** Player who is currently scanning for slime chunks */
    private final Player player;

    /** Instantiates a task object which will be scheduled for running
     * @param player Player who will be scanning for slime chunks
     */
    public SCFScannerTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if(SlimeChunkFinder.isInSlimeChunk(player)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(IN_CHUNK));
        } else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(NOT_IN_CHUNK));
        }
    }
}

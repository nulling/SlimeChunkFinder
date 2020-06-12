package io.github.nulling.appl;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/** Runnable class allowing scanning functionality to work with a specific player */
public class SCFScannerTask extends BukkitRunnable {

    /** Player who is currently scanning for slime chunks */
    private final Player player;
    private int count;


    public SCFScannerTask(Player player) {
        this.player = player;
        this.count = 0;
    }

    @Override
    public void run() {
        count++;
        String message = String.format("%d", count);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}

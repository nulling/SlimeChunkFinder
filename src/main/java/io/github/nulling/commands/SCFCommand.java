package io.github.nulling.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SCFCommand implements CommandExecutor {

    private static final String NO_PERMISSION_TARGET = ChatColor.RED + "" + ChatColor.BOLD +
            "You do not have sufficient permissions to perform this command.";
    private static final String IS_NOT_PLAYER = ChatColor.RED + "Command cannot be executed because " +
            "executor is not a player!";
    private static final String TARGET_NOT_ONLINE = ChatColor.RED + "" + ChatColor.BOLD + "Player \"%s\" is not online!";

    private static final String SCF_ON = ChatColor.BOLD + "" + ChatColor.GREEN + "Slime Chunk Finder is enabled!";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(args.length > 1) return false;
        if(!sender.hasPermission("scf.toggle")) return false;

        Player target;
        boolean targetDifferent = false;
        if(args.length == 1) {
            String namedTarget = args[0];
            target = Bukkit.getPlayer(namedTarget);
            if(target == null) { // Target is not online
                sender.sendMessage(String.format(TARGET_NOT_ONLINE, namedTarget));
                return true;
            }
            targetDifferent = target.equals(sender);
            if(targetDifferent && !sender.hasPermission("scf.toggleother")) { // Executor does not have permission
                sender.sendMessage(NO_PERMISSION_TARGET);
                return true;
            }
        } else {
            if(!(sender instanceof Player)) { // Executor is not a player
                sender.sendMessage(IS_NOT_PLAYER);
                return true;
            }
            target = (Player) sender;
        }
        target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(SCF_ON));
        if(targetDifferent)
            sender.sendMessage(String.format(ChatColor.GREEN + "Slime Chunk Finder enabled for user %s!", args[0]));
        return true;
    }

}

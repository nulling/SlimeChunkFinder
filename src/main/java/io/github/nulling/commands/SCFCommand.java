package io.github.nulling.commands;

import io.github.nulling.appl.SCFManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

/** Class that handles invokation of the "/scf" command */
public class SCFCommand implements CommandExecutor {

    private static final String NO_PERMISSION_TARGET = ChatColor.RED + "" + ChatColor.BOLD +
            "You do not have sufficient permissions to perform this command.";
    private static final String IS_NOT_PLAYER = ChatColor.RED + "Command cannot be executed because " +
            "executor is not a player!";
    private static final String TARGET_NOT_ONLINE = ChatColor.RED + "" + ChatColor.BOLD + "Player \"%s\" is not online!";

    private static final String TOGGLED_ON = ChatColor.GREEN + "" + ChatColor.BOLD + "Slime chunk scanning turned on!";
    private static final String TOGGLED_ON_OTHER = ChatColor.GREEN + "" + ChatColor.BOLD +
            "Slime chunk scanning turned on for %s";
    private static final String TOGGLED_OFF = ChatColor.RED + "" + ChatColor.BOLD + "Slime chunk scanning turned off!";
    private static final String TOGGLED_OFF_OTHER = ChatColor.RED + "" + ChatColor.BOLD +
            "Slime chunk scanning turned off for %s";

    private static final String LOGGER_ENABLED = "%s turned on slime chunk scanning for %s";
    private static final String LOGGER_DISABLED = "%s turned off slime chunk scanning for %s";

    /** Manager instance to communicate with */
    private final SCFManager manager;
    /** Plugin logger */
    private final Logger logger;

    public SCFCommand(SCFManager manager, Logger logger) {
        this.manager = manager;
        this.logger = logger;
    }

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
        boolean toggled = manager.toggle(target);
        if(toggled) {
            target.sendMessage(TOGGLED_ON);
            if(targetDifferent && !sender.equals(target))
                sender.sendMessage(String.format(TOGGLED_ON_OTHER, args[0]));
            logger.info(String.format(LOGGER_ENABLED, sender.getName(), target.getName()));
        } else {
            target.sendMessage(TOGGLED_OFF);
            if(targetDifferent && !sender.equals(target))
                sender.sendMessage(String.format(TOGGLED_OFF_OTHER, args[0]));
            logger.info(String.format(LOGGER_DISABLED, sender.getName(), target.getName()));
        }
        return true;
    }

}

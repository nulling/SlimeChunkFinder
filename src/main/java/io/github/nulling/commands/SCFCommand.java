package io.github.nulling.commands;

import io.github.nulling.appl.SCFManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

/** Class that handles invokation of the "/scf" command */
public class SCFCommand implements CommandExecutor {

    /** Message sent to the command sender if they have insufficient permissions */
    private static final String NO_PERMISSION_TARGET = ChatColor.RED + "" + ChatColor.BOLD +
            "You do not have sufficient permissions to perform this command.";
    /** Message sent to the command sender if it's trying to toggle itself on and itself is not a player */
    private static final String IS_NOT_PLAYER = ChatColor.RED + "Command cannot be executed because " +
            "executor is not a player!";
    /** Message sent to the command sender if the target is not online or does not exist */
    private static final String TARGET_NOT_ONLINE = ChatColor.RED + "" + ChatColor.BOLD + "Player \"%s\" is not online!";

    /** Message sent to the target upon slime chunk scanning being enabled for them */
    private static final String TOGGLED_ON = ChatColor.GREEN + "" + ChatColor.BOLD + "Slime chunk scanning turned on!";
    /** Message sent to the command sender upon target's slime chunk scanning being enabled if target is other player */
    private static final String TOGGLED_ON_OTHER = ChatColor.GREEN + "" + ChatColor.BOLD +
            "Slime chunk scanning turned on for %s";
    /** Message sent to the target upon slime chunk scanning being disabled for them */
    private static final String TOGGLED_OFF = ChatColor.RED + "" + ChatColor.BOLD + "Slime chunk scanning turned off!";
    /** Message sent to the command sender upon target's slime chunk scanning being disabled if target is other player */
    private static final String TOGGLED_OFF_OTHER = ChatColor.RED + "" + ChatColor.BOLD +
            "Slime chunk scanning turned off for %s";

    /** Message sent to the target if they are not in the Overworld */
    private static final String SELF_NOT_IN_OVERWORLD = ChatColor.RED + "" + ChatColor.BOLD +
            "Cannot enable slime chunk scanning, you are not in the Overworld!";
    /** Message sent to the command sender if target is not in the Overworld given target is not the sender */
    private static final String TARGET_NOT_IN_OVERWORLD = ChatColor.RED + "" + ChatColor.BOLD +
            "Cannot enable slime chunk scanning, %s is not in the Overworld!";

    /** Message sent to the logger upon slime chunk being enabled, specifies who enabled scanning for whomst */
    private static final String LOGGER_ENABLED = "%s turned on slime chunk scanning for %s";
    /** Message sent to the logger upon slime chunk being disabled, specifies who enabled scanning for whomst */
    private static final String LOGGER_DISABLED = "%s turned off slime chunk scanning for %s";

    /** Manager instance to communicate with */
    private final SCFManager manager;
    /** Plugin logger */
    private final Logger logger;

    /** Instantiates instance of class that handles "/scf" method
     * @param manager Manager that should keep track of tasks allowing the scanning
     * @param logger Logger instance associated with plugin to allow logging to console upon command invokation
     */
    public SCFCommand(SCFManager manager, Logger logger) {
        this.manager = manager;
        this.logger = logger;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(args.length > 1) return false; // Excessive argument count
        if(!sender.hasPermission("scf.toggle")) return false; // Executor does not have permission

        // Determine who to toggle chunk scanning for
        Player target; // Player who will be toggled
        boolean targetDifferent = false;
        if(args.length == 1) { // if another player's name is given
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
        } else { // No argument give, just "/scf"
            if(!(sender instanceof Player)) { // Executor is not a player
                sender.sendMessage(IS_NOT_PLAYER);
                return true;
            }
            target = (Player) sender;
        }

        // Ensure player is in Overworld
        boolean targetSame = target.equals(sender);
        if(target.getWorld().getEnvironment() != World.Environment.NORMAL) { // Prevent command if not in overworld
            target.sendMessage(SELF_NOT_IN_OVERWORLD);
            if(!targetSame) sender.sendMessage(String.format(TARGET_NOT_IN_OVERWORLD,
                    args[0]));
            return true;
        }

        // Toggle slime chunk scanning
        boolean toggled = manager.toggle(target);
        if(toggled) {
            target.sendMessage(TOGGLED_ON);
            if(targetDifferent && !targetSame) // if toggling on another player's
                sender.sendMessage(String.format(TOGGLED_ON_OTHER, args[0]));
            logger.info(String.format(LOGGER_ENABLED, sender.getName(), target.getName()));
        } else {
            target.sendMessage(TOGGLED_OFF);
            if(targetDifferent && !targetSame) // if toggling off another's player
                sender.sendMessage(String.format(TOGGLED_OFF_OTHER, args[0]));
            logger.info(String.format(LOGGER_DISABLED, sender.getName(), target.getName()));
        }
        return true;
    }

}

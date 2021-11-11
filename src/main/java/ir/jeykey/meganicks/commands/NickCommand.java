package ir.jeykey.meganicks.commands;

import ir.jeykey.meganicks.MegaNicks;
import ir.jeykey.meganicks.database.models.Nick;
import ir.jeykey.meganicks.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class NickCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (sender instanceof ConsoleCommandSender) {
                        Common.send(sender, "&cConsole cannot execute nick commands!");
                        return true;
                }

                if (!sender.hasPermission("meganicks.use")) {
                        Common.send(sender, "&cYou don't have &4meganicks.use &cpermission to use this command!");
                        return true;
                }

                Player player = (Player) sender;

                Bukkit.getScheduler().runTaskAsynchronously(MegaNicks.getInstance(), () -> {
                        if (args.length > 0) {
                                if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("disable")) {
                                        Nick.unDisguise(player);
                                }
                        } else {
                                Nick.disguise(player);
                        }
                });

                return true;
        }
}

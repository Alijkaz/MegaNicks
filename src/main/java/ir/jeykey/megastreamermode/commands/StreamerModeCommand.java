package ir.jeykey.megastreamermode.commands;

import ir.jeykey.megastreamermode.MegaStreamermode;
import ir.jeykey.megastreamermode.config.RandomNames;
import ir.jeykey.megastreamermode.database.models.StreamerMode;
import ir.jeykey.megastreamermode.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class StreamerModeCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (sender instanceof ConsoleCommandSender) {
                        Common.send(sender, "&cConsole cannot execute streamermode commands!");
                        return true;
                }

                if (!sender.hasPermission("megasm.use")) {
                        Common.send(sender, "&cYou don't have &4megasm.use &cpermission to use this command!");
                        return true;
                }

                Player player = (Player) sender;

                Bukkit.getScheduler().runTaskAsynchronously(MegaStreamermode.getInstance(), () -> {
                        if (args.length > 0) {
                                if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("disable")) {
                                        StreamerMode.unDisguise(player);
                                }
                        } else {
                                StreamerMode.disguise(player);
                        }
                });

                return true;
        }
}

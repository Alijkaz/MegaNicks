package ir.jeykey.megastreamermode;

import com.zaxxer.hikari.pool.HikariPool;
import ir.jeykey.megastreamermode.commands.StreamerModeCommand;
import ir.jeykey.megastreamermode.config.Messages;
import ir.jeykey.megastreamermode.config.RandomNames;
import ir.jeykey.megastreamermode.config.Storage;
import ir.jeykey.megastreamermode.database.DataSource;
import ir.jeykey.megastreamermode.events.PlayerJoin;
import ir.jeykey.megastreamermode.utils.Common;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;

public class MegaStreamermode extends JavaPlugin {
        @Getter public static MegaStreamermode instance;

        @Override
        public void onEnable() {
                // Assigning instance
                instance = this;
                
                // For calculating
                long start = System.currentTimeMillis();

                // Creating/Loading configuration files
                new Messages().setup();
                new Storage().setup();
                new RandomNames().setup();

                // Registering commands
                getCommand("streamermode").setExecutor(new StreamerModeCommand());

                // Registering events
                getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

                // Setting up datasource
                try {
                        if (Storage.LOCATION.equalsIgnoreCase("sqlite")) {
                                DataSource.SQLite();
                        } else if (Storage.LOCATION.equalsIgnoreCase("mysql")) {
                                DataSource.MySQL();
                        } else {
                                disablePlugin(true, "&cStorage type defined in config (" + Storage.LOCATION + ") is not valid!");
                                return;
                        }
                } catch (SQLException | HikariPool.PoolInitializationException exception) {
                        disablePlugin(true, "&cPlugin could not work with database! [ Check Stack Trace For More Information ]");
                        return;
                }
                catch (IOException exception) {
                        disablePlugin(true,"&cPlugin is unable to create database file, Please check directory permissions [ Check Stack Trace For More Information ]");
                        return;
                } catch (ClassNotFoundException exception) {
                        disablePlugin(true, "&cIt seems that there's a problem with plugin and it could not be loaded, Please contact plugin developers [ Check Stack Trace For More Information ]");
                        return;
                }

                // For calculating
                long end = System.currentTimeMillis();

                // Logging plugin has been activated
                Common.log(
                        Common.repeat("&a&m=", 12, "&2"),
                        "&a&lMegaStreamerMode &aActivated",
                        "&a&lVersion: &2" + getDescription().getVersion(),
                        "&a&lTook: &2" + (end - start) + " ms",
                        Common.repeat("&a&m=", 12, "&2")
                );

        }

        @Override
        public void onDisable() {
                // Logging plugin has been deactivated
                Common.log(
                        Common.repeat("&c&m=", 12, "&4"),
                        "&c&lMegaStreamerMode &cDeactivated",
                        Common.repeat("&c&m=", 12, "&4")
                );
        }

        public static void disablePlugin(boolean addPrefix, String... messages) {
                if (addPrefix)
                        Common.logPrefixed(messages);
                else
                        Common.log(messages);
                Bukkit.getPluginManager().disablePlugin(instance);
        }

}

package ir.jeykey.meganicks.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ir.jeykey.meganicks.MegaNicks;
import ir.jeykey.meganicks.config.Storage;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataSource {
        private static HikariConfig config = new HikariConfig();
        private static HikariDataSource ds;
        private static Connection connection;

        public static void SQLite() throws SQLException, IOException, ClassNotFoundException {
                Class.forName("org.sqlite.JDBC");

                File file = new File(MegaNicks.getInstance().getDataFolder(), "data.db");
                if (!file.exists()) file.createNewFile();

                config.setJdbcUrl("jdbc:sqlite:" + MegaNicks.getInstance().getDataFolder() + "/data.db");
                config.setConnectionTestQuery("SELECT 1");
                config.addDataSourceProperty("cachePrepStmts", "true");
                config.addDataSourceProperty("prepStmtCacheSize", "250");
                config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

                ds = new HikariDataSource(config);

                connection = ds.getConnection();

                NicksDB.createTables(false);
        }

        public static void MySQL() throws SQLException {
                config.setJdbcUrl("jdbc:mysql://" + Storage.MYSQL_HOST + ":" + Storage.MYSQL_PORT + "/" + Storage.MYSQL_DB + "?useSSL=false");
                config.setUsername(Storage.MYSQL_USERNAME);
                config.setPassword(Storage.MYSQL_PASSWORD);
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
                config.addDataSourceProperty("cachePrepStmts", "true");
                config.addDataSourceProperty("prepStmtCacheSize", "250");
                config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

                ds = new HikariDataSource(config);

                connection = ds.getConnection();

                NicksDB.createTables(true);
        }

        public static Connection getConnection() {
                return connection;
        }

        public static void executeQueryAsync(PreparedStatement statement) {
                Bukkit.getScheduler().runTaskAsynchronously(MegaNicks.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                                try {
                                        statement.execute();
                                } catch (SQLException e) {
                                        e.printStackTrace();
                                }
                        }
                });
        }
}
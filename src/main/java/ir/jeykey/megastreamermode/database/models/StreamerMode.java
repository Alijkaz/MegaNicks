package ir.jeykey.megastreamermode.database.models;

import ir.jeykey.megastreamermode.MegaStreamermode;
import ir.jeykey.megastreamermode.config.RandomNames;
import ir.jeykey.megastreamermode.database.DataSource;
import ir.jeykey.megastreamermode.database.Queries;
import ir.jeykey.megastreamermode.utils.Common;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class StreamerMode {
        @Setter @Getter private Integer id;
        @Setter @Getter private String userName;
        @Setter @Getter private String nickName;
        @Setter @Getter private String createdAt;

        public StreamerMode(int id) {
                this(null, null, id);
        }

        public StreamerMode(String userName) {
                this(userName, null);
        }

        public StreamerMode(String userName, String nickName) {
                this(userName, nickName, -1);
        }

        public StreamerMode(String userName, String nickName, int id) {
                setId(id);
                setUserName(userName);
                setNickName(nickName);
        }

        /**
         * Saves current model to database
         */
        public void save() {
                try {
                        PreparedStatement pst = DataSource.getConnection().prepareStatement(Queries.INSERT_SM);
                        pst.setString(1, getUserName());
                        pst.setString(2, getNickName());
                        DataSource.executeQueryAsync(pst);
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }
        }

        /**
         * Saves current model to database
         */
        public void update() {
                try {
                        PreparedStatement pst = DataSource.getConnection().prepareStatement(Queries.UPDATE_NICK_BY_USER);
                        pst.setString(1, getNickName());
                        pst.setString(2, getUserName());
                        DataSource.executeQueryAsync(pst);
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }
        }

        /**
         * This method is used to find and load specific sm using StreamerMode#id or StreamerMode#userName
         */
        public void load() {
                try {
                        Connection con = DataSource.getConnection();
                        PreparedStatement pst;

                        if (getId() != -1) {
                                pst = con.prepareStatement(Queries.SELECT_SM);
                                pst.setInt(1, getId());
                        } else {
                                pst = con.prepareStatement(Queries.SELECT_SM_BY_USER);
                                pst.setString(1, getUserName());
                        }

                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                                setId(rs.getInt("id"));
                                setUserName(rs.getString("username"));
                                setNickName(rs.getString("nickname"));
                                setCreatedAt(rs.getString("created_at"));
                        }
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }
        }

        public static boolean exists(Player p) {
                StreamerMode streamerMode = new StreamerMode(p.getName());
                streamerMode.load();
                return streamerMode.getNickName() != null;
        }

        public void delete() {
                try {
                        Connection con = DataSource.getConnection();
                        PreparedStatement pst;

                        if (getId() != -1) {
                                pst = con.prepareStatement(Queries.DELETE_SM);
                                pst.setInt(1, getId());
                        } else {
                                pst = con.prepareStatement(Queries.DELETE_SM_BY_USER);
                                pst.setString(1, getUserName());
                        }

                        DataSource.executeQueryAsync(pst);
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }
        }

        public static String fetchNickName(Player player) {
                String nickName = null;
                try {
                        Connection con = DataSource.getConnection();
                        PreparedStatement pst;

                        pst = con.prepareStatement(Queries.SELECT_SM_BY_USER);
                        pst.setString(1, player.getName());

                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                                nickName = rs.getString("nickname");
                        }
                } catch (SQLException exception) {
                        exception.printStackTrace();
                }
                return nickName;
        }

        public static void apply(Player player, String nickName) {
                player.setDisplayName(nickName);
                player.setCustomName(nickName);
                player.setPlayerListName(nickName);
                player.setCustomNameVisible(true);
        }

        public static void disguise(Player player) {
                String nick = RandomNames.get();

                if (nick == null) {
                        Common.send(player, "&cThere was a problem generating a random name for you, Please contact server administrators.");
                }

                StreamerMode streamerMode = new StreamerMode(player.getName(), nick);

                if (exists(player)) {
                        streamerMode.update();
                } else {
                        streamerMode.save();
                }

                Common.send(player, "&aYou're now disguised as &2" + nick + " &aacross our network!");

                apply(player, nick);
        }

        public static void unDisguise(Player player) {
                apply(player, player.getName());

                StreamerMode streamerMode = new StreamerMode(player.getName());
                streamerMode.delete();

                Common.send(player, "&aYou're no longer disguised");
        }


}

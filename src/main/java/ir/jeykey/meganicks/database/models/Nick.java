package ir.jeykey.meganicks.database.models;

import ir.jeykey.meganicks.config.RandomNames;
import ir.jeykey.meganicks.database.DataSource;
import ir.jeykey.meganicks.database.Queries;
import ir.jeykey.meganicks.utils.Common;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Nick {
        @Setter @Getter private Integer id;
        @Setter @Getter private String userName;
        @Setter @Getter private String nickName;
        @Setter @Getter private String createdAt;

        public Nick(int id) {
                this(null, null, id);
        }

        public Nick(String userName) {
                this(userName, null);
        }

        public Nick(String userName, String nickName) {
                this(userName, nickName, -1);
        }

        public Nick(String userName, String nickName, int id) {
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
         * This method is used to find and load specific sm using Nick#id or Nick#userName
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
                Nick nick = new Nick(p.getName());
                nick.load();
                return nick.getNickName() != null;
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
                String newName = RandomNames.get();

                if (newName == null) {
                        Common.send(player, "&cThere was a problem generating a random name for you, Please contact server administrators.");
                }

                Nick nick = new Nick(player.getName(), newName);

                if (exists(player)) {
                        nick.update();
                } else {
                        nick.save();
                }

                Common.send(player, "&aYou're now disguised as &2" + newName + " &aacross our network!");

                apply(player, newName);
        }

        public static void unDisguise(Player player) {
                apply(player, player.getName());

                Nick nick = new Nick(player.getName());
                nick.delete();

                Common.send(player, "&aYou're no longer disguised");
        }


}

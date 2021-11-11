package ir.jeykey.meganicks.database;

public class Queries {
        public static String INSERT_SM = "INSERT INTO mega_nicks (username, nickname) VALUES (?, ?)";
        public static String SELECT_SM = "SELECT * FROM mega_nicks WHERE id = ?";
        public static String SELECT_SM_BY_USER = "SELECT * FROM mega_nicks WHERE username = ?";
        public static String SELECT_ALL_SM = "SELECT * FROM mega_nicks ORDER BY created_at DESC";
        public static String UPDATE_NICK_BY_USER = "UPDATE mega_nicks SET nickname = ? WHERE username = ?";
        public static String DELETE_SM = "DELETE FROM mega_nicks WHERE id = ?";
        public static String DELETE_SM_BY_USER = "DELETE FROM mega_nicks WHERE username = ?";
        public static String CREATE_SM_TABLE = "CREATE TABLE IF NOT EXISTS mega_nicks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username VARCHAR(16) NOT NULL UNIQUE," +
                "nickname VARCHAR(16) NOT NULL UNIQUE," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";
}

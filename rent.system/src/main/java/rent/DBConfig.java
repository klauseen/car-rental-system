package rent;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConfig {
    public final static String URL;
    public final static String USER;
    public final static String PASSWORD;

    static {
        try {
            Properties props = new Properties();
            InputStream input = DBConfig.class
                    .getClassLoader()
                    .getResourceAsStream("db.local.properties");

            if (input == null) {
                throw new RuntimeException("db.local.properties NOT FOUND");
            }

            props.load(input);

            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");

        } catch (Exception e) {
            throw new RuntimeException("Failed to load database config", e);
        }
    }
    
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
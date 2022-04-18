import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Properties jdbcProperties;
    private Connection instance = null;

    public JdbcUtils(Properties properties) {
        jdbcProperties = properties;
    }

    private Connection getNewConnection() {
        String url = jdbcProperties.getProperty("jdbc.url");
        String user = jdbcProperties.getProperty("jdbc.user");
        String password = jdbcProperties.getProperty("jdbc.password");
        Connection connection = null;
        try{
            if(user != null && password != null) {
                connection = DriverManager.getConnection(url, user, password);
            }
            else {
                connection = DriverManager.getConnection(url);
            }
        } catch (SQLException exception) {
            System.out.println("Error getting connection "  +exception);
        }
        return connection;
    }

    public Connection getConnection() {
        try{
            if(instance == null || instance.isClosed()) {
                instance = getNewConnection();
            }
        } catch (SQLException exception) {
            System.out.println("Error database " + exception);
        }
        return instance;
    }
}

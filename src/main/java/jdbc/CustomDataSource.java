package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;
    private Connection connection;

    private CustomDataSource(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.name = name;
        this.password = password;
    }

    public static CustomDataSource getInstance() {
        if (instance == null) {
            Properties properties = loadProperties("app.properties");
            synchronized (CustomDataSource.class) {
                if (instance == null) {
                    instance = new CustomDataSource(properties.getProperty("postgres.driver"), properties.getProperty("postgres.url"), properties.getProperty("postgres.password"), properties.getProperty("postgres.name"));
                }
                try {
                    Class.forName(getInstance().driver);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url,name,password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    private static Properties loadProperties(String propertiesFilename){
        Properties properties = new Properties();
        ClassLoader loader = CustomDataSource.class.getClassLoader();
        try (InputStream stream = loader.getResourceAsStream(propertiesFilename)){
            if (stream == null){
                throw new FileNotFoundException();
            }
            properties.load(stream);
        }catch (IOException e){
            e.printStackTrace();
        }
        return properties;
    }
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}

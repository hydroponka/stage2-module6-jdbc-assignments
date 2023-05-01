package jdbc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CustomConnector {
    public Connection getConnection(String url) {
        Properties properties = loadProperties("app.properties");
        try {
            Class.forName(properties.getProperty("postgres.driver"));
            Connection connection = DriverManager.getConnection(url);
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection getConnection(String url, String user, String password)  {
        Properties properties = loadProperties("app.properties");
        try {
            Class.forName(properties.getProperty("postgres.driver"));
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
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
}


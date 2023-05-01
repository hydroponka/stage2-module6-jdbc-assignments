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
        try {
            Connection connection = DriverManager.getConnection(url);
            return connection;
        } catch (SQLException  e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection getConnection(String url, String user, String password)  {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}


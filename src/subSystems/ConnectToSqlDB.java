package subSystems;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConnectToSqlDB {

    public static Connection connect = null;
//    public static Statement statement = null;
    public static PreparedStatement ps = null;
//    public static ResultSet resultSet = null;

    public static void main(String[] args) throws Exception {

        ConnectToSqlDB connectToSqlDB = new ConnectToSqlDB();

        connectToSqlDB.insertCredentialsIntoDB("random_accounts", "test@gmail.com", "Eddy", "123456", "Email", "First_Name", "Password");

    }

    public static Properties loadProperties() throws IOException {
        Properties prop = new Properties();
        InputStream ism = new FileInputStream("src/secret.properties");
        prop.load(ism);
        ism.close();
        return prop;
    }

    public static Connection connectToSqlDatabase() throws IOException, SQLException, ClassNotFoundException {
        Properties prop = loadProperties();
        String driverClass = prop.getProperty("MYSQLJDBC.driver");
        String url = prop.getProperty("MYSQLJDBC.url");
        String userName = prop.getProperty("MYSQLJDBC.userName");
        String password = prop.getProperty("MYSQLJDBC.password");
        Class.forName(driverClass);

        try {
            connect = DriverManager.getConnection(url, userName, password);
            System.out.println("Connected to Database.");
        } catch (Exception e) {
            System.out.println("COULD NOT CONNECT TO DATABASE");
            e.printStackTrace();

        }
        return connect;
    }

    public void insertCredentialsIntoDB(String tableName, String email, String firstName, String password, String columnName1, String columnName2, String columnName3) {
        try {
            connectToSqlDatabase();
            ps = connect.prepareStatement("INSERT INTO " + tableName + " ( " + columnName1 + ", " + columnName2 + ", " + columnName3 + " ) VALUES ('" +
                    email + "', '" + firstName + "', '" + password + "')");
            ps.executeUpdate();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}



